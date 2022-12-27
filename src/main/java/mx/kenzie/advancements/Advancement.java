package mx.kenzie.advancements;

import mx.kenzie.argo.meta.Any;
import mx.kenzie.argo.meta.Optional;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

import java.util.function.Supplier;

public class Advancement extends DataObject {
    
    protected transient final String id;
    public @Optional String parent;
    
    public Display display;
    public @Any Criteria criteria = new DefaultCriteria();
    public @Optional String[][] requirements;
    
    protected Advancement() {
        this.id = null;
    }
    
    public Advancement(String id) {
        this.id = id;
    }
    
    public void setParent(Supplier<NamespacedKey> key) {
        this.parent = key.get().asString();
    }
    
    public void setParent(NamespacedKey key) {
        this.parent = key.asString();
    }
    
    public void setParent(String key) {
        this.parent = key;
    }
    
    public void register(Plugin plugin) {
        final NamespacedKey key = this.getKey(plugin);
        Bukkit.getUnsafe().loadAdvancement(key, this.toString());
    }
    
    public NamespacedKey getKey(Plugin plugin) {
        return new NamespacedKey(plugin, id);
    }
    
    public void unregister(Plugin plugin) {
        final NamespacedKey key = this.getKey(plugin);
        Bukkit.getUnsafe().removeAdvancement(key);
    }
    
}
