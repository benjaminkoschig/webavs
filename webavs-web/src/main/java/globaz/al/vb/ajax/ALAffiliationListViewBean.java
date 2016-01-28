package globaz.al.vb.ajax;

import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.naos.business.data.AssuranceInfo;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;

/**
 * listViewBean pr�sentant une liste de r�sultats des affiliations pour un n�affili� donn�. Utilise les services
 * AFBusinessServiceLocator.getAffiliationService()
 * 
 * @author GMO
 * 
 */
public class ALAffiliationListViewBean extends BJadePersistentObjectListViewBean {

    /**
     * la date � laquelle on veut les informations de l'affiliation
     */
    String dateCalcul = null;
    /**
     * crit�re n�affili� pour la r�cup�ration des infos d'un affili�
     */
    String forNumeroAffilie = null;
    /**
     * les informations du r�sultat
     */
    AssuranceInfo infoAffiliation = null;

    /**
     * Permet de savoir l'affiliation r�sultante (infoAffiliation) est une maison m�re ou non
     */
    boolean isMaisonMere = false;

    /**
     * Permet de savoir l'affiliation r�sultante (infoAffiliation) est une succursale ou non
     */
    boolean isSuccursale = false;
    /**
     * crit�re n�affili� pour la r�cup�ration des n� affili�s "matchant" avec celui en cours de saisie
     */
    String likeNumeroAffilie = null;

    /**
     * Liste des n� affili�s "matchant" avec le crit�re likeNumeroAffilie
     */
    String[] suggestNumerosAffilie = null;

    /**
     * le type d'activit� de l'allocataire du dossier
     */
    String typeActivite = null;

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */
    @Override
    public void find() throws Exception {

        if (!JadeStringUtil.isEmpty(likeNumeroAffilie)) {
            suggestNumerosAffilie = AFBusinessServiceLocator.getAffiliationService().findAllForNumeroAffilieLike(
                    getLikeNumeroAffilie());
        } else if (!JadeStringUtil.isEmpty(forNumeroAffilie)
                && (AFBusinessServiceLocator.getAffiliationService().nombreAffiliationExists(getForNumeroAffilie()) > 0)) {

            infoAffiliation = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(forNumeroAffilie,
                    typeActivite, dateCalcul);

            AffiliationSimpleModel maisonMere = AFBusinessServiceLocator.getAffiliationService().findMaisonMere(
                    infoAffiliation.getNumeroAffilie());
            if (maisonMere == null) {
                isMaisonMere = false;
                isSuccursale = false;
            } else if (maisonMere.getAffiliationId().equals(infoAffiliation.getIdAffiliation())) {
                isMaisonMere = true;
            } else {
                isSuccursale = true;
            }

        }

    }

    /**
     * @return la date pour laquelle les informations sont cherch�es
     */
    public String getDateCalcul() {
        return dateCalcul;
    }

    /**
     * @return forNumeroAffilie le crit�re n� affili�
     */
    public String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    /**
     * @return infoAffiliation les infos de l'affiliation correspond au n� affili�
     */
    public AssuranceInfo getInfoAffiliation() {
        return infoAffiliation;
    }

    /**
     * Retourne la partie du n�affili� en cours de saisie
     * 
     * @return likeNumeroAffilie
     */
    public String getLikeNumeroAffilie() {
        return likeNumeroAffilie;
    }

    /**
     * @return null car pas de SearchModel utilis� dans ce listViewBean
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getManagerModel()
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return null;
    }

    /**
     * On surcharge car getSize de la super classe travaille avec un searchModel et ici on a un tableau de string
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getSize()
     */
    @Override
    public int getSize() {

        return (suggestNumerosAffilie != null) ? suggestNumerosAffilie.length : (infoAffiliation != null ? 1 : 0);
    }

    public String[] getSuggestNumerosAffilie() {
        return suggestNumerosAffilie;
    }

    public boolean isMaisonMere() {
        return isMaisonMere;
    }

    public boolean isSuccursale() {
        return isSuccursale;
    }

    /**
     * @param dateCalcul
     *            - la date � laquelle chercher les infos
     */
    public void setDateCalcul(String dateCalcul) {
        this.dateCalcul = dateCalcul;
    }

    /**
     * D�finit le n�affili�, crit�re de recherche
     * 
     * @param forNumeroAffilie
     *            le n�affili�
     */
    public void setForNumeroAffilie(String forNumeroAffilie) {
        this.forNumeroAffilie = forNumeroAffilie;
    }

    /**
     * D�finit la partie du n� affili� � chercher
     * 
     * @param likeNumeroAffilie
     *            le partie du n� affili�
     */
    public void setLikeNumeroAffilie(String likeNumeroAffilie) {
        this.likeNumeroAffilie = likeNumeroAffilie;
    }

    public void setSuggestNumerosAffilie(String[] suggestNumerosAffilie) {
        this.suggestNumerosAffilie = suggestNumerosAffilie;
    }

    /**
     * D�finit le type d'activit� de l'allocataire => permet de conna�tre le genre d'affili� � chercher (paritaire ou
     * pers)
     * 
     * @param typeActivite
     */
    public void setTypeActivite(String typeActivite) {
        this.typeActivite = typeActivite;
    }

}
