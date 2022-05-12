package it.polimi.ingsw.common.requests;

import it.polimi.ingsw.server.model.StudentsContainer;

/**
 * This class represents the request to select a cloud in the currently played game
 */
public class SelectCloudRequest extends GameRequest{

    private final StudentsContainer cloud;

    public SelectCloudRequest(StudentsContainer cloud) {
        this.cloud = cloud;
    }

    public StudentsContainer getCloud() {
        return cloud;
    }
}
