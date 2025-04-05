package me.zombie_striker.verticality.util;

import me.zombie_striker.verticality.Verticality;

public class FastPowUtil {
    private final double[] precompute;
    private final int precision;
    private final int powerprecision;
    private final float minpower;
    private final float maxpower;
    private final float minunit;
    private final float maxunit;
    private final boolean samePower;
    private final boolean sameunit;

    public FastPowUtil(int precision, int powerprecision, float minunit, float maxunit, float minpower, float maxpower) {
        this.precision = precision;
        this.powerprecision = powerprecision;
        this.minpower = minpower;
        this.maxpower = maxpower;
        this.minunit = minunit;
        this.maxunit = maxunit;
        this.samePower = minpower == maxpower;
        this.sameunit = minunit == maxunit;
        if (samePower) {
            precompute = new double[(int) (precision * (maxunit - minunit))];
        } else if (sameunit) {
            precompute = new double[(int) (powerprecision * (maxpower - minpower))];
        } else {
            precompute = new double[(int) (precision * powerprecision * (maxpower - minpower) * (maxunit - minunit))];
        }

        double increment = (maxpower - minpower) / powerprecision;
        double unitincrement = (maxunit - minunit) / precision;

        if (samePower) {
            int place = 0;
            for (double unit = minunit; unit < maxunit; unit += unitincrement) {
                this.precompute[place] = Math.pow(unit, minpower);
                place++;
            }
        } else if (sameunit) {
            int place = 0;
            for (double power = minpower; power < maxpower; power += increment) {
                this.precompute[place] = Math.pow(minunit, power);
                place++;
            }
        } else {
            int place = 0;
            for (double power = minpower; power < maxpower; power += increment) {
                for (double unit = minunit; unit < maxunit; unit += unitincrement) {
                    this.precompute[place] = Math.pow(unit, power);
                    place++;
                }
            }

        }
    }

    /**
     * Returns the result of unit^power,  where unit is between [minunit] and [maxunit], and power is between [minpower] and [maxpower]
     *
     * @param unit  the base number
     * @param power the  exponent
     * @return output of unit raised to power exponent
     */
    public double getOutput(double unit, double power) {
        if ((unit < minunit || unit > maxunit) && !sameunit) {
            Verticality.log("FastPowerUtil encountered error: unit=" + unit + ", minunit=" + minunit + ", maxunitt=" + maxunit + ".");
            return 0;
        }
        if ((power < minpower || power > maxpower) && !samePower) {
            Verticality.log("FastPowerUtil encountered error: power=" + power + ", minpower=" + minpower + ", maxpower=" + maxpower + ".");
            return 0;
        }
        if (samePower) {
            int index = (int) ((((unit - minunit) / (maxunit - minunit)) * precision));
            if (index < 0 || index >= this.precompute.length) {
                Verticality.log("FastPowerUtil encountered error: unit=" + unit + ", power=" + power + ", index=" + index + ", length=" + this.precompute.length + ".");
                return 0;
            }
            return this.precompute[index];
        } else if (sameunit) {
            int index = (int) ((((power - minpower) / (maxpower - minpower)) * powerprecision));
            if (index < 0 || index >= this.precompute.length) {
                Verticality.log("FastPowerUtil encountered error: unit=" + unit + ", power=" + power + ", index=" + index + ", length=" + this.precompute.length + ".");
                return 0;
            }
            return this.precompute[index];

        } else {
            int index = (int) ((((int) (power - minpower) * powerprecision / (maxpower - minpower)) * (precision)) + (int) ((((unit - minunit) / (maxunit - minunit)) * precision)));
            if (index < 0 || index >= this.precompute.length) {
                Verticality.log("FastPowerUtil encountered error: unit=" + unit + ", power=" + power + ", index=" + index + ", length=" + this.precompute.length + ".");
                return 0;
            }
            return this.precompute[index];
        }
    }
}
