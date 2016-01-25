package globaz.pegasus.vb.dossier;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.PCUserHelper;
import globaz.prestation.interfaces.fx.PRGestionnaireHelper;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.models.demande.DemandeSearch;
import ch.globaz.pegasus.business.models.dossier.Dossier;
import ch.globaz.pegasus.business.models.dossier.DossierRCList;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class PCDossierViewBean extends BJadePersistentObjectViewBean {
    private Dossier dossier = null;
    private DossierRCList dossierRcList = null;
    private boolean hasDemande = true;

    public PCDossierViewBean() {
        super();
        dossierRcList = new DossierRCList();
        dossier = new Dossier();
    }

    public PCDossierViewBean(Dossier dossier) {
        super();
        this.dossier = dossier;
        dossierRcList = new DossierRCList();
    }

    public PCDossierViewBean(DossierRCList dossier) {
        super();
        dossierRcList = dossier;

        this.dossier = new Dossier();
        this.dossier.setDemandePrestation(dossier.getDemandePrestation());
        this.dossier.setDossier(dossier.getSimpleDossier());

    }

    @Override
    public void add() throws Exception {
        dossier = PegasusServiceLocator.getDossierService().create(dossier);

    }

    @Override
    public void delete() throws Exception {

        dossier = PegasusServiceLocator.getDossierService().delete(dossier);
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les listes
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailAssure() throws Exception {

        return PCUserHelper.getDetailAssure(getSession(), dossier.getDemandePrestation().getPersonneEtendue());

    }

    /**
     * Donne le détail du gestionnaire (visa - prénom nom) ou (visa) si une erreure se produit lors de la recherche du
     * gestionnaire
     * 
     * @return
     */
    public String getDetailGestionnaire() {
        try {
            return PRGestionnaireHelper.getDetailGestionnaire(getSession(), dossier.getDossier().getIdGestionnaire());
        } catch (Exception e) {
            return dossier.getDossier().getIdGestionnaire() + " - "
                    + getSession().getLabel("JSP_PC_GESTIONNAIRE_VISA_INCONNU");
        }
    }

    /**
     * @return the dossier
     */
    public Dossier getDossier() {
        return dossier;
    }

    @Override
    public String getId() {
        return dossier.getId();
    }

    public String getIdTiersRequerant() {
        return dossier.getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers();
    }

    /**
     * Donne l'image qui correspond a l'etat general d'un dossier PC
     * 
     * @return
     */
    public String getImageEtatDemande() {
        String img = null;
        String cs_etat = dossierRcList.getSimpleDemande().getCsEtatDemande();
        if ((IPCDemandes.CS_EN_ATTENTE_JUSTIFICATIFS.equals(cs_etat))
                || (IPCDemandes.CS_EN_ATTENTE_CALCUL.equals(cs_etat)) || JadeStringUtil.isEmpty(cs_etat)) {
            img = "small_warning.png";
        }

        if (IPCDemandes.CS_OCTROYE.equals(cs_etat)) {
            img = "small_good.png";
        }
        if ((IPCDemandes.CS_RENONCE.equals(cs_etat)) || (IPCDemandes.CS_SUPPRIME.equals(cs_etat))
                || (IPCDemandes.CS_TRANSFERE.equals(cs_etat)) || (IPCDemandes.CS_REFUSE.equals(cs_etat))
                || IPCDemandes.CS_REOUVERT.equals(cs_etat)) {
            img = "small_error.png";
        }
        // il faut tenir compt de la révision
        if (IPCDemandes.CS_REVISION.equals(cs_etat)) {
            img = "small_info.png";
        }

        return img;
    }

    /**
     * Donne l'infobulle qui correspond a l'etat general d'un dossier PC
     * 
     * @return
     */
    public String getInfobulleEtatDemande() {
        String infobulle = "";
        String cs_etat = dossierRcList.getSimpleDemande().getCsEtatDemande();
        if ((IPCDemandes.CS_EN_ATTENTE_JUSTIFICATIFS.equals(cs_etat))
                || (IPCDemandes.CS_EN_ATTENTE_CALCUL.equals(cs_etat)) || JadeStringUtil.isEmpty(cs_etat)) {
            infobulle = getSession().getLabel("JSP_PC_DOS_L_ETAT_GEN_PREMIERE_INSTRUCTION");
        }

        if (IPCDemandes.CS_OCTROYE.equals(cs_etat)) {
            infobulle = getSession().getLabel("JSP_PC_DOS_L_ETAT_GEN_OCTROI");
        }
        if (IPCDemandes.CS_REFUSE.equals(cs_etat)) {
            infobulle = getSession().getLabel("JSP_PC_DOS_L_ETAT_GEN_REFUS");
        }
        if (IPCDemandes.CS_SUPPRIME.equals(cs_etat)) {
            infobulle = getSession().getLabel("JSP_PC_DOS_L_ETAT_GEN_SUPPRESSION");
        }
        if (IPCDemandes.CS_RENONCE.equals(cs_etat)) {
            infobulle = getSession().getLabel("JSP_PC_DOS_L_ETAT_GEN_RENONCEMENT");
        }
        if (IPCDemandes.CS_TRANSFERE.equals(cs_etat)) {
            infobulle = getSession().getLabel("JSP_PC_DOS_L_ETAT_GEN_TRANSFERT");
        }
        if (IPCDemandes.CS_REOUVERT.equals(cs_etat)) {
            infobulle = getSession().getLabel("JSP_PC_DOS_L_ETAT_GEN_REOUVERT");
        }

        // il faut tenir compt de la révision
        if (IPCDemandes.CS_REVISION.equals(cs_etat)) {
            infobulle = getSession().getLabel("JSP_PC_DOS_L_ETAT_GEN_EN_REVISION");
        }
        return infobulle;
    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est dans le vb
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public String getLibellePays(PersonneEtendueComplexModel personne) {
        BSession session = getSession();

        if ("999".equals(session.getCode(session.getSystemCode("CIPAYORI", personne.getTiers().getIdPays())))) {
            return "";
        } else {
            return session.getCodeLibelle(session.getSystemCode("CIPAYORI", personne.getTiers().getIdPays()));
        }

    }

    public String getNoAvs() {
        return dossier.getDemandePrestation().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();
    }

    /**
     * Méthode qui retourne le NNSS formaté sans le préfixe (756.) ou alors le NSS normal
     * 
     * @return NNSS formaté sans préfixe ou NSS normal
     */
    public String getNumeroAvsFormateSansPrefixe() {
        return PCUserHelper.getNumeroAvsFormateSansPrefixe(dossier.getDemandePrestation().getPersonneEtendue()
                .getPersonneEtendue());
    }

    private BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    /**
     * @return the hasDemande
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DemandeException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DossierException
     */
    public boolean isHasDemande() throws DemandeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        if (!JadeStringUtil.isEmpty(dossier.getId())) {
            DemandeSearch search = new DemandeSearch();
            search.setForIdDossier(getId());
            hasDemande = PegasusServiceLocator.getDemandeService().count(search) > 0;
        } else {
            hasDemande = false;
        }

        return hasDemande;
    }

    @Override
    public void retrieve() throws Exception {
        dossier = PegasusServiceLocator.getDossierService().read(dossier.getId());
    }

    /**
     * @param dossier
     *            the dossier to set
     */
    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    /**
     * @param hasDemande
     *            the hasDemande to set
     */
    public void setHasDemande(boolean hasDemande) {
        this.hasDemande = hasDemande;
    }

    @Override
    public void setId(String newId) {
        dossier.setId(newId);
    }

    @Override
    public void update() throws Exception {
        dossier = PegasusServiceLocator.getDossierService().update(dossier);
    }
}
