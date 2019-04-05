package se.miniwa.thief.game;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Position {
    public static Position create(int x, int y) {
        return new AutoValue_Position(x, y);
    }

    public abstract int getX();
    public abstract int getY();
}
