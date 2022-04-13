package it.polimi.ingsw.common;

import it.polimi.ingsw.common.requests.Request;
import it.polimi.ingsw.common.responses.Response;

/**
 * This interface models a generic Parser used for encoding and decoding Request(s) and Response(s)
 */
public interface Parser {

    Request decodeRequest(String encodedString);

    String encodeRequest(Request request);

    Response decodeResponse(String encodedString);

    String encodeResponse(Response response);
}
