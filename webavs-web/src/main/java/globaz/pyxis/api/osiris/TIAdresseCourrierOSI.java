package globaz.pyxis.api.osiris;

import globaz.globall.parameters.FWParametersUserCode;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.external.IntAdresseCourrier;
import globaz.pyxis.db.adressecourrier.TIAdresse;
import globaz.pyxis.db.adressecourrier.TILocalite;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.tiers.TITiers;

/**
 * Insérez la description du type ici. Date de création : (07.02.2003 13:26:17)
 * 
 * @author: Administrator
 */
/**
 * @author sch
 */
public class TIAdresseCourrierOSI implements IntAdresseCourrier {
    private TIAdresse adresse = new TIAdresse();
    private TILocalite localite = new TILocalite();
    private TIPays pays = new TIPays();

    /**
     * Commentaire relatif au constructeur TIAdresseCourrierOSI.
     */
    public TIAdresseCourrierOSI() {
        super();
    }

    /**
     * Cette méthode retourne l'adresse de courrier d'un tiers.
     * 
     * @return java.lang.String[]
     */
    @Override
    public java.lang.String[] getAdresse() {
        String[] adr = new String[3];
        adr[0] = adresse.getLigneAdresse2();
        adr[1] = adresse.getLigneAdresse3();
        adr[2] = adresse.getLigneAdresse4();
        return adr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.external.IntAdresseCourrier#getAttention()
     */
    @Override
    public String getAttention() {
        return adresse.getAttention();
    }

    /**
     * Cette méthode retourne le nom du tiers
     * 
     * @return java.lang.String
     */
    @Override
    public String getAutreNom() {
        return adresse.getLigneAdresse1();
    }

    @Override
    public String getCasePostale() {
        return adresse.getCasePostale();
    }

    @Override
    public String getCivilite() {
        if (JadeStringUtil.isIntegerEmpty(adresse.getTitreAdresse())) {
            return "";
        } else {
            FWParametersUserCode code = new FWParametersUserCode();
            code.setSession(adresse.getSession());
            code.setIdCodeSysteme(adresse.getTitreAdresse());
            if (adresse.getLangueDocument().equals(TITiers.CS_FRANCAIS)) {
                code.setIdLangue("F");
            } else if (adresse.getLangueDocument().equals(TITiers.CS_ALLEMAND)) {
                code.setIdLangue("D");
            } else if (adresse.getLangueDocument().equals(TITiers.CS_ITALIEN)) {
                code.setIdLangue("I");
            } else {
                code.setIdLangue("F");
            }
            try {
                code.retrieve();
                if (code.isNew()) {
                    return "";
                } else {
                    return code.getLibelle();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
    }

    /**
     * Renvoie l'id unique de l'entité
     * 
     * @return l'id unique de l'entité
     */
    @Override
    public String getId() {
        return adresse.getId();
    }

    @Override
    public String getIdAdresseCourrier() {
        return adresse.getIdAdresse();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.external.IntAdresseCourrier#getIdCanton()
     */
    @Override
    public String getIdCanton() {
        return localite.getIdCanton();
    }

    /**
     * Renvoie la session en cours
     * 
     * @return la session en cours
     */
    @Override
    public globaz.globall.api.BISession getISession() {
        return adresse.getSession();
    }

    /**
     * Renvoie la date de dernière modification de l'objet (format DD.MM.YYYY).
     * 
     * @return la date de dernière modification de l'objet, null si pas disponible
     */
    @Override
    public final String getLastModifiedDate() {
        if (adresse.getSpy() == null) {
            return null;
        }
        return adresse.getSpy().getDate();
    }

    /**
     * Renvoie l'heure de dernière modification de l'objet (format HH:MM:SS).
     * 
     * @return l'heure de dernière modification de l'objet, null si pas disponible
     */
    @Override
    public final String getLastModifiedTime() {
        if (adresse.getSpy() == null) {
            return null;
        }
        return adresse.getSpy().getTime();
    }

    /**
     * Renvoie l'id du dernier utilisateur qui a modifié l'objet.
     * 
     * @return l'id du dernier utilisateur qui a modifié l'objet, null si pas disponible
     */
    @Override
    public final String getLastModifiedUser() {
        if (adresse.getSpy() == null) {
            return null;
        }
        return adresse.getSpy().getUser();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:35:16)
     * 
     * @return java.lang.String
     */
    @Override
    public String getLocalite() {
        return localite.getLocalite();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.external.IntAdresseCourrier#getNumCommuneOfs()
     */
    @Override
    public String getNumCommuneOfs() {
        return localite.getNumCommuneOfs();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:34:58)
     * 
     * @return java.lang.String
     */
    @Override
    public String getNumPostal() {
        return localite.getNumPostal();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:35:39)
     * 
     * @return java.lang.String
     */
    @Override
    public String getPays() {
        // todo: récupérer le pays
        return "Schweiz";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:36:10)
     * 
     * @return java.lang.String
     */
    @Override
    public String getPaysISO() {
        if (pays == null) {
            pays = new TIPays();
        }
        if (!localite.getIdPays().equalsIgnoreCase(pays.getIdPays())) {
            pays.setIdPays(localite.getIdPays());
            pays.setSession(localite.getSession());

            try {
                pays.retrieve();
                if (pays.getSession().hasErrors()) {
                    throw new Exception(pays.getSession().getErrors().toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        return pays.getCodeIso();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:34:04)
     * 
     * @return java.lang.String
     */
    @Override
    public String getRue() {
        return adresse.getRue() + " " + adresse.getNumeroRue();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:32:46)
     * 
     * @return java.lang.String
     */
    @Override
    public String getTitre() {
        if (JadeStringUtil.isIntegerEmpty(adresse.getTitreAdresse())) {
            return "";
        } else {
            FWParametersUserCode code = new FWParametersUserCode();
            code.setSession(adresse.getSession());
            code.setIdCodeSysteme(adresse.getTitreAdresse());
            if (adresse.getLangueDocument().equals(TITiers.CS_FRANCAIS)) {
                code.setIdLangue("F");
            } else if (adresse.getLangueDocument().equals(TITiers.CS_ALLEMAND)) {
                code.setIdLangue("D");
            } else if (adresse.getLangueDocument().equals(TITiers.CS_ANGLAIS)) {
                code.setIdLangue("E");
            } else if (adresse.getLangueDocument().equals(TITiers.CS_ITALIEN)) {
                code.setIdLangue("I");
            } else if (adresse.getLangueDocument().equals(TITiers.CS_ROMANCHE)) {
                code.setIdLangue("R");
            } else {
                code.setIdLangue("F");
            }
            try {
                code.retrieve();
                if (code.isNew()) {
                    return "";
                } else {
                    return code.getLibelle();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.external.IntAdresseCourrier#getTitre(java.lang.String)
     */
    @Override
    public String getTitre(String language) {
        if (JadeStringUtil.isIntegerEmpty(adresse.getTitreAdresse())) {
            return "";
        } else {
            FWParametersUserCode code = new FWParametersUserCode();
            code.setSession(adresse.getSession());
            code.setIdCodeSysteme(adresse.getTitreAdresse());
            code.setIdLangue(language.substring(0, 1));

            try {
                code.retrieve();
                if (code.isNew()) {
                    return "";
                } else {
                    return code.getLibelle();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:36:39)
     * 
     * @return int
     */
    @Override
    public String getTypeAdresse() {
        // todo: retourner le type d'adresse correct
        return PRINCIPALE;
    }

    /**
     * Indique si l'entité est nouvelle (i.e. n'existe pas dans la BD)
     * 
     * @return true si l'entité n'existe pas dans la BD; false sinon
     */
    @Override
    public boolean isNew() {
        return adresse.isNew();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:31:30)
     */
    @Override
    public void retrieve(globaz.globall.api.BITransaction transaction, String idAdresseCourrier) throws Exception {
        adresse.setIdAdresseUnique(idAdresseCourrier);
        adresse.retrieve(transaction);
        if (!adresse.isNew()) {
            localite.setSession(adresse.getSession());
            localite.setIdLocalite(adresse.getIdLocalite());
            localite.retrieve(transaction);
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:31:30)
     */
    @Override
    public void retrieve(String idAdresseCourrier) throws Exception {
        retrieve(null, idAdresseCourrier);
    }

    /**
     * Modifie la session en cours
     * 
     * @param newISession
     *            la nouvelle session
     */
    @Override
    public void setISession(globaz.globall.api.BISession newSession) {
        adresse.setISession(newSession);
        localite.setISession(newSession);
    }
}
