import clojure.java.api.Clojure;
import clojure.lang.IFn;
import clojure.lang.PersistentVector;
import com.mxgraph.layout.*;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
   public static final IFn FN;
   public static final String GRAMMAR;
   
   static {
      // set up instaparse-wrapper
      IFn require = Clojure.var("clojure.core", "require");
      require.invoke(Clojure.read("instaparse-wrapper.core"));
      FN = Clojure.var("instaparse-wrapper.core", "parse-grammar");
      
      GRAMMAR = readGrammar("grammar.txt");
   }
   
   public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> new GUI());
   }
   
   public static void buildGraph(DefaultDirectedGraph<Node,DefaultEdge> g, Node root) {
      boolean added;
      String str;
      
      if (!g.containsVertex(root)) {
         g.addVertex(root);
      }
      
      if (root.getNext().size() > 1) {
         str = "true";
      } else {
         str = "";
      }
      
      for (Node n: root.getNext()) {
         added = false;
         
         if (!g.containsVertex(n)) {
            g.addVertex(n);
            added = true;
         }
         
         if (!g.containsEdge(root,n)) {
            g.addEdge(root,n,new Edge(str));
            added = true;
         }
         
         if (added) {
            buildGraph(g,n);
         }
   
         str = "false";
      }
   }
   
   public static DefaultDirectedGraph<Node, DefaultEdge> generateGraph(IFn fn, String grammar, String test) {
      System.out.printf("Source: %s\n", test);
      
      try {
         test = readTest(test);
      } catch (Exception e) {
         // test is not a file name
      }
      
      PersistentVector vec = (PersistentVector) fn.invoke(grammar, test);
      DefaultDirectedGraph<Node, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);
      Structure s = new Structure(vec);
      s.getStart().collapse(new ArrayList<>());
      buildGraph(g, s.getStart());
      
      return g;
   }
   
   public static JGraphXAdapter<Node,DefaultEdge> graph2Adapter(DefaultDirectedGraph<Node,DefaultEdge> g) {
      JGraphXAdapter<Node, DefaultEdge> graphAdapter = new JGraphXAdapter<>(g);
      graphAdapter.setCellsDisconnectable(false);
      graphAdapter.setCellsDisconnectable(false);
      graphAdapter.setConnectableEdges(false);
      return graphAdapter;
   }
   
   public static mxGraphComponent graph2Component(DefaultDirectedGraph<Node,DefaultEdge> g) {
      JGraphXAdapter<Node, DefaultEdge> graphAdapter = graph2Adapter(g);
   
      mxIGraphLayout layout;
      layout = new mxHierarchicalLayout(graphAdapter);
      ((mxHierarchicalLayout) layout).setInterRankCellSpacing(48);
   
      layout.execute(graphAdapter.getDefaultParent());
   
      mxGraphComponent gc = new mxGraphComponent(graphAdapter);
      gc.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
      gc.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
      gc.stopEditing(true);
   
      //JOptionPane.showMessageDialog(null,gc);
   
      return gc;
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
