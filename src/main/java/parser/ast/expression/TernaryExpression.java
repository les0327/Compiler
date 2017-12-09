package parser.ast.expression;

import exceptions.SemanticException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import parser.ast.DataType;

@AllArgsConstructor
public final class TernaryExpression implements Expression {

    private static int limit = 0;

    @Getter
    private Expression condition;
    @Getter
    private Expression expr1, expr2;

    @Override
    public DataType returnType() {
        if (condition.returnType() != DataType.Bool)
            throw new SemanticException("Ternary: expected bool type, get " + condition.returnType());
        DataType type1 = expr1.returnType();
        DataType type2 = expr2.returnType();
        if (type1 == type2)
            return type1;

        throw new SemanticException("Ternary: return types are not equals '" + type1 + "!=" + type2 + "'");
    }

    @Override
    public boolean canOptimize() {
        return condition.canOptimize() || expr1.canOptimize() || expr2.canOptimize();
    }

    @Override
    public Expression optimize() {
        if (condition.canOptimize())
            condition = condition.optimize();
        if (expr1.canOptimize())
            expr1 = expr1.optimize();
        if (expr2.canOptimize())
            expr2 = expr2.optimize();
        if (condition instanceof BoolExpression) {
            if (((BoolExpression) condition).getValue().isValue()) {
                return expr1;
            } else {
                return expr2;
            }
        }
        return this;
    }

    @Override
    public String toString() {
        return "(" + condition + ")?" + expr1 + ":" + expr2;
    }

    @Override
    public String toAsm() {
        if (expr1.isTerminal() && expr2.isTerminal()) {
            return condition.toAsm() +
                    "pop eax\n" +
                    "CMP eax, 0\n" +
                    "je @ternary" + limit + "\n" +
                    "push " + expr1.toAsm() + "\n" +
                    "jmp @endternary" + limit + "\n" +
                    "@ternary" + limit + ":\n" +
                    "push " + expr2.toAsm() + "\n" +
                    "@endternary" + limit++ + ":\n";
        }
        if (expr1.isTerminal() && !expr2.isTerminal()) {
            return condition.toAsm() +
                    "pop eax\n" +
                    "CMP eax, 0\n" +
                    "je @ternary" + limit + "\n" +
                    "push " + expr1.toAsm() + "\n" +
                    "jmp @endternary" + limit + "\n" +
                    "@ternary" + limit + ":\n" +
                    expr2.toAsm() +
                    "@endternary" + limit++ + ":\n";
        }
        if (!expr1.isTerminal() && expr2.isTerminal()) {
            return condition.toAsm() +
                    "pop eax\n" +
                    "CMP eax, 0\n" +
                    "je @ternary" + limit + "\n" +
                    expr1.toAsm() +
                    "jmp @endternary" + limit + "\n" +
                    "@ternary" + limit + ":\n" +
                    "push " + expr2.toAsm() + "\n" +
                    "@endternary" + limit++ + ":\n";
        }
        return condition.toAsm() +
                "pop eax\n" +
                "CMP eax, 0\n" +
                "je @ternary" + limit + "\n"
                + expr1.toAsm() +
                "jmp @endternary" + limit + "\n" +
                "@ternary" + limit + ": "
                + expr2.toAsm() +
                "@endternary" + limit++ + ":\n";
    }
}
