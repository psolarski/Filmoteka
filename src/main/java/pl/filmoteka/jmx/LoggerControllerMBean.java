package pl.filmoteka.jmx;

/**
 * Created by Marek on 7/25/2017.
 */
public interface LoggerControllerMBean {

    void activateLoggingLevelOff();
    void activateLoggingLevelConfig();
    void activateLoggingLevelFine();
    void activateLoggingLevelFiner();
    void activateLoggingLevelFinest();
    void activateLoggingLevelInfo();
    void activateLoggingLevelSevere();
    void activateLoggingLevelWarning();
    void activateLoggingLevelAll();

    String getLoggingLevel();
}
