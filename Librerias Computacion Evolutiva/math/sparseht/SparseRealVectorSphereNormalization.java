/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.types.real.array.sparse;

import unalcol.algorithm.Algorithm;
import unalcol.clone.Clone;

/**
 *
 * @author jgomez
 */
public class SparseRealVectorSphereNormalization extends Algorithm<SparseRealVector, SparseRealVector>{
    protected SparseRealVectorDotProduct dot = new SparseRealVectorDotProduct();
    protected SparseRealVectorScalarProduct scale = new SparseRealVectorScalarProduct();
  /**
   * Applies the transformation on the data record
   * @param x Data record to be transformed
   */
  public void fastApply(SparseRealVector x) {
    double d = dot.norm(x);
    scale.fastDivide(x, d);
  }

  /**
   * Applies the transformation on the Data Sample and creates a new DataSample
   * with the transformed version
   * @param x Data sample to be transformed
   * @return Transformed Data Sample (new object)
   */
  @Override
  public SparseRealVector apply( SparseRealVector input ) {
    input = (SparseRealVector)Clone.get(input);
    fastApply(input);
    return input;
  }    
}

