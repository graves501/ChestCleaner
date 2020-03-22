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

    public boolean setFallbackValueIfPropertyIsNotSet(final Property property,
        final Object fallbackValue) {
        if (!configContainsProperty(property)) {
            this.yamlConfig.set(property.getString(), fallbackValue);
            return true;
        }
        return false;
    }

    public boolean setFallbackValueIfPropertyIsNotSet(final Property property,
        final boolean fallbackValue) {
        if (!configContainsProperty(property)) {
            this.yamlConfig.set(property.getString(), fallbackValue);
            return true;
        }
        return false;
    }

    public boolean configContainsProperty(final Property property) {
        return this.yamlConfig.contains(property.getString());
    }

    public void setAndSaveBooleanProperty(final Property property, boolean value) {
        this.yamlConfig.set(property.getString(), value);
        saveOrOverwriteConfigToFile();
    }

    public boolean getBooleanProperty(final Property property) {
        return this.yamlConfig.getBoolean(property.getString());
    }

    public void setAndSaveStringProperty(final Property property, String value) {
        this.yamlConfig.set(property.getString(), value);
        saveOrOverwriteConfigToFile();
    }

    public String getStringProperty(final Property property) {
        return this.yamlConfig.getString(property.getString());
    }

    public void setConfigProperty(final Property property, final Object propertyValue) {
        this.yamlConfig.set(property.getString(), propertyValue);
    }

    public void setAndSaveConfigProperty(final Property property,
        final Object propertyValue) {
        this.yamlConfig.set(property.getString(), propertyValue);
        saveOrOverwriteConfigToFile();
    }

    public Object getConfigProperty(final Property property) {
        return this.yamlConfig.get(property.getString());
    }
}
