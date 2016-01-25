package globaz.pegasus.vb.home;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Vector;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.exceptions.models.home.TypeChambreException;
import ch.globaz.pegasus.business.models.home.PrixChambre;
import ch.globaz.pegasus.business.models.home.TypeChambre;
import ch.globaz.pegasus.business.models.home.TypeChambreSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCPrixChambreViewBean extends BJadePersistentObjectViewBean {

    private PrixChambre prixChambre = null;
    private Vector typesChambres = null;

    public PCPrixChambreViewBean() {
        super();
        prixChambre = new PrixChambre();
    }

    public PCPrixChambreViewBean(PrixChambre prixChambre) {
        super();
        this.prixChambre = prixChambre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        prixChambre = PegasusServiceLocator.getHomeService().createPrixChambre(prixChambre);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        prixChambre = PegasusServiceLocator.getHomeService().deletePrixChambre(prixChambre);
    }

    /**
     * Donne la designation d'un type de chambre ("Designation" + si particularite "Nom, prénom")
     * 
     * @return la designation d'un type de chambre
     */
    public String getDesignationTypeChambre(BSession session) {

        return prixChambre.getTypeChambre().getDesignationTypeChambre();
    }

    @Override
    public String getId() {
        return prixChambre.getId();
    }

    /**
     * @return the idHome
     */
    public String getIdHome() {
        return prixChambre.getTypeChambre().getSimpleTypeChambre().getIdHome();
    }

    /**
     * Donne la période d'un prix de chambre
     * 
     * @return
     */
    public String getPeriode() {
        return prixChambre.getSimplePrixChambre().getDateDebut().trim() + " - "
                + prixChambre.getSimplePrixChambre().getDateFin().trim();
    }

    /**
     * @return the prixChambre
     */
    public PrixChambre getPrixChambre() {
        return prixChambre;
    }

    private BSession getSession() {
        return (BSession) getISession();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (prixChambre != null) && !prixChambre.isNew() ? new BSpy(prixChambre.getSpy()) : new BSpy(getSession());

    }

    /**
     * Donne Vector des {(idTypeChambre, descriptionTypeChambre),...} des type de chambre d'un home
     * 
     * @return
     * @throws TypeChambreException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public Vector getTypesChambresData() throws TypeChambreException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        return typesChambres;
    }

    /**
     * Initialisation de la liste des types de chambres
     * 
     * @throws TypeChambreException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws HomeException
     */
    public void init() throws TypeChambreException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, HomeException {
        retrieveTypesChambres();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        // this.prixChambre = PegasusServiceLocator.getHomeService().readPrixChambre(this.prixChambre.getId());
    }

    /**
     * Construit le Vector des {(idTypeChambre, descriptionTypeChambre),...} des type de chambre d'un home et
     * typeChambre
     * 
     * @param prixChambre
     * @throws TypeChambreException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws HomeException
     */
    private void retrieveTypesChambres() throws TypeChambreException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, HomeException {

        TypeChambreSearch typeChambreSearch = new TypeChambreSearch();
        typeChambreSearch.setForIdHome(getIdHome());
        typeChambreSearch.setForIdTypeChambre(prixChambre.getSimplePrixChambre().getIdTypeChambre());

        typeChambreSearch = PegasusServiceLocator.getHomeService().searchTypeChambre(typeChambreSearch);

        Vector typesChambres = new Vector(typeChambreSearch.getSize());

        for (int i = 0; i < typeChambreSearch.getSearchResults().length; i++) {
            TypeChambre typeChambre = (TypeChambre) typeChambreSearch.getSearchResults()[i];

            typesChambres.add(new String[] { typeChambre.getSimpleTypeChambre().getIdTypeChambre(),
                    typeChambre.getDesignationTypeChambre() });
        }

        this.typesChambres = typesChambres;
    }

    @Override
    public void setId(String newId) {
        prixChambre.setId(newId);
    }

    /**
     * @param idHome
     *            the idHome to set
     */
    public void setIdHome(String idHome) {
        prixChambre.getTypeChambre().getSimpleTypeChambre().setIdHome(idHome);
    }

    /**
     * @param prixChambre
     *            the prixChambre to set
     */
    public void setPrixChambre(PrixChambre prixChambre) {
        this.prixChambre = prixChambre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        // this.prixChambre = PegasusServiceLocator.getHomeService().updatePrixChambre(this.prixChambre);
    }

}
