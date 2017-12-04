package parser.ast.statement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import parser.ast.Variable;
import parser.ast.expression.Expression;

@AllArgsConstructor
public class DefineStatement implements Statement {
    @Getter
    private Variable variable;
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
