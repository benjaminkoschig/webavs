package globaz.pavo.process;

import globaz.commons.nss.NSUtil;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.pavo.db.compte.CIEcrituresSomme;
import globaz.pavo.db.compte.CINSSRACompteIndividuel;
import globaz.pavo.db.compte.CINSSRACompteIndividuelManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import ch.globaz.common.exceptions.CommonTechnicalException;

public class CIControleCIInactifInvalideProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String CSV_CONTROLE_CI_INACTIF_INVALIDE_OUTPUT_FILE_NAME = "controleCIInactifInvalide.csv";

    private List<String> listCsvLine;

    public CIControleCIInactifInvalideProcess() {
        super();
        listCsvLine = new ArrayList<String>();
    }

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {

        boolean processOnError = false;

        String mailDetailMessage = getSession().getLabel("CI_CONTROLE_CI_INACTIF_INVALIDE_MAIL_DETAIL_MESSAGE_SUCCES");

        try {
            createCsvHeaderLine();
            controleCIInactifInvalide();
            String theCsvFilePath = createCsvFile();
            publieCsvFile(theCsvFilePath);

            if (isAborted() || isOnError() || getSession().hasErrors()) {
                processOnError = true;
                mailDetailMessage = getSession().getLabel("CI_CONTROLE_CI_INACTIF_INVALIDE_MAIL_DETAIL_MESSAGE_ERREUR");

            }

        } catch (Exception e) {
            processOnError = true;
            mailDetailMessage = getSession().getLabel("CI_CONTROLE_CI_INACTIF_INVALIDE_MAIL_DETAIL_MESSAGE_ERREUR")
                    + " : " + e.toString();

        }

        getMemoryLog().logMessage(mailDetailMessage, FWMessage.INFORMATION, this.getClass().getName());

        return processOnError;
    }

    private void addALineInListCsvLine(String nss, String nom, String message) {
        listCsvLine.add(nss + ";" + nom + ";" + message + ";" + IOUtils.LINE_SEPARATOR);
    }

    private void controleCIInactif(CINSSRACompteIndividuel ciNSSRACompteIndividuelEntity) {

        if (!ciNSSRACompteIndividuelEntity.getCiInactif()) {
            String theMessage = getSession()
                    .getLabel("CI_CONTROLE_CI_INACTIF_ERREUR_CI_TOUJOURS_ACTIF_REGISTRE_CAISSE");
            addALineInListCsvLine(NSUtil.formatAVSUnknown(ciNSSRACompteIndividuelEntity.getNumeroAVS()),
                    ciNSSRACompteIndividuelEntity.getNomPrenom(), theMessage);
        }
    }

    private void controleCIInactifInvalide() throws Exception {

        CINSSRACompteIndividuelManager ciNSSRACompteIndividuelManager = new CINSSRACompteIndividuelManager();
        ciNSSRACompteIndividuelManager.setSession(getSession());
        ciNSSRACompteIndividuelManager.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < ciNSSRACompteIndividuelManager.size(); i++) {
            CINSSRACompteIndividuel ciNSSRACompteIndividuelEntity = (CINSSRACompteIndividuel) ciNSSRACompteIndividuelManager
                    .getEntity(i);

            if ("2".equalsIgnoreCase(ciNSSRACompteIndividuelEntity.getCodeMutation())) {
                controleCIInactif(ciNSSRACompteIndividuelEntity);
            } else if ("3".equalsIgnoreCase(ciNSSRACompteIndividuelEntity.getCodeMutation())) {
                controleCIInvalide(ciNSSRACompteIndividuelEntity);
            } else {
                throw new CommonTechnicalException("Not Implemented");
            }
        }

    }

    private void controleCIInvalide(CINSSRACompteIndividuel ciNSSRACompteIndividuelEntity) throws Exception {

        if (!ciNSSRACompteIndividuelEntity.getCiInvalide()) {
            String theMessage = getSession().getLabel("CI_CONTROLE_CI_INVALIDE_ERREUR_CI_PAS_INVALIDE_REGISTRE_CAISSE");
            addALineInListCsvLine(NSUtil.formatAVSUnknown(ciNSSRACompteIndividuelEntity.getNumeroAVS()),
                    ciNSSRACompteIndividuelEntity.getNomPrenom(), theMessage);
        }

        if (ciNSSRACompteIndividuelEntity.getCiOuvert()) {
            String theMessage = getSession().getLabel("CI_CONTROLE_CI_INVALIDE_ERREUR_CI_PAS_FERME_REGISTRE_CAISSE");
            addALineInListCsvLine(NSUtil.formatAVSUnknown(ciNSSRACompteIndividuelEntity.getNumeroAVS()),
                    ciNSSRACompteIndividuelEntity.getNomPrenom(), theMessage);
        }

        if (!isRevenuTotalCIZero(ciNSSRACompteIndividuelEntity.getIdCI())) {
            String theMessage = getSession().getLabel(
                    "CI_CONTROLE_CI_INVALIDE_ERREUR_CI_PAS_TOTAL_REVENU_ZERO_REGISTRE_CAISSE");
            addALineInListCsvLine(NSUtil.formatAVSUnknown(ciNSSRACompteIndividuelEntity.getNumeroAVS()),
                    ciNSSRACompteIndividuelEntity.getNomPrenom(), theMessage);
        }

    }

    private String createCsvFile() throws Exception {

        String filePath = Jade.getInstance().getPersistenceDir()
                + JadeFilenameUtil
                        .addFilenameSuffixUID(CIControleCIInactifInvalideProcess.CSV_CONTROLE_CI_INACTIF_INVALIDE_OUTPUT_FILE_NAME);

        JadeFsFacade.writeFile(getLignesInByteFormat(listCsvLine), filePath);

        return filePath;

    }

    private void createCsvHeaderLine() {
        addALineInListCsvLine(getSession().getLabel("CI_CONTROLE_CI_INACTIF_INVALIDE_CSV_COLUMN_NAME_NSS"),
                getSession().getLabel("CI_CONTROLE_CI_INACTIF_INVALIDE_CSV_COLUMN_NAME_NOM"),
                getSession().getLabel("CI_CONTROLE_CI_INACTIF_INVALIDE_CSV_COLUMN_NAME_MESSAGE"));
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("CI_CONTROLE_CI_INACTIF_INVALIDE_MAIL_SUBJECT");
    }

    @Override
    public boolean getForceCompletionMail() {
        return true;
    }

    private List<Byte> getLignesInByteFormat(List<String> lignes) {
        List<Byte> lignesInByteFormat = new ArrayList<Byte>();

        if (lignes != null) {
            for (String aLigne : lignes) {
                for (byte aByte : aLigne.getBytes()) {
                    lignesInByteFormat.add(Byte.valueOf(aByte));
                }
            }
        }

        return lignesInByteFormat;
    }

    public List<String> getListCsvLine() {
        return listCsvLine;
    }

    private boolean isRevenuTotalCIZero(String theCompteIndividuelId) throws Exception {

        CIEcrituresSomme somme = new CIEcrituresSomme();
        somme.setSession(getSession());

        // Si ForAllEcritures contient une chaîne de caractères quelconque et
        // que ForCompteIndividuelId est setté
        // alors les écritures CIEcriture.CS_CI_SUSPENS_SUPPRIMES et CIEcriture.CS_CORRECTION ne sont pas prises en
        // compte
        somme.setForAllEcritures("all");
        somme.setForCompteIndividuelId(theCompteIndividuelId);

        BigDecimal result = somme.getSum("KBMMON", null);

        return result.doubleValue() == 0;

    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    private void publieCsvFile(String theFilePath) throws Exception {

        JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
        this.registerAttachedDocument(docInfoExcel, theFilePath);

    }

    public void setListCsvLine(List<String> listCsvLine) {
        this.listCsvLine = listCsvLine;
    }

}
