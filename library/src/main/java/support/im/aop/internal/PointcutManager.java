package support.im.aop.internal;

import support.im.aop.Advice;
import support.im.aop.Pointcut;

public class PointcutManager<P> implements Pointcut {
  private Advice localAdvice;
  protected final P pointcut;

  public PointcutManager(P pointcut) {
    this.pointcut = pointcut;
  }

  public void registerAdvice(Advice advice) {
    this.localAdvice = advice;
  }

  public Advice getAdvices() {
    return this.localAdvice;
  }
}
