package globaz.apg.excel;

import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import globaz.apg.db.liste.APListePrestationsMensuellesModel;
import globaz.apg.process.APGenererDroitPandemieMensuelProcess;
import globaz.corvus.excel.REAbstractListExcel;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.log.JadeLogger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import java.sql.SQLException;
import java.util.List;

public class APListeDroitPrestationMensuelExcel extends REAbstractListExcel {

    private static final short COLUMN_WIDTH_BIG = 12000;
    private static final short COLUMN_WIDTH_MEDIUM = 7000;

    private List<APListePrestationsMensuellesModel> list;
    private HSSFCellStyle styleHeader;

    //HEADER
    private static final String HEADER_COL_1 = "DOC_LISTE_PRESTATIONS_MENSUELLE_NSS";
    private static final String HEADER_COL_2 = "DOC_LISTE_PRESTATIONS_MENSUELLE_DESIGNATION";
    private static final String HEADER_COL_3 = "DOC_LISTE_PRESTATIONS_MENSUELLE_ID_DROIT";
    private static final String HEADER_COL_4 = "DOC_LISTE_PRESTATIONS_MENSUELLE_SERVICE";
    private static final String HEADER_COL_5 = "DOC_LISTE_PRESTATIONS_MENSUELLE_MONTANT";
    private static final String HEADER_COL_6 = "DOC_LISTE_PRESTATIONS_MENSUELLE_DATE_DE_DEBUT";
    private static final String HEADER_COL_7 = "DOC_LISTE_PRESTATIONS_MENSUELLE_DATE_DE_FIN";
    private static final String HEADER_COL_8 = "DOC_LISTE_PRESTATIONS_MENSUELLE_ACTION";

    public static final String ACTION_SUPPRIME = "DOC_LISTE_PRESTATIONS_MENSUELLE_ACTION_SUPPRIME";
    public static final String ACTION_AJOUTE = "DOC_LISTE_PRESTATIONS_MENSUELLE_ACTION_AJOUTE";
    public static final String ACTION_MODIFIE = "DOC_LISTE_PRESTATIONS_MENSUELLE_ACTION_MODIFIE";

    public static final String VERIFIER_INDEPENDANT = "DOC_VERIFIER_INDEPENDANT";

    public APListeDroitPrestationMensuelExcel(BSession session) {

        super(session, "APListeDroitPrestationMensuel", session.getLabel(APGenererDroitPandemieMensuelProcess.LABEL_TITRE_DOCUMENT));

        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
            styleHeader = getWorkbook().createCellStyle();
            styleHeader.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            styleHeader.setFont(getFontBold());
        } catch (SQLException ex) {
            JadeLogger.error(APListeDroitPrestationMensuelExcel.class,ex.getMessage());
        }finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }

    public List<APListePrestationsMensuellesModel> getList() {
        return list;
    }

    public void setList(List<APListePrestationsMensuellesModel> list) {
        this.list = list;
    }

    public void creerDocument() {
        HSSFSheet currentSheet = createSheet(getSession().getLabel(APGenererDroitPandemieMensuelProcess.LABEL_TITRE_DOCUMENT));
        initPage(true);
        initColumnWidthSheetListePrestation();
        createHeaderRows();
        createDataRows();
        //Adaptation de la colonne.
        currentSheet.autoSizeColumn((short) 0);
        currentSheet.autoSizeColumn((short) 1);
        currentSheet.autoSizeColumn((short) 2);
        currentSheet.autoSizeColumn((short) 3);
        currentSheet.autoSizeColumn((short) 4);
        currentSheet.autoSizeColumn((short) 5);
        currentSheet.autoSizeColumn((short) 6);
        currentSheet.autoSizeColumn((short) 7);
        currentSheet.autoSizeColumn((short) 8);
    }

    private void createDataRows() {
        for (APListePrestationsMensuellesModel model : list) {
            createRow();
            this.createCell(model.getNss());
            this.createCell(model.getNom());
            this.createCell(model.getIdDroit());
            this.createCell(model.getService());
            this.createCell(model.getMontant());
            this.createCell(model.getDateDebut());
            this.createCell(model.getDateFin());

            String comment = "";
            if(model.getIndependant()){
                comment = " - " + getSession().getLabel(VERIFIER_INDEPENDANT);
            }

            this.createCell(model.getAction()+comment);

        }
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

    private void initColumnWidthSheetListePrestation() {

        short numCol = 0;

        currentSheet.setColumnWidth(numCol++, COLUMN_WIDTH_MEDIUM);
        currentSheet.setColumnWidth(numCol++, COLUMN_WIDTH_MEDIUM);
        currentSheet.setColumnWidth(numCol++, COLUMN_WIDTH_MEDIUM);
        currentSheet.setColumnWidth(numCol++, COLUMN_WIDTH_MEDIUM);
        currentSheet.setColumnWidth(numCol++, COLUMN_WIDTH_MEDIUM);
        currentSheet.setColumnWidth(numCol++, COLUMN_WIDTH_MEDIUM);
        currentSheet.setColumnWidth(numCol++, COLUMN_WIDTH_MEDIUM);
        currentSheet.setColumnWidth(numCol++, COLUMN_WIDTH_MEDIUM);
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

}
