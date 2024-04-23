package net.saint.crop_growth_modifier.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "crop_growth_modifier")
public class ModConfig implements ConfigData {
	@Comment("Chance for a random world tick to be forwarded to a crop block (percentage). Default: 1.0 (100%)")
	public float cropTickChance = 0.75f;

	@Comment("Chance for an applied growth event to succeed and actually make the crop grow (e.g. bonemeal) (percentage). Default: 0.5 (50%)")
	public float cropGrowthChance = 0.5f;

	@Comment("Minimum amount of growth stages to apply to a crop block when applied, default is vanilla. Default: 2")
	public int cropGrowthAmountMin = 2;

	@Comment("Maximum amount of growth stages to apply to a crop block when applied, default is vanilla. Default: 5")
	public int cropGrowthAmountMax = 5;

	@Comment("Chance for an extra crop tick roll to happen. Extra rolls also follow the crop tick chance. Accelerates crop growth. Default: 0 (0%)")
	public float cropExtraRollChance = 0f;

	@Comment("Maximum number of extra rolls that can be scheduled for a crop block from a single tick. Used to prevent infinite ticks for high chances. Default: 20")
	public int cropExtraRollMax = 20;
}