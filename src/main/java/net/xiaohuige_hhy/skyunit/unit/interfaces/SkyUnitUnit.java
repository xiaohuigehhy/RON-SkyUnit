package net.xiaohuige_hhy.skyunit.unit.interfaces;

import com.solegendary.reignofnether.faction.Faction;
import com.solegendary.reignofnether.unit.interfaces.Unit;

public interface SkyUnitUnit extends Unit {
	
	@Override
	default Faction getFaction() {
		return Faction.NONE;
	}
}
