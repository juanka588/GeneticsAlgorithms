/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.evolution.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;
import unalcol.algorithm.iterative.ForLoopCondition;
import unalcol.evolution.GrowingFunction;
import unalcol.evolution.Individual;
import unalcol.evolution.IndividualInstance;
import unalcol.evolution.ga.GeneticAlgorithm;
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
import unalcol.optimization.operators.real.GeneXOver;
import unalcol.optimization.operators.real.SimpleXOver;
import unalcol.optimization.replacement.Generational;
import unalcol.optimization.replacement.SteadyState;
import unalcol.optimization.selection.Elitism;
import unalcol.optimization.selection.IdSelection;
import unalcol.optimization.selection.Selection;
import unalcol.optimization.selection.Tournament;
import unalcol.optimization.selection.Uniform;
import unalcol.optimization.solution.Solution;
import unalcol.optimization.solution.SolutionInstance;
import unalcol.optimization.testbed.binary.MaxOnes;
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
public class BitArrayTest {

    public static void main(String[] args) {
        int experiments = 50;
        int BITARRAYLENGTH = 100;
        int POPSIZE = 100;
        int MAXITER = 200;
        double px = 0.06, pm = 0.05;
        double mat[][] = new double[50][4];
        String Pmuta=pm==1.0/POPSIZE?"1/n":pm+"" ;
        System.out.println("Params: \npx:" + px
                + "\tpm:" + Pmuta + "\tPsize:" + POPSIZE);
        for (int j = 0; j < experiments; j++) {
            // Reflection
            ServiceProvider provider = ReflectUtil.getProvider("services/");

            // Search Space 
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
            OptimizationFunction function = new MaxOnes();
            // Evaluating the fitness of the initial population
            Solution.evaluate((Vector) pop, function);

            ArityTwo xover = new XOver();
            ArityOne mutation = new Mutation();
            ArityOne transposition = new Transposition();

            // Genetic operators
            Operator[] opers = new Operator[]{mutation, transposition, xover};
       // HaeaOperators haeaOperators = new SimpleHaeaOperators(opers);

            // Extra parent selection mechanism
            Selection selection = new Tournament(4);

            // Genetic Algorithm Transformation
            Transformation transformation = new GeneticAlgorithm(selection,
                    grow, mutation, xover, pm, new Generational<BitArray>());
            // Evolution generations
            Predicate condition = new ForLoopCondition(MAXITER);

            // Evolutionary algorithm (is a population optimizer)
            PopulationOptimizer ea = new IterativePopulationOptimizer(condition,
                    transformation, pop);

            boolean tracing = false;
            //System.out.println("datos #it idx best worst ave. des.");
            if (tracing) {
                // A console set tracer
                Tracer tracer = new ConsoleTracer(ea);
                // Adding the tracer collection to the given population optimizer (evolutionary algorithm)
                provider.register(tracer);
                tracer = new FileTracer(ea, "haea.txt", true);
                provider.register(tracer);
            }

            try {
                // running the population optimizer (the evolutionary algorithm)
                pop = (Vector<Solution<BitArray>>) ea.apply(function);
                double max = Double.NEGATIVE_INFINITY, min = Double.POSITIVE_INFINITY, avg = 0, des = 0;
                for (int i = 0; i < pop.size(); i++) {
                    if ((double) function.apply(pop.get(i).get()) > max) {
                        max = (double) function.apply(pop.get(i).get());
                    }
                    if ((double) function.apply(pop.get(i).get()) < min) {
                        min = (double) function.apply(pop.get(i).get());
                    }
                    avg += (double) function.apply(pop.get(i).get());
                }
                avg = avg / pop.size();
                for (int i = 0; i < pop.size(); i++) {
                    des += Math.pow(((double) function.apply(pop.get(i).get()) - avg), 2);
                }
                des = des / (pop.size() - 1);
                des = Math.pow(des, 0.5);
                String out = (j + 1) + "\t" + max + "\t" + min + "\t" + avg + "\t" + des;
                //System.out.println(out.replace('.', ','));
                TracerProvider.close(ea);
                mat[j][0] = max;
                mat[j][1] = min;
                mat[j][2] = avg;
                mat[j][3] = des;
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        double mMax = Double.NEGATIVE_INFINITY, minMax = Double.POSITIVE_INFINITY, avgMax = 0, desMax = 0;
        double mMin = Double.NEGATIVE_INFINITY, minMin = Double.POSITIVE_INFINITY, avgMin = 0, desMin = 0;
        double mAvg = Double.NEGATIVE_INFINITY, minAvg = Double.POSITIVE_INFINITY, avgAvg = 0, desAvg = 0;
        double mDes = Double.NEGATIVE_INFINITY, minDes = Double.POSITIVE_INFINITY, avgDes = 0, desDes = 0;
        for (int i = 0; i < mat.length; i++) {
            if (mat[i][0] > mMax) {
                mMax = mat[i][0];
            }
            if (mat[i][0] < minMax) {
                minMax = mat[i][0];
            }
            if (mat[i][1] > mMin) {
                mMin = mat[i][1];
            }
            if (mat[i][1] < minMin) {
                minMin = mat[i][1];
            }
            if (mat[i][2] > mAvg) {
                mAvg = mat[i][2];
            }
            if (mat[i][2] < minAvg) {
                minAvg = mat[i][2];
            }
            if (mat[i][3] > mDes) {
                mDes = mat[i][3];
            }
            if (mat[i][3] < minDes) {
                minDes = mat[i][3];
            }
            avgMax += mat[i][0];
            avgMin += mat[i][1];
            avgAvg += mat[i][2];
            avgDes += mat[i][3];
        }
        avgMax = avgMax / mat.length;
        avgMin = avgMin / mat.length;
        avgAvg = avgAvg / mat.length;
        avgDes = avgDes / mat.length;
        for (int i = 0; i < mat.length; i++) {
            desMax += Math.pow(mat[i][0] - avgMax, 2);
            desMin += Math.pow(mat[i][1] - avgMin, 2);
            desAvg += Math.pow(mat[i][2] - avgAvg, 2);
            desDes += Math.pow(mat[i][3] - avgDes, 2);
        }
        desMax = desMax / (mat.length - 1);
        desMin = desMin / (mat.length - 1);
        desAvg = desAvg / (mat.length - 1);
        desDes = desDes / (mat.length - 1);
        desMax = Math.pow(desMax, 0.5);
        desMin = Math.pow(desMin, 0.5);
        desAvg = Math.pow(desAvg, 0.5);
        desDes = Math.pow(desDes, 0.5);
        System.out.println("valores\tmaximo\tminimo\tpromedio\tdesviacion");
        String out = "MAX\t" + mMax + "\t" + minMax + "\t" + avgMax + "\t" + desMax;
        String out2 = "MIN\t" + mMin + "\t" + minMin + "\t" + avgMin + "\t" + desMin;
        String out3 = "AVG\t" + mAvg + "\t" + minAvg + "\t" + avgAvg + "\t" + desAvg;
        String out4 = "DES\t" + mDes + "\t" + minDes + "\t" + avgDes + "\t" + desDes;
        out = out.replace('.', ',');
        out2 = out2.replace('.', ',');
        out3 = out3.replace('.', ',');
        out4 = out4.replace('.', ',');
        System.out.println(out + "\n" + out2 + "\n" + out3 + "\n" + out4 + "\n");
    }
}
