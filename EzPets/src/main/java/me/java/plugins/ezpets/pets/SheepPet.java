package me.java.plugins.ezpets.pets;

import me.java.plugins.ezpets.EzPets;
import me.java.plugins.ezpets.data.PetData;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

import java.io.IOException;
import java.util.HashMap;

public class SheepPet extends Pet{
    public SheepPet(Player p) throws IOException, ClassNotFoundException {
        Sheep sheep = p.getWorld().spawn(p.getLocation().add(0,1,0), Sheep.class);
        sheep.setBaby();
        sheep.setRemoveWhenFarAway(false);
        sheep.setAgeLock(true);
        sheep.setBreed(false);
        sheep.setCustomNameVisible(true);
        sheep.setCustomName(p.getDisplayName()+"'s sheep");

        if(config.getString("players.petdata."+p.getUniqueId().toString()) != null) {
            PetData data = (PetData) EzPets.From_String(config.getString("players.petdata."+p.getUniqueId().toString()));
            if(!data.getPets().containsKey(sheep.getName())) {
                if(data.getPets() == null) {
                    HashMap<String, Double> map = new HashMap<>();
                    map.put(sheep.getName(), 1.0);

                    data.setPets(map);
                }else {
                    HashMap<String, Double> pets = data.getPets();
                    pets.put(sheep.getName(), 1.0);

                    data.setPets(pets);
                }
            }else {
                HashMap<String, Double> pets = data.getPets();
                pets.replace(sheep.getName(), data.getPets().get(sheep.getName()));

                data.setPets(pets);
            }
            if(!data.getLevel().containsKey(sheep.getCustomName())) {
                if(data.getLevel() == null) {
                    HashMap<String, Double> level = new HashMap<>();
                    level.put(sheep.getName(), 0.0);

                    data.setLevel(level);
                }else {
                    HashMap<String, Double> level = data.getLevel();
                    level.put(sheep.getName(), 0.0);

                    data.setLevel(level);
                }
            }

            config.set("players.petdata."+p.getUniqueId().toString(), EzPets.To_String(data));
            plugin.saveConfig();
        }else {
            PetData data = new PetData();
            data.setUuid(p.getUniqueId().toString());
            HashMap<String, Double> pets = new HashMap<>();
            pets.put(sheep.getName(), 1.0);
            data.setPets(pets);

            HashMap<String, Double> level = new HashMap<>();
            level.put(sheep.getName(), 0.0);

            data.setLevel(level);

            config.set("players.petdata."+p.getUniqueId().toString(), EzPets.To_String(data));
            plugin.saveConfig();
        }
        Pet.petsInGame.add(sheep);

        follow(p, sheep );
        expEarn(p, sheep);
    }
}
