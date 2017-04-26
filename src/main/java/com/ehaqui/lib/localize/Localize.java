package com.ehaqui.lib.localize;

/*
 * This plugin needs a default_lang.yml file in the jar file. This file includes the default strings.
 */

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Localize
{
    private JavaPlugin plugin;
    private String languageFile;
    private String defaultLanguage = "language_pt_BR.yml";

    public Localize(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }

    public Localize(JavaPlugin plugin, String langFile)
    {
        this.plugin = plugin;
        this.setLanguageFile(langFile);
    }

    public void setLanguageFile(String langFile)
    {
        if (langFile.equals("") || langFile == null)
        {
            this.languageFile = "language_pt_BR.yml";
        }
        else
        {
            this.languageFile = langFile + ".yml";
        }

        File file = new File(plugin.getDataFolder(), languageFile);

        try
        {
            if (!file.exists())
                plugin.saveResource(languageFile, false);
        } catch (IllegalArgumentException ex)
        {
            try
            {
                if (!plugin.getDataFolder().exists())
                    plugin.getDataFolder().mkdir();

                file.createNewFile();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }


        this.reloadLocalizedStrings();
    }

    public String getLanguageFile()
    {
        return this.languageFile;
    }

    public String getS(String pathToString, String defaultMessage)
    {
        if (!getLocalizedStrings().contains(pathToString))
        {
            plugin.getLogger().severe("Locate: '" + pathToString + "' not found on language file.");
            plugin.getLogger().severe("Using default: '" + defaultMessage + "'");

            this.getLocalizedStrings().set(pathToString, defaultMessage);
            this.getLocalizedStrings().options().copyDefaults(true);
            saveLocalizedStrings();

            return defaultMessage;
        }

        return getLocalizedStrings().getString(pathToString);
    }

    public String getS(String pathToString, String defaultMessage, Variable... variables)
    {
        String string = getS(pathToString, defaultMessage);

        for (Variable var : variables)
        {
            string = string.replaceAll("\\{" + var.getPlaceholder() + "\\}", var.getValue().toString());
        }

        return string;
    }

    public List<String> getListString(String pathToString, List<String> defaultMessage)
    {
        if (!getLocalizedStrings().contains(pathToString))
        {
            plugin.getLogger().severe("Locate: '" + pathToString + "' not found on language file.");
            plugin.getLogger().severe("Using default: '" + defaultMessage.toString() + "'");

            this.getLocalizedStrings().set(pathToString, defaultMessage);
            this.getLocalizedStrings().options().copyDefaults(true);
            saveLocalizedStrings();

            return defaultMessage;
        }

        return getLocalizedStrings().getStringList(pathToString);
    }

    public List<String> getListString(String pathToString, List<String> defaultMessage, Variable... variables)
    {
        List<String> string = new ArrayList<>(getListString(pathToString, defaultMessage));

        for (Variable var : variables)
        {
            ListIterator<String> iter = string.listIterator();
            while (iter.hasNext())
            {
                iter.set(iter.next().replaceAll("\\{" + var.getPlaceholder() + "\\}", var.getValue().toString()));
            }
        }

        return string;
    }

    public void loadDefaultLanguageFiles()
    {
        String oldLanguageFile = this.getLanguageFile();

        this.setLanguageFile(defaultLanguage);
        this.getLocalizedStrings().options().copyDefaults(true);
        this.saveLocalizedStrings();

        this.setLanguageFile(oldLanguageFile);
        this.reloadLocalizedStrings();
    }

    private FileConfiguration localizedStrings = null;
    private File localizedStringsFile = null;

    private void reloadLocalizedStrings()
    {
        localizedStringsFile = new File(plugin.getDataFolder(), languageFile);
        localizedStrings = YamlConfiguration.loadConfiguration(localizedStringsFile);

        InputStream defConfigStream = plugin.getResource(defaultLanguage);
        if (defConfigStream != null)
        {
            Reader defConfigReader = new InputStreamReader(defConfigStream);
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigReader);
            localizedStrings.setDefaults(defConfig);
        }
    }

    private FileConfiguration getLocalizedStrings()
    {
        if (localizedStrings == null)
        {
            reloadLocalizedStrings();
        }
        return localizedStrings;
    }

    public void saveLocalizedStrings()
    {
        if (localizedStrings == null || localizedStringsFile == null)
        {
            return;
        }
        try
        {
            localizedStrings.save(localizedStringsFile);
        } catch (IOException ex)
        {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not save configuration to " + localizedStringsFile, ex);
        }
    }

}
