package se.miniwa.diamond;

import se.miniwa.bot.BotApi;
import se.miniwa.bot.MoveApi;
import se.miniwa.diamond.client.DiamondClient;

import java.time.Instant;

public class Bot {
    private MoveApi moveApi;
    private BotApi botApi;

    public void tick() {
        Instant now = Instant.now();
        boolean canMove = now.isAfter(self.getNextMoveAvailableAt());
        if(canMove) {

        }
    }

    public move(String direction) {

    }
}
