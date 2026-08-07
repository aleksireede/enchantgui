package io.github.aleksireede.enchantgui.localization;

import com.github.sarhatabaot.kraken.core.config.ConfigFile;
import io.github.aleksireede.enchantgui.EnchantGUIPlugin;

import java.io.File;
import java.util.List;

/**
 * @author aleksireede
 */
public class LocalizedConfigFile extends ConfigFile<EnchantGUIPlugin> {
    // langFolder = en / he / pt-br etc
    public LocalizedConfigFile(final String langFolder, final String fileName) {
        super(EnchantGUIPlugin.getInstance(), "languages" + File.separator + langFolder + File.separator, fileName, "languages" + File.separator + langFolder);
    }

    public String getString(final String path) {
        return getConfig().getString(path);
    }

    public String getString(final String path, final String def) {
        return getConfig().getString(path, def);
    }

    public List<String> getStringList(final String path) {
        return getConfig().getStringList(path);
    }
}
