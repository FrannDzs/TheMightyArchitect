package com.simibubi.mightyarchitect.block.symmetry;

import com.simibubi.mightyarchitect.item.symmetry.SymmetryCrossPlane;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;

public class BlockSymmetryCrossPlane extends BlockSymmetry {

	public static final EnumProperty<SymmetryCrossPlane.Align> align = EnumProperty.create("align",
			SymmetryCrossPlane.Align.class);

	public BlockSymmetryCrossPlane() {
		super(Properties.create(Material.AIR));
		this.setDefaultState(getDefaultState().with(align, SymmetryCrossPlane.Align.Y));
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(align);
		super.fillStateContainer(builder);
	}

}
