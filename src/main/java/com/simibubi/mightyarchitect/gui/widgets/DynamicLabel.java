package com.simibubi.mightyarchitect.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class DynamicLabel extends AbstractSimiWidget {

	public String text;
	protected boolean hasShadow;
	protected int color;
	protected FontRenderer font;
	
	public DynamicLabel(int x, int y, String text) {
		super(x, y, Minecraft.getInstance().fontRenderer.getStringWidth(text), 10);
		font = Minecraft.getInstance().fontRenderer;
		this.text = "Label";
		color = 0xFFFFFF;
		hasShadow = false;
	}
	
	public DynamicLabel colored(int color) {
		this.color = color;
		return this;
	}
	
	public DynamicLabel withShadow() {
		this.hasShadow = true;
		return this;
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		if (!visible)
			return;
		
		if (hasShadow)
			font.drawStringWithShadow(text, x, y, color);
		else
			font.drawString(text, x, y, color);
	}
	
}
