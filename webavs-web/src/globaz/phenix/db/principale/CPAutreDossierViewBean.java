package globaz.phenix.db.principale;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BTransaction;
import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.application.TIApplication;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPAutreDossierViewBean extends CPDecision implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String action;
    private java.lang.String codeDirection = "";
    private String idAffiliation = "";
    private String idTiers = "";
    private java.lang.String numAffilie = "";

    /**
     * contr�le le num�ro d'affili� saisi et stocke le nombre d'affiliation du tiers trouv� (utilis� par la suite pour
     * le direction des �crans)
     */
    public void _controle() throws Exception {
        BTransaction transaction = null;
        try {
            String[] typeAffiliation = new String[5];
            typeAffiliation[0] = CodeSystem.TYPE_AFFILI_INDEP_EMPLOY;
            typeAffiliation[1] = CodeSystem.TYPE_AFFILI_INDEP;
            typeAffiliation[2] = CodeSystem.TYPE_AFFILI_NON_ACTIF;
            typeAffiliation[3] = CodeSystem.TYPE_AFFILI_TSE;
            typeAffiliation[4] = CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE;
            transaction = new BTransaction(getSession());
            // Creer une transaction
            transaction.openTransaction();
            if (!JadeStringUtil.isEmpty(getNumAffilie())) {
                // formatage du numero selon caisse
                TIApplication app = (TIApplication) GlobazSystem.getApplication("PYXIS");
                IFormatData affilieFormater = app.getAffileFormater();
                if (affilieFormater != null) {
                    setNumAffilie(affilieFormater.format(getNumAffilie()));
                }
                // Recherche des affiliation pour ce num�ro
                AFAffiliationManager affiManager = new AFAffiliationManager();
                affiManager.setSession(getSession());
                affiManager.setForAffilieNumero(getNumAffilie());
                affiManager.setForTypeAffiliation(typeAffiliation);
                if (!JadeStringUtil.isEmpty(getIdTiers())) {
                    // l'id tiers est r�cup�r� seulement quand on utilise la
                    // PopupList
                    affiManager.setForIdTiers(getIdTiers());
                }
                affiManager.find();
                if (affiManager.size() == 0) {
                    _addError(transaction, getSession().getLabel("CP_MSG_0033"));
                } else {
                    if (affiManager.size() == 1) {
                        // On ne fait pas de recherche sur des employeuers
                        if (Boolean.TRUE.equals(((AFAffiliation) affiManager.getEntity(0)).isTraitement())) {
                            _addError(transaction, getSession().getLabel("CP_MSG_0036"));
                        } else {
                            setIdTiers(((AFAffiliation) affiManager.getEntity(0)).getIdTiers());
                            setIdAffiliation(((AFAffiliation) affiManager.getEntity(0)).getAffiliationId());
                            setCodeDirection("1");
                            // Permet de se diriger vers l'�cran de gestion des
                            // d�cisions du tiers
                        }
                    } else {
                        // Si l'id tiers n'a pas �t� renseign�, il se peut qu'un
                        // m�me num�ro d'affili� soit attribu� � 2 tiers
                        // diff�rents
                        if (JadeStringUtil.isEmpty(getIdTiers())) {
                            // Enregistrement du premier id tiers trouv� dans
                            // une variable d'aide
                            String premierIdTiers = ((AFAffiliation) affiManager.getFirstEntity()).getIdTiers();
                            boolean trouveAutreIdTiers = false;
                            int i = 0;
                            while (i < affiManager.size() && !trouveAutreIdTiers) {
                                AFAffiliation affiliation = (AFAffiliation) affiManager.getEntity(i);
                                if (!affiliation.getIdTiers().equals(premierIdTiers)) {
                                    trouveAutreIdTiers = true;
                                }
                                i++;
                            }
                            if (trouveAutreIdTiers) {
                                _addError(transaction, getSession().getLabel("CP_MSG_0037"));
                            } else {
                                setIdTiers(premierIdTiers);
                                setCodeDirection("2");
                                // Permet de se diriger vers l'�cran des
                                // affiliations du tiers
                            }
                        } else {
                            setCodeDirection("2");
                            // Permet de se diriger vers l'�cran des
                            // affiliations du tiers
                        }
                    }
                }

            }
            if (transaction.hasErrors()) {
                setMsgType(FWViewBeanInterface.ERROR);
                setMessage(transaction.getErrors().toString());
            } else if (transaction.hasWarnings()) {
                setMsgType(FWViewBeanInterface.WARNING);
                setMessage(transaction.getErrors().toString());
            } else {
                setMsgType(FWViewBeanInterface.OK);
                setMessage("");
            }
        } catch (Exception e) {
            _addError(transaction, e.toString());
            // ferme la connexion ouverte dans cette m�thode
        } finally {
            if (transaction != null) {
                try {
                    transaction.closeTransaction();
                } catch (Exception e) {
                    // Pas besoin de g�rer
                }
            }
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAction() {
        return (action);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (30.07.2003 13:59:46)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCodeDirection() {
        return codeDirection;
    }

    /**
     * Returns the idAffiliation.
     * 
     * @return String
     */
    @Override
    public String getIdAffiliation() {
        return idAffiliation;
    }

    /**
     * Returns the idTiers.
     * 
     * @return String
     */
    @Override
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (30.07.2003 13:23:12)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNumAffilie() {
        return numAffilie;
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (30.07.2003 13:59:46)
     * 
     * @param newCodeDirection
     *            java.lang.String
     */
    public void setCodeDirection(java.lang.String newCodeDirection) {
        codeDirection = newCodeDirection;
    }

    /**
     * Sets the idAffiliation.
     * 
     * @param idAffiliation
     *            The idAffiliation to set
     */
    @Override
    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    /**
     * Sets the idTiers.
     * 
     * @param idTiers
     *            The idTiers to set
     */
    @Override
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (30.07.2003 13:23:12)
     * 
     * @param newNumAffilie
     *            java.lang.String
     */
    public void setNumAffilie(java.lang.String newNumAffilie) {
        numAffilie = newNumAffilie;
    }

}
