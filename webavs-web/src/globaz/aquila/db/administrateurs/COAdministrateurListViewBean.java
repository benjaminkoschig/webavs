/*
 * Cr�� le 22 f�vr. 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
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

        // v�rifie si le num�ro contient un "-00" et le remplace par "-0"
        // (utilis� pour la requ�te).

        if (!JadeStringUtil.isEmpty(getLikeIdExterneRole())) {
            if (application.numeroAdministrateurReplaceTiret()
                    && JadeStringUtil.contains(getLikeIdExterneRole(), "-00")) {
                setLikeIdExterneRole(JadeStringUtil.change(getLikeIdExterneRole(), "-00", "-0"));
            } else {
                setLikeIdExterneRole(getLikeIdExterneRole());
            }
        }

        // ajout le nom comme crit�re de recherche.
        if (!JadeStringUtil.isEmpty(getLikeNom())) {
            setLikeNumNom(getLikeNom());
            setForIdRole(IntRole.ROLE_ADMINISTRATEUR);
        }

        // ne pas supprimer ! obligatoire m�me si le param�tre est vide.
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
