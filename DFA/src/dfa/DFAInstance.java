/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfa;

import static dfa.DFA.nAlphabet;
import java.util.Random;
import unalcol.instance.InstanceService;

/**
 *
 * @author JuanCamilo
 */
public class DFAInstance implements InstanceService<AgentDFA> {

    public DFAInstance(int stateNumber, int nAlpha) {
        this.stateNumber = stateNumber;
        nAlphabet = nAlpha;
    }

    int stateNumber;
    int nAlphabet;

    @Override
    public Object owner() {
        return AgentDFA.class;
    }

    @Override
    public AgentDFA get(AgentDFA t) {
        Random rn = new Random();
        int mat[] = new int[stateNumber * (nAlphabet + 1)];
        for (int i = 0; i <= nAlphabet; i++) {
            for (int j = 0; j < stateNumber; j++) {
                if (i == 0) {
                    mat[i * stateNumber + j] = rn.nextInt(nAlphabet);
                } else {
                    mat[i * stateNumber + j] = rn.nextInt(stateNumber);
                }
            }
        }
        t.mat = mat;
        t.nAlphabet = nAlphabet;
        t.states = stateNumber;
        t.express = rn.nextInt(stateNumber) + 2;//stateNumber;// 
        t.initial = (int) (Math.random() * t.states);//0;//
        return t;
    }

}
