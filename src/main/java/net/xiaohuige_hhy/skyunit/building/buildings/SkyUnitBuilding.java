package net.xiaohuige_hhy.skyunit.building.buildings;

import com.solegendary.reignofnether.faction.Faction;

public interface SkyUnitBuilding {
	
	default Faction getFaction() {
		return Faction.NONE;
	}
	
}
