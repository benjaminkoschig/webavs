package ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.HashMap;
import org.apache.commons.lang.NotImplementedException;
import ch.globaz.vulpecula.external.models.ContactComplexModel;
import ch.globaz.vulpecula.external.models.ContactSearchSimpleModel;
import ch.globaz.vulpecula.external.models.ContactSimpleModel;
import ch.globaz.vulpecula.external.models.MoyenContactTiersSearchSimpleModel;
import ch.globaz.vulpecula.external.models.MoyenContactTiersSimpleModel;
import ch.globaz.vulpecula.external.models.pyxis.Contact;
import ch.globaz.vulpecula.external.models.pyxis.MoyenContact;
import ch.globaz.vulpecula.external.models.pyxis.TypeContact;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

/**
 * @since WebBMS 2.3
 */
public final class ContactConverter implements DomaineConverterJade<Contact, ContactComplexModel, ContactSimpleModel> {

    @Override
    public Contact convertToDomain(ContactComplexModel contactModel) {
        Contact contact = new Contact();

        contact.setId(contactModel.getId());
        contact.setNom(contactModel.getContactSimpleModel().getNom());
        contact.setPrenom(contactModel.getContactSimpleModel().getPrenom());
        contact.setSpy(contactModel.getSpy());

        return contact;
    }

    public static Contact staticConvertToDomain(ContactComplexModel contactModel) {
        Contact contact = new Contact();

        contact.setId(contactModel.getId());
        contact.setNom(contactModel.getContactSimpleModel().getNom());
        contact.setPrenom(contactModel.getContactSimpleModel().getPrenom());
        contact.setSpy(contactModel.getSpy());

        return contact;
    }

    public static HashMap<TypeContact, MoyenContact> fillMoyensContact(
            MoyenContactTiersSearchSimpleModel moyenContactSearch) {
        HashMap<TypeContact, MoyenContact> moyens = new HashMap<TypeContact, MoyenContact>();
        for (int i = 0; i < moyenContactSearch.getSize(); i++) {
            MoyenContactTiersSimpleModel moyenSimpleModel = (MoyenContactTiersSimpleModel) moyenContactSearch
                    .getSearchResults()[i];
            MoyenContact moyen = new MoyenContact();
            moyen.setApplication(moyenSimpleModel.getApplication());
            moyen.setId(moyenSimpleModel.getId());
            moyen.setIdContact(moyenSimpleModel.getIdContact());
            moyen.setSpy(moyenSimpleModel.getSpy());
            moyen.setValeur(moyenSimpleModel.getValeur());
            if (TypeContact.isValid(moyenSimpleModel.getTypeContact())) {
                moyen.setType(TypeContact.fromValue(moyenSimpleModel.getTypeContact()));
                moyens.put(moyen.getType(), moyen);
            }
        }
        return moyens;
    }

    @Override
    public ContactSimpleModel convertToPersistence(Contact entity) {
        ContactSimpleModel model = new ContactSimpleModel();
        model.setIdContact(entity.getId());
        model.setNom(entity.getNom());
        model.setPrenom(entity.getPrenom());
        model.setSpy(entity.getSpy());
        return model;
    }

    @Override
    public Contact convertToDomain(ContactSimpleModel simpleModel) {
        // TODO Auto-generated method stub
        throw new NotImplementedException();
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new ContactSearchSimpleModel();

    }
}
