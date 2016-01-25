/*
 * Créé le 10 novembre 2010
 */
package globaz.cygnus.services.preparerDecision;

import globaz.cygnus.api.motifsRefus.IRFMotifsRefus;
import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.qds.RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande;
import globaz.cygnus.db.qds.RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager;
import globaz.cygnus.utils.RFUtils;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author jje
 * 
 *         Recherche la Qd principale selon la date de début traitement ou la date de facture si la date de début de
 *         traitement est nulle.
 */
public class RFVerificationQdPrincipaleService {

    public RFCalculMontantAPayerData calculMontantAPayer(String mntResiduel, String montantAPayer,
            String soldeExcedentDeRevenu, String idQdPrincipale, boolean isQdPlafonnee, String csTypeBeneficiaire,
            String csTypePcAccordee, String csGenrePcAccordee, BigDecimal montantAutresQdBigDec,
            BigDecimal montantCorrectionBigDec, boolean isRi, boolean isLaprams, String codeTypeDeSoin,
            String codeSousTypeDeSoin, String remboursementConjoint, String remboursementRequerent, String csDegreApi) {

        RFCalculMontantAPayerData rfCalculMonAPay = new RFCalculMontantAPayerData();
        mntResiduel = mntResiduel.replace("'", "");
        montantAPayer = montantAPayer.replace("'", "");
        soldeExcedentDeRevenu = soldeExcedentDeRevenu.replace("'", "");

        rfCalculMonAPay.setCsTypeBeneficiaire(csTypeBeneficiaire);
        rfCalculMonAPay.setCsTypePcAccordee(csTypePcAccordee);
        rfCalculMonAPay.setIdQd(idQdPrincipale);
        rfCalculMonAPay.setQdPrincipale(true);
        rfCalculMonAPay.setMontantResiduelInitial(mntResiduel);
        rfCalculMonAPay.setSoldeExcedentInitial(soldeExcedentDeRevenu);
        rfCalculMonAPay.setCsGenrePcAccordee(csGenrePcAccordee);
        rfCalculMonAPay.setMontantAutresQdBigDec(montantAutresQdBigDec);
        rfCalculMonAPay.setRi(isRi);
        rfCalculMonAPay.setLaprams(isLaprams);
        rfCalculMonAPay.setPlafonnee(isQdPlafonnee);
        rfCalculMonAPay.setRemboursementConjoint(remboursementConjoint);
        rfCalculMonAPay.setRemboursementRequerant(remboursementRequerent);
        rfCalculMonAPay.setCsDegreApi(csDegreApi);

        BigDecimal montantAPayerBigDec = new BigDecimal(montantAPayer);

        // Si le sous type de la demande ne s'impute pas sur la grande Qd, on n'ajoute aucun motif de refus
        if (!RFUtils.isSousTypeDeSoinNonImputeSurGrandeQd(codeTypeDeSoin, codeSousTypeDeSoin)) {

            if (!JadeStringUtil.isBlankOrZero(montantAPayer)) {
                // Gestion du solde excédent de recette
                if (!JadeStringUtil.isBlankOrZero(soldeExcedentDeRevenu)) {

                    rfCalculMonAPay.setHasSoldeExcedent(true);

                    BigDecimal montantAPayerMoinsSoldeExcedentDeRevenuBigDec = montantAPayerBigDec.add(new BigDecimal(
                            soldeExcedentDeRevenu).negate());

                    if (montantAPayerMoinsSoldeExcedentDeRevenuBigDec.floatValue() >= 0) {

                        rfCalculMonAPay.getIdStrMotifDeRefus().add(
                                new String[] { IRFMotifsRefus.ID_SOLDE_EXECEDENT_DE_REVENU, soldeExcedentDeRevenu });

                    } else {

                        rfCalculMonAPay.getIdStrMotifDeRefus().add(
                                new String[] { IRFMotifsRefus.ID_SOLDE_EXECEDENT_DE_REVENU, montantAPayer });
                    }

                }

                if (isQdPlafonnee) {
                    rfCalculMonAPay = setRFCalculMontantAPayerData(rfCalculMonAPay,
                            new BigDecimal(mntResiduel).add(montantCorrectionBigDec)
                                    .add(montantAutresQdBigDec.negate()).add(montantAPayerBigDec.negate()),
                            montantAPayerBigDec,
                            new BigDecimal(mntResiduel).add(montantCorrectionBigDec)
                                    .add(montantAutresQdBigDec.negate()), true);
                } else {
                    rfCalculMonAPay.setMontantAccepte(montantAPayer);
                }

            }
        } else {
            rfCalculMonAPay.setMontantAccepte(montantAPayer);
        }

        return rfCalculMonAPay;

    }

    /**
     * @param data
     * @param session
     * @param isProcessPreparerValiderDecision
     * @return RFCalculMontantAPayerData
     * @throws Exception
     */
    public RFCalculMontantAPayerData montantQdPrincipale(RFVerificationQdPrincipaleData data, BSession session,
            boolean isProcessPreparerValiderDecision, BITransaction transaction,
            Map<String, RFImputationQdsData> qdsAimputerMap) throws Exception {

        RFCalculMontantAPayerData rfCalculMonAPay = null;

        RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager rfQdJointPerValJointDosJointTieJointDemMgr = new RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager();
        rfQdJointPerValJointDosJointTieJointDemMgr.setSession(session);
        rfQdJointPerValJointDosJointTieJointDemMgr.setForCsGenreQd(IRFQd.CS_GRANDE_QD);
        // rfQdJointPerValJointDosJointTieJointDemMgr.setForIdDossier(viewBean.getIdDossier());
        // Impossible car le dossier n'existe pas forcément
        rfQdJointPerValJointDosJointTieJointDemMgr.setForIdTiers(data.getIdTiers());

        JADate dateDebutDemandeJd = null;
        if (JadeStringUtil.isBlankOrZero(data.getDateDebutTraitement())) {

            rfQdJointPerValJointDosJointTieJointDemMgr.setForAnneeQd(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(data
                    .getDateFacture()));
            dateDebutDemandeJd = new JADate(data.getDateFacture());
        } else {

            if (data.getDateDebutTraitement().length() == 7) {
                data.setDateDebutTraitement("01." + data.getDateDebutTraitement());
            }

            rfQdJointPerValJointDosJointTieJointDemMgr.setForAnneeQd(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(data
                    .getDateDebutTraitement()));
            dateDebutDemandeJd = new JADate(data.getDateDebutTraitement());
        }
        rfQdJointPerValJointDosJointTieJointDemMgr.changeManagerSize(0);
        rfQdJointPerValJointDosJointTieJointDemMgr.find(transaction);

        JACalendar cal = new JACalendarGregorian();
        BigDecimal montantResiduelBigDec = new BigDecimal(0);
        String idQdPrincipale = "";
        String soldeExcedentDeRevenu = "";
        boolean isQdPlafonee = false;
        boolean isRi = false;
        boolean isLaprams = false;
        String csTypeBeneficiaire = "";
        String csTypePCAccordee = "";
        String csDegreApi = "";
        String csGenrePCAccordee = "";
        String idQdIntermediaire = "";
        String remboursementConjoint = "";
        String remboursementRequerant = "";
        BigDecimal montantAutresQdBigDec = new BigDecimal(0);
        Set<String[]> qdsMemeAnnee = new HashSet<String[]>();

        Iterator<RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande> rfQdJointPerValJointDosJointTieJointDemItr = rfQdJointPerValJointDosJointTieJointDemMgr
                .iterator();
        if (rfQdJointPerValJointDosJointTieJointDemMgr.size() > 0) {
            boolean isPeriodeDejaTraite = false;
            while (rfQdJointPerValJointDosJointTieJointDemItr.hasNext()) {

                RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande rfQdJointPerValJointDosJointTieJointDem = rfQdJointPerValJointDosJointTieJointDemItr
                        .next();

                if (rfQdJointPerValJointDosJointTieJointDem != null) {

                    JADate dateDebutPeriodeQdJd = new JADate(
                            rfQdJointPerValJointDosJointTieJointDem.getDateDebutPeriode());
                    JADate dateFinPeriodeQdJd = null;
                    if (!JadeStringUtil.isBlankOrZero(rfQdJointPerValJointDosJointTieJointDem.getDateFinPeriode())) {
                        dateFinPeriodeQdJd = new JADate(rfQdJointPerValJointDosJointTieJointDem.getDateFinPeriode());
                    } else {
                        dateFinPeriodeQdJd = new JADate("31.12.9999");
                    }

                    // Contrôle si la Qd concerne la période de la demande ou la même année
                    if (((cal.compare(dateDebutPeriodeQdJd, dateDebutDemandeJd) == JACalendar.COMPARE_EQUALS) || (cal
                            .compare(dateDebutPeriodeQdJd, dateDebutDemandeJd) == JACalendar.COMPARE_FIRSTLOWER))
                            && ((cal.compare(dateFinPeriodeQdJd, dateDebutDemandeJd) == JACalendar.COMPARE_EQUALS) || (cal
                                    .compare(dateFinPeriodeQdJd, dateDebutDemandeJd) == JACalendar.COMPARE_FIRSTUPPER))) {

                        if (isPeriodeDejaTraite) {
                            throw new Exception(
                                    "RFVerificationQdPrincipaleService.montantQdPrincipale(): Plusieurs Qds pour la même période (N°Qd "
                                            + rfQdJointPerValJointDosJointTieJointDem.getIdQdPrincipale() + ")");
                        }
                        isPeriodeDejaTraite = true;

                        if (rfQdJointPerValJointDosJointTieJointDem.getIsComprisDansCalcul()) {

                            soldeExcedentDeRevenu = RFUtils.getSoldeExcedentDeRevenu(
                                    rfQdJointPerValJointDosJointTieJointDem.getIdQd(), session);
                            csTypeBeneficiaire = rfQdJointPerValJointDosJointTieJointDem.getCsTypeBeneficiaire();
                            csTypePCAccordee = rfQdJointPerValJointDosJointTieJointDem.getCsTypePCAccordee();
                            csGenrePCAccordee = rfQdJointPerValJointDosJointTieJointDem.getCsGenrePCAccordee();
                            csDegreApi = rfQdJointPerValJointDosJointTieJointDem.getCsDegreApi();
                            idQdPrincipale = rfQdJointPerValJointDosJointTieJointDem.getIdQdPrincipale();
                            isRi = rfQdJointPerValJointDosJointTieJointDem.getIsRI();
                            isLaprams = rfQdJointPerValJointDosJointTieJointDem.getIsLAPRAMS();
                            remboursementConjoint = rfQdJointPerValJointDosJointTieJointDem.getRemboursementConjoint();
                            remboursementRequerant = rfQdJointPerValJointDosJointTieJointDem
                                    .getRemboursementRequerant();

                            if (rfQdJointPerValJointDosJointTieJointDem.getIsPlafonnee()) {
                                isQdPlafonee = true;
                            }

                            if (isQdPlafonee) {
                                montantResiduelBigDec = new BigDecimal(RFUtils.getMntResiduel(
                                        rfQdJointPerValJointDosJointTieJointDem.getLimiteAnnuelle(), RFUtils
                                                .getAugmentationQd(rfQdJointPerValJointDosJointTieJointDem.getIdQd(),
                                                        session), RFUtils.getSoldeDeCharge(
                                                rfQdJointPerValJointDosJointTieJointDem.getIdQd(), session),
                                        rfQdJointPerValJointDosJointTieJointDem.getMontantChargeRfm()));
                            } else {
                                montantResiduelBigDec = new BigDecimal(0);
                            }

                        } else {
                            rfCalculMonAPay = new RFCalculMontantAPayerData();
                            rfCalculMonAPay.setPlafonnee(isQdPlafonee);
                            return setRFCalculMontantAPayerData(rfCalculMonAPay, null, new BigDecimal(data
                                    .getMontantAPayer().replace("'", "")), null, false);
                        }

                    } else if (!idQdIntermediaire.equals(rfQdJointPerValJointDosJointTieJointDem.getIdQdPrincipale())
                            && rfQdJointPerValJointDosJointTieJointDem.getIsComprisDansCalcul().booleanValue()) {

                        RFImputationQdsData rfImpQdData = null;

                        if (null != qdsAimputerMap) {
                            rfImpQdData = qdsAimputerMap.get(rfQdJointPerValJointDosJointTieJointDem
                                    .getIdQdPrincipale());
                        }

                        if (null == rfImpQdData) {

                            qdsMemeAnnee.add(new String[] {
                                    rfQdJointPerValJointDosJointTieJointDem.getIdQdPrincipale(),
                                    rfQdJointPerValJointDosJointTieJointDem.getCsTypeBeneficiaire(),
                                    rfQdJointPerValJointDosJointTieJointDem.getCsTypePCAccordee(),
                                    rfQdJointPerValJointDosJointTieJointDem.getMontantChargeRfm() });

                        } else {

                            qdsMemeAnnee.add(new String[] { rfImpQdData.getIdQd(), rfImpQdData.getCsTypeBeneficiaire(),
                                    rfImpQdData.getCsTypePcAccordee(), rfImpQdData.getChargeRfm().toString() });
                        }

                    }

                    idQdIntermediaire = rfQdJointPerValJointDosJointTieJointDem.getIdQdPrincipale();

                } else {
                    if (isProcessPreparerValiderDecision) {
                        throw new Exception("RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande null");
                    }
                }
            }

            if (!JadeStringUtil.isBlankOrZero(idQdPrincipale)) {

                for (String[] infosQd : qdsMemeAnnee) {
                    if (infosQd[1].equals(csTypeBeneficiaire) && infosQd[2].equals(csTypePCAccordee)
                            && !infosQd[0].equals(idQdPrincipale)) {
                        if (!JadeStringUtil.isBlankOrZero(infosQd[3])) {
                            montantAutresQdBigDec = montantAutresQdBigDec.add(new BigDecimal(infosQd[3]));
                        }
                    }
                }

                rfCalculMonAPay = calculMontantAPayer(montantResiduelBigDec.toString(), data.getMontantAPayer(),
                        soldeExcedentDeRevenu, idQdPrincipale, isQdPlafonee, csTypeBeneficiaire, csTypePCAccordee,
                        csGenrePCAccordee, montantAutresQdBigDec, new BigDecimal("0"), isRi, isLaprams,
                        data.getCodeTypeDeSoin(), data.getCodeSousTypeDeSoin(), remboursementConjoint,
                        remboursementRequerant, csDegreApi);
            }

        }

        return rfCalculMonAPay;

    }

    private RFCalculMontantAPayerData setRFCalculMontantAPayerData(RFCalculMontantAPayerData rfCalculMonAPay,
            BigDecimal futurMontantResiduelBigDec, BigDecimal montantAPayer, BigDecimal montantResiduelInitial,
            boolean isComprisDansCalcul) {

        if (isComprisDansCalcul) {
            if (futurMontantResiduelBigDec.floatValue() < 0) {

                rfCalculMonAPay.getIdStrMotifDeRefus().add(
                        new String[] { IRFMotifsRefus.ID_MAXIMUM_N_FRANC_PAR_ANNEE,
                                futurMontantResiduelBigDec.abs().toString() });

                if (montantResiduelInitial.floatValue() > 0) {
                    rfCalculMonAPay.setMontantAccepte(montantResiduelInitial.toString());
                } else {
                    rfCalculMonAPay.setMontantAccepte("0.00");
                }

            } else {

                rfCalculMonAPay.setMontantAccepte(montantAPayer.toString());
                // rfCalculMonAPay.montantResiduel = futurMontantResiduelBigDec.toString();
            }
        } else {
            rfCalculMonAPay.setMontantAccepte("0.00");
            rfCalculMonAPay.setIdQd("");
            rfCalculMonAPay.getIdStrMotifDeRefus().add(
                    new String[] { IRFMotifsRefus.ID_ENFANT_EXCLUS_PC, montantAPayer.toString() });

            return rfCalculMonAPay;
        }

        return rfCalculMonAPay;
    }
}
