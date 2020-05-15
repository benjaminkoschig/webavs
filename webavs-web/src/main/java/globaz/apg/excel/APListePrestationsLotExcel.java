package globaz.apg.excel;

import globaz.apg.db.prestation.APPrestationJointLotTiersDroit;
import globaz.apg.db.prestation.APRepartJointCotJointPrestJointEmployeur;
import globaz.apg.db.prestation.APRepartJointCotJointPrestJointEmployeurManager;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.excel.REAbstractListExcel;
import globaz.framework.printing.itext.dynamique.FWITableModel;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.prestation.acor.PRACORConst;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdressePaiementBeneficiaireFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.util.Region;

import java.util.*;

public class APListePrestationsLotExcel extends REAbstractListExcel {
    List<APPrestationJointLotTiersDroit> list;

    //HEADER
    private static final String HEADER_COL_1 = "LIST_CTRL_COL_DETAIL_BEN";
    private static final String HEADER_COL_2 = "LIST_CTRL_PERIODE";
    private static final String HEADER_COL_3 = "LIST_CTRL_COL_PERIODE";
    private static final String HEADER_COL_4 = "DOC_LISTE_DOUBLONS_PAIEMENT_GENRE_DE_SERVICE";
    private static final String HEADER_COL_5 = "DOC_LISTE_DOUBLONS_PAIEMENT_ETAT";
    private static final String HEADER_COL_6 = "DOC_LISTE_DOUBLONS_PAIEMENT_ID_DEMANDE";
    private static final String HEADER_COL_7 = "DOC_LISTE_DOUBLONS_PAIEMENT_ID_DROIT";
    FWCurrency totalVentilations = new FWCurrency("0");
    FWCurrency totalIndemBrutes = new FWCurrency("0");
    Map finalTotauxMap  = new Hashtable();
    Map listBenef = new Hashtable();
    FWCurrency totalParNSS = new FWCurrency("0");
    private JadePublishDocumentInfo docInfoExcel;
    APRepartJointCotJointPrestJointEmployeur lastBenef;

    /**
     * @param session
     */
    public APListePrestationsLotExcel(BSession session) {
        super(session, "PRESTATIONS", session.getLabel("LISTE_CONTROLE_TITLE"));
        docInfoExcel = new JadePublishDocumentInfo();
        docInfoExcel.setSystem(JadeUtil.getCurrentIP());
        docInfoExcel.setDocumentTypeNumber("");
        docInfoExcel.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new java.util.Date()));
        docInfoExcel.setBarcode("");
        docInfoExcel.setFileDate(JadeDateUtil.getGlobazFormattedDate(new java.util.Date()));
        docInfoExcel.setOwnerId(session.getUserId());
        docInfoExcel.setOwnerName(session.getUserFullName());
        docInfoExcel.setOwnerLanguage(session.getIdLangueISO());
        JadeUser user = session.getUserInfo();
        docInfoExcel.setOwnerCompany((user != null ? user.getCompany() : ""));
        docInfoExcel.setOwnerDepartment((user != null ? user.getDepartment() : ""));
        docInfoExcel.setOwnerPhone((user != null ? user.getPhone() : ""));
        docInfoExcel.setOwnerEmail(session.getUserEMail());
        docInfoExcel.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, session.getUserEMail());

    }

    public List<APPrestationJointLotTiersDroit> getList() {
        return list;
    }

    public void setList(List<APPrestationJointLotTiersDroit> list) {
        this.list = list;
    }

    public void creerDocument() throws Exception {
        try{
            createSheet(getSession().getLabel("LIST_CTRL_LOT_SHEET_1"));
            createHeaderFooterDocument();
            initPage(true);
            createDetailPrestation();
            for(int i = 0;i<5;i++){
                currentSheet.autoSizeColumn((short) i);
            }

            createSheet(getSession().getLabel("LIST_CTRL_LOT_SHEET_2"));
            initPage(true);
            createTotaux();
        }catch(Exception e){
            throw e;
        }


    }
    private void createHeaderFooterDocument() {
        HSSFHeader header = currentSheet.getHeader();
        header.setLeft(FWIImportProperties.getInstance().getProperty(docInfoExcel,
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
        header.setRight(new ch.globaz.common.domaine.Date().toString());
        createFooter();
    }
    private void createDetailPrestation() throws Exception {
        FWCurrency montantBrut;
        for (APPrestationJointLotTiersDroit prestation : list) {
            totalVentilations = new FWCurrency(0);
            totalParNSS = new FWCurrency(0);
            montantBrut = new FWCurrency(prestation.getMontantBrut());
            //ROW 1 : Headers
            createHeaderPrestation();
            createRow();
            String nss = prestation.getNoAVS() + " / " + prestation.getNom() + " " + prestation.getPrenom() + " / "
                    + prestation.getDateNaissance() + " / " + getLibelleCourtSexe(prestation.getCsSexe()) + " / "
                    + getLibellePays(prestation.getCsNationalite());
            createCell(nss);
            createCell("");
            createCell(prestation.getDateFin());
            createCell(getSession().getCodeLibelle(prestation.getGenre()));
            //ROW2
            createHeaderDetailPrestation(prestation);
            //ROW3
            createRow();
            createEmptyCell(1);
            createCell(prestation.getDateDebut() + " - " + prestation.getDateFin());
            createCell(prestation.getNombreJoursSoldes());
            //ROW4 : Cotis
            APRepartJointCotJointPrestJointEmployeurManager repManager = new APRepartJointCotJointPrestJointEmployeurManager();
            repManager.setSession(getSession());
            repManager.setForIdPrestation(prestation.getIdPrestationApg());
            repManager.find(getSession().getCurrentThreadTransaction(), BManager.SIZE_NOLIMIT);
            createRowCoti(repManager,prestation.getMontantBrut());
            createBenef();
            listBenef.clear();
        }


    }

    private void createMontantTotal() {
        createRow();
        createEmptyCell(3);
        createCell(getSession().getLabel("LIST_CTRL_MONTANT_TOT"));
        createCell(totalParNSS.toStringFormat());
        totalParNSS = new FWCurrency("0");
    }

    private void createRowCoti(APRepartJointCotJointPrestJointEmployeurManager repManager,String montantBrut) throws Exception {
        APRepartJointCotJointPrestJointEmployeur entity;

        Map<String, FWCurrency> mapCotis = new HashMap<>();

        for (int i = 0; i < repManager.size(); i++) {
            entity = (APRepartJointCotJointPrestJointEmployeur) repManager.get(i);
            if (JadeStringUtil.toDouble(entity.getMontantVentile()) > 0) {
                createRow();
                createEmptyCell(1);
                createCell(getPaiementAdresseBeneficiaireFormate(entity.loadAdressePaiement(null)));
                createCell(getSession().getLabel("LIST_CTRL_VENTILATION"));
                createCell(new FWCurrency(entity.getMontantVentile()).toStringFormat(),getStyleRightMontant());
                totalVentilations.add(new FWCurrency(entity.getMontantVentile()));
            } else if (JadeStringUtil.toDouble(entity.getMontantBrut()) != 0) {
                this.addMontant(entity.getGenreCotisation(), new FWCurrency(entity.getMontantCotisation()), mapCotis);
                this.addMontant(entity.getGenreCotisation(),entity.getMontantCotisation(),finalTotauxMap,entity.isBeneficiaireEmployeur());
                if(!listBenef.containsKey(entity.getIdRepartitionBeneficiairePaiement())){
                    listBenef.put(entity.getIdRepartitionBeneficiairePaiement(),entity);
                    this.addMontant(getSession().getLabel("LIST_CTRL_INDEMNITE_BRUTE"),entity.getMontantBrut(),finalTotauxMap,entity.isBeneficiaireEmployeur());
                    this.addMontant(getSession().getLabel("LIST_CTRL_MONTANT_TOT"), entity.getMontantNet(),finalTotauxMap,entity.isBeneficiaireEmployeur());
                }

            }
        }
        Iterator itCoti = mapCotis.keySet().iterator();
        List<String> listKeys = new ArrayList<>();
        listKeys.addAll(mapCotis.keySet());
        Collections.sort(listKeys, Collections.reverseOrder());
        createCell(getSession().getLabel("LIST_CTRL_INDEMNITE_BRUTE"));
        createCell(montantBrut,getStyleRightMontant());

        totalParNSS.add(new FWCurrency(montantBrut));
        for (String key : listKeys) {
            key = (String) itCoti.next();
            createRow();
            createEmptyCell(3);
            if (APRepartJointCotJointPrestJointEmployeur.IMPOT_SOURCE.equals(key)) {
                createCell(getSession().getLabel("LIST_CTRL_IMPOTS_SOURCE"));
            } else {
                createCell(key);
            }
            createCell(((FWCurrency) mapCotis.get(key)).toStringFormat(),getStyleRightMontant());
            totalParNSS.add((FWCurrency) mapCotis.get(key));
        }
        createRow();
        createEmptyCell(3);
        createCell(getSession().getLabel("LIST_CTRL_MONTANT_TOT"), getStyleLeftBorderTop());
        createCell(totalParNSS.toStringFormat(),getStyleRightMontantBorderTop());
    }

    private void createBenef() throws Exception {
        APRepartJointCotJointPrestJointEmployeur benef;
        if(!listBenef.isEmpty()){
            for(Object key : listBenef.keySet()){
                benef = (APRepartJointCotJointPrestJointEmployeur)listBenef.get(key);
                String adresses = getPaiementAdresseBeneficiaireFormate(benef.loadAdressePaiement(null));
                String[] adressesMap = adresses.split("\n");
                createCell("");
                createRow();
                currentSheet.addMergedRegion(new Region(currentSheet.getPhysicalNumberOfRows()-1, (short) 3, currentSheet.getPhysicalNumberOfRows()+2, (short) 3));
                currentSheet.addMergedRegion(new Region(currentSheet.getPhysicalNumberOfRows()-1, (short) 4, currentSheet.getPhysicalNumberOfRows()+2, (short) 4));
                createCell("");
                createCell(adressesMap[0]);
                createCell("");
                createCell(getSession().getCodeLibelle(benef.getTypePaiement()),getStyleLeftCenterVert());
                FWCurrency montantNet = new FWCurrency(benef.getMontantNet());
                montantNet.sub(totalVentilations);
                createCell(montantNet.toStringFormat(),getStyleRightMontantVert());
                for(int i=1;i<adressesMap.length;i++){
                    createRow();
                    createCell("");
                    createCell(adressesMap[i]);
                }
            }
        }
    }

    private void createHeaderDetailPrestation(APPrestationJointLotTiersDroit entityPrestation) {
        createRow();
        createEmptyCell(1);
        createCell(getSession().getLabel("LIST_CTRL_PERIODE"),getStyleBold());
        createCell(getSession().getLabel("LIST_CTRL_NB_JOURS_SOLDES"),getStyleBold());

    }

    private void createHeaderPrestation() {
        createRow();
        createCell(getSession().getLabel("LIST_CTRL_COL_DETAIL_BEN"), getStyleBold());
        createEmptyCell(1);
        createCell(getSession().getLabel("LIST_CTRL_COL_PERIODE"), getStyleBold());
        createCell(getSession().getLabel("LIST_CTRL_COL_GENRE"), getStyleBold());
    }

    private void createTotaux() {
        FWCurrency totauxIndBrut = new FWCurrency("0.00");
        FWCurrency totauxMontantTotal = new FWCurrency("0.00");
        List orderedList = new ArrayList();
        createRow();
        createCell("");
        createCell(getSession().getLabel("LIST_CTRL_INDEMNITE_BRUTE"),getStyleRightBold());
        orderedList.add(getSession().getLabel("LIST_CTRL_INDEMNITE_BRUTE"));
        Iterator itColumn = finalTotauxMap.keySet().iterator();
        String key = null;
        while (itColumn.hasNext()) {
            key = (String) itColumn.next();
            if (!getSession().getLabel("LIST_CTRL_INDEMNITE_BRUTE").equals(key)
                    && !getSession().getLabel("LIST_CTRL_MONTANT_TOT").equals(key)) {
                createCell(key,getStyleRightBold());
                orderedList.add(key);
            }
        }
        createCell(getSession().getLabel("LIST_CTRL_MONTANT_TOT"),getStyleRightBold());
        orderedList.add(getSession().getLabel("LIST_CTRL_MONTANT_TOT"));
        createRow();
        createCell(getSession().getLabel("LIST_CTRL_AFFILIE"),getStyleCenterNoBorder());
        Iterator itAff = orderedList.iterator();
        Map values = null;
        while (itAff.hasNext()) {
            key = (String) itAff.next();
            values = (Map) finalTotauxMap.get(key);
            createCell(((FWCurrency) values.get(Boolean.TRUE.toString())).toStringFormat(),getStyleRightMontant());

            if (getSession().getLabel("LIST_CTRL_INDEMNITE_BRUTE").equals(key)) {
                totauxIndBrut.add(((FWCurrency) values.get(Boolean.TRUE.toString())));
            }
            if (getSession().getLabel("LIST_CTRL_MONTANT_TOT").equals(key)) {
                totauxMontantTotal.add(((FWCurrency) values.get(Boolean.TRUE.toString())));
            }
        }
        createRow();
        createCell(getSession().getLabel("LIST_CTRL_ASSURE"),getStyleCenterNoBorder());
        // Affichage des Assurés
        Iterator itAss = orderedList.iterator();
        while (itAss.hasNext()) {
            key = (String) itAss.next();
            values = (Map) finalTotauxMap.get(key);
            createCell(((FWCurrency) values.get(Boolean.FALSE.toString())).toStringFormat(),getStyleRightMontant());

            if (getSession().getLabel("LIST_CTRL_INDEMNITE_BRUTE").equals(key)) {
                totauxIndBrut.add(((FWCurrency) values.get(Boolean.FALSE.toString())));
            }
            if (getSession().getLabel("LIST_CTRL_MONTANT_TOT").equals(key)) {
                totauxMontantTotal.add(((FWCurrency) values.get(Boolean.FALSE.toString())));
            }
        }
        createRow();
        createCell(getSession().getLabel("LIST_CTRL_TOTAUX"),getStyleCenterBorderTop());
        Iterator it = orderedList.iterator();
        it = orderedList.iterator();
        while (it.hasNext()) {
            key = (String) it.next();

            if (getSession().getLabel("LIST_CTRL_INDEMNITE_BRUTE").equals(key)) {
                createCell(totauxIndBrut.toStringFormat(),getStyleRightMontantBorderTop());
            } else if (getSession().getLabel("LIST_CTRL_MONTANT_TOT").equals(key)) {
                createCell(totauxMontantTotal.toStringFormat(),getStyleRightMontantBorderTop());
            } else {
                createCell("",getStyleRightMontantBorderTop());
            }
        }

        for(int i = 0;i<orderedList.size()+2;i++){
            currentSheet.autoSizeColumn((short) i);
        }
    }

    private String getPaiementAdresseBeneficiaireFormate(TIAdressePaiementData adresse) {
        String retValue = "";

        try {

            if ((adresse != null) && !adresse.isNew()) {
                TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();

                source.load(adresse);

                retValue = new TIAdressePaiementBeneficiaireFormater().format(source);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retValue;
    }

    public String getLibelleCourtSexe(String csSexe) {

        if (PRACORConst.CS_HOMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }
    }

    public String getLibellePays(String csNationalite) {

        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", csNationalite)))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", csNationalite));
        }

    }

    private void addMontant(String field, FWCurrency value, Map table) {
        FWCurrency montant;
        if (table.containsKey(field)) {
            montant = (FWCurrency) table.get(field);
        } else {
            montant = new FWCurrency(0);
        }
        montant.add(value);
        table.put(field, montant);
    }

    private void addMontant(String field, String value, Map table, boolean dependant) {
        FWCurrency montant;
        Map montants;
        if (JadeStringUtil.isEmpty(field)) {
            return;
        }
        if (!table.containsKey(field)) {
            montants = new HashMap(2);
            montants.put("true", new FWCurrency(0));
            montants.put("false", new FWCurrency(0));
        } else {
            montants = (Map) table.get(field);
        }
        montant = (FWCurrency) montants.get("" + dependant);
        montant.add(value);
        // On ajoute la valeur et on remonte la liste
        // TODO: Check if needed, not such if yes
        montants.put("" + dependant, montant);
        table.put(field, montants);
    }
    public JadePublishDocumentInfo getDocInfoExcel() {
        return docInfoExcel;
    }

    public void setDocInfoExcel(JadePublishDocumentInfo docInfoExcel) {
        this.docInfoExcel = docInfoExcel;
    }
}
