package pl.filmoteka.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.filmoteka.model.Actor;

import java.util.List;

/**
 * Created by Piotr on 15.04.2017.
 */
@Aspect
@Component
public class TracingAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Before("pl.filmoteka.aspect.FilmotekaPointcuts.anyControllerBean()")
    public void beforeAnyControllerMethod(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        logger.info(" ---> Method " + className + "." + methodName + " is about to be called");
    }

    @AfterReturning(pointcut = "pl.filmoteka.aspect.FilmotekaPointcuts.anyControllerBean()", returning="actor")
    public void afterActorControllerReturningActor(JoinPoint joinPoint, Actor actor) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        logger.info(" ---> Method " + className + "." + methodName + " performed as expected");
        logger.info("Returned Actor: " + actor);
    }

    @AfterReturning(pointcut = "pl.filmoteka.aspect.FilmotekaPointcuts.anyControllerBean()", returning="actors")
    public void afterActorControllerReturningActorsList(JoinPoint joinPoint, List<Actor> actors) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        logger.info(" ---> Method " + className + "." + methodName + " performed as expected");
        actors.forEach(s -> {
                logger.info("Actor in list: " + s);
        });
    }
}
