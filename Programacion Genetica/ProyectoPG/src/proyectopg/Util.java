/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectopg;

import java.util.Random;

/**
 *
 * @author JuanCamilo
 */
public class Util {

    public static boolean isTerminal(Nodo n, Programa p) {
        String[] terms = p.terminal;
        for (int i = 0; i < terms.length; i++) {
            if (n.value.equals(terms[i])) {
                return true;
            }

        }
        return false;
    }

    public static int nextTerminal(Tree mutated, Programa p) {
        Random rn = new Random();
        int pos = rn.nextInt(mutated.size() - 1) + 1;
        Nodo n1 = mutated.get(pos);
        int cnt = 0;
        while (!isTerminal(n1, p) && cnt < 100) {
            pos = rn.nextInt(mutated.size() - 1) + 1;
            n1 = mutated.get(pos);
            cnt++;
        }
        if (cnt >= 100) {
            return -1;
        }
        return pos;
    }

    public static int nextOper(Tree mutated, Programa p) {
        Random rn = new Random();
        int pos = rn.nextInt(mutated.size() - 1) + 1;
        Nodo n1 = mutated.get(pos);
        int cnt = 0;
        while (isTerminal(n1, p) && cnt < 100) {
            pos = rn.nextInt(mutated.size() - 1) + 1;
            n1 = mutated.get(pos);
            cnt++;
        }
        if (cnt >= 100) {
            return -1;
        }
        return pos;
    }

    public static Tree reparar(Tree t, Programa p) {
        //System.out.println("reparar cruce");
        return randomTree(t, p);
    }

    public static Tree randomTree(Tree t, Programa p) {
        Random rn = new Random();
        ProgramaInstance pi = new ProgramaInstance();
        Nodo r = new Nodo("=", null, null);
        t.root = pi.generateChild(false, p, r, rn.nextInt(p.maxTerms), 0, 2);
        t.toList();
        return t;
    }

    public static int indexOf(Nodo n1, Nodo p) {
        for (int i = 0; i < p.childs.size(); i++) {
            if (isEqual(n1, p.childs.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isEqual(Nodo n1, Nodo n2) {
        if (n1 == null && n2 == null) {
            return true;
        }
        if (n1 != null && n2 != null) {
            boolean cond = true;
            if (n1.value.equals(n2.value)) {
                if (n1.isLeaf() && n2.isLeaf()) {
                    return true;
                }
                for (int i = 0; i < n1.childs.size(); i++) {
                    cond = cond & isEqual(n1.childs.get(i), n2.childs.get(i));
                }
                return cond;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static Nodo getRoot(Nodo n1) {
        if (n1.parent == null) {
            return n1;
        } else {
            return getRoot(n1.parent);
        }
    }
}
