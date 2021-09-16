/*
 * Créé le 09 septembre 05
 */
package globaz.ij.vb.prononces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BStatement;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJPrononceJointDemande;
import globaz.ij.db.prononces.IJPrononceJointDemandeManager;
import globaz.ij.servlet.IIJActions;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.tools.PRCodeSystem;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.Vector;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author dvh
 */
public class IJPrononceJointDemandeViewBean extends IJPrononceJointDemande implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String LABEL_DROITS_NON_COMMUNIQUES = "DROITS_NON_COMMUNIQUES";
    private static final String LABEL_DROITS_TOUS = "DROITS_TOUS";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private transient Vector etatsDroit = null;

    private String nbPostit = "";
    private transient Vector orderBy = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        nbPostit = statement.dbReadNumeric(IJPrononceJointDemandeListViewBean.FIELDNAME_COUNT_POSTIT);

        super._readProperties(statement);

        setIdTiers(statement.dbReadNumeric(PRDemande.FIELDNAME_IDTIERS));
    }

    /**
     * Retourne la liste des codes systèmes pour l'état du droit augmentée des champs 'tous' et 'non définitif'.
     * 
     * @return un Vector de String[2]{codeSysteme, libelleCodeSysteme}.
     */
    public Vector getCsEtatPrononceData() {
        if (etatsDroit == null) {
            etatsDroit = PRCodeSystem.getLibellesPourGroupe(IIJPrononce.CS_GROUPE_ETAT_PRONONCE, getSession());

            // ajout des options custom
            etatsDroit.add(
                    0,
                    new String[] { IJPrononceJointDemandeManager.CLE_DROITS_TOUS,
                            getSession().getLabel(IJPrononceJointDemandeViewBean.LABEL_DROITS_TOUS) });
            etatsDroit.add(0, new String[] { IJPrononceJointDemandeManager.CLE_DROITS_NON_COMMUNIQUES,
                    getSession().getLabel(IJPrononceJointDemandeViewBean.LABEL_DROITS_NON_COMMUNIQUES) });
        }

        return etatsDroit;
    }

    /**
     * getter pour l'attribut current user id
     * 
     * @return la valeur courante de l'attribut current user id
     */
    public String getCurrentUserId() {
        return getSession().getUserId();
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les listes
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailRequerant() {

        return PRNSSUtil.formatDetailRequerantListe(getNoAVS(), getNomPrenom(), getDateNaissance(),
                getLibelleCourtSexe(), getLibellePays());

    }

    /**
     * getter pour le libelle de la valeur de etat demande
     * 
     * @return le libelle de l'attribut etat demande
     */
    public String getEtatDemandeLibelle() {
        return getSession().getCodeLibelle(getCsEtatDemande());
    }

    /**
     * getter pour l'attribut etat droit libelle
     * 
     * @return la valeur courante de l'attribut etat droit libelle
     */
    public String getEtatPrononceLibelle() {
        return getSession().getCodeLibelle(getCsEtatPrononce());
    }

    /**
     * getter pour l'attribut genre prononce libelle
     * 
     * @return la valeur courante de l'attribut genre droit libelle
     */
    public String getGenrePrononceLibelle() {
        return getSession().getCodeLibelle(getCsTypeIJ());
    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public String getLibelleCourtSexe() {

        if (PRACORConst.CS_HOMME.equals(getCsSexe())) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(getCsSexe())) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }

    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est dans le vb
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public String getLibellePays() {

        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", getCsNationalite())))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", getCsNationalite()));
        }

    }

    /**
     * Méthode qui retourne le libellé du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé du sexe (homme ou femme)
     */
    public String getLibelleSexe() {
        return getSession().getCodeLibelle(getCsSexe());
    }

    public String getNbPostit() {
        return nbPostit;
    }

    /**
     * getter pour l'attribut nom prenom
     * 
     * @return la valeur courante de l'attribut nom prenom
     */
    public String getNomPrenom() {
        return getNom() + " " + getPrenom();
    }

    /**
     * getter pour l'attribut nom gestionnaire
     * 
     * @return la valeur courante de l'attribut nom gestionnaire
     */
    public String getNomPrenomGestionnaire() {
        return getPrenomGestionnaire() + " " + getNomGestionnaire();
    }

    /**
     * getter pour l'attribut order by data
     * 
     * @return la valeur courante de l'attribut order by data
     */
    public Vector getOrderByData() {
        if (orderBy == null) {

            String orderByZeroFirst = " CASE WHEN " + IJPrononce.FIELDNAME_DATE_FIN_PRONONCE
                    + " =0 THEN 99999999 ELSE " + IJPrononce.FIELDNAME_DATE_FIN_PRONONCE + " END DESC";

            orderBy = new Vector(4);

            orderBy.add(new String[] {
                    IJPrononceJointDemande.FIELDNAME_NOM + ", " + IJPrononceJointDemande.FIELDNAME_PRENOM + ", "
                            + orderByZeroFirst+","+ IJPrononce.FIELDNAME_ID_PRONONCE + " DESC" ,
                    getSession().getLabel("JSP_NOM") + ", " + getSession().getLabel("JSP_PRENOM") });
            orderBy.add(new String[] { IJPrononceJointDemande.FIELDNAME_NUM_AVS + ", " + orderByZeroFirst+","+ IJPrononce.FIELDNAME_ID_PRONONCE + " DESC" ,
                    getSession().getLabel("JSP_NSS_ABREGE") });
            orderBy.add(new String[] { IJPrononce.FIELDNAME_DATE_DEBUT_PRONONCE + " DESC, "+ IJPrononce.FIELDNAME_ID_PRONONCE + " DESC",
                    getSession().getLabel("JSP_DATE_DEBUT") });
            orderBy.add(new String[] { IJPrononce.FIELDNAME_ID_PRONONCE + " DESC", getSession().getLabel("JSP_NUMERO") });
        }

        return orderBy;
    }

    /**
     * getter pour l'attribut type IJLibelle
     * 
     * @return la valeur courante de l'attribut type IJLibelle
     */
    public String getTypeIJLibelle() {
        return getSession().getCodeLibelle(getCsTypeIJ());
    }

    /**
     * Renvoie la partie classpart de l'action
     * 
     * @return la valeur courante de l'attribut user action
     */
    public String getUserAction() {
        if (IIJPrononce.CS_PETITE_IJ.equals(getCsTypeIJ())) {
            return IIJActions.ACTION_PETITE_IJ;
        } else if (IIJPrononce.CS_FPI.equals(getCsTypeIJ())) {
            return IIJActions.ACTION_FPI;
        } else {
            return IIJActions.ACTION_GRANDE_IJ;
        }
    }

    public boolean hasPostit() {
        return Integer.parseInt(nbPostit) > 0;
    }

    /**
     * Méthode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     * 
     * @return String (true ou false)
     */
    public String isNNSS() {

        if (JadeStringUtil.isBlankOrZero(getNoAVS())) {
            return "";
        }

        if (getNoAVS().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * Méthode qui retourne une string avec true si le NSS passé en paramètre est un NNSS, sinon false
     * 
     * @param noAvs
     * @return String (true ou false)
     */
    public String isNNSS(String noAvs) {

        if (JadeStringUtil.isBlankOrZero(noAvs)) {
            return "";
        }

        if (noAvs.length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

}
