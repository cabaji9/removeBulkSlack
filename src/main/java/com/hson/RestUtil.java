package com.hson;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hyun Woo Son on 11/20/17.
 */
public class RestUtil {


    public static WebClient getWebClient(String path,String endPointAddress){
        List<Object> providers = new ArrayList<>();
        providers.add(new JacksonJaxbJsonProvider());
        WebClient client = WebClient.create(endPointAddress, providers);
        client.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
        client.path(path);
        return client;
    }


    public static <T> T obtainEntity(Response response , Class<T> valueType) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue((InputStream) response.getEntity(),valueType);
    }



}
