package com.simibubi.mightyarchitect.item.symmetry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.simibubi.mightyarchitect.block.AllBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SymmetryTriplePlane extends SymmetryElement {

	public SymmetryTriplePlane(Vec3d pos) {
		super(pos);
		orientationIndex = 0;
	}

	@Override
	public Map<BlockPos, IBlockState> process(BlockPos position, IBlockState block) {
		Map<BlockPos, IBlockState> result = new HashMap<>();

		result.put(flipX(position), flipX(block));
		result.put(flipZ(position), flipZ(block));
		result.put(flipX(flipZ(position)), flipX(flipZ(block)));

		result.put(flipD1(position), flipD1(block));
		result.put(flipD1(flipX(position)), flipD1(flipX(block)));
		result.put(flipD1(flipZ(position)), flipD1(flipZ(block)));
		result.put(flipD1(flipX(flipZ(position))), flipD1(flipX(flipZ(block))));

		return result;
	}

	@Override
	public String typeName() {
		return TRIPLE_PLANE;
	}

	@Override
	public IBlockState getModel() {
		return AllBlocks.symmetry_tripleplane.getDefaultState();
	}

	@Override
	protected void setOrientation() {
	}

	@Override
	public void setOrientation(int index) {
	}
	
	@Override
	public IStringSerializable getOrientation() {
		return SymmetryCrossPlane.Align.Y;
	}
	
	@Override
	public List<String> getAlignToolTips() {
		return ImmutableList.of("Horizontal");
	}

}
