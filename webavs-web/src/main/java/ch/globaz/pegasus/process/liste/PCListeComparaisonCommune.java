package ch.globaz.pegasus.process.liste;

import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.excel.PCListeComparaisonCommuneExcel;
import ch.globaz.pegasus.excel.model.PCListeComparaisonCommuneModel;
import ch.globaz.pegasus.excel.model.PCListeLocaliteFromPCModel;
import ch.globaz.queryexec.bridge.jade.SCM;
import ch.globaz.simpleoutputlist.exception.TechnicalException;
import com.google.common.base.Throwables;
import globaz.externe.IPTConstantesExternes;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersManager;
import globaz.pyxis.db.tiers.TITiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

public class PCListeComparaisonCommune extends BProcess implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String LABEL_TITRE_DOCUMENT = "";
    private static final Logger LOG = LoggerFactory.getLogger(PCListeComparaisonCommune.class);
    private static final boolean IS_DIFFERENT = false;
    private LinkedList<String> errors = new LinkedList<>();
    private BSession bsession;
    private transient List<PCListeComparaisonCommuneModel> listCourrier = new ArrayList<>();
    private transient List<PCListeComparaisonCommuneModel> listNotFound = new ArrayList<>();
    private transient Map<String, TIAdresseDataSource> listAddressCache = new LinkedHashMap<>();
    private transient Map<String, TIAdresseDataSource> listAddressCourrierCache = new LinkedHashMap<>();
    private List<PCListeComparaisonCommuneModel> listDroits = new ArrayList<>();
    private String ID_DROIT = "";
    private final String MSG_NOT_FOUND = "NOT FOUND";

    public PCListeComparaisonCommune() {
        // Do nothing
    }

    @Override
    protected void _executeCleanUp() {
        // Do nothing
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            this.initBsession();
            this.setSendCompletionMail(false);
            this.setSendMailOnError(false);
            LOG.info("APListeComparaisonCommune - Start Export");
            listDroits = createSource();
            List<PCListeComparaisonCommuneModel> listModelForExcel = new ArrayList<>();
            Map<String, PCListeLocaliteFromPCModel> listeLocaliteFromPC = createSourceLocalite();
            List<List<PCListeComparaisonCommuneModel>> lists = divide(listDroits, 2000);
            for (List<PCListeComparaisonCommuneModel> subList : lists) {
                listModelForExcel.addAll(searchAndControlCommune(subList, listeLocaliteFromPC));
            }
            listModelForExcel.sort(Comparator.comparing(PCListeComparaisonCommuneModel::getNom));
            listCourrier.sort(Comparator.comparing(PCListeComparaisonCommuneModel::getNom));
            listNotFound.sort(Comparator.comparing(PCListeComparaisonCommuneModel::getNom));
            if (!listModelForExcel.isEmpty() || !listCourrier.isEmpty() || !listNotFound.isEmpty()) {
                List<String> fichiers = new ArrayList<>();
                String path = Jade.getInstance().getHomeDir() + "work/";
                JadePublishDocumentInfo docInfoExcel = JadePublishDocumentInfoProvider.newInstance(this);
                docInfoExcel.setPublishDocument(true);
                docInfoExcel.setArchiveDocument(false);
                PCListeComparaisonCommuneExcel listeExcel = new PCListeComparaisonCommuneExcel(this.getSession());
                listeExcel.setListSheetNormal(listModelForExcel);
                listeExcel.setListSheetCourrier(listCourrier);
                listeExcel.setListSheetNotFound(listNotFound);
                listeExcel.creerDocument();
                fichiers.add(listeExcel.getOutputFile(path));
                JadeSmtpClient.getInstance().sendMail(this.getEMailAddress(), this.getEMailObject(), this.getBodyMessage(), JadeConversionUtil.toStringArray(fichiers));
            } else {
                JadeSmtpClient.getInstance().sendMail(this.getEMailAddress(), this.getEMailObject() + " - Aucune donnée", "Aucune donnée trouvée", new String[0]);
            }
        } catch (Exception var12) {
            this.setReturnCode(-1);
            this.errors.add("erreur fatale : " + Throwables.getStackTraceAsString(var12));
            throw new TechnicalException("Erreur lors de la liste de comparaison de commune", var12);
        } finally {
            this.closeBsession();
        }
        return false;
    }


    private List<List<PCListeComparaisonCommuneModel>> divide(List<PCListeComparaisonCommuneModel> listIdsDroits, int limit) {
        List<List<PCListeComparaisonCommuneModel>> list = new LinkedList<>();

        List<PCListeComparaisonCommuneModel> listSub = new LinkedList<>();
        for (int i = 0; i < listIdsDroits.size(); i++) {
            if (i % limit != 0) {
                listSub.add(listIdsDroits.get(i));
            } else {
                if (!listSub.isEmpty()) {
                    list.add(listSub);
                }
                listSub = new LinkedList<>();
                listSub.add(listIdsDroits.get(i));
            }
        }
        list.add(listSub);
        return list;
    }

    private String getBodyMessage() {
        StringBuilder message = new StringBuilder();
        message.append("Liste de contrôle des communes" + System.lineSeparator() + System.lineSeparator());
        JADate date = JACalendar.today();
        message.append(System.lineSeparator() + System.lineSeparator());
        message.append("Extraction du : " + date.getDay() + "." + String.format("%02d", date.getMonth()) + "." + date.getYear());
        message.append(System.lineSeparator() + System.lineSeparator());
        message.append("Remarques : Si les champs du requérants ne sont pas remplis alors la personne cité est un requérant");
        return message.toString();
    }

    private List<PCListeComparaisonCommuneModel> searchAndControlCommune(List<PCListeComparaisonCommuneModel> listDroits, Map<String, PCListeLocaliteFromPCModel> listeLocaliteCached) throws Exception {
        List<PCListeComparaisonCommuneModel> list;
        List<PCListeComparaisonCommuneModel> listDataCompleted = new LinkedList<>();
        TIAdresseDataSource adresseFromTiers = null;
        PCListeLocaliteFromPCModel localiteCached;

        for (PCListeComparaisonCommuneModel model : listDroits) {
            if (listeLocaliteCached.containsKey(model.getIdLocaliteFromPc()) && !JadeStringUtil.isBlankOrZero(model.getIdLocaliteFromPc())) {
                localiteCached = listeLocaliteCached.get(model.getIdLocaliteFromPc());
                model.setNomLocaliteFromPC(localiteCached.getNomLocalite());
            } else {
                model.setIdLocaliteFromPc("");
                model.setNomLocaliteFromPC("");
            }
            if (model.getCsRoleFamille().equals(RoleMembreFamille.REQUERANT.getValue())) {
                model.setNomRequerant("");
                model.setIdLocaliteFromRequerant("");
                model.setNssRequerant("");
                model.setRequerant(true);
            }
            if (listeLocaliteCached.containsKey(model.getIdLocaliteFromTiers()) && !JadeStringUtil.isBlankOrZero(model.getIdLocaliteFromTiers())) {
                localiteCached = listeLocaliteCached.get(model.getIdLocaliteFromTiers());
                model.setNomLocaliteFromTiers(localiteCached.getNomLocalite());
                listDataCompleted.add(model);
            } else {
                if (listAddressCache.containsKey(model.getIdTiers())) {
                    adresseFromTiers = listAddressCache.get(model.getIdTiers());
                    if (!JadeStringUtil.isBlankOrZero(adresseFromTiers.localiteId)) {
                        model.setIdLocaliteFromTiers(adresseFromTiers.localiteId);
                        model.setNomLocaliteFromTiers(adresseFromTiers.localiteNpa + "" + (adresseFromTiers.localiteNpaSup.equals("0") ? "00" : adresseFromTiers.localiteNpaSup) + " " + adresseFromTiers.localiteNom);
                        listDataCompleted.add(model);
                    } else {
                        model.setIdLocaliteFromTiers(MSG_NOT_FOUND);
                        model.setNomLocaliteFromTiers(MSG_NOT_FOUND);
                        model.setRemarque("Aucunes adresses (domicile et courrier) dans la gestion des tiers");
                        listNotFound.add(model);
                    }
                } else {
                    if (listAddressCourrierCache.containsKey(model.getIdTiers())) {
                        adresseFromTiers = listAddressCourrierCache.get(model.getIdTiers());
                        model.setIdLocaliteFromTiers(adresseFromTiers.localiteId);
                        model.setNomLocaliteFromTiers(adresseFromTiers.localiteNpa + "" + (adresseFromTiers.localiteNpaSup.equals("0") ? "00" : adresseFromTiers.localiteNpaSup) + " " + adresseFromTiers.localiteNom);
                        model.setRemarque("Adresse de domicile non trouvable mais Adresse de courrier trouvé.");
                        listCourrier.add(model);
                        break;
                    } else {
                        TITiersManager tiersManager = new TITiersManager();
                        tiersManager.setSession(this.getSession());
                        tiersManager.setForIdTiers(model.getIdTiers());
                        tiersManager.find(0);
                        TITiers tiersPersonne = (TITiers) tiersManager.getFirstEntity();
                        adresseFromTiers = tiersPersonne.getAdresseAsDataSource(IPTConstantesExternes.TIERS_ADRESSE_TYPE_COURRIER, IConstantes.CS_APPLICATION_DEFAUT, JACalendar.todayJJsMMsAAAA(), true);
                        if (adresseFromTiers != null) {
                            model.setIdLocaliteFromTiers(adresseFromTiers.localiteId);
                            model.setNomLocaliteFromTiers(adresseFromTiers.localiteNpa + "" + (adresseFromTiers.localiteNpaSup.equals("0") ? "00" : adresseFromTiers.localiteNpaSup) + " " + adresseFromTiers.localiteNom);
                            model.setRemarque("Adresse de domicile non trouvable mais Adresse de courrier trouvé.");
                            listCourrier.add(model);
                            listAddressCourrierCache.put(tiersPersonne.getIdTiers(), adresseFromTiers);
                            continue;
                        } else {
                            model.setIdLocaliteFromTiers(MSG_NOT_FOUND);
                            model.setNomLocaliteFromTiers(MSG_NOT_FOUND);
                            model.setRemarque("Aucunes adresses (domicile et courrier) dans la gestion des tiers");
                            listAddressCache.put(tiersPersonne.getIdTiers(), new TIAdresseDataSource());
                            listNotFound.add(model);
                            continue;
                        }
                    }
                }
            }


        }
        list = controlCommune(listDataCompleted);
        return list;
    }

    private List<PCListeComparaisonCommuneModel> controlCommune(List<PCListeComparaisonCommuneModel> listAControler) throws Exception {
        List<PCListeComparaisonCommuneModel> list = new LinkedList<>();
        try {
            for (PCListeComparaisonCommuneModel modelAControler : listAControler) {
                if (modelAControler.getIdLocaliteFromPc().equals(modelAControler.getIdLocaliteFromTiers()) == IS_DIFFERENT) {
                    list.add(modelAControler);
                }
            }
            list.sort(Comparator.comparing(PCListeComparaisonCommuneModel::getNom));
        } catch (Exception e) {
            throw new Exception("Problème de comparaison : " + e.getMessage());
        }
        return list;
    }

    @Override
    protected String getEMailObject() {
        return "List de contrôle des communes";
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;

    }

    private List<PCListeComparaisonCommuneModel> createSource() {
        List<PCListeComparaisonCommuneModel> list;
        list = SCM.newInstance(PCListeComparaisonCommuneModel.class).session(getSession()).query(getSQL()).execute();
        return list;
    }

    private Map<String, PCListeLocaliteFromPCModel> createSourceLocalite() {
        List<PCListeLocaliteFromPCModel> list;
        list = SCM.newInstance(PCListeLocaliteFromPCModel.class).session(getSession()).query(getSQLLocalite()).execute();
        Map<String, PCListeLocaliteFromPCModel> map = new LinkedHashMap<>();
        for (PCListeLocaliteFromPCModel model : list) {
            if (model.getIdLocalite() == null) {
                model.setIdLocalite("");
            }
            map.put(model.getIdLocalite(), model);
        }
        return map;
    }

    private String getSQL() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "droits.BCIDRO AS ID_DROIT,\n" +
                "membres.WGITIE AS ID_TIERS,\n" +
                "droitMembresPC.BEIDOP AS ID_DONNEE_PERSONNE,\n" +
                "noAVS.HXNAVS AS NSS,\n" +
                "CONCAT (CONCAT (TRIM(tiers.HTLDE1),' '),TRIM(tiers.HTLDE2)) AS NOM,\n" +
                "donneePers.BFIDDL AS ID_LOCALITE_FROM_PC,\n" +
                "adressesBenef.HJILOC AS ID_LOCALITE_FROM_TIERS,\n" +
                "noAVSRequerant.HXNAVS AS NSS_REQUERANT,\n" +
                "CONCAT (CONCAT (TRIM(tiersRequerant.HTLDE1),' '),TRIM(tiersRequerant.HTLDE2)) AS NOM_REQUERANT,\n" +
                "donneePersRequerant.BFIDDL AS ID_LOCALITE_FROM_REQUERANT,\n" +
                "droitMembresPC.BETROF AS CS_ROLE_FAMILLE\n" +
                "FROM SCHEMA.PCDOSSI dossiers\n" +
                "INNER JOIN SCHEMA.PCDEMPC demandesPC ON ( demandesPC.BBIDOS=dossiers.BAIDOS )\n" +
                "LEFT OUTER JOIN SCHEMA.PCDROIT droits ON ( droits.BCIDPC=demandesPC.BBIDPC)\n" +
                "LEFT JOIN SCHEMA.PCDRMBRFA droitMembresPC ON (droitMembresPC.BEIDRO = droits.BCIDRO  ) \n" +
                "LEFT JOIN SCHEMA.SFMBRFAM membres ON ( droitMembresPC.BEIMEF=membres.WGIMEF )\n" +
                "LEFT  JOIN SCHEMA.PCDONPERS donneePers ON\n" +
                "(\n" +
                "   donneePers.BFIDOP=droitMembresPC.BEIDOP\n" +
                ")\n" +
                "LEFT JOIN SCHEMA.PCDRMBRFA droitMembresPCRequerant ON (droitMembresPCRequerant.BEIDRO = droits.BCIDRO AND droitMembresPCRequerant.BETROF=64004001  ) \n" +
                "LEFT JOIN SCHEMA.SFMBRFAM membresRequerant ON ( droitMembresPCRequerant.BEIMEF=membresRequerant.WGIMEF )\n" +
                "LEFT OUTER JOIN SCHEMA.PCDONPERS donneePersRequerant ON\n" +
                "(\n" +
                "   donneePersRequerant.BFIDOP=droitMembresPCRequerant.BEIDOP\n" +
                ")\n" +
                "LEFT JOIN SCHEMA.TIAADRP adressesCourrier ON (membres.WGITIE = adressesCourrier.HTITIE AND adressesCourrier.HETTAD = 508008 AND (adressesCourrier.HEDDAD = (SELECT MAX(adressesCourrier2.HEDDAD) FROM SCHEMA.TIAADRP adressesCourrier2  WHERE  adressesCourrier2.HTITIE = membres.WGITIE AND adressesCourrier2.HETTAD = 508008  AND adressesCourrier2.HEDEFA>=\'20200101\'  ) OR adressesCourrier.HEDFAD = 0) ) \n" +
                "LEFT JOIN SCHEMA.TIADREP adressesBenef on (adressesCourrier.HAIADR = adressesBenef.HAIADR )\n" +
                "LEFT  JOIN SCHEMA.TIPAVSP noAVS ON ( membres.WGITIE=noAVS.HTITIE )\n" +
                "LEFT  JOIN SCHEMA.TITIERP tiers ON ( noAVS.HTITIE=tiers.HTITIE )\n" +
                "LEFT  JOIN SCHEMA.TIPAVSP noAVSRequerant ON ( membresRequerant.WGITIE=noAVSRequerant.HTITIE )\n" +
                "LEFT  JOIN SCHEMA.TITIERP tiersRequerant ON ( noAVSRequerant.HTITIE=tiersRequerant.HTITIE )\n" +
                "WHERE (demandesPC.BBDFIN = 0 OR demandesPC.BBTETD='64001003')\n" +
                "AND droits.BCIDRO IS NOT NULL\n" +
                "AND (donneePers.BFIDDL <>adressesBenef.HJILOC OR adressesBenef.HJILOC IS NULL)  AND membres.WGITIE IS NOT NULL");
        if (ID_DROIT.length() > 0) {
            sql.append(" AND droits.BCIDRO = " + ID_DROIT);
        }
        return sql.toString();
    }

    private String getSQLLocalite() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "PCDRMBRFA1.BEIDOP AS ID_DONNEE_PERS,\n" +
                "CASE WHEN PCDONPERS1.BFIDDL IS NULL THEN 0 ELSE PCDONPERS1.BFIDDL END AS ID_LOCALITE, \n" +
                "CASE WHEN TRIM(CONCAT(CONCAT(TRIM(TILOCAP1.HJNPA),' '),TILOCAP1.HJLOCA)) IS NULL THEN '' ELSE TRIM(CONCAT(CONCAT(TRIM(TILOCAP1.HJNPA),' '),TILOCAP1.HJLOCA)) END  AS NOM_LOCALITE\n" +
                "FROM schema.PCDONPERS PCDONPERS1\n" +
                "LEFT OUTER JOIN schema.TILOCAP TILOCAP1 ON\n" +
                "(\n" +
                "   PCDONPERS1.BFIDDL=TILOCAP1.HJILOC\n" +
                ")\n" +
                "LEFT OUTER JOIN schema.PCDRMBRFA PCDRMBRFA1 ON\n" +
                "(\n" +
                "   PCDONPERS1.BFIDOP=PCDRMBRFA1.BEIDOP\n" +
                ")\n" +
                "WHERE BEIDOP IS NOT NULL ");
        return sql.toString();
    }


    private void initBsession() throws Exception {
        this.bsession = this.getSession();
        BSessionUtil.initContext(this.bsession, this);
    }

    private void closeBsession() {
        BSessionUtil.stopUsingContext(this);
    }
}
