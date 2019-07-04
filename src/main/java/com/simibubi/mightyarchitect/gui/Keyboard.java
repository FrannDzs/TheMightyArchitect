package com.simibubi.mightyarchitect.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Keyboard {
	
	public static final int LSHIFT = 340;
	public static final int LALT = 342;
	public static final int RETURN = 257;
	
	public static final int DOWN = 264;
	public static final int LEFT = 263;
	public static final int RIGHT = 262;
	public static final int UP = 265;

	public static boolean isKeyDown(int key) {
		return InputMappings.isKeyDown(Minecraft.getInstance().mainWindow.getHandle(), key);
	}
	
}
