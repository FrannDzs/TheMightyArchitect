package com.simibubi.mightyarchitect.buildomatico.model.sketch;

import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class Wall extends Design {

	public enum ExpandBehaviour {
		None, Repeat, MergedRepeat
	}

	public ExpandBehaviour expandBehaviour;

	@Override
	public Design fromNBT(NBTTagCompound compound) {
		Wall wall = new Wall();
		wall.applyNBT(compound);
		wall.expandBehaviour = ExpandBehaviour.valueOf(compound.getString("ExpandBehaviour"));
		return wall;
	}

	@Override
	public boolean fitsHorizontally(int width) {
		switch (expandBehaviour) {
		case MergedRepeat:
			return (width % (this.defaultWidth - 1)) == 1;
		case Repeat:
			return (width % this.defaultWidth) == 0;
		case None:
		default:
			return super.fitsHorizontally(width);
		}
	}

	@Override
	public void getBlocks(DesignInstance instance, Map<BlockPos, PaletteBlockInfo> blocks) {
		if (expandBehaviour == ExpandBehaviour.None) {
			super.getBlocks(instance, blocks);
			
		} else {
			boolean merge = expandBehaviour == ExpandBehaviour.MergedRepeat;
			int instances = merge ? (instance.width - 1) / (defaultWidth - 1) : instance.width / defaultWidth;
			int multiplierWidth = (merge ? defaultWidth - 1 : defaultWidth);
			for (int i = 0; i < instances; i++) {
				BlockPos shift = new BlockPos(i * multiplierWidth, 0, 0);
				super.getBlocksShifted(instance, blocks, shift);
			}

		}
	}

}
