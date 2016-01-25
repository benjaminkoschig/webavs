package ch.globaz.common.listoutput;

public abstract class LoaderHandlerOuter<P, L, H, O> {
    public abstract L load(P p);

    public abstract H handl(L l);

    public abstract O out(H h);

    public O run(P p) {
        L l = load(p);
        H h = handl(l);
        O o = out(h);
        return o;
    }
}
