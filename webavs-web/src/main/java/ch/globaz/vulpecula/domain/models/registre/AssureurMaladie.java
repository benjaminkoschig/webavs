package ch.globaz.vulpecula.domain.models.registre;

/**
 * Un assureur maladie est li�e � une cotisation du poste de travail.
 * 
 */
public class AssureurMaladie {
    private String id;
    private String designation;

    public AssureurMaladie() {
    }

    /**
     * Retourne l'id de l'assureur maladie
     * 
     * @return String repr�sentant l'id
     */
    public String getId() {
        return id;
    }

    /**
     * Mise � jour de l'id de l'assureur maladie
     * 
     * @param id Nouvel id
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Retourne la d�signation
     * 
     * @return String repr�sentant la d�signation
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Mise � jour de la d�signation
     * 
     * @param designation Nouvelle d�signation
     */
    public void setDesignation(final String designation) {
        this.designation = designation;
    }

}
