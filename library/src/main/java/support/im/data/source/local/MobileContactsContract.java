package support.im.data.source.local;

import android.os.Build;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import support.ui.utilities.BuildCompat;

public final class MobileContactsContract {

  public static final int ID_INDEX = 0;
  public static final int LOOKUP_KEY_INDEX = 1;
  public static final int DISPLAY_NAME_INDEX = 2;
  public static final int HAS_PHONE_NUMBER_INDEX = 3;

  public static final String SORT_ORDER =
      (BuildCompat.hasHoneycomb() ? Contacts.SORT_KEY_PRIMARY : Contacts. SORT_KEY_ALTERNATIVE) + " ASC";

  public static final String[] PROJECTION = {
      Contacts._ID,
      Contacts.LOOKUP_KEY,
      BuildCompat.hasHoneycomb() ? Contacts.DISPLAY_NAME_PRIMARY : Contacts.DISPLAY_NAME,
      Contacts.HAS_PHONE_NUMBER,
  };



  public static class PhoneNumberContract {
    public static final int PHONE_NUMBER_INDEX = 0;
    public static final String[] PROJECTION = {
        Phone.NUMBER,
    };
  }

  public static class EmailContract {
    public static final int EMAIL_DATA_INDEX = 0;
    public static final int EMAIL_TYPE_INDEX = 1;
    public static final String[] PROJECTION = {
        ContactsContract.CommonDataKinds.Email.DATA,
        ContactsContract.CommonDataKinds.Email.TYPE,
    };
  }

  public static class NoteContract {
    public static final int NOTE_INDEX = 0;
    public static final String[] PROJECTION = {
        ContactsContract.CommonDataKinds.Note.NOTE
    };
  }

  public static class AddressContract {
    public static final int POBOX_INDEX = 0;
    public static final int STREET_INDEX = 1;
    public static final int CITY_INDEX = 2;
    public static final int REGION_INDEX = 3;
    public static final int POSTCODE_INDEX = 4;
    public static final int COUNTRY_INDEX = 5;
    public static final int TYPE_INDEX = 6;
    public static final String[] PROJECTION = {
        ContactsContract.CommonDataKinds.StructuredPostal.POBOX,
        ContactsContract.CommonDataKinds.StructuredPostal.STREET,
        ContactsContract.CommonDataKinds.StructuredPostal.CITY,
        ContactsContract.CommonDataKinds.StructuredPostal.REGION,
        ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE,
        ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY,
        ContactsContract.CommonDataKinds.StructuredPostal.TYPE
    };
  }

  public static class ImContract {
    public static final int NAME_INDEX = 0;
    public static final int TYPE_INDEX = 0;
    public static final String[] PROJECTION = {
        ContactsContract.CommonDataKinds.Im.DATA,
        ContactsContract.CommonDataKinds.Im.TYPE
    };
  }

  public static class OrganizationContract {
    public static final int NAME_INDEX = 0;
    public static final int TITLE_INDEX = 0;
    public static final String[] PROJECTION = {
        ContactsContract.CommonDataKinds.Organization.DATA,
        ContactsContract.CommonDataKinds.Organization.TITLE
    };
  }

}
