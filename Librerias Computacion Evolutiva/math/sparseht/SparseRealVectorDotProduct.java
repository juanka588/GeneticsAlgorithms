/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.types.real.array.sparse;

import java.util.Enumeration;
import java.util.Iterator;

/**
 *
 * @author jgomez
 */
public class SparseRealVectorDotProduct {
    public double apply(SparseRealVector x, SparseRealVector y){
        double s = 0.0;
        int c;
        Enumeration<Integer> nz = x.nonZeroes();
        while( nz.hasMoreElements() ){
            c = nz.nextElement();
            if( y.nonZero(c) ){
                s += x.get(c) * y.get(c);
            }
        }
        return s;
    }
    
    public double sqr_norm(SparseRealVector x){
        double s = 0.0;
        double c;
        Iterator<Double> iter = x.nonZeroValues();
        while( iter.hasNext() ){
            c = iter.next();
            s += c*c;
        }
        return s;
    }
    
    public double norm(SparseRealVector x){
        return Math.sqrt(sqr_norm(x));
    }
}
