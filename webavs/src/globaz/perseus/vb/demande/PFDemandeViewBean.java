/**
 * 
 */
package globaz.perseus.vb.demande;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.perseus.utils.PFAgenceCommunaleHelper;
import java.util.Vector;
import ch.globaz.perseus.business.constantes.CSEtatDemande;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.models.situationfamille.EnfantFamilleAddCheckMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author DDE
 * 
 */
public class PFDemandeViewBean extends BJadePersistentObjectViewBean {

    private Vector agencesList = null;
    private String calculParticulier;
    private String casRigueur;
    private String coaching;
    private Demande demande;
    private String fromRI;
    private boolean isAnnulable = false;
    private String permisB;
    private String refusForce;
    private String nonEntreeEnMatiere;
    private Vector riList = null;
    EnfantFamilleAddCheckMessage enfantFamilleWarnMessages;

    public void setWarnCopieFamilleMessages(EnfantFamilleAddCheckMessage enfantFamilleWarnMessages) {
        this.enfantFamilleWarnMessages = enfantFamilleWarnMessages;
    }

    public String getEnfantFamilleWarnMessages() {
        if (enfantFamilleWarnMessages != null) {
            return enfantFamilleWarnMessages.getMessage();
        } else {
            return "";
        }

    }

    public String toJson() {
        GsonBuilder gsb = new GsonBuilder();

        Gson gson = gsb.create();

        return gson.toJson(demande).toString();

    }

    public PFDemandeViewBean() {
        super();
        demande = new Demande();
    }

    public PFDemandeViewBean(Demande demande) {
        super();
        this.demande = demande;
    }

    public String getNonEntreeEnMatiere() {
        return nonEntreeEnMatiere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        demande.getSimpleDemande().setFromRI("on".equals(fromRI));
        demande.getSimpleDemande().setCasRigueur("on".equals(casRigueur));
        demande.getSimpleDemande().setCalculParticulier("on".equals(calculParticulier));
        demande.getSimpleDemande().setRefusForce("on".equals(refusForce));
        demande.getSimpleDemande().setPermisB("on".equals(permisB));
        demande.getSimpleDemande().setCoaching("on".equals(getCoaching()));
        demande.getSimpleDemande().setNonEntreeEnMatiere("on".equals(nonEntreeEnMatiere));
        PerseusServiceLocator.getDemandeService().create(demande);
    }

    public void setNonEntreeEnMatiere(String nonEntreeEnMatiere) {
        this.nonEntreeEnMatiere = nonEntreeEnMatiere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        PerseusServiceLocator.getDemandeService().delete(demande);
    }

    /**
     * @return the agencesList
     */
    public Vector getAgencesList() {
        return agencesList;
    }

    public String getCalculParticulier() {
        return calculParticulier;
    }

    /**
     * @return the casRigueur
     */
    public String getCasRigueur() {
        return casRigueur;
    }

    public String getCoaching() {
        return coaching;
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebutConverter() {
        return (!JadeStringUtil.isEmpty(demande.getSimpleDemande().getDateDebut())) ? demande.getSimpleDemande()
                .getDateDebut().substring(3) : "";
    }

    /**
     * @return the dateFin
     */
    public String getDateFinConverter() {
        return (!JadeStringUtil.isEmpty(demande.getSimpleDemande().getDateFin())) ? demande.getSimpleDemande()
                .getDateFin().substring(3) : "";
    }

    /**
     * @return the demande
     */
    public Demande getDemande() {
        return demande;
    }

    /**
     * @return the fromRI
     */
    public String getFromRI() {
        return fromRI;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return demande.getId();
    }

    /**
     * @return the permisB
     */
    public String getPermisB() {
        return permisB;
    }

    public String getRefusForce() {
        return refusForce;
    }

    /**
     * @return the riList
     */
    public Vector getRiList() {
        return riList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return new BSpy(demande.getSpy());
    }

    public void init() throws Exception {
        setAgencesList(PFAgenceCommunaleHelper.getAgencesList());
        setRiList(PFAgenceCommunaleHelper.getRiList());
    }

    /**
     * @return the isAnnulable
     */
    public boolean isAnnulable() {
        return isAnnulable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        init();
        demande = PerseusServiceLocator.getDemandeService().read(getId());

        if (CSEtatDemande.VALIDE.getCodeSystem().equals(demande.getSimpleDemande().getCsEtatDemande())
                && JadeDateUtil.areDatesEquals(demande.getSimpleDemande().getDateDebut(), "01."
                        + PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt())) {
            isAnnulable = true;
        } else {
            isAnnulable = false;
        }

        getISession()
                .setAttribute(
                        "likeNss",
                        demande.getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                                .getNumAvsActuel());
    }

    /**
     * @param agencesList
     *            the agencesList to set
     */
    public void setAgencesList(Vector agencesList) {
        this.agencesList = agencesList;
    }

    /**
     * @param isAnnulable
     *            the isAnnulable to set
     */
    public void setAnnulable(boolean isAnnulable) {
        this.isAnnulable = isAnnulable;
    }

    public void setCalculParticulier(String calculParticulier) {
        this.calculParticulier = calculParticulier;
    }

    /**
     * @param casRigueur
     *            the casRigueur to set
     */
    public void setCasRigueur(String casRigueur) {
        this.casRigueur = casRigueur;
    }

    public void setCoaching(String coaching) {
        this.coaching = coaching;
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebutConverter(String dateDebut) {
        if (!JadeStringUtil.isEmpty(dateDebut)) {
            if (JadeDateUtil.isGlobazDateMonthYear(dateDebut)) {
                dateDebut = "01." + dateDebut;
                demande.getSimpleDemande().setDateDebut(dateDebut);
            } else {
                demande.getSimpleDemande().setDateDebut(dateDebut);
            }
        } else {
            demande.getSimpleDemande().setDateDebut(null);
        }
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFinConverter(String dateFin) {
        if (!JadeStringUtil.isEmpty(dateFin)) {
            if (JadeDateUtil.isGlobazDateMonthYear(dateFin)) {
                dateFin = "01." + dateFin;
                dateFin = JadeDateUtil.addMonths(dateFin, 1);
                dateFin = JadeDateUtil.addDays(dateFin, -1);
                demande.getSimpleDemande().setDateFin(dateFin);
            } else {
                demande.getSimpleDemande().setDateFin(dateFin);
            }
        } else {
            demande.getSimpleDemande().setDateFin(null);
        }
    }

    /**
     * @param demande
     *            the demande to set
     */
    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    /**
     * @param fromRI
     *            the fromRI to set
     */
    public void setFromRI(String fromRI) {
        this.fromRI = fromRI;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        demande.setId(newId);
    }

    /**
     * @param permisB
     *            the permisB to set
     */
    public void setPermisB(String permisB) {
        this.permisB = permisB;
    }

    public void setRefusForce(String refusForce) {
        this.refusForce = refusForce;
    }

    /**
     * @param riList
     *            the riList to set
     */
    public void setRiList(Vector riList) {
        this.riList = riList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        // Pour résoudre le problème des checkbox pas envoyées lorsqu'elles sont pas checkés
        demande.getSimpleDemande().setFromRI("on".equals(fromRI));
        demande.getSimpleDemande().setCasRigueur("on".equals(casRigueur));
        demande.getSimpleDemande().setCalculParticulier("on".equals(calculParticulier));
        demande.getSimpleDemande().setRefusForce("on".equals(refusForce));
        demande.getSimpleDemande().setPermisB("on".equals(permisB));
        demande.getSimpleDemande().setCoaching("on".equals(getCoaching()));
        demande.getSimpleDemande().setNonEntreeEnMatiere("on".equals(nonEntreeEnMatiere));

        // HACK:Pour la comparaison des demandes plus tard puisque le null en base de donnée est 0
        if (JadeStringUtil.isEmpty(demande.getSimpleDemande().getIdAgenceRi())) {
            demande.getSimpleDemande().setIdAgenceRi("0");
        }
        if (CSEtatDemande.VALIDE.getCodeSystem().equals(demande.getSimpleDemande().getCsEtatDemande())) {
            PerseusServiceLocator.getDemandeService().update(demande, false, true);
        } else {
            PerseusServiceLocator.getDemandeService().updateAndClean(demande, true);
        }
    }

}
