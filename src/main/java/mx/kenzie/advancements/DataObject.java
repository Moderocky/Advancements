package mx.kenzie.advancements;

import mx.kenzie.argo.Json;

abstract class DataObject {
    
    @Override
    public String toString() {
        return Json.toJson(this);
    }
    
}
