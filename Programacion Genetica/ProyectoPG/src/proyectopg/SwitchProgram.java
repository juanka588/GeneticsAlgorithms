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
class SwitchProgram extends ArityTwo<Programa> {

    public SwitchProgram() {
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
        int xoverpoint = rn.nextInt(minsize);
        for (int i = xoverpoint; i < minsize; i++) {
            Tree treeP1 = c1.trees.get(i);
            Tree treeP2 = c2.trees.get(i);
            c1.trees.set(i, treeP2);
            c2.trees.set(i, treeP1);
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
        SwitchProgram mp = new SwitchProgram();
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
}
