package ch.globaz.common.listoutput;


public abstract class LoaderOuter<P, L, O> {
    private ExecutionTime time;
    private L dataLoaded;
    private P param;
    private O out;

    public abstract L load(P p) throws Exception;

    public abstract O out(L l) throws Exception;

    public O run(P p) throws Exception {
        param = p;
        time = new ExecutionTime();
        dataLoaded = load(p);
        time.addLoad();
        out = out(dataLoaded);
        time.addHandler();
        return out;
    }

    public O run() throws Exception {
        param = null;
        time = new ExecutionTime();
        dataLoaded = load(param);
        time.addLoad();
        out = out(dataLoaded);
        time.addHandler();
        return out;
    }

    public ExecutionTime getTime() {
        return time;
    }

    public L getDataLoaded() {
        return dataLoaded;
    }

    public P getParam() {
        return param;
    }

    public O getOut() {
        return out;
    }

}
