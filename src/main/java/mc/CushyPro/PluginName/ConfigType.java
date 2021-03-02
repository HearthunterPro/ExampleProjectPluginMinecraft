package mc.CushyPro.TestPlugin;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public enum ConfigType {

	A("Hello");

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

	ConfigType(Object obj) {
		this.obj = obj;
	}

	@Override
	public String toString() {
		return super.toString().replace("_", ".");
	}

	public Object get() {
		if (cfg.isSet(toString())) {
			return cfg.get(toString());
		}
		return obj;
	}

	public String getString() {
		return (String) get();
	}

	public int getInt() {
		return (int) get();
	}

	public double getDouble() {
		return (double) get();
	}

	public long getLong() {
		return (long) get();
	}

	public float getFloat() {
		return (float) get();
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
		return (boolean) get();
	}

}
