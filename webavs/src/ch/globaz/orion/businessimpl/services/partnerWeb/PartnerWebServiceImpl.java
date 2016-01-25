package ch.globaz.orion.businessimpl.services.partnerWeb;

import globaz.globall.db.BSessionUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.services.AFAffiliationServices;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.orion.businessimpl.services.ServicesProviders;
import ch.globaz.xmlns.eb.partnerweb.ContactsAffilie;
import ch.globaz.xmlns.eb.partnerweb.PartnerWebService;

public class PartnerWebServiceImpl {

    private static PartnerWebService partnerWebService = ServicesProviders.partnerWebServiceProvide(BSessionUtil
            .getSessionFromThreadContext());

    public static Set<ContactEbusinessAffilie> searchAllActivContactAffilie() {
        List<ContactsAffilie> contacts = partnerWebService.searchAllActivContact();

        List<AfilliationForContactEb> affiliations = findAffiliationByNumerosAffilie(contacts);

        Set<ContactEbusinessAffilie> list = new ContactEbAffilierList().merge(contacts, affiliations);

        return list;
    }

    public static Set<ContactEbusinessAffilie> searchSuiviDeclarationContact() {

        String query = "select AFFIlIE.MALNAF as numeroAffilie, AFFIlIE.MADFIN as dateDeRadiation,  AFFIlIE.MATDEC as codeDeclaration, complement.JCJOVA as etape "
                + "       from schema.jojpcjo complement "
                + "       inner join (select journal.jgjoid, max(journal.jjouid) as maxEtape "
                + "                     from schema.JOJPJOU journal "
                + "                     join schema.jojpcjo complement on complement.JJOUID = journal.JJOUID "
                + "                     join schema.JOJPGJO groupe on groupe.JGJOID = journal.JGJOID "
                + "                    where complement.JCJOVA in (6700003, 6700009) "
                + "                      and (groupe.JGJORE is null or groupe.JGJORE = 0 ) "
                + "                    group by journal.jgjoid "
                + "             ) as suviDS on suviDS.maxEtape = complement.jjouid "
                + "        join schema.JOJPRED REFERANCECDestination ON REFERANCECDestination.JJOUID = complement.JJOUID "
                + "        join schema.AFAFFIP AFFIlIE ON AFFIlIE.HTITIE = REFERANCECDestination.JREDIR "
                + "        join schema.TIPERSP TIERS ON TIERS.HTITIE = AFFIlIE.HTITIE where jcjoty = 16000002 and complement.JCJOVA in (6200017,620005)";

        List<AfilliationForContactEb> suvies = QueryExecutor.execute(query, AfilliationForContactEb.class);

        List<String> affilierNumeros = resolveNumeroAffilies(suvies);

        List<ContactsAffilie> contacts = partnerWebService.searchContactByNoAffilie(affilierNumeros);

        Set<ContactEbusinessAffilie> list = new ContactEbAffilierList().merge(contacts, suvies)
                .filtreAffiliationNotNull();

        Set<ContactEbusinessAffilie> listFiltre = new HashSet<ContactEbusinessAffilie>();

        for (ContactEbusinessAffilie contactEbusinessAffilie : list) {
            // On n'envois le mail qu'aux administrateur et pas à une fiduciaire
            if (contactEbusinessAffilie.isAdministrateur() && !contactEbusinessAffilie.isMandataire()) {
                listFiltre.add(contactEbusinessAffilie);
            }
        }

        return listFiltre;
    }

    private static List<String> resolveNumeroAffilies(List<AfilliationForContactEb> suvies) {
        List<String> affilierNumeros = new ArrayList<String>();
        for (AfilliationForContactEb s : suvies) {
            if (s.getNumeroAffilie() != null && !s.getNumeroAffilie().trim().isEmpty()) {
                affilierNumeros.add(s.getNumeroAffilie().trim());
            }
        }
        return affilierNumeros;
    }

    private static List<AfilliationForContactEb> findAffiliationByNumerosAffilie(List<ContactsAffilie> contacts) {
        List<String> affilierNumeros = new ArrayList<String>();
        for (ContactsAffilie c : contacts) {
            if (c.getNumeroAffilie() != null && !c.getNumeroAffilie().trim().isEmpty()) {
                affilierNumeros.add(c.getNumeroAffilie().trim());
            }
        }

        List<AFAffiliation> affiliations = AFAffiliationServices.searchAffiliationByNumeros(affilierNumeros,
                BSessionUtil.getSessionFromThreadContext());
        List<AfilliationForContactEb> affilliationsEb = new ArrayList<AfilliationForContactEb>();

        for (AFAffiliation afAffiliation : affiliations) {
            AfilliationForContactEb aff = new AfilliationForContactEb();
            aff.setCodeDeclaration(afAffiliation.getDeclarationSalaire());
            aff.setDateDeRadiation(afAffiliation.getDateFin());
            aff.setNumeroAffilie(afAffiliation.getAffilieNumero());
            affilliationsEb.add(aff);
        }
        return affilliationsEb;
    }

}
