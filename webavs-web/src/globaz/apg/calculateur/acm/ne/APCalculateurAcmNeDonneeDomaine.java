package globaz.apg.calculateur.acm.ne;

import globaz.apg.module.calcul.APSituationProfessionnelleData;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class APCalculateurAcmNeDonneeDomaine {

    private String dateDebut = "";
    private String dateFin = "";
    private String idDroit = "";
    private int nombreDeJoursSoldes = 0;

    // Map<idSituationProfessionnelle, montant brut repartition>
    private Map<String, BigDecimal> repartitions = new HashMap<String, BigDecimal>();

    private Map<String, APSituationProfessionnelleData> situationProfessionnelle = new HashMap<String, APSituationProfessionnelleData>();

    // Map<idSituationProfessionnelle, {taux AVS par, taux AC par,taux FNE par}>
    private Map<String, BigDecimal[]> taux = new HashMap<String, BigDecimal[]>();

    public APCalculateurAcmNeDonneeDomaine(String dateDebut, String dateFin, int nombreDeJoursSoldes, String idDroit,
            Map<String, BigDecimal[]> taux) {
        super();
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.nombreDeJoursSoldes = nombreDeJoursSoldes;
        this.idDroit = idDroit;
        this.taux = taux;
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

    public int getNombreDeJoursSoldes() {
        return nombreDeJoursSoldes;
    }

    public Map<String, BigDecimal> getRepartitions() {
        return repartitions;
    }

    public Map<String, APSituationProfessionnelleData> getSituationProfessionnelle() {
        return situationProfessionnelle;
    }

    public Map<String, BigDecimal[]> getTaux() {
        return taux;
    }

    public void setDateDebut(String dateDeDebut) {
        dateDebut = dateDeDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setNombreDeJoursSoldes(int nombreDeJoursSoldes) {
        this.nombreDeJoursSoldes = nombreDeJoursSoldes;
    }

    public void setRepartitions(Map<String, BigDecimal> repartitions) {
        this.repartitions = repartitions;
    }

    public void setSituationProfessionnelle(Map<String, APSituationProfessionnelleData> situationProfessionnelle) {
        this.situationProfessionnelle = situationProfessionnelle;
    }

    public void setTaux(Map<String, BigDecimal[]> taux) {
        this.taux = taux;
    }

}
