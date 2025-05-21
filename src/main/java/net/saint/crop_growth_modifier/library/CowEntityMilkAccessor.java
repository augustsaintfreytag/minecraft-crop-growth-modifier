package net.saint.crop_growth_modifier.library;

public interface CowEntityMilkAccessor {

	public float getMilkAmount();

	public void setMilkAmount(float milkProductionAmount);

	public long getLastMilkProductionTime();

	public void setLastMilkProductionTime(long lastMilkProductionTime);

}
