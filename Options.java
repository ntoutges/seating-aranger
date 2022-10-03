// key: value // comment

import java.util.HashMap;

class Options {
  HashMap<String, String> props = new HashMap<String, String>();
  String filename;

  Options(String filename, String[] properties) {
    for (String prop : properties) {
      this.props.put(prop, "unset"); // set default value for all properties
    }
    this.filename = filename;
    setProperties();
  }

  Options(String filename) {
    this.filename = filename;
    setProperties();
  }

  void setProperties() {
    try {
      String[] lines = IO.read(this.filename);
      this.props = parseProperties(lines);
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }

  static HashMap<String, String> parseProperties(String[] lines) {
    HashMap<String, String> properties = new HashMap<String, String>();
    for (String line : lines) {
      line = line.replace(" ", ""); // remove all spaces
      int nameEnd = line.indexOf(":");
      int valEnd = line.indexOf("//"); // allow for comments

      if (valEnd == 0) { // line is just a comment
        continue;
      }
      if (nameEnd == -1) { // invalid property
        System.out.println("Invalid line: " + line);
        continue;
      }
      if (valEnd == -1) { // no comments
        valEnd = line.length();
      }
      String propName = line.substring(0, nameEnd);
      String propVal = line.substring(nameEnd + 1, valEnd);
      properties.put(propName, propVal);
    }
    return properties;
  }

  String getProp(String propName) {
    if (this.props.containsKey(propName))
      return this.props.get(propName);
    return "null";
  }
}
