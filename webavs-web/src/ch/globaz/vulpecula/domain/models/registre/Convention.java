/**
 * 
 */
package ch.globaz.vulpecula.domain.models.registre;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;

/**
 * Un affili� peut �tre rattach� � une convention
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 18 d�c. 2013
 * 
 */
public class Convention implements DomainEntity, Comparable<Convention> {
    // FIXME: Ajouter une propri�t� dans le fichier de param�trage d�finissant les cotisations � ignorer.
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
     * Retourne la d�signation de la convention
     * 
     * @return String repr�sentant le nom de la convention
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Mise � jour du code
     * 
     * @param code
     *            String repr�sentant le nouveau code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Mise � jour de la d�sigantion de la convention
     * 
     * @param designation
     *            String repr�sentant le nouveau nom de la convention
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
     * Mise � jour des qualifications possibles de la conventions
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
