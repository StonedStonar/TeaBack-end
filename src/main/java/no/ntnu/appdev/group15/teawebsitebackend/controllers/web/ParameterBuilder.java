package no.ntnu.appdev.group15.teawebsitebackend.controllers.web;

/**
 * A class that represents basic methods to make a parameter line for web.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class ParameterBuilder {

    private StringBuilder stringBuilder;

    private String pageName;

    private boolean firstAppend;

    /**
     * Makes an instance of the ParameterBuilder class.
     * @param pageName the name of the page.
     */
    public ParameterBuilder(String pageName) {
        checkString(pageName,  "page name");
        stringBuilder = new StringBuilder();
        this.pageName = pageName;
        firstAppend = true;
    }

    /**
     * Sets the page name.
     * @param pageName the page name.
     */
    public void setPageName(String pageName){
        checkString(pageName, "pageName");
        this.pageName = pageName;
    }

    /**
     * Adds a parameter and parameter value to the link url.
     * @param parameterName the parameter name like "error"
     * @param parameterValue the value like "true"
     */
    public void addParameter(String parameterName, String parameterValue){
        checkString(parameterName, "parameter name");
        checkString(parameterValue, "parameter value");
        if (firstAppend){
            stringBuilder.append("?");
            firstAppend = false;
        }else {
            stringBuilder.append("&");
        }
        stringBuilder.append(parameterName).append("=").append(parameterValue);
    }

    /**
     * Returns if the parameter builder is empty.
     * @return <code>true</code> if the parameter builder is empty.
     *         <code>false</code> if the parameter builder is not empty.
     */
    public boolean isFirstAppend(){
        return firstAppend;
    }

    /**
     * Makes the string from the input parameters.
     * @return the string.
     */
    public String buildString(){
        return pageName + stringBuilder.toString();
    }

    /**
     * Checks if a string is of a valid format or not.
     *
     * @param stringToCheck the string you want to check.
     * @param errorPrefix   the error the exception should have if the string is invalid.
     * @throws IllegalArgumentException gets thrown if the string to check is empty or null.
     */
    private void checkString(String stringToCheck, String errorPrefix) {
        checkIfObjectIsNull(stringToCheck, errorPrefix);
        if (stringToCheck.isEmpty()) {
            throw new IllegalArgumentException("The " + errorPrefix + " cannot be empty.");
        }
    }

    /**
     * Checks if an object is null.
     *
     * @param object the object you want to check.
     * @param error  the error message the exception should have.
     * @throws IllegalArgumentException gets thrown if the object is null.
     */
    private void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }
}
