package com.simibubi.mightyarchitect.buildomatico.client;

import com.simibubi.mightyarchitect.buildomatico.PaletteDefinition;
import com.simibubi.mightyarchitect.buildomatico.PaletteStorage;
import com.simibubi.mightyarchitect.buildomatico.model.schematic.Schematic;
import com.simibubi.mightyarchitect.buildomatico.model.sketch.Sketch;

public class PalettePickerClient {

	private static PalettePickerClient instance;
	private PaletteDefinition primary;
	private PaletteDefinition secondary;
	private Schematic schematic;
	

	public static void initWithDefault() {
		instance = new PalettePickerClient();
		instance.primary = PaletteDefinition.defaultPalette().clone();
		instance.secondary = PaletteDefinition.defaultPalette().clone();
	}

	public static boolean isPresent() {
		return instance != null;
	}

	public static void reset() {
		instance = null;
	}

	public static PalettePickerClient getInstance() {
		return instance;
	}

	public static Schematic providePalette(Sketch sketch) {
		if (!isPresent())
			initWithDefault();
		Schematic schematic = new Schematic(sketch, instance.primary, instance.secondary);
		instance.schematic = schematic;
		return schematic;
	}

	public PaletteDefinition getPrimary() {
		return primary;
	}

	public void setPrimary(PaletteDefinition primary) {
		this.primary = primary;
		schematic.swapPrimaryPalette(primary);
	}

	public PaletteDefinition getSecondary() {
		return secondary;
	}

	public void setSecondary(PaletteDefinition secondary) {
		this.secondary = secondary;
		schematic.swapSecondaryPalette(secondary);
	}
	
	public Schematic setSketch(Sketch sketch) {
		schematic.swapSketch(sketch);
		return schematic;
	}
	
	public Schematic getSchematic() {
		return schematic;
	}
	
	public void reapplyCurrentPalettes() {
		PaletteDefinition primary = PaletteStorage.getPalette(getPrimary().getName());
		PaletteDefinition secondary = PaletteStorage.getPalette(getSecondary().getName());
		this.primary = primary;
		this.secondary = secondary;
		schematic.swapPalettes(primary, secondary);
	}

}
