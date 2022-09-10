package me.java.plugins.ezpets.events;

import me.java.plugins.ezpets.pets.ChickenPet;
import me.java.plugins.ezpets.pets.PigPet;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OnPlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException, ClassNotFoundException {
        Player player = event.getPlayer();
        /*player.getInventory().addItem(createEgg("Maiale"));
        player.getInventory().addItem(createEgg("Lupo"));
        player.getInventory().addItem(createEgg("Pollo"));
        player.getInventory().addItem(createEgg("Mucca"));
        player.getInventory().addItem(createEgg("Pecora"));*/
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        List<Entity> nearbyEntities = (List<Entity>) location.getWorld().getNearbyEntities(location, 100, 100, 100);
        for (Entity nearbyEntity : nearbyEntities) {
            if (nearbyEntity.getCustomName() != null) {
                if (nearbyEntity.getCustomName().contains(player.getDisplayName() + "'s")) {
                    nearbyEntity.remove();
                    System.out.println("Removed " + nearbyEntity.getCustomName() + " from " + player.getDisplayName());
                }
            }
        }
    }

    public static ItemStack createEgg(String petType) {
        ItemStack item = new ItemStack(Material.EGG);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Pet Egg");

        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        List<String> lore = new ArrayList<String>();
        lore.add("§fQuesto uovo spawna §d"+petType+"§f da compagnia!");
        lore.add("§fQuesto tuo piccolo amico ti §daiuterà§f in battaglia!");
        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }
}
