package fun.rich.client.feature.impl.player;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.utils.Helper;

public class FastPlace extends Feature {
    public FastPlace() {
        super("FastPlace", FeatureCategory.Player);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        mc.rightClickDelayTimer = 0;
    }

    @Override
    public void onDisable() {
        mc.rightClickDelayTimer = 6;
        super.onDisable();
    }
}
