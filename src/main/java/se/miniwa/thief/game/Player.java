package se.miniwa.thief.game;

import com.google.auto.value.AutoValue;

import java.time.Instant;

@AutoValue
public abstract class Player extends AbstractPositionable {
    public static final int MAX_DIAMONDS = 5;
    public static Builder builder() {
        return new AutoValue_Player.Builder();
    }

    public boolean canPickup(Diamond diamond) {
        return getDiamondCount() + diamond.getSize() <= MAX_DIAMONDS;
    }

    public boolean isInventoryFull() {
        return getDiamondCount() == MAX_DIAMONDS;
    }

    public boolean isMoveAvailable(Instant now) {
        return now.isAfter(getNextMoveAvailableAt());
    }

    public abstract String getName();
    public abstract String getId();

    @Override
    public abstract Position getPosition();

    public abstract Position getBase();
    public abstract int getDiamondCount();
    public abstract int getScore();
    public abstract Instant getNextMoveAvailableAt();
    public abstract Instant getRoundOverAt();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setName(String name);
        public abstract Builder setId(String id);
        public abstract Builder setPosition(Position position);
        public abstract Builder setBase(Position base);
        public abstract Builder setDiamondCount(int diamondCount);
        public abstract Builder setScore(int score);
        public abstract Builder setNextMoveAvailableAt(Instant nextMoveAvailableAt);
        public abstract Builder setRoundOverAt(Instant roundOverAt);
        public abstract Player build();
    }
}
