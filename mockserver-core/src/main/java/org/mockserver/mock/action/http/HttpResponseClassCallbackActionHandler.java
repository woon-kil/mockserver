package org.mockserver.mock.action.http;

import org.mockserver.log.model.LogEntry;
import org.mockserver.logging.MockServerLogger;
import org.mockserver.mock.action.ExpectationResponseCallback;
import org.mockserver.model.HttpClassCallback;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.slf4j.event.Level;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.mockserver.model.HttpResponse.notFoundResponse;

/**
 * @author jamesdbloom
 */
public class HttpResponseClassCallbackActionHandler {

    private static ClassLoader contextClassLoader = ClassLoader.getSystemClassLoader();

    public static void setContextClassLoader(ClassLoader contextClassLoader) {
        HttpResponseClassCallbackActionHandler.contextClassLoader = contextClassLoader;
    }

    private final MockServerLogger mockServerLogger;

    public HttpResponseClassCallbackActionHandler(MockServerLogger mockServerLogger) {
        this.mockServerLogger = mockServerLogger;
    }

    public HttpResponse handle(HttpClassCallback httpClassCallback, HttpRequest request) {
        return invokeCallbackMethod(httpClassCallback, request);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private ExpectationResponseCallback instantiateCallback(HttpClassCallback httpClassCallback) {
        try {
            Class expectationResponseCallbackClass = contextClassLoader.loadClass(httpClassCallback.getCallbackClass());
            if (ExpectationResponseCallback.class.isAssignableFrom(expectationResponseCallbackClass)) {
                Constructor<? extends ExpectationResponseCallback> constructor = expectationResponseCallbackClass.getConstructor();
                return constructor.newInstance();
            } else {
                mockServerLogger.logEvent(
                    new LogEntry()
                        .setLogLevel(Level.ERROR)
                        .setHttpRequest(null)
                        .setMessageFormat(httpClassCallback.getCallbackClass() + " does not implement " + ExpectationResponseCallback.class.getName() + " required for responses using class callback")
                );
            }
        } catch (ClassNotFoundException e) {
            mockServerLogger.logEvent(
                new LogEntry()
                    .setLogLevel(Level.ERROR)
                    .setMessageFormat("ClassNotFoundException - while trying to instantiate ExpectationResponseCallback class \"" + httpClassCallback.getCallbackClass() + "\"")
                    .setThrowable(e)
            );
        } catch (NoSuchMethodException e) {
            mockServerLogger.logEvent(
                new LogEntry()
                    .setLogLevel(Level.ERROR)
                    .setMessageFormat("NoSuchMethodException - while trying to create default constructor on ExpectationResponseCallback class \"" + httpClassCallback.getCallbackClass() + "\"")
                    .setThrowable(e)
            );
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            mockServerLogger.logEvent(
                new LogEntry()
                    .setLogLevel(Level.ERROR)
                    .setMessageFormat("InvocationTargetException - while trying to execute default constructor on ExpectationResponseCallback class \"" + httpClassCallback.getCallbackClass() + "\"")
                    .setThrowable(e)
            );
        }
        return null;
    }

    private HttpResponse invokeCallbackMethod(HttpClassCallback httpClassCallback, HttpRequest httpRequest) {
        if (httpRequest != null) {
            ExpectationResponseCallback expectationResponseCallback = instantiateCallback(httpClassCallback);
            if (expectationResponseCallback != null) {
                try {
                    return expectationResponseCallback.handle(httpRequest);
                } catch (Throwable throwable) {
                    mockServerLogger.logEvent(
                        new LogEntry()
                            .setLogLevel(Level.ERROR)
                            .setHttpRequest(httpRequest)
                            .setMessageFormat(httpClassCallback.getCallbackClass() + " throw exception while executing handle callback method - " + throwable.getMessage())
                            .setThrowable(throwable)
                    );
                    return notFoundResponse();
                }
            } else {
                return notFoundResponse();
            }
        } else {
            return notFoundResponse();
        }
    }
}
