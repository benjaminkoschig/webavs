package ch.globaz.osiris.business.service;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModelSearch;
import ch.globaz.osiris.exception.OsirisException;

/**
 * Services sur le compte annexe
 * 
 * @author SCO 19 mai 2010
 */
public interface CompteAnnexeService extends JadeApplicationService {

    /**
     * R�cup�ration d'un compte annexe (possibilit� de cr�er le compte si il n'existe pas
     * 
     * @param idJournal
     *            Identifiant du journal
     * @param idTiers
     *            Identifiant du tiers
     * @param idRole
     *            Identifiant du r�le
     * @param idExterneRole
     *            Identifiant d'un r�le externe
     * @param createIfNotExist
     *            true si le compte annexe doit �tre cr�� si il n'existe pas
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public CompteAnnexeSimpleModel getCompteAnnexe(String idJournal, String idTiers, String idRole,
            String idExterneRole, Boolean createIfNotExist) throws JadePersistenceException, JadeApplicationException;

    /**
     * R�cup�ration d'un model compte annexe par son r�le (ne le cr�e pas le compte si n'existe pas)
     * 
     * @param idJournal
     *            L'identifiant d'un journal
     * @param idTiers
     *            L'identifiant d'un tiers
     * @param idRole
     *            L'identifiant d'un role
     * @param idExterneRole
     *            l'identifiant d'un r�le externe
     * @return Un model simple de compte annexe
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @deprecated Utiliser getCompteAnnexe
     */
    @Deprecated
    CompteAnnexeSimpleModel getCompteAnnexeByIdTiers(String idJournal, String idTiers, String idRole,
            String idExterneRole) throws JadePersistenceException, JadeApplicationException;

    /**
     * R�cup�ration d'un model compte annexe par son r�le (ou cr�er le compte si n'existe pas)
     * 
     * @param idJournal
     *            L'identifiant d'un journal
     * @param idTiers
     *            L'identifiant d'un tiers
     * @param idRole
     *            L'identifiant d'un role
     * @param idExterneRole
     *            l'identifiant d'un r�le externe
     * @return Un model simple de compte annexe
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @deprecated Utiliser getCompteAnnexe
     */
    @Deprecated
    CompteAnnexeSimpleModel getCompteAnnexeByRole(String idJournal, String idTiers, String idRole, String idExterneRole)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * Permet de lire un compte annexe
     * 
     * @param idCompteAnnexe
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public CompteAnnexeSimpleModel read(String idCompteAnnexe) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Permet de rechercher les comptesAnnexe
     * 
     * @param search
     * @return
     * @throws OsirisException
     */
    public List<CompteAnnexeSimpleModel> search(CompteAnnexeSimpleModelSearch search) throws OsirisException;
}
