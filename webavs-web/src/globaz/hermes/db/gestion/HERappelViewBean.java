package globaz.hermes.db.gestion;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.application.HEApplication;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;

/** Fichier HEAREAP */
public class HERappelViewBean extends BEntity implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** annonce sortante */
    private HEOutputAnnonceViewBean annonceDepart = new HEOutputAnnonceViewBean();
    private Boolean aucuneCaisse = new Boolean(false);
    /** date */
    private String date = new String();
    private String dateDernierRappel = "";
    /** (RNIANN) */
    private String idAnnonce = new String();
    /** (HEA_RNIANN) */
    private String idAnnonceRetour = new String();
    /** Fichier HEAREAP */
    /** (ROIARA) */
    private String idAttenteRetour = new String();
    /** (ROTATT) */
    private String idParamAttenteRetour = new String();
    private Boolean inactif = new Boolean(false);
    private String isoLangueCaisse = "";
    /** (ROMOT) */
    private String motif = new String();
    /** modif NNSS */
    private Boolean nnss = new Boolean(false);
    /** annonce de confirmation le 20 * */
    private String nom = "";
    private String numAVSBeneficiaire = "";
    private String numeroAffilie = "";
    /** (ROAVS) */
    private String numeroAvs = new String();
    private String numeroAvsNNSS = "";
    /** (ROCAIS) */
    private String numeroCaisse = new String();

    /** (ROLRUN) */
    private String referenceUnique = new String();
    /** adresse via Tiers */
    private TIAdministrationViewBean tiersCaisse = new TIAdministrationViewBean();

    // code systeme
    /**
     * Commentaire relatif au constructeur HERappel
     */
    public HERappelViewBean() {
        super();
    }

    // code systeme
    /**
     * Commentaire relatif au constructeur HERappel
     */
    public HERappelViewBean(BSession session) {
        super();
        setSession(session);
    }

    /**
     * Effectue des traitements après une lecture dans la BD et après avoir vidé le tampon de lecture <i>
     * <p>
     * A surcharger pour effectuer les traitements après la lecture de l'entité dans la BD
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws java.lang.Exception {
        if (!isLoadedFromManager()) {
            HEOutputAnnonceListViewBean annoncesDepart = new HEOutputAnnonceListViewBean();
            annoncesDepart.setSession(getSession());
            annoncesDepart.setForRefUnique(getReferenceUnique());
            annoncesDepart.setForCodeApplication("11");
            annoncesDepart.setOrder("RNIANN");
            annoncesDepart.find(transaction);
            for (int i = 0; i < annoncesDepart.size(); i++) {
                HEOutputAnnonceViewBean tmpAnnonce = (HEOutputAnnonceViewBean) annoncesDepart.getEntity(i);
                if (tmpAnnonce.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT).equals("01")) {
                    annonceDepart = (HEOutputAnnonceViewBean) annoncesDepart.getEntity(i);
                    setNumAVSBeneficiaire(getNumeroAvs());
                } else if (tmpAnnonce.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT).equals("04")) {
                    String tmpAVS = tmpAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT);
                    if (!JadeStringUtil.isBlankOrZero(tmpAVS)) {
                        setNumAVSBeneficiaire(tmpAVS);
                    } else {
                        setNumAVSBeneficiaire(annonceDepart.getNumeroAVS());
                    }
                } else if (tmpAnnonce.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT).equals("05")) {
                    String tmpAVS = tmpAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_CONJOINT_SPLITTING_DIVORCE);
                    if (!JadeStringUtil.isBlankOrZero(tmpAVS)) {
                        setNumAVSBeneficiaire(tmpAVS);
                    } else {
                        setNumAVSBeneficiaire(annonceDepart.getNumeroAVS());
                    }
                }
            }
            //
            /*
             * annonceDepart.setSession(getSession()); annonceDepart.setIdAnnonce(getIdAnnonce());
             * annonceDepart.retrieve(transaction);
             */
            // on récupère l'adresse...
            TIAdministrationManager admin = new TIAdministrationManager();
            admin.setSession(getSession());
            admin.setForGenreAdministration(TIAdministrationViewBean.CS_CAISSE_COMPENSATION);
            admin.setForCodeAdministration(getNumeroCaisse());
            admin.setForIncludeInactif(false);
            admin.find(transaction);
            tiersCaisse = (TIAdministrationViewBean) admin.getFirstEntity();
            if (tiersCaisse != null) {
                tiersCaisse.getLangue();
                isoLangueCaisse = tiersCaisse.getLangueIso();
            }
            // ///////////
            // je cherche la confirmation pour cette annonce
            // pour le numéro d'affilié et le nom
            HEOutputAnnonceListViewBean annonce20Liste = new HEOutputAnnonceListViewBean();
            annonce20Liste.setSession(getSession());
            annonce20Liste.setForCodeApplication("20");
            annonce20Liste.setForCodeEnr01Or001(true);
            annonce20Liste.setForRefUnique(annonceDepart.getRefUnique());
            annonce20Liste.find(transaction);
            if (annonce20Liste.size() >= 1) {
                // il pourrait y avoir plusieurs 2001 (ex: la centrale envoi le
                // statut en cours puis exécuté)...
                for (int i = 0; i < annonce20Liste.size(); i++) {
                    HEOutputAnnonceViewBean annonce20 = (HEOutputAnnonceViewBean) annonce20Liste.getEntity(i);
                    String nomCrt = annonce20.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF);
                    if (!JadeStringUtil.isEmpty(nomCrt)) {
                        setNom(nomCrt);
                    }
                }
            } else {
                setNom("");
            }
        }
    }

    public String _getCaisseAdresseWithInactive() {
        try {
            TIAdministrationManager admin = new TIAdministrationManager();
            admin.setSession(getSession());
            admin.setForGenreAdministration(TIAdministrationViewBean.CS_CAISSE_COMPENSATION);
            admin.setForCodeAdministration(getNumeroCaisse());
            admin.find();

            // Cas d'aucune caisse trouvée
            if (admin.isEmpty()) {
                aucuneCaisse = true;
                return "";
            }

            tiersCaisse = (TIAdministrationViewBean) admin.getFirstEntity();

            // Si plusioeurs caisse, on recherche si il y a une active
            if (admin.size() > 1) {
                for (int i = 0; i < admin.size(); i++) {
                    TIAdministrationViewBean tiers = (TIAdministrationViewBean) admin.getEntity(i);
                    if (!tiers.getInactif()) {
                        tiersCaisse = tiers;
                    }
                }
            }

            inactif = tiersCaisse.getInactif();

            return tiersCaisse.getAdresseAsString(new JadePublishDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    HEApplication.CS_DOMAINE_ADRESSE_CI_ARC);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "HEAREAP inner join " + _getCollection() + "HEANNOP on " + _getCollection() + "HEANNOP.RNIANN="
                + _getCollection() + "HEAREAP.RNIANN";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAttenteRetour = statement.dbReadNumeric("ROIARA");
        idAnnonce = statement.dbReadNumeric("RNIANN");
        idAnnonceRetour = statement.dbReadNumeric("HEA_RNIANN");
        idParamAttenteRetour = statement.dbReadNumeric("ROTATT");
        referenceUnique = statement.dbReadString("ROLRUN");
        numeroAvs = statement.dbReadString("ROAVS");
        motif = statement.dbReadString("ROMOT");
        numeroCaisse = statement.dbReadString("ROCAIS");
        date = statement.dbReadDateAMJ("RNDDAN");
        dateDernierRappel = statement.dbReadDateAMJ("RODRAP");

        // Modification NNSS
        nnss = statement.dbReadBoolean("RNBNNS");
        if (nnss.booleanValue()) {
            numeroAvsNNSS = "true";
        } else {
            numeroAvsNNSS = "false";
        }

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    /**

	 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("ROIARA", this._dbWriteNumeric(statement.getTransaction(), getIdAttenteRetour(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("ROIARA",
                this._dbWriteNumeric(statement.getTransaction(), getIdAttenteRetour(), "idAttenteRetour"));
        statement.writeField("RNIANN", this._dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "idAnnonce"));
        statement.writeField("HEA_RNIANN",
                this._dbWriteNumeric(statement.getTransaction(), getIdAnnonceRetour(), "idAnnonceRetour"));
        statement.writeField("ROTATT",
                this._dbWriteNumeric(statement.getTransaction(), getIdParamAttenteRetour(), "idParamAttenteRetour"));
        statement.writeField("ROLRUN",
                this._dbWriteString(statement.getTransaction(), getReferenceUnique(), "referenceUnique"));
        statement.writeField("ROAVS", this._dbWriteString(statement.getTransaction(), getNumeroAvs(), "numeroAvs"));
        statement.writeField("ROMOT", this._dbWriteString(statement.getTransaction(), getMotif(), "motif"));
        statement.writeField("ROCAIS",
                this._dbWriteString(statement.getTransaction(), getNumeroCaisse(), "numeroCaisse"));
        statement.writeField("RODRAP",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateDernierRappel(), "dateDernierRappel"));
    }

    /**
     * Returns the annonceDepart.
     * 
     * @return HEOutputAnnonceViewBean
     */
    public HEOutputAnnonceViewBean getAnnonceDepart() {
        return annonceDepart;
    }

    public String getCaisseAdresse() {
        if (tiersCaisse == null) {
            tiersCaisse = new TIAdministrationViewBean();
        }
        try {
            // S120411_004 - Domaine Cotisation
            if (JadeStringUtil.isEmpty(tiersCaisse.getAdresseAsString(new JadePublishDocumentInfo(),
                    IConstantes.CS_AVOIR_ADRESSE_COURRIER, HEApplication.CS_DOMAINE_ADRESSE_CI_ARC).trim())) {
                TIAdministrationManager admin = new TIAdministrationManager();
                admin.setSession(getSession());
                admin.setForGenreAdministration(TIAdministrationViewBean.CS_CAISSE_COMPENSATION);
                admin.setForCodeAdministration(getNumeroCaisse());
                // Modif jmc =>retirer le correc
                // admin.setForIncludeInactif(false);
                admin.find();
                tiersCaisse = (TIAdministrationViewBean) admin.getFirstEntity();
            }
            return tiersCaisse.getAdresseAsString(new JadePublishDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    HEApplication.CS_DOMAINE_ADRESSE_CI_ARC);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Returns the date.
     * 
     * @return String
     */
    public String getDate() {
        return date;
    }

    /**
     * Returns the dateDernierRappel.
     * 
     * @return String
     */
    public String getDateDernierRappel() {
        return dateDernierRappel;
    }

    public String getIdAnnonce() {
        return idAnnonce;
    }

    public String getIdAnnonceRetour() {
        return idAnnonceRetour;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getIdAttenteRetour() {
        return idAttenteRetour;
    }

    public String getIdParamAttenteRetour() {
        return idParamAttenteRetour;
    }

    /**
     * @return
     */
    public String getIsoLangueCaisse() {
        return isoLangueCaisse;
    }

    /**
     * Returns the langueCaisse.
     * 
     * @return String
     */
    public String getLangueCaisse() {
        if ((tiersCaisse == null) || tiersCaisse.getLangue().trim().equals("")) {
            try {
                TIAdministrationManager admin = new TIAdministrationManager();
                admin.setSession(getSession());
                admin.setForGenreAdministration(TIAdministrationViewBean.CS_CAISSE_COMPENSATION);
                admin.setForCodeAdministration(getNumeroCaisse());
                // admin.setForIncludeInactif(false);
                admin.find();
                tiersCaisse = (TIAdministrationViewBean) admin.getFirstEntity();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
            return tiersCaisse.getLangue();
        } else {
            return tiersCaisse.getLangue();
        }
    }

    public String getMotif() {
        return motif;
    }

    /**
     * Returns the tiers.
     * 
     * @return TITiersViewBean
     */
    /**
     * Returns the nom.
     * 
     * @return String
     */
    public String getNom() {
        if (JadeStringUtil.isEmpty(nom)) {
            // je cherche la confirmation pour cette annonce
            // pour le numéro d'affilié et le nom
            if (!JadeStringUtil.isEmpty(annonceDepart.getRefUnique())) {
                HEOutputAnnonceListViewBean annonce20Liste = new HEOutputAnnonceListViewBean();
                annonce20Liste.setSession(getSession());
                annonce20Liste.setForCodeApplication("20");
                annonce20Liste.setForCodeEnr01Or001(true);
                annonce20Liste.setForRefUnique(annonceDepart.getRefUnique());
                try {
                    annonce20Liste.find();
                    if (annonce20Liste.size() >= 1) {
                        // il pourrait y avoir plusieurs 2001 (ex: la centrale
                        // envoi le statut en cours puis exécuté)...
                        for (int i = 0; i < annonce20Liste.size(); i++) {
                            HEOutputAnnonceViewBean annonce20 = (HEOutputAnnonceViewBean) annonce20Liste.getEntity(i);
                            String nomCrt = annonce20.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF);
                            if (!JadeStringUtil.isEmpty(nomCrt)) {
                                return nomCrt;
                            }
                        }
                        return "";
                    } else {
                        return "";
                    }

                } catch (Exception e) {
                    // e.printStackTrace();
                    return "";
                }
            } else {
                return nom;
            }
        } else {
            return nom;
        }
    }

    /**
     * Returns the numAVSBeneficiaire.
     * 
     * @return String
     */
    public String getNumAVSBeneficiaire() {
        return numAVSBeneficiaire;
    }

    /**
     * Returns the numeroAffilie.
     * 
     * @return String
     */
    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public String getNumeroAvs() {

        // modif NNSS
        return globaz.commons.nss.NSUtil.formatAVSUnknown(numeroAvs);

        // return numeroAvs;
    }

    public String getNumeroAvsNNSS() {
        return numeroAvsNNSS;
    }

    public String getNumeroCaisse() {
        return numeroCaisse;
    }

    public String getReferenceUnique() {
        return referenceUnique;
    }

    /**
     * Returns the tiersCaisse.
     * 
     * @return TIAdministrationViewBean
     */
    public TIAdministrationViewBean getTiersCaisse() {
        return tiersCaisse;
    }

    public Boolean isAucuneCaisse() {
        return aucuneCaisse;
    }

    public Boolean isInactif() {
        return inactif;
    }

    /**
     * Sets the date.
     * 
     * @param date
     *            The date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Sets the dateDernierRappel.
     * 
     * @param dateDernierRappel
     *            The dateDernierRappel to set
     */
    public void setDateDernierRappel(String dateDernierRappel) {
        this.dateDernierRappel = dateDernierRappel;
    }

    public void setIdAnnonce(String newIdAnnonce) {
        idAnnonce = newIdAnnonce;
    }

    public void setIdAnnonceRetour(String newIdAnnonceRetour) {
        idAnnonceRetour = newIdAnnonceRetour;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newH
     *            String
     */
    public void setIdAttenteRetour(String newIdAttenteRetour) {
        idAttenteRetour = newIdAttenteRetour;
    }

    public void setIdParamAttenteRetour(String newIdParamAttenteRetour) {
        idParamAttenteRetour = newIdParamAttenteRetour;
    }

    public void setMotif(String newMotif) {
        motif = newMotif;
    }

    /**
     * Sets the nom.
     * 
     * @param nom
     *            The nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Sets the numAVSBeneficiaire.
     * 
     * @param numAVSBeneficiaire
     *            The numAVSBeneficiaire to set
     */
    public void setNumAVSBeneficiaire(String numAVSBeneficiaire) {
        this.numAVSBeneficiaire = numAVSBeneficiaire;
    }

    /**
     * Sets the numeroAffilie.
     * 
     * @param numeroAffilie
     *            The numeroAffilie to set
     */
    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public void setNumeroAvs(String newNumeroAvs) {
        numeroAvs = newNumeroAvs;
    }

    public void setNumeroCaisse(String newNumeroCaisse) {
        numeroCaisse = newNumeroCaisse;
    }

    public void setReferenceUnique(String newReferenceUnique) {
        referenceUnique = newReferenceUnique;
    }
}
