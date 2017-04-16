package pl.filmoteka.aspect;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by Piotr on 15.04.2017.
 */
public class FilmotekaPointcuts {

    @Pointcut(value = "execution(* *.controller..findAll(..))")
    public void anyControllerMethod() {}

    @Pointcut("bean(*Controller)")
    public void anyControllerBean() {}

    @Pointcut("bean(ActorsC*)")
    public void actorControllerBean() {}

    @Pointcut("@annotation(Monitored)")
    public void monitored() {}

}
