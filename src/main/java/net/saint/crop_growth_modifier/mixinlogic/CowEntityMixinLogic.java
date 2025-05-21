package net.saint.crop_growth_modifier.mixinlogic;

import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.random.Random;
import net.saint.crop_growth_modifier.Mod;
import net.saint.crop_growth_modifier.library.CowEntityMilkAccessor;
import net.saint.crop_growth_modifier.library.CowEntityMilkActionResult;

public interface CowEntityMixinLogic extends CowEntityMilkAccessor {

	// Logic

	public default CowEntityMilkActionResult onInteractMob(CowEntity cowEntity, PlayerEntity player, Hand hand) {
		if (!Mod.config.cowLimitedMilkProduction) {
			return CowEntityMilkActionResult.DidNothing;
		}

		var world = player.getWorld();
		var itemStack = player.getStackInHand(hand);

		if (cowEntity.isBaby()) {
			return CowEntityMilkActionResult.DidNothing;
		}

		if (player.getStackInHand(hand).isEmpty() && player.isSneaking()) {
			if (!world.isClient) {
				var formattedMilkCapacity = String.format("%.2f", getMilkAmount());
				var milkMessage = Text.translatable("text.crop_growth_modifier.cow_milk_capacity",
						formattedMilkCapacity);
				player.sendMessage(milkMessage, true);
			}

			return CowEntityMilkActionResult.DidCancelAndReport;
		}

		if (!itemStack.isOf(Items.BUCKET)) {
			return CowEntityMilkActionResult.DidNothing;
		}

		var milkProductionAmount = getMilkAmount();

		if (milkProductionAmount < 1.0f) {
			if (!world.isClient) {
				var milkMessage = Text.translatable("text.crop_growth_modifier.cow_milk_empty");
				player.sendMessage(milkMessage, true);
			}

			return CowEntityMilkActionResult.DidCancelAndReport;
		}

		// NOTICE: This is a workaround to manually build what `CowEntity` already does
		// but with the mixin deferring to the original logic, interaction sounds won't
		// play.

		if (world.isClient) {
			setMilkAmount(milkProductionAmount - 1.0f);
			player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
		}

		var stackWithMilk = ItemUsage.exchangeStack(itemStack, player, Items.MILK_BUCKET.getDefaultStack());
		player.setStackInHand(hand, stackWithMilk);

		return CowEntityMilkActionResult.DidMilk;
	}

	public default float getInitialRandomMilkAmount(Random random, CowEntity cowEntity) {
		float maxInitialMilkAmount = Mod.config.cowMilkProductionCapacity * Mod.config.cowMilkInitialRandomFraction;
		float initialMilkAmount = random.nextFloat() * maxInitialMilkAmount;

		return initialMilkAmount;
	}

}
