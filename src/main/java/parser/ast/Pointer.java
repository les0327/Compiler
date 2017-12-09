package parser.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Pointer implements Value {
    @Getter
    private String name;
    @Getter
    private DataType type;
    @Getter
    @Setter
    private boolean assigned;
    @Getter
    @Setter
    private boolean pointer;
}
