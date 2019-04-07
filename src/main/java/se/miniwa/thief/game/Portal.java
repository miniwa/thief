package se.miniwa.thief.game;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Portal {
    public static Portal create(Position firstExit, Position secondExit) {
        return new AutoValue_Portal(firstExit, secondExit);
    }

    public Position getExitClosestTo(Positionable positionable) {
        if(positionable.distanceTo(getFirstExit()) <= positionable.distanceTo(getSecondExit())) {
            return getFirstExit();
        } else {
            return getSecondExit();
        }
    }

    public abstract Position getFirstExit();
    public abstract Position getSecondExit();
}
