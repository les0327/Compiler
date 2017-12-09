package parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class NumberValue implements Value {
    @Getter
    @Setter
    private Number value;
    @Getter
    private DataType type;

    @Override
    public boolean isAssigned() {
        return true;
    }
    @Override
    public String getName() {
        return value.toString();
    }
    @Override
    public boolean isPointer() {
        return false;
    }

    @Override
    public void setAssigned(boolean value) {

    }
}
