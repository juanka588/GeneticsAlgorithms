package fplearning;

/*
 * FPLearningApp.java
 * -Xms512m -Xmx1024m
 * -Xms32m -Xmx32m
 */
import fplearning.interpreter.Evaluator;
import fplearning.interpreter.GoalException;
import fplearning.interpreter.ProgramException;
import fplearning.language.LexicalException;
import fplearning.language.SyntacticalException;

/**
 * The main class of the application.
 */
public class FPLearningApp {

    public FPLearningApp() {
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        try {
            System.out.println(Evaluator.evalue(
                    "mod3(0) = 0; mod3(1) = 1; mod3(2) = 2; mod3(s(s(s(X)))) = mod3(X)",
                    "mod3(5)"));
            System.out.println(Evaluator.evalue(
                    "even(0) = true; even(1) = false; even(s(s(X))) = even(X)",
                    "even(5)"));
            System.out.println(Evaluator.evalue(
                    "sum(0,X) = X; sum(s(X),Y) = s(sum(X,Y))",
                    "sum(5,3)"));
             System.out.println(Evaluator.evalue(
                     "max(X,X)=max(X,X);max(Y,Y)=max(0,Y)","max(3,98)"));
            String[] functor = {"max", "s"};
            int[] arityFun = {2, 1};
            String[] terminal = {"0", "X", "Y"};
        } catch (ProgramException ex) {
            System.out.println(ex);
        } catch (GoalException ex) {
            System.out.println(ex);
        } catch (LexicalException ex) {
            System.out.println(ex);
        } catch (SyntacticalException ex) {
            System.out.println(ex);
        }
    }
}