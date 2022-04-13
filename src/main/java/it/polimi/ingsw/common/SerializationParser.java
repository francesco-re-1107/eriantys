package it.polimi.ingsw.common;

import it.polimi.ingsw.common.requests.Request;
import it.polimi.ingsw.common.responses.Response;

import java.io.*;
import java.util.Base64;

/**
 * This parser implements communication using java default serialization
 */
public class SerializationParser implements Parser {

    @Override
    public Request decodeRequest(String encodedString) throws IOException, ClassNotFoundException {
        return (Request) decode(encodedString);
    }

    @Override
    public String encodeRequest(Request request) throws IOException {
        return encode(request);
    }

    @Override
    public Response decodeResponse(String encodedString) throws IOException, ClassNotFoundException {
        return (Response) decode(encodedString);
    }

    @Override
    public String encodeResponse(Response response) throws IOException {
        return encode(response);
    }

    private String encode(Object o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();

        return Base64
                .getEncoder()
                .encodeToString(baos.toByteArray());
    }

    private Object decode(String s) throws IOException, ClassNotFoundException {
        byte [] data = Base64
                .getDecoder()
                .decode(s);

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();

        return o;
    }
}
