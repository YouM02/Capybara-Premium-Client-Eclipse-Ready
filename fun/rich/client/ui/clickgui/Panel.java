package fun.rich.client.ui.clickgui;

import fun.rich.client.Rich;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.feature.impl.hud.ClickGUI;
import fun.rich.client.ui.clickgui.component.AnimationState;
import fun.rich.client.ui.clickgui.component.Component;
import fun.rich.client.ui.clickgui.component.DraggablePanel;
import fun.rich.client.ui.clickgui.component.ExpandableComponent;
import fun.rich.client.ui.clickgui.component.impl.ModuleComponent;
import fun.rich.client.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.List;


public final class Panel extends DraggablePanel {
    Minecraft mc = Minecraft.getMinecraft();
    public static final int HEADER_WIDTH = 107;
    public static final int X_ITEM_OFFSET = 1;
    public static final int ITEM_HEIGHT = 15;
    public static final int HEADER_HEIGHT = 17;
    public List<Feature> features;
    public FeatureCategory type;
    public AnimationState state;
    private int prevX;
    private int prevY;
    private boolean dragging;

    public Panel(FeatureCategory category, int x, int y) {
        super(null, category.name(), x, y, HEADER_WIDTH, HEADER_HEIGHT);
        int moduleY = HEADER_HEIGHT;
        this.state = AnimationState.STATIC;
        this.features = Rich.instance.featureManager.getFeaturesCategory(category);
        for (Feature feature : features) {
            this.components.add(new ModuleComponent(this, feature, X_ITEM_OFFSET, moduleY, HEADER_WIDTH - (X_ITEM_OFFSET * 2), ITEM_HEIGHT));
            moduleY += ITEM_HEIGHT;
        }
        this.type = category;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        if (dragging) {
            setX(mouseX - prevX);
            setY(mouseY - prevY);
        }
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        int headerHeight;
        int heightWithExpand = getHeightWithExpand();
        headerHeight = (isExpanded() ? heightWithExpand : height);
        float startAlpha1 = 0.14f;
        int size1 = 25;
        float left1 = x + 1.0f;
        float right1 = x + width;
        float bottom1 = y + headerHeight - 6.0f;
        float top1 = y + headerHeight - 2.0f;
        float top2 = y + 13.0f;

        Color color = new Color(ClickGUI.color.getColorValue());

        float extendedHeight = 2;
        if (this.isExpanded()) {
            RenderUtils.renderShadowVertical(new Color(8, 8, 8, 255), 3.0f, startAlpha1, size1, right1 - 1.0f, top2, bottom1 + 2, true, false);
            RenderUtils.renderShadowVertical(new Color(8, 8, 8, 255), 3.0f, startAlpha1, size1, (double) left1 - 0.3, top2, bottom1 + 4, false, false);
        }
        if (ClickGUI.panelMode.currentMode.equals("Blur")) {
            RenderUtils.drawBlur(9, () -> {
                RenderUtils.drawSmoothRect(x, y + 14.5f, x + width, y + headerHeight - extendedHeight, new Color(20, 20, 20, 190).getRGB());
            });
            RenderUtils.drawSmoothRect(x, y + 14.5f, x + width, y + headerHeight - extendedHeight, new Color(20, 20, 20, 150).getRGB());
        } else if (ClickGUI.panelMode.currentMode.equals("Rect")) {
            RenderUtils.drawSmoothRect(x, y + 14.5f, x + width, y + headerHeight - extendedHeight, new Color(20, 20, 20, 150).getRGB());
        }
        RenderUtils.drawGradientSideways(x - 1, y + headerHeight - extendedHeight, x + width + 1, y + headerHeight - extendedHeight + 2 , color.getRGB(), color.darker().getRGB());
        if (!ClickGUI.potato_mode.getBoolValue()) {
            RenderUtils.drawBlurredShadow(x - 1, y + headerHeight - extendedHeight - 1.5f, width + 1, extendedHeight + 2, 5, RenderUtils.injectAlpha(color.darker(), 115));
        }
        RenderUtils.drawColorRect(x - 4, y,  x + width + 3,  y + this.getHeight() - 2.5f, new Color(15, 15, 15, 230), new Color(15, 15, 15, 230),new Color(25, 25, 25, 230),new Color(35, 35, 35, 230));

        mc.rubik_16.drawCenteredString(getName().toUpperCase(), x + 53.5f, y + HEADER_HEIGHT / 2F - 4, Color.LIGHT_GRAY.getRGB());

        super.drawComponent(scaledResolution, mouseX, mouseY);

        if (isExpanded()) {
            for (Component component : components) {
                component.setY(height);
                component.drawComponent(scaledResolution, mouseX, mouseY);
                int cHeight = component.getHeight();
                if (component instanceof ExpandableComponent) {
                    ExpandableComponent expandableComponent = (ExpandableComponent) component;
                    if (expandableComponent.isExpanded()) {
                        cHeight = expandableComponent.getHeightWithExpand() + 5;
                    }
                }
                height += cHeight;
            }
        }
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
        if (button == 0 && !this.dragging) {
            dragging = true;
            prevX = mouseX - getX();
            prevY = mouseY - getY();
        }
    }


    @Override
    public void onMouseRelease(int button) {
        super.onMouseRelease(button);
        dragging = false;
    }

    @Override
    public boolean canExpand() {
        return !features.isEmpty();
    }

    @Override
    public int getHeightWithExpand() {
        int height = getHeight();
        if (isExpanded()) {
            for (Component component : components) {
                int cHeight = component.getHeight();
                if (component instanceof ExpandableComponent) {
                    ExpandableComponent expandableComponent = (ExpandableComponent) component;
                    if (expandableComponent.isExpanded())
                        cHeight = expandableComponent.getHeightWithExpand() + 5;
                }
                height += cHeight;
            }
        }
        return height;
    }
}
