package globaz.apg.process;

import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.simpleoutputlist.exception.TechnicalException;
import com.google.common.base.Throwables;
import globaz.apg.db.liste.APListePandemieControleComptaOFASModel;
import globaz.apg.db.liste.APListePandemieControleOFASModel;
import globaz.apg.excel.APListePandemieControleOFASExcel;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.*;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.zip.JadeZipUtil;
import globaz.jade.common.Jade;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.jade.smtp.JadeSmtpClient;

import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersManager;
import globaz.webavs.common.CommonProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class APListePandemieControleOFAS extends BProcess {
    public static final String LABEL_TITRE_DOCUMENT = "";
    private static final Logger LOG = LoggerFactory.getLogger(APListePandemieControleOFAS.class);
    private LinkedList<String> errors = new LinkedList();
    private LinkedList<String> infos = new LinkedList();
    private BSession bsession;
    List<APListePandemieControleComptaOFASModel> listEchecComptaToAPG = new ArrayList<>();
    List<APListePandemieControleOFASModel> listEchecAPGtoCompta = new ArrayList<>();
    private Map<String, List> mapList = new HashMap<>();
    private static String CS_DOMAINE_PANDEMIE = "19150100";
    private String numCaisse = "";
    private String nomCaisse = "";

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {

            initBsession();
            // Pour ne pas envoyer de double mail, un mail d'erreur de validation est déjà généré
            this.setSendCompletionMail(false);
            this.setSendMailOnError(false);
            LOG.info("ImportAPGPandemie - Start Import - Demandes APG Covid19");
            // création de la list
            JadePublishDocumentInfo docInfo = new JadePublishDocumentInfo();
            nomCaisse = FWIImportProperties.getInstance().getProperty(docInfo,
                    ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase());
            numCaisse = JadePropertiesService.getInstance().getProperty("common.noCaisseFormate");
            List<APListePandemieControleOFASModel> list = createSource();
            if (!list.isEmpty()) {
                List<String> fichiers = new ArrayList<>();
                String path = Jade.getInstance().getHomeDir() + "work/";
                JadePublishDocumentInfo docInfoExcel = JadePublishDocumentInfoProvider.newInstance(this);
                // TODO Changer numéro de document par la suite
                docInfoExcel.setDocumentTypeNumber("XXXXPAP");
                docInfoExcel.setPublishDocument(true);
                docInfoExcel.setArchiveDocument(false);
                APListePandemieControleOFASExcel listeExcel = new APListePandemieControleOFASExcel(getSession());
                listeExcel.setListeSheet(list);
                listeExcel.creerDocument();
                fichiers.add(listeExcel.getOutputFile(path, numCaisse));
//                if (!listEchecComptaToAPG.isEmpty()) {
//                    String fileCSV = createRejectComptaToAPG(path);
//                    fichiers.add(fileCSV);
//                }
                if(!listEchecAPGtoCompta.isEmpty()){
                    String fileCSV =createRejectAPGToCompta(path);
                    fichiers.add(fileCSV);
                }
                JadeSmtpClient.getInstance().sendMail(getEMailAddress(), getEMailObject(), "Liste de contrôle OFAS", JadeConversionUtil.toStringArray(fichiers));
            }

        } catch (Exception e) {
            setReturnCode(-1);
            errors.add("erreur fatale : " + Throwables.getStackTraceAsString(e));
            throw new TechnicalException("Erreur lors de la liste OFAS", e);
        } finally {
            closeBsession();
        }
        LOG.info("ImportAPGPandemie - END PROCESS");
        return true;
    }

    private String createRejectComptaToAPG(String path) throws IOException {
        Files.deleteIfExists(Paths.get(path + "listRejectComptaToAPG.csv"));
        File outputCSV = new File(path + "listRejectComptaToAPG.csv");
        outputCSV.delete();
        if (!listEchecComptaToAPG.isEmpty()) {
            try (PrintWriter pw = new PrintWriter(outputCSV)) {
                listEchecComptaToAPG.stream()
                        .map((APListePandemieControleComptaOFASModel data) -> data.getLineCSV())
                        .forEach(pw::println);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw e;
            }

        }
        return outputCSV.getAbsolutePath();
    }

    private String createRejectAPGToCompta(String path) throws IOException {
        Files.deleteIfExists(Paths.get(path + "listRejectAPGToCompta.csv"));
        File outputCSV = new File(path + "listRejectAPGToCompta.csv");
        outputCSV.delete();
        if (!listEchecAPGtoCompta.isEmpty()) {
            try (PrintWriter pw = new PrintWriter(outputCSV)) {
                listEchecAPGtoCompta.stream()
                        .map((APListePandemieControleOFASModel data) -> data.getLineCSV())
                        .forEach(pw::println);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw e;
            }

        }
        return outputCSV.getAbsolutePath();
    }

    private List<APListePandemieControleOFASModel> createSource() {
        List<APListePandemieControleOFASModel> list = new ArrayList<>();
        List<APListePandemieControleComptaOFASModel> listCompta = new ArrayList<>();
        list = QueryExecutor.execute(getSQL(), APListePandemieControleOFASModel.class, getSession());
        listCompta = QueryExecutor.execute(getSQLCompta(), APListePandemieControleComptaOFASModel.class, getSession());
        list = mappingCompta(list, listCompta);
        list = updateAdresses(list);
        return list;
    }

    private List<APListePandemieControleOFASModel> mappingCompta(List<APListePandemieControleOFASModel> list, List<APListePandemieControleComptaOFASModel> listCompta) {
        List<APListePandemieControleOFASModel> listFinal = new ArrayList<>();

        Map<String, List<APListePandemieControleOFASModel>> mapListTemp = new HashMap<>();
        Map<String, APListePandemieControleOFASModel> mapList = new HashMap<>();
        Map<String, FWCurrency> mapKeyToSomme = new HashMap<>();
        Map<String, APListePandemieControleComptaOFASModel> mapListCompta = new HashMap<>();
        List<APListePandemieControleOFASModel> listAPG = new ArrayList<>();
        List<APListePandemieControleComptaOFASModel> listComptaControl = new ArrayList<>();
        APListePandemieControleOFASModel modelAAjouter;
        APListePandemieControleOFASModel modelError;
        String date;
        try {
            for (APListePandemieControleOFASModel model : list) {
                if (model.getIdTiers() == null) {
                    System.out.println("ID Tiers null");
                }
                if (model.getDateComptable() == null) {
                    date = "0";
                } else {
                    date = model.getDateComptable();
                }
                if (model.getNumAffilie() == null) {
                    System.out.println("Num Affilie null");
                }
                String keyAPG;
                if (model.getIsVerseEmployeur().equals("1")) {
                    keyAPG = model.getIdTiersAff().trim() + date.trim() + model.getNumAffilie().trim();
                } else {
                    keyAPG = model.getIdTiers().trim() + date.trim() + model.getNss().trim();
                }

                if (!mapListTemp.containsKey(keyAPG)) {
                    mapListTemp.put(keyAPG, new ArrayList<>());
                }
                listAPG = mapListTemp.get(keyAPG);
                listAPG.add(model);
                mapListTemp.put(keyAPG, listAPG);
            }
        } catch (Exception e) {
            throw e;
        }
        for (String oldKey : mapListTemp.keySet()) {
            listAPG = mapListTemp.get(oldKey);
            FWCurrency sommeTotal = new FWCurrency("0");
            for (APListePandemieControleOFASModel model : listAPG) {
                sommeTotal.add(model.getMontantNet());
            }
            for (APListePandemieControleOFASModel model : listAPG) {
                if (new FWCurrency(model.getMontantNet()).isPositive()) {
                    mapList.put(oldKey + sommeTotal.getBigDecimalValue(), model);
                } else {
                    mapList.put(oldKey + "-" + sommeTotal.getBigDecimalValue(), model);
                }
            }

        }
        try {
            for (APListePandemieControleComptaOFASModel model : listCompta) {
                if (model.getIdTiers() == null) {
                    System.out.println("ID Tiers null");
                }
                if (model.getDateOp() == null) {
                    date = "0";
                } else {
                    date = model.getDateOp();
                }
                if (model.getNumAffilie() == null) {
                    System.out.println("Num Affilie null");
                }
                String keyCompta;

                if ((model.getMontantis() != null && new FWCurrency(model.getMontantis()).isPositive()) || model.getMontantis().isEmpty()) {
                    keyCompta = model.getIdTiers().trim() + date.trim() + model.getNumAffilie().trim() + model.getMontant();
                } else {
                    keyCompta = model.getIdTiers().trim() + date.trim() + model.getNumAffilie().trim() + "-" + model.getMontant();

                }
                mapListCompta.put(keyCompta, model);
            }
        } catch (Exception e) {
            throw e;
        }
        for (String keyAPG : mapList.keySet()) {
            if (mapListCompta.containsKey(keyAPG)) {
                modelAAjouter = mapList.get(keyAPG);
                APListePandemieControleComptaOFASModel modelCompta = mapListCompta.get(keyAPG);
                if (modelCompta.getDateTransmission().isEmpty() ||modelCompta.getDateTransmission() ==null ) {
                    modelAAjouter.setDatePaiement("");
                } else {
                    ch.globaz.common.domaine.Date d = new ch.globaz.common.domaine.Date(modelCompta.getDateTransmission());
                    modelAAjouter.setDatePaiement(d.getSwissValue());
                }
                modelAAjouter.setIBANBenef(modelCompta.getIban());
                if (modelCompta.getMontantis() == null) {
                    modelAAjouter.setDeductionSource("");
                } else {
                    modelAAjouter.setDeductionSource(modelCompta.getMontantis());
                }
                ch.globaz.common.domaine.Date d = new ch.globaz.common.domaine.Date(modelAAjouter.getDateComptable());
                modelAAjouter.setDateComptable(d.getSwissValue());
                modelAAjouter.setNumCaisse(numCaisse);
                modelAAjouter.setNomCaisse(nomCaisse);
                listFinal.add(modelAAjouter);
            } else {
                listEchecAPGtoCompta.add(mapList.get(keyAPG));
            }
        }
        return listFinal;
    }

    private List<APListePandemieControleOFASModel> updateAdresses(List<APListePandemieControleOFASModel> list) {
        List<APListePandemieControleOFASModel> listFinal = new ArrayList<>();
        try {
            int i = 0;
            this.setProgressScaleValue(list.size());
            this.setProcessDescription("Update Adresss");
            this.setProgressCounter(0);
            for (APListePandemieControleOFASModel model : list) {
                String idTiers = model.getIdTiers();
                String idTiersEmployeur;
                if (model.getIdTiersAff().isEmpty()) {
                    idTiersEmployeur = idTiers;
                } else {
                    idTiersEmployeur = model.getIdTiersAff();
                }


                TIAdresseDataSource adresseEmployeur = null;
                TIAdresseDataSource adresse = null;

                TITiersManager tiersManagerEmployeur = new TITiersManager();
                tiersManagerEmployeur.setSession(getSession());
                tiersManagerEmployeur.setForIdTiers(idTiersEmployeur);
                tiersManagerEmployeur.find(BManager.SIZE_NOLIMIT);
                TITiers tiersEmployeur = (TITiers) tiersManagerEmployeur.getFirstEntity();


                TITiersManager tiersManager = new TITiersManager();
                tiersManager.setSession(getSession());
                tiersManager.setForIdTiers(idTiers);
                tiersManager.find(BManager.SIZE_NOLIMIT);
                TITiers tiers = (TITiers) tiersManager.getFirstEntity();

                if (Objects.nonNull(tiersEmployeur)) {
                    adresseEmployeur = tiersEmployeur.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, CS_DOMAINE_PANDEMIE, JACalendar.todayJJsMMsAAAA(), true);
                    model.setAdresseNomProf(tiersEmployeur.getNomPrenom());
                    if (Objects.nonNull(adresseEmployeur)) {
                        model.setRueProf(adresseEmployeur.rue + " " + adresseEmployeur.numeroRue);
                        model.setNpaProf(adresseEmployeur.localiteNpa);
                        model.setLocaliteProf(adresseEmployeur.localiteNom);
                    }
                }
                if (Objects.nonNull(tiers)) {
                    adresse = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, CS_DOMAINE_PANDEMIE, JACalendar.todayJJsMMsAAAA(), true);
                    model.setAdresseNomDom(tiers.getNomPrenom());
                    if (Objects.nonNull(adresse)) {
                        model.setRueDom(adresse.rue + " " + adresse.numeroRue);
                        model.setNpaDom(adresse.localiteNpa);
                        model.setLocaliteDom(adresse.localiteNom);
                    }
                }
                listFinal.add(model);
                this.setProgressCounter(i++);
            }
        } catch (Exception e) {
            LOG.error("Impossible de récupérer la properties APProperties.DOMAINE_ADRESSE_APG_PANDEMIE : ", e);
        }
        return listFinal;
    }

    private String getSQL() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT TRIM(REPLACE(hxnavs,'.','')) as NSS,\n" +
                "trim(htlde1) as NOM,\n" +
                "trim(htlde2) as PRENOM,\n" +
                "libelleGenre.PCOLUT as GENRESERVICE,\n" +
                "case WHEN vhddeb='0' or vhddeb IS NULL THEN '00.00.0000' ELSE substr(char(vhddeb),7,2) CONCAT '.' CONCAT substr(char(vhddeb),5,2) CONCAT '.' CONCAT substr(char(vhddeb),1,4) end as DE,\n" +
                "case WHEN vhdfin='0' or vhdfin IS NULL THEN '00.00.0000' ELSE substr(char(vhdfin),7,2) CONCAT '.' CONCAT substr(char(vhdfin),5,2) CONCAT '.' CONCAT substr(char(vhdfin),1,4) end as A,\n" +
                "vhnnjs as NBREJOURS,\n" +
                "SUM(vhmtjr) as MONTANTJOURNALIER,\n" +
                "SUM(VHDMOB) as MONTANTBRUT,\n" +
                "SUM(VIMMON) as MONTANTNET,\n" +
                "vilnom as \"BENEFICIAIRE\",\n" +
                "avs.htitie AS IDTIERS,\n" +
                "affiliation.MALNAF as NUMAFFILIE,\n" +
                "affiliation.MAIAFF as IDAFF,\n" +
                "affiliation.HTITIE as IDTIERSAFF,\n" +
                "VMDCOM AS DATECOMPTABLE,\n" +
                "situ_prof.VFBVEM as ISVERSEEMPLOYEUR\n" +
                "FROM SCHEMA.apdroip as dr1\n" +
                "inner join SCHEMA.prdemap as dem1 on dr1.vaidem = dem1.waidem\n" +
                "inner join SCHEMA.tipavsp as avs on dem1.waitie = avs.htitie\n" +
                "inner join SCHEMA.titierp as tie on dem1.waitie = tie.htitie\n" +
                "inner join SCHEMA.appresp as pre on dr1.vaidro = pre.vhidro\n" +
                "inner join SCHEMA.aprepap as rep on pre.vhiprs = rep.viipra\n" +
                "inner join SCHEMA.APLOTSP as lot on lot.VMILOT = pre.VHILOT\n" +
                "INNER JOIN SCHEMA.fwcoup as libelleGenre on libelleGenre.PCOSID = vatgse AND libelleGenre.PLAIDE='F'\n" +
                "LEFT join SCHEMA.apsiprp as situ_prof on rep.viisit = situ_prof.vfisip\n" +
                "LEFT join SCHEMA.apemplp as apg_employeur on situ_prof.vfiemp = apg_employeur.vgiemp\n" +
                "LEFT join SCHEMA.afaffip as affiliation on apg_employeur.vgiaff = affiliation.maiaff\n" +
                "where dr1.vatgse in (52001040,52001041,52001042,52001043,52001044,52001045)  \n" +
                "and lot.VMTETA=52011003  \n" +
//                "AND avs.HTITIE=2128555  \n" +
//                "AND affiliation.HTITIE=1923070 \n" +
                "GROUP BY hxnavs,htlde1,htlde2,libelleGenre.PCOLUT,vhddeb,vhdfin,vhnnjs,vilnom,avs.htitie,affiliation.MALNAF,affiliation.MAIAFF,affiliation.HTITIE,VMDCOM,situ_prof.VFBVEM");
        return sql.toString();
    }

    private String getSQLCompta() {
        StringBuilder sql = new StringBuilder();
        sql.append("select ca.IDTIERS,ca.IDJOURNAL,TRIM(ca.IDEXTERNEROLE) as NUMAFFILIE,og.DATETRANSMISSION,op.DATE as DATEOP,op.MONTANT,op_is.MONTANT as MONTANTIS,TRIM(tiadr.HINCBA) as IBAN from SCHEMA.CACPTAP ca\n" +
                "    inner join SCHEMA.CAOPERP op on ca.IDCOMPTEANNEXE = op.idcompteannexe\n" +
                "    inner join SCHEMA.CAOPOVP ov on op.idoperation = ov.IDORDRE\n" +
                "    left join SCHEMA.CAORGRP og on ov.IDORDREGROUPE = og.IDORDREGROUPE\n" +
                "    LEFT join SCHEMA.CAOPERP op_is on (ca.IDCOMPTEANNEXE = op_is.idcompteannexe AND op.DATE=op_is.DATE AND op_is.IDCOMPTE =(select IDRUBRIQUE from SCHEMA.CARERUP where IDCODEREFERENCE = 237409))\n" +
                "    inner join SCHEMA.CASECTP se on se.IDSECTION = op.IDSECTION\n" +
                "    left join SCHEMA.TIAPAIP tiap on ov.IDADRESSEPAIEMENT = tiap.HCIAIU\n" +
                "    inner join SCHEMA.TIADRPP tiadr on tiap.HIIAPA = tiadr.HIIAPA\n" +
                "    WHERE se.CATEGORIESECTION=227027"+
//                " and op.ETAT=205006 " +
//                "    AND ca.IDTIERS=1923070" +
                "    group by ca.IDTIERS,ca.IDJOURNAL,TRIM(ca.IDEXTERNEROLE),og.DATETRANSMISSION,op.DATE,op.MONTANT,op_is.MONTANT,TRIM(tiadr.HINCBA),ca.IDCOMPTEANNEXE" +
                "");
        return sql.toString();
    }

    @Override
    protected String getEMailObject() {
        return "List de contrôle - OFAS";
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    private void initBsession() throws Exception {
//        bsession = (BSession) GlobazServer.getCurrentSystem()
//                .getApplication(APApplication.DEFAULT_APPLICATION_APG)
//                .newSession(getUsername(), getPassword());
        bsession = getSession();
        BSessionUtil.initContext(bsession, this);
    }

    private void closeBsession() {
        BSessionUtil.stopUsingContext(this);
    }

}
