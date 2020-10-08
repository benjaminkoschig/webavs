/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul;

import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;

import java.util.*;

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
    private String montantPrixHomeReforme;
    private String montantRentesAvsAi;
    private String montantPartCantonale = null;
    private TupleDonneeRapport montants = null;
    private String nbEnfants;
    private List<PersonnePCAccordee> personnes = null;
    private String primeMoyenneAssMaladie = null;
    private String primeVerseeAssMaladie = null;
    private boolean isReformePc = false;
    private boolean comparer = false;
    private boolean refusFortune = false;
    private String fortune = null;

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
        cc.montantPartCantonale = montantPartCantonale;
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

    public String getMontantPartCantonale() {
        return montantPartCantonale;
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

    public void setMontantPartCantonale(String montantPartCantonale) {
        this.montantPartCantonale = montantPartCantonale;
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

        float montantPrixHomeReforme = montants.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TAXES_PENSION_RECONNUE);
        this.montantPrixHomeReforme = String.valueOf(montantPrixHomeReforme);

        float primeVerseeAssMaladie = montants.getValeurEnfant(IPCValeursPlanCalcul.MONTANT_VERSE_CAISSE_MALADIE);
        this.primeVerseeAssMaladie = String.valueOf(primeVerseeAssMaladie);

        float montantPrixHome = montants.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_TAXEHOME_TOTAL);
        this.montantPrixHome = String.valueOf(montantPrixHome);

        float montantPartCantonale = montants.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_PART_CANTONALE);
        this.montantPartCantonale = String.valueOf(montantPartCantonale);

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

        if(montants.containsValeurEnfant(IPCValeursPlanCalcul.CLE_REFUS_SEUIL_FORTUNE)){
            this.refusFortune = true;
            this.fortune = String.valueOf(montants.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL_AVANT_FRACTION));
        }
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

    public boolean isReformePc() {
        return isReformePc;
    }

    public void setReformePc(boolean reformePc) {
        isReformePc = reformePc;
    }

    public boolean isComparer() {
        return comparer;
    }

    public void setComparer(boolean comparer) {
        this.comparer = comparer;
    }

    public boolean isRefusFortune() {
        return this.refusFortune;
    }

    public void setRefusFortune(boolean refusFortune) {
        this.refusFortune = refusFortune;
    }

    public String getFortune() {
        return fortune;
    }

    public void setFortune(String fortune) {
        this.fortune = fortune;
    }

    public String getMontantPrixHomeReforme() {
        return montantPrixHomeReforme;
    }

    public void setMontantPrixHomeReforme(String montantPrixHomeReforme) {
        this.montantPrixHomeReforme = montantPrixHomeReforme;
    }

    public String getPrimeVerseeAssMaladie() {
        return primeVerseeAssMaladie;
    }

    public void setPrimeVerseeAssMaladie(String primeVerseeAssMaladie) {
        this.primeVerseeAssMaladie = primeVerseeAssMaladie;
    }
}
