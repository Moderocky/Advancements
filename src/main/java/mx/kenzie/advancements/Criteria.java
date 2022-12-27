package mx.kenzie.advancements;

import mx.kenzie.argo.meta.Name;

import java.util.LinkedHashMap;
import java.util.Map;

public class Criteria {
    
    public final @Name("__data") Map<String, Object> dataset = new LinkedHashMap<>();
    
    
    public static class Criterion {
        public String trigger;
    }
}

class DefaultCriteria extends Criteria {
    
    public final Criterion impossible = new Criterion() {{trigger = "minecraft:impossible";}};
    
}
