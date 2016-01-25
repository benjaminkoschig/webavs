/*
 * Créé le 7 oct. 05
 */
package globaz.ij.process;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEInputAnnonceLight;
import globaz.ij.api.annonces.IIJAnnonce;
import globaz.ij.application.IJApplication;
import globaz.ij.db.annonces.IJAnnonce;
import globaz.ij.db.annonces.IJAnnonceManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRSession;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJEnvoyerAnnoncesProcess extends BProcess {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String APPLICATION_ANNONCES = "HERMES";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private List annoncesAEnvoyer = new ArrayList();
    private String forDateEnvoi = "";

    private String forMoisAnneeComptable = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APEnvoyerAnnoncesProcess.
     */
    public IJEnvoyerAnnoncesProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe APEnvoyerAnnoncesProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public IJEnvoyerAnnoncesProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe APEnvoyerAnnoncesProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public IJEnvoyerAnnoncesProcess(BSession session) {
        super(session);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _executeProcess() {
        try {
            // On prend la date du jour qu'on mettra a toutes les annonces
            // envoyées (pour éviter le problème qui
            // surviendrait si le processus était lancé a 23H59 et se terminait
            // apres minuit.
            String date = JACalendar.todayJJsMMsAAAA();
            BSession session = getSession();
            BTransaction transaction = getTransaction();
            IJAnnonceManager mgr = null;
            IJAnnonce annonce = null;
            BStatement statement = null;
            // on doit envoyer dans l'ordre : les annonces du mois et annee
            // comptable en état validé , les annonces
            // erronnées, et si réannonce, les annonces qui ont le meme mois et
            // année comptable qu'une annonce envoyée a
            // la date d'envoi et les annonces envoyées a cette date dont le
            // mois et annee Comptable est différent

            // si il ne s'agit pas d'une réannonce
            if (JAUtil.isDateEmpty(forDateEnvoi)) {
                mgr = new IJAnnonceManager();
                mgr.setSession(session);
                mgr.setForCsEtat(IIJAnnonce.CS_VALIDE);
                mgr.setForMoisAnneeComptable(forMoisAnneeComptable);

                statement = mgr.cursorOpen(transaction);

                while ((annonce = (IJAnnonce) (mgr.cursorReadNext(statement))) != null) {
                    annonce.setCsEtat(IIJAnnonce.CS_ENVOYEE);
                    annonce.setDateEnvoi(date);
                    annonce.update(transaction);
                    prepareEnvoieAnnonce(annonce);
                }

                mgr.cursorClose(statement);
            }

            // Envoi des annonces erronnées
            mgr = new IJAnnonceManager();
            mgr.setSession(session);
            mgr.setForCsEtat(IIJAnnonce.CS_ERRONEE);

            statement = mgr.cursorOpen(transaction);
            while ((annonce = (IJAnnonce) (mgr.cursorReadNext(statement))) != null) {
                annonce.setCsEtat(IIJAnnonce.CS_ENVOYEE);
                annonce.setDateEnvoi(date);
                annonce.update(transaction);
                prepareEnvoieAnnonce(annonce);
            }

            mgr.cursorClose(statement);

            // si réenvois
            if (!JAUtil.isDateEmpty(forDateEnvoi)) {
                // envoi des annonces ayant la même date d'envoi et le même
                // moisAnneeComptable
                mgr = new IJAnnonceManager();
                mgr.setSession(session);
                mgr.setForDateEnvoi(forDateEnvoi);
                mgr.setForMoisAnneeComptable(forMoisAnneeComptable);
                statement = mgr.cursorOpen(transaction);

                while ((annonce = (IJAnnonce) (mgr.cursorReadNext(statement))) != null) {
                    prepareEnvoieAnnonce(annonce);
                }

                mgr.cursorClose(statement);

                // envoi des annonces ayant la même date d'envoi mais un
                // moisAnneeComptable différent.
                mgr = new IJAnnonceManager();
                mgr.setSession(session);
                mgr.setForDateEnvoi(forDateEnvoi);
                mgr.setForMoisAnneeComptableDifferentDe(forMoisAnneeComptable);
                statement = mgr.cursorOpen(transaction);

                while ((annonce = (IJAnnonce) (mgr.cursorReadNext(statement))) != null) {
                    prepareEnvoieAnnonce(annonce);
                }

                mgr.cursorClose(statement);
            }

            envoieAnnonces();
        } catch (Exception e) {
            try {
                getTransaction().rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, getSession().getLabel("ENVOYER_ANNONCES"));

            return false;
        }

        return true;
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate() throws Exception {
        if (getParent() == null) {
            if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
                setSendCompletionMail(false);
                setSendMailOnError(false);
            } else {
                setSendCompletionMail(true);
                setSendMailOnError(true);
            }

            setControleTransaction(getTransaction() == null);
        }

        if (getSession().hasErrors()) {
            abort();
        }
    }

    private void envoieAnnonces() throws Exception {
        BISession remoteSession = PRSession.connectSession(getSession(), IJEnvoyerAnnoncesProcess.APPLICATION_ANNONCES);

        // création de l'API
        IHEInputAnnonceLight remoteEcritureAnnonce = (IHEInputAnnonceLight) remoteSession
                .getAPIFor(IHEInputAnnonceLight.class);

        Iterator iter = annoncesAEnvoyer.iterator();

        while (iter.hasNext()) {
            remoteEcritureAnnonce.clear();
            remoteEcritureAnnonce.setIdProgramme(IJApplication.DEFAULT_APPLICATION_IJ);
            remoteEcritureAnnonce.setUtilisateur(getSession().getUserId());
            remoteEcritureAnnonce.setStatut(IHEAnnoncesViewBean.CS_EN_ATTENTE);

            Map element = (Map) iter.next();
            remoteEcritureAnnonce.putAll(element);
            remoteEcritureAnnonce.add(getTransaction());
        }
    }

    private String formatMoisAnneeComptable(String MM_AAAA) {
        return MM_AAAA.substring(0, 2) + MM_AAAA.substring(5);
    }

    private String formatXPosAppendWithBlank(int nombrePos, boolean isAppendLeft, String value) {
        StringBuffer result = new StringBuffer();

        if (JadeStringUtil.isEmpty(value)) {

            for (int i = 0; i < nombrePos; i++) {
                result.append(" ");
            }
        } else {
            int diff = nombrePos - value.length();
            // Append left
            if (isAppendLeft) {
                for (int i = 0; i < diff; i++) {
                    result.append(" ");
                }
                result.append(value);
            }
            // Append right
            else {
                result.append(value);
                for (int i = 0; i < diff; i++) {
                    result.append(" ");
                }
            }
        }
        return result.toString();
    }

    private String formatXPosAppendWithZero(int nombrePos, boolean isAppendLeft, String value) {
        StringBuffer result = new StringBuffer();

        if (JadeStringUtil.isEmpty(value)) {

            for (int i = 0; i < nombrePos; i++) {
                result.append("0");
            }
        } else {
            int diff = nombrePos - value.length();
            // Append left
            if (isAppendLeft) {
                for (int i = 0; i < diff; i++) {
                    result.append("0");
                }
                result.append(value);
            }
            // Append right
            else {
                result.append(value);
                for (int i = 0; i < diff; i++) {
                    result.append("0");
                }
            }
        }
        return result.toString();
    }

    /**
     * getter pour l'attribut EMail object
     * 
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {
        if (getMemoryLog().isOnErrorLevel() || getMemoryLog().isOnFatalLevel()) {
            return getSession().getLabel("PROCESS_ENVOI_ANNONCES_FAILED");
        } else {
            return getSession().getLabel("PROCESS_ENVOI_ANNONCES_SUCCESS");
        }
    }

    /**
     * getter pour l'attribut for date envoi
     * 
     * @return la valeur courante de l'attribut for date envoi
     */
    public String getForDateEnvoi() {
        return forDateEnvoi;
    }

    /**
     * getter pour l'attribut for mois annee comptable
     * 
     * @return la valeur courante de l'attribut for mois annee comptable
     */
    public String getForMoisAnneeComptable() {
        return forMoisAnneeComptable;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    private void prepareEnvoieAnnonce(IJAnnonce annonce) throws Exception {
        annonce.loadPeriodesAnnonces(getTransaction());

        if (annonce.getCodeApplication().equals("85")) {
            /*
             * 
             * 3 me Révision =================================================
             */

            Map attributs = new HashMap();

            FWCurrency totalAI = new FWCurrency(0);

            if (!JadeStringUtil.isIntegerEmpty(annonce.getPeriodeAnnonce1().getNombreJours())) {
                if (annonce.getPeriodeAnnonce1().getCodeValeurTotalIJ().equals("0")) {
                    totalAI.add(annonce.getPeriodeAnnonce1().getTotalIJ());
                } else if (annonce.getPeriodeAnnonce1().getCodeValeurTotalIJ().equals("1")) {
                    FWCurrency truc = new FWCurrency(annonce.getPeriodeAnnonce1().getTotalIJ());
                    truc.negate();
                    totalAI.add(truc);
                } else {
                    throw new Exception("données de l'annonce " + annonce.getIdAnnonce()
                            + " invalides (signe total ij)");
                }
            }

            if (!JadeStringUtil.isIntegerEmpty(annonce.getPeriodeAnnonce2().getNombreJours())) {
                if (annonce.getPeriodeAnnonce2().getCodeValeurTotalIJ().equals("0")) {
                    totalAI.add(annonce.getPeriodeAnnonce2().getTotalIJ());
                } else if (annonce.getPeriodeAnnonce2().getCodeValeurTotalIJ().equals("1")) {
                    FWCurrency truc = new FWCurrency(annonce.getPeriodeAnnonce2().getTotalIJ());
                    truc.negate();
                    totalAI.add(truc);
                } else {
                    throw new Exception("données de l'annonce " + annonce.getIdAnnonce()
                            + " invalides (signe total ij)");
                }
            }

            attributs.put(IHEAnnoncesViewBean.CODE_APPLICATION, annonce.getCodeApplication());
            attributs.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, annonce.getCodeEnregistrement());
            attributs.put(IHEAnnoncesViewBean.NUMERO_CAISSE, annonce.getNoCaisse());
            attributs.put(IHEAnnoncesViewBean.NUMERO_AGENCE, annonce.getNoAgence());
            attributs.put(IHEAnnoncesViewBean.CS_MOIS_COMPTABLE_ET_ANNEE,
                    formatMoisAnneeComptable(annonce.getMoisAnneeComptable()));
            attributs.put(IHEAnnoncesViewBean.CS_GENRE_DE_CARTE, annonce.getCodeGenreCarte());
            attributs.put(IHEAnnoncesViewBean.CS_NUMERO_ASSURE, JadeStringUtil.removeChar(annonce.getNoAssure(), '.'));

            if (!JadeStringUtil.isEmpty(annonce.getCodeEtatCivil())) {
                attributs.put(IHEAnnoncesViewBean.CS_ETAT_CIVIL_AYANT_DROIT, annonce.getCodeEtatCivil());
            }

            attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_ENFANTS,
                    formatXPosAppendWithZero(2, true, annonce.getNombreEnfants()));

            if (!JadeStringUtil.isEmpty(annonce.getRevenuJournalierDeterminant())) {
                attributs.put(IHEAnnoncesViewBean.CS_REVENU_JOURNALIER_MOYEN,
                        formatXPosAppendWithZero(6, true, unFormatMontant(annonce.getRevenuJournalierDeterminant())));
            }

            // Les offices AI sont toujours saisi sur 3 positions, pour
            // l'ancienne révision, on ne prend que les 2 derniers chiffres.
            if (!JadeStringUtil.isEmpty(annonce.getOfficeAI())) {
                attributs.put(
                        IHEAnnoncesViewBean.CS_OFFICE_AI,
                        formatXPosAppendWithBlank(2, true,
                                annonce.getOfficeAI().substring(1, annonce.getOfficeAI().length())));
            }

            if (!JadeStringUtil.isEmpty(annonce.getParamSpecifique3emeRevisionSur5Positions())) {
                attributs.put(IHEAnnoncesViewBean.CS_ALLOC_PERSONNE_SEULE, annonce
                        .getParamSpecifique3emeRevisionSur5Positions().substring(0, 1));
                attributs.put(IHEAnnoncesViewBean.CS_ALLOC_MENAGE, annonce
                        .getParamSpecifique3emeRevisionSur5Positions().substring(1, 2));
                attributs.put(IHEAnnoncesViewBean.CS_ALLOC_ASSISTANCE_JOUR, annonce
                        .getParamSpecifique3emeRevisionSur5Positions().substring(2, 3));
                attributs.put(IHEAnnoncesViewBean.CS_ALLOCATION_EXPLOITATION, annonce
                        .getParamSpecifique3emeRevisionSur5Positions().substring(3, 4));
                attributs.put(IHEAnnoncesViewBean.CS_SUPPLEMENT_DE_READAPTATION, annonce
                        .getParamSpecifique3emeRevisionSur5Positions().substring(4, 5));
            }

            if (!JadeStringUtil.isEmpty(annonce.getPetiteIJ())) {
                attributs.put(IHEAnnoncesViewBean.CS_PETITE_INDEMNITE_JOURNALIERE_AI, annonce.getPetiteIJ());
            }

            // Si aucun jour attesté pour 1ère période, on rempli avec les
            // données de la 2ème période
            if (JadeStringUtil.isIntegerEmpty(annonce.getPeriodeAnnonce1().getNombreJours())) {

                attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_DE_JOURS_PERIODE_1,
                        formatXPosAppendWithZero(3, true, annonce.getPeriodeAnnonce2().getNombreJours()));
                attributs.put(
                        IHEAnnoncesViewBean.CS_TAUX_JOURNALIER_MONTANT_FFFCC_PERIODE_1,
                        formatXPosAppendWithZero(5, true, unFormatMontant(annonce.getPeriodeAnnonce2()
                                .getTauxJournalier())));

                if (!JadeStringUtil.isEmpty(annonce.getCodeGenreReadaptation())) {
                    attributs.put(IHEAnnoncesViewBean.CS_GENRE_DE_READAPTATION_1_A_8_PERIODE_1,
                            formatXPosAppendWithZero(1, true, annonce.getCodeGenreReadaptation()));
                }

                if (!JadeStringUtil.isEmpty(annonce.getGarantieAA())) {
                    attributs.put(IHEAnnoncesViewBean.CS_GARANTIE_AA_PERIODE_1,
                            formatXPosAppendWithZero(1, true, annonce.getGarantieAA()));
                }

                if (!JadeStringUtil.isEmpty(annonce.getIjReduite())) {
                    attributs.put(IHEAnnoncesViewBean.CS_INDEMNITE_JOURNALIERE_REDUITE_PERIODE_1,
                            formatXPosAppendWithZero(1, true, annonce.getIjReduite()));
                }

                // 2ème période à blanc
                // mise à zéro des données de la 2ème périodes, car elles on été
                // inséré à la place de celle de la 1ère période.
                attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_DE_JOURS_PERIODE_2,
                        formatXPosAppendWithBlank(3, true, null));

                attributs.put(IHEAnnoncesViewBean.CS_TAUX_JOURNALIER_MONTANT_FFFCC_PERIODE_2,
                        formatXPosAppendWithBlank(5, true, null));

                attributs.put(IHEAnnoncesViewBean.CS_GENRE_DE_READAPTATION_1_A_8_PERIODE_2,
                        formatXPosAppendWithBlank(1, true, null));

                attributs.put(IHEAnnoncesViewBean.CS_GARANTIE_AA_PERIODE_2, formatXPosAppendWithBlank(1, true, null));

                attributs.put(IHEAnnoncesViewBean.CS_INDEMNITE_JOURNALIERE_REDUITE_PERIODE_2,
                        formatXPosAppendWithBlank(1, true, null));
            }

            // On rempli les données avec la 1ère période
            else {
                attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_DE_JOURS_PERIODE_1,
                        formatXPosAppendWithZero(3, true, annonce.getPeriodeAnnonce1().getNombreJours()));

                attributs.put(
                        IHEAnnoncesViewBean.CS_TAUX_JOURNALIER_MONTANT_FFFCC_PERIODE_1,
                        formatXPosAppendWithZero(5, true, unFormatMontant(annonce.getPeriodeAnnonce1()
                                .getTauxJournalier())));

                attributs.put(IHEAnnoncesViewBean.CS_GENRE_DE_READAPTATION_1_A_8_PERIODE_1,
                        formatXPosAppendWithZero(1, true, annonce.getCodeGenreReadaptation()));

                attributs.put(IHEAnnoncesViewBean.CS_GARANTIE_AA_PERIODE_1,
                        formatXPosAppendWithZero(1, true, annonce.getGarantieAA()));

                attributs.put(IHEAnnoncesViewBean.CS_INDEMNITE_JOURNALIERE_REDUITE_PERIODE_1,
                        formatXPosAppendWithZero(1, true, annonce.getIjReduite()));

                // 2ème période si elle existe pas... tout a blank
                if (annonce.getPeriodeAnnonce2().isNew()
                        && JadeStringUtil.isIntegerEmpty(annonce.getPeriodeAnnonce2().getNombreJours())) {
                    attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_DE_JOURS_PERIODE_2,
                            formatXPosAppendWithBlank(3, true, null));

                    attributs.put(IHEAnnoncesViewBean.CS_TAUX_JOURNALIER_MONTANT_FFFCC_PERIODE_2,
                            formatXPosAppendWithBlank(5, true, null));

                    attributs.put(IHEAnnoncesViewBean.CS_GENRE_DE_READAPTATION_1_A_8_PERIODE_2,
                            formatXPosAppendWithBlank(1, true, null));

                    attributs.put(IHEAnnoncesViewBean.CS_GARANTIE_AA_PERIODE_2,
                            formatXPosAppendWithBlank(1, true, null));

                    attributs.put(IHEAnnoncesViewBean.CS_INDEMNITE_JOURNALIERE_REDUITE_PERIODE_2,
                            formatXPosAppendWithBlank(1, true, null));

                }
                // sinon
                else {
                    attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_DE_JOURS_PERIODE_2,
                            formatXPosAppendWithZero(3, true, annonce.getPeriodeAnnonce2().getNombreJours()));
                    attributs.put(
                            IHEAnnoncesViewBean.CS_TAUX_JOURNALIER_MONTANT_FFFCC_PERIODE_2,
                            formatXPosAppendWithZero(5, true, unFormatMontant(annonce.getPeriodeAnnonce2()
                                    .getTauxJournalier())));

                    if (!JadeStringUtil.isEmpty(annonce.getCodeGenreReadaptation())) {
                        attributs.put(IHEAnnoncesViewBean.CS_GENRE_DE_READAPTATION_1_A_8_PERIODE_2,
                                formatXPosAppendWithZero(1, true, annonce.getCodeGenreReadaptation()));
                    }

                    if (!JadeStringUtil.isEmpty(annonce.getGarantieAA())) {
                        attributs.put(IHEAnnoncesViewBean.CS_GARANTIE_AA_PERIODE_2,
                                formatXPosAppendWithZero(1, true, annonce.getGarantieAA()));
                    }

                    if (!JadeStringUtil.isEmpty(annonce.getIjReduite())) {
                        attributs.put(IHEAnnoncesViewBean.CS_INDEMNITE_JOURNALIERE_REDUITE_PERIODE_2,
                                formatXPosAppendWithZero(1, true, annonce.getIjReduite()));
                    }
                }
            }

            // 3ème période, toujorus à blank.
            attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_DE_JOURS_PERIODE_3, formatXPosAppendWithBlank(3, true, null));

            attributs.put(IHEAnnoncesViewBean.CS_TAUX_JOURNALIER_MONTANT_FFFCC_PERIODE_3,
                    formatXPosAppendWithBlank(5, true, null));

            attributs.put(IHEAnnoncesViewBean.CS_GENRE_DE_READAPTATION_1_A_8_PERIODE_3,
                    formatXPosAppendWithBlank(1, true, null));

            attributs.put(IHEAnnoncesViewBean.CS_GARANTIE_AA_PERIODE_3, formatXPosAppendWithBlank(1, true, null));

            attributs.put(IHEAnnoncesViewBean.CS_INDEMNITE_JOURNALIERE_REDUITE_PERIODE_3,
                    formatXPosAppendWithBlank(1, true, null));

            boolean isPositive = false;

            if (totalAI.isPositive()) {
                isPositive = true;
            } else if (totalAI.isNegative()) {
                totalAI.negate();
            }

            attributs.put(IHEAnnoncesViewBean.CS_TOTAL_INDEMNITE_JOURNALIERE_AI_MONTANT_FFFFFCC,
                    formatXPosAppendWithZero(7, true, unFormatMontant(totalAI.toString())));
            attributs.put(IHEAnnoncesViewBean.CS_CODE_VALEUR_CHAMP_33, isPositive ? "0" : "1");
            attributs.put(IHEAnnoncesViewBean.CS_PERIODE_DE_JJMMAA, annonce.getPeriodeAnnonce1().getPeriodeDe());
            attributs.put(IHEAnnoncesViewBean.CS_PERIODE_A_JJMMAA, annonce.getPeriodeAnnonce1().getPeriodeA());

            // Cas spécial pour les annonces de type 3 et 4
            if ("3".equals(annonce.getCodeGenreCarte()) || "4".equals(annonce.getCodeGenreCarte())) {
                // Dans ce cas, certains champs doivent être mis à blanc
                attributs.put(IHEAnnoncesViewBean.CS_ETAT_CIVIL_AYANT_DROIT, formatXPosAppendWithBlank(1, true, null));
                attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_ENFANTS, formatXPosAppendWithBlank(2, true, null));
                attributs.put(IHEAnnoncesViewBean.CS_REVENU_JOURNALIER_MOYEN, formatXPosAppendWithBlank(6, true, null));
                attributs.put(IHEAnnoncesViewBean.CS_OFFICE_AI, formatXPosAppendWithBlank(2, true, null));

                attributs.put(IHEAnnoncesViewBean.CS_ALLOC_PERSONNE_SEULE, formatXPosAppendWithBlank(1, true, null));
                attributs.put(IHEAnnoncesViewBean.CS_ALLOC_MENAGE, formatXPosAppendWithBlank(1, true, null));
                attributs.put(IHEAnnoncesViewBean.CS_ALLOC_ASSISTANCE_JOUR, formatXPosAppendWithBlank(1, true, null));
                attributs.put(IHEAnnoncesViewBean.CS_ALLOCATION_EXPLOITATION, formatXPosAppendWithBlank(1, true, null));
                attributs.put(IHEAnnoncesViewBean.CS_SUPPLEMENT_DE_READAPTATION,
                        formatXPosAppendWithBlank(1, true, null));

                attributs.put(IHEAnnoncesViewBean.CS_PETITE_INDEMNITE_JOURNALIERE_AI,
                        formatXPosAppendWithBlank(1, true, null));

                attributs.put(IHEAnnoncesViewBean.CS_GENRE_DE_READAPTATION_1_A_8_PERIODE_1,
                        formatXPosAppendWithBlank(1, true, null));

                attributs.put(IHEAnnoncesViewBean.CS_GARANTIE_AA_PERIODE_1, formatXPosAppendWithBlank(1, true, null));

                attributs.put(IHEAnnoncesViewBean.CS_INDEMNITE_JOURNALIERE_REDUITE_PERIODE_1,
                        formatXPosAppendWithBlank(1, true, null));

                attributs.put(IHEAnnoncesViewBean.CS_GENRE_DE_READAPTATION_1_A_8_PERIODE_2,
                        formatXPosAppendWithBlank(1, true, null));

                attributs.put(IHEAnnoncesViewBean.CS_GARANTIE_AA_PERIODE_2, formatXPosAppendWithBlank(1, true, null));

                attributs.put(IHEAnnoncesViewBean.CS_INDEMNITE_JOURNALIERE_REDUITE_PERIODE_2,
                        formatXPosAppendWithBlank(1, true, null));

                attributs.put(IHEAnnoncesViewBean.CS_GENRE_DE_READAPTATION_1_A_8_PERIODE_3,
                        formatXPosAppendWithBlank(1, true, null));

                attributs.put(IHEAnnoncesViewBean.CS_GARANTIE_AA_PERIODE_3, formatXPosAppendWithBlank(1, true, null));

                attributs.put(IHEAnnoncesViewBean.CS_INDEMNITE_JOURNALIERE_REDUITE_PERIODE_3,
                        formatXPosAppendWithBlank(1, true, null));

            }

            annoncesAEnvoyer.add(attributs);
        } else {

            /*
             * 
             * 4 Eme Révision =================================================
             */
            Map attributs = new HashMap();

            // ////////////////////////////////////////////////////////////////////////
            // pour les petites et les grandes IJ
            // ////////////////////////////////////////////////////////////////////////
            if ("1".equals(annonce.getPetiteIJ()) || "2".equals(annonce.getPetiteIJ())) {

                attributs.put(IHEAnnoncesViewBean.CODE_APPLICATION, annonce.getCodeApplication());
                attributs.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, annonce.getCodeEnregistrement());
                attributs.put(IHEAnnoncesViewBean.NUMERO_CAISSE, annonce.getNoCaisse());
                attributs.put(IHEAnnoncesViewBean.NUMERO_AGENCE, annonce.getNoAgence());
                attributs.put(IHEAnnoncesViewBean.CS_MOIS_COMPTABLE_ET_ANNEE,
                        formatMoisAnneeComptable(annonce.getMoisAnneeComptable()));
                attributs.put(IHEAnnoncesViewBean.CS_CONTENU_DE_L_ANNONCE, annonce.getCodeGenreCarte());
                attributs.put(IHEAnnoncesViewBean.CS_GENRE_INDEMNITE_JOURNALIERE, annonce.getPetiteIJ());
                attributs.put(IHEAnnoncesViewBean.CS_NUMERO_ASSURE,
                        JadeStringUtil.removeChar(annonce.getNoAssure(), '.'));
                attributs.put(IHEAnnoncesViewBean.CS_NUMERO_ASSURE_AYANT_DROIT_CONJOINT,
                        JadeStringUtil.removeChar(annonce.getNoAssureConjoint(), '.'));

                attributs.put(IHEAnnoncesViewBean.CS_DROITS_ACQUIS_4_RéVISION_AI, annonce.getDroitAcquis4emeRevision());

                // Cas spécial pour les annonces de type 3 et 4
                if ("3".equals(annonce.getCodeGenreCarte()) || "4".equals(annonce.getCodeGenreCarte())) {
                    // Dans ce cas, certains champs doivent être mis à blanc
                    attributs.put(IHEAnnoncesViewBean.CS_ETAT_CIVIL_AYANT_DROIT,
                            formatXPosAppendWithBlank(1, true, null));
                    attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_ENFANTS, formatXPosAppendWithBlank(2, true, null));
                    attributs.put(IHEAnnoncesViewBean.CS_REVENU_JOURNALIER_MOYEN,
                            formatXPosAppendWithBlank(6, true, null));
                    attributs.put(IHEAnnoncesViewBean.CS_OFFICE_AI, formatXPosAppendWithBlank(3, true, null));

                    attributs.put(IHEAnnoncesViewBean.CS_GENRE_DE_READAPTATION,
                            formatXPosAppendWithBlank(1, true, null));

                    attributs.put(IHEAnnoncesViewBean.CS_GARANTIE_AA, formatXPosAppendWithBlank(1, true, null));

                    attributs.put(IHEAnnoncesViewBean.CS_INDEMNITE_JOURNALIERE_REDUITE,
                            formatXPosAppendWithBlank(1, true, null));

                    attributs.put(IHEAnnoncesViewBean.CS_DEDUCTION_NOURRITURE_LOGEMENT_PERIODE1,
                            formatXPosAppendWithBlank(1, true, null));

                    attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_JOURS_INTERR_READAPT_AVEC_IJ_P1,
                            formatXPosAppendWithBlank(3, true, null));
                    attributs.put(IHEAnnoncesViewBean.CS_MOTIFS_INTERRUPTION_PERIODE1,
                            formatXPosAppendWithBlank(1, true, null));

                    attributs.put(IHEAnnoncesViewBean.CS_VERSEMENT_INDEMNITE_JOURNALIERE_PERIODE1,
                            formatXPosAppendWithBlank(1, true, null));

                    attributs.put(IHEAnnoncesViewBean.CS_REVENU_JOURN_DET_NON_PLAFONNE_FFFFCC,
                            formatXPosAppendWithBlank(6, true, null));

                }

                else {
                    attributs.put(IHEAnnoncesViewBean.CS_ETAT_CIVIL_AYANT_DROIT, annonce.getCodeEtatCivil());
                    attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_ENFANTS, annonce.getNombreEnfants());
                    attributs.put(IHEAnnoncesViewBean.CS_OFFICE_AI,
                            formatXPosAppendWithBlank(3, true, annonce.getOfficeAI()));
                    attributs.put(IHEAnnoncesViewBean.CS_GENRE_DE_READAPTATION, annonce.getCodeGenreReadaptation());
                    attributs.put(IHEAnnoncesViewBean.CS_GARANTIE_AA, annonce.getGarantieAA());
                    attributs.put(IHEAnnoncesViewBean.CS_INDEMNITE_JOURNALIERE_REDUITE, annonce.getIjReduite());

                    attributs.put(IHEAnnoncesViewBean.CS_REVENU_JOURNALIER_MOYEN,
                            formatXPosAppendWithZero(6, true, annonce.getRevenuJournalierDeterminant()));

                    // Si petite IJ
                    if ("2".equals(annonce.getPetiteIJ())) {
                        attributs.put(IHEAnnoncesViewBean.CS_REVENU_JOURN_DET_NON_PLAFONNE_FFFFCC,
                                formatXPosAppendWithZero(6, true, null));
                    } else {
                        attributs.put(
                                IHEAnnoncesViewBean.CS_REVENU_JOURN_DET_NON_PLAFONNE_FFFFCC,
                                formatXPosAppendWithZero(6, true,
                                        unFormatMontant(annonce.getRevenuJournalierDeterminant())));
                    }
                }

                // 1ère période, incluse dans l'annonce 8G01
                // Si cette 1ère période a un montant total != 0, on insère les
                // données de cette période dans 8G01
                if (!JadeStringUtil.isIntegerEmpty(annonce.getPeriodeAnnonce1().getTotalIJ())) {

                    if (!"3".equals(annonce.getCodeGenreCarte()) && !"4".equals(annonce.getCodeGenreCarte())) {

                        attributs.put(IHEAnnoncesViewBean.CS_DEDUCTION_NOURRITURE_LOGEMENT_PERIODE1, annonce
                                .getPeriodeAnnonce1().getDeductionNourritureLogement());

                        if (JadeStringUtil.isIntegerEmpty(annonce.getPeriodeAnnonce1().getNombreJoursInterruption())) {
                            attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_JOURS_INTERR_READAPT_AVEC_IJ_P1,
                                    formatXPosAppendWithBlank(3, true, null));

                            attributs.put(IHEAnnoncesViewBean.CS_MOTIFS_INTERRUPTION_PERIODE1,
                                    formatXPosAppendWithBlank(1, true, null));
                        } else {
                            attributs.put(
                                    IHEAnnoncesViewBean.CS_NOMBRE_JOURS_INTERR_READAPT_AVEC_IJ_P1,
                                    formatXPosAppendWithZero(3, true, annonce.getPeriodeAnnonce1()
                                            .getNombreJoursInterruption()));

                            attributs.put(IHEAnnoncesViewBean.CS_MOTIFS_INTERRUPTION_PERIODE1, annonce
                                    .getPeriodeAnnonce1().getCodeMotifInterruption());
                        }

                        attributs.put(IHEAnnoncesViewBean.CS_VERSEMENT_INDEMNITE_JOURNALIERE_PERIODE1, annonce
                                .getPeriodeAnnonce1().getVersementIJ());
                    }

                    attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_DE_JOURS_PERIODE1,
                            formatXPosAppendWithZero(3, true, annonce.getPeriodeAnnonce1().getNombreJours()));
                    attributs.put(IHEAnnoncesViewBean.CS_TAUX_JOURNALIER_MONTANT_FFFCC_PERIODE1,
                            unFormatMontant(annonce.getPeriodeAnnonce1().getTauxJournalier()));

                    attributs.put(IHEAnnoncesViewBean.CS_TOTAL_INDEMN_JOURN_AI_MONTANT_FFFFFCC_P1,
                            unFormatMontant(annonce.getPeriodeAnnonce1().getTotalIJ()));
                    attributs.put(IHEAnnoncesViewBean.CS_CODE_VALEUR_DU_CHAMP_24_PERIODE1, annonce.getPeriodeAnnonce1()
                            .getCodeValeurTotalIJ());
                    attributs.put(IHEAnnoncesViewBean.CS_PERIODE_DE_JJMMAA_PERIODE1, annonce.getPeriodeAnnonce1()
                            .getPeriodeDe());
                    attributs.put(IHEAnnoncesViewBean.CS_PERIODE_A_JJMMAA_PERIODE1, annonce.getPeriodeAnnonce1()
                            .getPeriodeA());

                    attributs.put(IHEAnnoncesViewBean.CS_DROIT_PRESTATION_POUR_ENFANT_PERIODE_1, annonce
                            .getPeriodeAnnonce1().getDroitPrestationPourEnfant());
                    attributs.put(IHEAnnoncesViewBean.CS_GARANTIE_DROIT_ACQUIS_5_EME_REV_PERIODE_1, annonce
                            .getPeriodeAnnonce1().getGarantieDroitAcquis5emeRevision());
                    attributs.put(IHEAnnoncesViewBean.CS_NO_DECISION_AI_COMMUNICATION_PERIODE_1,
                            annonce.getNoDecisionAiCommunication());

                    annoncesAEnvoyer.add(attributs);

                    // 2Eme période si elle existe, création dans 8G02
                    if (!annonce.getPeriodeAnnonce2().isNew()
                            && !JadeStringUtil.isIntegerEmpty(annonce.getPeriodeAnnonce2().getTotalIJ())) {
                        Map attributsAnnonce2 = new HashMap();
                        attributsAnnonce2.put(IHEAnnoncesViewBean.CODE_APPLICATION, annonce.getCodeApplication());
                        attributsAnnonce2.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT,
                                annonce.getCodeEnregistrementSuivantPeriodeIJ2Et3());
                        attributsAnnonce2.put(IHEAnnoncesViewBean.CS_NOMBRE_DE_JOURS_PERIODE2,
                                formatXPosAppendWithZero(3, true, annonce.getPeriodeAnnonce2().getNombreJours()));
                        attributsAnnonce2.put(IHEAnnoncesViewBean.CS_TAUX_JOURNALIER_MONTANT_FFFCC_PERIODE2,
                                unFormatMontant(annonce.getPeriodeAnnonce2().getTauxJournalier()));

                        if (JadeStringUtil.isIntegerEmpty(annonce.getPeriodeAnnonce2().getNombreJoursInterruption())) {
                            attributsAnnonce2.put(IHEAnnoncesViewBean.CS_NOMBRE_JOURS_INTERR_READAPT_AVEC_IJ_P2,
                                    formatXPosAppendWithBlank(3, true, null));
                            attributsAnnonce2.put(IHEAnnoncesViewBean.CS_MOTIFS_INTERRUPTION_PERIODE2,
                                    formatXPosAppendWithBlank(1, true, null));

                        } else {

                            attributsAnnonce2.put(
                                    IHEAnnoncesViewBean.CS_NOMBRE_JOURS_INTERR_READAPT_AVEC_IJ_P2,
                                    formatXPosAppendWithZero(3, true, annonce.getPeriodeAnnonce2()
                                            .getNombreJoursInterruption()));
                            attributsAnnonce2.put(IHEAnnoncesViewBean.CS_MOTIFS_INTERRUPTION_PERIODE2, annonce
                                    .getPeriodeAnnonce2().getCodeMotifInterruption());
                        }

                        attributsAnnonce2.put(IHEAnnoncesViewBean.CS_VERSEMENT_INDEMNITE_JOURNALIERE_PERIODE2, annonce
                                .getPeriodeAnnonce2().getVersementIJ());
                        attributsAnnonce2.put(IHEAnnoncesViewBean.CS_TOTAL_INDEMN_JOURN_AI_MONTANT_FFFFFCC_P2,
                                unFormatMontant(annonce.getPeriodeAnnonce2().getTotalIJ()));
                        attributsAnnonce2.put(IHEAnnoncesViewBean.CS_CODE_VALEUR_DU_CHAMP_24_PERIODE2, annonce
                                .getPeriodeAnnonce2().getCodeValeurTotalIJ());
                        attributsAnnonce2.put(IHEAnnoncesViewBean.CS_PERIODE_DE_JJMMAA_PERIODE2, annonce
                                .getPeriodeAnnonce2().getPeriodeDe());
                        attributsAnnonce2.put(IHEAnnoncesViewBean.CS_PERIODE_A_JJMMAA_PERIODE2, annonce
                                .getPeriodeAnnonce2().getPeriodeA());

                        attributsAnnonce2.put(IHEAnnoncesViewBean.CS_DEDUCTION_NOURRITURE_LOGEMENT_PERIODE2, annonce
                                .getPeriodeAnnonce2().getDeductionNourritureLogement());

                        // Cas spécial pour les annonces de type 3 et 4
                        if ("3".equals(annonce.getCodeGenreCarte()) || "4".equals(annonce.getCodeGenreCarte())) {

                            attributsAnnonce2.put(IHEAnnoncesViewBean.CS_DEDUCTION_NOURRITURE_LOGEMENT_PERIODE2,
                                    formatXPosAppendWithBlank(1, true, null));

                            attributsAnnonce2.put(IHEAnnoncesViewBean.CS_NOMBRE_JOURS_INTERR_READAPT_AVEC_IJ_P2,
                                    formatXPosAppendWithBlank(3, true, null));
                            attributsAnnonce2.put(IHEAnnoncesViewBean.CS_MOTIFS_INTERRUPTION_PERIODE2,
                                    formatXPosAppendWithBlank(1, true, null));
                            attributsAnnonce2.put(IHEAnnoncesViewBean.CS_VERSEMENT_INDEMNITE_JOURNALIERE_PERIODE2,
                                    formatXPosAppendWithBlank(1, true, null));
                        }

                        attributsAnnonce2.put(IHEAnnoncesViewBean.CS_DROIT_PRESTATION_POUR_ENFANT_PERIODE_2, annonce
                                .getPeriodeAnnonce2().getDroitPrestationPourEnfant());
                        attributsAnnonce2.put(IHEAnnoncesViewBean.CS_GARANTIE_DROIT_ACQUIS_5_EME_REV_PERIODE_2, annonce
                                .getPeriodeAnnonce2().getGarantieDroitAcquis5emeRevision());

                        annoncesAEnvoyer.add(attributsAnnonce2);

                    }
                }
                // Aucune indemnisation pour la première période, on va donc
                // récupérer les info de la 2ème période
                // et les inséré dans la 8G01; on ne générera pas d'annonce 8G02
                else {

                    if (!"3".equals(annonce.getCodeGenreCarte()) && !"4".equals(annonce.getCodeGenreCarte())) {

                        attributs.put(IHEAnnoncesViewBean.CS_DEDUCTION_NOURRITURE_LOGEMENT_PERIODE1, annonce
                                .getPeriodeAnnonce2().getDeductionNourritureLogement());

                        if (JadeStringUtil.isIntegerEmpty(annonce.getPeriodeAnnonce2().getNombreJoursInterruption())) {
                            attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_JOURS_INTERR_READAPT_AVEC_IJ_P1,
                                    formatXPosAppendWithBlank(3, true, null));

                            attributs.put(IHEAnnoncesViewBean.CS_MOTIFS_INTERRUPTION_PERIODE1,
                                    formatXPosAppendWithBlank(1, true, null));
                        } else {
                            attributs.put(
                                    IHEAnnoncesViewBean.CS_NOMBRE_JOURS_INTERR_READAPT_AVEC_IJ_P1,
                                    formatXPosAppendWithZero(3, true, annonce.getPeriodeAnnonce2()
                                            .getNombreJoursInterruption()));

                            attributs.put(IHEAnnoncesViewBean.CS_MOTIFS_INTERRUPTION_PERIODE1, annonce
                                    .getPeriodeAnnonce2().getCodeMotifInterruption());
                        }

                        attributs.put(IHEAnnoncesViewBean.CS_VERSEMENT_INDEMNITE_JOURNALIERE_PERIODE1, annonce
                                .getPeriodeAnnonce2().getVersementIJ());
                    }

                    attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_DE_JOURS_PERIODE1,
                            formatXPosAppendWithZero(3, true, annonce.getPeriodeAnnonce2().getNombreJours()));
                    attributs.put(IHEAnnoncesViewBean.CS_TAUX_JOURNALIER_MONTANT_FFFCC_PERIODE1,
                            unFormatMontant(annonce.getPeriodeAnnonce2().getTauxJournalier()));

                    attributs.put(IHEAnnoncesViewBean.CS_TOTAL_INDEMN_JOURN_AI_MONTANT_FFFFFCC_P1,
                            unFormatMontant(annonce.getPeriodeAnnonce2().getTotalIJ()));
                    attributs.put(IHEAnnoncesViewBean.CS_CODE_VALEUR_DU_CHAMP_24_PERIODE1, annonce.getPeriodeAnnonce2()
                            .getCodeValeurTotalIJ());
                    attributs.put(IHEAnnoncesViewBean.CS_PERIODE_DE_JJMMAA_PERIODE1, annonce.getPeriodeAnnonce2()
                            .getPeriodeDe());
                    attributs.put(IHEAnnoncesViewBean.CS_PERIODE_A_JJMMAA_PERIODE1, annonce.getPeriodeAnnonce2()
                            .getPeriodeA());

                    attributs.put(IHEAnnoncesViewBean.CS_DROIT_PRESTATION_POUR_ENFANT_PERIODE_1, annonce
                            .getPeriodeAnnonce2().getDroitPrestationPourEnfant());
                    attributs.put(IHEAnnoncesViewBean.CS_GARANTIE_DROIT_ACQUIS_5_EME_REV_PERIODE_1, annonce
                            .getPeriodeAnnonce2().getGarantieDroitAcquis5emeRevision());
                    attributs.put(IHEAnnoncesViewBean.CS_NO_DECISION_AI_COMMUNICATION_PERIODE_1,
                            annonce.getNoDecisionAiCommunication());
                    annoncesAEnvoyer.add(attributs);
                }

            }
            // ////////////////////////////////////////////////////////////////////////
            // pour les annonces AIT
            // ////////////////////////////////////////////////////////////////////////
            else if ("3".equals(annonce.getPetiteIJ())) {

                attributs.put(IHEAnnoncesViewBean.CODE_APPLICATION, annonce.getCodeApplication());
                attributs.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, annonce.getCodeEnregistrement());
                attributs.put(IHEAnnoncesViewBean.NUMERO_CAISSE, annonce.getNoCaisse());
                attributs.put(IHEAnnoncesViewBean.NUMERO_AGENCE, annonce.getNoAgence());
                attributs.put(IHEAnnoncesViewBean.CS_MOIS_COMPTABLE_ET_ANNEE,
                        formatMoisAnneeComptable(annonce.getMoisAnneeComptable()));
                attributs.put(IHEAnnoncesViewBean.CS_CONTENU_DE_L_ANNONCE, annonce.getCodeGenreCarte());
                attributs.put(IHEAnnoncesViewBean.CS_GENRE_INDEMNITE_JOURNALIERE, annonce.getPetiteIJ());
                attributs.put(IHEAnnoncesViewBean.CS_NUMERO_ASSURE,
                        JadeStringUtil.removeChar(annonce.getNoAssure(), '.'));
                attributs.put(IHEAnnoncesViewBean.CS_ETAT_CIVIL_AYANT_DROIT,
                        formatXPosAppendWithBlank(1, true, annonce.getCodeEtatCivil()));
                attributs.put(IHEAnnoncesViewBean.CS_OFFICE_AI,
                        formatXPosAppendWithBlank(3, true, annonce.getOfficeAI()));

                // 1ère période, incluse dans l'annonce 8G01
                // uniquement pour le numero de la decision AI
                attributs.put(IHEAnnoncesViewBean.CS_NO_DECISION_AI_COMMUNICATION_PERIODE_1,
                        formatXPosAppendWithZero(27, true, annonce.getNoDecisionAiCommunication()));

                // si restitution ou paiement retroactif les champs sont a blanc
                if ("3".equals(annonce.getCodeGenreCarte()) || "4".equals(annonce.getCodeGenreCarte())) {

                    attributs.put(IHEAnnoncesViewBean.CS_GARANTIE_AA, formatXPosAppendWithBlank(1, true, null));
                    attributs.put(IHEAnnoncesViewBean.CS_INDEMNITE_JOURNALIERE_REDUITE,
                            formatXPosAppendWithBlank(1, true, null));
                    attributs.put(IHEAnnoncesViewBean.CS_DEDUCTION_NOURRITURE_LOGEMENT_PERIODE1,
                            formatXPosAppendWithBlank(1, true, null));
                    attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_ENFANTS, formatXPosAppendWithBlank(2, true, null));
                    attributs.put(IHEAnnoncesViewBean.CS_REVENU_JOURN_DET_NON_PLAFONNE_FFFFCC,
                            formatXPosAppendWithBlank(6, true, null));

                    // bz-5528
                    // attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_DE_JOURS_PERIODE1, formatXPosAppendWithBlank(3, true,
                    // null));

                    // attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_DE_JOURS_PERIODE1,
                    // this.formatXPosAppendWithZero(3, true, null));

                    // bz-6043
                    if ((annonce.getPeriodeAnnonce1() != null)
                            && !JadeStringUtil.isBlankOrZero((annonce.getPeriodeAnnonce1().getNombreJours()))) {
                        attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_DE_JOURS_PERIODE1,
                                formatXPosAppendWithZero(3, true, annonce.getPeriodeAnnonce1().getNombreJours()));
                    } else {
                        attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_DE_JOURS_PERIODE1,
                                formatXPosAppendWithZero(3, true, null));
                    }

                    // sinon les champs sont à 0
                } else {

                    attributs.put(IHEAnnoncesViewBean.CS_GARANTIE_AA, formatXPosAppendWithZero(1, true, null));
                    attributs.put(IHEAnnoncesViewBean.CS_INDEMNITE_JOURNALIERE_REDUITE,
                            formatXPosAppendWithZero(1, true, null));
                    attributs.put(IHEAnnoncesViewBean.CS_DEDUCTION_NOURRITURE_LOGEMENT_PERIODE1,
                            formatXPosAppendWithZero(1, true, null));
                    attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_ENFANTS, formatXPosAppendWithZero(2, true, null));
                    attributs.put(IHEAnnoncesViewBean.CS_REVENU_JOURN_DET_NON_PLAFONNE_FFFFCC,
                            formatXPosAppendWithZero(6, true, null));
                    attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_DE_JOURS_PERIODE1,
                            formatXPosAppendWithZero(3, true, null));

                }

                // set a 0
                attributs.put(IHEAnnoncesViewBean.CS_CODE_VALEUR_DU_CHAMP_24_PERIODE1,
                        formatXPosAppendWithZero(1, true, null));
                attributs.put(IHEAnnoncesViewBean.CS_DROIT_PRESTATION_POUR_ENFANT_PERIODE_1,
                        formatXPosAppendWithZero(1, true, null));
                attributs.put(IHEAnnoncesViewBean.CS_GARANTIE_DROIT_ACQUIS_5_EME_REV_PERIODE_1,
                        formatXPosAppendWithZero(1, true, null));

                // 2Eme période si elle existe, création dans 8G02
                Map attributsAnnonce2 = new HashMap();
                attributsAnnonce2.put(IHEAnnoncesViewBean.CODE_APPLICATION, annonce.getCodeApplication());
                attributsAnnonce2.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "02");

                attributsAnnonce2.put(IHEAnnoncesViewBean.CS_NOMBRE_DE_JOURS_PERIODE2,
                        formatXPosAppendWithZero(3, true, annonce.getPeriodeAnnonce1().getNombreJours()));
                attributsAnnonce2.put(IHEAnnoncesViewBean.CS_TAUX_JOURNALIER_MONTANT_FFFCC_PERIODE2,
                        unFormatMontant(annonce.getPeriodeAnnonce1().getTauxJournalier()));

                if (JadeStringUtil.isIntegerEmpty(annonce.getPeriodeAnnonce1().getNombreJoursInterruption())) {
                    attributsAnnonce2.put(IHEAnnoncesViewBean.CS_NOMBRE_JOURS_INTERR_READAPT_AVEC_IJ_P2,
                            formatXPosAppendWithBlank(3, true, null));
                    attributsAnnonce2.put(IHEAnnoncesViewBean.CS_MOTIFS_INTERRUPTION_PERIODE2,
                            formatXPosAppendWithBlank(1, true, null));

                } else {
                    attributsAnnonce2
                            .put(IHEAnnoncesViewBean.CS_NOMBRE_JOURS_INTERR_READAPT_AVEC_IJ_P2,
                                    formatXPosAppendWithZero(3, true, annonce.getPeriodeAnnonce1()
                                            .getNombreJoursInterruption()));
                    attributsAnnonce2.put(IHEAnnoncesViewBean.CS_MOTIFS_INTERRUPTION_PERIODE2, annonce
                            .getPeriodeAnnonce1().getCodeMotifInterruption());
                }

                attributsAnnonce2.put(IHEAnnoncesViewBean.CS_VERSEMENT_INDEMNITE_JOURNALIERE_PERIODE2, annonce
                        .getPeriodeAnnonce1().getVersementIJ());
                attributsAnnonce2.put(IHEAnnoncesViewBean.CS_CODE_VALEUR_DU_CHAMP_24_PERIODE2, annonce
                        .getPeriodeAnnonce1().getCodeValeurTotalIJ());
                attributsAnnonce2.put(IHEAnnoncesViewBean.CS_PERIODE_DE_JJMMAA_PERIODE2, annonce.getPeriodeAnnonce1()
                        .getPeriodeDe());
                attributsAnnonce2.put(IHEAnnoncesViewBean.CS_PERIODE_A_JJMMAA_PERIODE2, annonce.getPeriodeAnnonce1()
                        .getPeriodeA());

                // si restitution ou paiement retroactif les champs sont a blanc
                if ("3".equals(annonce.getCodeGenreCarte()) || "4".equals(annonce.getCodeGenreCarte())) {

                    attributsAnnonce2.put(IHEAnnoncesViewBean.CS_DEDUCTION_NOURRITURE_LOGEMENT_PERIODE2,
                            formatXPosAppendWithBlank(1, true, null));

                    // bz-???? selon tél. M. Erb
                    // set a 0
                    // attributsAnnonce2.put(IHEAnnoncesViewBean.CS_MONTANT_ALLOC_INIT_TRAVAIL_PERIODE_2,
                    // this.formatXPosAppendWithZero(7, true, null));
                    //
                    // attributsAnnonce2.put(
                    // IHEAnnoncesViewBean.CS_TOTAL_INDEMN_JOURN_AI_MONTANT_FFFFFCC_P2,
                    // this.formatXPosAppendWithZero(7, true,
                    // this.unFormatMontant(annonce.getPeriodeAnnonce1().getMontantAit())));

                    attributsAnnonce2.put(
                            IHEAnnoncesViewBean.CS_MONTANT_ALLOC_INIT_TRAVAIL_PERIODE_2,
                            formatXPosAppendWithZero(7, true, unFormatMontant(annonce.getPeriodeAnnonce1()
                                    .getMontantAit())));

                    attributsAnnonce2.put(IHEAnnoncesViewBean.CS_TOTAL_INDEMN_JOURN_AI_MONTANT_FFFFFCC_P2,
                            formatXPosAppendWithZero(7, true, null));

                    // set a 0, selon info. M. Erb.
                    attributsAnnonce2.put(IHEAnnoncesViewBean.CS_MONTANT_ALLOC_ASSISTANCE_PERIODE_2,
                            formatXPosAppendWithZero(7, true, null));

                    // sinon les champs sont à 0
                } else {

                    attributsAnnonce2.put(IHEAnnoncesViewBean.CS_DEDUCTION_NOURRITURE_LOGEMENT_PERIODE2,
                            formatXPosAppendWithZero(1, true, null));

                    attributsAnnonce2.put(
                            IHEAnnoncesViewBean.CS_MONTANT_ALLOC_INIT_TRAVAIL_PERIODE_2,
                            formatXPosAppendWithZero(7, true, unFormatMontant(annonce.getPeriodeAnnonce1()
                                    .getMontantAit())));

                    // set a blanc
                    attributsAnnonce2.put(IHEAnnoncesViewBean.CS_MONTANT_ALLOC_ASSISTANCE_PERIODE_2,
                            formatXPosAppendWithBlank(7, true, ""));
                }

                // set a 0
                attributsAnnonce2.put(IHEAnnoncesViewBean.CS_DROIT_PRESTATION_POUR_ENFANT_PERIODE_2,
                        formatXPosAppendWithZero(1, true, null));
                attributsAnnonce2.put(IHEAnnoncesViewBean.CS_GARANTIE_DROIT_ACQUIS_5_EME_REV_PERIODE_2,
                        formatXPosAppendWithZero(1, true, null));

                attributsAnnonce2.put(IHEAnnoncesViewBean.CS_MONTANT_ALLOC_INIT_TRAVAIL_PERIODE_3,
                        formatXPosAppendWithBlank(7, true, ""));
                attributsAnnonce2.put(IHEAnnoncesViewBean.CS_MONTANT_ALLOC_ASSISTANCE_PERIODE_3,
                        formatXPosAppendWithBlank(7, true, ""));

                annoncesAEnvoyer.add(attributs);
                annoncesAEnvoyer.add(attributsAnnonce2);

            }
            // ////////////////////////////////////////////////////////////////////////
            // pour les annonces d'allocation d'âssistance
            // ////////////////////////////////////////////////////////////////////////
            else {

                attributs.put(IHEAnnoncesViewBean.CODE_APPLICATION, annonce.getCodeApplication());
                attributs.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, annonce.getCodeEnregistrement());
                attributs.put(IHEAnnoncesViewBean.NUMERO_CAISSE, annonce.getNoCaisse());
                attributs.put(IHEAnnoncesViewBean.NUMERO_AGENCE, annonce.getNoAgence());
                attributs.put(IHEAnnoncesViewBean.CS_MOIS_COMPTABLE_ET_ANNEE,
                        formatMoisAnneeComptable(annonce.getMoisAnneeComptable()));
                attributs.put(IHEAnnoncesViewBean.CS_CONTENU_DE_L_ANNONCE, annonce.getCodeGenreCarte());
                attributs.put(IHEAnnoncesViewBean.CS_GENRE_INDEMNITE_JOURNALIERE, annonce.getPetiteIJ());
                attributs.put(IHEAnnoncesViewBean.CS_NUMERO_ASSURE,
                        JadeStringUtil.removeChar(annonce.getNoAssure(), '.'));
                attributs.put(IHEAnnoncesViewBean.CS_ETAT_CIVIL_AYANT_DROIT,
                        formatXPosAppendWithBlank(1, true, annonce.getCodeEtatCivil()));
                attributs.put(IHEAnnoncesViewBean.CS_OFFICE_AI,
                        formatXPosAppendWithBlank(3, true, annonce.getOfficeAI()));
                attributs.put(IHEAnnoncesViewBean.CS_GENRE_DE_READAPTATION, annonce.getCodeGenreReadaptation());

                // 1ère période, incluse dans l'annonce 8G01
                // uniquement pour le numero de la decision AI
                attributs.put(IHEAnnoncesViewBean.CS_NO_DECISION_AI_COMMUNICATION_PERIODE_1,
                        formatXPosAppendWithZero(27, true, annonce.getNoDecisionAiCommunication()));

                // si restitution ou paiement retroactif les champs sont a blanc
                if ("3".equals(annonce.getCodeGenreCarte()) || "4".equals(annonce.getCodeGenreCarte())) {

                    attributs.put(IHEAnnoncesViewBean.CS_GARANTIE_AA, formatXPosAppendWithBlank(1, true, null));
                    attributs.put(IHEAnnoncesViewBean.CS_INDEMNITE_JOURNALIERE_REDUITE,
                            formatXPosAppendWithBlank(1, true, null));
                    attributs.put(IHEAnnoncesViewBean.CS_DEDUCTION_NOURRITURE_LOGEMENT_PERIODE1,
                            formatXPosAppendWithBlank(1, true, null));
                    attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_ENFANTS, formatXPosAppendWithBlank(2, true, null));
                    attributs.put(IHEAnnoncesViewBean.CS_REVENU_JOURN_DET_NON_PLAFONNE_FFFFCC,
                            formatXPosAppendWithBlank(6, true, null));
                    attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_DE_JOURS_PERIODE1,
                            formatXPosAppendWithBlank(3, true, null));

                    // sinon les champs sont à 0
                } else {
                    attributs.put(IHEAnnoncesViewBean.CS_GARANTIE_AA, formatXPosAppendWithZero(1, true, null));
                    attributs.put(IHEAnnoncesViewBean.CS_INDEMNITE_JOURNALIERE_REDUITE,
                            formatXPosAppendWithZero(1, true, null));
                    attributs.put(IHEAnnoncesViewBean.CS_DEDUCTION_NOURRITURE_LOGEMENT_PERIODE1,
                            formatXPosAppendWithZero(1, true, null));
                    attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_ENFANTS, formatXPosAppendWithZero(2, true, null));
                    attributs.put(IHEAnnoncesViewBean.CS_REVENU_JOURN_DET_NON_PLAFONNE_FFFFCC,
                            formatXPosAppendWithZero(6, true, null));
                    attributs.put(IHEAnnoncesViewBean.CS_NOMBRE_DE_JOURS_PERIODE1,
                            formatXPosAppendWithZero(3, true, null));
                }

                // set a 0
                attributs.put(IHEAnnoncesViewBean.CS_CODE_VALEUR_DU_CHAMP_24_PERIODE1,
                        formatXPosAppendWithZero(1, true, null));
                attributs.put(IHEAnnoncesViewBean.CS_DROIT_PRESTATION_POUR_ENFANT_PERIODE_1,
                        formatXPosAppendWithZero(1, true, null));
                attributs.put(IHEAnnoncesViewBean.CS_GARANTIE_DROIT_ACQUIS_5_EME_REV_PERIODE_1,
                        formatXPosAppendWithZero(1, true, null));

                // 2Eme période création dans 8G02
                Map attributsAnnonce2 = new HashMap();
                attributsAnnonce2.put(IHEAnnoncesViewBean.CODE_APPLICATION, annonce.getCodeApplication());
                attributsAnnonce2.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "02");

                attributsAnnonce2.put(IHEAnnoncesViewBean.CS_NOMBRE_DE_JOURS_PERIODE2,
                        formatXPosAppendWithZero(3, true, annonce.getPeriodeAnnonce1().getNombreJours()));

                if (JadeStringUtil.isIntegerEmpty(annonce.getPeriodeAnnonce1().getNombreJoursInterruption())) {
                    attributsAnnonce2.put(IHEAnnoncesViewBean.CS_NOMBRE_JOURS_INTERR_READAPT_AVEC_IJ_P2,
                            formatXPosAppendWithBlank(3, true, null));
                    attributsAnnonce2.put(IHEAnnoncesViewBean.CS_MOTIFS_INTERRUPTION_PERIODE2,
                            formatXPosAppendWithBlank(1, true, null));

                } else {
                    attributsAnnonce2
                            .put(IHEAnnoncesViewBean.CS_NOMBRE_JOURS_INTERR_READAPT_AVEC_IJ_P2,
                                    formatXPosAppendWithZero(3, true, annonce.getPeriodeAnnonce1()
                                            .getNombreJoursInterruption()));
                    attributsAnnonce2.put(IHEAnnoncesViewBean.CS_MOTIFS_INTERRUPTION_PERIODE2, annonce
                            .getPeriodeAnnonce1().getCodeMotifInterruption());
                }

                attributsAnnonce2.put(IHEAnnoncesViewBean.CS_VERSEMENT_INDEMNITE_JOURNALIERE_PERIODE2, annonce
                        .getPeriodeAnnonce1().getVersementIJ());
                attributsAnnonce2.put(IHEAnnoncesViewBean.CS_CODE_VALEUR_DU_CHAMP_24_PERIODE2, annonce
                        .getPeriodeAnnonce1().getCodeValeurTotalIJ());
                attributsAnnonce2.put(IHEAnnoncesViewBean.CS_PERIODE_DE_JJMMAA_PERIODE2, annonce.getPeriodeAnnonce1()
                        .getPeriodeDe());
                attributsAnnonce2.put(IHEAnnoncesViewBean.CS_PERIODE_A_JJMMAA_PERIODE2, annonce.getPeriodeAnnonce1()
                        .getPeriodeA());
                attributsAnnonce2.put(
                        IHEAnnoncesViewBean.CS_MONTANT_ALLOC_ASSISTANCE_PERIODE_2,
                        formatXPosAppendWithZero(7, true, unFormatMontant(annonce.getPeriodeAnnonce1()
                                .getMontantAllocAssistance())));

                // si restitution ou paiement retroactif les champs sont a blanc
                if ("3".equals(annonce.getCodeGenreCarte()) || "4".equals(annonce.getCodeGenreCarte())) {

                    attributsAnnonce2.put(IHEAnnoncesViewBean.CS_DEDUCTION_NOURRITURE_LOGEMENT_PERIODE2,
                            formatXPosAppendWithBlank(1, true, null));

                    // sinon les champs sont à 0
                } else {

                    attributsAnnonce2.put(IHEAnnoncesViewBean.CS_DEDUCTION_NOURRITURE_LOGEMENT_PERIODE2,
                            formatXPosAppendWithZero(1, true, null));
                }

                // set a 0
                attributsAnnonce2.put(IHEAnnoncesViewBean.CS_DROIT_PRESTATION_POUR_ENFANT_PERIODE_2,
                        formatXPosAppendWithZero(1, true, null));
                attributsAnnonce2.put(IHEAnnoncesViewBean.CS_GARANTIE_DROIT_ACQUIS_5_EME_REV_PERIODE_2,
                        formatXPosAppendWithZero(1, true, null));

                // set a blanc
                attributsAnnonce2.put(IHEAnnoncesViewBean.CS_MONTANT_ALLOC_INIT_TRAVAIL_PERIODE_2,
                        formatXPosAppendWithBlank(7, true, ""));
                attributsAnnonce2.put(IHEAnnoncesViewBean.CS_MONTANT_ALLOC_INIT_TRAVAIL_PERIODE_3,
                        formatXPosAppendWithBlank(7, true, ""));
                attributsAnnonce2.put(IHEAnnoncesViewBean.CS_MONTANT_ALLOC_ASSISTANCE_PERIODE_3,
                        formatXPosAppendWithBlank(7, true, ""));

                annoncesAEnvoyer.add(attributs);
                annoncesAEnvoyer.add(attributsAnnonce2);

            }
        }

        getMemoryLog().logMessage(
                MessageFormat.format(getSession().getLabel("ANNONCE_PRETE_POUR_ENVOI"),
                        new Object[] { annonce.getIdAnnonce() }), FWMessage.INFORMATION,
                getSession().getLabel("ENVOYER_ANNONCES"));

    }

    /**
     * setter pour l'attribut for date envoi
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForDateEnvoi(String string) {
        forDateEnvoi = string;
    }

    /**
     * setter pour l'attribut for mois annee comptable
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForMoisAnneeComptable(String string) {
        forMoisAnneeComptable = string;
    }

    /**
     * supprime tous les caracteres qui ne sont pas des chiffres dans une chaine de caractere
     * 
     * @param montant
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */

    private String unFormatMontant(String montant) {
        StringBuffer montantDeformate = new StringBuffer();

        for (int i = 0; i < montant.length(); i++) {
            if (Character.isDigit(montant.charAt(i))) {
                montantDeformate.append(montant.charAt(i));
            }
        }

        return montantDeformate.toString();
    }

}
