package ch.globaz.orion.businessimpl.services.partnerWeb;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import ch.globaz.xmlns.eb.partnerweb.ContactFindResultBean;
import ch.globaz.xmlns.eb.partnerweb.UserLevelEnum;

public class ContactEbAfillierMergerTest {

    @Test
    public void testResolveAndSetIsAdministrateurTrue() throws Exception {
        ContactEbusinessAffilie contact = buildContactEbAff();
        ContactEbAfillierMerger.resolveAndSetIsAdministrateur(UserLevelEnum.ADMIN, contact);
        assertTrue(contact.isAdministrateur());
    }

    @Test
    public void testResolveAndSetIsAdministrateurFalse() throws Exception {
        for (UserLevelEnum val : UserLevelEnum.values()) {
            if (!val.equals(UserLevelEnum.ADMIN)) {
                ContactEbusinessAffilie contact = buildContactEbAff();
                ContactEbAfillierMerger.resolveAndSetIsAdministrateur(val, contact);
                assertFalse(contact.isAdministrateur());
            }
        }
    }

    @Test
    public void testCreateMapAffilieByNumeroAffilie() throws Exception {
        List<AfilliationForContactEb> affilisations = new ArrayList<AfilliationForContactEb>();
        AfilliationForContactEb aff = buildAffiliation();
        aff.setNumeroAffilie("");
        affilisations.add(aff);
        assertEquals(1, ContactEbAfillierMerger.createMapAffilieByNumeroAffilie(affilisations).size());
    }

    private AfilliationForContactEb buildAffiliation() {
        return buildAffiliation("100.000");
    }

    private AfilliationForContactEb buildAffiliation(String numeroAffiliation) {
        AfilliationForContactEb afAffiliation = new AfilliationForContactEb();
        afAffiliation.setNumeroAffilie(numeroAffiliation);
        afAffiliation.setDateDeRadiation("01.01.2015");
        afAffiliation.setCodeDeclaration("codeDeclaration");
        return afAffiliation;
    }

    private ContactEbusinessAffilie buildContactEbAff() {
        ContactEbusinessAffilie contactExpected = new ContactEbusinessAffilie();
        contactExpected.setNom("nom");
        contactExpected.setEmail("mail");
        contactExpected.setNumeroAffilie("100.000");
        contactExpected.setIsMandataire(false);
        contactExpected.setIsAdministrateur(true);
        return contactExpected;
    }

    private ContactFindResultBean buildContactEb() {
        return buildContactEb("100.000");
    }

    private ContactFindResultBean buildContactEb(String numeros) {
        ContactFindResultBean contactFindResultBean = new ContactFindResultBean();
        contactFindResultBean.setNom("nom");
        contactFindResultBean.setEmail("mail");
        contactFindResultBean.setNumeroAffilie(numeros);
        contactFindResultBean.setLevel(UserLevelEnum.ADMIN);
        return contactFindResultBean;
    }

}
