package parser.ast.statement;

import parser.ast.expression.Expression;

public final class AssignmentStatement implements Statement {

    private final String variable;
    private final Expression expression;

    public AssignmentStatement(String variable, Expression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public void execute() {
        final double result = expression.eval();
//        Variables.set(variables, result);
    }

    @Override
    public String toString() {
        return String.format("%s = %s", variable, expression);
    }
}
