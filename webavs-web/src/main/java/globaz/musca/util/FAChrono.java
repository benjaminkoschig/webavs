package globaz.musca.util;

public class FAChrono {

    private long startTime = 0;
    private long stopTime = 0;

    public long getStartTime() {
        return startTime;
    }

    public long getStopTime() {
        return stopTime;
    }

    public String giveDurationHeureMin() throws Exception {

        if (!isChronometrageValide()) {
            throw new Exception("Duration HeureMin Error Chrono : (startTime=" + getStartTime() + "/stopTime="
                    + getStopTime() + ")");
        }

        long durationMin = (getStopTime() - getStartTime()) / 1000 / 60;
        long durationHeure = durationMin / 60;

        return durationHeure + ":" + (durationMin - (durationHeure * 60)) + "(HH:MM)";

    }

    public long giveDurationMillis() throws Exception {

        if (!isChronometrageValide()) {
            throw new Exception("Duration Millis Error Chrono : (startTime=" + getStartTime() + "/stopTime="
                    + getStopTime() + ")");
        }

        return getStopTime() - getStartTime();
    }

    private boolean isChronometrageValide() {
        return (getStartTime() > 0) && (getStopTime() > 0) && (getStartTime() <= getStopTime());
    }

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void stop() {
        stopTime = System.currentTimeMillis();
    }

}
