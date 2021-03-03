package mc.CushyPro.PluginName;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("ALL")
public enum ConfigType {

    A("Test-Config", "hello world");

    public static YamlConfiguration cfg;

    public static void LoadConfig(YamlConfiguration yaml) {
        cfg = yaml;
        for (ConfigType type : values()) {
            if (!cfg.isSet(type.toString())) {
                cfg.set(type.toString(), type.obj);
            }
        }
    }

    private Object obj;
    private String prefix;

    ConfigType(String prefix, Object obj) {
        this.prefix = prefix;
        this.obj = obj;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public Object get() {
        if (cfg.isSet(prefix)) {
            return cfg.get(prefix);
        }
        return obj;
    }

    public String getString() {
        return get().toString();
    }

    public int getInt() {
        return (Integer) get();
    }

    public double getDouble() {
        return (Double) get();
    }

    public long getLong() {
        return (Long) get();
    }

    public float getFloat() {
        return (Float) get();
    }

    @SuppressWarnings("unchecked")
    public List<String> getStringList() {
        return (List<String>) get();
    }

    public List<?> getList() {
        return (List<?>) get();
    }

    public ItemStack getItemStack() {
        return (ItemStack) get();
    }

    public Location getLocation() {
        return (Location) get();
    }

    @SuppressWarnings("unchecked")
    public <T> T get(T def) {
        return (get() != null) ? (T) get() : def;
    }

    public boolean getBoolean() {
        return (Boolean) get();
    }

}
