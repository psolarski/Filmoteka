package pl.filmoteka.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.logging.Logger;

/**
 * Created by Piotr on 15.04.2017.
 */
@Aspect
@Component
public class MonitoringAspect {

    private static Logger logger = Logger.getLogger(MonitoringAspect.class.getName());

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
