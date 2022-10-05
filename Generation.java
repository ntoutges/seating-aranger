import java.util.HashMap;
import java.util.Random;
import java.lang.Math;

class Generation { // multiple pods
  private Pod[] pods;
  private String[] students;
  private int unassignedStudents;

  Generation(String[] students) {
    this.students = students;
    this.unassignedStudents = students.length;

    int podCount = (int) Math.ceil(students.length / 4.0);
    this.pods = new Pod[podCount];
    for (int i = 0; i < podCount; i++) {
      this.pods[i] = new Pod();
    }
  }

  void assignPairs() {
    // initially pair up all students
    for (Pod pod : this.pods) {
      while (!pod.hasPairs() && this.unassignedStudents != 0) {
        if (pod.add(this.nextStudent())) { // take last student in list
          this.unassignedStudents -= 1; // student has been put into the pod successfully
        } else {
          break; // pod is unable to take student, for some reason
        }
      }
      if (this.unassignedStudents == 0) {
        break;
      }
    }
  }

  // assign all students into a pod : try to fill all pods with equal amount of
  // students
  boolean assignSpread() {
    this.assignPairs(); // get at least two students in all pods
    while (this.unassignedStudents != 0) {
      boolean action = false; // prevet possible infinite loops
      for (Pod pod : this.pods) {
        if (this.unassignedStudents == 0) {
          break;
        }
        if (pod.hasRoom() && pod.add(this.nextStudent())) { // short-circuitry magic!
          this.unassignedStudents -= 1;
          action = true;
        }
      }
      if (!action) {
        return false;
      }
    }
    return true;
  }

  // assign all students into a pod : try to completely fill some pods, then leave
  // pairs in the rest
  boolean assignFill() {
    this.assignPairs(); // get at least two students in all pods
    return this.assignConsecutive();
  }

  boolean assignConsecutive() {
    while (this.unassignedStudents != 0) {
      boolean action = false; // prevet possible infinite loops
      for (Pod pod : this.pods) {
        while (pod.hasRoom()) {
          if (this.unassignedStudents == 0) {
            break;
          }
          if (pod.hasRoom() && pod.add(this.nextStudent())) { // short-circuitry magic!
            this.unassignedStudents -= 1;
            action = true;
          }
        }
        if (this.unassignedStudents == 0) {
          break;
        }
      }
      if (!action) {
        return false;
      }
    }
    return true;
  }

  Generation nextGen(int xStep, int yStep) {
    int x = 0;
    int y = 0;
    int resets = 0;
    String[] orderedStudents = new String[this.pods.length * 4];
    int studentCount = orderedStudents.length - 1; // count backwards

    HashMap<String, Boolean> students = new HashMap<String, Boolean>();
    HashMap<String, Boolean> nonCoords = new HashMap<String, Boolean>();
    for (int i = 0; i < this.pods.length; i++) {
      int startY = y;
      int startX = x;
      while (true) { // search for new start point
        if (!students.containsKey(getStudentAt(x, y, true))
            && !nonCoords.containsKey(String.valueOf(x) + "," + String.valueOf(y))) { // find open spot to start
                                                                                      // algorithm from
          break;
        }
        x++;
        if (x >= 4) {
          x = 0;
          y++;
          if (y >= this.pods.length) {
            y = 0;
          }
        }
        if (x == startX && y == startY) { // all spots taken
          Generation gen = new Generation(new String[] { "-", "-", "-", "-" });
          gen.assignConsecutive();
          return gen;
        }
      }
      startY = y;
      startX = x;
      boolean tempStudentsValid = true;
      String[] tempStudents = new String[4];
      for (int j = 0; j < 4; j++) {
        String student = this.getStudentAt(x, y, true);
        if ((j != 0 && y == startY) || students.containsKey(student)) {
          tempStudentsValid = false;
          break;
        }

        students.put(student, true);
        tempStudents[j] = this.getStudentAt(x, y, false);
        x = (x + xStep) % 4;
        y = (y + yStep) % this.pods.length; // y roll over
      }
      if (tempStudentsValid) {
        for (int j = 0; j < 4; j++) {
          orderedStudents[studentCount - j] = tempStudents[j];
        }
        studentCount -= 4;
      } else { // undo invalid path
        for (int j = 0; j < 4; j++) {
          if (tempStudents[j] == null) {
            break;
          }
          students.remove(tempStudents[j]);
        }
        nonCoords.put(String.valueOf(startX) + "," + String.valueOf(startY), true);
        // reset coordinate values to restart search for valid spot
        x = 0;
        y = 0;
        resets++; // prevent infinite loops
        i--;
        if (resets >= this.pods.length * 4) { // max amount of [resets] ever needed
          Generation gen = new Generation(new String[] { "-", "-", "-", "-" });
          gen.assignConsecutive();
          return gen;
        }
      }
    }
    Generation gen = new Generation(orderedStudents);
    gen.assignConsecutive();
    return gen;
  }

  private String nextStudent() {
    return this.students[this.unassignedStudents - 1];
  }

  private String getStudentAt(int x, int y, boolean isComparing) {
    String student = this.pods[y].getStudents()[x];
    return (student.equals("") && isComparing) ? "_" + String.valueOf(x) + ":" + String.valueOf(y) : student;
  }

  // will return 'true' if this generation has pods with students that have
  // already sat with each other
  boolean conflictsWith(Generation otherGen) {
    HashMap<String, HashMap<String, Boolean>> otherAssociations = otherGen.getAssociations();
    for (Pod pod : this.pods) {
      String[] podStudents = pod.getStudents();
      for (int i = 0; i < podStudents.length; i++) {
        if (!otherAssociations.containsKey(podStudents[i])) {
          continue;
        }
        for (int j = i + 1; j < podStudents.length; j++) {
          if (otherAssociations.get(podStudents[i]).containsKey(podStudents[j])) {
            return true;
          }
        }
      }
    }
    return false;
  }

  HashMap<String, HashMap<String, Boolean>> getAssociations() {
    HashMap<String, HashMap<String, Boolean>> associations = new HashMap<String, HashMap<String, Boolean>>();
    for (Pod pod : this.pods) {
      for (String student : pod.getStudents()) {
        if (student.length() == 0) {
          continue;
        }
        HashMap<String, Boolean> studentAssociations = new HashMap<String, Boolean>();
        for (String classMate : pod.getStudents()) {
          if (classMate.length() == 0) {
            continue;
          }
          if (student != classMate) {
            studentAssociations.put(classMate, true);
          }
        }
        associations.put(student, studentAssociations);
      }
    }
    return associations;
  }

  int size() {
    return this.pods.length;
  }

  boolean exists() {
    if (this.students.length != 4)
      return true;
    for (int i = 0; i < 4; i++) {
      if (!this.students[i].equals("-")) {
        return true;
      }
    }
    return false;
  }

  String toCSV(boolean nameNulls) {
    String podCSVs = "";
    for (int i = 0; i < this.pods.length; i++) {
      if (i != 0)
        podCSVs += ",";
      podCSVs += this.pods[i].toCSV(nameNulls);
    }
    return podCSVs;
  }

  String toCSV() {
    return toCSV(true);
  }

  String toRandCSV(boolean nameNulls) {
    String podCSVs = "";
    HashMap<Integer,Boolean> blacklist = new HashMap<Integer,Boolean>();
    Random random = new Random();
    for (int i = 0; i < this.pods.length; i++) {
      int index = -1;
      while (blacklist.containsKey(index) || index == -1) {
        index = random.nextInt(this.pods.length);
      }
      blacklist.put(index, true);
      if (i != 0)
        podCSVs += ",";
      podCSVs += this.pods[index].toCSV(nameNulls);
    }
    return podCSVs;
  }

  String toRandCSV() {
    return toRandCSV(true);
  }

  String toVerticalCSV(boolean nameNulls) {
    String podCSVs = "";
    for (int i = 0; i < this.pods.length; i++) {
      if (i != 0)
        podCSVs += "\n";
      podCSVs += this.pods[i].toVerticalCSV(nameNulls);
    }
    return podCSVs;
  }
  
  String toverticalCSV() {
    return toVerticalCSV(true);
  }

  String toRandVerticalCSV(boolean nameNulls) {
    String podCSVs = "";
    HashMap<Integer,Boolean> blacklist = new HashMap<Integer,Boolean>();
    Random random = new Random();
    for (int i = 0; i < this.pods.length; i++) {
      int index = -1;
      while (blacklist.containsKey(index) || index == -1) {
        index = random.nextInt(this.pods.length);
      }
      blacklist.put(index, true);
      if (i != 0)
        podCSVs += "\n";
      podCSVs += this.pods[index].toVerticalCSV(nameNulls);
    }
    return podCSVs;
  }

  String toRandVerticalCSV() {
    return toRandCSV(true);
  }

  String toGrid() {
    String podGrid = "";
    for (int i = 0; i < this.pods.length; i++) {
      if (i != 0)
        podGrid += "\n";
      podGrid += this.pods[i].toCSV();
    }
    return podGrid;
  }
}
