package parser.ast.expression;

import exceptions.SemanticException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import parser.ast.DataType;
import parser.ast.NumberValue;

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
        if (type1 == DataType.Bool) {
            throw new SemanticException("Unsupported operation '" + operation + "' for type " + type1);
        }
        if (type2 == DataType.Bool) {
            throw new SemanticException("Unsupported operation '" + operation + "' for type " + type2);
        }
        return DataType.Int;
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

        if (expr1 instanceof NumberExpression && expr2 instanceof NumberExpression) {
            Integer n1 = (Integer) ((NumberExpression) expr1).getValue().getValue();
            Integer n2 = (Integer) ((NumberExpression) expr2).getValue().getValue();
            Integer result = null;
            switch (operation) {
                case ADD:
                    result = n1 + n2;
                    break;
                case SUBTRACT:
                    result = n1 - n2;
                    break;
                case MULTIPLY:
                    result = n1 * n2;
                    break;
                case DIVIDE:
                    result = n1 / n2;
                    break;
                case REMAINDER:
                    result = n1 % n2;
                    break;
                case OR:
                    result = n1 | n2;
                    break;
                case AND:
                    result = n1 & n2;
                    break;
                case XOR:
                    result = n1 ^ n2;
                    break;
                case LSHIFT:
                    result = n1 << n2;
                    break;
                case RSHIFT:
                    result = n1 >> n2;
                    break;
            }
            return new NumberExpression(new NumberValue(result, DataType.Int));
        }
        return this;
    }

    @Override
    public String toAsm() {
        if (expr1.isTerminal() && expr2.isTerminal()) {
            switch (operation) {
                case ADD:
                    return "MOV eax, " + expr1.toAsm() + "\nMOV ebx, " + expr2.toAsm() + "\nADD eax, ebx\npush eax\n";
                case SUBTRACT:
                    return "MOV eax, " + expr1.toAsm() + "\nMOV ebx, " + expr2.toAsm() + "\nSUB eax, ebx\npush eax\n";
                case MULTIPLY:
                    return "MOV eax, " + expr1.toAsm() + "\nMOV ebx, " + expr2.toAsm() + "\nMUL ebx\npush eax\n";
                case DIVIDE:
                    return "MOV eax, " + expr1.toAsm() + "\nMOV ebx, " + expr2.toAsm() + "\nXOR edx, edx\nDIV ebx\npush eax\n";
                case REMAINDER:
                    return "MOV eax, " + expr1.toAsm() + "\nMOV ebx, " + expr2.toAsm() + "\nXOR edx, edx\nDIV ebx\npush edx\n";
                case OR:
                    return "MOV eax, " + expr1.toAsm() + "\nMOV ebx, " + expr2.toAsm() + "\nOR  eax, ebx\npush eax\n";
                case AND:
                    return "MOV eax, " + expr1.toAsm() + "\nMOV ebx, " + expr2.toAsm() + "\nAND eax, ebx\npush eax\n";
                case XOR:
                    return "MOV eax, " + expr1.toAsm() + "\nMOV ebx, " + expr2.toAsm() + "\nXOR eax, ebx\npush eax\n";
                case LSHIFT:
                    return "MOV eax, " + expr1.toAsm() + "\nMOV ecx, " + expr2.toAsm() + "\nSHL eax, cl\npush eax\n";
                case RSHIFT:
                    return "MOV eax, " + expr1.toAsm() + "\nMOV ebx, " + expr2.toAsm() + "\nSHR eax, cl\npush eax\n";
            }
        }
        if (expr1.isTerminal() && !expr2.isTerminal()) {
            switch (operation) {
                case ADD:
                    return "MOV eax, " + expr1.toAsm() + "\npop ebx\nADD eax, ebx\npush eax\n";
                case SUBTRACT:
                    return "MOV eax, " + expr1.toAsm() + "\npop ebx\nSUB eax, ebx\npush eax\n";
                case MULTIPLY:
                    return "MOV eax, " + expr1.toAsm() + "\npop ebx\nMUL ebx\npush eax\n";
                case DIVIDE:
                    return "MOV eax, " + expr1.toAsm() + "\npop ebx\nXOR edx, edx\nDIV ebx\npush eax\n";
                case REMAINDER:
                    return "MOV eax, " + expr1.toAsm() + "\npop ebx\nXOR edx, edx\nDIV ebx\npush edx\n";
                case OR:
                    return "MOV eax, " + expr1.toAsm() + "\npop ebx\nOR  eax, ebx\npush eax\n";
                case AND:
                    return "MOV eax, " + expr1.toAsm() + "\npop ebx\nAND eax, ebx\npush eax\n";
                case XOR:
                    return "MOV eax, " + expr1.toAsm() + "\npop ebx\nXOR eax, ebx\npush eax\n";
                case LSHIFT:
                    return "MOV eax, " + expr1.toAsm() + "\npop ecx\nSHL eax, cl\npush eax\n";
                case RSHIFT:
                    return "MOV eax, " + expr1.toAsm() + "\npop ecx\nSHR eax, cl\npush eax\n";
            }
        }
        if (!expr1.isTerminal() && expr2.isTerminal()) {
            switch (operation) {
                case ADD:
                    return "pop eax\nMOV ebx, " + expr2.toAsm() + "\nADD eax, ebx\npush eax\n";
                case SUBTRACT:
                    return "pop eax\nMOV ebx, " + expr2.toAsm() + "\nSUB eax, ebx\npush eax\n";
                case MULTIPLY:
                    return "pop eax\nMOV ebx, " + expr2.toAsm() + "\nMUL ebx\npush eax\n";
                case DIVIDE:
                    return "pop eax\nMOV ebx, " + expr2.toAsm() + "\nXOR edx, edx\nDIV ebx\npush eax\n";
                case REMAINDER:
                    return "pop eax\nMOV ebx, " + expr2.toAsm() + "\nXOR edx, edx\nDIV ebx\npush edx\n";
                case OR:
                    return "pop eax\nMOV ebx, " + expr2.toAsm() + "\nOR  eax, ebx\npush eax\n";
                case AND:
                    return "pop eax\nMOV ebx, " + expr2.toAsm() + "\nAND eax, ebx\npush eax\n";
                case XOR:
                    return "pop eax\nMOV ebx, " + expr2.toAsm() + "\nXOR eax, ebx\npush eax\n";
                case LSHIFT:
                    return "pop eax\nMOV ecx, " + expr2.toAsm() + "\nSHL eax, cl\npush eax\n";
                case RSHIFT:
                    return "pop eax\nMOV ebx, " + expr2.toAsm() + "\nSHR eax, cl\npush eax\n";
            }
        }
        switch (operation) {
            case ADD:
                return expr1.toAsm() + expr2.toAsm() + "pop ebx\npop eax\nADD eax, ebx\npush eax\n";
            case SUBTRACT:
                return expr1.toAsm() + expr2.toAsm() + "pop ebx\npop eax\nSUB eax, ebx\npush eax\n";
            case MULTIPLY:
                return expr1.toAsm() + expr2.toAsm() + "pop ebx\npop eax\nMUL ebx\npush eax\n";
            case DIVIDE:
                return expr1.toAsm() + expr2.toAsm() + "pop ebx\npop eax\nXOR edx, edx\nDIV ebx\npush eax\n";
            case REMAINDER:
                return expr1.toAsm() + expr2.toAsm() + "pop ebx\npop eax\nXOR edx, edx\nDIV ebx\npush edx\n";
            case OR:
                return expr1.toAsm() + expr2.toAsm() + "pop ebx\npop eax\nOR  eax, ebx\npush eax\n";
            case AND:
                return expr1.toAsm() + expr2.toAsm() + "pop ebx\npop eax\nAND eax, ebx\npush eax\n";
            case XOR:
                return expr1.toAsm() + expr2.toAsm() + "pop ebx\npop eax\nXOR eax, ebx\npush eax\n";
            case LSHIFT:
                return expr1.toAsm() + expr2.toAsm() + "pop ecx\npop eax\nSHL eax, cl\npush eax\n";
            case RSHIFT:
                return expr1.toAsm() + expr2.toAsm() + "pop ecx\npop eax\nSHR eax, cl\npush eax\n";
            default:
                return "";
        }
    }

    @Override
    public String toString() {
        return "(" + expr1 + ")" + operation + "(" + expr2 + ")";
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