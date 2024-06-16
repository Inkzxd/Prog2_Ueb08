package de.htwsaar.esch.Codeopolis.DomainModel.Utilities;

import java.text.DecimalFormat;

import de.htwsaar.esch.Codeopolis.DomainModel.Silo;

public class DepotVisualizer {

    private StringBuilder builder = new StringBuilder();
    private DecimalFormat df = new DecimalFormat("0.00");

    private int index = 0;

    // adding information to a silo
    public void appendSiloInfo(Silo silo) {

        builder.append("Silo ").append(index + 1).append(": ");

        String grainName = (silo.getGrainType() != null) ? silo.getGrainType().toString() : "EMPTY";
        builder.append(grainName).append("\n");

        int fillLevel = silo.getFillLevel();
        int capacity = silo.getCapacity();

        double fillPercentage = (double) fillLevel / capacity * 100;
        int fillBarLength = 20;

        int filledBars = (int) (fillPercentage / 100 * fillBarLength);
        int emptyBars = fillBarLength - filledBars;

        builder.append("Amount of Grain: ").append(fillLevel).append(" units\n");
        builder.append("|");

        for (int j = 0; j < filledBars; j++) {
            builder.append("=");
        }

        for (int j = 0; j < emptyBars; j++) {
            builder.append("-");
        }

        builder.append("| ").append(df.format(fillPercentage)).append("% filled\n");
        builder.append("Capacity: ").append(capacity).append(" units\n\n");

        index++;
    }

    public String visualize() {
        return builder.toString();
    }
}