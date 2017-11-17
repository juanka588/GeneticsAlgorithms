/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamictictactoe;

import java.util.ArrayList;

/**
 *
 * @author JuanCamilo
 */
public class TicTacToePlayer {

    public TicTacToePlayer(int size, ArrayList<StatesMap> jugadas, int[] desitionP1, int[] desitionP2) {
        this.size = size;
        this.jugadas = jugadas;
        this.desitionP1 = desitionP1;
        this.desitionP2 = desitionP2;
    }

    int size;
    ArrayList<StatesMap> jugadas;
    int desitionP1[];
    int desitionP2[];

    public TicTacToePlayer(TicTacToePlayer two) {
        this(two.size, two.jugadas, two.desitionP1.clone(), two.desitionP2.clone());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            sb.append(desitionP1[i]);
            sb.append(' ');
        }
        sb.append('\n');
        for (int i = 0; i < 50; i++) {
            sb.append(desitionP2[i]);
            sb.append(' ');
        }
        return sb.toString();
    }

}
