package BasicBoard.Placeholders;

import BasicBoard.Main.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

public class Placeholders {

    private final Main plugin;
    public Player player;
    public Placeholders(Main plugin) {
        this.plugin = plugin;
    }

    public String Colorize(Player player, String s) {
        s = ChatColor.translateAlternateColorCodes('&', s);
        s = s.replace("{server_online}", String.valueOf(Bukkit.getOnlinePlayers().size()));
        s = s.replace("{server_max_online}", String.valueOf(Bukkit.getMaxPlayers()));
        if (Double.parseDouble(plugin.getTPS(0)) >= 20) {
            s = s.replace("{server_tps}", ChatColor.translateAlternateColorCodes('&', "&a" + 20 + "*"));
        } else if (Double.parseDouble(plugin.getTPS(0)) == 20) {
            s = s.replace("{server_tps}", ChatColor.translateAlternateColorCodes('&', "&a" + plugin.getTPS(0) + "*"));
        } else if (Double.parseDouble(plugin.getTPS(0)) > 17.0) {
            s = s.replace("{server_tps}", ChatColor.translateAlternateColorCodes('&', "&a" + plugin.getTPS(0)));
        } else if (Double.parseDouble(plugin.getTPS(0)) < 17.0 && Double.parseDouble(plugin.getTPS(0)) > 15.0) {
            s = s.replace("{server_tps}", ChatColor.translateAlternateColorCodes('&', "&e" + plugin.getTPS(0)));
        } else if (Double.parseDouble(plugin.getTPS(0)) < 15.0) {
            s = s.replace("{server_tps}", ChatColor.translateAlternateColorCodes('&', "&c" + plugin.getTPS(0)));
        }

        Long maxRam = Runtime.getRuntime().maxMemory() / 1048576L;
        Long freeRam = Runtime.getRuntime().freeMemory() / 1048576L;
        Long usedRam = (maxRam - freeRam);

        s = s.replace("{server_max_ram}", String.valueOf(maxRam));
        s = s.replace("{server_free_ram}", String.valueOf(freeRam));
        s = s.replace("{server_used_ram}", String.valueOf(usedRam));

        List<World> world = Bukkit.getWorlds();
        for (World value : world) {
            String worldname = value.getName();
            s = s.replace("{" + "<" + worldname + ">" + "_online_players}",
                    String.valueOf(Bukkit.getWorld(worldname).getPlayers().size()));
        }
        try {
            Object field = player.getClass().getMethod("getHandle").invoke(player);
            s = s.replace("{player_ping}", String.valueOf(field.getClass().getDeclaredField("ping").get(field)));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        s = s.replace("{player_world}", player.getWorld().getName());
        s = s.replace("{player_exp}", String.valueOf(player.getExp()));
        s = s.replace("{player_health}", String.valueOf(player.getHealth()));
        s = s.replace("{player_max_health}", String.valueOf(player.getMaxHealth()));
        s = s.replace("{player_food}", String.valueOf(player.getFoodLevel()));
        s = s.replace("{player_kills}", String.valueOf(player.getStatistic(Statistic.PLAYER_KILLS)));
        s = s.replace("{mob_kills}", String.valueOf(player.getStatistic(Statistic.MOB_KILLS)));
        s = s.replace("{player_deaths}", String.valueOf(player.getStatistic(Statistic.DEATHS)));
        s = s.replace("{player_gamemode}", player.getGameMode().name());

        s = s.replace("{player_is_op}", String.valueOf(player.isOp()));
        s = s.replace("{player_item_cursor}", player.getItemOnCursor().toString());
        s = s.replace("{player_last_damage}", String.valueOf(player.getLastDamage()));
        s = s.replace("{player_location_X}", String.valueOf(player.getLocation().getBlockX()));
        s = s.replace("{player_location_Y}", String.valueOf(player.getLocation().getBlockY()));
        s = s.replace("{player_location_Z}", String.valueOf(player.getLocation().getBlockZ()));
        s = s.replace("{player_walk_speed}", String.valueOf(player.getWalkSpeed()));
        s = s.replace("{player_jump}", String.valueOf(player.getStatistic(Statistic.JUMP)));
        s = s.replace("{player_leave_game}", String.valueOf(player.getStatistic(Statistic.LEAVE_GAME)));

        if (player != null) {
            s = s.replace("{player_name}", player.getName());
        }
        s = s.replace("{prefix}", ChatColor.translateAlternateColorCodes('&',
                Objects.requireNonNull(plugin.getConfig().getString("BoardSettings.board-prefix"))));

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")
                && plugin.getConfig().getBoolean("PlaceholderAPI.enable")) {
            s = PlaceholderAPI.setPlaceholders((OfflinePlayer) player, s);
        }
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("Vault")) {
            if (plugin.setupEconomy()) {
                s = s.replace("{player_balance}", String.valueOf(Main.getEconomy().getBalance(player)));
            }
            if (plugin.setupChat()) {
                s = s.replace("{player_prefix}", ChatColor.translateAlternateColorCodes('&', Main.chat.getPlayerPrefix(player)));
                s = s.replace("{player_suffix}",
                        ChatColor.translateAlternateColorCodes('&', Main.chat.getPlayerSuffix(player)));
                s = s.replace("{player_group}", Main.chat.getPrimaryGroup(player));
            }

        }

        return s;
    }
}
