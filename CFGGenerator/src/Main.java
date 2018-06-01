import clojure.java.api.Clojure;
import clojure.lang.IFn;
import clojure.lang.PersistentVector;
import com.mxgraph.layout.*;
import com.mxgraph.swing.mxGraphComponent;
import org.jgrapht.Graph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultListenableGraph;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
   
   public static void main(String[] args) {
      // set up instaparse wrapper
      IFn require = Clojure.var("clojure.core", "require");
      require.invoke(Clojure.read("instaparse-wrapper.core"));
      
      // read grammar
      IFn fn = Clojure.var("instaparse-wrapper.core", "parse-grammar");
      String grammar = readGrammar("grammar.txt");
      
      testAndPrint(fn, grammar, "tests/test0.txt");
      testAndPrint(fn, grammar, "tests/test1.txt");
      testAndPrint(fn, grammar, "tests/test2.txt");
      testAndPrint(fn, grammar, "tests/test3.txt");
   }
   
   private static void buildGraph(DefaultDirectedGraph<Node,DefaultEdge> g, Node root) {
      boolean added;
      
      if (!g.containsVertex(root)) {
         g.addVertex(root);
      }
      
      for (Node n: root.getNext()) {
         added = false;
         
         if (!g.containsVertex(n)) {
            g.addVertex(n);
            added = true;
         }
         
         if (!g.containsEdge(root,n)) {
            g.addEdge(root,n);
            added = true;
         }
         
         if (added) {
            buildGraph(g,n);
         }
      }
   }
   
   private static void testAndPrint(IFn fn, String grammar, String test) {
      System.out.printf("Source: %s\n", test);
      
      try {
         test = readTest(test);
      } catch (Exception e) {
         // test is not a file name
      }
      
      PersistentVector vec = (PersistentVector) fn.invoke(grammar, test);
      DefaultDirectedGraph<Node, DefaultEdge> g = new DefaultDirectedGraph(DefaultEdge.class);
      buildGraph(g, new Structure(vec).getStart());
      
      JGraphXAdapter<Node, DefaultEdge> graphAdapter = new JGraphXAdapter(g);
      mxIGraphLayout layout = new mxOrganicLayout(graphAdapter);
      ((mxOrganicLayout) layout).setMinDistanceLimit(.001);
      ((mxOrganicLayout) layout).setMaxDistanceLimit(1);
      layout.execute(graphAdapter.getDefaultParent());
      
      mxGraphComponent gc = new mxGraphComponent(graphAdapter);
      gc.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
      gc.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
      gc.setPreferredSize(new Dimension(512,512));
      gc.stopEditing(true);
      gc.setZoomFactor(8.0);
      
      JOptionPane.showMessageDialog(null,gc);
   }
   
   private static String readGrammar(String fn) {
      File file = new File(fn);
      StringBuilder grammar = new StringBuilder();
      
      FileReader fr;
      try {
         fr = new FileReader(file);
         Scanner scan = new Scanner(fr);
         
         while (scan.hasNextLine()) {
            grammar.append(scan.nextLine()).append("\n");
         }
         
         scan.close();
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
      
      return grammar.toString();
   }
   
   private static String readTest(String fn) throws FileNotFoundException {
      StringBuilder out = new StringBuilder();
      
      FileReader fr = new FileReader(new File(fn));
      Scanner scan = new Scanner(fr);
      
      while (scan.hasNextLine()) {
         out.append(scan.nextLine()).append("\n");
      }
      
      scan.close();
      
      return String.format("{%s}", out.toString());
   }
}
