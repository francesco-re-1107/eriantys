package it.polimi.ingsw.common;

import it.polimi.ingsw.common.requests.Request;
import it.polimi.ingsw.common.responses.Response;

public interface Parser {

    Request parseRequest(String encodedString);

    String encodeRequest(Request request);

    Response parseResponse(String encodedString);

    String encodeResponse(Response response);
}
