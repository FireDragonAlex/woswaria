package fr.firedragonalex.rankandlevels.saveandload;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import fr.firedragonalex.rankandlevels.Main;
import fr.firedragonalex.rankandlevels.rank.Rank;
import fr.firedragonalex.spellandweapon.alexlibrairy.UseCsvFiles;

public class LoadRanks {
	
	public static void loadPlayer(Player player) {
		List<List<String>> allLines = UseCsvFiles.load("RanksWoswaria", "");
		for (List<String> line : allLines) {
			UUID uuid = UUID.fromString(line.get(0));
			if (uuid.equals(player.getUniqueId())) {
				Rank rank = Rank.valueOf(line.get(1));
				long timesampExpirationDate = Long.valueOf(line.get(2));
				if (System.currentTimeMillis()*0.001 > timesampExpirationDate) {
					Main.setRank(player, rank);
				} else {
					player.sendMessage(ChatColor.RED+"GRADE "+rank.getColor()+rank.getName()+ChatColor.RED+" EXPIR� ! (plus de 6 mois)");
					Main.setRank(player, Rank.CITOYEN);
					player.sendMessage(ChatColor.YELLOW+"Tu as maintenant le grade "+rank.getColor()+rank.getName()+ChatColor.YELLOW+" !");
				}
				return;
			}
		}
		Main.setRank(player, Rank.CITOYEN);
	}
	
	public static void loadAllPlayers() {
		//load from RanksWoswaria.csv [playerUUID,rank,timesampExpirationDate]
		List<List<String>> allLines = UseCsvFiles.load("RanksWoswaria", "");
		for (Player player : Bukkit.getOnlinePlayers()) {
			boolean isFound = false;
			for (List<String> line : allLines) {
				UUID uuid = UUID.fromString(line.get(0));
				if (uuid.equals(player.getUniqueId())) {
					Rank rank = Rank.valueOf(line.get(1));
					long timesampExpirationDate = Long.valueOf(line.get(2));
					if (System.currentTimeMillis()*0.001 > timesampExpirationDate) {
						Main.setRank(player, rank);
					} else {
						player.sendMessage(ChatColor.RED+"GRADE "+rank.getColor()+rank.getName()+ChatColor.RED+" EXPIR� ! (plus de 6 mois)");
						Main.setRank(player, Rank.CITOYEN);
						player.sendMessage(ChatColor.YELLOW+"Tu as maintenant le grade "+Rank.CITOYEN.getColor()+Rank.CITOYEN.getName()+ChatColor.YELLOW+" !");
					}
					isFound = true;
					break;
				}
			}
			if (!isFound) {
				Main.setRank(player, Rank.CITOYEN);
			}
		}
	}

}
