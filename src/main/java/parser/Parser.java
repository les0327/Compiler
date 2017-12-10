package parser;

import exceptions.SyntaxException;
import parser.ast.*;
import parser.ast.expression.*;
import parser.ast.statement.*;

import java.util.List;

public final class Parser {

    private static final Token EOF = new Token(TokenType.EOF, "", -1, -1);
    private final List<Token> tokens;
    private final int size;
    private int pos;

    private Parser(List<Token> tokens) {
        this.tokens = tokens;
        size = tokens.size();
    }

    public static Statement parse(List<Token> tokens) {
        return new Parser(tokens).parse();
    }

    private Statement parse() {
        final BlockStatement result = new BlockStatement();
        while (!match(TokenType.EOF)) {
            result.add(statement());
        }
        return result;
    }

    private Statement statementOrBlock() {
        if (lookMatch(0, TokenType.LEFT_BRACE)) return block();
        return statement();
    }

    private Statement statement() {
        if (match(TokenType.IF)) {
            return ifElse();
        }
        if (match(TokenType.INT)) {
            return defineInt();
        }
        if (match(TokenType.BOOL)) {
            return defineBool();
        }

        return assignmentStatement();
    }

    private Statement block() {
        final BlockStatement block = new BlockStatement();
        consume(TokenType.LEFT_BRACE);
        while (!match(TokenType.RIGHT_BRACE)) {
            block.add(statement());
        }
        return block;
    }

    private Statement assignmentStatement() {
        if (lookMatch(0, TokenType.MUL) && lookMatch(1, TokenType.VAR) && lookMatch(2, TokenType.ASSIGN)) {
            consume(TokenType.MUL);
            String variable = consume(TokenType.VAR).getText();
            consume(TokenType.ASSIGN);
            Statement s = new AssignmentStatement(variable, expression(), true);
            Variables.assign(variable);
            match(TokenType.SEMI_COLON);
            return s;
        }
        if (lookMatch(0, TokenType.VAR) && lookMatch(1, TokenType.ASSIGN)) {
            String variable = consume(TokenType.VAR).getText();
            consume(TokenType.ASSIGN);
            Statement s = new AssignmentStatement(variable, expression(), false);
            Variables.assign(variable);
            match(TokenType.SEMI_COLON);
            return s;
        }
        throw new SyntaxException(get(0).getRow(), get(0).getCol(), "Unknown statement: " + get(0));
    }

    private Statement defineInt() {

        if (lookMatch(0, TokenType.MUL) && lookMatch(1, TokenType.VAR) && lookMatch(2, TokenType.SEMI_COLON)) {
            consume(TokenType.MUL);
            String variable = consume(TokenType.VAR).getText();
            consume(TokenType.SEMI_COLON);
            return new DefineStatement(Variables.define(variable, DataType.Int, true), null, true);
        }

        if (lookMatch(0, TokenType.MUL) && lookMatch(1, TokenType.VAR) && lookMatch(2, TokenType.ASSIGN)) {
            consume(TokenType.MUL);
            String variable = consume(TokenType.VAR).getText();
            consume(TokenType.ASSIGN);
            Statement s = new DefineStatement(Variables.define(variable, DataType.Int, true), expression(), true);
            Variables.assign(variable);
            match(TokenType.SEMI_COLON);
            return s;
        }

        if (lookMatch(0, TokenType.VAR) && lookMatch(1, TokenType.SEMI_COLON)) {
            String variable = consume(TokenType.VAR).getText();
            consume(TokenType.SEMI_COLON);
            return new DefineStatement(Variables.define(variable, DataType.Int, false), null, false);
        }

        if (lookMatch(0, TokenType.VAR) && lookMatch(1, TokenType.ASSIGN)) {
            String variable = consume(TokenType.VAR).getText();
            consume(TokenType.ASSIGN);
            Statement s = new DefineStatement(Variables.define(variable, DataType.Int, false), expression(), false);
            Variables.assign(variable);
            match(TokenType.SEMI_COLON);
            return s;
        }
        throw new SyntaxException(get(0).getRow(), get(0).getCol(), "Unknown statement: " + get(0));
    }

    private Statement defineBool() {
        if (lookMatch(0, TokenType.VAR) && lookMatch(1, TokenType.SEMI_COLON)) {
            String variable = consume(TokenType.VAR).getText();
            consume(TokenType.SEMI_COLON);
            return new DefineStatement(Variables.define(variable, DataType.Bool, false), null, false);
        }
        if (lookMatch(0, TokenType.VAR) && lookMatch(1, TokenType.ASSIGN)) {
            String variable = consume(TokenType.VAR).getText();
            consume(TokenType.ASSIGN);
            Statement s = new DefineStatement(Variables.define(variable, DataType.Bool, false), expression(), false);
            Variables.assign(variable);
            match(TokenType.SEMI_COLON);
            return s;
        }
        throw new SyntaxException(get(0).getRow(), get(0).getCol(), "Unknown statement: " + get(0));
    }

    private Statement ifElse() {
        final Expression condition = expression();
        final Statement ifStatement = statementOrBlock();
        final Statement elseStatement;
        if (match(TokenType.ELSE)) {
            elseStatement = statementOrBlock();
        } else {
            elseStatement = null;
        }
        return new IfStatement(condition, ifStatement, elseStatement);
    }

    private Expression expression() {
        return ternary();
    }

    private Expression ternary() {
        Expression result = logicalOr();

        if (match(TokenType.QUESTION)) {
            final Expression trueExpr = expression();
            consume(TokenType.COLON);
            final Expression falseExpr = expression();
            Expression e = new TernaryExpression(result, trueExpr, falseExpr);
            consume(TokenType.SEMI_COLON);
            return e;
        }

        return result;
    }

    private Expression logicalOr() {
        Expression result = logicalAnd();

        while (true) {
            if (match(TokenType.OR_LOGICAL)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.OR, result, logicalAnd());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression logicalAnd() {
        Expression result = bitwiseOr();

        while (true) {
            if (match(TokenType.AND_LOGICAL)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.AND, result, bitwiseOr());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression bitwiseOr() {
        Expression expression = bitwiseXor();

        while (true) {
            if (match(TokenType.OR)) {
                expression = new BinaryExpression(BinaryExpression.Operator.OR, expression, bitwiseXor());
                continue;
            }
            break;
        }

        return expression;
    }

    private Expression bitwiseXor() {
        Expression expression = bitwiseAnd();

        while (true) {
            if (match(TokenType.XOR)) {
                expression = new BinaryExpression(BinaryExpression.Operator.XOR, expression, bitwiseAnd());
                continue;
            }
            break;
        }

        return expression;
    }

    private Expression bitwiseAnd() {
        Expression expression = equality();

        while (true) {
            if (match(TokenType.AND)) {
                expression = new BinaryExpression(BinaryExpression.Operator.AND, expression, equality());
                continue;
            }
            break;
        }

        return expression;
    }

    private Expression equality() {
        Expression result = conditional();

        if (match(TokenType.EQUALS)) {
            return new ConditionalExpression(ConditionalExpression.Operator.EQUALS, result, conditional());
        }
        if (match(TokenType.NOT_EQUALS)) {
            return new ConditionalExpression(ConditionalExpression.Operator.NOT_EQUALS, result, conditional());
        }

        return result;
    }

    private Expression conditional() {
        Expression result = shift();

        while (true) {
            if (match(TokenType.LESS)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.LT, result, shift());
                continue;
            }
            if (match(TokenType.LESS_EQUALS)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.LTEQ, result, shift());
                continue;
            }
            if (match(TokenType.MORE)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.GT, result, shift());
                continue;
            }
            if (match(TokenType.MORE_EQUALS)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.GTEQ, result, shift());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression shift() {
        Expression expression = additive();

        while (true) {
            if (match(TokenType.LEFT_SHIFT)) {
                expression = new BinaryExpression(BinaryExpression.Operator.LSHIFT, expression, additive());
                continue;
            }
            if (match(TokenType.RIGHT_SHIFT)) {
                expression = new BinaryExpression(BinaryExpression.Operator.RSHIFT, expression, additive());
                continue;
            }
            break;
        }

        return expression;
    }

    private Expression additive() {
        Expression result = multiplicative();

        while (true) {
            if (match(TokenType.PLUS)) {
                result = new BinaryExpression(BinaryExpression.Operator.ADD, result, multiplicative());
                continue;
            }
            if (match(TokenType.MINUS)) {
                result = new BinaryExpression(BinaryExpression.Operator.SUBTRACT, result, multiplicative());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression multiplicative() {
        Expression result = unary();

        while (true) {
            if (match(TokenType.MUL)) {
                result = new BinaryExpression(BinaryExpression.Operator.MULTIPLY, result, unary());
                continue;
            }
            if (match(TokenType.DIV)) {
                result = new BinaryExpression(BinaryExpression.Operator.DIVIDE, result, unary());
                continue;
            }
            if (match(TokenType.PERCENT)) {
                result = new BinaryExpression(BinaryExpression.Operator.REMAINDER, result, unary());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression unary() {
        if (match(TokenType.AND)) {
            String address = consume(TokenType.VAR).getText();
            return new AddressExpression((Variable) Variables.get(address));
        }
        if (match(TokenType.MUL)) {
            String pointer = consume(TokenType.VAR).getText();
            return new PointerExpression((Pointer) Variables.get(pointer));
        }
        if (match(TokenType.MINUS)) {
            return new UnaryExpression(UnaryExpression.Operator.NEGATE, primary());
        }
        if (match(TokenType.NOT)) {
            return new UnaryExpression(UnaryExpression.Operator.NOT, primary());
        }
        if (match(TokenType.PLUS)) {
            return primary();
        }
        return primary();
    }

    private Expression primary() {
        final Token current = get(0);

        if (match(TokenType.TRUE)) {
            return new BoolExpression(new BoolValue(true, DataType.Bool));
        }
        if (match(TokenType.FALSE)) {
            return new BoolExpression(new BoolValue(false, DataType.Bool));
        }
        if (match(TokenType.NUMBER)) {
            return new NumberExpression(new NumberValue(Integer.parseInt(current.getText()), DataType.Int));
        }
        if (match(TokenType.VAR)) {
            return new VariableExpression((Variable) Variables.get(current.getText()));
        }
        if (match(TokenType.LEFT_ROUND_BRACKET)) {
            Expression result = expression();
            match(TokenType.RIGHT_ROUND_BRACKET);
            return result;
        }
        throw new SyntaxException(current.getRow(), current.getCol(), "Unknown expression: " + current);
    }

    private Token consume(TokenType type) {
        final Token current = get(0);
        if (type != current.getType())
            throw new SyntaxException(current.getRow(), current.getCol(), "Token " + current + " doesn't match " + type);
        pos++;
        return current;
    }

    private boolean match(TokenType type) {
        final Token current = get(0);
        if (type != current.getType()) return false;
        pos++;
        return true;
    }

    private boolean lookMatch(int pos, TokenType type) {
        return get(pos).getType() == type;
    }

    private Token get(int relativePosition) {
        final int position = pos + relativePosition;
        if (position >= size) return EOF;
        return tokens.get(position);
    }
}