package ch.globaz.al.business.services.rubriques;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;

/**
 * Service permettant la récupération de rubriques (comptable ou autre
 * 
 * @author jts
 * 
 */
public interface RubriqueService extends JadeApplicationService {
    /**
     * Recherche le numéro de la rubrique comptable en fonction des paramètres passés à la méthode
     * 
     * @param dossier
     *            Dossier pour lequel la rubrique doit être déterminée
     * @param entete
     *            En-tête pour laquelle la rubrique doit être déterminée
     * @param detail
     *            Détail de la prestation pour laquelle la rubrique doit être déterminée
     * @param date
     *            Date pour laquelle la rubrique doit être déterminée
     * 
     * @return numéro de la rubrique définie en fonction des paramètres
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public String getRubriqueComptable(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException;
}
