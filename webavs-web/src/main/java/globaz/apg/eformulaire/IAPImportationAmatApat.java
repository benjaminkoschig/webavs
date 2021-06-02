package globaz.apg.eformulaire;

import apg.amatapat.*;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.globall.db.BTransaction;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

public interface IAPImportationAmatApat {
    void createRoleApgTiers(String idTiers) throws Exception;
    void createContact(PRTiersWrapper tiers, String email) throws Exception;
    PRTiersWrapper createTiers(InsuredPerson assure, String codeNpa, boolean isWomen) throws Exception;
    PRDemande createDemande(String idTiers) throws Exception;
    APDroitLAPG createDroit(Content content, String npaFormat, PRDemande demande, BTransaction transaction);
    void createSituationFamiliale(FamilyMembers membresFamille, String idDroit, BTransaction transaction);
    void createSituationProfessionnel(Content content, APDroitLAPG droit, BTransaction transaction);
    void createAdresses(PRTiersWrapper tiers, AddressType adresseAssure, PaymentContact adressePaiement, String npa);
}
