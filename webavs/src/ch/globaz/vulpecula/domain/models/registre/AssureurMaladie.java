package ch.globaz.vulpecula.domain.models.registre;

/**
 * Un assureur maladie est liée à une cotisation du poste de travail.
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
     * @return String représentant l'id
     */
    public String getId() {
        return id;
    }

    /**
     * Mise à jour de l'id de l'assureur maladie
     * 
     * @param id Nouvel id
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Retourne la désignation
     * 
     * @return String représentant la désignation
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Mise à jour de la désignation
     * 
     * @param designation Nouvelle désignation
     */
    public void setDesignation(final String designation) {
        this.designation = designation;
    }

}
