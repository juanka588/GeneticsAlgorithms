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
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author JuanCamilo
 */
public class Nodo //implements Serializable
{

    public Nodo(Nodo n1, Nodo p) {
        this.value = n1.value;
        this.childs = n1.childs;
        this.parent = p;
    }

    /**
     * This method makes a "deep clone" of any Java object it is given.
     * @return a clone of the object
     */
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

    public Nodo(String value, Nodo parent, ArrayList<Nodo> childs) {
        this.value = value;
        this.parent = parent;
        this.childs = childs;
    }

    public Nodo() {
        childs = new ArrayList<>();
    }

    String value;
    Nodo parent;
    ArrayList<Nodo> childs;

    public boolean isLeaf() {
        if (childs == null) {
            return true;
        }
        return childs.isEmpty();
    }

    @Override
    public String toString() {
        String result = "";
        if (childs == null) {
            return value;
        }
        if (childs.isEmpty()) {
            return value;
        }
        if (childs.size() == 1) {
            return value + "(" + childs.get(0).toString() + ")";
        }
        for (int i = 0; i < childs.size() - 1; i++) {
            Nodo nodo = childs.get(i);
            result += value + "(" + nodo.toString() + ",";
        }
        Nodo nodo = childs.get(childs.size() - 1);
        result += nodo.toString() + ")";
        return result;
    }

    public int height() {
        int max = 0, h = 0;
        if (childs == null) {
            return 0;
        }
        for (int i = 0; i < childs.size(); i++) {
            Nodo nodo = childs.get(i);
            h = nodo.height();
            max = Math.max(max, h);
        }
        return max + 1;
    }

    public Nodo get(int current) {
        return toList().get(current);
    }

    public ArrayList<Nodo> toList() {
        ArrayList<Nodo> lista = new ArrayList<>();
        lista.add(this);
        for (int i = 0; i < lista.size(); i++) {
            Nodo nodo = lista.get(i);
            if (!nodo.isLeaf()) {
                lista.addAll(nodo.childs);
            }
        }
        return lista;
    }

    void replace(Nodo aux2) {
        this.parent = aux2.parent;
        this.value = aux2.value;
        this.childs = aux2.childs;
    }

}
