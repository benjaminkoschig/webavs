package globaz.apg.process;

import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Periode;
import ch.globaz.common.sql.QueryExecutor;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.db.liste.APListePandemieControleV2Model;
import globaz.apg.db.liste.APListePandemieControleModel;
import globaz.apg.db.liste.APListePandemieSuiviDossierModel;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.excel.APListePandemieControleExcel;
import globaz.apg.excel.APListePandemieSuiviDossierExcel;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.common.Jade;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.jade.smtp.JadeSmtpClient;

import java.util.*;

public class APListePandemieControleProcess extends BProcess {
    public final static String LABEL_TITRE_DOCUMENT = "DOC_PANDEMIE_LISTE_CONTROLE";
    public final static String LABEL_TITRE_SHEET_DOCUMENT = "DOC_LISTE_DOUBLONS_PAIEMENT";
    public final static String LABEL_TITRE_SHEET_2_DOCUMENT = "DOC_LISTE_RULE_65";
    public final static String LABEL_TITRE_SHEET_3_DOCUMENT = "DOC_LISTE_RULE_66";
    public final static String LABEL_TITRE_SHEET_4_DOCUMENT = "DOC_LISTE_RULE_67";
    public final static String LABEL_TITRE_SHEET_5_DOCUMENT = "DOC_LISTE_MONTANT_SUP_196";
    public final static String LABEL_TITRE_SHEET_6_DOCUMENT = "DOC_LISTE_MULTI_SITUATION_PROF";
    public final static String LABEL_TITRE_SHEET_7_DOCUMENT = "DOC_LISTE_DOUBLE_DEMANDE";
    public final static String LABEL_TITRE_SHEET_8_DOCUMENT = "DOC_LISTE_GARDE_IND_SUP_30";
    public final static String LABEL_TITRE_SHEET_9_DOCUMENT = "DOC_LISTE_JOURS_CARENCE_3";

    public final static String NUM_INFOROM = "6000PAP";
    private String eMailObject = "";
    private Map<String, List> mapList = new HashMap<>();
    private List<APListePandemieSuiviDossierModel> listSuivi;

    public APListePandemieControleProcess() {
        super();
    }

    public APListePandemieControleProcess(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            setSendMailOnError(true);
            setSendCompletionMail(false);
            //ADD LISTS
            List list = createSource();
            mapList = new LinkedHashMap<>();
            mapList.put(getSession().getLabel(APListePandemieControleProcess.LABEL_TITRE_SHEET_DOCUMENT), list);
            list = createSourceRule65();
            mapList.put(getSession().getLabel(APListePandemieControleProcess.LABEL_TITRE_SHEET_2_DOCUMENT), list);
            list = createSourceRule66();
            mapList.put(getSession().getLabel(APListePandemieControleProcess.LABEL_TITRE_SHEET_3_DOCUMENT), list);
            list = createSourceRule67();
            mapList.put(getSession().getLabel(APListePandemieControleProcess.LABEL_TITRE_SHEET_4_DOCUMENT), list);
            list = createSourceMontantJournalierSup();
            mapList.put(getSession().getLabel(APListePandemieControleProcess.LABEL_TITRE_SHEET_5_DOCUMENT), list);
            list = createSourceMultiSituationProf();
            mapList.put(getSession().getLabel(APListePandemieControleProcess.LABEL_TITRE_SHEET_6_DOCUMENT), list);
            list = createSourceDoubleDemande();
            mapList.put(getSession().getLabel(APListePandemieControleProcess.LABEL_TITRE_SHEET_7_DOCUMENT), list);
            list = createSourceGardeParentaleIndep30Sup();
            mapList.put(getSession().getLabel(APListePandemieControleProcess.LABEL_TITRE_SHEET_8_DOCUMENT), list);
            list = createSourceJoursCarence();
            mapList.put(getSession().getLabel(APListePandemieControleProcess.LABEL_TITRE_SHEET_9_DOCUMENT), list);


            listSuivi = createSourceSuiviDossier();

            if (!checkEmptyList(mapList)) {
                List<String> fichierXLS = new ArrayList<>();
                String path = Jade.getInstance().getHomeDir()+"work/";
                JadePublishDocumentInfo docInfoExcel = JadePublishDocumentInfoProvider.newInstance(this);
                docInfoExcel.setDocumentTypeNumber("5047PAP");
                docInfoExcel.setPublishDocument(true);
                docInfoExcel.setArchiveDocument(false);
                APListePandemieControleExcel listeExcel = new APListePandemieControleExcel(getSession());
                listeExcel.setMapListes(mapList);
                listeExcel.creerDocument();
                fichierXLS.add(listeExcel.getOutputFile(path+docInfoExcel.getDocumentTypeNumber()+"_"));
                docInfoExcel = JadePublishDocumentInfoProvider.newInstance(this);
                docInfoExcel.setDocumentTypeNumber("5048PAP");
                docInfoExcel.setPublishDocument(true);
                docInfoExcel.setArchiveDocument(false);
                APListePandemieSuiviDossierExcel listeExcelSuivi = new APListePandemieSuiviDossierExcel(getSession());
                listeExcelSuivi.setListSuivi(listSuivi);
                listeExcelSuivi.creerDocument();
                fichierXLS.add(listeExcelSuivi.getOutputFile(path+docInfoExcel.getDocumentTypeNumber()+"_"));
                JadeSmtpClient.getInstance().sendMail(getEMailAddress(),getEMailObject(),getSession().getLabel("PANDEMIE_LISTE_CONTROLE_OK_MAIL")+"\n"+getSession().getLabel("PANDEMIE_LISTE_SUIVI_OK_MAIL"), JadeConversionUtil.toStringArray(fichierXLS));
            }
        } catch (Exception e) {
            _addError(getSession().getCurrentThreadTransaction(), e.getMessage());
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, getSession().getLabel("MENU_PANDEMIE_LISTE_CONTROLE"));
            return false;
        }

        return true;
    }

    private List createSourceSuiviDossier() {
        List<APListePandemieSuiviDossierModel> list = new ArrayList<>();
        list = QueryExecutor.execute(getSqlSuiviDossier(), APListePandemieSuiviDossierModel.class, getSession());
        return list;
    }




    private boolean checkEmptyList(Map<String, List> mapList) {
        for (String key : mapList.keySet()) {
            if (!mapList.get(key).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private List<APListePandemieControleModel> createSource() {
        List<APListePandemieControleModel> list = new ArrayList<>();
        list = QueryExecutor.execute(getSql(), APListePandemieControleModel.class, getSession());
        list = triDoublePaiement(list);
        return list;
    }

    private List<APListePandemieControleModel> triDoublePaiement(List<APListePandemieControleModel> list) {
        boolean isToAddFirst = false;

        Map<String,List<APListePandemieControleModel>> mapCas = new HashMap<>();
        Set<APListePandemieControleModel> mapTriParNSS = new LinkedHashSet<>();
        List<APListePandemieControleModel> listModels = new ArrayList<>();
        List<APListePandemieControleModel> listTrie = new ArrayList<>();
        APListePandemieControleModel modelTri;
        for(APListePandemieControleModel model : list){
            if(!mapCas.containsKey(model.getIdTiers())){
                mapCas.put(model.getIdTiers(),new ArrayList<APListePandemieControleModel>());
            }
            listModels = mapCas.get(model.getIdTiers());
            listModels.add(model);
            mapCas.put(model.getIdTiers(),listModels);
        }
        //1er tri : Supprimer les cas où il y'a qu'une seule période.
        mapCas.entrySet().removeIf(e -> e.getValue().size()==1);
        //2ème tri : Checker les chervauchement
        for(String key : mapCas.keySet()){
            listModels = mapCas.get(key);
            Collections.sort(listModels, new Comparator<APListePandemieControleModel>() {
                @Override
                public int compare(APListePandemieControleModel t1, APListePandemieControleModel t2) {
                    Periode p1 = new Periode(t1.getDateDebut(),t1.getDateFin());
                    Periode p2 = new Periode(t2.getDateDebut(),t2.getDateFin());
                    return p1.compareTo(p2);
                }
            });
            mapTriParNSS.clear();
            APListePandemieControleModel model1;
            APListePandemieControleModel model2;
            for(int i=0;i<listModels.size();i++){
                for(int j=i+1;j<listModels.size();j++){
                    model1 = listModels.get(i);
                    model2 = listModels.get(j);
                    if (isChevauchement(model1,model2) && controleTypeAPG(model1,model2)){
                        mapTriParNSS.add(model1);
                        mapTriParNSS.add(model2);
                    }
                }
            }
            listTrie.addAll(mapTriParNSS);

        }

        
        return listTrie;
    }

    private boolean controleTypeAPG(APListePandemieControleModel model1, APListePandemieControleModel model2) {
        /**
         * CAS1 : Contrôle d'un sens
         */
        if(model1.getTypeAPG().equals(APTypeDePrestation.STANDARD.getCodesystemString()) && model2.getTypeAPG().equals(APTypeDePrestation.ACM_ALFA.getCodesystemString())||
                model1.getTypeAPG().equals(APTypeDePrestation.STANDARD.getCodesystemString()) && model2.getTypeAPG().equals(APTypeDePrestation.ACM2_ALFA.getCodesystemString())||
                model1.getTypeAPG().equals(APTypeDePrestation.STANDARD.getCodesystemString()) && model2.getTypeAPG().equals(APTypeDePrestation.ACM_NE.getCodesystemString())||
                model1.getTypeAPG().equals(APTypeDePrestation.STANDARD.getCodesystemString()) && model2.getTypeAPG().equals(APTypeDePrestation.LAMAT.getCodesystemString())||
                model1.getTypeAPG().equals(APTypeDePrestation.STANDARD.getCodesystemString()) && model2.getTypeAPG().equals(APTypeDePrestation.COMPCIAB.getCodesystemString())||
               model1.getTypeAPG().equals(APTypeDePrestation.ACM_ALFA.getCodesystemString()) && model2.getTypeAPG().equals(APTypeDePrestation.LAMAT.getCodesystemString()) ||
                model1.getTypeAPG().equals(APTypeDePrestation.ACM_NE.getCodesystemString()) && model2.getTypeAPG().equals(APTypeDePrestation.LAMAT.getCodesystemString())
        ){
            return false;
        }
        /**
         * CAS2 : Contrôle de l'autre sens
         * */
        if(model1.getTypeAPG().equals(APTypeDePrestation.ACM_ALFA.getCodesystemString()) && model2.getTypeAPG().equals(APTypeDePrestation.STANDARD.getCodesystemString())||
                model1.getTypeAPG().equals(APTypeDePrestation.LAMAT.getCodesystemString()) && model2.getTypeAPG().equals(APTypeDePrestation.STANDARD.getCodesystemString())||
                model1.getTypeAPG().equals(APTypeDePrestation.ACM_NE.getCodesystemString()) && model2.getTypeAPG().equals(APTypeDePrestation.STANDARD.getCodesystemString())||
                model1.getTypeAPG().equals(APTypeDePrestation.ACM2_ALFA.getCodesystemString()) && model2.getTypeAPG().equals(APTypeDePrestation.STANDARD.getCodesystemString())||
                model1.getTypeAPG().equals(APTypeDePrestation.COMPCIAB.getCodesystemString()) && model2.getTypeAPG().equals(APTypeDePrestation.STANDARD.getCodesystemString())||
                model1.getTypeAPG().equals(APTypeDePrestation.LAMAT.getCodesystemString()) && model2.getTypeAPG().equals(APTypeDePrestation.ACM_ALFA.getCodesystemString()) ||
                model1.getTypeAPG().equals(APTypeDePrestation.LAMAT.getCodesystemString()) && model2.getTypeAPG().equals(APTypeDePrestation.ACM_NE.getCodesystemString())
        ){
            return false;
        }

        return true;

    }

    private boolean isChevauchement(APListePandemieControleModel model1, APListePandemieControleModel model2) {
            boolean isChevauchement = false;
        boolean isIncluded = false;
        Date dateDebutP1 = new Date(model1.getDateDebut());
        Date dateDebutP2 = new Date(model2.getDateDebut());
        Date dateFinP1;
        Date dateFinP2;
        //Cas 1 : Pas de date de fin des 2 périodes => true
        if((model1.getDateFin().equals("00.00.0000") )
            && model2.getDateFin().equals("00.00.0000") ){
                return true;
        }
        //Cas 2 : Une période a une date de fin
        if((!model1.getDateFin().equals("00.00.0000") )
                && ( model2.getDateFin().equals("00.00.0000") )){
            dateFinP1 = new Date(model1.getDateFin());
            if(dateFinP1.before(dateDebutP2)){
                return false;
            }else{
                return true;
            }
        }
        if(( model1.getDateFin().equals("00.00.0000") )
                && ( !model2.getDateFin().equals("00.00.0000") )) {
            dateFinP2 = new Date(model2.getDateFin());
            if (dateFinP2.before(dateDebutP1)) {
                return false;
            } else {
                return true;
            }
        }
        //Cas 3 : Les 2 périodes ont une date de fin;
        dateFinP1 = new Date(model1.getDateFin());
        dateFinP2 = new Date(model2.getDateFin());
        if (dateFinP1.before(dateDebutP2) || dateFinP2.before(dateDebutP1) ) {
            return false;
        } else {
            return true;
        }
    }

    private List<APListePandemieControleModel> createSourceRule65() {
        List<APListePandemieControleModel> list = new ArrayList<>();
        list = QueryExecutor.execute(getSqlRule65(), APListePandemieControleModel.class, getSession());
        return list;
    }

    private List<APListePandemieControleModel> createSourceRule66() {
        List<APListePandemieControleModel> list = new ArrayList<>();
        list = QueryExecutor.execute(getSqlRule66(), APListePandemieControleModel.class, getSession());
        return list;
    }


    private List<APListePandemieControleModel> createSourceRule67() {
        List<APListePandemieControleModel> list = new ArrayList<>();
        list = QueryExecutor.execute(getSqlRule67(), APListePandemieControleModel.class, getSession());
        return list;
    }

    private List<APListePandemieControleModel> createSourceMontantJournalierSup() {
        List<APListePandemieControleModel> list = new ArrayList<>();
        list = QueryExecutor.execute(getSqlMontantJournalierSup(), APListePandemieControleModel.class, getSession());
        return list;
    }


    private List<APListePandemieControleModel> createSourceDoubleDemande() {
        List<APListePandemieControleModel> list = new ArrayList<>();
        list = QueryExecutor.execute(getSqlSourceDoubleDemande(), APListePandemieControleModel.class, getSession());
        return list;
    }


    private List<APListePandemieControleV2Model> createSourceMultiSituationProf() {
        List<APListePandemieControleV2Model> list = new ArrayList<>();
        list = QueryExecutor.execute(getSqlSourceMultiSituationProf(), APListePandemieControleV2Model.class, getSession());
        return list;
    }

    private List<APListePandemieControleV2Model> createSourceJoursCarence() {
        List<APListePandemieControleV2Model> list = new ArrayList<>();
        list = QueryExecutor.execute(getSqlJoursCarence(), APListePandemieControleV2Model.class, getSession());
        return list;
    }



    private List<APListePandemieControleModel> createSourceGardeParentaleIndep30Sup() {
        List<APListePandemieControleModel> list = new ArrayList<>();
        list = QueryExecutor.execute(getSqlSourceGardeParentaleIndep30Sup(), APListePandemieControleModel.class, getSession());
        list = triGardeParentalIndep30Sup(list);
        return list;
    }

    private List<APListePandemieControleModel> triGardeParentalIndep30Sup(List<APListePandemieControleModel> list) {
        List<APListePandemieControleModel> listTri = new ArrayList<>();
        Map<String,List<APListePandemieControleModel> > mapCas = new HashMap<>();
        List<APListePandemieControleModel> listModels = new ArrayList<>();
        for(APListePandemieControleModel model: list){
            if(!mapCas.containsKey(model.getIdTiers())){
                mapCas.put(model.getIdTiers(),new ArrayList<APListePandemieControleModel>());
            }
            listModels = mapCas.get(model.getIdTiers());
            listModels.add(model);
            mapCas.put(model.getIdTiers(),listModels);
        }
        for(String key : mapCas.keySet()){
            List<APListePandemieControleModel> listPrestation = mapCas.get(key);
            int totalDay = 0;
            for(APListePandemieControleModel model : listPrestation){
                totalDay=totalDay+Integer.valueOf(model.getNbreJours());
            }
            if(totalDay>30){
                listTri.addAll(listPrestation);
            }

        }
        return listTri;
    }

    private String getSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("hxnavs as NSS,");
        sql.append("trim(htlde1) concat ' ' concat trim(htlde2) as NOM, ");
        sql.append("case WHEN vhddeb=\'0\'  or vhddeb IS NULL  THEN \'00.00.0000\' ");
        sql.append("ELSE ");
        sql.append("substr(char(vhddeb),7,2) CONCAT '.' CONCAT substr(char(vhddeb),5,2) CONCAT '.' CONCAT substr(char(vhddeb),1,4) END as \"DATEDEBUT\", ");
        sql.append("case WHEN vhdfin=\'0\'  or vhdfin IS NULL THEN \'00.00.0000\' ");
        sql.append("ELSE ");
        sql.append("substr(char(vhdfin),7,2) CONCAT '.' CONCAT substr(char(vhdfin),5,2) CONCAT '.' CONCAT substr(char(vhdfin),1,4) end as \"DATEFIN\", ");
        sql.append("\'0\' as \"NBREJOURS\",");
        sql.append("vatgse as \"GENRESERVICE\", ");
        sql.append("vateta as \"ETAT\", ");
        sql.append("vaidem as \"IDDEMANDE\", ");
        sql.append("vaidro as \"IDDROIT\", ");
        sql.append("dem1.waitie as \"IDTIERS\", ");
        sql.append("vhtgen as \"TYPEAPG\"");
        sql.append("FROM schema.prdemap  as dem1 ");
        sql.append("inner join schema.tipavsp as tip on dem1.waitie = tip.htitie ");
        sql.append("inner join schema.apdroip as dr1 on dem1.waidem = dr1.vaidem ");
        sql.append("inner join schema.titierp as tie on dem1.waitie = tie.htitie ");
        sql.append("inner join schema.appresp as prest on dr1.vaidro = prest.vhidro ");
        sql.append("where ");
        sql.append("dr1.vateta in (52003007,52003003,52003002) ");
        sql.append("and vhdmob > 0 ");
        sql.append("and (vhddeb > 20200301 OR vhdfin > 20200301 ) ");
        sql.append("and dr1.vaidro not in (select dr1.vaidro from schema.apdroip as dr4 where dr4.vaipar = dr1.vaidro) ");
        sql.append("and prest.vhteta in (52006002,52006003,52006004,52006005) ");
        return sql.toString();
    }

    private String getSqlRule65() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("hxnavs as NSS,");
        sql.append("trim(htlde1) concat ' ' concat trim(htlde2) as NOM, ");
        sql.append("case WHEN vadddr=\'0\' or vadddr IS NULL THEN \'00.00.0000\'   ");
        sql.append("ELSE ");
        sql.append("substr(char(vadddr),7,2) CONCAT '.' CONCAT substr(char(vadddr),5,2) CONCAT '.' CONCAT substr(char(vadddr),1,4) END as \"DATEDEBUT\", ");
        sql.append("case WHEN vaddfd=\'0\' or vaddfd IS NULL THEN \'00.00.0000\'   ");
        sql.append("ELSE ");
        sql.append("substr(char(vaddfd),7,2) CONCAT '.' CONCAT substr(char(vaddfd),5,2) CONCAT '.' CONCAT substr(char(vaddfd),1,4) end as \"DATEFIN\", ");
        sql.append("\'0\' as \"NBREJOURS\",");
        sql.append("vatgse as \"GENRESERVICE\", ");
        sql.append("vateta as \"ETAT\", ");
        sql.append("vaidem as \"IDDEMANDE\", ");
        sql.append("vaidro as \"IDDROIT\", ");
        sql.append("dem1.waitie as \"IDTIERS\" ");
        sql.append("FROM schema.apdroip  as dro ");
        sql.append("inner join schema.prdemap as dem1 on dro.vaidem = dem1.waidem ");
        sql.append("inner join schema.tipavsp as tip on dem1.waitie = tip.htitie ");
        sql.append("inner join schema.apbrules on dro.vaidro = vriddr ");
        sql.append("inner join schema.titierp as tie on dem1.waitie = tie.htitie ");
        sql.append("where dro.vatgse in (52001040,52001041,52001042,52001043,52001044,52001045,52001046)");
        sql.append("and dro.vateta in (52003007,52003003,52003002) ");
        sql.append("and vrbrco = 65 ");
        sql.append("and dro.vaidro not in (select dro.vaidro from schema.apdroip as dr10 where dr10.vaipar = dro.vaidro)");
        return sql.toString();
    }

    private String getSqlRule66() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("hxnavs as NSS,");
        sql.append("trim(htlde1) concat ' ' concat trim(htlde2) as NOM, ");
        sql.append("case WHEN vadddr=\'0\' or vadddr IS NULL THEN \'00.00.0000\'   ");
        sql.append("ELSE ");
        sql.append("substr(char(vadddr),7,2) CONCAT '.' CONCAT substr(char(vadddr),5,2) CONCAT '.' CONCAT substr(char(vadddr),1,4) END as \"DATEDEBUT\", ");
        sql.append("case WHEN vaddfd=\'0\' or vaddfd IS NULL THEN \'00.00.0000\'   ");
        sql.append("ELSE ");
        sql.append("substr(char(vaddfd),7,2) CONCAT '.' CONCAT substr(char(vaddfd),5,2) CONCAT '.' CONCAT substr(char(vaddfd),1,4) end as \"DATEFIN\", ");
        sql.append("\'0\' as \"NBREJOURS\",");
        sql.append("vatgse as \"GENRESERVICE\", ");
        sql.append("vateta as \"ETAT\", ");
        sql.append("vaidem as \"IDDEMANDE\", ");
        sql.append("vaidro as \"IDDROIT\", ");
        sql.append("dem1.waitie as \"IDTIERS\" ");
        sql.append("FROM schema.apdroip  as dro ");
        sql.append("inner join schema.prdemap as dem1 on dro.vaidem = dem1.waidem ");
        sql.append("inner join schema.tipavsp as tip on dem1.waitie = tip.htitie ");
        sql.append("inner join schema.apbrules on dro.vaidro = vriddr ");
        sql.append("inner join schema.titierp as tie on dem1.waitie = tie.htitie ");
        sql.append("where dro.vatgse in (52001040,52001041,52001042,52001043,52001044,52001045,52001046)");
        sql.append("and dro.vateta in (52003007,52003003,52003002) ");
        sql.append("and vrbrco = 66 ");
        sql.append("and dro.vaidro not in (select dro.vaidro from schema.apdroip as dr10 where dr10.vaipar = dro.vaidro)");
        return sql.toString();
    }

    private String getSqlRule67() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("hxnavs as NSS,");
        sql.append("trim(htlde1) concat ' ' concat trim(htlde2) as NOM, ");
        sql.append("case WHEN vadddr=\'0\' or vadddr IS NULL THEN \'00.00.0000\'   ");
        sql.append("ELSE ");
        sql.append("substr(char(vadddr),7,2) CONCAT '.' CONCAT substr(char(vadddr),5,2) CONCAT '.' CONCAT substr(char(vadddr),1,4) END as \"DATEDEBUT\", ");
        sql.append("case WHEN vaddfd=\'0\' or vaddfd IS NULL THEN \'00.00.0000\'   ");
        sql.append("ELSE ");
        sql.append("substr(char(vaddfd),7,2) CONCAT '.' CONCAT substr(char(vaddfd),5,2) CONCAT '.' CONCAT substr(char(vaddfd),1,4) end as \"DATEFIN\", ");
        sql.append("\'0\' as \"NBREJOURS\",");
        sql.append("vatgse as \"GENRESERVICE\", ");
        sql.append("vateta as \"ETAT\", ");
        sql.append("vaidem as \"IDDEMANDE\", ");
        sql.append("vaidro as \"IDDROIT\", ");
        sql.append("dem1.waitie as \"IDTIERS\" ");
        sql.append("FROM schema.apdroip  as dro ");
        sql.append("inner join schema.prdemap as dem1 on dro.vaidem = dem1.waidem ");
        sql.append("inner join schema.tipavsp as tip on dem1.waitie = tip.htitie ");
        sql.append("inner join schema.apbrules on dro.vaidro = vriddr ");
        sql.append("inner join schema.titierp as tie on dem1.waitie = tie.htitie ");
        sql.append("where dro.vatgse in (52001040,52001041,52001042,52001043,52001044,52001045,52001046)");
        sql.append("and dro.vateta in (52003007,52003003,52003002) ");
        sql.append("and vrbrco = 67 ");
        sql.append("and dro.vaidro not in (select dro.vaidro from schema.apdroip as dr10 where dr10.vaipar = dro.vaidro)");
        return sql.toString();
    }


    private String getSqlMontantJournalierSup() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("hxnavs as NSS,");
        sql.append("trim(htlde1) concat ' ' concat trim(htlde2) as NOM, ");
        sql.append("case WHEN vaddep=\'0\' or vaddep IS NULL THEN \'00.00.0000\'   ");
        sql.append("ELSE ");
        sql.append("substr(char(vaddep),7,2) CONCAT '.' CONCAT substr(char(vaddep),5,2) CONCAT '.' CONCAT substr(char(vaddep),1,4) END as \"DATEDEBUT\", ");
        sql.append("case WHEN vaddfd=\'0\'  or vaddfd IS NULL THEN \'00.00.0000\'  ");
        sql.append("ELSE ");
        sql.append("substr(char(vaddfd),7,2) CONCAT '.' CONCAT substr(char(vaddfd),5,2) CONCAT '.' CONCAT substr(char(vaddfd),1,4) end as \"DATEFIN\", ");
        sql.append("\'0\' as \"NBREJOURS\",");
        sql.append("vatgse as \"GENRESERVICE\", ");
        sql.append("vateta as \"ETAT\", ");
        sql.append("vaidem as \"IDDEMANDE\", ");
        sql.append("vaidro as \"IDDROIT\", ");
        sql.append("dem1.waitie as \"IDTIERS\" ");
        sql.append("FROM schema.prdemap as dem1 ");
        sql.append("inner join schema.tipavsp as avs on dem1.waitie = avs.htitie ");
        sql.append("inner join schema.titierp as tie on dem1.waitie = tie.htitie ");
        sql.append("inner join schema.apdroip as dr1 on dem1.waidem = dr1.vaidem ");
        sql.append("inner join schema.appresp on dr1.vaidro = vhidro ");
        sql.append("inner join schema.appandemie on dr1.vaidro = id_droit ");
        sql.append("where vhmtjr > 196.00 and vhtgen = 52015012 ");
        sql.append("and vhdmob > 0 ");
        sql.append("and dr1.vaidro not in (select dr1.vaidro from schema.apdroip as dr4 where dr4.vaipar = dr1.vaidro)");
        return sql.toString();
    }
    private String getSqlSourceDoubleDemande() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("hxnavs as NSS,");
        sql.append("trim(htlde1) concat ' ' concat trim(htlde2) as NOM,");
        sql.append("case WHEN vadddr=\'0\' or vadddr IS NULL THEN \'00.00.0000\'  ");
        sql.append("ELSE ");
        sql.append("substr(char(vadddr), 7, 2) CONCAT '.' CONCAT substr(char(vadddr), 5, 2) CONCAT '.' CONCAT substr(char(vadddr), 1, 4) end  as \"DATEDEBUT\", ");
        sql.append("case WHEN vaddfd=\'0\' or vaddfd IS NULL THEN \'00.00.0000\'  ");
        sql.append("ELSE ");
        sql.append("substr(char(vaddfd), 7, 2) CONCAT '.' CONCAT substr(char(vaddfd), 5, 2) CONCAT '.' CONCAT substr(char(vaddfd), 1, 4) end  as \"DATEFIN\", ");
        sql.append("\'0\' as \"NBREJOURS\",");
        sql.append("vatgse as \"GENRESERVICE\", ");
        sql.append("vateta as \"ETAT\", ");
        sql.append("vaidem as \"IDDEMANDE\", ");
        sql.append("vaidro as \"IDDROIT\", ");
        sql.append("dem1.waitie as \"IDTIERS\" ");
        sql.append("FROM schema.prdemap as dem1 ");
        sql.append("inner join schema.tipavsp as avs on dem1.waitie = avs.htitie ");
        sql.append("inner join schema.titierp as tie on dem1.waitie = tie.htitie ");
        sql.append("inner join schema.apdroip as dr1 on dem1.waidem = dr1.vaidem  ");
        sql.append("where dem1.waitie in (select waitie from (SELECT dem10.waitie, count(*) ");
        sql.append("FROM schema.apdroip as dr2 ");
        sql.append("inner join schema.prdemap as dem10 on dr2.vaidem = dem10.waidem ");
        sql.append("where dr2.vatgse in (52001040,52001041,52001042,52001043,52001044,52001045,52001046) ");
        sql.append("and dem10.wattde = 52201008 ");
        sql.append("and dr2.vaidro not in (select dr2.vaidro from schema.apdroip as dr3 where dr3.vaipar = dr2.vaidro)");
        sql.append("GROUP by dem10.waitie ");
        sql.append("having count(*)> 1))");
        sql.append("and dr1.vatgse in (52001040,52001041,52001042,52001043,52001044,52001045) ");
        sql.append("and dem1.wattde = 52201008 ");
        sql.append("and dr1.vaidro not in (select dr1.vaidro from schema.apdroip as dr4 where dr4.vaipar = dr1.vaidro)");
        return sql.toString();
    }

    private String getSqlSourceMultiSituationProf() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("hxnavs as NSS,");
        sql.append("trim(htlde1) concat ' ' concat trim(htlde2) as NOM,");
        sql.append("case WHEN vhddeb=\'0\'  or vhddeb IS NULL THEN \'00.00.0000\'   ");
        sql.append("ELSE ");
        sql.append("substr(char(vhddeb),7,2) CONCAT '.' CONCAT substr(char(vhddeb),5,2) CONCAT '.' CONCAT substr(char(vhddeb),1,4) end as \"DATEDEBUTPRESTATION\",");
        sql.append("case WHEN vhdfin=\'0\' or vhdfin IS NULL  THEN \'00.00.0000\'   ");
        sql.append("ELSE ");
        sql.append("substr(char(vhdfin),7,2) CONCAT '.' CONCAT substr(char(vhdfin),5,2) CONCAT '.' CONCAT substr(char(vhdfin),1,4) end as \"DATEFINPRESTATION\", ");
        sql.append("vhmtjr as \"MNTJOURNALIER\",");
        sql.append("vhnnjs as \"NBREJOURS\",");
        sql.append("vhdmob as \"MNTPRESTATION\",");
        sql.append("vimmob as \"MNTBRUTREPARTITION\",");
        sql.append("vilnom as \"BENEFPAIEMENT\",");
        sql.append("vatgse as \"GENRESERVICE\",");
        sql.append("vateta as \"ETAT\", ");
        sql.append("case WHEN vadddr=\'0\' or vadddr IS NULL THEN \'00.00.0000\'    ");
        sql.append("ELSE ");
        sql.append("substr(char(vadddr),7,2) CONCAT '.' CONCAT substr(char(vadddr),5,2) CONCAT '.' CONCAT substr(char(vadddr),1,4) end as \"DATEDEBUTDROIT\",");
        sql.append("case WHEN vaddfd=\'0\' or vaddfd IS NULL THEN \'00.00.0000\'     ");
        sql.append("ELSE ");
        sql.append("substr(char(vaddfd),7,2) CONCAT '.' CONCAT substr(char(vaddfd),5,2) CONCAT '.' CONCAT substr(char(vaddfd),1,4) end AS \"DATEFINDROIT\",");
        sql.append("vaidem as \"IDDEMANDE\", ");
        sql.append("vaidro as \"IDDROIT\", ");
        sql.append("dem1.waitie AS IDTIERS, ");
        sql.append("\'0\' AS DATEDEBUT1STPERIODE, ");
        sql.append("\'0\' AS DATEFIN1STPERIODE ");
        sql.append("FROM schema.apdroip as dr1 ");
        sql.append("inner join schema.prdemap as dem1 on dr1.vaidem = dem1.waidem ");
        sql.append("inner join schema.tipavsp as avs on dem1.waitie = avs.htitie ");
        sql.append("inner join schema.titierp as tie on dem1.waitie = tie.htitie ");
        sql.append("inner join schema.appresp as pre on dr1.vaidro = pre.vhidro ");
        sql.append("inner join schema.aprepap as rep on pre.vhiprs = rep.viipra ");
        sql.append("where dr1.vaidro in ");
        sql.append("(select vaidro from ( SELECT dr2.vaidro, count(*) FROM schema.apdroip as dr2 inner join schema.apsiprp on dr2.vaidro = vfidro ");
        sql.append("where dr2.vatgse in (52001040,52001041,52001042,52001043,52001044,52001045,52001046) and dr2.vaidro not in ( select dr2.vaidro from schema.apdroip as dr3 where dr3.vaipar = dr2.vaidro) ");
        sql.append("GROUP by dr2.vaidro having count(*)> 1))");
        sql.append("and dr1.vatgse in (52001040,52001041,52001042,52001043,52001044,52001045,52001046) ");
        sql.append("and dem1.wattde = 52201008 ");
        sql.append("and pre.vhdmob > 0 ");
        sql.append("and dr1.vaidro not in (select dr1.vaidro from schema.apdroip as dr4 where dr4.vaipar = dr1.vaidro) ");
        return sql.toString();
    }
    private String getSqlSourceGardeParentaleIndep30Sup() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("hxnavs as NSS,");
        sql.append("trim(htlde1) concat ' ' concat trim(htlde2) as NOM,");
        sql.append("case WHEN vhddeb=\'0\' or vhddeb IS NULL THEN \'00.00.0000\'  ");
        sql.append("ELSE ");
        sql.append("substr(char(vhddeb), 7, 2) CONCAT '.' CONCAT substr(char(vhddeb), 5, 2) CONCAT '.' CONCAT substr(char(vhddeb), 1, 4) end  as \"DATEDEBUT\", ");
        sql.append("case WHEN vhdfin=\'0\' or vhdfin IS NULL THEN \'00.00.0000\'  ");
        sql.append("ELSE ");
        sql.append("substr(char(vhdfin), 7, 2) CONCAT '.' CONCAT substr(char(vhdfin), 5, 2) CONCAT '.' CONCAT substr(char(vhdfin), 1, 4) end  as \"DATEFIN\", ");
        sql.append("vhnnjs as \"NBREJOURS\",");
        sql.append("vatgse as \"GENRESERVICE\", ");
        sql.append("vateta as \"ETAT\", ");
        sql.append("vaidem as \"IDDEMANDE\", ");
        sql.append("vaidro as \"IDDROIT\", ");
        sql.append("dem1.waitie as \"IDTIERS\" ");
        sql.append("FROM schema.prdemap as dem1 ");
        sql.append("inner join schema.tipavsp as avs on dem1.waitie = avs.htitie ");
        sql.append("inner join schema.titierp as tie on dem1.waitie = tie.htitie ");
        sql.append("inner join schema.apdroip as dr1 on dem1.waidem = dr1.vaidem ");
        sql.append("inner join schema.apsiprp as si1 on dr1.vaidro = si1.vfidro ");
        sql.append("inner join schema.appresp on dr1.vaidro = vhidro ");
        sql.append("inner join schema.appandemie on dr1.vaidro = id_droit ");
        sql.append("where vatgse in (52001040,52001044) and vfbind = '1' and vhtgen = 52015012 ");
        sql.append("and vhdmob > 0 ");
        sql.append("and dr1.vaidro not in (select dr1.vaidro from schema.apdroip as dr4 where dr4.vaipar = dr1.vaidro) ");
        sql.append("and vhddeb < 20200917 ");
        return sql.toString();
    }

    private String getSqlJoursCarence() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("hxnavs as NSS,");
        sql.append("trim(htlde1) concat ' ' concat trim(htlde2) as NOM,");
        sql.append("case WHEN vcddeb=\'0\' or vcddeb IS NULL THEN \'00.00.0000\'  ");
        sql.append("ELSE ");
        sql.append("substr(char(vcddeb), 7, 2) CONCAT '.' CONCAT substr(char(vcddeb), 5, 2) CONCAT '.' CONCAT substr(char(vcddeb), 1, 4) end as \"DATEDEBUT1STPERIODE\",");
        sql.append("case WHEN vcdfin=\'0\' or vcdfin IS NULL THEN \'00.00.0000\'  ");
        sql.append("ELSE ");
        sql.append("substr(char(vcdfin), 7, 2) CONCAT '.' CONCAT substr(char(vcdfin), 5, 2) CONCAT '.' CONCAT substr(char(vcdfin), 1, 4) end as \"DATEFIN1STPERIODE\", ");
        sql.append("case WHEN vadddr=\'0\' or vadddr IS NULL THEN \'00.00.0000\'  ");
        sql.append("ELSE ");
        sql.append("substr(char(vadddr),7,2) CONCAT '.' CONCAT substr(char(vadddr),5,2) CONCAT '.' CONCAT substr(char(vadddr),1,4) end as \"DATEDEBUTPRESTATION\",");
        sql.append("case WHEN vaddfd=\'0\' or vaddfd IS NULL THEN \'00.00.0000\'    ");
        sql.append("ELSE ");
        sql.append("substr(char(vaddfd),7,2) CONCAT '.' CONCAT substr(char(vaddfd),5,2) CONCAT '.' CONCAT substr(char(vaddfd),1,4) end AS \"DATEFINPRESTATION\",");
        sql.append("vhnnjs as \"NBREJOURS\",");
        sql.append("vateta as \"ETAT\", ");
        sql.append("vatgse as \"GENRESERVICE\",");
        sql.append("vaidem as \"IDDEMANDE\", ");
        sql.append("vaidro as \"IDDROIT\", ");
        sql.append("dem1.waitie AS \"IDTIERS\", ");
        sql.append("\'0\' as \"MNTJOURNALIER\",");
        sql.append("\'0\' as \"MNTPRESTATION\",");
        sql.append("\'0\' as \"MNTBRUTREPARTITION\",");
        sql.append("\'0\' as \"BENEFPAIEMENT\" ");
        sql.append("FROM schema.prdemap as dem1 ");
        sql.append("inner join schema.tipavsp as avs on dem1.waitie = avs.htitie ");
        sql.append("inner join schema.titierp as tie on dem1.waitie = tie.htitie ");
        sql.append("inner join schema.apdroip as dr1 on dem1.waidem = dr1.vaidem ");
        sql.append("inner join schema.appresp on dr1.vaidro = vhidro ");
        sql.append("inner join schema.appandemie on dr1.vaidro = id_droit ");
        sql.append("CROSS JOIN LATERAL (select SUB_APPERIP.*  from schema.apperip AS SUB_APPERIP where SUB_APPERIP.vcidro = dr1.vaidro order by SUB_APPERIP.vcddeb asc fetch first row only) as apperip  ");
        sql.append("where dr1.vatgse in (52001040,52001044) ");
        sql.append("and (((vcdfin - vcddeb) < 2) and vcdfin <> 0) ");
        sql.append("and vhdmob > 0 ");
        sql.append("and dr1.vaidro not in (select dr1.vaidro from schema.apdroip as dr4 where dr4.vaipar = dr1.vaidro) ");
        return sql.toString();
    }

    private String getSqlSuiviDossier() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("CASE WHEN KUSER IS NULL THEN '' ELSE KUSER END AS \"USERNAME\", ");
        sql.append("CASE WHEN FFIRSTNAME IS NULL THEN '' ELSE FFIRSTNAME END  AS \"NOMGESTIONNAIRE\", ");
        sql.append("CASE WHEN FLASTNAME IS NULL THEN '' ELSE FLASTNAME END  AS \"PRENOMGESTIONNAIRE\", ");
        sql.append("WAIDEM AS \"NUMERODEMANDE\", ");
        sql.append("HXNAVS AS \"NSS\", ");
        sql.append("HTLDE1 AS \"NOM\", ");
        sql.append("HTLDE2 AS \"PRENOM\", ");
        sql.append("substr(char(HPDNAI), 7, 2) CONCAT '.' CONCAT substr(char(HPDNAI), 5, 2) CONCAT '.' CONCAT substr(char(HPDNAI), 1, 4) AS \"DATENAISSANCE\", ");
        sql.append("HPTSEX AS \"SEXE\", ");
        sql.append("HPTETC AS \"ETATCIVIL\", ");
        sql.append("HNIPAY AS \"PAYS\", ");
        sql.append("VAIDRO AS \"IDDROIT\", ");
//        sql.append("WATETA AS \"ETATDEMANDE\", ");
        sql.append("VATGSE AS \"TYPEDROIT\", ");
        sql.append("CATEGORIE_ENTREPRISE AS \"CATEGORIEETABLISSEMENT\", ");
        sql.append("MOTIF_GARDE AS \"MOTIFGARDE\", ");
        sql.append("CONCAT(630000, QUARANTAINE_ORDONNEE)  AS \"QUARANTAINEORDONNE\", ");
        sql.append("CASE WHEN MANIFESTATION_ANNULEE_DEBUT IS NULL OR MANIFESTATION_ANNULEE_DEBUT=\'0\' THEN '00.00.0000' ELSE substr(char(MANIFESTATION_ANNULEE_DEBUT), 7, 2) CONCAT '.' CONCAT substr(char(MANIFESTATION_ANNULEE_DEBUT), 5, 2) CONCAT '.' CONCAT substr(char(MANIFESTATION_ANNULEE_DEBUT), 1, 4) END  AS \"DATEDEBUTMANIF\", ");
        sql.append("CASE WHEN MANIFESTATION_ANNULEE_FIN IS NULL OR MANIFESTATION_ANNULEE_FIN=\'0\'  THEN '00.00.0000' ELSE substr(char(MANIFESTATION_ANNULEE_FIN),7,2) CONCAT '.' CONCAT substr(char(MANIFESTATION_ANNULEE_FIN),5,2) CONCAT '.' CONCAT substr(char(MANIFESTATION_ANNULEE_FIN),1,4) END  AS \"DATEFINMANIF\", ");
        sql.append("substr(char(VADREC), 7, 2) CONCAT '.' CONCAT substr(char(VADREC), 5, 2) CONCAT '.' CONCAT substr(char(VADREC), 1, 4)  AS \"DATERECEPTION\", ");
        sql.append("substr(char(VADDEP), 7, 2) CONCAT '.' CONCAT substr(char(VADDEP), 5, 2) CONCAT '.' CONCAT substr(char(VADDEP), 1, 4) AS \"DATEDEPOTDROIT\", ");
        sql.append("substr(char(VADDDR), 7, 2) CONCAT '.' CONCAT substr(char(VADDDR), 5, 2) CONCAT '.' CONCAT substr(char(VADDDR), 1, 4) AS \"DATEDEBUTDROIT\", ");
        sql.append("CASE WHEN VADDFD IS NULL or VADDFD=\'0\' THEN '00.00.0000' ELSE substr(char(VADDFD), 7, 2) CONCAT '.' CONCAT substr(char(VADDFD), 5, 2) CONCAT '.' CONCAT substr(char(VADDFD), 1, 4)END AS \"DATEFINDROIT\", ");
        sql.append("VATETA AS \"ETATDROIT\", ");
        sql.append("VARCHAR_FORMAT(current_timestamp,'DD.MM.YYYY')AS \"DATEEXPORT\" ");
        sql.append("FROM schema.PRDEMAP ");
        sql.append("INNER JOIN schema.APDROIP ON schema.PRDEMAP.WAIDEM=schema.APDROIP.VAIDEM ");
        sql.append("LEFT JOIN schema.APPANDEMIE ON schema.APDROIP.VAIDRO = schema.APPANDEMIE.ID_DROIT ");
        sql.append("LEFT JOIN schema.TITIERP ON schema.PRDEMAP.WAITIE=schema.TITIERP.HTITIE ");
        sql.append("LEFT JOIN schema.TIPERSP ON schema.TITIERP.HTITIE=schema.TIPERSP.HTITIE ");
        sql.append("LEFT JOIN schema.TIPAVSP ON schema.PRDEMAP.WAITIE=schema.TIPAVSP.HTITIE ");
        sql.append("LEFT JOIN schema.FWSUSRP ON schema.APDROIP.VAIGES=schema.FWSUSRP.KUSER ");
        sql.append("WHERE schema.PRDEMAP.WATTDE=52201008 ");
        return sql.toString();
    }
    @Override
    protected String getEMailObject() {
        if(getSession().hasErrors() || isAborted() ){
            return getSession().getLabel("PANDEMIE_LISTE_CONTROLE_KO_MAIL");
        }else{
            return getSession().getLabel("PANDEMIE_LISTE_CONTROLE_OK_MAIL");
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

}
