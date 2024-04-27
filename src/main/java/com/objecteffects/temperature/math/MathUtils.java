package com.objecteffects.temperature.math;

/*
 * https://stackoverflow.com/questions/5731863/mapping-a-numeric-range-onto-another
 */
public class MathUtils {
    @SuppressWarnings("boxing")
    public static int mapRange(final int sourceNumber, final int fromA,
            final int fromB, final int toA, final int toB) {
        final double deltaA = fromB - fromA;
        final double deltaB = toB - toA;
        final double scale = deltaB / deltaA;
        final double negA = -1 * fromA;
        final double offset = negA * scale + toA;
        final double finalNumber = sourceNumber * scale + offset;
        final int calcScale = (int) Math.pow(10, 0);

        return (int) Math.round(finalNumber * calcScale) / calcScale;
    }
}
