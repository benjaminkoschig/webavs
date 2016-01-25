package ch.globaz.pegasus.business.services.models.blocage;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.blocage.BlocageException;
import ch.globaz.pegasus.business.models.blocage.PcaBloque;
import ch.globaz.pegasus.business.models.blocage.PcaBloqueSearch;

public interface PcaBloqueService extends JadeApplicationService {
    /**
     * Retourne un pca de type blocage si l'idPca correspond à une PCA bloqué. Attention pour les couples DOM2R le
     * montant du déblocage est additionner des 2 montants qui se trouve dans la table des blocages.
     * 
     * @param idPca
     * @return
     * @throws BlocageException
     * @throws JadePersistenceException
     */
    public PcaBloque readPcaBloque(String idPca) throws BlocageException, JadePersistenceException;

    /**
     * Permet d'effectuer un recherche sur les PCA bloqués. Attention pour les couples DOM2R le montant du déblocage est
     * additionner des 2 montants qui se trouve dans la table des blocages.
     * 
     * @param search
     * @return
     * @throws BlocageException
     * @throws JadePersistenceException
     */
    public PcaBloqueSearch searchPcaBloque(PcaBloqueSearch search) throws BlocageException, JadePersistenceException;
}
