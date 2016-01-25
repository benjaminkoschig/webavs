package ch.globaz.al.business.services.rafam.sedex;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * Service li� � la gestion de l'envoi/r�ception des annonces RAFam
 * 
 * @author jts
 * 
 */
public interface AnnonceRafamSedexService extends JadeApplicationService {

    /**
     * V�rifie si une annonce est li�e � un employeur d�l�gu�
     * 
     * @param recordNumber
     *            record number de l'annonce � v�rifier
     * @return <code>true</code> si l'annonce correspond � un employeur d�l�gu�
     * @throws JadeApplicationException
     */
    public boolean isAnnonceEmployeurDelegue(String recordNumber) throws JadeApplicationException;

    /**
     * V�rifie si une annonce est li�e � un employeur d�l�gu�
     * 
     * @param internalOfficeReference
     *            num�ro de r�f�rence interne de l'annonce.
     * @param recordNumber
     *            record number de l'annonce � v�rifier
     * @return <code>true</code> si l'annonce correspond � un employeur d�l�gu�
     * @throws JadeApplicationException
     */
    public boolean isAnnonceEmployeurDelegue(String internalOfficeReference, String recordNumber)
            throws JadeApplicationException;
}
