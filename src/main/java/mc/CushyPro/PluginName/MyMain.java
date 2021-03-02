package mc.CushyPro.TestPlugin;

import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

public class MyMain extends JavaPlugin {

    @Override
    public void onEnable() {
        Data.plugin = this;
        Data.pluginname = getDescription().getName();
        Data.version = getDescription().getVersion();
        Data.author = getDescription().getAuthors().get(0);

        for (String st : getDescription().getCommands().keySet()) {
            Map<String, Object> map = getDescription().getCommands().get(st);
            getCommand(st).setExecutor(new Commands(st, map.get("permission")));
        }

        Data.LoadConfig();
    }

    @Override
    public void onDisable() {
        Data.SaveConfig();
    }

}
