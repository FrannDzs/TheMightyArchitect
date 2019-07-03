package com.simibubi.mightyarchitect.item.symmetry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.simibubi.mightyarchitect.AllBlocks;
import com.simibubi.mightyarchitect.block.symmetry.BlockSymmetryPlane;

import net.minecraft.block.BlockState;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SymmetryPlane extends SymmetryElement {

	public static enum Align implements IStringSerializable {
		XY("xy"), YZ("yz");

		private final String name;

		private Align(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public SymmetryPlane(Vec3d pos) {
		super(pos);
		orientation = Align.XY;
	}

	@Override
	protected void setOrientation() {
		if (orientationIndex < 0)
			orientationIndex += Align.values().length;
		if (orientationIndex >= Align.values().length)
			orientationIndex -= Align.values().length;
		orientation = Align.values()[orientationIndex];
	}

	@Override
	public void setOrientation(int index) {
		this.orientation = Align.values()[index];
		orientationIndex = index;
	}

	@Override
	public Map<BlockPos, BlockState> process(BlockPos position, BlockState block) {
		Map<BlockPos, BlockState> result = new HashMap<>();
		switch ((Align) orientation) {

		case XY:
			result.put(flipZ(position), flipZ(block));
			break;
		case YZ:
			result.put(flipX(position), flipX(block));
			break;
		default:
			break;

		}
		return result;
	}

	@Override
	public String typeName() {
		return PLANE;
	}

	@Override
	public BlockState getModel() {
		return AllBlocks.SYMMETRY_PLANE.block.getDefaultState().with(BlockSymmetryPlane.align, (Align) orientation);
	}

	@Override
	public List<String> getAlignToolTips() {
		return ImmutableList.of("Mirror Z", "Mirror X");
	}

}
