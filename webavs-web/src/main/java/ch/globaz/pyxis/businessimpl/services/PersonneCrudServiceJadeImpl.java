package ch.globaz.pyxis.businessimpl.services;

import globaz.globall.db.BSessionUtil;
import globaz.pyxis.db.tiers.TIPersonne;
import ch.globaz.corvus.businessimpl.services.models.CanevasCrudService;
import ch.globaz.pyxis.business.services.PersonneCrudService;
import ch.globaz.pyxis.domaine.Personne;
import ch.globaz.pyxis.exceptions.TITechnicalExcpetion;

public class PersonneCrudServiceJadeImpl extends CanevasCrudService<Personne> implements PersonneCrudService {

    @Override
    public Personne create(Personne objetDeDomaine) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public boolean delete(Long id) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public Personne read(Long id) {

        Personne objetDeDomaine = new Personne();
        objetDeDomaine.setId(id);

        try {
            TIPersonne personne = new TIPersonne();
            personne.setSession(BSessionUtil.getSessionFromThreadContext());
            personne.setIdTiers(objetDeDomaine.getId().toString());
            personne.retrieve();

            objetDeDomaine.setDateNaissance(personne.getDateNaissance());
            objetDeDomaine.setDateDeces(personne.getDateDeces());
            objetDeDomaine.setNom(personne.getDesignation1());
            objetDeDomaine.setPrenom(personne.getDesignation2());
        } catch (Exception ex) {
            throw new TITechnicalExcpetion(ex);
        }

        return objetDeDomaine;
    }

    @Override
    public Personne update(Personne objetDeDomaine) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
