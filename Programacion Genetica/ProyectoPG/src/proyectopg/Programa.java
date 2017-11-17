/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectopg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author JuanCamilo
 */
public class Programa {

    public Programa(Programa p) {
        this.examples = p.examples;
        this.functor = p.functor;
        this.arityFun = p.arityFun;
        this.terminal = p.terminal;
        this.maxEcu = p.maxEcu;
        this.maxTerms = p.maxTerms;
        this.lines = p.lines;
        ArrayList<Tree> forest = new ArrayList<>();
        for (int i = 0; i < p.trees.size(); i++) {
            Tree tree = new Tree(p.trees.get(i));
            forest.add(tree);
        }
        trees = forest;
    }

    @Override
    protected Programa clone() throws CloneNotSupportedException {
        Programa p = new Programa(examples, functor, arityFun, terminal, maxEcu, maxTerms);
        ArrayList<Tree> forest = new ArrayList<>();
        for (int i = 0; i < trees.size(); i++) {
            Tree tree = trees.get(i).clone();//new Tree(trees.get(i));//
            forest.add(tree);
        }
        p.trees = forest;
        p.toProgram();
        return p;
    }

    public Programa(String[][] examples, String[] functor, int[] arityFun, String[] terminal, int maxEcu, int maxTerms, String[] lines) {
        this.examples = examples;
        this.functor = functor;
        this.arityFun = arityFun;
        this.terminal = terminal;
        this.maxEcu = maxEcu;
        this.maxTerms = maxTerms;
        this.lines = lines;
    }

    public Programa() {
        String[][] exam = {
            {"max(0,0)", "0"},
            {"max(0,1)", "1"},
            {"max(1,0)", "1"},
            {"max(1,1)", "1"},
            {"max(5,2)", "5"},
            {"max(2,5)", "5"},
            {"max(3,3)", "3"},
            {"max(8,7)", "8"}
        };
        String[] func = {"max", "s"};
        int[] arityF = {2, 1};
        String[] term = {"0", "X", "Y"};
        this.examples = exam;
        this.functor = func;
        this.arityFun = arityF;
        this.terminal = term;
        this.maxEcu = 3;
        this.maxTerms = 3;
    }

    public Programa(String[][] examples, String[] functor, int[] arityFun, String[] terminal, int maxEcu, int maxTerms) {
        this.examples = examples;
        this.functor = functor;
        this.arityFun = arityFun;
        this.terminal = terminal;
        this.maxEcu = maxEcu;
        this.maxTerms = maxTerms;
    }
    public String[][] examples;
    public String[] functor;
    public int[] arityFun;
    public String[] terminal;
    public int maxEcu;
    public int maxTerms;
    public String[] lines;
    public ArrayList<Tree> trees = new ArrayList<Tree>();

    public String toProgram() {
        lines = new String[trees.size()];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.length - 1; i++) {
            lines[i] = trees.get(i).toString();
            sb.append(lines[i]);
            sb.append(';');
        }
        lines[lines.length - 1] = trees.get(lines.length - 1).toString();
        sb.append(lines[lines.length - 1]);
        return sb.toString();
    }

    @Override
    public String toString() {
        return toProgram();
    }

    public static void main(String[] args) {
        Programa p = new Programa();
        for (int i = 0; i < 1; i++) {
            Tree t = new Tree();
            Nodo r = new Nodo("=", null, null);
            Random rn = new Random();
            t.root = generateChild(false, p, r, 20, 0, 2);
            System.out.println(t.toString());
            p.trees.add(t);
            System.out.println("Altura: " + p.trees.get(i).height());
        }
        /*
         p.trees.add(xampleTree());
         System.out.println(xampleTree());
         */
        MutationPrograma mp = new MutationPrograma();
        mp.generates(p);
        System.out.println(p.toProgram());
    }

    static Tree xampleTree() {
        Tree t = new Tree();
        Nodo p1 = new Nodo("=", null, null);
        ArrayList<Nodo> childs = new ArrayList<>();
        Nodo c1 = new Nodo("max", p1, null);
        Nodo c2 = new Nodo("s", p1, null);

        Nodo c3 = new Nodo("s", c1, null);
        Nodo c6 = new Nodo("max", c1, null);

        Nodo c4 = new Nodo("max", c2, null);

        Nodo c5 = new Nodo("5", c3, null);

        Nodo c9 = new Nodo("2", c6, null);
        Nodo c10 = new Nodo("3", c6, null);

        Nodo c7 = new Nodo("5", c4, null);
        Nodo c8 = new Nodo("8", c4, null);

        childs.add(c7);
        childs.add(c8);
        c4.childs = childs;
        childs = new ArrayList<>();

        childs.add(c9);
        childs.add(c10);
        c6.childs = childs;
        childs = new ArrayList<>();

        childs.add(c5);
        c3.childs = childs;
        childs = new ArrayList<>();

        childs.add(c4);
        c2.childs = childs;
        childs = new ArrayList<>();

        childs.add(c3);
        childs.add(c6);
        c1.childs = childs;
        childs = new ArrayList<>();

        childs.add(c1);
        childs.add(c2);
        p1.childs = childs;

        t.root = p1;
        return t;
    }

    public static Object deepClone(Object object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Nodo generateChild(boolean b, Programa p, Nodo Parent, int level, int current, int childNum) {
        ArrayList<Nodo> childs = new ArrayList<>();
        Nodo c1 = null;
        Nodo c2 = null;
        if (b) {
            for (int i = 0; i < childNum; i++) {
                int idx = termValues(p);
                c1 = new Nodo(p.terminal[idx], Parent, null);
                childs.add(c1);
            }
        } else {
            for (int i = 0; i < childNum; i++) {
                int idx = funcTerm(p);
                c1 = new Nodo(p.functor[idx], Parent, null);
                generateChild(level == current, p, c1, level, current + 1, p.arityFun[idx]);
                childs.add(c1);
            }
        }

        Parent.childs = childs;
        return Parent;
    }

    private static int termValues(Programa p) {
        Random rd = new Random();
        return rd.nextInt(p.terminal.length);
    }

    private static int funcTerm(Programa p) {
        Random rd = new Random();
        return rd.nextInt(p.functor.length);
    }
}
