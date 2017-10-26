package ch.globaz.pegasus.rpc.domaine;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.rpc.businessImpl.RpcTechnicalException;

public class PersonsElementsCalcul {
    private final List<PersonElementsCalcul> personsElementsCalcul;

    public PersonsElementsCalcul(List<PersonElementsCalcul> personsElementsCalcul) {
        this.personsElementsCalcul = personsElementsCalcul;
    }

    public PersonsElementsCalcul(PersonElementsCalcul personElementCalcul) {
        personsElementsCalcul = new ArrayList<PersonElementsCalcul>();
        personsElementsCalcul.add(personElementCalcul);
    }

    public List<PersonElementsCalcul> getPersonsElementsCalcul() {
        return personsElementsCalcul;
    }

    public PersonElementsCalcul getFirst() {

        return personsElementsCalcul.get(0);
    }

    public PersonsElementsCalcul filtreByRole(RoleMembreFamille roleMembreFamille) {
        List<PersonElementsCalcul> elementsCalcul = new ArrayList<PersonElementsCalcul>();
        for (PersonElementsCalcul personElementsCalcul : personsElementsCalcul) {
            if (roleMembreFamille.equals(personElementsCalcul.getMembreFamille().getRoleMembreFamille())) {
                elementsCalcul.add(personElementsCalcul);
            }
        }
        return new PersonsElementsCalcul(elementsCalcul);
    }

    public Montant sumHomeParticipationAuxCoutDesPatients() {
        Montant sum = Montant.ZERO_ANNUEL;
        for (PersonElementsCalcul elementsCalcul : personsElementsCalcul) {
            sum = sum.add(elementsCalcul.getHomeParticipationAuxCoutDesPatients());
        }
        return sum.arrondiAUnIntier();
    }

    public Montant sumHomeDepensesPersonnelles() {
        Montant sum = Montant.ZERO_ANNUEL;
        for (PersonElementsCalcul elementsCalcul : personsElementsCalcul) {
            sum = sum.add(elementsCalcul.getHomeDepensesPersonnelles());
        }
        return sum.arrondiAUnIntier();
    }

    public Montant sumPrimeLamal() {
        Montant sum = Montant.ZERO_ANNUEL;
        for (PersonElementsCalcul elementsCalcul : personsElementsCalcul) {
            sum = sum.add(elementsCalcul.getPrimeLamal());
        }
        return sum.arrondiAUnIntier();
    }

    public Montant sumAutresDepenses() {
        Montant sum = Montant.ZERO_ANNUEL;
        for (PersonElementsCalcul elementsCalcul : personsElementsCalcul) {
            sum = sum.add(elementsCalcul.getAutresDepenses());
        }
        return sum.arrondiAUnIntier();
    }

    public Montant sumHomeTaxeHomePrisEnCompte() {
        Montant sum = Montant.ZERO_ANNUEL;
        for (PersonElementsCalcul elementsCalcul : personsElementsCalcul) {
            sum = sum.add(elementsCalcul.getHomeTaxeHomePrisEnCompte());
        }
        // la calculateur fait un arrondi inférieur(Ceci me parait pas juste!!!) on est obligé de faire le même arrodi
        return sum.arrondiAUnIntier();
    }

    public Montant sumRenteAvsAi() {
        Montant sum = Montant.ZERO_ANNUEL;
        for (PersonElementsCalcul elementsCalcul : personsElementsCalcul) {
            sum = sum.add(elementsCalcul.getRenteAvsAi());
        }
        return sum.arrondiAUnIntierSupperior(); // 756.9313.5351.33 raison
    }

    public Montant sumRenteApi() {
        Montant sum = Montant.ZERO_ANNUEL;
        for (PersonElementsCalcul elementsCalcul : personsElementsCalcul) {
            sum = sum.add(elementsCalcul.getRenteApi());
        }
        return sum.arrondiAUnIntier();
    }

    public Montant sumRenteIj() {
        Montant sum = Montant.ZERO_ANNUEL;
        for (PersonElementsCalcul elementsCalcul : personsElementsCalcul) {
            sum = sum.add(elementsCalcul.getRenteIj());
        }
        return sum;
    }

    public Montant sumHomeContributionLca() {
        Montant sum = Montant.ZERO_ANNUEL;
        for (PersonElementsCalcul elementsCalcul : personsElementsCalcul) {
            sum = sum.add(elementsCalcul.getHomeContributionLca());
        }
        return sum.arrondiAUnIntier();
    }

    public Montant sumTotalRentes() {
        Montant sum = Montant.ZERO_ANNUEL;
        for (PersonElementsCalcul elementsCalcul : personsElementsCalcul) {
            sum = sum.add(elementsCalcul.getTotalRentes());
        }
        return sum.arrondiAUnIntier();
    }

    public Montant sumAutresRevenus() {
        Montant sum = Montant.ZERO_ANNUEL;
        for (PersonElementsCalcul elementsCalcul : personsElementsCalcul) {
            sum = sum.add(elementsCalcul.getAutresRevenus());
        }
        return sum.arrondiAUnIntierSupperior();
    }

    public boolean hasConjoint() {
        for (PersonElementsCalcul personneCalcul : personsElementsCalcul) {
            if (personneCalcul.getMembreFamille().getRoleMembreFamille().isConjoint()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasEnfant() {
        for (PersonElementsCalcul personneCalcul : personsElementsCalcul) {
            if (personneCalcul.getMembreFamille().getRoleMembreFamille().isEnfant()) {
                return true;
            }
        }
        return false;
    }

    public Montant sumUsufructIncome() {
        Montant sum = Montant.ZERO_ANNUEL;
        for (PersonElementsCalcul elementsCalcul : personsElementsCalcul) {
            sum = sum.add(elementsCalcul.getUsufructIncome());
        }
        return sum;
    }

    public Montant sumValeurLocativeProprietaire() {
        Montant sum = Montant.ZERO_ANNUEL;
        for (PersonElementsCalcul elementsCalcul : personsElementsCalcul) {
            sum = sum.add(elementsCalcul.getValeurLocativeProprietaire());
        }
        return sum;
    }

    public Montant sumRevenuBruteActiviteLucrative() {
        Montant sum = Montant.ZERO_ANNUEL;
        for (PersonElementsCalcul elementsCalcul : personsElementsCalcul) {
            sum = sum.add(elementsCalcul.getRevenuBruteActiviteLucrative());
        }
        return sum;
    }

    public Montant sumRevenuBrutHypothetique() {
        Montant sum = Montant.ZERO_ANNUEL;
        for (PersonElementsCalcul elementsCalcul : personsElementsCalcul) {
            sum = sum.add(elementsCalcul.getRevenuBrutHypothetique());
        }
        return sum;
    }

    public PersonElementsCalcul resolveRequerant() {
        for (PersonElementsCalcul elementsCalcul : personsElementsCalcul) {
            if (elementsCalcul.getMembreFamille().getRoleMembreFamille().isRequerant()) {
                return elementsCalcul;
            }
        }
        return null;
    }

    public PersonsElementsCalcul fusionElementsRequerantConjoint(RoleMembreFamille roleMembreFamille) {
        PersonElementsCalcul elementsCalculC = filtreByRole(RoleMembreFamille.CONJOINT).getFirst();
        PersonElementsCalcul elementsCalculR = filtreByRole(RoleMembreFamille.REQUERANT).getFirst();
        if (RoleMembreFamille.CONJOINT.equals(roleMembreFamille)) {
            return new PersonsElementsCalcul(fusionneMontants(elementsCalculC, elementsCalculR));
        } else if (RoleMembreFamille.REQUERANT.equals(roleMembreFamille)) {
            return new PersonsElementsCalcul(fusionneMontants(elementsCalculR, elementsCalculC));
        }
        throw new RpcTechnicalException(
                "Impossible de fusionner, aucuns requerant ou conjoint trouvé avec le roleMembreFamille suivant "
                        + roleMembreFamille);
    }

    private PersonElementsCalcul fusionneMontants(PersonElementsCalcul source, PersonElementsCalcul partner) {
        source.addRenteAvsAi(partner);
        source.addRenteApi(partner);
        source.addRenteIj(partner);
        source.addRevenuBruteActiviteLucrative(partner);
        source.addRevenuBrutHypothetique(partner);
        source.addTotalRentes(partner);
        source.addLpp(partner);
        source.addRenteEtrangere(partner);
        source.addAutresRevenus(partner);
        source.addUsufructIncome(partner);
        source.addValeurLocativeProprietaire(partner);
        source.addRetraitCapitalLpp(partner);
        // source.addPrimeLamal(partner);
        // source.addAutresDepenses(partner);
        // source.addHomeContributionLca(partner);
        // source.addHomeTaxeHomeTotal(partner);
        // source.addHomeTaxeHomePrisEnCompte(partner);
        // source.addHomeParticipationAuxCoutDesPatients(partner);
        // source.addHomeDepensesPersonnelles(partner);
        return source;
    }
}
