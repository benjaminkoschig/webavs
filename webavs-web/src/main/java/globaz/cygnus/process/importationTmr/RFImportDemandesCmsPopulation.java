package globaz.cygnus.process.importationTmr;

import ch.globaz.jade.process.annotation.BusinessKey;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.population.*;
import ch.globaz.jade.process.utils.JadeProcessCommonUtils;
import com.google.gson.Gson;
import globaz.cygnus.application.RFApplication;
import globaz.cygnus.exceptions.RFXmlmlException;
import globaz.cygnus.mappingXmlml.IRFImportationTmrListeColumns;
import globaz.cygnus.mappingXmlml.RFXmlmlMappingLogImportationTmr;
import globaz.cygnus.process.RFImportDemandesCmsData;
import globaz.cygnus.process.importationTmr.RFProcessImportationTmrEnum;
import globaz.cygnus.process.importationTmr.creationDemandesStep1.RFImportDemandesCmsCreationHandler;
import globaz.cygnus.utils.RFExcelmlUtils;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.utils.RFXmlmlContainer;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author jje
 * 
 */
public class RFImportDemandesCmsPopulation extends JadeProcessPopulationByFileCsv implements
        JadeProcessPopulationNeedProperties<RFProcessImportationTmrEnum>, JadeProcessPopulationPropertySavable,
        JadeProcessPopulationCheckable<RFProcessImportationTmrEnum>,
        JadeProcessPopulationResolveDescription<RFProcessImportationTmrEnum> {

    private enum PositionsImportDemandesCmsPopulationEnum {

        POSITION_DATE_DEBUT_TRAITEMENT(75, 80),
        POSITION_DATE_FACTURE(75, 80),
        POSITION_DATE_FIN_TRAITEMENT(81, 86),
        POSITION_MONTANT_FACTURE(92, 98),
        POSITION_NO_AF(19, 21),
        POSITION_NO_EQUIPE(108, 111),
        POSITION_NSS_BENEFICIAIRE(34, 44),
        POSITION_TYPE_ENREGISTREMENT(1, 2);

        private int debut;
        private int fin;

        PositionsImportDemandesCmsPopulationEnum(int debut, int fin) {
            this.debut = debut;
            this.fin = fin;
        }

        public int getDebut() {
            return debut;
        }

        public int getFin() {
            return fin;
        }
    }

    private static final String DESCRIPTION_ENTITE_SEPARATEUR = " / ";
    private static final String DESCRIPTION_ENTITE_VIDE = " - ";
    private static final String NUMERO_LIGNE = " N° ligne: ";
    private static final String TYPE_ENREGISTREMENT_DERNIERE_LIGNE = "99";
    private static final String TYPE_ENREGISTREMENT_PREMIERE_LIGNE = "01";

    public static String traiterLigne(int debut, int fin, String ligne, int numeroLigne, String source) {

        debut--;

        if (JadeStringUtil.isBlankOrZero(ligne)) {
            throw new IllegalArgumentException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "RF_IMPORT_TMR_LIGNE_VIDE")
                    + RFImportDemandesCmsPopulation.NUMERO_LIGNE + String.valueOf(numeroLigne) + " [" + source + "]");
        }

        if (ligne.length() < fin) {
            throw new IllegalArgumentException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "RF_IMPORT_TMR_POSITION_INATEIGNABLE")
                    + RFImportDemandesCmsPopulation.NUMERO_LIGNE + String.valueOf(numeroLigne) + " [" + source + "]");
        } else {
            return ligne.substring(debut, fin);
        }
    }

    private String descriptionProcess = "";
    private String idGestionnaire = "";
    private List<String[]> logsList = new ArrayList<String[]>();
    Map<Enum<?>, String> numeroAfPropertiesMap = null;

    private Map<RFProcessImportationTmrEnum, String> properties = null;

    private String buildDescription(RFImportDemandesCmsData rfImpDemCmsDat) {

        StringBuffer descriptionStrBfr = new StringBuffer();
        descriptionStrBfr.append(RFImportDemandesCmsCreationHandler.formatNssFromEntity(rfImpDemCmsDat
                .getNssBeneficiaire()));
        descriptionStrBfr.append(RFImportDemandesCmsPopulation.DESCRIPTION_ENTITE_SEPARATEUR);
        descriptionStrBfr.append(retrieveDateFormattee(rfImpDemCmsDat.getDateDeDebutTraitement()));
        descriptionStrBfr.append(RFImportDemandesCmsPopulation.DESCRIPTION_ENTITE_VIDE);
        descriptionStrBfr.append(retrieveDateFormattee(rfImpDemCmsDat.getDateDeFinTraitement()));

        return descriptionStrBfr.toString();
    }

    @Override
    public void checker(Map<RFProcessImportationTmrEnum, String> map) throws JadePersistenceException,
            JadeApplicationException {

        BufferedReader reader = null;

        try {
            String file = getFileName();

            if (JadeStringUtil.isEmpty(getFileName())) {
                file = map.get(RFProcessImportationTmrEnum.FILE_PATH_FOR_POPULATION);
            }

            String fileName = JadeFsFacade.readFile(file);

            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

            String ligne = reader.readLine();
            String idTiersAdressePaiement = getAdministrationAfParCode(getNumeroAf(ligne, 1));

            TIAdressePaiementData retValue = PRTiersHelper.getAdressePaiementData(
                    BSessionUtil.getSessionFromThreadContext(), null, idTiersAdressePaiement,
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA());

            if (JadeStringUtil.isBlankOrZero(retValue.getIdAvoirPaiementUnique())) {
                throw new JadePersistenceException("Le CMS référence  n°:" + getNumeroAf(ligne, 1)
                        + " n'a pas d'adresse de paiement [getAdministrationAfParCode()]");
            }

        } catch (Exception e) {
            throw new JadePersistenceException(e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(
                            "RFImportDemandesCmsPopulation.checker: Impossible de fermer le reader ", e);
                }
            }

        }
    }

    private void envoyerMail(Map<RFProcessImportationTmrEnum, String> map) {
        try {
            JadeSmtpClient.getInstance().sendMail(
                    getEmail(map),
                    BSessionUtil.getSessionFromThreadContext().getLabel("RF_IMPORT_TMR_PROCESS_NAME"),
                    /*
                     * !JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR) !RFUtils
                     * .hasErrorsLogList(this.logsList) ?
                     */BSessionUtil.getSessionFromThreadContext().getLabel(
                            "RF_IMPORT_TMR_PROCESS_POPULATION_SUCCESS") /*
                                                                            * :
                                                                            * BSessionUtil.getSessionFromThreadContext
                                                                            * () .getLabel(
                                                                            * "RF_IMPORT_TMR_PROCESS_POPULATION_FAILED"
                                                                            * )
                                                                            */,
                    new String[] { generateDocumentLog(properties) });
        } catch (Exception e) {
            JadeProcessCommonUtils.addError(e);
        }
    }

    private String generateDocumentLog(Map<RFProcessImportationTmrEnum, String> map) throws RFXmlmlException,
            Exception {
        RFXmlmlContainer container = RFXmlmlMappingLogImportationTmr.loadResults(logsList, BSessionUtil
                .getSessionFromThreadContext().getUserId());

        String nomDoc = BSessionUtil.getSessionFromThreadContext().getLabel("RF_IMPORT_TMR_NOM_LOG_POPULATION");
        String docPath = RFExcelmlUtils.createDocumentExcel(BSessionUtil.getSessionFromThreadContext().getIdLangueISO()
                .toUpperCase()
                + "/" + IRFImportationTmrListeColumns.MODEL_NAME, nomDoc, container);
        return docPath;
    }

    private String getAdministrationAfParCode(String code) throws JadePersistenceException {

        String idTiers = "";

        if (JadeStringUtil.isBlankOrZero(code)) {
            throw new IllegalArgumentException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "RF_IMPORT_TMR_NUMERO_AF_VIDE")
                    + " [getAdministrationAfParCode()]");
        }

        PRTiersWrapper[] administrationCms = null;
        try {

            BISession cygnusSession = GlobazSystem.getApplication(RFApplication.DEFAULT_APPLICATION_CYGNUS)
                    .newSession();
            administrationCms = PRTiersHelper.getAdministrationActiveForGenreAndCode(cygnusSession,
                    RFPropertiesUtils.getCsGenreAdminstrationCMS(), code);
        } catch (Exception e) {
            throw new JadePersistenceException(e.getMessage());
        }

        if ((administrationCms == null) || (administrationCms.length == 0)) {
            throw new JadePersistenceException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "RF_IMPORT_TMR_ADMINISTRATION_AF_INEXISANT")
                    + " n°:" + code + " [getAdministrationAfParCode()]");

        } else {
            idTiers = administrationCms[0].getIdTiers();
            if (JadeStringUtil.isBlankOrZero(idTiers)) {
                throw new JadePersistenceException(BSessionUtil.getSessionFromThreadContext().getLabel(
                        "RF_IMPORT_TMR_ADMINISTRATION_AF_INEXISANT")
                        + " n°:" + code + " [getAdministrationAfParCode()]");
            } else {
                descriptionProcess = administrationCms[0].getNom() + " " + administrationCms[0].getPrenom();
            }
        }

        return idTiers;
    }

    @Override
    @BusinessKey(unique = false)
    public String getBusinessKey() {

        try {
            String fileName = getFileName();
            if (JadeStringUtil.isEmpty(fileName)) {
                fileName = properties.get(RFProcessImportationTmrEnum.FILE_PATH_FOR_POPULATION);
                setFileName(fileName);
            }

            return getHachageMd5();

        } catch (Exception e) {
            JadeProcessCommonUtils.addError(e);
            return null;
        }

    }

    private String getDateDebutTraitement(String ligne, int numeroLigne) {
        return RFImportDemandesCmsPopulation.traiterLigne(
                PositionsImportDemandesCmsPopulationEnum.POSITION_DATE_DEBUT_TRAITEMENT.getDebut(),
                PositionsImportDemandesCmsPopulationEnum.POSITION_DATE_DEBUT_TRAITEMENT.getFin(), ligne, numeroLigne,
                "getDateDebutTraitement()");
    }

    private String getDateFacture(String ligne, int numeroLigne) {
        return RFImportDemandesCmsPopulation.traiterLigne(
                PositionsImportDemandesCmsPopulationEnum.POSITION_DATE_FACTURE.getDebut(),
                PositionsImportDemandesCmsPopulationEnum.POSITION_DATE_FACTURE.getFin(), ligne, numeroLigne,
                "getDateFacture()");
    }

    private String getDateFinTraitement(String ligne, int numeroLigne) {
        return RFImportDemandesCmsPopulation.traiterLigne(
                PositionsImportDemandesCmsPopulationEnum.POSITION_DATE_FIN_TRAITEMENT.getDebut(),
                PositionsImportDemandesCmsPopulationEnum.POSITION_DATE_FIN_TRAITEMENT.getFin(), ligne, numeroLigne,
                "getDateFinTraitement()");
    }

    private String getEmail(Map<RFProcessImportationTmrEnum, String> map) {
        return JadeStringUtil.isBlankOrZero(map.get(RFProcessImportationTmrEnum.EMAIL)) ? BSessionUtil
                .getSessionFromThreadContext().getUserEMail() : map.get(RFProcessImportationTmrEnum.EMAIL);
    }

    @Override
    public Class<RFProcessImportationTmrEnum> getEnumForProperties() {
        return RFProcessImportationTmrEnum.class;
    }

    private String getHachageMd5() throws Exception {

        String fileName = getFileName();

        if (null != fileName) {
            String ligne;
            BufferedReader reader = null;
            String md5 = null;
            try {
                fileName = JadeFsFacade.readFile(fileName);

                reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
                StringBuilder s = new StringBuilder();
                while ((ligne = reader.readLine()) != null) {
                    s.append(ligne);
                }

                if (!JadeStringUtil.isBlankOrZero(s.toString())) {
                    md5 = DigestUtils.md5Hex(s.toString());
                } else {
                    throw new JadePersistenceException("RFImportDemandesCmsPopulation.getHachageMd5(): fichier vide"
                            + " [getHachageMd5()]");
                }

            } catch (Exception e) {
                throw new JadePersistenceException(e.getMessage());
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }

            return md5;

        } else {
            throw new JadePersistenceException("RFImportDemandesCmsPopulation.getHachageMd5(): fichier null"
                    + " [getHachageMd5()]");
        }
    }

    private String getMontantFacture(String ligne, int numeroLigne) {
        return RFImportDemandesCmsPopulation.traiterLigne(
                PositionsImportDemandesCmsPopulationEnum.POSITION_MONTANT_FACTURE.getDebut(),
                PositionsImportDemandesCmsPopulationEnum.POSITION_MONTANT_FACTURE.getFin(), ligne, numeroLigne,
                "getMontantFacture()");
    }

    private String getNssBeneficiaire(String ligne, int numeroLigne) {
        return RFImportDemandesCmsPopulation.traiterLigne(
                PositionsImportDemandesCmsPopulationEnum.POSITION_NSS_BENEFICIAIRE.getDebut(),
                PositionsImportDemandesCmsPopulationEnum.POSITION_NSS_BENEFICIAIRE.getFin(), ligne, numeroLigne,
                "getNssBeneficiaire()");
    }

    private String getNumeroAf(String ligne, int numeroLigne) {
        return RFImportDemandesCmsPopulation.traiterLigne(
                PositionsImportDemandesCmsPopulationEnum.POSITION_NO_AF.getDebut(),
                PositionsImportDemandesCmsPopulationEnum.POSITION_NO_AF.getFin(), ligne, numeroLigne, "getNumeroAf()");
    }

    private String getNumeroEquipe(String ligne, int numeroLigne) {
        return RFImportDemandesCmsPopulation.traiterLigne(
                PositionsImportDemandesCmsPopulationEnum.POSITION_NO_EQUIPE.getDebut(),
                PositionsImportDemandesCmsPopulationEnum.POSITION_NO_EQUIPE.getFin(), ligne, numeroLigne,
                "getNumeroCms()");
    }

    @Override
    public String getParametersForUrl(JadeProcessEntity entity) throws JadePersistenceException,
            JadeApplicationException {
        return null;
    }

    @Override
    public JadeProcessEntity getPopulation(String ligne, int i) {

        JadeProcessEntity entity = new JadeProcessEntity();
        entity.setIdRef(new Integer(i).toString());
        RFImportDemandesCmsData rfImpDemCmsDat = new RFImportDemandesCmsData();
        Gson gson = new Gson();

        try {
            rfImpDemCmsDat.setNumeroLigne(Integer.valueOf(i).toString());
            rfImpDemCmsDat.setLigne(ligne);

            // La première ligne contient uniquement le numéro tmr
            if (siPremiereLigne(ligne, i)) {

                rfImpDemCmsDat.setPremiereLigne(true);

                idGestionnaire = BSessionUtil.getSessionFromThreadContext().getUserId();

                numeroAfPropertiesMap = new HashMap<Enum<?>, String>();
                numeroAfPropertiesMap.put(RFProcessImportationTmrEnum.NUMERO_AF, getNumeroAf(ligne, i));

                entity.setDescription(RFImportDemandesCmsPopulation.DESCRIPTION_ENTITE_VIDE);

            } else {
                if (!siDerniereLigne(ligne, i)) {
                    // Remarque: Les erreurs sont détectées dans l'étape 1 (RFImportDemandesCmsCreation)
                    rfImpDemCmsDat.setDateDeDebutTraitement(getDateDebutTraitement(ligne, i));
                    rfImpDemCmsDat.setDateDeFinTraitement(getDateFinTraitement(ligne, i));
                    rfImpDemCmsDat.setDateFacture(getDateFacture(ligne, i));
                    rfImpDemCmsDat.setGestionnaire(idGestionnaire);
                    rfImpDemCmsDat.setMontantDemande(getMontantFacture(ligne, i));
                    rfImpDemCmsDat.setNssBeneficiaire(getNssBeneficiaire(ligne, i));
                    rfImpDemCmsDat.setNumeroEquipe(getNumeroEquipe(ligne, i));

                    entity.setDescription(buildDescription(rfImpDemCmsDat));

                } else {
                    rfImpDemCmsDat.setDerniereLigne(true);
                    entity.setDescription(RFImportDemandesCmsPopulation.DESCRIPTION_ENTITE_VIDE);
                }
            }

            entity.setValue1(gson.toJson(rfImpDemCmsDat));

            logLigne("RFImportDemandesCmsPopulation.getPopulation()", "Lecture de la ligne réussie",
                    rfImpDemCmsDat.getNumeroLigne(), rfImpDemCmsDat.getNssBeneficiaire(), false);

            if (siDerniereLigne(ligne, i)) {
                envoyerMail(properties);

            }

        } catch (Exception e) {
            logLigne("RFImportDemandesCmsPopulation.getPopulation()", e.getMessage(), rfImpDemCmsDat.getNumeroLigne(),
                    rfImpDemCmsDat.getNssBeneficiaire(), true);
            entity.setValue1(gson.toJson(rfImpDemCmsDat));
            return entity;
        }

        return entity;
    }

    private String getTypeEnregistrement(String ligne, int numeroLigne) {
        return RFImportDemandesCmsPopulation.traiterLigne(
                PositionsImportDemandesCmsPopulationEnum.POSITION_TYPE_ENREGISTREMENT.getDebut(),
                PositionsImportDemandesCmsPopulationEnum.POSITION_TYPE_ENREGISTREMENT.getFin(), ligne, numeroLigne,
                "getTypeEnregistrement()");
    }

    @Override
    public Map<Enum<?>, String> getValueToSave() {
        return numeroAfPropertiesMap;
    }

    private void logLigne(String source, String message, String numeroLigne, String nss, boolean isError)
            throws JadeNoBusinessLogSessionError {
        // Les entités ne doivent pas être en erreur, car celles-ci sont réutilisées dans les étapes suivantes
        if (isError) {
            JadeThread.logWarn(source, message);
        } else {
            JadeThread.logInfo(source, message);
        }
        RFUtils.ajouterLogImportationsTmr(
                isError ? JadeBusinessMessageLevels.ERROR : JadeBusinessMessageLevels.WARN, numeroLigne, nss, message, logsList);
    }

    @Override
    public String resolveDescriptionProcess(Map<RFProcessImportationTmrEnum, String> map) {
        return descriptionProcess;
    }

    private String retrieveDateFormattee(String date) {

        try {
            return PRDateFormater.convertDate_AAMMJJ_to_JJxMMxAAAA(date);
        } catch (JAException e) {
            return RFImportDemandesCmsPopulation.DESCRIPTION_ENTITE_VIDE;
        }
    }

    @Override
    public void setProperties(Map<RFProcessImportationTmrEnum, String> hashMap) {
        properties = hashMap;
    }

    private boolean siDerniereLigne(String ligne, int numeroLigne) {
        return getTypeEnregistrement(ligne, numeroLigne).equals(
                RFImportDemandesCmsPopulation.TYPE_ENREGISTREMENT_DERNIERE_LIGNE);
    }

    private boolean siPremiereLigne(String ligne, int numeroLigne) {
        return getTypeEnregistrement(ligne, numeroLigne).equals(
                RFImportDemandesCmsPopulation.TYPE_ENREGISTREMENT_PREMIERE_LIGNE);
    }
}
