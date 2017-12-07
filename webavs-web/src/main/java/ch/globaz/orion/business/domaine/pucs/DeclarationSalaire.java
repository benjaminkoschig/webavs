package ch.globaz.orion.business.domaine.pucs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.swissdec.schema.sd._20130514.salarydeclaration.CantonAndEXType;

public class DeclarationSalaire {
    private static final Logger LOG = LoggerFactory.getLogger(DeclarationSalaire.class);

    private String numeroAffilie;
    private Integer nbSalaire;
    private Integer annee;
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
    private boolean test; // premet de savoir si la déclaration de salaire est pour le test(Utilisé pour les
                          // swissDec)
    private boolean duplicate;
    private boolean substitution;

    private List<String> montantAVSDuplicate = new ArrayList<String>();
    private List<String> montantAVSDiff = new ArrayList<String>();
    private Map<CantonAndEXType, List<String>> montantAFDuplicate = new EnumMap<CantonAndEXType, List<String>>(
            CantonAndEXType.class);
    private Map<CantonAndEXType, List<String>> montantAFDiff = new EnumMap<CantonAndEXType, List<String>>(
            CantonAndEXType.class);

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

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
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

    // produit la somme de tous les cantons
    public Montant getMontantCaf() {
        BigDecimal montant = BigDecimal.valueOf(0);

        for (Montant m : montantCaf.values()) {
            montant = montant.add(m.getBigDecimalValue());
        }

        return Montant.valueOf(montant);
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

    public Integer getNbSalaire() {
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

    public List<String> getMontantAVSDuplicate() {
        return montantAVSDuplicate;
    }

    public void setMontantAVSDuplicate(List<String> montantAVSDuplicate) {
        this.montantAVSDuplicate = montantAVSDuplicate;
    }

    public List<String> getMontantAVSDiff() {
        return montantAVSDiff;
    }

    public void setMontantAVSDiff(List<String> montantAVSDiff) {
        this.montantAVSDiff = montantAVSDiff;
    }

    public Map<CantonAndEXType, List<String>> getMontantAFDuplicate() {
        return montantAFDuplicate;
    }

    public void setMontantAFDuplicate(Map<CantonAndEXType, List<String>> montantAFDuplicate) {
        this.montantAFDuplicate = montantAFDuplicate;
    }

    public Map<CantonAndEXType, List<String>> getMontantAFDiff() {
        return montantAFDiff;
    }

    public void setMontantAFDiff(Map<CantonAndEXType, List<String>> montantAFDiff) {
        this.montantAFDiff = montantAFDiff;
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
