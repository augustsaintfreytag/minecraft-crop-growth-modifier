package net.saint.crop_growth_modifier.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "crop_growth_modifier")
public class ModConfig implements ConfigData {
	@Comment("Chance for a random world tick to be forwarded to a crop block (percentage). Default: 1.0 (100%)")
	public float cropTickChance = 1.0f;

	@Comment("Chance for an applied growth event to succeed and actually make the crop grow (e.g. bonemeal) (percentage). Default: 1.0 (100%)")
	public float cropGrowthChance = 1.0f;

	@Comment("Multiplier for the growth speed of stem-based blocks (e.g. pumpkins, melons) (0.5 = stem blocks grow half the speed of crops). Don't set higher than 1.0. Default: 1.0 (1x)")
	public float stemBlockMultiplier = 1.0f;

	@Comment("Minimum number of stages a crop block grows when growth happens, default is vanilla. Default: 2")
	public int cropGrowthStagesMin = 2;

	@Comment("Maximum number of stages a crop block grows when growth happens, default is vanilla. Default: 5")
	public int cropGrowthStagesMax = 5;

	@Comment("Chance for an extra crop tick roll to happen. Extra rolls also follow the crop tick chance. Accelerates crop growth. Default: 0 (0%)")
	public float cropExtraRollChance = 0f;

	@Comment("Maximum number of extra rolls that can be scheduled for a crop block from a single tick. Used to prevent infinite ticks for high chances. Default: 20")
	public int cropExtraRollMax = 20;

	@Comment("Enable limited milk production for cows. Default: false")
	public boolean cowLimitedMilkProduction = false;

	@Comment("Rate for cows to produce buckets of milk per 100 ticks (5 seconds). 1.0f = 1 bucket. Cows may hold more than one bucket. Formula for time calculation in minutes: (1 / <rate value> * (100/20)) / 60. Default: 0.00825 (approx. 10 minutes)")
	public float cowMilkProductionPerHundredTicks = 0.00825f;

	@Comment("Maximum amount of milk a cow can hold in buckets. Default: 2.0 buckets")
	public float cowMilkProductionCapacity = 2.0f;

	@Comment("Maximum amount of milk a cow can initially get randomly set to. Default: 0.5 (50%)")
	public float cowMilkInitialRandomFraction = 0.5f;
}