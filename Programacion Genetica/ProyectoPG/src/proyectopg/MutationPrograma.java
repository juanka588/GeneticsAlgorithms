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
public class MutationPrograma extends ArityOne<Programa> {

    /**
     * Probability of mutating one single bit
     */
    protected double bit_mutation_rate = 0.0;
    protected int maxLen;

    /**
     * Constructor: Creates a mutation with a mutation probability
     *
     * @param rate mutation rate
     */
    public MutationPrograma(double rate, int maxL) {
        bit_mutation_rate = rate;
        maxLen = maxL;
    }
    public MutationPrograma(int maxL) {
        this(0.05, maxL);
    }

    /**
     * Constructor: Creates a mutation with a mutation probability
     *
     * @param void default mutation rate of 0.05
     */
    public MutationPrograma() {
        this(0.05, 5);
    }

    /**
     * Class of objects the operator is able to process
     *
     * @return Class of objects the operator is able to process
     */
    @Override
    public Object owner() {
        return int[].class;
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
            Tree t;
            if(bit_mutation_rate==0){
                bit_mutation_rate=1.0/pm.trees.get(i).size();
            }
            if (rn.nextDouble() < bit_mutation_rate) {
                t = Util.randomTree(pm.trees.get(i), pm);
            } else {
                t = MutatedTree(pm, i);
            }
//            if (t.height() > maxLen) {
//                t = Util.reparar(t, pm);
//            }
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
            MutationPrograma mp = new MutationPrograma();
            Programa PM = mp.generates(p).get(0);
            String cad = PM.toString();
            System.out.println(cad);
        }

    }

    public Tree MutatedTree(Programa p, int idx) {
        Random rn = new Random();
        Tree mutated = p.trees.get(idx);
        int branch1 = rn.nextInt(mutated.size() - 1) + 1;
        Nodo n1 = mutated.get(branch1);

        int branch2 = 0;
        boolean cond = Util.isTerminal(n1, p);
        if (cond) {
            branch2 = Util.nextTerminal(mutated, p);
        } else {
            branch2 = Util.nextOper(mutated, p);
        }
        Nodo n2 = mutated.get(branch2);
        Nodo aux = n1.parent;
        Nodo aux2 = n2.parent;
        n1.parent = aux2;
        n2.parent = aux;
        if (!cond) {
            int idx1 = aux.childs.indexOf(n1);//indexOf(n1, aux);//
            int idx2 = aux2.childs.indexOf(n2);//indexOf(n2, aux2);// 
            aux.childs.set(idx1, n2);
            aux2.childs.set(idx2, n1);
        }
        return mutated;
    }

}
