package net.saint.crop_growth_modifier.mixinlogic;

import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.random.Random;
import net.saint.crop_growth_modifier.Mod;

public interface CowEntityMixinLogic {

	// Properties

	public float getMilkAmount();

	public void setMilkAmount(float milkProductionAmount);

	// Logic

	public default boolean onInteractMob(CowEntity cowEntity, PlayerEntity player, Hand hand) {
		var world = player.getWorld();
		var itemStack = player.getStackInHand(hand);

		if (cowEntity.isBaby()) {
			return false;
		}

		if (player.getStackInHand(hand).isEmpty() && player.isSneaking()) {
			if (!world.isClient) {
				var formattedMilkCapacity = String.format("%.2f", getMilkAmount());
				var milkMessage = Text.translatable("text.crop_growth_modifier.cow_milk_capacity",
						formattedMilkCapacity);
				player.sendMessage(milkMessage, true);
			}

			return false;
		}

		if (!itemStack.isOf(Items.BUCKET)) {
			return false;
		}

		var milkProductionAmount = getMilkAmount();

		if (milkProductionAmount < 1.0f) {
			if (!world.isClient) {
				var milkMessage = Text.translatable("text.crop_growth_modifier.cow_milk_empty");
				player.sendMessage(milkMessage, true);
			}

			return false;
		}

		setMilkAmount(milkProductionAmount - 1.0f);
		return true;
	}

	public default float getInitialRandomMilkAmount(Random random, CowEntity cowEntity) {
		var maxInitialMilkAmount = Mod.config.cowMilkProductionCapacity * Mod.config.cowMilkInitialRandomFraction;
		var initialMilkAmount = (float) (random.nextBetween(0, (int) maxInitialMilkAmount * 1000)
				/ 1000);

		return initialMilkAmount;
	}

}
