package se.miniwa.thief.bot;

import se.miniwa.thief.game.client.DiamondClient;

import java.io.IOException;

public class BotInstance {
    private DiamondClient client;
    private String name;
    private String email;
    private String token;

    public void register() {
        if(token != null) {
            throw new IllegalStateException("Bot has already received a token.");
        }

        try {
            client.registerBot(name, email);
        } catch(IOException ex) {
            // LOL
        }
    }

    public v
}
