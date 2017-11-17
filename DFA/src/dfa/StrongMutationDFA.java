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
class StrongMutationDFA extends ArityOne<AgentDFA> {

    int nStates;
    int nAlphabet;
    double mutRate;

    public StrongMutationDFA(int nStates, int nAlphabet, double mutRate) {
        this.nStates = nStates;
        this.nAlphabet = nAlphabet;
        this.mutRate = mutRate;

    }

    public StrongMutationDFA(int nStates, int nAlphabet) {
        this(nStates, nAlphabet, 0.05);
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
        StrongMutationDFA xo = new StrongMutationDFA(sn, alN);
        System.out.println(a);
        Vector<AgentDFA> ans = xo.generates(a);
        System.out.println(ans.get(0));
        System.out.println(ans.get(1));
        System.out.println(ans.get(2));
        System.out.println(ans.get(3));
    }

    @Override
    public Vector<AgentDFA> generates(AgentDFA g) {
        Vector<AgentDFA> solu = new Vector<AgentDFA>();
        AgentDFA child1 = new AgentDFA(g);
        AgentDFA child2 = new AgentDFA(g);
        AgentDFA child3 = new AgentDFA(g);
        AgentDFA child4 = new AgentDFA(g);
        /*
         mutar estado inicial
         */
        child1.initial = (int) (Math.random() * (child1.states));
        /*
         expresar mas info
         */
        child2.express = (int) (Math.random() * (child2.states - 1)) + 1;
        /*
         expresar menos info
         */
        child3.express = child3.express > 2 ? child3.express - 1 : 2;
        solu.add(child1);
        solu.add(child2);
        solu.add(child3);
        /*
         mutar estados
         */
        for (int i = 0; i < nStates; i++) {
            if (Math.random() < mutRate) {
                child4.mat[i] = (int) (Math.random() * nAlphabet);
            }
        }
        solu.add(child4);
        return solu;
    }
}
