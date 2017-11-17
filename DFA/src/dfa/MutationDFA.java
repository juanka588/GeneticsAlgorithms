/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfa;

import java.io.File;
import java.io.IOException;
import unalcol.optimization.operators.ArityOne;
import unalcol.types.collection.vector.Vector;

/**
 *
 * @author JuanCamilo
 */
class MutationDFA extends ArityOne<AgentDFA> {

    int nStates;
    int nAlphabet;
    double mutRate;

    public MutationDFA(int nStates, int nAlphabet) {
        this(nStates, nAlphabet, 0.05);
    }

    public MutationDFA(int nStates, int nAlphabet, double mutRate) {
        this.nStates = nStates;
        this.nAlphabet = nAlphabet;
        this.mutRate = mutRate;
    }

    @Override
    public Object owner() {
        return AgentDFA.class;
    }

    public static void main(String[] args) throws IOException {
        int sn = 10;
        int alN = 2;
        DFAInstance di = new DFAInstance(sn, alN);
        AgentDFA a = new AgentDFA(sn, alN, 10, new int[sn * (alN + 1)]);
        a = di.get(a);
        MutationDFA xo = new MutationDFA(sn, alN);
        System.out.println(a);
        Vector<AgentDFA> ans = xo.generates(a);
        System.out.println(ans.get(0));
        System.out.println(ans.get(1));
    }

    @Override
    public Vector<AgentDFA> generates(AgentDFA g) {
        Vector<AgentDFA> solu = new Vector<AgentDFA>();
        AgentDFA child = new AgentDFA(g);
        AgentDFA child2 = new AgentDFA(g);
        /*
         crear estados de clausura
         */
        for (int k = 0; k < nStates; k++) {
            if (Math.random() < mutRate || k == 0) {
                for (int i = 0; i < nAlphabet; i++) {
                    child.mat[(i + 1) * nStates + k] = k;
                }
            }
        }
        /*
         quitar estados
         */
        for (int i = 0; i < nAlphabet; i++) {
            int quitar = (int) (Math.random() * nStates);
            if (Math.random() < mutRate || i == 0) {
                for (int j = 0; j < nStates; j++) {
                    if (child2.mat[(i + 1) * nStates + j] == quitar) {
                        child2.mat[(i + 1) * nStates + j] = ((int) (Math.random() * nStates) + nStates) % nStates;
                    }
                }
            }
        }
        solu.add(child);
        solu.add(child2);
        return solu;
    }
}
