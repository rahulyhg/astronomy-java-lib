package net.arwix.astronomy.events;


import net.arwix.astronomy.GeocentricEquatorialCoordinates;

import java.util.Calendar;

/**
 * The type Rise set event.
 */
public class RiseSetEvent extends AbstractEvent {

    private ObjectType objectType;

    /**
     * Instantiates a new Rise set event.
     *
     * @param type        the type
     * @param coordinates the coordinates
     */
    public RiseSetEvent(ObjectType type, GeocentricEquatorialCoordinates coordinates) {
        super(coordinates);
        this.objectType = type;
    }

    /**
     * Gets rise time.
     *
     * @return the rise time
     */
    public Calendar getRiseTime() {
        if (!super.isValid) {
            calc();
        }
        return super.riseDate;
    }

    /**
     * Gets set time.
     *
     * @return the set time
     */
    public Calendar getSetTime() {
        if (!super.isValid) {
            calc();
        }
        return super.downDate;
    }

    /**
     * Gets above.
     *
     * @return the above
     */
    public boolean getAbove() {
        if (!super.isValid) {
            calc();
        }
        return super.above;
    }


    private void calc() {
        super.callCircleOfLatitudeRiseSet(objectType.getSinRefractionAngle());
    }


}
