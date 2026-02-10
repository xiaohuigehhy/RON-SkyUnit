package net.xiaohuige_hhy.skyunit.mixin.sandbox;

import static com.solegendary.reignofnether.util.MiscUtil.fcs;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.building.BuildingPlaceButton;
import com.solegendary.reignofnether.building.production.ProductionItems;
import com.solegendary.reignofnether.faction.Faction;
import com.solegendary.reignofnether.faction.FactionRegistries;
import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.hud.buttons.UnitSpawnButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.sandbox.SandboxClientEvents;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.xiaohuige_hhy.skyunit.SkyUnit;
import net.xiaohuige_hhy.skyunit.SkyUnitConfigs;
import net.xiaohuige_hhy.skyunit.building.production.SkyUnitProductionItems;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SandboxClientEvents.class)
public class SandboxClientEventsMixin {
	
	@Shadow
	private static Faction faction;
	
	@Inject(method = "getUnitButtons", at = @At("HEAD"), remap = false, cancellable = true)
	private static void getSkyUnitUnitButtons(CallbackInfoReturnable<List<UnitSpawnButton>> cir) {
		
		if (faction == Faction.VILLAGERS) {
			cir.setReturnValue(List.of(
					ProductionItems.VILLAGER.getPlaceButton(),
					ProductionItems.VINDICATOR.getPlaceButton(),
					ProductionItems.PILLAGER.getPlaceButton(),
					ProductionItems.IRON_GOLEM.getPlaceButton(),
					ProductionItems.WITCH.getPlaceButton(),
					ProductionItems.EVOKER.getPlaceButton(),
					ProductionItems.RAVAGER.getPlaceButton(),
					SkyUnitProductionItems.ILLUSIONER.getPlaceButton(),
					ProductionItems.ROYAL_GUARD.getPlaceButton()
				)
			);
			cir.cancel();
		}
		
		if (faction == Faction.NONE) {
			cir.setReturnValue(
				SkyUnitConfigs.selectedSkyUnitFaction ?
					SkyUnitConfigs.unitPlaceButtons
					:
					List.of(
						ProductionItems.ENDERMAN.getPlaceButton(),
						ProductionItems.POLAR_BEAR.getPlaceButton(),
						ProductionItems.GRIZZLY_BEAR.getPlaceButton(),
						ProductionItems.PANDA.getPlaceButton(),
						ProductionItems.WOLF.getPlaceButton(),
						ProductionItems.LLAMA.getPlaceButton()
					)
			);
			cir.cancel();
		}
	}
	
	@Inject(method = "getNeutralBuildingButtons", at = @At(value = "RETURN"), remap = false, cancellable = true)
	private static void addSkyUnitBuildingButtons(CallbackInfoReturnable<List<BuildingPlaceButton>> cir) {
		if (SkyUnitConfigs.selectedSkyUnitFaction)
			cir.setReturnValue(SkyUnitConfigs.SKYUNIT.getBuildingButtons());
		else
			cir.setReturnValue(FactionRegistries.NONE.getBuildingButtons());
	}
	
	@Inject(method = "getToggleFactionButton", at = @At(value = "RETURN"), remap = false, cancellable = true)
	private static void getToggleSkyUnitFactionButton(CallbackInfoReturnable<Button> cir) {
		cir.setReturnValue(
			new Button(
				"Toggle Faction",
				Button.itemIconSize,
				switch (faction) {
					case VILLAGERS -> ResourceLocation.fromNamespaceAndPath(ReignOfNether.MOD_ID, "textures/mobheads/villager.png");
					case MONSTERS -> ResourceLocation.fromNamespaceAndPath(ReignOfNether.MOD_ID, "textures/mobheads/creeper.png");
					case PIGLINS -> ResourceLocation.fromNamespaceAndPath(ReignOfNether.MOD_ID, "textures/mobheads/grunt.png");
					case NONE -> SkyUnitConfigs.selectedSkyUnitFaction ?
						ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "textures/mobheads/allay.png") :
						ResourceLocation.fromNamespaceAndPath(ReignOfNether.MOD_ID, "textures/mobheads/sheep.png");
				},
				(Keybinding) null,
				() -> false,
				() -> false,
				() -> true,
				() -> {
					switch (faction) {
						case VILLAGERS -> faction = Faction.MONSTERS;
						case MONSTERS -> faction = Faction.PIGLINS;
						case PIGLINS -> {
							faction = Faction.NONE;
							SkyUnitConfigs.selectedSkyUnitFaction = true;
							
						}
						case NONE -> {
							if (SkyUnitConfigs.selectedSkyUnitFaction) {
								SkyUnitConfigs.selectedSkyUnitFaction = false;
							} else {
								faction = Faction.VILLAGERS;
							}
						}
						
						
					}
				},
				() -> {
					switch (faction) {
						case VILLAGERS -> faction = Faction.NONE;
						case MONSTERS -> faction = Faction.VILLAGERS;
						case PIGLINS -> faction = Faction.MONSTERS;
						case NONE -> {
							if (SkyUnitConfigs.selectedSkyUnitFaction) {
								faction = Faction.PIGLINS;
								SkyUnitConfigs.selectedSkyUnitFaction = false;
							} else {
								SkyUnitConfigs.selectedSkyUnitFaction = true;
							}
						}
					}
				},
				List.of(
					fcs(I18n.get("hud.faction.reignofnether.villager"), faction == Faction.VILLAGERS),
					fcs(I18n.get("hud.faction.reignofnether.monster"), faction == Faction.MONSTERS),
					fcs(I18n.get("hud.faction.reignofnether.piglin"), faction == Faction.PIGLINS),
					fcs(I18n.get("hud.faction.skyunit.skyunit"), faction == Faction.NONE && SkyUnitConfigs.selectedSkyUnitFaction),
					fcs(I18n.get("hud.faction.reignofnether.neutral"), faction == Faction.NONE && !SkyUnitConfigs.selectedSkyUnitFaction)
				)
			));
	}
	
}
