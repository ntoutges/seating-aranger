import java.util.ArrayList;

class Format {
  static String combineHorizontalCSV(ArrayList<String> csvStrs) {
    String saveString = "";
    for (int i = 0; i < csvStrs.size(); i++) {
      if (i != 0)
        saveString += "\n";
      saveString += csvStrs.get(i);
    }
    return saveString;
  }
  
  static String combineVerticalCSV(ArrayList<String> csvStrs) {
    if (csvStrs.size() == 0)
      return "";
    int maxLen = 0;
    ArrayList<String[]> csvArrs = new ArrayList<String[]>();
    for (int i = 0; i < csvStrs.size(); i++) {
      String[] csvArr = csvStrs.get(i).split("\n", -1);
      int len = csvArr.length;
      csvArrs.add(csvArr);
      if (len > maxLen)
        maxLen = len;
    }
    String saveString = "";
    for (int i = 0; i < maxLen; i++) {
      for (int j = 0; j < csvArrs.size(); j++) {
        if (j != 0)
          saveString += ",";
        if (csvArrs.get(j).length > i)
          saveString += csvArrs.get(j)[i];
      }
      saveString += "\n";
    }
    return saveString;
  }

  static String[][] horizontalToHorizontalCSV(String[] horizontal) {
    ArrayList<String[]> horizontalArrs = new ArrayList<String[]>();
    for (int i = 0; i < horizontal.length; i++) { // get individual names from 'horizontal'
      ArrayList<String> arr = new ArrayList<String>();
      int startJ = 0;
      boolean inStr = false;
      for (int j = 0; j < horizontal[i].length(); j++) {
        char ch = horizontal[i].charAt(j);
        if (ch == '\"') {
          inStr = !inStr;
        }
        else if (!inStr && ch == ',') {
          arr.add(horizontal[i].substring(startJ, j));
          startJ = j+1;
        }
      }
      arr.add(horizontal[i].substring(startJ));
      String[] row = new String[arr.size()];
      for (int j = 0; j < arr.size(); j++) {
        row[j] = arr.get(j).replace("\"", "");
      }
      horizontalArrs.add(row);
    }

    String[][] output = new String[horizontalArrs.size()][];
    for (int i = 0; i < horizontalArrs.size(); i++) {
      output[i] = horizontalArrs.get(i);
    }
    return output;
  }
  
  static String[][] horizontalToVerticalCSV(String[] horizontal) {
    String[][] horizontalArr = horizontalToHorizontalCSV(horizontal);
    ArrayList<ArrayList<String>> verticalArr = new ArrayList<ArrayList<String>>();

    // effectively rotate 'horizontalArr' 90 degrees
    int maxLen = 0;
    for (int i = 0; i < horizontalArr.length; i++) {
      if (horizontalArr[i].length > maxLen)
        maxLen = horizontalArr[i].length;
    }
    for (int i = 0; i < maxLen; i++) {
      ArrayList<String> arr = new ArrayList<String>();
      for (int j = 0; j < horizontalArr.length; j++) {
        if (horizontalArr[j].length > i)
          arr.add(horizontalArr[j][i]);
        else
          arr.add("");
      }
      verticalArr.add(arr);
    }

    String[][] output = new String[verticalArr.size()][];
    for (int i = 0; i < verticalArr.size(); i++) {
      output[i] = new String[verticalArr.get(i).size()];
      for (int j = 0; j < verticalArr.get(i).size(); j++) {
        output[i][j] = verticalArr.get(i).get(j);
      }
    }
    return output;
  }
}
