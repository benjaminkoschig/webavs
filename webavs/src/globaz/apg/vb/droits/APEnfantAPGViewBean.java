/*
 * Créé le 27 mai 05
 */
package globaz.apg.vb.droits;

import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APEnfantAPGJointTiers;
import globaz.apg.db.droits.APSituationFamilialeAPG;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.INSSViewBean;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APEnfantAPGViewBean extends APEnfantAPGJointTiers implements INSSViewBean, FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // pour savoir si on a cliqué sur le bouton arrêt
    private boolean arret = false;

    private APDroitAPG droit = null;

    private APDroitAPGDTO droitAPGDTO = null;
    // mis à null pour ne pas avoir a passer par isDecimalEmpty qui renverra
    // true. Si une modif était faite en mettant
    // 0, elle ne serait pas prise en compte dans getFraisGarde();
    private String fraisGarde = null;

    private String idAssure = null;

    private String idSituationFamAPG = "";

    private String nbrEnfantsDebutDroit;

    // le nombre d'enfants qu'on devra ajouter d'un coup, à 1 par défaut
    private String nombreEnfants = "1";

    private String provenance = null;
    private transient APSituationFamilialeAPG situationFamilialeAPG = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getCsCantonDomicile()
     * 
     * @return la valeur courante de l'attribut cs canton domicile
     */
    @Override
    public String getCsCantonDomicile() {
        // TODO Raccord de méthode auto-généré
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getCsEtatCivil()
     * 
     * @return la valeur courante de l'attribut cs etat civil
     */
    @Override
    public String getCsEtatCivil() {
        // TODO Raccord de méthode auto-généré
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getCsNationalite()
     * 
     * @return la valeur courante de l'attribut cs nationalite
     */
    @Override
    public String getCsNationalite() {
        // TODO Raccord de méthode auto-généré
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getCsSexe()
     * 
     * @return la valeur courante de l'attribut cs sexe
     */
    @Override
    public String getCsSexe() {
        // TODO Raccord de méthode auto-généré
        return null;
    }

    /**
     * getter pour l'attribut date debut droit APG
     * 
     * @return la valeur courante de l'attribut date debut droit APG
     */
    public String getDateDebutDroitAPG() {
        if (droitAPGDTO != null) {
            return droitAPGDTO.getDateDebutDroit();
        } else {
            return "";
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getDateDeces()
     * 
     * @return la valeur courante de l'attribut date deces
     */
    @Override
    public String getDateDeces() {
        // TODO Raccord de méthode auto-généré
        return null;
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les rcListes
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailRequerant() {
        return PRNSSUtil.formatDetailRequerantListe(getNss(), getNomPrenom(), getDateNaissance(),
                getLibelleCourtSexe(), getLibellePays());

    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les détails
     * 
     * @return le détail du requérant formaté
     * @throws Exception
     */
    public String getDetailRequerantDetail() throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), getNoAVSDroitAPG());

        if (tiers != null) {

            String nationalite = "";

            if (!"999".equals(getSession()
                    .getCode(
                            getSession().getSystemCode("CIPAYORI",
                                    tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))))) {
                nationalite = getSession().getCodeLibelle(
                        getSession().getSystemCode("CIPAYORI",
                                tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            }

            return PRNSSUtil.formatDetailRequerantDetail(getNoAVSDroitAPG(), getNomPrenomDroitAPG(),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }
    }

    /**
     * getter pour l'attribut frais garde
     * 
     * @return la valeur courante de l'attribut frais garde
     */
    public String getFraisGarde() {
        if (fraisGarde == null) {
            fraisGarde = loadSituationFamilialeAPG().getFraisGarde();
        }

        if (JadeStringUtil.isDecimalEmpty(fraisGarde)) {
            return "";
        } else {
            return fraisGarde;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getIdAssure()
     * 
     * @return la valeur courante de l'attribut id assure
     */
    @Override
    public String getIdAssure() {
        if (!JadeStringUtil.isIntegerEmpty(getIdTiers())) {
            return getIdTiers();
        } else {
            return idAssure;
        }
    }

    /**
     * getter pour l'attribut id droit
     * 
     * @return la valeur courante de l'attribut id droit
     */
    public String getIdDroit() {
        if (droitAPGDTO != null) {
            return droitAPGDTO.getIdDroit();
        } else {
            return "";
        }
    }

    /**
     * getter pour l'attribut id situation familiale APG
     * 
     * @return la valeur courante de l'attribut id situation familiale APG
     */
    public String getIdSituationFamilialeAPG() {
        if (droitAPGDTO != null) {
            return droitAPGDTO.getIdSituationFam();
        } else {
            return "";
        }
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

        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", getCsNationaliteEnf())))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", getCsNationaliteEnf()));
        }

    }

    /**
     * getter pour l'attribut nbr enfants debut droit
     * 
     * @return la valeur courante de l'attribut nbr enfants debut droit
     */
    public String getNbrEnfantsDebutDroit() {
        if (JadeStringUtil.isIntegerEmpty(nbrEnfantsDebutDroit)) {
            nbrEnfantsDebutDroit = loadSituationFamilialeAPG().getNbrEnfantsDebutDroit();
        }

        if (JadeStringUtil.isIntegerEmpty(nbrEnfantsDebutDroit)) {
            return "";
        } else {
            return nbrEnfantsDebutDroit;
        }
    }

    public String getNbrEnfantsDebutDroitBrut() {
        if (JadeStringUtil.isIntegerEmpty(nbrEnfantsDebutDroit)) {
            return "";
        } else {
            return nbrEnfantsDebutDroit;
        }
    }

    /**
     * getter pour l'attribut no AVSDroit APG
     * 
     * @return la valeur courante de l'attribut no AVSDroit APG
     */
    public String getNoAVSDroitAPG() {
        if (droitAPGDTO != null) {
            if (droitAPGDTO.getNoAVS().length() > 12) {
                return droitAPGDTO.getNoAVS();
            } else {
                return "756." + droitAPGDTO.getNoAVS();
            }
        } else {
            return "000.00.000.000";
        }
    }

    /**
     * getter pour l'attribut nombre enfants
     * 
     * @return la valeur courante de l'attribut nombre enfants
     */
    public String getNombreEnfants() {
        return nombreEnfants;
    }

    /**
     * getter pour l'attribut nom prenom
     * 
     * @return nom prenom
     */
    public String getNomPrenom() {
        return getNom() + " " + getPrenom();
    }

    /**
     * getter pour l'attribut nom prenom droit APG
     * 
     * @return la valeur courante de l'attribut nom prenom droit APG
     */
    public String getNomPrenomDroitAPG() {
        if (droitAPGDTO != null) {
            return droitAPGDTO.getNomPrenom();
        } else {
            return "";
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getProvenance()
     * 
     * @return la valeur courante de l'attribut provenance
     */
    @Override
    public String getProvenance() {
        return provenance;
    }

    /**
     * getter pour l'attribut arret
     * 
     * @return la valeur courante de l'attribut arret
     */
    public boolean isArret() {
        return arret;
    }

    /**
     * getter pour l'attribut modifiable
     * 
     * @return la valeur courante de l'attribut modifiable
     */
    public boolean isModifiable() {
        if (droitAPGDTO != null) {
            return droitAPGDTO.isModifiable();
        } else {
            return false;
        }
    }

    /**
     * Charge la situation Familiale avec laquelle ce droit est lié. Cette méthode recharge automatiquement la demande
     * si (et seulement si) la valeur de Id SituationFamiliale de ce bean a été modifiée.
     * 
     * @return la demande liée à ce droit (jamais nul).
     */
    public APSituationFamilialeAPG loadSituationFamilialeAPG() {
        // si la demande est nulle, instancier
        if (situationFamilialeAPG == null) {
            situationFamilialeAPG = new APSituationFamilialeAPG();
        }

        // on s'assure que la session est la bonne (pour les cas où on
        // chargerait le tiers...)
        situationFamilialeAPG.setSession(getSession());

        // si la situationfam est différente, charger la situation fam
        if (!idSituationFamAPG.equals(situationFamilialeAPG.getIdSitFamAPG())) {
            situationFamilialeAPG.setIdSitFamAPG(idSituationFamAPG);

            try {
                situationFamilialeAPG.retrieve();
            } catch (Exception e) {
                getSession().addError(getSession().getLabel("ERREUR_CHARGEMENT_SITUATION_FAMILIALE"));
            }
        }

        return situationFamilialeAPG;
    }

    /**
     * setter pour l'attribut arret
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setArret(boolean b) {
        arret = b;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setCsCantonDomicile(java.lang.String)
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setCsCantonDomicile(String string) {
        // TODO Raccord de méthode auto-généré
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setCsEtatCivil(java.lang.String)
     * 
     * @param s
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setCsEtatCivil(String s) {
        // TODO Raccord de méthode auto-généré
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setCsNationalite(java.lang.String)
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setCsNationalite(String string) {
        // TODO Raccord de méthode auto-généré
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setCsSexe(java.lang.String)
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setCsSexe(String string) {
        // TODO Raccord de méthode auto-généré
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setDateDeces(java.lang.String)
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setDateDeces(String string) {
        // TODO Raccord de méthode auto-généré
    }

    /**
     * setter pour l'attribut droit DTO
     * 
     * @param droitAPGDTO
     *            une nouvelle valeur pour cet attribut
     */
    public void setDroitAPGDTO(APDroitAPGDTO droitAPGDTO) {
        this.droitAPGDTO = droitAPGDTO;

        if (droitAPGDTO != null) {
            idSituationFamAPG = droitAPGDTO.getIdSituationFam();
        }
    }

    /**
     * setter pour l'attribut frais garde
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFraisGarde(String string) {
        fraisGarde = JANumberFormatter.deQuote(string);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setIdAssure(java.lang.String)
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setIdAssure(String string) {
        idAssure = string;

        if (PRUtil.PROVENANCE_TIERS.equals(getProvenance())) {
            setIdTiers(string);
        } else {
            setIdTiers(null);
        }
    }

    /**
     * setter pour l'attribut nbr enfants debut droit
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNbrEnfantsDebutDroit(String string) {
        nbrEnfantsDebutDroit = string;
    }

    /**
     * setter pour l'attribut nombre enfants
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNombreEnfants(String string) {
        nombreEnfants = string;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setProvenance(java.lang.String)
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setProvenance(String string) {
        provenance = string;
    }
}
