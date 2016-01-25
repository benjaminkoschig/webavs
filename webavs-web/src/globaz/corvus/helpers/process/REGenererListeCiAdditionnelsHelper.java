package globaz.corvus.helpers.process;

import globaz.corvus.process.REGenererListeCiAdditionnelsProcess;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.vb.process.REGenererListeCiAdditionnelsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import ch.globaz.common.properties.CommonProperties;

/**
 * @author BSC
 */
public class REGenererListeCiAdditionnelsHelper extends PRAbstractHelper {

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
    }

    /**
     * Valide les paramètres d'entrée du processus REGenererListeCiAdditionnelsProcess et l'exécute
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession bisession) {
        REGenererListeCiAdditionnelsViewBean glCiAddViewBean = (REGenererListeCiAdditionnelsViewBean) viewBean;

        boolean error = false;
        String message = null;
        String noCaisse = null;
        String noAgence = null;
        String dateDernierPaiement = null;
        BSession session = (BSession) bisession;

        try {
            // Test si la propriété KEY_NO_CAISSE n'existe pas
            try {
                noCaisse = CommonProperties.KEY_NO_CAISSE.getValue();
            } catch (Exception exception) {
                error = true;
                String description = getCommonPropertiesDescription(session, CommonProperties.KEY_NO_CAISSE);
                message = getLabel(session, "LISTE_CIA_ERREUR_ACCES_PROPRIETE_NON_DECLAREE");
                message = message.replace("{0}", description);
                setViewBeanErrorMessage(viewBean, message);
            }
            // Test si la valeur de la propriété est vide ou incorrecte
            if (!error) {
                if (JadeStringUtil.isBlank(noCaisse)) {
                    error = true;
                    String description = getCommonPropertiesDescription(session, CommonProperties.KEY_NO_CAISSE);
                    message = getLabel(session, "LISTE_CIA_ERREUR_VALEUR_PROPRIETE_INVALIDE");
                    message = message.replace("{0}", description);
                    message = message.replace("{1}", "[" + noCaisse + "]");
                    setViewBeanErrorMessage(viewBean, message);
                }
            }

            if (!error) {
                // Test si la propriété NUMERO_AGENCE n'existe pas
                try {
                    noAgence = CommonProperties.NUMERO_AGENCE.getValue();
                } catch (Exception exception) {
                    error = true;
                    String description = getCommonPropertiesDescription(session, CommonProperties.NUMERO_AGENCE);
                    message = getLabel(session, "LISTE_CIA_ERREUR_ACCES_PROPRIETE_NON_DECLAREE");
                    message = message.replace("{0}", description);
                    setViewBeanErrorMessage(viewBean, message);
                }
            }
            // Test si la valeur de la propriété est vide ou incorrecte
            if (!error) {
                if (JadeStringUtil.isBlank(noAgence)) {
                    error = true;
                    String description = getCommonPropertiesDescription(session, CommonProperties.NUMERO_AGENCE);
                    message = getLabel(session, "LISTE_CIA_ERREUR_VALEUR_PROPRIETE_INVALIDE");
                    message = message.replace("{0}", description);
                    message = message.replace("{1}", "[" + noAgence + "]");
                    setViewBeanErrorMessage(viewBean, message);
                }
            }
            if (!error) {
                // Récupération de la date du dernier paiement
                dateDernierPaiement = REPmtMensuel.getDateDernierPmt(session);
                if (!JadeDateUtil.isGlobazDate("01." + dateDernierPaiement)) {
                    error = true;
                    message = "Internal error : ";
                    message += getLabel(session, "LISTE_CIA_ERREUR_IMPOSSIBLE_RECUPERER_DATE_DERNIER_PAIEMENT");
                    setViewBeanErrorMessage(viewBean, message);
                }
            }

            // ==> lancement du process uniquement si aucune erreur détectée
            if (!error) {
                REGenererListeCiAdditionnelsProcess process = new REGenererListeCiAdditionnelsProcess();

                process.setSession(session);
                process.setEMailAddress(glCiAddViewBean.getEMailAddress());
                process.setDateDebut(glCiAddViewBean.getDateDebut());
                process.setDateFin(glCiAddViewBean.getDateFin());
                process.setDateDernierPaiement(dateDernierPaiement);
                process.setGenreCiAdd(glCiAddViewBean.getGenreCiAdd());
                process.setNoCaisse(noCaisse);
                process.setNoAgence(noAgence);
                process.setAjouterCommunePolitique(CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue());
                process.validateProcess();
                process.start();
            }

        } catch (Exception e) {
            error = true;
            viewBean.setMessage("Internal error occured : " + e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }

    /**
     * Retourne une description détaillée d'une CommonProperties
     * 
     * @param session
     *            La session à utiliser pour récupérer les traduction
     * @param properties
     *            La properties à decrire
     * @return
     * @throws Exception
     */
    private String getCommonPropertiesDescription(BSession session, CommonProperties properties) throws Exception {
        if ((session == null) || (properties == null)) {
            return null;
        }
        String description = getLabel(session, "DESCRIPTION_PROPRIETE");
        StringBuilder message = new StringBuilder();
        message.append("[common.");
        message.append(properties.getPropertyName());
        message.append("]. ");
        message.append(description);
        message.append(": ");
        message.append(properties.getDescription());
        return message.toString();
    }

    /**
     * Retourne la traduction d'un label
     * 
     * @param session
     *            La session à utiliser pour récupérer la traduction
     * @param labelKey
     *            La clé du label à récupérer
     * @return Le texte correspondant à la clé du label
     * @throws Exception
     *             Si la session est null ou si la clé est vide ou null
     */
    private String getLabel(BSession session, String labelKey) throws Exception {
        if (JadeStringUtil.isEmpty(labelKey)) {
            throw new Exception("Empty labelKey received for translation");
        } else if (session == null) {
            throw new Exception("No session founded for labelKey [" + labelKey + "] translation");
        } else {
            return session.getLabel(labelKey);
        }
    }

    /**
     * Met le viewBean en erreur et renseigne un message
     * 
     * @param viewBean
     *            Le viewBean à mettre en erreur
     * @param message
     *            Le message à affecter au viewBean
     * @throws Exception
     *             Si le viewBean est null ou si le message esvide ou null
     */
    private void setViewBeanErrorMessage(FWViewBeanInterface viewBean, String message) throws Exception {
        if (JadeStringUtil.isEmpty(message)) {
            throw new Exception("Empty message received for viewBean error message");
        } else if (viewBean == null) {
            throw new Exception("The viewBean is null. Can not set him in error");
        } else {
            viewBean.setMessage(message);
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
