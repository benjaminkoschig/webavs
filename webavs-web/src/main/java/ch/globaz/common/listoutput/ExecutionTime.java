package ch.globaz.common.listoutput;

import org.apache.commons.lang.time.StopWatch;

public class ExecutionTime {

    private long check;
    private long load;
    private int nombre;
    private long out;
    private long start;
    private long stop;
    private long handle;
    private StopWatch watch = new StopWatch();

    public ExecutionTime() {
        start = System.nanoTime();
        watch.start();
    }

    public void addCheck() {
        watch.stop();
        check = watch.getTime();
        startAndReset();
    }

    public void addLoad() {
        watch.stop();
        load = watch.getTime();
        startAndReset();
    }

    public void addOut() {
        watch.stop();
        out = watch.getTime();
        startAndReset();
    }

    public void addHandler() {
        watch.stop();
        handle = watch.getTime();
        startAndReset();
    }

    public long getCheck() {
        return check;
    }

    public long getLoad() {
        return load;
    }

    public int getNombre() {
        return nombre;
    }

    public long getOut() {
        return out;
    }

    public long getHandle() {
        return handle;
    }

    public long getTotal() {
        return stop - start;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    private void startAndReset() {
        watch.reset();
        watch.start();
    }

    public void stop() {
        stop = System.nanoTime();
    }

    float total() {
        return (load + check + handle + out);
    }

    float computeAvrage() {
        if (nombre > 0) {
            return (total() / nombre);
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        if (check > 0) {
            return "Loader: " + load + "ms | Checker: " + check + "ms | Handle: " + handle + "ms | Out: " + out
                    + "ms | nb: " + nombre + "| Average: " + computeAvrage() + "ms" + " | Total:" + total() + "ms";
        }
        return "Loader: " + load + "ms | Handle: " + handle + "ms | Out: " + out + "ms | nb: " + nombre + "| Average: "
                + computeAvrage() + "ms" + " | Total:" + total() + "ms";
    }
}
