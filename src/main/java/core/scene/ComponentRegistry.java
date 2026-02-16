package core.scene;

import java.util.*;
import java.util.function.Supplier;

public class ComponentRegistry {
    private static final Map<String, Supplier<Component>> registry = new LinkedHashMap<>();

    public static void register(String name, Supplier<Component> supplier){
        registry.put(name, supplier);
    }

    public static Set<String> getComponentNames(){
        return registry.keySet();
    }

    public static Component create(String name){
        Supplier<Component> supplier = registry.get(name);
        if(supplier==null) return  null;
        return supplier.get();
    }

    public static void autoRegister(String className){
        try{
            Class.forName(className);
        }catch(ClassNotFoundException e){
            throw new RuntimeException("Component not found: " + className);
        }
    }
}
