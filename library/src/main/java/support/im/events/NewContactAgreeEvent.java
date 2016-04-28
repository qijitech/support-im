package support.im.events;

import support.im.leanclound.contacts.AddRequest;

public class NewContactAgreeEvent {

  public AddRequest mAddRequest;

  public NewContactAgreeEvent(AddRequest addRequest) {
    mAddRequest = addRequest;
  }
}
