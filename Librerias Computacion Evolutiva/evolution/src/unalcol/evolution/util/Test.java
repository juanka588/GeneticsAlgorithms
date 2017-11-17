/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.evolution.util;

import sun.misc.FloatingDecimal;
import unalcol.types.collection.bitarray.BitArray;

/**
 *
 * @author JuanCamilo
 */
public class Test {

    public static void main(String[] args) {
        GrowingRastrigin gr = new GrowingRastrigin();
        gr.set(new double[]{0.198787});
        System.out.println(Integer.toBinaryString(20));
        System.out.println(Integer.parseInt("1000001000", 2));
        boolean a[] = new boolean[]{true, false, false, false};
        BitArray el = new BitArray(a);
        System.out.println(gr.get(new BitArray[]{el})[0]);
        gr.set(gr.get(new BitArray[]{el}));
    }
}
