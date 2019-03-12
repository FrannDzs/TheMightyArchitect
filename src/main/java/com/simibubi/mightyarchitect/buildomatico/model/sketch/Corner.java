package com.simibubi.mightyarchitect.buildomatico.model.sketch;

import net.minecraft.nbt.NBTTagCompound;

public class Corner extends Design {
	
	@Override
	public Design fromNBT(NBTTagCompound compound) {
		Corner corner = new Corner();
		corner.applyNBT(compound);
		return corner;
	}

}
