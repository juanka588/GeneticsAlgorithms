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
public class VertexOPT2 extends ArityOne<BitArray> {

    static ArrayList<ADrow> arisMat;

    public VertexOPT2(ArrayList<ADrow> readFromFileAR) {
        arisMat = readFromFileAR;
    }

    @Override
    public Vector<BitArray> generates(BitArray g) {
//        System.out.println(g.toString());
        Vector<BitArray> mutated = new Vector<>();
        int idx = (int) (Math.random() * g.size());
        BitArray opt = (BitArray) g.clone();
//        System.out.println("idx " + (idx + 1));
        boolean cond = true;
        ArrayList line = arisMat.get(idx).row;
        for (int i = 0; i < line.size(); i++) {
            int x = (int) line.get(i);
            cond = cond && opt.get(x);
        }
        if (cond) {
            opt.set(idx, !cond);
        }
//        System.out.println(opt.toString());
        mutated.add(opt);
        return mutated;
    }

    @Override
    public Object owner() {
        return BitArray.class;
    }

    public static void main(String[] args) throws IOException {
        String file = "./frb7-15-1.mis";
        VertexOPT2 op = new VertexOPT2(readFromFileAR(file));
        op.generates(new BitArray(7, true));
    }
}
