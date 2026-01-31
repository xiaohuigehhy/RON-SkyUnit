//package net.xiaohuige_hhy.skyunit.hud;
//
//import static com.solegendary.reignofnether.util.MiscUtil.fcs;
//
//import com.solegendary.reignofnether.ReignOfNether;
//import com.solegendary.reignofnether.cursor.CursorClientEvents;
//import com.solegendary.reignofnether.gamemode.ClientGameModeHelper;
//import com.solegendary.reignofnether.gamemode.GameMode;
//import com.solegendary.reignofnether.guiscreen.TopdownGui;
//import com.solegendary.reignofnether.hud.Button;
//import com.solegendary.reignofnether.hud.HudClientEvents;
//import com.solegendary.reignofnether.hud.buttons.StartButtons;
//import com.solegendary.reignofnether.keybinds.Keybinding;
//import com.solegendary.reignofnether.orthoview.OrthoviewClientEvents;
//import com.solegendary.reignofnether.player.PlayerClientEvents;
//import com.solegendary.reignofnether.startpos.StartPosClientEvents;
//import com.solegendary.reignofnether.tutorial.TutorialClientEvents;
//import com.solegendary.reignofnether.tutorial.TutorialStage;
//import com.solegendary.reignofnether.unit.UnitAction;
//import com.solegendary.reignofnether.util.LanguageUtil;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraftforge.client.event.ScreenEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//
//import java.util.List;
//
//public class SkyUnitHudClientEvents {
//
//	public static final int ICON_SIZE = 14;
//	public static final Button skyUnitStartButton = new Button(
//		"SkyUnit",
//		ICON_SIZE,
//		ResourceLocation.fromNamespaceAndPath(ReignOfNether.MOD_ID, "textures/mobheads/villager.png"),
//		(Keybinding) null,
//		() -> CursorClientEvents.getLeftClickAction() == UnitAction.SPIN_WEBS,
//		() -> !TutorialClientEvents.isAtOrPastStage(TutorialStage.PLACE_WORKERS_B) || !PlayerClientEvents.canStartRTS,
//		() -> true,
//		() -> CursorClientEvents.setLeftClickAction(UnitAction.NONE),
//		null,
//		List.of(
//			fcs(LanguageUtil.getTranslation("hud.startbuttons.skyunit.reignofnether.first"), true),
//			fcs(LanguageUtil.getTranslation("hud.startbuttons.skyunit.reignofnether.second"))
//		)
//	);
//	private static final Minecraft MC = Minecraft.getInstance();
//	public static int mouseX = 0;
//	public static int mouseY = 0;
//
//	@SubscribeEvent
//	public static void onDrawScreen(ScreenEvent.Render.Post evt) {
//
//		if (!OrthoviewClientEvents.isEnabled() || !(evt.getScreen() instanceof TopdownGui)) {
//			return;
//		}
//		if (MC.level == null) {
//			return;
//		}
//
//		mouseX = evt.getMouseX();
//		mouseY = evt.getMouseY();
//
//		int screenWidth = MC.getWindow().getGuiScaledWidth();
//		int screenHeight = MC.getWindow().getGuiScaledHeight();
//
//		if (!PlayerClientEvents.isRTSPlayer() && !PlayerClientEvents.rtsLocked) {
//			if (ClientGameModeHelper.gameMode != GameMode.SANDBOX) {
//
//				if (!StartPosClientEvents.isEnabled()) {
//					if (!skyUnitStartButton.isHidden.get()) {
//						skyUnitStartButton.render(evt.getGuiGraphics(),
//							screenWidth - (StartButtons.ICON_SIZE * 6),
//							StartButtons.ICON_SIZE / 2,
//							mouseX,
//							mouseY
//						);
//						IHudClientEvents hudClientEvents = (IHudClientEvents) new HudClientEvents();
//						hudClientEvents.skyUnit$addRendererButtons(skyUnitStartButton);
//					}
//				}
//			}
//		}
//
//	}
//}
