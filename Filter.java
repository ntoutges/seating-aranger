import java.util.ArrayList;

class Filter {
  // returns a list of all generations that are compatible with eachother,
  // starting with gens[0] and moving up
  static ArrayList<Generation> findSubOptimalCombination(ArrayList<Generation> gens,
      ArrayList<Generation> initialGens) {
    ArrayList<Generation> confirmedGenerations = new ArrayList<Generation>();
    for (int i = 0; i < gens.size(); i++) {
      if (conflictExists(
        gens.get(i), initialGens)) {
        continue;
      }
      if (true && !conflictExists(gens.get(i), confirmedGenerations)) {
        confirmedGenerations.add(gens.get(i));
      }
    }
    return confirmedGenerations;
  }

  static ArrayList<Generation> findSubOptimalCombination(ArrayList<Generation> gens) {
    return findSubOptimalCombination(gens, new ArrayList<Generation>());
  }

  // returns a list of all generations that are compatible with eachother, moving
  // the starting point from gens[0] to gens[n]
  static ArrayList<Generation> findOptimalCombination(ArrayList<Generation> gens, ArrayList<Generation> initialGens) {
    ArrayList<Generation> maxGen = new ArrayList<Generation>();
    for (int i = 0; i < gens.size(); i++) {
      ArrayList<Generation> testGen = new ArrayList<Generation>();
      testGen.add(gens.get(i));
      if (conflictExists(gens.get(i), initialGens))
        continue; // 'gens[i]' not compatible with other generations
      for (int j = i + 1; j < gens.size(); j++) {
        if (!conflictExists(gens.get(j), testGen)) {
          testGen.add(gens.get(j));
        }
      }
      if (testGen.size() > maxGen.size())
        maxGen = testGen;
    }
    System.out.println(maxGen.size());
    return maxGen;
  }

  static ArrayList<Generation> findOptimalCombination(ArrayList<Generation> gens) {
    return findOptimalCombination(gens, new ArrayList<Generation>());
  }

  // checks of 'test' is compatible with all of 'cases'
  static boolean conflictExists(Generation test, ArrayList<Generation> cases) {
    for (int i = 0; i < cases.size(); i++) {
      if (test.conflictsWith(cases.get(i)))
        return true;
    }
    return false;
  }
}
