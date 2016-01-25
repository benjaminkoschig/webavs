package globaz.hercule.db.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.hercule.service.CEAffiliationService;
import globaz.hercule.service.CEAttributionPtsService;
import globaz.hercule.service.CEControleEmployeurService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.services.controleemployeur.CACompteAnnexeService;

public class CEControleEmployeurViewBean extends CEControleEmployeur implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de CEControleEmployeurViewBean
     */
    public CEControleEmployeurViewBean() {
        super();
    }

    public String _getBrancheEcoLibelle() {
        if (!JadeStringUtil.isEmpty(getBrancheEco())) {
            return getSession().getCodeLibelle(getBrancheEco());
        }

        return "";
    }

    public String _getFormeJuriLibelle() {
        if (!JadeStringUtil.isEmpty(getFormeJuri())) {
            return getSession().getCodeLibelle(getFormeJuri());
        }

        return "";
    }

    /**
     * Retourne l'id attribution relatif a ce controle
     * 
     * @return
     */
    public String _getIdAttributionPts() {

        String id = null;

        try {
            id = CEAttributionPtsService.findIdAttributionPtsActifForControle(getSession(), getNumAffilie(),
                    getDateDebutControle(), getDateFinControle());
        } catch (Exception e) {
            id = null;
        }

        return id;
    }

    /**
     * Cherche l'id du compte annexe de l'affilie pour un lien direct depuis l'ecran du controle.
     * 
     * @return
     */
    public String _getIdCompteAnnexe() {
        String idCompteAnnexe = null;

        try {
            idCompteAnnexe = CACompteAnnexeService.getIdCompteAnnexeByRole(getSession(),
                    CEAffiliationService.getRoleForAffilieParitaire(getSession()), getNumAffilie());
        } catch (Exception e) {
            idCompteAnnexe = null;
        }

        return idCompteAnnexe;
    }

    /**
     * Permet de retrouver l'id du groupe de l'affilié, si groupe il y a.
     * 
     * @return
     */
    public String _getIdGroupe() {

        String idGroupe = null;

        try {
            idGroupe = CEControleEmployeurService.findIdGroupeForIdAffilie(getSession(), null, getAffiliationId());
        } catch (Exception e) {
            idGroupe = null;
        }

        return idGroupe;
    }
}
