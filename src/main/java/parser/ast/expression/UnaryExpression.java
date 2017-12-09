package parser.ast.expression;

import exceptions.SemanticException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import parser.ast.DataType;
import parser.ast.Pointer;

@AllArgsConstructor
public class UnaryExpression implements Expression {

    @Getter
    private Operator operation;
    @Getter
    private Expression expr;

    @Override
    public DataType returnType() {
        DataType type = expr.returnType();
        if (type == DataType.Int && operation == Operator.NEGATE)
            return type;
        if (type == DataType.Bool && operation == Operator.NOT)
            return type;

        throw new SemanticException("Operation '" + operation + "' is unsupported for type '" + type);
    }

    @Override
    public boolean canOptimize() {
        return expr.canOptimize();
    }

    @Override
    public Expression optimize() {
        switch (operation) {
            case NEGATE:
                NumberExpression e = (NumberExpression) expr;
                e.getValue().setValue((Integer) e.getValue().getValue() * -1);
                expr = e;
                break;
            case NOT:
                BoolExpression be = (BoolExpression) expr;
                be.getValue().setValue(!be.getValue().isValue());
                expr = be;
        }
        return expr;
    }

    @Override
    public String toAsm() {
        if (expr instanceof BoolExpression) {
                    return "\tMOV eax, " + expr.toAsm() + "\nXOR eax, 01h\n push eax\n";
        }
        if (expr instanceof VariableExpression || expr instanceof NumberExpression || expr instanceof Pointer) {
                    return "MOV eax, " + expr.toAsm() + "\nXOR eax, ffffffffh\nADD eax, 01h\npush eax\n";
        } else {
            if (operation == Operator.NOT)
                return "pop eax\nXOR eax, ffffffffh\npush eax\n";
            else
                return "pop eax\nXOR eax, ffffffffh\nADD eax, 01h\npush eax\n";
        }
    }

    @Override
    public String toString() {
        return operation + "" + expr.toString();
    }

    public enum Operator {
        //int
        NEGATE("-"),
        // Boolean
        NOT("!");

        private final String name;

        Operator(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
