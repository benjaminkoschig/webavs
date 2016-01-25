/*
 * Cr�� le 26 sept. 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.cepheus.vb.demande;

import globaz.cepheus.db.demande.DODemandePrestations;
import globaz.cepheus.db.demande.DODemandePrestationsManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.Vector;

/**
 * @author bsc
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class DODemandePrestationsViewBean extends DODemandePrestations implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String demandeApgId = "";

    private String demandeIjId = "";
    private String demandeMatId = "";
    private String demandeRentesId = "";
    private Boolean hasDemandeApg = null;

    private Boolean hasDemandeIj = null;
    private Boolean hasDemandeMat = null;
    private Boolean hasDemandeRentes = null;
    private String metaDossierIdDemandeApg = "";

    private String metaDossierIdDemandeIj = "";
    private String metaDossierIdDemandeMat = "";
    private String metaDossierIdDemandeRentes = "";
    private transient Vector orderBy = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     * @return
     */
    public String getDemandeApgId() {

        if (hasDemandeApg == null) {
            hasDemandeApg();
        }

        return demandeApgId;
    }

    /**
     * @return
     */
    public String getDemandeIjId() {

        if (hasDemandeIj == null) {
            hasDemandeIj();
        }

        return demandeIjId;
    }

    /**
     * @return
     */
    public String getDemandeMatId() {

        if (hasDemandeMat == null) {
            hasDemandeMat();
        }

        return demandeMatId;
    }

    /**
     * @return
     */
    public String getDemandeRentesId() {
        return demandeRentesId;
    }

    /**
     * M�thode qui retourne le d�tail du requ�rant format� pour les rcListes
     * 
     * @return le d�tail du requ�rant format�
     */
    public String getDetailRequerant() {

        return PRNSSUtil.formatDetailRequerantListe(getNoAvs(), getNom() + " " + getPrenom(), getDateNaissance(),
                getLibelleCourtSexe(), getLibellePays());

    }

    /**
     * M�thode qui retourne le d�tail du requ�rant format� pour les d�tails
     * 
     * @return le d�tail du requ�rant format�
     * @throws Exception
     */
    public String getDetailRequerantDetail() throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), getNoAvs());

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

            return PRNSSUtil.formatDetailRequerantDetail(getNoAvs(), getNomPrenom(),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }
    }

    /**
     * 
     * @return
     */
    public String getEtatDemandeLibelle() {
        return getSession().getCodeLibelle(getEtatDemande());
    }

    /**
     * M�thode qui retourne le libell� court du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libell� court du sexe (H ou F)
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
     * M�thode qui retourne le libell� de la nationalit� par rapport au csNationalit� qui est dans le vb
     * 
     * @return le libell� du pays (retourne une cha�ne vide si pays inconnu)
     */
    public String getLibellePays() {

        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", getCsNationalite())))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", getCsNationalite()));
        }

    }

    /**
     * M�thode qui retourne le libell� du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libell� du sexe (homme ou femme)
     */
    public String getLibelleSexe() {
        return getSession().getCodeLibelle(getCsSexe());
    }

    /**
     * @return
     */
    public String getMetaDossierIdDemandeApg() {

        if (hasDemandeApg == null) {
            hasDemandeApg();
        }

        return metaDossierIdDemandeApg;
    }

    /**
     * @return
     */
    public String getMetaDossierIdDemandeIj() {

        if (hasDemandeIj == null) {
            hasDemandeIj();
        }

        return metaDossierIdDemandeIj;
    }

    /**
     * @return
     */
    public String getMetaDossierIdDemandeMat() {

        if (hasDemandeMat == null) {
            hasDemandeMat();
        }

        return metaDossierIdDemandeMat;
    }

    /**
     * @return
     */
    public String getMetaDossierIdDemandeRentes() {
        return metaDossierIdDemandeRentes;
    }

    /**
     * @return
     */
    public String getNomPrenom() {
        return getNom() + " " + getPrenom();
    }

    /**
     * getter pour l'attribut order by data
     * 
     * @return la valeur courante de l'attribut order by data
     */
    public Vector getOrderByData() {
        if (orderBy == null) {
            orderBy = new Vector(2);
            orderBy.add(new String[] { FIELDNAME_NUM_AVS, getSession().getLabel("JSP_NSS") });
            orderBy.add(new String[] { FIELDNAME_NOM, getSession().getLabel("JSP_NOM") });
        }

        return orderBy;
    }

    /**
     * 
     * @return
     */
    public String getTypeDemandeLibelle() {
        return getSession().getCode(getTypeDemande());
    }

    /**
     * @return
     */
    public String getTypeEtatDemande() {
        return getIdDemande() + " - " + getTypeDemandeLibelle() + " - " + getEtatDemandeLibelle();
    }

    /**
     * Vrais si il existe une demande APG ouverte pour le tiers
     * 
     * @return
     */
    public Boolean hasDemandeApg() {

        if (hasDemandeApg == null) {

            if (!JadeStringUtil.isIntegerEmpty(getIdTiers())) {
                DODemandePrestationsManager manager = new DODemandePrestationsManager();
                manager.setSession(getSession());
                manager.setForIdTiers(getIdTiers());
                manager.setForTypeDemande(IPRDemande.CS_TYPE_APG);
                manager.setForEtatDemande(IPRDemande.CS_ETAT_OUVERT);
                manager.changeManagerSize(1);

                try {
                    manager.find();

                    if (manager.getSize() > 0) {
                        hasDemandeApg = Boolean.TRUE;
                        demandeApgId = ((DODemandePrestations) manager.get(0)).getIdDemande();

                        // on cherche l'id du meta dossier
                        PRDemande demande = new PRDemande();
                        demande.setSession(getSession());
                        demande.setIdDemande(demandeApgId);
                        demande.retrieve();
                        metaDossierIdDemandeApg = demande.getIdMetaDossier();
                    } else {
                        hasDemandeApg = Boolean.FALSE;
                    }

                } catch (Exception e) {
                    hasDemandeApg = Boolean.FALSE;
                }

            } else {
                hasDemandeApg = Boolean.FALSE;
            }
        }

        return hasDemandeApg;
    }

    /**
     * Vrais si il existe une demande IJ ouverte pour le tiers
     * 
     * @return
     */
    public Boolean hasDemandeIj() {

        if (hasDemandeIj == null) {

            if (!JadeStringUtil.isIntegerEmpty(getIdTiers())) {
                DODemandePrestationsManager manager = new DODemandePrestationsManager();
                manager.setSession(getSession());
                manager.setForIdTiers(getIdTiers());
                manager.setForTypeDemande(IPRDemande.CS_TYPE_IJ);
                manager.setForEtatDemande(IPRDemande.CS_ETAT_OUVERT);
                manager.changeManagerSize(1);

                try {
                    manager.find();

                    if (manager.getSize() > 0) {
                        hasDemandeIj = Boolean.TRUE;
                        demandeIjId = ((DODemandePrestations) manager.get(0)).getIdDemande();

                        // on cherche l'id du meta dossier
                        PRDemande demande = new PRDemande();
                        demande.setSession(getSession());
                        demande.setIdDemande(demandeIjId);
                        demande.retrieve();
                        metaDossierIdDemandeIj = demande.getIdMetaDossier();
                    } else {
                        hasDemandeIj = Boolean.FALSE;
                    }

                } catch (Exception e) {
                    hasDemandeIj = Boolean.FALSE;
                }

            } else {
                hasDemandeIj = Boolean.FALSE;
            }
        }

        return hasDemandeIj;
    }

    /**
     * Vrais si il existe une demande Mat ouverte pour le tiers
     * 
     * @return
     */
    public Boolean hasDemandeMat() {

        if (hasDemandeMat == null) {

            if (!JadeStringUtil.isIntegerEmpty(getIdTiers())) {
                DODemandePrestationsManager manager = new DODemandePrestationsManager();
                manager.setSession(getSession());
                manager.setForIdTiers(getIdTiers());
                manager.setForTypeDemande(IPRDemande.CS_TYPE_MATERNITE);
                manager.setForEtatDemande(IPRDemande.CS_ETAT_OUVERT);
                manager.changeManagerSize(1);

                try {
                    manager.find();

                    if (manager.getSize() > 0) {
                        hasDemandeMat = Boolean.TRUE;
                        demandeMatId = ((DODemandePrestations) manager.get(0)).getIdDemande();

                        // on cherche l'id du meta dossier
                        PRDemande demande = new PRDemande();
                        demande.setSession(getSession());
                        demande.setIdDemande(demandeMatId);
                        demande.retrieve();
                        metaDossierIdDemandeMat = demande.getIdMetaDossier();
                    } else {
                        hasDemandeMat = Boolean.FALSE;
                    }

                } catch (Exception e) {
                    hasDemandeMat = Boolean.FALSE;
                }

            } else {
                hasDemandeMat = Boolean.FALSE;
            }
        }

        return hasDemandeMat;
    }

    /**
     * Vrais si il existe une demande Rente ouverte pour le tiers
     * 
     * @return
     */
    public Boolean hasDemandeRentes() {

        if (hasDemandeRentes == null) {

            if (!JadeStringUtil.isIntegerEmpty(getIdTiers())) {
                DODemandePrestationsManager manager = new DODemandePrestationsManager();
                manager.setSession(getSession());
                manager.setForIdTiers(getIdTiers());
                manager.setForTypeDemande(IPRDemande.CS_TYPE_RENTE);
                manager.setForEtatDemande(IPRDemande.CS_ETAT_OUVERT);
                manager.changeManagerSize(1);

                try {
                    manager.find();

                    if (manager.getSize() > 0) {
                        hasDemandeRentes = Boolean.TRUE;
                        demandeRentesId = ((DODemandePrestations) manager.get(0)).getIdDemande();

                        // on cherche l'id du meta dossier
                        PRDemande demande = new PRDemande();
                        demande.setSession(getSession());
                        demande.setIdDemande(demandeIjId);
                        demande.retrieve();
                        metaDossierIdDemandeRentes = demande.getIdMetaDossier();
                    } else {
                        hasDemandeRentes = Boolean.FALSE;
                    }

                } catch (Exception e) {
                    hasDemandeRentes = Boolean.FALSE;
                }

            } else {
                hasDemandeRentes = Boolean.FALSE;
            }
        }

        return hasDemandeRentes;
    }

    /**
     * M�thode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     * 
     * @return String (true ou false)
     */
    public String isNNSS() {
        if (getNoAvs().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

}
