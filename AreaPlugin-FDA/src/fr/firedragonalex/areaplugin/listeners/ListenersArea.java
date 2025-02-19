package fr.firedragonalex.areaplugin.listeners;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.firedragonalex.areaplugin.MainAreaPlugin;
import fr.firedragonalex.areaplugin.area.Area;
import fr.firedragonalex.areaplugin.farmarea.FarmArea;

public class ListenersArea implements Listener {
	
	private MainAreaPlugin mainAreaPlugin;

	public ListenersArea(MainAreaPlugin mainAreaPlugin) {
		this.mainAreaPlugin = mainAreaPlugin;
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onInteractSpecialMaterials(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		
		if (action==Action.LEFT_CLICK_BLOCK || action==Action.RIGHT_CLICK_BLOCK) {
			Block block = event.getClickedBlock();
			/////////////easter egg//////////////
			if (mainAreaPlugin.getConfig().getBoolean("easter_egg")) {
				if (block.getType()==Material.SPONGE) {
					player.sendMessage("<Eponge que tu as frapp�> Aie !");
				}
			}
			/////////////easter egg//////////////
			if (player.hasPermission("area.edit")) {
				return;
			}
			for (Material material : mainAreaPlugin.getMaterialChests()) {
				if (block.getType()==material) {
					for (Area area : mainAreaPlugin.getAllAreas()) {
						if (area.isInTheArea(block.getLocation())){
							if (player.getUniqueId().equals(area.getOwner())) {
								event.setCancelled(false);
								return;
							}
							if (area.getMatesCanOpenChests()) {
								for (UUID mate : area.getMates()) {
									if (player.getUniqueId().equals(mate)) {
										event.setCancelled(false);
										return;
									}
								}
							}
							if (area.getEveryoneCanOpenChests()) {
								event.setCancelled(false);
								return;
							}else {
								event.setCancelled(true);
								return;
							}
						}
					}
				}
			}
			for (Material material : mainAreaPlugin.getMaterialDoors()) {
				if (block.getType()==material) {
					for (Area area : mainAreaPlugin.getAllAreas()) {
						if (area.isInTheArea(block.getLocation())){
							if (player.getUniqueId().equals(area.getOwner())) {
								event.setCancelled(false);
								return;
							}
							if (area.getMatesCanOpenDoors()) {
								for (UUID mate : area.getMates()) {
									if (player.getUniqueId().equals(mate)) {
										event.setCancelled(false);
										return;
									}
								}
							}
							if (area.getEveryoneCanOpenDoors()) {
								event.setCancelled(false);
								return;
							}else {
								event.setCancelled(true);
								return;
							}
						}
					}
				}
			}
			for (Material material : mainAreaPlugin.getMaterialRedstone()) {
				if (block.getType()==material) {
					for (Area area : mainAreaPlugin.getAllAreas()) {
						if (area.isInTheArea(block.getLocation())){
							if (player.getUniqueId().equals(area.getOwner())) {
								event.setCancelled(false);
								return;
							}
							if (area.getMatesCanUseRedstone()) {
								for (UUID mate : area.getMates()) {
									if (player.getUniqueId().equals(mate)) {
										event.setCancelled(false);
										return;
									}
								}
							}
							if (area.getEveryoneCanUseRedstone()) {
								event.setCancelled(false);
								return;
							}else {
								event.setCancelled(true);
								return;
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (player.hasPermission("area.edit") || (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK)) {
			return;
		}
		for (Area area : mainAreaPlugin.getAllAreas()) {
			if (area.isInTheArea(event.getClickedBlock().getLocation())) {
				if(player.getUniqueId().equals(area.getOwner())) return;
				if (area.getMatesCanBreakAndPlaceBlocks()) {
					for (UUID mate : area.getMates()) {
						if (player.getUniqueId().equals(mate)) {
							return;
						}
					}
				}
				event.setCancelled(true);
				return;
			}
		}
		if (mainAreaPlugin.getIsWildernessUnbreakable()) {
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (player.hasPermission("area.edit")) {
			return;
		}
		for (Area area : mainAreaPlugin.getAllAreas()) {
			if (area.isInTheArea(event.getBlock().getLocation())) {
				if(player.getUniqueId().equals(area.getOwner())) return;
				if (area.getMatesCanBreakAndPlaceBlocks()) {
					for (UUID mate : area.getMates()) {
						if (player.getUniqueId().equals(mate)) {
							return;
						}
					}
				}
				if (area instanceof FarmArea) {
					Block block = event.getBlock();
					if (area.isInTheArea(block.getLocation())) {
					    FarmArea farmArea = (FarmArea)area;
					    if (farmArea.isThisBlockFarmable(block)) {
					    	farmArea.minableBlockActivate(block);
							event.setCancelled(false);
							return;
						} else {
							event.getPlayer().sendMessage("�cD�sol�, tu ne peux pas casser des blocks ici !");
						}
					}
				} else {
					player.sendMessage("�cD�sol�, tu ne peux pas casser des blocks ici !");
					//System.out.println("[Areaplugin-FDA] "+event.getPlayer().getName()+" try break a block but it was cancelled.");
				}
				event.setCancelled(true);
				return;
			}
		}
		if (mainAreaPlugin.getIsWildernessUnbreakable()) {
			player.sendMessage("�cD�sol�, tu ne peux pas casser des blocks dans la nature !");
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (player.hasPermission("area.edit")) {
			return;
		}
		if (event.getBlock().getType()==Material.TNT && !mainAreaPlugin.getPlayerCanPlaceTnt()) {
			player.sendMessage("�c[AreeaPlugin-FDA]D�sol�, les TNT sont d�sactiv�s !");
			event.setCancelled(true);
			return;
		}
		for (Area area : mainAreaPlugin.getAllAreas()) {
			if (area.isInTheArea(event.getBlock().getLocation())) {
				if(player.getUniqueId().equals(area.getOwner())) return;
				if (area.getMatesCanBreakAndPlaceBlocks()) {
					for (UUID mate : area.getMates()) {
						if (player.getUniqueId().equals(mate)) {
							return;
						}
					}
				}
				player.sendMessage("�cD�sol�, tu ne peux pas poser des blocks ici !");
				//System.out.println("[Areaplugin-FDA] "+event.getPlayer().getName()+" try place a block but it was cancelled.");
				event.setCancelled(true);
				return;
			}
		}
		if (mainAreaPlugin.getIsWildernessUnbreakable()) {
			player.sendMessage("�cD�sol�, tu ne peux pas poser des blocks dans la nature !");
			event.setCancelled(true);
			return;
		}
	}

}
