package me.java.plugins.ezpets.pets;

import me.java.plugins.ezpets.EzPets;
import me.java.plugins.ezpets.data.PetData;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;

public class CowPet extends Pet{
    public CowPet(Player p) throws IOException, ClassNotFoundException {
        Cow cow = p.getWorld().spawn(p.getLocation().add(0,1,0), Cow.class);
        cow.setBaby();
        cow.setRemoveWhenFarAway(false);
        cow.setAgeLock(true);
        cow.setCustomNameVisible(true);
        cow.setCustomName(p.getDisplayName()+"'s cow");

        if(config.getString("players.petdata."+p.getUniqueId().toString()) != null) {
            PetData data = (PetData) EzPets.From_String(config.getString("players.petdata."+p.getUniqueId().toString()));
            if(!data.getPets().containsKey(cow.getName())) {
                if(data.getPets() == null) {
                    HashMap<String, Double> map = new HashMap<>();
                    map.put(cow.getName(), 1.0);

                    data.setPets(map);
                }else {
                    HashMap<String, Double> pets = data.getPets();
                    pets.put(cow.getName(), 1.0);

                    data.setPets(pets);
                }
            }else {
                HashMap<String, Double> pets = data.getPets();
                pets.replace(cow.getName(), data.getPets().get(cow.getName()));

                data.setPets(pets);
            }
            if(!data.getLevel().containsKey(cow.getCustomName())) {
                if(data.getLevel() == null) {
                    HashMap<String, Double> level = new HashMap<>();
                    level.put(cow.getName(), 0.0);

                    data.setLevel(level);
                }else {
                    HashMap<String, Double> level = data.getLevel();
                    level.put(cow.getName(), 0.0);

                    data.setLevel(level);
                }
            }

            config.set("players.petdata."+p.getUniqueId().toString(), EzPets.To_String(data));
            plugin.saveConfig();
        }else {
            PetData data = new PetData();
            data.setUuid(p.getUniqueId().toString());
            HashMap<String, Double> pets = new HashMap<>();
            pets.put(cow.getName(), 1.0);
            data.setPets(pets);

            HashMap<String, Double> level = new HashMap<>();
            level.put(cow.getName(), 0.0);

            data.setLevel(level);

            config.set("players.petdata."+p.getUniqueId().toString(), EzPets.To_String(data));
            plugin.saveConfig();
        }
        petsInGame.add(cow);

        follow(p, cow );
        expEarn(p, cow);
    }
}
