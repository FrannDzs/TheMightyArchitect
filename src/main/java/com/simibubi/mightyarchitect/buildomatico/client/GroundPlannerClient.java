package com.simibubi.mightyarchitect.buildomatico.client;

import com.simibubi.mightyarchitect.buildomatico.RaycastHelper;
import com.simibubi.mightyarchitect.buildomatico.TessellatorHelper;
import com.simibubi.mightyarchitect.buildomatico.model.groundPlan.Cuboid;
import com.simibubi.mightyarchitect.buildomatico.model.groundPlan.GroundPlan;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextComponentString;

public class GroundPlannerClient {

	private static Minecraft mc;
	private static GroundPlannerClient instance;

	private boolean active;
	private BlockPos anchor;
	private GroundPlan groundPlan;
	private GuiScreen openedGuiNextTick;
	private GroundPlanRenderer renderer;

	private BlockPos firstPosition;
	private BlockPos selectedPosition;

	public GroundPlannerClient() {
		groundPlan = new GroundPlan();
		renderer = new GroundPlanRenderer(mc);
	}

	public static boolean isActive() {
		return instance != null && instance.active;
	}
	
	public static boolean isPresent() {
		return instance != null;
	}

	public static GroundPlannerClient getInstance() {
		return instance;
	}

	public static void startComposing() {
		mc = Minecraft.getMinecraft();
		instance = new GroundPlannerClient();
		instance.active = true;
	}
	
	public static void reset() {
		instance = null;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void handleSelect() {
		if (selectedPosition == null)
			return;
		
		if (anchor == null)
			anchor = selectedPosition;
		BlockPos actualPos = selectedPosition.subtract(anchor);

		if (firstPosition == null) {
			for (Cuboid c : groundPlan.getAll()) {
				if (c.contains(actualPos)) {
					setOpenedGuiNextTick(new GuiComposer(c));
					return;
				}
			}

			firstPosition = actualPos;
			mc.player.sendStatusMessage(new TextComponentString("First position marked"), true);
			return;

		} else {
			Cuboid c = new Cuboid(firstPosition, actualPos.subtract(firstPosition));
			c.width++; c.length++;
			c.height = 4;
			int facadeWidth = Math.min(c.width, c.length);

			if (facadeWidth % 2 == 0) {
				mc.player.sendStatusMessage(new TextComponentString("�cFacade cannot have even width: " + facadeWidth),
						true);
				return;
			}
			if (facadeWidth < 5) {
				mc.player.sendStatusMessage(new TextComponentString("�cFacade is too narrow (<5): " + facadeWidth),
						true);
				return;
			}
			if (facadeWidth > 15) {
				mc.player.sendStatusMessage(new TextComponentString("�cFacade is too wide (>15): " + facadeWidth),
						true);
				return;
			}

			mc.player.sendStatusMessage(new TextComponentString("�aNew Cuboid was added"), true);
			groundPlan.add(c, 0);
			firstPosition = null;
		}
	}

	public GroundPlan getGroundPlan() {
		return groundPlan;
	}

	public BlockPos getAnchor() {
		return anchor;
	}

	public void update() {
		EntityPlayerSP player = mc.player;

		if (openedGuiNextTick != null) {
			mc.displayGuiScreen(instance.openedGuiNextTick);
			openedGuiNextTick = null;
		}

		RayTraceResult trace = RaycastHelper.rayTraceRange(player.world, player, 75);
		if (trace != null && trace.typeOfHit == Type.BLOCK) {

			BlockPos hit = trace.getBlockPos();
			if (trace.sideHit.getAxis() == Axis.Y)
				hit = hit.offset(trace.sideHit);
			selectedPosition = hit;

			if (firstPosition != null) {
				BlockPos size = selectedPosition.subtract(anchor.add(firstPosition));
				if (size.getX() % 2 != 0) {
					selectedPosition = selectedPosition.east(size.getX() > 0 ? 1 : -1);
				}
				if (size.getZ() % 2 != 0) {
					selectedPosition = selectedPosition.south(size.getZ() > 0 ? 1 : -1);
				}
			}

		} else {
			selectedPosition = null;
		}
	}
	
	public void render() {
		TessellatorHelper.prepareForDrawing();
		renderer.renderSelection(selectedPosition, firstPosition, anchor);
		renderer.renderGroundPlan(groundPlan, anchor);
		TessellatorHelper.cleanUpAfterDrawing();
	}
	
	public void setOpenedGuiNextTick(GuiScreen openedGuiNextTick) {
		this.openedGuiNextTick = openedGuiNextTick;
	}

}