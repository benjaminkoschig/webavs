package globaz.apg.calculateur.complement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.prestation.APCotisation;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.apg.module.calcul.APSituationProfessionnelleCanton;
import globaz.apg.module.calcul.constantes.EMontantsMax;

public class APCalculateurComplementDonneeDomaine {
    
    private APPrestation prestation;
    private APDroitLAPG droit;
    
    private List <APRepartitionJointPrestation> repartitions= new ArrayList<>();

    private Map<String, APSituationProfessionnelleCanton> situationProfessionnelle = new HashMap<String, APSituationProfessionnelleCanton>();

    private Map<String, BigDecimal[]> taux = new HashMap<String, BigDecimal[]>();
    private Map<EMontantsMax, BigDecimal> montantsMax;

    private Map<APRepartitionJointPrestation,Map<String, APCotisation>> mapCotisation = new HashMap<>();
    
    public APCalculateurComplementDonneeDomaine(APPrestation prestation) {
        this.prestation = prestation;
    }
    
    public List<APRepartitionJointPrestation> getRepartitions() {
        return repartitions;
    }

    public Map<String, APSituationProfessionnelleCanton> getSituationProfessionnelle() {
        return situationProfessionnelle;
    }

    public Map<String, BigDecimal[]> getTaux() {
        return taux;
    }

    public void setRepartitions(List<APRepartitionJointPrestation> repartitions) {
        this.repartitions = repartitions;
    }

    public void setSituationProfessionnelle(Map<String, APSituationProfessionnelleCanton> situationProfessionnelle) {
        this.situationProfessionnelle = situationProfessionnelle;
    }

    public void setTaux(Map<String, BigDecimal[]> taux) {
        this.taux = taux;
    }
    
    public APPrestation getPrestation() {
        return prestation;
    }
    
    public void setPrestation(APPrestation prestation) {
        this.prestation = prestation;
    }
    
    public APDroitLAPG getDroit() {
        return droit;
    }
    
    public void setDroit(APDroitLAPG droit) {
        this.droit = droit;
    }

    public Map<EMontantsMax, BigDecimal> getMontantsMax() {
        return montantsMax;
    }

    public void setMontantsMax(Map<EMontantsMax, BigDecimal> montantsMax) {
        this.montantsMax = montantsMax;
    }

    public Map<APRepartitionJointPrestation, Map<String, APCotisation>> getMapCotisation() {
        return mapCotisation;
    }

    public void setMapCotisation (Map<APRepartitionJointPrestation,Map<String, APCotisation>> mapCotisation) {
        this.mapCotisation = mapCotisation;
    }
}
