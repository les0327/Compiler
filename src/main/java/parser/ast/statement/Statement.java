package parser.ast.statement;

public interface Statement {
    void semanticAnalyse();

    String toAsm();
}
