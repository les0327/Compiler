package parser.ast.statement;

import exceptions.SemanticException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import parser.ast.DataType;
import parser.ast.Variables;
import parser.ast.expression.BoolExpression;
import parser.ast.expression.Expression;

@AllArgsConstructor
public class IfStatement implements Statement {

    @Getter
    private Expression expression;
    @Getter
    private Statement ifStatement, elseStatement;

    private static int limit = 0;

    @Override
    public void semanticAnalyse() {
        DataType type = expression.returnType();
        if (type != DataType.Bool)
            throw new SemanticException("If: expected bool, get '" + expression.returnType() + "'");
        ifStatement.semanticAnalyse();
        if (elseStatement != null)
            elseStatement.semanticAnalyse();
    }

    @Override
    public Statement optimize() {
        if (expression.canOptimize())
            expression = expression.optimize();
        ifStatement.optimize();
        if (elseStatement != null)
            elseStatement = elseStatement.optimize();
        if (expression instanceof BoolExpression) {
            if (((BoolExpression) expression).getValue().isValue()) {
                return ifStatement;
            } else if (elseStatement != null) {
                return elseStatement;
            }
        }
        return this;
    }

    @Override
    public String toAsm() {
        if (elseStatement != null)
        return expression.toAsm() +
                "pop eax\n" +
                "CMP eax, 0\n" +
                "je @if" + limit +"\n"
                + ifStatement.toAsm() +
                "jmp @endIf" + limit + "\n" +
                "@if" + limit + ":\n"
                + elseStatement.toAsm() +
                "@endIf" + limit++ + ":\n";
        else
            return expression.toAsm() +
                    "pop eax\n" +
                    "CMP eax, 0\n" +
                    "je @endIf" + limit +"\n"
                    + ifStatement.toAsm() +
                    "@endIf" + limit++ + ":\n";
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("if ").append(expression).append("{\n\t").append(ifStatement).append("}");
        if (elseStatement != null) {
            result.append("else {\n\t").append(elseStatement).append("\n}\n");
        }
        return result.toString();
    }

}
