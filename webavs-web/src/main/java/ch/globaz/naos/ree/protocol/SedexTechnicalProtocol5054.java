package ch.globaz.naos.ree.protocol;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SedexTechnicalProtocol5054 extends SedexTechnicalProtocol {

    /**
     * key is the year
     */
    private Map<Integer, Integer> lotByYear;

    public SedexTechnicalProtocol5054(Date debutTraitement, Date finTraitement, Map<Integer, Integer> lotByYear,
            List<File> files) {
        super(debutTraitement, finTraitement, 0, files);
        this.lotByYear = lotByYear;
    }

    /**
     * key is the year
     */
    public Map<Integer, Integer> getLotByYear() {
        return lotByYear;
    }

    @Override
    public int getNombreLot() {
        throw new RuntimeException("Don't use this method");
    }
}
