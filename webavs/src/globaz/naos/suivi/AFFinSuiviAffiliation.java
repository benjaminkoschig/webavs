package globaz.naos.suivi;

import globaz.globall.api.BIApplication;
import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.jade.log.JadeLogger;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.process.handler.LEJournalHandler;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.lupus.db.journalisation.LUJournalViewBean;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.phenix.db.principale.CPDecision;
import globaz.pyxis.db.tiers.TIRole;

public class AFFinSuiviAffiliation extends BAbstractEntityExternalService {

    @Override
    public void afterAdd(BEntity entity) throws Throwable {
    }

    @Override
    public void afterDelete(BEntity entity) throws Throwable {
    }

    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
    }

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeAdd(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeDelete(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeRetrieve(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeUpdate(BEntity entity) throws Throwable {
        CPDecision decision = (CPDecision) entity;
        if (CPDecision.CS_FACTURATION.equals(decision.getDernierEtat())
                || CPDecision.CS_PB_COMPTABILISATION.equals(decision.getDernierEtat())) {
            genererReception(decision);
        }
    }

    private void genererReception(CPDecision decision) {
        try {
            BTransaction transaction = decision.withTransactionForExternalService();
            if (transaction == null) {
                transaction = decision.getSession().getCurrentThreadTransaction();
            }

            // Création d'une session LEO
            BIApplication remoteApplication = GlobazServer.getCurrentSystem().getApplication("LEO");
            BSession sessionLeo = (BSession) remoteApplication.newSession(decision.getSession());
            // On sette les critères qui nous permettent de retrouver l'envoi
            // (si il en a un)
            AFAffiliation aff = new AFAffiliation();
            aff.setSession(decision.getSession());
            aff.setAffiliationId(decision.getIdAffiliation());
            aff.retrieve();

            LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO, aff.getAffilieNumero());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                    AFApplication.DEFAULT_APPLICATION_NAOS);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS, decision.getIdTiers());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_AFFILIATION, decision.getIdAffiliation());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE, decision.getAnneeDecision());

            // On recherche dans la gestion des envois s'il y a un envoi qui
            // concerne le relevé qu'on est en train d'ajouter
            LUJournalListViewBean viewBean = new LUJournalListViewBean();
            viewBean.setSession(decision.getSession());
            viewBean.setProvenance(provenanceCriteres);
            viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_CATEGORIE_GROUPE);
            viewBean.setForValeurCodeSysteme(ILEConstantes.CS_CATEGORIES_NOUVELLE_AFFILIATION);
            viewBean.setForIdSuivant("0");
            viewBean.find();

            for (int i = 0; i < viewBean.size(); i++) {
                LUJournalViewBean vBean = (LUJournalViewBean) viewBean.getEntity(i);
                // On génère la reception du document dans LEO avec la date du
                // jour
                LEJournalHandler journalHandler = new LEJournalHandler();
                journalHandler.genererJournalisationReception(vBean.getIdJournalisation(),
                        String.valueOf(JACalendar.today()), sessionLeo, transaction);
            }

            // On recherche dans la gestion des envois s'il y a un envoi qui
            // concerne le relevé qu'on est en train d'ajouter
            viewBean = new LUJournalListViewBean();
            viewBean.setSession(decision.getSession());
            viewBean.setProvenance(provenanceCriteres);
            viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_CATEGORIE_GROUPE);
            viewBean.setForValeurCodeSysteme(ILEConstantes.CS_CATEGORIE_RADIATION);
            viewBean.setForIdSuivant("0");
            viewBean.find();

            for (int i = 0; i < viewBean.size(); i++) {
                LUJournalViewBean vBean = (LUJournalViewBean) viewBean.getEntity(i);
                // On génère la reception du document dans LEO avec la date du
                // jour
                LEJournalHandler journalHandler = new LEJournalHandler();
                journalHandler.genererJournalisationReception(vBean.getIdJournalisation(),
                        String.valueOf(JACalendar.today()), sessionLeo, transaction);
            }
        } catch (Exception e) {
            // On catch les exceptions, car les mauvais suivis déjà générés
            // engendrent des erreurs lors de la comptabilisation
            JadeLogger.warn(this, "Erreur lors de la génération de la réception du suivi " + e.toString());
        }
    }

    @Override
    public void init(BEntity entity) throws Throwable {
    }

    @Override
    public void validate(BEntity entity) throws Throwable {
    }
}
