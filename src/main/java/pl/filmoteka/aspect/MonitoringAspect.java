package pl.filmoteka.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Objects;

/**
 * Created by Piotr on 15.04.2017.
 */
@Aspect
@Component
public class MonitoringAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Around("pl.filmoteka.aspect.FilmotekaPointcuts.monitored()")
    public Object monitor(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        logger.info("Monitoring: " + proceedingJoinPoint.getSignature());

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        logger.info("Method took: " + stopWatch.getTotalTimeSeconds());

        return result;
    }
}
