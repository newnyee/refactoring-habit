package com.refactoringhabit.common.custom;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class TimerAop {

    @Pointcut("@annotation(com.refactoringhabit.common.annotation.Timer)")
    private void enableTimer() {}

    @Around("enableTimer()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        joinPoint.proceed();
        stopWatch.stop();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.debug("{} - 실행시간 : {} ms", signature.getName(), stopWatch.getTotalTimeMillis());
    }
}
