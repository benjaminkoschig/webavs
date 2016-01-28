package globaz.draco.process;

import globaz.draco.application.DSApplication;
import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BIApplication;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.process.handler.LEJournalHandler;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.lupus.db.journalisation.LUJournalViewBean;
import globaz.pyxis.db.tiers.TIRole;

public class DSRattrapageEnvoi extends BProcess {

    private static final long serialVersionUID = -4585323058933117022L;
    public final static int NO_ERROR = 0;
    public final static int ON_ERROR = 200;

    private String annee = "";

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            DSDeclarationListViewBean dsMgr = new DSDeclarationListViewBean();
            dsMgr.setSession(getSession());
            dsMgr.setForAnnee(annee);
            dsMgr.setForTypeDeclaration(DSDeclarationViewBean.CS_PRINCIPALE);
            dsMgr.changeManagerSize(BManager.SIZE_NOLIMIT);
            dsMgr.find();
            for (int i = 0; i < dsMgr.size(); i++) {
                DSDeclarationViewBean ds = (DSDeclarationViewBean) dsMgr.getEntity(i);

                // On recherche dans la gestion des envois s'il y a un envoi qui
                // concerne le relevé qu'on est en train d'ajouter
                LUJournalListViewBean viewBean = new LUJournalListViewBean();
                // On sette les données nécessaires à la recherche de l'envoi
                LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
                provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS, ds.getAffiliation().getIdTiers());
                provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO, ds.getAffiliation()
                        .getAffilieNumero());
                provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
                provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                        DSApplication.DEFAULT_APPLICATION_DRACO);
                provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS, ds.getAffiliation().getIdTiers());
                provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE, ds.getAnnee());
                // On va regarder s'il y a déjà un envoi correspondant aux
                // critères qui nous intéressent.
                viewBean.setSession(getSession());
                viewBean.setProvenance(provenanceCriteres);
                viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_CATEGORIE_GROUPE);
                viewBean.setForValeurCodeSysteme(ILEConstantes.CS_CATEGORIE_SUIVI_DS);
                viewBean.find();
                // Dans le cas où il existe un envoi, on va mettre la date de
                // réception dans LEO
                if (viewBean.size() > 0) {
                    LUJournalViewBean vBean = new LUJournalViewBean();
                    // Comme les critères entrés assurent l'unicité de l'envoi,
                    // on est sûrs qu'une seule ligne est retournée par la
                    // requete
                    vBean = (LUJournalViewBean) viewBean.getFirstEntity();
                    // On génère la reception du document dans LEO avec la date
                    // du jour
                    // Création d'une session LEO
                    BIApplication remoteApplication = GlobazServer.getCurrentSystem().getApplication("LEO");
                    BSession sessionLeo = (BSession) remoteApplication.newSession(getSession());
                    LEJournalHandler journalHandler = new LEJournalHandler();

                    try {
                        journalHandler.genererJournalisationReception(vBean.getIdJournalisation(),
                                ds.getDateRetourEff(), sessionLeo, getTransaction());

                        getMemoryLog().logMessage(ds.getAffiliation().getAffilieNumero() + " mis à jour",
                                FWMessage.INFORMATION, "Rattrapage Envoi");
                        System.out.println(ds.getAffiliation().getAffilieNumero() + " mis à jour");
                    } catch (Exception e) {

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public String getAnnee() {
        return annee;
    }

    @Override
    protected String getEMailObject() {
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

}
