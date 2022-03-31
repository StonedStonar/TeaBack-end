package no.ntnu.appdev.group15.teawebsitebackend.model;

import javax.persistence.Entity;

/**
 * Represents an address class.
 * @author Trine Merete Staverløkk
 * @version 0.1
 */
public class Address {

  private int postalCode;
  private String place;
  private String address;
  private int houseNumber;
  private String country;

  public Address() {
  }

  /**
   * makes an instance of Address class
   * @param postalCode The postal code
   * @param place The place
   * @param address the address
   * @param houseNumber The housenumber
   * @param country The country
   *
   * @throws
   */


  public Address(int postalCode, String place, String address, int houseNumber, String country){
    checkIfPostalCodeIsAboveZero(postalCode);
    checkIfPlaceIsValid(place);
    checkIfAddressIsValid(address);
    checkIfHouseNumberIsAboveZero(houseNumber);
    checkIfCountryIsValid(country);
    this.postalCode = postalCode;
    this.place = place;
    this.address = address;
    this.houseNumber = houseNumber;
    this.country = country;
  }

  public int getPostalCode() {
    return postalCode;
  }

  public String getPlace() {
    return place;
  }

  public String getAddress() {
    return address;
  }

  public int getHouseNumber() {
    return houseNumber;
  }

  public String getCountry() {
    return country;
  }

  public String getWholeAddressAsString() {
    String wholeAddress = getAddress()+getHouseNumber()+getPlace()+getPostalCode()+getCountry();
    return wholeAddress;
  }


/**
 * Checks if postalCode is above zero.
 * @param postalCode the postal code to check
 * @throws IllegalArgumentException gets thrown if the postal code is zero or below zero.
 */
  private void checkIfPostalCodeIsAboveZero(int postalCode) {
    checkIfIntIsAboveZero(postalCode, "postal Code");
  }

  /**
   * Checks if country is valid
   * @param country the input to check
   * @throws IllegalArgumentException gets thrown if the input is invalid
   */
  private void checkIfCountryIsValid(String country) {
    checkString(country, "country");
  }

  /**
   * Checks if house number is above zero.
   * @param houseNumber the house number to check
   * @throws IllegalArgumentException gets thrown if housenumber is zero or below zero.
   */
  private void checkIfHouseNumberIsAboveZero(int houseNumber) {
    checkIfIntIsAboveZero(houseNumber, "house number");
  }

  /**
   * Checks if address is valid
   * @param address the address to check
   */
  private void checkIfAddressIsValid(String address) {
    checkString(address, "address");
  }

  /**
   * Checks if
   */
  private void checkIfPlaceIsValid(String place) {
  }

  /**
   * Checks if the input long is above zero.
   * @param number the number to check.
   * @param prefix the prefix the error should have.
   * @throws IllegalArgumentException gets thrown if the number is below or equal to zero.
   */
  private void checkIfIntIsAboveZero(int number, String prefix){
    if (number <= 0){
      throw new IllegalArgumentException("The " + prefix + " must be above zero.");
    }
  }

  /**
   * Checks if a string is of a valid format or not.
   * @param stringToCheck the string you want to check.
   * @param errorPrefix   the error the exception should have if the string is invalid.
   * @throws IllegalArgumentException gets thrown if the string is empty or null.
   */
  private void checkString(String stringToCheck, String errorPrefix) {
    checkIfObjectIsNull(stringToCheck, errorPrefix);
    if (stringToCheck.isEmpty()) {
      throw new IllegalArgumentException("The " + errorPrefix + " cannot be empty.");
    }
  }

  /**
   * Checks if an object is null.
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
