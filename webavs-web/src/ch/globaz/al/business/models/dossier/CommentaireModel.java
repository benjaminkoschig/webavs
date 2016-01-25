package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Classe mod�le d'un commentaire pouvant �tre li� � un dossier.
 * 
 * @author jts
 * 
 */
public class CommentaireModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * identifiant du commentaire
     */
    private String idCommentaire = null;
    /**
     * identifiant du dossier li� au commentaire
     */
    private String idDossier = null;
    /**
     * texte relatif au commentaire
     */
    private String texte = "";
    /**
     * type de commentaire
     */
    private String type = null;

    /**
     * Retourne l'id du commentaire
     */
    @Override
    public String getId() {
        return idCommentaire;
    }

    /**
     * @return the idCommentaire
     */
    public String getIdCommentaire() {
        return idCommentaire;
    }

    /**
     * Retourne l'id du dossier auquel le commentaire appartient
     * 
     * @return the idDossier
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * Retourne le texte du commentaire
     * 
     * @return the texte
     */
    public String getTexte() {
        return texte.trim();
    }

    /**
     * Retourne le type de commentaire
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * D�finit l'id du commentaire
     */
    @Override
    public void setId(String id) {
        idCommentaire = id;
    }

    /**
     * @param idCommentaire
     *            the idCommentaire to set
     */
    public void setIdCommentaire(String idCommentaire) {
        this.idCommentaire = idCommentaire;
    }

    /**
     * D�finit l'id du dossier auquel le commentaire appartient
     * 
     * @param idDossier
     *            the idDossier to set
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * D�finit le texte du commentaire
     * 
     * @param texte
     *            the texte to set
     */
    public void setTexte(String texte) {
        this.texte = texte;
    }

    /**
     * D�finit le type de commentaire
     * 
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

}
