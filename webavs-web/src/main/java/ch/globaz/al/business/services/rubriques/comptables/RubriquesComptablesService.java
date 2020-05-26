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
 * Service g�n�rique permettant de r�cup�rer une rubrique comptable
 * 
 * @author jts
 * 
 */
public interface RubriquesComptablesService extends JadeApplicationService {

    /**
     * D�termine et retourne la rubrique comptable appropri�e en fonction des param�tres
     * 
     * @param dossier
     *            Dossier pour lequel la rubrique doit �tre d�termin�e
     * @param entete
     *            En-t�te pour laquelle la rubrique doit �tre d�termin�e
     * @param detail
     *            D�tail de la prestation pour laquelle la rubrique doit �tre d�termin�e
     * @param date
     *            Date pour laquelle la rubrique doit �tre d�termin�e
     * @return la rubrique comptable identifi�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    String getRubriqueComptable(DossierModel dossier, EntetePrestationModel entete,
            DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException;


    /**
     * Recherche les rubriques li�s aux inp�ts � la source.
     *
     * @param dossierComplex
     *            Dossier pour lequel la rubrique doit �tre d�termin�e
     * @param date
     *            Date pour laquelle la rubrique doit �tre d�termin�e
     * @return la rubrique pour impot � la source identifi�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    String getRubriqueForIS(DossierComplexModel dossierComplex, String date) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche les rubriques li�s aux inp�ts � la source.
     *
     * @param caisse
     *            Caisse pour laquelle on cherche la rubrique
     * @param date
     *            Date pour laquelle la rubrique doit �tre d�termin�e
     * @return les rubriques pour impot rattach� � cette caisse
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    List<String> getAllRubriquesForIS(String caisse, String date) throws JadeApplicationException, JadePersistenceException;
}
