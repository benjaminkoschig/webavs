package globaz.hercule.service;

import globaz.globall.db.BSession;
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.controleEmployeur.CEAffilieControle;
import globaz.hercule.itext.controleEmployeur.CEDSLettre_Param;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.process.LEGenererEnvoi;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.pyxis.db.tiers.TIRole;
import java.util.HashMap;

/**
 * @author SCO
 * @since 19 juil. 2010
 */
public abstract class CEDeclarationStructureeService {

    /**
     * Permet la g�n�ration du d�but de suivi dans "LEO : la gestion des envoi" <BR>
     * 
     * @param session
     *            Une session
     * @param affilie
     *            Un affili�
     * @param categorieMasse
     *            La cat�gorie masse salariale de l'affili�
     * @param annee
     *            L'ann�e du d�but de suivi
     * @return r�sultat de l'execution
     * @throws Exception
     */
    public static String generationDebutSuivi(BSession session, CEAffilieControle affilie, String categorieMasse,
            String annee) throws Exception {

        // pr�pare les donn�es pour l'envoi
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS, affilie.getIdTiers());
        params.put(ILEConstantes.CS_PARAM_GEN_NUMERO, affilie.getNumeroAffilie());
        params.put(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
        params.put(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE, CEApplication.DEFAULT_APPLICATION_HERCULE);
        params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE, affilie.getIdTiers());
        params.put(ILEConstantes.CS_PARAM_GEN_ID_AFFILIATION, affilie.getIdAffilie());
        params.put(ILEConstantes.CS_PARAM_GEN_PERIODE, annee);
        params.put(CEDSLettre_Param.PARAM_CATEGORIE_MASSE, categorieMasse);
        params.put(CEDSLettre_Param.PARAM_DATE_DEBUT_AFFILIATION, affilie.getDateDebutAffiliation());
        params.put(CEDSLettre_Param.PARAM_NUM_AFFILIATION, affilie.getNumeroAffilie());

        // execute le process de g�n�ration
        LEGenererEnvoi gen = new LEGenererEnvoi();
        gen.setSession(session);
        gen.setCsDocument(ILEConstantes.CS_DS_ST_DEBUT_SUIVI);
        gen.setParamEnvoiList(params);
        gen.setSendCompletionMail(false);
        gen.setGenerateEtapeSuivante(Boolean.FALSE);

        gen.executeProcess();

        return null;
    }

    /**
     * Permet de savoir si l'affili� a d�j� une gestion d'envoi.
     * 
     * @param session
     *            Une session
     * @param affNumero
     *            Un numero d'affili�
     * @param annee
     *            Une ann�e
     * @return
     * @throws Exception
     */
    public static boolean isDejaJournalise(BSession session, String affNumero, String annee) throws Exception {
        // On sette les crit�res qui font que l'envoi est unique
        LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                CEApplication.DEFAULT_APPLICATION_HERCULE);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO, affNumero);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE, annee);

        LUJournalListViewBean viewBean = new LUJournalListViewBean();

        viewBean.setSession(session);
        viewBean.setProvenance(provenanceCriteres);
        viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_CATEGORIE_GROUPE);
        viewBean.setForValeurCodeSysteme(ILEConstantes.CS_CATEGORIE_SUIVI_DS_STRUCTURE);

        viewBean.find();

        // Si le viewBean retourne un enregistrement c'est que l'envoi a d�j�
        // �t� journalis� donc on retourne true
        if (viewBean.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

}
