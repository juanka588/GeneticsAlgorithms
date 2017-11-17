import unalcol.optimization.Population;
import unalcol.optimization.transformation.Transformation;
import unalcol.optimization.operators.binary.Transposition;
import unalcol.optimization.operators.binary.Mutation;
import unalcol.optimization.operators.binary.XOver;
import unalcol.optimization.selection.Selection;
import unalcol.optimization.selection.Tournament;
import unalcol.optimization.operators.Operator;
import java.util.*;

//Services
import unalcol.reflect.service.*;
import unalcol.reflect.util.*;
import unalcol.tracer.*;
import unalcol.io.*;

//Optimization classes
import unalcol.optimization.*;

//Evolution classes
import unalcol.evolution.*;
import unalcol.optimization.testbed.binary.*;
import unalcol.optimization.testbed.real.*;
import unalcol.evolution.haea.*;
import unalcol.evolution.genotype.binary.*;
import unalcol.evolution.genotype.binary.util.*;
import unalcol.evolution.genotype.real.util.*;


//Additional classes
import unalcol.math.logic.*;
import unalcol.algorithm.iterative.*;
import unalcol.optimization.IterativePopulationOptimizer;
import unalcol.types.real.array.*;

import lsgo_benchmark.*;
import unalcol.evolution.coga.*;
import unalcol.evolution.coea.*;

/**
 * <p>Title: EATest</p>
 * <p>Description: Test of Ea</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Universidad Nacional de Colombia - The University of Memphis</p>
 * @author Jonatan Gomez Reviewed by (Aurelio Benitez, Giovanni Cantor, Nestor Bohorquez)
 * @version 1.0
 */
public class COGATest {
    public static void main( String[] args ){
        // Reflection
        ServiceProvider provider = ReflectUtil.getProvider("services/");
        // Array persistency methods (writes the array as a line of real values separated by comma character
        DoubleArraySimpleWriteService key = new DoubleArraySimpleWriteService(',');
        provider.register(key);
        provider.setDefault_service(WriteService.class,double[].class,key);

//        int CHROMOSOMESIZE = 100;
//        GrowingFunction grow = new GrowingFunction();
//        GrowingFunction grow = new GrowingBinaryToRealVector(GENESIZE, false);
        
        Function lsgo_f = new F1();
        int CHROMOSOMESIZE = lsgo_f.getDimension();
        double[] min = DoubleArrayInit.create(CHROMOSOMESIZE, lsgo_f.getMin());
        double[] max = DoubleArrayInit.create(CHROMOSOMESIZE, lsgo_f.getMax());


        int GENESIZE = 32;
        int SUB_POPS = 10;
        int FRIENDS = SUB_POPS-1;
        int SOLUTION_BITS = CHROMOSOMESIZE*GENESIZE/SUB_POPS;
        int FRIEND_BITS = 3;
        int SUB_POPS_SIZE = (1 << FRIEND_BITS);

        // Function being optimized
        //OptimizationFunction function = new MaxOnes();
//        OptimizationFunction function = new Rastrigin();
        OptimizationFunction theF = null; //new FunctionWrap(lsgo_f);
        OptimizationFunction[] function = new OptimizationFunction[SUB_POPS];
        for( int i=0; i<SUB_POPS; i++ ){
            function[i] = theF;
        }

        
        // Encoding decoding scheme
        Genotype genotype = new BinaryGenotype();        
        genotype.setSize( SOLUTION_BITS + FRIENDS*FRIEND_BITS);
        
        // Growing function
        GrowingBinaryToRealVector llg = new GrowingBinaryToRealVector(GENESIZE, min, max);
        GrowingFunction[] grow = new GrowingFunction[SUB_POPS];
        for( int i=0; i<SUB_POPS; i++){
           grow[i] = new COGAGrowingFunction(llg, i, SOLUTION_BITS, FRIENDS, FRIEND_BITS);
        }   
        // Initial populations
        Population[] pop = new Population[SUB_POPS];
        for( int i=0; i<SUB_POPS; i++){
            pop[i] = grow[i].build( genotype.build(SUB_POPS_SIZE) );
        }    

        // Genetic operators
        XOver xover = new XOver();
        Mutation mutation = new Mutation();
        Transposition transposition = new Transposition();

        //HaeaOperators
        Operator[] opers = new Operator[]{xover, mutation, transposition};
        HaeaOperators haeaOperators = new SimpleHaeaOperators(opers);

        // Extra parent selection mechanism
        Selection selection = new Tournament(4);

        // Genetic Algorithm Transformation
        Transformation[] transformation = new Transformation[SUB_POPS];
        // Evolutionary algorithm (is a population optimizer)
        IterativePopulationOptimizer[] niches = new IterativePopulationOptimizer[SUB_POPS];
        
        // Evolution generations
        int MAXITER = 12500;
        Predicate condition = new ForLoopCondition(MAXITER);

        for( int i=0; i<SUB_POPS; i++){
            transformation[i] = new HAEA(haeaOperators, grow[i], selection );
            niches[i] = new IterativePopulationOptimizer(condition,
                transformation[i], pop[i]);
        }    

        // Evolutionary algorithm (is a population optimizer)
        CoEvolutionaryAlgorithm ea = new CoEvolutionaryAlgorithm(condition, niches, pop);
        
        for( int i=0; i<SUB_POPS; i++){
            ((COGAGrowingFunction)grow[i]).setCoEA(ea);
        }

        // Evaluating the fitness of the initial population
        for( int i=0; i<SUB_POPS; i++){
            pop[i].evaluate(function[i]);
        }    

        
        // A console set tracer
        Tracer tracer = new ConsoleTracer(ea);
        // Adding the tracer collection to the given population optimizer (evolutionary algorithm)
        provider.register(tracer);
        //tracer = new FileTracer(ea, "haea.txt", true);
        //provider.register(tracer);
        
        // running the population optimizer (the evolutionary algorithm)
        ea.apply(function);
        
        tracer.close();
    }

}
