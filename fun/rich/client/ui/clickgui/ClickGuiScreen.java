package fun.rich.client.ui.clickgui;

import fun.rich.client.feature.impl.hud.ClickGUI;
import fun.rich.client.ui.particle.ParticleUtils;
import fun.rich.client.utils.math.BlurUtil;
import fun.rich.client.utils.math.MathematicHelper;
import fun.rich.client.utils.math.animations.Animation;
import fun.rich.client.utils.math.animations.Direction;
import fun.rich.client.utils.math.animations.impl.DecelerateAnimation;
import fun.rich.client.utils.render.ColorUtils;
import fun.rich.client.utils.render.GLUtils;
import fun.rich.client.utils.render.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.clickgui.component.Component;
import fun.rich.client.ui.clickgui.component.ExpandableComponent;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen extends GuiScreen {
    public static boolean escapeKeyInUse;
    public float scale = 2;

    public List<fun.rich.client.ui.clickgui.Panel> components = new ArrayList<>();
    public ScreenHelper screenHelper;
    public boolean exit = false;
    public FeatureCategory type;
    private Component selectedPanel;
    private Animation initAnimation;
    private static ResourceLocation ANIME_GIRL;
    public static GuiSearcher search;

    public ClickGuiScreen() {
        int x = 20;
        int y = 80;
        for (FeatureCategory type : FeatureCategory.values()) {
            this.type = type;
            this.components.add(new fun.rich.client.ui.clickgui.Panel(type, x, y));
            selectedPanel = new fun.rich.client.ui.clickgui.Panel(type, x, y);
            x += width + 125;
        }
        this.screenHelper = new ScreenHelper(0, 0);
    }

    @Override
    public void initGui() {

        ScaledResolution sr = new ScaledResolution(mc);
        initAnimation = new DecelerateAnimation(600, 1);
        this.screenHelper = new ScreenHelper(0, 0);
        search = new GuiSearcher(1337, this.mc.fontRendererObj, sr.getScaledWidth() / 2 + 320, 80, 150, 18);
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);

        Color color = Color.WHITE;
        Color onecolor = new Color(ClickGUI.bgcolor.getColorValue());
        switch (ClickGUI.backGroundColor.currentMode) {
            case "Astolfo":
                color = ColorUtils.astolfo(true, width);
                break;
            case "Rainbow":
                color = ColorUtils.rainbow(300, 1, 1);
                break;
            case "Static":
                color = onecolor;
                break;
        }

        if (!ClickGUI.potato_mode.getBoolValue() && ClickGUI.blur.getBoolValue() && ClickGUI.blurInt.getNumberValue() > 0) {
            if (this.mc.gameSettings.ofFastRender) {
                this.mc.gameSettings.ofFastRender = false;
            }
            RenderUtils.renderBlur(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), (int) ClickGUI.blurInt.getNumberValue());
        }
        if (!ClickGUI.potato_mode.getBoolValue()) {
            Color color1 = new Color(color.getRed(), color.getBlue(), color.getGreen(), 90);
            Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 180);
            RenderUtils.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), color1.getRGB(), color2.getRGB());
            drawDefaultBackground();
        }
        if (!ClickGUI.potato_mode.getBoolValue() && ClickGUI.particles.getBoolValue()) {
            ParticleUtils.drawParticles(Mouse.getX() * this.width / this.mc.displayWidth, this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1);
        }
        if (ClickGUI.girl.getBoolValue()) {
            String animeGirlStr = "";
            if (ClickGUI.girlmode.currentMode.equals("Girl")) {
                animeGirlStr = "girl1";
            } else if (ClickGUI.girlmode.currentMode.equals("Rem")) {
                animeGirlStr = "girl2";
            } else if (ClickGUI.girlmode.currentMode.equals("Kaneki")) {
                animeGirlStr = "girl3";
            } else if (ClickGUI.girlmode.currentMode.equals("Violet")) {
                animeGirlStr = "girl4";
            } else if (ClickGUI.girlmode.currentMode.equals("Kirshtein")) {
                animeGirlStr = "girl5";
            } else if (ClickGUI.girlmode.currentMode.equals("002")) {
                animeGirlStr = "girl6";
            }
            RenderUtils.drawImage(new ResourceLocation("rich/anime/" + animeGirlStr + ".png"), (float) (sr.getScaledWidth() - 280), (float) (sr.getScaledHeight() - 380 * initAnimation.getOutput()), 280, 380);
        }

        mc.rubik_18.drawStringWithShadow("Лицензия закончится - " + "666", 2, sr.getScaledHeight() - 10, new Color(255, 255, 255).getRGB());
        mc.rubik_18.drawStringWithShadow("UID " + "Null", sr.getScaledWidth() - mc.rubik_18.getStringWidth("UID " + "Null") - 4, sr.getScaledHeight() - 9, new Color(255, 255, 255).getRGB());
        search.drawTextBox();
        if (search.getText().isEmpty() && !search.isFocused())
            mc.rubik_18.drawStringWithShadow("Search Feature...", (sr.getScaledWidth() / 2.0F + 362.0F), 86, -1);


        for (Panel panel : components) {
            panel.drawComponent(sr, mouseX, (int) (mouseY));
        }
        updateMouseWheel();
        super.drawScreen(mouseX, mouseY, partialTicks);

    }

    public void updateMouseWheel() {
        int scrollWheel = Mouse.getDWheel();
        for (Component panel : components) {
            if (scrollWheel > 0) {
                panel.setY(panel.getY() + 15);
            }
            if (scrollWheel < 0) {
                panel.setY(panel.getY() - 15);
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.selectedPanel.onKeyPress(keyCode);
        if (!escapeKeyInUse)
            super.keyTyped(typedChar, keyCode);
        search.textboxKeyTyped(typedChar, keyCode);
        if ((typedChar == '\t' || typedChar == '\r') && search.isFocused())
            search.setFocused(!search.isFocused());
        escapeKeyInUse = false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        search.setFocused(false);
        search.setText("");
        search.mouseClicked(mouseX, mouseY, mouseButton);
        for (Component component : components) {
            int x = component.getX();
            int y = component.getY();
            int cHeight = component.getHeight();
            if (component instanceof ExpandableComponent) {
                ExpandableComponent expandableComponent = (ExpandableComponent) component;
                if (expandableComponent.isExpanded())
                    cHeight = expandableComponent.getHeightWithExpand();
            }
            if (mouseX > x && mouseY > y && mouseX < x + component.getWidth() && mouseY < y + cHeight) {
                selectedPanel = component;
                component.onMouseClick(mouseX, mouseY, mouseButton);
                break;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        selectedPanel.onMouseRelease(state);
    }

    @Override
    public void onGuiClosed() {
        this.screenHelper = new ScreenHelper(0, 0);
        mc.entityRenderer.theShaderGroup = null;
        super.onGuiClosed();
    }
}
