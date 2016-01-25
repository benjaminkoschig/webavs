/*
 * Créé le 6 mars 07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.pavo.db.inscriptions.declaration;

import globaz.globall.db.BSession;

/**
 * @author JMC
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CIDeclarationParametre {
    public static String CS_ANNEE = "328005";
    public static String CS_AVEC_CTS = "328011";
    public static String CS_AVEC_POINT = "328007";
    public static String CS_GENRE = "328014";
    public static String CS_JOUR_DEBUT = "328009";
    public static String CS_JOUR_FIN = "328010";
    public static String CS_MOIS_DEBUT = "328003";
    public static String CS_MOIS_FIN = "328004";
    public static String CS_MONTANT = "328006";
    public static String CS_NEGATIF = "328008";
    public static String CS_NO_AFF_SUITE = "328002";
    public static String CS_NO_AFFILIE = "328001";
    public static String CS_NO_AVS = "328000";
    public static String CS_NOM_PRENOM = "328012";
    public static String CS_STARTS_WITH = "328013";

    private String anneeDebut = "";
    private String anneeFin = "";

    private String avecCentime = "";
    private String avecPoint = "";

    private String genreDebut = "";
    private String genreFin = "";

    private String jourDebutDebut = "";
    private String jourDebutFin = "";

    private String jourFinDebut = "";
    private String jourFinFin = "";

    private String moisDebutDebut = "";
    private String moisDebutFin = "";

    private String moisFinDebut = "";
    private String moisFinFin = "";

    private String montantDebut = "";
    private String montantFin = "";

    private String negatif = "";

    private String negatifValue = "";

    private String noAffillieDebut = "";

    private String noAffillieFin = "";

    private String noAffillieSuiteDebut = "";
    private String noAffillieSuiteFin = "";

    private String noAvsDebut = "";

    private String noAvsFin = "";

    private String nomPrenomDebut = "";
    private String nomPrenomFin = "";
    private String startsWithDebut = "";
    private String startsWithFin = "";

    /**
     * @return
     */
    public String getAnneeDebut() {
        return anneeDebut;
    }

    /**
     * @return
     */
    public String getAnneeFin() {
        return anneeFin;
    }

    /**
     * @return
     */
    public String getAvecCentime() {
        return avecCentime;
    }

    /**
     * @return
     */
    public String getAvecPoint() {
        return avecPoint;
    }

    public String getGenreDebut() {
        return genreDebut;
    }

    public String getGenreFin() {
        return genreFin;
    }

    /**
     * @return
     */
    public String getJourDebutDebut() {
        return jourDebutDebut;
    }

    /**
     * @return
     */
    public String getJourDebutFin() {
        return jourDebutFin;
    }

    /**
     * @return
     */
    public String getJourFinDebut() {
        return jourFinDebut;
    }

    /**
     * @return
     */
    public String getJourFinFin() {
        return jourFinFin;
    }

    /**
     * @return
     */
    public String getMoisDebutDebut() {
        return moisDebutDebut;
    }

    /**
     * @return
     */
    public String getMoisDebutFin() {
        return moisDebutFin;
    }

    /**
     * @return
     */
    public String getMoisFinDebut() {
        return moisFinDebut;
    }

    /**
     * @return
     */
    public String getMoisFinFin() {
        return moisFinFin;
    }

    /**
     * @return
     */
    public String getMontantDebut() {
        return montantDebut;
    }

    /**
     * @return
     */
    public String getMontantFin() {
        return montantFin;
    }

    /**
     * @return
     */
    public String getNegatif() {
        return negatif;
    }

    /**
     * @return
     */
    public String getNegatifValue() {
        return negatifValue;
    }

    /**
     * @return
     */
    public String getNoAffillieDebut() {
        return noAffillieDebut;
    }

    /**
     * @return
     */
    public String getNoAffillieFin() {
        return noAffillieFin;
    }

    /**
     * @return
     */
    public String getNoAffillieSuiteDebut() {
        return noAffillieSuiteDebut;
    }

    /**
     * @return
     */
    public String getNoAffillieSuiteFin() {
        return noAffillieSuiteFin;
    }

    /**
     * @return
     */
    public String getNoAvsDebut() {
        return noAvsDebut;
    }

    /**
     * @return
     */
    public String getNoAvsFin() {
        return noAvsFin;
    }

    /**
     * @return
     */
    public String getNomPrenomDebut() {
        return nomPrenomDebut;
    }

    /**
     * @return
     */
    public String getNomPrenomFin() {
        return nomPrenomFin;
    }

    /**
     * @return
     */
    public String getStartsWithDebut() {
        return startsWithDebut;
    }

    /**
     * @return
     */
    public String getStartsWithFin() {
        return startsWithFin;
    }

    /**
     * Méthode qui permet d'initialiser le paramétrage pour l'importation des déclarations de salaires
     * 
     * @param session
     * @param typeImport
     * @throws Exception
     */
    public void init(BSession session, String typeImport) throws Exception {
        /**
         * Numéro AVS
         */
        CIDeclarationParametrageManager decMgr = new CIDeclarationParametrageManager();
        CIDeclarationParametrage param = new CIDeclarationParametrage();
        decMgr.setSession(session);
        decMgr.setForTypeImport(typeImport);
        decMgr.setForTypeChamp(CIDeclarationParametre.CS_NO_AVS);
        decMgr.find();
        if (decMgr.size() > 0) {
            param = (CIDeclarationParametrage) decMgr.getFirstEntity();
            noAvsDebut = param.getDebut();
            noAvsFin = param.getFin();
        }
        /**
         * Numéro d'affillié
         */

        decMgr = new CIDeclarationParametrageManager();
        param = new CIDeclarationParametrage();
        decMgr.setSession(session);
        decMgr.setForTypeImport(typeImport);
        decMgr.setForTypeChamp(CIDeclarationParametre.CS_NO_AFFILIE);
        decMgr.find();
        if (decMgr.size() > 0) {
            param = (CIDeclarationParametrage) decMgr.getFirstEntity();
            noAffillieDebut = param.getDebut();
            noAffillieFin = param.getFin();
        }
        /**
         * Numéro d'affilié suite
         */
        decMgr = new CIDeclarationParametrageManager();
        param = new CIDeclarationParametrage();
        decMgr.setSession(session);
        decMgr.setForTypeImport(typeImport);
        decMgr.setForTypeChamp(CIDeclarationParametre.CS_NO_AFF_SUITE);
        decMgr.find();
        if (decMgr.size() > 0) {
            param = (CIDeclarationParametrage) decMgr.getFirstEntity();
            noAffillieSuiteDebut = param.getDebut();
            noAffillieSuiteFin = param.getFin();
        }
        /**
         * Mois début
         */

        decMgr = new CIDeclarationParametrageManager();
        param = new CIDeclarationParametrage();
        decMgr.setSession(session);
        decMgr.setForTypeImport(typeImport);
        decMgr.setForTypeChamp(CIDeclarationParametre.CS_MOIS_DEBUT);
        decMgr.find();
        if (decMgr.size() > 0) {
            param = (CIDeclarationParametrage) decMgr.getFirstEntity();
            moisDebutDebut = param.getDebut();
            moisDebutFin = param.getFin();
        }
        /**
         * Mois fin
         */

        decMgr = new CIDeclarationParametrageManager();
        param = new CIDeclarationParametrage();
        decMgr.setSession(session);
        decMgr.setForTypeImport(typeImport);
        decMgr.setForTypeChamp(CIDeclarationParametre.CS_MOIS_FIN);
        decMgr.find();
        if (decMgr.size() > 0) {
            param = (CIDeclarationParametrage) decMgr.getFirstEntity();
            moisFinDebut = param.getDebut();
            moisFinFin = param.getFin();
        }
        /**
         * Année cotisation
         */
        decMgr = new CIDeclarationParametrageManager();
        param = new CIDeclarationParametrage();
        decMgr.setSession(session);
        decMgr.setForTypeImport(typeImport);
        decMgr.setForTypeChamp(CIDeclarationParametre.CS_ANNEE);
        decMgr.find();
        if (decMgr.size() > 0) {
            param = (CIDeclarationParametrage) decMgr.getFirstEntity();
            anneeDebut = param.getDebut();
            anneeFin = param.getFin();
        }
        /**
         * genre
         */
        decMgr = new CIDeclarationParametrageManager();
        param = new CIDeclarationParametrage();
        decMgr.setSession(session);
        decMgr.setForTypeImport(typeImport);
        decMgr.setForTypeChamp(CIDeclarationParametre.CS_GENRE);
        decMgr.find();
        if (decMgr.size() > 0) {
            param = (CIDeclarationParametrage) decMgr.getFirstEntity();
            genreDebut = param.getDebut();
            genreFin = param.getFin();
        }
        /**
         * Montant
         */
        decMgr = new CIDeclarationParametrageManager();
        param = new CIDeclarationParametrage();
        decMgr.setSession(session);
        decMgr.setForTypeImport(typeImport);
        decMgr.setForTypeChamp(CIDeclarationParametre.CS_MONTANT);
        decMgr.find();
        if (decMgr.size() > 0) {
            param = (CIDeclarationParametrage) decMgr.getFirstEntity();
            montantDebut = param.getDebut();
            montantFin = param.getFin();
        }
        /**
         * Avec cts
         */
        decMgr = new CIDeclarationParametrageManager();
        param = new CIDeclarationParametrage();
        decMgr.setSession(session);
        decMgr.setForTypeImport(typeImport);
        decMgr.setForTypeChamp(CIDeclarationParametre.CS_AVEC_CTS);
        decMgr.find();
        if (decMgr.size() > 0) {
            param = (CIDeclarationParametrage) decMgr.getFirstEntity();
            avecCentime = param.getDebut();
        }
        /**
         * Avec points
         */
        decMgr = new CIDeclarationParametrageManager();
        param = new CIDeclarationParametrage();
        decMgr.setSession(session);
        decMgr.setForTypeImport(typeImport);
        decMgr.setForTypeChamp(CIDeclarationParametre.CS_AVEC_POINT);
        decMgr.find();
        if (decMgr.size() > 0) {
            param = (CIDeclarationParametrage) decMgr.getFirstEntity();
            avecPoint = param.getDebut();
        }

        /**
         * Jour debut
         */
        decMgr = new CIDeclarationParametrageManager();
        param = new CIDeclarationParametrage();
        decMgr.setSession(session);
        decMgr.setForTypeImport(typeImport);
        decMgr.setForTypeChamp(CIDeclarationParametre.CS_JOUR_DEBUT);
        decMgr.find();
        if (decMgr.size() > 0) {
            param = (CIDeclarationParametrage) decMgr.getFirstEntity();
            jourDebutDebut = param.getDebut();
            jourDebutFin = param.getFin();
        }

        /**
         * Jour fin
         */
        decMgr = new CIDeclarationParametrageManager();
        param = new CIDeclarationParametrage();
        decMgr.setSession(session);
        decMgr.setForTypeImport(typeImport);
        decMgr.setForTypeChamp(CIDeclarationParametre.CS_JOUR_FIN);
        decMgr.find();
        if (decMgr.size() > 0) {
            param = (CIDeclarationParametrage) decMgr.getFirstEntity();
            jourFinDebut = param.getDebut();
            jourFinFin = param.getFin();
        }
        /**
         * Négatif
         */
        decMgr = new CIDeclarationParametrageManager();
        param = new CIDeclarationParametrage();
        decMgr.setSession(session);
        decMgr.setForTypeImport(typeImport);
        decMgr.setForTypeChamp(CIDeclarationParametre.CS_NEGATIF);
        decMgr.find();
        if (decMgr.size() > 0) {
            param = (CIDeclarationParametrage) decMgr.getFirstEntity();
            negatif = param.getDebut();
            negatifValue = param.getFin();
        }
        /**
         * Nom, prénom
         */
        decMgr = new CIDeclarationParametrageManager();
        param = new CIDeclarationParametrage();
        decMgr.setSession(session);
        decMgr.setForTypeImport(typeImport);
        decMgr.setForTypeChamp(CIDeclarationParametre.CS_NOM_PRENOM);
        decMgr.find();
        if (decMgr.size() > 0) {
            param = (CIDeclarationParametrage) decMgr.getFirstEntity();
            nomPrenomDebut = param.getDebut();
            nomPrenomFin = param.getFin();
        }

        /**
         * Starts with
         */
        decMgr = new CIDeclarationParametrageManager();
        param = new CIDeclarationParametrage();
        decMgr.setSession(session);
        decMgr.setForTypeImport(typeImport);
        decMgr.setForTypeChamp(CIDeclarationParametre.CS_STARTS_WITH);
        decMgr.find();
        if (decMgr.size() > 0) {
            param = (CIDeclarationParametrage) decMgr.getFirstEntity();
            nomPrenomDebut = param.getDebut();
            nomPrenomFin = param.getFin();
        }

    }

    /**
     * @param string
     */
    public void setAnneeDebut(String string) {
        anneeDebut = string;
    }

    /**
     * @param string
     */
    public void setAnneeFin(String string) {
        anneeFin = string;
    }

    /**
     * @param string
     */
    public void setAvecCentime(String string) {
        avecCentime = string;
    }

    /**
     * @param string
     */
    public void setAvecPoint(String string) {
        avecPoint = string;
    }

    public void setGenreDebut(String genreDebut) {
        this.genreDebut = genreDebut;
    }

    public void setGenreFin(String genreFin) {
        this.genreFin = genreFin;
    }

    /**
     * @param string
     */
    public void setJourDebutDebut(String string) {
        jourDebutDebut = string;
    }

    /**
     * @param string
     */
    public void setJourDebutFin(String string) {
        jourDebutFin = string;
    }

    /**
     * @param string
     */
    public void setJourFinDebut(String string) {
        jourFinDebut = string;
    }

    /**
     * @param string
     */
    public void setJourFinFin(String string) {
        jourFinFin = string;
    }

    /**
     * @param string
     */
    public void setMoisDebutDebut(String string) {
        moisDebutDebut = string;
    }

    /**
     * @param string
     */
    public void setMoisDebutFin(String string) {
        moisDebutFin = string;
    }

    /**
     * @param string
     */
    public void setMoisFinDebut(String string) {
        moisFinDebut = string;
    }

    /**
     * @param string
     */
    public void setMoisFinFin(String string) {
        moisFinFin = string;
    }

    /**
     * @param string
     */
    public void setMontantDebut(String string) {
        montantDebut = string;
    }

    /**
     * @param string
     */
    public void setMontantFin(String string) {
        montantFin = string;
    }

    /**
     * @param string
     */
    public void setNegatif(String string) {
        negatif = string;
    }

    /**
     * @param string
     */
    public void setNegatifValue(String string) {
        negatifValue = string;
    }

    /**
     * @param string
     */
    public void setNoAffillieDebut(String string) {
        noAffillieDebut = string;
    }

    /**
     * @param string
     */
    public void setNoAffillieFin(String string) {
        noAffillieFin = string;
    }

    /**
     * @param string
     */
    public void setNoAffillieSuiteDebut(String string) {
        noAffillieSuiteDebut = string;
    }

    /**
     * @param string
     */
    public void setNoAffillieSuiteFin(String string) {
        noAffillieSuiteFin = string;
    }

    /**
     * @param string
     */
    public void setNoAvsDebut(String string) {
        noAvsDebut = string;
    }

    /**
     * @param string
     */
    public void setNoAvsFin(String string) {
        noAvsFin = string;
    }

    /**
     * @param string
     */
    public void setNomPrenomDebut(String string) {
        nomPrenomDebut = string;
    }

    /**
     * @param string
     */
    public void setNomPrenomFin(String string) {
        nomPrenomFin = string;
    }

    /**
     * @param string
     */
    public void setStartsWithDebut(String string) {
        startsWithDebut = string;
    }

    /**
     * @param string
     */
    public void setStartsWithFin(String string) {
        startsWithFin = string;
    }

}
