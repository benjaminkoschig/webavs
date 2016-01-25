/*
 * Créé le 17 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.draco.db.preimpression;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JADate;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.util.CIUtil;

/**
 * ViewBean permettant de sélectionner tous les assurés d'un employeur pour la liste de pré-impression des déclarations
 * de salaire
 * 
 * @author sda
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class DSPreImpressionDeclarationListViewBean extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean affilieTous = false;
    private String annee = new String();
    private int anneeDeclaration = 0;
    private boolean dateProdNNSS = false;
    private String eMailAddress = new String();
    // private String forAffilies = new String();
    private String forAffiliesNumero = new String();
    private boolean imprimerDeclaration = false;

    private boolean imprimerLettre = false;

    public DSPreImpressionDeclarationListViewBean() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return _getCollection() + "CIINDIP.KAIIND, " + _getCollection() + "CIINDIP.KANAVS, " + _getCollection()
        // Modif jmc 1-5-8 => date de naissance pour détecter les renties en
        // cours d'annnée
                + "CIINDIP.KADNAI, " + _getCollection() + "CIINDIP.KATSEX, " + _getCollection() + "CIINDIP.KALNOM,"
                // Modif jmc 15.10.2006 pour faire apparaite 01-01 sur
                // l'impression
                + getAnnee() + "0101 AS DAENG, 0 AS DALIC," + _getCollection() + "CIECRIP.KBTCAT";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CIINDIP, " + _getCollection() + "CIECRIP, " + _getCollection() + "CIJOURP";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        if (isDateProdNNSS()) {
            return "KALNOM";
        } else {
            return "KANAVS";
        }
        // return "KANAVS";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        anneeDeclaration = new Integer(getAnnee().trim()).intValue() - 1;
        // JADate datedebut = new JADate();
        String dateDebut = new String();
        dateDebut = annee + "00" + "00";
        JADate datefin = new JADate();
        // datedebut.setYear(Integer.parseInt(annee));
        // datedebut.setDay(1);
        // datedebut.setMonth(1);
        datefin.setYear(Integer.parseInt(annee));
        datefin.setDay(31);
        datefin.setMonth(12);
        String sqlWhere = _getCollection() + "CIINDIP.KAIIND =" + _getCollection() + "CIECRIP.KAIIND AND "
                + _getCollection() + "CIECRIP.KCID =" + _getCollection() + "CIJOURP.KCID AND "
                + _getCollection()
                + "CIECRIP.KBNANN="
                + _dbWriteNumeric(statement.getTransaction(), String.valueOf(anneeDeclaration))
                + " AND "
                + _getCollection()
                + "CIECRIP.KBTEXT =0"
                + " AND ("
                + _getCollection()
                + "CIECRIP.KBTGEN ="
                + CIEcriture.CS_CIGENRE_1
                // modif 24.08.2006, ne pas prendre les insc. d'indépendant,gre
                // 7 code 2
                + " OR (" + _getCollection() + "CIECRIP.KBTGEN =" + CIEcriture.CS_CIGENRE_7 + " AND "
                + _getCollection() + "CIECRIP.KBTSPE <> " + CIEcriture.CS_NONFORMATTEUR_INDEPENDANT + ")) AND "
                + _getCollection() + "CIECRIP.KBNMOF = 12 AND " + _getCollection() + "CIECRIP.KBBATT = '2' AND "
                + _getCollection() + "CIINDIP.KAIREG =" + CICompteIndividuel.CS_REGISTRE_ASSURES + " AND "
                + _getCollection() + "CIINDIP.KABINA <> '1'";

        if (getForAffiliesNumero().length() != 0) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBITIE IN(" + CIUtil.unFormatAVS(getForAffiliesNumero())
                    + ")";
        }
        sqlWhere += " UNION SELECT " + _getCollection() + "CIINDIP.KAIIND, " + _getCollection() + "CIINDIP.KANAVS, "
                + _getCollection() + "CIINDIP.KADNAI, " + _getCollection() + "CIINDIP.KATSEX, " + _getCollection()
                + "CIINDIP.KALNOM, " + _getCollection() + "CIEXCP.DAENG," + _getCollection() + "CIEXCP.DALIC, "
                + "0 AS KBTCAT FROM " + _getCollection() + "CIEXCP INNER JOIN " + _getCollection() + "CIINDIP ON "
                + _getCollection() + "CIINDIP.KAIIND = " + _getCollection() + "CIEXCP.KAIIND WHERE " + _getCollection()
                + "CIEXCP.DAENG >= " + dateDebut + " AND " + _getCollection() + "CIEXCP.DAENG <= "
                + _dbWriteDateAMJ(statement.getTransaction(), datefin.toString()) + " AND " + _getCollection()
                + " CIEXCP.MAIAFF IN (" + getForAffiliesNumero() + ")" + " AND " + _getCollection()
                + "CIINDIP.KABINA <> '1'";
        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new DSPreImpressionViewBean();
    }

    /**
     * Renvoie un booleen nous indiquant si on veut séléctionner tous les employeurs
     * 
     * @return affilieTous
     */
    public boolean getAffilieTous() {
        return affilieTous;
    }

    /**
     * Renvoie l'année de la déclaration de salaires
     * 
     * @return annee
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * @return
     */
    /*
     * public String getForAffilies() { return forAffilies; } /**
     * 
     * @param string
     */
    /*
     * public void setForAffilies(String string) { forAffilies = string; } /**
     * 
     * @return
     */
    public int getAnneeDeclaration() {
        return anneeDeclaration;
    }

    /**
     * Renvoie l'adresse e-mail ou envoyer le fichier joint
     * 
     * @return eMailAddress
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    /**
     * Renvoie l'id du premier employeur
     * 
     * @return forAffiliesNumero
     */
    public String getForAffiliesNumero() {
        return forAffiliesNumero;
    }

    public boolean isDateProdNNSS() {
        return dateProdNNSS;
    }

    /**
     * Renvoie une information nous indiquant si on doit imprimer une déclaration
     * 
     * @return imprimerDeclaration
     */
    public boolean isImprimerDeclaration() {
        return imprimerDeclaration;
    }

    /**
     * Renvoie une information nous indiquant si on doit imprimer une lettre
     * 
     * @return imprimerLettre
     */
    public boolean isImprimerLettre() {
        return imprimerLettre;
    }

    /**
     * Sette un booleen nous indiquant si on veut sélectionner tous les employeurs
     * 
     * @param boolean
     */
    public void setAffilieTous(boolean b) {
        affilieTous = b;
    }

    /**
     * Sette l'annee de la déclaration de salaires
     * 
     * @param string
     */
    public void setAnnee(String string) {
        annee = string;
    }

    /**
     * @param i
     */
    public void setAnneeDeclaration(int i) {
        anneeDeclaration = i;
    }

    public void setDateProdNNSS(boolean dateProdNNSS) {
        this.dateProdNNSS = dateProdNNSS;
    }

    /**
     * Sette l'adresse e-mail où envoyer le fichier joint
     * 
     * @param string
     */
    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

    /**
     * Sette l'id du premier employeur
     * 
     * @param string
     */
    public void setForAffiliesNumero(String string) {
        forAffiliesNumero = string;
    }

    /**
     * Sette un booleen nous indiquant si on doit imprimer une déclaration
     * 
     * @param b
     */
    public void setImprimerDeclaration(boolean b) {
        imprimerDeclaration = b;
    }

    /**
     * Sette un booleen nous indiquant si on doit imprimer une lettre
     * 
     * @param b
     */
    public void setImprimerLettre(boolean b) {
        imprimerLettre = b;
    }
}
