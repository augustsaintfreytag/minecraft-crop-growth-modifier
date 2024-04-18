package net.saint.crop_growth_modifier.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "crop_growth_modifier")
public class ModConfig implements ConfigData {
    @Comment("Chance for a random world tick to be forwarded to a crop block (1/x). Examples: 1 is vanilla behavior, all ticks are passed; 2 means 1/2, half are passed. Default: 1 (1/1)")
    public int cropTickChance = 1;

	@Comment("Chance works to send another tick instead of preventing one, used to increase growth instead of decreasing it. Default: false")
	public boolean cropChanceInverted = false;
}