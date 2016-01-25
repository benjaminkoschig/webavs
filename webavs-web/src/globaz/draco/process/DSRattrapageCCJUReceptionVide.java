package globaz.draco.process;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.draco.application.DSApplication;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BIApplication;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.process.handler.LEJournalHandler;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.lupus.db.journalisation.LUJournalViewBean;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CACompteur;
import globaz.osiris.db.comptes.CACompteurManager;
import globaz.pavo.application.CIApplication;
import globaz.pyxis.db.tiers.TIRole;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;

public class DSRattrapageCCJUReceptionVide extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        java.io.BufferedReader fileIn = new java.io.BufferedReader(new java.io.FileReader("d:/ti/StatScannageDSA.log"));
        File outputFile = new File("d:/ti/out.txt");
        FileWriter out = new FileWriter(outputFile);
        String line = "";
        java.util.StringTokenizer tokens;

        while ((line = fileIn.readLine()) != null) {
            tokens = new java.util.StringTokenizer(line, ";");
            // On recherche 2 fois car la première colonne ne nous intéresse pas
            String noAffilie = tokens.nextToken();
            noAffilie = tokens.nextToken();

            String date = tokens.nextToken();
            date = tokens.nextToken();
            date = tokens.nextToken();
            // date = tokens.nextToken();
            date = JadeStringUtil.removeChar(date, '-');
            // JADate dateJA = new JADate(date);
            // date =dateJA.toStr(".");
            JADate dateJa = new JADate(new BigDecimal(date));
            date = JACalendar.format(dateJa, JACalendar.FORMAT_DDsMMsYYYY);
            String nbAssures = tokens.nextToken();
            nbAssures = tokens.nextToken();
            if (JadeStringUtil.isIntegerEmpty(nbAssures)) {
                // Regarger ne compta si un des compteurs existe
                String resultCa = getTotalCompteur(noAffilie, getSession());
                if (JadeStringUtil.isBlankOrZero(resultCa)) {
                    System.out.println(noAffilie);
                    try {
                        // Creér réception

                        effectueReception(noAffilie, date, "2013");
                    } catch (Exception e) {
                        System.out.println("Aucun envoi : " + noAffilie);
                    }
                } else {
                    out.write(noAffilie + " " + date);
                }
            }
        }
        out.close();
        // TODO Auto-generated method stub
        return false;
    }

    private void effectueReception(String noAffilie, String dateReception, String annee) throws Exception {
        AFAffiliation affilie = null;
        try {
            CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);

            affilie = application.getAffilieByNo(getSession(), noAffilie, false, false, "", "", annee, "", "");
        } catch (Exception e) {

        }

        String idTiers = affilie.getIdTiers();
        LUJournalListViewBean viewBean = new LUJournalListViewBean();
        // On sette les données nécessaires à la recherche de l'envoi
        LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS, idTiers);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO, noAffilie);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                DSApplication.DEFAULT_APPLICATION_DRACO);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS, idTiers);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE, annee);
        // On va regarder s'il y a déjà un envoi correspondant aux critères qui
        // nous intéressent.
        viewBean.setSession(getSession());
        viewBean.setProvenance(provenanceCriteres);
        viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_CATEGORIE_GROUPE);
        viewBean.setForValeurCodeSysteme(ILEConstantes.CS_CATEGORIE_SUIVI_DS);
        viewBean.find();
        // Dans le cas où il existe un envoi, on va mettre la date de réception
        // dans LEO
        if (viewBean.size() > 0) {
            LUJournalViewBean vBean = new LUJournalViewBean();
            // Comme les critères entrés assurent l'unicité de l'envoi, on est
            // sûrs qu'une seule ligne est retournée par la requete
            vBean = (LUJournalViewBean) viewBean.getFirstEntity();
            // On génère la reception du document dans LEO avec la date du jour
            // Création d'une session LEO
            BIApplication remoteApplication = GlobazServer.getCurrentSystem().getApplication("LEO");
            BSession sessionLeo = (BSession) remoteApplication.newSession(getSession());
            LEJournalHandler journalHandler = new LEJournalHandler();

            try {
                journalHandler.genererJournalisationReception(vBean.getIdJournalisation(), dateReception, sessionLeo,
                        getTransaction());

                getMemoryLog().logMessage(noAffilie + " mis à jour", FWMessage.INFORMATION, "Rattrapage Envoi");
                System.out.println(noAffilie + " mis à jour");
            } catch (Exception e) {

            }
        }

    }

    @Override
    protected String getEMailObject() {
        // TODO Auto-generated method stub
        return "le rattrapage est terminé";
    }

    private String getTotalCompteur(String numeroAffilie, BSession session) {
        String resultCA = new String();
        CACompteAnnexeManager mgrMasse = new CACompteAnnexeManager();
        CACompteur cptr = new CACompteur();
        CACompteurManager mgrCompteur = new CACompteurManager();
        mgrMasse.setSession(session);
        mgrMasse.setForIdExterneRole(numeroAffilie);
        try {
            CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
            mgrMasse.setForIdRole(CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(application));

            mgrMasse.find();

            if (mgrMasse.size() != 0) {
                CACompteAnnexe compte = new CACompteAnnexe();
                compte = (CACompteAnnexe) mgrMasse.getFirstEntity();
                mgrCompteur.setSession(session);
                mgrCompteur.setForAnnee(String.valueOf(2013));
                mgrCompteur.setForIdCompteAnnexe(compte.getIdCompteAnnexe());
                mgrCompteur.setForIdRubriqueIn("28,30,1005,1006");
                try {
                    resultCA = mgrCompteur.getSum("CUMULMASSE").toString();

                } catch (Exception e) {
                    resultCA = "0";
                }
            }

        } catch (Exception e) {

        }
        return resultCA;

    }

    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Auto-generated method stub
        return GlobazJobQueue.UPDATE_LONG;
    }

}
