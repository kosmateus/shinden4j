package com.github.kosmateus.shinden.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Invocation handler for method validation.
 *
 * <p>The {@code ValidationInvocationHandler} class uses Java's reflection API to create a dynamic proxy
 * that intercepts method calls to the target object, allowing for validation of method parameters before
 * invoking the actual method.</p>
 *
 * @version 1.0.0
 */
public class ValidationInvocationHandler implements InvocationHandler {

    private final Object target;
    private final MethodValidator methodValidator;

    /**
     * Constructs a new {@code ValidationInvocationHandler} for the specified target object.
     *
     * @param target the target object whose methods will be intercepted
     */
    public ValidationInvocationHandler(Object target) {
        this.target = target;
        this.methodValidator = new MethodValidator();
    }

    /**
     * Creates a proxy instance that performs validation on method calls.
     *
     * @param target        the target object whose methods are to be proxied
     * @param interfaceType the interface class that the proxy should implement
     * @param <T>           the type of the interface
     * @return a proxy instance that intercepts method calls to the target object
     */
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T target, Class<T> interfaceType) {
        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class<?>[]{interfaceType},
                new ValidationInvocationHandler(target)
        );
    }

    /**
     * Intercepts method calls on the proxy instance to validate parameters and invoke the actual method.
     *
     * @param proxy  the proxy instance that the method was invoked on
     * @param method the {@link Method} instance corresponding to the interface method invoked on the proxy instance
     * @param args   an array of objects containing the values of the arguments passed in the method invocation
     * @return the result of the method invocation
     * @throws Throwable if an error occurs during method invocation or validation
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        methodValidator.validateMethodParameters(args);
        return method.invoke(target, args);
    }
}
