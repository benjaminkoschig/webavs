/*
 * Créé le 22 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.db.administrateurs;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.application.COApplication;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.external.IntRole;

/**
 * @author dvh
 */
public class COAdministrateurListViewBean extends CACompteAnnexeManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idExterneRoleChoisi = "";
    private String likeNom = "";
    private String nomAffilie = "";

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        COApplication application = (COApplication) GlobazSystem
                .getApplication(ICOApplication.DEFAULT_APPLICATION_AQUILA);
        COAdministrateurViewBean administrateurViewBean = new COAdministrateurViewBean();

        // vérifie si le numéro contient un "-00" et le remplace par "-0"
        // (utilisé pour la requête).

        if (!JadeStringUtil.isEmpty(getLikeIdExterneRole())) {
            if (application.numeroAdministrateurReplaceTiret()
                    && JadeStringUtil.contains(getLikeIdExterneRole(), "-00")) {
                setLikeIdExterneRole(JadeStringUtil.change(getLikeIdExterneRole(), "-00", "-0"));
            } else {
                setLikeIdExterneRole(getLikeIdExterneRole());
            }
        }

        // ajout le nom comme critère de recherche.
        if (!JadeStringUtil.isEmpty(getLikeNom())) {
            setLikeNumNom(getLikeNom());
            setForIdRole(IntRole.ROLE_ADMINISTRATEUR);
        }

        // ne pas supprimer ! obligatoire même si le paramètre est vide.
        administrateurViewBean.setForIdExterneLike("");

        return administrateurViewBean;
    }

    public String getIdExterneRoleChoisi() {
        return idExterneRoleChoisi;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public String getNomAffilie() {
        return nomAffilie;
    }

    public void setIdExterneRoleChoisi(String string) {
        idExterneRoleChoisi = string;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public void setNomAffilie(String string) {
        nomAffilie = string;
    }

}
