/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package glovito;

import unalcol.optimization.OptimizationFunction;

/**
 *
 * @author Jonatan
 */
public class FitnessGlovito extends OptimizationFunction<Glovito>{
    protected int[] env;
    public FitnessGlovito( int[] _env ){
        env = _env;
    }
    
    public Double apply( Glovito g ){
        double f = 0.0;
        int[] out = g.simulate(env);
        for( int i=0; i<out.length; i++){
            if(out[i]==env[i+1]){
                f++;
            }
        }
        return f;
    }
}
