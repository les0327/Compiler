package parser.ast.variables;

import java.util.HashMap;
import java.util.Map;

public final class Variables {

    private static final Map<String, IVariable> variables = new HashMap<>();


    public static boolean isExists(String key) {
        return variables.containsKey(key);
    }

    public static IVariable get(String key) {
        if (!isExists(key)) return null;
        return variables.get(key);
    }

    public static void setValue(String key, double value) {
        IVariable v = Variables.get(key);
        if (v == null) {
            System.out.println("Var does not exist");
            return;
        }
        v.setValue(value);
    }

    public static void setAddress(String key, int address) {
        IVariable v = Variables.get(key);
        if (v == null) {
            System.out.println("Var does not exist");
            return;
        }
        v.setAddress(address);

    }
}
