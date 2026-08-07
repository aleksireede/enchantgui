package io.github.aleksireede.enchantgui.localization;


/**
 * @author aleksireede
 */
public class LocalLanguageConfigFile extends LocalizedConfigFile {
    public LocalLanguageConfigFile(final String lang) {
        super(lang, "localization.yml");
    }

    public String getPrefix() {
        return getConfig().getString("prefix", "EnchantGUI");
    }
}
