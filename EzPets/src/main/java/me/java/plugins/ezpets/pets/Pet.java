package me.java.plugins.ezpets.pets;

import me.java.plugins.ezpets.EzPets;
import me.java.plugins.ezpets.data.PetData;
import me.java.plugins.ezpets.events.OnPlayerJoin;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.*;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.*;

public class Pet implements Listener {
    static EzPets plugin = EzPets.getPlugin(EzPets.class);
    static FileConfiguration config = plugin.getConfig();

    public static List<Entity> petsInGame = new ArrayList<Entity>();

    @EventHandler
    public void onEntityHit(EntityDamageEvent event) {
        if(event.getEntity().getCustomName() == null) return;

        if(event.getEntity().getCustomName().contains("'s")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if(event.getEntity().getCustomName() == null) return;

        if(event.getEntity().getCustomName().contains("'s")) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onSheepEatGrass(EntityChangeBlockEvent e) {
        EntityType sheep = EntityType.SHEEP;
        if(e.getTo().equals(Material.DIRT)) {
            if(e.getEntityType() == sheep) {
                if(e.getEntity().getCustomName() == null) return;

                if(e.getEntity().getCustomName().contains("'s")) {
                    e.setCancelled(true);
                }
            }
        }
    }

    Map<String, Long> cooldowns = new HashMap<String, Long>();
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) throws IOException, ClassNotFoundException {
        if(event.getEntity() instanceof  Player) {

            Player p = (Player) event.getEntity();

            if(!cooldowns.containsKey(p.getDisplayName())) {
                cooldowns.put(p.getDisplayName(), System.currentTimeMillis());
                run(p);
            }else if(cooldowns.containsKey(p.getDisplayName())) {
                long timeElapsed = System.currentTimeMillis() - cooldowns.get(p.getDisplayName());
                if(timeElapsed >= 100000) {
                    cooldowns.put(p.getDisplayName(), System.currentTimeMillis());
                    run(p);
                }
            }
        }
    }

    private void run(Player p) throws IOException, ClassNotFoundException {
        Location location = p.getLocation();
        List<Entity> nearbyEntites = (List<Entity>) location.getWorld().getNearbyEntities(location, 100, 100, 100);

        PetData data = (PetData) EzPets.From_String(config.getString("players.petdata."+p.getUniqueId().toString()));
        for(Entity nearbyEntity : nearbyEntites) {
            if (nearbyEntity.getCustomName() != null) {
                if (nearbyEntity.getCustomName().contains(p.getDisplayName() + "'s pig")) {
                    int level = (int) Math.round(data.getPets().get(nearbyEntity.getName()));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, level * 20, 0, false));
                }

                if (nearbyEntity.getCustomName().contains(p.getDisplayName() + "'s chicken")) {
                    int level = (int) Math.round(data.getPets().get(nearbyEntity.getName()));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, level * 20, 0, false));
                }

                if (nearbyEntity.getCustomName().contains(p.getDisplayName() + "'s wolf")) {
                    int level = (int) Math.round(data.getPets().get(nearbyEntity.getName()));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, level * 20, 0, false));
                }

                if (nearbyEntity.getCustomName().contains(p.getDisplayName() + "'s cow")) {
                    int level = (int) Math.round(data.getPets().get(nearbyEntity.getName()));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, level * 20, 0, false));
                }

                if (nearbyEntity.getCustomName().contains(p.getDisplayName() + "'s sheep")) {
                    int level = (int) Math.round(data.getPets().get(nearbyEntity.getName()));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, level * 20, 0, false));
                }

                if (nearbyEntity.getCustomName().contains(p.getDisplayName() + "'s cat")) {
                    int level = (int) Math.round(data.getPets().get(p.getDisplayName() + "'s cat"));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, level * 20, 0, false));
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) throws IOException, ClassNotFoundException {
        Player p = e.getPlayer();

        if(p.getItemInHand().getType().equals(Material.EGG)) {
            ItemStack item = p.getItemInHand();
            if(!item.hasItemMeta() || !item.getItemMeta().hasLore()) return;

            List<Entity> localEntities = (List<Entity>) p.getWorld().getNearbyEntities(p.getLocation(), 100, 100, 100);
            for(Entity en : localEntities) {
                if(en.getCustomName() != null) {
                    if(en.getCustomName().contains(p.getDisplayName()+"'s")) {
                        en.remove();
                    }
                }
            }

            List<String> lore = item.getItemMeta().getLore();
            if(lore.get(0).contains("Maiale")) {
                PetData data = (PetData) EzPets.From_String(config.getString("players.petdata."+p.getUniqueId().toString()));
                if(data != null) {
                    Location location = p.getLocation();
                    List<Entity> nearbyEntites = (List<Entity>) location.getWorld().getNearbyEntities(location, 100, 100, 100);
                    for(Entity nearbyEntity : nearbyEntites) {
                        if(nearbyEntity.getCustomName() != null) {
                            if(nearbyEntity.getCustomName().contains(p.getDisplayName()+"'s pig")) {
                                p.sendMessage("§cHai già questo pet!");
                                e.setCancelled(true);
                                break;
                            }
                        }
                    }

                    new PigPet(p);
                }else {
                    new PigPet(p);
                }

                p.getInventory().removeItem(OnPlayerJoin.createEgg("Maiale"));
            }else if(lore.get(0).contains("Pollo")) {
                PetData data = (PetData) EzPets.From_String(config.getString("players.petdata."+p.getUniqueId().toString()));
                if(data != null) {
                    Location location = p.getLocation();
                    List<Entity> nearbyEntites = (List<Entity>) location.getWorld().getNearbyEntities(location, 100, 100, 100);
                    for(Entity nearbyEntity : nearbyEntites) {
                        if(nearbyEntity.getCustomName() != null) {
                            if(nearbyEntity.getCustomName().contains(p.getDisplayName()+"'s chicken")) {
                                p.sendMessage("§cHai già questo pet!");
                                e.setCancelled(true);
                                break;
                            }
                        }
                    }

                    new ChickenPet(p);
                }else {
                    new ChickenPet(p);
                }

                p.getInventory().removeItem(OnPlayerJoin.createEgg("Pollo"));
            }else if(lore.get(0).contains("Lupo")) {
                PetData data = (PetData) EzPets.From_String(config.getString("players.petdata." + p.getUniqueId().toString()));
                if (data != null) {
                    Location location = p.getLocation();
                    List<Entity> nearbyEntites = (List<Entity>) location.getWorld().getNearbyEntities(location, 100, 100, 100);
                    for (Entity nearbyEntity : nearbyEntites) {
                        if (nearbyEntity.getCustomName() != null) {
                            if (nearbyEntity.getCustomName().contains(p.getDisplayName() + "'s wolf")) {
                                p.sendMessage("§cHai già questo pet!");
                                e.setCancelled(true);
                                break;
                            }
                        }
                    }

                    new DogPet(p);
                } else {
                    new DogPet(p);
                }

                p.getInventory().removeItem(OnPlayerJoin.createEgg("Lupo"));
            }else if(lore.get(0).contains("Mucca")) {
                PetData data = (PetData) EzPets.From_String(config.getString("players.petdata." + p.getUniqueId().toString()));
                if (data != null) {
                    Location location = p.getLocation();
                    List<Entity> nearbyEntites = (List<Entity>) location.getWorld().getNearbyEntities(location, 100, 100, 100);
                    for (Entity nearbyEntity : nearbyEntites) {
                        if (nearbyEntity.getCustomName() != null) {
                            if (nearbyEntity.getCustomName().contains(p.getDisplayName() + "'s cow")) {
                                p.sendMessage("§cHai già questo pet!");
                                e.setCancelled(true);
                                break;
                            }
                        }
                    }

                    new CowPet(p);
                } else {
                    new CowPet(p);
                }

                p.getInventory().removeItem(OnPlayerJoin.createEgg("Mucca"));
            }else if(lore.get(0).contains("Pecora")) {
                PetData data = (PetData) EzPets.From_String(config.getString("players.petdata." + p.getUniqueId().toString()));
                if (data != null) {
                    Location location = p.getLocation();
                    List<Entity> nearbyEntites = (List<Entity>) location.getWorld().getNearbyEntities(location, 100, 100, 100);
                    for (Entity nearbyEntity : nearbyEntites) {
                        if (nearbyEntity.getCustomName() != null) {
                            if (nearbyEntity.getCustomName().contains(p.getDisplayName() + "'s sheep")) {
                                p.sendMessage("§cHai già questo pet!");
                                e.setCancelled(true);
                                break;
                            }
                        }
                    }

                    new SheepPet(p);
                } else {
                    new SheepPet(p);
                }

                p.getInventory().removeItem(OnPlayerJoin.createEgg("Pecora"));
            }else if(lore.get(0).contains("Gatto")) {
                PetData data = (PetData) EzPets.From_String(config.getString("players.petdata." + p.getUniqueId().toString()));
                if (data != null) {
                    Location location = p.getLocation();
                    List<Entity> nearbyEntites = (List<Entity>) location.getWorld().getNearbyEntities(location, 100, 100, 100);
                    for (Entity nearbyEntity : nearbyEntites) {
                        if (nearbyEntity.getCustomName() != null) {
                            if (nearbyEntity.getCustomName().contains(p.getDisplayName() + "'s cat")) {
                                p.sendMessage("§cHai già questo pet!");
                                e.setCancelled(true);
                                break;
                            }
                        }
                    }

                    new OcelotPet(p);
                } else {
                    new OcelotPet(p);
                }

                p.getInventory().removeItem(OnPlayerJoin.createEgg("Gatto"));
            }
        }
    }



    @EventHandler
    public void onPlayerEntityInteraction(PlayerInteractEntityEvent event) throws IOException, ClassNotFoundException {
        if(event.getRightClicked().getCustomName() == null) return;

        Entity entity = event.getRightClicked();
        Player player = event.getPlayer();
        if(entity.getCustomName().contains(event.getPlayer().getDisplayName())) {
            event.setCancelled(true);
            PetData data = (PetData) EzPets.From_String(config.getString("players.petdata."+player.getUniqueId().toString()));

            double level = data.getPets().get(entity.getName());
            double exp = data.getLevel().get(entity.getName());
            double progress = (exp / (level*100)) * 100;

            player.sendMessage(" §a§l[*]§fPET STATS§a§l[*]");
            player.sendMessage(" Pet level: §a"+Math.round(level));
            player.sendMessage(" Pet exp: §a"+exp);
            player.sendMessage(" \nIl pet è a §a"+Math.round(progress)+"%§f per passare al prossimo livello");
            if(progress == 0) player.sendMessage("                  \n §l[⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛§l]");
            else if(progress <= 10) player.sendMessage("                  \n §l[§a⬛§f§l⬛⬛⬛⬛⬛⬛⬛⬛⬛§l]");
            else if(progress <= 20) player.sendMessage("                  \n §l[§a⬛⬛§f⬛⬛⬛⬛⬛⬛⬛⬛§l]");
            else if(progress <= 30) player.sendMessage("                  \n §l[§a⬛⬛⬛§f⬛⬛⬛⬛⬛⬛⬛§l]");
            else if(progress <= 40) player.sendMessage("                  \n §l[§a⬛⬛⬛⬛§f⬛⬛⬛⬛⬛⬛§l]");
            else if(progress <= 50) player.sendMessage("                  \n §l[§a⬛⬛⬛⬛⬛§f⬛⬛⬛⬛⬛§l]");
            else if(progress <= 60) player.sendMessage("                  \n §l[§a⬛⬛⬛⬛⬛⬛§f⬛⬛⬛⬛§l]");
            else if(progress <= 70) player.sendMessage("                  \n §l[§a⬛⬛⬛⬛⬛⬛⬛§f⬛⬛⬛§l]");
            else if(progress <= 80) player.sendMessage("                  \n §l[§a⬛⬛⬛⬛⬛⬛⬛⬛§f⬛⬛§l]");
            else if(progress <= 90) player.sendMessage("                  \n §l[§a⬛⬛⬛⬛⬛⬛⬛⬛⬛§f⬛§l]");
            else if(progress <= 100) player.sendMessage("                  \n §l[§a⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛§f§l]");

        }
    }

    public void follow(final Player player, final Entity pet) {
        EzPets plugin = EzPets.getPlugin(EzPets.class);
        FileConfiguration config = plugin.getConfig();

        new BukkitRunnable() {

            public void run() {
                if(!pet.isValid()) {
                    this.cancel();
                    return;
                }

                if(pet instanceof Sheep) {
                    ((Sheep) pet).setBaby();
                }

                if(pet.getWorld() != player.getWorld()) {
                    pet.teleport(player);
                }

                if(!player.isOnline()) {
                    this.cancel();
                    pet.remove();
                    return;
                }

                Object petObject = ((CraftEntity) pet).getHandle();

                Location loc = player.getLocation();

                PathEntity path;

                path = ((EntityInsentient)petObject).getNavigation().a(loc.getX() + 1, loc.getY(), loc.getZ() +1);

                if(path != null) {
                    ((EntityInsentient)petObject).getNavigation().a(path, 1.0D);

                    ((EntityInsentient)petObject).getNavigation().a(1.5D);

                }

                int distance = (int) loc.distance(pet.getLocation());

                if(distance > 10 && ! pet.isDead()) {
                    pet.teleport(loc);
                }

            }

        }.runTaskTimer(EzPets.getPlugin(EzPets.class), 0L, 5L);


    }

    public static void expEarn(final Player player, final Entity pet) {
        new BukkitRunnable() {

            @Override
            public void run() {
                if(!player.isOnline()) {
                    this.cancel();
                    pet.remove();
                    return;
                }

                if(!pet.isValid()) {
                    this.cancel();
                    return;
                }

                try {
                    PetData data = (PetData) EzPets.From_String(config.getString("players.petdata."+player.getUniqueId().toString()));

                    if (data.getPets().containsKey(pet.getName()) && data.getLevel().containsKey(pet.getName())) {
                        if(data.getPets().get(pet.getName()) < 100) {
                            if(data.getLevel().get(pet.getName()) < data.getPets().get(pet.getName())*100) {

                                HashMap<String, Double> level = data.getLevel();
                                level.put(pet.getName(), data.getLevel().get(pet.getName())+10);

                                data.setLevel(level);

                                config.set("players.petdata."+player.getUniqueId().toString(), EzPets.To_String(data));
                                plugin.saveConfig();

                            }else if(Objects.equals(data.getLevel().get(pet.getName()), data.getPets().get(pet.getName()) * 100)){

                                HashMap<String, Double> level = data.getLevel();
                                level.put(pet.getName(), 0.0);

                                data.setLevel(level);

                                player.playEffect(pet.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
                                String petName = pet.getCustomName().replace(player.getDisplayName()+"'s ", "");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aIl tuo pet &e"+petName+"&a si è alzato di livello!"));

                                HashMap<String, Double> pets = data.getPets();
                                pets.put(pet.getName(), data.getPets().get(pet.getName()) + 1);

                                data.setPets(pets);

                                config.set("players.petdata."+player.getUniqueId().toString(), EzPets.To_String(data));
                                plugin.saveConfig();
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskTimer(EzPets.getPlugin(EzPets.class), 0L, 100*20);
    }
}
