/*
 * Créé le 21 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.db.administrateurs;

import globaz.aquila.vb.COAbstractViewBeanSupport;

/**
 * @author dvh
 */
public class CORechercheCompteAnnexeViewBean extends COAbstractViewBeanSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idExterneRoleChoisi = "";
    private String likeIdExterneRole = null;
    private String nomAffilie = null;

    /**
	 *
	 */
    public CORechercheCompteAnnexeViewBean() {
        super();
    }

    public String getIdExterneRoleChoisi() {
        return idExterneRoleChoisi;
    }

    public String getLikeIdExterneRole() {
        if (likeIdExterneRole != null) {
            return likeIdExterneRole;
        } else {
            return "";
        }
    }

    public String getNomAffilie() {
        if (nomAffilie != null) {
            return nomAffilie;
        } else {
            return "";
        }
    }

    public void setIdExterneRoleChoisi(String string) {
        idExterneRoleChoisi = string;
    }

    public void setLikeIdExterneRole(String string) {
        likeIdExterneRole = string;
    }

    public void setNomAffilie(String string) {
        nomAffilie = string;
    }

}
