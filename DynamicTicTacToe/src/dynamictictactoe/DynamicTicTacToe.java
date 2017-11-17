/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamictictactoe;

import java.io.BufferedWriter;
import java.io.File;
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
import unalcol.optimization.operators.binary.Transposition;
import unalcol.optimization.selection.Selection;
import unalcol.optimization.selection.Tournament;
import unalcol.optimization.solution.Solution;
import unalcol.optimization.solution.SolutionInstance;
import unalcol.optimization.testbed.real.Griewangk;
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
public class DynamicTicTacToe {

    private static int BITARRAYLENGTH;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        BITARRAYLENGTH = 100;
        int POPSIZE = 20;
        int MAXITER = 100;
        int nExper = 50;
        boolean tracing = true;
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
        // Reflection
        ServiceProvider provider = ReflectUtil.getProvider("services/");

        // Search Space 
        
        ArrayList<StatesMap> mapearJugadas = StatesMap.mapearJugadas();
        BITARRAYLENGTH=mapearJugadas.size();

        InstanceService ikey = new PlayerInstanceTTT(mapearJugadas);
        TicTacToePlayer array = new TicTacToePlayer(nExper, mapearJugadas, new int[10], new int[10]);
        array = (TicTacToePlayer) ikey.get(array);

        provider.register(ikey);
        provider.setDefault_service(InstanceService.class, TicTacToePlayer.class, ikey);

        // Solution space
        Solution<TicTacToePlayer> solution = new Individual<TicTacToePlayer, TicTacToePlayer>(array, array);
        GrowingFunction<TicTacToePlayer, TicTacToePlayer> grow = new GrowingFunction();
        SolutionInstance skey = new IndividualInstance(grow);
        provider.setDefault_service(InstanceService.class, Solution.class, skey);

        // Initial population
        provider.register(skey);
        Vector<Solution<TicTacToePlayer>> pop = InstanceProvider.get(solution, POPSIZE);

        // Function being optimized
        OptimizationFunction function = new coEvolutionFitness(pop);

        // Evaluating the fitness of the initial population
        Solution.evaluate((Vector) pop, function);

        ArityTwo xover = new XOverTTT();
        ArityOne mutation = new MutationTTT(0.05);
        ArityOne transposition = new Transposition();

        // Genetic operators
        Operator[] opers = new Operator[]{xover,mutation};
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
        pop = (Vector<Solution<TicTacToePlayer>>) ea.apply(function);
        System.out.println(pop.get(0).get().toString());
        System.out.println(pop.get(0).value());
        JugarTicTacToe ventana=new JugarTicTacToe();
        ventana.cpu=pop.get(0).get();
        ventana.show();
        TracerProvider.close(ea);
        guardarParams(BITARRAYLENGTH, POPSIZE, MAXITER, nExper);
    }

    public static void guardarParams(int dime, int popSize, int iter, int nExper) throws IOException {
        BufferedWriter bf = new BufferedWriter(new FileWriter(new File("./params.txt")));
        PrintWriter pw = new PrintWriter(bf);
        pw.println(dime + " " + popSize + " " + iter + " " + nExper);
        pw.close();
        bf.close();
    }
}
