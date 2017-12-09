package parser.ast.statement;

import exceptions.SemanticException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import parser.ast.DataType;
import parser.ast.Variables;
import parser.ast.expression.Expression;

@AllArgsConstructor
public class AssignmentStatement implements Statement {

    @Getter
    private String variable;
    @Getter
    private Expression expression;
    @Getter
    private boolean forPointer;

    @Override
    public void semanticAnalyse() {
        DataType expType = expression.returnType();
        DataType varType = Variables.get(variable).getType();
        if (expType != varType)
            throw new SemanticException("Variable '" + variable + "(" + Variables.get(variable).getType() + ")' does not match type " + expType);
    }

    @Override
    public Statement optimize() {
        if (expression.canOptimize())
            expression = expression.optimize();
        return this;
    }

    @Override
    public String toAsm() {
        if (expression.isTerminal())
            return "MOV " + variable + ", " + expression.toAsm() + "\n";
        else
            return expression.toAsm() + "pop " + variable + "\n";
    }

    @Override
    public String toString() {
        return variable + "=" + expression + ";";
    }
}