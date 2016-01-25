package ch.globaz.perseus.businessimpl.checkers.rentepont;

import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.perseus.utils.PFUserHelper;
import globaz.prestation.tools.PRDateFormater;
import java.util.Calendar;
import ch.globaz.perseus.business.constantes.CSEtatRentePont;
import ch.globaz.perseus.business.constantes.CSVariableMetier;
import ch.globaz.perseus.business.exceptions.models.rentepont.RentePontException;
import ch.globaz.perseus.business.exceptions.models.variablemetier.VariableMetierException;
import ch.globaz.perseus.business.models.qd.SimpleFacture;
import ch.globaz.perseus.business.models.rentepont.FactureRentePont;
import ch.globaz.perseus.business.models.rentepont.RentePontSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;

/**
 * @author jsi
 * 
 */
public class FactureRentePontChecker extends PerseusAbstractChecker {

    /**
     * @param factureRentePont
     * @throws Exception
     */
    public static void checkForCreate(FactureRentePont factureRentePont) throws Exception {
        // FactureRentePontChecker.checkQD(factureRentePont);
        FactureRentePontChecker.checkDemandeAndDates(factureRentePont);
        FactureRentePontChecker.checkAdressePaiement(factureRentePont.getSimpleFactureRentePont()
                .getIdTiersAdressePaiement(), factureRentePont.getSimpleFactureRentePont()
                .getIdApplicationAdressePaiement());

        if (!JadeStringUtil.isEmpty(factureRentePont.getSimpleFactureRentePont().getNumRefFacture())) {
            FactureRentePontChecker.checkNumRefFacture(factureRentePont);
        }
    }

    private static void checkDemandeAndDates(FactureRentePont factureRentePont) throws Exception {

        String dateFacture = "";
        String dateReception = "";

        if (!JadeStringUtil.isEmpty(factureRentePont.getSimpleFactureRentePont().getDateDebutTraitement())
                && !JadeDateUtil.isGlobazDate(factureRentePont.getSimpleFactureRentePont().getDateDebutTraitement())) {

            JadeThread.logError(FactureRentePontChecker.class.getName(), "perseus.facture.date.debut.traitement");
        }

        if (!JadeStringUtil.isEmpty(factureRentePont.getSimpleFactureRentePont().getDateFinTraitement())
                && !JadeDateUtil.isGlobazDate(factureRentePont.getSimpleFactureRentePont().getDateFinTraitement())) {

            JadeThread.logError(FactureRentePontChecker.class.getName(), "perseus.facture.date.fin.traitement");
        }

        if (!JadeStringUtil.isEmpty(factureRentePont.getSimpleFactureRentePont().getDateReception())
                && !JadeDateUtil.isGlobazDate(factureRentePont.getSimpleFactureRentePont().getDateReception())) {

            JadeThread.logError(FactureRentePontChecker.class.getName(), "perseus.facture.date.reception");

        } else {
            dateReception = factureRentePont.getSimpleFactureRentePont().getDateReception();
        }

        if (!JadeStringUtil.isEmpty(factureRentePont.getSimpleFactureRentePont().getDateFacture())
                && !JadeDateUtil.isGlobazDate(factureRentePont.getSimpleFactureRentePont().getDateFacture())) {

            JadeThread.logError(FactureRentePontChecker.class.getName(), "perseus.facture.date.facture");

        } else {
            dateFacture = factureRentePont.getSimpleFactureRentePont().getDateFacture();
        }

        if (!JadeStringUtil.isEmpty(dateReception) && !JadeStringUtil.isEmpty(dateFacture)) {
            checkDemande(dateReception, dateFacture, factureRentePont.getSimpleFactureRentePont()
                    .getDatePriseEnCharge(), factureRentePont.getQdRentePont().getDossier().getId(), factureRentePont
                    .getQdRentePont().getSimpleQDRentePont().getAnnee());

            // Voir que la facture a bien été envoyé dans les 15 mois
            // Définir le nombre de mois que l'assuré a pour envoyer sa facture
            if (JadeDateUtil.isGlobazDate(dateFacture) && JadeDateUtil.isGlobazDate(dateReception)) {
                checkDelai(dateFacture, dateReception);
            }
        }
    }

    /**
     * Check si une demande de rente-pont valide existe pour la date de facture
     * 
     * @param dateReception
     * @param dateFacture
     * @param datePriseEnCharge
     * @param idDossier
     * @param anneeQD
     * @throws RentePontException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkDemande(String dateReception, String dateFacture, String datePriseEnCharge,
            String idDossier, String anneeQD) throws RentePontException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        String dateAVerifier = dateFacture;

        // Si la date de prise en charge est renseigné, c est avec cette date que nous allons voir si une demande
        // PCF etait en cours au moment de la prise en charge.
        if (!JadeStringUtil.isEmpty(datePriseEnCharge)) {
            dateAVerifier = datePriseEnCharge;
        }

        // Voir si il existe une demande de rente-pont valide pour la date de la facture
        RentePontSearchModel rpsm = new RentePontSearchModel();

        rpsm.setForDateValable(dateAVerifier);
        rpsm.setForIdDossier(idDossier);
        rpsm.setForCsEtat(CSEtatRentePont.VALIDE.getCodeSystem());

        if (PerseusServiceLocator.getRentePontService().count(rpsm) < 1) {
            JadeThread.logError(FactureRentePontChecker.class.getName(),
                    "perseus.rentePont.factureRentePont.rentePont.valide");
        } else {
            if (!PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(dateAVerifier).equals(anneeQD)) {
                String[] t = new String[1];
                t[0] = anneeQD;

                JadeThread.logError(FactureRentePontChecker.class.getName(),
                        "perseus.rentePont.factureRentePont.anneeQd.pasMemeDateFacture", t);
            }
        }
    }

    /**
     * Check si le délai de transmission d'une facture définit dans les paramètres de l'application est respecté
     * 
     * @param dateFacture
     * @param dateReception
     * @throws VariableMetierException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private static void checkDelai(String dateFacture, String dateReception) throws VariableMetierException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        Float nbMoisLimite = PerseusServiceLocator
                .getVariableMetierService()
                .getFromCS(CSVariableMetier.NB_MOIS_DELAIS_FACTURE_MALADIE.getCodeSystem(),
                        Calendar.getInstance().getTime()).getMontant();

        // Définir la plus vieille date possible pour une date de facture selon la date d'envoie
        String dateLimiteDeFacture = JadeDateUtil.addMonths(dateReception, -nbMoisLimite.intValue());
        dateLimiteDeFacture = "01." + dateLimiteDeFacture.substring(3);

        // Si la date de la facture est pas avant la date limite erreur
        if (JadeDateUtil.isDateBefore(dateFacture, dateLimiteDeFacture)) {
            JadeThread.logError(FactureRentePontChecker.class.getName(),
                    "perseus.rentePont.factureRentePont.delais.15mois");
        }
    }

    private static void checkNumRefFacture(FactureRentePont factureRentePont) throws JadePersistenceException,
            JadeApplicationException {
        boolean bvrOk = true;
        boolean ccpOk = true;

        String bvrReturn = PerseusServiceLocator.getBVRService().validationNumeroBVR(
                factureRentePont.getSimpleFactureRentePont().getNumRefFacture());

        if (bvrReturn.equals("")) {
            bvrOk = false;
        } else {
            factureRentePont.getSimpleFactureRentePont().setNumRefFacture(bvrReturn);
        }

        if (JadeStringUtil.isEmpty(factureRentePont.getSimpleFactureRentePont().getIdTiersAdressePaiement())
                || JadeStringUtil.isEmpty(factureRentePont.getSimpleFactureRentePont()
                        .getIdApplicationAdressePaiement())) {
            ccpOk = false;
        } else if (!PerseusServiceLocator.getBVRService().validationCCP(
                factureRentePont.getSimpleFactureRentePont().getIdTiersAdressePaiement(),
                factureRentePont.getSimpleFactureRentePont().getIdApplicationAdressePaiement())) {
            ccpOk = false;
        }

        if (!bvrOk && !ccpOk) {
            JadeThread.logError(FactureRentePontChecker.class.getName(),
                    "perseus.facture.numero.reference.et.ccp.incorrect");
        } else if (!bvrOk) {
            JadeThread.logError(FactureRentePontChecker.class.getName(), "perseus.facture.numero.reference.incorrect");
        } else if (!ccpOk) {
            JadeThread.logError(FactureRentePontChecker.class.getName(), "perseus.facture.ccp.incorrect");
        }

    }

    private static void checkAdressePaiement(String idTiersAdressePaiement, String idApplicationAdressePaiement)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        // Check de l'adresse de paiement
        AdresseTiersDetail adressePaiement = PFUserHelper.getAdressePaiementAssure(idTiersAdressePaiement,
                idApplicationAdressePaiement, JACalendar.todayJJsMMsAAAA());

        if ((adressePaiement == null) || (adressePaiement.getFields() == null)) {
            JadeThread.logError(SimpleFacture.class.getName(), "perseus.facture.adressepaiement.mandatory");
        }
    }

    private static void checkQD(FactureRentePont factureRentePont) throws Exception {
        Float excedantRevenuCompenseQD = Float.parseFloat(JadeStringUtil.change(factureRentePont.getQdRentePont()
                .getSimpleQDRentePont().getExcedantRevenuCompense(), "'", ""));
        Float excedantRevenuCompenseFacture = Float.parseFloat(JadeStringUtil.change(factureRentePont
                .getSimpleFactureRentePont().getExcedantRevenuCompense(), "'", ""));
        Float montantUtiliseQD = Float.parseFloat(JadeStringUtil.change(factureRentePont.getQdRentePont()
                .getSimpleQDRentePont().getMontantUtilise(), "'", ""));
        Float montantRembourseFacture = Float.parseFloat(JadeStringUtil.change(factureRentePont
                .getSimpleFactureRentePont().getMontantRembourse(), "'", ""));
        Float excedantRevenuQD = Float.parseFloat(JadeStringUtil.change(factureRentePont.getQdRentePont()
                .getSimpleQDRentePont().getExcedantRevenu(), "'", ""));
        Float montantLimiteQD = Float.parseFloat(JadeStringUtil.change(factureRentePont.getQdRentePont()
                .getSimpleQDRentePont().getMontantLimite(), "'", ""));

        if ((montantRembourseFacture < 0) || (excedantRevenuCompenseFacture < 0)) {
            JadeThread.logError(FactureRentePontChecker.class.getName(),
                    "perseus.rentePont.factureRentePont.montants.negatifs");
        }

        if (montantRembourseFacture > Float.parseFloat(JadeStringUtil.change(factureRentePont
                .getSimpleFactureRentePont().getMontant(), "'", ""))) {
            JadeThread.logError(FactureRentePontChecker.class.getName(),
                    "perseus.rentePont.factureRentePont.montants.incoherents");
        }

        if (!factureRentePont.getSimpleFactureRentePont().getAcceptationForcee()) {
            if (montantRembourseFacture > (montantLimiteQD - montantUtiliseQD)) {
                JadeThread.logError(FactureRentePontChecker.class.getName(),
                        "perseus.rentePont.factureRentePont.qd.montanttropfaible");
            }
            if (((excedantRevenuQD - excedantRevenuCompenseQD) > 0)
                    && (excedantRevenuCompenseFacture > (excedantRevenuQD - excedantRevenuCompenseQD))) {
                JadeThread.logError(FactureRentePontChecker.class.getName(),
                        "perseus.rentePont.factureRentePont.qd.excedantRevenuTropGrand");
            }
        }
    }

    /**
     * Surcharge de la méthode checkQD afin d'être appelée en Ajax
     * 
     * @param pExcedantRevenuCompenseQD
     * @param pExcedantRevenuCompenseFacture
     * @param pMontantUtiliseQD
     * @param pMontantRembourseFacture
     * @param pExcedantRevenuQD
     * @param pMontantLimiteQD
     * @param factureRentePont
     * @throws Exception
     */
    @SuppressWarnings("unused")
    private static void checkQD(String pExcedantRevenuCompenseQD, String pExcedantRevenuCompenseFacture,
            String pMontantUtiliseQD, String pMontantRembourseFacture, String pExcedantRevenuQD,
            String pMontantLimiteQD, String pMontantFacture, boolean acceptationForcee) throws Exception {

        Float excedantRevenuCompenseQD = Float.parseFloat(JadeStringUtil.change(pExcedantRevenuCompenseQD, "'", ""));
        Float excedantRevenuCompenseFacture = Float.parseFloat(JadeStringUtil.change(pExcedantRevenuCompenseFacture,
                "'", ""));
        Float montantUtiliseQD = Float.parseFloat(JadeStringUtil.change(pMontantUtiliseQD, "'", ""));
        Float montantRembourseFacture = Float.parseFloat(JadeStringUtil.change(pMontantRembourseFacture, "'", ""));
        Float excedantRevenuQD = Float.parseFloat(JadeStringUtil.change(pExcedantRevenuQD, "'", ""));
        Float montantLimiteQD = Float.parseFloat(JadeStringUtil.change(pMontantLimiteQD, "'", ""));

        if ((montantRembourseFacture < 0) || (excedantRevenuCompenseFacture < 0)) {
            JadeThread.logError(FactureRentePontChecker.class.getName(),
                    "perseus.rentePont.factureRentePont.montants.negatifs");
        }

        if (montantRembourseFacture > Float.parseFloat(JadeStringUtil.change(pMontantFacture, "'", ""))) {
            JadeThread.logError(FactureRentePontChecker.class.getName(),
                    "perseus.rentePont.factureRentePont.montants.incoherents");
        }

        if (!acceptationForcee) {
            if (montantRembourseFacture > (montantLimiteQD - montantUtiliseQD)) {
                JadeThread.logError(FactureRentePontChecker.class.getName(),
                        "perseus.rentePont.factureRentePont.qd.montanttropfaible");
            }
            if (((excedantRevenuQD - excedantRevenuCompenseQD) > 0)
                    && (excedantRevenuCompenseFacture > (excedantRevenuQD - excedantRevenuCompenseQD))) {
                JadeThread.logError(FactureRentePontChecker.class.getName(),
                        "perseus.rentePont.factureRentePont.qd.excedantRevenuTropGrand");
            }
        }
    }

    /**
     * @param factureRentePont
     */
    public static void checkForDelete(FactureRentePont factureRentePont) {

    }

    /**
     * @param factureRentePont
     */
    public static void checkForUpdate(FactureRentePont factureRentePont) {

    }

}
