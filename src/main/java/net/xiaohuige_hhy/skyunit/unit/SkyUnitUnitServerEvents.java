package net.xiaohuige_hhy.skyunit.unit;

import static com.solegendary.reignofnether.player.PlayerServerEvents.isRTSPlayer;

import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.util.MiscUtil;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import org.joml.Vector3d;

import java.util.List;

public class SkyUnitUnitServerEvents {
	
	@SubscribeEvent
	// assign unit owner when spawned with an egg based on whoever is closest
	public static void onMobSpawn(MobSpawnEvent.FinalizeSpawn evt) {
		if (!evt.getSpawnType().equals(MobSpawnType.SPAWN_EGG)) {
			return;
		}
		
		Entity entity = evt.getEntity();
		if (evt.getEntity() instanceof Unit) {
			
			Vec3 pos = entity.position();
			List<Player> nearbyPlayers = MiscUtil.getEntitiesWithinRange(new Vector3d(pos.x, pos.y, pos.z),
				10,
				Player.class,
				evt.getEntity().level()
			);
			
			float closestPlayerDist = 10;
			Player closestPlayer = null;
			for (Player player : nearbyPlayers) {
				if (player.distanceTo(entity) < closestPlayerDist && isRTSPlayer(player.getName().getString())) {
					closestPlayerDist = player.distanceTo(entity);
					closestPlayer = player;
				}
			}
			if (closestPlayer != null) {
				((Unit) entity).setOwnerName(closestPlayer.getName().getString());
			}
		}
	}
	
}
