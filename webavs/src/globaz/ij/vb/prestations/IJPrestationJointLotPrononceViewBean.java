package globaz.ij.vb.prestations;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BStatement;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.prestations.IJPrestationJointLotPrononce;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.regles.IJPrestationRegles;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * @author DVH
 */
public class IJPrestationJointLotPrononceViewBean extends IJPrestationJointLotPrononce implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String montantNet = "";
    private String nbPostit = "";

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        nbPostit = statement.dbReadNumeric(IJPrestationJointLotPrononceListViewBean.FIELDNAME_COUNT_POSTIT);
        montantNet = statement.dbReadNumeric(IJPrestationJointLotPrononceListViewBean.FIELDNAME_SUM_MONTANT_NET);

        super._readProperties(statement);

    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les rcListes
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailRequerant() {

        return PRNSSUtil.formatDetailRequerantListe(getNoAVS(), getNom() + " " + getPrenom(), getDateNaissance(),
                getLibelleCourtSexe(), getLibellePays());

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
     * getter pour l'attribut libelle etat.
     * 
     * @return la valeur courante de l'attribut libelle etat
     */
    public String getLibelleEtat() {
        return getSession().getCodeLibelle(getCsEtat());
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

    /**
     * getter pour l'attribut libelle type.
     * 
     * @return la valeur courante de l'attribut libelle type
     */
    public String getLibelleType() {
        return getSession().getCodeLibelle(getCsType());
    }

    public String getMontantNet() {
        return montantNet;
    }

    public String getNbPostit() {
        return nbPostit;
    }

    public boolean hasPostit() {
        return Integer.parseInt(nbPostit) > 0;
    }

    /**
     * 
     * @return true si le prononce de la prestation est de type AIT ou alloc. Assist. false si Petite ou grande IJ
     */
    public boolean isAitOrAllocAssist() {

        try {
            // Récupération du prononcé pour determiner s'il s'agit d'un cas AIT
            // ou Alloc. assist.
            IJPrononce prononce = new IJPrononce();
            prononce.setSession(getSession());
            prononce.setIdPrononce(loadBaseIndemnisation(null).getIdPrononce());
            prononce.retrieve();
            PRAssert.notIsNew(prononce, null);
            if (IIJPrononce.CS_ALLOC_ASSIST.equals(prononce.getCsTypeIJ())
                    || IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(prononce.getCsTypeIJ())) {

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * getter pour l'attribut modifier permis.
     * 
     * @return la valeur courante de l'attribut modifier permis
     */
    public boolean isModifierPermis() {

        return IJPrestationRegles.isModifierPermis(this);
    }

    /**
     * getter pour l'attribut supprimer permis.
     * 
     * @return la valeur courante de l'attribut supprimer permis.
     */
    public boolean isSupprimerPermis() {
        return IJPrestationRegles.isSupprimerPermis(this);
    }

    public void setMontantNet(String montantNet) {
        this.montantNet = montantNet;
    }

}
