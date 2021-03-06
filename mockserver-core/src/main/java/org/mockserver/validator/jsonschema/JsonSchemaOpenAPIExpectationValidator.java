package org.mockserver.validator.jsonschema;

import org.mockserver.logging.MockServerLogger;
import org.mockserver.mock.OpenAPIExpectation;

/**
 * @author jamesdbloom
 */
public class JsonSchemaOpenAPIExpectationValidator extends JsonSchemaValidator {

    private JsonSchemaOpenAPIExpectationValidator(MockServerLogger mockServerLogger) {
        super(
            mockServerLogger,
            OpenAPIExpectation.class,
            "org/mockserver/model/schema/",
            "openAPIExpectation"
        );
    }

    private static JsonSchemaOpenAPIExpectationValidator jsonSchemaExpectationValidator;

    public static JsonSchemaOpenAPIExpectationValidator jsonSchemaOpenAPIExpectationValidator(MockServerLogger mockServerLogger) {
        if (jsonSchemaExpectationValidator == null) {
            jsonSchemaExpectationValidator = new JsonSchemaOpenAPIExpectationValidator(mockServerLogger);
        }
        return jsonSchemaExpectationValidator;
    }
}
