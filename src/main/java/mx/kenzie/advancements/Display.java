package mx.kenzie.advancements;

import mx.kenzie.argo.Json;
import mx.kenzie.argo.meta.Optional;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class Display extends DataObject {
    public final Icon icon = new Icon();
    public @Optional String frame = "task", background;
    public boolean show_toast = true, announce_to_chat = true, hidden = false;
    public Map<String, Object> title = getJson(Component.text("Title")),
        description = getJson(Component.text("Description"));
    
    public Display() {
    }
    
    public void setBackground(String background) {
        if (!background.endsWith(".png")) background = background + ".png";
        if (!background.contains("/")) background = "minecraft:textures/gui/advancements/backgrounds/" + background;
        if (!background.contains(":")) background = "minecraft:" + background;
        this.background = background;
    }
    
    public Component getTitle() {
        return GsonComponentSerializer.gson().deserialize(Json.toJson(title));
    }
    
    public void setTitle(Component component) {
        this.title = getJson(component);
    }
    
    @SuppressWarnings("unchecked")
    private static Map<String, Object> getJson(Component component) {
        final String string = GsonComponentSerializer.gson().serialize(component);
        try (final Json json = new Json(string)) {
            if (string.startsWith("{")) return json.toMap();
            for (Object o : json.toList()) {
                final Map<String, Object> element = (Map<String, Object>) o;
                if (element.size() < 1) continue;
                return element;
            }
        }
        return null;
    }
    
    public Component getDescription() {
        return GsonComponentSerializer.gson().deserialize(Json.toJson(description));
    }
    
    public void setDescription(Component component) {
        this.description = getJson(component);
    }
    
    public class Icon extends DataObject {
        
        private static Class<?> NBTTagCompound;
        private static Method asNMSCopy, save;
        
        static {
            Class<?> itemStack;
            Class<?> craftItemStack;
            try {
                craftItemStack = Class.forName("org.bukkit.craftbukkit." + getCraftVersion() + ".inventory.CraftItemStack");
                NBTTagCompound = Class.forName("net.minecraft.nbt.NBTTagCompound");
                itemStack = Class.forName("net.minecraft.world.item.ItemStack");
                asNMSCopy = craftItemStack.getMethod("asNMSCopy", ItemStack.class);
                for (Method method : itemStack.getMethods()) {
                    if (method.getParameterCount() != 0) continue;
                    if (method.getReturnType() != NBTTagCompound) continue;
                    save = method;
                }
                assert save != null;
            } catch (ClassNotFoundException | NoSuchMethodException ignore) {
                NBTTagCompound = null;
            }
        }
        
        public String item = "minecraft:stone";
        public @Optional String nbt;
        
        private static String getCraftVersion() {
            try {
                return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            } catch (Throwable ex) {
                return "v1_19_R2"; // current, nice fallback
            }
        }
        
        public void setItem(ItemStack item) {
            this.item = item.getType().getKey().asString();
            this.nbt = getNBT(item);
        }
        
        private static String getNBT(ItemStack item) {
            if (NBTTagCompound == null) return null;
            try {
                final Object copy = asNMSCopy.invoke(null, item);
                assert copy != null;
                final Object compound = save.invoke(copy);
                if (compound == null) return "{}";
                return compound.toString();
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        
        public Display getDisplay() {
            return Display.this;
        }
        
    }
}
