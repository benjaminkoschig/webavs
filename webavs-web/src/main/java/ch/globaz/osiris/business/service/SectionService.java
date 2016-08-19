package ch.globaz.osiris.business.service;

import globaz.framework.util.FWCurrency;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleSearch;
import ch.globaz.osiris.exception.OsirisException;

/**
 * Services des sections
 * 
 * @author SCO 19 mai 2010
 */
public interface SectionService extends JadeApplicationService {
    /**
     * Service de création d'une section
     * 
     * @param idCompteAnnexe
     *            Un identifiant de compte annexe
     * @param idExterne
     *            Un identifiant externe
     * @param idTypeSection
     *            L'identifiant du type de section
     * @param date
     *            La date de la section
     * @param idJournal
     *            L'identifiant du journal auquel sera rattaché la section
     * @return Un model de section
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    SectionSimpleModel createSection(String idCompteAnnexe, String idExterne, String idTypeSection, String date,
            String idJournal) throws JadePersistenceException, JadeApplicationException;

    /**
     * service de création d'un numéro de section unique pour un compte annexe identifié par idRole, idExterneRole, sur
     * la base de la racine du numéro de section idTypeSection, année et idSousType.
     * 
     * @param idRole
     *            Un identifiant de rôle
     * @param idExterneRole
     *            Un identifiant de rôle externe
     * @param idTypeSection
     *            Un identifiant de type de section
     * @param annee
     *            Une année
     * @param categorieSection
     *            Une catégorie de section
     * @return Un model de section contenant un numéro unique de section
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    SectionSimpleModel creerNumeroSectionUnique(String idRole, String idExterneRole, String idTypeSection,
            String annee, String categorieSection) throws JadePersistenceException, JadeApplicationException;

    /**
     * Retrouve la description de la section en DB.
     * 
     * @param idSection
     * @return la description de la section
     * @throws JadeApplicationException
     */
    public String findDescription(String idSection) throws JadeApplicationException;

    /**
     * Retrouve la description de la section en DB selon la langue donnée.
     * 
     * @param idSection
     * @param isoLangue
     * @return la description de la section
     * @throws JadeApplicationException
     */
    public String findDescription(String idSection, String isoLangue) throws JadeApplicationException;

    /**
     * Service de récupération d'une section.
     * 
     * @param idCompteAnnexe
     *            Un identifiant de compte annexe
     * @param idTypeSection
     *            Un identification de type de section
     * @param idExterne
     *            Un identifiant externe
     * @return Un model de section
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    SectionSimpleModel getSectionByIdExterne(String idCompteAnnexe, String idTypeSection, String idExterne,
            JournalSimpleModel journal) throws JadePersistenceException, JadeApplicationException;

    /**
     * Service qui retourne le solde pour une section en cours de traitement. <BR>
     * Au solde de la section sera ajouté le total des écritures et versements d'un journal. <BR>
     * (Etat des opérations ouvertes également sommées)
     * 
     * @param idJournal
     *            Un identifiant de journal
     * @param idSection
     *            Un identifiant de section
     * @return La somme sous forme de FWCurrency
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    FWCurrency getSoldeSection(String idJournal, String idSection) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Service qui retourne une section
     * 
     * @param idSection
     * @return SectionSimpleModel
     * @throws Exception
     */
    SectionSimpleModel readSection(String idSection) throws OsirisException;

    List<SectionSimpleModel> search(SectionSimpleSearch search) throws OsirisException;

}
