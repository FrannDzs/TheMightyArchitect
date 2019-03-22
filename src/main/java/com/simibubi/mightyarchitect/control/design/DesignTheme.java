package com.simibubi.mightyarchitect.control.design;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.simibubi.mightyarchitect.control.design.partials.Design;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class DesignTheme {

	private String filePath;
	private String displayName;
	private String designer;
	private IPickDesigns designPicker;
	private boolean imported;
	
	private List<DesignLayer> layers;
	private List<DesignType> types;
	private Map<DesignLayer, Map<DesignType, Set<Design>>> designs;

	public DesignTheme(String filePath, String displayName, String designer, IPickDesigns designPicker) {
		this.filePath = filePath;
		this.designer = designer;
		this.displayName = displayName;
		this.designPicker = designPicker;
		this.designPicker.setTheme(this);
		imported = false;
	}

	public DesignTheme withLayers(DesignLayer... designLayers) {
		layers = ImmutableList.copyOf(designLayers);
		return this;
	}

	public DesignTheme withTypes(DesignType... designtypes) {
		types = ImmutableList.copyOf(designtypes);
		return this;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getDisplayName() {
		return displayName;
	}

	public IPickDesigns getDesignPicker() {
		return designPicker;
	}

	public List<DesignLayer> getLayers() {
		return layers;
	}

	public List<DesignType> getTypes() {
		return types;
	}
	
	public boolean isImported() {
		return imported;
	}

	public Set<Design> getDesigns(DesignLayer designLayer, DesignType designType) {
		if (designs == null) {
			designs = DesignResourceLoader.loadDesignsForTheme(this);
		}

		if (!getLayers().contains(designLayer))
			fail(getDisplayName() + " theme does not support " + designLayer.getDisplayName() + " layers.");
		if (!getTypes().contains(designType))
			fail(getDisplayName() + " theme does not support the type " + designLayer.getDisplayName() + ".");

		if (designs.containsKey(designLayer)) {
			Map<DesignType, Set<Design>> typeMap = designs.get(designLayer);

			if (typeMap.containsKey(designType)) {
				return typeMap.get(designType);
			}
		}

		return new HashSet<>();
	}

	private void fail(String message) {
		throw new IllegalArgumentException(message);
	}

	public void clearDesigns() {
		designs = null;
	}
	
	public void setDesigner(String designer) {
		this.designer = designer;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDesigner() {
		return designer;
	}
	
	public void setLayers(List<DesignLayer> layers) {
		this.layers = layers;
	}
	
	public void setTypes(List<DesignType> types) {
		this.types = types;
	}

	public NBTTagCompound asTagCompound() {
		NBTTagCompound compound = new NBTTagCompound();

		compound.setString("Name", getDisplayName());
		compound.setString("Designer", getDesigner());

		NBTTagList layers = new NBTTagList();
		NBTTagList types = new NBTTagList();

		this.layers.forEach(layer -> layers.appendTag(new NBTTagString(layer.name())));
		this.types.forEach(type -> types.appendTag(new NBTTagString(type.name())));

		compound.setTag("Layers", layers);
		compound.setTag("Types", types);

		return compound;
	}

	public static DesignTheme fromNBT(String filepath, NBTTagCompound compound) {
		if (compound == null)
			return null;

		DesignTheme theme = new DesignTheme(filepath, compound.getString("Name"), compound.getString("Designer"),
				new StandardDesignPicker());

		theme.layers = new ArrayList<>();
		theme.types = new ArrayList<>();

		compound.getTagList("Layers", 8)
				.forEach(s -> theme.layers.add(DesignLayer.valueOf(((NBTTagString) s).getString())));
		compound.getTagList("Types", 8)
				.forEach(s -> theme.types.add(DesignType.valueOf(((NBTTagString) s).getString())));

		theme.imported = true;
		return theme;
	}

}
