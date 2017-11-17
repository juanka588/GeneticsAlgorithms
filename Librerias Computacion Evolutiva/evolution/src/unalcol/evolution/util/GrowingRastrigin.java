/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.evolution.util;

import unalcol.evolution.GrowingFunction;
import unalcol.types.collection.bitarray.BitArray;

/**
 *
 * @author JuanCamilo
 */
public class GrowingRastrigin extends GrowingFunction<BitArray[], double[]> {

    public double[] get(BitArray[] genome) {
        double ret[] = new double[genome.length];
        for (int i = 0; i < genome.length; i++) {
            int a[] = new int[genome[i].size()];
            for (int j = 0; j < a.length; j++) {
                a[j] = genome[i].get(j) ? 1 : 0;
            }
            double sum=0;
            for (int j = 0; j < a.length; j++) {
                sum+=Math.pow(2, a.length-1-j)*a[j];
            }
            ret[i]=(sum-512)/100;
        }
        return ret;
    }

    /**
     * Generates a genome from the given thing
     *
     * @param thing A thing expressed from the genome
     * @return Genome of the thing
     */
    @Override
    public BitArray[] set(double thing[]) {
        boolean[] ans = new boolean[10];
        BitArray ret[] = new BitArray[thing.length];
        for (int i = 0; i < thing.length; i++) {
            int d = (int) (thing[i] * 100)+512;
            for (int j = 0; j < 10; j++) {
                ans[ans.length - 1 - j] = (d >> j) % 2 == 1;
            }
            ret[i] = new BitArray(ans);
            System.out.println(ret[i].toString());
        }
        return ret;
    }
}
