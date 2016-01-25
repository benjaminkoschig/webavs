/**
 * 
 */
package ch.globaz.vulpecula.domain.models.registre;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;

/**
 * Un affilié peut être rattaché à une convention
 * 
 * @author Arnaud Geiser (AGE) | Créé le 18 déc. 2013
 * 
 */
public class Convention implements DomainEntity, Comparable<Convention> {
    // FIXME: Ajouter une propriété dans le fichier de paramétrage définissant les cotisations à ignorer.
    public static final String ELECTRICITE = "05";

    private String id;
    private String code;
    private String designation;
    private String spy;
    private List<ConventionQualification> qualifications;

    public Convention() {
        qualifications = new ArrayList<ConventionQualification>();
    }

    /**
     * Retourne le code utilisateur de la convention
     * 
     * @return le code utilisateur de la convention
     */
    public String getCode() {
        return code;
    }

    /**
     * Retourne la désignation de la convention
     * 
     * @return String représentant le nom de la convention
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Mise à jour du code
     * 
     * @param code
     *            String représentant le nouveau code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Mise à jour de la désigantion de la convention
     * 
     * @param designation
     *            String représentant le nouveau nom de la convention
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /**
     * Retourne la liste des qualifications possibles de la convention
     * 
     * @return Liste de qualifications de la convention
     */
    public List<ConventionQualification> getQualifications() {
        return qualifications;
    }

    /**
     * Mise à jour des qualifications possibles de la conventions
     * 
     * @param qualifications
     *            Liste des qualifications
     */
    public void setQualifications(List<ConventionQualification> qualifications) {
        this.qualifications = qualifications;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    @Override
    public int compareTo(Convention o) {
        if (code == null && o.code == null) {
            return 0;
        }
        return code.compareTo(o.code);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Convention) {
            Convention other = (Convention) obj;
            return other.id.equals(id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public boolean isElectricite() {
        return ELECTRICITE.equals(code);
    }
}
