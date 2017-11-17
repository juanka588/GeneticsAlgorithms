/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;
import unalcol.optimization.OptimizationFunction;

/**
 *
 * @author JuanCamilo
 */
class FitnessDFA extends OptimizationFunction<AgentDFA> {

    public FitnessDFA(String[][] examples, int pSize) {
        this(examples, pSize, false, false);
    }

    public FitnessDFA(String[][] example, int pSize, double p) {
        this(example, pSize, false, false);
    }

    public FitnessDFA(String[][] example, int pSize, boolean postEti, boolean todo) {
        this.examples = example;
        examples = organizar(this.examples);
        popSize = pSize;
        this.posEti = postEti;
        if (todo) {
            percentage = index.length - 1;
        }
    }
    public int percentage = 0;
    int count;
    public int popSize;
    boolean posEti = false;
    String examples[][];
    public int index[];

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String examples[][] = DFA.readExamples(new File("./datasets/train-" + 10 + ".txt"));
        DFAInstance dfai = new DFAInstance(10, 2);
        AgentDFA agent = new AgentDFA(10, 2, 10, new int[10 * 3], 0);
        agent = dfai.get(agent);
        System.out.println(agent);
        // mostrar(examples);
        agent.express = 10;
        FitnessDFA adfa = new FitnessDFA(examples, 1, false, true);
        // mostrar(adfa.examples);
        System.out.println(adfa.apply(agent));

    }

    private static void mostrar(String[][] examples) {
        for (int i = 0; i < examples.length; i++) {
            for (int j = 0; j < examples[i].length; j++) {
                if (i != 0) {
                    System.out.print(examples[i][j] + "\t");
                } else {
                    System.out.print(examples[i][j]);
                }
            }
            System.out.println("");
        }
        System.out.println(examples.length);
    }

    @Override
    public Double apply(AgentDFA s) {
        if (count == popSize * 280) {
            count = 0;
            percentage++;
            if (percentage >= index.length) {
                percentage = index.length - 1;
            }
            //System.out.println(percentage);
        }
        int currentState = s.initial;
        double positive = 0.0;
        double positive2 = 0.0;
        int mat[] = s.mat.clone();
        for (int i = 0; i < mat.length; i++) {
            if (s.express <= 1) {
                s.express = 1 + ((int) (Math.random() * s.states - 1));
            }
            mat[i] = s.mat[i] % s.express;
        }
        int newState = currentState;
        int arr[] = new int[s.states * 2];
        for (int i = 0; i < examples.length; i++) {
            String cad = examples[i][0];
            boolean cond = (cad.length() / 2 <= index[percentage]);
            if (cond) {
                for (int j = 0; j < cad.length(); j++) {
                    if (cad.charAt(j) != ' ') {
                        if (cad.charAt(j) == '0') {
                            newState = mat[s.states + currentState];
                            currentState = newState;
                        } else {
                            newState = mat[2 * s.states + currentState];
                            currentState = newState;
                        }
                    }
                }
                int ans = Integer.valueOf(examples[i][1]);
                arr[currentState + ans * s.states]++;
                positive += ans == mat[currentState] ? 1 : 0;
                currentState = s.initial;
            }
        }
//        for (int j = 0; j < s.states; j++) {
//            System.out.println(arr[j] + " " + arr[s.states + j]);
//        }
        for (int i = 0; i < s.states; i++) {
            if (arr[i] - arr[s.states + i] == 0) {
                s.mat[i] = (int) (Math.random() * s.nAlphabet);
            } else {
                s.mat[i] = arr[i] - arr[s.states + i] > 0 ? 0 : 1;
            }
        }
//        System.out.println(s);
        for (int j = 0; j < s.states; j++) {
            positive2 += Math.abs(arr[j] - arr[s.states + j]);
        }
        count++;
        return posEti ? positive2 : positive;
    }

    private String[][] organizar(String[][] examples) {
        int arr[][] = new int[2][examples.length];
        for (int i = 0; i < examples.length; i++) {
            arr[0][i] = Integer.valueOf(examples[i][2].trim());
            arr[1][i] = i;
            // System.out.print(arr[0][i] + "\t" + arr[1][i] + "\n");
        }
        arr = mergeSort(arr);
              //  Collections.sort(arr,new ArrayComparator(0, true));
        String examples2[][] = new String[examples.length][3];
        SortedMap<Integer, Integer> tipos = new TreeMap<Integer, Integer>();
        for (int i = 0; i < examples2.length; i++) {
            int nIdx = arr[1][i];
            examples2[i][0] = examples[nIdx][0];
            examples2[i][1] = examples[nIdx][1];
            examples2[i][2] = examples[nIdx][2];
//            System.out.print(arr[0][i] + "\t" + arr[1][i] + "\n");
            tipos.put(arr[0][i], i);
        }
        index = new int[tipos.size()];
        for (int i = 0; i < tipos.keySet().size(); i++) {
            index[i] = (int) tipos.keySet().toArray()[i];
//            System.out.print(index[i] + "\t");
//            System.out.print(tipos.values().toArray()[i] + "\n");
        }
        return examples2;
    }

    private int[][] mergeSort(int[][] arr) {
        if (arr[0].length == 1) {
            return arr;
        }
        if (arr[0].length == 2) {
            if (arr[0][0] > arr[0][1]) {
                int aux = arr[0][0];
                int aIdx = arr[1][0];
                arr[0][0] = arr[0][1];
                arr[1][0] = arr[1][1];
                arr[0][1] = aux;
                arr[1][1] = aIdx;
            }
            return arr;
        }
        int left[][] = new int[2][arr[0].length / 2];
        int right[][] = new int[2][(arr[0].length / 2) + (arr[0].length) % 2];
        int j = 0;
        for (int i = 0; i < arr[0].length; i++) {
            if (i < (arr[0].length / 2)) {
                left[0][i] = arr[0][i];
                left[1][i] = arr[1][i];
            } else {
                right[0][j] = arr[0][i];
                right[1][j] = arr[1][i];
                j++;
            }
        }
        // System.out.println("l " + left[0].length + " r " + right[0].length);
        left = mergeSort(left);
        right = mergeSort(right);
        arr = merge(left, right);
        return arr;
    }

    private int[][] merge(int[][] left, int[][] right) {
        int sort[][] = new int[2][left[0].length + right[0].length];
        int idxL = 0, idxR = 0;
        int i = 0;
        while (i < sort[0].length) {
            if (left[0][idxL] > right[0][idxR]) {
                sort[0][i] = right[0][idxR];
                sort[1][i] = right[1][idxR];
                idxR++;
            } else {
                sort[0][i] = left[0][idxL];
                sort[1][i] = left[1][idxL];
                idxL++;
            }
            i++;
            if (idxL >= left[0].length) {
                while (idxR < right[0].length) {
                    sort[0][i] = right[0][idxR];
                    sort[1][i] = right[1][idxR];
                    i++;
                    idxR++;
                }
                i = sort[0].length;

            } else if (idxR >= right[0].length) {
                while (idxL < left[0].length) {
                    sort[0][i] = left[0][idxL];
                    sort[1][i] = right[1][idxL];
                    i++;
                    idxL++;
                }
                i = sort[0].length;
            }
        }
        return sort;
    }
}
