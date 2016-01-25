package ch.globaz.al.business.services.importation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.adi.AdiEnfantMoisModel;
import ch.globaz.al.business.models.adi.DecompteAdiModel;

/**
 * Service d'importation des donn�es ADI
 * 
 * @author jts
 * 
 */
public interface ImportationADIService extends JadeApplicationService {
    /**
     * 
     * @param adiEnfantMois
     *            selon mod�le de base AdiEnfnatMoisModel
     * @param idDecompteAdi
     *            identifiant du d�compte Adi
     * @param idDroit
     *            identifiant du droit
     * @return adiEnfantMois
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AdiEnfantMoisModel importAdiEnfantMois(AdiEnfantMoisModel adiEnfantMois, String idDecompteAdi, String idDroit)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Importation d'un d�compte Adi
     * 
     * @param decompteAdi
     *            selon mod�le de base du d�compte Adi (AdiDecompteModel)
     * @param idEntetePrestationSolde
     *            identifiant de l'ent�te de prestation pour le solde
     * @param csMonnaie
     *            Code syst�me de la monnaie
     * @return decompteAdi selon mod�le de base du d�compte Adi (AdiDecompteModel)
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DecompteAdiModel importDecompteAdi(DecompteAdiModel decompteAdi, String idEntetePrestationSolde,
            String csMonnaie) throws JadeApplicationException, JadePersistenceException;
}
