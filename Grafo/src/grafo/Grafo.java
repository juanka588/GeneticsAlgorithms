/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.AbstractGraph;
import edu.uci.ics.jung.graph.DirectedOrderedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.GraphDecorator;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.UndirectedOrderedSparseMultigraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Stroke;
import javafx.stage.FileChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author JuanCamilo
 */
public class Grafo {

    static int mat[];
    static int sol[];

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        readMat();
        System.out.println("");
        readSol();
        /*
         System.out.println("");
         showRowMat(mat);
         */
        llamar();
    }

    public static <Paint> void dibujar(Graph g, String cad) {
        // Layout<V, E>, BasicVisualizationServer<V,E>
        int ancho = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        int alto = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        Layout<Integer, String> layout = new ISOMLayout<>(g);
        layout.setSize(new Dimension(ancho / 2 - 10, alto - 80));
        BasicVisualizationServer<Integer, String> vv = new BasicVisualizationServer<Integer, String>(
                layout);
        vv.setPreferredSize(new Dimension(ancho / 2 - 10, alto - 80));
        /*vv.getRenderContext()
         .setVertexFillPaintTransformer(
         new org.apache.commons.collections15.Transformer<Integer, Paint>() {

         @Override
         public Paint transform(Integer arg0) {
         // TODO Auto-generated method stub
         return (Paint) Color.white;
         }
         });
         */
        float dash[] = {10.0f};
        final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        vv.getRenderContext()
                .setEdgeStrokeTransformer(
                        new org.apache.commons.collections15.Transformer<String, Stroke>() {
                            @Override
                            public Stroke transform(String arg0) {
                                return edgeStroke;
                            }
                        });
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        // vv.getRenderContext().setEdgeLabelTransformer(new
        // ToStringLabeller());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
        JFrame frame = new JFrame(cad);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);

    }

    private static void llamar() {
        Graph g = transformMat(mat);
        dibujar(g, "Grafo");
        g = transformMat(mat, sol);
        dibujar(g, "Solucion");
    }

    private static Graph transformMat(int[] mat) {
        Graph g = new SparseGraph();
        int cntEdges = 0;
        int size = (int) Math.sqrt(mat.length);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (mat[i * size + j] == 1) {
                    g.addEdge("" + cntEdges, i, j);
                    cntEdges++;
                }
            }

        }
        return g;
    }

    private static Graph transformMat(int[] mat, int sol[]) {
        Graph g = new SparseGraph();
        int cntEdges = 0;
        int size = (int) Math.sqrt(mat.length);
        for (int i = 0; i < size; i++) {
            if (sol[i] == 1) {
                for (int j = 0; j < size; j++) {
                    if (mat[i * size + j] == 1) {
                        g.addEdge("" + cntEdges, i, i);
                        cntEdges++;
                    }
                }
            }

        }
        return g;
    }

    private static void showRowMat(int[] mat) {
        int size = (int) Math.sqrt(mat.length);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(mat[i * size + j] + " ");
            }
            System.out.println("");
        }
    }

    private static void readMat() throws FileNotFoundException, IOException {
        BufferedReader bf = new BufferedReader(new FileReader(new File("../VertexCoverHaea/grafo.txt")));
        String cad = bf.readLine();
        String split[] = cad.split(" ");
        mat = new int[split.length * split.length];
        int j = 0;
        while (cad != null) {
            System.out.println(cad);
            split = cad.split(" ");
            for (int i = 0; i < split.length; i++) {
                int val = Integer.parseInt(split[i]);
                mat[i * split.length + j] = val;
            }
            cad = bf.readLine();
            j++;
        }
    }

    private static void readSol() throws FileNotFoundException, IOException {

        BufferedReader bf = new BufferedReader(new FileReader(new File("../VertexCoverHaea/solucion.txt")));
        String cad = bf.readLine();
        String split[] = cad.split(" ");
        sol = new int[split.length];
        int j = 0;
        while (cad != null) {
            System.out.println(cad);
            split = cad.split(" ");
            for (int i = 0; i < split.length; i++) {
                int val = Integer.parseInt(split[i]);
                sol[i] = val;
            }
            cad = bf.readLine();
            j++;
        }
    }

}
