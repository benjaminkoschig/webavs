package globaz.ij.module;

import globaz.babel.utils.BabelContainer;
import globaz.babel.utils.CatalogueText;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.application.IJApplication;
import globaz.ij.db.prestations.IJRepartitionPaiements;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJSitProJointEmployeur;
import globaz.ij.db.prononces.IJSitProJointEmployeurManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.prestation.db.tauxImposition.PRTauxImposition;
import globaz.prestation.db.tauxImposition.PRTauxImpositionManager;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tauxImposition.api.IPRTauxImposition;
import globaz.prestation.tools.PRCalcul;
import java.util.Iterator;
import java.util.List;

public class IJDecisionCotisationBuilder {

    class Deduction {

        public String montantCotisation = "";
        public String tauxCotisation = "";
        public String typeDeduction = "";

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
            String montant

    ) throws Exception {

        double retValue = 0.0;
        String csSexe = prononce.loadDemande(transaction).loadTiers().getProperty(PRTiersWrapper.PROPERTY_SEXE);

        // Si situation prof == AGRICULTEUR_INDEPENDANT_LFA
        if (IIJPrononce.CS_AGRICULTEUR_INDEPENDANT_LFA.equals(prononce.getCsStatutProfessionnel())) {

            // salarié
            // moins AVS/AI/APG paritaire
            retValue += somme(creerAssurance(session, transaction, montant, "",
                    PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                            PRAffiliationHelper.TYPE_PERSONNEL), csSexe, prononce.getDateDebutPrononce()));

            // moins AC paritaire
            retValue += somme(creerAssurance(session, transaction, montant, "",
                    PRAffiliationHelper.GENRE_AC.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                            PRAffiliationHelper.TYPE_PARITAIRE), csSexe, prononce.getDateDebutPrononce()));

        }

        // Les nons actifs ne paient pas de cotisations AC (ex. étudiant de - de
        // 20 ans).
        else if (IIJPrononce.CS_NON_ACTIF.equals(prononce.getCsStatutProfessionnel())) {

            // moins AVS/AI/APG personnelle
            retValue += somme(creerAssurance(session, transaction, montant, "",
                    PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                            PRAffiliationHelper.TYPE_PERSONNEL), csSexe, prononce.getDateDebutPrononce()));

        }

        else if (IIJPrononce.CS_INDEPENDANT.equals(prononce.getCsStatutProfessionnel())) {

            // moins AVS/AI/APG personnelle
            retValue += somme(creerAssurance(session, transaction, montant, "",
                    PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                            PRAffiliationHelper.TYPE_PERSONNEL), csSexe, prononce.getDateDebutPrononce()));

        }
        // supposé salarié
        else {
            // salarié
            // moins AVS/AI/APG paritaire

            retValue += somme(creerAssurance(session, transaction, montant, "",
                    PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                            PRAffiliationHelper.TYPE_PARITAIRE), csSexe, prononce.getDateDebutPrononce()));

            // moins AC paritaire

            retValue += somme(creerAssurance(session, transaction, montant, "",
                    PRAffiliationHelper.GENRE_AC.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                            PRAffiliationHelper.TYPE_PARITAIRE), csSexe, prononce.getDateDebutPrononce()));

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
            String montant

    ) throws Exception {

        double retValue = 0.0;
        String csSexe = prononce.loadDemande(transaction).loadTiers().getProperty(PRTiersWrapper.PROPERTY_SEXE);
        String idTiersAssure = prononce.loadDemande(transaction).loadTiers()
                .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);

        IJSitProJointEmployeurManager mgr = new IJSitProJointEmployeurManager();
        mgr.setForIdGrandeIJ(prononce.getIdPrononce());
        mgr.setSession(session);
        mgr.find(transaction, 1);
        String idTiersSitProf = null;
        if (!mgr.isEmpty()) {
            idTiersSitProf = ((IJSitProJointEmployeur) mgr.getFirstEntity()).getIdTiers();
        }

        // Si le bénéficiare est l'assuré lui-même, on considère qu'il est
        // indépendant
        if (idTiersAssure.equals(idTiersSitProf)) {

            // Indépendant + salarie
            if (IIJPrononce.CS_SALARIE.equals(prononce.getCsStatutProfessionnel())) {

                // indépendant
                // moins AVS/AI/APG personnel
                // moins AC personnel
                retValue += somme(creerAssurance(session, transaction, montant, "",
                        PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                                PRAffiliationHelper.TYPE_PERSONNEL), csSexe, prononce.getDateDebutPrononce()));

                retValue += somme(creerAssurance(session, transaction, montant, "",
                        PRAffiliationHelper.GENRE_AC.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                                PRAffiliationHelper.TYPE_PERSONNEL), csSexe, prononce.getDateDebutPrononce()));

            }

            else if (IIJPrononce.CS_INDEPENDANT.equals(prononce.getCsStatutProfessionnel())) {
                // moins AVS/AI/APG personnel
                retValue += somme(creerAssurance(session, transaction, montant, "",
                        PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                                PRAffiliationHelper.TYPE_PERSONNEL), csSexe, prononce.getDateDebutPrononce()));
            }

            else if (IIJPrononce.CS_NON_ACTIF.equals(prononce.getCsStatutProfessionnel())) {
                // moins AVS/AI/APG personnel
                retValue += somme(creerAssurance(session, transaction, montant, "",
                        PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                                PRAffiliationHelper.TYPE_PERSONNEL), csSexe, prononce.getDateDebutPrononce()));
            } else if (IIJPrononce.CS_AGRICULTEUR_INDEPENDANT_LFA.equals(prononce.getCsStatutProfessionnel())) {

                // add AVS/AI/APG personnel
                // add AC personnel
                retValue += somme(creerAssurance(session, transaction, montant, "",
                        PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                                PRAffiliationHelper.TYPE_PARITAIRE), csSexe, prononce.getDateDebutPrononce()));

                // double[] montantsAC = (creerAssurance(session, transaction,
                // repartition.getIdRepartitionPaiement(),
                // repartition.getMontantBrut(),
                // PRAffiliationHelper.GENRE_AC.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                // PRAffiliationHelper.TYPE_PARITAIRE),
                // csSexe, baseIndemnisation.getDateDebutPeriode(),
                // baseIndemnisation.getDateFinPeriode(), true));

                // Add LFA
                retValue += somme(creerAssurance(session, transaction, montant, "",
                        PRAffiliationHelper.GENRE_LFA.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                                PRAffiliationHelper.TYPE_PARITAIRE), csSexe, prononce.getDateDebutPrononce()));
            }

        }
        // N'est pas un indépendant
        else {
            // add AVS/AI/APG personnel
            // add AC personnel
            retValue += somme(creerAssurance(session, transaction, montant, "",
                    PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                            PRAffiliationHelper.TYPE_PARITAIRE), csSexe, prononce.getDateDebutPrononce()));

            retValue += somme(creerAssurance(session, transaction, montant, "",
                    PRAffiliationHelper.GENRE_AC.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                            PRAffiliationHelper.TYPE_PARITAIRE), csSexe, prononce.getDateDebutPrononce()));

            if (IIJPrononce.CS_AGRICULTEUR_INDEPENDANT_LFA.equals(prononce.getCsStatutProfessionnel())) {

                // Add LFA
                retValue += somme(creerAssurance(session, transaction, montant, "",
                        PRAffiliationHelper.GENRE_LFA.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                                PRAffiliationHelper.TYPE_PARITAIRE), csSexe, prononce.getDateDebutPrononce()));
            }

        }
        return retValue;
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
     * @return le libelle des cotisations pour cette repartition
     * 
     * @throws Exception
     * 
     * @see #buildAssurancesGrandeIJ(BSession, IJPrononce, IJRepartitionPaiements)
     * @see #buildAssurancesPetiteIJ(BSession, IJPrononce, IJRepartitionPaiements)
     */
    public String buildCotisationsLibelleAssure(BSession session, BITransaction transaction, IJPrononce prononce,
            String montant, BabelContainer babelContainer, CatalogueText ct) throws Exception {

        String retValue = "";

        String AVS_AI_APG = babelContainer.getTexte(ct.getKey(), 10, 1);
        String AC = babelContainer.getTexte(ct.getKey(), 10, 2);

        // String AVS_AI_APG = session.getLabel("TYPE_COTI_DECISION_AVS_AI_APG");
        // String AC = session.getLabel("TYPE_COTI_DECISION_AC");

        // Si situation prof == AGRICULTEUR_INDEPENDANT_LFA
        if (IIJPrononce.CS_AGRICULTEUR_INDEPENDANT_LFA.equals(prononce.getCsStatutProfessionnel())) {

            // salarié
            // moins AVS/AI/APG/AC paritaire
            retValue += AVS_AI_APG + AC;

        }

        // Les nons actifs ne paient pas de cotisations AC (ex. étudiant de - de
        // 20 ans).
        else if (IIJPrononce.CS_NON_ACTIF.equals(prononce.getCsStatutProfessionnel())) {

            // moins AVS/AI/APG personnelle
            retValue += AVS_AI_APG;

        }

        else if (IIJPrononce.CS_INDEPENDANT.equals(prononce.getCsStatutProfessionnel())) {

            // moins AVS/AI/APG personnelle
            retValue += AVS_AI_APG;

        }
        // supposé salarié
        else {
            // salarié
            // moins AVS/AI/APG/AC paritaire
            retValue += AVS_AI_APG + AC;

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
     * @return le libelle des cotisations pour cette repartition
     * 
     * @throws Exception
     * 
     * @see #buildAssurancesGrandeIJ(BSession, IJPrononce, IJRepartitionPaiements)
     * @see #buildAssurancesPetiteIJ(BSession, IJPrononce, IJRepartitionPaiements)
     */

    public String buildCotisationsLibelleEmployeur(BSession session, BITransaction transaction, IJPrononce prononce,
            String montant, BabelContainer babelContainer, CatalogueText ct) throws Exception {

        String retValue = "";
        String idTiersAssure = prononce.loadDemande(transaction).loadTiers()
                .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);

        IJSitProJointEmployeurManager mgr = new IJSitProJointEmployeurManager();
        mgr.setForIdGrandeIJ(prononce.getIdPrononce());
        mgr.setSession(session);
        mgr.find(transaction, 1);
        String idTiersSitProf = null;
        if (!mgr.isEmpty()) {
            idTiersSitProf = ((IJSitProJointEmployeur) mgr.getFirstEntity()).getIdTiers();
        }

        String AVS_AI_APG = babelContainer.getTexte(ct.getKey(), 10, 1);
        String AC = babelContainer.getTexte(ct.getKey(), 10, 2);
        String LFA = babelContainer.getTexte(ct.getKey(), 10, 3);

        // String AVS_AI_APG = session.getLabel("TYPE_COTI_DECISION_AVS_AI_APG");
        // String AC = session.getLabel("TYPE_COTI_DECISION_AC");
        // String LFA = session.getLabel("TYPE_COTI_DECISION_LFA");

        // Si le bénéficiare est l'assuré lui-même, on considère qu'il est
        // indépendant
        if (idTiersAssure.equals(idTiersSitProf)) {

            // Indépendant + salarie
            if (IIJPrononce.CS_SALARIE.equals(prononce.getCsStatutProfessionnel())) {

                // indépendant
                // moins AVS/AI/APG personnel
                // moins AC personnel
                retValue += AVS_AI_APG + AC;
            }

            else if (IIJPrononce.CS_INDEPENDANT.equals(prononce.getCsStatutProfessionnel())) {
                // moins AVS/AI/APG personnel
                retValue += AVS_AI_APG;
            }

            else if (IIJPrononce.CS_NON_ACTIF.equals(prononce.getCsStatutProfessionnel())) {
                // moins AVS/AI/APG personnel
                retValue += AVS_AI_APG;
            } else if (IIJPrononce.CS_AGRICULTEUR_INDEPENDANT_LFA.equals(prononce.getCsStatutProfessionnel())) {

                // add AVS/AI/APG personnel
                // add AC personnel
                // Add LFA
                retValue += AVS_AI_APG + AC + LFA;

            }

        }
        // N'est pas un indépendant
        else {
            // add AVS/AI/APG personnel
            // add AC personnel
            retValue += AVS_AI_APG + AC;

            if (IIJPrononce.CS_AGRICULTEUR_INDEPENDANT_LFA.equals(prononce.getCsStatutProfessionnel())) {

                // Add LFA
                retValue += LFA;

            }

        }

        return retValue;
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
     * @return le taux total des cotisations pour cette repartition
     * 
     * @throws Exception
     * 
     * @see #buildAssurancesGrandeIJ(BSession, IJPrononce, IJRepartitionPaiements)
     * @see #buildAssurancesPetiteIJ(BSession, IJPrononce, IJRepartitionPaiements)
     */
    public double buildCotisationsTauxAssure(BSession session, BITransaction transaction, IJPrononce prononce,
            String montant) throws Exception {

        double retValue = 0.0;
        String csSexe = prononce.loadDemande(transaction).loadTiers().getProperty(PRTiersWrapper.PROPERTY_SEXE);

        // Si situation prof == AGRICULTEUR_INDEPENDANT_LFA
        if (IIJPrononce.CS_AGRICULTEUR_INDEPENDANT_LFA.equals(prononce.getCsStatutProfessionnel())) {

            // salarié
            // moins AVS/AI/APG paritaire
            retValue += sommeTaux(creerAssurance(session, transaction, montant, "",
                    PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                            PRAffiliationHelper.TYPE_PERSONNEL), csSexe, prononce.getDateDebutPrononce()));

            // moins AC paritaire
            retValue += sommeTaux(creerAssurance(session, transaction, montant, "",
                    PRAffiliationHelper.GENRE_AC.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                            PRAffiliationHelper.TYPE_PARITAIRE), csSexe, prononce.getDateDebutPrononce()));

        }

        // Les nons actifs ne paient pas de cotisations AC (ex. étudiant de - de
        // 20 ans).
        else if (IIJPrononce.CS_NON_ACTIF.equals(prononce.getCsStatutProfessionnel())) {

            // moins AVS/AI/APG personnelle
            retValue += sommeTaux(creerAssurance(session, transaction, montant, "",
                    PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                            PRAffiliationHelper.TYPE_PERSONNEL), csSexe, prononce.getDateDebutPrononce()));

        }

        else if (IIJPrononce.CS_INDEPENDANT.equals(prononce.getCsStatutProfessionnel())) {

            // moins AVS/AI/APG personnelle
            retValue += sommeTaux(creerAssurance(session, transaction, montant, "",
                    PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                            PRAffiliationHelper.TYPE_PERSONNEL), csSexe, prononce.getDateDebutPrononce()));

        }
        // supposé salarié
        else {
            // salarié
            // moins AVS/AI/APG paritaire

            retValue += sommeTaux(creerAssurance(session, transaction, montant, "",
                    PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                            PRAffiliationHelper.TYPE_PARITAIRE), csSexe, prononce.getDateDebutPrononce()));

            // moins AC paritaire

            retValue += sommeTaux(creerAssurance(session, transaction, montant, "",
                    PRAffiliationHelper.GENRE_AC.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                            PRAffiliationHelper.TYPE_PARITAIRE), csSexe, prononce.getDateDebutPrononce()));

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
     * @return le taux total des cotisations pour cette repartition
     * 
     * @throws Exception
     * 
     * @see #buildAssurancesGrandeIJ(BSession, IJPrononce, IJRepartitionPaiements)
     * @see #buildAssurancesPetiteIJ(BSession, IJPrononce, IJRepartitionPaiements)
     */

    public double buildCotisationsTauxEmployeur(BSession session, BITransaction transaction, IJPrononce prononce,
            String montant) throws Exception {

        double retValue = 0.0;
        String csSexe = prononce.loadDemande(transaction).loadTiers().getProperty(PRTiersWrapper.PROPERTY_SEXE);
        String idTiersAssure = prononce.loadDemande(transaction).loadTiers()
                .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);

        IJSitProJointEmployeurManager mgr = new IJSitProJointEmployeurManager();
        mgr.setForIdGrandeIJ(prononce.getIdPrononce());
        mgr.setSession(session);
        mgr.find(transaction, 1);
        String idTiersSitProf = null;
        if (!mgr.isEmpty()) {
            idTiersSitProf = ((IJSitProJointEmployeur) mgr.getFirstEntity()).getIdTiers();
        }

        // Si le bénéficiare est l'assuré lui-même, on considère qu'il est
        // indépendant
        if (idTiersAssure.equals(idTiersSitProf)) {

            // Indépendant + salarie
            if (IIJPrononce.CS_SALARIE.equals(prononce.getCsStatutProfessionnel())) {

                // indépendant
                // moins AVS/AI/APG personnel
                // moins AC personnel
                retValue += sommeTaux(creerAssurance(session, transaction, montant, "",
                        PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                                PRAffiliationHelper.TYPE_PERSONNEL), csSexe, prononce.getDateDebutPrononce()));

                retValue += sommeTaux(creerAssurance(session, transaction, montant, "",
                        PRAffiliationHelper.GENRE_AC.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                                PRAffiliationHelper.TYPE_PERSONNEL), csSexe, prononce.getDateDebutPrononce()));

            }

            else if (IIJPrononce.CS_INDEPENDANT.equals(prononce.getCsStatutProfessionnel())) {
                // moins AVS/AI/APG personnel
                retValue += sommeTaux(creerAssurance(session, transaction, montant, "",
                        PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                                PRAffiliationHelper.TYPE_PERSONNEL), csSexe, prononce.getDateDebutPrononce()));
            }

            else if (IIJPrononce.CS_NON_ACTIF.equals(prononce.getCsStatutProfessionnel())) {
                // moins AVS/AI/APG personnel
                retValue += sommeTaux(creerAssurance(session, transaction, montant, "",
                        PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                                PRAffiliationHelper.TYPE_PERSONNEL), csSexe, prononce.getDateDebutPrononce()));
            } else if (IIJPrononce.CS_AGRICULTEUR_INDEPENDANT_LFA.equals(prononce.getCsStatutProfessionnel())) {

                // add AVS/AI/APG personnel
                // add AC personnel
                retValue += sommeTaux(creerAssurance(session, transaction, montant, "",
                        PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                                PRAffiliationHelper.TYPE_PARITAIRE), csSexe, prononce.getDateDebutPrononce()));

                // double[] montantsAC = (creerAssurance(session, transaction,
                // repartition.getIdRepartitionPaiement(),
                // repartition.getMontantBrut(),
                // PRAffiliationHelper.GENRE_AC.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                // PRAffiliationHelper.TYPE_PARITAIRE),
                // csSexe, baseIndemnisation.getDateDebutPeriode(),
                // baseIndemnisation.getDateFinPeriode(), true));

                // Add LFA
                retValue += sommeTaux(creerAssurance(session, transaction, montant, "",
                        PRAffiliationHelper.GENRE_LFA.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                                PRAffiliationHelper.TYPE_PARITAIRE), csSexe, prononce.getDateDebutPrononce()));
            }

        }
        // N'est pas un indépendant
        else {
            // add AVS/AI/APG personnel
            // add AC personnel
            retValue += sommeTaux(creerAssurance(session, transaction, montant, "",
                    PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                            PRAffiliationHelper.TYPE_PARITAIRE), csSexe, prononce.getDateDebutPrononce()));

            retValue += sommeTaux(creerAssurance(session, transaction, montant, "",
                    PRAffiliationHelper.GENRE_AC.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                            PRAffiliationHelper.TYPE_PARITAIRE), csSexe, prononce.getDateDebutPrononce()));

            if (IIJPrononce.CS_AGRICULTEUR_INDEPENDANT_LFA.equals(prononce.getCsStatutProfessionnel())) {

                // Add LFA
                retValue += sommeTaux(creerAssurance(session, transaction, montant, "",
                        PRAffiliationHelper.GENRE_LFA.getIdAssurance(IJApplication.DEFAULT_APPLICATION_IJ,
                                PRAffiliationHelper.TYPE_PARITAIRE), csSexe, prononce.getDateDebutPrononce()));
            }

        }
        return retValue;
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
    private Deduction creerAssurance(BSession session, BITransaction transaction, String montant,
            String genreAssurance, String idAssurance, String csSexe, String date) throws Exception {

        Deduction deduction = new Deduction();

        if (JadeStringUtil.isIntegerEmpty(idAssurance)) {
            // il n'y a pas d'assurances, on n'ajoute rien
            return null;
        }

        List taux = PRAffiliationHelper.getTauxAssurance(session, idAssurance, csSexe, date, date);

        for (Iterator iter = taux.iterator(); iter.hasNext();) {

            AFTauxAssurance tauxAssurance = (AFTauxAssurance) iter.next();

            deduction.montantCotisation = getMontantsCotisation(montant, tauxAssurance.getValeurEmployeur());
            deduction.tauxCotisation = tauxAssurance.getValeurEmployeur();
            deduction.typeDeduction = genreAssurance;

            break;
        }

        return deduction;
    }

    private String getMontantImpot(String montant, String taux) throws Exception {

        return JANumberFormatter.formatNoQuote(PRCalcul.pourcentage100(montant, taux), 0.05, 2, JANumberFormatter.NEAR);

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
    private String getMontantsCotisation(String montant, String taux) throws Exception {

        return JANumberFormatter.formatNoQuote(PRCalcul.pourcentage100(montant, taux), 0.05, 2, JANumberFormatter.NEAR);

    }

    // calcule la somme des valeurs d'un tableau de deductions
    private double somme(Deduction deduction) {

        double retValue = 0.0;
        retValue += new Double((deduction).montantCotisation).doubleValue();

        return retValue;
    }

    // calcule la somme des taux d'un tableau de deductions
    private double sommeTaux(Deduction deduction) {

        double retValue = 0.0;
        retValue += new Double((deduction).tauxCotisation).doubleValue();

        return retValue;
    }

    /**
     * Recherche le taux d'imposition selon le canton si celui-ci n'est pas spécifié. Puis calcule le montant en
     * fonction du taux.
     * 
     * @param session
     * @param transaction
     *            DOCUMENT ME!
     * @param montant
     * @param csCantonImposition
     * @param tauxImposition
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     */
    public String[] soustraireImpots(BSession session, BITransaction transaction, String montant,
            String csCantonImposition, String tauxImposition) throws Exception {

        if (JadeStringUtil.isBlankOrZero(csCantonImposition) && JadeStringUtil.isBlankOrZero(tauxImposition)) {

            throw new Exception(session.getLabel("TAUX_CANTONS_ERR"));
        }

        // Si taux non renseigné, on prend le taux du canton
        if (JadeStringUtil.isBlankOrZero(tauxImposition)) {

            PRTauxImpositionManager tauxManager = new PRTauxImpositionManager();

            tauxManager.setForCsCanton(csCantonImposition);
            tauxManager.setForTypeImpot(IPRTauxImposition.CS_TARIF_D);
            tauxManager.setOrderBy(PRTauxImposition.FIELDNAME_DATEDEBUT);
            tauxManager.setSession(session);
            tauxManager.find();

            tauxImposition = ((PRTauxImposition) tauxManager.getFirstEntity()).getTaux();
        }

        String[] ret = { getMontantImpot(montant, tauxImposition), tauxImposition };

        return ret;

    }

}