/*
 * Créé le 13 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.droits;

import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.clone.factory.IPRCloneable;

/**
 * DOCUMENT ME!
 * 
 * @author vre
 */
public class APSituationFamilialeAPG extends BEntity implements IPRCloneable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** DOCUMENT ME! */
    public static final String FIELDNAME_FRAISGARDE = "VDMFRG";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_IDSITUATIONFAMAPG = "VDISIF";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_NBRENFANTDEBUTDROIT = "VDNNOE";

    /** DOCUMENT ME! */
    public static final String TABLE_NAME = "APSIFAP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private boolean deleteprestationsRequis = false;

    /** DOCUMENT ME! */
    protected String fraisGarde = "";

    /** DOCUMENT ME! */
    protected String idSitFamAPG = "";

    /** DOCUMENT ME! */
    protected String nbrEnfantsDebutDroit = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        // eradication des enfants
        APEnfantAPGManager mgr = new APEnfantAPGManager();

        mgr.setSession(getSession());
        mgr.setForIdSituationFamiliale(idSitFamAPG);
        mgr.find(transaction);

        for (int idEnfant = 0; idEnfant < mgr.size(); ++idEnfant) {
            APEnfantAPG enfant = (APEnfantAPG) mgr.get(idEnfant);

            enfant.setSession(getSession());
            enfant.delete(transaction);
        }
    }

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        // quand un droit est mis à jour, les prestations qui ont pu être
        // calculée pour ce droit n'ont plus aucun sens,
        // on les efface.

        if (deleteprestationsRequis) {
            APDroitAPGManager droitAPGManager = new APDroitAPGManager();
            droitAPGManager.setSession(getSession());
            droitAPGManager.setForIdSituationFamiliale(idSitFamAPG);
            droitAPGManager.find(transaction);

            if (droitAPGManager.size() != 0) {
                String idDroit = null;
                APDroitAPG droitAPG = (APDroitAPG) droitAPGManager.getEntity(0);
                idDroit = droitAPG.getIdDroit();

                if (idDroit != null) {
                    APPrestationManager prestationManager = new APPrestationManager();
                    prestationManager.setSession(getSession());
                    prestationManager.setForIdDroit(idDroit);
                    prestationManager.find(transaction, BManager.SIZE_NOLIMIT);

                    for (int i = 0; i < prestationManager.size(); i++) {
                        APPrestation prestation = (APPrestation) prestationManager.getEntity(i);
                        prestation.delete(transaction);
                    }
                }
            }
        }
        deleteprestationsRequis = false;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdSitFamAPG(_incCounter(transaction, "0"));
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
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
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        nbrEnfantsDebutDroit = statement.dbReadNumeric(FIELDNAME_NBRENFANTDEBUTDROIT);
        idSitFamAPG = statement.dbReadNumeric(FIELDNAME_IDSITUATIONFAMAPG);
        fraisGarde = statement.dbReadNumeric(FIELDNAME_FRAISGARDE, 2);
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
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
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
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_IDSITUATIONFAMAPG,
                _dbWriteNumeric(statement.getTransaction(), idSitFamAPG, "idSituationFamiliale"));
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
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_NBRENFANTDEBUTDROIT,
                _dbWriteNumeric(statement.getTransaction(), nbrEnfantsDebutDroit, "nbrEnfantsDebutDroit"));
        statement.writeField(FIELDNAME_IDSITUATIONFAMAPG,
                _dbWriteNumeric(statement.getTransaction(), idSitFamAPG, "idSituationFamiliale"));
        statement.writeField(FIELDNAME_FRAISGARDE,
                _dbWriteNumeric(statement.getTransaction(), fraisGarde, "fraisGarde"));
    }

    /**
     * @param actionType
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public IPRCloneable duplicate(int actionType) {
        APSituationFamilialeAPG clone = new APSituationFamilialeAPG();
        clone.setFraisGarde(getFraisGarde());
        clone.setNbrEnfantsDebutDroit(getNbrEnfantsDebutDroit());

        // On ne veut pas de la validation pendant une duplication
        clone.wantCallValidate(false);

        return clone;
    }

    /**
     * getter pour l'attribut frais garde
     * 
     * @return la valeur courante de l'attribut frais garde
     */
    public String getFraisGarde() {
        return fraisGarde;
    }

    /**
     * getter pour l'attribut id sit fam APG
     * 
     * @return la valeur courante de l'attribut id sit fam APG
     */
    public String getIdSitFamAPG() {
        return idSitFamAPG;
    }

    /**
     * getter pour l'attribut inbr enfants debut droit
     * 
     * @return la valeur courante de l'attribut inbr enfants debut droit
     */
    public String getNbrEnfantsDebutDroit() {
        return nbrEnfantsDebutDroit;
    }

    /**
     * getter pour l'attribut unique primary key
     * 
     * @return la valeur courante de l'attribut unique primary key
     */
    @Override
    public String getUniquePrimaryKey() {
        return getIdSitFamAPG();
    }

    /**
     * getter pour l'attribut deleteprestations requis
     * 
     * @return la valeur courante de l'attribut deleteprestations requis
     */
    public boolean isDeleteprestationsRequis() {
        return deleteprestationsRequis;
    }

    /**
     * setter pour l'attribut deleteprestations requis
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setDeleteprestationsRequis(boolean b) {
        deleteprestationsRequis = b;
    }

    /**
     * setter pour l'attribut frais garde
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFraisGarde(String string) {
        // on risque de passer dans un JAUtils.setBeanProperties, il faut donc
        // déformater le montant, sinon numberFormatException
        string = JANumberFormatter.deQuote(string);

        if (!string.equals(JANumberFormatter.format(fraisGarde, 0.01, 2, JANumberFormatter.NEAR))) {
            deleteprestationsRequis = true;
        }

        fraisGarde = string;
    }

    /**
     * setter pour l'attribut id sit fam APG
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdSitFamAPG(String string) {
        idSitFamAPG = string;
    }

    /**
     * setter pour l'attribut inbr enfants debut droit
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNbrEnfantsDebutDroit(String string) {
        if (!string.equals(nbrEnfantsDebutDroit)
                && !(JadeStringUtil.isIntegerEmpty(string) && JadeStringUtil.isIntegerEmpty(nbrEnfantsDebutDroit))) {
            deleteprestationsRequis = true;
        }

        nbrEnfantsDebutDroit = string;
    }

    /**
     * setter pour l'attribut unique primary key
     * 
     * @param pk
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setUniquePrimaryKey(String pk) {
        setIdSitFamAPG(pk);
    }
}
