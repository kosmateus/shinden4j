package com.github.kosmateus.shinden.utils;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Utility class for method parameter validation.
 *
 * <p>The {@code MethodValidator} class provides functionality to validate method parameters
 * using the Bean Validation API. It checks for constraint violations and throws a {@link ConstraintViolationException}
 * if any violations are found.</p>
 *
 * @version 1.0.0
 */
public class MethodValidator {

    private final Validator validator;

    /**
     * Constructs a new {@code MethodValidator} instance.
     *
     * <p>This constructor initializes a {@link Validator} using the default provider and a custom message interpolator.</p>
     */
    public MethodValidator() {
        try (ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    /**
     * Validates the method parameters against any constraints defined by annotations.
     *
     * <p>If any parameter is invalid, a {@link ConstraintViolationException} is thrown containing the set of violations.</p>
     *
     * @param args the method parameters to validate
     * @throws ConstraintViolationException if any constraint violations are found
     */
    public void validateMethodParameters(Object[] args) {
        for (Object arg : args) {
            if (arg != null) {
                Set<ConstraintViolation<Object>> violations = validator.validate(arg);
                if (!violations.isEmpty()) {
                    throw new ConstraintViolationException(violations);
                }
            }
        }
    }
}
