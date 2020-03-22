package io.github.graves501.chestcleanerx.config;

import io.github.graves501.chestcleanerx.util.constant.Property;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class ConfigManager {

    protected File configFile;
    protected FileConfiguration yamlConfig;

    /*
     * This method will save using the system default encoding, or possibly using UTF8.
     */
    protected void saveOrOverwriteConfigToFile() {
        try {
            this.yamlConfig.save(this.configFile);
        } catch (IOException exception) {
            // TODO proper logging
            exception.printStackTrace();
        }
    }

    // Helper functions

    public boolean configContainsProperty(final Property property) {
        return this.yamlConfig.contains(property.getString());
    }

    public boolean getBooleanProperty(final Property property) {
        return this.yamlConfig.getBoolean(property.getString());
    }

    public String getStringProperty(final Property property) {
        return this.yamlConfig.getString(property.getString());
    }
}
