package ch.globaz.vulpecula.external.repositoriesjade.pyxis;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.vulpecula.external.models.ContactComplexModel;
import ch.globaz.vulpecula.external.models.ContactSearchComplexModel;
import ch.globaz.vulpecula.external.models.ContactSimpleModel;
import ch.globaz.vulpecula.external.models.MoyenContactTiersSearchSimpleModel;
import ch.globaz.vulpecula.external.models.pyxis.Contact;
import ch.globaz.vulpecula.external.models.pyxis.MoyenContact;
import ch.globaz.vulpecula.external.models.pyxis.TypeContact;
import ch.globaz.vulpecula.external.repositories.tiers.ContactRepository;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters.ContactConverter;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

/**
 * @since WebBMS 2.3
 */
public class ContactRepositoryJade extends RepositoryJade<Contact, ContactComplexModel, ContactSimpleModel> implements
        ContactRepository {
    private final Logger LOGGER = LoggerFactory.getLogger(ContactRepositoryJade.class);

    @Override
    public Contact findForIdContact(String idContact) {
        Contact contact = null;
        ContactSearchComplexModel contactSearch = new ContactSearchComplexModel();
        contactSearch.setForIdContact(idContact);
        try {
            JadePersistenceManager.search(contactSearch);
            if (contactSearch.getSize() > 0) {
                ContactComplexModel contactComplexModel = (ContactComplexModel) contactSearch.getSearchResults()[0];
                contact = ContactConverter.staticConvertToDomain(contactComplexModel);
            }
        } catch (JadePersistenceException ex) {
            LOGGER.error(ex.getMessage());
        }
        return contact;
    }

    @Override
    public HashMap<TypeContact, MoyenContact> findMoyenContactForIdTiers(String idTiers) {
        return findForIdTiersWithMoyens(idTiers).getMoyenContact();
    }

    @Override
    public Contact findForIdTiersWithMoyens(String idTiers) {
        Contact contact = null;
        ContactSearchComplexModel contactSearch = new ContactSearchComplexModel();
        contactSearch.setForIdTiers(idTiers);
        try {
            JadePersistenceManager.search(contactSearch);
            if (contactSearch.getSize() > 0) {
                ContactComplexModel contactComplexModel = (ContactComplexModel) contactSearch.getSearchResults()[0];
                contact = ContactConverter.staticConvertToDomain(contactComplexModel);

                MoyenContactTiersSearchSimpleModel moyenContactSearch = new MoyenContactTiersSearchSimpleModel();
                moyenContactSearch.setForId(contact.getId());
                JadePersistenceManager.search(moyenContactSearch);
                HashMap<TypeContact, MoyenContact> moyensContact = ContactConverter
                        .fillMoyensContact(moyenContactSearch);
                contact.setMoyenContact(moyensContact);
            }
        } catch (JadePersistenceException ex) {
            LOGGER.error(ex.getMessage());
        }
        return contact;
    }

    @Override
    public Contact findForIdTiersAndNom(String idTiers, String nom) {
        Contact contact = null;
        ContactSearchComplexModel contactSearch = new ContactSearchComplexModel();
        contactSearch.setForIdTiers(idTiers);
        contactSearch.setForNom(nom);
        try {
            JadePersistenceManager.search(contactSearch);
            if (contactSearch.getSize() > 0) {
                ContactComplexModel contactComplexModel = (ContactComplexModel) contactSearch.getSearchResults()[0];
                contact = ContactConverter.staticConvertToDomain(contactComplexModel);
            }
        } catch (JadePersistenceException ex) {
            LOGGER.error(ex.getMessage());
        }
        return contact;
    }

    @Override
    public Collection<Contact> findForNom(String nom) {
        // TODO à terminer
        Contact contact = null;
        ContactSearchComplexModel contactSearch = new ContactSearchComplexModel();
        contactSearch.setForNom(nom);
        try {
            JadePersistenceManager.search(contactSearch);
            if (contactSearch.getSize() > 0) {
                ContactComplexModel contactComplexModel = (ContactComplexModel) contactSearch.getSearchResults()[0];
                contact = ContactConverter.staticConvertToDomain(contactComplexModel);
            }
        } catch (JadePersistenceException ex) {
            LOGGER.error(ex.getMessage());
        }
        return Arrays.asList(contact);
    }

    @Override
    public Collection<Contact> findForPrenom(String prenom) {
        // TODO à terminer
        Contact contact = null;
        ContactSearchComplexModel contactSearch = new ContactSearchComplexModel();
        contactSearch.setForPrenom(prenom);
        try {
            JadePersistenceManager.search(contactSearch);
            if (contactSearch.getSize() > 0) {
                ContactComplexModel contactComplexModel = (ContactComplexModel) contactSearch.getSearchResults()[0];
                contact = ContactConverter.staticConvertToDomain(contactComplexModel);
            }
        } catch (JadePersistenceException ex) {
            LOGGER.error(ex.getMessage());
        }
        return Arrays.asList(contact);
    }

    @Override
    public DomaineConverterJade<Contact, ContactComplexModel, ContactSimpleModel> getConverter() {
        return new ContactConverter();
    }

    @Override
    public List<Contact> findContactsByIdTiers(String idTiers) {
        List<Contact> contacts = new ArrayList<Contact>();
        ContactSearchComplexModel contactSearch = new ContactSearchComplexModel();
        contactSearch.setForIdTiers(idTiers);
        try {
            JadePersistenceManager.search(contactSearch);
            if (contactSearch.getSize() > 0) {
                for (int i = 0; i < contactSearch.getSize(); i++) {
                    // ContactComplexModel contactComplexModel = (ContactComplexModel)
                    // contactSearch.getSearchResults()[i];
                    // contacts.add(ContactConverter.staticConvertToDomain(contactComplexModel));
                    Contact contact;
                    ContactComplexModel contactComplexModel = (ContactComplexModel) contactSearch.getSearchResults()[i];
                    contact = ContactConverter.staticConvertToDomain(contactComplexModel);

                    MoyenContactTiersSearchSimpleModel moyenContactSearch = new MoyenContactTiersSearchSimpleModel();
                    moyenContactSearch.setForId(contact.getId());
                    JadePersistenceManager.search(moyenContactSearch);
                    HashMap<TypeContact, MoyenContact> moyensContact = ContactConverter
                            .fillMoyensContact(moyenContactSearch);
                    contact.setMoyenContact(moyensContact);
                    contacts.add(contact);
                }
            }
        } catch (JadePersistenceException ex) {
            LOGGER.error(ex.getMessage());
        }
        return contacts;
    }

}
