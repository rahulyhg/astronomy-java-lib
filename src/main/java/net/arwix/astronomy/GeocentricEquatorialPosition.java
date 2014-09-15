package net.arwix.astronomy;

import net.arwix.astronomy.coordinates.Vector;

/**
 * Вычисляет геоцентрические (в центре Земля) экваториальные координаты
 */
public interface GeocentricEquatorialPosition {


    /**
     * Вычисляет геоцентрические (в центре Земля) экваториальные координаты
     *
     * @param T     Юлианские столетия (ET) Time in Julian centuries since J2000
     * @param epoch эпоха
     * @return Гелиоцентрическая позиция в а.е. относительно эклиптики и равноденствия даты или J2000 зависит от конкретного исполнения
     */
    public Vector getGeocentricEquatorialPosition(double T, Epoch epoch);

}
