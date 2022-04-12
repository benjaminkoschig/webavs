package ch.globaz.eform.business.services;

import ch.globaz.eform.business.models.GFFormulaireModel;
import ch.globaz.eform.business.search.GFFormulaireSearch;
import ch.globaz.eform.business.search.GFStatistiqueSearch;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public interface GFEFormDBService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     *
     * @param search mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     */
    int count(GFFormulaireSearch search) throws JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� Prestation
     *
     * @param GFFormulaireModel Le formulaireModel � cr�er
     * @return formulaireModel cr��
     */
    GFFormulaireModel create(GFFormulaireModel GFFormulaireModel) throws JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� simplePresation
     *
     * @param id L'identifiant du formulaire � supprimer
     * @return formulaireModel supprim�
     */
    boolean delete(String id) throws JadePersistenceException;

    int delete(GFFormulaireSearch gfeFormSearch) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire une formulaireModel
     *
     * @param id L'identifiant du formulaireModel � charger en m�moire
     * @return formulaireModel charg�e
     */
    GFFormulaireModel read(String id) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire une formulaireModel avec les donn�es blob
     *
     * @param id L'identifiant du formulaireModel � charger en m�moire
     * @return formulaireModel charg�e
     */
    GFFormulaireModel readWithBlobs(String id) throws JadePersistenceException;

    /**
     * Permet de chercher des formulaires selon un mod�le de crit�res.
     *
     * @param gfeFormSearch Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     */
    GFFormulaireSearch search(GFFormulaireSearch gfeFormSearch) throws JadePersistenceException;

    /**
     * Permet de chercher des formulaires selon un mod�le de crit�res et de r�cup�rer les donn�es blob.
     *
     * @param GFFormulaireSearch Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     */
    GFFormulaireSearch searchWithBlobs(GFFormulaireSearch GFFormulaireSearch) throws JadePersistenceException;

    /**
     * Permet de chercher des formulaires selon un mod�le de crit�res.
     *
     * @param GFStatistiqueSearch Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     */
    GFStatistiqueSearch search(GFStatistiqueSearch GFStatistiqueSearch) throws JadePersistenceException;

    /**
     * Permet la mise � jour d'une entit� Prestation
     *
     * @param GFFormulaireModel Le mod�le � mettre � jour
     * @return simplePresation mis � jour
     */
    GFFormulaireModel update(GFFormulaireModel GFFormulaireModel) throws JadePersistenceException;

}
