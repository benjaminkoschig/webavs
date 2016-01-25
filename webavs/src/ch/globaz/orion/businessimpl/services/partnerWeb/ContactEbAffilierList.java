package ch.globaz.orion.businessimpl.services.partnerWeb;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import ch.globaz.xmlns.eb.partnerweb.ContactsAffilie;

public class ContactEbAffilierList extends TreeSet<ContactEbusinessAffilie> {

    private Set<ContactEbusinessAffilie> list = new TreeSet<ContactEbusinessAffilie>();

    public ContactEbAffilierList merge(List<ContactsAffilie> contactFindResultBeans,
            List<AfilliationForContactEb> affilliationsForContact) {
        addAll(ContactEbAfillierMerger.merge(contactFindResultBeans, affilliationsForContact));
        return this;
    }

    public ContactEbAffilierList filtreAffiliationNotNull() {
        Set<ContactEbusinessAffilie> listFiltrer = new TreeSet<ContactEbusinessAffilie>();
        for (ContactEbusinessAffilie aff : this) {
            if ((aff.getEmail() != null && !aff.getEmail().trim().isEmpty())) {
                listFiltrer.add(aff);
            }
        }
        return this;
    }
}
