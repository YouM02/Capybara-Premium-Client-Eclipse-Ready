package fun.rich.client.ui.clickgui.component.impl;

import fun.rich.client.feature.impl.hud.ClickGUI;
import fun.rich.client.ui.clickgui.component.Component;
import fun.rich.client.ui.clickgui.component.PropertyComponent;
import fun.rich.client.ui.settings.Setting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import fun.rich.client.utils.math.AnimationHelper;
import fun.rich.client.utils.math.MathematicHelper;
import fun.rich.client.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;


public class NumberSettingComponent extends fun.rich.client.ui.clickgui.component.Component implements PropertyComponent {

    public NumberSetting numberSetting;
    public float currentValueAnimate = 0f;
    private boolean sliding;
    Minecraft mc = Minecraft.getMinecraft();
    public NumberSettingComponent(Component parent, NumberSetting numberSetting, int x, int y, int width, int height) {
        super(parent, numberSetting.getName(), x, y, width, height);
        this.numberSetting = numberSetting;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);

        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        double min = numberSetting.getMinValue();
        double max = numberSetting.getMaxValue();
        boolean hovered = isHovered(mouseX, mouseY);

        if (this.sliding) {
            numberSetting.setValueNumber((float) MathematicHelper.round((double) (mouseX - x) * (max - min) / (double) width + min, numberSetting.getIncrement()));
            if (numberSetting.getNumberValue() > max) {
                numberSetting.setValueNumber((float) max);
            } else if (numberSetting.getNumberValue() < min) {
                numberSetting.setValueNumber((float) min);
            }
        }

        float amountWidth = (float) (((numberSetting.getNumberValue()) - min) / (max - min));
        Color onecolor = new Color(ClickGUI.color.getColorValue());


        currentValueAnimate = AnimationHelper.animation(currentValueAnimate, amountWidth, 0);
        float optionValue = (float) MathematicHelper.round(numberSetting.getNumberValue(), numberSetting.getIncrement());
        RenderUtils.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 111).getRGB());
        RenderUtils.drawRect(x + 3, y + height - 5, x + (width - 3), y + 13, new Color(40, 39, 39).getRGB());

        RenderUtils.drawRect(x + 3, y + 13.5, x + 5 + currentValueAnimate * (width - 8), y + 15F, ClickGUI.color.getColorValue());
        RenderUtils.drawBlurredShadow((float) (x + 3), (float) (y + 13.5), currentValueAnimate * (width - 8), 1,8, RenderUtils.injectAlpha(new Color(ClickGUI.color.getColorValue()),115));

        RenderUtils.drawFilledCircle((int) (x + 5 + currentValueAnimate * (width - 8)), (int) (y + 14F), 2.5F, new Color(ClickGUI.color.getColorValue()));
        RenderUtils.drawBlurredShadow((int) (x + 3 + currentValueAnimate * (width - 8)), (int) (y + 11), 6,5,8,RenderUtils.injectAlpha(new Color(ClickGUI.color.getColorValue()),115));

        String valueString = "";

        NumberSetting.NumberType numberType = numberSetting.getType();

        switch (numberType) {
            case PERCENTAGE:
                valueString += '%';
                break;
            case MS:
                valueString += "ms";
                break;
            case DISTANCE:
                valueString += 'm';
            case SIZE:
                valueString += "SIZE";
            case APS:
                valueString += "APS";
                break;
            default:
                valueString = "";
        }

        mc.neverlose500_13.drawStringWithShadow(numberSetting.getName(), x + 2.0F, y + height / 2.5F - 4F, Color.lightGray.getRGB());
        mc.neverlose500_14.drawStringWithShadow(optionValue + " " + valueString, x + width - mc.neverlose500_16.getStringWidth(optionValue + " " + valueString) - 5, y + height / 2.5F - 4F, Color.GRAY.getRGB());

        if (hovered) {
            if (numberSetting.getDesc() != null) {
                RenderUtils.drawSmoothRect(x + width + 20, y + height / 1.5F + 4.5F, x + width + 25 + mc.rubik_18.getStringWidth(numberSetting.getDesc()), y + 5.5F, new Color(0, 0, 0, 90).getRGB());
                mc.rubik_18.drawStringWithShadow(numberSetting.getDesc(), x + width + 22, y + height / 1.35F - 5F, -1);
            }
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (!sliding && button == 0 && isHovered(mouseX, mouseY)) {
            sliding = true;
        }
    }

    @Override
    public void onMouseRelease(int button) {
        this.sliding = false;
    }

    @Override
    public Setting getSetting() {
        return numberSetting;
    }
}
