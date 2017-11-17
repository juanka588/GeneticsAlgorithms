/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author JuanCamilo
 */
public class Visor extends JFrame {

    JPanel panel;
    static ArrayList<String[]> lista = new ArrayList<>();
    static double mat[][];
    static int dataSets = 3;

    public Visor() {
        setTitle("Grafica AG");
        int ancho = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        int alto = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        setSize(ancho - 100, alto - 100);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        initLista(20);
        init();
    }

    private void init() {
        panel = new JPanel();
        getContentPane().add(panel);
        // Fuente de Datos
        DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
        for (int i = 0; i < lista.size(); i++) {
            String values[] = lista.get(i);
            double val = Double.parseDouble(values[0]);
            line_chart_dataset.addValue(val, values[1], values[2]);
        }
        // Creando el Grafico
        JFreeChart chart = ChartFactory.createLineChart("Fitnnes evaluation de: ",
                "X", "Y", line_chart_dataset, PlotOrientation.VERTICAL,
                true, true, false);

        // Mostrar Grafico
        ChartPanel chartPanel = new ChartPanel(chart);
        panel.add(createChartPanel());
    }

    private JPanel createChartPanel() {
        String chartTitle = "Fitness evaluation de: ";
        String xAxisLabel = "iteracion";
        String yAxisLabel = "fitness";

        XYDataset dataset = createDataset();

        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle,
                xAxisLabel, yAxisLabel, dataset);
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        plot.setRenderer(renderer);

// sets paint color for each series
        renderer.setSeriesPaint(1, Color.RED);
        renderer.setSeriesPaint(1, Color.GREEN);
        renderer.setSeriesPaint(2, Color.YELLOW);

// sets thickness for series (using strokes)
        renderer.setSeriesStroke(0, new BasicStroke(4.0f));
        renderer.setSeriesStroke(1, new BasicStroke(3.0f));
        renderer.setSeriesStroke(2, new BasicStroke(2.0f));

        plot.setRenderer(renderer);
        return new ChartPanel(chart);
    }

    public static void main(String args[]) {
        new Visor().setVisible(true);
    }

    private void initLista(int nData) {
        for (int i = 0; i < nData; i++) {
            String data[] = new String[]{Math.pow(i, 0.2) + "", "y=x^1/5", i + ""};
            lista.add(data);
        }
    }

    private XYDataset createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (int i = 0; i < dataSets; i++) {
            XYSeries series1 = new XYSeries("Mejor " + i);
            for (int j = 0; j < mat.length; j++) {
                series1.add(j, mat[j][i + 3]);
            }
            dataset.addSeries(series1);
        }
        return dataset;
    }
}
