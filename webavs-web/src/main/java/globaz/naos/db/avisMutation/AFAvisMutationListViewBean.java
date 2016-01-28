package globaz.naos.db.avisMutation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Vector;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class AFAvisMutationListViewBean extends AFAvisMutationManager implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private globaz.naos.db.affiliation.AFAffiliation _affiliation;
    private globaz.pyxis.db.tiers.TITiers _tiers;
    private String action;
    private Vector affiliationId = new Vector();
    private Vector avisMutationId = new Vector();
    private Vector caisseEmetteur = new Vector();
    private Vector caisseReception = new Vector();

    private Vector dateAnnonce = new Vector();
    private Vector dateEnvoiAccuseReception = new Vector();
    private Vector dateOpposition = new Vector();
    private Vector dateReception = new Vector();
    private Vector dateReceptionAccuseReception = new Vector();
    private Vector droitOption = new Vector();
    private Vector genreAnnonce = new Vector();
    private String message;
    private Vector motifOpposition = new Vector();
    private String msgType;
    private String reqFind;
    private String reqLibelle;
    private Vector tiersId = new Vector();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAction() {
        return (action);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 15:03:11)
     * 
     * @return globaz.osiris.db.comptes.CAJournal
     */
    public globaz.naos.db.affiliation.AFAffiliation getAffiliation(int pos) {

        // Si pas d'identifiant d'objet
        if (JadeStringUtil.isIntegerEmpty(getAffiliationId(pos))) {
            return null;
        }

        // Si log pas d�j� charg�
        // if (_affiliation == null) {
        // Instancier un nouveau LOG
        _affiliation = new globaz.naos.db.affiliation.AFAffiliation();
        _affiliation.setIdTiers(getTiersId(pos));
        _affiliation.setSession(getSession());

        // R�cup�rer le log en question
        _affiliation.setAffiliationId(getAffiliationId(pos));

        try {
            _affiliation.retrieve();
            if (_affiliation.hasErrors()) {
                _affiliation = null;
            }
        } catch (Exception e) {
            _addError(null, e.getMessage());
            _affiliation = null;
            // }

        }

        return _affiliation;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAffiliationId(int index) {
        return ((AFAvisMutation) getEntity(index)).getAffiliationId();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAvisMutationId(int index) {
        return ((AFAvisMutation) getEntity(index)).getAvisMutationId();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getCaisseEmetteur(int index) {
        return ((AFAvisMutation) getEntity(index)).getCaisseEmetteur();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getCaisseReception(int index) {
        return ((AFAvisMutation) getEntity(index)).getCaisseReception();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getDateAnnonce(int index) {
        return ((AFAvisMutation) getEntity(index)).getDateAnnonce();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getDateEnvoiAccuseReception(int index) {
        return ((AFAvisMutation) getEntity(index)).getDateEnvoiAccuseReception();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getDateOpposition(int index) {
        return ((AFAvisMutation) getEntity(index)).getDateOpposition();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getDateReception(int index) {
        return ((AFAvisMutation) getEntity(index)).getDateReception();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getDateReceptionAccuseReception(int index) {
        return ((AFAvisMutation) getEntity(index)).getDateReceptionAccuseReception();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public Boolean getDroitOption(int index) {
        return ((AFAvisMutation) getEntity(index)).getDroitOption();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getGenreAnnonce(int index) {
        return ((AFAvisMutation) getEntity(index)).getGenreAnnonce();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getMotifOpposition(int index) {
        return ((AFAvisMutation) getEntity(index)).getMotifOpposition();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getReqFind() {
        return (reqFind);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getReqLibelle() {
        return (reqLibelle);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 15:03:11)
     * 
     * @return globaz.osiris.db.comptes.CAJournal
     */
    public globaz.pyxis.db.tiers.TITiers getTiers(int pos) {

        // Si si pas d'identifiant, pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getTiersId(pos))) {
            return null;
        }

        // Si log pas d�j� charg�
        // if (_tiers == null) {
        // Instancier un nouveau LOG
        _tiers = new globaz.pyxis.db.tiers.TITiers();
        _tiers.setSession(getSession());

        // R�cup�rer le log en question
        _tiers.setIdTiers(getTiersId(pos));
        try {
            _tiers.retrieve();
            // if (_tiers.hasErrors())
            // _tiers = null;
        } catch (Exception e) {
            _addError(null, e.getMessage());
            _tiers = null;
        }
        // }

        return _tiers;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getTiersId(int index) {
        return ((AFAvisMutation) getEntity(index)).getTiersId();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:20:01)
     * 
     * @param action
     *            java.lang.String
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setAffiliationId(String affiliationId) {
        this.affiliationId.addElement(affiliationId);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setAvisMutationId(String avisMutationId) {
        this.avisMutationId.addElement(avisMutationId);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setCaisseEmetteur(String caisseEmetteur) {
        this.caisseEmetteur.addElement(caisseEmetteur);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setCaisseReception(String caisseReception) {
        this.caisseReception.addElement(caisseReception);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setDateAnnonce(String dateAnnonce) {
        this.dateAnnonce.addElement(dateAnnonce);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setDateEnvoiAccuseReception(String dateEnvoiAccuseReception) {
        this.dateEnvoiAccuseReception.addElement(dateEnvoiAccuseReception);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setDateOpposition(String dateOpposition) {
        this.dateOpposition.addElement(dateOpposition);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setDateReception(String dateReception) {
        this.dateReception.addElement(dateReception);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setDateReceptionAccuseReception(String dateReceptionAccuseReception) {
        this.dateReceptionAccuseReception.addElement(dateReceptionAccuseReception);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setDroitOption(Boolean droitOption) {
        this.droitOption.addElement(droitOption);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setGenreAnnonce(String genreAnnonce) {
        this.genreAnnonce.addElement(genreAnnonce);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setMotifOpposition(String motifOpposition) {
        this.motifOpposition.addElement(motifOpposition);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:20:01)
     * 
     * @param action
     *            java.lang.String
     */
    public void setReqFind(String reqFind) {
        this.reqFind = reqFind;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:20:01)
     * 
     * @param action
     *            java.lang.String
     */
    public void setReqLibelle(String reqLibelle) {
        this.reqLibelle = reqLibelle;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.05.2002 09:33:53)
     * 
     * @param message
     *            java.lang.String
     */

    public void setTiersId(String tiersId) {
        this.tiersId.addElement(tiersId);
    }
}
