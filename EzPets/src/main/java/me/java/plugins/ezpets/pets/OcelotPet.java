package me.java.plugins.ezpets.pets;

import me.java.plugins.ezpets.EzPets;
import me.java.plugins.ezpets.data.PetData;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class OcelotPet extends Pet{
    public OcelotPet(Player p) throws IOException, ClassNotFoundException {
        Ocelot ocelot = p.getWorld().spawn(p.getLocation().add(0, 1, 0), Ocelot.class);
        ocelot.setCatType(Ocelot.Type.values()[new Random().nextInt(Ocelot.Type.values().length)]);
        ocelot.setTamed(true);
        while (ocelot.getCatType().equals(Ocelot.Type.WILD_OCELOT))
            ocelot.setCatType(Ocelot.Type.values()[new Random().nextInt(Ocelot.Type.values().length)]);
        ocelot.setRemoveWhenFarAway(false);
        ocelot.setOwner(p);
        ocelot.setSitting(false);
        ocelot.setAgeLock(true);
        ocelot.setBreed(false);
        ocelot.setCustomNameVisible(true);
        ocelot.setCustomName(p.getDisplayName() + "'s cat");

        if (config.getString("players.petdata." + p.getUniqueId().toString()) != null) {
            PetData data = (PetData) EzPets.From_String(config.getString("players.petdata." + p.getUniqueId().toString()));
            if (!data.getPets().containsKey(ocelot.getName())) {
                if (data.getPets() == null) {
                    HashMap<String, Double> map = new HashMap<>();
                    map.put(ocelot.getName(), 1.0);

                    data.setPets(map);
                } else {
                    HashMap<String, Double> pets = data.getPets();
                    pets.put(ocelot.getName(), 1.0);

                    data.setPets(pets);
                }
            } else {
                HashMap<String, Double> pets = data.getPets();
                pets.replace(ocelot.getName(), data.getPets().get(ocelot.getName()));

                data.setPets(pets);
            }
            if (!data.getLevel().containsKey(ocelot.getCustomName())) {
                if (data.getLevel() == null) {
                    HashMap<String, Double> level = new HashMap<>();
                    level.put(ocelot.getName(), 0.0);

                    data.setLevel(level);
                } else {
                    HashMap<String, Double> level = data.getLevel();
                    level.put(ocelot.getName(), 0.0);

                    data.setLevel(level);
                }
            }

            config.set("players.petdata." + p.getUniqueId().toString(), EzPets.To_String(data));
            plugin.saveConfig();
        } else {
            PetData data = new PetData();
            data.setUuid(p.getUniqueId().toString());
            HashMap<String, Double> pets = new HashMap<>();
            pets.put(ocelot.getName(), 1.0);
            data.setPets(pets);

            HashMap<String, Double> level = new HashMap<>();
            level.put(ocelot.getName(), 0.0);

            data.setLevel(level);

            config.set("players.petdata." + p.getUniqueId().toString(), EzPets.To_String(data));
            plugin.saveConfig();
        }
        Pet.petsInGame.add(ocelot);

        follow(p, ocelot);
        expEarn(p, ocelot);
    }
}
