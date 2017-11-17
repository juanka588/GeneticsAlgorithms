//Services
import unalcol.optimization.Population;
import unalcol.optimization.transformation.Transformation;
import unalcol.optimization.PopulationOptimizer;
import unalcol.optimization.operators.binary.Transposition;
import unalcol.optimization.operators.binary.Mutation;
import unalcol.optimization.operators.binary.XOver;
import unalcol.optimization.selection.Selection;
import unalcol.optimization.selection.Tournament;
import unalcol.optimization.operators.Operator;
import unalcol.reflect.service.*;
import unalcol.reflect.util.*;
import unalcol.tracer.*;
import unalcol.io.*;

//Optimization classes
import unalcol.optimization.*;

//Evolution classes
import unalcol.evolution.*;
import unalcol.optimization.testbed.binary.*;
import unalcol.evolution.haea.*;
import unalcol.evolution.genotype.binary.*;


//Additional classes
import unalcol.math.logic.*;
import unalcol.algorithm.iterative.*;
import unalcol.optimization.IterativePopulationOptimizer;
import unalcol.types.real.array.*;

/**
 * <p>Title: HAEATest</p>
 * <p>Description: Test of Hybrid Adaptive Genetic Algorithm</p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: Kunsamu</p>
 * @author Jonatan Gomez
 * @version 1.0
 */
public class CAHAEATest{
    public static void main( String[] args ){
        // Reflection
        ServiceProvider provider = ReflectUtil.getProvider("services/");
        // Array persistency methods (writes the array as a line of real values separated by comma character
        DoubleArraySimpleWriteService key = new DoubleArraySimpleWriteService(',');
        provider.register(key);
        provider.setDefault_service(WriteService.class,double[].class,key);

        // Function being optimized
        OptimizationFunction function = new MaxOnes();

        // Encoding decoding scheme
        Genotype genotype = new BinaryGenotype();

        int CHROMOSOMESIZE = 100;
        genotype.setSize(CHROMOSOMESIZE);

        GrowingFunction grow = new GrowingFunction();

        // Initial population
        int POPSIZE = 100;
        Population pop = grow.build( genotype.build(POPSIZE) );

        // Evaluating the fitness of the initial population
        pop.evaluate(function);

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
        Transformation transformation = new CAHAEA(haeaOperators, grow, selection );

        // Evolution generations
        int MAXITER = 100;
        Predicate condition = new ForLoopCondition(MAXITER);

        // Evolutionary algorithm (is a population optimizer)
        PopulationOptimizer ea = new IterativePopulationOptimizer(condition,
                transformation, pop);

        // A console set tracer
        Tracer tracer = new ConsoleTracer(ea);
        // Adding the tracer collection to the given population optimizer (evolutionary algorithm)
        provider.register(tracer);
        tracer = new FileTracer(ea, "cahaea.txt", true);
        provider.register(tracer);

        // running the population optimizer (the evolutionary algorithm)
        ea.apply(function);

        tracer.close();
    }
}