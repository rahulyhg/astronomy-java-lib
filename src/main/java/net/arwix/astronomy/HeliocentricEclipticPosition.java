package net.arwix.astronomy;

import net.arwix.astronomy.coordinates.Vector;

/**
 * Вычисляет гелеоцентрические (в центре Солнце) эклиптические координаты
 * Computes ecliptical position using analytical series
 */
public interface HeliocentricEclipticPosition {

    /**
     * Вычисляет гелеоцентрические эклиптические координаты
     *
     * @param T Юлианские столетия (ET) Time in Julian centuries since J2000
     * @param epoch эпоха
     * @return Гелиоцентрическая позиция в а.е. относительно эклиптики и равноденствия даты или J2000 зависит от конкретного исполнения
     */
    public Vector getHeliocentricEclipticPosition(double T, Epoch epoch);
}