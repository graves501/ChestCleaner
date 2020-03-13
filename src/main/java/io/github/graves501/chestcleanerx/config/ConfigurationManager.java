package io.github.graves501.chestcleanerx.config;

import io.github.graves501.chestcleanerx.utils.enums.Property;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class ConfigurationManager {

    protected File configurationFile;
    protected FileConfiguration yamlConfiguration;

    /*
     * This method will save using the system default encoding, or possibly using UTF8.
     */
    protected void saveOrOverwriteConfigurationToFile() {
        try {
            this.yamlConfiguration.save(this.configurationFile);
        } catch (IOException exception) {
            // TODO proper logging
            exception.printStackTrace();
        }
    }

    // Helper functions

    public boolean setFallbackValueIfPropertyIsNotSet(final Property property,
        final Object fallbackValue) {
        if (!configurationContainsProperty(property)) {
            this.yamlConfiguration.set(property.getString(), fallbackValue);
            return true;
        }
        return false;
    }

    public boolean setFallbackValueIfPropertyIsNotSet(final Property property,
        final boolean fallbackValue) {
        if (!configurationContainsProperty(property)) {
            this.yamlConfiguration.set(property.getString(), fallbackValue);
            return true;
        }
        return false;
    }

    public boolean configurationContainsProperty(final Property property) {
        return this.yamlConfiguration.contains(property.getString());
    }

    public void setAndSaveBooleanProperty(final Property property, boolean value) {
        this.yamlConfiguration.set(property.getString(), value);
        saveOrOverwriteConfigurationToFile();
    }

    public boolean getBooleanProperty(final Property property) {
        return this.yamlConfiguration.getBoolean(property.getString());
    }

    public void setAndSaveStringProperty(final Property property, String value) {
        this.yamlConfiguration.set(property.getString(), value);
        saveOrOverwriteConfigurationToFile();
    }

    public String getStringProperty(final Property property) {
        return this.yamlConfiguration.getString(property.getString());
    }

    public void setConfigurationProperty(final Property property, final Object propertyValue) {
        this.yamlConfiguration.set(property.getString(), propertyValue);
    }

    public void setAndSaveConfigurationProperty(final Property property,
        final Object propertyValue) {
        this.yamlConfiguration.set(property.getString(), propertyValue);
        saveOrOverwriteConfigurationToFile();
    }

    public Object getConfigurationProperty(final Property property) {
        return this.yamlConfiguration.get(property.getString());
    }
}
