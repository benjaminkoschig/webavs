package globaz.cygnus.services.genererDecision;

import globaz.cygnus.services.validerDecision.RFDecisionDocumentData;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeDateUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import ch.globaz.common.constantes.CommonConstLangue;

public class RFGenererDecisionService {

    /**
     * Methode qui va nous convertir la date reçu en format nombre, pour la retourner en format caractères.........
     * Exemple : 01.02.2012 --> 01 février 2012
     * 
     * @param dateParams
     * @return date format caractère
     */
    public String getDateFormatter(String dateParams) {
        String params = null;

        // formatage de la date de décision
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.FRENCH);
        // Date d = JadeDateUtil.getGlobazDate(demande.getDateReception());
        // dateReception = df.format(d);
        if (8 == dateParams.length()) {
            params = ((dateParams.substring(6) + "." + dateParams.substring(4, 6) + "." + dateParams.substring(0, 4)));
            if (JadeDateUtil.isGlobazDate(params)) {
                Date d = JadeDateUtil.getGlobazDate(dateParams);
                params = df.format(d);
            }
        } else if (10 == dateParams.length()) {
            Date d = JadeDateUtil.getGlobazDate(dateParams);
            params = df.format(d);
        }

        if (params.startsWith("1 ")) {
            params = params.substring(1);
            params = "1er" + params;
        }

        return params;
    }

    /**
     * Cette méthode permet de récupérer une date en fonction de la langue de l'utilisateur passée en paramètre.
     * 
     * @param dateParams
     * @param codeISOLangue
     * @return La date formatée dans la langue de l'utilisateur
     */
    public String getDateFormatterWithLanguage(String dateParams, String codeISOLangue) {

        String dateToReturn = "";
        // Si le code ISO de la langue est en FR, on passe par le formatter spécifique RFM pour le français
        if (codeISOLangue.equalsIgnoreCase(CommonConstLangue.LANGUE_ISO_FRANCAIS)) {
            dateToReturn = getDateFormatter(dateParams);
        } else {
            // Sinon on utilise JACalendar
            dateToReturn = JACalendar.format(dateParams, codeISOLangue);
        }
        return dateToReturn;
    }

    /**
     * Cette méthode permet en passant par JACalendar de retourner une date sous le format suivant : Juin 2015 (FR) /
     * Juni 2015 (DE)
     * 
     * @param dateToTransform
     * @param codeIsoLangue
     * @return
     * @throws Exception
     */
    public String getDayAndMonthFormatted(String dateToTransform, String codeIsoLangue) throws JAException {
        String dateToReturn = "";
        try {
            // récupérer l'année
            Integer year = JACalendar.getYear(dateToTransform);
            // récupérer le mois
            String month = JACalendar.getMonthName(JACalendar.getMonth(dateToTransform), codeIsoLangue);

            dateToReturn = month + " " + year;

        } catch (JAException e) {
            throw new JAException(
                    "An error happened while loading the formatted date with the following input : dateToTransform = "
                            + dateToTransform + " codeIsoLangue = " + codeIsoLangue, e.getMessage().toString());
        }
        return dateToReturn;

    }

    /**
     * Methode qui retourne le nom et prénom de l'adresse de paiement du tiers
     * 
     * @param session
     * @param decisionDocument
     * @return String[] nom/prenom
     * @throws Exception
     */
    public String[] getTiersAdressePaiement(BSession session, RFDecisionDocumentData decisionDocument) throws Exception {

        String nomAdressePaiement = "";
        String prenomAdressePaiement = "";

        // Initialisation de l'adresse de paiement
        TIAdressePaiementData tiersAdressePaiement = PRTiersHelper.getAdressePaiementData(session,
                session.getCurrentThreadTransaction(), decisionDocument.getIdTiersAdressePaiement(),
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA());

        // Si TIAdressePaiementData est NULL, on initialise une adresse de paiement PRTiersWrapper
        PRTiersWrapper tiersWrapperAdressePaiement = null;

        if (null == tiersAdressePaiement) {
            tiersWrapperAdressePaiement = PRTiersHelper.getAdministrationParId(session,
                    decisionDocument.getIdTiersAdressePaiement());
        }

        // Charge TIAdressePaiementData si pas NULL
        if (null != tiersAdressePaiement) {
            nomAdressePaiement = tiersAdressePaiement.getNomTiers1().toUpperCase();
            prenomAdressePaiement = tiersAdressePaiement.getNomTiers2();
        }
        // Sinon charge PRTiersWrapper
        else {
            // Si c'est une administration on intervertit nom et prenom pour l'affichage
            nomAdressePaiement = tiersWrapperAdressePaiement.getProperty(PRTiersWrapper.PROPERTY_NOM);
            prenomAdressePaiement = tiersWrapperAdressePaiement.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
        }

        return new String[] { nomAdressePaiement, prenomAdressePaiement };

    }

}
