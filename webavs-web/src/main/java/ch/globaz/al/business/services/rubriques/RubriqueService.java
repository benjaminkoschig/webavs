package ch.globaz.al.business.services.rubriques;

import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

import java.util.List;

/**
 * Service permettant la r�cup�ration de rubriques (comptable ou autre
 * 
 * @author jts
 * 
 */
public interface RubriqueService extends JadeApplicationService {
    /**
     * Recherche le num�ro de la rubrique comptable en fonction des param�tres pass�s � la m�thode
     *
     * @param dossier Dossier pour lequel la rubrique doit �tre d�termin�e
     * @param entete  En-t�te pour laquelle la rubrique doit �tre d�termin�e
     * @param detail  D�tail de la prestation pour laquelle la rubrique doit �tre d�termin�e
     * @param date    Date pour laquelle la rubrique doit �tre d�termin�e
     * @return num�ro de la rubrique d�finie en fonction des param�tres
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *                                  faire
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    String getRubriqueComptable(DossierModel dossier, EntetePrestationModel entete,
                                DetailPrestationModel detail, String date) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche les rubriques li�s aux inp�ts � la source.
     *
     * @param dossierComplex Dossier pour lequel la rubrique doit �tre d�termin�e
     * @param date    Date pour laquelle la rubrique doit �tre d�termin�e
     * @return le num�ro de la rubrique en fonction
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *                                  faire
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    String getRubriqueForIS(DossierComplexModel dossierComplex, String date) throws JadePersistenceException, JadeApplicationException;

    /**
     * Recherche les rubriques li�s aux inp�ts � la source.
     *
     * @param caisse La caisse dont on cherche la rubrique
     * @param date    Date pour laquelle la rubrique doit �tre d�termin�e
     * @return les num�ros des rubriques en fonction
     * @throws JadePersistenceException Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *                                  faire
     * @throws JadeApplicationException Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    List<String> getAllRubriquesForIS(String caisse, String date) throws JadePersistenceException, JadeApplicationException;
}
