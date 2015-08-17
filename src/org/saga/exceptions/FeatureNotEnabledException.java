package org.saga.exceptions;

/**
 * Created by Oliver on 08/08/2015.
 */
public class FeatureNotEnabledException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Sets a feature name.
     *
     * @param name
     *            name
     */
    public FeatureNotEnabledException(String name) {
        super("feature=" + name);
    }

    /**
     * Sets a feature name and cause.
     *
     * @param name
     *            name
     * @param cause
     *            cause
     */
    public FeatureNotEnabledException(String name, String cause) {
        super("feature=" + name + ", cause=" + cause);
    }

}
