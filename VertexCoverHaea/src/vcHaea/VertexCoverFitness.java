/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vcHaea;

import java.io.IOException;
import java.util.ArrayList;
import unalcol.optimization.OptimizationFunction;
import unalcol.types.collection.bitarray.BitArray;

/**
 *
 * @author JuanCamilo
 */
public class VertexCoverFitness extends OptimizationFunction<BitArray> {

    static int mat[];
    static double matSum;

    static ArrayList<ADrow> arisMat;
    static int tEdges;
    int cVertex = -1, cEdges = -1, cVertexA = -1, cTotal = -1;

    public VertexCoverFitness(int mat[]) {
        this.mat = mat;
        matSum = sum(mat);
        //showMat(mat);
        //llamar();
    }

    public VertexCoverFitness(ArrayList<ADrow> arisMat, int totalEdges) {
        this.arisMat = arisMat;
        tEdges = totalEdges;
    }

    public VertexCoverFitness() {
        this(new int[]{0, 0, 1, 0, 1, 0,
            0, 0, 1, 0, 0, 0,
            1, 1, 0, 1, 0, 1,
            0, 0, 1, 0, 0, 1,
            1, 0, 0, 0, 0, 1,
            0, 0, 1, 1, 1, 0});
        arisMat = new ArrayList<>();
    }

//    @Override
    public Double apply2(BitArray s) {
        double resta = 0;
        int size = s.size();
        int mat2[] = new int[size * size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                mat2[i * size + j] = mat[i * size + j];
            }
        }
        for (int i = 0; i < s.size(); i++) {
            boolean j = s.get(i);
            if (j) {
                for (int k = 0; k < size; k++) {
                    if (mat2[i * size + k] == 1) {
                        mat2[i * size + k] = 0;
                        mat2[k * size + i] = 0;
                        resta += 2;
                    }
                }
            }
        }
        double n = sum(s), sumt = matSum - resta;
        return sumt * 0.75 - n * 2.25;
    }

    static Double realFit(BitArray s) {
//        int size = s.size();
        double n = sum(s);
        System.out.println("n vertices: " + n);
//        System.out.println("size " + size);
//        System.out.println("vertex number " + VertexCoverHaea.totalEdges);
//        int mat2[] = new int[size * size];
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                mat2[i * size + j] = mat[i * size + j];
//            }
//        }
//        for (int i = 0; i < s.size(); i++) {
//            boolean j = s.get(i);
//            if (j) {
//                for (int k = 0; k < size; k++) {
//                    if (mat2[i * size + k] == 1) {
//                        mat2[i * size + k] = 0;
//                        mat2[k * size + i] = 0;
//                    }
//                }
//            }
//        }
//        double sumt = sum(mat2);
        return n;
    }

    @Override
    public Double apply(BitArray s) {
        int size = s.size();
        int totalSize1 = 0;
        int currentSize1 = 0;
        double sum = 0.0;
        for (int i = 0; i < size; i++) {
            int rowSize = arisMat.get(i).size();
            if (rowSize == 1) {
                totalSize1++;
            }
            if (s.get(i)) {
                sum += arisMat.get(i).greaterSum;
                int j = 0;
                if (rowSize == 1) {
                    currentSize1++;
                }
                if (rowSize > 0) {
                    int currVal = arisMat.get(i).get(j);
                    while (j < rowSize && currVal < i) {
                        if (!s.get(currVal)) {
                            sum++;
                        }
                        j++;
                        if (j < rowSize) {
                            currVal = arisMat.get(i).get(j);
                        }
                    }
                }
            }
        }        
        if (cEdges == -1) {
            calcularCifras(tEdges, s.size(), totalSize1);
        }
        double n = sum(s);
        double alpha=0.98;
        //System.out.println("n: "+n+" / "+size+" edges "+sum+" Tedges "+tEdges);
        return ((sum-tEdges)/tEdges)*alpha-(n/size)*(1-alpha);
        /*
        if (totalSize1 == 0) {
            return (sum) * Math.pow(10, cTotal-cEdges) - (n);
        }
//        System.out.println("hay quitables");
        return (sum) * Math.pow(10, cTotal-cEdges) - (n) * Math.pow(10, cTotal - cEdges-cVertexA) - 
                currentSize1;
         */
    }

    public static void main(String[] args) throws IOException {
        ArrayList<ADrow> mat = new ArrayList<>();
        mat = VertexCoverHaea.readFromFileAR("./frb7-15-1.mis");

        VertexCoverFitness vcf = new VertexCoverFitness(mat, 10);
        boolean sol[] = new boolean[]{false, false, true, true, true, false, true};
        BitArray s = new BitArray(sol);
        System.out.println(vcf.apply(s));
        System.out.println(s);

    }

    public static void showRowMat(int[] mat) {
        int size = (int) Math.sqrt(mat.length);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(mat[i * size + j] + " ");
            }
            System.out.println("");
        }
    }

    private static double sum(int[] arr) {
        double d = 0.0;
        for (int i = 0; i < arr.length; i++) {
            d += arr[i];
        }
        return d;
    }

    public static double sum(BitArray arr) {
        double d = 0.0;
        for (int i = 0; i < arr.size(); i++) {
            d += arr.get(i) ? 1 : 0;
        }
        return d;
    }

    private void calcularCifras(int edges, int vertex, int vertexA) {
        cEdges = 0;
        cVertex = 0;
        cVertexA = 0;
        System.out.println("e: " + edges + " v: " + vertex + " vA: " + vertexA);
        while (edges > 0) {
            cEdges++;
            edges = edges / 10;
        }
        while (vertex > 0) {
            cVertex++;
            vertex = vertex / 10;
        }
        while (vertexA > 0) {
            cVertexA++;
            vertexA = vertexA / 10;
        }
        cTotal = cEdges + cVertex + cVertexA;
        System.out.println("ce: " + cEdges + " cv: " + cVertex + " cvA: " + cVertexA);
    }

}
