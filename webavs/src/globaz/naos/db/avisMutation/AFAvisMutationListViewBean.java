package globaz.naos.db.avisMutation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Vector;

/**
 * Insérez la description du type ici. Date de création : (10.05.2002 09:35:05)
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
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAction() {
        return (action);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.02.2002 15:03:11)
     * 
     * @return globaz.osiris.db.comptes.CAJournal
     */
    public globaz.naos.db.affiliation.AFAffiliation getAffiliation(int pos) {

        // Si pas d'identifiant d'objet
        if (JadeStringUtil.isIntegerEmpty(getAffiliationId(pos))) {
            return null;
        }

        // Si log pas déjà chargé
        // if (_affiliation == null) {
        // Instancier un nouveau LOG
        _affiliation = new globaz.naos.db.affiliation.AFAffiliation();
        _affiliation.setIdTiers(getTiersId(pos));
        _affiliation.setSession(getSession());

        // Récupérer le log en question
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
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAffiliationId(int index) {
        return ((AFAvisMutation) getEntity(index)).getAffiliationId();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAvisMutationId(int index) {
        return ((AFAvisMutation) getEntity(index)).getAvisMutationId();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getCaisseEmetteur(int index) {
        return ((AFAvisMutation) getEntity(index)).getCaisseEmetteur();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getCaisseReception(int index) {
        return ((AFAvisMutation) getEntity(index)).getCaisseReception();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getDateAnnonce(int index) {
        return ((AFAvisMutation) getEntity(index)).getDateAnnonce();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getDateEnvoiAccuseReception(int index) {
        return ((AFAvisMutation) getEntity(index)).getDateEnvoiAccuseReception();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getDateOpposition(int index) {
        return ((AFAvisMutation) getEntity(index)).getDateOpposition();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getDateReception(int index) {
        return ((AFAvisMutation) getEntity(index)).getDateReception();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getDateReceptionAccuseReception(int index) {
        return ((AFAvisMutation) getEntity(index)).getDateReceptionAccuseReception();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public Boolean getDroitOption(int index) {
        return ((AFAvisMutation) getEntity(index)).getDroitOption();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getGenreAnnonce(int index) {
        return ((AFAvisMutation) getEntity(index)).getGenreAnnonce();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getMotifOpposition(int index) {
        return ((AFAvisMutation) getEntity(index)).getMotifOpposition();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getReqFind() {
        return (reqFind);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getReqLibelle() {
        return (reqLibelle);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.02.2002 15:03:11)
     * 
     * @return globaz.osiris.db.comptes.CAJournal
     */
    public globaz.pyxis.db.tiers.TITiers getTiers(int pos) {

        // Si si pas d'identifiant, pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getTiersId(pos))) {
            return null;
        }

        // Si log pas déjà chargé
        // if (_tiers == null) {
        // Instancier un nouveau LOG
        _tiers = new globaz.pyxis.db.tiers.TITiers();
        _tiers.setSession(getSession());

        // Récupérer le log en question
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
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getTiersId(int index) {
        return ((AFAvisMutation) getEntity(index)).getTiersId();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:20:01)
     * 
     * @param action
     *            java.lang.String
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setAffiliationId(String affiliationId) {
        this.affiliationId.addElement(affiliationId);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setAvisMutationId(String avisMutationId) {
        this.avisMutationId.addElement(avisMutationId);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setCaisseEmetteur(String caisseEmetteur) {
        this.caisseEmetteur.addElement(caisseEmetteur);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setCaisseReception(String caisseReception) {
        this.caisseReception.addElement(caisseReception);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setDateAnnonce(String dateAnnonce) {
        this.dateAnnonce.addElement(dateAnnonce);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setDateEnvoiAccuseReception(String dateEnvoiAccuseReception) {
        this.dateEnvoiAccuseReception.addElement(dateEnvoiAccuseReception);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setDateOpposition(String dateOpposition) {
        this.dateOpposition.addElement(dateOpposition);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setDateReception(String dateReception) {
        this.dateReception.addElement(dateReception);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setDateReceptionAccuseReception(String dateReceptionAccuseReception) {
        this.dateReceptionAccuseReception.addElement(dateReceptionAccuseReception);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setDroitOption(Boolean droitOption) {
        this.droitOption.addElement(droitOption);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setGenreAnnonce(String genreAnnonce) {
        this.genreAnnonce.addElement(genreAnnonce);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */

    public void setMotifOpposition(String motifOpposition) {
        this.motifOpposition.addElement(motifOpposition);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:20:01)
     * 
     * @param action
     *            java.lang.String
     */
    public void setReqFind(String reqFind) {
        this.reqFind = reqFind;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:20:01)
     * 
     * @param action
     *            java.lang.String
     */
    public void setReqLibelle(String reqLibelle) {
        this.reqLibelle = reqLibelle;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2002 09:33:53)
     * 
     * @param message
     *            java.lang.String
     */

    public void setTiersId(String tiersId) {
        this.tiersId.addElement(tiersId);
    }
}
