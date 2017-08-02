/**
 * 
 */
package ch.globaz.naos.business.service;

import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import ch.globaz.common.properties.PropertiesException;

/**
 * Service permettant de gérer les cascades d'adresses IDE définit via 2 propriétés pour les personnes morales et
 * physiques
 * 
 * @author est
 */
public interface CascadeAdresseIdeService extends JadeApplicationService {

    /**
     * Retourne l'adresse en fonction de la cascade choisi via le premier argument (morale ou physique).
     * 
     * @param affilieNumero
     * @param idTiers
     * @return
     * @throws PropertiesException
     */
    TIAdresseDataSource getAdresseFromCascadeIde(Boolean isPersonneMorale, String affilieNumero, String idTiers)
            throws PropertiesException;

    /***
     * Retourne la cascade pour les adresses des affiliations morales de la propriété sous format de string
     * 
     * @return
     * @throws PropertiesException
     */
    String retrievePropretieCascadeIdeMorale() throws PropertiesException;

    /***
     * Retourne la cascade pour les adresses des affiliations physiques de la propriété sous format de string
     * 
     * @return
     * @throws PropertiesException
     */
    String retrievePropretieCascadeIdePhysique() throws PropertiesException;
}