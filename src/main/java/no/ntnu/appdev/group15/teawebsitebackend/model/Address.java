package no.ntnu.appdev.group15.teawebsitebackend.model;


import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Represents an address class.
 * @author Trine Merete Staverløkk
 * @version 0.1
 */
@Entity(name = "address")
public class Address {

  @Id
  @GeneratedValue
  private long addressID;
  private int postalCode;
  private String postalPlace;
  private String streetName;
  private int houseNumber;
  private String country;

  public Address() {
  }

  /**
   * makes an instance of Address class
   * @param addressID the id of the address.
   * @param postalCode The postal code
   * @param postalPlace The place
   * @param streetName the address
   * @param houseNumber The house number
   * @param country The country
   * @throws IllegalArgumentException gets thrown if the input parameters are invalid.
   */
  @JsonCreator
  public Address(long addressID, int postalCode, String postalPlace, String streetName, int houseNumber, String country){
    checkIfPostalCodeIsAboveZero(postalCode);
    checkIfPostalPlaceIsValid(postalPlace);
    checkIfStreetNameIsValid(streetName);
    checkIfHouseNumberIsAboveZero(houseNumber);
    checkIfCountryIsValid(country);
    checkIfNumberIsAboveZero(addressID, "address id");
    this.postalCode = postalCode;
    this.postalPlace = postalPlace;
    this.streetName = streetName;
    this.houseNumber = houseNumber;
    this.country = country;
    this.addressID = addressID;
  }

  /**
   * makes an instance of Address class
   * @param postalCode The postal code
   * @param postalPlace The place
   * @param streetName the address
   * @param houseNumber The house number
   * @param country The country
   * @throws IllegalArgumentException gets thrown if the input parameters are invalid.
   */
  public Address(int postalCode, String postalPlace, String streetName, int houseNumber, String country){
    checkIfPostalCodeIsAboveZero(postalCode);
    checkIfPostalPlaceIsValid(postalPlace);
    checkIfStreetNameIsValid(streetName);
    checkIfHouseNumberIsAboveZero(houseNumber);
    checkIfCountryIsValid(country);
    this.postalCode = postalCode;
    this.postalPlace = postalPlace;
    this.streetName = streetName;
    this.houseNumber = houseNumber;
    this.country = country;
    this.addressID = 0;
  }

  /**
   * Gets the id of the address.
   * @return the id of the address.
   */
  public long getAddressID(){
    return addressID;
  }

  /**
   * Gets the postal code.
   * @return Returns the postal code of the address.
   */
  public int getPostalCode() {
    return postalCode;
  }

  /**
   * Gets the postal place.
   * @return Returns the postal place of the address.
   */
  public String getPostalPlace() {
    return postalPlace;
  }

  /**
   * Gets the street name.
   * @return Returns the street name of the address.
   */
  public String getStreetName() {
    return streetName;
  }

  /**
   * Gets the house number.
   * @return Returns the house number of the address.
   */
  public int getHouseNumber() {
    return houseNumber;
  }

  /**
   * Gets the country.
   * @return Returns the country of the address.
   */
  public String getCountry() {
    return country;
  }

  /**
   * Gets the whole address as a string.
   * @return the whole address.
   */
  public String getWholeAddressAsString() {
    String wholeAddress = getStreetName()+" "+getHouseNumber()+ " "+getPostalPlace()+" "+getPostalCode()+" "+getCountry();
    return wholeAddress;
  }

/**
 * Checks if postalCode is above zero.
 * @param postalCode the postal code to check
 * @throws IllegalArgumentException gets thrown if the postal code is zero or below zero.
 */
  private void checkIfPostalCodeIsAboveZero(int postalCode) {
    checkIfNumberIsAboveZero(postalCode, "postal Code");
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
    checkIfNumberIsAboveZero(houseNumber, "house number");
  }

  /**
   * Checks if address is valid
   * @param streetName the address to check
   */
  private void checkIfStreetNameIsValid(String streetName) {
    checkString(streetName, "Streetname");
  }

  /**
   * Checks if postal value is valid.
   */
  private void checkIfPostalPlaceIsValid(String postalPlace) {
    checkString(postalPlace, "postalPlace");
  }

  /**
   * Checks if the input long is above zero.
   * @param number the number to check.
   * @param prefix the prefix the error should have.
   * @throws IllegalArgumentException gets thrown if the number is below or equal to zero.
   */
  private void checkIfNumberIsAboveZero(long number, String prefix){
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
