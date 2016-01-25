package ch.globaz.al.businessimpl.rafam.office;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

public interface Office {

    /**
     * R�cup�re le num�ro de la caisse juridiquement responsable � une date donn�e
     * 
     * @param date Date pour laquelle r�cup�rer la num�ro de caisse (legalOffice.officeIdentifier)
     * @return num�ro de la caisse juridiquement responsable
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public String getLegalOffice(String date) throws JadeApplicationException, JadePersistenceException;

    /**
     * R�cup�re le num�ro de la caisse juridiquement responsable (legalOffice.officeIdentifier)
     * 
     * @return num�ro de la caisse juridiquement responsable
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public String getLegalOffice() throws JadeApplicationException, JadePersistenceException;

    /**
     * R�cup�re l'identifiant de la caisse (deliveryOffice.officeIdentifier)
     * 
     * @return l'identifiant de la caisse
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public String getOfficeIdentifier() throws JadeApplicationException, JadePersistenceException;

    /**
     * R�cup�re le num�ro de l'agence (deliveryOffice.branch)
     * 
     * @return num�ro de l'agence
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public String getOfficeBranch() throws JadeApplicationException, JadePersistenceException;
}
