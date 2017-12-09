package parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class BoolValue implements Value {
    @Getter
    @Setter
    private boolean value;
    @Getter
    private DataType type;

    @Override
    public String getName() {
        return value + "";
    }

    @Override
    public boolean isAssigned() {
        return true;
    }

    @Override
    public boolean isPointer() {
        return false;
    }

    @Override
    public void setAssigned(boolean value) {

    }
}
