package me.java.plugins.ezpets.data;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Entity;
import org.bukkit.util.NumberConversions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PetData implements Serializable  {
    private String uuid;
    private HashMap<String, Double> pets;
    private HashMap<String, Double> level;

    public HashMap<String, Double> getLevel() {
        return level;
    }

    public void setLevel(HashMap<String, Double> level) {
        this.level = level;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public HashMap<String, Double> getPets() {
        return pets;
    }

    public void setPets(HashMap<String, Double> pets) {
        this.pets = pets;
    }


}
