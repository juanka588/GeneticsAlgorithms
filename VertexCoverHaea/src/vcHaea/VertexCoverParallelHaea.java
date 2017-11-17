/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vcHaea;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import unalcol.evolution.GrowingFunction;
import unalcol.evolution.Individual;
import unalcol.evolution.IndividualInstance;
import unalcol.instance.InstanceProvider;
import unalcol.instance.InstanceService;
import unalcol.optimization.OptimizationFunction;
import unalcol.optimization.operators.ArityOne;
import unalcol.optimization.operators.ArityTwo;
import unalcol.optimization.operators.Operator;
import unalcol.optimization.operators.binary.Mutation;
import unalcol.optimization.operators.binary.Transposition;
import unalcol.optimization.operators.binary.XOver;
import unalcol.optimization.solution.Solution;
import unalcol.optimization.solution.SolutionInstance;
import unalcol.reflect.service.ServiceProvider;
import unalcol.reflect.util.ReflectUtil;
import unalcol.types.collection.bitarray.BitArray;
import unalcol.types.collection.bitarray.BitArrayInstance;
import unalcol.types.collection.vector.Vector;
import static vcHaea.VertexCoverHaea.readFromFile;
import static vcHaea.VertexCoverHaea.readFromFileAR;

/**
 *
 * @author JuanCamilo
 */
public class VertexCoverParallelHaea {

    static Solution<BitArray> pop[][];
    static double fitness[];
    static double finish[];
    static int BITARRAYLENGTH, totalEdges;
    public static String file = "./sh2-10.dim.sh";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        BITARRAYLENGTH = 100;
        int POPSIZE = 100;
        int MAXITER = 5000;
        boolean tracing = false;
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                String cad = args[i];
                switch (i) {
                    case 0:
                        BITARRAYLENGTH = Integer.valueOf(cad);
                        break;
                    case 1:
                        POPSIZE = Integer.valueOf(cad);
                        break;
                    case 2:
                        MAXITER = Integer.valueOf(cad);
                        break;
                    case 4:
                        tracing = Integer.valueOf(cad) == 1;
                        break;
                }
            }
        }
        // Reflection
        ServiceProvider provider = ReflectUtil.getProvider("services/");
        // Search Space 
        // int[] mat = new int[BITARRAYLENGTH * BITARRAYLENGTH];
//        int[] mat = BookGraph3();
        ArrayList<ADrow> mat;
        mat = readFromFileAR(file);//centerGraph(BITARRAYLENGTH);//RandomMat(mat, BITARRAYLENGTH);//
        BITARRAYLENGTH = mat.size();
        BitArray array = new BitArray(BITARRAYLENGTH, true);
        InstanceService ikey = new BitArrayInstance();
        provider.register(ikey);
        provider.setDefault_service(InstanceService.class, BitArray.class, ikey);

        // Solution space
        Solution<BitArray> solution = new Individual<>(array, array);
        GrowingFunction<BitArray, BitArray> grow = new GrowingFunction();
        SolutionInstance skey = new IndividualInstance(grow);
        provider.setDefault_service(InstanceService.class, Solution.class, skey);

        OptimizationFunction function = new VertexCoverFitness(mat, VertexCoverHaea.totalEdges);
        //guardarMat(VertexCoverFitness.mat, BITARRAYLENGTH);

        // Initial population
        provider.register(skey);
        Vector<Solution<BitArray>> pop = InstanceProvider.get(solution, POPSIZE);

        ArityTwo xover = new XOver();
        ArityOne mutation = new Mutation();
        ArityOne transposition = new Transposition();

        // Genetic operators
        Operator[] opers = new Operator[]{mutation, transposition, xover};
        finish = new double[pop.size()];
        fitness = new double[pop.size()];
        for (int i = 0; i < pop.size(); i++) {
            finish[i] = 1.0;
            fitness[i] = Double.MAX_VALUE;
        }
        ParallelHaea ph;
        PrintThread pt = new PrintThread(pop, function);
        Thread t;
        //System.out.println(printMat(pop));
        t = new Thread(pt);
        t.start();
        for (int i = 0; i < pop.size(); i++) {
            ph = new ParallelHaea(i, function, pop, grow, MAXITER, opers, pt);
            t = new Thread(ph);
            t.start();

        }
        Thread.currentThread().join(1);
    }

    public static int[] RandomMat(int[] mat, int size) {
        Random rn = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i < j && rn.nextDouble() < 0.1) {
                    int val = 1;
                    mat[i * size + j] = val;
                    mat[j * size + i] = val;
                }
            }

        }
        return mat;
    }

    public static int[] centerGraph(int size) {
        int mat[] = new int[size * size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                mat[i * size + j] = i == 0 || j == 0 ? 1 : 0;
            }
        }

        return mat;
    }

    public static File centerGraphFile(int size) throws IOException {
        BufferedWriter bf = new BufferedWriter(new FileWriter(new File("./centerG" + size + ".mis")));
        PrintWriter pw = new PrintWriter(bf);
        pw.print("p edge " + (size + 1) + " " + size);
        pw.println();
        for (int j = 0; j < size; j++) {
            pw.print("e 1 " + (j + 2));
            pw.println();
        }
        pw.close();
        bf.close();
        return new File("./centerG" + size + ".mis");
    }

    public static int[] HelmGraph4() {
        int mat[] = new int[]{0, 1, 0, 1, 1, 0, 0, 0, 1,
            1, 0, 1, 0, 0, 1, 0, 0, 1,
            0, 1, 0, 1, 0, 0, 1, 0, 1,
            1, 0, 1, 0, 0, 0, 0, 1, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 1, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 1, 0, 0, 0, 0, 0,
            1, 1, 1, 1, 0, 0, 0, 0, 0};

        return mat;
    }

    public static File HemlmGraphFile(int size) throws IOException {
        BufferedWriter bf = new BufferedWriter(new FileWriter(new File("./HelmG" + size + ".mis")));
        PrintWriter pw = new PrintWriter(bf);
        pw.print("p edge " + (size * 2 + 1) + " " + (size * 2 + size));
        pw.println();
        for (int j = 0; j < size; j++) {
            pw.print("e 1 " + (j + 2));
            pw.println();
        }
        pw.print("e 2 3");
        pw.println();
        pw.print("e 2 " + (size + 1));
        pw.println();
        pw.print("e 2 " + (size + 2));
        pw.println();
        for (int i = 3; i < size + 1; i++) {
            pw.print("e " + i + " " + (1 + i));
            pw.println();
            pw.print("e " + i + " " + (size + i));
            pw.println();
        }
        pw.print("e " + (size + 1) + " " + (size * 2 + 1));
        pw.println();
        pw.close();
        bf.close();
        return new File("./HelmG" + size + ".mis");
    }

    public static int[] GearGraph4() {
        int mat[] = new int[]{0, 1, 0, 1, 0, 0, 0, 0, 0,
            1, 0, 1, 0, 1, 0, 0, 0, 0,
            0, 1, 0, 0, 0, 1, 0, 0, 0,
            1, 0, 0, 0, 1, 0, 1, 0, 0,
            0, 1, 0, 1, 0, 1, 0, 1, 0,
            0, 0, 1, 0, 1, 0, 0, 0, 1,
            0, 0, 0, 1, 0, 0, 0, 1, 0,
            0, 0, 0, 0, 1, 0, 1, 0, 1,
            0, 0, 0, 0, 0, 1, 0, 1, 0};

        return mat;
    }

    public static int[] BookGraph3() {
        int mat[] = new int[]{0, 0, 0, 1, 1, 0, 0, 0,
            0, 0, 0, 1, 0, 1, 0, 0,
            0, 0, 0, 1, 0, 0, 1, 0,
            1, 1, 1, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 1,
            0, 1, 0, 0, 0, 0, 0, 1,
            0, 0, 1, 0, 0, 0, 0, 1,
            0, 0, 0, 1, 1, 1, 1, 0};

        return mat;
    }

    public static int[] CrossedPrismGraph4() {
        int mat[] = new int[]{0, 1, 1, 0, 1, 0, 0, 0,
            1, 0, 0, 1, 0, 1, 0, 0,
            1, 0, 0, 1, 0, 0, 1, 0,
            0, 1, 1, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 1, 1, 0,
            0, 1, 0, 0, 1, 0, 0, 1,
            0, 0, 1, 0, 1, 0, 0, 1,
            0, 0, 0, 1, 0, 1, 1, 0};
        return mat;
    }

    public static int[] HanoiGraph2() {
        int mat[] = new int[]{0, 1, 1, 0, 0, 0, 0, 0, 0,
            1, 0, 1, 1, 0, 0, 0, 0, 0,
            1, 1, 0, 0, 1, 0, 0, 0, 0,
            0, 1, 0, 0, 0, 1, 1, 0, 0,
            0, 0, 1, 0, 0, 0, 0, 1, 1,
            0, 0, 0, 1, 0, 0, 1, 0, 0,
            0, 0, 0, 1, 0, 1, 0, 1, 0,
            0, 0, 0, 0, 1, 0, 1, 0, 1,
            0, 0, 0, 0, 1, 0, 0, 1, 0};
        return mat;
    }

    public static void guardarMat(int mat[], int size) throws IOException {
        BufferedWriter bf = new BufferedWriter(new FileWriter(new File("./grafo.txt")));
        PrintWriter pw = new PrintWriter(bf);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                pw.print(mat[i * size + j] + " ");
            }
            pw.println();
        }
        pw.close();
        bf.close();
    }

    public static void guardarIndividuo(BitArray s) throws IOException {
        BufferedWriter bf = new BufferedWriter(new FileWriter(new File("./solucion.txt")));
        PrintWriter pw = new PrintWriter(bf);
        for (int i = 0; i < s.size(); i++) {
            pw.print(s.get(i) ? 1 + " " : 0 + " ");
        }
        pw.close();
        bf.close();
    }

    private static String printMat(double[][] pop) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pop.length; i++) {
            for (int j = 0; j < pop.length; j++) {
                sb.append(pop[i][j]);
                sb.append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private static String printMat(Solution[][] pop) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pop.length; i++) {
            for (int j = 0; j < pop.length; j++) {
                if (pop[i][j] != null) {
                    sb.append(pop[i][j].get());
                } else {
                    sb.append('-');
                }
                sb.append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public static File GearGraphFile(int size) throws IOException {
        BufferedWriter bf = new BufferedWriter(new FileWriter(new File("./GearG" + size + ".mis")));
        PrintWriter pw = new PrintWriter(bf);
        pw.print("p edge " + (size * 2 + 1) + " " + (size * 2 + size));
        pw.println();
        for (int j = 0; j < size; j++) {
            pw.print("e 1 " + (j + 2));
            pw.println();
        }
        pw.print("e 2 " + (2 * size + 1));
        pw.println();
        int j = 2;
        for (int i = size + 2; i < 2 * size+1; i++) {
            pw.print("e " + j + " " + i);
            pw.println();
            pw.print("e " + (j + 1) + " " + i);
            pw.println();
            j++;
        }
        pw.print("e " + (size + 1) + " " + (size * 2 + 1));
        pw.println();
        pw.close();
        bf.close();
        return new File("./GearG" + size + ".mis");
    }
}
