/*
 * Créé le 13 sept. 05
 */
package globaz.ij.vb.prononces;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.commons.nss.NSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSpy;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.prononces.*;
import globaz.ij.regles.IJPrononceRegles;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.INSSViewBean;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.Vector;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJRequerantViewBean extends IJPrononce implements FWViewBeanInterface, INSSViewBean {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String csCantonDomicile = "";

    private String csNationalite = "";
    private String csSexe = "";
    private String dateNaissance = "";
    private String idAssure = "";
    private boolean modifie = false;
    private boolean noAVSmodifieDepuisDerniereRecherche = false;

    // private String noAVS = "";
    private String nom = "";
    private String nss = "";
    private String prenom = "";

    private String provenance = "";
    private boolean trouveDansCI = false;
    private boolean trouveDansTiers = false;

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param nomComplet
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public boolean extraireNomPrenomCI(String nomComplet) throws Exception {
        if (!JadeStringUtil.isEmpty(nomComplet)) {
            nomComplet = PRStringUtils.toWordsFirstLetterUppercase(nomComplet);

            int pos = nomComplet.indexOf(',');

            if (pos > 0) {
                // on tente de splitter le nom complet en se basant sur la
                // virgule
                setNom(nomComplet.substring(0, pos).trim());
                setPrenom(nomComplet.substring(pos + 1).trim());
            } else {
                // si pas de virgule, on met tout dans le champ nom
                setNom(nomComplet);
            }

            trouveDansCI = true;
        } else {
            trouveDansCI = false;
        }

        return trouveDansCI;
    }

    /**
     * getter pour l'attribut cs canton.
     * 
     * @return la valeur courante de l'attribut cs canton
     */
    @Override
    public String getCsCantonDomicile() {
        if (JadeStringUtil.isEmpty(csCantonDomicile)) {
            csCantonDomicile = getProprieteTiers(PRTiersWrapper.PROPERTY_ID_CANTON);
            if (JadeStringUtil.isEmpty(csCantonDomicile)) {
                try {
                    csCantonDomicile = CaisseHelperFactory.getInstance().getCsDefaultCantonCaisse(
                            getSession().getApplication());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return csCantonDomicile;
    }

    /**
     * (non-Javadoc).
     * 
     * @return la valeur courante de l'attribut cs etat civil
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getCsEtatCivil()
     */
    @Override
    public String getCsEtatCivil() {
        // TODO Raccord de méthode auto-généré
        return null;
    }

    /**
     * getter pour l'attribut cs nationalite.
     * 
     * @return la valeur courante de l'attribut cs nationalite
     */
    @Override
    public String getCsNationalite() {
        if (JadeStringUtil.isEmpty(csNationalite)) {
            csNationalite = getProprieteTiers(PRTiersWrapper.PROPERTY_ID_PAYS);
        }

        return csNationalite;
    }

    /**
     * (non-Javadoc).
     * 
     * @return la valeur courante de l'attribut cs sexe
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getCsSexe()
     */
    @Override
    public String getCsSexe() {
        if (JadeStringUtil.isEmpty(csSexe)) {
            csSexe = getProprieteTiers(PRTiersWrapper.PROPERTY_SEXE);
        }
        return csSexe;
    }

    /**
     * (non-Javadoc).
     * 
     * @return la valeur courante de l'attribut date deces
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getDateDeces()
     */
    @Override
    public String getDateDeces() {
        // TODO Raccord de méthode auto-généré
        return null;
    }

    /**
     * (non-Javadoc).
     * 
     * @return la valeur courante de l'attribut date naissance
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getDateNaissance()
     */
    @Override
    public String getDateNaissance() {
        if (JadeStringUtil.isEmpty(dateNaissance)) {
            dateNaissance = getProprieteTiers(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);
        }
        return dateNaissance;
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les détails
     * 
     * @return le détail du requérant formaté
     * @throws Exception
     */
    public String getDetailRequerantDetail() throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), getNss());

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

            return PRNSSUtil.formatDetailRequerantDetail(getNss(), tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }
    }

    /**
     * (non-Javadoc).
     * 
     * @return la valeur courante de l'attribut id assure
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getIdAssure()
     */
    @Override
    public String getIdAssure() {
        return idAssure;
    }

    // /**
    // * getter pour l'attribut no AVS
    // *
    // * @return la valeur courante de l'attribut no AVS
    // */
    // public String getNoAVS() {
    // if (JAUtil.isStringEmpty(noAVS)) {
    // noAVS = getProprieteTiers(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
    // }
    //
    // return noAVS;
    // }

    /**
     * getter pour l'attribut Id Gestionnaire.
     * 
     * @return la valeur courante de l'attribut Id Gestionnaire
     */
    @Override
    public String getIdGestionnaire() {
        if (JadeStringUtil.isEmpty(super.getIdGestionnaire())) {
            setIdGestionnaire(getSession().getUserId());
        }

        return super.getIdGestionnaire();
    }

    /**
     * getter pour l'attribut nom.
     * 
     * @return la valeur courante de l'attribut nom
     */
    @Override
    public String getNom() {
        if (JadeStringUtil.isEmpty(nom)) {
            nom = getProprieteTiers(PRTiersWrapper.PROPERTY_NOM);
        }

        return nom;
    }

    /**
     * (non-Javadoc).
     * 
     * @return la valeur courante de l'attribut nss
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getNss()
     */
    @Override
    public String getNss() {
        if (JadeStringUtil.isEmpty(nss)) {
            nss = getProprieteTiers(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        }

        return nss;
    }

    /**
     * Méthode qui retourne le NNSS formaté sans le préfixe (756.) ou alors le NSS normal
     * 
     * @return NNSS formaté sans préfixe ou NSS normal
     */
    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(getNss(), isNNSS().equals("true") ? true : false);
    }

    /**
     * getter pour l'attribut pays data.
     * 
     * @return la valeur courante de l'attribut pays data
     */
    public Vector getPaysData() {
        try {
            return PRTiersHelper.getPays(getISession());
        } catch (Exception e) {
            e.printStackTrace();

            return new Vector();
        }
    }

    /**
     * getter pour l'attribut prenom.
     * 
     * @return la valeur courante de l'attribut prenom
     */
    @Override
    public String getPrenom() {
        if (JadeStringUtil.isEmpty(prenom)) {
            prenom = getProprieteTiers(PRTiersWrapper.PROPERTY_PRENOM);
        }

        return prenom;
    }

    /**
     * getter pour l'attribut propriete du tiers lié à la demande liée à ce droit.
     * 
     * @param nom
     *            le nom d'une propriété (PRCiWrapper)
     * 
     * @return la valeur courante de l'attribut propriete tiers ou chaine vide
     */
    private String getProprieteTiers(String nom) {
        String retValue = "";

        if (!JadeStringUtil.isIntegerEmpty(getIdDemande())) {
            try {
                PRDemande demande = loadDemande(null);

                if (!JadeStringUtil.isIntegerEmpty(demande.getIdTiers())) {
                    retValue = demande.loadTiers().getProperty(nom);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // pas pu charger la demande ou le tiers
            }
        }

        return retValue;
    }

    /**
     * (non-Javadoc).
     * 
     * @return la valeur courante de l'attribut provenance
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getProvenance()
     */
    @Override
    public String getProvenance() {
        return provenance;
    }

    @Override
    public BSpy getSpy() {
        try {
            IJPrononce ij = null;

            if ("null".equals(getIdPrononce()) || JadeStringUtil.isBlankOrZero(getIdPrononce())) {
                return new BSpy(getSession());
            }

            if (super.getCsTypeIJ().equals(IIJPrononce.CS_GRANDE_IJ)) {
                ij = new IJGrandeIJ();
            } else if (super.getCsTypeIJ().equals(IIJPrononce.CS_PETITE_IJ)) {
                ij = new IJPetiteIJ();
            } else if (super.getCsTypeIJ().equals(IIJPrononce.CS_ALLOC_INIT_TRAVAIL)) {
                ij = new IJPrononceAit();
            } else if (super.getCsTypeIJ().equals(IIJPrononce.CS_ALLOC_ASSIST)) {
                ij = new IJPrononceAllocAssistance();
            } else if (super.getCsTypeIJ().equals(IIJPrononce.CS_FPI)) {
                ij = new IJFpi();
            }

            ij.setSession(getSession());
            ij.setIdPrononce(getIdPrononce());
            ij.retrieve();
            if (!ij.isNew()) {
                return ij.getSpy();
            } else {
                return new BSpy(getSession());
            }

        } catch (Exception e) {
            return new BSpy(getSession());
        }
    }

    /**
     * getter pour l'attribut ti pays.
     * 
     * @return la valeur courante de l'attribut ti pays
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public Vector getTiPays() throws Exception {
        return PRTiersHelper.getPays(getSession());
    }

    /**
     * getter pour l'attribut modifie.
     * 
     * @return la valeur courante de l'attribut modifie
     */
    public boolean isModifie() {
        return modifie;
    }

    /**
     * getter pour l'attribut modifier permis.
     * 
     * @return la valeur courante de l'attribut modifier permis
     */
    public boolean isModifierPermis() {
        return IJPrononceRegles.isModifierPermis(this);
    }

    /**
     * Méthode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     * 
     * @return String (true ou false)
     */
    public String isNNSS() {

        if (JadeStringUtil.isBlankOrZero(getNss())) {
            return "";
        }

        if (getNss().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * getter pour l'attribut no AVSmodifie depuis derniere recherche.
     * 
     * @return la valeur courante de l'attribut no AVSmodifie depuis derniere recherche
     */
    public boolean isNoAVSmodifieDepuisDerniereRecherche() {
        return noAVSmodifieDepuisDerniereRecherche;
    }

    /**
     * getter pour l'attribut trouve dans CI.
     * 
     * @return la valeur courante de l'attribut trouve dans CI
     */
    public boolean isTrouveDansCI() {
        return trouveDansCI;
    }

    /**
     * getter pour l'attribut trouve dans tiers.
     * 
     * @return la valeur courante de l'attribut trouve dans tiers
     */
    public boolean isTrouveDansTiers() {
        return trouveDansTiers;
    }

    /**
     */
    public void resetFlags() {
        trouveDansCI = false;
        noAVSmodifieDepuisDerniereRecherche = false;
    }

    // /** efface toutes les informations relatives au tiers. */
    // public void resetTiers() {
    // trouveDansCI = false;
    // nom = prenom = "";
    // csCanton = "";
    // csNationalite = "";
    // }
    //
    /**
     * setter pour l'attribut cs canton.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setCsCantonDomicile(String string) {
        csCantonDomicile = string;
    }

    /**
     * (non-Javadoc).
     * 
     * @param s
     *            une nouvelle valeur pour cet attribut
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setCsEtatCivil(java.lang.String)
     */
    @Override
    public void setCsEtatCivil(String s) {
        // TODO Raccord de méthode auto-généré
    }

    /**
     * setter pour l'attribut cs nationalite.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setCsNationalite(String string) {
        csNationalite = string;
    }

    // /**
    // * setter pour l'attribut no AVS
    // *
    // * @param string une nouvelle valeur pour cet attribut
    // */
    // public void setNoAVS(String string) {
    // noAVSmodifieDepuisDerniereRecherche = noAVSmodifieDepuisDerniereRecherche
    // || !noAVS.equals(string);
    // noAVS = string;
    // }

    /**
     * (non-Javadoc).
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setCsSexe(java.lang.String)
     */
    @Override
    public void setCsSexe(String string) {
        csSexe = string;
    }

    /**
     * (non-Javadoc).
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setDateDeces(java.lang.String)
     */
    @Override
    public void setDateDeces(String string) {
        // TODO Raccord de méthode auto-généré
    }

    /**
     * (non-Javadoc).
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setDateNaissance(java.lang.String)
     */
    @Override
    public void setDateNaissance(String string) {
        dateNaissance = string;
    }

    /**
     * (non-Javadoc).
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setIdAssure(java.lang.String)
     */
    @Override
    public void setIdAssure(String string) {
        idAssure = string;
    }

    /**
     * setter pour l'attribut modifie.
     * 
     * @param modifie
     *            une nouvelle valeur pour cet attribut
     */
    public void setModifie(boolean modifie) {
        this.modifie = modifie;
    }

    /**
     * setter pour l'attribut nom.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setNom(String string) {
        nom = string;
    }

    /**
     * (non-Javadoc).
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setNss(java.lang.String)
     */
    @Override
    public void setNss(String string) {
        noAVSmodifieDepuisDerniereRecherche = noAVSmodifieDepuisDerniereRecherche || !nss.equals(string);
        nss = string;
    }

    /**
     * setter pour l'attribut prenom.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setPrenom(String string) {
        prenom = string;
    }

    /**
     * (non-Javadoc).
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setProvenance(java.lang.String)
     */
    @Override
    public void setProvenance(String string) {
        provenance = string;

        if (PRUtil.PROVENANCE_TIERS.equals(string)) {
            trouveDansCI = false;
            trouveDansTiers = true;
        } else if (PRUtil.PROVENANCE_CI.equals(string)) {
            trouveDansCI = true;
            trouveDansTiers = false;
        } else {
            trouveDansCI = false;
            trouveDansTiers = false;
        }
    }

    /**
     * setter pour l'attribut trouve dans CI.
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setTrouveDansCI(boolean b) {
        trouveDansCI = b;
    }

    /**
     * setter pour l'attribut trouve dans tiers.
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setTrouveDansTiers(boolean b) {
        trouveDansTiers = b;
    }
}
