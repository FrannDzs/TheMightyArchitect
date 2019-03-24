package com.simibubi.mightyarchitect.item.symmetry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.simibubi.mightyarchitect.block.AllBlocks;
import com.simibubi.mightyarchitect.block.symmetry.BlockSymmetryCrossPlane;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SymmetryCrossPlane extends SymmetryElement {

	public static enum Align implements IStringSerializable {
		Y("y"),
		D("d");
		
		private final String name;
		private Align(String name) { this.name = name; }
		@Override public String getName() { return name; }
		@Override public String toString() { return name; }
	}

	public SymmetryCrossPlane(Vec3d pos) {
		super(pos);
		orientation = Align.Y;
	}

	@Override
	protected void setOrientation() {
		if (orientationIndex < 0) orientationIndex += Align.values().length;
		if (orientationIndex >= Align.values().length) orientationIndex -= Align.values().length;
		orientation = Align.values()[orientationIndex];
	}

	@Override
	public void setOrientation(int index) {
		this.orientation = Align.values()[index];
		orientationIndex = index;
	}

	@Override
	public Map<BlockPos, IBlockState> process(BlockPos position, IBlockState block) {
		Map<BlockPos, IBlockState> result = new HashMap<>();

		switch ((Align) orientation) {
		case D:
			result.put(flipD1(position), flipD1(block));
			result.put(flipD2(position), flipD2(block));
			result.put(flipD1(flipD2(position)), flipD1(flipD2(block)));
			break;
		case Y:
			result.put(flipX(position), flipX(block));
			result.put(flipZ(position), flipZ(block));
			result.put(flipX(flipZ(position)), flipX(flipZ(block)));
			break;
		default:
			break;
		}
		
		return result;
	}

	@Override
	public String typeName() {
		return CROSS_PLANE;
	}

	@Override
	public IBlockState getModel() {
		return AllBlocks.symmetry_crossplane.getDefaultState().withProperty(BlockSymmetryCrossPlane.align, (Align) orientation);
	}

	@Override
	public List<String> getAlignToolTips() {
		return ImmutableList.of("Orthogonal", "Diagonal");
	}
	
	

}
