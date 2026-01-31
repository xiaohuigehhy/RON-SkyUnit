//package net.xiaohuige_hhy.skyunit.mixin.cursor;
//
//
//import com.solegendary.reignofnether.cursor.CursorClientEvents;
//
//import org.spongepowered.asm.mixin.Implements;
//import org.spongepowered.asm.mixin.Interface;
//import org.spongepowered.asm.mixin.Intrinsic;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.Unique;
//
//@Mixin(CursorClientEvents.class)
//@Implements(@Interface(iface = SkyUnitCursorClientEvents.class, prefix = "skyUnit$"))
//public abstract class CursorClientEventsMixin {
//
//	@Unique
//	private static boolean skyUnit$isSkyUnitLeftClickFlag = false;
//
//	@Shadow(remap = false)
//	public static native void setLeftClickAction();
//
//	@Override
//	private static void skyUnit$setSkyUnitLeftClickAction() {
//		setLeftClickAction();
//		skyUnit$isSkyUnitLeftClickFlag = true;
//	}
//
//	@Intrinsic
//	public static boolean skyUnit$isSkyUnitLeftClick() {
//		return skyUnit$isSkyUnitLeftClickFlag;
//	}
//
//	@Intrinsic
//	public static void skyUnit$setSkyUnitLeftClick(boolean value) {
//		skyUnit$isSkyUnitLeftClickFlag = value;
//	}
//
//}
