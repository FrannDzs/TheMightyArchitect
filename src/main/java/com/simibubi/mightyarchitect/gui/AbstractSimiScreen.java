package com.simibubi.mightyarchitect.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;

public abstract class AbstractSimiScreen extends Screen {

	protected int sWidth, sHeight;
	protected int topLeftX, topLeftY;
	protected List<Widget> widgets;
	
	protected AbstractSimiScreen() {
		super(new StringTextComponent(""));
		widgets = new ArrayList<>();
	}
	
	protected void setWindowSize(int width, int height) {
		sWidth = width;
		sHeight = height;
		topLeftX = (this.width - sWidth) / 2;
		topLeftY = (this.height - sHeight) / 2;
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		renderBackground();
		renderWindow(mouseX, mouseY, partialTicks);
		for (Widget widget : widgets)
			widget.render(mouseX, mouseY, partialTicks);
		renderWindowForeground(mouseX, mouseY, partialTicks);
		for (Widget widget : widgets)
			widget.renderToolTip(mouseX, mouseY);
	}
	
	@Override
	public boolean mouseClicked(double x, double y, int button) {
		for (Widget widget : widgets) {
			if (widget.mouseClicked(x, y, button))
				return true;
		}
		return super.mouseClicked(x, y, button);
	}
	
	@Override
	public boolean keyPressed(int code, int p_keyPressed_2_, int p_keyPressed_3_) {
		for (Widget widget : widgets) {
			if (widget.keyPressed(code, p_keyPressed_2_, p_keyPressed_3_))
				return true;
		}
		return super.keyPressed(code, p_keyPressed_2_, p_keyPressed_3_);
	}
	
	@Override
	public boolean charTyped(char character, int code) {
		for (Widget widget : widgets) {
			if (widget.charTyped(character, code))
				return true;
		}
		return super.charTyped(character, code);
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		for (Widget widget : widgets) {
			if (widget.mouseScrolled(mouseX, mouseY, delta))
				return true;
		}
		return super.mouseScrolled(mouseX, mouseY, delta);
	}
	
	@Override
	public boolean shouldCloseOnEsc() {
		return true;
	}
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}
	
	protected abstract void renderWindow(int mouseX, int mouseY, float partialTicks);
	
	protected void renderWindowForeground(int mouseX, int mouseY, float partialTicks) {
	}
	

}
