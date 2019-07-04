package com.simibubi.mightyarchitect.control.phase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import com.simibubi.mightyarchitect.control.Schematic;
import com.simibubi.mightyarchitect.control.SchematicHologram;
import com.simibubi.mightyarchitect.control.helpful.TessellatorHelper;
import com.simibubi.mightyarchitect.control.helpful.TessellatorTextures;
import com.simibubi.mightyarchitect.control.palette.Palette;
import com.simibubi.mightyarchitect.control.palette.PaletteDefinition;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.model.data.EmptyModelData;

public class PhaseCreatingPalette extends PhaseBase implements IListenForBlockEvents, IDrawBlockHighlights {

	private PaletteDefinition palette;
	private BlockPos center;
	private Map<BlockPos, Palette> grid;

	@Override
	public void whenEntered() {

		Schematic model = getModel();
		ClientWorld world = minecraft.world;

		palette = model.getCreatedPalette();
		center = world.getHeight(Heightmap.Type.WORLD_SURFACE, minecraft.player.getPosition());
		grid = new HashMap<>();

		for (int i = 0; i < 16; i++) {
			BlockPos pos = positionFromIndex(i);
			grid.put(pos, Palette.values()[i]);
			if (!world.isAirBlock(pos))
				palette.put(Palette.values()[i], world.getBlockState(pos));
		}

		model.updatePalettePreview();
		SchematicHologram.display(getModel());
	}

	@Override
	public void update() {
	}

	@Override
	public void render() {
		TessellatorHelper.prepareForDrawing();
		TessellatorTextures.RoomTransparent.bind();
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		for (int i = 0; i < 16; i++) {
			TessellatorHelper.cube(bufferBuilder, positionFromIndex(i), new BlockPos(1, 1, 1), 0.125, true, true);
		}

		Tessellator.getInstance().draw();

		minecraft.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

		for (int i = 0; i < 16; i++) {
			BlockState state = palette.get(Palette.values()[i]);
			PaletteDefinition paletteDef = getModel().isEditingPrimary() ? getModel().getPrimary()
					: getModel().getSecondary();
			
			if (state == null)
				continue;
			if (!state.equals(paletteDef.get(Palette.values()[i])))
				continue;
			
			GlStateManager.pushMatrix();
			BlockPos translate = positionFromIndex(i);
			minecraft.getBlockRendererDispatcher().renderBlock(state, translate, minecraft.world,
					bufferBuilder, minecraft.world.rand, EmptyModelData.INSTANCE);
			GlStateManager.popMatrix();
		}

		Tessellator.getInstance().draw();
		
		TessellatorHelper.cleanUpAfterDrawing();
		Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
	}

	@Override
	public void whenExited() {
		getModel().stopPalettePreview();
		SchematicHologram.reset();
	}
	
	@Override
	public void onBlockPlaced(BlockPos pos, BlockState state) {
		if (grid.containsKey(pos)) {
			palette.put(grid.get(pos), state);
			notifyChange();
		}
	}

	protected void notifyChange() {
		getModel().updatePalettePreview();
		SchematicHologram.getInstance().schematicChanged();
	}

	@Override
	public void onBlockBroken(BlockPos pos) {
		if (grid.containsKey(pos)) {
			PaletteDefinition paletteDef = getModel().isEditingPrimary() ? getModel().getPrimary()
					: getModel().getSecondary();
			Palette key = grid.get(pos);
			palette.put(key, paletteDef.get(key));
			notifyChange();
		}
	}

	@Override
	public void onBlockHighlight(DrawBlockHighlightEvent event) {
		RayTraceResult raytrace = event.getTarget();
		if (raytrace != null && raytrace.getType() == Type.BLOCK) {
			BlockPos targetBlock = new BlockPos(raytrace.getHitVec());

			if (grid.containsKey(targetBlock)) {
				TessellatorHelper.prepareForDrawing();
				TessellatorHelper.drawString(grid.get(targetBlock).getDisplayName(), targetBlock.getX() + 0.5f,
						targetBlock.up().getY() + 0.3f, targetBlock.getZ() + 0.5f, false, true);
				TessellatorHelper.cleanUpAfterDrawing();
			}

		}
	}

	private BlockPos positionFromIndex(int index) {
		return center.east(-3 + (index % 4) * 2).south(-3 + (index / 4) * 2);
	}
	
	@Override
	public List<String> getToolTip() {
		return ImmutableList.of("The Ghost blocks show the individual materials used in this build.", "Modify the palette by placing blocks into the marked areas. You do not have to fill all the gaps.", "Once finished, make sure to save it. [F]");
	}

}
