import clojure.lang.Keyword;
import clojure.lang.PersistentVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class Structure {
   private Node start;
   private ArrayList<Node> end;
   
   private static final HashMap<String, BiConsumer<Structure, PersistentVector>> CONSUMERS;
   
   static {
      CONSUMERS = new HashMap<>();
      CONSUMERS.put("Pn", Structure::structPn);
      CONSUMERS.put("P1", Structure::structP1);
      CONSUMERS.put("D0", Structure::structD0);
      CONSUMERS.put("D1", Structure::structD1);
      CONSUMERS.put("D2", Structure::structD2);
      CONSUMERS.put("D3", Structure::structD3);
   }
   
   public Structure(PersistentVector struct) {
      end = new ArrayList<>();
      
      // determine type
      assert (struct.size() > 0)
              && (struct.get(0).getClass() == Keyword.class)
              && CONSUMERS.containsKey(struct.get(0));
      
      CONSUMERS.get(((Keyword)(struct.get(0))).getName()).accept(this, struct);
   }
   
   public Node getStart() {
      return start;
   }
   
   private void structPn(PersistentVector struct) {
      ArrayList<Structure> structures = new ArrayList<>();
      for (Object o : struct) {
         if (o.getClass() == PersistentVector.class) {
            structures.add(new Structure((PersistentVector) o));
         }
      }
      
      Structure a,b;
      
      a = structures.get(0);
      for (int i = 1; i < structures.size(); i++) {
         b = structures.get(i);
         
         // all end nodes of a structure should be linked to start node
         // of next consecutive structure
         for (Node n: a.end) {
            n.getNext().add(b.start);
            //System.out.printf("linked %s and %s\n",n.getCode(), b.start.getCode());
         }
         
         a = b;
      }
      
      start = structures.get(0).start;
      end.addAll(structures.get(structures.size() - 1).end);
   }
   
   private void structP1(PersistentVector struct) {
      start = new Node(struct);
      end.add(start);
   }
   
   private void structD0(PersistentVector struct) {
      // should be of form {Keyword tag, PersistentVector a, PersistentVector x}
      assert struct.get(1).getClass() == PersistentVector.class
              && struct.get(2).getClass() == PersistentVector.class;
      
      Structure a = new Structure((PersistentVector)struct.get(1));
      Structure x = new Structure((PersistentVector)struct.get(2));
      
      // connect end of a to start of x
      for (Node n: a.end) {
         n.getNext().add(x.start);
      }
      
      // set start to beginning of a
      start = a.start;
      
      // add end of x to end
      end.addAll(x.end);
      
      // add end of a to end
      end.addAll(a.end);
   }
   
   private void structD1(PersistentVector struct) {
      // should be of form {Keyword tag, PersistentVector a, PersistentVector x, PersistentVector y}
      assert struct.get(1).getClass() == PersistentVector.class
              && struct.get(2).getClass() == PersistentVector.class
              && struct.get(3).getClass() == PersistentVector.class;
      
      Structure a = new Structure((PersistentVector)struct.get(1));
      Structure x = new Structure((PersistentVector)struct.get(2));
      Structure y = new Structure((PersistentVector)struct.get(3));
      
      // connect end of a to beginnings of x and y
      for (Node n: a.end) {
         n.getNext().add(x.start);
         n.getNext().add(y.start);
      }
      
      // set start to start of a
      start = a.start;
      
      // add end of x to end
      end.addAll(x.end);
      
      // add end of y to end
      end.addAll(y.end);
   }
   
   private void structD2(PersistentVector struct) {
      // should be of format {Keyword tag, PersistentVector a, PersistentVector x}
      assert struct.get(1).getClass() == PersistentVector.class
              && struct.get(2).getClass() == PersistentVector.class;
      
      Structure a = new Structure((PersistentVector)struct.get(1));
      Structure x = new Structure((PersistentVector)struct.get(2));
      
      // connect end of a and start of x
      for (Node n: a.end) {
         n.getNext().add(x.start);
      }
      
      // connect end of x to start of a
      for (Node n: x.end) {
         n.getNext().add(a.start);
      }
      
      // set start to start of a
      start = a.start;
      
      // add end of a to end
      end.addAll(a.end);
   }
   
   private void structD3(PersistentVector struct) {
      // should be of format {Keyword tag, PersistentVector x, PersistentVector a}
      assert struct.get(1).getClass() == PersistentVector.class
              && struct.get(2).getClass() == PersistentVector.class;
      
      Structure a = new Structure((PersistentVector)struct.get(2));
      Structure x = new Structure((PersistentVector)struct.get(1));
      
      // connect end of x to start of a
      for (Node n: x.end) {
         n.getNext().add(a.start);
      }
      
      // connect end of a to start of x
      for (Node n: a.end) {
         n.getNext().add(x.start);
      }
      
      // set beginning to start of x
      start = x.start;
      
      // add end of a to end
      end.addAll(a.end);
   }
}
