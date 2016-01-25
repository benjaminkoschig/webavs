package globaz.naos.itext.statOfas;

import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.statOfas.AFInfoRom060StatistiquesOfasCotisationManager;
import globaz.naos.db.statOfas.AFInfoRom060StatistiquesOfasCotisationMinimumManager;
import globaz.naos.db.statOfas.AFInfoRom060StatistiquesOfasNombreManager;
import globaz.naos.db.statOfas.AFStatistiquesOfas;
import globaz.naos.db.statOfas.AFStatistiquesOfasManager;
import globaz.naos.translation.CodeSystem;

public class AFStatOFASPage1 extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String NUM_REF_INFOROM_STAT_OFAS_AFFILIE = "0118CAF";
    /**
     * Constantes
     */
    protected final static String TEMPLATE_FILENAME = "NAOS_STAT_OFAS";
    public final static Boolean TRUE = new Boolean(true);

    /**
     * Attributs
     */
    private String annee = "";
    private String anneeMoinUn = "";
    private String montantMinime = "";
    private boolean start = true;

    /**
     * Constructeurs
     */
    public AFStatOFASPage1() throws Exception {
        this(new BSession(AFApplication.DEFAULT_APPLICATION_NAOS));
    }

    public AFStatOFASPage1(BProcess parent) throws java.lang.Exception {
        super(parent, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "STATOFASCONTROLE");
        super.setDocumentTitle(getSession().getLabel("STAT_TITRE"));
        setParentWithCopy(parent);

    }

    public AFStatOFASPage1(BSession session) throws java.lang.Exception {
        super(session, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "STATOFASCONTROLE");
        super.setDocumentTitle(getSession().getLabel("STAT_TITRE"));
    }

    @Override
    public void _executeCleanUp() {
        super._executeCleanUp();
    }

    protected void _letterBody() {

        try {
            // On set le texte du haut du document
            super.setParametres(AFStatOFAS_Param.L_AFFILIE,
                    getSession().getApplication().getLabel("STAT_AFFILIE", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_INDEPENDANT,
                    getSession().getApplication().getLabel("STAT_INDEPENDANT", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_IND_EMPL,
                    getSession().getApplication().getLabel("STAT_IND_EMPL", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_NA_SANS_ETUDIANT,
                    getSession().getApplication().getLabel("STAT_NA_SANS_ETUDIANT", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_NA_PERSMAISON,
                    getSession().getApplication().getLabel("STAT_NA_PERSMAISON", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_NA_COTI_MINI,
                    getSession().getApplication().getLabel("STAT_NA_COTI_MINI", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_NB_ETUDIANT, FWMessageFormat.format(getSession().getApplication()
                    .getLabel("NB_ETUDIANT", getSession().getIdLangueISO()), getAnneeMoinUn()));

            super.setParametres(
                    AFStatOFAS_Param.L_NB_ETUDIANT_COTI_MINI,
                    FWMessageFormat.format(
                            getSession().getApplication().getLabel("NB_ETUDIANT_COTI_MINI",
                                    getSession().getIdLangueISO()), getAnneeMoinUn()));

            super.setParametres(AFStatOFAS_Param.L_SALA_NON_COTI,
                    getSession().getApplication().getLabel("STAT_SALA_NON_COTI", getSession().getIdLangueISO()));

            super.setParametres(
                    AFStatOFAS_Param.L_SALA_NON_COTI_PERSMAISON,
                    getSession().getApplication().getLabel("STAT_SALA_NON_COTI_PERSMAISON",
                            getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_EMPLOYEUR,
                    getSession().getApplication().getLabel("STAT_EMPLOYEUR", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_AFFILIE_SANS_COTI,
                    getSession().getApplication().getLabel("STAT_AFFILIE_SANS_COTI", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_TOTAL,
                    getSession().getApplication().getLabel("STAT_TOTAL", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_COTI_AVS,
                    getSession().getApplication().getLabel("STAT_COTI_AVS", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_COTI_IND,
                    getSession().getApplication().getLabel("STAT_COTI_IND", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_COTI_NA_SANS_ETUDIANT,
                    getSession().getApplication().getLabel("STAT_COTI_NA_SANS_ETUDIANT", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_COTI_NA_MINI,
                    getSession().getApplication().getLabel("STAT_COTI_NA_MINI", getSession().getIdLangueISO()));

            super.setParametres(AFStatOFAS_Param.L_COTI_ETUDIANT, FWMessageFormat.format(getSession().getApplication()
                    .getLabel("STAT_COTI_ETUDIANT", getSession().getIdLangueISO()), getAnneeMoinUn()));

            super.setParametres(
                    AFStatOFAS_Param.L_COTI_MINI_ETUDIANT,
                    FWMessageFormat.format(
                            getSession().getApplication().getLabel("STAT_COTI_MINI_ETUDIANT",
                                    getSession().getIdLangueISO()), getAnneeMoinUn()));

        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Impossible de trouver un libellé pour les statistique OFAS " + ": AFStatOFASPage1",
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }

        try {
            String nbrIndependant = "0";
            double montantIndependant = 0;
            String nbrEmployeur = "0";
            double montantEmployeur = 0;
            String nbrTse = "0";
            double montantTse = 0;
            String nbrTsePersMaison = "0";
            String nbrIndEmpl = "0";
            double montantIndEmpl = 0;
            String nbrNaPersMaison = "0";
            String nbrNaCotiMini = "0";
            double montantNaCotiMini = 0;
            String nbrNaSansCotiMini = "0";
            double montantNaSansCotiMini = 0;
            String nbrTotalAffilie = "0";
            String nbrAffilieSansCoti = "0";

            int nbrNAInfoRom060 = 0;
            int nbrEtudiantInfoRom060 = 0;
            double cotiNAInfoRom060 = 0;
            double cotiEtudiantInfoRom060 = 0;
            double cotiMiniEtudiantInfoRom060 = 0;
            int nbrEtudiantAvecCotiMiniInfoRom060 = 0;
            AFStatistiquesOfas entityStatOfas = new AFStatistiquesOfas();

            initMontantMinime();

            try {
                nbrNAInfoRom060 = giveNombreNAInfoRom060(getAnnee());
                nbrEtudiantInfoRom060 = giveNombreEtudiantInfoRom060(getAnneeMoinUn());
                cotiNAInfoRom060 = giveCotiNAInfoRom060(getAnnee());
                cotiEtudiantInfoRom060 = giveCotiEtudiantInfoRom060(getAnneeMoinUn());

                entityStatOfas = giveCotiMiniAndNbrEtudiantAvecCotiMiniInfoRom060(getAnneeMoinUn());
                cotiMiniEtudiantInfoRom060 = Double.valueOf(entityStatOfas.getMontant()).doubleValue();
                nbrEtudiantAvecCotiMiniInfoRom060 = Integer.valueOf(entityStatOfas.getNombre()).intValue();

            } catch (Exception e) {
                getMemoryLog().logMessage(e.toString(), FWMessage.AVERTISSEMENT, this.getClass().getName());
            }

            AFStatistiquesOfasManager manaRequeteA = new AFStatistiquesOfasManager();
            AFStatistiquesOfas resultRequeteA = new AFStatistiquesOfas();
            manaRequeteA.setSession(getSession());
            manaRequeteA.setForFields1(AFStatOFASPage1.TRUE);
            manaRequeteA.setForFrom1(AFStatOFASPage1.TRUE);
            manaRequeteA.setForRequetePrinc(AFStatOFASPage1.TRUE);
            manaRequeteA.setForOrder(AFStatistiquesOfas.FIELDNAME_TYPE_AFFILIE);
            manaRequeteA.setAnnee(getAnnee());
            manaRequeteA.find(BManager.SIZE_NOLIMIT);
            if (manaRequeteA.size() > 0) {
                String typePrecedent = "";
                for (int i = 0; i < manaRequeteA.size(); i++) {
                    resultRequeteA = (AFStatistiquesOfas) manaRequeteA.getEntity(i);
                    if (resultRequeteA.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_INDEP)
                            && resultRequeteA.getGenre().equals(CodeSystem.GENRE_ASS_PERSONNEL)) {
                        if (typePrecedent.equals(resultRequeteA.getTypeAffiliation())) {
                            nbrIndependant = ""
                                    + (JadeStringUtil.toInt(nbrIndependant) + JadeStringUtil.toInt(resultRequeteA
                                            .getNombre()));
                        } else {
                            nbrIndependant = resultRequeteA.getNombre();
                        }
                    }
                    if (resultRequeteA.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_EMPLOY)) {
                        if (typePrecedent.equals(resultRequeteA.getTypeAffiliation())) {
                            nbrEmployeur = ""
                                    + (JadeStringUtil.toInt(nbrEmployeur) + JadeStringUtil.toInt(resultRequeteA
                                            .getNombre()));
                        } else {
                            nbrEmployeur = resultRequeteA.getNombre();
                        }
                    }

                    if (resultRequeteA.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_TSE)) {
                        if (typePrecedent.equals(resultRequeteA.getTypeAffiliation())) {
                            nbrTse = ""
                                    + (JadeStringUtil.toInt(nbrTse) + JadeStringUtil.toInt(resultRequeteA.getNombre()));
                        } else {
                            nbrTse = resultRequeteA.getNombre();
                        }
                    }
                    typePrecedent = resultRequeteA.getTypeAffiliation();
                }
            }
            AFStatistiquesOfasManager manaRequeteMontant = new AFStatistiquesOfasManager();
            AFStatistiquesOfas resultRequeteMontant = new AFStatistiquesOfas();
            manaRequeteMontant.setSession(getSession());
            manaRequeteMontant.setForFields1(AFStatOFASPage1.TRUE);
            manaRequeteMontant.setForFromMontant(AFStatOFASPage1.TRUE);
            manaRequeteMontant.setForMontantCotiInd(AFStatOFASPage1.TRUE);
            manaRequeteMontant.setForOrder(AFStatistiquesOfas.FIELDNAME_TYPE_AFFILIE);
            manaRequeteMontant.setAnnee(getAnneeMoinUn());
            manaRequeteMontant.find(BManager.SIZE_NOLIMIT);
            if (manaRequeteMontant.size() > 0) {
                String typePrecedent = "";
                for (int i = 0; i < manaRequeteMontant.size(); i++) {
                    resultRequeteMontant = (AFStatistiquesOfas) manaRequeteMontant.getEntity(i);
                    if (resultRequeteMontant.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_INDEP)
                            && resultRequeteMontant.getGenre().equals(CodeSystem.GENRE_ASS_PERSONNEL)) {
                        if (typePrecedent.equals(resultRequeteMontant.getTypeAffiliation())) {
                            montantIndependant = montantIndependant
                                    + JadeStringUtil.toDouble(resultRequeteMontant.getMontant());
                        } else {
                            montantIndependant = JadeStringUtil.toDouble(resultRequeteA.getMontant());
                        }
                    }
                    if (resultRequeteMontant.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_EMPLOY)) {
                        if (typePrecedent.equals(resultRequeteMontant.getTypeAffiliation())) {
                            montantEmployeur = montantEmployeur
                                    + JadeStringUtil.toDouble(resultRequeteMontant.getMontant());
                        } else {
                            montantEmployeur = JadeStringUtil.toDouble(resultRequeteA.getMontant());
                        }
                    }

                    if (resultRequeteMontant.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_TSE)) {
                        if (typePrecedent.equals(resultRequeteMontant.getTypeAffiliation())) {
                            montantTse = montantTse + JadeStringUtil.toDouble(resultRequeteMontant.getMontant());
                        } else {
                            montantTse = JadeStringUtil.toDouble(resultRequeteA.getMontant());
                        }
                    }
                    if (resultRequeteMontant.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_INDEP_EMPLOY)
                            && resultRequeteMontant.getGenre().equals(CodeSystem.GENRE_ASS_PERSONNEL)) {
                        montantIndEmpl = JadeStringUtil.toDouble(resultRequeteMontant.getMontant());
                    }
                    typePrecedent = resultRequeteMontant.getTypeAffiliation();
                }
            }

            AFStatistiquesOfasManager manaRequeteB = new AFStatistiquesOfasManager();
            AFStatistiquesOfas resultRequeteB = new AFStatistiquesOfas();
            manaRequeteB.setSession(getSession());
            manaRequeteB.setForFields2(AFStatOFASPage1.TRUE);
            manaRequeteB.setForFrom2(AFStatOFASPage1.TRUE);
            manaRequeteB.setForIndEmpl(AFStatOFASPage1.TRUE);
            manaRequeteB.setAnnee(getAnnee());
            manaRequeteB.find(BManager.SIZE_NOLIMIT);
            if (manaRequeteB.size() > 0) {
                resultRequeteB = (AFStatistiquesOfas) manaRequeteB.getFirstEntity();
                nbrIndEmpl = resultRequeteB.getNombre();
            }

            AFStatistiquesOfasManager manaRequeteC = new AFStatistiquesOfasManager();
            AFStatistiquesOfas resultRequeteC = new AFStatistiquesOfas();
            manaRequeteC.setSession(getSession());
            manaRequeteC.setForFields2(AFStatOFASPage1.TRUE);
            manaRequeteC.setForFrom2(AFStatOFASPage1.TRUE);
            manaRequeteC.setForNaPersMaison(AFStatOFASPage1.TRUE);
            manaRequeteC.setAnnee(getAnnee());
            manaRequeteC.find(BManager.SIZE_NOLIMIT);
            if (manaRequeteC.size() > 0) {
                resultRequeteC = (AFStatistiquesOfas) manaRequeteC.getFirstEntity();
                nbrNaPersMaison = resultRequeteC.getNombre();
            }

            AFStatistiquesOfasManager manaRequeteD = new AFStatistiquesOfasManager();
            AFStatistiquesOfas resultRequeteD = new AFStatistiquesOfas();
            manaRequeteD.setSession(getSession());
            manaRequeteD.setForFields1(AFStatOFASPage1.TRUE);
            manaRequeteD.setForFrom1(AFStatOFASPage1.TRUE);
            manaRequeteD.setForNaCotiMiniEtuRI(AFStatOFASPage1.TRUE);
            manaRequeteD.setAnnee(getAnnee());
            manaRequeteD.find(BManager.SIZE_NOLIMIT);
            AFStatistiquesOfasManager manaRequeteE = new AFStatistiquesOfasManager();
            AFStatistiquesOfas resultRequeteE = new AFStatistiquesOfas();
            manaRequeteE.setSession(getSession());
            manaRequeteE.setMontantMinime(getMontantMinime());
            manaRequeteE.setForFields4(AFStatOFASPage1.TRUE);
            manaRequeteE.setForFrom3(AFStatOFASPage1.TRUE);
            manaRequeteE.setForNaCotiMiniNonEtuRI(AFStatOFASPage1.TRUE);
            manaRequeteE.setAnnee(getAnnee());
            manaRequeteE.find(BManager.SIZE_NOLIMIT);
            if ((manaRequeteD.size() > 0) || (manaRequeteE.size() > 0)) {
                resultRequeteD = (AFStatistiquesOfas) manaRequeteD.getFirstEntity();
                resultRequeteE = (AFStatistiquesOfas) manaRequeteE.getFirstEntity();
                if (resultRequeteD == null) {
                    if (resultRequeteE == null) {
                        nbrNaCotiMini = "0";
                        montantNaCotiMini = 0;
                    } else {
                        nbrNaCotiMini = resultRequeteE.getNombre();
                        montantNaCotiMini = JadeStringUtil.toDouble(resultRequeteE.getMontant());
                    }
                } else {
                    if (resultRequeteE == null) {
                        nbrNaCotiMini = resultRequeteD.getNombre();
                        montantNaCotiMini = JadeStringUtil.toDouble(resultRequeteD.getMontant());
                    } else {
                        nbrNaCotiMini = ""
                                + (JadeStringUtil.toInt(resultRequeteD.getNombre()) + JadeStringUtil
                                        .toInt(resultRequeteE.getNombre()));
                        montantNaCotiMini = JadeStringUtil.toDouble(resultRequeteD.getMontant())
                                + JadeStringUtil.toDouble(resultRequeteE.getMontant());
                    }
                }
            }
            if (nbrNAInfoRom060 != 0) {
                nbrNaSansCotiMini = String.valueOf(nbrNAInfoRom060 - JadeStringUtil.toInt(nbrNaCotiMini));
                montantNaSansCotiMini = cotiNAInfoRom060 - montantNaCotiMini;
            }

            AFStatistiquesOfasManager manaRequeteF = new AFStatistiquesOfasManager();
            AFStatistiquesOfas resultRequeteF = new AFStatistiquesOfas();
            manaRequeteF.setSession(getSession());
            manaRequeteF.setForFields2(AFStatOFASPage1.TRUE);
            manaRequeteF.setForFrom2(AFStatOFASPage1.TRUE);
            manaRequeteF.setForTsePersMaison(AFStatOFASPage1.TRUE);
            manaRequeteF.setAnnee(getAnnee());
            manaRequeteF.find(BManager.SIZE_NOLIMIT);
            if (manaRequeteF.size() > 0) {
                resultRequeteF = (AFStatistiquesOfas) manaRequeteF.getFirstEntity();
                nbrTsePersMaison = resultRequeteF.getNombre();
            }

            AFAffiliationManager manaTotalAffilie = new AFAffiliationManager();
            manaTotalAffilie.setSession(getSession());
            manaTotalAffilie.setFromDateFin("3112" + getAnnee());
            manaTotalAffilie.setForDateDebutAffLowerOrEqualTo("3112" + getAnnee());
            manaTotalAffilie.setForNotTypeAffiliation(CodeSystem.TYPE_AFFILI_FICHIER_CENT);
            nbrTotalAffilie = String.valueOf(manaTotalAffilie.getCount());

            nbrIndependant = "" + (JadeStringUtil.toInt(nbrIndependant) + JadeStringUtil.toInt(nbrIndEmpl));
            nbrAffilieSansCoti = ""
                    + (JadeStringUtil.toInt(nbrTotalAffilie) - JadeStringUtil.toInt(nbrIndependant) - nbrNAInfoRom060
                            - nbrEtudiantInfoRom060 - JadeStringUtil.toInt(nbrTse) - JadeStringUtil.toInt(nbrEmployeur));

            super.setParametres(AFStatOFAS_Param.P_031, nbrIndependant);
            super.setParametres(AFStatOFAS_Param.P_032, nbrIndEmpl);
            super.setParametres(AFStatOFAS_Param.P_033, String.valueOf(nbrNAInfoRom060));
            super.setParametres(AFStatOFAS_Param.P_034, nbrNaPersMaison);
            super.setParametres(AFStatOFAS_Param.P_701, nbrNaCotiMini);
            super.setParametres(AFStatOFAS_Param.P_702, String.valueOf(nbrEtudiantInfoRom060));
            super.setParametres(AFStatOFAS_Param.P_715, String.valueOf(nbrEtudiantAvecCotiMiniInfoRom060));
            super.setParametres(AFStatOFAS_Param.P_035, nbrTse);
            super.setParametres(AFStatOFAS_Param.P_036, nbrTsePersMaison);
            super.setParametres(AFStatOFAS_Param.P_037, nbrEmployeur);
            super.setParametres(AFStatOFAS_Param.P_038, nbrAffilieSansCoti);
            super.setParametres(AFStatOFAS_Param.P_039, nbrTotalAffilie);

            super.setParametres(AFStatOFAS_Param.P_703, new Double(montantIndependant + montantIndEmpl));
            super.setParametres(AFStatOFAS_Param.P_704, new Double(cotiNAInfoRom060));
            super.setParametres(AFStatOFAS_Param.P_705, new Double(montantNaCotiMini));
            super.setParametres(AFStatOFAS_Param.P_706, new Double(cotiEtudiantInfoRom060));
            super.setParametres(AFStatOFAS_Param.P_716, new Double(cotiMiniEtudiantInfoRom060));
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Impossible de trouver un chiffre pour les statistique OFAS " + ": AFStatOFASPage1",
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }

    }

    @Override
    public void afterPrintDocument() {
        getDocumentInfo().setDocumentProperty("annee", String.valueOf(JACalendar.today().getYear()));
        getDocumentInfo().setArchiveDocument(true);
    }

    @Override
    public void beforeBuildReport() {
        // super.setSendMailOnError(true);
        super.setDocumentTitle(getSession().getLabel("STAT_TITRE"));
        getDocumentInfo().setDocumentTypeNumber(AFStatOFASPage1.NUM_REF_INFOROM_STAT_OFAS_AFFILIE);

    }

    /**
     * Méthodes
     */
    @Override
    public final void beforeExecuteReport() {

        try {

        } catch (Exception e) {
            this._addError("false");
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            try {
                getTransaction().rollback();
            } catch (Exception f) {
                getMemoryLog().logMessage(f.getMessage(), FWMessage.FATAL, this.getClass().getName());

            } finally {
            }
        } finally {
        }

        // Initialise le document pour le catalogue de texte
    }

    @Override
    public void createDataSource() throws Exception {
        super.setTemplateFile(AFStatOFASPage1.TEMPLATE_FILENAME);
        // app = (FAApplication) getSession().getApplication();
        start = false;
        _letterBody();

    }

    /**
     * Getters
     */
    public String getAnnee() {
        return annee;
    }

    public String getAnneeMoinUn() {
        if (JadeStringUtil.isBlankOrZero(anneeMoinUn) && !JadeStringUtil.isBlank(getAnnee())) {
            setAnneeMoinUn("" + (JadeStringUtil.toInt(annee) - 1));
        }
        return anneeMoinUn;
    }

    public String getMontantMinime() {
        return montantMinime;
    }

    private double giveCotiEtudiantInfoRom060(String annee) throws Exception {

        if (new JACalendarGregorian().isValidField(JACalendar.YEAR, Integer.valueOf(annee).intValue())) {
            AFInfoRom060StatistiquesOfasCotisationManager mgrCotisation = new AFInfoRom060StatistiquesOfasCotisationManager();
            mgrCotisation.setSession(getSession());
            mgrCotisation.setForAnnee(annee);
            mgrCotisation.setForTypeAffiliation(CodeSystem.TYPE_AFFILI_NON_ACTIF);
            mgrCotisation.setForBrancheEconomique(CodeSystem.BRANCHE_ECO_ETUDIANTS);
            mgrCotisation.find(BManager.SIZE_NOLIMIT);

            if (mgrCotisation.size() == 1) {
                return Double.valueOf(((AFStatistiquesOfas) mgrCotisation.getFirstEntity()).getMontant()).doubleValue();
            }
            throw new Exception("Le manager retourne plus d'un enregistrement");
        }
        throw new Exception(FWMessageFormat.format(getSession().getLabel("ANNEE_NON_VALIDE"), annee));
    }

    private AFStatistiquesOfas giveCotiMiniAndNbrEtudiantAvecCotiMiniInfoRom060(String annee) throws Exception {

        if (new JACalendarGregorian().isValidField(JACalendar.YEAR, Integer.valueOf(annee).intValue())) {
            AFInfoRom060StatistiquesOfasCotisationMinimumManager mgrCotisation = new AFInfoRom060StatistiquesOfasCotisationMinimumManager();
            mgrCotisation.setSession(getSession());
            mgrCotisation.setForAnnee(annee);
            mgrCotisation.setForMontantMinime(getMontantMinime());
            mgrCotisation.setForTypeAffiliation(CodeSystem.TYPE_AFFILI_NON_ACTIF);
            mgrCotisation.setForBrancheEconomique(CodeSystem.BRANCHE_ECO_ETUDIANTS);
            mgrCotisation.find(BManager.SIZE_NOLIMIT);

            if (mgrCotisation.size() == 1) {
                return (AFStatistiquesOfas) mgrCotisation.getFirstEntity();
            }
            throw new Exception("Le manager retourne plus d'un enregistrement");
        }
        throw new Exception(FWMessageFormat.format(getSession().getLabel("ANNEE_NON_VALIDE"), annee));
    }

    private double giveCotiNAInfoRom060(String annee) throws Exception {

        if (new JACalendarGregorian().isValidField(JACalendar.YEAR, Integer.valueOf(annee).intValue())) {
            AFInfoRom060StatistiquesOfasCotisationManager mgrCotisation = new AFInfoRom060StatistiquesOfasCotisationManager();
            mgrCotisation.setSession(getSession());
            mgrCotisation.setForAnnee(annee);
            mgrCotisation.setForTypeAffiliation(CodeSystem.TYPE_AFFILI_NON_ACTIF);
            mgrCotisation.setForNotBrancheEconomique(CodeSystem.BRANCHE_ECO_ETUDIANTS);
            mgrCotisation.find(BManager.SIZE_NOLIMIT);

            double montantToReturn = 0;
            for (int i = 1; i <= mgrCotisation.size(); i++) {
                montantToReturn = montantToReturn
                        + Double.valueOf(((AFStatistiquesOfas) mgrCotisation.getEntity(i - 1)).getMontant())
                                .doubleValue();
            }
            return montantToReturn;
        }
        throw new Exception(FWMessageFormat.format(getSession().getLabel("ANNEE_NON_VALIDE"), annee));
    }

    private int giveNombreEtudiantInfoRom060(String annee) throws Exception {
        AFInfoRom060StatistiquesOfasNombreManager mgrNombreAffilie = new AFInfoRom060StatistiquesOfasNombreManager();
        mgrNombreAffilie.setSession(getSession());
        mgrNombreAffilie.setForAnnee(annee);
        mgrNombreAffilie.setForTypeAffiliation(CodeSystem.TYPE_AFFILI_NON_ACTIF);
        mgrNombreAffilie.setForBrancheEconomique(CodeSystem.BRANCHE_ECO_ETUDIANTS);

        return mgrNombreAffilie.getCount();
    }

    private int giveNombreNAInfoRom060(String annee) throws Exception {
        AFInfoRom060StatistiquesOfasNombreManager mgrNombreAffilie = new AFInfoRom060StatistiquesOfasNombreManager();
        mgrNombreAffilie.setSession(getSession());
        mgrNombreAffilie.setForAnnee(annee);
        mgrNombreAffilie.setForTypeAffiliation(CodeSystem.TYPE_AFFILI_NON_ACTIF);
        mgrNombreAffilie.setForNotBrancheEconomique(CodeSystem.BRANCHE_ECO_ETUDIANTS);
        mgrNombreAffilie.find(BManager.SIZE_NOLIMIT);

        return mgrNombreAffilie.getCount();

    }

    public void initMontantMinime() {
        if (JadeStringUtil.isBlankOrZero(montantMinime) && !JadeStringUtil.isBlankOrZero(getAnnee())) {
            try {
                setMontantMinime(FWFindParameter.findParameter(getTransaction(), "10500070", "COTMININDE", "0101"
                        + getAnnee(), "0", 2));
            } catch (Exception e) {
                getMemoryLog().logMessage("", FWMessage.ERREUR, e.getMessage());
            }
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    @Override
    public boolean next() throws FWIException {
        return start;
    }

    /**
     * Setters
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setAnneeMoinUn(String anneeMoinUn) {
        this.anneeMoinUn = anneeMoinUn;
    }

    public void setMontantMinime(String montantMinime) {
        this.montantMinime = montantMinime;
    }
}
