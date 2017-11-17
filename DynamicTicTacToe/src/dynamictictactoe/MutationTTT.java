/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamictictactoe;

import java.util.ArrayList;
import java.util.Random;
import unalcol.optimization.operators.ArityOne;
import unalcol.types.collection.vector.Vector;

/**
 *
 * @author JuanCamilo
 */
class MutationTTT extends ArityOne<TicTacToePlayer> {

    double mutRate;

    public MutationTTT(double mr) {
        mutRate = mr;
    }

    @Override
    public Vector<TicTacToePlayer> generates(TicTacToePlayer one) {
        Vector<TicTacToePlayer> childs = new Vector<TicTacToePlayer>();
        TicTacToePlayer c1 = new TicTacToePlayer(one);
        ArrayList<StatesMap> jugadas = one.jugadas;
        Random rn = new Random();
        int des1[] = new int[jugadas.size()];
        for (int i = 0; i < des1.length; i++) {
            int size = jugadas.get(i).posibilitiesT1.size();
            if (Math.random() < mutRate) {
                des1[i] = size > 0 ? rn.nextInt(size) : 0;
            }

        }
        int des2[] = new int[jugadas.size()];
        for (int i = 0; i < des2.length; i++) {
            int size = jugadas.get(i).posibilitiesT2.size();
            if (Math.random() < mutRate) {
                des2[i] = size > 0 ? rn.nextInt(size) : 0;
            }
        }
        TicTacToePlayer tttp = new TicTacToePlayer(jugadas.size(), jugadas, des1, des2);
        childs.add(c1);

        return childs;
    }

    @Override
    public Object owner() {
        return TicTacToePlayer.class;
    }

}
