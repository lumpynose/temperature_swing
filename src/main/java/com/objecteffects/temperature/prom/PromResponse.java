package com.objecteffects.temperature.prom;

import java.util.Arrays;
import java.util.Objects;

public abstract class PromResponse {
    private String status;
    private String errorType;
    private String error;
    private String[] warnings;

    public String getStatus() {
        return this.status;
    }

    public String getErrorType() {
        return this.errorType;
    }

    public String getError() {
        return this.error;
    }

    public String[] getWarnings() {
        return this.warnings;
    }

    static public class PromMetric {
        private String job;
        private String sensor;

        public String getJob() {
            return this.job;
        }

        public String getSensor() {
            return this.sensor;
        }

        @Override
        public String toString() {
            return "PromMetric <<job=" + this.job + ", sensor=" + this.sensor
                    + ">>";
        }
    }

    static public class PromValue implements Comparable<PromValue> {
        long timestamp;
        float value;

        public long getTimestamp() {
            return this.timestamp;
        }

        public float getValue() {
            return this.value;
        }

        PromValue(final long _timestamp, final float _value) {
            this.timestamp = _timestamp;
            this.value = _value;
        }

        @Override
        public int compareTo(final PromValue o) {
            return Float.compare(this.value, o.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(Long.valueOf(this.timestamp),
                    Float.valueOf(this.value));
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj)
                return true;

            if (obj == null)
                return false;

            if (getClass() != obj.getClass())
                return false;

            final PromValue other = (PromValue) obj;

            return this.timestamp == other.timestamp
                    && Float.floatToIntBits(this.value) == Float
                            .floatToIntBits(other.value);
        }

        @Override
        public String toString() {
            return "PromValue <<timestamp=" + this.timestamp + ", value="
                    + this.value + ">>";
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + Arrays.hashCode(this.warnings);

        result = prime * result
                + Objects.hash(this.error, this.errorType, this.status);

        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        final PromResponse other = (PromResponse) obj;

        return Objects.equals(this.error, other.error)
                && Objects.equals(this.errorType, other.errorType)
                && Objects.equals(this.status, other.status)
                && Arrays.equals(this.warnings, other.warnings);
    }

    @Override
    public String toString() {
        return "PromResponse [status=" + this.status + ", errorType="
                + this.errorType + ", error=" + this.error + ", warnings="
                + Arrays.toString(this.warnings) + "]";
    }
}
