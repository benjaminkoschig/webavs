package globaz.cygnus.process.importationTmr.miseAJourFichierSourceStep4;

import ch.globaz.common.util.errordriller.ErrorDriller;
import ch.globaz.common.util.errordriller.ErrorDriller.DrilledError;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepAfterable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.jade.process.utils.JadeProcessCommonUtils;
import globaz.cygnus.mappingXmlml.IRFImportationTmrListeColumns;
import globaz.cygnus.mappingXmlml.RFXmlmlMappingLogImportationTmr;
import globaz.cygnus.process.importationTmr.RFTmrException;
import globaz.cygnus.process.importationTmr.miseAJourFichierSourceStep4.RFImportDemandesCmsMajFichierSourceHandler.PositionsAMettreAJourFichierSourceEnum;
import globaz.cygnus.utils.*;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;

import java.io.*;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class RFImportDemandesCmsMajFichierSource implements JadeProcessStepInterface, JadeProcessStepBeforable,
        JadeProcessStepAfterable {

    private Map<String, String> lignes = new ConcurrentHashMap<String, String>();
    private List<String[]> logsList = Collections.synchronizedList(new ArrayList<String[]>());
    private RFMyBigDecimal montantAccepteTotal = new RFMyBigDecimal(new BigDecimal("0"));
    private RFLogToDB tmrDebugLogger;

    @Override
    public void after(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        try {
            envoyerMail(creerFichierTexte(step.getIdExecutionProcess()), generateDocumentLog(map, logsList), map);
            if (tmrDebugLogger != null) {// are we sure 'before' always complete?...
                tmrDebugLogger.logInfoToDB("ending Tmr step", "Tmr - step 4 - after");
            }
        } catch (Exception e) {
            JadeProcessCommonUtils.addError(e);
        }
    }

    @Override
    public void before(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        // on vérifie qu'on n'a pas déjà des erreurs en session.
        BSession sess = BSessionUtil.getSessionFromThreadContext();
        ErrorDriller ed = new ErrorDriller().add(sess).lookInThreadContext();
        List<DrilledError> errors = ed.drill();
        if (errors.size() > 0) {
            // oups, des méchantes erreurs... on les log et on fait tout péter, pour éviter des données incohérentes
            // plus loin
            String message = MessageFormat.format(sess.getLabel("RF_IMPORT_TMR_PROCESS_ERRORS_DRILLED"),
                    errors.size());
            JadeLogger.error(this, message);
            for (DrilledError e : errors) {
                JadeLogger.error(this, e.toString());
            }
            throw new RFTmrException("Tmr step 4. " + message, null);
        }
        tmrDebugLogger = new RFLogToDB(BSessionUtil.getSessionFromThreadContext());
        tmrDebugLogger.logInfoToDB("starting Tmr step", "Tmr - step 4 - before");
    }

    private String creerFichierTexte(String idExecutionProcess) throws IOException {

        String chemin = Jade.getInstance().getPersistenceDir() + "_"
                + BSessionUtil.getSessionFromThreadContext().getLabel("RF_IMPORT_TMR_NOM_FICHIER_SOURCE_MAJ") + "_"
                + idExecutionProcess + "_" + JadeUUIDGenerator.createStringUUID() + ".txt";

        File fichier = new File(chemin);
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fichier)));

        List<String> numeroLignesTriees = trierParNumeroLignes();
        int i = 1;
        for (String ligneCourante : numeroLignesTriees) {
            if (numeroLignesTriees.size() > i) {
                pw.println(ligneCourante);
            } else {
                pw.println(majDerniereLigne(ligneCourante));
            }
            i++;
        }

        pw.flush();
        pw.close();

        return chemin;
    }

    private void envoyerMail(String cheminFichier, String cheminLog, Map<Enum<?>, String> map) throws Exception {

        JadeSmtpClient.getInstance().sendMail(
                getEmail(map),
                BSessionUtil.getSessionFromThreadContext().getLabel("RF_IMPORT_TMR_PROCESS_NAME"),
                /* !JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR) */!RFUtils
                        .hasErrorsLogList(logsList) ? BSessionUtil.getSessionFromThreadContext().getLabel(
                        "RF_IMPORT_TMR_PROCESS_MAJ_FICHIER_SOURCE_SUCCESS") : BSessionUtil
                        .getSessionFromThreadContext().getLabel("RF_IMPORT_TMR_PROCESS_MAJ_FICHIER_SOURCE_FAILED"),
                new String[] { cheminFichier, cheminLog });
    }

    private String generateDocumentLog(Map<Enum<?>, String> map, List<String[]> logsList) throws Exception {

        RFXmlmlContainer container = RFXmlmlMappingLogImportationTmr.loadResults(logsList, BSessionUtil
                .getSessionFromThreadContext().getUserId());

        String nomDoc = BSessionUtil.getSessionFromThreadContext().getLabel("RF_IMPORT_TMR_NOM_LOG_ETAPE_QUATRE");
        String docPath = RFExcelmlUtils.createDocumentExcel(BSessionUtil.getSessionFromThreadContext().getIdLangueISO()
                .toUpperCase()
                + "/" + IRFImportationTmrListeColumns.MODEL_NAME, nomDoc, container);

        return docPath;
    }

    private String getEmail(Map<Enum<?>, String> map) {
        return /* JadeStringUtil.isBlankOrZero(map.get(RFProcessImportationTmrEnum.EMAIL)) ? */BSessionUtil
                .getSessionFromThreadContext().getUserEMail()/* : map.get(RFProcessImportationTmrEnum.EMAIL) */;
    }

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new RFImportDemandesCmsMajFichierSourceHandler(lignes, logsList, montantAccepteTotal);
    }

    private String majDerniereLigne(String ligne) {

        StringBuffer nouvelleLigneStB = new StringBuffer();

        nouvelleLigneStB.append(ligne.substring(0,
                PositionsAMettreAJourFichierSourceEnum.POSITION_DERNIERE_LIGNE.getDebut() - 1));
        nouvelleLigneStB.append(RFImportDemandesCmsMajFichierSourceHandler
                .getMontantFormatteSurNPositionsDontDeuxDecimales(montantAccepteTotal.getValeur().toString(),
                        RFImportDemandesCmsMajFichierSourceHandler.NOMBRE_DE_POSITIONS_FORMAT_DERNIERE_LIGNE));
        nouvelleLigneStB.append(ligne.substring(
                PositionsAMettreAJourFichierSourceEnum.POSITION_DERNIERE_LIGNE.getFin() - 1, ligne.length()));

        // this.lignes.put(this.entityData.getNumeroLigne(), nouvelleLigneStB.toString());

        return nouvelleLigneStB.toString();

    }

    public List<String> trierParNumeroLignes() {

        ArrayList<Integer> numeroLigneList = new ArrayList<Integer>();
        for (String key : lignes.keySet()) {
            numeroLigneList.add(Integer.parseInt(key));
        }

        Collections.sort(numeroLigneList);

        ArrayList<String> ligneTrieesList = new ArrayList<String>();
        for (Integer numLigneCourant : numeroLigneList) {
            ligneTrieesList.add(lignes.get(String.valueOf(numLigneCourant)));
        }

        return ligneTrieesList;
    }

}
