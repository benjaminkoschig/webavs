package globaz.apg.process;

import apg.amatapat.Content;
import apg.amatapat.FamilyMembers;
import apg.amatapat.InsuredPerson;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

public interface IAPImportationAmatApat {
    void createRoleApgTiers(String idTiers, BSession bsession) throws Exception;
    void createContact(PRTiersWrapper tiers, String email, BSession bsession) throws Exception;
    PRTiersWrapper createTiers(InsuredPerson assure, String codeNpa, BSession bsession, boolean isWomen) throws Exception;
    PRDemande createDemande(String idTiers, BSession bsession) throws Exception;
    APDroitLAPG createDroit(Content content, String npaFormat, PRDemande demande, BTransaction transaction, BSession bsession);
    void createSituationFamiliale(FamilyMembers membresFamille, String idDroit, BTransaction transaction, BSession bsession);
    void createSituationProfessionnel(Content content, String idDroit, BTransaction transaction, BSession bsession);
}
