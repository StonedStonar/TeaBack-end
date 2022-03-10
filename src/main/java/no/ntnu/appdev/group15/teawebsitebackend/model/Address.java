package no.ntnu.appdev.group15.teawebsitebackend.model;

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
    String wholeAddress = getAddress()+getHouseNumber()+getPlace();
    return wholeAddress;
  }


}
