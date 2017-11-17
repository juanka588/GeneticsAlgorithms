/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.types.real.array.sparse;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import unalcol.math.algebra.Group;
import unalcol.types.collection.vector.SortedVector;
import unalcol.types.collection.vector.sparse.SparseElement;
import unalcol.types.collection.vector.sparse.SparseElementOrder;
import unalcol.types.collection.vector.sparse.SparseVector;
import unalcol.sort.Order;

/**
 *
 * @author jgomez
 */
public class SparseRealVectorAddition  extends Group<SparseRealVector> {
    public SparseRealVector identity( SparseRealVector x ){
        return new SparseRealVector(x.dim());
    }

    public SparseRealVector fastInverse( SparseRealVector x ){
        Iterator<Map.Entry<Integer,Double>> iter =  x.elements();
        Map.Entry<Integer,Double> elem;
        while( iter.hasNext() ){
            elem = iter.next();
            elem.setValue(-elem.getValue());
        }
        return x;
    }
    
    /**
     * Adds the second vector to the first vector.
     * The addition process is component by component.
     * The result of the operation is stored in the first vector.
     * @param x The first vector
     * @param y The second vector
     */
    public SparseRealVector fastPlus(SparseRealVector x, SparseRealVector y) {
        int c;
        Enumeration<Integer> nz = y.nonZeroes();
        while( nz.hasMoreElements() ){
            c = nz.nextElement();
            if( x.nonZero(c)){
                double d = x.get(c) + y.get(c);
                if( d == 0.0 ){
                    x.values.remove(c);
                }else{
                    x.set(c, d);
                }
            }else{
                x.set(c, y.get(c));
            }
        }
        return x;
    }
        Order order = new SparseElementOrder();

    /**
     * Substracts the ssecond vector from the first vector.
     * The substraction process is component by component.
     * The result of the operation is stored in the first vector.
     * @param x The first vector
     * @param y The second vector
     */
    public SparseRealVector fastMinus(SparseRealVector x, SparseRealVector y) {
        int c;
        Enumeration<Integer> nz = y.nonZeroes();
        while( nz.hasMoreElements() ){
            c = nz.nextElement();
            if( x.nonZero(c)){
                double d = x.get(c) - y.get(c);
                if( d == 0.0 ){
                    x.values.remove(c);
                }else{
                    x.set(c, d);
                }
            }else{
                x.set(c, -y.get(c));
            }
        }
        return x;
    }
}

