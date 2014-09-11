package net.arwix.astronomy;

import net.arwix.astronomy.coordinates.Vector;

/**
 * Вычисляет эклиптические координаты
 * Computes ecliptical position using analytical series
 * Geliocentric position (in [AU])
 */
public interface HelioEclipticPosition {

    /**
     * Вычисляет эклиптические координаты
     *
     * @param T Юлианские столетия (ET) Time in Julian centuries since J2000
     * @return Гелиоцентрическая позиция в а.е. относительно эклиптики и равноденствия даты или J2000 зависит от конкретного исполнения
     */
    public Vector getEclipticCoordinates(double T);
}