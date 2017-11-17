/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectopg;

import java.util.ArrayList;

/**
 *
 * @author JuanCamilo
 */
public class Tree {

    Nodo root;
    ArrayList<Nodo> arbolLista;

    public Tree(Tree t) {
        this.root = clonate(t.root, null);// new Nodo(t.root);
        arbolLista = new ArrayList<>();
    }

    public Tree() {
        arbolLista = new ArrayList<>();
    }

    public Tree(Nodo r) {
        this.root = r;
        arbolLista = new ArrayList<>();
    }

    @Override
    public String toString() {
        String result = "";
        ArrayList<Nodo> childs = root.childs;
        for (int i = 0; i < childs.size() - 1; i++) {
            Nodo nodo = childs.get(i);
            result += nodo.toString() + root.value;
        }
        Nodo nodo = childs.get(childs.size() - 1);
        result += nodo.toString();
        return result;
    }

    @Override
    protected Tree clone() throws CloneNotSupportedException {
        Tree t = new Tree();
        t.root = clonate(root, null);//root.clone();//
        t.toList();
        return t;
    }

    public int height() {
        return root.height();
    }

    public Nodo get(int branch1) {
        return root.get(branch1);
    }

    public int size() {
        arbolLista = toList();
        return arbolLista.size();
    }

    public ArrayList<Nodo> toList() {
        arbolLista = root.toList();
        return arbolLista;
    }

    private Nodo clonate(Nodo root, Nodo p) {
        Nodo n = new Nodo(root.value, p, null);
        ArrayList<Nodo> lista2 = new ArrayList<>();
        if (root.childs == null) {
            return n;
        }
        for (Nodo c : root.childs) {
            Nodo k = clonate(c, n);
            lista2.add(k);
        }
        n.childs = lista2;
        return n;
    }
}
