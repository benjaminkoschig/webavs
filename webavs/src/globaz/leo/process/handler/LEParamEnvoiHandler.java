/*
 * Créé le 15 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.process.handler;

import globaz.envoi.constantes.ENConstantes;
import globaz.envoi.db.parametreEnvoi.access.ENDefinitionFormule;
import globaz.envoi.db.parametreEnvoi.access.ENRappel;
import globaz.envoi.db.parametreEnvoi.access.ENRappelManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.db.data.LEEnvoiDataSource;
import globaz.leo.db.data.LEParamEnvoiDataSource;
import globaz.leo.db.parametrage.LEComplementFormuleManager;
import globaz.leo.db.parametrage.LEComplementFormuleViewBean;
import globaz.leo.db.parametrage.LEFormulePDFListViewBean;
import globaz.leo.db.parametrage.LEFormulePDFViewBean;
import globaz.leo.util.LEUtil;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LEParamEnvoiHandler {
    public String calculDateEnvoiRappel(ENRappel rappel) throws Exception {
        return this.calculDateEnvoiRappel(rappel, JACalendar.todayJJsMMsAAAA());
    }

    public String calculDateEnvoiRappel(ENRappel rappel, String date) throws Exception {
        if (rappel != null) {
            JACalendarGregorian calendar = new JACalendarGregorian();
            JADate current = new JADate(date);
            if (ENConstantes.CS_EN_UNITE_JOURS.equals(rappel.getCsUnite())) {
                current = calendar.addDays(current, Integer.parseInt(rappel.getTempsAttente()));
            } else if (ENConstantes.CS_EN_UNITE_SEMAINES.equals(rappel.getCsUnite())) {
                current = calendar.addDays(current, Integer.parseInt(rappel.getTempsAttente()) * 7);
            } else if (ENConstantes.CS_EN_UNITE_MOIS.equals(rappel.getCsUnite())) {
                current = calendar.addMonths(current, Integer.parseInt(rappel.getTempsAttente()));
            } else {
                throw new Exception("Erreur : la valeur de l'unité n'est pas correct !");
            }
            JADate nextWorkingDay = calendar.getNextWorkingDay(current);
            return JACalendar.format(nextWorkingDay);
        } else {
            return "";
        }
    }

    /**
     * @param csDocument
     * @param session
     * @param transaction
     * @return
     */
    public String getCategorie(String idFormule, BSession session, BTransaction transaction) throws Exception {
        LEComplementFormuleManager cplManager = new LEComplementFormuleManager();
        cplManager.setSession(session);
        cplManager.setForCsTypeFormule(ILEConstantes.CS_CATEGORIE_GROUPE);
        cplManager.setForIdFormule(idFormule);
        cplManager.find(transaction);
        if (cplManager.size() > 0) {
            return ((LEComplementFormuleViewBean) cplManager.getFirstEntity()).getCsValeur();
        } else {
            throw new Exception("Erreur : aucun complément de formule trouvé pour idFormule=" + idFormule
                    + " et csTypeFormule=" + ILEConstantes.CS_CATEGORIE_GROUPE);
        }
    }

    public String getCategorieByCsDoc(String csDocument, BSession session, BTransaction transaction) throws Exception {
        LEFormulePDFViewBean formule = getFormule(csDocument, session, transaction);
        return getCategorie(formule.getIdFormule(), session, transaction);
    }

    /**
     * @param string
     * @param session
     * @param transaction
     * @return
     */
    public String getCsDocument(String idDefFormule, BSession session, BTransaction transaction) throws Exception {
        ENDefinitionFormule defForm = new ENDefinitionFormule();
        defForm.setSession(session);
        defForm.setIdDefinitionFormule(idDefFormule);
        defForm.retrieve(transaction);
        if (!defForm.isNew()) {
            return defForm.getCsDocument();
        } else {
            throw new Exception("Erreur: Aucune définition définie pour l'id definition formule =" + idDefFormule);
        }
    }

    public LEFormulePDFViewBean getFormule(String csDocument, BSession session, BTransaction transaction)
            throws Exception {
        LEFormulePDFListViewBean formuleDef = new LEFormulePDFListViewBean();
        formuleDef.setSession(session);
        formuleDef.setForCsLangue(LEUtil.getCodeSystemeLangue(session));
        formuleDef.setForCsDocument(csDocument);
        formuleDef.find(transaction);
        if (formuleDef.size() > 0) {
            return (LEFormulePDFViewBean) formuleDef.getFirstEntity();
        } else {
            throw new Exception("Erreur : aucun document trouvé dans le paramétrage pour le type "
                    + session.getCode(csDocument) + " spécifié");
        }
    }

    /**
     * @param string
     * @param session
     * @param transaction
     * @return
     */
    public LEFormulePDFViewBean getFormuleById(String idFormule, BSession session, BTransaction transaction)
            throws Exception {
        LEFormulePDFViewBean formule = new LEFormulePDFViewBean();
        formule.setSession(session);
        formule.setIdFormule(idFormule);
        formule.retrieve(transaction);
        if (formule.isNew()) {
            throw new Exception("Erreur : aucune formule trouvé pour l'id" + idFormule);
        } else {
            return formule;
        }
    }

    /**
     * @param csFormule
     * @param session
     * @param transaction
     * @return
     */
    public String getFormuleForCsRappel(String csFormule, BSession session, BTransaction transaction) throws Exception {
        LEFormulePDFViewBean formule = getFormule(csFormule, session, transaction);
        ENRappelManager rappMgr = new ENRappelManager();
        rappMgr.setSession(session);
        rappMgr.setForIdDefinitionFormule(formule.getIdDefinitionFormule());
        rappMgr.find(transaction);
        if (rappMgr.size() > 0) {
            ENRappel rappel = (ENRappel) rappMgr.getFirstEntity();
            return getFormuleById(rappel.getIdFormule(), session, transaction).getCsDocument();
        } else {
            throw new Exception("Erreur : aucun rappel trouvé pour l'id définition=" + formule.getIdDefinitionFormule());
        }
    }

    /**
     * @param csDocument
     * @param session
     * @param transaction
     * @return
     */
    public String getFormuleRappel(String csDocument, BSession session, BTransaction transaction) throws Exception {
        LEFormulePDFViewBean formule = getFormule(csDocument, session, transaction);
        ENRappel rappel = getRappel(session, transaction, formule.getIdFormule());
        return getCsDocument(rappel.getIdDefinitionFormule(), session, transaction);
    }

    public ENRappel getRappel(BSession session, BTransaction transaction, String idFormule) throws Exception {
        ENRappelManager rappels = new ENRappelManager();
        rappels.setSession(session);
        rappels.setForIdFormule(idFormule);
        rappels.find(transaction);
        if (rappels.size() > 0) {
            // on a trouve un rappel
            return (ENRappel) rappels.getFirstEntity();
        } else {
            return null;
        }
    }

    /**
     * @param session
     * @param object
     * @param csDoc
     */
    public ENRappel getRappelForCsFormule(BSession session, BTransaction transaction, String csDoc) throws Exception {
        String idFormule = getFormule(csDoc, session, transaction).getIdFormule();
        return getRappel(session, transaction, idFormule);
    }

    public LEEnvoiDataSource loadParametreEnvoi(String csDocument, BSession session, BTransaction transaction)
            throws Exception {
        return this.loadParametreEnvoi(csDocument, session, transaction, null);
    }

    public LEEnvoiDataSource loadParametreEnvoi(String csDocument, BSession session, BTransaction transaction,
            String dateRappel) throws Exception {
        return this.loadParametreEnvoi(csDocument, session, transaction, dateRappel, "");
    }

    public LEEnvoiDataSource loadParametreEnvoi(String csDocument, BSession session, BTransaction transaction,
            String dateRappel, String dateCreation) throws Exception {
        LEEnvoiDataSource envoiDS = new LEEnvoiDataSource();
        LEFormulePDFViewBean formule = getFormule(csDocument, session, transaction);
        envoiDS.put(LEEnvoiDataSource.CS_TYPE_DOCUMENT, csDocument);
        envoiDS.put(LEEnvoiDataSource.CS_MANU_AUTO, formule.getCsManuAuto());
        // charger aussi la catégorie
        envoiDS.put(LEEnvoiDataSource.CS_CATEGORIE, getCategorie(formule.getIdFormule(), session, transaction));

        ENRappel rappel = getRappel(session, transaction, formule.getIdFormule());
        if (rappel != null) {
            if (JadeStringUtil.isEmpty(dateRappel)) {
                if (JadeStringUtil.isEmpty(dateCreation)) {
                    envoiDS.put(LEEnvoiDataSource.DATE_RAPPEL, this.calculDateEnvoiRappel(rappel));
                } else {
                    envoiDS.put(LEEnvoiDataSource.DATE_RAPPEL, this.calculDateEnvoiRappel(rappel, dateCreation));
                }
            } else {
                envoiDS.put(LEEnvoiDataSource.DATE_RAPPEL, dateRappel);
            }
            // initialiser le type de l'étape suivante
            envoiDS.put(LEEnvoiDataSource.CS_ETAPE_SUIVANTE,
                    getCsDocument(rappel.getIdDefinitionFormule(), session, transaction));
        }

        envoiDS.put(LEEnvoiDataSource.NOM_CLASSE, formule.getClasseName());
        // envoiDS.put(LEEnvoiDataSource.NOM_DOC, formule.getNomDoc());
        return envoiDS;
    }

    public LEEnvoiDataSource loadParametreEnvoi(String csDocument, LEParamEnvoiDataSource paramsEnvoi,
            BSession session, BTransaction transaction) throws Exception {
        return this.loadParametreEnvoi(csDocument, paramsEnvoi, session, transaction, "", "");
    }

    /*
     * Méthode loadParametreEnvoi : charge les paramètres csDocument, Date de rappel, nom classe et nom document dans le
     * paramétrage de l'envoi
     */
    public LEEnvoiDataSource loadParametreEnvoi(String csDocument, LEParamEnvoiDataSource paramsEnvoi,
            BSession session, BTransaction transaction, String dateRappel, String dateCreation) throws Exception {
        LEEnvoiDataSource envoiDS = this.loadParametreEnvoi(csDocument, session, transaction, dateRappel, dateCreation);
        envoiDS.addParamEnvoi(paramsEnvoi);
        return envoiDS;
    }

    public LEParamEnvoiDataSource newParamsEnvoi(String type, String valeur) {
        LEParamEnvoiDataSource res = new LEParamEnvoiDataSource();
        res.addParamEnvoi(type, valeur);
        return res;
    }
}
