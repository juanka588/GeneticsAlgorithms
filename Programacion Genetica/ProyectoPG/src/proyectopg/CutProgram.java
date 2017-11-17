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
class CutProgram extends ArityOne<Programa> {

    public CutProgram() {
    }

    @Override
    public Vector<Programa> generates(Programa p) {
        Random rn = new Random();
        Vector<Programa> v = new Vector<>();
        Programa pm = new Programa(p);
        ArrayList<Tree> forest = pm.trees;
        for (int i = 0; i < forest.size(); i++) {
            Tree t = cutBranch(forest.get(i), p);
            //pm.trees.set(i, t);
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
            CutProgram mp = new CutProgram();
            Programa PM = mp.generates(p).get(0);
            String cad = PM.toString();
            System.out.println(cad);
        }

    }

    @Override
    public Object owner() {
        return Programa.class;
    }

    private Tree cutBranch(Tree get, Programa p) {
        ArrayList<Nodo> hijos = get.root.toList();
        Random rn = new Random();
        int branch = rn.nextInt(hijos.size() - 1) + 1;
        Nodo cutted = hijos.get(branch);
        if (!cutted.isLeaf()) {
            for (int i = 0; i < cutted.childs.size(); i++) {
                cutted.childs.set(i, null);
            }
        }
        cutted.childs = new ArrayList<Nodo>();
        cutted.value = p.terminal[rn.nextInt(p.terminal.length)];
        return get;
    }

}
