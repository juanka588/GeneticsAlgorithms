/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfa;

/**
 *
 * @author JuanCamilo
 */
public class AgentDFA {

    public AgentDFA(AgentDFA g) {
        this(g.states, g.nAlphabet, g.express, g.mat.clone());
    }

    public AgentDFA(int states, int nAlphabet, int express, int[] mat, int inI) {
        this.states = states;
        this.nAlphabet = nAlphabet;
        this.express = express;
        this.mat = mat;
        this.initial = inI;
    }

    public AgentDFA(int states, int nAlphabet, int express, int[] mat) {
        this.states = states;
        this.nAlphabet = nAlphabet;
        this.express = express;
        this.mat = mat;
        this.initial = (int) (Math.random() * states);
    }
    int states;
    int nAlphabet;
    int express;
    int mat[];
    int initial;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (int i = 0; i <= nAlphabet; i++) {
            for (int j = 0; j < states; j++) {
                sb.append(mat[i * states + j]);
                sb.append(',');
            }
            sb.append('\n');
        }
        sb.append('}');
        sb.append('\n');
        sb.append('e');
        sb.append(' ');
        sb.append(express);
        sb.append(' ');
        sb.append('i');
        sb.append(' ');
        sb.append(initial);
        return sb.toString();
    }

}
