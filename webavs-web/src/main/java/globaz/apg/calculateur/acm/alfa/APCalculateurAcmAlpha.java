package globaz.apg.calculateur.acm.alfa;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.db.droits.APEnfantAPG;
import globaz.apg.db.droits.APEnfantAPGManager;
import globaz.apg.db.droits.APSituationFamilialeAPG;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.db.prestation.APRepartitionPaiementsManager;
import globaz.apg.helpers.droits.APSituationProfessionnelleHelper;
import globaz.apg.utils.APGUtils;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.prestation.application.PRAbstractApplication;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Descpription
 * 
 * @author scr Date de création 13 oct. 05
 */
public class APCalculateurAcmAlpha {

    private static final String CS_MARIE = "515002";

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    public static final String KEY_TOTAL_JOURNALIER_ACM = "TOTAL_JOURNALIER_ACM";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe ACMCalculateur.
     */
    public APCalculateurAcmAlpha() {
        super();
        // TODO Raccord de constructeur auto-généré
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param genreService
     *            DOCUMENT ME!
     * @param idDroit
     *            DOCUMENT ME!
     * @param revenuMoyenDeterminant
     *            DOCUMENT ME!
     * @param montantJournalier
     *            DOCUMENT ME!
     * @param dateDebutPrestation
     *            DOCUMENT ME!
     * @param dateFinPrestation
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public HashMap calculerMontantACM(BSession session, BTransaction transaction, String genreService, String idDroit,
            String dateDebutPrestation, String dateFinPrestation) throws Exception {

        return this.calculerMontantACM(session, transaction, genreService, idDroit, dateDebutPrestation,
                dateFinPrestation, false);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param genreService
     *            DOCUMENT ME!
     * @param idDroit
     *            DOCUMENT ME!
     * @param revenuMoyenDeterminant
     *            DOCUMENT ME!
     * @param montantJournalier
     *            DOCUMENT ME!
     * @param dateDebutPrestation
     *            DOCUMENT ME!
     * @param dateFinPrestation
     *            DOCUMENT ME!
     * @param plusDeTrenteJours
     *            indique si des prestations ont deja ete versee pour une duree de 30 jours.
     * @return une map contanant le montant total des prestations ACM (avec la cle TOTAL_JOURNALIER_ACM) et le montant
     *         pour chacun des employeur (avec comme cle leur id employeur)
     * @throws Exception
     *             DOCUMENT ME!
     */
    public HashMap calculerMontantACM(BSession session, BTransaction transaction, String genreService, String idDroit,
            String dateDebutPrestation, String dateFinPrestation, boolean plusDeTrenteJours) throws Exception {

        HashMap result = new HashMap();

        BigDecimal montantAcmTotal = new BigDecimal("0");

        APDroitLAPG droit = APGUtils.loadDroit(session, idDroit, genreService);

        // on recherche les situation professionnelle de ce droit
        APSituationProfessionnelleManager sitProManager = new APSituationProfessionnelleManager();
        sitProManager.setSession(session);
        sitProManager.setForIdDroit(idDroit);
        sitProManager.find(transaction);

        Iterator iter = sitProManager.iterator();

        // pour toutes les situation prof. si la coche ACM est mise
        // on calcul l'acm pour ce cas et on l'ajoute au total
        while (iter.hasNext()) {

            APSituationProfessionnelle sitPro = (APSituationProfessionnelle) iter.next();
            if (sitPro.getHasAcmAlphaPrestations().booleanValue() && sitPro.getIsVersementEmployeur().booleanValue()) {

                // le montant de l'ACM pour cette situation prof.
                BigDecimal montantACM = new BigDecimal(0);

                // le revenu moyen determinant est donne par la situation prof.
                FWCurrency revenuMoyenDeterminant = APSituationProfessionnelleHelper.getSalaireJournalierVerse(sitPro);
                revenuMoyenDeterminant = new FWCurrency(JANumberFormatter.format(revenuMoyenDeterminant.toString(), 1,
                        2, JANumberFormatter.SUP));

                BigDecimal rmd = null;
                // pour la Maternité, arrondi au franc superieur
                // pour les APG, arrondi au demi franc superieur
                if (droit instanceof APDroitMaternite) {
                    rmd = new BigDecimal(JANumberFormatter.deQuote(JANumberFormatter.format(
                            revenuMoyenDeterminant.toString(), 1, 2, JANumberFormatter.SUP)));
                } else {
                    rmd = new BigDecimal(JANumberFormatter.deQuote(JANumberFormatter.format(
                            revenuMoyenDeterminant.toString(), 0.5, 2, JANumberFormatter.SUP)));
                }

                // on retrouve le montant journalier deja verse a l'employeur
                // pour les prestations Standard et LAMat
                String montantJournalier = retrieveMontantJournalierPrestationStdLAMat(idDroit,
                        sitPro.getIdEmployeur(), dateDebutPrestation, dateFinPrestation, session).toString();

                // Calculer montant ACM pour droit maternite
                if (droit instanceof APDroitMaternite) {
                    // Pas d'ACM pour les prestations avant 01.01.1997
                    // Dans le cas de la caisse GE on permet cela, mais on
                    // retourne un montant de 0 pour qu'il n'y ait pas de
                    // prestations
                    if ("true".equals(PRAbstractApplication.getApplication(APApplication.DEFAULT_APPLICATION_APG)
                            .getProperty("isDroitMaterniteCantonale"))) {
                        if (BSessionUtil.compareDateFirstLower(session, dateDebutPrestation, "01.07.2005")) {
                            continue;
                        }
                    } else {
                        if (BSessionUtil.compareDateFirstLowerOrEqual(session, dateDebutPrestation, "01.07.2005")) {
                            throw new Exception(
                                    session.getLabel("LABEL_ACM_NON_ALLOUE_POUR_PRESTATIONS_MAT_AVANT_01072005"));
                        }
                    }

                    BigDecimal mj = new BigDecimal(JANumberFormatter.deQuote(montantJournalier));
                    montantACM = rmd.subtract(mj);

                }

                // Calculer montant ACM pour droit APG
                else {
                    // Pas d'ACM pour les genres de service 30 ou 50
                    if (IAPDroitLAPG.CS_FORMATION_DE_CADRE_JEUNESSE_SPORTS.equals(genreService)
                            || IAPDroitLAPG.CS_COURS_MONITEURS_JEUNES_TIREURS.equals(genreService)) {
                        // throw new
                        // Exception(session.getLabel("ACM_NON_ALLOUE_GENRE_SERVICE_30_50"));
                        // pour permetre la generation automatique de toutes les
                        // prestations
                        continue;
                    }

                    // Pas d'ACM pour les prestations avant 01.01.1997
                    // Dans le cas de la caisse GE on permet cela, mais on
                    // retourne un montant de 0 pour qu'il n'y ait pas de
                    // prestations
                    if ("true".equals(PRAbstractApplication.getApplication(APApplication.DEFAULT_APPLICATION_APG)
                            .getProperty("isDroitMaterniteCantonale"))) {
                        if (BSessionUtil.compareDateFirstLowerOrEqual(session, dateDebutPrestation, "01.01.1997")) {
                            continue;
                        }
                    } else {
                        if (BSessionUtil.compareDateFirstLowerOrEqual(session, dateDebutPrestation, "01.01.1997")) {
                            throw new Exception(session.getLabel("ACM_NON_ALLOUE_POUR_PRESTATIONS_AVANT_01011997"));
                        }
                    }

                    int nbrEnfants = 0;

                    // Calcul du nombre d'enfant
                    nbrEnfants = getNombreEnfantsAPG(session, transaction, (APDroitAPG) droit, dateDebutPrestation,
                            dateFinPrestation);

                    // si l'assure est marie, il a droit aux memes preestations
                    // que si il a un enfant
                    boolean isEtatCivilMarie = false;
                    PRTiersWrapper tiers = droit.loadDemande().loadTiers();
                    if (APCalculateurAcmAlpha.CS_MARIE.equals(tiers
                            .getProperty(PRTiersWrapper.PROPERTY_PERSONNE_AVS_ETAT_CIVIL))) {
                        isEtatCivilMarie = true;
                    }

                    // Recrue sans enfant : genre sercie 11, 13, 21, 41
                    if ((IAPDroitLAPG.CS_SERVICE_EN_QUALITE_DE_RECRUE.equals(genreService)
                            || IAPDroitLAPG.CS_RECRUTEMENT.equals(genreService)
                            || IAPDroitLAPG.CS_FORMATION_DE_BASE.equals(genreService) || IAPDroitLAPG.CS_SERVICE_CIVIL_AVEC_TAUX_RECRUES
                                .equals(genreService)
                            || IAPDroitLAPG.CS_SERVICE_INTERRUPTION_AVANT_ECOLE_SOUS_OFF.equals(genreService) 
                                ) && ((nbrEnfants == 0) && !isEtatCivilMarie)) {
                        // Le revenu journalier est pris à 50%
                        rmd = rmd.multiply(new BigDecimal("0.5"));
                    }
                    // Recrue avec enfant : genre sercie 11, 13, 21, 41
                    else if ((IAPDroitLAPG.CS_SERVICE_EN_QUALITE_DE_RECRUE.equals(genreService)
                            || IAPDroitLAPG.CS_RECRUTEMENT.equals(genreService)
                            || IAPDroitLAPG.CS_FORMATION_DE_BASE.equals(genreService) || IAPDroitLAPG.CS_SERVICE_CIVIL_AVEC_TAUX_RECRUES
                                .equals(genreService)
                            || IAPDroitLAPG.CS_SERVICE_INTERRUPTION_AVANT_ECOLE_SOUS_OFF.equals(genreService)
                                ) && ((nbrEnfants > 0) || isEtatCivilMarie)) {
                        // Le revenu journalier est pris à 75%
                        rmd = rmd.multiply(new BigDecimal("0.75"));
                    }
                    // service avancement dès le 31 jours
                    else if (IAPDroitLAPG.CS_SERVICE_AVANCEMENT.equals(genreService) && plusDeTrenteJours) {
                        // sans enfants
                        if ((nbrEnfants == 0) && !isEtatCivilMarie) {
                            // Le revenu journalier est pris à 50%
                            rmd = rmd.multiply(new BigDecimal("0.50"));
                        } else {
                            // Le revenu journalier est pris à 80%
                            rmd = rmd.multiply(new BigDecimal("0.80"));
                        }
                    }

                    BigDecimal mj = new BigDecimal(JANumberFormatter.deQuote(montantJournalier));
                    montantACM = rmd.subtract(mj);
                }

                // si le montant ACM pour cet employeur est plus grand que zero,
                // on l'ajoute au montant ACM total
                if (montantACM.compareTo(new BigDecimal(0)) > 0) {
                    result.put(sitPro.getIdEmployeur(), montantACM);
                    montantAcmTotal = montantAcmTotal.add(montantACM);
                }
            }
        }

        // Le montant ACM total est > 0
        if (montantAcmTotal.compareTo(new BigDecimal(0)) > 0) {
            result.put(APCalculateurAcmAlpha.KEY_TOTAL_JOURNALIER_ACM, montantAcmTotal);
        } else {
            result.put(APCalculateurAcmAlpha.KEY_TOTAL_JOURNALIER_ACM, new BigDecimal(0));
        }

        return result;
    }

    /**
     * Retourne le nombre d'enfant du droit pour la période données
     * 
     * @param session
     * @param transaction
     * @param droit
     * @param dateDebutPrestation
     * @param dateFinPrestation
     * @return
     * @throws Exception
     */
    private int getNombreEnfantsAPG(BSession session, BTransaction transaction, APDroitAPG droit,
            String dateDebutPrestation, String dateFinPrestation) throws Exception {
        // Récupération du nombre d'enfant
        APSituationFamilialeAPG sitFam = new APSituationFamilialeAPG();
        sitFam.setSession(session);
        sitFam.setIdSitFamAPG(droit.getIdSituationFam());
        sitFam.retrieve(transaction);

        APEnfantAPGManager enfantsMgr = new APEnfantAPGManager();
        enfantsMgr.setSession(session);
        enfantsMgr.setForIdSituationFamiliale(sitFam.getIdSitFamAPG());
        enfantsMgr.find(transaction);

        int nbrEnfants = Integer.parseInt(sitFam.getNbrEnfantsDebutDroit());

        // On ne prends que les enfants dont date naisance incluse pour la
        // période du droit
        // conçernée
        for (Iterator iter = enfantsMgr.iterator(); iter.hasNext();) {
            APEnfantAPG element = (APEnfantAPG) iter.next();

            if (BSessionUtil.compareDateBetweenOrEqual(session, dateDebutPrestation, dateFinPrestation,
                    element.getDateDebutDroit()) == true) {
                nbrEnfants++;
            }
        }

        return nbrEnfants;
    }

    /**
     * Donne le montant journalier des prestations standard et LAMat (si il y en a) donne a un employeur pour un droit
     * et une periode donnee
     * 
     * @param idDroit
     * @param idEmployeur
     * @return
     */
    private FWCurrency retrieveMontantJournalierPrestationStdLAMat(String idDroit, String idEmployeur,
            String dateDebut, String dateFin, BSession session) {
        FWCurrency montantJournalierPrestationStdLAMat = new FWCurrency("0");

        // on cherche une prestation de type standard et LAMat
        APPrestationManager prestManager = new APPrestationManager();
        prestManager.setSession(session);
        prestManager.setForIdDroit(idDroit);
        prestManager.setInDateDebut(dateDebut);
        prestManager.setInDateFin(dateFin);
        try {
            prestManager.find();
            Iterator iterPrest = prestManager.iterator();

            while (iterPrest.hasNext()) {

                APPrestation prestation = (APPrestation) iterPrest.next();

                // on cherche les repartition de la prestation
                APRepartitionPaiementsManager repManager = new APRepartitionPaiementsManager();
                repManager.setSession(session);
                repManager.setForIdPrestation(prestation.getIdPrestationApg());
                repManager.find();

                // on cherche une repartition pour l'employeur donne
                Iterator iterRep = repManager.iterator();
                while (iterRep.hasNext()) {
                    APRepartitionPaiements repartition = (APRepartitionPaiements) iterRep.next();
                    APSituationProfessionnelle sitPro = repartition.loadSituationProfessionnelle();

                    if ((sitPro != null) && sitPro.getIdEmployeur().equals(idEmployeur)) {

                        BigDecimal montantBrut = new BigDecimal(JANumberFormatter.deQuote(repartition.getMontantBrut()));
                        BigDecimal nbJours = new BigDecimal(
                                JANumberFormatter.deQuote(prestation.getNombreJoursSoldes()));

                        // on ajoute le montant journalier au montant total
                        // journalier
                        montantJournalierPrestationStdLAMat.add(new FWCurrency(montantBrut.divide(nbJours,
                                BigDecimal.ROUND_HALF_UP).toString()));
                    }
                }
            }

            // Bugzilla 4639
            // on arrondi le montant total au franc superieur
            // montantJournalierPrestationStdLAMat = new FWCurrency(JANumberFormatter.round(
            // montantJournalierPrestationStdLAMat.getBigDecimalValue(), 1, 2, JANumberFormatter.SUP)
            // .doubleValue());

        } catch (Exception e) {
            return new FWCurrency("0");
        }

        return montantJournalierPrestationStdLAMat;
    }
}
