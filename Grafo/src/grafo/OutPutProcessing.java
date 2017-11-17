/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author JuanCamilo
 */
public class OutPutProcessing {

    static double data[][][];
    static int BITARRAYLENGTH = 100;
    static int POPSIZE = 100;
    static int MAXITER = 1000;
    static int nExper = 50;

    public static void main(String[] args) throws IOException {
        readParams();
        ArrayList<double[][]> soluciones = new ArrayList<>();
        for (int i = 0; i < nExper; i++) {
            double[][] sol = readSol(i);
            if (sol != null) {
                soluciones.add(sol);
                //mostrarMat(sol);
            }
        }
        double[][] sol = soluciones.get(0);
        Visor.mat = sol;
        Visor.dataSets = 1;
        Visor.main(args);
        data = new double[soluciones.size()][sol.length][sol[0].length];
        for (int i = 0; i < soluciones.size(); i++) {
            double[][] dses = soluciones.get(i);
            data[i] = dses;
        }
        double mMax = Double.NEGATIVE_INFINITY, minMax = Double.POSITIVE_INFINITY, avgMax = 0, desMax = 0;
        double mMin = Double.NEGATIVE_INFINITY, minMin = Double.POSITIVE_INFINITY, avgMin = 0, desMin = 0;
        double mAvg = Double.NEGATIVE_INFINITY, minAvg = Double.POSITIVE_INFINITY, avgAvg = 0, desAvg = 0;
        double mDes = Double.NEGATIVE_INFINITY, minDes = Double.POSITIVE_INFINITY, avgDes = 0, desDes = 0;
        for (int i = 0; i < soluciones.size(); i++) {
            for (int j = 0; j < sol.length; j++) {
                if (data[i][j][3] > mMax) {
                    mMax = data[i][j][3];
                }
                if (data[i][j][3] < minMax) {
                    minMax = data[i][j][3];
                }
                if (data[i][j][4] > mMin) {
                    mMin = data[i][j][4];
                }
                if (data[i][j][4] < minMin) {
                    minMin = data[i][j][4];
                }
                if (data[i][j][5] > mAvg) {
                    mAvg = data[i][j][5];
                }
                if (data[i][j][2] < minAvg) {
                    minAvg = data[i][j][5];
                }
                if (data[i][j][3] > mDes) {
                    mDes = data[i][j][6];
                }
                if (data[i][j][3] < minDes) {
                    minDes = data[i][j][6];
                }
                avgMax += data[i][j][3];
                avgMin += data[i][j][4];
                avgAvg += data[i][j][5];
                avgDes += data[i][j][6];
            }

        }
        avgMax = avgMax / sol.length;
        avgMin = avgMin / sol.length;
        avgAvg = avgAvg / sol.length;
        avgDes = avgDes / sol.length;
        for (int i = 0; i < soluciones.size(); i++) {
            for (int j = 0; j < sol.length; j++) {
                desMax += Math.pow(data[i][j][3] - avgMax, 2);
                desMin += Math.pow(data[i][j][4] - avgMin, 2);
                desAvg += Math.pow(data[i][j][5] - avgAvg, 2);
                desDes += Math.pow(data[i][j][6] - avgDes, 2);
            }
        }
        desMax = desMax / (sol.length - 1);
        desMin = desMin / (sol.length - 1);
        desAvg = desAvg / (sol.length - 1);
        desDes = desDes / (sol.length - 1);
        desMax = Math.pow(desMax, 0.5);
        desMin = Math.pow(desMin, 0.5);
        desAvg = Math.pow(desAvg, 0.5);
        desDes = Math.pow(desDes, 0.5);
        System.out.println("valores\tmaximo\tminimo\tpromedio\tdesviacion");
        String out = "MAX\t" + mMax + "\t" + minMax + "\t" + avgMax + "\t" + desMax;
        String out2 = "MIN\t" + mMin + "\t" + minMin + "\t" + avgMin + "\t" + desMin;
        String out3 = "AVG\t" + mAvg + "\t" + minAvg + "\t" + avgAvg + "\t" + desAvg;
        String out4 = "DES\t" + mDes + "\t" + minDes + "\t" + avgDes + "\t" + desDes;
        out = out.replace('.', ',');
        out2 = out2.replace('.', ',');
        out3 = out3.replace('.', ',');
        out4 = out4.replace('.', ',');
        System.out.println(out + "\n" + out2 + "\n" + out3 + "\n" + out4 + "\n");

    }

    private static void readParams() throws FileNotFoundException, IOException {
        File f = new File("../VertexCoverHaea/dist/params.txt");
        if (!f.exists()) {
            return;
        }
        BufferedReader bf = new BufferedReader(new FileReader(f));
        String cad = bf.readLine();
        String split[];
        int j = 0;
        while (cad != null) {
            System.out.println(cad);
            split = cad.split(" ");
            BITARRAYLENGTH = Integer.parseInt(split[0]);
            POPSIZE = Integer.parseInt(split[1]);
            MAXITER = Integer.parseInt(split[2]);
            nExper = Integer.parseInt(split[3]);
            cad = bf.readLine();
            j++;
        }
    }

    private static double[][] readSol(int a) throws FileNotFoundException, IOException {
//        JFileChooser fc = new JFileChooser();
//        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//        int respuesta = fc.showOpenDialog(null);
//        File chosen = null;
//        if (respuesta == JFileChooser.APPROVE_OPTION) {
//            Crear un objeto File con el archivo elegido
//            chosen = fc.getSelectedFile();
//        }
        File f = new File( "../VertexCoverHaea/dist/experimento" + a + ".txt");
        if (!f.exists()) {
            return null;
        }
        BufferedReader bf = new BufferedReader(new FileReader(f));
        String cad = bf.readLine();
        String split[] = cad.split(" ");;
        double dataT[][] = new double[MAXITER + 1][split.length];
        int j = 0;
        while (cad != null) {
            //System.out.println(cad);
            split = cad.split(" ");
            for (int i = 0; i < split.length; i++) {
                double val = Double.parseDouble(split[i]);
                dataT[j][i] = val;
            }
            cad = bf.readLine();
            j++;
            if (j == MAXITER + 1) {
                break;
            }
        }
        return dataT;
    }

    private static void mostrarMat(double[][] sol) {
        for (int i = 0; i < sol.length; i++) {
            for (int j = 0; j < sol[i].length; j++) {
                System.out.print(sol[i][j] + " ");
            }
            System.out.println("");
        }
    }
}
