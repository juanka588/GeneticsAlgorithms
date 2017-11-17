package unalcol.evolution.coea;
import unalcol.optimization.Population;
import unalcol.math.logic.*;
import unalcol.optimization.*;
import unalcol.optimization.IterativePopulationOptimizer;
import unalcol.algorithm.iterative.*;
import unalcol.descriptors.DescriptorsProvider;
import unalcol.tracer.*;

/**
 * <p>Title: CoEvolutionaryAlgorithm</p>
 * <p>Description: A class representing a co-evolutionary algorithm</p>
 * <p>Copyright:    Copyright (c) 2010</p>
 * <p>Company: Kunsamu</p>
 * @author Jonatan Gomez
 * @version 1.0
 */
public class CoEvolutionaryAlgorithm extends IterativeAlgorithm<OptimizationFunction[],Population[]> {
  /**
   * The transformation function used to evolve the population
   */
  protected IterativePopulationOptimizer[] niches = null;

  protected Population[] pops;

  /**
   * Constructor: Creates an evolutionary algorithm with the given population,
   * continuation condition and transformation function
   * @param population  The population to evolved
   * @param transformation The transformation operation
   * @param condition  The evolution condition (the evolutionary process is executed
   * until the condition is false)
   */
  public CoEvolutionaryAlgorithm( Predicate condition,
                                  IterativePopulationOptimizer[] niches,
                                  Population[] pops ) {
    super(condition);
    this.niches = niches;
    this.pops = pops;
  }

  /**
   * Initializes the algorithm.
   */
  public void init() {
    super.init();
    int n = niches.length;
    pops = new Population[n];
    for( int i=0; i<n; i++ ){
        niches[i].init();
        pops[i] = niches[i].pop();
    }
  }

  public Population[] nonIterOutput( OptimizationFunction[] f ){
      int n = niches.length;
      for( int i=0; i<n; i++ ){
          if( niches[i].pop() == null || niches[i].pop().size() == 0 ){
              niches[i].init();
          }
          pops[i] = niches[i].pop();
      }
      return pops;
  }

  public Population pop( int k ){
      return pops[k];
  }

  public void updateTrace(){
      // For the time being just for coga
      double[] other;
      double[] best = DescriptorsProvider.descriptors(niches[0]);
      for( int i=1; i<niches.length; i++ ){
          other = DescriptorsProvider.descriptors(niches[i]);
          if( other[3] > best[3] ){
              best = other;
          }
      }
      TracerProvider.trace( this, best );      
  }

  /**
   * An evolutionary algorithm iteration
   */
  public Population[] iteration( int k, OptimizationFunction[] input, Population[] output ) {
    int n = input.length;
    for( int i=0; i<n; i++ ){
        output[i] = niches[i].iteration(k, input[i], output[i] );
    }
    updateTrace();
//    for( int i=0; i<n; i++ ){
//        input[i] = output[i];
//        addToTrace(new CoEAStatistics(i, k+1, input[i], transformation[i]));
//    }
//    addToTrace( new EAStatistics( k+1, input[0], transformation[0] ) );
    return output;
  }
}
