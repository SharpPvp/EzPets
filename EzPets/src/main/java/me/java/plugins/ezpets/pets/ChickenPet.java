package me.java.plugins.ezpets.pets;

import me.java.plugins.ezpets.EzPets;
import me.java.plugins.ezpets.data.PetData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;

public class ChickenPet extends Pet{
    EzPets plugin = EzPets.getPlugin(EzPets.class);
    FileConfiguration config = plugin.getConfig();
    public ChickenPet(Player p) throws IOException, ClassNotFoundException {
        Chicken chicken = p.getWorld().spawn(p.getLocation().add(0,1,0), Chicken.class);
        chicken.setCustomNameVisible(true);
        chicken.setRemoveWhenFarAway(false);
        chicken.setCustomName(p.getDisplayName()+"'s chicken");

        if(config.getString("players.petdata."+p.getUniqueId().toString()) != null) {
            PetData data = (PetData) EzPets.From_String(config.getString("players.petdata."+p.getUniqueId().toString()));
            if(!data.getPets().containsKey(chicken.getCustomName())) {
                if(data.getPets() == null) {
                    HashMap<String, Double> map = new HashMap<>();
                    map.put(chicken.getName(), 1.0);

                    data.setPets(map);
                }else {
                    HashMap<String, Double> pets = data.getPets();
                    pets.put(chicken.getName(), 1.0);

                    data.setPets(pets);
                }
            }else {
                HashMap<String, Double> pets = data.getPets();
                pets.replace(chicken.getName(), data.getPets().get(chicken.getName()));

                data.setPets(pets);
            }
            if(!data.getLevel().containsKey(chicken.getCustomName())) {
                if(data.getLevel() == null) {
                    HashMap<String, Double> level = new HashMap<>();
                    level.put(chicken.getName(), 0.0);

                    data.setLevel(level);
                }else {
                    HashMap<String, Double> level = data.getLevel();
                    level.put(chicken.getName(), 0.0);

                    data.setLevel(level);
                }

            }

            config.set("players.petdata."+p.getUniqueId().toString(), EzPets.To_String(data));
            plugin.saveConfig();
        }else {
            PetData data = new PetData();
            data.setUuid(p.getUniqueId().toString());
            HashMap<String, Double> pets = new HashMap<>();
            pets.put(chicken.getName(), 1.0);
            data.setPets(pets);

            HashMap<String, Double> level = new HashMap<>();
            level.put(chicken.getName(), 0.0);

            data.setLevel(level);

            config.set("players.petdata."+p.getUniqueId().toString(), EzPets.To_String(data));
            plugin.saveConfig();
        }
        petsInGame.add(p);

        follow(p, chicken);
        expEarn(p, chicken);
    }
}
