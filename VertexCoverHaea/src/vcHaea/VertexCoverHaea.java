/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vcHaea;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import unalcol.algorithm.iterative.ForLoopCondition;
import unalcol.evolution.GrowingFunction;
import unalcol.evolution.Individual;
import unalcol.evolution.IndividualInstance;
import unalcol.evolution.haea.HAEA;
import unalcol.evolution.haea.HaeaOperators;
import unalcol.evolution.haea.SimpleHaeaOperators;
import unalcol.instance.InstanceProvider;
import unalcol.instance.InstanceService;
import unalcol.math.logic.Predicate;
import unalcol.optimization.PopulationOptimizer;
import unalcol.optimization.iterative.IterativePopulationOptimizer;
import unalcol.optimization.operators.ArityOne;
import unalcol.optimization.operators.ArityTwo;
import unalcol.optimization.operators.Operator;
import unalcol.optimization.operators.binary.Mutation;
import unalcol.optimization.operators.binary.Transposition;
import unalcol.optimization.operators.binary.XOver;
import unalcol.optimization.selection.Selection;
import unalcol.optimization.selection.Tournament;
import unalcol.optimization.solution.Solution;
import unalcol.optimization.solution.SolutionInstance;
import unalcol.optimization.transformation.Transformation;
import unalcol.reflect.service.ServiceProvider;
import unalcol.reflect.util.ReflectUtil;
import unalcol.tracer.ConsoleTracer;
import unalcol.tracer.FileTracer;
import unalcol.tracer.Tracer;
import unalcol.tracer.TracerProvider;
import unalcol.types.collection.bitarray.BitArray;
import unalcol.types.collection.bitarray.BitArrayInstance;
import unalcol.types.collection.vector.Vector;

/**
 *
 * @author JuanCamilo
 */
public class VertexCoverHaea {

    static int BITARRAYLENGTH;
    static int totalEdges;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        BITARRAYLENGTH = 100;
        int POPSIZE = 100;
        int MAXITER = 1000;
        int nExper = 30;
        int rep = nExper;
        boolean tracing = true;
        Solution<BitArray> p = null;
        String files[] = new String[]{"brock800_3.clq",
            "johnson32-2-4.clq",
            "p_hat1500-2.clq",
            "C1000.9.clq.compl",
            "keller6.clq.compl",
            "hamming10-2.clq.compl",
            "MANN_a45.clq.compl",
            "DSJC1000.1.col",
            "flat1000_50_0.col",
            "zeroin.i.2.col",
            "DSJR500.1.col",
            "graph17.vc",
            "graph20.vc",
            "graph22.vc",
            "sh2-3.dim.sh",
            "sh2-10.dim.sh",
            "sh2-10.dim.sh.pp",
            "C4000.5.clq",
            "c-fat500-2.clq",
            "MANN_a81.clq",
            "vtx_cov_3.gph"};
        for (int h = 12; h < 13; h++) {
            String file = files[h];
            //file = VertexCoverParallelHaea.HemlmGraphFile(BITARRAYLENGTH).getAbsolutePath();
        file = "./graph20.vc"; //archivos VC BENCHMARK
            if (args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    String cad = args[i];
                    switch (i) {
                        case 0:
                            nExper = Integer.valueOf(cad);
                            break;
                        case 1:
                            BITARRAYLENGTH = Integer.valueOf(cad);
                            break;
                        case 2:
                            POPSIZE = Integer.valueOf(cad);
                            break;
                        case 3:
                            MAXITER = Integer.valueOf(cad);
                            break;
                        case 4:
                            tracing = Integer.valueOf(cad) == 1;
                            break;
                    }
                }
            }
            Solution bests[] = new Solution[rep];
            // Reflection
            ServiceProvider provider = ReflectUtil.getProvider("services/");
            // Search Space 
            ArrayList<ADrow> mat = new ArrayList<>();
            mat = readFromFileAR(file);
            BitArray array = new BitArray(BITARRAYLENGTH, true);
            InstanceService ikey = new BitArrayInstance();
            provider.register(ikey);
            provider.setDefault_service(InstanceService.class, BitArray.class, ikey);
            // Solution space
            Solution<BitArray> solution = new Individual<BitArray, BitArray>(array, array);
            GrowingFunction<BitArray, BitArray> grow = new GrowingFunction();
            SolutionInstance skey = new IndividualInstance(grow);
            provider.setDefault_service(InstanceService.class, Solution.class, skey);
            // Initial population
            provider.register(skey);
            Vector<Solution<BitArray>> pop = InstanceProvider.get(solution, POPSIZE);
            // Function being optimized
            VertexCoverFitness function = new VertexCoverFitness(mat, totalEdges);
            // Evaluating the fitness of the initial population
            Solution.evaluate((Vector) pop, function);

            ArityTwo xover = new XOver();
            ArityOne mutation = new Mutation();
            ArityOne transposition = new Transposition();
            ArityOne opt = new VertexOPT(mat);
            ArityOne opt2 = new VertexOPT2(mat);
            // Genetic operators
            Operator[] opers = new Operator[]{xover, mutation, transposition, opt, opt2};

            for (int k = 0; k < rep; k++) {
//            if (k > 0) {
//                pop.set(0, p);                
//            }
                System.out.println(k + "-esimo Cambio pop\n_____________________________________________");
//            function = new VertexCoverFitness(mat, totalEdges);
                HaeaOperators haeaOperators = new SimpleHaeaOperators(opers);

                // Extra parent selection mechanism
                Selection selection = new Tournament(4);

                // Genetic Algorithm Transformation
                Transformation transformation = new HAEA(haeaOperators, grow, selection);

                // Evolution generations
                Predicate condition = new ForLoopCondition(MAXITER);

                // Evolutionary algorithm (is a population optimizer)
                PopulationOptimizer ea = new IterativePopulationOptimizer(condition,
                        transformation, pop);

                if (tracing) {
                    // A console set tracer
                    Tracer tracer = new ConsoleTracer(ea);
                    // Adding the tracer collection to the given population optimizer (evolutionary algorithm)
                    provider.register(tracer);
                    tracer = new FileTracer(ea, "haea.txt", true);
                    provider.register(tracer);
                }

                // running the population optimizer (the evolutionary algorithm)
                pop = (Vector<Solution<BitArray>>) ea.apply(function);
                p = pop.get(0);
                bests[k] = p;
                System.out.println(p.get().toString());
                System.out.println(p.value());
                TracerProvider.close(ea);
                guardarIndividuo(p.get());
//            guardarParams(BITARRAYLENGTH, POPSIZE, MAXITER, nExper);
//            System.out.println("solo con 1: " + function.apply(new BitArray(trueArray(BITARRAYLENGTH))));       
                function = new VertexCoverFitness(readFromFile(file));
                guardarMat(function.mat, BITARRAYLENGTH);
//            System.out.println("Real Fitness sol: " + VertexCoverFitness.realFit(p.get()));
                System.out.println("Real Repair Fitness sol: " + VertexCoverFitness.realFit(repararSol(p.get(), function.mat)));

                pop = InstanceProvider.get(solution, POPSIZE);
            }
            function = new VertexCoverFitness(readFromFile(file));
            stats(bests, function);
            System.out.println(file);
            System.out.println("iter: " + MAXITER);
        }
    }

    public static void stats(Solution pop[], VertexCoverFitness func) {
        double max = Double.NEGATIVE_INFINITY, min = Double.POSITIVE_INFINITY, avg = 0, des = 0;
        int size = pop.length;
        double realFit[] = new double[size];
        for (int i = 0; i < realFit.length; i++) {
            realFit[i] = VertexCoverFitness.realFit(repararSol((BitArray) pop[i].get(), func.mat));
        }
        for (int i = 0; i < size; i++) {
            if (realFit[i] > max) {
                max = realFit[i];
            }
            if (realFit[i] < min) {
                min = realFit[i];
            }
            avg += realFit[i];
        }
        avg = avg / size;
        for (int i = 0; i < size; i++) {
            des += Math.pow((realFit[i] - avg), 2);
        }
        if (size > 1) {
            des = des / (size - 1);
            des = Math.pow(des, 0.5);
        }
        String out = (1) + "\t" + min + "\t" + max + "\t" + avg + "\t" + des;
        System.out.println(out);
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

    private static BitArray trueArray(int s) {
        boolean arr[] = new boolean[s];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = true;
        }
        return new BitArray(arr);
    }

    public static void guardarMat(int mat[], int size) throws IOException {
//        System.out.println("size de mat: "+size);
//        VertexCoverFitness.showRowMat(mat);
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

    public static void guardarParams(int dime, int popSize, int iter, int nExper) throws IOException {
        BufferedWriter bf = new BufferedWriter(new FileWriter(new File("./params.txt")));
        PrintWriter pw = new PrintWriter(bf);
        pw.println(dime + " " + popSize + " " + iter + " " + nExper);
        pw.close();
        bf.close();
    }

    private static void guardarIndividuo(BitArray s) throws IOException {
        BufferedWriter bf = new BufferedWriter(new FileWriter(new File("./solucion.txt")));
        PrintWriter pw = new PrintWriter(bf);
        for (int i = 0; i < s.size(); i++) {
            pw.print(s.get(i) ? 1 + " " : 0 + " ");
        }
        pw.close();
        bf.close();
    }

    public static int[] readFromFile(String path) throws FileNotFoundException, IOException {
        int mat[];
        BufferedReader bf = new BufferedReader(new FileReader(new File(path)));
        String cad = bf.readLine();
        String split[] = cad.split(" ");
        int size = Integer.parseInt(split[2]);
        BITARRAYLENGTH = size;
        mat = new int[size * size];
        for (int i = 0; i < mat.length; i++) {
            mat[i] = 0;
        }
        int j = 0;
        boolean cond = false;
        cad = bf.readLine();
        while (cad != null) {
            //System.out.println(cad);
            split = cad.split(" ");
            int valI = Integer.parseInt(split[1]) - 1;
            int valJ = Integer.parseInt(split[2]) - 1;
            if (valI < 0) {
                cond = true;
            }
            if (cond) {
                valI++;
                valJ++;
            }
            mat[valI * size + valJ] = 1;
            mat[valJ * size + valI] = 1;
            cad = bf.readLine();
        }
        return mat;
    }

    public static ArrayList<ADrow> readFromFileAR(String path) throws FileNotFoundException, IOException {
        ArrayList<ADrow> mat = new ArrayList<ADrow>();
        BufferedReader bf = new BufferedReader(new FileReader(new File(path)));
        String cad = bf.readLine();
        String split[] = cad.split(" ");
        int size = Integer.parseInt(split[2]);
        for (int i = 0; i < size; i++) {
            ADrow mat2 = new ADrow(new ArrayList<Integer>(), i);
            mat.add(mat2);
        }
        BITARRAYLENGTH = size;
        int j = 0;
        cad = bf.readLine();
        boolean cond = false;
        while (cad != null) {
//            System.out.println(cad);
            split = cad.split(" ");
            int valI = Integer.parseInt(split[1]) - 1;
            int valJ = Integer.parseInt(split[2]) - 1;
            if (valI < 0) {
                cond = true;
            }
            if (cond) {
                valI++;
                valJ++;
            }
            mat.get(valI).add(valJ);
            mat.get(valJ).add(valI);
            j++;
            cad = bf.readLine();
        }
        totalEdges = j;
        int s = 0;
        for (int i = 0; i < mat.size(); i++) {
            s += mat.get(i).calGreaters();
        }
        System.out.println("count " + j + " " + s);
        return mat;
    }

    private static BitArray repararSol(BitArray get, int mat[]) {
        int size = get.size();
        for (int i = 0; i < size; i++) {
            boolean j = get.get(i);
            if (j) {
                for (int k = 0; k < size; k++) {
                    if (mat[i * size + k] == 1) {
                        mat[i * size + k] = 0;
                        mat[k * size + i] = 0;
                    }
                }
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                if (mat[i * size + j] == 1) {
                    get.set(i, true);
                }
            }
        }
        return get;
    }
}
