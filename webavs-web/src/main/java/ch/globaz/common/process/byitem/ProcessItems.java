package ch.globaz.common.process.byitem;

import java.util.List;

public interface ProcessItems<T extends ProcessItem> {
    public abstract void before();

    public abstract List<T> resolveItems();

    public abstract void after();

    public abstract String getKey();

}
