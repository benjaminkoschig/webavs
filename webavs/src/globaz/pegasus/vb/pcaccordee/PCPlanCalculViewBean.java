/**
 * 
 */
package globaz.pegasus.vb.pcaccordee;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRStringUtils;
import java.util.ArrayList;
import java.util.Collection;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.process.StatistiquesOFASException;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.home.Home;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;
import ch.globaz.pegasus.business.models.process.statistiquesofas.PlanCalculeDemandeDroitMembreFamille;
import ch.globaz.pegasus.business.models.process.statistiquesofas.PlanCalculeDemandeDroitMembreFamilleSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PegasusDateUtil;
import ch.globaz.pegasus.businessimpl.utils.calcul.IPeriodePCAccordee;
import ch.globaz.pegasus.businessimpl.utils.calcul.PegasusCalculUtil;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;
import ch.globaz.pegasus.businessimpl.utils.home.HomeUtil;
import ch.globaz.pegasus.businessimpl.utils.plancalcul.PCPlanCalculHandler;

/**
 * @author SCE
 * 
 *         19 oct. 2010
 */
public class PCPlanCalculViewBean extends BJadePersistentObjectViewBean {

    private String conjointInfos = null;
    private String dateval = null;
    private ArrayList<String> enfantsCompris = null;
    private String idBeneficiaire = null;
    private String idPca = null;
    private String idPcal = null;
    private String monnaie = "CHF";
    private String nssInfos = null;
    private SimplePCAccordee pcAccordee = null;
    private String periodeValidite = null;
    private PCPlanCalculHandler planCalculHandler = null;
    private SimplePlanDeCalcul planDeCalcul = null;
    private String requerantInfos = null;
    private TupleDonneeRapport tupleRoot = null;
    private Home home;
    private String designationHome = null;

    public PCPlanCalculViewBean() {

    }

    @Override
    public void add() throws Exception {
    }

    private void buildBeneficiaireString(String nom, String prenom, String nss, String naiss) {
        requerantInfos = nom + " " + prenom + " - " + naiss;
        nssInfos = nss;
    }

    private void buildConjointString(String nom, String prenom, String nss, String naiss) {
        conjointInfos = nom + " " + prenom + " - " + naiss;
    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public String getConjointInfos() {
        return conjointInfos != null ? conjointInfos : "";
    }

    public String getDateval() {
        return dateval;
    }

    public ArrayList<String> getEnfantsCompris() {
        return enfantsCompris;

    }

    @Override
    public String getId() {
        return null;
    }

    public String getIdBeneficiaire() {
        return idBeneficiaire;
    }

    public String getIdPca() {
        return idPca;
    }

    /**
     * @return the idPcal
     */
    public String getIdPcal() {
        return idPcal;
    }

    public String getLibelleVersementInfo(String membreRole) {

        String returnCh = BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PC_PLANCALCUL_D_VERSEMENT_POUR");

        if (membreRole.equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {
            return returnCh + " " + requerantInfos.split("-")[0];
        } else {
            return returnCh + " " + conjointInfos.split("-")[0];
        }
    }

    /**
     * @return the monnaie
     */
    public String getMonnaie() {
        return monnaie;
    }

    public String getMontantVersement(String membreRole) {
        if (membreRole.equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {
            return tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_TOTAL_DEDUIT_MENSUEL_REQ).toString();
        } else {
            return tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_TOTAL_DEDUIT_MENSUEL_CONJOINT).toString();
        }

    }

    public String getNssInfos() {
        return nssInfos;
    }

    public SimplePCAccordee getPcAccordee() {
        return pcAccordee;
    }

    /**
     * @return the periodeValidite
     */
    public String getPeriodeValidite() {
        return periodeValidite;
    }

    /**
     * @return the planCalcul
     */
    public PCPlanCalculHandler getPlanCalcul() {
        return planCalculHandler;
    }

    /**
     * @return the planDeCalcul
     */
    public SimplePlanDeCalcul getPlanDeCalcul() {
        return planDeCalcul;
    }

    public String getRequerantInfos() {
        return requerantInfos;
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    /**
     * @return the tupleRoot
     */
    public TupleDonneeRapport getTupleRoot() {
        return tupleRoot;
    }

    public String getTupleRootXML() {

        byte[] resultatCalcul = planDeCalcul.getResultatCalcul();
        return new String(resultatCalcul);
    }

    public String getValiditeInfos() {
        String debut = "01." + getPcAccordee().getDateDebut();
        String fin = getPcAccordee().getDateFin();

        String retour = "";
        if (JadeStringUtil.isEmpty(fin)) {
            // Chargement texte
            String chaine = BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PCACCORDE_D_VALABLE_DESLE");

            retour = chaine + " " + debut;
        } else {
            int month = Integer.parseInt(fin.substring(0, 2));
            int year = Integer.parseInt(fin.substring(3));
            int jour = PegasusDateUtil.getLastDayOfMonth(month - 1, year);
            String jourStr = "" + jour;
            String moiStr = "" + month;

            if (jour < 10) {
                jourStr = "0" + jour;
            }
            if (month < 10) {
                moiStr = "0" + month;
            }

            fin = jourStr + "." + moiStr + "." + year;

            // Chargement texte
            String chaine = BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PCACCORDE_D_VALABLE_DU_AU");
            chaine = PRStringUtils.replaceString(chaine, "{datedebut}", debut);
            retour = chaine + " " + fin;
        }
        return retour;
    }

    public Boolean hasDoubleVersement() throws CalculException {

        return PegasusCalculUtil.isRentesPrincipalesCoupleADom(tupleRoot)
                && (Float.parseFloat(planDeCalcul.getMontantPCMensuelle()) > 0.0f);
    }

    /**
     * Construit une Collection des membres de familles retenu dans le calcul courant.
     * 
     * @param search
     *            PCAIdMembreFamilleRetenuSearch
     * @return
     */
    private Collection<String> createListIdMembreFamille(PlanCalculeDemandeDroitMembreFamilleSearch search) {

        Collection<String> collection = new ArrayList<String>();
        for (JadeAbstractModel model : search.getSearchResults()) {
            PlanCalculeDemandeDroitMembreFamille m = (PlanCalculeDemandeDroitMembreFamille) model;
            collection.add(m.getIdMembreFamilleSF());
        }

        return collection;
    }

    private PlanCalculeDemandeDroitMembreFamilleSearch findMembreFamilleWithPlanPlanCalculRetenu(String idPca)
            throws PCAccordeeException, JadePersistenceException, JadeApplicationServiceNotAvailableException,
            StatistiquesOFASException {
        PlanCalculeDemandeDroitMembreFamilleSearch search = new PlanCalculeDemandeDroitMembreFamilleSearch();
        search.setForIdPCAccordee(idPca);
        search.setIsPlanRetenu(true);
        search.setIsComprisDansCalcul(true);
        PegasusServiceLocator.getStatistiquesOFASService().search(search);
        return search;
    }

    @Override
    public void retrieve() throws Exception {

        // chargement du plan de calcul via l'id pca
        if (JadeStringUtil.isBlank(idPcal)) {
            planCalculHandler = PCPlanCalculHandler.getHandlerForIdPca(getISession(), idPca, idBeneficiaire);
            pcAccordee = PegasusServiceLocator.getSimplePcaccordeeService().read(idPca);
            // On a une copie il faut utilisé la pca parente
            if (!JadeStringUtil.isBlankOrZero((pcAccordee.getIdPcaParent()))) {
                idPca = pcAccordee.getIdPcaParent();
                planCalculHandler = PCPlanCalculHandler.getHandlerForIdPca(getISession(), idPca, idBeneficiaire);
                pcAccordee = PegasusServiceLocator.getSimplePcaccordeeService().read(idPca);
            }
        } else {
            planCalculHandler = PCPlanCalculHandler.getHandlerForIdPlanCalcul(getISession(), idPcal, idBeneficiaire);
            pcAccordee = PegasusServiceLocator.getSimplePcaccordeeService().read(
                    planCalculHandler.getPlanDeCalcul().getIdPCAccordee());
        }

        planDeCalcul = planCalculHandler.getPlanDeCalcul();

        try {
            String byteArrayToString = null;

            if (planDeCalcul.getResultatCalcul() != null && planDeCalcul.getResultatCalcul().length == 0) {
                // on force a null dans le cas ou le tableau n'est pas vide mais null, cela sera dans la jsp a afficher
                // le
                // message d'erreur de plan de reprise
                planDeCalcul.setResultatCalcul(null);
                JadeLogger.error(null, "The pcAccordee is coming from reprise and did not contains blob for calcul");
            } else {

                if (planDeCalcul.getResultatCalcul() == null || planDeCalcul.getResultatCalcul().length == 0) {

                    // pour les cas issus de la reprise de données et qui n'ont pas de BLOB
                    Droit droit = PegasusServiceLocator.getDroitService().readDroitFromVersion(
                            pcAccordee.getIdVersionDroit());

                    DonneesHorsDroitsProvider containerGlobal = DonneesHorsDroitsProvider.getInstance();
                    containerGlobal.init();
                    IPeriodePCAccordee iPeriodePCAccordee = PegasusImplServiceLocator.getCalculDroitService()
                            .calculWithoutPersist(
                                    droit,
                                    createListIdMembreFamille(findMembreFamilleWithPlanPlanCalculRetenu(pcAccordee
                                            .getIdPCAccordee())), containerGlobal, pcAccordee.getDateDebut());

                    if (iPeriodePCAccordee.getCalculComparatifRetenus().length == 1) {
                        tupleRoot = iPeriodePCAccordee.getCalculComparatifRetenus()[0].getMontants();
                    } else {
                        if (droit.getDemande().getDossier().getDemandePrestation().getDemandePrestation().getIdTiers()
                                .equals(idBeneficiaire)) {
                            tupleRoot = iPeriodePCAccordee.getCalculComparatifRetenus()[0].getMontants();
                        } else {
                            tupleRoot = iPeriodePCAccordee.getCalculComparatifRetenus()[1].getMontants();
                        }
                    }

                } else {
                    byteArrayToString = new String(planDeCalcul.getResultatCalcul());
                    tupleRoot = PegasusImplServiceLocator.getCalculPersistanceService().deserialiseDonneesCcXML(
                            byteArrayToString);
                }

                enfantsCompris = planCalculHandler.getEnfantsCompris();
                requerantInfos = planCalculHandler.getRequerantInfos();
                conjointInfos = planCalculHandler.getConjointInfos();
                nssInfos = planCalculHandler.getNssInfos();
                planCalculHandler.generateBlocs(tupleRoot);

                home = HomeUtil.readHomeByPlanCacule(pcAccordee, tupleRoot);
            }

        } catch (Exception ex) {
            JadeLogger.error(null, "The pcAccordee is coming from reprise and did not contains blob for calcul");
        }

        if (home != null && !home.isNew()) {
            designationHome = HomeUtil.formatDesignationHome(home);
        }
    }

    public void setDateval(String dateval) {
        this.dateval = dateval;
    }

    public void setEnfantsCompris(ArrayList<String> enfantsCompris) {
        this.enfantsCompris = enfantsCompris;
    }

    @Override
    public void setId(String newId) {
    }

    public void setIdBeneficiaire(String idBeneficiaire) {
        this.idBeneficiaire = idBeneficiaire;
    }

    public void setIdPca(String idPca) {
        this.idPca = idPca;
    }

    /**
     * @param idPcal
     *            the idPcal to set
     */
    public void setIdPcal(String idPcal) {
        this.idPcal = idPcal;
    }

    /**
     * @param monnaie
     *            the monnaie to set
     */
    public void setMonnaie(String monnaie) {
        this.monnaie = monnaie;
    }

    public void setPcAccordee(SimplePCAccordee pcAccordee) {
        this.pcAccordee = pcAccordee;
    }

    /**
     * @param periodeValidite
     *            the periodeValidite to set
     */
    public void setPeriodeValidite(String periodeValidite) {
        this.periodeValidite = periodeValidite;
    }

    /**
     * @param planCalcul
     *            the planCalcul to set
     */
    public void setPlanCalcul(PCPlanCalculHandler planCalcul) {
        planCalculHandler = planCalcul;
    }

    /**
     * @param planDeCalcul
     *            the planDeCalcul to set
     */
    public void setPlanDeCalcul(SimplePlanDeCalcul planDeCalcul) {
        this.planDeCalcul = planDeCalcul;
    }

    /**
     * @param tupleRoot
     *            the tupleRoot to set
     */
    public void setTupleRoot(TupleDonneeRapport tupleRoot) {
        this.tupleRoot = tupleRoot;
    }

    @Override
    public void update() throws Exception {
    }

    public String getDesignationHome() {
        return designationHome;
    }

}
