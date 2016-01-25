package ch.globaz.orion.business.domaine.pucs;

import ch.globaz.common.domaine.Date;

/**
 * @author dma
 * 
 */
public class Employee {
    private String nss;
    private String nom;
    private String prenom;
    private String workPlaceCanton;
    private String sexe;
    private Date dateNaissance;
    private SalariesAvs salariesAvs = new SalariesAvs();
    private SalariesCaf salariesCaf = new SalariesCaf();

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getWorkPlaceCanton() {
        return workPlaceCanton;
    }

    public void setWorkPlaceCanton(String workPlaceCanton) {
        this.workPlaceCanton = workPlaceCanton;
    }

    public SalariesAvs getSalariesAvs() {
        return salariesAvs;
    }

    public void setSalariesAvs(SalariesAvs salariesAvs) {
        this.salariesAvs = salariesAvs;
    }

    public SalariesCaf getSalariesCaf() {
        return salariesCaf;
    }

    public void setSalariesCaf(SalariesCaf salariesCaf) {
        this.salariesCaf = salariesCaf;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public SalaryCaf resolveAf(SalaryAvs salaryAvs) {
        for (SalaryCaf caf : salariesCaf) {
            if (salaryAvs.getPeriode().equals(caf.getPeriode())) {
                return caf;
            }
        }
        return new SalaryCaf();
    }
}
