package parser.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Getter;
import parser.ast.DataType;
import parser.ast.Variable;

@AllArgsConstructor
public class AddressExpression implements Expression {
    @Getter
    private Variable address;

    @Override
    public DataType returnType() {
        return address.getType();
    }

    @Override
    public boolean canOptimize() {
        return false;
    }

    @Override
    public Expression optimize() {
        return this;
    }

    @Override
    public String toAsm() {
        return "offset " + address.getName();
    }

    @Override
    public String toString() {
        return "&" + address.getName();
    }
}
