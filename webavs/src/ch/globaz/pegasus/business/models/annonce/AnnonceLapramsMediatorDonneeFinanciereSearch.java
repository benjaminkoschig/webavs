package ch.globaz.pegasus.business.models.annonce;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pyxis.business.service.AdresseService;

public class AnnonceLapramsMediatorDonneeFinanciereSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsTypeDomicileHome = AdresseService.CS_TYPE_DOMICILE; // recherche les homes par adresse de
                                                                            // domicile
    private String forIdAnnonce = null;

    private List<String> forInIdDroitMbrFam = new ArrayList<String>();

    public String getForCsTypeDomicileHome() {
        return forCsTypeDomicileHome;
    }

    public String getForIdAnnonce() {
        return forIdAnnonce;
    }

    public List<String> getForInIdDroitMbrFam() {
        return forInIdDroitMbrFam;
    }

    public void setForCsTypeDomicileHome(String forCsTypeDomicileHome) {
        this.forCsTypeDomicileHome = forCsTypeDomicileHome;
    }

    public void setForIdAnnonce(String forIdAnnonce) {
        this.forIdAnnonce = forIdAnnonce;
    }

    public void setForInIdDroitMbrFam(List<String> forInIdDroitMbrFam) {
        this.forInIdDroitMbrFam = forInIdDroitMbrFam;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<AnnonceLapramsMediatorDonneeFinanciere> whichModelClass() {
        return AnnonceLapramsMediatorDonneeFinanciere.class;
    }

}
