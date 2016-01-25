package ch.globaz.al.business.services.rafam.sedex;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * Service lié à la gestion de l'envoi/réception des annonces RAFam
 * 
 * @author jts
 * 
 */
public interface AnnonceRafamSedexService extends JadeApplicationService {

    /**
     * Vérifie si une annonce est liée à un employeur délégué
     * 
     * @param recordNumber
     *            record number de l'annonce à vérifier
     * @return <code>true</code> si l'annonce correspond à un employeur délégué
     * @throws JadeApplicationException
     */
    public boolean isAnnonceEmployeurDelegue(String recordNumber) throws JadeApplicationException;

    /**
     * Vérifie si une annonce est liée à un employeur délégué
     * 
     * @param internalOfficeReference
     *            numéro de référence interne de l'annonce.
     * @param recordNumber
     *            record number de l'annonce à vérifier
     * @return <code>true</code> si l'annonce correspond à un employeur délégué
     * @throws JadeApplicationException
     */
    public boolean isAnnonceEmployeurDelegue(String internalOfficeReference, String recordNumber)
            throws JadeApplicationException;
}
