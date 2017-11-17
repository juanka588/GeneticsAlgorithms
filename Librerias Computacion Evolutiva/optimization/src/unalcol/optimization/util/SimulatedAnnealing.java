/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.optimization.util;
import unalcol.optimization.generation.PopulationGeneration;
import unalcol.optimization.generation.RandomCandidatesGeneration;
import unalcol.optimization.generation.RandomCandidatesGenerationOverTime;
import unalcol.optimization.transformation.Transformation;
import unalcol.optimization.replacement.*;
import unalcol.optimization.operators.*;
/**
 *
 * @author jgomez
 */
public class SimulatedAnnealing<T> extends Transformation<T>{
    public SimulatedAnnealing( ArityOne<T> mutation ){
        super( new RandomCandidatesGenerationOverTime<T>(mutation) {}, new SimulatedAnnealingReplace<T>());
    }
}

