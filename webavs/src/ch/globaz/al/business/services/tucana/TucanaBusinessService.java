package ch.globaz.al.business.services.tucana;

import globaz.itucana.model.ITUModelBouclement;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.math.BigDecimal;
import java.util.ArrayList;
import ch.globaz.al.business.models.allocataire.AllocataireModel;
import ch.globaz.al.business.models.droit.EnfantModel;

public interface TucanaBusinessService extends JadeApplicationService {

    public void deleteBouclement(String numBouclement) throws JadeApplicationException, JadePersistenceException;

    public String getRubriqueAllocation(String typePrestation, String catTarif, AllocataireModel allocataire,
            EnfantModel enfant) throws JadeApplicationException;

    public ArrayList<String> getRubriqueSupplements(String typePrestation, String catTarif,
            AllocataireModel allocataire, EnfantModel enfant) throws JadeApplicationException;

    public BigDecimal getSupplementConventionnel(String typePrestation, BigDecimal montantCaisse,
            BigDecimal montantCanton, BigDecimal montant) throws JadeApplicationException;

    public BigDecimal getSupplementLegal(String typePrestation, BigDecimal montantCaisse, BigDecimal montantCanton,
            BigDecimal montant) throws JadeApplicationException;

    public ITUModelBouclement initBouclement(ITUModelBouclement bouclement, String annee, String mois)
            throws JadeApplicationException, JadeApplicationException, JadePersistenceException;
}
