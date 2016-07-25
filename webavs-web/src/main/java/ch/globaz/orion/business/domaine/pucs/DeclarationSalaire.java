package ch.globaz.orion.business.domaine.pucs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;

public class DeclarationSalaire {
    private String numeroAffilie;
    private int nbSalaire = 0;
    private int annee = 0;
    private String nom;
    private String numeroIde;
    private Contact contact;
    private Adresse adresse;
    private Montant montantAvs = Montant.ZERO;
    private Montant montantAc1 = Montant.ZERO;
    private Montant montantAc2 = Montant.ZERO;
    private Map<String, Montant> montantCaf = new HashMap<String, Montant>();
    private DeclarationSalaireProvenance provenance;
    private Date transmissionDate;
    private List<Employee> employees = new ArrayList<Employee>();
    private boolean isAfSeul;
    private boolean test; // premet de savoir si la d�claration de salaire est pour le test(Utilis� pour les
                          // swissDec)
    private boolean duplicate = false;
    private boolean substitution = false;

    public boolean isAfSeul() {
        return isAfSeul;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public void setAfSeul(boolean isAfSeul) {
        this.isAfSeul = isAfSeul;
    }

    public DeclarationSalaireProvenance getProvenance() {
        return provenance;
    }

    public void setProvenance(DeclarationSalaireProvenance provenance) {
        this.provenance = provenance;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Montant getMontantAvs() {
        return montantAvs;
    }

    public void setMontantAvs(Montant montantAvs) {
        this.montantAvs = montantAvs;
    }

    public Montant getMontantAc1() {
        return montantAc1;
    }

    public void setMontantAc1(Montant montantAc1) {
        this.montantAc1 = montantAc1;
    }

    public Montant getMontantAc2() {
        return montantAc2;
    }

    public void setMontantAc2(Montant montantAc2) {
        this.montantAc2 = montantAc2;
    }

    public Montant getMontantCaf(String canton) {
        return montantCaf.get(canton);
    }

    public void setMontantCaf(String canton, Montant montantCaf) {
        this.montantCaf.put(canton, montantCaf);
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public int getNbSalaire() {
        return nbSalaire;
    }

    public void setNbSalaire(int nbSalaire) {
        this.nbSalaire = nbSalaire;
    }

    public Date getTransmissionDate() {
        return transmissionDate;
    }

    public void setTransmissionDate(Date transmissionDate) {
        this.transmissionDate = transmissionDate;
    }

    public String getAdresseCity() {
        return adresse.getCity();
    }

    public String getAdresseStreet() {
        return adresse.getStreet();
    }

    public String getAdresseZipCode() {
        return adresse.getZipCode();
    }

    public String getContactName() {
        return contact.getName();
    }

    public String getContactPhone() {
        return contact.getPhone();
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public String getContactMail() {
        return contact.getMail();
    }

    public String getNumeroIde() {
        return numeroIde;
    }

    public void setNumeroIde(String numeroIde) {
        this.numeroIde = numeroIde;
    }

    public boolean isDuplicate() {
        return duplicate;
    }

    public void setDuplicate(boolean duplicate) {
        this.duplicate = duplicate;
    }

    public boolean isSubstitution() {
        return substitution;
    }

    public void setSubstitution(boolean substitution) {
        this.substitution = substitution;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    /**
     * @return
     */
    public Set<String> resolveDistinctContant() {
        Set<String> cantons = new HashSet<String>();
        for (Employee employee : employees) {
            for (SalaryCaf caf : employee.getSalariesCaf()) {
                if (caf.getCanton() != null) {
                    cantons.add(caf.getCanton());
                }
            }
            if (employee.getWorkPlaceCanton() != null) {
                cantons.add(employee.getWorkPlaceCanton());
            }
        }
        return cantons;
    }
}
