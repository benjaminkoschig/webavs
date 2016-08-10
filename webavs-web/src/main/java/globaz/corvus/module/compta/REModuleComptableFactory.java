package globaz.corvus.module.compta;

import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.prestations.REPrestationsManager;
import globaz.corvus.db.rentesaccordees.REDecisionJointDemandeRente;
import globaz.corvus.db.rentesaccordees.REDecisionJointDemandeRenteManager;
import globaz.corvus.utils.REPmtMensuel;
import globaz.globall.api.BISession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APIRubrique;
import globaz.prestation.tools.PRAssert;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Description Factory retournant le module comptable approprié pour la génération des écritures comptables + ordres de
 * versement
 * 
 * @author scr
 */
public class REModuleComptableFactory {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // Class statique, référence par cette instance
    private static REModuleComptableFactory instance = null;

    /**
     * @return L'instance de cette classe
     * @throws Exception
     */
    public static synchronized REModuleComptableFactory getInstance() throws Exception {
        if (REModuleComptableFactory.instance == null) {
            REModuleComptableFactory.instance = new REModuleComptableFactory();
        }
        return REModuleComptableFactory.instance;
    }

    public APIRubrique API_AI = null;
    public APIRubrique API_AI_EXTOURNE = null;
    public APIRubrique API_AI_RETRO = null;
    public APIRubrique API_AVS = null;

    public APIRubrique API_AVS_EXTOURNE = null;
    public APIRubrique API_AVS_RETRO = null;
    public APIRubrique COMPENSATION = null;
    public APIRubrique IMPOT_SOURCE = null;
    public APIRubrique INTERET_MORATOIRE_AI = null;
    public APIRubrique INTERET_MORATOIRE_AVS = null;

    /**
     * Rubrique PC AI standard
     */
    public APIRubrique PC_AI = null;
    // public APIRubrique PC_AI_COTISATIONS_AVS = null;

    // Nouvelle rubrique PC AI
    public APIRubrique PC_AI_ALLOCATIONS_DE_NOEL = null;
    public APIRubrique PC_AI_DOMICILE_ORDINAIRES = null;
    public APIRubrique PC_AI_HOME_SASH = null;
    public APIRubrique PC_AI_HOME_SASH_HORS_CANTON = null;
    public APIRubrique PC_AI_HOME_SPAS = null;
    public APIRubrique PC_AI_HOME_SPAS_HORS_CANTON = null;
    public APIRubrique PC_AI_EN_HOME = null;
    /**
     * Rubrique PC AVS standard
     */
    public APIRubrique PC_AVS = null;

    public APIRubrique PC_AVS_A_DOMICILE_ORDINAIRES = null;
    // Nouvelle rubrique PC AVS
    public APIRubrique PC_AVS_ALLOCATIONS_DE_NOEL = null;
    // public APIRubrique PC_AVS_COTISATIONS_AVS = null;
    public APIRubrique PC_AVS_EN_HOME_HORS_CANTON_SASH = null;
    public APIRubrique PC_AVS_EN_HOME_HORS_CANTON_SPAS = null;
    public APIRubrique PC_AVS_EN_HOME_SASH = null;
    public APIRubrique PC_AVS_EN_HOME_SPAS = null;
    public APIRubrique PC_AVS_EN_HOME = null;

    public APIRubrique PRST_AI_RESTITUER = null;
    public APIRubrique PRST_API_AI_RESTITUER = null;
    public APIRubrique PRST_API_AVS_RESTITUER = null;
    public APIRubrique PRST_AVS_RESTITUER = null;
    public APIRubrique REGULARISATION_CCP = null;

    public APIRubrique REO_AI = null;
    public APIRubrique REO_AI_EXTOURNE = null;
    public APIRubrique REO_AI_RETRO = null;
    public APIRubrique REO_AVS = null;
    public APIRubrique REO_AVS_EXTOURNE = null;
    public APIRubrique REO_AVS_RETRO = null;

    public APIRubrique RFM_AI = null;
    public APIRubrique RFM_AI_FRANCHISE_ET_PARTICIPATION_FRQP = null;
    public APIRubrique RFM_AI_REGIME_ALIMENTAIRE = null;

    // public APIRubrique RFM_AI_AIDE_AU_MENAGE = null;
    // public APIRubrique RFM_AI_COTISATION_AVS_PARITAIRE = null;
    // public APIRubrique RFM_AI_ENCADREMENT_SOCIO_EDUC_ET_SECURITAIRE = null;
    // public APIRubrique RFM_AI_FRAIS_PENSION_COURT_SEJOUR = null;
    // public APIRubrique RFM_AI_MOYENS_AUX_APPAREILS_PROTHESE_LIT = null;
    // public APIRubrique RFM_AI_PARTICIPATION_COURT_SEJOUR = null;
    // public APIRubrique RFM_AI_PENSION_HOME_DE_JOUR = null;
    // public APIRubrique RFM_AI_SOINS_PAR_LA_FAMILLE = null;
    // public APIRubrique RFM_AI_STANDARD = null;
    // public APIRubrique RFM_AI_TRAITEMENT_DENTAIRE = null;
    // public APIRubrique RFM_AI_TRANSPORT = null;
    // public APIRubrique RFM_AI_UNITE_ACCUEIL_TEMPORAIRE = null;

    public APIRubrique RFM_AVS = null;
    public APIRubrique RFM_AVS_FRANCHISE_ET_PARTICIPATION_FRQP = null;
    public APIRubrique RFM_AVS_REGIME_ALIMENTAIRE = null;

    // public APIRubrique RFM_AVS_AIDE_AU_MENAGE = null;
    // public APIRubrique RFM_AVS_COTISATION_AVS_PARITAIRE = null;
    // public APIRubrique RFM_AVS_ENCADREMENT_SOCIO_EDUC_ET_SECURITAIRE = null;
    // public APIRubrique RFM_AVS_FRAIS_PENSION_COURT_SEJOUR = null;
    // public APIRubrique RFM_AVS_MOYENS_AUX_APPAREILS_PROTHESE_LIT = null;
    // public APIRubrique RFM_AVS_PARTICIPATION_COURT_SEJOUR = null;
    // public APIRubrique RFM_AVS_PENSION_HOME_DE_JOUR = null;
    // public APIRubrique RFM_AVS_SOINS_PAR_LA_FAMILLE = null;
    // public APIRubrique RFM_AVS_STANDARD = null;
    // public APIRubrique RFM_AVS_TRAITEMENT_DENTAIRE = null;
    // public APIRubrique RFM_AVS_TRANSPORT = null;
    // public APIRubrique RFM_AVS_UNITE_ACCUEIL_TEMPORAIRE = null;

    public APIRubrique RO_AI = null;
    public APIRubrique RO_AI_EXTOURNE = null;
    public APIRubrique RO_AI_RETRO = null;
    public APIRubrique RO_AVS = null;
    public APIRubrique RO_AVS_EXTOURNE = null;
    public APIRubrique RO_AVS_RETRO = null;

    private REModuleComptableFactory() throws Exception {
    }

    /**
     * @param decision
     * @param isEcritureComptablePourCeLot
     * @return
     * @throws Exception
     */
    public IREModuleComptable[] getModules(REDecisionEntity decision, boolean isEcritureComptablePourCeLot)
            throws Exception {

        // Si aucune écriture comptable ne doit être générée pour ce lot, seul les modules
        // 'Normal', 'Restit' et 'Sans écriture comptables' seront instanciés. Ce sont les seuls susceptible de créer
        // des écritures
        // pour la récap des rentes.

        Map<String, IREModuleComptable> modulesComptable = new TreeMap<String, IREModuleComptable>();

        if ((decision == null) || decision.isNew()) {
            throw new Exception("Décision incomplète !!!");
        }

        // Init des manager
        REPrestationsManager mgrPrst = new REPrestationsManager();
        mgrPrst.setSession(decision.getSession());
        mgrPrst.setForIdDecision(decision.getIdDecision());

        mgrPrst.find(2);
        if (mgrPrst.size() == 0) {
            throw new Exception("Aucune prestation trouvée pour la décision no : " + decision.getIdDecision());
        }

        if (mgrPrst.size() > 1) {
            throw new Exception("Plusieurs prestations trouvées pour la décision no : " + decision.getIdDecision());
        }
        REPrestations prst = (REPrestations) mgrPrst.getEntity(0);

        REDemandeRente demande = new REDemandeRente();
        demande.setSession(decision.getSession());
        demande.setIdDemandeRente(decision.getIdDemandeRente());
        demande.retrieve();
        PRAssert.notIsNew(demande, null);

        // Recherche de la date de début de la décision.
        JADate ddDroit = null;
        if (IREDecision.CS_TYPE_DECISION_COURANT.equals(decision.getCsTypeDecision())) {
            ddDroit = new JADate(decision.getDecisionDepuis());
        } else if (IREDecision.CS_TYPE_DECISION_RETRO.equals(decision.getCsTypeDecision())) {
            ddDroit = new JADate(decision.getDateDebutRetro());
        } else {
            // Date début correspond à la plus petite date de début des RA de la décision
            REDecisionJointDemandeRenteManager mgr = new REDecisionJointDemandeRenteManager();
            mgr.setForIdDecision(decision.getIdDecision());
            mgr.setSession(decision.getSession());
            mgr.find();

            ddDroit = new JADate("31.12.2999");
            JACalendar cal = new JACalendarGregorian();
            for (int i = 0; i < mgr.size(); i++) {
                REDecisionJointDemandeRente elm = (REDecisionJointDemandeRente) mgr.getEntity(i);
                JADate ddRA = new JADate(elm.getDateDebutDroit());
                if (cal.compare(ddDroit, ddRA) == JACalendar.COMPARE_SECONDLOWER) {
                    ddDroit = new JADate(elm.getDateDebutDroit());
                }
            }
        }

        JADate dateDernierPmt = new JADate(REPmtMensuel.getDateDernierPmt(decision.getSession()));
        JACalendar cal = new JACalendarGregorian();
        IREModuleComptable modCpt = null;
        boolean isSansEcritureComptablePourCetteDecision = false;
        if (cal.compare(ddDroit, dateDernierPmt) == JACalendar.COMPARE_FIRSTUPPER) {
            isSansEcritureComptablePourCetteDecision = true;
            modCpt = new REModRecapSansEcrituresComptables(!isSansEcritureComptablePourCetteDecision);
            if (!modulesComptable.containsKey(modCpt.toString())) {
                modulesComptable.put(modCpt.toString(), modCpt);
            }
        }

        if (!JadeStringUtil.isBlankOrZero(decision.getDateDebutRetro()) && isEcritureComptablePourCeLot) {

            JADate dd = new JADate(decision.getDateDebutRetro());
            JADate df = new JADate(decision.getDateFinRetro());

            if ((cal.compare(dd, df) == JACalendar.COMPARE_EQUALS)
                    || (cal.compare(dd, df) == JACalendar.COMPARE_FIRSTLOWER)) {

                modCpt = new REModCpt_Retroactif(!isSansEcritureComptablePourCetteDecision);

                if (!modulesComptable.containsKey(modCpt.toString())) {
                    modulesComptable.put(modCpt.toString(), modCpt);
                }
            }
        }

        REOrdresVersements[] ovs = prst.getOrdresVersement(null);
        for (REOrdresVersements ov : ovs) {

            switch (ov.getCsTypeOrdreVersement()) {
                case BENEFICIAIRE_PRINCIPAL:
                    if (!isSansEcritureComptablePourCetteDecision) {
                        modCpt = new REModCpt_Normal(!isSansEcritureComptablePourCetteDecision);
                        if (!modulesComptable.containsKey(modCpt.toString())) {
                            modulesComptable.put(modCpt.toString(), modCpt);
                        }
                    }
                    break;

                case CREANCIER:
                case ASSURANCE_SOCIALE:
                    if (isEcritureComptablePourCeLot) {
                        modCpt = new REModCpt_Creanciers(!isSansEcritureComptablePourCetteDecision);
                        if (!modulesComptable.containsKey(modCpt.toString())) {
                            modulesComptable.put(modCpt.toString(), modCpt);
                        }
                    }
                    break;

                case IMPOT_A_LA_SOURCE:
                    if (isEcritureComptablePourCeLot) {
                        modCpt = new REModCpt_IS(!isSansEcritureComptablePourCetteDecision);
                        if (!modulesComptable.containsKey(modCpt.toString())) {
                            modulesComptable.put(modCpt.toString(), modCpt);
                        }
                    }
                    break;

                case INTERET_MORATOIRE:
                    if (isEcritureComptablePourCeLot) {
                        modCpt = new REModCpt_IM(!isSansEcritureComptablePourCetteDecision);
                        if (!modulesComptable.containsKey(modCpt.toString())) {
                            modulesComptable.put(modCpt.toString(), modCpt);
                        }
                    }
                    break;

                case DETTE:
                case DIMINUTION_DE_RENTE:
                    modCpt = new REModCpt_Restitution(!isSansEcritureComptablePourCetteDecision);
                    if (!modulesComptable.containsKey(modCpt.toString())) {
                        modulesComptable.put(modCpt.toString(), modCpt);
                    }
                    break;

                case DETTE_RENTE_AVANCES:
                case DETTE_RENTE_DECISION:
                case DETTE_RENTE_PRST_BLOQUE:
                case DETTE_RENTE_RESTITUTION:
                case DETTE_RENTE_RETOUR:
                    if (isEcritureComptablePourCeLot) {
                        modCpt = new REModCpt_CompensationCA(!isSansEcritureComptablePourCetteDecision);
                        if (!modulesComptable.containsKey(modCpt.toString())) {
                            modulesComptable.put(modCpt.toString(), modCpt);
                        }
                    }
                    break;

                default:
                    break;
            }
        }

        Set<String> keys = modulesComptable.keySet();
        IREModuleComptable[] result = new IREModuleComptable[keys.size()];
        int i = 0;
        for (String key : keys) {
            result[i] = modulesComptable.get(key);
            i++;
        }

        return result;

    }

    /**
     * Retourne une APIRubrique correspondant au code de la rubrique (codeRubrique) passé en paramètre</br>
     * 
     * @param codeRubrique
     *            le code de la rubrique voulue.<strong> Ce code rubrique doit avoir une correspondance dans le fichier
     *            de constantes APIReferenceRubrique</strong>
     * @return Une APIRubrique correspondant au code de la rubrique
     * @throws Exception
     *             Dans le cas ou le codeRubrique est null OU si aucune APIRubrique correspond au codeRubrique
     */
    public APIRubrique getRubriqueComptablePC(String codeRubrique) throws Exception {
        if (JadeStringUtil.isEmpty(codeRubrique)) {
            throw new Exception("Can not return an APIRubrique for requestedrubrique [" + codeRubrique
                    + "] because rubrique is empty");
        }

        // AI
        if (codeRubrique.equals(APIReferenceRubrique.PC_AI_ALLOCATIONS_DE_NOEL)) {
            return PC_AI_ALLOCATIONS_DE_NOEL;
        } else if (codeRubrique.equals(APIReferenceRubrique.PC_AI)) {
            return PC_AI;
        } else if (codeRubrique.equals(APIReferenceRubrique.PC_AI_A_DOMICILE_ORDINAIRES_ESPECES)) {
            return PC_AI_DOMICILE_ORDINAIRES;
        } else if (codeRubrique.equals(APIReferenceRubrique.PC_AI_EN_HOME_SASH)) {
            return PC_AI_HOME_SASH;
        } else if (codeRubrique.equals(APIReferenceRubrique.PC_AI_EN_HOME_HORS_CANTON_SASH)) {
            return PC_AI_HOME_SASH_HORS_CANTON;
        } else if (codeRubrique.equals(APIReferenceRubrique.PC_AI_EN_HOME_SPAS)) {
            return PC_AI_HOME_SPAS;
        } else if (codeRubrique.equals(APIReferenceRubrique.PC_AI_EN_HOME_HORS_CANTON_SPAS)) {
            return PC_AI_HOME_SPAS_HORS_CANTON;
        } else if (codeRubrique.equals(APIReferenceRubrique.PC_AI_EN_HOME)) {
            return PC_AI_EN_HOME;
        }

        // AVS
        else if (codeRubrique.equals(APIReferenceRubrique.PC_AVS_ALLOCATIONS_DE_NOEL)) {
            return PC_AVS_ALLOCATIONS_DE_NOEL;
        } else if (codeRubrique.equals(APIReferenceRubrique.PC_AVS)) {
            return PC_AVS;
        } else if (codeRubrique.equals(APIReferenceRubrique.PC_AVS_A_DOMICILE_ORDINAIRES_ESPECES)) {
            return PC_AVS_A_DOMICILE_ORDINAIRES;
        } else if (codeRubrique.equals(APIReferenceRubrique.PC_AVS_EN_HOME_SASH)) {
            return PC_AVS_EN_HOME_SASH;
        } else if (codeRubrique.equals(APIReferenceRubrique.PC_AVS_EN_HOME_HORS_CANTON_SASH)) {
            return PC_AVS_EN_HOME_HORS_CANTON_SASH;
        } else if (codeRubrique.equals(APIReferenceRubrique.PC_AVS_EN_HOME_SPAS)) {
            return PC_AVS_EN_HOME_SPAS;
        } else if (codeRubrique.equals(APIReferenceRubrique.PC_AVS_EN_HOME_HORS_CANTON_SPAS)) {
            return PC_AVS_EN_HOME_HORS_CANTON_SPAS;
        } else if (codeRubrique.equals(APIReferenceRubrique.PC_AVS_EN_HOME)) {
            return PC_AVS_EN_HOME;
        }

        throw new Exception("Can not return an APIRubrique for requested rubrique [" + codeRubrique
                + "] because it's unknown");
    }

    /**
     * Retourne une APIRubrique correspondant au code de la rubrique (codeRubrique) passé en paramètre</br>
     * 
     * @param codeRubrique
     *            le code de la rubrique voulue.<strong> Ce code rubrique doit avoir une correspondance dans le fichier
     *            de constantes APIReferenceRubrique</strong>
     * @return Une APIRubrique correspondant au code de la rubrique
     * @throws Exception
     *             Dans le cas ou le codeRubrique est null OU si aucune APIRubrique correspond au codeRubrique
     */
    public APIRubrique getRubriqueComptableRFM(String codeRubrique) throws Exception {
        if (JadeStringUtil.isEmpty(codeRubrique)) {
            throw new Exception("Can not return an APIRubrique for requestedrubrique [" + codeRubrique
                    + "] because rubrique is empty");
        }

        // AI
        if (codeRubrique.equals(APIReferenceRubrique.RFM_AI)) {
            return RFM_AI;
        } else if (codeRubrique.equals(APIReferenceRubrique.RFM_AI_FRQP)) {
            return RFM_AI_FRANCHISE_ET_PARTICIPATION_FRQP;
        } else if (codeRubrique.equals(APIReferenceRubrique.RFM_AI_REGIME)) {
            return RFM_AI_REGIME_ALIMENTAIRE;
        }

        // AVS
        else if (codeRubrique.equals(APIReferenceRubrique.RFM_AVS)) {
            return RFM_AVS;
        } else if (codeRubrique.equals(APIReferenceRubrique.RFM_AVS_FRQP)) {
            return RFM_AVS_FRANCHISE_ET_PARTICIPATION_FRQP;
        } else if (codeRubrique.equals(APIReferenceRubrique.RFM_AVS_REGIME)) {
            return RFM_AVS_REGIME_ALIMENTAIRE;
        }

        throw new Exception("Can not return an APIRubrique for requested rubrique [" + codeRubrique
                + "] because it's unknown");
    }

    /**
     * Initialise les Id des rubriques
     * 
     * @param sessionOsiris
     *            une instance de APIProcessComptabilisation
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void initIdsRubriques(BISession sessionOsiris) throws Exception {
        APIReferenceRubrique referenceRubrique = (APIReferenceRubrique) sessionOsiris
                .getAPIFor(APIReferenceRubrique.class);

        RO_AVS = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RENTE_ORDINAIRE_AVS);
        REO_AVS = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RENTE_EXTRAORDINAIREAVS);
        API_AVS = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.API_AVS);
        RO_AI = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RENTE_ORDINAIRE_AI);
        REO_AI = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RENTE_EXTRAORDINAIRE_AI);
        API_AI = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.API_AI);

        RO_AVS_RETRO = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RENTE_ORDINAIRE_AVS_RETROACTIF);
        REO_AVS_RETRO = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RENTE_EXTRAORDINAIRE_AVS_RETROACTIF);
        API_AVS_RETRO = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.API_AVS_RETROACTIF);
        RO_AI_RETRO = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RENTE_ORDINAIRE_AI_RETROACTIF);
        REO_AI_RETRO = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RENTE_EXTRAORDINAIRE_AI_RETROACTIF);
        API_AI_RETRO = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.API_AI_RETROACTIF);

        RO_AVS_EXTOURNE = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RENTE_ORDINAIRE_AVS_EXTOURNE);
        REO_AVS_EXTOURNE = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RENTE_EXTRAORDINAIRE_AVS_EXTOURNE);
        API_AVS_EXTOURNE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.API_AVS_EXTOURNE);
        RO_AI_EXTOURNE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RENTE_ORDINAIRE_AI_EXTOURNE);
        REO_AI_EXTOURNE = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RENTE_EXTRAORDINAIRE_AI_EXTOURNE);
        API_AI_EXTOURNE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.API_AI_EXTOURNE);

        IMPOT_SOURCE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IMPOT_A_LA_SOURCE);
        REGULARISATION_CCP = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.REGULARISATION_CCP);
        PRST_AVS_RESTITUER = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.PRESTATION_AVS_A_RESTITUER);
        PRST_API_AVS_RESTITUER = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.PRESTATION_API_AVS_A_RESTITUER);
        PRST_AI_RESTITUER = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.PRESTATION_AI_A_RESTITUER);
        PRST_API_AI_RESTITUER = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.PRESTATION_API_AI_A_RESTITUER);
        INTERET_MORATOIRE_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.INTERETS_MORATOIRES_AI);
        INTERET_MORATOIRE_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.INTERETS_MORATOIRES_AVS);

        COMPENSATION = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.COMPENSATION_RENTES);

        // PC AI
        PC_AI_ALLOCATIONS_DE_NOEL = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.PC_AI_ALLOCATIONS_DE_NOEL);
        PC_AI = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_AI);
        PC_AI_DOMICILE_ORDINAIRES = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.PC_AI_A_DOMICILE_ORDINAIRES_ESPECES);
        PC_AI_HOME_SASH = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_AI_EN_HOME_SASH);
        PC_AI_HOME_SASH_HORS_CANTON = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.PC_AI_EN_HOME_HORS_CANTON_SASH);
        PC_AI_HOME_SPAS = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_AI_EN_HOME_SPAS);
        PC_AI_HOME_SPAS_HORS_CANTON = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.PC_AI_EN_HOME_HORS_CANTON_SPAS);
        PC_AI_EN_HOME = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_AI_EN_HOME);

        // PC AVS rubrique Initialization
        PC_AVS_ALLOCATIONS_DE_NOEL = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.PC_AVS_ALLOCATIONS_DE_NOEL);
        PC_AVS = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_AVS);
        PC_AVS_A_DOMICILE_ORDINAIRES = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.PC_AVS_A_DOMICILE_ORDINAIRES_ESPECES);
        PC_AVS_EN_HOME_SASH = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_AVS_EN_HOME_SASH);
        PC_AVS_EN_HOME_HORS_CANTON_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.PC_AVS_EN_HOME_HORS_CANTON_SASH);
        PC_AVS_EN_HOME_SPAS = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_AVS_EN_HOME_SPAS);
        PC_AVS_EN_HOME_HORS_CANTON_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.PC_AVS_EN_HOME_HORS_CANTON_SPAS);
        PC_AVS_EN_HOME = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_AVS_EN_HOME);

        RFM_AVS = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS);

        /**
         * Sous-types genre prestation RFM AVS
         */
        // this.RFM_AVS_STANDARD = referenceRubrique.
        // getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_STANDARD);
        RFM_AVS_FRANCHISE_ET_PARTICIPATION_FRQP = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_FRQP);
        // this.RFM_AVS_TRAITEMENT_DENTAIRE = referenceRubrique
        // .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_TRAITEMENT_DENTAIRE);
        // this.RFM_AVS_AIDE_AU_MENAGE = referenceRubrique
        // .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_AIDE_AU_MENAGE);
        // this.RFM_AVS_TRANSPORT =
        // referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_TRANSPORT);
        // this.RFM_AVS_ENCADREMENT_SOCIO_EDUC_ET_SECURITAIRE = referenceRubrique
        // .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_ENCADREMENT_SOCIO_EDUC_ET_SECURITAIRE);
        // this.RFM_AVS_COTISATION_AVS_PARITAIRE = referenceRubrique
        // .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_COTISATION_AVS_PARITAIRE);
        // this.RFM_AVS_MOYENS_AUX_APPAREILS_PROTHESE_LIT = referenceRubrique
        // .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_MOYENS_AUXILIAIRES);
        // this.RFM_AVS_SOINS_PAR_LA_FAMILLE = referenceRubrique
        // .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_SOINS_PAR_LA_FAMILLE);
        // this.RFM_AVS_UNITE_ACCUEIL_TEMPORAIRE = referenceRubrique
        // .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_UNITE_ACCUEIL_TEMPORAIRE);
        // this.RFM_AVS_PENSION_HOME_DE_JOUR = referenceRubrique
        // .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_PENSION_HOME_DE_JOUR);
        // this.RFM_AVS_PARTICIPATION_COURT_SEJOUR = referenceRubrique
        // .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_PARTICIPATION_COURT_SEJOUR);
        // this.RFM_AVS_FRAIS_PENSION_COURT_SEJOUR = referenceRubrique
        // .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_FRAIS_DE_PENSION_COURT_SEJOUR);
        RFM_AVS_REGIME_ALIMENTAIRE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_REGIME);

        RFM_AI = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI);

        /**
         * Sous-types genre prestation PCG AI
         */
        // this.RFM_AI_STANDARD = referenceRubrique.
        // getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_STANDARD);
        RFM_AI_FRANCHISE_ET_PARTICIPATION_FRQP = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_FRQP);
        // this.RFM_AI_TRAITEMENT_DENTAIRE = referenceRubrique
        // .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_TRAITEMENT_DENTAIRE);
        // this.RFM_AI_AIDE_AU_MENAGE = referenceRubrique
        // .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_AIDE_AU_MENAGE);
        // this.RFM_AI_TRANSPORT = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_TRANSPORT);
        // this.RFM_AI_ENCADREMENT_SOCIO_EDUC_ET_SECURITAIRE = referenceRubrique
        // .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_ENCADREMENT_SOCIO_EDUC_ET_SECURITAIRE);
        // this.RFM_AI_COTISATION_AVS_PARITAIRE = referenceRubrique
        // .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_COTISATION_AVS_PARITAIRE);
        // this.RFM_AI_MOYENS_AUX_APPAREILS_PROTHESE_LIT = referenceRubrique
        // .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_MOYENS_AUXILIAIRES);
        // this.RFM_AI_SOINS_PAR_LA_FAMILLE = referenceRubrique
        // .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_SOINS_PAR_LA_FAMILLE);
        // this.RFM_AI_UNITE_ACCUEIL_TEMPORAIRE = referenceRubrique
        // .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_UNITE_ACCUEIL_TEMPORAIRE);
        // this.RFM_AI_PENSION_HOME_DE_JOUR = referenceRubrique
        // .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_PENSION_HOME_DE_JOUR);
        // this.RFM_AI_PARTICIPATION_COURT_SEJOUR = referenceRubrique
        // .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_PARTICIPATION_COURT_SEJOUR);
        // this.RFM_AI_FRAIS_PENSION_COURT_SEJOUR = referenceRubrique
        // .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_FRAIS_DE_PENSION_COURT_SEJOUR);
        RFM_AI_REGIME_ALIMENTAIRE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_REGIME);
    }
}
