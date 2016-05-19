package support.im.aop;

public enum PointCutEnum {

  CONTACTS_OP_POINTCUT("ContactsFragmentOP");

  private String name;

  private PointCutEnum(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
