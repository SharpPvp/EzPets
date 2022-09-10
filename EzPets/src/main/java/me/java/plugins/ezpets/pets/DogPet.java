package me.java.plugins.ezpets.pets;

import me.java.plugins.ezpets.EzPets;
import me.java.plugins.ezpets.data.PetData;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import java.io.IOException;
import java.util.HashMap;

public class DogPet extends Pet{
    public DogPet(Player p) throws IOException, ClassNotFoundException {
        Wolf wolf = p.getWorld().spawn(p.getLocation().add(0,1,0), Wolf.class);
        wolf.setRemoveWhenFarAway(false);
        wolf.setBaby();
        wolf.setAgeLock(true);
        wolf.setCustomNameVisible(true);
        wolf.setCustomName(p.getDisplayName()+"'s wolf");

        if(config.getString("players.petdata."+p.getUniqueId().toString()) != null) {
            PetData data = (PetData) EzPets.From_String(config.getString("players.petdata."+p.getUniqueId().toString()));
            if(!data.getPets().containsKey(wolf.getName())) {
                if(data.getPets() == null) {
                    HashMap<String, Double> map = new HashMap<>();
                    map.put(wolf.getName(), 1.0);

                    data.setPets(map);
                }else {
                    HashMap<String, Double> pets = data.getPets();
                    pets.put(wolf.getName(), 1.0);

                    data.setPets(pets);
                }
            }else {
                HashMap<String, Double> pets = data.getPets();
                pets.replace(wolf.getName(), data.getPets().get(wolf.getName()));

                data.setPets(pets);
            }
            if(!data.getLevel().containsKey(wolf.getCustomName())) {
                if(data.getLevel() == null) {
                    HashMap<String, Double> level = new HashMap<>();
                    level.put(wolf.getName(), 0.0);

                    data.setLevel(level);
                }else {
                    HashMap<String, Double> level = data.getLevel();
                    level.put(wolf.getName(), 0.0);

                    data.setLevel(level);
                }
            }

            config.set("players.petdata."+p.getUniqueId().toString(), EzPets.To_String(data));
            plugin.saveConfig();
        }else {
            PetData data = new PetData();
            data.setUuid(p.getUniqueId().toString());
            HashMap<String, Double> pets = new HashMap<>();
            pets.put(wolf.getName(), 1.0);
            data.setPets(pets);

            HashMap<String, Double> level = new HashMap<>();
            level.put(wolf.getName(), 0.0);

            data.setLevel(level);

            config.set("players.petdata."+p.getUniqueId().toString(), EzPets.To_String(data));
            plugin.saveConfig();
        }
        petsInGame.add(wolf);

        follow(p, wolf);
        expEarn(p, wolf);
    }
}
