package globaz.pavo.process;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.db.inscriptions.CIJournalManager;
import globaz.pavo.print.itext.CIEcrituresNonRA_DS;
import globaz.pavo.print.itext.CIEcrituresNonRA_Doc;

public class CIImpressionEcrituresSuspens extends BProcess {

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

        CIJournalManager jourMgr = new CIJournalManager();
        jourMgr.setSession(getSession());
        jourMgr.setForIdEtat(CIJournal.CS_PARTIEL);
        jourMgr.changeManagerSize(BManager.SIZE_NOLIMIT);
        jourMgr.setOrderBy("MALNAF");
        jourMgr.setForAnneeCotisation("2009");
        jourMgr.find();
        int nbTraites = 0;
        System.out.println("Start : " + jourMgr.size());
        for (int i = 0; i < jourMgr.size(); i++) {
            CIJournal journal = (CIJournal) jourMgr.get(i);
            CIEcrituresNonRA_DS manager = new CIEcrituresNonRA_DS();
            manager.setSession(getSession());
            manager.setEcrituresNonRA(true);
            manager.setForIdJournal(journal.getIdJournal());
            manager.setCertificat(true);
            manager.setExclureRA(true);
            if (manager.getCount() > 0
                    && (CIJournal.CS_DECLARATION_COMPLEMENTAIRE.equals(journal.getIdTypeInscription()) || CIJournal.CS_DECLARATION_SALAIRES
                            .equals(journal.getIdTypeInscription()))) {
                nbTraites++;
                if (nbTraites % 100 == 0) {
                    System.out.println("Nb traites : " + nbTraites);
                }
                CIEcrituresNonRA_Doc document = new CIEcrituresNonRA_Doc();
                document.setParent(this);
                document.setSession(getSession());
                document.setIdJournal(journal.getIdJournal());
                AFAffiliation affilie = new AFAffiliation();
                affilie.setSession(getSession());
                affilie.setAffiliationId(journal.getIdAffiliation());
                affilie.retrieve();

                document.setIdAffilie(affilie.getAffilieNumero());
                document.executeProcess();

            }
        }
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        mergePDF(docInfo, true, 500, false, null);
        return true;
    }

    @Override
    protected String getEMailObject() {
        // TODO Auto-generated method stub
        return "L'impression de la réclamation des CA s'est effectuée avec succès";
    }

    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Auto-generated method stub
        return GlobazJobQueue.READ_LONG;
    }

}
