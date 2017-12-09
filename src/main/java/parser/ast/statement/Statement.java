package parser.ast.statement;

public interface Statement {
    void semanticAnalyse();

    Statement optimize();

    String toAsm();
}
