package globaz.phenix.listes.excel;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JATime;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.toolbox.CPToolBox;
import globaz.pyxis.constantes.IConstantes;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Insérez la description du type ici. Date de création : (17.06.2003 14:38:38)
 * 
 * @author: ado
 */
public class CPListeCommunicationStatistique {
    private String fromAnnee = "";
    private String fromNumAffilie = "";
    private String genreAffilie = "";

    private BProcess processAppelant = null;

    private BSession session = null;

    private HSSFSheet sheet;

    private String toAnnee = "";

    private String toNumAffilie = "";

    private HSSFWorkbook wb;

    private BPreparedStatement pDecisionProvisoire = null;
    private BPreparedStatement pPresenceCommunicationContribuableSedex = null;
    private BPreparedStatement pPresenceCommunicationConjointSedex = null;
    private BPreparedStatement pAnneeMinimumPresente = null;
    private BPreparedStatement pCommunicationEnAttenteValidation = null;
    private BPreparedStatement pPresenceCommunicationVS = null;
    private BPreparedStatement pPresenceCommunicationGE = null;
    private BPreparedStatement pPresenceCommunicationJU = null;
    private BPreparedStatement pPresenceCommunicationVD = null;
    private BPreparedStatement pPresenceCommunicationNE = null;

    /**
     * Commentaire relatif au constructeur TentDocument.
     */
    public CPListeCommunicationStatistique() {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet("Stat");
        // format
        sheet.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet.getPrintSetup().setLandscape(true);

    }

    /**
     * Commentaire relatif au constructeur TentDocument.
     */
    public CPListeCommunicationStatistique(BSession session, String fromNumAffilie, String toNumAffilie,
            String fromAnnee, String toAnnee, String genreAffilie) {
        setFromAnnee(fromAnnee);
        setToAnnee(toAnnee);
        setFromNumAffilie(fromNumAffilie);
        setToNumAffilie(toNumAffilie);
        setGenreAffilie(genreAffilie);
        this.session = session;
        wb = new HSSFWorkbook();
        sheet = wb.createSheet(getSession().getLabel("PAGE") + " 1");
        // format
        sheet.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet.getPrintSetup().setLandscape(true);
        sheet = setTitleRow(wb, sheet);

    }

    public String getFromAnnee() {
        return fromAnnee;
    }

    public String getFromNumAffilie() {
        return fromNumAffilie;
    }

    public String getGenreAffilie() {
        return genreAffilie;
    }

    public String getOutputFile() {
        try {
            File f = null;
            f = File.createTempFile(getSession().getLabel("LISTSTAT"), ".xls");
            f.deleteOnExit();
            FileOutputStream out = new FileOutputStream(f);
            wb.write(out);
            out.close();
            return f.getAbsolutePath();
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return "";
        }
    }

    /**
     * Returns the processAppelant.
     * 
     * @return BProcess
     */
    public BProcess getProcessAppelant() {
        return processAppelant;
    }

    /**
     * Returns the session.
     * 
     * @return BSession
     */
    public BSession getSession() {
        return session;
    }

    public String getToAnnee() {
        return toAnnee;
    }

    public String getToNumAffilie() {
        return toNumAffilie;
    }

    public HSSFSheet populateSheet(BTransaction transaction) {

        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        String schema = Jade.getInstance().getDefaultJdbcSchema();
        try {
            int fAnnee = 0;
            int tAnnee = JACalendar.today().getYear();
            if (!JadeStringUtil.isBlankOrZero(getFromAnnee())) {
                fAnnee = Integer.parseInt(getFromAnnee());
            } else {
                // Déterminer l'année minimum présente dans les décisions selon les critères de sélection
                pAnneeMinimumPresente = new BPreparedStatement(transaction);
                pAnneeMinimumPresente.prepareStatement(getSqlAnneePresenteSelonCritere(schema));
                ResultSet resultAnneeMinimumPresente = pAnneeMinimumPresente.executeQuery();
                resultAnneeMinimumPresente.next();
                fAnnee = Integer.parseInt(resultAnneeMinimumPresente.getObject(1).toString());
            }
            if (!JadeStringUtil.isBlankOrZero(getToAnnee())) {
                tAnnee = Integer.parseInt(getToAnnee());
            } else {
                tAnnee = JACalendar.today().getYear() - 2;
            }
            sheet.setColumnWidth((short) 0, (short) 5000);
            sheet.setColumnWidth((short) 1, (short) 3000);
            sheet.setColumnWidth((short) 2, (short) 5000);
            sheet.setColumnWidth((short) 3, (short) 5000);
            sheet.setColumnWidth((short) 4, (short) 5000);
            sheet.setColumnWidth((short) 5, (short) 5000);

            // Recherche du canton de la caisse afin de déterminer le fichier spécifique utilisé avant Sedex
            String cantonCaisse = ((CPApplication) GlobazSystem.getApplication("PHENIX")).getCantonDefaut();

            processAppelant.setProgressScaleValue((tAnnee - fAnnee) + 1);

            // Nombre de décision provisoire active
            pDecisionProvisoire = new BPreparedStatement(transaction);
            pDecisionProvisoire.prepareStatement(getSqlNombreProvisoire(schema));

            // Préparation requête pour tester présence dans communications reçues de type Sedex pour contribuable
            pPresenceCommunicationContribuableSedex = new BPreparedStatement(transaction);
            pPresenceCommunicationContribuableSedex
                    .prepareStatement(getSqlPresenceDansCommunicationContribuableSedex(schema));

            // Préparation requête pour tester présence dans communications reçues de type Sedex pour conjoint
            pPresenceCommunicationConjointSedex = new BPreparedStatement(transaction);
            pPresenceCommunicationConjointSedex.prepareStatement(getSqlPresenceDansCommunicationConjointSedex(schema));

            // Préparation requête pour tester présence dans communications reçues de type Valais (avant Sedex)
            pPresenceCommunicationVS = new BPreparedStatement(transaction);
            pPresenceCommunicationVS.prepareStatement(getSqlPresenceDansCommunicationsVS(schema));

            for (; (fAnnee <= tAnnee) && !getProcessAppelant().isAborted(); fAnnee++) {
                int nbDecProv = 0;      // Nombre de décision provisoire
                int nbCommAtt = 0;      // Nombre de communication en attente de validation1
                int nbProvNRecu = 0;    // Nombre de décision provisoire jamais reçue

                pDecisionProvisoire.clearParameters();
                pDecisionProvisoire.setInt(1, fAnnee);
                ResultSet resultDecisionProvisoire = pDecisionProvisoire.executeQuery();
                while (resultDecisionProvisoire.next()) {
                    // PO 8101: Ne pas compter si il y a une remise
                    int nbDec = CPToolBox.returnNombreDecision(getSession(),
                            resultDecisionProvisoire.getString("IAANNE"), resultDecisionProvisoire.getString("MAIAFF"),
                            CPDecision.CS_REMISE, Boolean.TRUE);
                    if (nbDec == 0) {
                        nbDecProv++;
                        // Tester si jamais reçu
                        int nbCommunicationRecu;

                        // Tester présence dans fichier contribuable Sedex
                        pPresenceCommunicationContribuableSedex.clearParameters();
                        pPresenceCommunicationContribuableSedex.setInt(1, fAnnee);
                        pPresenceCommunicationContribuableSedex.setString(2,
                                resultDecisionProvisoire.getString("HXNAVS").trim().replace(".", ""));
                        ResultSet resultSetCommunicationContribuableSedex = pPresenceCommunicationContribuableSedex
                                .executeQuery();
                        resultSetCommunicationContribuableSedex.next();
                        nbCommunicationRecu = JadeStringUtil.toInt(resultSetCommunicationContribuableSedex.getObject(1)
                                .toString());
                        if (nbCommunicationRecu == 0) {
                            // Tester présence dans fichier conjoint Sedex
                            pPresenceCommunicationConjointSedex.clearParameters();
                            pPresenceCommunicationConjointSedex.setInt(1, fAnnee);
                            pPresenceCommunicationConjointSedex.setString(2,
                                    resultDecisionProvisoire.getString("HXNAVS").trim().replace(".", ""));
                            ResultSet resultSetCommunicationConjointSedex = pPresenceCommunicationConjointSedex
                                    .executeQuery();
                            resultSetCommunicationConjointSedex.next();
                            nbCommunicationRecu = JadeStringUtil.toInt(resultSetCommunicationConjointSedex.getObject(1)
                                    .toString());
                            if (nbCommunicationRecu == 0) {
                                if (IConstantes.CS_LOCALITE_CANTON_VALAIS.equalsIgnoreCase(cantonCaisse)) {
                                    // Recherche existence dans fichier spécifique format VS (avant Sedex)
                                    String numAffilieNonFormate = resultDecisionProvisoire.getString("MALNAF").trim()
                                            .replace(".", "");
                                    String numAncienAffilieVS = formaterSelonAncienNumAffilie(resultDecisionProvisoire);
                                    pPresenceCommunicationVS.clearParameters();
                                    pPresenceCommunicationVS.setInt(1, fAnnee);
                                    pPresenceCommunicationVS.setString(2, numAncienAffilieVS);
                                    pPresenceCommunicationVS.setString(3, numAncienAffilieVS);
                                    pPresenceCommunicationVS.setString(4, numAffilieNonFormate);
                                    pPresenceCommunicationVS.setString(5, numAffilieNonFormate);
                                    ResultSet resultSetPresenceCommunicationVS = pPresenceCommunicationVS
                                            .executeQuery();
                                    resultSetPresenceCommunicationVS.next();
                                    nbCommunicationRecu = JadeStringUtil.toInt(resultSetPresenceCommunicationVS
                                            .getObject(1).toString());
                                } else if (IConstantes.CS_LOCALITE_CANTON_NEUCHATEL.equalsIgnoreCase(cantonCaisse)) {
                                    // Recherche existence dans fichier spécifique format VS (avant Sedex)
                                    // Préparation requête pour tester présence dans communications reçues de type
                                    // Neuchâtel (avant Sedex)
                                    pPresenceCommunicationNE = new BPreparedStatement(transaction);
                                    pPresenceCommunicationNE.prepareStatement(getSqlPresenceDansCommunicationsNE(
                                            schema, resultDecisionProvisoire.getString("HXNCON"), fAnnee));
                                    // pPresenceCommunicationNE.setInt(1, fAnnee);
                                    ResultSet resultSetPresenceCommunicationNE = pPresenceCommunicationNE
                                            .executeQuery();
                                    resultSetPresenceCommunicationNE.next();
                                    nbCommunicationRecu = JadeStringUtil.toInt(resultSetPresenceCommunicationNE
                                            .getObject(1).toString());
                                } else if (IConstantes.CS_LOCALITE_CANTON_VAUD.equalsIgnoreCase(cantonCaisse)) {
                                    // Préparation requête pour tester présence dans communications reçues de type Vaud
                                    // (avant Sedex)
                                    pPresenceCommunicationVD = new BPreparedStatement(transaction);
                                    pPresenceCommunicationVD.prepareStatement(getSqlPresenceDansCommunicationsVD(
                                            schema, resultDecisionProvisoire.getString("MALNAF").trim(), fAnnee));
                                    ResultSet resultSetPresenceCommunicationVD = pPresenceCommunicationVD
                                            .executeQuery();
                                    resultSetPresenceCommunicationVD.next();
                                    nbCommunicationRecu = JadeStringUtil.toInt(resultSetPresenceCommunicationVD
                                            .getObject(1).toString());
                                } else if (IConstantes.CS_LOCALITE_CANTON_GENEVE.equalsIgnoreCase(cantonCaisse)) {
                                    // Recherche existence dans fichier spécifique format GE (avant Sedex)
                                    // Préparation requête pour tester présence dans communications reçues de type
                                    // Genève (avant Sedex)
                                    pPresenceCommunicationGE = new BPreparedStatement(transaction);
                                    pPresenceCommunicationGE.prepareStatement(getSqlPresenceDansCommunicationsGE(
                                            schema, resultDecisionProvisoire.getString("MALNAF").trim(), fAnnee));
                                    ResultSet resultSetPresenceCommunicationGE = pPresenceCommunicationGE
                                            .executeQuery();
                                    resultSetPresenceCommunicationGE.next();
                                    nbCommunicationRecu = JadeStringUtil.toInt(resultSetPresenceCommunicationGE
                                            .getObject(1).toString());
                                } else if (IConstantes.CS_LOCALITE_CANTON_JURA.equalsIgnoreCase(cantonCaisse)) {
                                    // Recherche existence dans fichier spécifique format JU (avant Sedex)
                                    // Préparation requête pour tester présence dans communications reçues de type Jura
                                    // (avant Sedex)
                                    pPresenceCommunicationJU = new BPreparedStatement(transaction);
                                    pPresenceCommunicationJU.prepareStatement(getSqlPresenceDansCommunicationsJU(
                                            schema, resultDecisionProvisoire.getString("HXNCON").replace(".", ""),
                                            fAnnee));
                                    ResultSet resultSetPresenceCommunicationJU = pPresenceCommunicationJU
                                            .executeQuery();
                                    resultSetPresenceCommunicationJU.next();
                                    nbCommunicationRecu = JadeStringUtil.toInt(resultSetPresenceCommunicationJU
                                            .getObject(1).toString());
                                }
                                // Si trouvé dans aucun fichier => à incrémenter dans le nombre de communication non
                                // reçue
                                if (nbCommunicationRecu == 0) {
                                    nbProvNRecu++;
                                }
                            }
                        }
                    }
                }
                // Nombre de communications en attente de validation
                pCommunicationEnAttenteValidation = new BPreparedStatement(transaction);
                pCommunicationEnAttenteValidation.prepareStatement(getSqlCommunicationEnAttenteValidation(schema));
                pCommunicationEnAttenteValidation.clearParameters();
                pCommunicationEnAttenteValidation.setInt(1, fAnnee);
                ResultSet resultCommunicationEnAttentevalidation = pCommunicationEnAttenteValidation.executeQuery();
                while (resultCommunicationEnAttentevalidation.next()) {
                    String idDecision = resultCommunicationEnAttentevalidation.getString("IAIDEC");
                    CPDecision decision = CPDecision._returnDecisionBase(getSession(), idDecision);
                    if ((decision != null) && decision.isProvisoireMetier()) {
                        nbCommAtt++;
                    }
                }

                HSSFRow row = sheet.createRow(sheet.getPhysicalNumberOfRows() + 1);
                int colNum = 0;
                //
                HSSFCell cell = row.createCell((short) colNum++);
                cell.setCellValue(new HSSFRichTextString(""));
                // Année
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style);
                cell.setCellValue(fAnnee);
                // Nombre de décision provisoire
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style);
                cell.setCellValue(nbDecProv);
                // Nombre de communication en attente de validation1
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style);
                cell.setCellValue(nbCommAtt);
                // Nombre de décision provisoire jamais reçue
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style);
                cell.setCellValue(nbProvNRecu);
                // Nombre à traiter manuellement
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style);
                cell.setCellValue(nbDecProv - nbCommAtt - nbProvNRecu);
                processAppelant.incProgressCounter();

            }
            HSSFFooter footer = sheet.getFooter();
            footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8)
                    + "Ref: 0268CCP");
            return sheet;
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return null;
        } finally {
            if (pPresenceCommunicationVS != null) {
                pPresenceCommunicationVS.closePreparedStatement();
            }
            if (pDecisionProvisoire != null) {
                pDecisionProvisoire.closePreparedStatement();
            }
            if (pPresenceCommunicationContribuableSedex != null) {
                pPresenceCommunicationContribuableSedex.closePreparedStatement();
            }
            if (pPresenceCommunicationConjointSedex != null) {
                pPresenceCommunicationConjointSedex.closePreparedStatement();
            }
            if (pAnneeMinimumPresente != null) {
                pAnneeMinimumPresente.closePreparedStatement();
            }
            if (pCommunicationEnAttenteValidation != null) {
                pCommunicationEnAttenteValidation.closePreparedStatement();
            }
        }
    }

    private String formaterSelonAncienNumAffilie(ResultSet res) throws SQLException {
        String numAncienAffilieVS = res.getString("MALNAF");
        numAncienAffilieVS = numAncienAffilieVS.substring(0, 3) + numAncienAffilieVS.substring(4, 7)
                + numAncienAffilieVS.substring(10, 14) + "00";
        return numAncienAffilieVS;
    }

    private String getSqlAnneePresenteSelonCritere(String schema) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT MIN(IAANNE) FROM " + schema + ".CPDECIP de " + "inner join " + schema
                + ".AFAFFIP aff on aff.maiaff=de.maiaff where malnaf>=" + getFromNumAffilie());
        if (!JadeStringUtil.isBlankOrZero(getToNumAffilie())) {
            sql.append(" and malnaf<=" + getToNumAffilie());
        }
        if (!JadeStringUtil.isBlankOrZero(getGenreAffilie())) {
            if (CPDecision.CS_INDEPENDANT.equalsIgnoreCase(getGenreAffilie())) {
                sql.append(" and iatgaf in(" + CPDecision.CS_INDEPENDANT + ", " + CPDecision.CS_TSE + ", "
                        + CPDecision.CS_RENTIER + ", " + CPDecision.CS_AGRICULTEUR + ")");
            } else if (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getGenreAffilie())) {
                sql.append(" and iatgaf in(" + CPDecision.CS_NON_ACTIF + ", " + CPDecision.CS_ETUDIANT + ")");
            }
        }
        return sql.toString();
    }

    private String getSqlNombreProvisoire(String schema) {
        StringBuffer sql = new StringBuffer();
        sql.append("select de.maiaff MAIAFF, de.IAANNE IAANNE, aff.malnaf, avs.hxnavs, avs.hxncon from ");
        sql.append(schema + ".cpdecip de ");
        sql.append("inner join " + schema + ".afaffip aff on aff.maiaff=de.maiaff ");
        sql.append("inner join " + schema + ".tipavsp avs on de.htitie=avs.htitie ");
        sql.append("where iaacti='1' and iattde in (605001, 605003,605007)");
        if (!JadeStringUtil.isBlankOrZero(getFromNumAffilie())) {
            sql.append(" and malnaf >='" + getFromNumAffilie() + "'");
        }
        sql.append(" and iaanne=?");
        if (!JadeStringUtil.isBlankOrZero(getToNumAffilie())) {
            sql.append(" and malnaf<='" + getToNumAffilie() + "'");
        }
        if (!JadeStringUtil.isBlankOrZero(getGenreAffilie())) {
            if (CPDecision.CS_INDEPENDANT.equalsIgnoreCase(getGenreAffilie())) {
                sql.append(" and iatgaf in(" + CPDecision.CS_INDEPENDANT + ", " + CPDecision.CS_TSE + ", "
                        + CPDecision.CS_RENTIER + ", " + CPDecision.CS_AGRICULTEUR + ")");
            } else if (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getGenreAffilie())) {
                sql.append(" and iatgaf in(" + CPDecision.CS_NON_ACTIF + ", " + CPDecision.CS_ETUDIANT + ")");
            }
        }

        return sql.toString();
    }

    private String getSqlPresenceDansCommunicationContribuableSedex(String schema) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(*) FROM " + schema + ".CPSECON se ");
        sql.append("INNER JOIN " + schema + ".cpcretp re ON se.ikiret = re.ikiret ");
        sql.append("where ikann1 = ? AND SEVNAVS = ? and sevnavs <>''");
        return sql.toString();
    }

    private String getSqlPresenceDansCommunicationConjointSedex(String schema) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(*) FROM " + schema + ".CPSEFEM se ");
        sql.append("INNER JOIN " + schema + ".cpcretp re ON se.ikiret = re.ikiret ");
        sql.append("where ikann1 = ? AND SEVNAVS = ? and sevnavs <>''");
        return sql.toString();
    }

    private String getSqlPresenceDansCommunicationsVS(String schema) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(*) FROM "
                + schema
                + ".CPCRVSP where ikanne=? AND (rtrim(IKNOCO)=? or rtrim(IKNUMA)=? or rtrim(IKNOCO)=? or rtrim(IKNUMA)=?)");
        return sql.toString();
    }

    private String getSqlPresenceDansCommunicationsNE(String schema, String numContribuable, int annee) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select count(*) from " + schema + ".cpcrnep a");
        sql.append(" inner join " + schema + ".cpcretp b on a.ikiret=b.ikiret");
        sql.append(" where CAST(iknbdp AS char(25))='" + numContribuable + "' and (ikann1=" + annee + " or ikann2="
                + annee + ")");
        return sql.toString();
    }

    private String getSqlPresenceDansCommunicationsVD(String schema, String numAffilie, int annee) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select count(*) from " + schema + ".cpcrvdp a ");
        sql.append(" inner join " + schema + ".cpcretp b on a.ikiret = b.ikiret");
        sql.append(" where rtrim(ikafvd)='" + numAffilie + "' and ikafvd<>'' and (ikann1=" + annee + " or ikann2="
                + annee + ")");
        return sql.toString();
    }

    private String getSqlPresenceDansCommunicationsGE(String schema, String numAffilie, int annee) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select count(*) from " + schema + ".cpcrgep a ");
        sql.append(" inner join " + schema + ".cpcretp b on a.ikiret = b.ikiret");
        sql.append(" where rtrim(substr(ikafge,1,3) || substr(ikafge,5,3) || '-' || substr(ikafge,10,2))='"
                + numAffilie + "' and (ikann1=" + annee + " or ikann2=" + annee + ")");
        return sql.toString();
    }

    private String getSqlPresenceDansCommunicationsJU(String schema, String numContribuable, int annee) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select count(*) from " + schema + ".cpcrjup a ");
        sql.append(" inner join " + schema + ".cpcretp b ON a.ikiret = b.ikiret");
        sql.append(" where rtrim(CAST(iknnco AS CHAR(11)))='" + numContribuable + "' and (ikann1=" + annee
                + " or ikann2=" + annee + ")");
        return sql.toString();
    }

    private String getSqlCommunicationEnAttenteValidation(String schema) {
        StringBuffer sql = new StringBuffer();
        sql.append("select iaidec, malnaf from " + schema + ".cpdecip de  inner join " + schema
                + ".afaffip aff on aff.maiaff=de.maiaff " + "where iateta=604002 and ibidcf<>0");
        if (!JadeStringUtil.isBlankOrZero(getFromNumAffilie())) {
            sql.append(" and malnaf >='" + getFromNumAffilie() + "'");
        }
        sql.append(" and iaanne=?");
        if (!JadeStringUtil.isBlankOrZero(getToNumAffilie())) {
            sql.append(" and malnaf<='" + getToNumAffilie() + "'");
        }
        if (!JadeStringUtil.isBlankOrZero(getGenreAffilie())) {
            if (CPDecision.CS_INDEPENDANT.equalsIgnoreCase(getGenreAffilie())) {
                sql.append(" and iatgaf in(" + CPDecision.CS_INDEPENDANT + ", " + CPDecision.CS_TSE + ", "
                        + CPDecision.CS_RENTIER + ", " + CPDecision.CS_AGRICULTEUR + ")");
            } else if (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getGenreAffilie())) {
                sql.append(" and iatgaf in(" + CPDecision.CS_NON_ACTIF + ", " + CPDecision.CS_ETUDIANT + ")");
            }
        }
        return sql.toString();

    }

    public void setFromAnnee(String fromAnnee) {
        this.fromAnnee = fromAnnee;
    }

    public void setFromNumAffilie(String fromNumAffilie) {
        this.fromNumAffilie = fromNumAffilie;
    }

    public void setGenreAffilie(String genreAffilie) {
        this.genreAffilie = genreAffilie;
    }

    /**
     * Sets the processAppelant.
     * 
     * @param processAppelant
     *            The processAppelant to set
     */
    public void setProcessAppelant(BProcess processAppelant) {
        this.processAppelant = processAppelant;
    }

    /**
     * Sets the session.
     * 
     * @param session
     *            The session to set
     */
    public void setSession(BSession session) {
        this.session = session;
    }

    /**
     * @param wb
     * @param sheet
     * @param row
     * @return
     */
    private HSSFSheet setTitleRow(HSSFWorkbook wb, HSSFSheet sheet) {
        final String annee = getSession().getLabel("ANNEE");
        final String decProv = getSession().getLabel("DEC_PROV");
        final String decAtt = getSession().getLabel("DEC_EN_ATTENTE");
        final String commJRecu = getSession().getLabel("COMM_JAMAIS_RECUE");
        final String commATraiter = getSession().getLabel("COMM_ATRAITER");

        // final String[] COL_TITLES = { annee, decProv, decAtt, commJRecu, commATraiter };
        final String[] COL_TITLES = { annee, decProv, decAtt, commJRecu, commATraiter };
        HSSFRow row = null;
        HSSFCell c;
        // Déclaration des styles pour les cellules
        HSSFCellStyle style1 = wb.createCellStyle();
        HSSFFont font1 = wb.createFont();
        font1.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font1.setFontHeight((short) 200);
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style1.setFont(font1);

        HSSFFont font2 = wb.createFont();
        font2.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font2.setFontHeight((short) 200);
        font2.setColor(HSSFFont.COLOR_NORMAL);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font2);

        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font3.setFontHeight((short) 200);
        style3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style3.setFont(font3);

        HSSFCellStyle style4 = wb.createCellStyle();
        HSSFFont font4 = wb.createFont();
        font4.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font4.setFontHeight((short) 200);
        style4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        style4.setFont(font4);

        row = sheet.createRow(0);
        c = row.createCell((short) 0);
        try {
            c.setCellValue(new HSSFRichTextString(session.getApplication().getProperty(
                    "COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase())));
        } catch (Exception e) {
            c.setCellValue(new HSSFRichTextString(""));
        }
        c.setCellStyle(style2);
        c = row.createCell((short) 5);
        c.setCellStyle(style4);
        c.setCellValue(new HSSFRichTextString(JACalendar.todayJJsMMsAAAA() + " - "
                + new JATime(JACalendar.now()).toStr(":")));
        row = sheet.createRow(1);
        row = sheet.createRow(2);
        c = row.createCell((short) 2);
        c.setCellStyle(style2);
        c.setCellValue(new HSSFRichTextString(session.getLabel("LISTCOMFISSTAT")));

        row = sheet.createRow(3);
        // création de l'entête
        try {
            row = sheet.createRow(4);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(session.getLabel("ANNEE")));
            c.setCellStyle(style2);
            c = row.createCell((short) 1);
            c.setCellStyle(style3);
            c.setCellValue(new HSSFRichTextString(getFromAnnee() + " - " + getToAnnee()));
            row = sheet.createRow(5);
            c = row.createCell((short) 0);
            c.setCellStyle(style2);
            c.setCellValue(new HSSFRichTextString(session.getLabel("LISTE_AFF_DEB")));
            c = row.createCell((short) 1);
            c.setCellStyle(style3);
            c.setCellValue(new HSSFRichTextString(getFromNumAffilie()));
            c = row.createCell((short) 2);
            c.setCellStyle(style2);
            c.setCellValue(new HSSFRichTextString(session.getLabel("LISTE_AFF_FIN")));
            c = row.createCell((short) 3);
            c.setCellStyle(style3);
            c.setCellValue(new HSSFRichTextString(getToNumAffilie()));
            //
            row = sheet.createRow(6);
            c = row.createCell((short) 0);
            c.setCellStyle(style2);
            c.setCellValue(new HSSFRichTextString(getSession().getLabel("GENRE_DECISION")));
            c = row.createCell((short) 1);
            c.setCellStyle(style3);
            String lib = "";
            if ((getGenreAffilie() != null) && !JadeStringUtil.isEmpty(getGenreAffilie())) {
                lib = globaz.phenix.translation.CodeSystem.getLibelle(getSession(), getGenreAffilie());
            }
            c.setCellValue(new HSSFRichTextString(lib));

        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        // let's use a nifty font for the title
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setFont(font2);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
        style.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);
        // create Title Row
        row = sheet.createRow(8);
        for (int i = 1; i <= COL_TITLES.length; i++) {
            c = row.createCell((short) i);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[i - 1]));
            c.setCellStyle(style);
        }
        return sheet;
    }

    public void setToAnnee(String toAnnee) {
        this.toAnnee = toAnnee;
    }

    public void setToNumAffilie(String toNumAffilie) {
        this.toNumAffilie = toNumAffilie;
    }
}
