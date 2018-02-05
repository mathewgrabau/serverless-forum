
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.amazonaws.services.lambda.runtime.Context;


import com.serverlessarchitecture.lambda.LambdaHandler;

import org.junit.Test;
import static org.junit.Assert.*;

public class LambdaHandlerTest {
    protected static class TestInput {
        public String value;
    }

    protected static class TestOutput {
        public String value;
    }

    protected static class TestLambdaHandler extends LambdaHandler<TestInput, TestOutput> {
        @Override
        public TestOutput handleRequest(TestInput input, Context context) {
            TestOutput testOutput = new TestOutput();
            testOutput.value = input.value;
            return testOutput;
        }
    }

    @Test
    public void handleRequest() throws Exception {
        String jsonInputAndOutputExpected = "{\"value\":\"testValue\"}";
        InputStream testInputStream = new ByteArrayInputStream(jsonInputAndOutputExpected.getBytes(StandardCharsets.UTF_8));
        OutputStream testOutputStream = new OutputStream() {
            final StringBuilder stringBuilder = new StringBuilder();

            @Override
            public void write(int b) {
                stringBuilder.append((char)b);
            }

            @Override
            public String toString() {
                return stringBuilder.toString();
            }
        };

        TestLambdaHandler lambdaHandler = new TestLambdaHandler();
        lambdaHandler.handleRequest(testInputStream, testOutputStream, null);
        assertEquals(jsonInputAndOutputExpected, testOutputStream.toString());
    }
}