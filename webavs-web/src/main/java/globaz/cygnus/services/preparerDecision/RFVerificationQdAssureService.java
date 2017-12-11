/*
 * Créé le 10 novembre 2010
 */
package globaz.cygnus.services.preparerDecision;

import globaz.cygnus.api.IRFTypesBeneficiairePc;
import globaz.cygnus.api.motifsRefus.IRFMotifsRefus;
import globaz.cygnus.db.qds.RFQdAssureJointDossierJointTiers;
import globaz.cygnus.db.qds.RFQdAssureJointDossierJointTiersManager;
import globaz.cygnus.services.RFRetrieveLimiteAnnuelleSousTypeDeSoinService;
import globaz.cygnus.utils.RFUtils;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

/**
 * Vérifie l'imputation sur la petite Qd (ou Qd assuré)
 * 
 * @author jje
 */
public class RFVerificationQdAssureService {

    public RFCalculMontantAPayerData calculMontantAPayer(String montantResiduel, BigDecimal chargeAutresQdsBigDec,
            String montantAPayer, String codeTypeDeSoin, String idQd, String idPot, String limiteAnnuellePot,
            String dateDebutPetiteQd, String dateFinPetiteQd, BigDecimal montantCorrection) {

        BigDecimal montantResiduelBigDec = new BigDecimal(montantResiduel).add(chargeAutresQdsBigDec.negate());

        BigDecimal futurMontantResiduel = montantResiduelBigDec.add(montantCorrection).add(
                new BigDecimal(montantAPayer.replace("'", "")).negate());

        RFCalculMontantAPayerData rfCalculMonAPay = new RFCalculMontantAPayerData();
        rfCalculMonAPay.setMontantResiduelInitial(montantResiduel);
        rfCalculMonAPay.setQdAssure(true);
        rfCalculMonAPay.setDateDebutPetiteQd(dateDebutPetiteQd);
        rfCalculMonAPay.setDateDeFinPetiteQd(dateFinPetiteQd);
        rfCalculMonAPay.setMontantAutresQdBigDec(chargeAutresQdsBigDec);

        if (futurMontantResiduel.floatValue() < 0) {

            String idMotifRefus = "";

            if (codeTypeDeSoin.equals(RFUtils.CODE_TYPE_DE_SOIN_FRQP_STR)) {
                idMotifRefus = IRFMotifsRefus.ID_FRQP_MAXIMUM_N_FRANC_PAR_ASSURE;
            } else if (codeTypeDeSoin.equals(RFUtils.CODE_TYPE_DE_SOIN_MAINTIEN_A_DOMICILE_STR)) {
                idMotifRefus = IRFMotifsRefus.ID_MENAGE_TIERS_MAXIMUM_N_FRANC;
            } else {
                idMotifRefus = IRFMotifsRefus.ID_MAXIMUM_N_FRANC_PAR_ANNEE_PETITE_QD;
            }

            if ((montantResiduelBigDec.compareTo(new BigDecimal(0)) == 0)
                    && (montantCorrection.compareTo(new BigDecimal(0)) == 0)) {
                // Refusé
                setRfCalculMontantAPayerData("0", montantAPayer, idQd, idPot, limiteAnnuellePot, rfCalculMonAPay,
                        idMotifRefus);
            } else {
                // Partiellement accepté
                setRfCalculMontantAPayerData(montantResiduelBigDec.add(montantCorrection).toString(),
                        futurMontantResiduel.abs().toString(), idQd, idPot, limiteAnnuellePot, rfCalculMonAPay,
                        idMotifRefus);
            }
        } else {
            // Accepté
            setRfCalculMontantAPayerData(montantAPayer, "", idQd, idPot, limiteAnnuellePot, rfCalculMonAPay, "");
        }

        return rfCalculMonAPay;

    }

    private BigDecimal chargeTotaleAutresQds(List<String[]> arrayListQdsMemeAnnee, BigDecimal limiteAnnuelleBigDec) {

        BigDecimal chargeRFMAutresQdBigDec = new BigDecimal("0");

        // Recherche de la charge totale des autres Qds
        for (String[] qdCourante : arrayListQdsMemeAnnee) {
            if (null != qdCourante) {
                if (limiteAnnuelleBigDec.compareTo(new BigDecimal(qdCourante[2].replace("'", ""))) == 0) {
                    chargeRFMAutresQdBigDec = chargeRFMAutresQdBigDec
                            .add(new BigDecimal(qdCourante[3].replace("'", "")).add(new BigDecimal(qdCourante[4]
                                    .replace("'", ""))));
                }
            }
        }

        return chargeRFMAutresQdBigDec;
    }

    /**
     * Recherche les petites Qds concernées par la demande et vérifie si la limite annuelle est respectée.
     * 
     * @param data
     * @param session
     * @param isProcessPreparerValiderDecision
     * @return RFCalculMontantAPayerData
     * @throws Exception
     */
    public RFCalculMontantAPayerData montantQdAssure(RFVerificationQdAssureData data, BSession session,
            boolean isProcessPreparerValiderDecision, BITransaction transaction, String csTypeBeneficiaire,
            String csGenrePcAccordee) throws Exception {

        RFCalculMontantAPayerData rfCalculMonAPay = null;

        if (!JadeStringUtil.isBlankOrZero(data.getIdQdPrincipale())) {
            JACalendar cal = new JACalendarGregorian();

            // Recherche d'une petite Qd concernant un sous type de soin
            JADate dateDebutDemandeJd = null;
            if (JadeStringUtil.isBlankOrZero(data.getDateDebutTraitement())) {
                dateDebutDemandeJd = new JADate(data.getDateFacture());
            } else {

                if (data.getDateDebutTraitement().length() == 7) {
                    data.setDateDebutTraitement("01." + data.getDateDebutTraitement());
                }
                dateDebutDemandeJd = new JADate(data.getDateDebutTraitement());
            }

            RFQdAssureJointDossierJointTiersManager rfQdAssureJointPotTypeDeSoinMgr = RFUtils
                    .getRFQdAssureJointDossierJointTiersManager(
                            session,
                            null,
                            data.getIdTiers(),
                            data.getCodeTypeDeSoin(),
                            data.getCodeSousTypeDeSoin(),
                            "",
                            PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(JadeStringUtil.isBlankOrZero(data
                                    .getDateDebutTraitement()) ? data.getDateFacture() : data.getDateDebutTraitement()),
                            "", "", "", true, "", "");

            Iterator<RFQdAssureJointDossierJointTiers> rfQdAssureJointPotTypeDeSoinItr = rfQdAssureJointPotTypeDeSoinMgr
                    .iterator();

            // String[idQd,idPot,limiteAnnuelle,dateDebut,dateFin]
            List<String[]> arrayListQdsMemeAnnee = new ArrayList<String[]>();

            BigDecimal limiteAnnuelleBigDec = new BigDecimal("0");
            String idQd = "";
            BigDecimal chargeRFMBigDec = new BigDecimal("0");
            BigDecimal chargeRFMAutresQdBigDec = new BigDecimal("0");
            String idQdAssure = "";
            boolean hasPetiteQdPlafonnee = false;
            boolean isPeriodeDejaTraite = false;
            while (rfQdAssureJointPotTypeDeSoinItr.hasNext()) {

                RFQdAssureJointDossierJointTiers rfQdAssureJointPotTypeDeSoin = rfQdAssureJointPotTypeDeSoinItr.next();

                if (null != rfQdAssureJointPotTypeDeSoin) {

                    JADate dateDebutPeriodeQdJd = new JADate(rfQdAssureJointPotTypeDeSoin.getDateDebut());
                    JADate dateFinPeriodeQdJd = null;

                    if (!JadeStringUtil.isBlankOrZero(rfQdAssureJointPotTypeDeSoin.getDateFin())) {
                        dateFinPeriodeQdJd = new JADate(rfQdAssureJointPotTypeDeSoin.getDateFin());
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
                                    "RFVerificationQdAssureService.montantQdAssure() : plusieurs petites Qds pour la même période (N°Qd: "
                                            + rfQdAssureJointPotTypeDeSoin.getIdQd() + ")");
                        }
                        isPeriodeDejaTraite = true;

                        if (rfQdAssureJointPotTypeDeSoin.getIsPlafonnee()) {

                            limiteAnnuelleBigDec = new BigDecimal(rfQdAssureJointPotTypeDeSoin.getLimiteAnnuelle()
                                    .replace("'", ""));
                            idQd = rfQdAssureJointPotTypeDeSoin.getIdQd();
                            chargeRFMBigDec = chargeRFMBigDec.add(new BigDecimal(rfQdAssureJointPotTypeDeSoin
                                    .getMontantChargeRfm().replace("'", "")));
                            idQdAssure = rfQdAssureJointPotTypeDeSoin.getIdQdAssure();

                            hasPetiteQdPlafonnee = true;

                        } else { // Accepté car QdAssuré non plafonnée
                            rfCalculMonAPay = new RFCalculMontantAPayerData();

                            rfCalculMonAPay.setMontantAccepte(data.getMontantAPayer());
                            rfCalculMonAPay.setIdQd(rfQdAssureJointPotTypeDeSoin.getIdQdAssure());
                            rfCalculMonAPay.setPlafondPotAssure("");
                            rfCalculMonAPay.setIdPotAssure("");
                            rfCalculMonAPay.setPlafonnee(false);
                            rfCalculMonAPay.setQdAssure(true);

                            break;

                        }

                    } else {

                        String soldeDeCharge = RFUtils.getSoldeDeCharge(rfQdAssureJointPotTypeDeSoin.getIdQdAssure(),
                                session);

                        arrayListQdsMemeAnnee
                                .add(new String[] { rfQdAssureJointPotTypeDeSoin.getIdQdAssure(),
                                        rfQdAssureJointPotTypeDeSoin.getIdPotSousTypeDeSoin(),
                                        rfQdAssureJointPotTypeDeSoin.getLimiteAnnuelle(),
                                        rfQdAssureJointPotTypeDeSoin.getMontantChargeRfm(),
                                        JadeStringUtil.isBlankOrZero(soldeDeCharge) ? "0" : soldeDeCharge,
                                        rfQdAssureJointPotTypeDeSoin.getDateDebut(),
                                        rfQdAssureJointPotTypeDeSoin.getDateFin() });
                    }

                } else {
                    throw new Exception(
                            "RFVerificationQdAssureService.montantQdAssure() : impossible de retrouver la petite Qd");
                }
            }
            
            if (hasPetiteQdPlafonnee) {

                chargeRFMAutresQdBigDec = chargeTotaleAutresQds(arrayListQdsMemeAnnee, limiteAnnuelleBigDec);

                rfCalculMonAPay = calculMontantAPayer(RFUtils.getMntResiduel(limiteAnnuelleBigDec.toString(),
                        RFUtils.getAugmentationQd(idQd, session), RFUtils.getSoldeDeCharge(idQd, session),
                        chargeRFMBigDec.toString()), chargeRFMAutresQdBigDec, data.getMontantAPayer(),
                        data.getCodeTypeDeSoin(), idQdAssure, "", "", "", "", new BigDecimal("0"));

            } else { // Recherche si le type de soin possède un plafond

                RFRetrieveLimiteAnnuelleSousTypeDeSoinService rfRetLimAnnSouTypDeSoiSer = new RFRetrieveLimiteAnnuelleSousTypeDeSoinService();

                // Si le sous-type de soin est lié à une seule personne et que le bénéficiare de la demande est un
                // enfant (voir grande Qd) on prend le pot de l'enfant vivant séparé
                String csTypeBeneficiairePetiteQd = csTypeBeneficiaire;
                if (!(RFUtils.isSousTypeDeSoinCodeConcernePlusieursPersonnes(data.getCodeTypeDeSoin(),
                        data.getCodeSousTypeDeSoin()))
                        && data.isBeneficiaireEnfant()) {
                    csTypeBeneficiairePetiteQd = IRFTypesBeneficiairePc.ENFANTS_VIVANT_SEPARES;
                }

                String[] resultat = rfRetLimAnnSouTypDeSoiSer.getLimiteAnnuelleTypeDeSoinIdTiers(session, data
                        .getCodeTypeDeSoin(), data.getCodeSousTypeDeSoin(), data.getIdTiers(),
                        !JadeStringUtil.isBlankOrZero(data.getDateDebutTraitement()) ? data.getDateDebutTraitement()
                                : data.getDateFacture(), (BTransaction) transaction, csTypeBeneficiairePetiteQd,
                        csGenrePcAccordee, null);

                if (!JadeStringUtil.isBlankOrZero(resultat[0])) {

                    limiteAnnuelleBigDec = new BigDecimal(resultat[0].replace("'", ""));

                    chargeRFMAutresQdBigDec = chargeTotaleAutresQds(arrayListQdsMemeAnnee, limiteAnnuelleBigDec);

                    String[] periodePetiteQd = retrievePeriodeNouvellePetiteQd(arrayListQdsMemeAnnee, data,
                            dateDebutDemandeJd, cal);

                    rfCalculMonAPay = calculMontantAPayer(
                            limiteAnnuelleBigDec.add(chargeRFMBigDec.negate()).toString(), chargeRFMAutresQdBigDec,
                            data.getMontantAPayer(), data.getCodeTypeDeSoin(), "", resultat[1], resultat[0],
                            periodePetiteQd[0], periodePetiteQd[1], new BigDecimal("0"));
                }

            }
        }

        return rfCalculMonAPay;

    }

    private String[] retrievePeriodeNouvellePetiteQd(List<String[]> arrayListQdsMemeAnnee,
            RFVerificationQdAssureData data, JADate dateDebutDemandeJd, JACalendar cal) throws Exception {

        // On définit la date de début et de fin de la nouvelle petite Qd selon les Qds existantes
        String anneeQd = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(JadeStringUtil.isBlankOrZero(data
                .getDateDebutTraitement()) ? data.getDateFacture() : data.getDateDebutTraitement());
        String dateDebutPetiteQd = "01.01." + anneeQd;
        String dateFinPetiteQd = "31.12." + anneeQd;

        if (arrayListQdsMemeAnnee.size() > 0) {

            DateFormat dateFormatDDsMMsYYYY = new SimpleDateFormat("dd.MM.yyyy");
            DateFormat dateFormatDDMMYYYY = new SimpleDateFormat("ddMMyyyy");
            boolean premiereIteration = true;
            boolean dateDebutTrouvee = false;
            boolean dateFintrouvee = false;
            JADate dateDebutRetenueJd = new JADate();
            JADate dateFinRetenueJd = new JADate();

            for (String[] qdCourante : arrayListQdsMemeAnnee) {
                if (null != qdCourante) {

                    JADate dateDebutPetiteQdJd = new JADate(qdCourante[5]);
                    JADate dateFinPetiteQdJd = new JADate(qdCourante[6]);

                    // Recherche de la date de début
                    if (cal.compare(dateDebutDemandeJd, dateFinPetiteQdJd) == JACalendar.COMPARE_FIRSTUPPER) {

                        if (premiereIteration) {
                            dateDebutRetenueJd = dateFinPetiteQdJd;
                            dateDebutTrouvee = true;
                        } else {
                            if (cal.compare(dateDebutRetenueJd, dateFinPetiteQdJd) == JACalendar.COMPARE_FIRSTLOWER) {
                                dateDebutRetenueJd = dateFinPetiteQdJd;
                                dateDebutTrouvee = true;
                            }
                        }
                    }

                    // Recherche de la date de fin
                    if (cal.compare(dateDebutDemandeJd, dateDebutPetiteQdJd) == JACalendar.COMPARE_FIRSTLOWER) {

                        if (premiereIteration) {
                            dateFinRetenueJd = dateDebutPetiteQdJd;
                            dateFintrouvee = false;
                        } else {
                            if (cal.compare(dateFinRetenueJd, dateDebutPetiteQdJd) == JACalendar.COMPARE_FIRSTUPPER) {
                                dateFinRetenueJd = dateDebutPetiteQdJd;
                                dateFintrouvee = false;
                            }
                        }
                    }

                    premiereIteration = false;
                }
            }

            // Maj de la date de début et de fin de la Qd à créer
            GregorianCalendar calendar = new GregorianCalendar();

            if (dateDebutTrouvee) {
                Date dateDebutRetenueD = dateFormatDDMMYYYY.parse(dateDebutRetenueJd.toString());
                calendar.setTime(dateDebutRetenueD);
                calendar.add(Calendar.DAY_OF_WEEK, +1);
                dateDebutPetiteQd = dateFormatDDsMMsYYYY.format(calendar.getTime());
            }

            if (dateFintrouvee) {
                Date dateFinRetenueD = dateFormatDDMMYYYY.parse(dateFinRetenueJd.toString());
                calendar.setTime(dateFinRetenueD);
                calendar.add(Calendar.DAY_OF_WEEK, -1);
                dateFinPetiteQd = dateFormatDDsMMsYYYY.format(calendar.getTime());
            }

            if (JadeStringUtil.isBlankOrZero(dateDebutPetiteQd) || JadeStringUtil.isBlankOrZero(dateFinPetiteQd)) {
                throw new Exception(
                        "RFVerificationQdAssureService.retrievePeriodeNouvellePetiteQd() : période nouvelle petite QD null");
            }

        }

        return new String[] { dateDebutPetiteQd, dateFinPetiteQd };

    }

    private void setRfCalculMontantAPayerData(String montantAccepte, String montantAPayer, String idQd, String idPot,
            String limiteAnnuellePot, RFCalculMontantAPayerData rfCalculMonAPay, String idMotifRefus) {

        rfCalculMonAPay.setMontantAccepte(montantAccepte);
        rfCalculMonAPay.setIdQd(idQd);

        if (!JadeStringUtil.isBlankOrZero(idPot)) {
            rfCalculMonAPay.setPlafondPotAssure(limiteAnnuellePot);
        }

        rfCalculMonAPay.setIdPotAssure(idPot);

        if (!JadeStringUtil.isBlankOrZero(idMotifRefus)) {
            rfCalculMonAPay.getIdStrMotifDeRefus().add(new String[] { idMotifRefus, montantAPayer });
        }
    }

}
