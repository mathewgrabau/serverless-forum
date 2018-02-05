package com.serverlessarchitecture.lambda;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;

public abstract class LambdaHandler<I,O> implements RequestStreamHandler {
    final ObjectMapper mapper;

    protected LambdaHandler() {
        mapper = new ObjectMapper();
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        System.out.println("Inside handleRequest");
        I inputObject = deserializeEventJson(input, getInputType());
        O handlerResult = handleRequest(inputObject, context);
        serializeOutput(output, handlerResult);
    }

    public abstract O handleRequest(I input, Context context);

    @SuppressWarnings("unchecked")
    private Class<I> getInputType() {
        return (Class<I>) ((ParameterizedType)
            getClass().getGenericSuperclass()).getActualTypeArguments()[0]; 
    }

    private I deserializeEventJson(InputStream inputStream, Class<I> classType) throws IOException {
        return mapper.readerFor(classType).readValue(inputStream);
    }

    private void serializeOutput(OutputStream outputStream, O output) throws IOException {
        mapper.writer().writeValue(outputStream, output);
    }

}