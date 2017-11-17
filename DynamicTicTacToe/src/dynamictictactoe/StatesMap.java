/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamictictactoe;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author JuanCamilo
 */
public class StatesMap implements Comparable {

    int jugadores;
    int curState[];
    ArrayList<int[]> posibilitiesT1;
    ArrayList<int[]> posibilitiesT2;
    int turn;
    int boardSize;
    /*
     saber si en el estado actual ya gano alguien 0=nadie 1=jug1 2=jug2;
     */
    int ganador = 0;
    /*
     saber si ya se pusieron todas
     */
    boolean allIn;

    public static ArrayList<StatesMap> mapearJugadas() {
        Map<StatesMap, StatesMap> jugadas = new TreeMap<StatesMap, StatesMap>();
        int initialState[] = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        StatesMap sm = new StatesMap(initialState, 1);
        jugadas.put(sm, sm);
        ArrayList<StatesMap> estados = new ArrayList<>();
        estados.add(sm);
        for (int i = 0; i < estados.size(); i++) {
            StatesMap act = estados.get(i);
            ArrayList<int[]> posibilities = act.posibilitiesT1;
            for (int j = 0; j < posibilities.size(); j++) {
                StatesMap sm2 = new StatesMap(posibilities.get(j), 1);
                if (!jugadas.containsKey(sm2)) {
                    jugadas.put(sm2, sm2);
                    estados.add(sm2);
                }

            }
            posibilities = estados.get(i).posibilitiesT2;
            for (int j = 0; j < posibilities.size(); j++) {
                StatesMap sm2 = new StatesMap(posibilities.get(j), 1);
                if (!jugadas.containsKey(sm2)) {
                    jugadas.put(sm2, sm2);
                    estados.add(sm2);
                }
            }
        }
        //System.out.println(jugadas.size());
        return estados;
    }

    public static void main(String[] args) {
        Map<StatesMap, StatesMap> jugadas = new TreeMap<StatesMap, StatesMap>();
        int initialState[] = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        StatesMap sm = new StatesMap(initialState, 1);
        jugadas.put(sm, sm);
        System.out.println("");
        initialState = new int[]{
            0, 0, 1,
            0, 2, 0,
            1, 0, 0};
        sm = new StatesMap(initialState, 1);
        sm.printPosibilities(sm.posibilitiesT2);
        System.out.println("es ganador? " + sm.choseWiner());
        System.out.println("all in? " + sm.allIn);
        ArrayList<StatesMap> estados = new ArrayList<>();
        estados.add(sm);
//        for (int i = 0; i < estados.size(); i++) {
//            StatesMap act = estados.get(i);
//            ArrayList<int[]> posibilities = act.posibilitiesT1;
//            for (int j = 0; j < posibilities.size(); j++) {
//                StatesMap sm2 = new StatesMap(posibilities.get(j), 1);
//                if (!jugadas.containsKey(sm2)) {
//                    jugadas.put(sm2, sm2);
//                    estados.add(sm2);
//                }
//
//            }
//            posibilities = estados.get(i).posibilitiesT2;
//            for (int j = 0; j < posibilities.size(); j++) {
//                StatesMap sm2 = new StatesMap(posibilities.get(j), 1);
//                if (!jugadas.containsKey(sm2)) {
//                    jugadas.put(sm2, sm2);
//                    estados.add(sm2);
//                }
//            }
//            // System.out.println("i " + i);
//        }
//        System.out.println(jugadas.size());

    }

    public void listarPosibilities(int jug) {
        ArrayList<int[]> posibilities;
        if (jug == 1) {
            posibilitiesT1 = new ArrayList<>();
            posibilities = posibilitiesT1;
        } else {
            posibilitiesT2 = new ArrayList<>();
            posibilities = posibilitiesT2;
        }
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (curState[i * boardSize + j] == 0) {
                    if (!allIn) {
                        int newState[] = curState.clone();
                        newState[i * boardSize + j] = jug;
                        posibilities.add(newState);
                    }
                } else if (curState[i * boardSize + j] == jug && allIn) {
                    findAndReplace(i, j, jug, posibilities);
                }
            }
        }
        //printPosibilities(posibilities);
    }

    public void printPosibilities(ArrayList<int[]> posibilities) {
        for (int[] is : posibilities) {
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    System.out.print(is[i * boardSize + j] + " ");
                }
                System.out.println("");
            }
            System.out.println("");
        }
    }

    @Override
    public int compareTo(Object o) {
        StatesMap ob = (StatesMap) (o);
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                int idx = i * boardSize + j;
                if (curState[idx] < ob.curState[idx]) {
                    return -1;
                }
                if (curState[idx] > ob.curState[idx]) {
                    return 1;
                }
            }
        }
        return 0;
    }

    private void listarAllPosibilities() {
        for (int i = 1; i <= jugadores; i++) {
            listarPosibilities(i);
        }
    }

    public StatesMap(int[] curState, int turn) {
        this.curState = curState;
        this.turn = turn;
        this.allIn = verificar();
        posibilitiesT1 = new ArrayList<>();
        posibilitiesT2 = new ArrayList<>();
        boardSize = 3;
        jugadores = 2;
        listarAllPosibilities();
        ganador = choseWiner();
    }

    private boolean verificar() {
        int sum1 = 0, sum2 = 0;
        for (int i = 0; i < curState.length; i++) {
            if (curState[i] == 1) {
                sum1++;
            }
            if (curState[i] == 2) {
                sum2++;
            }
        }
        return sum1 >= 3 && sum2 >= 3;
    }

    private int choseWiner() {
        int winer = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (curState[i * boardSize + j] == 1) {
                    winer = verifiWiner(i, j, 1);
                }
                if (winer != 0) {
                    return winer;
                }
            }
        }
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (curState[i * boardSize + j] == 2) {
                    winer = verifiWiner(i, j, 2);
                }
                if (winer != 0) {
                    return winer;
                }
            }
        }
        return winer;
    }

    private int verifiWiner(int i, int j, int play) {
        int winer = 0;
        boolean cond = false;
        cond = cond || lookRight(i, j, play);
        cond = cond || lookLeft(i, j, play);
        cond = cond || lookDown(i, j, play);
        cond = cond || lookUp(i, j, play);
        cond = cond || lookSupDiag(i, j, play);
        cond = cond || lookSupDiag2(i, j, play);
        cond = cond || lookInfDiag(i, j, play);
        cond = cond || lookInfDiag2(i, j, play);
        if (cond) {
            return play;
        }
        return winer;
    }

    private boolean lookRight(int i, int j, int play) {
        if (j + 2 >= boardSize) {
            return false;
        }
        return curState[i * boardSize + j] == play
                && curState[i * boardSize + (j + 1)] == play
                && curState[i * boardSize + (j + 2)] == play;
    }

    private boolean lookLeft(int i, int j, int play) {
        if (j - 2 < 0) {
            return false;
        }
        return curState[i * boardSize + j] == play
                && curState[i * boardSize + (j - 1)] == play
                && curState[i * boardSize + (j - 2)] == play;
    }

    private boolean lookDown(int i, int j, int play) {
        if (i + 2 >= boardSize) {
            return false;
        }
        return curState[i * boardSize + j] == play
                && curState[(i + 1) * boardSize + j] == play
                && curState[(i + 2) * boardSize + j] == play;
    }

    private boolean lookUp(int i, int j, int play) {
        if (i - 2 < 0) {
            return false;
        }
        return curState[i * boardSize + j] == play
                && curState[(i - 1) * boardSize + j] == play
                && curState[(i - 2) * boardSize + j] == play;
    }

    private boolean lookSupDiag(int i, int j, int play) {
        if (i + 2 >= boardSize || j + 2 >= boardSize) {
            return false;
        }
        return curState[i * boardSize + j] == play
                && curState[(i + 1) * boardSize + (j + 1)] == play
                && curState[(i + 2) * boardSize + (j + 2)] == play;
    }

    private boolean lookInfDiag(int i, int j, int play) {
        if (i - 2 < 0 || j - 2 < 0) {
            return false;
        }
        return curState[i * boardSize + j] == play
                && curState[(i - 1) * boardSize + (j - 1)] == play
                && curState[(i - 2) * boardSize + (j - 2)] == play;
    }

    private boolean lookSupDiag2(int i, int j, int play) {
        if (i + 2 >= boardSize || j - 2 < 0) {
            return false;
        }
        return curState[i * boardSize + j] == play
                && curState[(i + 1) * boardSize + (j - 1)] == play
                && curState[(i + 2) * boardSize + (j - 2)] == play;
    }

    private boolean lookInfDiag2(int i, int j, int play) {
        if (i - 2 < 0 || j + 2 >= boardSize) {
            return false;
        }
        return curState[i * boardSize + j] == play
                && curState[(i - 1) * boardSize + (j + 1)] == play
                && curState[(i - 2) * boardSize + (j + 2)] == play;
    }

    private void findAndReplace(int i, int j, int jug, ArrayList<int[]> posibilities) {
        for (int k = 0; k < boardSize; k++) {
            for (int l = 0; l < boardSize; l++) {
                int newState[] = curState.clone();
                newState[i * boardSize + j] = 0;
                if (newState[k * boardSize + l] == 0 && !(k == i && l == j)) {
                    newState[k * boardSize + l] = jug;
                    posibilities.add(newState);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                sb.append(curState[i * boardSize + j]);
                sb.append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

}
