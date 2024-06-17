package de.htwsaar.esch.Codeopolis.Util;

import java.text.DecimalFormat;

import de.htwsaar.esch.Codeopolis.DomainModel.Silo;

public class DepotVisualizer {
    private StringBuilder builder = new StringBuilder();
    private DecimalFormat df = new DecimalFormat("0.00");

    private int i=0;

    public String visualize() {
        return builder.toString();
    }

    public void appendSiloInfo(Silo silo) {
        builder.append("Silo ").append(i + 1).append(": ");

        String grainName = (silo.getGrainType() != null) ? silo.getGrainType().toString() : "EMPTY";
        builder.append(grainName).append("\n");

        int fillLevel = silo.getFillLevel();
        int capacity = silo.getCapacity();
        double fillPercentage = (double) fillLevel / capacity * 100;
        //double emptyPercentage = 100 - fillPercentage;  Unused Variable

        // Absolute amount of grain
        builder.append("Amount of Grain: ").append(fillLevel).append(" units\n");

        // ASCII-ART representation of the fill level
        int fillBarLength = 20;
        int filledBars = (int) (fillPercentage / 100 * fillBarLength);
        int emptyBars = fillBarLength - filledBars;

        builder.append("|");
        for (int j = 0; j < filledBars; j++) {
            builder.append("=");
        }
        for (int j = 0; j < emptyBars; j++) {
            builder.append("-");
        }
        builder.append("| ").append(df.format(fillPercentage)).append("% filled\n");
        builder.append("Capacity: ").append(capacity).append(" units\n\n");
        i++;
    }
}
