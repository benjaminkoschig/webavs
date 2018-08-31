package ch.globaz.vulpecula.businessimpl.services.congepaye;

import java.net.URISyntaxException;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.congepaye.CongePayeService;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.parametrage.TableParametrage;
import ch.globaz.vulpecula.domain.repositories.congepaye.CongePayeRepository;

public class CongePayeServiceImpl implements CongePayeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CongePayeServiceImpl.class);

    private CongePayeRepository congePayeRepository;

    public CongePayeServiceImpl() {
        congePayeRepository = VulpeculaRepositoryLocator.getCongePayeRepository();
    }

    public CongePayeServiceImpl(CongePayeRepository congePayeRepository) {
        this.congePayeRepository = congePayeRepository;
    }

    @Override
    public CongePaye create(CongePaye congePaye) throws UnsatisfiedSpecificationException {
        return congePayeRepository.create(congePaye);
    }

    /**
     * Détermine si on doit tenir compte des cotisations pour le congé payé.
     * Dans un premier temps, on se base sur le numéro de convention
     * 
     * @throws URISyntaxException
     * @throws JAXBException
     */
    @Override
    public boolean tenirCompteDesCotisations(String idPosteTravail) {
        try {
            int numeroCaisse = VulpeculaServiceLocator.getPosteTravailService().getNumeroCaissePrincipale(
                    idPosteTravail);
            if (numeroCaisse != 0) {
                return TableParametrage.getInstance().hasCotisationsCongesPays(numeroCaisse);
            } else {
                return false;
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, ex);
        }

    }

    @Override
    public CongePaye findCongePayeById(String idCongePaye) {
        if (idCongePaye == null || idCongePaye.length() == 0) {
            return null;
        }
        return congePayeRepository.findByIdWithDependancies(idCongePaye);
    }
}
