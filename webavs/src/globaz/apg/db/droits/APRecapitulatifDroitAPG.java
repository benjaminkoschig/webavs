/*
 * Créé le 30 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.droits;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAVector;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class APRecapitulatifDroitAPG extends APAbstractRecapitulatifDroit {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private transient JAVector enfants = null;
    private String idSituationFamAPG = "";

    private String nbEnfantsDebDroit = "";
    private String nbJoursSoldes = "";
    private String noCompte = "";
    private String noControle = "";
    private transient JAVector periodes = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        super._afterRetrieve(transaction);

        // charge les periodes
        APPeriodeAPGManager pMgr = new APPeriodeAPGManager();

        pMgr.setSession(getSession());
        pMgr.setForIdDroit(idDroit);
        pMgr.find(transaction);

        periodes = pMgr.getContainer();

        // charge les enfants
        APEnfantComptesOrdonnesAPGManager eMgr = new APEnfantComptesOrdonnesAPGManager();

        eMgr.setSession(getSession());
        eMgr.setForIdSituationFamAPG(idSituationFamAPG);
        eMgr.find(transaction);

        enfants = eMgr.getContainer();
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            String schema = _getCollection();
            StringBuffer fromClauseBuffer = createFromBase(schema);

            // jointure sur la table des droit APG.
            fromClauseBuffer.append(" INNER JOIN ");
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(APDroitAPG.TABLE_NAME_DROIT_APG);
            fromClauseBuffer.append(" ON ");
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(APDroitLAPG.TABLE_NAME_LAPG);
            fromClauseBuffer.append(".");
            fromClauseBuffer.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
            fromClauseBuffer.append("=");
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(APDroitAPG.TABLE_NAME_DROIT_APG);
            fromClauseBuffer.append(".");
            fromClauseBuffer.append(APDroitAPG.FIELDNAME_IDDROIT_APG);

            // jointure sur la table des droit APG.
            fromClauseBuffer.append(" INNER JOIN ");
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(APSituationFamilialeAPG.TABLE_NAME);
            fromClauseBuffer.append(" ON ");
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(APDroitAPG.TABLE_NAME_DROIT_APG);
            fromClauseBuffer.append(".");
            fromClauseBuffer.append(APDroitAPG.FIELDNAME_IDSITUATIONFAM);
            fromClauseBuffer.append("=");
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(APSituationFamilialeAPG.TABLE_NAME);
            fromClauseBuffer.append(".");
            fromClauseBuffer.append(APSituationFamilialeAPG.FIELDNAME_IDSITUATIONFAMAPG);

            fromClause = fromClauseBuffer.toString();
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        nbJoursSoldes = statement.dbReadNumeric(APDroitAPG.FIELDNAME_NBRJOURSSOLDES);
        noCompte = statement.dbReadNumeric(APDroitAPG.FIELDNAME_NOCOMPTE);
        noControle = statement.dbReadNumeric(APDroitAPG.FIELDNAME_NOCONTROLEPERS);
        nbEnfantsDebDroit = statement.dbReadNumeric(APSituationFamilialeAPG.FIELDNAME_NBRENFANTDEBUTDROIT);
        idSituationFamAPG = statement.dbReadNumeric(APDroitAPG.FIELDNAME_IDSITUATIONFAM);
    }

    /**
     * getter pour l'attribut enfants
     * 
     * @return la valeur courante de l'attribut enfants
     */
    public JAVector getEnfants() {
        return enfants;
    }

    /**
     * getter pour l'attribut id situation fam APG
     * 
     * @return la valeur courante de l'attribut id situation fam APG
     */
    public String getIdSituationFamAPG() {
        return idSituationFamAPG;
    }

    /**
     * getter pour l'attribut nb enfants deb droit
     * 
     * @return la valeur courante de l'attribut nb enfants deb droit
     */
    public String getNbEnfantsDebDroit() {
        return nbEnfantsDebDroit;
    }

    /**
     * getter pour l'attribut nb jours soldes
     * 
     * @return la valeur courante de l'attribut nb jours soldes
     */
    public String getNbJoursSoldes() {
        return nbJoursSoldes;
    }

    /**
     * getter pour l'attribut no compte
     * 
     * @return la valeur courante de l'attribut no compte
     */
    public String getNoCompte() {
        return noCompte;
    }

    /**
     * getter pour l'attribut no controle
     * 
     * @return la valeur courante de l'attribut no controle
     */
    public String getNoControle() {
        return noControle;
    }

    /**
     * getter pour l'attribut periodes
     * 
     * @return la valeur courante de l'attribut periodes
     */
    public List getPeriodes() {
        return periodes;
    }

    /**
     * setter pour l'attribut id situation fam APG
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdSituationFamAPG(String string) {
        idSituationFamAPG = string;
    }

    /**
     * setter pour l'attribut nb enfants deb droit
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNbEnfantsDebDroit(String string) {
        nbEnfantsDebDroit = string;
    }

    /**
     * setter pour l'attribut nb jours soldes
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNbJoursSoldes(String string) {
        nbJoursSoldes = string;
    }

    /**
     * setter pour l'attribut no compte
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoCompte(String string) {
        noCompte = string;
    }

    /**
     * setter pour l'attribut no controle
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoControle(String string) {
        noControle = string;
    }
}
