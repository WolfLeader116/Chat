package io.github.wolfleader116.chat.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import io.github.wolfleader116.chat.ChatPlugin;
import io.github.wolfleader116.chat.Config;
import io.github.wolfleader116.wolfapi.ChatComponent;
import io.github.wolfleader116.wolfapi.ChatElement;
import io.github.wolfleader116.wolfapi.ComponentType;
import io.github.wolfleader116.wolfapi.Message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffC implements CommandExecutor {

	private static final Logger log = Logger.getLogger("Minecraft");

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("staff")) {
			if (ChatPlugin.plugin.getConfig().getBoolean("Staff")) {
				if (!(sender instanceof Player)) {
					log.info("Only a player can run this command!");
				} else {
					if (sender.hasPermission("chat.staff")) {
						Player player = (Player) sender;
						Config c = new Config("playerdata", ChatPlugin.plugin);
						String displayname = c.getConfig().getString("nick." + player.getUniqueId().toString()) + ChatColor.RESET;
						String prefix = ChatPlugin.chat.getPlayerPrefix(player) + ChatColor.RESET;
						String suffix = ChatPlugin.chat.getPlayerSuffix(player);
						String arrow = ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "> " + ChatColor.RESET;
						String splayer = player.getName() + ChatColor.RESET;
						prefix = prefix.replaceAll("&", "§");
						if (args.length == 0) {
							c.getConfig().set("channel." + player.getUniqueId().toString(), 3);
							c.save();
							sender.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.GREEN + "Now in staff chat mode!");
						} else {
							String message = "";
							for(int i = 0; i < args.length; i++) {
								String arg = args[i] + " ";
								message = message + arg;
							}
							if (sender.hasPermission("chat.color")) {
								message = message.replaceAll("&", "§");
							}
							for (Player rec : Bukkit.getOnlinePlayers()) {
								if (rec.hasPermission("chat.staff")) {
									if (c.getConfig().getString("nick." + player.getUniqueId().toString()) == null) {
										String format = ChatPlugin.plugin.getConfig().getString("StaffFormat");
										format = format.replaceAll("%ARROW%", arrow);
										format = format.replaceAll("%PREFIX%", prefix);
										format = format.replaceAll("%SUFFIX%", suffix);
										format = format.replaceAll("%MESSAGE%", message);
										format = format.replaceAll("&", "§");
										List<ChatElement> joinchats = new ArrayList<ChatElement>();
										for (String join : Arrays.asList(format.split("%PLAYER%"))) {
											joinchats.add(new ChatElement(join));
										}
										ChatElement elem = new ChatElement(splayer, new ChatComponent(ComponentType.RUN_COMMAND, "/p " + splayer), new ChatComponent(ComponentType.SHOW_TEXT, ChatColor.GREEN + "Click to Learn More!"));
										for (int i = 1; i < joinchats.size(); i++) {
											joinchats.add(i, elem);
											i++;
										}
										Message.sendJSONMessage(rec, joinchats);
									} else {
										String format = ChatPlugin.plugin.getConfig().getString("StaffFormat");
										format = format.replaceAll("%ARROW%", arrow);
										format = format.replaceAll("%PREFIX%", prefix);
										format = format.replaceAll("%SUFFIX%", suffix);
										format = format.replaceAll("%MESSAGE%", message);
										format = format.replaceAll("&", "§");
										List<ChatElement> joinchats = new ArrayList<ChatElement>();
										for (String join : Arrays.asList(format.split("%PLAYER%"))) {
											joinchats.add(new ChatElement(join));
										}
										ChatElement elem = new ChatElement(displayname, new ChatComponent(ComponentType.RUN_COMMAND, "/p " + splayer), new ChatComponent(ComponentType.SHOW_TEXT, ChatColor.GREEN + "Click to Learn More!"));
										for (int i = 1; i < joinchats.size(); i++) {
											joinchats.add(i, elem);
											i++;
										}
										Message.sendJSONMessage(rec, joinchats);
									}
								}
							}
						}
					} else {
						sender.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.GREEN + "You do not have permission to do this!");
					}
				}
			} else {
				if (!(sender instanceof Player)) {
					log.info("The staff channel has been disabled in the config!");
				} else {
					sender.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.GREEN + "The staff channel has been disabled in the config!");
				}
			}
		}
		return false;
	}
}
