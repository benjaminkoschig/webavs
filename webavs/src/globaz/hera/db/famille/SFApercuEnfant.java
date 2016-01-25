/*
 * Créé le 9 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.db.famille;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.pyxis.api.ITIPersonne;
import java.util.Iterator;

/**
 * @author mmu Classe qui fait la jointure entre les enfants(SFENFANT) et les membres de Famille(SFMBRFAM) à noter que
 *         la date d'adoption fait partie de la classe SFENFANT L'enfant est identifé dans la teble Membre famille par
 *         un idMembreFamille et est rattaché à un couple par un idConjoints.
 */
public class SFApercuEnfant extends SFMembreFamille {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final int ALT_KEY_IDENFANT = 100;

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            : ne nom de la base de donnée (p.ex. WEBAVSP), donnée par _getCollection()
     * 
     * @return DOCUMENT ME!
     */
    public static String createFromClause(String schema) {

        return schema + SFEnfant.TABLE_NAME + " AS " + SFEnfant.TABLE_NAME + " INNER JOIN " + schema
                + SFMembreFamille.TABLE_NAME + " AS " + SFMembreFamille.TABLE_NAME + " ON ("
                + SFMembreFamille.TABLE_NAME + "." + SFMembreFamille.FIELD_IDMEMBREFAMILLE + " = "
                + SFEnfant.TABLE_NAME + "." + SFEnfant.FIELD_IDMEMBREFAMILLE + ")"
                + SFMembreFamille.createJoinClause(schema);
    }

    // business fields
    private boolean checkedParents = false;
    private SFConjoint conjoint = null;

    private String dateAdoption = "";
    private String idConjoint = "";
    private String idEnfant = "";
    private SFMembreFamille mere = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private SFMembreFamille pere = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);

        // Si this est un viewBean, alors on ajoute la classe parent
        Class[] interfaces = getClass().getInterfaces();
        boolean interfaceViewBean = false;
        for (int i = 0; (i < interfaces.length) && (!interfaceViewBean); i++) {
            if (interfaces[i].getName().equals(globaz.framework.bean.FWViewBeanInterface.class.getName())) {
                interfaceViewBean = true;
            }
        }
        if (interfaceViewBean) {
            SFMembreFamille membre = new SFMembreFamille();
            membre.setSession(getSession());
            membre.copyDataFromEntity(this);
            membre.add(transaction);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String from = createFromClause(_getCollection());

        return from;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idEnfant = statement.dbReadNumeric(SFEnfant.FIELD_IDENFANT);
        dateAdoption = statement.dbReadDateAMJ(SFEnfant.FIELD_DATEADOPTION);
        idConjoint = statement.dbReadNumeric(SFEnfant.FIELD_IDCONJOINT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeAlternateKey(globaz.globall.db.BStatement , int)
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        if (alternateKey == ALT_KEY_IDENFANT) {
            statement.writeKey(SFEnfant.FIELD_IDENFANT,
                    _dbWriteNumeric(statement.getTransaction(), idEnfant, "idEnfant"));
        } else {
            super._writeAlternateKey(statement, alternateKey);
        }
    }

    // Fixe le père et la mère sur les champs, laisse à null si indeterminé
    private void checkParents() {
        if (checkedParents) {
            return;
        }
        if (getConjoints() == null) {
            return;
        }
        checkedParents = true;

        try {
            SFMembreFamille parent1 = getParent(getConjoints().getIdConjoint1());
            SFMembreFamille parent2 = getParent(getConjoints().getIdConjoint2());
            if (ITIPersonne.CS_HOMME.equals(parent1.getCsSexe()) || ITIPersonne.CS_FEMME.equals(parent2.getCsSexe())) {
                pere = parent1;
                mere = parent2;
            } else if (ITIPersonne.CS_FEMME.equals(parent1.getCsSexe())
                    || ITIPersonne.CS_HOMME.equals(parent2.getCsSexe())) {
                mere = parent1;
                pere = parent2;
            } else {
                // mis aléatoirement
                mere = parent1;
                pere = parent2;
            }
        } catch (RuntimeException e) {
            return;
        }
    }

    /**
     * Renvoie les parents d'un enfant ou null en cas d'erreur ou de conjoint non-trouvé
     * 
     * @param transaction
     * @return
     */
    public SFConjoint getConjoints() {
        if (conjoint == null) {
            conjoint = new SFConjoint();
            conjoint.setSession(getSession());
            conjoint.setIdConjoints(getIdConjoint());
            try {
                conjoint.retrieve();
            } catch (Exception e) {
                conjoint = null;
            }
            if (conjoint.isNew()) {
                conjoint = null;
            }
        }
        return conjoint;
    }

    /**
     * @return
     */
    public String getDateAdoption() {
        return dateAdoption;
    }

    /**
     * @return
     */
    public String getIdConjoint() {
        return idConjoint;
    }

    /**
     * @return
     */
    public String getIdEnfant() {
        return idEnfant;
    }

    /* méthodes business */

    /**
     * Renvoie la mère de l'enfant
     * 
     * @return null si indeterminé ou en cas d'erreur
     */
    public SFMembreFamille getMere() {
        checkParents();
        return mere;
    }

    /**
     * @return le numéro AVS de la mère ou null si non determiné
     */
    public String getNoAvsMere() {
        SFMembreFamille mere = getMere();
        if (mere == null) {
            return null;
        } else {
            return mere.getNss();
        }
    }

    /**
     * @return le numéro AVS du père ou null si non determiné
     */
    public String getNoAvsPere() {
        SFMembreFamille pere = getPere();
        if (pere == null) {
            return null;
        } else {
            return pere.getNss();
        }
    }

    /**
     * @return le numéro AVS de la mère ou null si non determiné
     */
    public String getNomMere() {
        SFMembreFamille mere = getMere();
        if (mere == null) {
            return null;
        } else {
            return mere.getNom();
        }
    }

    /**
     * @return le numéro AVS du père ou null si non determiné
     */
    public String getNomPere() {
        SFMembreFamille pere = getPere();
        if (pere == null) {
            return null;
        } else {
            return pere.getNom();
        }
    }

    private SFMembreFamille getParent(String idParent) {
        SFMembreFamille parent = new SFMembreFamille();
        parent.setSession(getSession());
        parent.setIdMembreFamille(idParent);
        try {
            parent.retrieve();
            if (parent.isNew()) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        return parent;
    }

    /**
     * Renvoie le père de l'enfant
     * 
     * @return null si indeterminé ou en cas d'erreur
     */
    public SFMembreFamille getPere() {
        checkParents();
        return pere;
    }

    /**
     * @return le numéro AVS de la mère ou null si non determiné
     */
    public String getPrenomMere() {
        SFMembreFamille mere = getMere();
        if (mere == null) {
            return null;
        } else {
            return mere.getPrenom();
        }
    }

    /**
     * @return le numéro AVS du père ou null si non determiné
     */
    public String getPrenomPere() {
        SFMembreFamille pere = getPere();
        if (pere == null) {
            return null;
        } else {
            return pere.getPrenom();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFEnfant#isRecueilli()
     */
    public boolean isRecueilli() {

        SFPeriodeManager perMgr = new SFPeriodeManager();
        perMgr.setSession(getSession());
        perMgr.setForIdMembreFamille(getIdMembreFamille());
        try {
            perMgr.find();
            for (Iterator iter = perMgr.iterator(); iter.hasNext();) {
                SFPeriode element = (SFPeriode) iter.next();
                if (ISFSituationFamiliale.CS_TYPE_PERIODE_ENFANT.equals(element.getType())) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * /**
     * 
     * @param string
     */
    public void setDateAdoption(String string) {
        dateAdoption = string;
    }

    /**
     * @param string
     */
    public void setIdConjoint(String string) {
        idConjoint = string;
    }

    /**
     * @param string
     */
    public void setIdEnfant(String string) {
        idEnfant = string;
    }

}
