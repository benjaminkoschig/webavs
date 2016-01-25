/*
 * Created on Jul 11, 2006
 * 
 * To change the template for this generated file go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 * Comments
 */
package globaz.corvus.itext;

import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.adaptation.RERentesAdaptees;
import globaz.corvus.db.adaptation.RERentesAdapteesJointRATiers;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.webavs.common.CommonProperties;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hpe
 * 
 */
public class REListeRecapitulativePrestationsAugmentees extends FWIDocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FICHIER_MODELE = "RE_RECAP_ADAPTATION";
    public static final String FICHIER_RESULTAT = "recap_adaptation";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forMoisAnnee = "";
    private boolean hasNext = true;

    Map<String, RERentesAdapteesJointRATiers> mapAutomatique = new HashMap<String, RERentesAdapteesJointRATiers>();
    Map<String, RERentesAdapteesJointRATiers> mapJavaCentrale = new HashMap<String, RERentesAdapteesJointRATiers>();
    Map<String, RERentesAdapteesJointRATiers> mapManuellement = new HashMap<String, RERentesAdapteesJointRATiers>();

    private FWCurrency montantAPIAI = new FWCurrency("0.00");
    private FWCurrency montantAPIAVS = new FWCurrency("0.00");

    private FWCurrency montantREOAI = new FWCurrency("0.00");
    private FWCurrency montantREOAVS = new FWCurrency("0.00");

    private FWCurrency montantROAI = new FWCurrency("0.00");
    private FWCurrency montantROAVS = new FWCurrency("0.00");

    private FWCurrency montantTotalAI = new FWCurrency("0.00");
    private FWCurrency montantTotalAVS = new FWCurrency("0.00");

    private FWCurrency montantTotalGeneral = new FWCurrency("0.00");
    private int nbRAAPIAI;

    private int nbRAAPIAVS;
    private int nbRAREOAI;

    private int nbRAREOAVS;
    private int nbRAROAI;

    private int nbRAROAVS;
    private int nbTotalAI;

    private int nbTotalAVS;
    private int nbTotalGeneral;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe REListeRecapitulativePrestationsAugmentees.
     */
    public REListeRecapitulativePrestationsAugmentees() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe REListeRecapitulativePrestationsAugmentees.
     * 
     * @param parent
     *            DOCUMENT ME!
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    public REListeRecapitulativePrestationsAugmentees(BProcess parent) throws FWIException {
        super(parent, REApplication.APPLICATION_CORVUS_REP, REListeRecapitulativePrestationsAugmentees.FICHIER_RESULTAT);
    }

    /**
     * Crée une nouvelle instance de la classe REListeRecapitulativePrestationsAugmentees.
     * 
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    public REListeRecapitulativePrestationsAugmentees(BSession session) throws FWIException {
        super(session, REApplication.APPLICATION_CORVUS_REP,
                REListeRecapitulativePrestationsAugmentees.FICHIER_RESULTAT);
    }

    public void addMontantAPIAI(FWCurrency i) {
        montantAPIAI.add(i);
        montantTotalAI.add(i);
        montantTotalGeneral.add(i);
    }

    public void addMontantAPIAVS(FWCurrency i) {
        montantAPIAVS.add(i);
        montantTotalAVS.add(i);
        montantTotalGeneral.add(i);
    }

    public void addMontantREOAI(FWCurrency i) {
        montantREOAI.add(i);
        montantTotalAI.add(i);
        montantTotalGeneral.add(i);
    }

    public void addMontantREOAVS(FWCurrency i) {
        montantREOAVS.add(i);
        montantTotalAVS.add(i);
        montantTotalGeneral.add(i);
    }

    public void addMontantROAI(FWCurrency i) {
        montantROAI.add(i);
        montantTotalAI.add(i);
        montantTotalGeneral.add(i);
    }

    public void addMontantROAVS(FWCurrency i) {
        montantROAVS.add(i);
        montantTotalAVS.add(i);
        montantTotalGeneral.add(i);
    }

    public void addNbRAAPIAI(int i) {
        nbRAAPIAI += i;
        nbTotalAI += i;
        nbTotalGeneral += i;
    }

    public void addNbRAAPIAVS(int i) {
        nbRAAPIAVS += i;
        nbTotalAVS += i;
        nbTotalGeneral += i;
    }

    public void addNbRAREOAI(int i) {
        nbRAREOAI += i;
        nbTotalAI += i;
        nbTotalGeneral += i;
    }

    public void addNbRAREOAVS(int i) {
        nbRAREOAVS += i;
        nbTotalAVS += i;
        nbTotalGeneral += i;
    }

    public void addNbRAROAAI(int i) {
        nbRAROAI += i;
        nbTotalAI += i;
        nbTotalGeneral += i;
    }

    public void addNbRAROAVS(int i) {
        nbRAROAVS += i;
        nbTotalAVS += i;
        nbTotalGeneral += i;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeBuildReport()
     */
    @Override
    public void beforeBuildReport() throws FWIException {

        // on ajoute au doc info le numéro de référence inforom
        getDocumentInfo().setDocumentTypeNumber(
                IRENoDocumentInfoRom.ADAPTATION_LISTE_RECAPITULATIVE_PRESTATIONS_AUGMENTEES);

    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        try {

            setTemplateFile(REListeRecapitulativePrestationsAugmentees.FICHIER_MODELE);

            setParamIfNotNull(
                    "P_DATE",
                    getSession().getLabel("LISTE_RECAP_DATE") + " "
                            + JACalendar.format(JACalendar.todayJJsMMsAAAA(), getSession().getIdLangueISO()));

            String numCaisse = null;

            numCaisse = (getSession().getApplication()).getProperty(CommonProperties.KEY_NO_CAISSE_FORMATE);

            setParamIfNotNull("P_NUM_CAISSE", getSession().getLabel("LISTE_RECAP_NO_CAISSE") + " " + numCaisse);
            setParamIfNotNull("P_TITRE_PAGE", getSession().getLabel("LISTE_RECAP_TITRE"));

            setParamIfNotNull("P_MOIS_ANNEE", getSession().getLabel("LISTE_RECAP_MOIS_ANNEE") + " " + getForMoisAnnee());

            setParamIfNotNull("P_TYPE_AVS", getSession().getLabel("LISTE_RECAP_AVS"));
            setParamIfNotNull("P_TYPE_AI", getSession().getLabel("LISTE_RECAP_AI"));

            setParamIfNotNull("P_RO", getSession().getLabel("LISTE_RECAP_RENTES_ORDINAIRES"));
            setParamIfNotNull("P_REO", getSession().getLabel("LISTE_RECAP_RENTES_EXTRAORDINAIRES"));
            setParamIfNotNull("P_API", getSession().getLabel("LISTE_RECAP_ALLOCATION_IMPOTENT"));
            setParamIfNotNull("P_TOTAL_LIGNE", getSession().getLabel("LISTE_RECAP_TOTAL"));

            setParamIfNotNull("P_NB", getSession().getLabel("LISTE_RECAP_NB"));
            setParamIfNotNull("P_MONTANT", getSession().getLabel("LISTE_RECAP_CHF"));
            setParamIfNotNull("P_EN_COURS", getSession().getLabel("LISTE_RECAP_VERSEMENT"));

            // Charger tous les montants !
            chargerMontants();

            // nb, montant, retenues et versement par categories
            setParamIfNotNull("P_NB_RO_AVS", String.valueOf(getNbRAROAVS()));
            setParamIfNotNull("P_MONTANT_RO_AVS", getMontantROAVS().toStringFormat());

            setParamIfNotNull("P_NB_REO_AVS", String.valueOf(getNbRAREOAVS()));
            setParamIfNotNull("P_MONTANT_REO_AVS", getMontantREOAVS().toStringFormat());

            setParamIfNotNull("P_NB_API_AVS", String.valueOf(getNbRAAPIAVS()));
            setParamIfNotNull("P_MONTANT_API_AVS", getMontantAPIAVS().toStringFormat());

            setParamIfNotNull("P_NB_TOTAL_AVS", String.valueOf(getNbTotalAVS()));
            setParamIfNotNull("P_MONTANT_TOTAL_AVS", getMontantTotalAVS().toStringFormat());

            setParamIfNotNull("P_NB_RO_AI", String.valueOf(getNbRAROAI()));
            setParamIfNotNull("P_MONTANT_RO_AI", getMontantROAI().toStringFormat());

            setParamIfNotNull("P_NB_REO_AI", String.valueOf(getNbRAREOAI()));
            setParamIfNotNull("P_MONTANT_REO_AI", getMontantREOAI().toStringFormat());

            setParamIfNotNull("P_NB_API_AI", String.valueOf(getNbRAAPIAI()));
            setParamIfNotNull("P_MONTANT_API_AI", getMontantAPIAI().toStringFormat());

            setParamIfNotNull("P_NB_TOTAL_AI", String.valueOf(getNbTotalAI()));
            setParamIfNotNull("P_MONTANT_TOTAL_AI", getMontantTotalAI().toStringFormat());

            // recapitulation
            setParamIfNotNull("P_TOTAL", getSession().getLabel("LISTE_RECAP_TOTAL_GENERAL"));
            setParamIfNotNull("P_NB_TOTAL_GENERAL", String.valueOf(getNbTotalGeneral()));
            setParamIfNotNull("P_MONTANT_TOTAL_GENERAL", getMontantTotalGeneral().toStringFormat());

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR,
                    getSession().getLabel("PROCESS_LISTE_RECAP_PRST_AUG_OBJET_MAIL"));
            throw new FWIException(e);
        }
    }

    private void chargerMontants() {

        Map<String, RERentesAdaptees> mapAll = new HashMap<String, RERentesAdaptees>();
        mapAll.putAll(mapAutomatique);
        mapAll.putAll(mapJavaCentrale);
        mapAll.putAll(mapManuellement);

        for (String keyIdRenteAdap : mapAll.keySet()) {
            RERentesAdaptees renteAdap = mapAll.get(keyIdRenteAdap);

            // Rentes ordinaires AVS
            if (REPrestationsAccordees.GROUPE_RO_AVS == getGroupeGenreRente(renteAdap.getCodePrestation())) {
                addMontantROAVS(new FWCurrency(renteAdap.getNouveauMontantPrestation()));
                addNbRAROAVS(1);

                // Rentes extraordinaires AVS
            } else if (REPrestationsAccordees.GROUPE_REO_AVS == getGroupeGenreRente(renteAdap.getCodePrestation())) {

                addMontantREOAVS(new FWCurrency(renteAdap.getNouveauMontantPrestation()));
                addNbRAREOAVS(1);

                // Allocation pour impotent AVS
            } else if (REPrestationsAccordees.GROUPE_API_AVS == getGroupeGenreRente(renteAdap.getCodePrestation())) {

                addMontantAPIAVS(new FWCurrency(renteAdap.getNouveauMontantPrestation()));
                addNbRAAPIAVS(1);

                // Rentes ordinaires AI
            } else if (REPrestationsAccordees.GROUPE_RO_AI == getGroupeGenreRente(renteAdap.getCodePrestation())) {
                addMontantROAI(new FWCurrency(renteAdap.getNouveauMontantPrestation()));
                addNbRAROAAI(1);

                // Rentes extraordinaires AI
            } else if (REPrestationsAccordees.GROUPE_REO_AI == getGroupeGenreRente(renteAdap.getCodePrestation())) {

                addMontantREOAI(new FWCurrency(renteAdap.getNouveauMontantPrestation()));
                addNbRAREOAI(1);

                // Allocation pour impotent AI
            } else if (REPrestationsAccordees.GROUPE_API_AI == getGroupeGenreRente(renteAdap.getCodePrestation())) {

                addMontantAPIAI(new FWCurrency(renteAdap.getNouveauMontantPrestation()));
                addNbRAAPIAI(1);

            }
        }
    }

    @Override
    public void createDataSource() throws Exception {

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     * 
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {
        if (getMemoryLog().isOnErrorLevel() || getMemoryLog().isOnFatalLevel()) {
            return getSession().getLabel("PROCESS_LISTE_RECAP_PRST_AUG_OBJET_MAIL");
        } else {
            return getSession().getLabel("PROCESS_LISTE_RECAP_PRST_AUG_OBJET_MAIL");
        }
    }

    /**
     * getter pour l'attribut for mois annee
     * 
     * @return la valeur courante de l'attribut for mois annee
     */
    public String getForMoisAnnee() {
        return forMoisAnnee;
    }

    public int getGroupeGenreRente(String codePrestation) {

        return REPrestationsAccordees.getGroupeGenreRente(codePrestation);
    }

    public Map<String, RERentesAdapteesJointRATiers> getMapAutomatique() {
        return mapAutomatique;
    }

    public Map<String, RERentesAdapteesJointRATiers> getMapJavaCentrale() {
        return mapJavaCentrale;
    }

    public Map<String, RERentesAdapteesJointRATiers> getMapManuellement() {
        return mapManuellement;
    }

    public FWCurrency getMontantAPIAI() {
        return montantAPIAI;
    }

    public FWCurrency getMontantAPIAVS() {
        return montantAPIAVS;
    }

    public FWCurrency getMontantREOAI() {
        return montantREOAI;
    }

    public FWCurrency getMontantREOAVS() {
        return montantREOAVS;
    }

    public FWCurrency getMontantROAI() {
        return montantROAI;
    }

    public FWCurrency getMontantROAVS() {
        return montantROAVS;
    }

    public FWCurrency getMontantTotalAI() {
        return montantTotalAI;
    }

    public FWCurrency getMontantTotalAVS() {
        return montantTotalAVS;
    }

    public FWCurrency getMontantTotalGeneral() {
        return montantTotalGeneral;
    }

    public int getNbRAAPIAI() {
        return nbRAAPIAI;
    }

    public int getNbRAAPIAVS() {
        return nbRAAPIAVS;
    }

    public int getNbRAREOAI() {
        return nbRAREOAI;
    }

    public int getNbRAREOAVS() {
        return nbRAREOAVS;
    }

    public int getNbRAROAI() {
        return nbRAROAI;
    }

    public int getNbRAROAVS() {
        return nbRAROAVS;
    }

    public int getNbTotalAI() {
        return nbTotalAI;
    }

    public int getNbTotalAVS() {
        return nbTotalAVS;
    }

    public int getNbTotalGeneral() {
        return nbTotalGeneral;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        if (hasNext) {
            hasNext = false;

            return true;
        } else {
            return false;
        }
    }

    /**
     * setter pour l'attribut for moisAnnee.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForMoisAnnee(String string) {
        forMoisAnnee = string;
    }

    public void setMapAutomatique(Map<String, RERentesAdapteesJointRATiers> mapAutomatique) {
        this.mapAutomatique = mapAutomatique;
    }

    public void setMapJavaCentrale(Map<String, RERentesAdapteesJointRATiers> mapJavaCentrale) {
        this.mapJavaCentrale = mapJavaCentrale;
    }

    public void setMapManuellement(Map<String, RERentesAdapteesJointRATiers> mapManuellement) {
        this.mapManuellement = mapManuellement;
    }

    public void setMontantAPIAI(FWCurrency montantAPIAI) {
        this.montantAPIAI = montantAPIAI;
    }

    public void setMontantAPIAVS(FWCurrency montantAPIAVS) {
        this.montantAPIAVS = montantAPIAVS;
    }

    public void setMontantREOAI(FWCurrency montantREOAI) {
        this.montantREOAI = montantREOAI;
    }

    public void setMontantREOAVS(FWCurrency montantREOAVS) {
        this.montantREOAVS = montantREOAVS;
    }

    public void setMontantROAI(FWCurrency montantROAI) {
        this.montantROAI = montantROAI;
    }

    public void setMontantROAVS(FWCurrency montantROAVS) {
        this.montantROAVS = montantROAVS;
    }

    public void setMontantTotalAI(FWCurrency montantTotalAI) {
        this.montantTotalAI = montantTotalAI;
    }

    public void setMontantTotalAVS(FWCurrency montantTotalAVS) {
        this.montantTotalAVS = montantTotalAVS;
    }

    public void setMontantTotalGeneral(FWCurrency montantTotalGeneral) {
        this.montantTotalGeneral = montantTotalGeneral;
    }

    public void setNbRAAPIAI(int nbRAAPIAI) {
        this.nbRAAPIAI = nbRAAPIAI;
    }

    public void setNbRAAPIAVS(int nbRAAPIAVS) {
        this.nbRAAPIAVS = nbRAAPIAVS;
    }

    public void setNbRAREOAI(int nbRAREOAI) {
        this.nbRAREOAI = nbRAREOAI;
    }

    public void setNbRAREOAVS(int nbRAREOAVS) {
        this.nbRAREOAVS = nbRAREOAVS;
    }

    public void setNbRAROAI(int nbRAROAI) {
        this.nbRAROAI = nbRAROAI;
    }

    public void setNbRAROAVS(int nbRAROAVS) {
        this.nbRAROAVS = nbRAROAVS;
    }

    public void setNbTotalAI(int nbTotalAI) {
        this.nbTotalAI = nbTotalAI;
    }

    public void setNbTotalAVS(int nbTotalAVS) {
        this.nbTotalAVS = nbTotalAVS;
    }

    public void setNbTotalGeneral(int nbTotalGeneral) {
        this.nbTotalGeneral = nbTotalGeneral;
    }

    private void setParamIfNotNull(String name, Object value) {
        if (value != null) {
            getImporter().setParametre(name, value);
        }
    }

}
