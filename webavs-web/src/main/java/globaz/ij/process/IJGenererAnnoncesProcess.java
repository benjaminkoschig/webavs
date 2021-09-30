/*
 * Créé le 10 oct. 05
 */
package globaz.ij.process;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JAStringFormatter;
import globaz.ij.api.prestations.IIJPrestation;
import globaz.ij.api.prononces.IIJMesure;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.annonces.IJAnnonce;
import globaz.ij.db.annonces.IJGenerationAnnonce;
import globaz.ij.db.annonces.IJGenerationAnnonceManager;
import globaz.ij.db.annonces.IJPeriodeAnnonce;
import globaz.ij.db.prestations.IJIndemniteJournaliere;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.utils.IJUtils;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;

/**
 * <H1>process générant les annonces IJ</H1>
 *
 * <p>
 * NB : par soucis de clarté, ce process n'est pas optimisé pour la performance. Les champs des annonces sont créés un
 * par un suivant le document de format d'annonce a envoyer à la centrale
 * </p>
 *
 * @author dvh
 */
public class IJGenererAnnoncesProcess extends BProcess {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String idLot = "";
    private String moisAnneeComptable = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJGenererAnnoncesProcess.
     */
    public IJGenererAnnoncesProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe IJGenererAnnoncesProcess.
     *
     * @param process
     *            DOCUMENT ME!
     */
    public IJGenererAnnoncesProcess(BProcess process) {
        super(process);
    }

    /**
     * Crée une nouvelle instance de la classe IJGenererAnnoncesProcess.
     *
     * @param session
     *            DOCUMENT ME!
     */
    public IJGenererAnnoncesProcess(BSession session) {
        super(session);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BProcess#_executeProcess()
     *
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _executeProcess() {
        try {
            // parcours de toutes les prestations du lot
            IJGenerationAnnonceManager generationAnnonceManager = new IJGenerationAnnonceManager();
            generationAnnonceManager.setSession(getSession());
            generationAnnonceManager.setForIdLot(idLot);

            BStatement statement = generationAnnonceManager.cursorOpen(getTransaction());

            IJGenerationAnnonce generationAnnonce = null;

            while ((generationAnnonce = (IJGenerationAnnonce) generationAnnonceManager.cursorReadNext(statement)) != null) {

                // Efface l'annonce de la prestation si déjà existante, ne
                // devrait jamais arriver.
                { // debut bloc
                    try {
                        if (!JadeStringUtil.isIntegerEmpty(generationAnnonce.getIdAnnonce())) {
                            IJAnnonce annonce = new IJAnnonce();
                            annonce.setSession(getSession());
                            annonce.setIdAnnonce(generationAnnonce.getIdAnnonce());
                            annonce.retrieve(getTransaction());
                            annonce.delete(getTransaction());
                        }
                    } catch (Exception e) {
                        ;
                    }
                } // fin bloc

                // D0103 ne pas créer d'annonce si décompte avec montant à 0.-
                if (JadeNumericUtil.isEmptyOrZero(generationAnnonce.getMontantBrutPrestation())) {
                    continue;
                }

                IJAnnonce annonceACreer = new IJAnnonce();
                annonceACreer.setSession(getSession());

                IJPeriodeAnnonce periodeAnnonce1, periodeAnnonce2 = null;

                // une annonce de type AIT
                if (IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(generationAnnonce.getCsTypeIJ())) {

                    annonceACreer.setCodeApplication("8G");
                    annonceACreer.setCodeEnregistrement("01");
                    annonceACreer.setNoCaisse(CaisseHelperFactory.getInstance().getNoCaisse(
                            getSession().getApplication()));
                    annonceACreer.setNoAgence(CaisseHelperFactory.getInstance().getNoAgence(
                            getSession().getApplication()));
                    annonceACreer.setMoisAnneeComptable(moisAnneeComptable);
                    annonceACreer.setCodeGenreCarte(getSession().getCode(generationAnnonce.getCsTypePrestation()));

                    // le genre d'indemnite journaliere
                    annonceACreer.setPetiteIJ("3");

                    annonceACreer.setNoAssure(JAStringFormatter.deformatAvs(generationAnnonce.getNoAVSPrononce()));

                    annonceACreer.setCodeEtatCivil(PRACORConst.csEtatCivilHeraForIJToAcor(getSession(),
                                                                                          generationAnnonce.getCsEtatCivil()));

                    annonceACreer.setOfficeAI(generationAnnonce.getOfficeAI());

                    String noDecisionComplet = IJUtils.getNumeroDecisionIJAIComplet(getSession(),
                                                                                    generationAnnonce.getOfficeAI(), generationAnnonce.getNoAVSPrononce(),
                                                                                    generationAnnonce.getNoDecisionAiCommunication());

                    annonceACreer.setNoDecisionAiCommunication(noDecisionComplet);

                    // la periode 1
                    // en fait pas de periode 1 car le numero de decision AI /
                    // communication
                    // est dans l'annonce

                    // la periode 2 -> la periode 1
                    periodeAnnonce1 = new IJPeriodeAnnonce();

                    periodeAnnonce1.setNombreJours(generationAnnonce.getNbJoursInternes());
                    periodeAnnonce1.setTauxJournalier(generationAnnonce.getTauxJournalierInterne());
                    periodeAnnonce1.setNombreJoursInterruption(generationAnnonce.getNombreJoursInterruption());
                    periodeAnnonce1.setCodeMotifInterruption(getSession().getCode(
                            generationAnnonce.getCsMotifInterruption()));
                    periodeAnnonce1.setVersementIJ("2");

                    if (IIJPrestation.CS_RESTITUTION.equals(generationAnnonce.getCsTypePrestation())) {
                        periodeAnnonce1.setCodeValeurTotalIJ("1");
                    } else {
                        periodeAnnonce1.setCodeValeurTotalIJ("0");
                    }

                    periodeAnnonce1.setPeriodeDe(generationAnnonce.getPeriodeDe());
                    periodeAnnonce1.setPeriodeA(generationAnnonce.getPeriodeA());

                    FWCurrency currencyTotalIJ = new FWCurrency(generationAnnonce.getMontantBrutPrestation());
                    if (!currencyTotalIJ.isPositive()) {
                        currencyTotalIJ.negate();
                    }
                    periodeAnnonce1.setMontantAit(currencyTotalIJ.toString());

                }// une annonce de type allocation d'assistance
                else if (IIJPrononce.CS_ALLOC_ASSIST.equals(generationAnnonce.getCsTypeIJ())) {

                    annonceACreer.setCodeApplication("8G");
                    annonceACreer.setCodeEnregistrement("01");
                    annonceACreer.setNoCaisse(CaisseHelperFactory.getInstance().getNoCaisse(
                            getSession().getApplication()));
                    annonceACreer.setNoAgence(CaisseHelperFactory.getInstance().getNoAgence(
                            getSession().getApplication()));
                    annonceACreer.setMoisAnneeComptable(moisAnneeComptable);
                    annonceACreer.setCodeGenreCarte(getSession().getCode(generationAnnonce.getCsTypePrestation()));
                    annonceACreer.setCodeGenreReadaptation(getSession().getCode(
                            generationAnnonce.getCsGenreReadaptation()));

                    // le genre d'indemnite journaliere
                    annonceACreer.setPetiteIJ("4");

                    annonceACreer.setNoAssure(JAStringFormatter.deformatAvs(generationAnnonce.getNoAVSPrononce()));
                    annonceACreer.setCodeEtatCivil(PRACORConst.csEtatCivilHeraForIJToAcor(getSession(),
                                                                                          generationAnnonce.getCsEtatCivil()));
                    annonceACreer.setOfficeAI(generationAnnonce.getOfficeAI());

                    String noDecisionComplet = IJUtils.getNumeroDecisionIJAIComplet(getSession(),
                                                                                    generationAnnonce.getOfficeAI(), generationAnnonce.getNoAVSPrononce(),
                                                                                    generationAnnonce.getNoDecisionAiCommunication());

                    annonceACreer.setNoDecisionAiCommunication(noDecisionComplet);

                    // la periode 1
                    // en fait pas de periode 1 car le numero de decision AI /
                    // communication
                    // est dans l'annonce

                    // la periode 2 -> la periode 1
                    periodeAnnonce1 = new IJPeriodeAnnonce();

                    periodeAnnonce1.setNombreJours(generationAnnonce.getNbJoursInternes());
                    periodeAnnonce1.setNombreJoursInterruption(generationAnnonce.getNombreJoursInterruption());
                    periodeAnnonce1.setCodeMotifInterruption(getSession().getCode(
                            generationAnnonce.getCsMotifInterruption()));
                    periodeAnnonce1.setVersementIJ("1");

                    if (IIJPrestation.CS_RESTITUTION.equals(generationAnnonce.getCsTypePrestation())) {
                        periodeAnnonce1.setCodeValeurTotalIJ("1");
                    } else {
                        periodeAnnonce1.setCodeValeurTotalIJ("0");
                    }

                    periodeAnnonce1.setPeriodeDe(generationAnnonce.getPeriodeDe());
                    periodeAnnonce1.setPeriodeA(generationAnnonce.getPeriodeA());

                    FWCurrency currencyTotalIJ = new FWCurrency(generationAnnonce.getMontantBrutPrestation());
                    if (!currencyTotalIJ.isPositive()) {
                        currencyTotalIJ.negate();
                    }

                    periodeAnnonce1.setMontantAllocAssistance(currencyTotalIJ.toString());

                } // pour les grandes et les petites IJ, on regarde la date de
                // la prestation pour savoir quel type d'annonce générer

                /*
                 *
                 * Grande IJ / Petite IJ -- 8G
                 */

                // else if
                // (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(),
                // generationAnnonce.getDateDebut(),
                // ((IJApplication) getSession().getApplication())
                // .getProperty(IJApplication.PROPERTY_DATE_DEBUT_4EME_REVISION)))
                // {

                else {

                    // nouveau droit
                    annonceACreer.setCodeApplication("8G");
                    annonceACreer.setCodeEnregistrement("01");
                    annonceACreer.setNoCaisse(CaisseHelperFactory.getInstance().getNoCaisse(
                            getSession().getApplication()));
                    annonceACreer.setNoAgence(CaisseHelperFactory.getInstance().getNoAgence(
                            getSession().getApplication()));
                    annonceACreer.setMoisAnneeComptable(moisAnneeComptable);
                    annonceACreer.setCodeGenreCarte(getSession().getCode(generationAnnonce.getCsTypePrestation()));

                    annonceACreer.setPetiteIJ(generationAnnonce.getCsTypeIJ().equals(IIJPrononce.CS_PETITE_IJ) ? "2"
                                                      : "1");
                    annonceACreer.setNoAssure(JAStringFormatter.deformatAvs(generationAnnonce.getNoAVSPrononce()));
                    annonceACreer.setNoAssureConjoint(JAStringFormatter.deformatAvs(generationAnnonce
                                                                                            .getNoAssureConjoint()));
                    annonceACreer.setCodeEtatCivil(PRACORConst.csEtatCivilHeraForIJToAcor(getSession(),
                                                                                          generationAnnonce.getCsEtatCivil()));
                    annonceACreer.setNombreEnfants(generationAnnonce.getNombreEnfants());
                    annonceACreer.setRevenuJournalierDeterminant(generationAnnonce
                                                                         .getRevenuMoyenDeterminantNonPlafonne());
                    annonceACreer.setOfficeAI(generationAnnonce.getOfficeAI());
                    annonceACreer.setCodeGenreReadaptation(getSession().getCode(
                            generationAnnonce.getCsGenreReadaptation()));
                    annonceACreer
                            .setGarantieAA(JadeStringUtil.isDecimalEmpty(generationAnnonce.getMontantGarantieAA()) ? "0"
                                                   : "1");
                    annonceACreer.setIjReduite(generationAnnonce.getGarantieAAReduite().booleanValue() ? "1" : "0");

                    String noRevision = generationAnnonce.getNoRevision();
                    if (!JadeStringUtil.isBlankOrZero(noRevision)) {
                        if (3 == Integer.valueOf(noRevision).intValue()) {

                            // Ce champ siginifie : Garantie des droit acquis de
                            // la 3ème révision
                            // pour une annonce de 4ème révision
                            annonceACreer.setDroitAcquis4emeRevision("1");
                        } else {
                            annonceACreer.setDroitAcquis4emeRevision("0");
                        }
                    }

                    String noDecisionComplet = IJUtils.getNumeroDecisionIJAIComplet(getSession(),
                                                                                    generationAnnonce.getOfficeAI(), generationAnnonce.getNoAVSPrononce(),
                                                                                    generationAnnonce.getNoDecisionAiCommunication());

                    annonceACreer.setNoDecisionAiCommunication(noDecisionComplet);

                    // première période (il y en a forcément une) On prend
                    // l'interne et si elle n'existe pas, l'externe
                    IJIndemniteJournaliere indemniteJournaliere = generationAnnonce.getIndemnitesJournalieresInternes();

                    // Externe
                    if (indemniteJournaliere == null) {
                        indemniteJournaliere = generationAnnonce.getIndemnitesJournalieresExternes();
                    }

                    // nb jours et total IJ
                    String totalIJ = null;
                    periodeAnnonce1 = new IJPeriodeAnnonce();
                    String montantJournalier = null;
                    if (indemniteJournaliere.getCsTypeIndemnisation().equals(IIJMesure.CS_INTERNE)) {
                        periodeAnnonce1.setDeductionNourritureLogement("1");
                        periodeAnnonce1.setNombreJours(generationAnnonce.getNbJoursInternes());

                        totalIJ = (generationAnnonce.getMontantBrutInterne());
                        montantJournalier = generationAnnonce.getTauxJournalierInterne();
                    } else {
                        periodeAnnonce1.setDeductionNourritureLogement("0");
                        periodeAnnonce1.setNombreJours(generationAnnonce.getNbJoursExternes());
                        totalIJ = (generationAnnonce.getMontantBrutExterne());
                        montantJournalier = generationAnnonce.getTauxJournalierExterne();
                    }

                    if (JadeStringUtil.isNull(montantJournalier)) {
                        montantJournalier = "";
                    }

                    if (IIJPrestation.CS_RESTITUTION.equals(generationAnnonce.getCsTypePrestation())) {
                        periodeAnnonce1.setCodeValeurTotalIJ("1");
                    } else {
                        periodeAnnonce1.setCodeValeurTotalIJ("0");
                    }

                    FWCurrency currencyTotalIJ = new FWCurrency(totalIJ);
                    if (!currencyTotalIJ.isPositive()) {
                        currencyTotalIJ.negate();
                    }
                    periodeAnnonce1.setTotalIJ(currencyTotalIJ.toString());

                    // taux journalier
                    periodeAnnonce1.setTauxJournalier(montantJournalier);

                    // nb jours interruption
                    periodeAnnonce1.setNombreJoursInterruption(generationAnnonce.getNombreJoursInterruption());

                    // motif interruption
                    periodeAnnonce1.setCodeMotifInterruption(getSession().getCode(
                            generationAnnonce.getCsMotifInterruption()));

                    // versement
                    periodeAnnonce1.setVersementIJ(generationAnnonce.getCodeVersement());

                    // periodeDe, periodeA
                    periodeAnnonce1.setPeriodeDe(generationAnnonce.getPeriodeDe());
                    periodeAnnonce1.setPeriodeA(generationAnnonce.getPeriodeA());

                    // Droit à la prestation pour enfant : 1 = oui, 0 = non
                    if (generationAnnonce.getIsDroitPrestationPourEnfant() != null
                            && generationAnnonce.getIsDroitPrestationPourEnfant().booleanValue()) {

                        periodeAnnonce1.setDroitPrestationPourEnfant("1");
                    } else {
                        periodeAnnonce1.setDroitPrestationPourEnfant("0");
                    }

                    // Garantie des droits acquis 5ème r évision AI : 0 = Non, 1
                    // = Oui
                    if (!JadeStringUtil.isBlankOrZero(noRevision)) {
                        if (4 == Integer.valueOf(noRevision).intValue()) {

                            // Ce champ siginifie : Garantie des droit acquis de
                            // la 4ème révision
                            // pour une annonce de 5ème révision
                            periodeAnnonce1.setGarantieDroitAcquis5emeRevision("1");
                        } else {
                            periodeAnnonce1.setGarantieDroitAcquis5emeRevision("0");
                        }
                    }

                    // 2eme periode si elle existe

                    if (indemniteJournaliere.getCsTypeIndemnisation().equals(IIJMesure.CS_INTERNE)) {
                        indemniteJournaliere = generationAnnonce.getIndemnitesJournalieresExternes();
                    } else {
                        indemniteJournaliere = null;
                    }

                    if (indemniteJournaliere != null) {
                        periodeAnnonce2 = new IJPeriodeAnnonce();

                        periodeAnnonce2.setDeductionNourritureLogement("0");
                        annonceACreer.setCodeEnregistrementSuivantPeriodeIJ2Et3("02");

                        // nb jours et total IJ
                        periodeAnnonce2.setNombreJours(generationAnnonce.getNbJoursExternes());
                        totalIJ = (generationAnnonce.getMontantBrutExterne());
                        currencyTotalIJ = new FWCurrency(totalIJ);

                        montantJournalier = generationAnnonce.getTauxJournalierExterne();
                        if (JadeStringUtil.isNull(montantJournalier)) {
                            montantJournalier = "";
                        }

                        if (currencyTotalIJ.isPositive()) {
                            periodeAnnonce2.setCodeValeurTotalIJ("0");
                        } else {
                            periodeAnnonce2.setCodeValeurTotalIJ("1");
                            currencyTotalIJ.negate();
                        }

                        periodeAnnonce2.setTotalIJ(currencyTotalIJ.toString());

                        // taux journalier
                        periodeAnnonce2.setTauxJournalier(montantJournalier);

                        // nb jours interruption
                        periodeAnnonce2.setNombreJoursInterruption(generationAnnonce.getNombreJoursInterruption());

                        // motif interruption
                        periodeAnnonce2.setCodeMotifInterruption(getSession().getCode(
                                generationAnnonce.getCsMotifInterruption()));

                        // versement
                        periodeAnnonce2.setVersementIJ(generationAnnonce.getCodeVersement());

                        // periodeDe, periodeA
                        periodeAnnonce2.setPeriodeDe(generationAnnonce.getPeriodeDe());
                        periodeAnnonce2.setPeriodeA(generationAnnonce.getPeriodeA());

                        // Droit à la prestation pour enfant : 1 = oui, 0 = non
                        if (generationAnnonce.getIsDroitPrestationPourEnfant() != null
                                && generationAnnonce.getIsDroitPrestationPourEnfant().booleanValue()) {

                            periodeAnnonce2.setDroitPrestationPourEnfant("1");
                        } else {
                            periodeAnnonce2.setDroitPrestationPourEnfant("0");
                        }

                        // Garantie des droits acquis 5ème r évision AI : 0 =
                        // Non, 1 = Oui
                        if (!JadeStringUtil.isBlankOrZero(noRevision)) {
                            if (4 == Integer.valueOf(noRevision).intValue()) {

                                // Ce champ siginifie : Garantie des droit
                                // acquis de la 4ème révision
                                // pour une annonce de 5ème révision
                                periodeAnnonce2.setGarantieDroitAcquis5emeRevision("1");
                            } else {
                                periodeAnnonce2.setGarantieDroitAcquis5emeRevision("0");
                            }
                        }

                    }
                }

                // //On ne doit plus envoyer d'annonces 85, car refusées par la
                // centrale
                //
                // /*
                // *
                // * Grande IJ / Petite IJ -- 85
                // *
                // *
                // */
                // else {
                // // ancien droit
                //
                // // code application
                // annonceACreer.setCodeApplication("85");
                //
                // // code enregistrement
                // annonceACreer.setCodeEnregistrement("01");
                //
                // // noCaisse
                // annonceACreer.setNoCaisse(CaisseHelperFactory.getInstance().getNoCaisse(getSession()
                // .getApplication()));
                //
                // // noAgence
                // annonceACreer.setNoAgence(CaisseHelperFactory.getInstance().getNoAgence(getSession()
                // .getApplication()));
                //
                // // moisAnneeComptable
                // annonceACreer.setMoisAnneeComptable(moisAnneeComptable);
                //
                // // genre de carte
                // annonceACreer.setCodeGenreCarte(getSession().getCode(generationAnnonce.getCsTypePrestation()));
                //
                // // noAssure
                // annonceACreer.setNoAssure(JAStringFormatter.deformatAvs(generationAnnonce.getNoAVSPrononce()));
                //
                // // etat civil
                // annonceACreer.setCodeEtatCivil(PRACORConst.csEtatCivilHeraToAcor(getSession(),
                // generationAnnonce.getCsEtatCivil()));
                //
                // // nombre d'enfants à charge
                // annonceACreer.setNombreEnfants(generationAnnonce.getNombreEnfants());
                //
                // // revenu journalier moyen effectif (non plafonné)
                // annonceACreer.setRevenuJournalierDeterminant(generationAnnonce
                // .getRevenuMoyenDeterminantNonPlafonne());
                //
                // // OfficeAI
                // annonceACreer.setOfficeAI(generationAnnonce.getOfficeAI());
                //
                // // petite ij ?
                // annonceACreer.setPetiteIJ(generationAnnonce.getCsTypeIJ().equals(IIJPrononce.CS_PETITE_IJ)
                // ? "1"
                // : "0");
                //
                // // genre readaptation
                // annonceACreer.setCodeGenreReadaptation(getSession().getCode(generationAnnonce
                // .getCsGenreReadaptation()));
                //
                // // garantieAA
                // annonceACreer.setGarantieAA(JadeStringUtil.isDecimalEmpty(generationAnnonce.getMontantGarantieAA())
                // ? "0"
                // : "1");
                //
                // // garantie AA reduite
                // annonceACreer.setIjReduite(generationAnnonce.getGarantieAAReduite().booleanValue()
                // ? "1" : "0");
                //
                // // premiere periode (il y en a forcement une)
                // // on prend d'abord l'interne. Si elle n'existe pas, on prens
                // l'externe
                // boolean isSupplementReadaptation = false;
                // IJIndemniteJournaliere indemniteJournaliere =
                // generationAnnonce
                // .getIndemnitesJournalieresInternes();
                //
                // if (indemniteJournaliere == null) {
                // indemniteJournaliere =
                // generationAnnonce.getIndemnitesJournalieresExternes();
                // }
                //
                // periodeAnnonce1 = new IJPeriodeAnnonce();
                // periodeAnnonce1.setSession(getSession());
                //
                // // periode de
                // periodeAnnonce1.setPeriodeDe(generationAnnonce.getPeriodeDe());
                //
                // // periode a
                // periodeAnnonce1.setPeriodeA(generationAnnonce.getPeriodeA());
                //
                //
                // // nb jours et totalIJ
                // if
                // (indemniteJournaliere.getCsTypeIndemnisation().equals(IIJMesure.CS_INTERNE))
                // {
                // periodeAnnonce1.setNombreJours(generationAnnonce.getNbJoursInternes());
                // periodeAnnonce1.setTotalIJ(generationAnnonce.getMontantBrutInterne());
                // // taux journalier
                // periodeAnnonce1.setTauxJournalier(generationAnnonce.getTauxJournalierInterne());
                //
                //
                // } else {
                // periodeAnnonce1.setNombreJours(generationAnnonce.getNbJoursExternes());
                // periodeAnnonce1.setTotalIJ(generationAnnonce.getMontantBrutExterne());
                // periodeAnnonce1.setTauxJournalier(generationAnnonce.getTauxJournalierExterne());
                // }
                //
                //
                // if
                // (IIJPrestation.CS_RESTITUTION.equals(generationAnnonce.getCsTypePrestation()))
                // {
                // periodeAnnonce1.setCodeValeurTotalIJ("1");
                // }
                // else {
                // periodeAnnonce1.setCodeValeurTotalIJ("0");
                // }
                //
                //
                //
                // FWCurrency currencyTotalIJ = new
                // FWCurrency(periodeAnnonce1.getTotalIJ());
                //
                //
                //
                // if (!currencyTotalIJ.isPositive()) {
                // currencyTotalIJ.negate();
                // }
                //
                // periodeAnnonce1.setTotalIJ(currencyTotalIJ.toString());
                // isSupplementReadaptation =
                // !JadeStringUtil.isDecimalEmpty(indemniteJournaliere
                // .getMontantSupplementaireReadaptation());
                //
                // // deuxieme periode si elle existe
                // // on prend l'externe. S'il y avait une interne, elle a été
                // prise comme premiere periode
                // if
                // (indemniteJournaliere.getCsTypeIndemnisation().equals(IIJMesure.CS_INTERNE))
                // {
                // indemniteJournaliere =
                // generationAnnonce.getIndemnitesJournalieresExternes();
                // } else {
                // indemniteJournaliere = null;
                // }
                //
                // if (indemniteJournaliere != null) {
                // periodeAnnonce2 = new IJPeriodeAnnonce();
                // periodeAnnonce2.setSession(getSession());
                //
                // // periode de
                // periodeAnnonce2.setPeriodeDe(generationAnnonce.getPeriodeDe());
                //
                // // periode a
                // periodeAnnonce2.setPeriodeA(generationAnnonce.getPeriodeA());
                //
                // // taux journalier
                // periodeAnnonce2.setTauxJournalier(generationAnnonce.getTauxJournalierExterne());
                //
                // periodeAnnonce2.setNombreJours(generationAnnonce.getNbJoursExternes());
                // periodeAnnonce2.setTotalIJ(generationAnnonce.getMontantBrutExterne());
                //
                // if
                // (IIJPrestation.CS_RESTITUTION.equals(generationAnnonce.getCsTypePrestation()))
                // {
                // periodeAnnonce2.setCodeValeurTotalIJ("1");
                // }
                // else {
                // periodeAnnonce2.setCodeValeurTotalIJ("0");
                // }
                //
                //
                //
                // currencyTotalIJ = new
                // FWCurrency(periodeAnnonce2.getTotalIJ());
                //
                // if (!currencyTotalIJ.isPositive()) {
                // currencyTotalIJ.negate();
                // }
                // periodeAnnonce2.setTotalIJ(currencyTotalIJ.toString());
                //
                //
                // isSupplementReadaptation |=
                // !JadeStringUtil.isDecimalEmpty(indemniteJournaliere
                // .getMontantSupplementaireReadaptation());
                // }
                //
                // // allocation personne seule, menage, assistance,
                // exploitation, supplement readaptation
                // StringBuffer lesCinqChamps = new StringBuffer();
                //
                // if
                // (generationAnnonce.getCsTypeBaseIJCalculee().equals(IIJIJCalculee.CS_PERSONNE_SEULE))
                // {
                // lesCinqChamps.append("10");
                // } else {
                // lesCinqChamps.append("01");
                // }
                //
                // lesCinqChamps.append(generationAnnonce.isAllocationAssistance()
                // ? "1" : "0");
                // lesCinqChamps.append(generationAnnonce.isAllocationExploitation()
                // ? "1" : "0");
                // lesCinqChamps.append(isSupplementReadaptation ? "1" : "0");
                // annonceACreer.setParamSpecifique3emeRevisionSur5Positions(lesCinqChamps.toString());
                // } // fin else (ancienne revision)

                doValidationAnnonce(annonceACreer, periodeAnnonce1, periodeAnnonce2);

                // ajout de l'annonce.
                annonceACreer.add(getTransaction());
                periodeAnnonce1.setIdAnnonce(annonceACreer.getIdAnnonce());
                periodeAnnonce1.add(getTransaction());

                if (periodeAnnonce2 != null) {
                    periodeAnnonce2.setIdAnnonce(annonceACreer.getIdAnnonce());
                    periodeAnnonce2.add(getTransaction());
                }

                // update de la prestation
                IJPrestation prestation = new IJPrestation();
                prestation.setSession(getSession());
                prestation.setIdPrestation(generationAnnonce.getIdPrestation());
                prestation.retrieve(getTransaction());
                prestation.setIdAnnonce(annonceACreer.getIdAnnonce());
                prestation.update(getTransaction());
            } // fin while
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());

            // try {
            // getTransaction().rollback();
            // } catch (Exception e1) {
            // return false;
            // }

            return false;
        }

        return true;
    }

    // met certains champs de l'annonce a vide s'il s'agit d'une restitution
    private void doValidationAnnonce(IJAnnonce annonce, IJPeriodeAnnonce periodeAnnonce1,
                                     IJPeriodeAnnonce periodeAnnonce2) {
        if (annonce.getCodeGenreCarte().equals("3") || annonce.getCodeGenreCarte().equals("4")) {
            annonce.setCodeEtatCivil("");
            annonce.setNombreEnfants("");

            if ("85".equals(annonce.getCodeApplication())) {
                annonce.setPetiteIJ("");
            }
            annonce.setRevenuJournalierDeterminant("");
            annonce.setOfficeAI("");
            annonce.setCodeGenreReadaptation("");
            annonce.setGarantieAA("");
            annonce.setIjReduite("");
            annonce.setParamSpecifique3emeRevisionSur5Positions("     ");
            periodeAnnonce1.setDeductionNourritureLogement("");
            periodeAnnonce1.setNombreJoursInterruption("");
            periodeAnnonce1.setVersementIJ("");
            periodeAnnonce1.setCodeMotifInterruption("");

            if (periodeAnnonce2 != null) {
                periodeAnnonce2.setDeductionNourritureLogement("");
                periodeAnnonce2.setNombreJoursInterruption("");
                periodeAnnonce2.setVersementIJ("");
                periodeAnnonce2.setCodeMotifInterruption("");
            }
        }
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
        return getSession().getLabel("GENERER_ANNONCE_PROCESS");
    }

    /**
     * getter pour l'attribut id lot
     *
     * @return la valeur courante de l'attribut id lot
     */
    public String getIdLot() {
        return idLot;
    }

    /**
     * getter pour l'attribut mois annee comptable
     *
     * @return la valeur courante de l'attribut mois annee comptable
     */
    public String getMoisAnneeComptable() {
        return moisAnneeComptable;
    }

    /**
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BProcess#jobQueue()
     *
     * @return DOCUMENT ME!
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * setter pour l'attribut id lot
     *
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdLot(String string) {
        idLot = string;
    }

    /**
     * setter pour l'attribut mois annee comptable
     *
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setMoisAnneeComptable(String string) {
        moisAnneeComptable = string;
    }
}
