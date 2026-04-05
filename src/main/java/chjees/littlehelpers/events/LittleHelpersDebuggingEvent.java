package chjees.littlehelpers.events;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;

/**
 * For reminding me that my debug command exist.
 */
public class LittleHelpersDebuggingEvent {
    @SuppressWarnings("removal")
    public static void onPlayerReady(PlayerReadyEvent event) {
        Player player = event.getPlayer();
        player.getPlayerRef().sendMessage(Message.raw("Type \"/lhfairy\" to debug fairies."));
    }
}