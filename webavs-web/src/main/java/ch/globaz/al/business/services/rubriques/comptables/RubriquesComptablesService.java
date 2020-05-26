package ch.globaz.al.business.services.rubriques.comptables;

import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

import java.util.List;

/**
 * Service générique permettant de récupérer une rubrique comptable
 * 
 * @author jts
 * 
 */
public interface RubriquesComptablesService extends JadeApplicationService {

    /**
     * Détermine et retourne la rubrique comptable appropriée en fonction des paramètres
     * 
     * @param dossier
     *            Dossier pour lequel la rubrique doit être déterminée
     * @param entete
     *            En-tête pour laquelle la rubrique doit être déterminée
     * @param detail
     *            Détail de la prestation pour laquelle la rubrique doit être déterminée
     * @param date
     *            Date pour laquelle la rubrique doit être déterminée
     * @return la rubrique comptable identifiée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    String getRubriqueComptable(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException;


    /**
     * Recherche les rubriques liés aux inpôts à la source.
     *
     * @param dossierComplex
     *            Dossier pour lequel la rubrique doit être déterminée
     * @param date
     *            Date pour laquelle la rubrique doit être déterminée
     * @return la rubrique pour impot ä la source identifiée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    String getRubriqueForIS(DossierComplexModel dossierComplex, String date) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche les rubriques liés aux inpôts à la source.
     *
     * @param caisse
     *            Caisse pour laquelle on cherche la rubrique
     * @param date
     *            Date pour laquelle la rubrique doit être déterminée
     * @return les rubriques pour impot rattaché à cette caisse
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    List<String> getAllRubriquesForIS(String caisse, String date) throws JadeApplicationException, JadePersistenceException;
}
