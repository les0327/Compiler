package parser.ast.statement;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class BlockStatement implements Statement {

    @Getter
    private List<Statement> statements;

    public BlockStatement() {
        statements = new ArrayList<>();
    }

    public void add(Statement statement) {
        statements.add(statement);
    }

    @Override
    public void semanticAnalyse() {
        statements.forEach(Statement::semanticAnalyse);
    }

    @Override
    public Statement optimize() {
        for (int i = 0; i < statements.size(); i++) {
            Statement s = statements.get(i);
            statements.set(i, s.optimize());
        }
        return this;
    }

    @Override
    public String toAsm() {
        StringBuilder sb = new StringBuilder();
        statements.forEach(s -> sb.append(s.toAsm()));
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder s  = new StringBuilder();
        for (Statement st : statements)
            s.append(st.toString()).append("\n");
        return s.toString();
    }
}
