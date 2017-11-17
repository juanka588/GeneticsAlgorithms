/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package glovito;

import unalcol.evolution.GrowingFunction;
import unalcol.types.collection.bitarray.BitArray;

/**
 *
 * @author Jonatan
 */
public class GrowGlovito extends GrowingFunction<BitArray, Glovito>{
  /**
   * Generates a thing from the given genome
   * @param genome Genome of the thing to be expressed
   * @return A thing expressed from the genome
   */
  public Glovito get(BitArray genome) {
      int s = genome.size()/3;
      int[][] t = new int[s/2][2];
      int[][] o = new int[s/2][2];
      int k = 0;
      for( int i=0; i<t.length; i++ ){
        for( int j=0; j<t[0].length; j++ ){
            int h = genome.get(k)?2:0;
            k++;
            int l = genome.get(k)?1:0;
            t[i][j] = h + l;
            k++;
            o[i][j] = genome.get(k)?1:0;
            k++;
        }
      }
      return new Glovito(t, o); 
  }

  /**
   * Generates a genome from the given thing
   * @param thing A thing expressed from the genome
   * @return Genome of the thing
   */
  @Override
  public BitArray set(Glovito thing) {
      int size = thing.T.length*6;
      BitArray ans = new BitArray(size, true);
      int k = 0;
      for(int i = 0; i < thing.T.length; i++){
        for(int j = 0; j < thing.T[i].length; j++){
            ans.set(k, thing.T[i][j] / 2 > 0);
            k++;
            ans.set(k, thing.T[i][j] % 2 != 0);
            k++;
            ans.set(k, thing.out[i][j] == 1);
            k++;
        }
      }
      
      return ans; 
  }
    
}
