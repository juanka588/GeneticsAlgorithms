/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.types.real.array.sparse;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 *
 * @author jgomez
 */
public class SparseRealVector{
    protected Hashtable<Integer,Double> values;
    protected int n;
    
    public SparseRealVector(int n){
        this.n = n;
        values = new Hashtable<>();        
    }
    
    public int dim(){
        return n;
    }
    
    public void set(int i, double x){
        values.put(i, x);
    }
    
    public double get( int i ){
        try{
            return values.get(i);
        }catch(Exception e){
            return 0.0;
        }
    }
    
    public boolean nonZero( int i ){
        return values.containsKey(i);
    }
    
    public Enumeration<Integer> nonZeroes(){
        return values.keys();
    }
    
    public Iterator<Double> nonZeroValues(){
        return values.values().iterator();
    }
    
    public Iterator<Entry<Integer,Double>> elements(){
        return values.entrySet().iterator();
    }
}
