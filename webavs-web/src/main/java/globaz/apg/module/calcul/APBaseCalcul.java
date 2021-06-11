/*
 * Créé le 27 avr. 05
 */
package globaz.apg.module.calcul;

import globaz.globall.util.JADate;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Description Données de base nécessaires au calcul des prestations APG. Si le no de révision est spécifiée, les donnée
 * de références pour le calcul de l'APG à récupérer seront basée sur cette révision. Si ce n'est pas le cas, les
 * données de références seront récupérée en fonction des dates données (dateDebut et dateFin)
 * 
 * @author scr
 */
public class APBaseCalcul implements Cloneable {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private boolean allocationExploitation = false;

    private boolean allocationMaximum = false;
    private List basesCalculSituationProfessionnel = null;

    private JADate dateDebut = null;
    private JADate dateFin = null;
    private String idTauxImposition = "";
    private boolean independant = false;

    private int nombreEnfants = 0;
    private int nombreJoursSoldes = 0;
    private String noRevision = null;

    private boolean salarie = false;

    private boolean soumisImpotSource;
    private String tauxImposition = "";
    private String typeAllocation = null;

    // Ajout boolean uniquement pour extension Maternité
    @Setter
    @Getter
    private boolean isExtension = false;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APBaseCalcul.
     */
    public APBaseCalcul() {
        basesCalculSituationProfessionnel = new ArrayList();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param bcSitProf
     *            DOCUMENT ME!
     */
    public void addBaseCalculSituationProfessionnel(APBaseCalculSituationProfessionnel bcSitProf) {
        basesCalculSituationProfessionnel.add(bcSitProf);
    }

    /**
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        APBaseCalcul retValue = (APBaseCalcul) super.clone();

        retValue.basesCalculSituationProfessionnel = new ArrayList(basesCalculSituationProfessionnel);

        return retValue;
    }

    /**
     * getter pour l'attribut bases calcul situation professionnel
     * 
     * @return la valeur courante de l'attribut bases calcul situation professionnel
     */
    public List getBasesCalculSituationProfessionnel() {
        return basesCalculSituationProfessionnel;
    }

    /**
     * getter pour l'attribut date debut
     * 
     * @return la valeur courante de l'attribut date debut
     */
    public JADate getDateDebut() {
        return dateDebut;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public JADate getDateFin() {
        return dateFin;
    }

    /**
     * getter pour l'attribut canton imposition
     * 
     * @return la valeur courante de l'attribut canton imposition
     */
    public String getIdTauxImposition() {
        return idTauxImposition;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public int getNombreEnfants() {
        return nombreEnfants;
    }

    /**
     * getter pour l'attribut nombre jours soldes
     * 
     * @return la valeur courante de l'attribut nombre jours soldes
     */
    public int getNombreJoursSoldes() {
        return nombreJoursSoldes;
    }

    /**
     * getter pour l'attribut no revision
     * 
     * @return la valeur courante de l'attribut no revision
     */
    public String getNoRevision() {
        return noRevision;
    }

    /**
     * getter pour l'attribut taux imposition
     * 
     * @return la valeur courante de l'attribut taux imposition
     */
    public String getTauxImposition() {
        return tauxImposition;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getTypeAllocation() {
        return typeAllocation;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public boolean isAllocationExploitation() {
        return allocationExploitation;
    }

    /**
     * getter pour l'attribut allocation maximum
     * 
     * @return la valeur courante de l'attribut allocation maximum
     */
    public boolean isAllocationMaximum() {
        return allocationMaximum;
    }

    /**
     * getter pour l'attribut independant
     * 
     * @return la valeur courante de l'attribut independant
     */
    public boolean isIndependant() {
        return independant;
    }

    /**
     * getter pour l'attribut salarie
     * 
     * @return la valeur courante de l'attribut salarie
     */
    public boolean isSalarie() {
        return salarie;
    }

    /**
     * getter pour l'attribut sans activite lucrative
     * 
     * @return la valeur courante de l'attribut sans activite lucrative
     */
    public boolean isSansActiviteLucrative() {
        return !(independant || salarie);
    }

    /**
     * getter pour l'attribut soumis impot source
     * 
     * @return la valeur courante de l'attribut soumis impot source
     */
    public boolean isSoumisImpotSource() {
        return soumisImpotSource;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param b
     */
    public void setAllocationExploitation(boolean b) {
        allocationExploitation = b;
    }

    /**
     * setter pour l'attribut allocation maximum
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setAllocationMaximum(boolean b) {
        allocationMaximum = b;
    }

    /**
     * setter pour l'attribut date debut
     * 
     * @param date
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebut(JADate date) {
        dateDebut = date;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param date
     */
    public void setDateFin(JADate date) {
        dateFin = date;
    }

    /**
     * setter pour l'attribut canton imposition
     * 
     * @param cantonImposition
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTauxImposition(String cantonImposition) {
        idTauxImposition = cantonImposition;
    }

    /**
     * setter pour l'attribut independant
     * 
     * @param independant
     *            une nouvelle valeur pour cet attribut
     */
    public void setIndependant(boolean independant) {
        this.independant = independant;
    }

    /**
     * setter pour l'attribut nombre enfants
     * 
     * @param i
     *            une nouvelle valeur pour cet attribut
     */
    public void setNombreEnfants(int i) {
        nombreEnfants = i;
    }

    /**
     * setter pour l'attribut nombre jours soldes
     * 
     * @param i
     *            une nouvelle valeur pour cet attribut
     */
    public void setNombreJoursSoldes(int i) {
        nombreJoursSoldes = i;
    }

    /**
     * setter pour l'attribut no revision
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoRevision(String string) {
        noRevision = string;
    }

    /**
     * setter pour l'attribut salarie
     * 
     * @param salarie
     *            une nouvelle valeur pour cet attribut
     */
    public void setSalarie(boolean salarie) {
        this.salarie = salarie;
    }

    /**
     * setter pour l'attribut soumis impot source
     * 
     * @param soumisImpotSource
     *            une nouvelle valeur pour cet attribut
     */
    public void setSoumisImpotSource(boolean soumisImpotSource) {
        this.soumisImpotSource = soumisImpotSource;
    }

    /**
     * setter pour l'attribut taux imposition
     * 
     * @param tauxImposition
     *            une nouvelle valeur pour cet attribut
     */
    public void setTauxImposition(String tauxImposition) {
        this.tauxImposition = tauxImposition;
    }

    /**
     * setter pour l'attribut type allocation
     * 
     * @param s
     *            une nouvelle valeur pour cet attribut
     */
    public void setTypeAllocation(String s) {
        typeAllocation = s;
    }
}
