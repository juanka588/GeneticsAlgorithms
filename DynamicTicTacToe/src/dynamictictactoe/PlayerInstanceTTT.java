/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamictictactoe;

import java.util.ArrayList;
import java.util.Random;
import unalcol.instance.InstanceService;

/**
 *
 * @author JuanCamilo
 */
class PlayerInstanceTTT implements InstanceService<TicTacToePlayer> {

    public PlayerInstanceTTT(ArrayList<StatesMap> jugadas) {
        this.jugadas = jugadas;
    }

    ArrayList<StatesMap> jugadas;

    @Override
    public TicTacToePlayer get(TicTacToePlayer t) {
        Random rn = new Random();
        int des1[] = new int[jugadas.size()];
        for (int i = 0; i < des1.length; i++) {
            int size = jugadas.get(i).posibilitiesT1.size();
            des1[i] = size > 0 ? rn.nextInt(size) : 0;

        }
        int des2[] = new int[jugadas.size()];
        for (int i = 0; i < des2.length; i++) {
            int size = jugadas.get(i).posibilitiesT2.size();
            des2[i] = size > 0 ? rn.nextInt(size) : 0;
        }
        TicTacToePlayer tttp = new TicTacToePlayer(jugadas.size(), jugadas, des1, des2);
        return tttp;
    }

    @Override
    public Object owner() {
        return TicTacToePlayer.class;
    }

}
