/**
 * Ins�rez la description du type ici. Date de cr�ation : (31.05.2002 09:49:17)
 * 
 * @author: Administrator
 */
package globaz.naos.db.plan;

import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pyxis.db.tiers.TITiers;

/**
 * Cette classe est cr�es pour facilit� le travail du FWDispatcher.
 * 
 * Elle etend AFPlanViewBean, et est utilis�e par l'�cran planSelect_rc.jsp.
 * 
 * @author sau
 */
public class AFPlanSelectViewBean extends AFPlanViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private AFAffiliation _affiliation = null;

    private TITiers _tiers = null;
    private String affiliationId = "";

    /**
     * Indique si l'impl�mentation des h�ritages est g�r�e automatiquement.
     * 
     * @see globaz.globall.db.BEntity#_autoInherits()
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * Rechercher l'affiliation du Control Employeur en fonction de son ID.
     * 
     * @return l'affiliation
     */
    public AFAffiliation getAffiliation() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(affiliationId)) {
            return null;
        }

        if (_affiliation == null) {

            _affiliation = new AFAffiliation();
            _affiliation.setSession(getSession());
            _affiliation.setAffiliationId(affiliationId);
            try {
                _affiliation.retrieve();
                /*
                 * if (_affiliation.hasErrors()) _affiliation = null;
                 */
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _affiliation = null;
            }
        }
        return _affiliation;
    }

    public String getAffiliationId() {
        return affiliationId;
    }

    /**
     * Rechercher le tiers de l'Avis de Mutation en fonction de son ID.
     * 
     * @return le tiers
     */
    public TITiers getTiers() {

        // Si pas d'identifiant => pas d'objet
        if (_tiers == null) {
            if (_affiliation == null) {
                getAffiliation();
                if (_affiliation == null) {
                    return null;
                }
            }
            _tiers = new TITiers();
            _tiers.setSession(getSession());
            _tiers.setIdTiers(_affiliation.getIdTiers());
            try {
                _tiers.retrieve();
                /*
                 * if (_tiers.getSession().hasErrors()) _tiers = null;
                 */
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _tiers = null;
            }
        }
        return _tiers;
    }

    public void setAffiliationId(String string) {
        affiliationId = string;
    }

}
