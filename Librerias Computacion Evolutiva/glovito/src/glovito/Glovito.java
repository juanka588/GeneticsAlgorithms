/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package glovito;

/**
 *
 * @author Jonatan
 */
public class Glovito {
    int[][] T;
    int[][] out;
    
    public Glovito( int[][] _T, int[][] _out){
      T = _T;
      out = _out;
    }
    
    public int[] simulate(int[] in ){
        int state = 0;
        int[] out_x = new int[in.length-1];
        for(int i=0; i<out_x.length; i++){
            out_x[i] = out[state][in[i]];
            state = T[state][in[i]];
        }
        return out_x;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
}
