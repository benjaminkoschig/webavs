/*
 * Globaz SA.
 */
package globaz.hercule.process.noncertifiesconformes;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.hercule.application.CEApplication;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.process.LEGenererEnvoi;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.services.AFAffiliationServices;
import globaz.pyxis.db.tiers.TIRole;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CEGenerationSuiviNCCProcess extends BProcess {

    private static final Logger LOG = LoggerFactory.getLogger(CEGenerationSuiviNCCProcess.class);

    private static final long serialVersionUID = 1L;

    private String forNumAffilie;
    private String annee;

    public CEGenerationSuiviNCCProcess() {
        super();
    }

    @Override
    protected void _validate() throws Exception {

        if (JadeStringUtil.isEmpty(getAnnee())) {
            getSession().addError(getSession().getLabel("VAL_ANNEE"));
        }

        if (JadeStringUtil.isEmpty(getForNumAffilie())) {
            getSession().addError(getSession().getLabel("NUMERO_AFFILIE_NON_SPECIFIE"));
        }

        if (isDejaJournalise()) {
            getSession().addError(getSession().getLabel("SUIVI_NCC_DEJA_PRESENT"));
        }
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        try {
            AFAffiliation affiliation = AFAffiliationServices.getAffiliationActiveParitaireByNumero(getForNumAffilie(),
                    getSession());

            generationDebutSuivi(affiliation);

            getMemoryLog().logMessage(getSession().getLabel("GENERATION_SUIVI_NCC_INFORMATION") + getForNumAffilie(),
                    FWMessage.INFORMATION, this.getClass().getName());

        } catch (Exception e) {
            LOG.error(this.getClass().getName(), "Error in the process CEGenerationSuiviNCCProcess");

            this._addError(getTransaction(), getSession().getLabel("EXECUTION_GENERATOIN_SUIVI_NCC_ERREUR"));

            String messageInformation = "Annee de génération du début du suivi : " + getAnnee() + "\n";
            messageInformation += "NumAffilie : " + getForNumAffilie() + "\n";
            messageInformation += CEUtils.stack2string(e);

            CEUtils.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());

            return false;
        }

        return true;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted()) {
            return getSession().getLabel("GENERATION_SUIVI_NCC_ERREUR") + getForNumAffilie();
        } else {
            return getSession().getLabel("GENERATION_SUIVI_NCC_OK") + getForNumAffilie();
        }
    }

    @Override
    protected void _executeCleanUp() {
        // not implemented
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public String getForNumAffilie() {
        return forNumAffilie;
    }

    public void setForNumAffilie(String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void generationDebutSuivi(AFAffiliation affilie) throws Exception {

        // prépare les données pour l'envoi
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS, affilie.getIdTiers());
        params.put(ILEConstantes.CS_PARAM_GEN_NUMERO, affilie.getAffilieNumero());
        params.put(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
        params.put(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE, CEApplication.DEFAULT_APPLICATION_HERCULE);
        params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE, affilie.getIdTiers());
        params.put(ILEConstantes.CS_PARAM_GEN_ID_AFFILIATION, affilie.getAffiliationId());
        params.put(ILEConstantes.CS_PARAM_GEN_PERIODE, getAnnee());

        // execute le process de génération
        LEGenererEnvoi gen = new LEGenererEnvoi();
        gen.setSession(getSession());
        gen.setCsDocument(ILEConstantes.CS_DEBUT_SUIVI_NCC);
        gen.setParamEnvoiList(params);
        gen.setSendCompletionMail(false);
        gen.setGenerateEtapeSuivante(Boolean.FALSE);

        gen.executeProcess();
    }

    public boolean isDejaJournalise() throws Exception {
        // On sette les critères qui font que l'envoi est unique
        LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                CEApplication.DEFAULT_APPLICATION_HERCULE);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO, getForNumAffilie());
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE, getAnnee());

        LUJournalListViewBean viewBean = new LUJournalListViewBean();

        viewBean.setSession(getSession());
        viewBean.setProvenance(provenanceCriteres);
        viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_CATEGORIE_GROUPE);
        viewBean.setForValeurCodeSysteme(ILEConstantes.CS_CATEGORIE_NON_CERTIFIES_CONFORMES);

        viewBean.find(BManager.SIZE_USEDEFAULT);

        // Si le viewBean retourne un enregistrement c'est que l'envoi a déjà
        // été journalisé donc on retourne true
        if (!viewBean.isEmpty()) {
            return true;
        }

        return false;

    }

}
