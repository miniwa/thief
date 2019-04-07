package se.miniwa.thief;

import com.google.common.util.concurrent.Uninterruptibles;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.miniwa.thief.bot.Bot;
import se.miniwa.thief.bot.PlayerBot;
import se.miniwa.thief.game.Board;
import se.miniwa.thief.game.client.BoardFullException;
import se.miniwa.thief.game.client.DiamondClient;
import se.miniwa.thief.game.client.DiamondException;
import se.miniwa.thief.game.client.InvalidBotException;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class App {
    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        HttpUrl testUrl = HttpUrl.parse("http://localhost:82/api");
        DiamondClient client = new DiamondClient(new OkHttpClient());
        String name = "miniwa";
        String email = "blarch3030@gmail.com";
        String prodToken = "cfe9692a-ea65-496f-9f20-5bdf8cd4d743";
        String testToken = "90bac52d-75c1-435a-a2dc-90db4d99e1d8";

        PlayerBot playerBot = new PlayerBot(client, name, email, prodToken);
        Bot bot = new Bot(playerBot);
        try {
            while(true) {
                bot.tick(Instant.now());
                Uninterruptibles.sleepUninterruptibly(5, TimeUnit.MILLISECONDS);
            }
        } catch(Exception ex) {
            logger.error("Exception in main thread.", ex);
            System.exit(1);
        }
    }
}
