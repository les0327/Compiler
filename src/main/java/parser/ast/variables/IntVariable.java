package parser.ast.variables;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class IntVariable implements IVariable<Integer> {

    @Getter @Setter private String name;
    @Getter @Setter private int value;
    @Getter @Setter private int address;

    @Override
    public String toString() {
        return "{name=" + name + ", value=" + value + ", address=" + address + "}";
    }

}
