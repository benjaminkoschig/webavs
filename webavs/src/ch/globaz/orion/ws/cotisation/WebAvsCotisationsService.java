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
     * Permet de r�cup�rer la liste des cotisations pour un num�ro d'affili�
     * @param noAffilie
     * @return
     */
    public abstract MassesForAffilie listerMassesActuelles(String noAffilie);
}