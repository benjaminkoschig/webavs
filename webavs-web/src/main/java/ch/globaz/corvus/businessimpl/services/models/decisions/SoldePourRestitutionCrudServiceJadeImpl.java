package ch.globaz.corvus.businessimpl.services.models.decisions;

import globaz.corvus.db.ordresversements.RESoldePourRestitution;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.globall.db.BSessionUtil;
import java.math.BigDecimal;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.corvus.business.services.models.decisions.SoldePourRestitutionCrudService;
import ch.globaz.corvus.business.services.models.ordresversements.OrdresVersementCrudService;
import ch.globaz.corvus.businessimpl.services.models.CanevasCrudService;
import ch.globaz.corvus.domaine.OrdreVersement;
import ch.globaz.corvus.domaine.SoldePourRestitution;
import ch.globaz.corvus.domaine.constantes.TypeSoldePourRestitution;

/**
 * Implementation du service de persistance des soldes pour restitution des rentes, utilisant Jade comme couche d'accès
 * à la base de données
 */
public class SoldePourRestitutionCrudServiceJadeImpl extends CanevasCrudService<SoldePourRestitution> implements
        SoldePourRestitutionCrudService {

    private OrdresVersementCrudService ordresVersementCrudService;

    public SoldePourRestitutionCrudServiceJadeImpl(OrdresVersementCrudService ordresVersementCrudService) {
        super();

        Checkers.checkNotNull(ordresVersementCrudService, "ordresVersementCrudService");

        this.ordresVersementCrudService = ordresVersementCrudService;
    }

    @Override
    public SoldePourRestitution create(SoldePourRestitution objetDeDomaine) {

        try {
            RESoldePourRestitution soldePourRestitution = new RESoldePourRestitution();
            soldePourRestitution.setSession(BSessionUtil.getSessionFromThreadContext());

            soldePourRestitution.setIdOrdreVersement(objetDeDomaine.getOrdreVersement().getId().toString());
            soldePourRestitution.setIdPrestation(objetDeDomaine.getOrdreVersement().getPrestation().getId().toString());

            soldePourRestitution.setMontant(objetDeDomaine.getMontantRestitution().toString());
            soldePourRestitution.setMontantMensuelARetenir(objetDeDomaine.getMontantRetenueMensuelle().toString());
            soldePourRestitution.setCsTypeRestitution(objetDeDomaine.getType().getCodeSysteme().toString());

            soldePourRestitution.add();

            objetDeDomaine.setId(Long.parseLong(soldePourRestitution.getIdSoldePourRestitution()));
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }

        return objetDeDomaine;
    }

    @Override
    public boolean delete(Long id) {

        try {
            RESoldePourRestitution soldePourRestitution = new RESoldePourRestitution();
            soldePourRestitution.setSession(BSessionUtil.getSessionFromThreadContext());
            soldePourRestitution.setIdSoldePourRestitution(id.toString());
            soldePourRestitution.retrieve();

            soldePourRestitution.delete();
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }

        return true;
    }

    @Override
    public SoldePourRestitution read(Long id) {

        SoldePourRestitution objetDeDomaine = new SoldePourRestitution();
        objetDeDomaine.setId(id);

        try {
            RESoldePourRestitution soldePourRestitution = new RESoldePourRestitution();
            soldePourRestitution.setSession(BSessionUtil.getSessionFromThreadContext());
            soldePourRestitution.setIdSoldePourRestitution(objetDeDomaine.getId().toString());
            soldePourRestitution.retrieve();

            objetDeDomaine.setType(TypeSoldePourRestitution.parse(soldePourRestitution.getCsTypeRestitution()));
            objetDeDomaine.setMontantRestitution(new BigDecimal(soldePourRestitution.getMontant()));
            objetDeDomaine.setMontantRetenueMensuelle(new BigDecimal(soldePourRestitution.getMontantMensuelARetenir()));

            OrdreVersement ordreVersement = ordresVersementCrudService.read(Long.parseLong(soldePourRestitution
                    .getIdOrdreVersement()));

            objetDeDomaine.setOrdreVersement(ordreVersement);
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }

        return objetDeDomaine;
    }

    @Override
    public SoldePourRestitution update(SoldePourRestitution objetDeDomaine) {

        try {
            RESoldePourRestitution soldePourRestitution = new RESoldePourRestitution();
            soldePourRestitution.setSession(BSessionUtil.getSessionFromThreadContext());
            soldePourRestitution.setIdSoldePourRestitution(objetDeDomaine.getId().toString());
            soldePourRestitution.retrieve();

            // il n'est possible de modifier que le montant total, le montant mensuel et le type de solde
            soldePourRestitution.setMontant(objetDeDomaine.getMontantRestitution().toString());
            soldePourRestitution.setMontantMensuelARetenir(objetDeDomaine.getMontantRetenueMensuelle().toString());
            soldePourRestitution.setCsTypeRestitution(objetDeDomaine.getType().getCodeSysteme().toString());

            soldePourRestitution.update();
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }

        return objetDeDomaine;
    }
}
