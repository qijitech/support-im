package support.im.events;

import support.im.emoticons.FuncItem;

public class FuncViewClickEvent {
  public FuncItem mFuncItem;

  public FuncViewClickEvent() {

  }

  public FuncViewClickEvent(FuncItem funcItem) {
    this.mFuncItem = funcItem;
  }
}
