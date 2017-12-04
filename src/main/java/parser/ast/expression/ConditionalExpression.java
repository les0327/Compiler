package parser.ast.expression;

import exceptions.SemanticException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import parser.ast.DataType;

@AllArgsConstructor
public class ConditionalExpression implements Expression {
    @Getter
    private Operator operation;
    @Getter
    private Expression expr1, expr2;

    @Override
    public DataType returnType() {
        DataType type1 = expr1.returnType();
        DataType type2 = expr2.returnType();

        if ((type1.equals(DataType.Bool) && !type2.equals(DataType.Bool)) || (!type1.equals(DataType.Bool) && type2.equals(DataType.Bool)))
            throw new SemanticException("Unsupported operation '" + operation + "' between " + type1 + " and " + type2);

        return DataType.Bool;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", expr1, operation.getName(), expr2);
    }

    @Override
    public String toAsm() {
        return null;
    }

    public enum Operator {
        EQUALS("=="),
        NOT_EQUALS("!="),

        LT("<"),
        LTEQ("<="),
        GT(">"),
        GTEQ(">="),

        AND("&&"),
        OR("||");

        private final String name;

        Operator(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
