package mc.CushyPro.TestPlugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface CmdCustom {

	}

	private String command;
	private String permission;

	public Commands(String command, Object permission) {
		this.command = command;
		this.permission = (String) permission;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase(command)) {

			if (!sender.hasPermission(this.permission)) {
				sender.sendMessage(MessageType.YOUNOTHAVEPERMISSION.toString());
				return false;
			}
			if (args.length > 0) {
				for (Method m : getClass().getMethods()) {
					if (m.isAnnotationPresent(CmdCustom.class)) {
						if (args[0].equalsIgnoreCase(m.getName())) {
							try {
								m.invoke(null, sender, "/" + label + " " + m.getName(), args);
							} catch (Exception e) {
								e.printStackTrace();
								sender.sendMessage("!Error Commands");
							}
							return true;
						}
					}
				}
				return false;
			}

			for (Method m : getClass().getMethods()) {
				if (m.isAnnotationPresent(CmdCustom.class)) {
					sender.sendMessage("/" + label + " " + m.getName());
				}
			}
			return true;
		}
		return false;
	}

	@CmdCustom
	public static void save(CommandSender sender, String label, String[] args) {
		Data.SaveConfig();
		sender.sendMessage("Save Config");
	}

	@CmdCustom
	public static void reload(CommandSender sender, String label, String[] args) {
		Data.LoadConfig();
		sender.sendMessage("Reload Config");
	}

}
