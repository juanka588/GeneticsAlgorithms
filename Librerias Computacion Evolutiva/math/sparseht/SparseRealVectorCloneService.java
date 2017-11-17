/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.types.real.array.sparse;

import java.util.Enumeration;
import unalcol.clone.Clone;
import unalcol.clone.CloneService;
import unalcol.types.collection.vector.sparse.SparseVector;

/**
 *
 * @author jgomez
 */
public class SparseRealVectorCloneService  
     implements CloneService<SparseRealVector>{
    public SparseRealVectorCloneService() {
    }

    public Object owner(){
        return SparseRealVector.class;
    }

    /**
     * Clones a Java Vector
     * @param obj The Java Vector to be cloned
     * @return A clone of the Java Vector
     */
    @Override
    public SparseRealVector clone(SparseRealVector obj){    
        int i;
        SparseRealVector x = new SparseRealVector( obj.dim() );
        Enumeration<Integer> iter = obj.nonZeroes();
        while( iter.hasMoreElements() ){
            i = iter.nextElement();
            x.set(i, obj.get(i));
        }
        return x;
    }    
}