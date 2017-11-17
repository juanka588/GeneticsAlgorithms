/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vcHaea;

import java.io.IOException;
import java.util.ArrayList;
import unalcol.optimization.operators.ArityOne;
import unalcol.types.collection.bitarray.BitArray;
import unalcol.types.collection.vector.Vector;
import static vcHaea.VertexCoverHaea.readFromFileAR;

/**
 *
 * @author JuanCamilo
 */
public class VertexOPT extends ArityOne<BitArray> {

    static ArrayList<ADrow> arisMat;

    public VertexOPT(ArrayList<ADrow> readFromFileAR) {
        arisMat = readFromFileAR;
    }

    @Override
    public Vector<BitArray> generates(BitArray g) {
//        System.out.println(g.toString());
        Vector<BitArray> mutated = new Vector<>();
        int idx = (int) (Math.random() * g.size());
//         System.out.println("idx "+(idx+1));
        BitArray opt = (BitArray) g.clone();
        BitArray opt2=(BitArray) g.clone();
        ArrayList line = arisMat.get(idx).row;
        for (int i = 0; i < line.size(); i++) {
            int x = (int) line.get(i);
            opt.set(x, true);
            opt2.set(x, false);
        }
        opt.set(idx, false);
        opt2.set(idx,true);
        //System.out.println(opt.toString());
        mutated.add(opt);
        mutated.add(opt2);
        return mutated;
    }

    @Override
    public Object owner() {
        return BitArray.class;
    }

    public static void main(String[] args) throws IOException {
        String file = "./frb7-15-1.mis";
        VertexOPT op = new VertexOPT(readFromFileAR(file));
        Vector<BitArray> mut=op.generates(new BitArray(7, true));
        System.out.println(mut.get(0));
        System.out.println(mut.get(1));
    }
}
