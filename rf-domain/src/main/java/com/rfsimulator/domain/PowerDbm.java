package com.rfsimulator.domain;

public record PowerDbm(double dbm) {

    public PowerDbm {
        if (!Double.isFinite(dbm)) {
            throw new IllegalArgumentException("dBm must be finite");
        }

        if (dbm == 0.0) {
            dbm = 0.0;
        }
    }

    public PowerWatts toWatts() {
        double watts = 0.001 * Math.pow(10.0, dbm / 10.0);
        if (!Double.isFinite(watts)) {
            throw new IllegalStateException("dBm value cannot be converted to finite watts");
        }
        return new PowerWatts(watts);
    }
}
