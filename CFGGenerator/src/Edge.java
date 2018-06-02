import org.jgrapht.graph.DefaultEdge;

public class Edge extends DefaultEdge {
   String str;
   
   public Edge(String s) {
      str = s;
   }
   
   @Override
   public String toString() {
      return str;
   }
}
