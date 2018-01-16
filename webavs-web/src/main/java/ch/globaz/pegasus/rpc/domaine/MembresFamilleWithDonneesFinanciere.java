package ch.globaz.pegasus.rpc.domaine;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.loyer1.Loyer;
import ch.globaz.pegasus.business.domaine.membreFamille.MembreFamilleWithDonneesFinanciere;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariablesMetier;

/**
 * wrapper des membres famille avec donnée financière,
 * sert a distribuer de manière métier les donnée agrégées
 * 
 * @author cel
 * 
 */
public class MembresFamilleWithDonneesFinanciere {
    private final List<MembreFamilleWithDonneesFinanciere> membresFamilleWithDonneesFinanciere;
    private final VariablesMetier variablesMetier;

    public MembresFamilleWithDonneesFinanciere(
            List<MembreFamilleWithDonneesFinanciere> membresFamilleWithDonneesFinanciere,
            VariablesMetier variablesMetier) {
        this.membresFamilleWithDonneesFinanciere = membresFamilleWithDonneesFinanciere;
        this.variablesMetier = variablesMetier;
    }

    public List<MembreFamilleWithDonneesFinanciere> getMembresFamilleWithDonneesFinanciere() {
        return membresFamilleWithDonneesFinanciere;
    }

    public boolean isLoyerValeurLocative() {
        for (MembreFamilleWithDonneesFinanciere membre : membresFamilleWithDonneesFinanciere) {
            for (Loyer loyer : membre.getDonneesFinancieres().getLoyers().getList()) {
                if (loyer.getType().isValeurLocativeChezProprietaire()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Montant sumLoyerTotalBrut() {
        Montant loyerM = Montant.ZERO_ANNUEL;
        for (MembreFamilleWithDonneesFinanciere membre : membresFamilleWithDonneesFinanciere) {
            loyerM = loyerM.add(membre.getDonneesFinancieres().getLoyers()
                    .sumLoyerBrut(variablesMetier.getForfaitFraitChauffage().resolveMostRecent().getMontant()));
            loyerM = loyerM.add(membre.getDonneesFinancieres().getBiensImmobiliersServantHbitationPrincipale().sumMontantValeurLocative().isPositive() ?
                    membre.getDonneesFinancieres().getBiensImmobiliersServantHbitationPrincipale().sumMontantValeurLocative()
                        .add(variablesMetier.getForfaitCharge().resolveMostRecent().getMontant()) 
                    : Montant.ZERO_ANNUEL);
        }
        return loyerM.arrondiAUnIntierSupperior();
    }

    public int getNombreEnfants() {
        int nbEnfant = 0;
        for (MembreFamilleWithDonneesFinanciere membre : membresFamilleWithDonneesFinanciere) {
            if (membre.getRoleMembreFamille().isEnfant()) {
                nbEnfant++;
            }
        }
        return nbEnfant;
    }

    public MembresFamilleWithDonneesFinanciere filtreByRoleMembreFamille(RoleMembreFamille roleBeneficiaire) {
        List<MembreFamilleWithDonneesFinanciere> membresFamille = new ArrayList<MembreFamilleWithDonneesFinanciere>();
        for (MembreFamilleWithDonneesFinanciere iter : getMembresFamilleWithDonneesFinanciere()) {
            if (iter.getRoleMembreFamille().equals(roleBeneficiaire)) {
                membresFamille.add(iter);
            }
        }
        return new MembresFamilleWithDonneesFinanciere(membresFamille, variablesMetier);
    }
}
