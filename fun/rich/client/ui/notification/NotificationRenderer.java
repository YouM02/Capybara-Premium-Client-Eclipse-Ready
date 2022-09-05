package fun.rich.client.ui.notification;

import fun.rich.client.Rich;
import fun.rich.client.feature.impl.hud.Notifications;
import fun.rich.client.ui.clickgui.ClickGuiScreen;
import fun.rich.client.ui.clickgui.ClickGuiScreen;
import fun.rich.client.utils.Helper;
import fun.rich.client.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class NotificationRenderer implements Helper {
    private static final List<Notification> NOTIFICATIONS = new CopyOnWriteArrayList<>();

    public static void queue(String title, String content, int second, NotificationMode type) {
        NOTIFICATIONS.add(new Notification(title, content, type, second * 1000, Minecraft.getMinecraft().neverlose500_18));
    }

    public static void publish(ScaledResolution sr) {
        if (Rich.instance.featureManager.getFeature(Notifications.class).isEnabled() && Notifications.notifMode.currentMode.equalsIgnoreCase("Rect") && !mc.gameSettings.showDebugInfo && mc.world != null && !(mc.currentScreen instanceof ClickGuiScreen)) {
            if (!NOTIFICATIONS.isEmpty()) {
                int y = sr.getScaledHeight() - 40;
                double better;
                for (Notification notification : NOTIFICATIONS) {
                    better = Minecraft.getMinecraft().neverlose500_18.getStringWidth(notification.getTitle() + " " + notification.getContent());

                    if (!notification.getTimer().hasReached(notification.getTime() / 2)) {
                        notification.notificationTimeBarWidth = 360;
                    } else {
                        notification.notificationTimeBarWidth = MathHelper.EaseOutBack((float) notification.notificationTimeBarWidth, 0, (float) (4 * Rich.deltaTime()));
                    }

                    if (!notification.getTimer().hasReached(notification.getTime())) {
                        notification.x = MathHelper.EaseOutBack((float) notification.x, (float) (notification.sr.getScaledWidth() - better), (float) (5 * Rich.deltaTime()));
                        notification.y = MathHelper.EaseOutBack((float) notification.y, (float) y, (float) (5 * Rich.deltaTime()));
                    } else {
                        notification.x = MathHelper.EaseOutBack((float) notification.x, (float) (notification.sr.getScaledWidth() + 50), (float) (5 * Rich.deltaTime()));
                        notification.y = MathHelper.EaseOutBack((float) notification.y, (float) y, (float) (5 * Rich.deltaTime()));
                        if (notification.x > notification.sr.getScaledWidth() + 24 && mc.player != null && mc.world != null && !mc.gameSettings.showDebugInfo) {
                            NOTIFICATIONS.remove(notification);
                        }
                    }
                    GlStateManager.pushMatrix();
                    GlStateManager.disableBlend();
                    RenderUtils.drawSmoothRect(notification.x - 30, notification.y - 13, notification.sr.getScaledWidth(), notification.y + 12.0f, new Color(20, 20, 20, 180).getRGB());
                    RenderUtils.drawSmoothRect(notification.x - 30, notification.y - 13, notification.x + 5, notification.y + 12.0f, new Color(155, 155, 155, 50).getRGB());
                    Minecraft.getMinecraft().notification.drawString(notification.getType().getIconString(), (float) (notification.x - 22), (float) (notification.y - 8), -1);

                    Minecraft.getMinecraft().rubik_18.drawString(notification.getTitle(), (float) (notification.x + 10), (float) (notification.y - 9), -1);
                    Minecraft.getMinecraft().rubik_17.drawString(notification.getContent(), (float) (notification.x + 10), (float) (notification.y + 2), -1);
                    GlStateManager.popMatrix();
                    y -= 30;
                }
            }
        }
    }
}