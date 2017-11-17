/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfa;

import unalcol.optimization.operators.ArityOne;
import unalcol.types.collection.vector.Vector;

/**
 *
 * @author JuanCamilo
 */
class TranspositionDFA extends ArityOne<AgentDFA> {

    int nStates;
    int nAlphabet;
    double transRate;

    public TranspositionDFA(int nStates, int nAlphabet, double transRate) {
        this.nStates = nStates;
        this.nAlphabet = nAlphabet;
        this.transRate = transRate;
    }

    public TranspositionDFA(int nStates, int nAlphabet) {
        this(nStates, nAlphabet, 0.05);
    }

    @Override
    public Object owner() {
        return AgentDFA.class;
    }

    public static void main(String[] args) {
        int sn = 10;
        int alN = 2;
        DFAInstance di = new DFAInstance(sn, alN);
        AgentDFA a = new AgentDFA(sn, alN, 10, new int[sn * (alN + 1)]);
        a = di.get(a);
        TranspositionDFA xo = new TranspositionDFA(sn, alN);
        System.out.println(a);
        Vector<AgentDFA> ans = xo.generates(a);
        System.out.println(ans.get(0));
    }

    @Override
    public Vector<AgentDFA> generates(AgentDFA g) {
        Vector<AgentDFA> childs = new Vector<AgentDFA>();
        AgentDFA c1 = new AgentDFA(g);
        /*
         transposicion de lectura de alfabeto estados para c1,c2
         */
        for (int i = 0; i < nStates; i++) {
            for (int j = 1; j < nAlphabet; j++) {
                c1.mat[(3 - j) * nStates + i] = g.mat[j * nStates + i];
            }
            // System.out.println("si");
        }
        childs.add(c1);
        return childs;
    }
}
