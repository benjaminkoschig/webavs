package globaz.naos.db.affiliation;

import globaz.aquila.api.ICOEtape;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.translation.CodeSystem;
import globaz.webavs.common.CommonExcelmlContainer;
import globaz.webavs.common.CommonExcelmlUtils;

/**
 * @author MMO
 * @since 22 août 2011
 */

public class AFListeAffilieFraisGestionReduitProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Constantes
     */
    public final static String MODEL_XML_NAME = "listeAffilieFraisGestionReduit.xml";
    public final static String MODELCOL_NOM_AFFILIE = "COL_NOMAFF_VALUE";
    public final static String MODELCOL_NOMBRE_POURSUITE = "COL_NBPOURSUITE_VALUE";
    public final static String MODELCOL_NOMBRE_SOMMATION = "COL_NBSOMMATION_VALUE";
    public final static String MODELCOL_NUMERO_AFFILIE = "COL_NOAFF_VALUE";
    public final static String MODELCOL_REMARQUE = "COL_REMARQUE_VALUE";
    public final static String MODELCOL_ROLE_AFFILIE = "COL_ROLEAFF_VALUE";
    public final static String MODELCOL_TAUX_FRAIS_GESTION = "COL_TFG_VALUE";
    public final static String NUMERO_INFOROM = "0272CAF";
    public final static String OUTPUTFILE_NAME = "listeAffilieFraisGestionReduit.xml";

    private String tauxFraisGestion = "";

    public AFListeAffilieFraisGestionReduitProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        try {

            // Chargement des données depuis la DB
            AFListeAffilieFraisGestionReduitManager mgrListeAffilieFraisGestionReduit = new AFListeAffilieFraisGestionReduitManager();
            mgrListeAffilieFraisGestionReduit.setSession(getSession());
            mgrListeAffilieFraisGestionReduit.find(BManager.SIZE_NOLIMIT);

            if (getSession().hasErrors()) {
                getMemoryLog().logMessage(getSession().getErrors().toString(), FWMessage.INFORMATION,
                        AFListeAffilieFraisGestionReduitManager.class.getName());
                return false;
            }

            // Création et remplissage d'un container avec les données récupérées de la DB
            CommonExcelmlContainer ContainerForModelXMLImport = new CommonExcelmlContainer();
            setProgressScaleValue(mgrListeAffilieFraisGestionReduit.size());
            for (int i = 0; (i < mgrListeAffilieFraisGestionReduit.size()) && !isAborted(); i++) {
                AFListeAffilieFraisGestionReduit entityListeAffilieFraisGestionReduit1 = (AFListeAffilieFraisGestionReduit) mgrListeAffilieFraisGestionReduit
                        .getEntity(i);

                String theDateReference = entityListeAffilieFraisGestionReduit1.getCotisation().getDateFin();
                if (JadeStringUtil.isBlankOrZero(theDateReference)) {
                    theDateReference = JACalendar.todayJJsMMsAAAA();
                }

                AFTauxAssurance theTaux = entityListeAffilieFraisGestionReduit1.getCotisation().findTaux(
                        theDateReference, entityListeAffilieFraisGestionReduit1.getCotisation().getMasseAnnuelle(),
                        true);

                if ((theTaux != null)
                        && (CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE.equalsIgnoreCase(theTaux.getGenreValeur()) || (theTaux
                                .getTauxDouble() <= Double.valueOf(getTauxFraisGestion()).doubleValue() / 100))) {

                    ContainerForModelXMLImport.put(AFListeAffilieFraisGestionReduitProcess.MODELCOL_NUMERO_AFFILIE,
                            entityListeAffilieFraisGestionReduit1.getNumeroAffilie());
                    ContainerForModelXMLImport.put(AFListeAffilieFraisGestionReduitProcess.MODELCOL_NOM_AFFILIE,
                            entityListeAffilieFraisGestionReduit1.getNomAffilieFirstPart() + " "
                                    + entityListeAffilieFraisGestionReduit1.getNomAffilieSecondPart());
                    ContainerForModelXMLImport.put(
                            AFListeAffilieFraisGestionReduitProcess.MODELCOL_ROLE_AFFILIE,
                            CodeSystem.getLibelle(getSession(),
                                    entityListeAffilieFraisGestionReduit1.getGenreAssurance()));
                    ContainerForModelXMLImport.put(AFListeAffilieFraisGestionReduitProcess.MODELCOL_TAUX_FRAIS_GESTION,
                            String.valueOf(theTaux.getTauxDouble() * 100));

                    AFListeAffilieFraisGestionReduit entityListeAffilieFraisGestionReduit2 = null;

                    if (i < mgrListeAffilieFraisGestionReduit.size() - 1) {
                        entityListeAffilieFraisGestionReduit2 = (AFListeAffilieFraisGestionReduit) mgrListeAffilieFraisGestionReduit
                                .getEntity(i + 1);
                    }

                    if (isToTransformInOneRow(entityListeAffilieFraisGestionReduit1,
                            entityListeAffilieFraisGestionReduit2)) {
                        ContainerForModelXMLImport.put(
                                AFListeAffilieFraisGestionReduitProcess.MODELCOL_NOMBRE_SOMMATION,
                                entityListeAffilieFraisGestionReduit2.getNombreSommationOrNombrePoursuite());
                        ContainerForModelXMLImport.put(
                                AFListeAffilieFraisGestionReduitProcess.MODELCOL_NOMBRE_POURSUITE,
                                entityListeAffilieFraisGestionReduit1.getNombreSommationOrNombrePoursuite());
                        i = i + 1;
                        incProgressCounter();
                    } else {
                        if (ICOEtape.CS_SOMMATION_ENVOYEE.equalsIgnoreCase(entityListeAffilieFraisGestionReduit1
                                .getTypeEtapeContentieux())) {
                            ContainerForModelXMLImport.put(
                                    AFListeAffilieFraisGestionReduitProcess.MODELCOL_NOMBRE_SOMMATION,
                                    entityListeAffilieFraisGestionReduit1.getNombreSommationOrNombrePoursuite());
                            ContainerForModelXMLImport.put(
                                    AFListeAffilieFraisGestionReduitProcess.MODELCOL_NOMBRE_POURSUITE, "0");
                        } else if (ICOEtape.CS_REQUISITION_DE_POURSUITE_ENVOYEE
                                .equalsIgnoreCase(entityListeAffilieFraisGestionReduit1.getTypeEtapeContentieux())) {
                            ContainerForModelXMLImport.put(
                                    AFListeAffilieFraisGestionReduitProcess.MODELCOL_NOMBRE_POURSUITE,
                                    entityListeAffilieFraisGestionReduit1.getNombreSommationOrNombrePoursuite());
                            ContainerForModelXMLImport.put(
                                    AFListeAffilieFraisGestionReduitProcess.MODELCOL_NOMBRE_SOMMATION, "0");
                        } else {
                            throw new Exception(
                                    "unreachable code : AFListeAffilieFraisGestionReduitManager must return only sommation and poursuite");
                        }
                    }

                    if (CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE.equalsIgnoreCase(theTaux.getGenreValeur())) {
                        ContainerForModelXMLImport.put(AFListeAffilieFraisGestionReduitProcess.MODELCOL_REMARQUE,
                                getSession().getLabel("AFF_FG_REDUIT_TAUX_VARIABLE_A_CONTROLER"));
                    } else {
                        ContainerForModelXMLImport.put(AFListeAffilieFraisGestionReduitProcess.MODELCOL_REMARQUE, " ");
                    }

                }
                incProgressCounter();
            }

            if (ContainerForModelXMLImport.size() >= 1) {

                // Création du fichier excel à partir du modèle xml et du container de données
                String xmlModelPath = Jade.getInstance().getExternalModelDir()
                        + AFApplication.DEFAULT_APPLICATION_NAOS_REP + "/model/excelml/"
                        + getSession().getIdLangueISO().toUpperCase() + "/"
                        + AFListeAffilieFraisGestionReduitProcess.MODEL_XML_NAME;

                String xlsDocPath = Jade.getInstance().getPersistenceDir()
                        + JadeFilenameUtil
                                .addOrReplaceFilenameSuffixUID(AFListeAffilieFraisGestionReduitProcess.OUTPUTFILE_NAME);

                xlsDocPath = CommonExcelmlUtils.createDocumentExcel(xmlModelPath, xlsDocPath,
                        ContainerForModelXMLImport);

                // Publication du document
                JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
                docInfoExcel.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
                docInfoExcel.setDocumentTitle(AFListeAffilieFraisGestionReduitProcess.OUTPUTFILE_NAME);
                docInfoExcel.setPublishDocument(true);
                docInfoExcel.setArchiveDocument(false);
                docInfoExcel.setDocumentTypeNumber(AFListeAffilieFraisGestionReduitProcess.NUMERO_INFOROM);
                this.registerAttachedDocument(docInfoExcel, xlsDocPath);

            }

        } catch (Exception e) {
            getSession().addError(getSession().getLabel("ERREUR_SYSTEM_AFF_FG_REDUIT") + " : " + e.toString());
            JadeLogger.error(this, e.toString());
            getMemoryLog().logMessage(getSession().getErrors().toString(), FWMessage.INFORMATION,
                    this.getClass().getName());
            return false;
        }

        return !(isAborted() || isOnError() || getSession().hasErrors());
    }

    @Override
    protected void _validate() throws Exception {

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);

        if (!JadeNumericUtil.isTauxEnPourcent(getTauxFraisGestion())) {
            getSession().addError(getSession().getLabel("TAUX_FG_SAISI_NON_VALIDE_AFF_FG_REDUIT"));
        }

        /**
         * sécurité supplémentaire ------------------------ mais on ne devrait pas se retrouver avec une adresse email
         * vide en effet, si l'email n'est pas renseigné getEMailAddress() prend l'email du parent ou à défaut, celui du
         * user connecté
         * 
         */
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            getSession().addError(getSession().getLabel("ADRESSE_EMAIL_OBLIGATOIRE"));
            setSendCompletionMail(false);
            setSendMailOnError(false);
        }
    }

    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("EMAIL_OBJECT_ERREUR_AFF_FG_REDUIT");
        } else {
            return getSession().getLabel("EMAIL_OBJECT_SUCCES_AFF_FG_REDUIT");
        }
    }

    public String getTauxFraisGestion() {
        return tauxFraisGestion;
    }

    private boolean isToTransformInOneRow(AFListeAffilieFraisGestionReduit entityListeAffilieFraisGestionReduit1,
            AFListeAffilieFraisGestionReduit entityListeAffilieFraisGestionReduit2) {

        if ((entityListeAffilieFraisGestionReduit1 != null)
                && (entityListeAffilieFraisGestionReduit2 != null)
                && entityListeAffilieFraisGestionReduit1.getNumeroAffilie().equalsIgnoreCase(
                        entityListeAffilieFraisGestionReduit2.getNumeroAffilie())
                && entityListeAffilieFraisGestionReduit1.getIdCotisation().equalsIgnoreCase(
                        entityListeAffilieFraisGestionReduit2.getIdCotisation())) {
            return true;
        }

        return false;

    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setTauxFraisGestion(String tauxFraisGestion) {
        this.tauxFraisGestion = tauxFraisGestion;
    }

}
