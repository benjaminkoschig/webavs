package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Classe de recherche de commnetaires
 * 
 * @author PTA
 * 
 */
public class CommentaireSearchModel extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Recherche de commentaires par l'identifiant du dossier
     */
    private String forIdDossier = null;

    /**
     * Recherche de commentaires par le texte
     */
    private String forIdTexte = null;
    /**
     * Rechehrche sur le type de commentaire
     */
    private String forTypeCommentaire = null;

    /**
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * @return the forIdTexte
     */
    public String getForIdTexte() {
        return forIdTexte;
    }

    /**
     * @return the forTypeCommentaire
     */
    public String getForTypeCommentaire() {
        return forTypeCommentaire;
    }

    /**
     * @param forIdDossier
     *            the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    /**
     * @param forIdTexte
     *            the forIdTexte to set
     */
    public void setForIdTexte(String forIdTexte) {
        this.forIdTexte = forIdTexte;
    }

    /**
     * @param forTypeCommentaire
     *            the forTypeCommentaire to set
     */
    public void setForTypeCommentaire(String forTypeCommentaire) {
        this.forTypeCommentaire = forTypeCommentaire;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<CommentaireModel> whichModelClass() {
        return CommentaireModel.class;
    }

}
