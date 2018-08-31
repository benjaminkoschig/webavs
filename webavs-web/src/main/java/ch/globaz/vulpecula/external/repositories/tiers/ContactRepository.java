package ch.globaz.vulpecula.external.repositories.tiers;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import ch.globaz.vulpecula.domain.repositories.Repository;
import ch.globaz.vulpecula.external.models.pyxis.Contact;
import ch.globaz.vulpecula.external.models.pyxis.MoyenContact;
import ch.globaz.vulpecula.external.models.pyxis.TypeContact;

/**
 * 
 * @since WebBMS 2.3
 */
public interface ContactRepository extends Repository<Contact> {

    Contact findForIdContact(String idContact);

    Contact findForIdTiersWithMoyens(String idTiers);

    HashMap<TypeContact, MoyenContact> findMoyenContactForIdTiers(String idTiers);

    Collection<Contact> findForNom(String nom);

    Collection<Contact> findForPrenom(String prenom);

    Contact findForIdTiersAndNom(String idTiers, String nom);

    List<Contact> findContactsByIdTiers(String idTiers);

}
