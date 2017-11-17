/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vcHaea;

import unalcol.evolution.GrowingFunction;
import unalcol.evolution.Individual;
import unalcol.optimization.OptimizationFunction;
import unalcol.optimization.operators.Operator;
import unalcol.optimization.solution.Solution;
import unalcol.types.collection.vector.Vector;

/**
 *
 * @author JuanCamilo
 */
public class ParallelHaea<G, P> implements Runnable {

    public ParallelHaea(int ThreadIdX, OptimizationFunction function, 
            Vector<Solution<P>> pop, GrowingFunction<G, P> grow, 
            int maxIter, Operator[] opers,PrintThread pt) {
        this.ThreadIdX = ThreadIdX;
        this.function = function;
        this.pop = pop;
        this.grow = grow;
        this.maxIter = maxIter;
        this.opers = opers;
        currentIter = 0;
        this.pt=pt;
        learingRate = Math.random();//0.36/opers.length;//0.12;//
        Solution.evaluate((Vector) pop, function);
        // Genetic operators
        individualParams = new double[opers.length];
        for (int i = 0; i < individualParams.length; i++) {
            individualParams[i] = 1.0 / individualParams.length;

        }
        mejoras = 0;
        vecindad = 4;//pop.size();
    }

    int ThreadIdX;
    OptimizationFunction function;
    Solution<P> center;
    Vector<Solution<P>> pop;
    GrowingFunction<G, P> grow;
    int maxIter;
    Operator op;
    int currentOper;
    Operator[] opers;
    double individualParams[];
    double pAcumulate[];
    double learingRate;
    int currentIter;
    double fitness;
    int counter;
    int mejoras;
    int vecindad;
    PrintThread pt;

    @Override
    public void run() {
        // Evaluating the fitness of the initial population
        Solution.evaluate((Vector) pop, function);

        // Genetic operators
        individualParams = new double[opers.length];
        for (int i = 0; i < individualParams.length; i++) {
            individualParams[i] = 1.0 / individualParams.length;

        }
        for (int i = 0; i < maxIter; i++) {
            iterar();
        }
        VertexCoverParallelHaea.finish[ThreadIdX] = 0.0;
    }

    public void iterar() {
        center = pop.get(ThreadIdX);
        pAcumulate = individualParams.clone();
        for (int k = 0; k < individualParams.length - 1; k++) {
            pAcumulate[k + 1] += pAcumulate[k];
        }
        Vector<Solution<P>> hijos = aplicarOper(opers, center, pAcumulate);
        double max = (double) function.apply(center.get());
        Solution<P> replace = center;
        for (int j = 0; j < hijos.size(); j++) {
            double fitness = (double) function.apply(hijos.get(j).get());
            if (fitness >= max) {
                max = fitness;
                replace = hijos.get(j);
            }
        }
        if (replace.equals(center)) {
            individualParams = rewardOper(individualParams, currentOper, learingRate, -1);
        } else {
            individualParams = rewardOper(individualParams, currentOper, learingRate, 1);
            mejoras++;
        }
        //replace.evaluate(function);
        fitness = max;
        pop.set(ThreadIdX, replace);
        if (VertexCoverParallelHaea.fitness[ThreadIdX] == Double.MAX_VALUE) {
            VertexCoverParallelHaea.fitness[ThreadIdX] = max;
        }
        counter++;
    }

    private Vector<Solution<P>> aplicarOper(Operator[] opers, Solution<P> center, double params[]) {
        Vector<Solution<P>> childs = new Vector<>();
        int idx = seleccionarOper(params);
        op = opers[idx];
        currentOper = idx;
        Vector<Solution<P>> indi = new Vector<>();
        Vector<G> searchSpace = new Vector<>();
        indi.add(center);
        if (op.getArity() > 1) {
            Vector<G> parents = new Vector<>();
            parents.add((G) center.get());
            parents.add((G) (pop.get(randomIdx()).get()));
            Vector<G> hijos = op.apply(parents);
            for (int i = 0; i < hijos.size(); i++) {
                searchSpace.add(hijos.get(i));
            }

        } else {
            Vector<G> parents = new Vector<>();
            parents.add((G) center.get());
            Vector<G> hijos = op.apply(parents);
            for (int i = 0; i < hijos.size(); i++) {
                searchSpace.add(hijos.get(i));
            }
        }
        for (int i = 0; i < searchSpace.size(); i++) {
            Solution object = new Individual(searchSpace.get(i), grow);
            childs.add(object);
        }
        return childs;
    }

    private int seleccionarOper(double[] opers) {
        double probability = Math.random();
        for (int i = 0; i < opers.length; i++) {
            if (probability <= opers[i]) {
                return i;
            }
        }
        return opers.length - 1;
    }

    public int randomIdx() {
        double rate = 0;
//        if (counter == 10) {
//            rate = mejoras / counter;
//            counter = 0;
//            mejoras = 0;
//            if (rate < 0.2) {
//                vecindad++;
//                if (vecindad >= pop.size()) {
//                    vecindad = pop.size() - 1;
//                }
//            } else if (rate > 0.2) {
//                vecindad--;
//                if (vecindad < 1) {
//                    vecindad = 1;
//                }
//            }
//        }
        int signo = (int) (Math.random() * 2);
        if (signo == 0) {
            signo = -1;
        }
        //return (int) (Math.random() * pop.size());
        return ((int) ((1 + Math.random() * vecindad) * signo) + ThreadIdX + pop.size()) % pop.size();
    }

    private double[] rewardOper(double[] individualParams, int currentOper, double learingRate, int i) {
        double newValues[] = new double[individualParams.length];
        double sum = 0.0;
        double d = learingRate;
        if (i == -1) {
            d = d * -1;
        }
        for (int j = 0; j < newValues.length; j++) {
            if (j == currentOper) {
                newValues[j] = individualParams[j] + d;
            } else {
                newValues[j] = individualParams[j] - d;
            }
            sum += newValues[j];
        }
        for (int j = 0; j < newValues.length; j++) {
            newValues[j] = newValues[j] / (sum);
        }
        return newValues;
    }

}
