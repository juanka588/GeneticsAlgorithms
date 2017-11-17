/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fplearning.language;

import java.text.StringCharacterIterator;
import java.util.LinkedList;

/**
 *
 * @author Camiku
 */
public class Scanner implements FplConstants {

    private static StringCharacterIterator sci = new StringCharacterIterator("");
    private static LinkedList<Token> listTokens = new LinkedList();
    private static StringBuilder value = new StringBuilder();
    private static StringBuilder stringProccesed = new StringBuilder();
    private static String textToken;

    /**
     *
     */
    public Scanner() {
    }
    /**
     *
     * @param text
     * @return
     */
    private static int ch, state, numberline;

    public static LinkedList<Token> generateTokens(String source) throws LexicalException {
        sci.setText(source + '×');
        ch = '×';
        state = INITIAL;
        numberline = 1;
        listTokens.clear();
        value.delete(0, value.length());
        stringProccesed.delete(0, stringProccesed.length());
        do {
            ch = sci.current();
            switch (ch) {
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                case 'g':
                case 'h':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z':
                case '_':
                    switch (state) {
                        case INITIAL:
                        case FUNCTOR:
                            value.append((char) ch);
                            stringProccesed.append((char) ch);
                            state = FUNCTOR;
                            break;
                        case COMMENT:
                            state = COMMENT;
                            break;
                        default:
                            processToken(listTokens, value, state, ch);
                            state = INITIAL;
                            sci.previous();
                            break;
                    }
                    break;
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'S':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                case 'Z':
                    switch (state) {
                        case INITIAL:
                        case VARIABLE:
                            value.append((char) ch);
                            stringProccesed.append((char) ch);
                            state = VARIABLE;
                            break;
                        case COMMENT:
                            state = COMMENT;
                            break;
                        default:
                            processToken(listTokens, value, state, ch);
                            state = INITIAL;
                            sci.previous();
                            break;
                    }
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    switch (state) {
                        case INITIAL:
                        case NATURAL:
                            value.append((char) ch);
                            stringProccesed.append((char) ch);
                            state = NATURAL;
                            break;
                        case VARIABLE:
                            value.append((char) ch);
                            stringProccesed.append((char) ch);
                            state = VARIABLE;
                            break;
                        case FUNCTOR:
                            value.append((char) ch);
                            stringProccesed.append((char) ch);
                            state = FUNCTOR;
                            break;
                        case COMMENT:
                            state = COMMENT;
                            break;
                        default:
                            processToken(listTokens, value, state, ch);
                            state = INITIAL;
                            sci.previous();
                            break;
                    }
                    break;
                case '=':
                    switch (state) {
                        case INITIAL:
                            value.append((char) ch);
                            stringProccesed.append((char) ch);
                            state = EQUAL_SYMBOL;
                            break;
                        case COMMENT:
                            state = COMMENT;
                            break;
                        default:
                            processToken(listTokens, value, state, ch);
                            state = INITIAL;
                            sci.previous();
                            break;
                    }
                    break;
                case '(':
                    switch (state) {
                        case INITIAL:
                            value.append((char) ch);
                            stringProccesed.append((char) ch);
                            state = OPEN_PARENTHESIS;
                            break;
                        case COMMENT:
                            state = COMMENT;
                            break;
                        default:
                            processToken(listTokens, value, state, ch);
                            state = INITIAL;
                            sci.previous();
                            break;
                    }
                    break;
                case ')':
                    switch (state) {
                        case INITIAL:
                            value.append((char) ch);
                            stringProccesed.append((char) ch);
                            state = CLOSE_PARENTHESIS;
                            break;
                        case COMMENT:
                            state = COMMENT;
                            break;
                        default:
                            processToken(listTokens, value, state, ch);
                            state = INITIAL;
                            sci.previous();
                            break;
                    }
                    break;
                case '[':
                    switch (state) {
                        case INITIAL:
                            value.append((char) ch);
                            stringProccesed.append((char) ch);
                            state = OPEN_BRACKET;
                            break;
                        case COMMENT:
                            state = COMMENT;
                            break;
                        default:
                            processToken(listTokens, value, state, ch);
                            state = INITIAL;
                            sci.previous();
                            break;
                    }
                    break;
                case ']':
                    switch (state) {
                        case INITIAL:
                            value.append((char) ch);
                            stringProccesed.append((char) ch);
                            state = CLOSE_BRACKET;
                            break;
                        case COMMENT:
                            state = COMMENT;
                            break;
                        default:
                            processToken(listTokens, value, state, ch);
                            state = INITIAL;
                            sci.previous();
                            break;
                    }
                    break;
                case '|':
                    switch (state) {
                        case INITIAL:
                            value.append((char) ch);
                            stringProccesed.append((char) ch);
                            state = LIST_SEPARATOR;
                            break;
                        case COMMENT:
                            state = COMMENT;
                            break;
                        default:
                            processToken(listTokens, value, state, ch);
                            state = INITIAL;
                            sci.previous();
                            break;
                    }
                    break;
                case ',':
                    switch (state) {
                        case INITIAL:
                            value.append((char) ch);
                            stringProccesed.append((char) ch);
                            state = COMMA;
                            break;
                        case COMMENT:
                            state = COMMENT;
                            break;
                        default:
                            processToken(listTokens, value, state, ch);
                            state = INITIAL;
                            sci.previous();
                            break;
                    }
                    break;
                case '\n':
                    switch (state) {
                        case INITIAL:
                            value.append((char) ch);
                            stringProccesed.append((char) ch);
                            state = SEMICOLON;
                            break;
                        case COMMENT:
                            value.append((char) ch);
                            stringProccesed.append((char) ch);
                            state = SEMICOLON;
                            processToken(listTokens, value, state, ch);
                            state = INITIAL;
                            break;
                        default:
                            processToken(listTokens, value, state, ch);
                            state = INITIAL;
                            sci.previous();
                            break;
                    }
                    stringProccesed.delete(0, stringProccesed.length());
                    numberline++;
                    break;
                case ';':
                    switch (state) {
                        case INITIAL:
                            value.append((char) ch);
                            stringProccesed.append((char) ch);
                            state = SEMICOLON;
                            break;
                        case COMMENT:
                            state = COMMENT;
                            break;
                        default:
                            processToken(listTokens, value, state, ch);
                            state = INITIAL;
                            sci.previous();
                            break;
                    }
                    stringProccesed.delete(0, stringProccesed.length());
                    numberline++;
                    break;
                case '×':
                    processToken(listTokens, value, state, ch);
                    value.append((char) ch);
                    state = EOF;
                    processToken(listTokens, value, state, ch);
                    break;
                case '%':
                    switch (state) {
                        case INITIAL:
                        case COMMENT:
                            state = COMMENT;
                            break;
                        default:
                            processToken(listTokens, value, state, ch);
                            state = INITIAL;
                            sci.previous();
                            break;
                    }
                    break;
                case ' ':
                case '\t':
                case '\r':
                case '\f':
                case '\b':
                    switch (state) {
                        case FUNCTOR:
                        case FUNCTOR_SPACE:
                            state = FUNCTOR_SPACE;
                            break;
                        case INITIAL:
                        case WHITE_SPACE:
                            state = WHITE_SPACE;
                            break;
                        case COMMENT:
                            state = COMMENT;
                            break;
                        default:
                            processToken(listTokens, value, state, ch);
                            state = INITIAL;
                            sci.previous();
                            break;
                    }
                    break;
                default:
                    if (state != COMMENT) {
                        do{
                            stringProccesed.append((char)ch);
                            sci.next();
                            ch = sci.current();
                        }while(ch != '\n' && ch != ';' && ch != '×');
                        throw new LexicalException(stringProccesed.toString());
                    }
            }
            sci.next();
        } while (ch != '×');
        return listTokens;
    }

    private static void processToken(LinkedList<Token> listTokens, StringBuilder value, int state, int ch) {
        textToken = value.toString();
        switch (state) {
            case FUNCTOR:
            case FUNCTOR_SPACE:
                if (textToken.equals("s")) {
                    listTokens.add(new Token(textToken, SUCCESSOR));
                } else if (textToken.equals("true")) {
                    listTokens.add(new Token(textToken, TRUE));
                } else if (textToken.equals("false")) {
                    listTokens.add(new Token(textToken, FALSE));
                } else if (textToken.equals("equal")) {
                    listTokens.add(new Token(textToken, EQUAL));
                } else if (textToken.equals("undef")) {
                    listTokens.add(new Token(textToken, UNDEFINED));
                } else if (ch != '(') {
                    listTokens.add(new Token(textToken, CONSTANT));
                } else {
                    listTokens.add(new Token(textToken, FUNCTOR));
                }
                break;
            case VARIABLE:
                listTokens.add(new Token(textToken, VARIABLE));
                break;
            case NATURAL:
                listTokens.add(new Token(textToken, NATURAL));
                break;
            case EQUAL_SYMBOL:
                listTokens.add(new Token(textToken, EQUAL_SYMBOL));
                break;
            case OPEN_PARENTHESIS:
                listTokens.add(new Token(textToken, OPEN_PARENTHESIS));
                break;
            case CLOSE_PARENTHESIS:
                listTokens.add(new Token(textToken, CLOSE_PARENTHESIS));
                break;
            case OPEN_BRACKET:
                listTokens.add(new Token(textToken, OPEN_BRACKET));
                break;
            case CLOSE_BRACKET:
                listTokens.add(new Token(textToken, CLOSE_BRACKET));
                break;
            case LIST_SEPARATOR:
                listTokens.add(new Token(textToken, LIST_SEPARATOR));
                break;
            case COMMA:
                listTokens.add(new Token(textToken, COMMA));
                break;
            case SEMICOLON:
                listTokens.add(new Token(";", SEMICOLON));
                break;
            case EOF:
                listTokens.add(new Token("×", MARK_OF_BOTTOM));
                break;
        }
        value.delete(0, value.length());
    }
}
