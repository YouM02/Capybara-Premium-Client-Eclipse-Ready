package fun.rich.client.ui.mainmenu;

import fun.rich.client.Rich;
import fun.rich.client.ui.altmanager.GuiAltManager;
import fun.rich.client.ui.button.GuiMainMenuButton;
import fun.rich.client.ui.particle.ParticleUtils;
import fun.rich.client.utils.math.animations.Animation;
import fun.rich.client.utils.math.animations.impl.DecelerateAnimation;
import fun.rich.client.utils.render.RenderUtils;
import net.minecraft.client.gui.*;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;

public class RichMainMenu extends GuiScreen {
    private int width;
    public float scale = 2;
    private int height;
    private final long initTime = System.currentTimeMillis();
    private Animation initAnimation;

    public RichMainMenu() {
    }

    @Override
    public void initGui() {
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.width = sr.getScaledWidth();
        this.height = sr.getScaledHeight();
        initAnimation = new DecelerateAnimation(300, 1);

        this.buttonList.add(new GuiMainMenuButton(0, (this.width / 2) - 90, this.height / 2 + 4, 180, 15, "Singleplayer"));
        this.buttonList.add(new GuiMainMenuButton(1, this.width / 2 - 90, this.height / 2 + 32, 180, 15, "Multiplayer"));
        this.buttonList.add(new GuiMainMenuButton(2, this.width / 2 - 90, this.height / 2 + 60, 180, 15, "Alt Manager"));
        this.buttonList.add(new GuiMainMenuButton(3, this.width / 2 - 90, this.height / 2 + 88, 180, 15, "Options"));
        this.buttonList.add(new GuiMainMenuButton(4, this.width / 2 - 90, this.height / 2 + 116, 180, 15, "Quit"));


    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        ScaledResolution res = new ScaledResolution(mc);
        Gui.drawRect(0, 0, 1500, 1500, new Color(17, 17, 17).getRGB());
        ParticleUtils.drawParticles(Mouse.getX() * this.width / this.mc.displayWidth, this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1);

        ScaledResolution sr = new ScaledResolution(mc);
        mc.rubik_30.drawCenteredStringWithShadow("Capybara Premium", (float) (sr.getScaledWidth() / 2), (float) (sr.getScaledHeight() / 2.2), -1);
        mc.rubik_18.drawStringWithOutline("Версия клиента - " + Rich.instance.version, 2.0F, (float) (sr.getScaledHeight() - mc.neverlose900_16.getFontHeight() - 2), -1);
        mc.rubik_18.drawStringWithShadow("UID " + "Null", sr.getScaledWidth() - mc.rubik_18.getStringWidth("UID " + "Null") - 4, sr.getScaledHeight() - 9, new Color(255, 255, 255).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiWorldSelection(this));
                break;
            case 1:
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 2:
                this.mc.displayGuiScreen(new GuiAltManager());
                break;
            case 3:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 4:
                System.exit(0);
                Rich.instance.configManager.saveConfig("default");
                Rich.instance.fileManager.saveFiles();
                break;
        }

        super.actionPerformed(button);
    }
}