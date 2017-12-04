package parser.ast.expression;

import exceptions.SemanticException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import parser.ast.DataType;

@AllArgsConstructor
public class BinaryExpression implements Expression {

    @Getter
    private Operator operation;
    @Getter
    private Expression expr1, expr2;

    @Override
    public DataType returnType() {
        DataType type1 = expr1.returnType();
        DataType type2 = expr2.returnType();
        if (type1.equals(DataType.Bool)) {
            throw new SemanticException("Unsupported operation '" + operation + "' for type " + type1);
        }
        if (type2.equals(DataType.Bool)) {
            throw new SemanticException("Unsupported operation '" + operation + "' for type " + type2);
        }
        return DataType.Int;
    }

    @Override
    public String toAsm() {
        return null;
    }

    public enum Operator {
        ADD("+"),
        SUBTRACT("-"),
        MULTIPLY("*"),
        DIVIDE("/"),
        REMAINDER("%"),
        // Bitwise
        AND("&"),
        OR("|"),
        XOR("^"),
        LSHIFT("<<"),
        RSHIFT(">>");

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