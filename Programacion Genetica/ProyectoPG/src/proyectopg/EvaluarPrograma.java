package proyectopg;

import fplearning.interpreter.Evaluator;
import fplearning.interpreter.GoalException;
import fplearning.interpreter.ProgramException;
import fplearning.language.LexicalException;
import fplearning.language.SyntacticalException;
import proyectopg.Programa;
import unalcol.optimization.OptimizationFunction;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author JuanCamilo
 */
public class EvaluarPrograma extends OptimizationFunction<Programa> {

    @Override
    public Double apply(Programa s) {
        double d = 0.0;
        String[][] examples = s.examples;
        for (int i = 0; i < examples.length; i++) {
            try {
                if (Evaluator.evalue(s.toProgram(), examples[i][0]).equals(examples[i][1])) {
                    d++;
                }
            } catch (LexicalException ex) {
                d--;
            } catch (SyntacticalException ex) {
                d--;

            } catch (ProgramException ex) {
                d--;
            } catch (GoalException ex) {
                d--;
            } catch (Exception e) {
                d -= 3;
            }
        }
        return d;
    }

}
