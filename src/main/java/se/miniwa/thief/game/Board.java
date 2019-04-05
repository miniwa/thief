package se.miniwa.thief.game;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import java.time.Duration;
import java.util.List;

@AutoValue
public abstract class Board {
    public static Builder builder() {
        return new AutoValue_Board.Builder();
    }

    public abstract String getId();
    public abstract int getWidth();
    public abstract int getHeight();
    public abstract Duration getMinimumDelayBetweenMoves();
    public abstract ImmutableList<Diamond> getDiamonds();
    public abstract ImmutableList<Player> getPlayers();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setId(String id);
        public abstract Builder setWidth(int width);
        public abstract Builder setHeight(int height);
        public abstract Builder setMinimumDelayBetweenMoves(Duration minimumDelayBetweenMoves);
        public abstract Builder setDiamonds(List<Diamond> diamonds);
        public abstract Builder setPlayers(List<Player> players);
        public abstract Board build();
    }
}
