package se.miniwa.thief.bot;

import com.google.common.collect.ImmutableList;
import se.miniwa.thief.game.Diamond;

public final class Diamonds {
    public ImmutableList<Diamond> getDiamonds() {
        return ImmutableList.copyOf(StateHolder.getBoard().getDiamonds());
    }
}
