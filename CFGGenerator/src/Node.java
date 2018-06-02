import clojure.lang.PersistentVector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Node implements Serializable {
   private ArrayList<Node> prev;
   private ArrayList<Node> next;
   private String code;
   private final int id;
   
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
   
   public void collapse(List<Node> visited) {
      visited.add(this);
      
      if (next.size() == 1 && next.get(0).prev.size() == 1) {
         code = String.format("%s\n%s",code,next.get(0).code);
         next = next.get(0).next;
         collapse(visited);
      } else {
         for (Node n : next) {
            if (!visited.contains(n)) {
               n.collapse(visited);
            }
         }
      }
   }
   
   public ArrayList<Node> getPrev() {
      return prev;
   }
   
   public ArrayList<Node> getNext() {
      return next;
   }
   
   @Override
   public boolean equals(Object obj) {
      return (obj.getClass() == getClass() && ((Node)obj).id == id);
   }
   
   public String toString() {
      return code;
   }
}
