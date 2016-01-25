package ch.globaz.pegasus.business.services.models.blocage;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.blocage.BlocageException;
import ch.globaz.pegasus.business.models.blocage.PcaBloque;
import ch.globaz.pegasus.business.models.blocage.SimpleLigneDeblocage;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.vo.blocage.DetteComptat;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public interface DeblocageDetteService extends JadeApplicationService {
    /**
     * Cette fonction permet de retrouver une PCA depuis l'idtiers de la version du droit et une date de début de PCA
     * Ceci est surtout utile pour trouver la PCA d'un conjoint, pour le couple séparé par la maladie.
     * 
     * @param idTiersConjoint
     * @param idVersionDroit
     * @param dateDebutPca
     * @return
     * @throws BlocageException
     */
    public PCAccordee findPcaByTiersDroitDateDebut(String idTiersConjoint, String idVersionDroit, String dateDebutPca)
            throws BlocageException;

    /**
     * Cette fonction prend paramètre la liste de dette débloqué afin de retourner une liste de dette qui contient les
     * dettes augmenter de nouvelle valeur et des nouvelles dettes de la compta.
     * 
     * @param deblocagesDettes
     * @param listIdTiers
     *            (liste de idTiers des membres de la famille)
     * @param pcaBloque
     *            (PCA à débloqué)
     * @param conjoint
     *            (Cette valeur peux être null car elle est seulement utile pour les couple séparé par la maladie)
     * @return
     * @throws BlocageException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public List<DetteComptat> generateDetteForDeblocage(List<SimpleLigneDeblocage> deblocagesDettes,
            List<String> listIdTiers, PcaBloque pcaBloque, PersonneEtendueComplexModel conjoint)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * Permet de rechercher la dette qui est enregistrer
     * 
     * @param idSectionDette
     * @throws BlocageException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public SimpleLigneDeblocage readDetteDeblocageConjointEnregistrer(String idSectionDette, String idPca)
            throws BlocageException, JadeApplicationServiceNotAvailableException, JadePersistenceException;
}
