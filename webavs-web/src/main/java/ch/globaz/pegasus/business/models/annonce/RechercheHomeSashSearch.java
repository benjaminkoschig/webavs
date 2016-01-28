package ch.globaz.pegasus.business.models.annonce;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCHomes;

public class RechercheHomeSashSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateDebut = null;
    private String forDateFin = null;
    private String forIdDroit = null;
    private String forIdDroitMemembreFamille = null;
    private String forIdVersionDroit = null;
    private String forNoVersionDroit = null;

    private List<String> forInCsServiceEtat = new ArrayList<String>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            this.add(IPCHomes.CS_SERVICE_ETAT_SASH);
            this.add(IPCHomes.CS_SERVICE_ETAT_SPAS);
        }
    };

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public String getForIdDroitMemembreFamille() {
        return forIdDroitMemembreFamille;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public List<String> getForInCsServiceEtat() {
        return forInCsServiceEtat;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForIdDroitMemembreFamille(String forIdDroitMemembreFamille) {
        this.forIdDroitMemembreFamille = forIdDroitMemembreFamille;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    public void setForInCsServiceEtat(List<String> forInCsServiceEtat) {
        this.forInCsServiceEtat = forInCsServiceEtat;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<RechercheHomeSash> whichModelClass() {
        return RechercheHomeSash.class;
    }

    public String getForNoVersionDroit() {
        return forNoVersionDroit;
    }

    public void setForNoVersionDroit(String forNoVersionDroit) {
        this.forNoVersionDroit = forNoVersionDroit;
    }

}
