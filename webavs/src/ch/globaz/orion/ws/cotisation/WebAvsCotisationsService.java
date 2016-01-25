package ch.globaz.orion.ws.cotisation;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * WebService permettant d'acceder a des informations sur les cotisations de Web@Avs
 * 
 * @author sco
 * 
 */
@WebService
public interface WebAvsCotisationsService {
    @WebMethod
    /**
     * Permet de récupérer la liste des cotisations pour un numéro d'affilié
     * @param noAffilie
     * @return
     */
    public abstract MassesForAffilie listerMassesActuelles(String noAffilie);
}