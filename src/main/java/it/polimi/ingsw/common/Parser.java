package it.polimi.ingsw.common;

import it.polimi.ingsw.common.requests.Request;
import it.polimi.ingsw.common.responses.Response;

import java.io.IOException;

/**
 * This interface models a generic Parser used for encoding and decoding Request(s) and Response(s)
 */
public interface Parser {

    Request decodeRequest(String encodedString) throws IOException, ClassNotFoundException;

    String encodeRequest(Request request) throws IOException;

    Response decodeResponse(String encodedString) throws IOException, ClassNotFoundException;

    String encodeResponse(Response response) throws IOException;
}
