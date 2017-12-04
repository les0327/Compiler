package parser.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Getter;
import parser.ast.DataType;

@AllArgsConstructor
public final class UnaryExpression implements Expression {

    @Getter
    private Operator operation;
    @Getter
    private Expression expr1;

    @Override
    public DataType returnType() {
        return null; // TODO: 04-Dec-17
    }

    @Override
    public String toString() {
        return String.format("%s %s", operation, expr1);
    }

    @Override
    public String toAsm() {
        return null;
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
