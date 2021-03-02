package mc.CushyPro.TestPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StringCommandRuning {

	public StringCommandRuning(Player player, String cmd) {
		if (cmd.startsWith("[run:")) {
			String time = cmd.substring(cmd.indexOf("[run:") + "[run:".length() + 1, cmd.indexOf("]"));
			try {
				int t = Integer.parseInt(time);
				new BukkitRunnable() {

					@Override
					public void run() {
						RunCommand(player, cmd);
					}
				}.runTaskLater(Data.plugin, t);
			} catch (Exception e) {
				e.printStackTrace();
				player.sendMessage("Error Integer Run time : " + time);
			}
		} else {
			RunCommand(player, cmd);
		}
	}

	private void RunCommand(Player player, String cmd) {
		cmd = cmd.replace("<player>", player.getName());
		if (cmd.startsWith("[cmd]")) {
			cmd = cmd.substring("[cmd]".length());
			Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), cmd);
			return;
		}
		if (cmd.startsWith("[op]")) {
			cmd = cmd.substring("[op]".length());
			if (player.isOp()) {
				Bukkit.dispatchCommand((CommandSender) player, cmd);
			} else {
				player.setOp(true);
				Bukkit.dispatchCommand((CommandSender) player, cmd);
				player.setOp(false);
			}
			return;
		}
		if (cmd.startsWith("[msg]")) {
			cmd = cmd.substring("[msg]".length());
			player.sendMessage(getColor(cmd));
			return;
		}
		if (cmd.startsWith("[sound]")) {
			cmd = cmd.substring("[sound]".length());
			Sound s = Sound.valueOf(cmd.substring(0, cmd.indexOf(":")));
			int data = Integer.parseInt(cmd.substring(cmd.indexOf(":") + 1));
			player.playSound(player.getLocation(), s, 100.0F, data);
			return;
		}
		Bukkit.dispatchCommand((CommandSender) player, cmd);
	}

	private String getColor(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

}
