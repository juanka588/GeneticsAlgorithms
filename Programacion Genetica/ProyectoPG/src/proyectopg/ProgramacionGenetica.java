/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectopg;

import unalcol.algorithm.iterative.ForLoopCondition;
import unalcol.evolution.GrowingFunction;
import unalcol.evolution.Individual;
import unalcol.evolution.IndividualInstance;
import unalcol.evolution.haea.CAHAEA;
import unalcol.evolution.haea.HAEA;
import unalcol.evolution.haea.HaeaOperators;
import unalcol.evolution.haea.SimpleHaeaOperators;
import unalcol.instance.InstanceProvider;
import unalcol.instance.InstanceService;
import unalcol.io.WriteService;
import unalcol.math.logic.Predicate;
import unalcol.optimization.OptimizationFunction;
import unalcol.optimization.PopulationOptimizer;
import unalcol.optimization.iterative.IterativePopulationOptimizer;
import unalcol.optimization.operators.ArityOne;
import unalcol.optimization.operators.ArityTwo;
import unalcol.optimization.operators.Operator;
import unalcol.optimization.selection.Selection;
import unalcol.optimization.selection.Tournament;
import unalcol.optimization.solution.Solution;
import unalcol.optimization.solution.SolutionInstance;
import unalcol.optimization.transformation.Transformation;
import unalcol.reflect.service.ServiceProvider;
import unalcol.reflect.util.ReflectUtil;
import unalcol.tracer.ConsoleTracer;
import unalcol.tracer.FileTracer;
import unalcol.tracer.Tracer;
import unalcol.tracer.TracerProvider;
import unalcol.types.collection.vector.Vector;
import unalcol.types.integer.array.IntArraySimplePersistent;
import unalcol.types.real.array.DoubleArraySimpleWriteService;

/**
 *
 * @author JuanCamilo
 */
public class ProgramacionGenetica {

    public static void main(String[] args) {

        // Reflection
        ServiceProvider provider = ReflectUtil.getProvider("services/");
        DoubleArraySimpleWriteService key = new DoubleArraySimpleWriteService(',');
        provider.register(key);
        provider.setDefault_service(WriteService.class, double[].class, key);

        IntArraySimplePersistent iskey = new IntArraySimplePersistent(',');
        provider.register(iskey);
        provider.setDefault_service(WriteService.class, int[].class, iskey);

        // Search Space 
        InstanceService ikey = new ProgramaInstance();
        provider.register(ikey);
        provider.setDefault_service(InstanceService.class, Programa.class, ikey);

        Solution<Programa> p = null;
        int rep = 10;
        int POPSIZE = 100;
        int maxLenght = 4;
        int MAXITER = 200;
        // Solution Space
        Programa x = new Programa();
        Solution<Programa> solution = new Individual<>(x, x);
        GrowingFunction<Programa, Programa> grow = new GrowingFunction();
        SolutionInstance skey = new IndividualInstance(grow);
        provider.register(skey);
        provider.setDefault_service(InstanceService.class, Solution.class, skey);
        Vector<Solution<Programa>> pop = InstanceProvider.get(solution, POPSIZE);
        // Function being optimized
        OptimizationFunction function = new EvaluarPrograma();

        // Evaluating the fitness of the initial population
        Solution.evaluate((Vector) pop, function);

        ArityOne mutation = new MutationPrograma(0.0, maxLenght);
        ArityTwo xover = new XOverProgram(maxLenght);
        ArityTwo switchP = new SwitchProgram();
        ArityOne swap = new SwapProgram();
        ArityOne cut = new CutProgram();

        // Genetic operators
        Operator[] opers = new Operator[]{mutation, xover, switchP, swap, cut};

        for (int k = 0; k < rep; k++) {

            int MAXITER2 = MAXITER;
            if (k % 2 != 0) {
                opers = new Operator[]{mutation, xover, switchP, swap, cut};
                MAXITER2 = MAXITER2 / 2;
            } else {
                opers = new Operator[]{mutation, switchP, swap, cut};
                pop = InstanceProvider.get(solution, POPSIZE);
            }
            HaeaOperators haeaOperators = new SimpleHaeaOperators(opers);
            // Initial population
            if (k > 0) {
                pop.set(k, p);
                if (p.value() == p.get().examples.length) {
                    System.out.println("Finalizado con exito");
                    System.out.println(p.get());
                    return;
                }
                System.out.println(k + "-esimo Cambio pop\n_____________________________________________");
            }

            // Extra parent selection mechanism
            Selection selection = new Tournament(4);

            // Genetic Algorithm Transformation
            Transformation transformation = new HAEA(haeaOperators, grow, selection);

            // Evolution generations
            Predicate condition = new ForLoopCondition(MAXITER2);

            // Evolutionary algorithm (is a population optimizer)
            PopulationOptimizer ea = new IterativePopulationOptimizer(condition,
                    transformation, pop);

            boolean tracing = true;
            if (tracing) {
                // A console set tracer
                Tracer tracer = new ConsoleTracer(ea);
                // Adding the tracer collection to the given population optimizer (evolutionary algorithm)
                provider.register(tracer);
                tracer = new FileTracer(ea, "haea-q.txt", true);
                provider.register(tracer);
            }

            int idx = 0;
            p = pop.get(0);
            pop = (Vector<Solution<Programa>>) ea.apply(function);
            for (int i = 0; i < POPSIZE; i++) {
                if (pop.get(i).value() > p.value()) {
                    idx = i;
                    p = pop.get(i);
                }
//            System.out.println(pop.get(i).get());
//            System.out.println("__________________________________________________________");        
            }
            System.out.println("Best:\n" + p.get().toProgram());
            pop = InstanceProvider.get(solution, POPSIZE);
            pop.set(0, p);
            TracerProvider.close(ea);
        }
    }
}
