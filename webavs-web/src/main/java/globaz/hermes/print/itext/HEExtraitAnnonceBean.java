package globaz.hermes.print.itext;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BSession;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.application.HEApplication;
import globaz.hermes.db.gestion.HEAnnoncesViewBean;
import globaz.hermes.utils.HENNSSUtils;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Arrays;

// import globaz.pavo.db.compte.CICompteIndividuel;
// import globaz.pavo.db.compte.CICompteIndividuelManager;
/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class HEExtraitAnnonceBean {
    private BSession hermesSession = null;
    private String langue = "FR";
    private String m_anneeCotti = "";
    private int m_clefExtourne = 0;
    private int m_clefGenre = 0;
    private int m_clefParticulier = 0;
    private String m_codeADS = "";
    private String m_codeRevenu = "";
    private String m_genreRevenu = "";
    private String m_ligne = "";
    private String m_moisCottiDebut = "00";
    private String m_moisCottiFin = "00";
    private boolean m_montantSecurise = false;
    private String m_motif = "";
    private String m_nomEmployeur = "";
    //
    private String m_numeroAffile = "";
    private String m_numeroCaisse = "";
    private String m_partBonn = "";
    private String m_revenu = "";
    private BSession pavoSession = null;
    private String referenceInterne = ""; //

    //
    public HEExtraitAnnonceBean() {
    }

    public HEExtraitAnnonceBean(HEAnnoncesViewBean entity, String userId) throws Exception {
        // ALD si le numéro d'affilié commence par un signe négatif, convertir
        // en NNSS !
        if (HENNSSUtils.isNNSSNegatif(entity.getField(IHEAnnoncesViewBean.NUMERO_AFILLIE))
                && "8".equals(entity.getField(IHEAnnoncesViewBean.CHIFFRE_CLE_GENRE_COTISATIONS))) {
            setNumeroAffile(HENNSSUtils.convertNegatifToNNSS(entity.getField(IHEAnnoncesViewBean.NUMERO_AFILLIE)));
        } else {
            setNumeroAffile(entity.getField(IHEAnnoncesViewBean.NUMERO_AFILLIE));
        }
        setMoisDebut(entity.getField(IHEAnnoncesViewBean.DUREE_COTISATIONS_DEBUT));
        setMoisFin(entity.getField(IHEAnnoncesViewBean.DUREE_COTISATIONS_FIN));
        setAnneeCotti(entity.getField(IHEAnnoncesViewBean.ANNEES_COTISATIONS_AAAA));
        if (StringUtils.isStringEmpty(userId)) {
            setRevenu(entity.getRevenu(entity.getUtilisateur()));
        } else {
            setRevenu(entity.getRevenu(userId));
        }
        setPartBonn(entity.getField(IHEAnnoncesViewBean.PART_BONIFICATIONS_ASSISTANCES));
        setCodeADS(entity.getField(IHEAnnoncesViewBean.CODE_A_D_S));
        setNumeroCaisse(entity.getNumeroCaisse());
        setGenreRevenu(entity.getField(IHEAnnoncesViewBean.CHIFFRE_CLE_EXTOURNES),
                entity.getField(IHEAnnoncesViewBean.CHIFFRE_CLE_GENRE_COTISATIONS),
                entity.getField(IHEAnnoncesViewBean.CHIFFRE_CLEF_PARTICULIER));
        pavoSession = (BSession) ((HEApplication) entity.getSession().getApplication()).getSessionCI(entity
                .getSession());
        setLigne(entity.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT));
        setMotif(entity.getMotif());
        hermesSession = entity.getSession();
    }

    /**
     * Returns the m_anneeCotti.
     * 
     * @return String
     */
    public String getAnneeCotti() {
        return m_anneeCotti;
    }

    /**
     * Returns the m_clefExtourne.
     * 
     * @return int
     */
    public int getClefExtourne() {
        return m_clefExtourne;
    }

    public String getCodeADS() {
        return m_codeADS;
    }

    /**
     * Returns the m_codeRevenu.
     * 
     * @return String
     */
    public String getCodeRevenu() {
        return m_codeRevenu;
    }

    public String getCOL_1() {
        return "1";
    }

    public String getCOL_10() {
        return getPartBonn();
    }

    public String getCOL_11() {
        return getMoisCotti();
    }

    public String getCOL_12() {
        return getAnneeCotti();
    }

    public Double getCOL_13() {
        return getRevenu();
    }

    public String getCOL_14() {
        return getTextRevenu();
    }

    public String getCOL_15() {
        return null;
    }

    public String getCOL_16() {
        return getNumeroCaisse();
    }

    public String getCOL_17() {
        return getCodeADS();
    }

    public String getCOL_2() {
        return null;
    }

    public String getCOL_3() {
        return null;
    }

    public String getCOL_4() {
        return null;
    }

    public String getCOL_5() {
        return null;
    }

    public String getCOL_6() {
        return null;
    }

    public String getCOL_7() {
        return null;
    }

    public String getCOL_8() {
        return getNumeroAffile();
    }

    public String getCOL_9() {
        return getGenreRevenu();
    }

    /**
     * Returns the m_genreRevenu.
     * 
     * @return String
     */
    public String getGenreRevenu() {
        return m_genreRevenu;
    }

    // Colonne plus utilise dans le template
    // A supprimer dès que c'est fait ds la template
    public Integer getID() {
        return new Integer(0);
    }

    /**
     * Retourne la clef identifiant l'extrait d'annonce Année de cotisation + mois debut + mois fin Sauf si l'extrait
     * est vide -> numero de caisse
     */
    protected String getKey() {
        // if(JAStrings.isNull(getAnneeCotti()))
        if (getRevenu().intValue() == 0) {
            return getNumeroCaisse() + m_ligne;
        } else {
            return new String(getAnneeCotti() + m_moisCottiDebut + m_moisCottiFin + m_ligne + getNumeroCaisse());
        }
    }

    /**
     * @return
     */
    public String getLangue() {
        return langue;
    }

    /**
     * Returns the m_moisCotti.
     * 
     * @return String
     */
    public String getMoisCotti() {
        return m_moisCottiDebut + "-" + m_moisCottiFin;
    }

    /**
     * Returns the m_moisCottiDebut.
     * 
     * @return String
     */
    public String getMoisCottiDebut() {
        return m_moisCottiDebut;
    }

    /**
     * Returns the m_moisCottiFin.
     * 
     * @return String
     */
    public String getMoisCottiFin() {
        return m_moisCottiFin;
    }

    /**
     * Returns the motif.
     * 
     * @return String
     */
    public String getMotif() {
        return m_motif;
    }

    /**
     * Returns the m_numeroAffile.
     * 
     * @return String
     */
    public String getNumeroAffile() {
        if (m_clefGenre == 8) { // format AVS
            return NSUtil.formatAVSUnknown(m_numeroAffile);
        } else {
            return m_numeroAffile;
        }
    }

    /**
     * Returns the m_numeroCaisse.
     * 
     * @return String
     */
    public String getNumeroCaisse() {
        return m_numeroCaisse;
    }

    /**
     * Returns the m_partBonn.
     * 
     * @return String
     */
    public String getPartBonn() {
        return m_partBonn;
    }

    /**
     * @return
     */
    public String getReferenceInterne() {
        return referenceInterne;
    }

    /**
     * Returns the m_revenu.
     * 
     * @return String
     */
    public Double getRevenu() {
        int[] values = { Integer.MIN_VALUE, 0, 2, 6, 8 };
        try {
            Double revenu = new Double(m_revenu);
            if (Arrays.binarySearch(values, m_clefExtourne) >= 0) {
                return revenu;
            } else {
                return new Double(revenu.doubleValue() * -1.0);
            }
        } catch (Exception e) {
            // e.printStackTrace();
            if (hermesSession != null) {
                setTextRevenu(hermesSession.getLabel("HERMES_10002"));
            }
            m_montantSecurise = true;
            return new Double(0.0);
        }
    }

    /**
     * Returns the m_textRevenu.
     * 
     * @return String
     */
    public String getTextRevenu() {
        if (!m_montantSecurise) {
            // return m_textRevenu;
            int codeRevenu = m_clefGenre;
            int extourne = m_clefExtourne;
            String affilieId = m_numeroAffile;
            try {
                if ("95".equals(m_motif.trim()) && codeRevenu == 8) {
                    if (extourne == 1) {
                        return pavoSession.getApplication().getLabel("PRTGENRE15", getLangue());
                        // "Part de revenu destinée au conjoint";
                    } else {
                        return pavoSession.getApplication().getLabel("PRTGENRE14", getLangue());
                        // "Part de revenu provenant du conjoint";
                    }
                } else if ("95".equals(m_motif.trim())) {
                    return "";
                }
                switch (codeRevenu) {
                    case 0:
                        if (affilieId.equals("11111111111")) {
                            return pavoSession.getApplication().getLabel("PRTGENRE3", getLangue());
                            // "Bonification pour tâches d'assistance";
                        } else {
                            return pavoSession.getApplication().getLabel("PRTGENRE2", getLangue());
                        }
                        // "Assurance facultative des Suisses à l'étranger";
                    case 1:
                        if (affilieId.equals("88888888888")) {
                            return pavoSession.getApplication().getLabel("PRTGENRE6", getLangue());
                        } else if (affilieId.equals("77777777777")) {
                            return pavoSession.getApplication().getLabel("PRTGENRE7", getLangue());
                        } else if (affilieId.equals("55555555555")) {
                            return pavoSession.getApplication().getLabel("PRTGENRE17", getLangue());
                        } else if (affilieId.equals("66666666666")) {
                            return pavoSession.getApplication().getLabel("PRTGENRE8", getLangue());
                        } else if (affilieId.startsWith("999999")) {
                            return pavoSession.getApplication().getLabel("PRTGENRE5", getLangue());
                        } else {
                            return m_nomEmployeur;
                        }
                    case 2:
                        return pavoSession.getApplication().getLabel("PRTGENRE9", getLangue());
                        // "Salarié/e dont l'employeur n'est pas soumis à cotisations";
                    case 3:
                        return pavoSession.getApplication().getLabel("PRTGENRE10", getLangue());
                        // "Personne de condition indépendante";
                    case 4:
                        if (m_revenu == "") {
                            return pavoSession.getApplication().getLabel("PRTGENRE1", getLangue());
                            // "Conjoint non actif à l'étranger";
                        } else {
                            return pavoSession.getApplication().getLabel("PRTGENRE11", getLangue());
                        }
                        // "Personne sans activité lucrative";
                    case 5:
                        return pavoSession.getApplication().getLabel("PRTGENRE12", getLangue());
                        // "Timbres-cotisations";
                    case 7:
                        return pavoSession.getApplication().getLabel("PRTGENRE13", getLangue());
                        // "Revenu soumis à cotisations de personnes retraitées";
                    case 8:
                        if (extourne == 1) {
                            return pavoSession.getApplication().getLabel("PRTGENRE15", getLangue());
                            // "Part de revenu destinée au conjoint";
                        } else {
                            return pavoSession.getApplication().getLabel("PRTGENRE14", getLangue());
                        }
                        // "Part de revenu provenant du conjoint";
                    case 9:
                        return pavoSession.getApplication().getLabel("PRTGENRE16", getLangue());
                        // "Personne de condition indépendante dans l'agriture";
                    default:
                        return "";
                }
            } catch (Exception e) {
                // e.printStackTrace();
                return m_nomEmployeur;
            }
        } else {
            return m_nomEmployeur;
        }
    }

    /**
     * Sets the m_anneeCotti.
     * 
     * @param m_anneeCotti
     *            The m_anneeCotti to set
     */
    public void setAnneeCotti(String anneeCotti) {
        m_anneeCotti = anneeCotti;
    }

    public void setCodeADS(String codeADS) {
        m_codeADS = codeADS;
    }

    /**
     * Sets the m_codeRevenu.
     * 
     * @param m_codeRevenu
     *            The m_codeRevenu to set
     */
    public void setCodeRevenu(String codeRevenu) {
        m_codeRevenu = codeRevenu;
    }

    /**
     * Sets the m_genreRevenu.
     * 
     * @param m_genreRevenu
     *            The m_genreRevenu to set
     */
    public void setGenreRevenu(String clefExtourne, String genreRevenu, String clefParticulier) {
        m_clefGenre = JadeStringUtil.toIntMIN(genreRevenu);
        m_clefExtourne = JadeStringUtil.toIntMIN(clefExtourne);
        m_clefParticulier = JadeStringUtil.toIntMIN(clefParticulier);
        m_genreRevenu = ((m_clefExtourne == 0) ? "" : clefExtourne) + genreRevenu;
    }

    /**
     * @param string
     */
    public void setLangue(String string) {
        langue = string;
    }

    /**
     * Sets the ligne.
     * 
     * @param ligne
     *            The ligne to set
     */
    public void setLigne(String ligne) {
        m_ligne = ligne;
    }

    /**
     * Sets the m_moisCotti.
     * 
     * @param m_moisCotti
     *            The m_moisCotti to set
     * @deprecated
     */
    @Deprecated
    public void setMoisCotti(String moisCotti) {
        try {
            int mois = Integer.parseInt(moisCotti);
            if (Integer.parseInt(m_moisCottiDebut) > mois) {
                m_moisCottiDebut = moisCotti;
            } else {
                m_moisCottiFin = moisCotti;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setMoisDebut(String moisDebut) {
        if (moisDebut.trim().length() > 0) {
            m_moisCottiDebut = moisDebut;
        }
    }

    public void setMoisFin(String moisFin) {
        if (moisFin.trim().length() > 0) {
            m_moisCottiFin = moisFin;
        }
    }

    /**
     * Sets the motif.
     * 
     * @param motif
     *            The motif to set
     */
    public void setMotif(String motif) {
        m_motif = motif;
    }

    /**
     * Sets the m_numeroAffile.
     * 
     * @param m_numeroAffile
     *            The m_numeroAffile to set
     */
    public void setNumeroAffile(String numeroAffile) {
        m_numeroAffile = numeroAffile;
    }

    /**
     * Sets the m_numeroCaisse.
     * 
     * @param m_numeroCaisse
     *            The m_numeroCaisse to set
     */
    public void setNumeroCaisse(String m_numeroCaisse) {
        this.m_numeroCaisse = m_numeroCaisse;
    }

    /**
     * Sets the m_partBonn.
     * 
     * @param m_partBonn
     *            The m_partBonn to set
     */
    public void setPartBonn(String partBonn) {
        m_partBonn = partBonn;
    }

    public void setPavoSession(BSession session) {
    }

    /**
     * @param string
     */
    public void setReferenceInterne(String string) {
        referenceInterne = string;
    }

    /**
     * Sets the m_revenu.
     * 
     * @param m_revenu
     *            The m_revenu to set
     */
    public void setRevenu(String revenu) {
        m_revenu = revenu;
    }

    /**
     * Sets the m_textRevenu.
     * 
     * @param m_textRevenu
     *            The m_textRevenu to set
     */
    public void setTextRevenu(String m_textRevenu) {
        m_nomEmployeur = m_textRevenu;
    }
}