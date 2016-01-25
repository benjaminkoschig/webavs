/*
 * Créé le 24 juillet 2009
 */
package globaz.libra.vb.dossiers;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeStringUtil;
import globaz.libra.db.domaines.LIDomaines;
import globaz.libra.db.dossiers.LIDossiersJointTiersManager;
import globaz.libra.db.groupes.LIGroupes;
import globaz.libra.db.utilisateurs.LIUtilisateurs;

/**
 * 
 * @author HPE
 * 
 */
public class LIDossiersJointTiersListViewBean extends LIDossiersJointTiersManager implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new LIDossiersJointTiersViewBean();
    }

    public String getLibelleDomaine() throws Exception {

        if (getForIdDomaine().startsWith("IN")) {
            return getSession().getLabel("ECRAN_TOUS");
        } else {
            LIDomaines domaine = new LIDomaines();
            domaine.setSession(getSession());
            domaine.setIdDomaine(getForIdDomaine());
            domaine.retrieve();

            return getSession().getCodeLibelle(domaine.getCsDomaine());
        }

    }

    public String getLibelleEtat() throws Exception {

        return getSession().getCodeLibelle(getForCsEtat());

    }

    public String getLibelleGroupe() throws Exception {

        if (JadeStringUtil.isBlankOrZero(getForIdGroupe())) {
            return getSession().getLabel("ECRAN_TOUS");
        } else {
            LIGroupes groupe = new LIGroupes();
            groupe.setSession(getSession());
            groupe.setIdGroupe(getForIdGroupe());
            groupe.retrieve();

            return groupe.getLibelleGroupe();
        }
    }

    public String getLibelleUser() throws Exception {

        if (getForIdUtilisateur().equals("idFX")) {
            return getSession().getUserId();
        } else {
            LIUtilisateurs utilisateur = new LIUtilisateurs();
            utilisateur.setSession(getSession());
            utilisateur.setIdUtilisateur(getForIdUtilisateur());
            utilisateur.retrieve();

            return utilisateur.getIdUtilisateurExterne();
        }
    }

}
