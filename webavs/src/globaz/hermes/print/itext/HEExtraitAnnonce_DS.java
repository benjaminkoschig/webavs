package globaz.hermes.print.itext;

// ITEXT
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.utils.DateUtils;
import java.util.Iterator;
import java.util.Vector;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * @author Alexandre Cuva To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 * @deprecated N'est plus utilisé
 */
@Deprecated
public class HEExtraitAnnonce_DS extends HEOutputAnnonceListViewBean implements JRDataSource {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private class ExtraitDetail {
        String m_anneeCotti = "";
        String m_codeADS = "";
        String m_codeRevenu = "";
        String m_genreRevenu = "";
        String m_moisCottiDebut = "12";
        String m_moisCottiFin = "01";
        String m_numeroAffile = "";
        String m_numeroCaisse = "";
        String m_partBonn = "";
        Double m_revenu = new Double(0.0);
        String m_textRevenu = "";

        public ExtraitDetail(HEAnnoncesViewBean entity) throws Exception {
            setNumeroAffile(entity.getField(IHEAnnoncesViewBean.NUMERO_AFILLIE));
            setMoisCotti(entity.getField(IHEAnnoncesViewBean.DUREE_COTISATIONS_DEBUT));
            setMoisCotti(entity.getField(IHEAnnoncesViewBean.DUREE_COTISATIONS_FIN));
            setAnneeCotti(entity.getField(IHEAnnoncesViewBean.ANNEES_COTISATIONS_AAAA));
            setRevenu(entity.getField(IHEAnnoncesViewBean.REVENU));
            setPartBonn(entity.getField(IHEAnnoncesViewBean.PART_BONIFICATIONS_ASSISTANCES));
            setCodeADS(entity.getField(IHEAnnoncesViewBean.CODE_A_D_S));
            setNumeroCaisse(entity.getNumeroCaisse());
            // Modif jmc 1-5-8 => le code particulier ne doit plus apparaître
            // sur l'extrait.
            setGenreRevenu(entity.getField(IHEAnnoncesViewBean.CHIFFRE_CLE_EXTOURNES)
                    + entity.getField(IHEAnnoncesViewBean.CHIFFRE_CLE_GENRE_COTISATIONS));
            setNumeroAffile(entity.getField(IHEAnnoncesViewBean.NUMERO_AFILLIE));
        }

        /**
         * Returns the m_anneeCotti.
         * 
         * @return String
         */
        public String getAnneeCotti() {
            return m_anneeCotti;
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

        /**
         * Returns the m_genreRevenu.
         * 
         * @return String
         */
        public String getGenreRevenu() {
            return m_genreRevenu;
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
         * Returns the m_numeroAffile.
         * 
         * @return String
         */
        public String getNumeroAffile() {
            return m_numeroAffile;
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
         * Returns the m_revenu.
         * 
         * @return String
         */
        public Double getRevenu() {
            return m_revenu;
        }

        /**
         * Returns the m_textRevenu.
         * 
         * @return String
         */
        public String getTextRevenu() {
            return m_textRevenu;
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
        public void setGenreRevenu(String genreRevenu) {
            m_genreRevenu = genreRevenu;
        }

        /**
         * Sets the m_moisCotti.
         * 
         * @param m_moisCotti
         *            The m_moisCotti to set
         */
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

        /**
         * Sets the m_revenu.
         * 
         * @param m_revenu
         *            The m_revenu to set
         */
        public void setRevenu(String revenu) {
            try {
                m_revenu = new Double(revenu);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Sets the m_textRevenu.
         * 
         * @param m_textRevenu
         *            The m_textRevenu to set
         */
        public void setTextRevenu(String m_textRevenu) {
            this.m_textRevenu = m_textRevenu;
        }
    }

    private ExtraitDetail _currExtrait = null;
    /**/
    private int _index = 0;
    private java.lang.String AVS = "";
    private String codePays = "";
    private String codeRevenu = "";
    private Iterator container = null;
    private java.lang.String dateLieux = "";
    private String dateNaissance = "";
    private HEOutputAnnonceViewBean entity = null;
    private Vector listAnnonce = new Vector();
    private java.lang.String nomPrenom = "";

    private String numeroCaisse = "";

    /**
     * Constructor for HEExtraiAnnonce_DS.
     */
    public HEExtraitAnnonce_DS() {
        super();
    }

    /**
     * Constructor for HEExtraiAnnonce_DS.
     * 
     * @param session
     */
    public HEExtraitAnnonce_DS(BSession session) {
        super(session);
    }

    private String _getCode1ou2() throws globaz.hermes.db.gestion.HEOutputAnnonceException {
        return entity.getField(IHEAnnoncesViewBean.CODE_1_OU_2);
    }

    private String _getCodeApplication() throws globaz.hermes.db.gestion.HEOutputAnnonceException {
        return entity.getField(IHEAnnoncesViewBean.CODE_APPLICATION);
    }

    private String _getCodeEnregistrement() throws globaz.hermes.db.gestion.HEOutputAnnonceException {
        return entity.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT);
    }

    /**
     * Copiez la méthode tel quel, permet la copy de l'objet Date de création : (01.04.2003 14:45:18)
     * 
     * @return java.lang.Object
     * @exception java.lang.CloneNotSupportedException
     *                La description de l'exception.
     */
    @Override
    public Object clone() throws java.lang.CloneNotSupportedException {
        return super.clone();
    }

    private void extractCode2001() throws Exception, globaz.hermes.db.gestion.HEOutputAnnonceException {
        super.setLikeEnregistrement("2001");
        find(0);
        container = getContainer().iterator();
        if (container.hasNext()) {
            entity = (HEOutputAnnonceViewBean) container.next();
            setNomPrenom(entity.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF));
            setDateNaissance(DateUtils.dateJJMMAAtoDateJJMMAAAA(entity
                    .getField(IHEAnnoncesViewBean.DATE_NAISSANCE_1_JJMMAA)));
            setCodePays(entity.getField(IHEAnnoncesViewBean.ETAT_ORIGINE));
        }
    }

    private void extractValues() {
        try {
            String codeApplication = _getCodeApplication();
            String codeEnregistrement = _getCodeEnregistrement();
            String code = _getCode1ou2();
            if (codeApplication.equals("38")) {
                if (code.equals("1")) { // Code type 1
                    _currExtrait = new ExtraitDetail(entity);
                    // on a un nouveau extrait
                } else { // Code type 2
                    _currExtrait.setTextRevenu(entity.getField(IHEAnnoncesViewBean.PARTIE_INFORMATION));
                    listAnnonce.add(_currExtrait);
                }
            } else { // Code Application == 39
                if (code.equals("1")) { // Code type 1
                    // Total des revenues d'une caisse
                } else { // Code type 2
                    setAVS(JAUtil.formatAvs(entity
                            .getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_DROIT_OU_PARTENAIRE)));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getAVS() {
        return AVS;
    }

    public String getCodePays() {
        return codePays;
    }

    public String getCodeRevenu() {
        return codeRevenu;
    }

    public String getDateLieux() {
        return "Bern, " + JACalendar.todayJJsMMsAAAA();
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * Appele chaque champ du modèle JRField : Field appeler
     */
    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        // retourne chaque champ
        if (jrField.getName().equals("COL_1")) {
            return _currExtrait.getNumeroCaisse();
        }
        if (jrField.getName().equals("COL_2")) {
            return getAVS();
        }
        if (jrField.getName().equals("COL_3")) {
            return getTextCaisseCompensation();
        }
        if (jrField.getName().equals("COL_4")) {
            return getNomPrenom();
        }
        if (jrField.getName().equals("COL_5")) {
            return getCodePays();
        }
        if (jrField.getName().equals("COL_6")) {
            return getDateNaissance();
        }
        if (jrField.getName().equals("COL_7")) {
            return getDateLieux();
        }
        if (jrField.getName().equals("COL_8")) {
            return _currExtrait.getNumeroAffile();
        }
        if (jrField.getName().equals("COL_9")) {
            return _currExtrait.getGenreRevenu();
        }
        if (jrField.getName().equals("COL_10")) {
            return _currExtrait.getPartBonn();
        }
        if (jrField.getName().equals("COL_11")) {
            return _currExtrait.getMoisCotti();
        }
        if (jrField.getName().equals("COL_12")) {
            return _currExtrait.getAnneeCotti();
        }
        if (jrField.getName().equals("COL_13")) {
            return _currExtrait.getRevenu();
        }
        if (jrField.getName().equals("COL_14")) {
            return _currExtrait.getTextRevenu();
        }
        if (jrField.getName().equals("COL_16")) {
            return _currExtrait.getNumeroCaisse();
        }
        if (jrField.getName().equals("COL_17")) {
            return _currExtrait.getCodeADS();
        }
        return "";
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public String getNumeroCaisse() {
        return numeroCaisse;
    }

    public String getTextCaisseCompensation() {
        String idTxtCaisse = _currExtrait.getNumeroCaisse();
        if (idTxtCaisse == null || "null".equals(idTxtCaisse)) {
            idTxtCaisse = "n/a";
        }
        return FWMessageFormat.format("Ausgleichkasse {0}\nCaisse de compensation {0}\nCassa di compensazione {0}",
                idTxtCaisse);
    }

    /**
     * Copier le contenu de cette méthode, elle devrait pas trop changer entre chaque class Retourne vrais si il existe
     * encore une entité.
     */
    @Override
    public boolean next() throws JRException {
        _index++;
        try {
            _currExtrait = (ExtraitDetail) listAnnonce.get(_index);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void prepareManager() {
        entity = null;
        try {
            // Information de base
            super.setOrder("RNIANN");
            // On recherche d'abord les informations de base -> 2001
            extractCode2001();
            // Ouvre le curseur si c'est le statement est null -> donc pas
            // encore ouvert
            super.setLikeEnregistrement("3");
            find(0);
            container = getContainer().iterator();
            // lit le nouveau entity
            while (container.hasNext()) {
                entity = (HEOutputAnnonceViewBean) container.next();
                extractValues();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAVS(String AVS) {
        this.AVS = AVS;
    }

    public void setCodePays(String codePays) {
        this.codePays = codePays;
    }

    public void setCodeRevenu(String codeRevenu) {
        this.codeRevenu = codeRevenu;
    }

    public void setDateLieux(String dateLieux) {
        this.dateLieux = dateLieux;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = JACalendar.format(dateNaissance, JACalendar.FORMAT_DDsMMsYYYY);
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public void setNumeroCaisse(String numeroCaisse) {
        this.numeroCaisse = numeroCaisse;
    }
}
