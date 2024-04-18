package net.saint.crop_growth_modifier.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "crop_growth_modifier")
public class ModConfig implements ConfigData {
	@Comment("Chance for a random world tick to be forwarded to a crop block (percentage). Default: 1.0 (100%)")
	public float cropTickChance = 0.75f;

	@Comment("Chance for an extra crop tick roll to happen. Extra rolls also follow the crop tick chance. Accelerates crop growth. Default: 0 (0%)")
	public float cropExtraRollChance = 0f;

	@Comment("Maximum number of extra rolls that can be scheduled for a crop block from a single tick. Used to prevent infinite ticks for high chances. Default: 20")
	public int cropExtraRollMax = 20;
}