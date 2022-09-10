package me.java.plugins.ezpets.commands;

import me.java.plugins.ezpets.EzPets;
import me.java.plugins.ezpets.data.PetData;
import me.java.plugins.ezpets.events.OnPlayerJoin;
import me.java.plugins.ezpets.pets.*;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Player.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandsManager implements CommandExecutor {
    static EzPets plugin = EzPets.getPlugin(EzPets.class);
    static FileConfiguration config = plugin.getConfig();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player)sender;
            if(command.getName().equalsIgnoreCase("mypets")) {
                try {
                    PetData data = (PetData) EzPets.From_String(config.getString("players.petdata."+p.getUniqueId().toString()));
                    if(data != null) {
                        if(args.length > 0) {
                            String petNameGiven = args[0];
                            HashMap<String, Double> dataMap = data.getPets();

                            List<Entity> localEntities = (List<Entity>) p.getWorld().getNearbyEntities(p.getLocation(), 100, 100, 100);
                            for(Entity e : localEntities) {
                                if(e.getCustomName() != null) {
                                    if(e.getCustomName().contains(p.getDisplayName()+"'s")) {
                                        e.remove();
                                    }
                                }
                            }

                            if(petNameGiven.contains("chicken")) {
                                for(String key: dataMap.keySet()) {
                                    if(key.contains(p.getDisplayName()+"'s chicken")) {
                                        new ChickenPet(p);
                                    }
                                }
                            }
                            if(petNameGiven.contains("pig")) {
                                for(String key: dataMap.keySet()) {
                                    if(key.contains(p.getDisplayName()+"'s pig")) {
                                        new PigPet(p);
                                    }
                                }
                            }
                            if(petNameGiven.contains("cow")) {
                                for(String key: dataMap.keySet()) {
                                    if(key.contains(p.getDisplayName()+"'s cow")) {
                                        new CowPet(p);
                                    }
                                }
                            }
                            if(petNameGiven.contains("wolf")) {
                                for(String key: dataMap.keySet()) {
                                    if(key.contains(p.getDisplayName()+"'s wolf")) {
                                        new DogPet(p);
                                    }
                                }
                            }
                            if(petNameGiven.contains("cat")) {
                                for(String key: dataMap.keySet()) {
                                    if(key.contains(p.getDisplayName()+"'s cat")) {
                                        new OcelotPet(p);
                                    }
                                }
                            }
                            if(petNameGiven.contains("sheep")) {
                                for(String key: dataMap.keySet()) {
                                    if(key.contains(p.getDisplayName()+"'s sheep")) {
                                        new SheepPet(p);
                                    }
                                }
                            }
                        }else {
                            HashMap<String, Double> dataMap = data.getPets();
                            p.sendMessage("I tui pet:");
                            p.sendMessage("-------------------------------------");
                            for (Map.Entry<String, Double> entry : dataMap.entrySet()) {
                                TextComponent pet = new TextComponent(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&a"+entry.getKey().replace(p.getDisplayName()+"'s ", "") + "&f: &alvl&f "+entry.getValue()));
                                pet.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&aClicca per spawnare il pet")).create()));
                                pet.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mypets "+entry.getKey().replace(p.getDisplayName()+"'s ", "")));
                                p.spigot().sendMessage(pet);
                            }
                            p.sendMessage("-------------------------------------");
                            p.sendMessage("Clicca su uno dei pet per §aspawnarlo§f!");
                        }
                    }else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNon hai dai pet!"));
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            if(command.getName().equalsIgnoreCase("givepet")) {
                if(p.isOp()) {
                    if(args.length > 0) {
                        String petNameGiven = args[0];

                        if(petNameGiven.contains("chicken")) {
                            p.getInventory().addItem(OnPlayerJoin.createEgg("Pollo"));
                        }
                        if(petNameGiven.contains("pig")) {
                            p.getInventory().addItem(OnPlayerJoin.createEgg("Maiale"));
                        }
                        if(petNameGiven.contains("cow")) {
                            p.getInventory().addItem(OnPlayerJoin.createEgg("Mucca"));
                        }
                        if(petNameGiven.contains("wolf")) {
                            p.getInventory().addItem(OnPlayerJoin.createEgg("Lupo"));
                        }
                        if(petNameGiven.contains("cat")) {
                            p.getInventory().addItem(OnPlayerJoin.createEgg("Gatto"));
                        }
                        if(petNameGiven.contains("sheep")) {
                            p.getInventory().addItem(OnPlayerJoin.createEgg("Pecora"));
                        }
                    }else {
                        if(!p.isOp()) return true;
                        List<String> pets = new ArrayList<String>();
                        pets.add("chicken");
                        pets.add("pig");
                        pets.add("cow");
                        pets.add("wolf");
                        pets.add("cat");
                        pets.add("sheep");

                        p.sendMessage("Pet disponibili:");
                        p.sendMessage("-------------------------------------");
                        for (String petx : pets) {
                            TextComponent pet = new TextComponent(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&a"+petx));
                            pet.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&aClicca per ottenere il pet")).create()));
                            pet.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/givepet "+petx));
                            p.spigot().sendMessage(pet);
                        }
                        p.sendMessage("-------------------------------------");
                        p.sendMessage("Clicca su uno dei pet per §aottenere§f l'uovo!");
                    }
                }
            }

            if(command.getName().equalsIgnoreCase("removepet")) {
                List<Entity> localEntities = (List<Entity>) p.getWorld().getNearbyEntities(p.getLocation(), 100, 100, 100);
                for(Entity e : localEntities) {
                    if(e.getCustomName() != null) {
                        if(e.getCustomName().contains(p.getDisplayName()+"'s")) {
                            e.remove();
                        }
                    }
                }
                p.sendMessage(ChatColor.GREEN+"Pet despawnato correttamente!");
            }
        }
        return true;
    }
}
