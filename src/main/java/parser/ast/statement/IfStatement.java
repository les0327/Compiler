package parser.ast.statement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import parser.ast.expression.Expression;

@AllArgsConstructor
public class IfStatement implements Statement {

    @Getter
    private Expression expression;
    @Getter
    private Statement ifStatement, elseStatement;


    @Override
    public void semanticAnalyse() {

    }

    @Override
    public String toAsm() {
        return null;
    }

}
