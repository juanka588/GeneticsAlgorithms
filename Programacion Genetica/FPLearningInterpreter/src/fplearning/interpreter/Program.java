/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fplearning.interpreter;

import fplearning.language.FplConstants;
import fplearning.language.LexicalException;
import fplearning.language.Parser;
import fplearning.language.SyntacticalException;
import fplearning.language.Term;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Camiku
 */
public class Program implements FplConstants {

    private LinkedList<Equation> listEquations = null;
    private double coverture = 0.0;
    private int maxLength = 0;
    private static int i_sort;
    private static LinkedList<Equation> listAux = new LinkedList<Equation>();
    private static Equation equaProg;
    private static Term termProg;

    public Program() {
        maxLength = 0;
    }

    public Program(Program p) {
        this.listEquations = new LinkedList<Equation>();
        maxLength = 0;
        for (Iterator<Equation> it = p.getListEquations().iterator(); it.hasNext();) {
            equaProg = (Equation) it.next().clone();
            this.listEquations.add(equaProg);
            maxLength += equaProg.getMaxLength();
            if (it.hasNext()) {
                maxLength++;
            }
        }
    }

    public Program(Example example) {
        this.listEquations = new LinkedList<Equation>();
        maxLength = 0;
        for (Iterator<Equation> it = example.getListEquations().iterator(); it.hasNext();) {
            equaProg = it.next();
            this.listEquations.add(equaProg);
            maxLength += equaProg.getMaxLength();
            if (it.hasNext()) {
                maxLength++;
            }
        }
    }

    public Program(String source) throws LexicalException, SyntacticalException, ProgramException {
        this(Parser.parsing(source));
    }

    public Program(Equation e) {
        this.listEquations = new LinkedList<Equation>();
        this.listEquations.add(e);
        maxLength = e.getMaxLength();
    }

    public Program(LinkedList<Term> list) throws ProgramException {
        if (!list.isEmpty()) {
            this.listEquations = new LinkedList<Equation>();
            maxLength = 0;
            for (Iterator<Term> it = list.iterator(); it.hasNext();) {
                termProg = it.next();
                if (termProg.isProgramTerm()) {
                    equaProg = new Equation(termProg);
                    this.listEquations.add(equaProg);
                    maxLength += equaProg.getMaxLength();
                    if (it.hasNext()) {
                        maxLength++;
                    }
                } else {
                    throw new ProgramException(termProg.toString());
                }
            }
        }
    }

    /**
     * Get the value of equation
     *
     * @return the value of equation
     */
    public LinkedList<Equation> getListEquations() {
        return this.listEquations;
    }

    /**
     * Set the value of equation
     *
     * @param equation new value of equation
     */
    public void setListEquation(LinkedList<Equation> listEquations) {
        this.listEquations = listEquations;
        maxLength = 0;
        for (Iterator<Equation> it = this.listEquations.iterator(); it.hasNext();) {
            maxLength += it.next().getMaxLength();
            if (it.hasNext()) {
                maxLength++;
            }
        }
    }

    public void addEquationItself(Equation e) {
        if (this.listEquations == null) {
            this.listEquations = new LinkedList<Equation>();
            maxLength = 0;
        } else {
            maxLength++;
        }
        this.listEquations.addFirst(e);
        maxLength += e.getMaxLength();
    }

    public void addProgramItself(Program p) {
        Equation e;
        if (this.listEquations == null) {
            this.listEquations = new LinkedList<Equation>();
            maxLength = 0;
        } else {
            maxLength++;
        }
        for (Iterator<Equation> it = p.getListEquations().iterator(); it.hasNext();) {
            e = it.next();
            this.listEquations.addFirst(e);
            maxLength += e.getMaxLength();
            if (it.hasNext()) {
                maxLength++;
            }
        }
    }

    public void addEquationClone(Equation e) {
        e = (Equation) e.clone();
        if (this.listEquations == null) {
            this.listEquations = new LinkedList<Equation>();
            maxLength = 0;
        } else {
            maxLength++;
        }
        this.listEquations.addFirst(e);
        maxLength += e.getMaxLength();
    }

    public void addProgramClone(Program p) {
        p = (Program) p.clone();
        Equation e;
        if (this.listEquations == null) {
            this.listEquations = new LinkedList<Equation>();
            maxLength = 0;
        } else {
            maxLength++;
        }
        for (Iterator<Equation> it = p.getListEquations().iterator(); it.hasNext();) {
            e = it.next();
            this.listEquations.addFirst(e);
            maxLength += e.getMaxLength();
            if (it.hasNext()) {
                maxLength++;
            }
        }
    }

    public void addEquation(int i, Equation e) {
        if (this.listEquations == null) {
            this.listEquations = new LinkedList<Equation>();
            maxLength = 0;
        } else {
            maxLength++;
        }
        this.listEquations.add(i, e);
        maxLength += e.getMaxLength();
    }

    public Equation removeEquation(int i) {
        if (getListEquations().size() > 1) {
            maxLength -= getListEquations().get(i).getMaxLength() + 1;
        } else {
            maxLength -= getListEquations().get(i).getMaxLength();
        }
        return getListEquations().remove(i);
    }

    @Override
    public Object clone() {
        return new Program(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Iterator<Equation> it = this.listEquations.iterator(); it.hasNext();) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append("; ");
            }
        }
        return sb.toString();
    }

    public double getCoverture() {
        return coverture;
    }

    public void setCoverture(double coverture) {
        this.coverture = coverture;
    }

    public int calculateMaxLength() {
        maxLength = 0;
        Equation e;
        for (Iterator<Equation> it = this.listEquations.iterator(); it.hasNext();) {
            e = it.next();
            maxLength += e.getMaxLength();
            if (it.hasNext()) {
                maxLength++;
            }
        }
        return maxLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void sortProgram() {
        for (i_sort = listEquations.size() - 1; i_sort >= 0; i_sort--) {
            if (!listEquations.get(i_sort).isSimpleEquation()) {
                listAux.add(listEquations.remove(i_sort));
            }
        }
        for (i_sort = listAux.size() - 1; i_sort >= 0; i_sort--) {
            listEquations.add(listAux.remove(i_sort));
        }
    }
}
