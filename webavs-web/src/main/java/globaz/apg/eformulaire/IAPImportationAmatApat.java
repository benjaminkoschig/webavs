package globaz.apg.eformulaire;

import apg.amatapat.Content;
import apg.amatapat.FamilyMembers;
import apg.amatapat.InsuredPerson;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

public interface IAPImportationAmatApat {
    void createRoleApgTiers(String idTiers) throws Exception;
    void createContact(PRTiersWrapper tiers, String email) throws Exception;
    PRTiersWrapper createTiers(InsuredPerson assure, String codeNpa, boolean isWomen) throws Exception;
    PRDemande createDemande(String idTiers) throws Exception;
    APDroitLAPG createDroit(Content content, String npaFormat, PRDemande demande, BTransaction transaction);
    void createSituationFamiliale(FamilyMembers membresFamille, String idDroit, BTransaction transactionn);
    void createSituationProfessionnel(Content content, String idDroit, BTransaction transaction);
}
