package globaz.amal.vb.caissemaladie;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladie;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.pyxis.business.model.AdministrationAdresseComplexModel;
import ch.globaz.pyxis.business.model.AdministrationAdresseSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class AMCaissemaladieViewBean extends BJadePersistentObjectViewBean {
    private AdministrationAdresseComplexModel adresseComplexModel = null;
    private CaisseMaladie caisseMaladie = null;

    public AMCaissemaladieViewBean() {
        super();
        setCaisseMaladie(new CaisseMaladie());
    }

    /**
     * Constructor called from rcListe
     * 
     * @param job
     */
    public AMCaissemaladieViewBean(CaisseMaladie caisseMaladie) {
        this();
        setCaisseMaladie(caisseMaladie);
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public AdministrationAdresseComplexModel getAdresse() throws JadePersistenceException {
        if (adresseComplexModel == null) {
            AdministrationAdresseSearchComplexModel adminAdresseSearch = new AdministrationAdresseSearchComplexModel();
            adminAdresseSearch.setForIdTiersAdministration(getCaisseMaladie().getIdTiersCaisse());
            try {
                adminAdresseSearch = TIBusinessServiceLocator.getAdministrationService().findAdministrationAdresse(
                        adminAdresseSearch);
                if (adminAdresseSearch.getSize() > 0) {
                    AdministrationAdresseComplexModel currentAdminAdr = (AdministrationAdresseComplexModel) adminAdresseSearch
                            .getSearchResults()[0];
                    adresseComplexModel = currentAdminAdr;
                    return adresseComplexModel;
                }

            } catch (JadeApplicationException e) {
                JadeLogger.error(this, "Error loading adresse for administration "
                        + getCaisseMaladie().getIdTiersCaisse());
            }
            return null;
        }

        return adresseComplexModel;
        // AdresseSearch adresseSearch = new AdresseSearch();
        // adresseSearch.setForIdTier(this.getCaisseMaladie().getIdTiersCaisse());
        // try {
        // adresseSearch = TIBusinessServiceLocator.getAdresseService().searchAdresseWithSimpleTiers(adresseSearch);
        // if (adresseSearch.getSize()>0) {
        // Adresse adresse = (Adresse) adresseSearch.getSearchResults()[0];
        // adresse.getAdresse().get
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
    }

    public CaisseMaladie getCaisseMaladie() {
        return caisseMaladie;
    }

    @Override
    public String getId() {
        return caisseMaladie.getId();
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(caisseMaladie.getSpy());
    }

    @Override
    public void retrieve() throws Exception {
        caisseMaladie = AmalServiceLocator.getCaisseMaladieService().read(getId());
    }

    public void setCaisseMaladie(CaisseMaladie caisseMaladie) {
        this.caisseMaladie = caisseMaladie;
    }

    @Override
    public void setId(String newId) {
        caisseMaladie.setId(newId);
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub
    }

}
