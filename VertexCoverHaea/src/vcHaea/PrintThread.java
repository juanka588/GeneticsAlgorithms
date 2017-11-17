/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vcHaea;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import unalcol.optimization.OptimizationFunction;
import unalcol.optimization.solution.Solution;
import unalcol.types.collection.bitarray.BitArray;
import unalcol.types.collection.vector.Vector;
import static vcHaea.VertexCoverHaea.readFromFile;

/**
 *
 * @author JuanCamilo
 */
public class PrintThread implements Runnable {

    public static double[] finish;
    public static double[] fitness;
    Solution best;

    public PrintThread(Vector<Solution<BitArray>> pop, OptimizationFunction function) {
        this.pop = pop;
        this.function = function;
    }
    int generation = 0;
    Vector<Solution<BitArray>> pop;
    OptimizationFunction function;

    @Override
    public void run() {
        boolean cond = true, cond2 = true;
        while (cond) {
            while (cond2 && cond) {
                cond2 = !print(VertexCoverParallelHaea.fitness);
                cond = !finish(VertexCoverParallelHaea.finish);
            }
            estadisticasPop(VertexCoverParallelHaea.fitness);
            cond = !finish(VertexCoverParallelHaea.finish);
            cond2 = true;
        }
        for (int i = 0; i < pop.size(); i++) {
            System.out.println(pop.get(i).evaluate(function));
        }
        System.out.println("todos acabaron");
        try {
            VertexCoverParallelHaea.guardarIndividuo((BitArray) getBest().get());
            System.out.println((BitArray) getBest().get());
            function = new VertexCoverFitness(readFromFile(VertexCoverParallelHaea.file));
            System.out.println("Real Fitness sol: " + VertexCoverFitness.realFit((BitArray) getBest().get()));
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }

    private boolean finish(double finish[]) {
        double sum = 0.0;
        for (int i = 0; i < finish.length; i++) {
            sum += finish[i];

        }
        return sum == 0.0;
    }

    private boolean print(double fitness[]) {
        for (int i = 0; i < fitness.length; i++) {
            if (fitness[i] == Double.MAX_VALUE) {
                return false;
            }
        }
        return true;
    }

    private void estadisticasPop(double fitness[]) {
        double max = Double.NEGATIVE_INFINITY, min = Double.POSITIVE_INFINITY, avg = 0, des = 0;
        int idx = 0;
        for (int i = 0; i < fitness.length; i++) {
            if (fitness[i] > max) {
                max = fitness[i];
                idx = i;
            }
            if (fitness[i] < min) {
                min = fitness[i];
            }
            avg += fitness[i];
        }
        avg = avg / fitness.length;
        for (int i = 0; i < fitness.length; i++) {
            des += Math.pow((fitness[i] - avg), 2);
        }
        des = des / (fitness.length - 1);
        des = Math.pow(des, 0.5);
        String out = (generation + 1) + "\t" + max + "\t" + min + "\t" + avg + "\t" + des;
        System.out.println(out.replace('.', ','));
        generation++;
        for (int i = 0; i < VertexCoverParallelHaea.fitness.length; i++) {
            VertexCoverParallelHaea.fitness[i] = Double.MAX_VALUE;
        }
        best = pop.get(idx);
    }

    public Solution getBest() {
        return best;
    }
}
