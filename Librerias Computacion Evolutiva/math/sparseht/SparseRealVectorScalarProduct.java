/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.types.real.array.sparse;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map.Entry;
import unalcol.math.metric.ScalarProduct;

/**
 *
 * @author jgomez
 */
public class SparseRealVectorScalarProduct extends ScalarProduct<SparseRealVector> {
	/**
	 * Multiplies a vector by an scalar.
	 * @param y The vector
	 * @param x The scalar
	 */
  public SparseRealVector fastMultiply(SparseRealVector y, double x) {
    if( x==0.0 ){
        y.values.clear();      
    }else{  
        Iterator<Entry<Integer,Double>> iter =  y.elements();
        Entry<Integer,Double> elem;
        while( iter.hasNext() ){
            elem = iter.next();
            elem.setValue(elem.getValue()*x);
        }
    }            
    return y;
  }
	/**
	 * Divides a vector by an scalar.
	 * @param one The vector
	 * @param x The scalar
	 */
  public SparseRealVector fastDivide(SparseRealVector y, double x) {
    Iterator<Entry<Integer,Double>> iter =  y.elements();
    Entry<Integer,Double> elem;
    while( iter.hasNext() ){
        elem = iter.next();
        elem.setValue(elem.getValue()/x);
    }
    return y;
  }
}

