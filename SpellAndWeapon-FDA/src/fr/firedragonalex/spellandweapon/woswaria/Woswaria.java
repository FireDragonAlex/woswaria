package fr.firedragonalex.spellandweapon.woswaria;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import fr.firedragonalex.spellandweapon.Main;
import fr.firedragonalex.spellandweapon.custom.easytoadd.Craft;
import fr.firedragonalex.spellandweapon.element.ElementType;

public class Woswaria {
	
	public static HashMap<DamageCause, ElementType> damageCauseToElementType;
	public static HashMap<Material, ItemStack> newDropOre;
	public static HashMap<String, Location> nameToLocationSpawnPoint;

	public static void enabled() {
		Woswaria.newDropOre = new HashMap<>();
		Woswaria.damageCauseToElementType = new HashMap<>();
		Woswaria.nameToLocationSpawnPoint = new HashMap<>();
		
		Craft.loadRecipes();
		
		Woswaria.createNewDropOre();
		Woswaria.createDamageCauseToElementType();
		Woswaria.createNameToLocationSpawnPoint();
		Woswaria.loadNewWorlds();
	}
	
	private static void loadNewWorlds() {
		Main.getInstance().getServer().createWorld(new WorldCreator("WoswariaBetweenWorld"));
		Main.getInstance().getServer().createWorld(new WorldCreator("WoswariaTestWorld"));
		Main.getInstance().getServer().createWorld(new WorldCreator("WoswariaFarmWorld"));
	}
	
	private static void createNewDropOre() {
		Woswaria.newDropOre.put(Material.DEEPSLATE_DIAMOND_ORE, Craft.customItem(Material.DIAMOND,ChatColor.AQUA+"Givralite"));
	}
	
	private static void createDamageCauseToElementType() {
		Woswaria.damageCauseToElementType.put(DamageCause.FIRE_TICK, ElementType.FIRE);
		Woswaria.damageCauseToElementType.put(DamageCause.FIRE, ElementType.FIRE);
		Woswaria.damageCauseToElementType.put(DamageCause.LAVA, ElementType.FIRE);
		Woswaria.damageCauseToElementType.put(DamageCause.FREEZE, ElementType.ICE);
		Woswaria.damageCauseToElementType.put(DamageCause.SUFFOCATION, ElementType.STONE);
		Woswaria.damageCauseToElementType.put(DamageCause.FALL, ElementType.STONE);
		Woswaria.damageCauseToElementType.put(DamageCause.POISON, ElementType.VEGETAL);
		Woswaria.damageCauseToElementType.put(DamageCause.CONTACT, ElementType.VEGETAL);
		Woswaria.damageCauseToElementType.put(DamageCause.DROWNING, ElementType.WATER);
		Woswaria.damageCauseToElementType.put(DamageCause.LIGHTNING, ElementType.ELECTRICITY);
	}
	
	private static void createNameToLocationSpawnPoint() {
		Woswaria.nameToLocationSpawnPoint.put(ChatColor.YELLOW+"Aller au centre du monde Woswaria", new Location(Bukkit.getWorld("WoswariaWorld"), 0, 70, 0));
		Woswaria.nameToLocationSpawnPoint.put(ChatColor.YELLOW+"Aller au centre de l'overworld (monde normal)", new Location(Bukkit.getWorld("WoswariaFarmWorld"), 0, 100, 0));
		Woswaria.nameToLocationSpawnPoint.put(ChatColor.YELLOW+"Aller au nether", new Location(Bukkit.getWorld("WoswariaWorld_Nether"), 0, 100, 0));
		Woswaria.nameToLocationSpawnPoint.put(ChatColor.YELLOW+"Aller � la zone de test", new Location(Bukkit.getWorld("WoswariaTestWorld"), -56, 66, -30));
	}
	
//	public static HashMap<DamageCause, ElementType> getDamageCauseToElementType() {
//		return Woswaria.damageCauseToElementType;
//	}
//	
//	public static HashMap<Material, ItemStack> getNewDropOre() {
//		return Woswaria.newDropOre;
//	}
}
