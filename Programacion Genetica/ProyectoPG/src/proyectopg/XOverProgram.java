/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectopg;

import java.util.ArrayList;
import java.util.Random;
import unalcol.optimization.operators.ArityTwo;
import unalcol.types.collection.vector.Vector;

/**
 *
 * @author JuanCamilo
 */
class XOverProgram extends ArityTwo<Programa> {

    protected int maxLen;

    /**
     * Generates a genome from the given thing
     *
     * @param ml indicates the maximum lenght of the tree
     */
    public XOverProgram(int ml) {
        maxLen = ml;
    }

    public XOverProgram() {
        this(5);
    }

    /**
     * Generates a genome from the given thing
     *
     * @param one Paretn 1
     * @param two Paretn 2
     * @return Xover for each Tree of each Program
     */
    @Override
    public Vector<Programa> generates(Programa one, Programa two) {
        Random rn = new Random();
        Vector<Programa> childs = new Vector<>();
        Programa c1 = new Programa(one);
        Programa c2 = new Programa(two);
        int minsize = Math.min(c1.trees.size(), c2.trees.size());
        for (int i = 0; i < minsize; i++) {
            int idx1 = i;//rn.nextInt(minsize), 
            int idx2 = i;//rn.nextInt(minsize);
            Tree treeP1 = c1.trees.get(idx1);
            Tree treeP2 = c2.trees.get(idx2);
            ArrayList<Tree> cruce = XOverTree(c1, treeP1, treeP2);
            treeP1 = cruce.get(0);
            treeP2 = cruce.get(1);
            if (treeP1.height() > maxLen) {
                treeP1 = Util.reparar(treeP1, c1);
            }
            if (treeP2.height() > maxLen) {
                treeP2 = Util.reparar(treeP2, c2);
            }
            c1.trees.set(idx1, treeP1);
            c2.trees.set(idx2, treeP2);
        }

        childs.add(c1);
        childs.add(c2);
        return childs;
    }

    @Override
    public Object owner() {
        return Programa.class;
    }

    public static void main(String[] args) {
        ProgramaInstance pi = new ProgramaInstance();
        XOverProgram mp = new XOverProgram();
        for (int i = 0; i < 1; i++) {
            Programa p1 = new Programa();
            p1 = pi.get(p1);
            Programa p2 = new Programa();
            p2 = pi.get(p2);
            System.out.println(p1.toProgram());
            System.out.println(p2.toProgram());
            Vector<Programa> cruzados = mp.generates(p1, p2);
            String cad = cruzados.get(0).toString();
            String cad2 = cruzados.get(1).toString();

            System.out.println(p1.toProgram());
            System.out.println(p2.toProgram());
            System.out.println(cad + "\n" + cad2);
        }

    }

    public ArrayList<Tree> XOverTree(Programa p, Tree pOne, Tree pTwo) {
        Random rn = new Random();
        ArrayList<Tree> XOverT = new ArrayList<>();
        int branch1 = rn.nextInt(pOne.size() - 1) + 1;
        Nodo n1 = pOne.get(branch1);

        int branch2;
        boolean cond = Util.isTerminal(n1, p);
        if (cond) {
            branch2 = Util.nextTerminal(pTwo, p);
        } else {
            branch2 = Util.nextOper(pTwo, p);
        }
        Nodo n2;
        if (branch2 == -1) {
            n2 = pTwo.get(1);
        } else {
            n2 = pTwo.get(branch2);
        }
        Nodo aux = n1.parent;
        Nodo aux2 = n2.parent;
        n1.parent = n2.parent;
        n2.parent = aux;
        if (!cond) {
            int idx1 = aux.childs.indexOf(n1);//indexOf(n1, aux);//
            int idx2 = aux2.childs.indexOf(n2);//indexOf(n2, aux2);//
            aux.childs.set(idx1, n2);
            aux2.childs.set(idx2, n1);
        }
        XOverT.add(pOne);
        XOverT.add(pTwo);
        return XOverT;
    }

}
