package it.polimi.ingsw.model;

import java.util.Map;

public interface InfluenceCalculator {

    int calculateInfluence(Player player, Island island, Map<Student, Player>  professors);

}
