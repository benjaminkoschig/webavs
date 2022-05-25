/*
 * Cr�� le 9 sept. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.hera.db.famille;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.pyxis.api.ITIPersonne;
import java.util.Iterator;

/**
 * @author mmu Classe qui fait la jointure entre les enfants(SFENFANT) et les membres de Famille(SFMBRFAM) � noter que
 *         la date d'adoption fait partie de la classe SFENFANT L'enfant est identif� dans la teble Membre famille par
 *         un idMembreFamille et est rattach� � un couple par un idConjoints.
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
     *            : ne nom de la base de donn�e (p.ex. WEBAVSP), donn�e par _getCollection()
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
    private SFMembreFamille parent2 = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private SFMembreFamille parent1 = null;

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

    // Fixe le p�re et la m�re sur les champs, laisse � null si indetermin�
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
                this.parent1 = parent1;
                this.parent2 = parent2;
            } else if (ITIPersonne.CS_FEMME.equals(parent1.getCsSexe())
                    || ITIPersonne.CS_HOMME.equals(parent2.getCsSexe())) {
                this.parent2 = parent1;
                this.parent1 = parent2;
            } else {
                // mis al�atoirement
                this.parent2 = parent1;
                this.parent1 = parent2;
            }
        } catch (RuntimeException e) {
            return;
        }
    }

    /**
     * Renvoie les parents d'un enfant ou null en cas d'erreur ou de conjoint non-trouv�
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

    /* m�thodes business */

    /**
     * Renvoie la m�re de l'enfant
     * 
     * @return null si indetermin� ou en cas d'erreur
     */
    public SFMembreFamille getParent2() {
        checkParents();
        return parent2;
    }

    /**
     * @return le num�ro AVS de la m�re ou null si non determin�
     */
    public String getNoAvsParent2() {
        SFMembreFamille parent2 = getParent2();
        if (parent2 == null) {
            return null;
        } else {
            return parent2.getNss();
        }
    }

    /**
     * @return le num�ro AVS du p�re ou null si non determin�
     */
    public String getNoAvsParent1() {
        SFMembreFamille parent1 = getParent1();
        if (parent1 == null) {
            return null;
        } else {
            return parent1.getNss();
        }
    }

    /**
     * @return le num�ro AVS de la m�re ou null si non determin�
     */
    public String getNomParent2() {
        SFMembreFamille parent2 = getParent2();
        if (parent2 == null) {
            return null;
        } else {
            return parent2.getNom();
        }
    }

    /**
     * @return le num�ro AVS du p�re ou null si non determin�
     */
    public String getNomParent1() {
        SFMembreFamille parent1 = getParent1();
        if (parent1 == null) {
            return null;
        } else {
            return parent1.getNom();
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
     * Renvoie le p�re de l'enfant
     * 
     * @return null si indetermin� ou en cas d'erreur
     */
    public SFMembreFamille getParent1() {
        checkParents();
        return parent1;
    }

    /**
     * @return le num�ro AVS de la m�re ou null si non determin�
     */
    public String getPrenomParent2() {
        SFMembreFamille parent2 = getParent2();
        if (parent2 == null) {
            return null;
        } else {
            return parent2.getPrenom();
        }
    }

    /**
     * @return le num�ro AVS du p�re ou null si non determin�
     */
    public String getPrenomParent1() {
        SFMembreFamille parent1 = getParent1();
        if (parent1 == null) {
            return null;
        } else {
            return parent1.getPrenom();
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

    public String getDateNaissanceParent1() {
        SFMembreFamille parent1 = getParent1();
        if (parent1 == null) {
            return null;
        } else {
            return parent1.getDateNaissance();
        }
    }

    public String getDateNaissanceParent2() {
        SFMembreFamille parent2 = getParent2();
        if (parent2 == null) {
            return null;
        } else {
            return parent2.getDateNaissance();
        }
    }

    public String getSexeParent1() {
        SFMembreFamille parent1 = getParent1();
        if (parent1 == null) {
            return null;
        } else {
            return parent1.getCsSexe();
        }
    }

    public String getSexeParent2() {
        SFMembreFamille parent2 = getParent2();
        if (parent2 == null) {
            return null;
        } else {
            return parent2.getCsSexe();
        }
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
