package parser.ast.expression;

import parser.ast.DataType;

public interface Expression {
    DataType returnType();

    String toAsm();
}
