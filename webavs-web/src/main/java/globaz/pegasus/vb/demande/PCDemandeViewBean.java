package globaz.pegasus.vb.demande;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.interfaces.fx.PRGestionnaireHelper;
import java.util.Arrays;
import java.util.Vector;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.demande.ListDemandes;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCDemandeViewBean extends BJadePersistentObjectViewBean {

    private static String[] listeIdEtatTransferable = new String[] { IPCDemandes.CS_EN_ATTENTE_CALCUL,
            IPCDemandes.CS_EN_ATTENTE_JUSTIFICATIFS, IPCDemandes.CS_TRANSFERE };

    private ListDemandes listDemandes = null;
    private final Droit droit;

    public PCDemandeViewBean() {
        super();
        listDemandes = new ListDemandes();
        droit = new Droit();
    }

    public PCDemandeViewBean(ListDemandes listDemandes, Droit droit) {
        super();
        this.listDemandes = listDemandes;
        if (droit != null) {
            this.droit = droit;
        } else {
            this.droit = new Droit();
        }
    }

    @Override
    public void add() throws Exception {
        System.out.println();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
    }

    public ListDemandes getDemande() {
        return listDemandes;

    }

    /**
     * Donne le détail du gestionnaire (visa - prénom nom) ou (visa) si une erreure se produit lors de la recherche du
     * gestionnaire
     * 
     * @return
     */
    public String getDetailGestionnaire() {
        try {
            return PRGestionnaireHelper.getDetailGestionnaire(getSession(), getIdGestionnaire());
        } catch (Exception e) {
            return getIdGestionnaire() + " - " + getSession().getLabel("JSP_PC_GESTIONNAIRE_VISA_INCONNU");
        }
    }

    @Override
    public String getId() {
        return listDemandes.getId();
    }

    public String getIdDecision() {
        if (listDemandes.getSimpleDecisionRefus() != null) {
            return listDemandes.getSimpleDecisionRefus().getIdDecisionHeader();
        } else {
            return "0";
        }
    }

    public String getIdGestionnaire() {
        return getDemande().getSimpleDemande().getIdGestionnaire();
    }

    /**
     * Utilise pour les parametres du menu
     * 
     * @return
     */
    public String getIsFratrie() {
        if (getDemande().getSimpleDemande().getIsFratrie()) {
            return "1";
        } else {
            return "0";
        }
    }

    /**
     * @return the demande
     */
    public ListDemandes getListDemandes() {
        return listDemandes;
    }

    /**
     * 
     * @return
     */
    public Vector<String> getOrderByData() {
        return new Vector<String>();
    }

    /**
     * Donne un String representant la periode de validite de la demande
     * 
     * @return
     */
    public String getPeriode() {
        return listDemandes.getSimpleDemande().getDateDebut() + " - " + listDemandes.getSimpleDemande().getDateFin();
    }

    private BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    /**
     * Returne true si une decision de refus existe (isDecision different de zero )
     * 
     * @return
     */
    public boolean hasDecisionDerefus() {
        // if (IPCDemandes.CS_REFUSE.equals(this.listDemandes.getSimpleDemande().getCsEtatDemande())) {
        // return true;
        // } else {
        // return false;
        // }
        return !JadeStringUtil.isBlankOrZero(getIdDecision());
    }

    public boolean isButtonNewEnable(String idDossier) {
        // TODO récupérer l'information sur un service
        return true;
    }

    /**
     * Return true si la demande est transférable (demande dans les etats EN_ATTENTE_CALCUL ou EN_ATTENTE_JUSTIFICATIF)
     * 
     * @return
     */
    public boolean isTransferable() {
        return Arrays.asList(PCDemandeViewBean.listeIdEtatTransferable).contains(
                listDemandes.getSimpleDemande().getCsEtatDemande());
    }

    /**
     * Retourne true si une décsion de refus peut être préparée (demande dans les etats EN_ATTENTE_CALCUL ou
     * EN_ATTENTE_JUSTIFICATIF) et pas de décisions de refus deja creee
     * 
     * @return
     */
    public boolean isValidForPrepDecisionRefus() throws DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        return (IPCDemandes.CS_EN_ATTENTE_CALCUL.equals(listDemandes.getSimpleDemande().getCsEtatDemande()) || IPCDemandes.CS_EN_ATTENTE_JUSTIFICATIFS
                .equals(listDemandes.getSimpleDemande().getCsEtatDemande())) && !hasDecisionDerefus();

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {

        listDemandes = PegasusServiceLocator.getDemandeService().readDemande(listDemandes.getId());
    }

    /**
     * @param dateArrivee
     *            the dateArrivee to set
     */
    public void setDateArrivee(String dateArrivee) {
        listDemandes.getSimpleDemande().setDateArrivee(dateArrivee);
    }

    /**
     * @param dateDepot
     *            the dateDepot to set
     */
    public void setDateDepot(String dateDepot) {
        listDemandes.getSimpleDemande().setDateDepot(dateDepot);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        listDemandes.setId(newId);
    }

    /**
     * @param demande
     *            the demande to set
     */
    public void setListDemandes(ListDemandes listDemandes) {
        this.listDemandes = listDemandes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        // this.demande = PegasusServiceLocator.getDemandeService().update(this.demande);
    }

    public String getNumeroVersionDroit() {
        if (droit.isNew()) {
            return "1";
        }
        return droit.getSimpleVersionDroit().getNoVersion();
    }

    public Droit getDroit() {
        return droit;
    }

}
