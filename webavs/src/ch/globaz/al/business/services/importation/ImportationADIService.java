package ch.globaz.al.business.services.importation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.adi.AdiEnfantMoisModel;
import ch.globaz.al.business.models.adi.DecompteAdiModel;

/**
 * Service d'importation des données ADI
 * 
 * @author jts
 * 
 */
public interface ImportationADIService extends JadeApplicationService {
    /**
     * 
     * @param adiEnfantMois
     *            selon modèle de base AdiEnfnatMoisModel
     * @param idDecompteAdi
     *            identifiant du décompte Adi
     * @param idDroit
     *            identifiant du droit
     * @return adiEnfantMois
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AdiEnfantMoisModel importAdiEnfantMois(AdiEnfantMoisModel adiEnfantMois, String idDecompteAdi, String idDroit)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Importation d'un décompte Adi
     * 
     * @param decompteAdi
     *            selon modèle de base du décompte Adi (AdiDecompteModel)
     * @param idEntetePrestationSolde
     *            identifiant de l'entête de prestation pour le solde
     * @param csMonnaie
     *            Code système de la monnaie
     * @return decompteAdi selon modèle de base du décompte Adi (AdiDecompteModel)
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DecompteAdiModel importDecompteAdi(DecompteAdiModel decompteAdi, String idEntetePrestationSolde,
            String csMonnaie) throws JadeApplicationException, JadePersistenceException;
}
