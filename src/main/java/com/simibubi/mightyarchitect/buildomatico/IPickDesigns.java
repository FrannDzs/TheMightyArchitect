package com.simibubi.mightyarchitect.buildomatico;

import com.simibubi.mightyarchitect.buildomatico.model.groundPlan.GroundPlan;
import com.simibubi.mightyarchitect.buildomatico.model.sketch.Sketch;

public interface IPickDesigns {

	public Sketch assembleSketch(GroundPlan groundPlan);
	
}