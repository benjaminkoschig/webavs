package globaz.naos.bulletinNeutre;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.log.JadeLogger;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.db.envoi.LEEtapesSuivantesListViewBean;
import globaz.leo.process.LEGenererEnvoi;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.lupus.db.journalisation.LUJournalViewBean;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pyxis.db.tiers.TIRole;
import java.util.ArrayList;
import java.util.HashMap;

public class AFBulletinNeutreUtil {

    public static void creerSuivi(AFAffiliation affiliation, String annee, BSession session, BTransaction transaction)
            throws Exception {
        if (AFBulletinNeutreUtil.isAlreadySent(affiliation, annee, ILEConstantes.CS_DEBUT_SUIVI_BULLETIN_NEUTRE,
                session) == null) {
            // prépare les données pour l'envoi
            HashMap params = new HashMap();
            params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS, affiliation.getIdTiers());
            params.put(ILEConstantes.CS_PARAM_GEN_NUMERO, affiliation.getAffilieNumero());
            params.put(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
            params.put(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE, AFApplication.DEFAULT_APPLICATION_NAOS);
            params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE, affiliation.getIdTiers());
            params.put(ILEConstantes.CS_PARAM_GEN_ID_AFFILIATION, affiliation.getAffiliationId());
            params.put(ILEConstantes.CS_PARAM_GEN_PERIODE, annee);

            // execute le process de génération
            LEGenererEnvoi gen = new LEGenererEnvoi();
            gen.setSession(session);
            gen.setCsDocument(ILEConstantes.CS_DEBUT_SUIVI_BULLETIN_NEUTRE);
            gen.setParamEnvoiList(params);
            gen.setSendCompletionMail(false);
            gen.setGenerateEtapeSuivante(new Boolean(true));
            gen.setTransaction(transaction);
            if (affiliation.isSuivisSuspendu()) {
                affiliation.addSuivi(gen);
            } else {
                gen.executeProcess();
            }
        }

    }

    public static void genererSommation(AFAffiliation affiliation, String annee, BSession session,
            BTransaction transaction) throws Exception {
        if (AFBulletinNeutreUtil.isAlreadySent(affiliation, annee, ILEConstantes.CS_DEF_FORMULE_BN_SOMMATION, session) == null) {
            // TODO juste générer étape suivante, ici va créer un nouveau suivi
            // avec prmière étape sommation
            // prépare les données pour l'envoi
            HashMap params = new HashMap();
            params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS, affiliation.getIdTiers());
            params.put(ILEConstantes.CS_PARAM_GEN_NUMERO, affiliation.getAffilieNumero());
            params.put(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
            params.put(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE, AFApplication.DEFAULT_APPLICATION_NAOS);
            params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE, affiliation.getIdTiers());
            params.put(ILEConstantes.CS_PARAM_GEN_ID_AFFILIATION, affiliation.getAffiliationId());
            params.put(ILEConstantes.CS_PARAM_GEN_PERIODE, annee);

            // execute le process de génération
            LEGenererEnvoi gen = new LEGenererEnvoi();
            gen.setSession(session);
            gen.setCsDocument(ILEConstantes.CS_DEF_FORMULE_BN_SOMMATION);
            gen.setParamEnvoiList(params);
            gen.setSendCompletionMail(false);
            gen.setGenerateEtapeSuivante(new Boolean(true));
            gen.setTransaction(transaction);
            if (affiliation.isSuivisSuspendu()) {
                affiliation.addSuivi(gen);
            } else {
                gen.executeProcess();
            }
        }

    }

    public static LUJournalViewBean isAlreadySent(AFAffiliation affiliation, String annee, String definitionFormule,
            BSession session) {
        try {
            LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO, affiliation.getAffilieNumero());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS, affiliation.getIdTiers());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
            // provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE,
            // affiliation.getIdTiers());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                    AFApplication.DEFAULT_APPLICATION_NAOS);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_AFFILIATION, affiliation.getAffiliationId());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE, annee);
            LUJournalListViewBean viewBean = new LUJournalListViewBean();
            viewBean.setSession(session);
            viewBean.setProvenance(provenanceCriteres);
            viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_DEF_FORMULE_GROUPE);
            viewBean.setForValeurCodeSysteme(definitionFormule);
            viewBean.find();
            if (viewBean.size() == 0) {
                return null;
            } else {
                return (LUJournalViewBean) viewBean.getFirstEntity();
            }
        } catch (Exception e) {
            JadeLogger.info("AFBulletinNeutreUtil", e.getMessage());
            return null;
        }
    }

    public static LEEtapesSuivantesListViewBean rechercherSommationAEffectuer(BSession session, String datePriseEnCompte)
            throws Exception {
        LEEtapesSuivantesListViewBean listeEtapes = new LEEtapesSuivantesListViewBean();
        listeEtapes.setSession(session);
        listeEtapes.setDatePriseEnCompte(datePriseEnCompte);
        ArrayList formule = new ArrayList();
        formule.add(ILEConstantes.CS_DEF_FORMULE_BN_SOMMATION);
        listeEtapes.setForCsFormule(formule);
        listeEtapes.setForCategories(ILEConstantes.CS_CATEGORIE_SUIVI_BULLETIN_NEUTRE);
        listeEtapes.setWantOrderBy(false);
        listeEtapes.find();
        return listeEtapes;
    }

    public static LEEtapesSuivantesListViewBean rechercherSommationEchue(BSession session, String datePriseEnCompte)
            throws Exception {
        LEEtapesSuivantesListViewBean listeEtapes = new LEEtapesSuivantesListViewBean();
        listeEtapes.setSession(session);
        listeEtapes.setDatePriseEnCompte(datePriseEnCompte);
        ArrayList formule = new ArrayList();
        formule.add(ILEConstantes.CS_DEF_FORMULE_BN_SOMMATION);
        listeEtapes.setForCsFormule(formule);
        listeEtapes.setForCategories(ILEConstantes.CS_DEF_FORMULE_BN_TRAITEMENT_SOMMATION_ECHUE);
        listeEtapes.setWantOrderBy(false);
        listeEtapes.find();
        return listeEtapes;
    }

}
