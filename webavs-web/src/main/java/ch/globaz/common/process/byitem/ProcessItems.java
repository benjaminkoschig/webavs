package ch.globaz.common.process.byitem;

import java.util.List;

public interface ProcessItems<T extends ProcessItem> {
    public abstract void before();

    /**
     * 
     * 
     * @return List des items à itérer.
     */
    public abstract List<T> resolveItems();

    /**
     * Cette fonction est de toute façons exécuté. Il est donc possible que les items soient vide si il y a eu une
     * exception.
     */
    public abstract void after();

    public abstract String getKey();

}
