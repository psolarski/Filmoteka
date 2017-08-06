package pl.filmoteka.jmx;

import org.junit.Test;

import java.util.logging.Level;

import static org.assertj.core.api.Assertions.assertThat;

public class LoggerControllerTests {

    private LoggerController sut = new LoggerController();

    @Test
    public void activateLoggingLevelOff() {
        sut.activateLoggingLevelOff();
        assertThat(sut.getLoggingLevel()).isEqualTo(Level.OFF.getName());
    }

    @Test
    public void activateLoggingLevelConfig() {
        sut.activateLoggingLevelConfig();
        assertThat(sut.getLoggingLevel()).isEqualTo(Level.CONFIG.getName());
    }

    @Test
    public void activateLoggingLevelFine() {
        sut.activateLoggingLevelFine();
        assertThat(sut.getLoggingLevel()).isEqualTo(Level.FINE.getName());
    }

    @Test
    public void activateLoggingLevelFiner() {
        sut.activateLoggingLevelFiner();
        assertThat(sut.getLoggingLevel()).isEqualTo(Level.FINER.getName());
    }

    @Test
    public void activateLoggingLevelFinest() {
        sut.activateLoggingLevelFinest();
        assertThat(sut.getLoggingLevel()).isEqualTo(Level.FINEST.getName());
    }

    @Test
    public void activateLoggingLevelInfo() {
        sut.activateLoggingLevelInfo();
        assertThat(sut.getLoggingLevel()).isEqualTo(Level.INFO.getName());
    }

    @Test
    public void activateLoggingLevelSevere() {
        sut.activateLoggingLevelSevere();
        assertThat(sut.getLoggingLevel()).isEqualTo(Level.SEVERE.getName());
    }

    @Test
    public void activateLoggingLevelWarning() {
        sut.activateLoggingLevelWarning();
        assertThat(sut.getLoggingLevel()).isEqualTo(Level.WARNING.getName());
    }

    @Test
    public void activateLoggingLevelAll() {
        sut.activateLoggingLevelAll();
        assertThat(sut.getLoggingLevel()).isEqualTo(Level.ALL.getName());
    }

    @Test
    public void checkSetUpLoggingLevel() {
        assertThat(sut.getLoggingLevel()).isNotNull().isNotEmpty();
    }
}
