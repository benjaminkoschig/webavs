package ch.globaz.al.business.services.tarif;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * Interface présentant les services métier liés au tarifs
 * 
 * @author gmo
 * 
 */
public interface TarifBusinessService extends JadeApplicationService {
    /**
     * Indique si le tarif est cantonal ou non (tarif caisses, tarif spécial, ...) Se base sur la legislation
     * (=cantonale), exception, tarif droit_vaud_acquis => legislation cantonale, mais tarif spécial => non cantonal
     * 
     * 
     * @param csTarif
     * @return true si le tarif est cantonal, false sinon
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public boolean isTarifCantonal(String csTarif) throws JadePersistenceException, JadeApplicationException;

}
