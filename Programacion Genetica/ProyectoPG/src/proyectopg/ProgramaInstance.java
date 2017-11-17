/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectopg;

import java.util.ArrayList;
import java.util.Random;
import unalcol.instance.InstanceService;

/**
 *
 * @author JuanCamilo
 */
public class ProgramaInstance implements InstanceService<Programa> {

    @Override
    public Programa get(Programa p) {
        p = new Programa();
        Random rn = new Random();
        for (int i = 0; i < p.maxEcu; i++) {//rn.nextInt(p.maxEcu)+3;
            Tree t = new Tree();
            Nodo r = new Nodo("=", null, null);
            t.root = generateChild(false, p, r, rn.nextInt(p.maxTerms), 0, 2);
            p.trees.add(t);
        }
        return p;
    }

    @Override
    public Object owner() {
        return ArrayList.class;
    }

    public Nodo generateChild(boolean b, Programa p, Nodo Parent, int level, int current, int childNum) {
        ArrayList<Nodo> childs = new ArrayList<>();
        Nodo c1 = null;
        if (b) {
            for (int i = 0; i < childNum; i++) {
                int idx = termValues(p);
                c1 = new Nodo(p.terminal[idx], Parent, new ArrayList<>());
                childs.add(c1);
            }
        } else {
            for (int i = 0; i < childNum; i++) {
                int idx = funcTerm(p);
                c1 = new Nodo(p.functor[idx], Parent, new ArrayList<>());
                generateChild(level == current, p, c1, level, current + 1, p.arityFun[idx]);
                childs.add(c1);
            }
        }
        Parent.childs = childs;
        return Parent;
    }

    protected int termValues(Programa p) {
        Random rd = new Random();
        return rd.nextInt(p.terminal.length);
    }

    protected int funcTerm(Programa p) {
        Random rd = new Random();
        return rd.nextInt(p.functor.length);
    }

}
