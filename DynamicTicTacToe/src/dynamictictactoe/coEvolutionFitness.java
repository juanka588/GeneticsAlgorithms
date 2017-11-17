/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamictictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import unalcol.optimization.OptimizationFunction;
import unalcol.optimization.solution.Solution;
import unalcol.types.collection.vector.Vector;

/**
 *
 * @author JuanCamilo
 */
class coEvolutionFitness extends OptimizationFunction<TicTacToePlayer> {

    Vector<Solution<TicTacToePlayer>> pop;

    public coEvolutionFitness(Vector<Solution<TicTacToePlayer>> pop) {
        this.pop = pop;
    }

    @Override
    public Double apply(TicTacToePlayer s) {
        double f = 0.0;
        for (int i = 0; i < pop.size(); i++) {
            f += simular(s, pop.get(i).get());
        }
        for (int i = 0; i < pop.size(); i++) {
            f += simular(pop.get(i).get(), s);
        }
        return f;
    }

    public int simular(TicTacToePlayer p1, TicTacToePlayer p2) {
        int count = 40;
        int curState[] = new int[]{
            0, 0, 0,
            0, 0, 0,
            0, 0, 0
        };

        while (count > 0) {
            int idxJugada = indexOf(curState, p1.jugadas);
            int idx2 = idxJugada;
            StatesMap sm = p1.jugadas.get(idxJugada);
            curState = sm.posibilitiesT1.get(p1.desitionP1[idxJugada]);
            sm = new StatesMap(curState, 1);
            StatesMap sm2 = sm;
            //System.out.println(print(curState));
            if (sm.ganador == 1) {
                return 1;
            }
            idxJugada = indexOf(curState, p2.jugadas);
            sm = p2.jugadas.get(idxJugada);
            curState = sm.posibilitiesT2.get(p1.desitionP2[idxJugada]);
            sm = new StatesMap(curState, 2);
            //System.out.println(print(curState));
            if (sm.ganador == 2) {
               // p1.desitionP1[idx2] = (int) (Math.random() * p1.jugadas.get(idx2).posibilitiesT1.size());
                //System.out.println( p1.desitionP1[idx2]);
                return 0;
            }
            count--;
        }
        return 0;
    }

    public static void main(String[] args) {
        ArrayList<StatesMap> jugadas = StatesMap.mapearJugadas();
        PlayerInstanceTTT pit = new PlayerInstanceTTT(jugadas);
        TicTacToePlayer p1 = new TicTacToePlayer(jugadas.size(), jugadas, new int[10], new int[10]);
        TicTacToePlayer p2 = new TicTacToePlayer(jugadas.size(), jugadas, new int[10], new int[10]);
        p1 = pit.get(p1);
        p2 = pit.get(p2);
        System.out.println(p1);
        System.out.println(p2);
        coEvolutionFitness cef = new coEvolutionFitness(null);
        System.out.println(cef.simular(p1, p2));
    }

    private int indexOf(int[] curState, ArrayList<StatesMap> jugadas) {
        for (int i = 0; i < jugadas.size(); i++) {
            StatesMap statesMap = jugadas.get(i);
            if (Arrays.equals(statesMap.curState, curState)) {
                return i;
            }
        }
        return -1;
    }

    private String print(int[] curState) {
        String cad = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cad += curState[i * 3 + j] + " ";
            }
            cad += "\n";
        }
        return cad;
    }

}
