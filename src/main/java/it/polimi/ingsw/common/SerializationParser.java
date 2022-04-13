package it.polimi.ingsw.common;

import it.polimi.ingsw.common.requests.Request;
import it.polimi.ingsw.common.responses.Response;

/**
 * This parser implements communication using java default serialization
 */
public class SerializationParser implements Parser {

    @Override
    public Request decodeRequest(String encodedString) {
        return null;
    }

    @Override
    public String encodeRequest(Request request) {
        return null;
    }

    @Override
    public Response decodeResponse(String encodedString) {
        return null;
    }

    @Override
    public String encodeResponse(Response response) {
        return null;
    }
}
