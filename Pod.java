class Pod {
  private String[] students = { "", "", "", "" };
  private byte studentCounter = 0;

  boolean add(String student) { // boolean indicates if operation occured
    if (studentCounter == 4)
      // throw new Exception("Cannot have more than 4 students in a pod");
      return false;
    students[studentCounter] = student;
    this.studentCounter += 1;
    return true;
  }

  int length() {
    return studentCounter;
  }

  boolean hasRoom() {
    return studentCounter != 4;
  }

  boolean hasPairs() {
    return studentCounter >= 2;
  }

  boolean contains(String student) {
    for (String name : this.students) {
      if (student.equals(name))
        return false;
    }
    return true;
  }

  String[] getStudents() {
    return this.students;
  }

  String toCSV(boolean nameNulls) {
    String names = "";
    for (int i = 0; i < students.length; i++) {
      if (i != 0)
        names += ",";
      if (students[i].equals("")) // no student stored
        names += (nameNulls) ? "null" : "";
      else
        names += "\"" + students[i] + "\"";
    }
    return names;
  }

  String toCSV() {
    return this.toCSV(true);
  }

  String toVerticalCSV(boolean nameNulls) {
    String names = "";
    for (int i = 0; i < students.length; i++) {
      if (i != 0)
        names += "\n";
      if (students[i].equals("")) // no student stored
        names += (nameNulls) ? "null" : "";
      else
        names += "\"" + students[i] + "\"";
    }
    return names;
  }
  String toVerticalCSV() {
    return this.toCSV(true);
  }
}
