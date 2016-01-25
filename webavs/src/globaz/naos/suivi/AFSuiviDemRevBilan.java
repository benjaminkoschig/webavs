/*
 * Créé le 14 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.suivi;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.process.LEGenererEnvoi;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.lupus.db.journalisation.LUJournalViewBean;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.tiers.TIRole;
import java.util.HashMap;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public abstract class AFSuiviDemRevBilan extends BAbstractEntityExternalService {
    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterDelete(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeAdd(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeDelete(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeRetrieve(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeUpdate(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    public void genererControle(AFAffiliation affiliation) throws Exception {
        // prépare les données pour l'envoi
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS, affiliation.getTiers().getIdTiers());
        params.put(ILEConstantes.CS_PARAM_GEN_NUMERO, affiliation.getAffilieNumero());
        params.put(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
        // Modif JMC selon accord avec DGI, quand on crée un suivi pour le
        // module NAOS, l'application est NAOS
        params.put(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE, AFApplication.DEFAULT_APPLICATION_NAOS);
        params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE, getIdDestinataire(affiliation));
        params.put(ILEConstantes.CS_PARAM_GEN_ID_AFFILIATION, affiliation.getAffiliationId());

        // execute le process de génération
        LEGenererEnvoi gen = new LEGenererEnvoi();
        gen.setSession(affiliation.getSession());
        gen.setCsDocument(getDefinitionFormule());
        gen.setParamEnvoiList(params);
        gen.setSendCompletionMail(false);
        gen.setGenerateEtapeSuivante(isGenererEtapeSuivante(affiliation));
        if (affiliation.isSuivisSuspendu()) {
            affiliation.addSuivi(gen);
        } else {
            gen.executeProcess();
        }
    }

    public void genererControle(AFAffiliation affiliation, String annee, String eMail) throws Exception {
        // prépare les données pour l'envoi
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS, affiliation.getTiers().getIdTiers());
        params.put(ILEConstantes.CS_PARAM_GEN_NUMERO, affiliation.getAffilieNumero());
        params.put(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
        // Modif JMC selon accord avec DGI, quand on crée un suivi pour le
        // module NAOS, l'application est NAOS
        params.put(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE, AFApplication.DEFAULT_APPLICATION_NAOS);
        params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE, getIdDestinataire(affiliation));
        params.put(ILEConstantes.CS_PARAM_GEN_ID_AFFILIATION, affiliation.getAffiliationId());
        params.put(ILEConstantes.CS_PARAM_GEN_PERIODE, annee);

        // execute le process de génération
        LEGenererEnvoi gen = new LEGenererEnvoi();
        gen.setSession(affiliation.getSession());
        if (!JadeStringUtil.isEmpty(eMail)) {
            gen.setEMailAddress(eMail);
        }
        gen.setCsDocument(getDefinitionFormule());
        gen.setParamEnvoiList(params);
        gen.setSendCompletionMail(false);
        gen.setGenerateEtapeSuivante(isGenererEtapeSuivante(affiliation));
        if (affiliation.isSuivisSuspendu()) {
            affiliation.addSuivi(gen);
        } else {
            gen.executeProcess();
        }
    }

    public abstract String getDefinitionFormule();

    public abstract String getIdDestinataire(AFAffiliation affiliation);

    @Override
    public void init(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    public boolean isAffiliationConcerne(AFAffiliation affiliation) {
        return ((CodeSystem.TYPE_AFFILI_EMPLOY.equalsIgnoreCase(affiliation.getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_LTN.equalsIgnoreCase(affiliation.getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_EMPLOY_D_F.equalsIgnoreCase(affiliation.getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equalsIgnoreCase(affiliation.getTypeAffiliation()) || CodeSystem.TYPE_AFFILI_FICHIER_CENT
                    .equalsIgnoreCase(affiliation.getTypeAffiliation())) && (this.isAlreadySent(affiliation) == null));

    }

    public LUJournalViewBean isAlreadySent(AFAffiliation affiliation) {
        try {
            LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO, affiliation.getAffilieNumero());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS, affiliation.getIdTiers());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                    AFApplication.DEFAULT_APPLICATION_NAOS);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_AFFILIATION, affiliation.getAffiliationId());
            LUJournalListViewBean viewBean = new LUJournalListViewBean();
            viewBean.setSession(affiliation.getSession());
            viewBean.setProvenance(provenanceCriteres);
            viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_DEF_FORMULE_GROUPE);
            viewBean.setForValeurCodeSysteme(getDefinitionFormule());
            viewBean.find();
            if (viewBean.size() == 0) {
                return null;
            } else {
                return (LUJournalViewBean) viewBean.getFirstEntity();
            }
        } catch (Exception e) {
            JadeLogger.info(this, e.getMessage());
            return null;
        }
    }

    public LUJournalViewBean isAlreadySent(AFAffiliation affiliation, String annee) {
        try {
            LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO, affiliation.getAffilieNumero());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS, affiliation.getIdTiers());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                    AFApplication.DEFAULT_APPLICATION_NAOS);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_AFFILIATION, affiliation.getAffiliationId());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE, annee);
            LUJournalListViewBean viewBean = new LUJournalListViewBean();
            viewBean.setSession(affiliation.getSession());
            viewBean.setProvenance(provenanceCriteres);
            viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_DEF_FORMULE_GROUPE);
            viewBean.setForValeurCodeSysteme(getDefinitionFormule());
            viewBean.find();
            if (viewBean.size() == 0) {
                return null;
            } else {
                return (LUJournalViewBean) viewBean.getFirstEntity();
            }
        } catch (Exception e) {
            JadeLogger.info(this, e.getMessage());
            return null;
        }
    }

    protected Boolean isGenererEtapeSuivante(AFAffiliation affiliation) throws Exception {
        // générer également étape suivante dans le cas où la propriété est à
        // false ou inexistante
        String propImpressionAuto = affiliation.getSession().getApplication()
                .getProperty("naos.application.impression.batch");
        return new Boolean(JadeStringUtil.isEmpty(propImpressionAuto) || "false".equals(propImpressionAuto));
    }

    @Override
    public void validate(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

}
