package ch.globaz.naos.ree.protocol;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Protocole technique concernant la configuration Sedex pour l'exécution du process
 * 
 * @author lga
 * 
 */
public class SedexTechnicalProtocol extends TechnicalProtocol {

    private int nombreLot;
    private List<File> files;

    public SedexTechnicalProtocol(Date debutTraitement, Date finTraitement, int nombreLot, List<File> files) {
        super(debutTraitement, finTraitement);
        this.nombreLot = nombreLot;
        this.files = files;
    }

    public int getNombreLot() {
        return nombreLot;
    }

    public List<File> getFiles() {
        return files;
    }
}
