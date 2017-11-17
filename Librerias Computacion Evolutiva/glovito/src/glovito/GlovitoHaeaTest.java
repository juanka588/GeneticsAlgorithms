/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package glovito;

import unalcol.algorithm.iterative.ForLoopCondition;
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
import unalcol.optimization.operators.binary.Mutation;
import unalcol.optimization.operators.binary.Transposition;
import unalcol.optimization.operators.binary.XOver;
import unalcol.optimization.selection.Selection;
import unalcol.optimization.selection.Tournament;
import unalcol.optimization.solution.Solution;
import unalcol.optimization.solution.SolutionInstance;
import unalcol.optimization.testbed.binary.BoundedlyDeceptive;
import unalcol.optimization.testbed.binary.Deceptive;
import unalcol.optimization.testbed.binary.MaxOnes;
import unalcol.optimization.testbed.binary.RoyalRoad;
import unalcol.optimization.transformation.Transformation;
import unalcol.reflect.service.ServiceProvider;
import unalcol.reflect.util.ReflectUtil;
import unalcol.tracer.ConsoleTracer;
import unalcol.tracer.FileTracer;
import unalcol.tracer.Tracer;
import unalcol.tracer.TracerProvider;
import unalcol.types.collection.bitarray.BitArray;
import unalcol.types.collection.bitarray.BitArrayInstance;
import unalcol.types.collection.vector.*;

/**
 *
 * @author jgomez
 */
public class GlovitoHaeaTest {
    public static void main( String[] args ){
        // Reflection
        ServiceProvider provider = ReflectUtil.getProvider("services/");

        // Search Space 
        int BITARRAYLENGTH = 24;
        BitArray array = new BitArray(BITARRAYLENGTH, true);
        InstanceService ikey = new BitArrayInstance();
        provider.register(ikey);
        provider.setDefault_service(InstanceService.class,BitArray.class,ikey);

        // Solution space
        GrowGlovito grow = new GrowGlovito();
        Solution<Glovito> solution = new Individual<BitArray,Glovito>(array, grow);
        SolutionInstance skey = new IndividualInstance( grow );
        provider.setDefault_service(InstanceService.class,Solution.class,skey);

        // Initial population
        int POPSIZE = 100;
        provider.register(skey);
        Vector<Solution<BitArray>> pop = InstanceProvider.get(solution, POPSIZE);

        // Function being optimized
        OptimizationFunction function =  new FitnessGlovito( new int[]{1,1,0,0,0,1,1,0,1,0,1,0,0,0,0,1,1,1,0,1,0,1,0,1,0,1,0,0,1,0} );
        // Evaluating the fitness of the initial population
        Solution.evaluate((Vector)pop, function);

        ArityTwo xover = new XOver();
        ArityOne mutation = new Mutation();
        ArityOne transposition = new Transposition();
               
        // Genetic operators
        Operator[] opers = new Operator[]{mutation, transposition, xover};            
        HaeaOperators haeaOperators = new SimpleHaeaOperators(opers);

        // Extra parent selection mechanism
        Selection selection = new Tournament(4);

        // Genetic Algorithm Transformation
        Transformation transformation = new HAEA(haeaOperators, grow, selection );

        // Evolution generations
        int MAXITER = 100;
        Predicate condition = new ForLoopCondition(MAXITER);

        // Evolutionary algorithm (is a population optimizer)
        PopulationOptimizer ea = new IterativePopulationOptimizer(condition,
                transformation, pop);

        boolean tracing = true;
        if( tracing){
            // A console set tracer
            Tracer tracer = new ConsoleTracer(ea);
            // Adding the tracer collection to the given population optimizer (evolutionary algorithm)
            provider.register(tracer);
            tracer = new FileTracer(ea, "haea.txt", true);
            provider.register(tracer);
        }

        // running the population optimizer (the evolutionary algorithm)
        pop = (Vector<Solution<BitArray>>)ea.apply(function);
        System.out.println( pop.get(0).get() );
        
        TracerProvider.close(ea);
    }
    
}
