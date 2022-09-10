package me.java.plugins.ezpets.pets;

import me.java.plugins.ezpets.EzPets;
import me.java.plugins.ezpets.data.PetData;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;

public class PigPet extends Pet{
    public PigPet(Player p) throws IOException, ClassNotFoundException {
        Pig pig = p.getWorld().spawn(p.getLocation().add(0,1,0), Pig.class);
        pig.setBaby();
        pig.setRemoveWhenFarAway(false);
        pig.setAgeLock(true);
        pig.setCustomNameVisible(true);
        pig.setCustomName(p.getDisplayName()+"'s pig");

        if(config.getString("players.petdata."+p.getUniqueId().toString()) != null) {
            PetData data = (PetData) EzPets.From_String(config.getString("players.petdata."+p.getUniqueId().toString()));
            if(!data.getPets().containsKey(pig.getName())) {
                if(data.getPets() == null) {
                    HashMap<String, Double> map = new HashMap<>();
                    map.put(pig.getName(), 1.0);

                    data.setPets(map);
                }else {
                    HashMap<String, Double> pets = data.getPets();
                    pets.put(pig.getName(), 1.0);

                    data.setPets(pets);
                }
            }else {
                HashMap<String, Double> pets = data.getPets();
                pets.replace(pig.getName(), data.getPets().get(pig.getName()));

                data.setPets(pets);
            }
            if(!data.getLevel().containsKey(pig.getCustomName())) {
                if(data.getLevel() == null) {
                    HashMap<String, Double> level = new HashMap<>();
                    level.put(pig.getName(), 0.0);

                    data.setLevel(level);
                }else {
                    HashMap<String, Double> level = data.getLevel();
                    level.put(pig.getName(), 0.0);

                    data.setLevel(level);
                }
            }

            config.set("players.petdata."+p.getUniqueId().toString(), EzPets.To_String(data));
            plugin.saveConfig();
        }else {
            PetData data = new PetData();
            data.setUuid(p.getUniqueId().toString());
            HashMap<String, Double> pets = new HashMap<>();
            pets.put(pig.getName(), 1.0);
            data.setPets(pets);

            HashMap<String, Double> level = new HashMap<>();
            level.put(pig.getName(), 0.0);

            data.setLevel(level);

            config.set("players.petdata."+p.getUniqueId().toString(), EzPets.To_String(data));
            plugin.saveConfig();
        }
        Pet.petsInGame.add(pig);

        follow(p, pig );
        expEarn(p, pig);
    }
}
