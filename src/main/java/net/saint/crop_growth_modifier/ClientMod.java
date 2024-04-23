package net.saint.crop_growth_modifier;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;

public class ClientMod implements ClientModInitializer, RenderAttachmentBlockEntity {
	@Override
	public void onInitializeClient() {
	}

	@Override
	public @Nullable Object getRenderAttachmentData() {
		return null;
	}
}
