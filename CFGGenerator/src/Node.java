import clojure.lang.PersistentVector;

import java.util.ArrayList;

public class Node {
   private ArrayList<Node> prev;
   private ArrayList<Node> next;
   private String code;
   private int id;
   
   private static int count = 0;
   
   public Node(PersistentVector vec) {
      code = "";
      next = new ArrayList<>();
      prev = new ArrayList<>();
      id = count++;
      
      // All string type objects in vec should be parts of the line of code
      // this node represents.
      for (Object o : vec) {
         if (o.getClass() == String.class) {
            code = String.format("%s%s ", code, o);
         }
      }
   }
   
   public ArrayList<Node> getPrev() {
      return prev;
   }
   
   public ArrayList<Node> getNext() {
      return next;
   }
   
   public String getCode() {
      return code;
   }
   
   @Override
   public boolean equals(Object obj) {
      return (obj.getClass() == getClass() && ((Node)obj).id == id);
   }
   
   public String toString() {
      return code;
   }
}
