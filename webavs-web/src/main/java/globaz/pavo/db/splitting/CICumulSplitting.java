package globaz.pavo.db.splitting;

import globaz.globall.db.BSession;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CICumulSplitting {

    private String codeADS = "";
    private boolean empty = true;
    private int m_annee = 0;
    private String m_commentaire = new String();
    private double m_revenu = 0.0;
    private double m_revenuDonne = 0.0;
    private double m_revenuRecu = 0.0;
    private double m_revenuToCheck = 0.0;
    private boolean nonConforme = false;
    private boolean splitted = false;

    // public CI_SplitingBean() {}

    public CICumulSplitting(BSession session) throws Exception {
        // par défaut, pas une année splittée
        setCommentaire(session.getLabel("PARAM_SPL_REVENUS"));

    }

    public void add(BSession session, String annee, String revenu, boolean extourne, boolean ecrSplitting,
            boolean noPart, String code) throws Exception {
        this.setAnnee(annee);
        setRevenu(session, revenu, extourne, ecrSplitting, noPart);
        setCodeADS(code);
        setEmpty(false);

    }

    private double addMontant(double orig, String toAdd) {
        if (orig == 0.0) {
            return Double.parseDouble(toAdd);
        } else {
            return orig + Double.parseDouble(toAdd);
        }
    }

    /**
     * Méthode de test de conformité du partage
     * 
     * @param conjoint
     *            instance de même type que cette classe contenant, pour la même année, les données du conjoint
     * @return true si pour l'année représentée par cet objet, le cumul des revenus est identique à celui du conjoint.
     *         False si les montants sont différent, si aucune écritures de splitting n'est présente ou si les années ne
     *         sont pas les mêmes. Remarque: les écritures provenant du partage du RAM ne sont pas prises en
     *         considération pour la comparaison.
     * @author David Girardin
     */
    public boolean checkSplitting(CICumulSplitting conjoint) {
        if (!splitted) {
            // pas d'écriture de splitting
            nonConforme = true;
            return false;
        }
        if (conjoint == null || m_annee != conjoint.getAnnee().intValue()) {
            // aucune écriture ou mauvaise année
            nonConforme = true;
            return false;
        }
        // test du total de ctrl
        if (m_revenuToCheck != conjoint.getRevenuToCheck().doubleValue()) {
            nonConforme = true;
            return false;
        }
        // tests ok
        return true;
    }

    public boolean containAnnee(String annee) {
        return m_annee == Integer.parseInt(annee);
    }

    /**
     * Returns the m_annee.
     * 
     * @return Integer
     */
    public Integer getAnnee() {
        return new Integer(m_annee);
    }

    public String getCodeADS() {
        return codeADS;
    }

    /**
     * Returns the m_annee.
     * 
     * @return Integer
     */
    public Integer getCOL_1() {
        return getAnnee();
    }

    public Double getCOL_2() {
        return new Double(m_revenu + m_revenuDonne - m_revenuRecu);
    }

    public Double getCOL_3() {
        return getRevenuDonne();
    }

    public Double getCOL_4() {
        return getRevenuRecu();
    }

    /**
     * Returns the m_revenu.
     * 
     * @return Double
     */
    public Double getCOL_5() {
        return getRevenu();
    }

    public Boolean getCOL_6() {
        return new Boolean(nonConforme);
    }

    /**
     * Returns the m_commentaire.
     * 
     * @return String
     */
    public String getCOL_x() {
        return getCommentaire();
    }

    /**
     * Returns the m_commentaire.
     * 
     * @return String
     */
    protected String getCommentaire() {
        return m_commentaire;
    }

    /**
     * Returns the m_revenu.
     * 
     * @return Double
     */
    protected Double getRevenu() {
        return new Double(m_revenu);
    }

    /**
     * Retourne le total donné.
     * 
     * @return Double
     */
    protected Double getRevenuDonne() {
        return new Double(m_revenuDonne);
    }

    /**
     * Retourne de total reçu.
     * 
     * @return Double
     */
    protected Double getRevenuRecu() {
        return new Double(m_revenuRecu);
    }

    protected Double getRevenuToCheck() {
        return new Double(m_revenuToCheck);
    }

    /**
     * Returns the empty.
     * 
     * @return boolean
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     * Sets the m_annee.
     * 
     * @param m_annee
     *            The m_annee to set
     */
    protected void setAnnee(int annee) {
        m_annee = annee;
        setEmpty(false);
    }

    /**
     * Sets the m_annee.
     * 
     * @param m_annee
     *            The m_annee to set
     */
    protected void setAnnee(String annee) {
        m_annee = Integer.parseInt(annee);
        setEmpty(false);
    }

    public void setCodeADS(String codeADS) {
        this.codeADS = codeADS;
    }

    /**
     * Sets the m_commentaire.
     * 
     * @param commentaire
     *            The commentaire to set
     */
    protected void setCommentaire(String commentaire) {
        m_commentaire = commentaire;
        setEmpty(false);
    }

    /**
     * Sets the empty.
     * 
     * @param empty
     *            The empty to set
     */
    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    /**
     * Sets the m_revenu.
     * 
     * @param m_revenu
     *            The m_revenu to set
     */
    protected void setRevenu(BSession session, String revenu, boolean negative, boolean ecrSplitting, boolean noPart) {
        // test si écriture de splitting
        if (ecrSplitting) {
            // mettre à jour le flag splitting, le commentaire et les totaux de
            // spltting
            if (!splitted) {
                setCommentaire(session.getLabel("PARAM_SPL_REVENUS_PART"));
                splitted = true;
            }
            // totaux
            if (negative) {
                m_revenuDonne = addMontant(m_revenuDonne, revenu);
            } else {
                m_revenuRecu = addMontant(m_revenuRecu, revenu);
            }
        }
        // ajoute le revenu au montant total et montant de ctrl
        if (negative) {
            revenu = "-" + revenu;
        }
        m_revenu = addMontant(m_revenu, revenu);
        if (noPart) {
            // ajouter au total de ctrl si pas de chiffres particulier (1-5)
            m_revenuToCheck = addMontant(m_revenuToCheck, revenu);
        }

        // System.out.println(m_revenu);
        setEmpty(false);

    }

}
