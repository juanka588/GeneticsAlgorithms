/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fplearning.interpreter;

import fplearning.language.Term;

/**
 *
 * @author Camiku
 */
public class Equation implements fplearning.language.FplConstants {

    private Term rhs;
    private Term lhs;
    private Term root;
    private int maxLength = 0;

    public Equation(Equation e) {
        this((Term) e.getLhs().clone(), (Term) e.getRhs().clone());
    }

    public Equation(Term lhs, Term rhs) {
        this.root = new Term("equal", EQUAL, null);
        this.lhs = lhs;
        this.rhs = rhs;
        this.root.addChild(this.lhs);
        this.root.addChild(this.rhs);
        maxLength = toString().length() - 2;
    }

    public Equation(Term root) {
        this.root = root;
        lhs = (Term) root.getChild(0);
        rhs = (Term) root.getChild(1);
        maxLength = getRoot().toString().length() - 2;
    }

    public Term getRoot() {
        return root;
    }

    public void setRoot(Term root) {
        this.root = root;
    }

    /**
     * Get the value of rhs
     *
     * @return the value of rhs
     */
    public Term getRhs() {
        return rhs;
    }

    /**
     * Set the value of rhs
     *
     * @param rhs new value of rhs
     */
    public void setRhs(Term rhs) {
        this.rhs = rhs;
    }

    /**
     * Get the value of lhs
     *
     * @return the value of lhs
     */
    public Term getLhs() {
        return lhs;
    }

    /**
     * Set the value of lhs
     *
     * @param lhs new value of lhs
     */
    public void setLhs(Term lhs) {
        this.lhs = lhs;
    }

    @Override
    public Object clone() {
        return new Equation(this);
    }

    @Override
    public String toString() {
        return root.printSyntacticSugar();
    }

    public Equation getVariant() {
        return new Equation(this.getRoot().getVariant());
    }

    private int calculateMaxLength() {
        maxLength = getRoot().toString().length() - 2;
        return maxLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public Equation renameVar() {
        root.renameVar();
        calculateMaxLength();
        return this;
    }

    public boolean isSimpleEquation() {
        return isSimpleEquation(this.rhs);
    }

    private boolean isSimpleEquation(Term term) {
        switch (term.getType()) {
            case FUNCTOR:
                return false;
            case SUCCESSOR:
                return isSimpleEquation(term.getChild(0));
            case LIST:
                return isSimpleEquation(term.getChild(0)) && isSimpleEquation(term.getChild(1));
            case VARIABLE:
            case NULL:
            case NATURAL:
            case TRUE:
            case FALSE:
            case CONSTANT:
            case UNDEFINED:
                return true;
            default:
                System.out.println("Equation neither simple nor composed");
                return true;
        }
    }
}
