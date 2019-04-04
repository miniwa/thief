package se.miniwa;

import okhttp3.OkHttpClient;
import se.miniwa.diamond.Board;
import se.miniwa.diamond.client.BoardFullException;
import se.miniwa.diamond.client.DiamondClient;
import se.miniwa.diamond.client.DiamondException;
import se.miniwa.diamond.client.InvalidBotException;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        DiamondClient client = new DiamondClient(new OkHttpClient());
        String name = "miniwa";
        String email = "blarch3030@gmail.com";
        String token = "cfe9692a-ea65-496f-9f20-5bdf8cd4d743";

        try {
            //String token = client.registerBot(name, email);
            //System.out.println("Token: " + token);

            Board board = client.getBoards().get(0);
            try {
                client.joinBoard(board.getId(), token);
            } catch(BoardFullException ex) {
                // Ignore
            }

            for(int i = 0; i < 10; i++) {
                try {
                    client.moveBot(board.getId(), token, "North");
                } catch(InvalidBotException ex) {
                    // Ignore
                }
            }
        } catch (IOException|DiamondException ex) {
            ex.printStackTrace();
        }
    }
}
