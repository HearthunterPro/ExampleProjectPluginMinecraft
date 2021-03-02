package mc.CushyPro.PluginName;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Data {

	public static JavaPlugin plugin;
	public static String pluginname;
	public static String version;
	public static String author;

	public static File file;
	public static YamlConfiguration cfg;

	public static File getFile(String name) {
		return new File(plugin.getDataFolder(), name);
	}

	public static void LoadConfig() {
		file = getFile("config.yml");
		cfg = YamlConfiguration.loadConfiguration(file);

		MessageType.LoadConfig(cfg);
		ConfigType.LoadConfig(cfg);

		SaveConfig();
	}

	public static void SaveConfig() {
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean ItemisNotNull(ItemStack item) {
		return item != null && item.getType() != Material.AIR;
	}

	public static int getRandom(int lower, int upper) {
		return new Random().nextInt((upper - lower) + 1) + lower;
	}

	public static double getRandom(double lower, double upper) {
		Random r = new Random();
		return lower + (upper - lower) * r.nextDouble();
	}

	public static String getColor(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

	public static Location lookAt(Location loc, Location lookat) {
		loc = loc.clone();
		double dx = lookat.getX() - loc.getX();
		double dy = lookat.getY() - loc.getY();
		double dz = lookat.getZ() - loc.getZ();

		if (dx != 0) {
			if (dx < 0) {
				loc.setYaw((float) (1.5 * Math.PI));
			} else {
				loc.setYaw((float) (0.5 * Math.PI));
			}
			loc.setYaw((float) loc.getYaw() - (float) Math.atan(dz / dx));
		} else if (dz < 0) {
			loc.setYaw((float) Math.PI);
		}

		double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));

		loc.setPitch((float) -Math.atan(dy / dxz));

		loc.setYaw(-loc.getYaw() * 180f / (float) Math.PI);
		loc.setPitch(loc.getPitch() * 180f / (float) Math.PI);

		return loc;
	}

}
