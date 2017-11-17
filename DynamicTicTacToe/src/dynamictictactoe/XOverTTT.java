/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamictictactoe;

import java.util.Random;
import unalcol.optimization.operators.ArityTwo;
import unalcol.types.collection.vector.Vector;

/**
 *
 * @author JuanCamilo
 */
class XOverTTT extends ArityTwo<TicTacToePlayer> {

    public XOverTTT() {
    }

    @Override
    public Vector<TicTacToePlayer> generates(TicTacToePlayer one, TicTacToePlayer two) {
        Vector<TicTacToePlayer> childs = new Vector<TicTacToePlayer>();
        TicTacToePlayer c1 = new TicTacToePlayer(one);
        TicTacToePlayer c2 = new TicTacToePlayer(two);

        /*
         intercambio de jugadas p1 con jugadas p2
         */
        Random rn = new Random();
        int pCorte = rn.nextInt(one.jugadas.size());
        for (int i = 0; i < one.size; i++) {
            if (i > pCorte) {
                c1.desitionP1[i] = two.desitionP1[i];
                c2.desitionP1[i] = one.desitionP1[i];
                c1.desitionP2[i] = two.desitionP2[i];
                c2.desitionP2[i] = one.desitionP2[i];
            }
        }
        childs.add(c1);
        childs.add(c2);
        return childs;
    }

    @Override
    public Object owner() {
        return TicTacToePlayer.class;
    }

}
