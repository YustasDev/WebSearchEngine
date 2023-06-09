import java.util.ArrayList;

public class TodoList {

  ArrayList<String> manageList = new ArrayList<>();

  public void printList() {
    System.out.println("Полный список дел: " + manageList);
  }

  public boolean add(int i, String affairs) {
    if (i < 0 || i >= manageList.size()) {
      return false;
    }
    manageList.add(i, affairs);
    return true;
  }

  public void add(String affairs) {
    manageList.add(affairs);
    System.out.println(manageList);
  }

  public boolean editList(int i, String affairs) {
    if (i < 0 || i >= manageList.size()) {
      return false;
    }
    manageList.set(i, affairs);
    return true;
  }

  public String delete(int i) {
    return manageList.remove(i);
  }
}

