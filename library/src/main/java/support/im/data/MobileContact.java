package support.im.data;

import java.util.List;

public class MobileContact {

  private String mId;
  private String mName;
  private String mSortLetters;
  private String mLookupKey;
  private boolean mHasPhoneNumber;
  private List<PhoneNumber> mPhoneNumbers;
  private List<Email> mEmails;
  private Note mNote;
  private List<Address> mAddress;
  private List<InstantMessenger> mInstantMessengers;
  private List<Organization> mOrganizations;

  public void setId(String id) {
    mId = id;
  }

  public void setName(String name) {
    mName = name;
  }

  public void setSortLetters(String sortLetters) {
    mSortLetters = sortLetters;
  }

  public void setLookupKey(String lookupKey) {
    mLookupKey = lookupKey;
  }

  public void setHasPhoneNumber(boolean hasPhoneNumber) {
    mHasPhoneNumber = hasPhoneNumber;
  }

  public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
    mPhoneNumbers = phoneNumbers;
  }

  public void setEmails(List<Email> emails) {
    mEmails = emails;
  }

  public void setNote(Note note) {
    mNote = note;
  }

  public void setAddress(List<Address> address) {
    mAddress = address;
  }

  public void setInstantMessengers(List<InstantMessenger> instantMessengers) {
    mInstantMessengers = instantMessengers;
  }

  public void setOrganizations(List<Organization> organizations) {
    mOrganizations = organizations;
  }

  public String getId() {
    return mId;
  }

  public String getName() {
    return mName;
  }

  public String getSortLetters() {
    return mSortLetters;
  }

  public String getLookupKey() {
    return mLookupKey;
  }

  public boolean hasPhoneNumber() {
    return mHasPhoneNumber;
  }

  public List<PhoneNumber> getPhoneNumbers() {
    return mPhoneNumbers;
  }

  public List<Email> getEmails() {
    return mEmails;
  }

  public Note getNote() {
    return mNote;
  }

  public List<Address> getAddress() {
    return mAddress;
  }

  public List<InstantMessenger> getInstantMessengers() {
    return mInstantMessengers;
  }

  public List<Organization> getOrganizations() {
    return mOrganizations;
  }

  public static class PhoneNumber {
    private String mNumber;

    public PhoneNumber(String number) {
      mNumber = number;
    }
    public String getNumber() {
      return mNumber;
    }

    @Override public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      PhoneNumber that = (PhoneNumber) o;

      return mNumber.equals(that.mNumber);
    }

    @Override public int hashCode() {
      return mNumber.hashCode();
    }
  }

  public static class Email {
    private String mEmail;
    private String mEmailType;

    public Email(String email, String emailType) {
      mEmail = email;
      mEmailType = emailType;
    }

    public String getEmail() {
      return mEmail;
    }

    public String getEmailType() {
      return mEmailType;
    }
  }

  public static class Note {
    private String mNote;

    public Note(String note) {
      mNote = note;
    }
    public String getNote() {
      return mNote;
    }
  }

  public static class Address {
    private String mPoBox;
    private String mStreet;
    private String mCity;
    private String mRegion;
    private String mPostcode;
    private String mCountry;
    private String mType;

    public void setPoBox(String poBox) {
      mPoBox = poBox;
    }

    public void setStreet(String street) {
      mStreet = street;
    }

    public void setCity(String city) {
      mCity = city;
    }

    public void setRegion(String region) {
      this.mRegion = region;
    }

    public void setPostcode(String postcode) {
      mPostcode = postcode;
    }

    public void setCountry(String country) {
      mCountry = country;
    }

    public void setType(String type) {
      mType = type;
    }

    public String getPoBox() {
      return mPoBox;
    }

    public String getStreet() {
      return mStreet;
    }

    public String getCity() {
      return mCity;
    }

    public String getRegion() {
      return mRegion;
    }

    public String getmPostcode() {
      return mPostcode;
    }

    public String getCountry() {
      return mCountry;
    }

    public String getType() {
      return mType;
    }
  }

  public static class InstantMessenger {
    private String mName;
    private String mType;

    public InstantMessenger(String imName, String imType) {
      mName = imName;
      mType = imType;
    }

    public String getName() {
      return mName;
    }

    public String getType() {
      return mType;
    }
  }

  public static class Organization {
    private String mOrgName;
    private String mTitle;

    public Organization(String orgName, String title) {
      mOrgName = orgName;
      mTitle = title;
    }

    public String getOrgName() {
      return mOrgName;
    }

    public String getTitle() {
      return mTitle;
    }
  }
}
