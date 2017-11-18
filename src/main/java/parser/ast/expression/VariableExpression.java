package parser.ast.expression;

import parser.ast.variables.IVariable;
import parser.ast.variables.Variables;

public final class VariableExpression implements Expression {

    private final IVariable variable;

    public VariableExpression(IVariable variable) {
        this.variable = variable;
    }

    @Override
    public double eval() {
        if (!Variables.isExists(variable.getName())) throw new RuntimeException("Constant does not exists");
        return (Double) Variables.get(variable.getName()).getValue();
    }

    @Override
    public String toString() {
        return String.format("%s", variable);
    }
}
