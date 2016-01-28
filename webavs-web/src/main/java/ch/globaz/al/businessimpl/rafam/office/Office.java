package ch.globaz.al.businessimpl.rafam.office;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

public interface Office {

    /**
     * Récupère le numéro de la caisse juridiquement responsable à une date donnée
     * 
     * @param date Date pour laquelle récupérer la numéro de caisse (legalOffice.officeIdentifier)
     * @return numéro de la caisse juridiquement responsable
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public String getLegalOffice(String date) throws JadeApplicationException, JadePersistenceException;

    /**
     * Récupère le numéro de la caisse juridiquement responsable (legalOffice.officeIdentifier)
     * 
     * @return numéro de la caisse juridiquement responsable
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public String getLegalOffice() throws JadeApplicationException, JadePersistenceException;

    /**
     * Récupère l'identifiant de la caisse (deliveryOffice.officeIdentifier)
     * 
     * @return l'identifiant de la caisse
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public String getOfficeIdentifier() throws JadeApplicationException, JadePersistenceException;

    /**
     * Récupère le numéro de l'agence (deliveryOffice.branch)
     * 
     * @return numéro de l'agence
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public String getOfficeBranch() throws JadeApplicationException, JadePersistenceException;
}
