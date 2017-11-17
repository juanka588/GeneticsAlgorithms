

//Services
import unalcol.optimization.Population;
import unalcol.optimization.transformation.Transformation;
import unalcol.optimization.PopulationOptimizer;
import unalcol.optimization.operators.real.OneDimensionGaussianMutation;
import unalcol.optimization.operators.real.SimpleXOver;
import unalcol.optimization.operators.real.GaussianMutation;
import unalcol.optimization.operators.real.UniformMutation;
import unalcol.optimization.operators.real.RTransposition;
import unalcol.optimization.operators.binary.Transposition;
import unalcol.optimization.operators.binary.SingleBitMutation;
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
import unalcol.optimization.testbed.real.*;
import unalcol.evolution.haea.*;
import unalcol.evolution.genotype.binary.*;
import unalcol.evolution.genotype.binary.util.*;
import unalcol.evolution.genotype.real.util.*;
import unalcol.evolution.genotype.real.*;


//Additional classes
import unalcol.math.logic.*;
import unalcol.algorithm.iterative.*;
import unalcol.optimization.IterativePopulationOptimizer;
import unalcol.types.real.array.*;

import lsgo_benchmark.*;

/**
 * <p>Title: HAEATest</p>
 * <p>Description: Test of Hybrid Adaptive Genetic Algorithm</p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: Kunsamu</p>
 * @author Jonatan Gomez
 * @version 1.0
 */
public class HAEATest{
    public static void main( String[] args ){
        // Reflection
        ServiceProvider provider = ReflectUtil.getProvider("services/");
        // Array persistency methods (writes the array as a line of real values separated by comma character
        DoubleArraySimpleWriteService key = new DoubleArraySimpleWriteService(',');
        provider.register(key);
        provider.setDefault_service(WriteService.class,double[].class,key);

        // Function being optimized
        // OptimizationFunction function = new MaxOnes();
        // OptimizationFunction function = new Rastrigin();
        Function lsgo_f = new F1();        
        int CHROMOSOMESIZE = lsgo_f.getDimension();
        double[] min = DoubleArrayInit.create(CHROMOSOMESIZE, lsgo_f.getMin());
        double[] max = DoubleArrayInit.create(CHROMOSOMESIZE, lsgo_f.getMax());
        OptimizationFunction function = new LSGOFunctionWrap(lsgo_f,10000);
        
        Genotype genotype;
        GrowingFunction grow;
       
        boolean binary = false;
        // Binary Encoding
        if( binary ){
            // Encoding decoding scheme
            int GENESIZE = 32;
            genotype = new BinaryGenotype();

            genotype.setSize(CHROMOSOMESIZE*GENESIZE);
            grow = new GrowingBinaryToRealVector(GENESIZE, min, max );
        }else{
        //Real Encoding
            genotype = new RealVectorGenotype(CHROMOSOMESIZE);
            grow = new ScaleRealVectorGrowingFunction(min, max);
        }
        
        // Initial population
        int POPSIZE = 1;
        Population pop = grow.build( genotype.build(POPSIZE) );

        // Evaluating the fitness of the initial population
        pop.evaluate(function);

        // Genetic operators
        Operator[] opers;
        if( binary ){
            XOver xover = new XOver();
            SingleBitMutation sb_mutation = new SingleBitMutation();
            Mutation mutation = new Mutation();
            Transposition transposition = new Transposition();

            //HaeaOperators
            opers = new Operator[]{sb_mutation, transposition};            
        }else{
            SimpleXOver xover = new SimpleXOver();
            //OneDimensionLinearXOver xover = new OneDimensionLinearXOver();
            //FlipMutation flip = new FlipMutation();
            GaussianMutation gauss0 = 
                    new GaussianMutation(DoubleArrayInit.create(CHROMOSOMESIZE, 0.01));
            GaussianMutation gauss1 = 
                    new GaussianMutation(DoubleArrayInit.create(CHROMOSOMESIZE, 0.1));
            OneDimensionGaussianMutation gauss2 = 
                    new OneDimensionGaussianMutation(DoubleArrayInit.create(CHROMOSOMESIZE, 0.01));
            OneDimensionGaussianMutation gauss3 = 
                    new OneDimensionGaussianMutation(DoubleArrayInit.create(CHROMOSOMESIZE, 0.1));
            UniformMutation unif = new UniformMutation();
            RTransposition tr = new RTransposition();
            //HaeaOperators
            //opers = new Operator[]{gauss1, tr};            
            opers = new Operator[]{ gauss0, gauss1, gauss2, gauss3};            
            // opers = new Operator[]{ gauss2, gauss3};            
        }
            
        //Operator[] opers = new Operator[]{mutation, transposition};
        HaeaOperators haeaOperators = new SimpleHaeaOperators(opers);

        // Extra parent selection mechanism
        Selection selection = new Tournament(4);

        // Genetic Algorithm Transformation
        Transformation transformation = new HAEA(haeaOperators, grow, selection );

        // Evolution generations
        int MAXITER = 1000 / POPSIZE;
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
        pop = (Population)ea.apply(function);
        for( int i=0; i<pop.size(); i++ ){
            Individual ind = (Individual)pop.get(i);
            System.out.println(ind.evaluate(function));
        }
        
        TracerProvider.close(ea);
    }
}