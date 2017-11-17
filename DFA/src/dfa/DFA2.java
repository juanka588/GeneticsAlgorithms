/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import unalcol.optimization.OptimizationFunction;
import unalcol.optimization.PopulationOptimizer;
import unalcol.optimization.iterative.IterativePopulationOptimizer;
import unalcol.optimization.operators.ArityOne;
import unalcol.optimization.operators.ArityTwo;
import unalcol.optimization.operators.Operator;
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
import unalcol.types.collection.vector.Vector;

/**
 *
 * @author JuanCamilo
 */
public class DFA2 {

    static int nStates;
    static int nAlphabet;

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        test(args);
    }

    public static void test(String[] args) throws IOException {
        nStates = 10;
        int resta = 0;
        nAlphabet = 2;
        int POPSIZE = 100;
        int MAXITER = 1500;
        int nExper = 50;
        int nRep = 1;
        String file = "train-";
        boolean tracing = true, posEti = false;
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                String cad = args[i];
                switch (i) {
                    case 0:
                        nExper = Integer.valueOf(cad);
                        break;
                    case 1:
                        nStates = Integer.valueOf(cad);
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
        // Reflection
        ServiceProvider provider = ReflectUtil.getProvider("services/");

        // Search Space 
        AgentDFA array = new AgentDFA(nStates, nAlphabet, nStates, new int[nStates * (nAlphabet + 1)]);
        InstanceService ikey = new DFAInstance(nStates, nAlphabet);
        provider.register(ikey);
        provider.setDefault_service(InstanceService.class, AgentDFA.class, ikey);

        // Solution space
        Solution<AgentDFA> solution = new Individual<AgentDFA, AgentDFA>(array, array);
        GrowingFunction<AgentDFA, AgentDFA> grow = new GrowingFunction();
        SolutionInstance skey = new IndividualInstance(grow);
        provider.setDefault_service(InstanceService.class, Solution.class, skey);

        // Initial population
        provider.register(skey);
        Vector<Solution<AgentDFA>> pop = InstanceProvider.get(solution, POPSIZE);

        // Function being optimized
        String examples[][] = readExamples(new File("./datasets/" + file + //+ "10.txt"));
                +(nStates - resta) + ".txt"));
        OptimizationFunction function = new FitnessDFA(examples, POPSIZE, posEti, false);
        // Evaluating the fitness of the initial population
        Solution.evaluate((Vector) pop, function);
        Solution<AgentDFA> p = null;
        ArityTwo xover = new XOverDFA(nStates, nAlphabet);
        ArityOne mutation = new MutationDFA(nStates, nAlphabet);
        ArityOne transposition = new TranspositionDFA(nStates, nAlphabet);
        ArityOne sMutation = new StrongMutationDFA(nStates, nAlphabet);

        // Genetic operators
        Operator[] opers = new Operator[]{mutation, sMutation, transposition};

        ArrayList<Solution> bests = new ArrayList<>();
        for (int k = 0; (k < nRep); k++) {
            examples = readExamples(new File("./datasets/" + file + //+ "10.txt"));
                    +(nStates - resta) + ".txt"));
            function = new FitnessDFA(examples, POPSIZE, posEti, k % 2 != 0);
            int MAXITER2 = MAXITER;
            if (k % 2 != 0) {
                opers = new Operator[]{mutation, xover, sMutation, transposition};
                MAXITER2 = MAXITER2 / 2;
            } else {
                opers = new Operator[]{mutation, xover, sMutation, transposition};
                pop = InstanceProvider.get(solution, POPSIZE);
            }

            // Genetic operators
            HaeaOperators haeaOperators = new SimpleHaeaOperators(opers);

            // Extra parent selection mechanism
            Selection selection = new Tournament(4);

            // Genetic Algorithm Transformation
            Transformation transformation = new HAEA(haeaOperators, grow, selection);

            // Evolution generations
            Predicate condition = new ForLoopCondition(MAXITER2);

            // Evolutionary algorithm (is a population optimizer)
            PopulationOptimizer ea = new IterativePopulationOptimizer(condition,
                    transformation, pop);
            if (k > 0) {
                pop.set(0, p);
                System.out.println(k + "-esimo Cambio pop\n_____________________________________________");
            }
            if (tracing) {
                // A console set tracer
                Tracer tracer = new ConsoleTracer(ea);
                // Adding the tracer collection to the given population optimizer (evolutionary algorithm)
                provider.register(tracer);
                tracer = new FileTracer(ea, "haea.txt", true);
                provider.register(tracer);
            }

            // running the population optimizer (the evolutionary algorithm)
            pop = (Vector<Solution<AgentDFA>>) ea.apply(function);
            int idx = 0;
            p = pop.get(0);
            pop = (Vector<Solution<AgentDFA>>) ea.apply(function);
            for (int i = 0; i < POPSIZE; i++) {
                if (pop.get(i).value() > p.value()) {
                    idx = i;
                    p = pop.get(i);
                }
            }
            bests.add(p);
            pop = InstanceProvider.get(solution, POPSIZE);
            pop.set(0, p);
            for (int i = 0; i < nStates; i++) {
                System.out.print(i + " ");
            }
            System.out.println("");
            System.out.println(pop.get(0).get());
            System.out.println(pop.get(0).value());
            examples = readExamples(new File("./datasets/test-" //+ "10.txt"));
                    + (nStates - resta) + ".txt"));
            function = new FitnessDFA(examples, POPSIZE, posEti, true);
            System.out.println(pop.get(0).evaluate(function));
            TracerProvider.close(ea);
        }
        examples = readExamples(new File("./datasets/test-" //+ "10.txt"));
                + (nStates - resta) + ".txt"));
        function = new FitnessDFA(examples, POPSIZE, posEti, true);
        for (Solution solution1 : bests) {
            System.out.println(solution1.evaluate(function));
        }
        guardarParams(nStates, POPSIZE, MAXITER, nExper);
    }

    public static String[][] readExamples(File file) throws FileNotFoundException, IOException {
        String arr[][];
        BufferedReader bf = new BufferedReader(new FileReader(file));
        String cad = bf.readLine();
        String[] split = cad.split(" ");
        arr = new String[Integer.valueOf(split[0])][3];
        int i = 0;
        while (cad != null) {
            cad = bf.readLine();
            if (cad == null) {
                break;
            }
            split = cad.split("\t");
            arr[i][1] = split[0];
            arr[i][0] = split[2];
            arr[i][2] = split[1];
            i++;
        }
        return arr;
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

    public static void guardarParams(int dime, int popSize, int iter, int nExper) throws IOException {
        BufferedWriter bf = new BufferedWriter(new FileWriter(new File("./params.txt")));
        PrintWriter pw = new PrintWriter(bf);
        pw.println(dime + " " + popSize + " " + iter + " " + nExper);
        pw.close();
        bf.close();
    }

    public static String print(int[] arr, int stateNumber) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (int i = 0; i < nAlphabet + 1; i++) {
            for (int j = 0; j < stateNumber; j++) {
                sb.append(arr[i * stateNumber + j]);
                sb.append(',');
            }
            sb.append('\n');
        }
        sb.append('}');
        return sb.toString();
    }

}
