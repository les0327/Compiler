package parser.ast.expression;

import exceptions.SemanticException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import parser.ast.BoolValue;
import parser.ast.DataType;

@AllArgsConstructor
public class ConditionalExpression implements Expression {
    @Getter
    private Operator operation;
    @Getter
    private Expression expr1, expr2;

    private static int limit = 0;

    @Override
    public DataType returnType() {
        DataType type1 = expr1.returnType();
        DataType type2 = expr2.returnType();

        if (operation == Operator.AND || operation == Operator.OR)
            if (type1 == DataType.Int || type2 == DataType.Int)
                throw new SemanticException("Unsupported operation '" + operation + "' between " + type1 + " and " + type2);
        if (operation == Operator.EQUALS || operation == Operator.NOT_EQUALS)
            if (type1 != type2)
                throw new SemanticException("Unsupported operation '" + operation + "' between " + type1 + " and " + type2);
        if (operation == Operator.LTEQ || operation == Operator.LT || operation == Operator.GT || operation == Operator.GTEQ)
            if (type1 == DataType.Bool || type2 == DataType.Bool)
                throw new SemanticException("Unsupported operation '" + operation + "' between " + type1 + " and " + type2);

        return DataType.Bool;
    }

    @Override
    public boolean canOptimize() {
        return expr1.canOptimize() || expr2.canOptimize();
    }

    @Override
    public Expression optimize() {
        if (expr1.canOptimize())
            expr1 = expr1.optimize();
        if (expr2.canOptimize())
            expr2 = expr2.optimize();
        if (expr1 instanceof BoolExpression && expr2 instanceof BoolExpression) {
            Boolean b1 = ((BoolExpression) expr1).getValue().isValue();
            Boolean b2 = ((BoolExpression) expr2).getValue().isValue();
            Boolean result = null;
            switch (operation) {
                case EQUALS:
                    result = b1 == b2;
                    break;
                case NOT_EQUALS:
                    result = b1 != b2;
                    break;
                case AND:
                    result = b1 && b2;
                    break;
                case OR:
                    result = b1 || b2;
                    break;
            }
            return new BoolExpression(new BoolValue(result, DataType.Bool));
        }
        if (expr1 instanceof NumberExpression && expr2 instanceof NumberExpression) {
            Integer b1 = (Integer) ((NumberExpression) expr1).getValue().getValue();
            Integer b2 = (Integer) ((NumberExpression) expr2).getValue().getValue();
            Boolean result = null;
            switch (operation) {
                case EQUALS:
                    result = b1.equals(b2);
                    break;
                case NOT_EQUALS:
                    result = !b1.equals(b2);
                    break;
                case LTEQ:
                    result = b1 <= b2;
                    break;
                case LT:
                    result = b1 < b2;
                    break;
                case GTEQ:
                    result = b1 >= b2;
                    break;
                case GT:
                    result = b1 > b2;
                    break;
            }
            return new BoolExpression(new BoolValue(result, DataType.Bool));
        }

        return this;
    }

    @Override
    public String toAsm() {
        if (expr1.isTerminal() && expr2.isTerminal()) {
            switch (operation) {
                case EQUALS:
                    return "MOV eax, " + expr1.toAsm() +
                            "\nMOV ebx, " + expr2.toAsm() +
                            "\nCMP eax, ebx" +
                            "\njne @C" + limit +
                            "\npush 1" +
                            "\njmp @endC" + limit +
                            "\n@C" + limit + ":" +
                            "\npush 0" +
                            "\n@endC" + limit++ + ":\n";
                case NOT_EQUALS:
                    return "MOV eax, " + expr1.toAsm() +
                            "\nMOV ebx, " + expr2.toAsm() +
                            "\nCMP eax, ebx" +
                            "\njne @C" + limit +
                            "\npush 0" +
                            "\njmp @endC" + limit +
                            "\n@C" + limit + ":" +
                            "\npush 1" +
                            "\n@endC" + limit++ + ":\n";
                case AND:
                    return "MOV eax, " + expr1.toAsm() +
                            "\nMOV ebx, " + expr2.toAsm() +
                            "\nAND eax, ebx" +
                            "\npush eax\n";
                case OR:
                    return "MOV eax, " + expr1.toAsm() +
                            "\nMOV ebx, " + expr2.toAsm() +
                            "\nOR  eax, ebx" +
                            "\npush eax\n";
                case LTEQ:
                    return "MOV eax, " + expr1.toAsm() +
                            "\nMOV ebx, " + expr2.toAsm() +
                            "\nCMP eax, ebx" +
                            "\njle @C" + limit +
                            "\npush 0" +
                            "\njmp @endC" + limit +
                            "\n@C" + limit + ":" +
                            "\npush 1" +
                            "\n@endC" + limit++ + ":\n";
                case LT:
                    return "MOV eax, " + expr1.toAsm() +
                            "\nMOV ebx, " + expr2.toAsm() +
                            "\nCMP eax, ebx" +
                            "\njl @C" + limit +
                            "\npush 0" +
                            "\njmp @endC" + limit +
                            "\n@C" + limit + ":" +
                            "\npush 1" +
                            "\n@endC" + limit++ + ":\n";
                case GTEQ:
                    return "MOV eax, " + expr1.toAsm() +
                            "\nMOV ebx, " + expr2.toAsm() +
                            "\nCMP eax, ebx" +
                            "\njge @C" + limit +
                            "\npush 0" +
                            "\njmp @endC" + limit +
                            "\n@C" + limit + ":" +
                            "\npush 1" +
                            "\n@endC" + limit++ + ":\n";
                case GT:
                    return "MOV eax, " + expr1.toAsm() +
                            "\nMOV ebx, " + expr2.toAsm() +
                            "\nCMP eax, ebx" +
                            "\njg @C" + limit +
                            "\npush 0" +
                            "\njmp @endC" + limit +
                            "\n@C" + limit + ":" +
                            "\npush 1" +
                            "\n@endC" + limit++ + ":\n";
            }
        }
        if (expr1.isTerminal() && !expr2.isTerminal()) {
            switch (operation) {
                case EQUALS:
                    return "MOV eax, " + expr1.toAsm() +
                            "\npop ebx"+
                            "\nCMP eax, ebx" +
                            "\njne @C" + limit +
                            "\npush 1" +
                            "\njmp @endC" + limit +
                            "\n@C" + limit + ":" +
                            "\npush 0" +
                            "\n@endC" + limit++ + ":\n";
                case NOT_EQUALS:
                    return "MOV eax, " + expr1.toAsm() +
                            "\npop ebx"+
                            "\nCMP eax, ebx" +
                            "\njne @C" + limit +
                            "\npush 0" +
                            "\njmp @endC" + limit +
                            "\n@C" + limit + ":" +
                            "\npush 1" +
                            "\n@endC" + limit++ + ":\n";
                case AND:
                    return "MOV eax, " + expr1.toAsm() +
                            "\npop ebx"+
                            "\nAND eax, ebx" +
                            "\npush eax\n";
                case OR:
                    return "MOV eax, " + expr1.toAsm() +
                            "\npop ebx"+
                            "\nOR  eax, ebx" +
                            "\npush eax\n";
                case LTEQ:
                    return "MOV eax, " + expr1.toAsm() +
                            "\npop ebx"+
                            "\nCMP eax, ebx" +
                            "\njle @C" + limit +
                            "\npush 0" +
                            "\njmp @endC" + limit +
                            "\n@C" + limit + ":" +
                            "\npush 1" +
                            "\n@endC" + limit++ + ":\n";
                case LT:
                    return "MOV eax, " + expr1.toAsm() +
                            "\npop ebx"+
                            "\nCMP eax, ebx" +
                            "\njl @C" + limit +
                            "\npush 0" +
                            "\njmp @endC" + limit +
                            "\n@C" + limit + ":" +
                            "\npush 1" +
                            "\n@endC" + limit++ + ":\n";
                case GTEQ:
                    return "MOV eax, " + expr1.toAsm() +
                            "\npop ebx"+
                            "\nCMP eax, ebx" +
                            "\njge @C" + limit +
                            "\npush 0" +
                            "\njmp @endC" + limit +
                            "\n@C" + limit + ":" +
                            "\npush 1" +
                            "\n@endC" + limit++ + ":\n";
                case GT:
                    return "MOV eax, " + expr1.toAsm() +
                            "\npop ebx"+
                            "\nCMP eax, ebx" +
                            "\njg @C" + limit +
                            "\npush 0" +
                            "\njmp @endC" + limit +
                            "\n@C" + limit + ":" +
                            "\npush 1" +
                            "\n@endC" + limit++ + ":\n";
            }
        }
        if (!expr1.isTerminal() && expr2.isTerminal()) {
            switch (operation) {
                case EQUALS:
                    return "pop eax" +
                            "\nMOV ebx, " + expr2.toAsm() +
                            "\nCMP eax, ebx" +
                            "\njne @C" + limit +
                            "\npush 1" +
                            "\njmp @endC" + limit +
                            "\n@C" + limit + ":" +
                            "\npush 0" +
                            "\n@endC" + limit++ + ":\n";
                case NOT_EQUALS:
                    return "pop eax" +
                            "\nMOV ebx, " + expr2.toAsm() +
                            "\nCMP eax, ebx" +
                            "\njne @C" + limit +
                            "\npush 0" +
                            "\njmp @endC" + limit +
                            "\n@C" + limit + ":" +
                            "\npush 1" +
                            "\n@endC" + limit++ + ":\n";
                case AND:
                    return "pop eax" +
                            "\nMOV ebx, " + expr2.toAsm() +
                            "\nAND eax, ebx" +
                            "\npush eax\n";
                case OR:
                    return "pop eax" +
                            "\nMOV ebx, " + expr2.toAsm() +
                            "\nOR  eax, ebx" +
                            "\npush eax\n";
                case LTEQ:
                    return "pop eax" +
                            "\nMOV ebx, " + expr2.toAsm() +
                            "\nCMP eax, ebx" +
                            "\njle @C" + limit +
                            "\npush 0" +
                            "\njmp @endC" + limit +
                            "\n@C" + limit + ":" +
                            "\npush 1" +
                            "\n@endC" + limit++ + ":\n";
                case LT:
                    return "pop eax" +
                            "\nMOV ebx, " + expr2.toAsm() +
                            "\nCMP eax, ebx" +
                            "\njl @C" + limit +
                            "\npush 0" +
                            "\njmp @endC" + limit +
                            "\n@C" + limit + ":" +
                            "\npush 1" +
                            "\n@endC" + limit++ + ":\n";
                case GTEQ:
                    return "pop eax" +
                            "\nMOV ebx, " + expr2.toAsm() +
                            "\nCMP eax, ebx" +
                            "\njge @C" + limit +
                            "\npush 0" +
                            "\njmp @endC" + limit +
                            "\n@C" + limit + ":" +
                            "\npush 1" +
                            "\n@endC" + limit++ + ":\n";
                case GT:
                    return "pop eax" +
                            "\nMOV ebx, " + expr2.toAsm() +
                            "\nCMP eax, ebx" +
                            "\njg @C" + limit +
                            "\npush 0" +
                            "\njmp @endC" + limit +
                            "\n@C" + limit + ":" +
                            "\npush 1" +
                            "\n@endC" + limit++ + ":\n";
            }
        }
        switch (operation) {
            case EQUALS:
                return "pop ebx" +
                        "\npop eax" +
                        "\nCMP eax, ebx" +
                        "\njne @C" + limit +
                        "\npush 1" +
                        "\njmp @endC" + limit +
                        "\n@C" + limit + ":" +
                        "\npush 0" +
                        "\n@endC" + limit++ + ":\n";
            case NOT_EQUALS:
                return "pop ebx" +
                        "\npop eax" +
                        "\nCMP eax, ebx" +
                        "\njne @C" + limit +
                        "\npush 0" +
                        "\njmp @endC" + limit +
                        "\n@C" + limit + ":" +
                        "\npush 1" +
                        "\n@endC" + limit++ + ":\n";
            case AND:
                return "pop ebx" +
                        "\npop eax" +
                        "\nAND eax, ebx" +
                        "\npush eax\n";
            case OR:
                return "pop ebx" +
                        "\npop eax" +
                        "\nOR  eax, ebx" +
                        "\npush eax\n";
            case LTEQ:
                return "pop ebx" +
                        "\npop eax" +
                        "\nCMP eax, ebx" +
                        "\njle @C" + limit +
                        "\npush 0" +
                        "\njmp @endC" + limit +
                        "\n@C" + limit + ":" +
                        "\npush 1" +
                        "\n@endC" + limit++ + ":\n";
            case LT:
                return "pop ebx" +
                        "\npop eax" +
                        "\nCMP eax, ebx" +
                        "\njl @C" + limit +
                        "\npush 0" +
                        "\njmp @endC" + limit +
                        "\n@C" + limit + ":" +
                        "\npush 1" +
                        "\n@endC" + limit++ + ":\n";
            case GTEQ:
                return "pop eax" +
                        "\nMOV ebx, " + expr2.toAsm() +
                        "\nCMP eax, ebx" +
                        "\njge @C" + limit +
                        "\npush 0" +
                        "\njmp @endC" + limit +
                        "\n@C" + limit + ":" +
                        "\npush 1" +
                        "\n@endC" + limit++ + ":\n";
            case GT:
                return "pop ebx" +
                        "\npop eax" +
                        "\nCMP eax, ebx" +
                        "\njg @C" + limit +
                        "\npush 0" +
                        "\njmp @endC" + limit +
                        "\n@C" + limit + ":" +
                        "\npush 1" +
                        "\n@endC" + limit++ + ":\n";
        }
        return "";
    }

    @Override
    public String toString() {
        return "(" + expr1 + ")" + operation + "(" + expr2 + ")";
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


        @Override
        public String toString() {
            return getName();
        }
    }
}
