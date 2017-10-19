package ch.globaz.amal.businessimpl.services.sedexRP.utils;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.db.tiers.TIAvoirContact;
import globaz.pyxis.db.tiers.TIAvoirContactManager;
import globaz.pyxis.db.tiers.TIMoyenCommunication;
import globaz.pyxis.db.tiers.TIMoyenCommunicationManager;
import java.util.Iterator;
import javax.xml.datatype.XMLGregorianCalendar;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.businessimpl.utils.AMGestionTiers;
import ch.globaz.amal.businessimpl.utils.SessionProvider;

/**
 * Class utilitaire pour la gestion, construction et réception des annonces RP AMAL
 * 
 * @author cbu
 * 
 */
public class AMSedexRPUtil {

    /**
     * Formate une date de type XMLGregorianCalendar en STRING<br>
     * 
     * La date formatée est construite selon ce que contient la date XMLGregorianCalendar et peut retourner 3 format
     * différents :
     * <ul>
     * <li>JJ.MM.YYYY</li>
     * <li>MM.YYYY</li>
     * <li>YYYY</li>
     * </ul>
     * 
     * @param xmlDate
     * @return une chaîne représentant la date
     */
    public static String getDateXMLToString(XMLGregorianCalendar xmlDate) {
        String s_day = "";
        String s_month = "";
        String s_year = "";
        String returnDate = "";

        if (xmlDate == null) {
            return "";
        }

        try {
            s_day = String.valueOf(xmlDate.getDay());
            if (JadeNumericUtil.isIntegerPositif(s_day)) {
                returnDate = JadeStringUtil.fillWithZeroes(s_day, 2) + ".";
            }
        } catch (Exception e) {
            // On laisse la valeur par défaut (vide)
        }

        try {
            s_month = String.valueOf(xmlDate.getMonth());
            if (JadeNumericUtil.isIntegerPositif(s_month)) {
                returnDate += JadeStringUtil.fillWithZeroes(s_month, 2) + ".";
            }
        } catch (Exception e) {
            // On laisse la valeur par défaut (vide)
        }

        try {
            s_year = String.valueOf(xmlDate.getYear());
            if (JadeNumericUtil.isIntegerPositif(s_year)) {
                returnDate += s_year;
            }
        } catch (Exception e) {
            // On laisse la valeur par défaut (vide)
        }

        if (JadeDateUtil.isGlobazDate(returnDate) || JadeDateUtil.isGlobazDateMonthYear(returnDate)
                || JadeDateUtil.isGlobazDateYear(returnDate)) {
            return returnDate;
        }

        return "";
    }

    /**
     * Va chercher l'idTiers d'une caisse par rapport à son idSedex
     * 
     * @param sedexId
     *            l'id Sedex de la caisse
     * @return idTiers de la caisse
     * @throws AnnonceSedexException
     */
    public static String getIdTiersFromSedexId(String sedexId) throws AnnonceSedexException {

        try {
            TIMoyenCommunicationManager moyenCommunicationManager = new TIMoyenCommunicationManager();
            moyenCommunicationManager.setSession(SessionProvider.findSession());
            moyenCommunicationManager.setForTypeCommunication(IAMCodeSysteme.CS_MOYEN_COMMUNICATION_SEDEX);
            moyenCommunicationManager.setForIdApplication(AMGestionTiers.CS_DOMAINE_AMAL);
            moyenCommunicationManager.setForMoyenLike(sedexId);
            moyenCommunicationManager.find();

            // S170201_010 - prendre le premier id tiers trouvé d'un même sedex-id
            if (!moyenCommunicationManager.getContainer().isEmpty()) {
                TIMoyenCommunication moyenCommunication = (TIMoyenCommunication) moyenCommunicationManager.get(0);
                return moyenCommunication.getIdContact();
            } else {
                throw new AnnonceSedexException("No SedexId defined for id tiers : " + sedexId);
            }
        } catch (Exception e) {
            throw new AnnonceSedexException("Error searching idTiers for sedexId: " + sedexId + " ==> "
                    + e.getMessage());
        }
    }

    /**
     * Va chercher idSedex d'une caisse par rapport à son idTiers
     * 
     * @param idAdministration
     *            idTiers de la caisse
     * @return l'id Sedex
     * @throws AnnonceSedexException
     */
    public static String getSedexIdFromIdTiers(String idAdministration) throws AnnonceSedexException {

        if (JadeStringUtil.isBlankOrZero(idAdministration)) {
            throw new AnnonceSedexException("Error searching idSedex : idAdministration can't be null !");
        }

        try {
            TIAvoirContactManager avoirContactManager = new TIAvoirContactManager();
            avoirContactManager.setSession(SessionProvider.findSession());
            avoirContactManager.setForIdTiers(idAdministration);
            avoirContactManager.find(2);

            if (avoirContactManager.getContainer().size() == 0) {

                throw new AnnonceSedexException("Error searching idSedex : no SedexId defined for id tiers : "
                        + idAdministration);
            }

            for (Iterator it = avoirContactManager.iterator(); it.hasNext();) {
                TIAvoirContact avoirContact = (TIAvoirContact) it.next();

                TIMoyenCommunicationManager communicationManager = new TIMoyenCommunicationManager();
                communicationManager.setSession(SessionProvider.findSession());
                communicationManager.setForIdContact(avoirContact.getIdContact());
                communicationManager.setForTypeCommunication(IAMCodeSysteme.CS_MOYEN_COMMUNICATION_SEDEX);
                communicationManager.setForIdApplication(AMGestionTiers.CS_DOMAINE_AMAL);
                communicationManager.find(2);

                if (communicationManager.getContainer().size() == 1) {
                    TIMoyenCommunication moyenCommunication = (TIMoyenCommunication) communicationManager
                            .getContainer().get(0);

                    return moyenCommunication.getMoyen();
                } else if (communicationManager.getContainer().size() > 1) {
                    throw new AnnonceSedexException(
                            "Error searching idSedex : Multiple SedexId defined for id tiers : " + idAdministration);
                } else {
                    throw new AnnonceSedexException("Error searching idSedex : No SedexId defined for id tiers : "
                            + idAdministration);
                }
            }

            return null;
        } catch (Exception e) {
            throw new AnnonceSedexException("Error searching SedexId for id tiers : " + idAdministration + " ==> "
                    + e.getMessage());
        }
    }
}
