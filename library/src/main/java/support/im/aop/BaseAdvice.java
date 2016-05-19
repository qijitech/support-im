package support.im.aop;

public class BaseAdvice implements Advice {

  public BaseAdvice(Pointcut pointcut) {
    if (pointcut != null) {
      pointcut.registerAdvice(this);
    }
  }
}
