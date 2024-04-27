package com.objecteffects.temperature.prom;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;

public class PromDataMatrix extends PromResponse {
    private PromResponseData data;
//    private static TUnit tunit = Configuration.getTUnit();

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
        private List<JsonArray> values;
        private List<PromValue> pvValues = null;

        public PromMetric getMetric() {
            return this.metric;
        }

        public List<PromValue> getValues() {
            if (this.pvValues == null) {
                this.pvValues = new ArrayList<>();

                for (final JsonArray v : this.values) {
                    float floatVal;

                    try {
                        floatVal = Float.parseFloat(v.get(1).getAsString());
                    }
                    catch (@SuppressWarnings("unused") final Exception e) {
                        floatVal = Float.NaN;
                    }

                    this.pvValues
                            .add(new PromValue(v.get(0).getAsLong(), floatVal));
                }
            }

            return this.pvValues;
        }

        @Override
        public String toString() {
            return "PromSensor: <<metric=" + this.metric + ", value="
                    + this.values + ">>";
        }
    }

}
