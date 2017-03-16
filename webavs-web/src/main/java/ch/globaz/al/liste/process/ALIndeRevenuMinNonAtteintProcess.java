/**
 * 
 */
package ch.globaz.al.liste.process;

import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAPassageManager;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.al.business.constantes.ALConstIndependantAF;
import ch.globaz.al.liste.container.ALIndeRevenuMinNonAtteintContainer;
import ch.globaz.al.properties.ALProperties;
import ch.globaz.al.web.application.ALApplication;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.common.process.ProcessMailUtils;
import ch.globaz.queryexec.bridge.jade.SCM;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.core.Details;

/**
 * Process qui envoi la liste des indépendants avec un revenu minimal non atteint pour les AF
 * 
 * @author est
 */
public class ALIndeRevenuMinNonAtteintProcess extends BProcess {

    private static final long serialVersionUID = 1L;
    private static final String NUM_INFOROM = "0321CFA";

    private static final Logger logger = LoggerFactory.getLogger(ALIndeRevenuMinNonAtteintProcess.class);

    private List<String> mailsList = new ArrayList<String>();

    private String noPassage;

    private String chaineRevenusMinimaux;
    private String numJournal;
    private String libelle;
    private String dateFacturation;

    @Override
    protected void _executeCleanUp() {
        //
    }

    @Override
    protected boolean _executeProcess() {
        mailsList.add(getEMailAddress());

        try {
            // Récupération des properties et génère la chaine (string) avec les couple année:valeur min
            String revenusMinimauxProperties = ALProperties.REVENUS_MINIMAUX.getValue();
            chaineRevenusMinimaux = new GenerateurAnneeRevenuMinimal()
                    .genererStringAnneeRevenuMinimal(revenusMinimauxProperties);
        } catch (Exception e) {
            ProcessMailUtils.sendMailError(mailsList, e, this, null, null, "Erreur de propriété");
            return false;
        }

        // Création de la liste excel
        setInfosPourListe();

        String sql = getSqlRequest();
        List<ALIndeRevenuMinNonAtteintContainer> listRevenuMinNonAtteint = SCM
                .newInstance(ALIndeRevenuMinNonAtteintContainer.class).session(getSession()).query(sql).execute();

        if (!listRevenuMinNonAtteint.isEmpty()) {
            File file = genererFileExcel(listRevenuMinNonAtteint);
            // Envoi du mail
            sendMail(mailsList, file.getAbsolutePath());
        } else {
            // Envoi du mail disant que la liste est vide
            sendMail(mailsList, "");
        }

        return true;
    }

    /***
     * Défini l'objet du mail quand le process est terminé quand on imprime la liste seul (depuis le viewbean)
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("LISTE_INDE_PROCESSUS_TERMINE");
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    private File genererFileExcel(List<ALIndeRevenuMinNonAtteintContainer> listRevenuMinNonAtteint) {
        Details detail = new Details();
        detail.add(getSession().getLabel("LISTE_INDE_NUM_INFOROM"), NUM_INFOROM);
        detail.newLigne();
        detail.add(getSession().getLabel("LISTE_INDE_NUM_CAISSE"), getNoCaisse());
        detail.newLigne();
        detail.add(getSession().getLabel("LISTE_INDE_TRAITEMENT_DU"), Date.now().toString());
        detail.newLigne();
        detail.add(getSession().getLabel("LISTE_INDE_JOURNAL_DE_FACTURATION"), getInfoJournalFacturation());

        SimpleOutputListBuilderJade simpleOutputListBuilderJade = SimpleOutputListBuilderJade.newInstance();

        File file = simpleOutputListBuilderJade.session(getSession())
                .outputNameAndAddPath(NUM_INFOROM + "_" + getSession().getLabel("LISTE_INDE_EXCEL_NAME")).globazTheme()
                .headerLeftTop(NUM_INFOROM).addTranslater().addList(listRevenuMinNonAtteint)
                .classElementList(ALIndeRevenuMinNonAtteintContainer.class)
                .addTitle(getSession().getLabel("LISTE_INDE_TITRE"), Align.CENTER).addHeaderDetails(detail).asXls()
                .build();
        simpleOutputListBuilderJade.close();
        return file;
    }

    private String getSqlRequest() {
        return "SELECT DISTINCT AFFILIE.MALNAF AS NUM_AFFILIE, AFFILIE.MADESL AS NOM_AFFILIE, CP_DECI.IADDEB AS DEBUT_PERIODE, CP_DECI.IADFIN AS FIN_PERIODE, CP_DONNEE.IHMDCA AS REVENU_DETERMINANT, REVENU_MIN.MINIMAL AS REVENU_MINIMAL,  AF_PRES_ENTETE.EID AS ID_DOSSIER_AF "
                + "FROM SCHEMA.CPDECIP AS CP_DECI "
                + "INNER JOIN SCHEMA.CPDOCAP AS CP_DONNEE  ON CP_DECI.IAIDEC = CP_DONNEE.IAIDEC "
                + "INNER JOIN SCHEMA.CPCOTIP AS CP_COTI ON CP_DECI.IAIDEC = CP_COTI.IAIDEC  "
                + "INNER JOIN SCHEMA.AFAFFIP AS AFFILIE  ON AFFILIE.MAIAFF = CP_DECI.MAIAFF  "
                + "INNER JOIN SCHEMA.ALALLOC AS AF_ALLOCATAIRE  ON AFFILIE.HTITIE = AF_ALLOCATAIRE.HTITIE "
                + "INNER JOIN SCHEMA.ALDOS AS AF_DOSSIER  ON AF_ALLOCATAIRE.BID = AF_DOSSIER.BID "
                + "INNER JOIN SCHEMA.ALENTPRE AS AF_PRES_ENTETE  ON AF_DOSSIER.EID = AF_PRES_ENTETE.EID "
                + "INNER JOIN (SELECT * FROM (VALUES "
                + chaineRevenusMinimaux
                + ") AS TEMP(ANNEE, MINIMAL)) AS REVENU_MIN ON ANNEE = Cast(CP_DECI.IADDEB / 10000 as integer) "
                + "WHERE (AF_PRES_ENTETE.MPERD * 100 ) BETWEEN CP_DECI.IADDEB AND CP_DECI.IADFIN "
                + "AND CP_DECI.IATTDE IN("
                + ALConstIndependantAF.CS_TYPE_DECISION_DEFINITIF
                + ","
                + ALConstIndependantAF.CS_TYPE_DECISION_RECTIFICATION
                + ") "
                + "AND CP_DONNEE.IHIDCA = "
                + ALConstIndependantAF.CS_REVENU_DETERMINANT
                + " AND CP_DECI.IADDEB >= "
                + ALConstIndependantAF.DATE_AMJ_DECISION_MINIMAL
                + " AND CP_DONNEE.IHMDCA < REVENU_MIN.MINIMAL "
                + "AND CP_DECI.IAANNE >= "
                + ALConstIndependantAF.ANNEE_DECISION_MINIMALE
                + " AND AFFILIE.MATTAF IN("
                + ALConstIndependantAF.CS_GENRE_INDEPENDANT
                + ","
                + ALConstIndependantAF.CS_GENRE_INDEPENDANT_ET_EMPLOYEUR + ") ORDER BY NOM_AFFILIE";
    }

    private void sendMail(List<String> mailsList, String joinFilePath) {
        // ajout de la pièce jointe
        List<String> joinsFilesPathsList = new ArrayList<String>();

        String subject;
        String body;

        subject = MessageFormat.format(getSession().getLabel("LISTE_INDE_TITRE"), "");

        // joindre la pièce jointe si le fichier est présent
        if (!joinFilePath.isEmpty()) {
            joinsFilesPathsList.add(joinFilePath);

            body = MessageFormat.format(
                    FWMessageFormat.prepareQuotes(getSession().getLabel("LISTE_INDE_MAIL_BODY"), false), numJournal,
                    libelle, dateFacturation);
        } else {
            body = MessageFormat.format(
                    FWMessageFormat.prepareQuotes(getSession().getLabel("LISTE_INDE_MAIL_BODY_LISTE_VIDE"), false),
                    numJournal, libelle, dateFacturation);
        }

        try {
            // envoi
            ProcessMailUtils.sendMail(mailsList, subject, body, joinsFilesPathsList);
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi du mail", e);
        }
    }

    private String getNoCaisse() {
        try {
            ALApplication app = (ALApplication) GlobazServer.getCurrentSystem().getApplication(
                    ALApplication.DEFAULT_APPLICATION_WEBAF);

            return app.getNoCaisse();
        } catch (Exception e) {
            logger.error("Unabled to retrieve the propertie 'common.noCaisseFormate' " + e);
        }

        return "";
    }

    private String getInfoJournalFacturation() {
        StringBuilder sb = new StringBuilder();
        sb.append(numJournal);
        sb.append(" - ");
        sb.append(libelle);
        sb.append(" ");
        sb.append(getSession().getLabel("LISTE_INDE_DU"));
        sb.append(" ");
        sb.append(dateFacturation);

        return sb.toString();
    }

    public String getNoPassage() {
        return noPassage;
    }

    /**
     * Récupération des informations du passage pour les détails dans le fichier excel
     * 
     * @param passage
     * @return
     * @throws Exception
     */
    public void setInfosPourListe() {
        try {
            FAPassageManager manager = new FAPassageManager();
            manager.setForIdPassage(noPassage);
            manager.setSession(getSession());
            manager.find(BManager.SIZE_NOLIMIT);

            IFAPassage passage = (IFAPassage) manager.getFirstEntity();

            numJournal = passage.getIdPassage();
            libelle = passage.getLibelle();
            dateFacturation = passage.getDateFacturation();
        } catch (Exception e) {
            ProcessMailUtils.sendMailError(mailsList, e, this, null, null);
        }
    }

    public void setNoPassage(String noPassage) {
        this.noPassage = noPassage;
    }
}
