/*
 * Créé le 10 novembre 2010
 */
package globaz.cygnus.services.preparerDecision;

import globaz.cygnus.api.IRFTypesBeneficiairePc;
import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.conventions.IRFConventions;
import globaz.cygnus.api.motifsRefus.IRFMotifsRefus;
import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.conventions.RFConventionJointAssConFouTsJointConventionAssureJointFournisseurJointMontant;
import globaz.cygnus.db.conventions.RFConventionJointAssConFouTsJointConventionAssureJointFournisseurJointMontantManager;
import globaz.cygnus.db.conventions.RFConventionJointAssConFouTsJointFournisseurJointMontant;
import globaz.cygnus.db.conventions.RFConventionJointAssConFouTsJointFournisseurJointMontantManager;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.demandes.RFDemandeJointSousTypeDeSoinJointTypeDeSoinManager;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.utils.RFUtils;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 * @author jje
 * 
 *         Recherche une convention selon la date de début traitement ou la date de facture si la date de début de
 *         traitement est nulle.
 */
public class RFVerificationConventionService {

    private RFVerificationConventionData data = null;
    private RFCalculMontantAPayerData rfCalMonAPayDat = null;
    private BSession session = null;
    private Set<String> sousTypesDeSoinsConvention = null;
    private BITransaction transaction = null;

    public RFVerificationConventionService(RFVerificationConventionData cData, BSession cSession,
            boolean isProcessPreparerValiderDecision, BITransaction ctransaction) {

        data = cData;
        session = cSession;
        rfCalMonAPayDat = new RFCalculMontantAPayerData();
        transaction = ctransaction;

    }

    private void calculMontantAPayer(String plafond, String montantAPayer, BigDecimal montantDemandes,
            String idConvention, Boolean isMontantPlafonne) throws Exception {

        rfCalMonAPayDat = null;

        BigDecimal plafondBigDec = new BigDecimal(plafond);

        if (null != montantDemandes) {
            plafondBigDec = plafondBigDec.add(montantDemandes.negate());
            // ne devrait pas se produire sauf si le plafond d'une convention est modifié
            if (plafondBigDec.compareTo(new BigDecimal(0)) < 0) {
                throw new Exception(
                        "RFVerificationConventionService.calculMontantAPayer(): montant demandes conventionnées supérieur au plafond");
            }
        }

        if (!JadeStringUtil.isBlankOrZero(montantAPayer)) {

            if (isMontantPlafonne) {

                rfCalMonAPayDat = new RFCalculMontantAPayerData();

                BigDecimal futurMontantResiduel = plafondBigDec.add(new BigDecimal(montantAPayer.replace("'", ""))
                        .negate());

                if (futurMontantResiduel.floatValue() < 0) {
                    if (plafondBigDec.compareTo(new BigDecimal(0)) == 0) {

                        rfCalMonAPayDat.setIdConvention(idConvention);
                        rfCalMonAPayDat.getIdStrMotifDeRefus().add(
                                new String[] { IRFMotifsRefus.ID_MAXIMUM_N_FRANC_PAR_ANNEE_CONVENTION, montantAPayer });
                        rfCalMonAPayDat.setMontantAccepte("0");

                    } else {

                        rfCalMonAPayDat.setIdConvention(idConvention);
                        rfCalMonAPayDat.getIdStrMotifDeRefus().add(
                                new String[] { IRFMotifsRefus.ID_MAXIMUM_N_FRANC_PAR_ANNEE_CONVENTION,
                                        futurMontantResiduel.abs().toString() });
                        rfCalMonAPayDat.setMontantAccepte(plafond.replace("'", ""));

                    }
                } else {

                    rfCalMonAPayDat.setIdConvention(idConvention);
                    rfCalMonAPayDat.setMontantAccepte(montantAPayer.replace("'", ""));

                }
            }

        }

    }

    private BigDecimal calculMontantDemandesConvention(String csPeriodicite) throws Exception {

        JACalendar cal = new JACalendarGregorian();

        String dateDebutStr = "";
        String dateFinStr = "";

        if (csPeriodicite.equals(IRFConventions.MENSUELLE)) {
            JADate jaDateDebutTraitement = new JADate(data.getDateDebutTraitement());
            JADate jaDateFinMois = cal.addDays(cal.addMonths(new JADate("01." + jaDateDebutTraitement.getMonth() + "."
                    + jaDateDebutTraitement.getYear()), 1), -1);
            dateDebutStr = "01." + jaDateDebutTraitement.getMonth() + "." + jaDateDebutTraitement.getYear();
            dateFinStr = jaDateFinMois.getDay() + "." + jaDateFinMois.getMonth() + "." + jaDateFinMois.getYear();
        } else if (csPeriodicite.equals(IRFConventions.ANNUELLE)) {
            JADate jaDateDebutTraitement = new JADate(data.getDateDebutTraitement());

            dateDebutStr = "01.01." + jaDateDebutTraitement.getYear();
            dateFinStr = "31.12." + jaDateDebutTraitement.getYear();
        } else if (csPeriodicite.equals(IRFConventions.TRIMESTRIELLE)) {
            JADate jaDateDebutTraitement = new JADate(data.getDateDebutTraitement());
            JADate jaDateFinMois = cal.addDays(cal.addMonths(new JADate("01." + jaDateDebutTraitement.getMonth() + "."
                    + jaDateDebutTraitement.getYear()), 3), -1);
            dateDebutStr = "01." + jaDateDebutTraitement.getMonth() + "." + jaDateDebutTraitement.getYear();
            dateFinStr = jaDateFinMois.getDay() + "." + jaDateFinMois.getMonth() + "." + jaDateFinMois.getYear();
        }

        RFDemandeJointSousTypeDeSoinJointTypeDeSoinManager rfDemJointSouJointTypMgr = new RFDemandeJointSousTypeDeSoinJointTypeDeSoinManager();
        rfDemJointSouJointTypMgr.setSession(session);
        rfDemJointSouJointTypMgr.setForIdQdPrincipale(data.getIdQdPrincipale());
        rfDemJointSouJointTypMgr.setForDateDebutBetweenDateDebutDem(dateDebutStr);
        rfDemJointSouJointTypMgr.setForDateFinBetweenDateDebutDem(dateFinStr);
        rfDemJointSouJointTypMgr.setForCodeTypeDeSoin(data.getCodeTypeDeSoin());
        rfDemJointSouJointTypMgr.setForCodeSousTypeDeSoin(data.getCodeSousTypeDeSoin());
        rfDemJointSouJointTypMgr.setForIdFournisseur(data.getIdFournisseur());
        if ((null != data.getIdDemandeToIgnore()) && !data.getIdDemandeToIgnore().equals("null")) {
            rfDemJointSouJointTypMgr.setForIdDemandeToIgnore(data.getIdDemandeToIgnore());
        }
        rfDemJointSouJointTypMgr.changeManagerSize(0);
        rfDemJointSouJointTypMgr.find();

        Iterator<RFDemande> rfDemandeItr = rfDemJointSouJointTypMgr.iterator();

        BigDecimal total = new BigDecimal(0);

        while (rfDemandeItr.hasNext()) {

            RFDemande rfDemande = rfDemandeItr.next();

            if (null != rfDemande) {
                total = total.add(new BigDecimal(rfDemande.getMontantAPayer()));
            }
        }

        return total;

    }

    /**
     * 
     * @param isProcessPreparerValiderDecision
     * @return String[idMotifDeRefus,montantAccepté,idConvention]
     * @throws Exception
     */
    public RFCalculMontantAPayerData convention() throws Exception {

        initSousTypesDeSoinsConvention();

        // Recherche d'une convention concernant un assuré
        rechercheConventionAssure();

        return rfCalMonAPayDat;

    }

    private String getPlafondConvention(String csDegreApi, String montantApiFaible, String montantApiMoyenne,
            String montantApiGrave, String montantApiDefaut) {

        if (!JadeStringUtil.isBlankOrZero(csDegreApi)) {

            if (!JadeStringUtil.isBlankOrZero(montantApiDefaut)) {
                return montantApiDefaut;
            } else {

                if (IRFQd.CS_DEGRE_API_FAIBLE.equals(csDegreApi)) {
                    return montantApiFaible;
                } else if (IRFQd.CS_DEGRE_API_MOYEN.equals(csDegreApi)) {
                    return montantApiMoyenne;
                } else if (IRFQd.CS_DEGRE_API_GRAVE.equals(csDegreApi)) {
                    return montantApiGrave;
                } else {
                    return montantApiDefaut;
                }
            }

        } else {
            return montantApiDefaut;
        }
    }

    private void initSousTypesDeSoinsConvention() throws Exception {

        if (sousTypesDeSoinsConvention == null) {

            // if (!RFApplication.PROPERTY_NUMERO_CAISSE_CCJU.equals(RFVerificationConventionService.session
            // .getApplication().getProperty("noCaisse"))) {

            sousTypesDeSoinsConvention = new HashSet<String>();

            // Selon properties caisses
            if (RFPropertiesUtils.verificationConventionAjoutBarriere()) {
                sousTypesDeSoinsConvention.add(IRFTypesDeSoins.st_9_BARRIERES);
            }
            // Selon properties caisses
            if (RFPropertiesUtils.verificationConventionAjoutLitElectrique()) {
                sousTypesDeSoinsConvention.add(IRFTypesDeSoins.st_9_LIT_ELECTRIQUE);
            }
            // Selon properties caisses
            if (RFPropertiesUtils.verificationConventionAjoutPotence()) {
                sousTypesDeSoinsConvention.add(IRFTypesDeSoins.st_9_POTENCE);
            }
            // Selon properties caisses
            if (RFPropertiesUtils.verificationConventionAjoutAccompagnementSocial()) {
                sousTypesDeSoinsConvention.add(IRFTypesDeSoins.st_14_ACCOMPAGNEMENT_SOCIAL);
            }
            // Selon properties caisses
            if (RFPropertiesUtils.verificationConventionAjoutAnimation()) {
                sousTypesDeSoinsConvention.add(IRFTypesDeSoins.st_14_ANIMATION);
            }
            // Selon properties caisses
            if (RFPropertiesUtils.verificationConventionAjoutencadrementSecuritaire()) {
                sousTypesDeSoinsConvention.add(IRFTypesDeSoins.st_14_ENCADREMENT_SECURITAIRE);
            }
            // Selon properties caisses
            if (RFPropertiesUtils.verificationConventionAjoutEncadrementSocioEducatif()) {
                sousTypesDeSoinsConvention.add(IRFTypesDeSoins.st_14_ENCADREMENT_SOCIO_EDUCATIF);
            }

            // } else {
            // RFVerificationConventionService.sousTypesDeSoinsConvention = new HashSet<String>();
            // }
        }
    }

    private void rechercheConvention() throws Exception {

        String csTypeBeneficiaire = data.getCsTypeBeneficiaire();

        if (!JadeStringUtil.isBlankOrZero(csTypeBeneficiaire)) {

            RFConventionJointAssConFouTsJointFournisseurJointMontantManager rfConJointAssConFouTsJointFouJointMntMgr = new RFConventionJointAssConFouTsJointFournisseurJointMontantManager();

            rfConJointAssConFouTsJointFouJointMntMgr.setSession(session);
            rfConJointAssConFouTsJointFouJointMntMgr.setForIdFournisseur(data.getIdFournisseur());
            rfConJointAssConFouTsJointFouJointMntMgr.setForCodeTypeDeSoin(data.getCodeTypeDeSoin());
            rfConJointAssConFouTsJointFouJointMntMgr.setForCodeSousTypeDeSoin(data.getCodeSousTypeDeSoin());

            if (JadeStringUtil.isBlankOrZero(data.getDateDebutTraitement())) {
                rfConJointAssConFouTsJointFouJointMntMgr.setForDateMontant(data.getDateFacture());
            } else {
                rfConJointAssConFouTsJointFouJointMntMgr.setForDateMontant(data.getDateDebutTraitement());
            }

            rfConJointAssConFouTsJointFouJointMntMgr.setForCsTypeBeneficiaire(csTypeBeneficiaire);
            rfConJointAssConFouTsJointFouJointMntMgr.setForCsTypeBeneficiairePourTous(true);

            rfConJointAssConFouTsJointFouJointMntMgr.setForCsTypePc(data.getCsTypePc());

            rfConJointAssConFouTsJointFouJointMntMgr.setForCsGenrePc(data.getCsGenrePc());
            rfConJointAssConFouTsJointFouJointMntMgr.changeManagerSize(0);
            rfConJointAssConFouTsJointFouJointMntMgr.find();

            RFConventionJointAssConFouTsJointFournisseurJointMontant rfConJointAssConFouTsJointFouJointMnt = null;

            // Pour un fournisseur, un sous-type et un type bénéficiaire le mgr
            // doit retourner aux max. 2 tuples, concernant
            // le csTypeBeneficiaire du tiers et le type bénéficiaire
            // "pour tous" si il existe
            if ((rfConJointAssConFouTsJointFouJointMntMgr.size() == 1)
                    || (rfConJointAssConFouTsJointFouJointMntMgr.size() == 2)) {

                Iterator<RFConventionJointAssConFouTsJointFournisseurJointMontant> rfConJointAssConFouTsJointFouJointMntItr = rfConJointAssConFouTsJointFouJointMntMgr
                        .iterator();

                while (rfConJointAssConFouTsJointFouJointMntItr.hasNext()) {
                    rfConJointAssConFouTsJointFouJointMnt = rfConJointAssConFouTsJointFouJointMntItr.next();

                    if ((null != rfConJointAssConFouTsJointFouJointMnt)
                            && IRFTypesBeneficiairePc.POUR_TOUS.equals(rfConJointAssConFouTsJointFouJointMnt
                                    .getCsTypeBeneficiaire())) {
                        break;
                    }
                }
            }

            if (null != rfConJointAssConFouTsJointFouJointMnt) {
                // Gestion de la périodicité du montant (recherche les demandes
                // de la période -> pas de montants)
                BigDecimal calculMontantDemandesConvention = null;

                if (IRFConventions.MENSUELLE.equals(rfConJointAssConFouTsJointFouJointMnt.getPeriodicite())) {
                    calculMontantDemandesConvention = calculMontantDemandesConvention(IRFConventions.MENSUELLE);
                } else if (IRFConventions.ANNUELLE.equals(rfConJointAssConFouTsJointFouJointMnt.getPeriodicite())) {
                    calculMontantDemandesConvention = calculMontantDemandesConvention(IRFConventions.ANNUELLE);
                }

                if (null != calculMontantDemandesConvention) {
                    calculMontantAPayer(
                            getPlafondConvention(data.getCsDegreApi(),
                                    rfConJointAssConFouTsJointFouJointMnt.getMntMaxAvecApiFaible(),
                                    rfConJointAssConFouTsJointFouJointMnt.getMntMaxAvecApiMoyen(),
                                    rfConJointAssConFouTsJointFouJointMnt.getMntMaxAvecApiGrave(),
                                    rfConJointAssConFouTsJointFouJointMnt.getMntMaxDefaut()), data.getMontantAPayer(),
                            calculMontantDemandesConvention, rfConJointAssConFouTsJointFouJointMnt.getIdConvention(),
                            rfConJointAssConFouTsJointFouJointMnt.getMntPlafonne());
                } else {
                    throw new Exception(
                            "RFVerificationConventionService.rechercheConvention(): Périodicité non trouvée");
                }
            } else {

                if (sousTypesDeSoinsConvention.contains(RFUtils.getIdSousTypeDeSoin(data.getCodeTypeDeSoin(),
                        data.getCodeSousTypeDeSoin(), session))) {

                    rfCalMonAPayDat = new RFCalculMontantAPayerData();
                    rfCalMonAPayDat.getIdStrMotifDeRefus().add(
                            new String[] { IRFMotifsRefus.ID_CONVENTION_NON_TROUVEE, "" });
                    rfCalMonAPayDat.setMontantAccepte("0");
                    rfCalMonAPayDat.setConventionNonTrouvee(true);

                }

                rfCalMonAPayDat = null;
            }
        } else {
            rfCalMonAPayDat = null;
        }
    }

    private void rechercheConventionAssure() throws Exception {

        // Recherche d'une convention comportant un montant
        // pour un sous type de soin, un fournisseur et un assuré
        RFConventionJointAssConFouTsJointConventionAssureJointFournisseurJointMontantManager rfConJointAssConFouTsJointConvAssJointFouJointMntMgr = new RFConventionJointAssConFouTsJointConventionAssureJointFournisseurJointMontantManager();

        rfConJointAssConFouTsJointConvAssJointFouJointMntMgr.setSession(session);
        rfConJointAssConFouTsJointConvAssJointFouJointMntMgr.setForIdFournisseur(data.getIdFournisseur());
        rfConJointAssConFouTsJointConvAssJointFouJointMntMgr.setForCodeTypeDeSoin(data.getCodeTypeDeSoin());
        rfConJointAssConFouTsJointConvAssJointFouJointMntMgr.setForCodeSousTypeDeSoin(data.getCodeSousTypeDeSoin());
        rfConJointAssConFouTsJointConvAssJointFouJointMntMgr.setForIdTiers(data.getIdTiers());

        if (JadeStringUtil.isBlankOrZero(data.getDateDebutTraitement())) {
            rfConJointAssConFouTsJointConvAssJointFouJointMntMgr.setForDateConventionAssure(data.getDateFacture());
        } else {
            rfConJointAssConFouTsJointConvAssJointFouJointMntMgr.setForDateConventionAssure(data
                    .getDateDebutTraitement());
        }

        rfConJointAssConFouTsJointConvAssJointFouJointMntMgr.setForCsTypePc(data.getCsTypePc());

        rfConJointAssConFouTsJointConvAssJointFouJointMntMgr.setForCsGenrePc(data.getCsGenrePc());
        rfConJointAssConFouTsJointConvAssJointFouJointMntMgr.changeManagerSize(0);
        rfConJointAssConFouTsJointConvAssJointFouJointMntMgr.find(transaction);

        if (rfConJointAssConFouTsJointConvAssJointFouJointMntMgr.size() == 1) {
            RFConventionJointAssConFouTsJointConventionAssureJointFournisseurJointMontant rfConJointAssConFouTsJointConvAssJointFouJointMnt = (RFConventionJointAssConFouTsJointConventionAssureJointFournisseurJointMontant) rfConJointAssConFouTsJointConvAssJointFouJointMntMgr
                    .getFirstEntity();

            calculMontantAPayer(rfConJointAssConFouTsJointConvAssJointFouJointMnt.getMntAssure(),
                    data.getMontantAPayer(), null,
                    rfConJointAssConFouTsJointConvAssJointFouJointMnt.getIdConventionAssure(),
                    rfConJointAssConFouTsJointConvAssJointFouJointMnt.getMntPlafonne());

        }// Recherche une convention générale si pas de convention assuré
        else {
            if (rfConJointAssConFouTsJointConvAssJointFouJointMntMgr.size() == 0) {
                rechercheConvention();
            } else {
                throw new Exception(
                        "RFVerificationConventionService.rechercheConventionAssure(): il existe plusieurs conventions pour un sous-type/fournisseur/assuré");
            }
        }
    }

}
