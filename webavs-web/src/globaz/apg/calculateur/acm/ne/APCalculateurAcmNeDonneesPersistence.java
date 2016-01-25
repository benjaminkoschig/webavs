package globaz.apg.calculateur.acm.ne;

import globaz.apg.module.calcul.APRepartitionPaiementData;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class APCalculateurAcmNeDonneesPersistence {

    private String dateDebut = "";
    private String dateFin = "";
    private String idDroit = "";
    private BigDecimal montantBrut = null;
    private BigDecimal montantJournalier = null;
    private BigDecimal montantNet = null;
    private int nombreDeJoursSoldes = 0;

    private List<APRepartitionPaiementData> repartitionsPaiementMap = new ArrayList<APRepartitionPaiementData>();

    public APCalculateurAcmNeDonneesPersistence(String dateDebut, String dateFin, int nombreDeJoursSoldes,
            String idDroit) {
        super();
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.nombreDeJoursSoldes = nombreDeJoursSoldes;
        this.idDroit = idDroit;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public BigDecimal getMontantBrut() {
        return montantBrut;
    }

    public BigDecimal getMontantJournalier() {
        return montantJournalier;
    }

    public BigDecimal getMontantNet() {
        return montantNet;
    }

    public int getNombreDeJoursSoldes() {
        return nombreDeJoursSoldes;
    }

    public List<APRepartitionPaiementData> getRepartitionsPaiementMap() {
        return repartitionsPaiementMap;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setMontantBrut(BigDecimal montantBrut) {
        this.montantBrut = montantBrut;
    }

    public void setMontantJournalier(BigDecimal montantJournalier) {
        this.montantJournalier = montantJournalier;
    }

    public void setMontantNet(BigDecimal montantNet) {
        this.montantNet = montantNet;
    }

    public void setNombreDeJoursSoldes(int nombreDeJoursSoldes) {
        this.nombreDeJoursSoldes = nombreDeJoursSoldes;
    }

    public void setRepartitionsPaiementMap(List<APRepartitionPaiementData> repartitionsPaiementMap) {
        this.repartitionsPaiementMap = repartitionsPaiementMap;
    }

}
