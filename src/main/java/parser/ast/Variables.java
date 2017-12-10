package parser.ast;


import exceptions.SemanticException;

import java.util.HashMap;
import java.util.Map;

public class Variables {

    private static Map<String, Value> variables = new HashMap<>();

    public static Value define(String name, DataType type, boolean pointer) {
        if (isDefined(name))
            throw new SemanticException("Variable '" + name + "' has been already defined");
        if (pointer)
            variables.put(name, new Pointer(name, type, false, true));
        else
            variables.put(name, new Variable(name, type, false, false));

        return variables.get(name);
    }

    public static void assign(String name) {
        if (!isDefined(name)) throw new SemanticException("Variable \"" + name + "\" is not defined");
        variables.get(name).setAssigned(true);
    }

    private static boolean isDefined(String name) {
        return variables.containsKey(name);
    }

    private static boolean isAssigned(String name) {
        return variables.get(name).isAssigned();
    }

    public static Value get(String name) {
        if (!isDefined(name)) throw new SemanticException("Variable \"" + name + "\" is not defined");
        if (!isAssigned(name)) throw new SemanticException("Variable \"" + name + "\" is not assigned");
        return variables.get(name);
    }
}
