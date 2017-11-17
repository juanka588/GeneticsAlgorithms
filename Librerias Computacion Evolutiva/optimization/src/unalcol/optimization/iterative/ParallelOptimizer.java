/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.optimization.iterative;

import unalcol.algorithm.iterative.IterativeAlgorithm;
import unalcol.math.logic.Predicate;
import unalcol.optimization.OptimizationFunction;
import unalcol.optimization.Optimizer;
import unalcol.optimization.solution.Solution;
import unalcol.optimization.solution.SolutionTransformation;

/**
 *
 * @author JuanCamilo
 */
public class ParallelOptimizer<S>
        extends IterativeAlgorithm<OptimizationFunction<S>, Solution<S>>
        implements Optimizer<S> {

    protected SolutionTransformation<S> transformation;

    /**
     * Constructor: Creates an evolutionary algorithm with the given population,
     * continuation condition and transformation function
     *
     * @param population The population to evolved
     * @param transformation The transformation operation
     * @param condition The evolution condition (the evolutionary process is
     * executed until the condition is false)
     */
    public ParallelOptimizer(Predicate condition,
            SolutionTransformation<S> transformation,
            Solution<S> solution) {
        super(condition);
        this.transformation = transformation;
        this.output = solution;
    }

    /**
     * Initializes the algorithm.
     */
    public void init() {
        super.init();
        if (transformation != null) {
            transformation.init();
        }
    }

    @Override
    public Solution<S> nonIterOutput(OptimizationFunction<S> i) {
        ParallelIndividual pi;
        Thread t;
        pi=new ParallelIndividual();
        t=new Thread(pi);
        t.start();
        return null;
    }

    @Override
    public Solution<S> iteration(int i, OptimizationFunction<S> i1, Solution<S> o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected class ParallelIndividual implements Runnable {

        protected int index;
        Solution<S> population;

        @Override
        public void run() {

        }

    }
}
