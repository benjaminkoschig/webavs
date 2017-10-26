package ch.globaz.pegasus.rpc.businessImpl.sedex;

import java.io.File;

public interface Converter<T, D> {
    public D convert(T xml, File file);
}
