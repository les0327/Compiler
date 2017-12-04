package parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class BoolValue implements Value {
    @Getter
    private boolean value;
    @Getter
    private DataType type;
}
