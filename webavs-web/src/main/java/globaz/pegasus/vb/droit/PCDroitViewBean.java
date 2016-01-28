/**
 * 
 */
package globaz.pegasus.vb.droit;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.hera.business.exceptions.models.MembreFamilleException;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneesPersonnellesException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.checkers.droit.DroitChecker;

/**
 * @author ECO
 */
public class PCDroitViewBean extends BJadePersistentObjectViewBean {

    static final private List<String> etatsPretPourCalculer = new ArrayList<String>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            this.add(IPCDroits.CS_CALCULE);
            this.add(IPCDroits.CS_AU_CALCUL);
            this.add(IPCDroits.CS_ENREGISTRE);
        }
    };

    private String csMotif = null;
    private String dateAnnonce = null;

    private Droit droit = null;
    private String idDemande = null;

    private String idDroit = null;

    private String idVersionDroit = null;

    private String noVersion = null;

    /**
	 * 
	 */
    public PCDroitViewBean() {
        super();
        droit = new Droit();
    }

    /**
     * @param droit
     */
    public PCDroitViewBean(Droit droit) {
        super();
        this.droit = droit;
        csMotif = droit.getSimpleVersionDroit().getCsMotif();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     * 
     * crée un nouveau droit et une première version du droit
     */
    @Override
    public void add() throws Exception {
        Demande demande = PegasusServiceLocator.getDemandeService().read(idDemande);

        droit = PegasusServiceLocator.getDroitService().createDroitInitial(demande);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     * 
     * Supprime une version du droit. Ne supprime pas de droit.
     */
    @Override
    public void delete() throws Exception {
        droit = retrieveDroitParIdVersion();

        droit = PegasusServiceLocator.getDroitService().supprimerVersionDroit(droit);
    }

    public String getCsMotif() {
        return csMotif;
    }

    /**
     * @return the dateAnnonce
     */
    public String getDateAnnonce() {
        return dateAnnonce;
    }

    /**
     * @return the droit
     */
    public Droit getDroit() {
        return droit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return droit.getId();
    }

    /**
     * @return the idDemande
     */
    public String getIdDemande() {
        return idDemande;
    }

    /**
     * @return the idDroit
     */
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * @return the idVersionDroit
     */
    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    /**
     * @return the noVersion
     */
    public String getNoVersion() {
        return noVersion;
    }

    public boolean getReadyForCalcul() {
        return PCDroitViewBean.etatsPretPourCalculer.contains(droit.getSimpleVersionDroit().getCsEtatDroit());
    }

    public boolean getReadyForDecisionApresCalcul() {
        // Si etat demande sur en attente de justificatif ou de calcul
        if ((IPCDroits.CS_CALCULE.equals(droit.getSimpleVersionDroit().getCsEtatDroit()))
                || (IPCDroits.CS_COURANT_VALIDE.equals(droit.getSimpleVersionDroit().getCsEtatDroit()))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getReadyForDecisionSuppression() {

        if ((IPCDroits.CS_ENREGISTRE.equals(droit.getSimpleVersionDroit().getCsEtatDroit()) || IPCDroits.CS_AU_CALCUL
                .equals(droit.getSimpleVersionDroit().getCsEtatDroit()))
                && !droit.getSimpleVersionDroit().getNoVersion().equals("1")
                && !droit.getDemande().getSimpleDemande().getCsEtatDemande().equals(IPCDemandes.CS_SUPPRIME)) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return new BSpy(droit.getSpy());
    }

    public boolean isUpdatable() throws DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        return DroitChecker.isUpdatable(droit);
    }

    /**
     * Met à jour la version du droit d'après les modifications faites dans l'écran de la version du droit
     * 
     * @throws JadeApplicationServiceNotAvailableException
     *             en cas d'erreur de service
     * @throws DroitException
     *             en cas d'erreur lié au droit
     * @throws MembreFamilleException
     *             en cas d'erreur lié au membre de la famielle
     * @throws DonneesPersonnellesException
     *             en cas d'erreur lié aux données personnelles
     * @throws JadePersistenceException
     *             en cas d'erreur de persistence
     */
    public void modifierDateAnnonce() throws JadeApplicationServiceNotAvailableException, DroitException,
            MembreFamilleException, DonneesPersonnellesException, JadePersistenceException {

        droit = retrieveDroitParIdVersion();
        droit.getSimpleVersionDroit().setDateAnnonce(dateAnnonce);
        droit.getSimpleVersionDroit().setCsMotif(csMotif);
        droit = PegasusServiceLocator.getDroitService().updateDroit(droit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        // this.droit = PegasusServiceLocator.getDroitService().readDroit(
        // this.droit.getId());

        // load droit
        DroitSearch droitSearch = new DroitSearch();

        // note : vu que le selectedIf de la requete correspond ici au
        // idVersionDroit, venant de l'ecran des droits de la demande, et que le
        // viewbean met automatiquement le id dans le simpledroit, il faut
        // chercher uniquement sur le version droit. A terme, il faut
        // implémenter un beforeAfficher qui remplisse correctement les champs
        // du viewbean.
        droitSearch.setForIdVersionDroit(getId());
        droitSearch.setForIdDroit(idDroit);
        droitSearch = PegasusServiceLocator.getDroitService().searchDroit(droitSearch);
        if (droitSearch.getSize() == 1) {
            droit = (Droit) droitSearch.getSearchResults()[0];
        } else {
            throw new DroitException("can't retrieve droit. " + droitSearch.getSize()
                    + " element(s) was found for the same no version droit and id droit");
        }
    }

    public String getCssDroitDiffAdaptaion() {
        return (IPCDroits.CS_MOTIF_DROIT_REPRISE_ADAPTATION_ERRONE.equals(getCsMotif())) ? "errorAdaptation" : "";
    }

    /**
     * @return
     * @throws JadePersistenceException
     * @throws DroitException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private Droit retrieveDroitParIdVersion() throws JadePersistenceException, DroitException,
            JadeApplicationServiceNotAvailableException {
        DroitSearch droitSearch = new DroitSearch();
        droitSearch.setForIdVersionDroit(idVersionDroit);
        droitSearch.setDefinedSearchSize(1);
        droitSearch = PegasusServiceLocator.getDroitService().searchDroit(droitSearch);
        if (droitSearch.getSize() != 1) {
            throw new DroitException("Unable to find VersionDroit (id=" + idVersionDroit + ")!");
        }
        return (Droit) droitSearch.getSearchResults()[0];
    }

    public void setCsMotif(String csMotif) {
        this.csMotif = csMotif;
    }

    /**
     * @param dateAnnonce
     *            the dateAnnonce to set
     */
    public void setDateAnnonce(String dateAnnonce) {
        this.dateAnnonce = dateAnnonce;
    }

    /**
     * @param droit
     *            the droit to set
     */
    public void setDroit(Droit droit) {
        this.droit = droit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        droit.setId(newId);
    }

    /**
     * @param idDemande
     *            the idDemande to set
     */
    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /**
     * @param idDroit
     *            the idDroit to set
     */
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    /**
     * @param idVersionDroit
     *            the idVersionDroit to set
     */
    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    /**
     * @param noVersion
     *            the noVersion to set
     */
    public void setNoVersion(String noVersion) {
        this.noVersion = noVersion;
    }

    public void synchronizeMembresFamille() throws JadeApplicationServiceNotAvailableException, DroitException,
            MembreFamilleException, DonneesPersonnellesException, JadePersistenceException {
        DroitSearch search = new DroitSearch();
        search.setForIdDemandePc(idDemande);
        search.setDefinedSearchSize(1);
        search = PegasusServiceLocator.getDroitService().searchDroit(search);
        if (search.getSize() > 0) {
            droit = (Droit) search.getSearchResults()[0];
            droit = PegasusServiceLocator.getDroitService().synchroniseMembresFamille(droit);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        droit = retrieveDroitParIdVersion();

        droit = PegasusServiceLocator.getDroitService().corrigerDroit(droit, "", csMotif);
    }
}
