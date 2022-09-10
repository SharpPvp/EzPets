package me.java.plugins.ezpets;

import me.java.plugins.ezpets.commands.CommandsManager;
import me.java.plugins.ezpets.data.PetData;
import me.java.plugins.ezpets.events.OnPlayerJoin;
import me.java.plugins.ezpets.pets.Pet;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.util.Base64;

public final class EzPets extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(new OnPlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new Pet(), this);

        getCommand("mypets").setExecutor(new CommandsManager());
        getCommand("givepet").setExecutor(new CommandsManager());
        getCommand("removepet").setExecutor(new CommandsManager());

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        for(World world : Bukkit.getServer().getWorlds()) {
            for(Entity entity : world.getEntities()) {
                if(entity.getCustomName() == null) return;

                if(entity.getCustomName().contains("'s ")) {
                    entity.remove();
                }
            }
        }
    }



    public static Object From_String( String s ) throws IOException,
            ClassNotFoundException {
        if(s == null) return null;
        byte [] Byte_Data = Base64.getDecoder().decode( s );
        BukkitObjectInputStream Object_Input_Stream = new BukkitObjectInputStream( new ByteArrayInputStream(Byte_Data) );
        Object Demo_Object  = Object_Input_Stream.readObject();
        Object_Input_Stream.close();
        return Demo_Object;
    }

    public static String To_String( Serializable Demo_Object ) throws IOException {
        ByteArrayOutputStream Byte_Array_Output_Stream = new ByteArrayOutputStream();
        BukkitObjectOutputStream Object_Output_Stream = new BukkitObjectOutputStream( Byte_Array_Output_Stream );
        Object_Output_Stream.writeObject( Demo_Object );
        Object_Output_Stream.close();
        return Base64.getEncoder().encodeToString(Byte_Array_Output_Stream.toByteArray());
    }

    public static ItemStack createPetEgg(String name) {
        return OnPlayerJoin.createEgg(name);
    }
}
