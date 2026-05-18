package ru.practicum.ewm.stats.avro;

import java.util.Arrays;

public enum ActionWeight {
    VIEW(ActionTypeAvro.VIEW, 0.4),
    REGISTER(ActionTypeAvro.REGISTER, 0.8),
    LIKE(ActionTypeAvro.LIKE, 1.0);

    private final ActionTypeAvro actionType;
    private final double weight;

    ActionWeight(ActionTypeAvro actionType, double weight) {
        this.actionType = actionType;
        this.weight = weight;
    }

    public ActionTypeAvro getActionType() {
        return actionType;
    }

    public double getWeight() {
        return weight;
    }

    public static double getWeight(ActionTypeAvro actionType) {
        return Arrays.stream(values())
                .filter(value -> value.actionType == actionType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown action type: " + actionType))
                .weight;
    }
}
