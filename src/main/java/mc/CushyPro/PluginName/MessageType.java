package mc.CushyPro.PluginName;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public enum MessageType {

    YOUNOTHAVEPERMISSION("You-not-have-premission", "&aYou not have Permission!"),
    ;

    public static YamlConfiguration cfg;

    public static void LoadConfig(YamlConfiguration yaml) {
        cfg = yaml;
        for (MessageType type : values()) {
            if (!cfg.isSet("MessageType." + type.getID())) {
                cfg.set("MessageType." + type.getID(), type.msg);
            }
        }
    }

    String msg;
    String prefix;

    MessageType(String prefix, String string) {
        this.prefix = prefix;
        msg = string;
    }

    public String getID() {
        return prefix;
    }

    @Override
    public String toString() {
        return getColor(getMessage());
    }

    String getMessage() {
        if (cfg.isSet(getID())) {
            return cfg.getString(getID());
        }
        return msg;
    }

    @SuppressWarnings("ALL")
    public String toString(String... replace) {
        String mxs = getMessage();
        if (replace.length % 2 == 0) {
            for (int x = 0; x < replace.length / 2; x++) {
                String key = replace[x * 2];
                String value = replace[(x * 2) + 1];
                mxs = mxs.replace(key, value);
            }
        }
        return getColor(mxs);
    }

    public String getColor(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
