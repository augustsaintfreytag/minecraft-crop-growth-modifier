package net.saint.crop_growth_modifier.mixinlogic;

import static net.minecraft.util.math.MathHelper.clamp;

import blue.endless.jankson.annotation.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.saint.crop_growth_modifier.Mod;
import net.saint.crop_growth_modifier.library.CowEntityMilkAccessor;

public interface AnimalEntityMixinLogic extends CowEntityMilkAccessor {

	public static final Identifier SYNC_PACKET_ID_MILK = new Identifier("crop_growth_modifier",
			"cow_entity_milk_sync");

	public static final String NBT_KEY_MILK = "MilkAmount";
	public static final String NBT_KEY_MILK_LAST_PRODUCED = "MilkLastProductionTick";

	// Init

	public default void initClientNetworking() {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			return;
		}

		ClientPlayNetworking.registerReceiver(SYNC_PACKET_ID_MILK, (client, handler, buffer, responseSender) -> {
			var entityId = buffer.readInt();
			var milkAmount = buffer.readFloat();
			var lastMilkProductionTime = buffer.readLong();

			client.execute(() -> {
				var world = client.world;
				var entity = world.getEntityById(entityId);

				if (entity instanceof CowEntityMilkAccessor cowEntity) {
					cowEntity.setMilkAmount(milkAmount);
					cowEntity.setLastMilkProductionTime(lastMilkProductionTime);
				}
			});
		});
	}

	public default void sendMilkSyncPacketToClients(CowEntity cowEntity) {
		var world = cowEntity.getWorld();

		if (world.isClient) {
			Mod.LOGGER.warn("Can not sync cow entity data from client thread.");
			return;
		}

		var entityId = cowEntity.getId();
		var milkAmount = getMilkAmount();
		var lastMilkProductionTime = getLastMilkProductionTime();

		var buffer = PacketByteBufs.create();

		buffer.writeInt(entityId);
		buffer.writeFloat(milkAmount);
		buffer.writeLong(lastMilkProductionTime);

		PlayerLookup.tracking(cowEntity).forEach(player -> {
			ServerPlayNetworking.send(player, SYNC_PACKET_ID_MILK, buffer);
		});
	}

	public default void deinitNetworking() {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			return;
		}

		ClientPlayNetworking.unregisterReceiver(SYNC_PACKET_ID_MILK);
	}

	// NBT

	public default void writeNbt(CowEntity cowEntity, NbtCompound nbt) {
		if (!Mod.config.cowLimitedMilkProduction) {
			return;
		}

		nbt.putFloat(NBT_KEY_MILK, this.getMilkAmount());
		nbt.putLong(NBT_KEY_MILK_LAST_PRODUCED, this.getLastMilkProductionTime());
	}

	public default void readNbt(CowEntity cowEntity, NbtCompound nbt) {
		if (!Mod.config.cowLimitedMilkProduction) {
			return;
		}

		if (nbt.contains(NBT_KEY_MILK)) {
			setMilkAmount(nbt.getFloat(NBT_KEY_MILK));
		}

		if (nbt.contains(NBT_KEY_MILK_LAST_PRODUCED)) {
			setLastMilkProductionTime(nbt.getLong(NBT_KEY_MILK_LAST_PRODUCED));
		}
	}
	// Logic

	public default void breedWithBaby(ServerWorld world, AnimalEntity other, @Nullable PassiveEntity baby) {
		var animalEntity = (AnimalEntity) (Object) this;
		var random = world.getRandom();

		var baseAnimalMultiplifer = 1 + random.nextFloat() * Mod.config.animalBreedingCooldownMultiplier;
		var baseAnimalCooldown = (int) (Mod.config.animalBreedingCooldown * baseAnimalMultiplifer);

		var otherAnimalMultiplifer = 1 + random.nextFloat() * Mod.config.animalBreedingCooldownMultiplier;
		var otherAnimalCooldown = (int) (Mod.config.animalBreedingCooldown * otherAnimalMultiplifer);

		animalEntity.setBreedingAge(baseAnimalCooldown);
		other.setBreedingAge(otherAnimalCooldown);
	}

	public default void mobTick(CowEntity cowEntity) {
		var world = cowEntity.getWorld();

		if (!Mod.config.cowLimitedMilkProduction || world.isClient) {
			return;
		}

		var time = world.getTime();

		if (time - getLastMilkProductionTime() < 100) {
			return;
		}

		var milkProductionAmount = getMilkAmount();

		setLastMilkProductionTime(time);
		setMilkAmount(clamp(milkProductionAmount + Mod.config.cowMilkProductionPerHundredTicks, 0,
				Mod.config.cowMilkProductionCapacity));

		sendMilkSyncPacketToClients(cowEntity);
	}

}