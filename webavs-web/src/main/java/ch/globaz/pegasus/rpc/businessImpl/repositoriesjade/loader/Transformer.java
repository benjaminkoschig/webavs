package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

public interface Transformer<I, O> extends Cloneable {
    public O transform(I input);
}
