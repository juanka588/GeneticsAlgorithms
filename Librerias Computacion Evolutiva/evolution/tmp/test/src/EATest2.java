//PlugIns
import unalcol.optimization.Population;
import unalcol.optimization.transformation.Transformation;
import unalcol.optimization.PopulationOptimizer;
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
import unalcol.evolution.provider.*;
import unalcol.types.collection.bitarray.*;
import unalcol.optimization.testbed.binary.*;
import unalcol.evolution.haea.*;

//Additional classes
import unalcol.math.logic.*;
import unalcol.algorithm.iterative.*;
import unalcol.optimization.IterativePopulationOptimizer;
import unalcol.types.real.array.*;

/**
 * <p>Title: EATest2</p>
 * <p>Description: Test of Ea</p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: Kunsamu</p>
 * @author Jonatan Gomez
 * @version 1.0
 */
public class EATest2 {
    public static Operator get( Operator[] operators, String name ){
        int i=0;
        while(i<operators.length && !operators[i].getClass().getName().equals(name)){
            i++;
        }
        if( i<operators.length )  return operators[i];
        return null;
    }

    public static Operator[] get(){
        // Genetic operators
        Operator[] opers = OperatorProvider.get(BitArray.class);
        String[] operName = new String[]{
            "unalcol.evolution.genotype.binary.operators.XOver",
            "unalcol.evolution.genotype.binary.operators.Mutation",
            "unalcol.evolution.genotype.binary.operators.Transposition"};
        Operator[] operators = new Operator[operName.length];
        for( int i=0; i<operName.length; i++ ){
            operators[i] = get( opers, operName[i] );
        }
        return operators;
    }

    public static Transformation getEA(){
        Class[] eas = ReflectUtil.getProvider().available_service_classes(Transformation.class);
//        EvolutionaryTransformation[] eas = EAProvider.get();
        for( int i=0; i<eas.length; i++ ){
//            System.out.println( eas[i].getClass().getCanonicalName() );
            System.out.println( eas[i].getCanonicalName() );
        }
        return null; //eas[0];
    }

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
        Genotype genotype = GenotypeProvider.getService( BitArray.class );

        int CHROMOSOMESIZE = 100;
        genotype.setSize(CHROMOSOMESIZE);

        GrowingFunction grow = new GrowingFunction();

        // Initial population
        int POPSIZE = 100;
        Population pop = grow.build( genotype.build(POPSIZE) );

        // Evaluating the fitness of the initial population
        pop.evaluate(function);

        // Genetic operators
        HaeaOperators haeaOperators = new SimpleHaeaOperators(get());

        // Extra parent selection mechanism
        Selection selection = new Tournament(4);

        getEA();

        // Haea transformation
        Transformation transformation = new HAEA(haeaOperators, grow, selection);

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
        tracer = new FileTracer(ea, "haea.txt", true);
        provider.register(tracer);

        // running the population optimizer (the evolutionary algorithm)
        ea.apply(function);

        tracer.close();
    }
}