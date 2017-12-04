package parser.ast;


import exceptions.SemanticException;

import java.util.HashMap;
import java.util.Map;

public class Variables {

    private static Map<String, Variable> variables = new HashMap<>();

    public static Variable define(String name, DataType type) {
        if (isDefined(name))
            throw new SemanticException("Variable '" + name + "' has been already defined");
        variables.put(name, new Variable(name, type, false));
        return variables.get(name);
    }

    public static void assign(String name) {
        variables.get(name).setAssigned(true);
    }

    public static boolean isDefined(String name) {
        return variables.containsKey(name);
    }

    public static boolean isAssigned(String name) {
        return variables.get(name).isAssigned();
    }

    public static Variable get(String name) {
        if (!isDefined(name)) throw new SemanticException("Variable \"" + name + "\" is not defined");
        if (!isAssigned(name)) throw new SemanticException("Variable \"" + name + "\" is not assigned");
        return variables.get(name);
    }
}
