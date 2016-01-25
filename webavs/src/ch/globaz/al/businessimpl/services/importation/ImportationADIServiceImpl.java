package ch.globaz.al.businessimpl.services.importation;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.adi.AdiEnfantMoisModel;
import ch.globaz.al.business.models.adi.DecompteAdiModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.importation.ImportationADIService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Implémentation du service d'importation des données ADI
 * 
 * @author jts
 * 
 */
public class ImportationADIServiceImpl extends ALAbstractBusinessServiceImpl implements ImportationADIService {

    @Override
    public AdiEnfantMoisModel importAdiEnfantMois(AdiEnfantMoisModel adiEnfantMois, String idDecompteAdi, String idDroit)
            throws JadeApplicationException, JadePersistenceException {

        // pour l'importation: affectation du total étranger avec le
        // montantAllocEtrangère
        adiEnfantMois.setMontantEtrTotal(adiEnfantMois.getMontantAllocEtr());

        // affectation de l'identifiant décompte Adi
        adiEnfantMois.setIdDecompteAdi(idDecompteAdi);
        // affectation de l'identifiant du droit
        adiEnfantMois.setIdDroit(idDroit);

        // création de l'enregistrement en base de donnée
        return adiEnfantMois = ALImplServiceLocator.getAdiEnfantMoisModelService().create(adiEnfantMois);
    }

    @Override
    public DecompteAdiModel importDecompteAdi(DecompteAdiModel decompteAdi, String idEntetePrestationSolde,
            String csMonnaie) throws JadeApplicationException, JadePersistenceException {

        // affectation du code système monnaie
        decompteAdi.setCodeMonnaie(csMonnaie);

        if (!JadeStringUtil.isEmpty(idEntetePrestationSolde)) {
            decompteAdi.setIdPrestationAdi(idEntetePrestationSolde);
        }

        return decompteAdi = ALServiceLocator.getDecompteAdiModelService().create(decompteAdi);
    }
}
