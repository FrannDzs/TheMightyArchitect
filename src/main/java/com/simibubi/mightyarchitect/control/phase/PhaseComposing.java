package com.simibubi.mightyarchitect.control.phase;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.simibubi.mightyarchitect.control.compose.planner.Tools;
import com.simibubi.mightyarchitect.control.helpful.ShaderManager;
import com.simibubi.mightyarchitect.control.helpful.Shaders;
import com.simibubi.mightyarchitect.control.helpful.TessellatorHelper;
import com.simibubi.mightyarchitect.gui.Keyboard;

import net.minecraft.client.MainWindow;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;

public class PhaseComposing extends PhaseBase implements IRenderGameOverlay {

	private Tools activeTool;

	@Override
	public void whenEntered() {
		activeTool = Tools.Room;
		activeTool.getTool().init();

		ShaderManager.setActiveShader(Shaders.Blueprint);
	}

	@Override
	public void update() {
		activeTool.getTool().updateSelection();
	}

	@Override
	public void onClick(int button) {
		if (button == 1) {
			String message = activeTool.getTool().handleRightClick();
			sendStatusMessage(message);
		}

	}

	@Override
	public void onKey(int key) {
		if (key == Keyboard.RIGHT) {
			activeTool = activeTool.next();
			
			if (!getModel().getGroundPlan().theme.getStatistics().hasTowers) {
				if (activeTool == Tools.Cylinder)
					activeTool = activeTool.next();					
			}
			
			activeTool.getTool().init();
			return;
		}

		if (key == Keyboard.LEFT) {
			activeTool = activeTool.previous();
			
			if (!getModel().getGroundPlan().theme.getStatistics().hasTowers) {
				if (activeTool == Tools.Cylinder)
					activeTool = activeTool.previous();					
			}
			
			activeTool.getTool().init();
			return;
		}
	}
	
	@Override
	public boolean onScroll(int amount) {
		return activeTool.getTool().handleMouseWheel(amount);
	}

	@Override
	public void render() {
		TessellatorHelper.prepareForDrawing();
		activeTool.getTool().renderGroundPlan();
		activeTool.getTool().renderTool();
		TessellatorHelper.cleanUpAfterDrawing();
	}

	@Override
	public void whenExited() {
		ShaderManager.stopUsingShaders();
	}

	@Override
	public void renderGameOverlay(Post event) {
		MainWindow window = minecraft.mainWindow;
		minecraft.fontRenderer.drawString(activeTool.getDisplayName(), window.getScaledWidth() / 2 + 15,
				window.getScaledHeight() / 2 + 5, 0xDDDDDD);
	}

	@Override
	public List<String> getToolTip() {
		return ImmutableList.of("Draw the layout of your build, adding rooms, towers and other. Modify their size, style and palette using the Selection Tool.", "Use your < > Arrow Keys to switch tools.");
	}

}
