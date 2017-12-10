package parser.ast.statement;

import exceptions.SemanticException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import parser.ast.DataType;
import parser.ast.Value;
import parser.ast.Variables;
import parser.ast.expression.Expression;

@AllArgsConstructor
public class DefineStatement implements Statement {
    @Getter
    private Value variable;
    @Getter
    private Expression expression;
    @Getter
    private boolean forPointer;

    @Override
    public void semanticAnalyse() {
        if (expression != null) {
            DataType expType = expression.returnType();
            DataType varType = Variables.get(variable.getName()).getType();
            if (expType != varType)
                throw new SemanticException("Variable '" + variable + "(" + Variables.get(variable.getName()).getType() + ")' does not match type " + expType);
        }
    }

    @Override
    public Statement optimize() {
        if (expression != null)
            if (expression.canOptimize())
                expression = expression.optimize();
        return this;
    }

    @Override
    public String toAsm() {
        if (expression == null) {
            return "LOCAL " + variable.getName() + ":DWORD\n";
        } else if (expression.isTerminal()) {
            return "LOCAL " + variable.getName() + ":DWORD\n" + "MOV " + variable.getName() + ", " + expression.toAsm() + "\n";
        } else {
            return "LOCAL " + variable.getName() + ":DWORD\n" + expression.toAsm() + "pop " + variable.getName() + "\n";
        }
    }

    @Override
    public String toString() {
        return (variable.getType() == DataType.Int ? "int " : "bool ") + (forPointer ? "*" : "") + variable.getName() + "=" + expression + ";";
    }
}
