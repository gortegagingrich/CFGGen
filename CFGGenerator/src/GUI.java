import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
   DefaultDirectedGraph<Node, DefaultEdge> graph;
   
   mxGraphComponent graphComponent;
   JTextArea source;
   JTextArea output;
   final GridBagLayout gbl;
   final GridBagConstraints gbc;
   
   public GUI() {
      JScrollPane srcPane, outPane;
      source = new JTextArea();
      source.setColumns(80);
      source.setLineWrap(false);
      source.setRows(40);
      srcPane = new JScrollPane(source);
      output = new JTextArea();
      outPane = new JScrollPane(output);
      
      gbc = new GridBagConstraints();
      gbl = new GridBagLayout();
      setLayout(gbl);
      
      initGraph(gbc,gbl);
      initMenu();
      
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.weightx = 1.0;
      gbc.weighty = 1.0;
      gbc.gridwidth = 1;
      gbc.gridheight = 2;
      gbc.fill = GridBagConstraints.BOTH;
      gbl.setConstraints(srcPane,gbc);
      add(srcPane,gbc);
      
      gbc.gridx = 1;
      gbc.gridy = 1;
      gbc.gridheight = 1;
      gbl.setConstraints(outPane,gbc);
      add(outPane,gbc);
      
      gbc.gridy = 0;
      gbl.setConstraints(graphComponent,gbc);
      add(graphComponent,gbc);
   
      pack();
      Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
      this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
      setSize(new Dimension(1024,512));
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      setVisible(true);
   }
   
   private void initMenu() {
      JMenuBar mb = new JMenuBar();
      JMenuItem update = new JMenuItem("update");
      update.addActionListener(l -> {
         initGraph(gbc,gbl);
      });
      mb.add(update);
      setJMenuBar(mb);
   }
   
   private void initGraph(GridBagConstraints gbc, GridBagLayout gbl) {
      try {
         graph = Main.generateGraph(Main.FN,Main.GRAMMAR,String.format("{%s}",source.getText()));
      } catch (Exception e) {
         graph = new DefaultDirectedGraph<>(DefaultEdge.class);
      }
      JGraphXAdapter<Node, DefaultEdge> graphAdapter = new JGraphXAdapter<>(graph);
      graphAdapter.setCellsDisconnectable(false);
      graphAdapter.setCellsDisconnectable(false);
      graphAdapter.setConnectableEdges(false);
      mxIGraphLayout layout;
      layout = new mxHierarchicalLayout(graphAdapter);
      ((mxHierarchicalLayout) layout).setInterRankCellSpacing(48);
      layout.execute(graphAdapter.getDefaultParent());
      
      mxGraphComponent gc;
   
      gc = Main.graph2Component(graph);
   
      if (graphComponent == null) {
         graphComponent = gc;
      } else {
         graphComponent.setGraph(graphAdapter);
      }
      
      output.setText(graph.toString());
   }
   
   private void tryParse() {
   
   }
}
