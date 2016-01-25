package ch.globaz.orion.businessimpl.services.partnerWeb;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ch.globaz.xmlns.eb.partnerweb.ContactFindResultBean;
import ch.globaz.xmlns.eb.partnerweb.ContactsAffilie;
import ch.globaz.xmlns.eb.partnerweb.User;
import ch.globaz.xmlns.eb.partnerweb.UserLevelEnum;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;

class ContactEbAfillierMerger {

    public static Set<ContactEbusinessAffilie> merge(List<ContactsAffilie> contactsAffilie,
            List<AfilliationForContactEb> affilliationsForContact) {

        ImmutableListMultimap<String, AfilliationForContactEb> mapAffiliation = createMapAffilieByNumeroAffilie(affilliationsForContact);

        Set<ContactEbusinessAffilie> contacts = new HashSet<ContactEbusinessAffilie>();

        for (ContactsAffilie contactEb : contactsAffilie) {
            ImmutableList<AfilliationForContactEb> affiliations = mapAffiliation.get(contactEb.getNumeroAffilie());
            AfilliationForContactEb affiliation = null;
            if (!affiliations.isEmpty()) {
                affiliation = affiliations.get(0);
            }
            List<ContactEbusinessAffilie> contactsMerged = flateAndMerge(contactEb, affiliation);
            contacts.addAll(contactsMerged);
        }

        return contacts;
    }

    static ImmutableListMultimap<String, ContactFindResultBean> createMapContactByNumeroAffilie(
            List<ContactFindResultBean> affilisations) {
        ImmutableListMultimap<String, ContactFindResultBean> mapAffiliation = Multimaps.index(affilisations,
                new Function<ContactFindResultBean, String>() {
                    @Override
                    public String apply(ContactFindResultBean affiliation) {
                        return affiliation.getNumeroAffilie();
                    }
                });
        return mapAffiliation;
    }

    static ImmutableListMultimap<String, AfilliationForContactEb> createMapAffilieByNumeroAffilie(
            List<AfilliationForContactEb> affilisations) {
        ImmutableListMultimap<String, AfilliationForContactEb> mapAffiliation = Multimaps.index(affilisations,
                new Function<AfilliationForContactEb, String>() {
                    @Override
                    public String apply(AfilliationForContactEb affiliation) {
                        return affiliation.getNumeroAffilie();
                    }
                });
        return mapAffiliation;
    }

    static List<ContactEbusinessAffilie> flateAndMerge(ContactsAffilie contact, AfilliationForContactEb affiliation) {
        List<ContactEbusinessAffilie> list = new ArrayList<ContactEbusinessAffilie>();

        for (User user : contact.getAffilie().getUsers()) {
            list.add(mergeUnitaire(contact, user, affiliation, false));
        }

        if (contact.getFiduciaire() != null) {
            for (User user : contact.getFiduciaire().getUsers()) {
                list.add(mergeUnitaire(contact, user, affiliation, true));
            }
        }
        return list;
    }

    static ContactEbusinessAffilie mergeUnitaire(ContactsAffilie contact, User user,
            AfilliationForContactEb affiliation, boolean isFiduciaire) {
        ContactEbusinessAffilie contactEbusinessAffilie = new ContactEbusinessAffilie();

        if (contact != null) {
            contactEbusinessAffilie.setNom(contact.getAffilie().getNom());
            contactEbusinessAffilie.setNomMandataire(contact.getFiduciaire().getNom());
            contactEbusinessAffilie.setNumeroAffilie(contact.getNumeroAffilie());
            contactEbusinessAffilie.setEmail(user.getEmail());
            contactEbusinessAffilie.setNomUser(user.getNom());
            contactEbusinessAffilie.setPrenomUser(user.getPrenom());
            contactEbusinessAffilie.setUser(user.getNomLogin());
            contactEbusinessAffilie.setIsActif(user.isActif());
            contactEbusinessAffilie.setTelephoneUser(user.getTelephone());
            contactEbusinessAffilie.setIdUser(user.getUserId());
            contactEbusinessAffilie.setIsMandataire(isFiduciaire);
            resolveAndSetIsAdministrateur(user.getLevel(), contactEbusinessAffilie);
        }

        if (affiliation != null) {
            contactEbusinessAffilie.setNumeroAffilie(affiliation.getNumeroAffilie());
            contactEbusinessAffilie.setDateRadiation(affiliation.getDateDeRadiation());
            contactEbusinessAffilie.setCodeDeclaration(affiliation.getCodeDeclaration());
            contactEbusinessAffilie.setEtape(affiliation.getEtape());
        }
        return contactEbusinessAffilie;
    }

    static void resolveAndSetIsAdministrateur(UserLevelEnum level, ContactEbusinessAffilie contactEbusinessAffilie) {
        if (level.equals(UserLevelEnum.ADMIN)) {
            contactEbusinessAffilie.setIsAdministrateur(Boolean.TRUE);
        } else {
            contactEbusinessAffilie.setIsAdministrateur(Boolean.FALSE);
        }
    }
}
