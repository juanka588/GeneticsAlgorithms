/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vcHaea;

import unalcol.optimization.operators.ArityOne;
import unalcol.optimization.operators.ArityTwo;
import unalcol.optimization.operators.binary.Mutation;
import unalcol.optimization.operators.binary.XOver;
import unalcol.types.collection.bitarray.BitArray;
import unalcol.types.collection.vector.Vector;

/**
 *
 * @author JuanCamilo
 */
public class VertexXOver extends ArityTwo<BitArray> {

    @Override
    public Vector<BitArray> generates(BitArray g, BitArray g1) {
        ArityOne mutation;
        ArityTwo xover;
        if (VertexCoverFitness.sum(g) != VertexCoverFitness.sum(g1)) {
            Vector<BitArray> mutated;
            mutation = new Mutation();
            mutated=mutation.generates(g);
            mutated.add((BitArray) mutation.generates(g1).get(0));
            return mutated;
        } else {
            xover = new XOver();
            return xover.generates(g, g1);
        }
    }

    @Override
    public Object owner() {
        return BitArray.class;
    }

}
