package se.miniwa.thief.bot;

import com.google.common.collect.ImmutableList;
import se.miniwa.thief.game.Player;

public final class Players {
    public static ImmutableList<Player> getPlayers() {
        return ImmutableList.copyOf(StateHolder.getBoard().getPlayers());
    }
}
