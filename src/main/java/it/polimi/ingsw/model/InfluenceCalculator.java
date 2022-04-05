package it.polimi.ingsw.model;

import java.util.Map;

/**
 * Represents the common interface for all calculators used for calculating influence
 */
public interface InfluenceCalculator {

    /**
     * Calculate influence
     * @param player for which calculate influence
     * @param island on which calculate influence
     * @param professors the current professors
     * @return the calculated influence
     */
    int calculateInfluence(Player player, Island island, Map<Student, Player>  professors);

}
