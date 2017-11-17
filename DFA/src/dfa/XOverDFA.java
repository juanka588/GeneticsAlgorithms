/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfa;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import unalcol.optimization.operators.ArityTwo;
import unalcol.types.collection.vector.Vector;

/**
 *
 * @author JuanCamilo
 */
class XOverDFA extends ArityTwo<AgentDFA> {

    int maxStates;
    int nAlphabet;

    public XOverDFA(int maxStates, int nAlphabet) {
        this.maxStates = maxStates;
        this.nAlphabet = nAlphabet;
    }

    @Override
    public Object owner() {
        return int[].class;
    }

    public static void main(String[] args) throws IOException {
        int sn = 10;
        int alN = 2;
        DFAInstance di = new DFAInstance(sn, alN);
        AgentDFA a = new AgentDFA(sn, alN, 10, new int[sn * (alN + 1)]);
        a = di.get(a);
        AgentDFA b = new AgentDFA(sn, alN, 10, new int[sn * (alN + 1)]);
        b = di.get(b);
        XOverDFA xo = new XOverDFA(sn, alN);
        System.out.println(a);
        Vector<AgentDFA> ans = xo.generates(a, b);
        System.out.println(ans.get(0));
        System.out.println(ans.get(1));
        System.out.println(ans.get(2));
        System.out.println(ans.get(3));

    }

    @Override
    public Vector<AgentDFA> generates(AgentDFA one, AgentDFA two) {
        Vector<AgentDFA> childs = new Vector<AgentDFA>();
        AgentDFA c2 = new AgentDFA(two);
        if (Arrays.equals(one.mat, two.mat)) {
            DFAInstance da = new DFAInstance(maxStates, nAlphabet);
            c2 = da.get(c2);
            c2.express = (one.express + two.express) % one.states;
        }
        AgentDFA c1 = new AgentDFA(one);
        AgentDFA c11 = new AgentDFA(one);
        AgentDFA c12 = new AgentDFA(two);
        AgentDFA c21 = new AgentDFA(one);
        AgentDFA c22 = new AgentDFA(two);
        /*
         cruce por estados para c1,c2
         */
        int idx = maxStates + (int) (Math.random() * maxStates * 2);
        for (int i = 0; i < nAlphabet + 1; i++) {
            for (int j = 0; j < maxStates; j++) {
                if (i * maxStates + j > idx) {
                    c1.mat[i * maxStates + j] = two.mat[i * maxStates + j];
                    c2.mat[i * maxStates + j] = one.mat[i * maxStates + j];
                }
            }
        }
        /*
         cruce por terminales para c11,c12
         */
        int idx2 = (int) (Math.random() * maxStates);
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < maxStates; j++) {
                if (i * maxStates + j > idx2) {
                    c11.mat[i * maxStates + j] = two.mat[i * maxStates + j];
                    c12.mat[i * maxStates + j] = one.mat[i * maxStates + j];
                }
            }
        }
        c21.express = (((one.express + two.express) / 2) % one.states) + 1;
        c22.express = (((one.express - two.express) + one.states) % one.states) + 1;
        childs.add(c1);
        childs.add(c2);
        childs.add(c11);
        childs.add(c12);
        childs.add(c21);
        childs.add(c22);
        return childs;
    }
}
