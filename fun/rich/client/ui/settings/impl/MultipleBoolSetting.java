package fun.rich.client.ui.settings.impl;

import fun.rich.client.ui.settings.Setting;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MultipleBoolSetting extends Setting {

    private final List<BooleanSetting> boolSettings;

    public MultipleBoolSetting(String name, BooleanSetting... booleanSettings) {
        this.name = name;
        boolSettings = Arrays.asList(booleanSettings);
    }
// смешно)
    public BooleanSetting getSetting(String settingName) {
        return boolSettings.stream().filter(booleanSetting -> booleanSetting.getName().equals(settingName)).findFirst().orElse(null);
    }

    public List<BooleanSetting> getBoolSettings() {
        return boolSettings;
    }

}
