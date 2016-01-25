package globaz.cygnus.process.adaptationAnnuelle.frqppp;

import globaz.cygnus.api.paiement.IRFPrestations;
import globaz.cygnus.db.paiement.RFPrestationAccordeeJointREPrestationAccordee;
import globaz.cygnus.db.paiement.RFPrestationAccordeeJointREPrestationAccordeeManager;
import globaz.cygnus.mappingXmlml.IRFAdaptationsJournalieresListeColumns;
import globaz.cygnus.mappingXmlml.RFXmlmlMappingLogAdaptationJournaliere;
import globaz.cygnus.utils.RFExcelmlUtils;
import globaz.cygnus.utils.RFXmlmlContainer;
import globaz.globall.db.BSessionUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepAfterable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.horizon.jaspe.util.JACalendar;

public class RFProcessAdaptationAnnuelleFrqpppStep implements JadeProcessStepInterface, JadeProcessStepAfterable {

    private List<String[]> logsList = new ArrayList<String[]>();

    @Override
    public void after(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {

        try {

            // On vérifie si toutes les RFAccordées de type frqp-pp ont été traitées
            RFPrestationAccordeeJointREPrestationAccordeeManager rfPrestAccJointRePrestAccMgr1 = new RFPrestationAccordeeJointREPrestationAccordeeManager();
            rfPrestAccJointRePrestAccMgr1.setSession(BSessionUtil.getSessionFromThreadContext());

            rfPrestAccJointRePrestAccMgr1.setForDateDiminution("01.12." + String.valueOf(JACalendar.today().getYear()));
            rfPrestAccJointRePrestAccMgr1.setForIsAdaptation(true);
            rfPrestAccJointRePrestAccMgr1.changeManagerSize(0);
            rfPrestAccJointRePrestAccMgr1.find();
            String nbRfmAccordeeDiminuees = String.valueOf(rfPrestAccJointRePrestAccMgr1.size());

            RFPrestationAccordeeJointREPrestationAccordeeManager rfPrestAccJointRePrestAccMgr2 = new RFPrestationAccordeeJointREPrestationAccordeeManager();
            rfPrestAccJointRePrestAccMgr2.setSession(BSessionUtil.getSessionFromThreadContext());
            rfPrestAccJointRePrestAccMgr2
                    .setForCsSourceRfmAccordee(new String[] { IRFPrestations.CS_SOURCE_RFACCORDEES_FRQP });
            rfPrestAccJointRePrestAccMgr2.setDateDiminutionNull(true);
            rfPrestAccJointRePrestAccMgr2.setForEnCoursAtMois("01." + String.valueOf(JACalendar.today().getYear() + 1));
            rfPrestAccJointRePrestAccMgr2.changeManagerSize(0);
            rfPrestAccJointRePrestAccMgr2.find();
            Iterator<RFPrestationAccordeeJointREPrestationAccordee> rfPrestAccJointRePrestAccItr2 = rfPrestAccJointRePrestAccMgr2
                    .iterator();

            String nbRfmAccordeeNonDiminuees = String.valueOf(rfPrestAccJointRePrestAccMgr2.size());
            StringBuffer detailRfmAccordeeNonDiminuees = new StringBuffer();

            while (rfPrestAccJointRePrestAccItr2.hasNext()) {

                RFPrestationAccordeeJointREPrestationAccordee prestCourante = rfPrestAccJointRePrestAccItr2.next();

                PRTiersWrapper prTiersWrapper = PRTiersHelper.getTiersParId(BSessionUtil.getSessionFromThreadContext(),
                        prestCourante.getIdTiers());
                detailRfmAccordeeNonDiminuees.append(prTiersWrapper.getNSS() + " " + prTiersWrapper.getNom() + " "
                        + prTiersWrapper.getPrenom() + " No Prestation: " + prestCourante.getIdRFMAccordee() + "\n");

            }

            // Liste log
            RFXmlmlContainer container = RFXmlmlMappingLogAdaptationJournaliere.loadResults(logsList,
                    new ArrayList<String>(), null);

            String nomDoc = BSessionUtil.getSessionFromThreadContext().getLabel("PROCESS_ADAPTATION_ANNUELLE_FRQPPP");
            String docPath = RFExcelmlUtils.createDocumentExcel(BSessionUtil.getSessionFromThreadContext()
                    .getIdLangueISO().toUpperCase()
                    + "/" + IRFAdaptationsJournalieresListeColumns.MODEL_NAME, nomDoc, container);

            JadeSmtpClient.getInstance().sendMail(
                    BSessionUtil.getSessionFromThreadContext().getUserEMail(),
                    "Process adaptation annuelle RFM: Frqp PP",
                    "Nombre de RFMAccordées diminuées: " + nbRfmAccordeeDiminuees + "\n"
                            + "Nombre de RFMAccordées non diminuées: " + nbRfmAccordeeNonDiminuees + "\n\n"
                            + "Détail RFMAccordées non diminuées:\n" + detailRfmAccordeeNonDiminuees.toString(),
                    new String[] { docPath });

        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
        }

    }

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new RFProcessAdaptationAnnuelleFrqpppHandlerEntity(logsList);
    }

}
