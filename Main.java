
// v3 of this project
import java.util.ArrayList;

class Main {
  public static void main(String[] args) {
    try {
      Options opt = new Options("options.jdon",
          new String[] { "fill", "debug", "filter", "include_lookback", "random_pods", "vertical" });
      boolean debug = opt.getProp("debug").equals("1");
      String[][] input = new String[][]{};
      boolean verticalOut = opt.getProp("vertical").equals("1");
      if (verticalOut)
        input = Format.horizontalToVerticalCSV(IO.read("input.csv"));
      else
        input = Format.horizontalToHorizontalCSV(IO.read("input.csv"));
      
      ArrayList<Generation> inputGens = new ArrayList<Generation>();
      for (int i = 0; i < input.length; i++) {
        String[] genStr = input[i];
        Generation gen = new Generation(genStr);
        gen.assignConsecutive();
        inputGens.add(gen);
      }

      if (inputGens.size() == 0)
        inputGens.add(new Generation("-,-,-,-".split(",")));
      Generation genA = inputGens.get(inputGens.size() - 1); // set genA to the last input generation

      String fillMethod = opt.getProp("fill").toLowerCase();
      if (fillMethod.equals("fill"))
        genA.assignFill();
      else if (fillMethod.equals("spread"))
        genA.assignConsecutive();
      else // fillMethod.equals("spread") || default
        genA.assignSpread();

      if (debug) {
        System.out.println("--- Parent Generation ---");
        System.out.println(genA.toGrid());
      }

      ArrayList<Generation> allGenerations = findGenerations(genA);
      if (debug) {
        int size = allGenerations.size();
        String plural = (size == 1) ? "" : "s";
        System.out.println("\nFound " + String.valueOf(allGenerations.size()) + " generation option" + plural
            + "\n(Many are likely not intercompatible)\n");
        for (int i = 0; i < size; i++) {
          System.out.println(String.valueOf(i + 1) + ". " + allGenerations.get(i).toCSV());
        }
        System.out.println("");
      }

      ArrayList<Generation> gens;
      if (opt.getProp("filter").equals("all")) {
        if (debug)
          System.out.println("Searching for optimal generation order (SLOW)");
        gens = Filter.findSubOptimalCombination(allGenerations, inputGens);
      } else {
        if (debug)
          System.out.println("Searching for optimal generation order (FAST)");
        gens = Filter.findSubOptimalCombination(allGenerations, inputGens);
      }

      String plural = gens.size() == 1 ? "" : "s";
      if (gens.size() != 0)
        System.out.println("Done! Found " + String.valueOf(gens.size()) + " compatible generation" + plural + ".");
      else
        System.out
            .println("Unable to find any compatible generations. Try removing some restricting lines from the input.");

      if (gens.size() < 3)
        System.out.println("Try adding some empty seats (nameless \"students\") to input.csv to increase the amount of results");

      ArrayList<String> saveStrs = new ArrayList<String>();
      if (!opt.getProp("include_lookback").equals("0") && !opt.getProp("include_lookback").equals("null")) {
        int lookback = Integer.parseInt(opt.getProp("include_lookback"));

        for (int i = 0; i < lookback; i++) {
          int j = inputGens.size() - i - 1;
          if (j < 0)
            break;
          if (verticalOut)
            saveStrs.add(inputGens.get(j).toVerticalCSV(false));
          else
            saveStrs.add(inputGens.get(j).toCSV(false));
        }
      }

      boolean randomPods = opt.getProp("random_pods").equals("1");
      for (int i = 0; i < gens.size(); i++) {
        if (debug) {
          System.out.println("\n-- (" + String.valueOf(i + 1) + ") -- ");
          System.out.println(gens.get(i).toGrid());
        }
        if (randomPods)
          if (verticalOut)
            saveStrs.add(gens.get(i).toRandVerticalCSV(false));
          else
            saveStrs.add(gens.get(i).toRandCSV(false));
        else if (verticalOut)
          saveStrs.add(gens.get(i).toVerticalCSV(false));
        else
          saveStrs.add(gens.get(i).toCSV(false));
      }

      String saveStr = "";
      if (verticalOut)
        saveStr = Format.combineVerticalCSV(saveStrs);
      else
        saveStr = Format.combineHorizontalCSV(saveStrs);
      IO.save(saveStr);
      if (opt.getProp("auto_feedback").equals("1"))
        IO.save(saveStr, "input.csv");
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }

  // finds all possible generations
  // these WILL be incompatable with one another
  static ArrayList<Generation> findGenerations(Generation generator) {
    ArrayList<Generation> res = new ArrayList<Generation>();

    for (int yStep = 1; yStep < generator.size(); yStep++) {
      for (int xStep = 1; xStep < 4; xStep++) {
        Generation test = generator.nextGen(xStep, yStep);
        if (test.exists()) {
          res.add(test);
        }
      }
    }
    return res;
  }
}
