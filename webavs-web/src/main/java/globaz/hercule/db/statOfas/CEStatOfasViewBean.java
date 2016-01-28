package globaz.hercule.db.statOfas;

import globaz.hercule.db.CEAbstractViewBean;

/**
 * Bean pour l'écran de lancement des statistiques pour l'OFAS
 * 
 * @author bjo
 * 
 */
public class CEStatOfasViewBean extends CEAbstractViewBean {

    private String annee;

    public CEStatOfasViewBean() {
        super();
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }
}
