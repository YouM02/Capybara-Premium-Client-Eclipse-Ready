package fun.rich.client.ui.altmanager;

import fun.rich.client.Rich;
import fun.rich.client.feature.impl.misc.NameProtect;
import fun.rich.client.ui.altmanager.alt.Alt;
import fun.rich.client.ui.altmanager.alt.AltLoginThread;
import fun.rich.client.ui.altmanager.alt.AltManager;
import fun.rich.client.ui.altmanager.api.AltService;
import fun.rich.client.ui.mainmenu.RichMainMenu;
import fun.rich.client.utils.render.ColorUtils;
import fun.rich.client.utils.render.RenderUtils;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.RandomStringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiAltManager extends GuiScreen {
    public static final AltService altService = new AltService();
    public Alt selectedAlt = null;
    public String status;
    private GuiAltButton login;
    private GuiAltButton remove;
    private GuiAltButton rename;
    private AltLoginThread loginThread;
    private float offset;
    private GuiTextField searchField;

    private ResourceLocation resourceLocation;

    public GuiAltManager() {
        status = TextFormatting.DARK_GRAY + "(" + TextFormatting.GRAY + AltManager.registry.size() + TextFormatting.DARK_GRAY + ")";
    }

    private void getDownloadImageSkin(ResourceLocation resourceLocationIn, String username) {
        TextureManager textureManager = mc.getTextureManager();
        textureManager.getTexture(resourceLocationIn);
        ThreadDownloadImageData textureObject = new ThreadDownloadImageData(null, String.format("https://minotar.net/avatar/%s/64.png", StringUtils.stripControlCodes(username)), DefaultPlayerSkin.getDefaultSkin(AbstractClientPlayer.getOfflineUUID(username)), new ImageBufferDownload());
        textureManager.loadTexture(resourceLocationIn, textureObject);
    }

    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                break;
            case 1:
                (loginThread = new AltLoginThread(selectedAlt)).start();
                break;
            case 2:
                if (loginThread != null) {
                    loginThread = null;
                }

                AltManager.registry.remove(selectedAlt);
                status = TextFormatting.GREEN + "Removed.";

                selectedAlt = null;
                break;
            case 3:
                mc.displayGuiScreen(new GuiAddAlt(this));
                break;
            case 4:
                mc.displayGuiScreen(new GuiAltLogin(this));
                break;
            case 5:
                String randomName = RandomStringUtils.randomAlphabetic(5).toLowerCase() + RandomStringUtils.randomNumeric(2);
                (loginThread = new AltLoginThread(new Alt(randomName, ""))).start();
                AltManager.registry.add(new Alt(randomName, ""));
                break;
            case 6:
                mc.displayGuiScreen(new GuiRenameAlt(this));
                break;
            case 7:
                mc.displayGuiScreen(new RichMainMenu());
                break;
            case 8:
                /*try {
                    AltManager.registry.clear();
                    Main.instance.fileManager.getFile(Alts.class).loadFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                status = TextFormatting.RED + "Refreshed!";
                break;
            case 8931:
                mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 4545:
                mc.displayGuiScreen(new GuiConnecting(this, mc, new ServerData(I18n.format("selectServer.defaultName"), "play.hypixel.net", false)));
                break;
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        RenderUtils.drawRect(0.0, 0.0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(17, 17, 17, 255).getRGB());
        super.drawScreen(par1, par2, par3);
        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                this.offset += 26.0;
                if (this.offset < 0.0) {
                    this.offset = 0.0F;
                }
            } else if (wheel > 0) {
                this.offset -= 26.0;
                if (this.offset < 0.0) {
                    this.offset = 0.0F;
                }
            }
        }
        String altName = "Name: " + (Rich.instance.featureManager.getFeature(NameProtect.class).isEnabled() && NameProtect.myName.getBoolValue() ? "Protected" : this.mc.session.getUsername());
        mc.neverlose900_30.drawStringWithShadow("CAPYBARA PREMIUM", 11, 10, 14540253);

        this.mc.sfui18.drawStringWithShadow((Object)((Object)TextFormatting.GRAY) + "~ User Info ~", 16.0, 40, -1);
        this.mc.sfui18.drawStringWithShadow(altName, 11.0, 50, 0xDDDDDD);
        this.mc.sfui18.drawStringWithShadow("Account Status: " + (Object)((Object)TextFormatting.GREEN) + "Working", 11.0, 60, 0xDDDDDD);
        RenderUtils.drawRect(this.mc.sfui18.getStringWidth("Account Status: Working") + 14, this.mc.sfui18.getStringHeight("Account Status: Working") + 51, 9.0, this.mc.sfui18.getStringHeight("Account Status: Working") + 62, ColorUtils.getColor(255, 30));
        RenderUtils.drawRect(this.mc.sfui18.getStringWidth(altName) + 14, this.mc.sfui18.getStringHeight(altName) + 42, 9.0, this.mc.sfui18.getStringHeight(altName) + 51, ColorUtils.getColor(255, 30));
        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        this.mc.sfui18.drawCenteredString("Account Manager", (float)this.width / 2.0f, 10.0f, -1);
        this.mc.sfui18.drawCenteredString(this.loginThread == null ? this.status : this.loginThread.getStatus(), (float)this.width / 2.0f, 20.0f, -1);
        GlStateManager.pushMatrix();
        RenderUtils.scissorRect(0.0f, 33.0f, this.width, this.height - 50);
        GL11.glEnable(3089);
        int y = 38;
        int number = 0;
        Iterator<Alt> e = this.getAlts().iterator();
        while (true) {
            if (!e.hasNext()) {
                GL11.glDisable(3089);
                GL11.glPopMatrix();
                if (this.selectedAlt == null) {
                    this.login.enabled = false;
                    this.remove.enabled = false;
                } else {
                    this.login.enabled = true;
                    this.remove.enabled = true;
                }
                if (Keyboard.isKeyDown(200)) {
                    this.offset -= 26.0;
                } else if (Keyboard.isKeyDown(208)) {
                    this.offset += 26.0;
                }
                if (this.offset < 0.0) {
                    this.offset = 0.0F;
                }
                this.searchField.drawTextBox();
                if (this.searchField.getText().isEmpty() && !this.searchField.isFocused()) {
                    this.mc.sfui18.drawStringWithShadow("Search Alt", (float)this.width / 2.0f + 120, this.height - 18, ColorUtils.getColor(180));
                }
                return;
            }
            Alt alt = e.next();
            if (!this.isAltInArea(y)) continue;
            ++number;
            String name = alt.getMask().equals("") ? alt.getUsername() : alt.getMask();
            if (name.equalsIgnoreCase(this.mc.session.getUsername())) {
                name = "§n" + name;
            }
            String prefix = alt.getStatus().equals((Object)Alt.Status.Banned) ? "§c" : (alt.getStatus().equals((Object)Alt.Status.NotWorking) ? "§m" : "");
            name = prefix + name + "§r §7| " + alt.getStatus().toFormatted();
            String pass = alt.getPassword().equals("") ? "§cNot License" : alt.getPassword().replaceAll(".", "*");
            if (alt == this.selectedAlt) {
                GlStateManager.pushMatrix();
                boolean hovering = (float)par1 >= (float)this.width / 1.5f + 5.0f && (double)par1 <= (double)this.width / 1.5 + 26.0 && (double)par2 >= (double)y - this.offset - 4.0 && (double)par2 <= (double)y - this.offset + 20.0;
                GlStateManager.popMatrix();
                if (this.isMouseOverAlt(par1, par2, y) && Mouse.isButtonDown(0)) {
                    RenderUtils.drawBorderedRect((float)this.width / 2.0f - 125.0f, (double)y - this.offset - 4.0, (float)this.width / 1.5f, (double)y - this.offset + 30.0, 1.0, ColorUtils.getColor(255, 50), ColorUtils.getColor(40, 50));
                } else if (this.isMouseOverAlt(par1, par2, (double)y - this.offset)) {
                    RenderUtils.drawBorderedRect((float)this.width / 2.0f - 125.0f, (double)y - this.offset - 4.0, (float)this.width / 1.5f, (double)y - this.offset + 30.0, 1.0, ColorUtils.getColor(255, 50), ColorUtils.getColor(40, 50));
                } else {
                    RenderUtils.drawBorderedRect((float)this.width / 2.0f - 125.0f, (double)y - this.offset - 4.0, (float)this.width / 1.5f, (double)y - this.offset + 30.0, 1.0, ColorUtils.getColor(255, 50), ColorUtils.getColor(40, 50));
                }
            }
            String numberP = "§7" + number + ". §f";
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            if (this.resourceLocation == null) {
                this.resourceLocation = AbstractClientPlayer.getLocationSkin(name);
                this.getDownloadImageSkin(this.resourceLocation, name);
            } else {
                this.mc.getTextureManager().bindTexture(this.resourceLocation);
                GlStateManager.enableTexture2D();
                Gui.drawScaledCustomSizeModalRect((float)this.width / 2.0f - 161.0f, (float)y - (float)this.offset - 4.0f, 8.0f, 8.0f, 8.0f, 8.0f, 33.0f, 33.0f, 64.0f, 64.0f);
            }
            GlStateManager.popMatrix();
            mc.sfui18.drawCenteredString(numberP + (Rich.instance.featureManager.getFeature(NameProtect.class).isEnabled() && NameProtect.myName.getBoolValue() ? "Protected" : name), (float)this.width / 2.0f, (float)((double)y - this.offset + 5.0), -1);
            mc.sfui18.drawCenteredString((alt.getStatus().equals((Object)Alt.Status.NotWorking) ? "§m" : "") + pass, (float)this.width / 2.0f, (float)((double)y - this.offset + 15.0), ColorUtils.getColor(110));
            y += 40;
        }
    }


    public void initGui() {
        searchField = new GuiTextField(eventButton, mc.fontRendererObj, width / 2 + 116, height - 22, 72, 16);
        buttonList.add(login = new GuiAltButton(1, width / 2 - 122, height - 48, 100, 20, "Login"));
        buttonList.add(remove = new GuiAltButton(2, width / 2 - 40, height - 24, 70, 20, "Remove"));
        buttonList.add(new GuiAltButton(3, width / 2 + 4 + 86, height - 48, 100, 20, "Add"));
        buttonList.add(new GuiAltButton(4, width / 2 - 16, height - 48, 100, 20, "Direct Login"));
        buttonList.add(new GuiAltButton(5, width / 2 - 122, height - 24, 78, 20, "Random"));
        buttonList.add(rename = new GuiAltButton(6, width / 2 + 38, height - 24, 70, 20, "Edit"));
        buttonList.add(new GuiAltButton(7, width / 2 - 190, height - 24, 60, 20, "Back"));

        login.enabled = false;
        remove.enabled = false;
        rename.enabled = false;
    }

    protected void keyTyped(char par1, int par2) {
        searchField.textboxKeyTyped(par1, par2);
        if ((par1 == '\t' || par1 == '\r') && searchField.isFocused()) {
            searchField.setFocused(!searchField.isFocused());
        }

        try {
            super.keyTyped(par1, par2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isAltInArea(int y) {
        return y - offset <= height - 50;
    }

    private boolean isMouseOverAlt(double x, double y, double y1) {
        return x >= width / 2F - 125 && y >= y1 - 4 && x <= width / 1.5 && y <= y1 + 20 && x >= 0 && y >= 33 && x <= width && y <= height - 50;
    }

    protected void mouseClicked(int par1, int par2, int par3) {
        searchField.mouseClicked(par1, par2, par3);
        if (offset < 0) {
            offset = 0;
        }

        double y = 38 - offset;

        for (Iterator<Alt> e = getAlts().iterator(); e.hasNext(); y += 40) {
            Alt alt = e.next();
            if (isMouseOverAlt(par1, par2, y)) {
                if (alt == selectedAlt) {
                    actionPerformed(login);
                    return;
                }
                selectedAlt = alt;
            }
        }

        try {
            super.mouseClicked(par1, par2, par3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Alt> getAlts() {
        List<Alt> altList = new ArrayList<>();
        Iterator iterator = AltManager.registry.iterator();

        while (true) {
            Alt alt;
            do {
                if (!iterator.hasNext()) {
                    return altList;
                }

                alt = (Alt) iterator.next();
            }
            while (!searchField.getText().isEmpty() && !alt.getMask().toLowerCase().contains(searchField.getText().toLowerCase()) && !alt.getUsername().toLowerCase().contains(searchField.getText().toLowerCase()));

            altList.add(alt);
        }
    }
}
