package parser.ast.statement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import parser.ast.expression.Expression;

@AllArgsConstructor
public class AssignmentStatement implements Statement {

    @Getter
    private String variable;
    @Getter
    private Expression expression;

    @Override
    public void semanticAnalyse() {

    }

    @Override
    public String toAsm() {
        return null;
    }
}