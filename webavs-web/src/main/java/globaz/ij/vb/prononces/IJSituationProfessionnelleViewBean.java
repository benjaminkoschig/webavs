/*
 * Créé le 28 sept. 05
 */
package globaz.ij.vb.prononces;

import java.util.Vector;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JANumberFormatter;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJSituationProfessionnelle;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRImagesConstants;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * <H1>Description</H1>
 *
 * @author dvh
 */
public class IJSituationProfessionnelleViewBean extends IJSituationProfessionnelle implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final String ERREUR_EMPLOYEUR_INTROUVABLE = "EMPLOYEUR_INTROUVABLE";

    private static final Vector MENU_DEROULANT_BLANK = new Vector();

    private static final Object[] METHODES_SEL_EMPLOYEUR = new Object[] {
            new String[] { "idTiersEmployeurDepuisPyxis", "idTiers" }, new String[] { "nomEmployeur", "nom" },
            new String[] { "numAffilieEmployeur", "numAffilieActuel" } };

    /**
     * Retourne les données d'un objet de type TIAdministrationAdresse
     */
    private static final Object[] METHODES_SEL_EMPLOYEUR_ADDMINISTRATION = new Object[] {
            new String[] { "idTiersEmployeurDepuisPyxis", "idTiers" }, new String[] { "nomEmployeur", "nom" } };

    static {
        MENU_DEROULANT_BLANK.add(new String[] { "", "" });
    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Vector affiliationsEmployeur;
    private String csTypeIJ = "";
    private String dateDebutPrononce = "";

    // Setté à true lors de la recherche d'un affilié directement par sont no
    // (action : rechercerAffilie)
    private boolean isActionRechercherAffilie = false;
    // menu deroulant departement
    private Vector menuDepartement = MENU_DEROULANT_BLANK;
    private boolean modifierPermis;
    // infos relatives au prononce
    private String noAVS = "";

    // champ relatifs a l'employeur
    private String nomEmployeur = "";
    private String numAffilieEmployeur = "";

    private String prenomNom = "";

    private transient IJPrononce prononce;

    // champs supplementaires utilises pour la gestion du choix de l'employeur
    // avec Pyxis
    private boolean retourDepuisPyxis;

    // boolean supplementaire utilise pour la gestion de reprise de l'employeur
    // par numero d'affilie
    private boolean retourDesTiers = false;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut affiliations employeur.
     *
     * @return la valeur courante de l'attribut affiliations employeur
     */
    public Vector getAffiliationsEmployeur() {
        if (affiliationsEmployeur == null) {
            return MENU_DEROULANT_BLANK;
        }

        return affiliationsEmployeur;
    }

    /**
     * getter pour l'attribut autre remuneration.
     *
     * @return la valeur courante de l'attribut autre remuneration
     */
    @Override
    public String getAutreRemuneration() {
        // return JANumberFormatter.format(super.getAutreRemuneration(), 0.05,
        // 2, JANumberFormatter.NEAR);
        // on oublie l'arrondi, car dans le cas d'un pourcentage, incohérence
        // des données
        return super.getAutreRemuneration();

    }

    /**
     * getter pour l'attribut cs type IJ.
     *
     * @return la valeur courante de l'attribut cs type IJ
     */
    public String getCsTypeIJ() {
        return csTypeIJ;
    }

    /**
     * getter pour l'attribut date debut prononce.
     *
     * @return la valeur courante de l'attribut date debut prononce
     */
    public String getDateDebutPrononce() {
        return dateDebutPrononce;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getDetailRequerant() throws Exception {

        if (JadeStringUtil.isBlankOrZero(getNoAVS()) || "null".equals(getNoAVS())) {

            PRTiersWrapper tier = PRTiersHelper.getTiersParId(getSession(),
                    loadPrononce().loadDemande(getSession().getCurrentThreadTransaction()).getIdTiers());

            if (tier != null) {
                setNoAVS(tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
            }

        }

        PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), getNoAVS());

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

            return PRNSSUtil.formatDetailRequerantDetail(
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }

    }

    /**
     * getter pour l'attribut id affilie employeur.
     *
     * @return la valeur courante de l'attribut id affilie employeur
     */
    public String getIdAffilieEmployeur() {
        try {
            return loadEmployeur().getIdAffilie();
        } catch (Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);

            return "";
        }
    }

    /**
     * getter pour l'attribut id particularite employeur.
     *
     * @return la valeur courante de l'attribut id particularite employeur
     */
    public String getIdParticulariteEmployeur() {
        try {
            return loadEmployeur().getIdParticularite();
        } catch (Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);

            return "";
        }
    }

    /**
     * getter pour l'attribut id tiers employeur.
     *
     * @return la valeur courante de l'attribut id tiers employeur
     */
    public String getIdTiersEmployeur() {
        try {
            return loadEmployeur().getIdTiers();
        } catch (Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);

            return "";
        }
    }

    /**
     * getter pour l'attribut versement assure.
     *
     * @return la valeur courante de l'attribut versement assure
     */
    public String getImageVersementAssure() {
        if (getVersementEmployeur().booleanValue()) {
            return PRImagesConstants.IMAGE_ERREUR;
        } else {
            return PRImagesConstants.IMAGE_OK;
        }
    }

    /**
     * getter pour l'attribut menu departement.
     *
     * @return la valeur courante de l'attribut menu departement
     */
    public Vector getMenuDepartement() {
        return menuDepartement;
    }

    /**
     * getter pour l'attribut methodes selection employeur.
     *
     * @return la valeur courante de l'attribut methodes selection employeur
     */
    public Object[] getMethodesSelecteurEmployeur() {
        return METHODES_SEL_EMPLOYEUR;
    }

    public Object[] getMethodesSelecteurEmployeurAdministration() {
        return METHODES_SEL_EMPLOYEUR_ADDMINISTRATION;
    }

    /**
     * getter pour l'attribut no AVSAssure.
     *
     * @return la valeur courante de l'attribut no AVSAssure
     */
    public String getNoAVS() {
        return noAVS;
    }

    /**
     * getter pour l'attribut nom employeur.
     *
     * @return la valeur courante de l'attribut nom employeur
     */
    public String getNomEmployeur() {
        if (JadeStringUtil.isEmpty(nomEmployeur)) {
            try {
                nomEmployeur = loadEmployeur().loadNom();
            } catch (Exception e) {
                _addError(getSession().getCurrentThreadTransaction(),
                        getSession().getLabel(ERREUR_EMPLOYEUR_INTROUVABLE));
            }
        }

        return nomEmployeur;
    }

    /**
     * getter pour l'attribut num affilie.
     *
     * @return la valeur courante de l'attribut num affilie
     */
    public String getNumAffilieEmployeur() {
        if (JadeStringUtil.isEmpty(numAffilieEmployeur)) {
            try {
                IPRAffilie affilie = loadEmployeur().loadAffilie();

                if (affilie != null) {
                    numAffilieEmployeur = affilie.getNumAffilie();
                }
            } catch (Exception e) {
                _addError(getSession().getCurrentThreadTransaction(),
                        getSession().getLabel(ERREUR_EMPLOYEUR_INTROUVABLE));
            }
        }

        return numAffilieEmployeur;
    }

    /**
     * getter pour l'attribut prenom nom assure.
     *
     * @return la valeur courante de l'attribut prenom nom assure
     */
    public String getPrenomNom() {
        return prenomNom;
    }

    /**
     * getter pour l'attribut salaire.
     *
     * @return la valeur courante de l'attribut salaire
     */
    @Override
    public String getSalaire() {
        return JANumberFormatter.format(super.getSalaire(), 0.05, 2, JANumberFormatter.NEAR);
    }

    /**
     * getter pour l'attribut salaire nature.
     *
     * @return la valeur courante de l'attribut salaire nature
     */
    @Override
    public String getSalaireNature() {
        return JANumberFormatter.format(super.getSalaireNature(), 0.05, 2, JANumberFormatter.NEAR);
    }

    /**
     * @return
     */
    public boolean isActionRechercherAffilie() {
        return isActionRechercherAffilie;
    }

    /**
     * getter pour l'attribut modifier permis.
     *
     * @return la valeur courante de l'attribut modifier permis
     */
    public boolean isModifierPermis() {
        return modifierPermis;
    }

    /**
     * getter pour l'attribut retour depuis pyxis.
     *
     * @return la valeur courante de l'attribut retour depuis pyxis
     */
    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    /**
     * @return
     */
    public boolean isRetourDesTiers() {
        return retourDesTiers;
    }

    /**
     * @return DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJPrononce loadPrononce() throws Exception {
        if ((prononce == null) && !JadeStringUtil.isIntegerEmpty(getIdPrononce())) {
            prononce = IJPrononce.loadPrononce(getSession(), null, getIdPrononce(), csTypeIJ);
        }

        return prononce;
    }

    /**
     * @param b
     */
    public void setActionRechercherAffilie(boolean b) {
        isActionRechercherAffilie = b;
    }

    /**
     * setter pour l'attribut affiliations employeur.
     *
     * @param affiliationsEmployeur
     *            une nouvelle valeur pour cet attribut
     */
    public void setAffiliationsEmployeur(Vector affiliationsEmployeur) {
        if (affiliationsEmployeur != null) {
            this.affiliationsEmployeur = affiliationsEmployeur;
            affiliationsEmployeur.addAll(0, MENU_DEROULANT_BLANK);
        } else {
            this.affiliationsEmployeur = MENU_DEROULANT_BLANK;
        }
    }

    /**
     * setter pour l'attribut autre remuneration.
     *
     * @param autreRemuneration
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setAutreRemuneration(String autreRemuneration) {
        super.setAutreRemuneration(JANumberFormatter.deQuote(autreRemuneration));
    }

    /**
     * setter pour l'attribut cs type IJ.
     *
     * @param csTypeIJ
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeIJ(String csTypeIJ) {
        this.csTypeIJ = csTypeIJ;
    }

    /**
     * setter pour l'attribut date debut prononce.
     *
     * @param dateDebutPrononce
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutPrononce(String dateDebutPrononce) {
        this.dateDebutPrononce = dateDebutPrononce;
    }

    /**
     * setter pour l'attribut id affilie employeur.
     *
     * @param idAffilieEmployeur
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdAffilieEmployeur(String idAffilieEmployeur) {
        try {
            loadEmployeur().setIdAffilie(idAffilieEmployeur);
        } catch (Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
        }
    }

    /**
     * setter pour l'attribut id particularite employeur.
     *
     * @param idParticulariteEmployeur
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdParticulariteEmployeur(String idParticulariteEmployeur) {
        try {
            loadEmployeur().setIdParticularite(idParticulariteEmployeur);
        } catch (Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
        }
    }

    /**
     * setter pour l'attribut id tiers employeur.
     *
     * @param idTiersEmployeur
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiersEmployeur(String idTiersEmployeur) {
        try {
            loadEmployeur().setIdTiers(idTiersEmployeur);
        } catch (Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
        }
    }

    /**
     * setter pour l'attribut id tiers employeur depuis pyxis.
     *
     * <p>
     * Note: cette methode renseigne le champ retourDepuisPyxis a vrai ce qui sera interprete dans l'action comme le
     * fait que l'on revient depuis pyxis, il est necessaire de reinitialiser ce champ a la fin.
     * </p>
     *
     * @param idTiersEmployeur
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiersEmployeurDepuisPyxis(String idTiersEmployeur) {
        setIdTiersEmployeur(idTiersEmployeur);
        setIdAffilieEmployeur("");
        numAffilieEmployeur = "";
        retourDepuisPyxis = true;
    }

    /**
     * set le menu deroulant transmis ou un menu deroulant vide si l'argument est nul.
     *
     * @param menuDepartement
     *            une nouvelle valeur pour cet attribut
     */
    public void setMenuDepartement(Vector menuDepartement) {
        if (menuDepartement == null) {
            this.menuDepartement = MENU_DEROULANT_BLANK;
        } else {
            this.menuDepartement = menuDepartement;
            menuDepartement.addAll(0, MENU_DEROULANT_BLANK);
        }
    }

    /**
     * setter pour l'attribut modifier permis.
     *
     * @param modifierPermis
     *            une nouvelle valeur pour cet attribut
     */
    public void setModifierPermis(boolean modifierPermis) {
        this.modifierPermis = modifierPermis;
    }

    /**
     * setter pour l'attribut no AVSAssure.
     *
     * @param noAVSAssure
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoAVS(String noAVSAssure) {
        noAVS = noAVSAssure;
    }

    /**
     * setter pour l'attribut nom employeur.
     *
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNomEmployeur(String string) {
        nomEmployeur = string;
    }

    /**
     * setter pour l'attribut num affilie employeur.
     *
     * @param numAffilieEmployeur
     *            une nouvelle valeur pour cet attribut
     */
    public void setNumAffilieEmployeur(String numAffilieEmployeur) {
        this.numAffilieEmployeur = numAffilieEmployeur;
    }

    /**
     * setter pour l'attribut prenom nom assure.
     *
     * @param prenomNomAssure
     *            une nouvelle valeur pour cet attribut
     */
    public void setPrenomNom(String prenomNomAssure) {
        prenomNom = prenomNomAssure;
    }

    /**
     * setter pour l'attribut retour depuis pyxis.
     *
     * @param retourDepuisPyxis
     *            une nouvelle valeur pour cet attribut
     */
    public void setRetourDepuisPyxis(boolean retourDepuisPyxis) {
        this.retourDepuisPyxis = retourDepuisPyxis;
    }

    /**
     * @param b
     */
    public void setRetourDesTiers(boolean b) {
        retourDesTiers = b;
    }

    /**
     * setter pour l'attribut salaire.
     *
     * @param salaire
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setSalaire(String salaire) {
        super.setSalaire(JANumberFormatter.deQuote(salaire));
    }

    /**
     * setter pour l'attribut salaire nature.
     *
     * @param salaireNature
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setSalaireNature(String salaireNature) {
        super.setSalaireNature(JANumberFormatter.deQuote(salaireNature));
    }

}
