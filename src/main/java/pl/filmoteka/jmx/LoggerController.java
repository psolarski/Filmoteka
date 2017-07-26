package pl.filmoteka.jmx;


import org.springframework.jmx.export.annotation.ManagedResource;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Created by Marek on 7/25/2017.
 */
@ManagedResource(objectName = "pl.filmoteka.jmx:type=JMX,name=LoggerController")
public class LoggerController implements LoggerControllerMBean {

    private static Logger logger = LogManager.getLogManager().getLogger("");
    
    @Override
    public void activateLoggingLevelOff() {
        logger.setLevel(Level.OFF);
    }

    @Override
    public void activateLoggingLevelConfig() {
        logger.setLevel(Level.CONFIG);
    }

    @Override
    public void activateLoggingLevelFine() {
        logger.setLevel(Level.FINE);
    }

    @Override
    public void activateLoggingLevelFiner() {
        logger.setLevel(Level.FINER);
    }

    @Override
    public void activateLoggingLevelFinest() {
        logger.setLevel(Level.FINEST);
    }

    @Override
    public void activateLoggingLevelInfo() {
        logger.setLevel(Level.INFO);
    }

    @Override
    public void activateLoggingLevelSevere() {
        logger.setLevel(Level.SEVERE);
    }

    @Override
    public void activateLoggingLevelWarning() {
        logger.setLevel(Level.WARNING);
    }

    @Override
    public void activateLoggingLevelAll() {
        logger.setLevel(Level.ALL);
    }

    @Override
    public String getLoggingLevel() {
        return logger.getLevel().getName();
    }
}
