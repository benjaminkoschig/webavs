/**
 * 
 */
package globaz.pegasus.vb.decision;

import globaz.globall.db.BSession;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.pegasus.utils.PCUserHelper;
import globaz.prestation.tools.nnss.PRNSSUtil;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

/**
 * @author SCE 20 août 2010
 */
public abstract class PCPrepDecisionAbstractViewBean extends BJadePersistentObjectViewBean {
    /**
     * Retourne le nom de l'utilisateur cournat
     * 
     * @return
     */
    public String getCurrentUserId() {
        return getSession().getUserId();
    }

    /**
     * Formatte une chaine de caratere pour afficher les infos du requérant
     * 
     * @return
     */
    public String getRequerantInfos(PersonneEtendueComplexModel personneEtendue, TiersSimpleModel tiers) {

        String NSS = personneEtendue.getPersonneEtendue().getNumAvsActuel();
        String NomPrenom = tiers.getDesignation1() + " " + tiers.getDesignation2();
        String dateNaissance = personneEtendue.getPersonne().getDateNaissance();
        String sexe = getSession().getCodeLibelle(personneEtendue.getPersonne().getSexe());
        String nationalite = PCUserHelper.getLibellePays(personneEtendue);//
        getSession().getCodeLibelle(personneEtendue.getPersonne().getSexe());

        String reqInfos = PRNSSUtil.formatDetailRequerantDetail(NSS, NomPrenom, dateNaissance, sexe, nationalite);

        return reqInfos;
    }

    /**
     * Retourn la session
     * 
     * @return objet BSEssion
     */
    public BSession getSession() {
        return (BSession) getISession();
    }
}
