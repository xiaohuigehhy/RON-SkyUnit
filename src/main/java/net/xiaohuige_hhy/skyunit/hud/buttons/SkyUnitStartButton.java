package net.xiaohuige_hhy.skyunit.hud.buttons;

import static com.solegendary.reignofnether.startpos.StartPosClientEvents.getPos;
import static com.solegendary.reignofnether.startpos.StartPosClientEvents.isEnabled;
import static com.solegendary.reignofnether.startpos.StartPosClientEvents.isSelectedPosReservedByOther;
import static com.solegendary.reignofnether.startpos.StartPosClientEvents.isStarting;
import static com.solegendary.reignofnether.util.MiscUtil.fcs;

import com.solegendary.reignofnether.building.Building;
import com.solegendary.reignofnether.building.Buildings;
import com.solegendary.reignofnether.cursor.CursorClientEvents;
import com.solegendary.reignofnether.faction.Faction;
import com.solegendary.reignofnether.faction.FactionRegister;
import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.keybinds.Keybindings;
import com.solegendary.reignofnether.player.PlayerClientEvents;
import com.solegendary.reignofnether.startpos.StartPos;
import com.solegendary.reignofnether.startpos.StartPosServerboundPacket;
import com.solegendary.reignofnether.tutorial.TutorialClientEvents;
import com.solegendary.reignofnether.tutorial.TutorialStage;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.util.LanguageUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.xiaohuige_hhy.skyunit.SkyUnit;
import net.xiaohuige_hhy.skyunit.building.SkyUnitBuildings;

import java.util.List;

public class SkyUnitStartButton {
	public static final int ICON_SIZE = 14;
	
	public static final FactionRegister SKYUNIT =new FactionRegister();
	
	public static void register(Building building) {
		SKYUNIT.registerBuilding(building);
	}
	
	public static void register(Building building, Keybinding keybinding) {
		SKYUNIT.registerBuilding(building, keybinding);
	}
	
	public static void register() {
		register(SkyUnitBuildings.SKY_TOWN, Keybindings.keyQ);
		register(SkyUnitBuildings.BEE_HIVE, Keybindings.keyW);
		register(Buildings.BEACON);
	}
	
	public static boolean selectedSkyUnitFaction = false;
	
	public static final Button skyUnitStartButton = new Button(
		"SkyUnit",
		ICON_SIZE,
		ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "textures/mobheads/allay.png"),
		(Keybinding) null,
		() -> CursorClientEvents.getLeftClickAction() == UnitAction.STARTRTS_VILLAGERS,
		() -> !TutorialClientEvents.isAtOrPastStage(TutorialStage.PLACE_WORKERS_B) || !PlayerClientEvents.canStartRTS,
		() -> true,
		() -> {
			CursorClientEvents.setLeftClickAction(UnitAction.STARTRTS_VILLAGERS);
			selectedSkyUnitFaction = true;
		},
		null,
		List.of(
			fcs(LanguageUtil.getTranslation("hud.startbuttons.skyunit.reignofnether.first"), true),
			fcs(LanguageUtil.getTranslation("hud.startbuttons.skyunit.reignofnether.second"))
		)
	);
	
	private static final Minecraft MC = Minecraft.getInstance();
	
	public static Button skyUnitReadyButton = new Button(
		"SkyUnit",
		14,
		ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "textures/mobheads/allay.png"),
		(Keybinding) null,
		() -> selectedSkyUnitFaction,
		() -> TutorialClientEvents.isEnabled() || !PlayerClientEvents.canStartRTS || !isEnabled(),
		() -> !isSelectedPosReservedByOther() && getPos() != null && !isStarting,
		() -> {
			StartPos startPos = getPos();
			if (startPos != null && MC.player != null) {
				if (!selectedSkyUnitFaction) {
					selectedSkyUnitFaction = true;
					StartPosServerboundPacket.reservePos(startPos.pos, Faction.VILLAGERS, MC.player.getName().getString());
				} else {
					selectedSkyUnitFaction = false;
					StartPosServerboundPacket.unreservePos(getPos().pos);
				}
			}
		},
		null,
		List.of(
			fcs(I18n.get("hud.readybuttons.villagers.reignofnether.first_startpos"), true),
			fcs(I18n.get("hud.startbuttons.villagers.reignofnether.second_startpos"))
		)
	);
	
}
