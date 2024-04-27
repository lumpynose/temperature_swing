package com.objecteffects.temperature.prom;

import java.util.List;

import com.google.gson.JsonPrimitive;

public class PromDataVector extends PromResponse {
    private PromResponseData data;

    public PromResponseData getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return "PromDataMatrix: <<data=" + this.data + ">>";
    }

    static class PromResponseData {
        private String resultType;
        private List<PromSensor> result;

        public List<PromSensor> getResult() {
            return this.result;
        }

        @Override
        public String toString() {
            return "PromResponseData: <<resultType=" + this.resultType
                    + ", result=" + this.result + ">>";
        }
    }

    static class PromSensor {
        private PromMetric metric;
        private List<JsonPrimitive> value;
        private PromValue pvValue = null;

        public PromMetric getMetric() {
            return this.metric;
        }

        public PromValue getValue() {
            if (this.pvValue == null) {
                float floatVal;

                try {
                    floatVal = Float
                            .parseFloat(this.value.get(1).getAsString());
                }
                catch (@SuppressWarnings("unused") final Exception e) {
                    floatVal = Float.NaN;
                }

                this.pvValue = new PromValue(this.value.get(0).getAsLong(),
                        floatVal);
            }

            return this.pvValue;
        }

        @Override
        public String toString() {
            return "PromSensor: <<metric=" + this.metric + ", value="
                    + this.value + ">>";
        }
    }
}
