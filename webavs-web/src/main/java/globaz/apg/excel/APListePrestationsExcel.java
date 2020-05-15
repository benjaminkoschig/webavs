package globaz.apg.excel;

import ch.globaz.common.domaine.Date;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationJointLotTiersDroit;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.vb.prestation.APPrestationJointLotTiersDroitListViewBean;
import globaz.apg.vb.prestation.APPrestationJointLotTiersDroitViewBean;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.excel.REAbstractListExcel;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRStringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

import java.awt.*;
import java.util.List;

public class APListePrestationsExcel extends REAbstractListExcel {

    private static final long serialVersionUID = 1L;
    private String derniereIdPrestation = null;
    private String forCsSexe = "";

    private String forDateNaissance = "";

    private String forEtat = "";
    private String forIdDroit = "";
    private String forNoLot = "";

    private String forTypeDroit = "";
    private String fromDateDebut = "";
    private String toDateFin = "";
    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likeNumeroAVSNNSS = "";
    private String likePrenom = "";

    private FWCurrency montantTotalAllocationsListe = new FWCurrency("0.00");
    private FWCurrency montantTotalFraisGardeListe = new FWCurrency("0.00");
    private int nombreTotalCas = 0;
    private String orderBy = "";
    private FWCurrency totalMontantBrut = new FWCurrency("0.00");
    private FWCurrency totalMontantCotisation = new FWCurrency("0.00");
    private FWCurrency totalMontantNet = new FWCurrency("0.00");

    //HEADER
    private static final String HEADER_COL_1 = "LISTE_PRESTATIONS_CONTROLEES_DETAIL_ASSURE";
    private static final String HEADER_COL_2 = "LISTE_PRESTATIONS_CONTROLEES_PERIODE";
    private static final String HEADER_COL_3 = "LISTE_PRESTATIONS_GENRE";
    private static final String HEADER_COL_4 = "LISTE_PRESTATIONS_JOURS_SOLDES";
    private static final String HEADER_COL_5 = "LISTE_PRESTATIONS_MONTANT_JOURNALIER";
    private static final String HEADER_COL_6 = "LISTE_PRESTATIONS_MONTANT_BRUT";
    private static final String HEADER_COL_7 = "LISTE_PRESTATIONS_CONTROLEES_COTISATIONS";
    private static final String HEADER_COL_8 = "LISTE_PRESTATIONS_CONTROLEES_MONTANT_NET";


    private List<APPrestationJointLotTiersDroitViewBean> list;

    private JadePublishDocumentInfo docInfoExcel;
    private HSSFCellStyle styleHeader;
    private HSSFCellStyle styleAnnule;

    public APListePrestationsExcel(BSession session) {
        super(session, "PRESTATIONS", "Liste des prestations");
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

    public void prepareData() {
        try {
            JadeThreadContext threadContext = initThreadContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
            styleHeader = getWorkbook().createCellStyle();
            styleHeader.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            styleHeader.setFont(getFontBold());
            styleAnnule = getWorkbook().createCellStyle();
            styleAnnule.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
            APPrestationJointLotTiersDroitListViewBean manager = new APPrestationJointLotTiersDroitListViewBean();
            manager.setSession(getSession());
            manager.setForCsSexe(getForCsSexe());
            manager.setForEtat(getForEtat());
            manager.setForDateNaissance(getForDateNaissance());
            manager.setForIdDroit(getForIdDroit());
            manager.setForIdLot(getForNoLot());
            manager.setForTypeDroit(getForTypeDroit());
            manager.setLikeNom(getLikeNom());
            manager.setLikePrenom(getLikePrenom());
            manager.setLikeNumeroAVS(getLikeNumeroAVS());
            manager.setLikeNumeroAVSNNSS(getLikeNumeroAVSNNSS());
            manager.setFromDateDebut(getFromDateDebut());
            manager.setToDateFin(getToDateFin());

            manager.setHasSumMontantNet(true);

            manager.setOrderBy(
                    APPrestationJointLotTiersDroit.FIELDNAME_NOM + " , " + APPrestationJointLotTiersDroit.FIELDNAME_PRENOM
                            + " , " + APPrestation.FIELDNAME_DATEDEBUT + ", " + APPrestation.FIELDNAME_IDPRESTATIONAPG);

            manager.find(getSession().getCurrentThreadTransaction(), BManager.SIZE_NOLIMIT);
            list = manager.getContainer();
        } catch (Exception ex) {
            JadeLogger.error(APListePrestationsExcel.class, ex.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }


    }

    public void creerDocument() {
        createSheet("Sheet 1");
        initPage(true);
        createHeaderFooterDocument();
        createCriteresSelected();
        createHeaderRows();
        createDataRows();
        createTotaux();
        currentSheet.autoSizeColumn((short) 0);
        currentSheet.autoSizeColumn((short) 1);
        currentSheet.autoSizeColumn((short) 2);
        currentSheet.autoSizeColumn((short) 3);
        currentSheet.autoSizeColumn((short) 4);
        currentSheet.autoSizeColumn((short) 5);
        currentSheet.autoSizeColumn((short) 6);


    }

    private void createCriteresSelected() {
        currentSheet.addMergedRegion(new Region(0, (short) 1, 0, (short) 7));
        createRow();
        StringBuffer criteres = new StringBuffer();

        if (!JadeStringUtil.isEmpty(likeNumeroAVS)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_NNSS"));
            criteres.append(likeNumeroAVS);
        }

        if (!JadeStringUtil.isEmpty(likeNom)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_NOM"));
            criteres.append(likeNom);
        }

        if (!JadeStringUtil.isEmpty(likePrenom)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_PRENOM"));
            criteres.append(likePrenom);
        }

        if (!JadeStringUtil.isIntegerEmpty(forDateNaissance)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_DATE_NAISSANCE"));
            criteres.append(forDateNaissance);
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsSexe)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_SEXE"));
            criteres.append(getSession().getCodeLibelle(forCsSexe));
        }

        if (!JadeStringUtil.isEmpty(fromDateDebut)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_DATE_DEBUT"));
            criteres.append(fromDateDebut);
        }

        if (!JadeStringUtil.isEmpty(toDateFin)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_DATE_FIN"));
            criteres.append(toDateFin);
        }

        if (!JadeStringUtil.isIntegerEmpty(forEtat)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_ETAT"));

            if (APPrestationManager.ETAT_NON_DEFINITIF.equals(forEtat)) {
                criteres.append(getSession().getLabel("JSP_NON_DEFINITIF"));
            } else {
                criteres.append(getSession().getCodeLibelle(forEtat));
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(forNoLot)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_NO_LOT"));
            criteres.append(forNoLot);
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdDroit)) {
            if (criteres.length() > 0) {
                criteres.append(", ");
            }
            criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_NO_DROIT"));
            criteres.append(forIdDroit);
        }

        String cell = criteres.toString();
        this.createCell(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_SELECTION"));
        this.createCell(cell);

    }

    private void createTotaux() {
        createRow();
        this.createCell("Totaux", styleHeader);
        this.createCell("");
        this.createCell("");
        this.createCell("");
        this.createCell("");
        this.createCell(totalMontantBrut.toStringFormat(), styleHeader);
        this.createCell(totalMontantCotisation.toStringFormat(), styleHeader);
        this.createCell(totalMontantNet.toStringFormat(), styleHeader);
        if (!montantTotalAllocationsListe.isZero()) {
            createRow();
            this.createCell("");
            this.createCell(getSession().getLabel("JSP_LISTE_PRESTATION_ALLOCATION_EXPLOITATION"), styleHeader);
            this.createCell("");
            this.createCell("");
            this.createCell("");
            this.createCell(montantTotalAllocationsListe.toStringFormat(), styleHeader);
        }
        if (!montantTotalFraisGardeListe.isZero()) {
            createRow();
            this.createCell("");
            this.createCell(getSession().getLabel("JSP_LISTE_PRESTATION_FRAIS_DE_GARDE"), styleHeader);
            this.createCell("");
            this.createCell("");
            this.createCell("");
            this.createCell("");
            this.createCell(montantTotalFraisGardeListe.toStringFormat(), styleHeader);
        }
        if (nombreTotalCas > 0) {
            createRow();
            this.createCell(getSession().getLabel("JSP_LISTE_PRESTATION_NOMBRE_DE_CAS"), styleHeader);
            this.createCell(String.valueOf(nombreTotalCas));
        }

    }

    private void createHeaderFooterDocument() {
        HSSFHeader header = currentSheet.getHeader();
        header.setLeft(FWIImportProperties.getInstance().getProperty(docInfoExcel,
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
        header.setRight(new Date().toString());
        createFooter();
    }

    private void createHeaderRows() {
        createRow();
        this.createCell(getSession().getLabel(HEADER_COL_1), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_2), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_3), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_4), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_5), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_6), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_7), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_8), styleHeader);
    }

    private void createDataRows() {
        for (APPrestationJointLotTiersDroitViewBean prestation : list) {
            HSSFRow row = createRow();
            PRTiersWrapper tier = null;
            try {
                tier = PRTiersHelper.getTiers(getSession(), prestation.getNoAVS());
            } catch (Exception e) {
                getSession().addError(getSession().getLabel("ERROR_TIERS_INTROUVABLE_PAR_NO_AVS"));
            }
            if (tier != null) {

                this.createCell((prestation.getNoAVS() + " / " + formatNom(prestation.getNom() + " " + prestation.getPrenom())
                        + " / " + tier.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE) + " / "
                        + getLibelleCourtSexe(tier.getProperty(PRTiersWrapper.PROPERTY_SEXE)) + " / "
                        + getLibellePays(tier.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))));
            } else {
                this.createCell("");
            }

            this.createCell(prestation.getDateDebut() + " - " + prestation.getDateFin());
            this.createCell(getSession().getCodeLibelle(prestation.getGenreService()));
            this.createCell(prestation.getNombreJoursSoldes());
            this.createCell(new FWCurrency(prestation.getMontantJournalier()).toStringFormat());
            FWCurrency montantBrut = new FWCurrency(prestation.getMontantBrut());
            montantBrut.add(new FWCurrency(prestation.getFraisGarde()));
            FWCurrency cotisations = new FWCurrency(prestation.getMontantNet());
            cotisations.sub(montantBrut);
            this.createCell(montantBrut.toStringFormat());
            this.createCell(cotisations.toStringFormat());
            this.createCell(new FWCurrency(prestation.getMontantNet()).toStringFormat());
            if (!prestation.isAnnule()) {
                // mise a jour des totaux
                totalMontantBrut.add(montantBrut);
                totalMontantNet.add(prestation.getMontantNet());
                totalMontantCotisation.add(cotisations);
            } else {
                HSSFCell cell;
                for (int i = 0; i < 8; i++) {
                    cell = row.getCell((short) i);
                    cell.setCellStyle(styleAnnule);
                }
            }
            if (((!JadeStringUtil.isEmpty(prestation.getMontantTotalAllocExploitation()))
                    && !(new FWCurrency(prestation.getMontantTotalAllocExploitation()).isZero()))) {
                row = createRow();
                this.createCell("");
                this.createCell(getSession().getLabel("JSP_LISTE_PRESTATION_ALLOCATION_EXPLOITATION"));
                this.createCell("");
                this.createCell("");
                this.createCell("");
                this.createCell(new FWCurrency(prestation.getMontantTotalAllocExploitation()).toString());

                montantTotalAllocationsListe
                        .add(new FWCurrency(prestation.getMontantTotalAllocExploitation()).toString());
            }
            if ((!JadeStringUtil.isEmpty(prestation.getFraisGarde())
                    && !(new FWCurrency(prestation.getFraisGarde()).isZero()))) {

                this.createCell("");
                this.createCell(getSession().getLabel("JSP_LISTE_PRESTATION_FRAIS_DE_GARDE"));
                this.createCell("");
                this.createCell("");
                this.createCell("");
                this.createCell(new FWCurrency(prestation.getFraisGarde()).toString());

                montantTotalFraisGardeListe.add(new FWCurrency(prestation.getFraisGarde()).toString());

            }

            // Ajout du cas au nombre de cas totaux
            nombreTotalCas++;
        }

    }

    public String getLibellePays(String csNationalite) {

        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", csNationalite)))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", csNationalite));
        }

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

    private String formatNom(String nom) {
        // TODO c pas bien fait du tout, mais au moins ça marche
        if (nom.length() > 20) {
            // on coupe suivant les espaces
            String[] nomCoupe = PRStringUtils.split(nom, ' ');
            char modeCoupage = ' ';

            // si ça n'a pas suffit, on le coupe avec le -
            if (nomCoupe.length == 1) {
                nomCoupe = PRStringUtils.split(nom, '-');
                modeCoupage = '-';
            }

            int size = 0;
            nom = "";

            StringBuffer buffer = new StringBuffer();

            for (int i = 0; i < nomCoupe.length; i++) {
                size += nomCoupe[i].length();

                if (size > 20) {
                    if (i == 0) {
                        nom = nomCoupe[i].substring(0, 20) + "...";
                        buffer.delete(0, 60);

                        break;
                    } else {
                        buffer.append(modeCoupage);
                        buffer.append("\n");
                        size = nomCoupe[i].length();

                        if (size > 20) {
                            buffer.append(nomCoupe[i].substring(0, 20) + "...");
                            nom += buffer.toString();
                            buffer.delete(0, 60);

                            break;
                        }

                        nom += buffer.toString();
                        buffer.delete(0, 30);
                        buffer.append(nomCoupe[i]);
                    }
                } else {
                    if (i != 0) {
                        buffer.append(modeCoupage);
                    }

                    buffer.append(nomCoupe[i]);
                    size += 1;
                }
            }

            if (buffer.length() != 0) {
                nom += buffer.toString();
            }
        }

        return nom;
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public String getForEtat() {
        return forEtat;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public String getForNoLot() {
        return forNoLot;
    }

    public void setForNoLot(String forNoLot) {
        this.forNoLot = forNoLot;
    }

    public String getForTypeDroit() {
        return forTypeDroit;
    }

    public void setForTypeDroit(String forTypeDroit) {
        this.forTypeDroit = forTypeDroit;
    }

    public String getFromDateDebut() {
        return fromDateDebut;
    }

    public void setFromDateDebut(String fromDateDebut) {
        this.fromDateDebut = fromDateDebut;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    public void setLikeNumeroAVS(String likeNumeroAVS) {
        this.likeNumeroAVS = likeNumeroAVS;
    }

    public String getLikeNumeroAVSNNSS() {
        return likeNumeroAVSNNSS;
    }

    public void setLikeNumeroAVSNNSS(String likeNumeroAVSNNSS) {
        this.likeNumeroAVSNNSS = likeNumeroAVSNNSS;
    }

    public String getLikePrenom() {
        return likePrenom;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }

    public String getToDateFin() {
        return toDateFin;
    }

    public void setToDateFin(String toDateFin) {
        this.toDateFin = toDateFin;
    }

    protected JadeThreadContext initThreadContext(BSession session) {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles;
        try {
            roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                    .findAllIdRoleForIdUser(session.getUserId());
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);

        return context;
    }

    public List<APPrestationJointLotTiersDroitViewBean> getList() {
        return list;
    }

    public void setList(List<APPrestationJointLotTiersDroitViewBean> list) {
        this.list = list;
    }

    public JadePublishDocumentInfo getDocInfoExcel() {
        return docInfoExcel;
    }

    public void setDocInfoExcel(JadePublishDocumentInfo docInfoExcel) {
        this.docInfoExcel = docInfoExcel;
    }

}
