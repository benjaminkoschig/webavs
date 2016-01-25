/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;

/**
 * @author ECO
 * 
 */
public class CalculComparatif implements ICalculComparatif {

    private CalculComparatif ccConjoint = null;

    private String etatPC;
    private String excedentAnnuel = null;
    private String idPlanCalcul = null;
    private boolean isPlanRetenu = false;
    private String montantDonation;
    private String montantMensuelConjoint = null;
    private String montantMensuelRequerant = null;
    private String montantPCMensuel = null;
    private String montantPrixHome;
    private String montantRentesAvsAi;
    private TupleDonneeRapport montants = null;
    private String nbEnfants;
    private List<PersonnePCAccordee> personnes = null;
    private String primeMoyenneAssMaladie = null;

    /**
	 * 
	 */
    public CalculComparatif() {
        super();
    }

    // public void arronditMontants() {
    // this.montants.arronditValeurs();
    // }

    public CalculComparatif copyCC() {

        CalculComparatif cc = new CalculComparatif();
        cc.etatPC = etatPC;
        cc.excedentAnnuel = excedentAnnuel;
        cc.idPlanCalcul = idPlanCalcul;
        cc.isPlanRetenu = isPlanRetenu;
        cc.montantDonation = montantDonation;
        cc.montantMensuelConjoint = montantMensuelConjoint;
        cc.montantPCMensuel = montantPCMensuel;
        cc.montantPrixHome = montantPrixHome;
        cc.montantRentesAvsAi = montantRentesAvsAi;
        cc.montantPrixHome = montantPrixHome;
        cc.montants = montants;
        cc.nbEnfants = nbEnfants;
        cc.personnes = personnes;
        cc.primeMoyenneAssMaladie = primeMoyenneAssMaladie;

        return cc;

    }

    @Override
    public boolean equalsPersonnes(Collection<String> idPersonnes) {
        return getIdPersonnes().equals(new TreeSet<String>(idPersonnes));
    }

    public CalculComparatif getCcConjoint() {
        return ccConjoint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.ICalculComparatif#getEtatPC()
     */
    @Override
    public String getEtatPC() {
        return etatPC;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.ICalculComparatif#getExcedentAnnuel()
     */
    @Override
    public String getExcedentAnnuel() {
        return excedentAnnuel;
    }

    @Override
    public Collection<String> getIdPersonnes() {
        Set<String> result = new TreeSet<String>();

        for (PersonnePCAccordee personne : personnes) {
            result.add(personne.getIdPersonne());
        }

        return result;
    }

    @Override
    public String getIdPlanCalcul() {
        return idPlanCalcul;
    }

    public boolean getIsPlanretenu() {
        return isPlanRetenu;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.ICalculComparatif#getMontantDonation()
     */
    @Override
    public String getMontantDonation() {
        return montantDonation;
    }

    public String getMontantMensuelConjoint() {
        return montantMensuelConjoint;
    }

    public String getMontantMensuelRequerant() {
        return montantMensuelRequerant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.ICalculComparatif#getMontantPCMensuel()
     */
    @Override
    public String getMontantPCMensuel() {
        return montantPCMensuel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.ICalculComparatif#getMontantPrixHome()
     */
    @Override
    public String getMontantPrixHome() {
        return montantPrixHome;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.ICalculComparatif#getMontantRentesAvsAi()
     */
    @Override
    public String getMontantRentesAvsAi() {
        return montantRentesAvsAi;
    }

    /**
     * @return the montants
     */
    @Override
    public TupleDonneeRapport getMontants() {
        return montants;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.ICalculComparatif#getNbEnfants()
     */
    @Override
    public String getNbEnfants() {
        return nbEnfants;
    }

    /**
     * @return the personnes
     */
    public List<PersonnePCAccordee> getPersonnes() {
        return personnes;
    }

    /*
	 * 
	 */
    public String getPrimeMoyenneAssMaladie() {
        return primeMoyenneAssMaladie;
    }

    /**
     * @return the isPlanRetenu
     */
    public boolean isPlanRetenu() {
        return isPlanRetenu;
    }

    public void setCcConjoint(CalculComparatif ccConjoint) {
        this.ccConjoint = ccConjoint;
    }

    public void setEtatPC(String etatPC) {
        this.etatPC = etatPC;
    }

    public void setExcedentAnnuel(String excedentAnnuel) {
        this.excedentAnnuel = excedentAnnuel;
    }

    public void setIdPlanCalcul(String idPlanCalcul) {
        this.idPlanCalcul = idPlanCalcul;
    }

    public void setMontantMensuelConjoint(String montantMensuelConjoint) {
        this.montantMensuelConjoint = montantMensuelConjoint;
    }

    public void setMontantMensuelRequerant(String montantMensuelRequerant) {
        this.montantMensuelRequerant = montantMensuelRequerant;
    }

    /**
     * @param montantPCMensuel
     *            the montantPCMensuel to set
     */
    public void setMontantPCMensuel(String montantPCMensuel) {
        this.montantPCMensuel = montantPCMensuel;
    }

    /**
     * @param montants
     *            the montants to set
     */
    public void setMontants(TupleDonneeRapport montants) {
        this.montants = montants;

        // definit montant mensuel
        float montantMensuel = montants.getValeurEnfant(IPCValeursPlanCalcul.CLE_TOTAL_CC_DEDUIT_MENSUEL);
        montantPCMensuel = String.valueOf(montantMensuel);

        // lors de couple à DOM avec 2 rentes principales
        float montantMensuelRequerantFloat = montants
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_TOTAL_DEDUIT_MENSUEL_REQ);
        montantMensuelRequerant = String.valueOf(montantMensuelRequerantFloat);
        float montantMensuelConjointFloat = montants
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_TOTAL_DEDUIT_MENSUEL_CONJOINT);
        montantMensuelConjoint = String.valueOf(montantMensuelConjointFloat);

        etatPC = montants.getLegendeEnfant(IPCValeursPlanCalcul.CLE_TOTAL_CC_STATUS);

        float montantAnnuel = montants.getValeurEnfant(IPCValeursPlanCalcul.CLE_TOTAL_CC);
        excedentAnnuel = String.valueOf(montantAnnuel);

        float primeAssMaladie = montants.getValeurEnfant(IPCValeursPlanCalcul.CLE_TOTAL_PRIMEMAL_TOTAL);
        primeMoyenneAssMaladie = String.valueOf(primeAssMaladie);

        float montantDonation = montants.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_DESS_TOTAL);
        this.montantDonation = String.valueOf(montantDonation);

        float montantRentesAvsAi = montants.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENAVSAI_TOTAL);
        this.montantRentesAvsAi = String.valueOf(montantRentesAvsAi);

        float montantPrixHome = montants.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_TAXEHOME_TOTAL);
        this.montantPrixHome = String.valueOf(montantPrixHome);

        float nbEnfants = 0;
        if (personnes != null) {
            for (Iterator<PersonnePCAccordee> iterator = personnes.iterator(); iterator.hasNext();) {
                PersonnePCAccordee personnePCA = iterator.next();
                if (IPCDroits.CS_ROLE_FAMILLE_ENFANT.equalsIgnoreCase(personnePCA.getCsRoleFamille())) {
                    nbEnfants++;
                }
            }
        }
        this.nbEnfants = String.valueOf(nbEnfants);
    }

    /**
     * @param personnes
     *            the personnes to set
     */
    public void setPersonnes(List<PersonnePCAccordee> personnes) {
        this.personnes = personnes;
    }

    /**
     * @param isPlanRetenu
     *            the isPlanRetenu to set
     */
    public void setPlanRetenu(boolean isPlanRetenu) {
        this.isPlanRetenu = isPlanRetenu;
    }

    public void setPrimeMoyenneAssMaladie(String primeMoyenneAssMaladie) {
        this.primeMoyenneAssMaladie = primeMoyenneAssMaladie;
    }

}
