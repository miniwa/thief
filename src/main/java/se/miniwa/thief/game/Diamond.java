package se.miniwa.thief.game;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Diamond {
    public static Diamond create(int size, Position position) {
        return new AutoValue_Diamond(size, position);
    }

    public abstract int getSize();
    public abstract Position getPosition();
}
