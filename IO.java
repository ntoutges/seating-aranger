import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

class IO {
  static void save(String data, String filename) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter("./IO/" + filename));
    writer.write(data);
    writer.close();
  }

  static void save(String data) throws IOException {
    save(data, "output.csv");
  }

  static String[] read(String filename) throws FileNotFoundException {
    File file = new File("./IO/" + filename);
    Scanner scanner = new Scanner(file);

    ArrayList<String> linesList = new ArrayList<String>();
    while (scanner.hasNextLine()) {
      linesList.add(scanner.nextLine());
    }
    // translate ArrayList into classic array
    String[] lines = new String[linesList.size()];
    for (int i = 0; i < linesList.size(); i++) {
      lines[i] = linesList.get(i);
    }
    scanner.close();
    return lines;
  }
}
