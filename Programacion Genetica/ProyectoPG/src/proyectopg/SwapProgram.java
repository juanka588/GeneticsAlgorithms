/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectopg;

import java.util.ArrayList;
import java.util.Random;
import unalcol.optimization.operators.ArityOne;
import unalcol.types.collection.vector.Vector;

/**
 *
 * @author JuanCamilo
 */
class SwapProgram extends ArityOne<Programa> {

    public SwapProgram() {
    }

    /**
     * Flips a branch in the given tree structure of the program
     *
     * @param p Original Program
     * @return p Mutated
     */
    @Override
    public Vector<Programa> generates(Programa p) {
        Random rn = new Random();
        Vector<Programa> v = new Vector<>();
        Programa pm = new Programa(p);
        ArrayList<Tree> forest = pm.trees;
        for (int i = 0; i < forest.size(); i++) {
            Tree t = swapBranch(forest.get(i));
            pm.trees.set(i, t);
        }
        v.add(pm);
        return v;
    }

    /**
     * Testing function
     */
    public static void main(String[] argv) {
        for (int i = 0; i < 1; i++) {
            ProgramaInstance pi = new ProgramaInstance();
            Programa p = new Programa();
            p = pi.get(p);
            System.out.println(p.toProgram());
            SwapProgram mp = new SwapProgram();
            Programa PM = mp.generates(p).get(0);
            String cad = PM.toString();
            System.out.println(cad);
        }

    }

    @Override
    public Object owner() {
        return Programa.class;
    }

    private Tree swapBranch(Tree get) {
        ArrayList<Nodo> hijos=get.root.childs;
        Nodo aux=hijos.get(0);
        hijos.set(0,hijos.get(1));
        hijos.set(1,aux);
        get.root.childs=hijos;
        return get;
    }

}
