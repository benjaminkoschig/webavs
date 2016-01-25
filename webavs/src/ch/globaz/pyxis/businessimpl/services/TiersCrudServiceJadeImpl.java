package ch.globaz.pyxis.businessimpl.services;

import globaz.globall.db.BSessionUtil;
import globaz.pyxis.db.tiers.TITiers;
import ch.globaz.corvus.businessimpl.services.models.CanevasCrudService;
import ch.globaz.pyxis.business.services.TiersCrudService;
import ch.globaz.pyxis.domaine.Tiers;
import ch.globaz.pyxis.exceptions.TITechnicalExcpetion;

/**
 * Service de persistance pour un tiers, utilisant Jade comme couche d'accès à la base de donnée
 */
public class TiersCrudServiceJadeImpl extends CanevasCrudService<Tiers> implements TiersCrudService {

    @Override
    public Tiers create(Tiers objetDeDomaine) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public boolean delete(Long id) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public Tiers read(Long id) {

        Tiers objetDeDomaine = new Tiers();
        objetDeDomaine.setId(id);

        try {
            TITiers tiers = new TITiers();
            tiers.setSession(BSessionUtil.getSessionFromThreadContext());
            tiers.setIdTiers(objetDeDomaine.getId().toString());
            tiers.retrieve();

            objetDeDomaine.setDesignation1(tiers.getDesignation1());
            objetDeDomaine.setDesignation2(tiers.getDesignation2());
        } catch (Exception ex) {
            throw new TITechnicalExcpetion(ex);
        }

        return objetDeDomaine;
    }

    @Override
    public Tiers update(Tiers objetDeDomaine) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
