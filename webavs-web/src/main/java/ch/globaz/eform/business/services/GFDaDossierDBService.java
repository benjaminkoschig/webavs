package ch.globaz.eform.business.services;

import ch.globaz.common.validation.ValidationResult;
import ch.globaz.eform.business.models.GFDaDossierModel;
import ch.globaz.eform.business.search.GFDaDossierSearch;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public interface GFDaDossierDBService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     *
     * @param search mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     */
    int count(GFDaDossierSearch search) throws JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� Prestation
     *
     * @param gfDaDossierModel Le formulaireModel � cr�er
     * @return formulaireModel cr��
     */
    GFDaDossierModel create(GFDaDossierModel gfDaDossierModel) throws JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� Prestation
     *
     * @param gfDaDossierModel Le formulaireModel � cr�er
     * @param validationResult permet de remonter la validation des informations
     * @return formulaireModel cr��
     */
    GFDaDossierModel create(GFDaDossierModel gfDaDossierModel, ValidationResult validationResult) throws JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� simplePresation
     *
     * @param id L'identifiant du formulaire � supprimer
     * @return formulaireModel supprim�
     */
    boolean delete(String id) throws JadePersistenceException;

    int delete(GFDaDossierSearch gfDaDossierSearch) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire une formulaireModel
     *
     * @param id L'identifiant du formulaireModel � charger en m�moire
     * @return formulaireModel charg�e
     */
    GFDaDossierModel read(String id) throws JadePersistenceException;

    /**
     * Permet de chercher des formulaires selon un mod�le de crit�res.
     *
     * @param gfeFormSearch Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     */
    GFDaDossierSearch search(GFDaDossierSearch gfeFormSearch) throws JadePersistenceException;

    /**
     * Permet la mise � jour d'une entit� Prestation
     *
     * @param gfDaDossierModel Le mod�le � mettre � jour
     * @return simplePresation mis � jour
     */
    GFDaDossierModel update(GFDaDossierModel gfDaDossierModel) throws JadePersistenceException;

    /**
     * Permet la mise � jour d'une entit� Prestation
     *
     * @param gfDaDossierModel Le mod�le � mettre � jour
     * @param validationResult permet de remonter la validation des informations
     * @return simplePresation mis � jour
     */
    GFDaDossierModel update(GFDaDossierModel gfDaDossierModel, ValidationResult validationResult) throws JadePersistenceException;

}
