package globaz.ij.module;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.ij.api.prestations.IIJPrestation;
import globaz.ij.api.prestations.IIJRepartitionPaiements;
import globaz.ij.api.prononces.IIJMesure;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.application.IJApplication;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJCotisation;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJIndemniteJournaliere;
import globaz.ij.db.prestations.IJIndemniteJournaliereManager;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJPrestationManager;
import globaz.ij.db.prestations.IJRepartitionPaiements;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJSituationProfessionnelle;
import globaz.ij.db.prononces.IJSituationProfessionnelleManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.prestation.db.tauxImposition.PRTauxImposition;
import globaz.prestation.db.tauxImposition.PRTauxImpositionManager;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tauxImposition.api.IPRTauxImposition;
import globaz.prestation.tools.PRCalcul;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * module de creation des repartitions et des cotisations d'assurances d'une prestation.
 * </p>
 * 
 * <p>
 * tout est toujours versé a l'assure (le boolean verse à l'employeur de la situation pro est ignore).
 * </p>
 * 
 * <p>
 * La classe se base sur le revenu durant la situation professionnelle durant la readaptation pour savoir quelles
 * assurances il doit payer.
 * </p>
 * 
 * @author vre
 */
public class IJRepartitionPaiementBuilder {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // une instance statique de calendar pour utiliser les methodes qui
    // devraient etre statiques comme daysBetween
    private static final JACalendar CALENDAR = new JACalendarGregorian();

    private static final IJRepartitionPaiementBuilder INSTANCE = new IJRepartitionPaiementBuilder();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * singleton.
     * 
     * @return la valeur courante de l'attribut instance
     */
    public static final IJRepartitionPaiementBuilder getInstance() {
        return IJRepartitionPaiementBuilder.INSTANCE;
    }

    private void addCotisation(BSession session, BITransaction transaction, IJCotisation cotisation) throws Exception {
        cotisation.wantMiseAJourMontantRepartition(false);
        cotisation.setSession(session);
        cotisation.add(transaction);
    }

    /**
     * calcul des cotisations pour une repartition de paiement.
     * 
     * @param session
     * @param prononce
     * @param baseIndemnisation
     * @param repartition
     *            DOCUMENT ME!
     * 
     * @return la somme totale des cotisations pour cette repartition
     * 
     * @throws Exception
     * 
     * @see #buildAssurancesGrandeIJ(BSession, IJPrononce, IJRepartitionPaiements)
     * @see #buildAssurancesPetiteIJ(BSession, IJPrononce, IJRepartitionPaiements)
     */
    public double buildCotisationsAssure(BSession session, BITransaction transaction, IJPrononce prononce,
            IJBaseIndemnisation baseIndemnisation, IJRepartitionPaiements repartition, String montantBrutPrestation,
            String montantJournalierAvantDeduction) throws Exception {
        double retValue = 0.0;
        String csSexe = prononce.loadDemande(transaction).loadTiers().getProperty(PRTiersWrapper.PROPERTY_SEXE);

        // Si situation prof == AGRICULTEUR_INDEPENDANT_LFA
        if (IIJPrononce.CS_AGRICULTEUR_INDEPENDANT_LFA.equals(prononce.getCsStatutProfessionnel())) {

            // salarié
            // moins AVS/AI/APG paritaire
            double[] montantsAVS = creerAssurance(session, transaction, repartition.getIdRepartitionPaiement(),
                    repartition.getMontantBrut(), PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(
                            IJApplication.DEFAULT_APPLICATION_IJ, PRAffiliationHelper.TYPE_PARITAIRE), csSexe,
                    baseIndemnisation.getDateDebutPeriode(), baseIndemnisation.getDateFinPeriode(), false);
            retValue += somme(montantsAVS);

            // moins AC paritaire
            retValue += somme(creerAssurance(session, transaction, repartition.getIdRepartitionPaiement(),
                    repartition.getMontantBrut(), PRAffiliationHelper.GENRE_AC.getIdAssurance(
                            IJApplication.DEFAULT_APPLICATION_IJ, PRAffiliationHelper.TYPE_PARITAIRE), csSexe,
                    baseIndemnisation.getDateDebutPeriode(), baseIndemnisation.getDateFinPeriode(), false));

            //
            // selon point ouvert 00549
            //
            // moins LFA
            // double[] montantsLFA = (creerAssurance(session, transaction,
            // repartition.getIdRepartitionPaiement(),
            // repartition.getMontantBrut(),
            // PRAffiliationHelper.GENRE_LFA.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
            // PRAffiliationHelper.TYPE_PARITAIRE),
            // csSexe, baseIndemnisation.getDateDebutPeriode(),
            // baseIndemnisation.getDateFinPeriode(), false));
            //
            // retValue += somme(montantsLFA);
        }

        // Les nons actifs ne paient pas de cotisations AC (ex. étudiant de - de
        // 20 ans).
        else if (IIJPrononce.CS_NON_ACTIF.equals(prononce.getCsStatutProfessionnel())) {
            // moins AVS/AI/APG personnelle
            retValue += somme(creerAssurance(session, transaction, repartition.getIdRepartitionPaiement(),
                    repartition.getMontantBrut(), PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(
                            IJApplication.DEFAULT_APPLICATION_IJ, PRAffiliationHelper.TYPE_PERSONNEL), csSexe,
                    baseIndemnisation.getDateDebutPeriode(), baseIndemnisation.getDateFinPeriode(), false));

        }

        else if (IIJPrononce.CS_INDEPENDANT.equals(prononce.getCsStatutProfessionnel())) {

            // moins AVS/AI/APG personnelle
            retValue += somme(creerAssurance(session, transaction, repartition.getIdRepartitionPaiement(),
                    repartition.getMontantBrut(), PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(
                            IJApplication.DEFAULT_APPLICATION_IJ, PRAffiliationHelper.TYPE_PERSONNEL), csSexe,
                    baseIndemnisation.getDateDebutPeriode(), baseIndemnisation.getDateFinPeriode(), false));
        }
        // supposé salarié
        else {
            // salarié
            // moins AVS/AI/APG paritaire
            double[] montantsAVS = creerAssurance(session, transaction, repartition.getIdRepartitionPaiement(),
                    repartition.getMontantBrut(), PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(
                            IJApplication.DEFAULT_APPLICATION_IJ, PRAffiliationHelper.TYPE_PARITAIRE), csSexe,
                    baseIndemnisation.getDateDebutPeriode(), baseIndemnisation.getDateFinPeriode(), false);
            retValue += somme(montantsAVS);

            // moins AC paritaire
            retValue += somme(creerAssurance(session, transaction, repartition.getIdRepartitionPaiement(),
                    repartition.getMontantBrut(), PRAffiliationHelper.GENRE_AC.getIdAssurance(
                            IJApplication.DEFAULT_APPLICATION_IJ, PRAffiliationHelper.TYPE_PARITAIRE), csSexe,
                    baseIndemnisation.getDateDebutPeriode(), baseIndemnisation.getDateFinPeriode(), false));

        }

        // impot a la source
        if (prononce.getSoumisImpotSource().booleanValue()) {

            BigDecimal montantBrutPourCalculIS = new BigDecimal(0);
            try {
                BigDecimal montantBrutPrst = new BigDecimal(montantBrutPrestation);
                BigDecimal montantBrutRepartition = new BigDecimal(repartition.getMontantBrut());
                BigDecimal taux = new BigDecimal(1);

                // proratisation en cas de versement splitté entre employeur et
                // employé...
                if (!montantBrutPrst.equals(montantBrutRepartition)) {
                    taux = new BigDecimal(montantBrutRepartition.toString());
                    taux = taux.divide(montantBrutPrst, 10, BigDecimal.ROUND_HALF_EVEN);
                }
                montantBrutPourCalculIS = montantBrutRepartition.multiply(taux);
            } catch (Exception e) {
                e.printStackTrace();
                montantBrutPourCalculIS = new BigDecimal(repartition.getMontantBrut());
            }

            retValue += somme(soustraireImpots(session, transaction, repartition.getIdRepartitionPaiement(),
                    montantBrutPourCalculIS.toString(), baseIndemnisation.getCsCantonImpotSource(),
                    baseIndemnisation.getTauxImpotSource(), baseIndemnisation.getDateDebutPeriode(),
                    baseIndemnisation.getDateFinPeriode()));
        }
        return retValue;
    }

    /**
     * calcul des cotisations pour un employeur
     * <p>
     * Si Bénéficiaire == assuré : on considère qu'il est indépendant -> Si indépendant ET status saisie == INDEPENDANT
     * -> -AVS -AC -> Si indépendant ET status saisie == SALARIE -> -AVS
     * 
     * Sinon (pas indépendant) -> +AVS +AC (indépendemment du statut saisi) -> Si status saisie == Agriculteur
     * independant LFA -> +LFA
     * 
     * Pas d'impot à la source si versé à l'employeur.
     * </p>
     * 
     * 
     * @param session
     * @param prononce
     * @param baseIndemnisation
     * @param repartition
     *            DOCUMENT ME!
     * 
     * @return la somme totale des cotisations pour cette repartition
     * 
     * @throws Exception
     * 
     * @see #buildAssurancesGrandeIJ(BSession, IJPrononce, IJRepartitionPaiements)
     * @see #buildAssurancesPetiteIJ(BSession, IJPrononce, IJRepartitionPaiements)
     */

    public double buildCotisationsEmployeur(BSession session, BITransaction transaction, IJPrononce prononce,
            IJBaseIndemnisation baseIndemnisation, IJRepartitionPaiements repartition) throws Exception {
        double retValue = 0.0;
        String csSexe = prononce.loadDemande(transaction).loadTiers().getProperty(PRTiersWrapper.PROPERTY_SEXE);
        String idTiersAssure = prononce.loadDemande(transaction).loadTiers()
                .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);

        // Si le bénéficiare est l'assuré lui-même, on considère qu'il est
        // indépendant
        if (idTiersAssure.equals(repartition.getIdTiers())) {

            // Indépendant + salarie
            if (IIJPrononce.CS_SALARIE.equals(prononce.getCsStatutProfessionnel())) {

                // indépendant
                // moins AVS/AI/APG personnel
                // moins AC personnel
                double[] montantsAVS = creerAssurance(session, transaction, repartition.getIdRepartitionPaiement(),
                        repartition.getMontantBrut(), PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(
                                IJApplication.DEFAULT_APPLICATION_IJ, PRAffiliationHelper.TYPE_PERSONNEL), csSexe,
                        baseIndemnisation.getDateDebutPeriode(), baseIndemnisation.getDateFinPeriode(), false);
                retValue += somme(montantsAVS);

                double[] montantsAC = (creerAssurance(session, transaction, repartition.getIdRepartitionPaiement(),
                        repartition.getMontantBrut(), PRAffiliationHelper.GENRE_AC.getIdAssurance(
                                IJApplication.DEFAULT_APPLICATION_IJ, PRAffiliationHelper.TYPE_PERSONNEL), csSexe,
                        baseIndemnisation.getDateDebutPeriode(), baseIndemnisation.getDateFinPeriode(), false));

                retValue += somme(montantsAC);

            }

            else if (IIJPrononce.CS_INDEPENDANT.equals(prononce.getCsStatutProfessionnel())) {
                // moins AVS/AI/APG personnel
                double[] montantsAVS = creerAssurance(session, transaction, repartition.getIdRepartitionPaiement(),
                        repartition.getMontantBrut(), PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(
                                IJApplication.DEFAULT_APPLICATION_IJ, PRAffiliationHelper.TYPE_PERSONNEL), csSexe,
                        baseIndemnisation.getDateDebutPeriode(), baseIndemnisation.getDateFinPeriode(), false);
                retValue += somme(montantsAVS);
            }

            else if (IIJPrononce.CS_NON_ACTIF.equals(prononce.getCsStatutProfessionnel())) {
                // moins AVS/AI/APG personnel
                double[] montantsAVS = creerAssurance(session, transaction, repartition.getIdRepartitionPaiement(),
                        repartition.getMontantBrut(), PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(
                                IJApplication.DEFAULT_APPLICATION_IJ, PRAffiliationHelper.TYPE_PERSONNEL), csSexe,
                        baseIndemnisation.getDateDebutPeriode(), baseIndemnisation.getDateFinPeriode(), false);
                retValue += somme(montantsAVS);
            } else if (IIJPrononce.CS_AGRICULTEUR_INDEPENDANT_LFA.equals(prononce.getCsStatutProfessionnel())) {

                // add AVS/AI/APG personnel
                // add AC personnel
                double[] montantsAVS = creerAssurance(session, transaction, repartition.getIdRepartitionPaiement(),
                        repartition.getMontantBrut(), PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(
                                IJApplication.DEFAULT_APPLICATION_IJ, PRAffiliationHelper.TYPE_PARITAIRE), csSexe,
                        baseIndemnisation.getDateDebutPeriode(), baseIndemnisation.getDateFinPeriode(), true);
                retValue += somme(montantsAVS);

                /*
                 * double[] montantsAC = (creerAssurance(session, transaction, repartition.getIdRepartitionPaiement(),
                 * repartition.getMontantBrut(), PRAffiliationHelper.GENRE_AC.getIdAssurance
                 * (IJApplication.DEFAULT_APPLICATION_IJ, PRAffiliationHelper.TYPE_PARITAIRE), csSexe,
                 * baseIndemnisation.getDateDebutPeriode(), baseIndemnisation.getDateFinPeriode(), true));
                 */

                // Add LFA
                double[] montantsLFA = (creerAssurance(session, transaction, repartition.getIdRepartitionPaiement(),
                        repartition.getMontantBrut(), PRAffiliationHelper.GENRE_LFA.getIdAssurance(
                                IJApplication.DEFAULT_APPLICATION_IJ, PRAffiliationHelper.TYPE_PARITAIRE), csSexe,
                        baseIndemnisation.getDateDebutPeriode(), baseIndemnisation.getDateFinPeriode(), true));

                retValue += somme(montantsLFA);
            }

        }
        // N'est pas un indépendant
        else {
            // add AVS/AI/APG personnel
            // add AC personnel
            double[] montantsAVS = creerAssurance(session, transaction, repartition.getIdRepartitionPaiement(),
                    repartition.getMontantBrut(), PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(
                            IJApplication.DEFAULT_APPLICATION_IJ, PRAffiliationHelper.TYPE_PARITAIRE), csSexe,
                    baseIndemnisation.getDateDebutPeriode(), baseIndemnisation.getDateFinPeriode(), true);
            retValue += somme(montantsAVS);

            double[] montantsAC = (creerAssurance(session, transaction, repartition.getIdRepartitionPaiement(),
                    repartition.getMontantBrut(), PRAffiliationHelper.GENRE_AC.getIdAssurance(
                            IJApplication.DEFAULT_APPLICATION_IJ, PRAffiliationHelper.TYPE_PARITAIRE), csSexe,
                    baseIndemnisation.getDateDebutPeriode(), baseIndemnisation.getDateFinPeriode(), true));

            retValue += somme(montantsAC);

            if (IIJPrononce.CS_AGRICULTEUR_INDEPENDANT_LFA.equals(prononce.getCsStatutProfessionnel())) {

                // Add LFA
                double[] montantsLFA = (creerAssurance(session, transaction, repartition.getIdRepartitionPaiement(),
                        repartition.getMontantBrut(), PRAffiliationHelper.GENRE_LFA.getIdAssurance(
                                IJApplication.DEFAULT_APPLICATION_IJ, PRAffiliationHelper.TYPE_PARITAIRE), csSexe,
                        baseIndemnisation.getDateDebutPeriode(), baseIndemnisation.getDateFinPeriode(), true));

                retValue += somme(montantsLFA);
            }

        }
        return retValue;
    }

    /**
     * Cree la repartition de paiement et les cotisations d'assurances de cette repartition pour toutes les prestations
     * d'une base qui ne sont pas du type restitution.
     * 
     * <p>
     * Note: le versement est toujours effectue à l'assure.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void buildRepartitionPaiements(BSession session, BITransaction transaction, IJPrononce prononce,
            IJBaseIndemnisation baseIndemnisation) throws Exception {
        IJPrestationManager prestations = new IJPrestationManager();

        prestations.setForIdBaseIndemnisation(baseIndemnisation.getIdBaseIndemisation());
        prestations.setNotForCsType(IIJPrestation.CS_RESTITUTION);
        prestations.setSession(session);
        prestations.find();

        this.buildRepartitionPaiements(session, transaction, prononce, baseIndemnisation, prestations.getContainer());
    }

    /**
     * Cree la repartition de paiement et les cotisations d'assurances de cette repartition pour toutes les prestations
     * d'une liste.
     * 
     * <p>
     * Note: le versement est toujours effectue à l'assure.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * @param prestationsList
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void buildRepartitionPaiements(BSession session, BITransaction transaction, IJPrononce prononce,
            IJBaseIndemnisation baseIndemnisation, List prestationsList) throws Exception {
        for (Iterator prestations = prestationsList.iterator(); prestations.hasNext();) {
            IJPrestation prestation = (IJPrestation) prestations.next();

            // creer la repartition de paiement
            IJRepartitionPaiements repartition = new IJRepartitionPaiements();
            String idTiers = prononce.loadDemande(transaction).getIdTiers();

            repartition.setIdPrestation(prestation.getIdPrestation());
            repartition.setIdTiers(idTiers);

            repartition.setNom(prononce.loadDemande(transaction).loadTiers().getProperty(PRTiersWrapper.PROPERTY_NOM)
                    + " " + prononce.loadDemande(transaction).loadTiers().getProperty(PRTiersWrapper.PROPERTY_PRENOM));

            // le montant à verser
            repartition.setMontantBrut(prestation.getMontantBrut());
            repartition.setMontantNet(prestation.getMontantBrut());
            repartition.setTypePaiement(IIJRepartitionPaiements.CS_PAIEMENT_DIRECT);

            // sauver dans la base
            repartition.setSession(session);
            repartition.add(transaction);

            // La nouvelle version de ACOR n'importe plus le montant jrn ext si
            // la base ne contient que des code interne.
            // Il faut donc aller le rechercher dans IJIndemniteJournaliere.
            String montantJrnExt = prestation.getMontantJournalierExterne();

            if (JadeStringUtil.isBlankOrZero(montantJrnExt)) {
                IJIJCalculee ijc = new IJIJCalculee();
                ijc.setSession(session);
                ijc.setIdIJCalculee(prestation.getIdIJCalculee());
                ijc.retrieve(transaction);

                IJIndemniteJournaliereManager ijMgr = new IJIndemniteJournaliereManager();
                ijMgr.setSession(session);
                ijMgr.setForIdIJCalculee(ijc.getIdIJCalculee());
                ijMgr.setForCsTypeIndemnite(IIJMesure.CS_EXTERNE);
                ijMgr.find(transaction);
                if (!ijMgr.isEmpty()) {
                    montantJrnExt = ((IJIndemniteJournaliere) ijMgr.getFirstEntity()).getMontantJournalierIndemnite();
                }
            }

            // calculer les cotisations d'assurances et impots
            double somme = buildCotisationsAssure(session, transaction, prononce, baseIndemnisation, repartition,
                    prestation.getMontantBrut(), montantJrnExt);

            repartition.setMontantNet(JANumberFormatter.formatNoQuote(JadeStringUtil.toDouble(repartition
                    .getMontantBrut()) + somme));
            repartition.update(transaction);
        }
    }

    /**
     * Cree la repartition de paiement et les cotisations d'assurances de cette repartition pour toutes les prestations
     * d'une base qui ne sont pas du type restitution.
     * 
     * <p>
     * Note: le versement est toujours effectue à l'employeur.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void buildRepartitionPaiementsEmployeur(BSession session, BITransaction transaction, IJPrononce prononce,
            IJBaseIndemnisation baseIndemnisation) throws Exception {
        IJPrestationManager prestations = new IJPrestationManager();

        prestations.setForIdBaseIndemnisation(baseIndemnisation.getIdBaseIndemisation());
        prestations.setNotForCsType(IIJPrestation.CS_RESTITUTION);
        prestations.setSession(session);
        prestations.find();

        this.buildRepartitionPaiementsEmployeur(session, transaction, prononce, baseIndemnisation,
                prestations.getContainer());
    }

    /**
     * Cree la repartition de paiement et les cotisations d'assurances de cette repartition pour toutes les prestations
     * d'une liste.
     * 
     * <p>
     * Note: le versement est toujours effectue à l'employeur.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * @param prestationsList
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void buildRepartitionPaiementsEmployeur(BSession session, BITransaction transaction, IJPrononce prononce,
            IJBaseIndemnisation baseIndemnisation, List prestationsList) throws Exception {
        for (Iterator prestations = prestationsList.iterator(); prestations.hasNext();) {
            IJPrestation prestation = (IJPrestation) prestations.next();

            // creer la repartition de paiement
            // Dans ce cas le paiement est fait a l'employeur
            // Il ne doit y avoir qu'un seul employeur
            IJRepartitionPaiements repartition = new IJRepartitionPaiements();

            // on cherche l'employeur dans la situation prof.
            IJSituationProfessionnelleManager spManager = new IJSituationProfessionnelleManager();
            spManager.setSession(session);
            spManager.setForIdPrononce(prononce.getIdPrononce());
            spManager.find(transaction);

            if (spManager.getSize() == 0) {
                throw new Exception(session.getLabel("AUCUN_EMPL_PRONONCE") + " - " + prononce.getIdPrononce());
            }

            IJSituationProfessionnelle sp = (IJSituationProfessionnelle) spManager.getFirstEntity();
            String idTiers = sp.loadEmployeur().getIdTiers();

            repartition.setIdPrestation(prestation.getIdPrestation());
            repartition.setIdTiers(idTiers);

            repartition.setNom(sp.loadEmployeur().loadTiers().getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + sp.loadEmployeur().loadTiers().getProperty(PRTiersWrapper.PROPERTY_PRENOM));

            // le montant à verser
            repartition.setMontantBrut(prestation.getMontantBrut());
            repartition.setMontantNet(prestation.getMontantBrut());
            repartition.setTypePaiement(IIJRepartitionPaiements.CS_PAIEMENT_DIRECT);

            // sauver dans la base
            repartition.setSession(session);
            repartition.add(transaction);

            // calculer les cotisations d'assurances et impots
            double somme = buildCotisationsEmployeur(session, transaction, prononce, baseIndemnisation, repartition);

            repartition.setMontantNet(JANumberFormatter.formatNoQuote(JadeStringUtil.toDouble(repartition
                    .getMontantBrut()) + somme));
            repartition.update(transaction);
        }
    }

    /**
     * Cree la repartition de paiement pour toutes les prestations d'une base qui ne sont pas du type restitution.
     * 
     * <p>
     * Note: le versement est toujours effectue à l'employeur.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void buildRepartitionPaiementsEmployeurSansCotisations(BSession session, BITransaction transaction,
            IJPrononce prononce, IJBaseIndemnisation baseIndemnisation) throws Exception {
        IJPrestationManager prestations = new IJPrestationManager();

        prestations.setForIdBaseIndemnisation(baseIndemnisation.getIdBaseIndemisation());
        prestations.setNotForCsType(IIJPrestation.CS_RESTITUTION);
        prestations.setSession(session);
        prestations.find();

        this.buildRepartitionPaiementsEmployeurSansCotisations(session, transaction, prononce, baseIndemnisation,
                prestations.getContainer());
    }

    /**
     * Cree la repartition de paiement pour toutes les prestations d'une liste.
     * 
     * <p>
     * Note: le versement est toujours effectue à l'employeur.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * @param prestationsList
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void buildRepartitionPaiementsEmployeurSansCotisations(BSession session, BITransaction transaction,
            IJPrononce prononce, IJBaseIndemnisation baseIndemnisation, List prestationsList) throws Exception {
        for (Iterator prestations = prestationsList.iterator(); prestations.hasNext();) {
            IJPrestation prestation = (IJPrestation) prestations.next();

            // creer la repartition de paiement
            // Dans ce cas le paiement est fait a l'employeur
            // Il ne doit y avoir qu'un seul employeur
            IJRepartitionPaiements repartition = new IJRepartitionPaiements();

            // on cherche l'employeur dans la situation prof.
            IJSituationProfessionnelleManager spManager = new IJSituationProfessionnelleManager();
            spManager.setSession(session);
            spManager.setForIdPrononce(prononce.getIdPrononce());
            spManager.find(transaction);

            if (spManager.getSize() == 0) {
                throw new Exception(session.getLabel("AUCUN_EMPL_PRONONCE") + " - " + prononce.getIdPrononce());
            }

            IJSituationProfessionnelle sp = (IJSituationProfessionnelle) spManager.getFirstEntity();
            String idTiers = sp.loadEmployeur().getIdTiers();

            repartition.setIdPrestation(prestation.getIdPrestation());
            repartition.setIdTiers(idTiers);

            repartition.setNom(sp.loadEmployeur().loadTiers().getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + sp.loadEmployeur().loadTiers().getProperty(PRTiersWrapper.PROPERTY_PRENOM));

            // le montant à verser
            repartition.setMontantBrut(prestation.getMontantBrut());
            repartition.setMontantNet(prestation.getMontantBrut());
            repartition.setTypePaiement(IIJRepartitionPaiements.CS_PAIEMENT_DIRECT);

            // Dans le cas des AIT uniquement, mais a priori cette methode est
            // appelée uniquement
            // dans les calcul des AIT !!!
            // 1 seul employeur pour une AIT !!!
            // On récupère l'idAffilié de la sit. prof. et on le set dans la
            // répartition des pmts.
            // Nécessaire d'avoir cet idAffilié pour avoir d'eventuelle
            // compensation pour cet affilié.
            if (IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(prononce.getCsTypeIJ())) {
                if (sp.loadEmployeur() != null) {
                    repartition.setIdAffilie(sp.loadEmployeur().getIdAffilie());
                    repartition.setIdAffilieAdrPmt(sp.loadEmployeur().getIdAffilie());
                }
            }

            // sauver dans la base
            repartition.setSession(session);
            repartition.add(transaction);
        }
    }

    public void buildRepartitionPaiementsPourUnePrestation(BSession session, BITransaction transaction,
            IJPrononce prononce, IJBaseIndemnisation baseIndemnisation, String idPrestation) throws Exception {

        IJPrestation prestation = new IJPrestation();
        prestation.setSession(session);
        prestation.setIdPrestation(idPrestation);
        prestation.retrieve(transaction);

        // creer la repartition de paiement
        IJRepartitionPaiements repartition = new IJRepartitionPaiements();
        String idTiers = prononce.loadDemande(transaction).getIdTiers();

        repartition.setIdPrestation(prestation.getIdPrestation());
        repartition.setIdTiers(idTiers);

        repartition.setNom(prononce.loadDemande(transaction).loadTiers().getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                + prononce.loadDemande(transaction).loadTiers().getProperty(PRTiersWrapper.PROPERTY_PRENOM));

        // le montant à verser
        repartition.setMontantBrut(prestation.getMontantBrut());
        repartition.setMontantNet(prestation.getMontantBrut());
        repartition.setTypePaiement(IIJRepartitionPaiements.CS_PAIEMENT_DIRECT);

        // sauver dans la base
        repartition.setSession(session);
        repartition.add(transaction);

        // La nouvelle version de ACOR n'importe plus le montant jrn ext si
        // la base ne contient que des code interne.
        // Il faut donc aller le rechercher dans IJIndemniteJournaliere.
        String montantJrnExt = prestation.getMontantJournalierExterne();

        if (JadeStringUtil.isBlankOrZero(montantJrnExt)) {
            IJIJCalculee ijc = new IJIJCalculee();
            ijc.setSession(session);
            ijc.setIdIJCalculee(prestation.getIdIJCalculee());
            ijc.retrieve(transaction);

            IJIndemniteJournaliereManager ijMgr = new IJIndemniteJournaliereManager();
            ijMgr.setSession(session);
            ijMgr.setForIdIJCalculee(ijc.getIdIJCalculee());
            ijMgr.setForCsTypeIndemnite(IIJMesure.CS_EXTERNE);
            ijMgr.find(transaction);
            if (!ijMgr.isEmpty()) {
                montantJrnExt = ((IJIndemniteJournaliere) ijMgr.getFirstEntity()).getMontantJournalierIndemnite();
            }
        }

        // calculer les cotisations d'assurances et impots
        double somme = buildCotisationsAssure(session, transaction, prononce, baseIndemnisation, repartition,
                prestation.getMontantBrut(), montantJrnExt);

        repartition.setMontantNet(JANumberFormatter.formatNoQuote(JadeStringUtil.toDouble(repartition.getMontantBrut())
                + somme));
        repartition.update(transaction);

    }

    /**
     * Cree la repartition de paiement pour toutes les prestations d'une base qui ne sont pas du type restitution.
     * 
     * <p>
     * Note: le versement est toujours effectue à l'assure.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void buildRepartitionPaiementsSansCotisations(BSession session, BITransaction transaction,
            IJPrononce prononce, IJBaseIndemnisation baseIndemnisation) throws Exception {
        IJPrestationManager prestations = new IJPrestationManager();

        prestations.setForIdBaseIndemnisation(baseIndemnisation.getIdBaseIndemisation());
        prestations.setNotForCsType(IIJPrestation.CS_RESTITUTION);
        prestations.setSession(session);
        prestations.find();

        this.buildRepartitionPaiementsSansCotisations(session, transaction, prononce, baseIndemnisation,
                prestations.getContainer());
    }

    /**
     * Cree la repartition de paiement pour toutes les prestations d'une liste.
     * 
     * <p>
     * Note: le versement est toujours effectue à l'assure.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * @param prestationsList
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void buildRepartitionPaiementsSansCotisations(BSession session, BITransaction transaction,
            IJPrononce prononce, IJBaseIndemnisation baseIndemnisation, List prestationsList) throws Exception {
        for (Iterator prestations = prestationsList.iterator(); prestations.hasNext();) {
            IJPrestation prestation = (IJPrestation) prestations.next();

            // creer la repartition de paiement
            IJRepartitionPaiements repartition = new IJRepartitionPaiements();
            String idTiers = prononce.loadDemande(transaction).getIdTiers();

            repartition.setIdPrestation(prestation.getIdPrestation());
            repartition.setIdTiers(idTiers);

            repartition.setNom(prononce.loadDemande(transaction).loadTiers().getProperty(PRTiersWrapper.PROPERTY_NOM)
                    + " " + prononce.loadDemande(transaction).loadTiers().getProperty(PRTiersWrapper.PROPERTY_PRENOM));

            // le montant à verser
            repartition.setMontantBrut(prestation.getMontantBrut());
            repartition.setMontantNet(prestation.getMontantBrut());
            repartition.setTypePaiement(IIJRepartitionPaiements.CS_PAIEMENT_DIRECT);

            // sauver dans la base
            repartition.setSession(session);
            repartition.add(transaction);
        }
    }

    /**
     * Recherche dans les taux d'assurance pour la periode donnee et cree une instance de IJCotisation pour chaque taux
     * de l'assurance specifiee.
     * 
     * <p>
     * S'il y a plus d'un taux pour la periode donnee, les montants des cotisations seront ponderes par le nombre de
     * jours effectifs durant lesquels les taux sont en vigueur.
     * </p>
     * 
     * @param session
     * @param transaction
     *            DOCUMENT ME!
     * @param idRepartitionPaiement
     *            DOCUMENT ME!
     * @param montant
     * @param idAssurance
     * @param csSexe
     * @param dateDebut
     * @param dateFin
     * 
     * @return les montants des cotisations a payer (nombres positifs).
     * 
     * @throws Exception
     */
    private double[] creerAssurance(BSession session, BITransaction transaction, String idRepartitionPaiement,
            String montant, String idAssurance, String csSexe, String dateDebut, String dateFin, boolean isAjouter)
            throws Exception {
        if (JadeStringUtil.isIntegerEmpty(idAssurance)) {
            // il n'y a pas d'assurances, on n'ajoute rien
            return new double[0];
        }

        long nbJoursPeriode = IJRepartitionPaiementBuilder.CALENDAR.daysBetween(dateDebut, dateFin);
        List taux = PRAffiliationHelper.getTauxAssurance(session, idAssurance, csSexe, dateDebut, dateFin);
        double[] retValue = new double[taux.size()];
        int idMontant = 0;

        for (Iterator iter = taux.iterator(); iter.hasNext(); ++idMontant) {
            AFTauxAssurance tauxAssurance = (AFTauxAssurance) iter.next();
            IJCotisation cotisation = new IJCotisation();

            // date debut
            if (IJRepartitionPaiementBuilder.CALENDAR.compare(tauxAssurance.getDateDebut(), dateDebut) == JACalendar.COMPARE_FIRSTLOWER) {
                cotisation.setDateDebut(dateDebut);
            } else {
                cotisation.setDateDebut(tauxAssurance.getDateDebut());
            }

            // date fin
            if (!JAUtil.isDateEmpty(tauxAssurance.getDateFin())
                    && (IJRepartitionPaiementBuilder.CALENDAR.compare(tauxAssurance.getDateFin(), dateFin) == JACalendar.COMPARE_FIRSTLOWER)) {
                cotisation.setDateFin(tauxAssurance.getDateFin());
            } else {
                cotisation.setDateFin(dateFin);
            }

            cotisation.setIdRepartitionPaiement(idRepartitionPaiement);
            cotisation.setIdExterne(idAssurance);
            cotisation.setIsImpotSource(Boolean.FALSE);
            cotisation.setTaux("");

            setMontantsCotisation(cotisation, montant, tauxAssurance.getValeurEmployeur(), nbJoursPeriode);

            if (isAjouter) {
                retValue[idMontant] = JadeStringUtil.toDouble(cotisation.getMontant());
            } else {
                retValue[idMontant] = JadeStringUtil.toDouble("-" + cotisation.getMontant());
            }

            // changer le signe (toujours des soustractions
            if (isAjouter) {
                cotisation.setMontant(cotisation.getMontant());
            } else {
                cotisation.setMontant("-" + cotisation.getMontant());
            }

            // sauver dans la base
            addCotisation(session, transaction, cotisation);
        }

        return retValue;
    }

    /**
     * cree une instance de cotisation en calculant le montant correct de la cotisation.
     * 
     * <p>
     * pondere le montant si le nombre de jours entre les dates de debut et dates de fin n'est pas egal au nombre de
     * jours complet de la periode.
     * </p>
     * 
     * @param cotisation
     * @param montant
     * @param taux
     * @param nbJoursPeriode
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    private void setMontantsCotisation(IJCotisation cotisation, String montant, String taux, long nbJoursPeriode)
            throws Exception {
        long daysBetween = IJRepartitionPaiementBuilder.CALENDAR.daysBetween(cotisation.getDateDebut(),
                cotisation.getDateFin());

        cotisation.setDateDebut(cotisation.getDateDebut());
        cotisation.setDateFin(cotisation.getDateFin());

        if (daysBetween != nbJoursPeriode) {
            // ponderer le montant en fonction du nombre de jours effectifs
            montant = JANumberFormatter.formatNoQuote(Double.parseDouble(montant) * daysBetween / nbJoursPeriode, 0.05,
                    2, JANumberFormatter.NEAR);
        }

        cotisation.setMontantBrut(montant);
        cotisation.setMontant(JANumberFormatter.formatNoQuote(PRCalcul.pourcentage100(montant, taux), 0.05, 2,
                JANumberFormatter.NEAR));
    }

    // calcule la somme des valeurs d'un tableau de doubles
    private double somme(double[] montants) {
        double retValue = 0.0;

        for (int idMontant = 0; idMontant < montants.length; ++idMontant) {
            retValue += montants[idMontant];
        }

        return retValue;
    }

    /**
     * recherche tous les taux d'impositions pour la periode donnee et insere une instance de IJCotisation dans la base
     * pour chacun d'eux ou, si le taux d'imposition a ete force par l'utilisateur, insere une seule cotisation avec ce
     * taux unique pour le canton d'imposition.
     * 
     * <p>
     * S'il y a plus d'un taux pour la periode donnee, les montants des cotisations seront ponderes par le nombre de
     * jours effectifs durant lesquels les taux sont en vigueur.
     * </p>
     * 
     * @param session
     * @param transaction
     *            DOCUMENT ME!
     * @param idRepartitionPaiement
     *            DOCUMENT ME!
     * @param montant
     * @param csCantonImposition
     * @param tauxImposition
     * @param dateDebut
     * @param dateFin
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     */
    private double[] soustraireImpots(BSession session, BITransaction transaction, String idRepartitionPaiement,
            String montant, String csCantonImposition, String tauxImposition, String dateDebut, String dateFin)
            throws Exception {
        double[] retValue = { 0.0 };

        if (JadeStringUtil.isBlankOrZero(csCantonImposition) && JadeStringUtil.isBlankOrZero(tauxImposition)) {

            throw new Exception(session.getLabel("TAUX_CANTONS_ERR"));
        }

        // le nombre de jours exact de la periode
        long nbJoursPeriode = IJRepartitionPaiementBuilder.CALENDAR.daysBetween(dateDebut, dateFin);

        // rechercher les taux d'imposition
        PRTauxImpositionManager tauxManager = new PRTauxImpositionManager();

        tauxManager.setForCsCanton(csCantonImposition);
        tauxManager.setForPeriode(dateDebut, dateFin);
        tauxManager.setForTypeImpot(IPRTauxImposition.CS_TARIF_D);
        tauxManager.setOrderBy(PRTauxImposition.FIELDNAME_DATEDEBUT);

        tauxManager.setSession(session);
        tauxManager.find();

        // s'il n'y a aucun taux d'imposition pour ce canton, on ne fait rien
        if (tauxManager.isEmpty()) {
            return retValue;
        }

        // creation des cotisation
        if (!JadeStringUtil.isDecimalEmpty(tauxImposition)) {
            // le taux d'imposition a ete redefini, on utilise que celui-ci et
            // l'id du taux imposition trouve
            IJCotisation cotisation = new IJCotisation();

            cotisation.setIdRepartitionPaiement(idRepartitionPaiement);
            cotisation.setDateDebut(dateDebut);
            cotisation.setDateFin(dateFin);
            cotisation.setIdExterne(((PRTauxImposition) tauxManager.get(0)).getIdTauxImposition());
            cotisation.setTaux(tauxImposition);
            cotisation.setIsImpotSource(Boolean.TRUE);
            setMontantsCotisation(cotisation, montant, tauxImposition, nbJoursPeriode);

            retValue[0] = JadeStringUtil.toDouble("-" + cotisation.getMontant());

            // changer le signe (toujours des soustractions)
            cotisation.setMontant("-" + cotisation.getMontant());

            // sauver dans la base
            addCotisation(session, transaction, cotisation);
        } else {
            // sinon on cree une cotisation pour chacun des taux retournes
            int idMontant = 0;

            retValue = new double[tauxManager.size()];

            for (Iterator iter = tauxManager.iterator(); iter.hasNext(); ++idMontant) {
                PRTauxImposition taux = (PRTauxImposition) iter.next();
                IJCotisation cotisation = new IJCotisation();

                // date debut
                if (IJRepartitionPaiementBuilder.CALENDAR.compare(taux.getDateDebut(), dateDebut) == JACalendar.COMPARE_FIRSTLOWER) {
                    cotisation.setDateDebut(dateDebut);
                } else {
                    cotisation.setDateDebut(taux.getDateDebut());
                }

                // date fin
                if (!JAUtil.isDateEmpty(taux.getDateFin())
                        && (IJRepartitionPaiementBuilder.CALENDAR.compare(taux.getDateFin(), dateFin) == JACalendar.COMPARE_FIRSTLOWER)) {
                    cotisation.setDateFin(taux.getDateFin());
                } else {
                    cotisation.setDateFin(dateFin);
                }

                cotisation.setIdRepartitionPaiement(idRepartitionPaiement);
                cotisation.setIdExterne(taux.getIdTauxImposition());
                cotisation.setIsImpotSource(Boolean.TRUE);
                cotisation.setTaux(taux.getTaux());

                setMontantsCotisation(cotisation, montant, taux.getTaux(), nbJoursPeriode);
                retValue[idMontant] = JadeStringUtil.toDouble("-" + cotisation.getMontant());

                // changer le signe (toujours des soustractions)
                cotisation.setMontant("-" + cotisation.getMontant());

                // sauver dans la base
                addCotisation(session, transaction, cotisation);
            }
        }

        return retValue;
    }

}
