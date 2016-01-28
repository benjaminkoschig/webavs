package ch.globaz.pyxis.businessimpl.services;

import globaz.globall.db.BSessionUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import ch.globaz.corvus.businessimpl.services.models.CanevasCrudService;
import ch.globaz.pyxis.business.services.PersonneAvsCrudService;
import ch.globaz.pyxis.domaine.NumeroSecuriteSociale;
import ch.globaz.pyxis.domaine.PersonneAVS;
import ch.globaz.pyxis.exceptions.TITechnicalExcpetion;

/**
 * Implementation avec le framework Jade du service de DAO pour les personnes avec numéro AVS
 */
public class PersonneAvsCrudServiceJadeImpl extends CanevasCrudService<PersonneAVS> implements PersonneAvsCrudService {

    @Override
    public PersonneAVS create(PersonneAVS objetDeDomaine) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public boolean delete(Long id) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public PersonneAVS read(Long id) {

        PersonneAVS objetDeDomaine = new PersonneAVS();
        objetDeDomaine.setId(id);

        try {
            PRTiersWrapper tiers = PRTiersHelper.getTiersParId(BSessionUtil.getSessionFromThreadContext(),
                    objetDeDomaine.getId().toString());

            objetDeDomaine.setNom(tiers.getNom());
            objetDeDomaine.setPrenom(tiers.getPrenom());
            objetDeDomaine.setDateNaissance(tiers.getDateNaissance());
            objetDeDomaine.setDateDeces(tiers.getDateDeces());
            objetDeDomaine.setNss(new NumeroSecuriteSociale(tiers.getNSS()));

        } catch (Exception ex) {
            throw new TITechnicalExcpetion(ex);
        }

        return objetDeDomaine;
    }

    @Override
    public PersonneAVS update(PersonneAVS objetDeDomaine) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
