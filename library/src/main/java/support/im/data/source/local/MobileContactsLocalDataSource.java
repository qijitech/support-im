package support.im.data.source.local;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.support.annotation.NonNull;
import android.support.v4.os.AsyncTaskCompat;
import com.google.common.collect.Lists;
import java.util.List;
import support.im.data.MobileContact;
import support.im.data.source.MobileContactsDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

public class MobileContactsLocalDataSource implements MobileContactsDataSource {

  private static MobileContactsLocalDataSource INSTANCE;

  private Context mContext;

  // Prevent direct instantiation.
  public MobileContactsLocalDataSource(Context context) {
    mContext = checkNotNull(context);
  }

  public static MobileContactsLocalDataSource getInstance(@NonNull Context context) {
    if (INSTANCE == null) {
      INSTANCE = new MobileContactsLocalDataSource(context);
    }
    return INSTANCE;
  }
  
  @Override public void getMobileContacts(@NonNull final LoadMobileContactsCallback callback) {
    AsyncTaskCompat.executeParallel(new AsyncTask<Void, Void, List<MobileContact>>() {
      @Override protected List<MobileContact> doInBackground(Void... params) {
        List<MobileContact> mobileContacts = loadMobileContacts();
        return mobileContacts;
      }

      @Override protected void onPostExecute(List<MobileContact> mobileContacts) {
        super.onPostExecute(mobileContacts);
        callback.onMobileContactsLoaded(mobileContacts);
      }
    });
  }

  /**
   *
   */
  private List<MobileContact> loadMobileContacts() {
    ContentResolver cr = mContext.getContentResolver();
    Cursor cursor = null;
    try {
      cursor = cr.query(Contacts.CONTENT_URI, MobileContactsContract.PROJECTION, null, null, null);
      List<MobileContact> mobileContacts = Lists.newArrayList();
      while (cursor != null && cursor.moveToNext()) {
        MobileContact mobileContact = new MobileContact();
        String id = cursor.getString(MobileContactsContract.ID_INDEX);
        String name = cursor.getString(MobileContactsContract.DISPLAY_NAME_INDEX);
        String lookupKey = cursor.getString(MobileContactsContract.LOOKUP_KEY_INDEX);
        boolean hasPhoneNumber = Integer.parseInt(cursor.getString(MobileContactsContract.HAS_PHONE_NUMBER_INDEX)) > 0;
        mobileContact.setId(id);
        mobileContact.setName(name);
        mobileContact.setLookupKey(lookupKey);
        mobileContact.setHasPhoneNumber(hasPhoneNumber);
        if (hasPhoneNumber) {
          mobileContact.setPhoneNumbers(loadPhoneNumber(cr, id));
          mobileContact.setEmails(loadMobileEmail(cr, id));
          mobileContact.setNote(loadMobileNote(cr, id));
          mobileContact.setAddress(loadPostalAddress(cr, id));
          mobileContact.setInstantMessengers(loadInstantMessengers(cr, id));
          mobileContact.setOrganizations(loadOrganizations(cr, id));
        }
        mobileContacts.add(mobileContact);
      }
      return mobileContacts;
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }

  /**
   *
   */
  private List<MobileContact.PhoneNumber> loadPhoneNumber(ContentResolver cr, String contactId) {
    Cursor cursor = null;
    try {
      cursor = cr.query(Phone.CONTENT_URI, MobileContactsContract.PhoneNumberContract.PROJECTION,
          Phone.CONTACT_ID + " = ?", new String[] { contactId },
          null);
      List<MobileContact.PhoneNumber> phoneNumbers = Lists.newArrayList();
      while (cursor != null && cursor.moveToNext()) {
        String phone = cursor.getString(MobileContactsContract.PhoneNumberContract.PHONE_NUMBER_INDEX);
        phoneNumbers.add(new MobileContact.PhoneNumber(phone));
      }
      return phoneNumbers;
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }

  private List<MobileContact.Email> loadMobileEmail(ContentResolver cr, String contactId) {
    // get email and type
    Cursor cursor = null;
    try {
      cursor = cr.query(Email.CONTENT_URI, MobileContactsContract.EmailContract.PROJECTION,
          Email.CONTACT_ID + " = ?", new String[] { contactId }, null);
      List<MobileContact.Email> emails = Lists.newArrayList();
      while (cursor != null && cursor.moveToNext()) {
        // This would allow you get several email addresses
        // if the email addresses were stored in an array
        String email = cursor.getString(MobileContactsContract.EmailContract.EMAIL_DATA_INDEX);
        String emailType = cursor.getString(MobileContactsContract.EmailContract.EMAIL_TYPE_INDEX);
        emails.add(new MobileContact.Email(email, emailType));
      }
      return emails;
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }

  /**
   *
   */
  private MobileContact.Note loadMobileNote(ContentResolver cr, String contactId) {
    // get email and type
    Cursor cursor = null;
    try {
      String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
      cursor = cr.query(ContactsContract.Data.CONTENT_URI, MobileContactsContract.NoteContract.PROJECTION, noteWhere,
          new String[] { contactId, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE}, null);
      MobileContact.Note note = null;
      if (cursor != null && cursor.moveToFirst()) {
        note = new MobileContact.Note(cursor.getString(MobileContactsContract.NoteContract.NOTE_INDEX));
      }
      return note;
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }

  private List<MobileContact.Address> loadPostalAddress(ContentResolver cr, String contactId) {
    Cursor cursor = null;
    try {
      String addressWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
      String[] addressWhereParams = new String[]{contactId, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
      cursor = cr.query(ContactsContract.Data.CONTENT_URI,
          MobileContactsContract.AddressContract.PROJECTION, addressWhere, addressWhereParams, null);
      List<MobileContact.Address> addresses = Lists.newArrayList();
      while(cursor != null && cursor.moveToNext()) {
        String poBox = cursor.getString(MobileContactsContract.AddressContract.POBOX_INDEX);
        String street = cursor.getString(MobileContactsContract.AddressContract.STREET_INDEX);
        String city = cursor.getString(MobileContactsContract.AddressContract.CITY_INDEX);
        String region = cursor.getString(MobileContactsContract.AddressContract.REGION_INDEX);
        String postalCode = cursor.getString(MobileContactsContract.AddressContract.POSTCODE_INDEX);
        String country = cursor.getString(MobileContactsContract.AddressContract.COUNTRY_INDEX);
        String type = cursor.getString(MobileContactsContract.AddressContract.TYPE_INDEX);
        MobileContact.Address address = new MobileContact.Address();
        address.setPoBox(poBox);
        address.setStreet(street);
        address.setCity(city);
        address.setRegion(region);
        address.setPostcode(postalCode);
        address.setCountry(country);
        address.setType(type);
        addresses.add(address);
      }
      return addresses;
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }

  private List<MobileContact.InstantMessenger> loadInstantMessengers(ContentResolver cr, String contactId) {
    Cursor cursor = null;
    try {
      String imWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
      String[] imWhereParams = new String[]{contactId, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE};
      cursor = cr.query(ContactsContract.Data.CONTENT_URI, MobileContactsContract.ImContract.PROJECTION, imWhere, imWhereParams, null);
      List<MobileContact.InstantMessenger> instantMessengers = Lists.newArrayList();
      while (cursor != null && cursor.moveToNext()) {
        String imName = cursor.getString(MobileContactsContract.ImContract.NAME_INDEX);
        String imType = cursor.getString(MobileContactsContract.ImContract.TYPE_INDEX);
        instantMessengers.add(new MobileContact.InstantMessenger(imName, imType));
      }
      return instantMessengers;
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }

  private List<MobileContact.Organization> loadOrganizations(ContentResolver cr, String contactId) {
    Cursor cursor = null;
    try {
      String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
      String[] orgWhereParams = new String[]{contactId, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
      cursor = cr.query(ContactsContract.Data.CONTENT_URI, MobileContactsContract.OrganizationContract.PROJECTION, orgWhere, orgWhereParams, null);
      List<MobileContact.Organization> organizations = Lists.newArrayList();
      while (cursor != null && cursor.moveToNext()) {
        String orgName = cursor.getString(MobileContactsContract.OrganizationContract.NAME_INDEX);
        String title = cursor.getString(MobileContactsContract.OrganizationContract.TITLE_INDEX);
        organizations.add(new MobileContact.Organization(orgName, title));
      }
      return organizations;
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }

  @Override public void refreshMobileContacts() {
    // Not required because the {@link MobileContactsRepository} handles the logic of refreshing the
    // tasks from all the available data sources.
  }
}
