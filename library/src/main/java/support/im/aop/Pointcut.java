package support.im.aop;

public interface Pointcut {
  void registerAdvice(Advice advice);
}
