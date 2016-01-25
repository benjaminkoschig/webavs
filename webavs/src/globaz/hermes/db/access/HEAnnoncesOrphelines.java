package globaz.hermes.db.access;

import globaz.commons.nss.NSUtil;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.application.HEApplication;
import globaz.hermes.db.gestion.HEAnnoncesListViewBean;
import globaz.hermes.db.gestion.HEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.db.parametrage.HEAttenteRetourListViewBean;
import globaz.hermes.db.parametrage.HEAttenteRetourViewBean;
import globaz.hermes.db.parametrage.HECodeapplicationListViewBean;
import globaz.hermes.db.parametrage.HECodeapplicationViewBean;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author ald To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class HEAnnoncesOrphelines extends BEntity {

    private static final long serialVersionUID = 1L;
    private String champEnregistrement = "";
    private String dateAnnonce = "";
    private String idAnnonce = "";
    private String idLot = "";
    private String idMessage = "";
    private String idProgramme = "";
    private String motif = "";
    // private HEOutputAnnonceListViewBean annoncesPossibles = null;
    private String newRefUnique = "";
    /** modif NNSS */
    private Boolean nnss = new Boolean(false);
    private String numAvs = "";
    private String numeroAvsNNSS = "";
    private String numeroCaisse = "";
    private String oldRefUnique = "";
    private String refUnique = "";
    private String statut = "";

    private String typeAnnonce = "";
    private String utilisateur = "";

    /**
     * Constructor for HEAnnoncesOrphelines.
     */
    public HEAnnoncesOrphelines() {
        super();
    }

    /**
     * @see globaz.globall.db.BEntity#_afterDelete(BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        HEAnnoncesListViewBean serie = new HEAnnoncesListViewBean();
        serie.setSession(getSession());
        serie.setForMotif(getMotif());
        serie.setForRefUnique(getRefUnique());
        serie.setForNumAVS(getNumAvs());
        serie.setForStatut(getStatut());
        serie.wantCallMethodAfter(false);
        serie.wantCallMethodAfterFind(false);
        serie.wantCallMethodBefore(false);
        serie.wantCallMethodBeforeFind(false);
        serie.find(transaction, BManager.SIZE_NOLIMIT);
        for (int i = 0; i < serie.size(); i++) {
            HEAnnoncesViewBean crtArc = (HEAnnoncesViewBean) serie.getEntity(i);
            crtArc.wantCallMethodAfter(false);
            crtArc.wantCallMethodBefore(false);
            crtArc.wantCallValidate(false);
            crtArc.delete();
        }
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
        HECodeapplicationListViewBean codeApp = new HECodeapplicationListViewBean();
        codeApp.setSession(getSession());
        codeApp.setForCodeUtilisateur(getChampEnregistrement().substring(0, 2));
        codeApp.find(transaction);
        setTypeAnnonce(((HECodeapplicationViewBean) codeApp.getFirstEntity()).getCurrentCodeUtilisateur().getLibelle());
    }

    /**
     * @see globaz.globall.db.BEntity#_afterUpdate(BTransaction)
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        if (JadeStringUtil.isEmpty(getOldRefUnique()) || JadeStringUtil.isEmpty(getRefUnique())) {
            throw new Exception("Impossible d'associer cette annonce : la référence unique n'est pas renseignée");
        }
        // mettre à jour les attentes retours (si il a en a )
        if (getChampEnregistrement().startsWith("39001")) {
            HEAttenteRetourListViewBean attentes39 = new HEAttenteRetourListViewBean();
            attentes39.setSession(transaction.getSession());
            attentes39.setForNumeroCaisse(getNumeroCaisse());
            attentes39.setForNumeroAVS(getNumAvs());
            attentes39.setForReferenceUnique(getRefUnique());
            attentes39.setForMotif(getMotif());
            attentes39.setForIdAnnonceRetour("0");
            attentes39.setForIdAnnonceRetourAttendue("9");
            attentes39.wantCallMethodAfter(false);
            attentes39.wantCallMethodBefore(false);
            attentes39.wantCallMethodAfterFind(false);
            attentes39.wantCallMethodBeforeFind(false);
            attentes39.find(transaction, BManager.SIZE_NOLIMIT);
            if (attentes39.size() > 0) {
                HEAttenteRetourViewBean att = (HEAttenteRetourViewBean) attentes39.getFirstEntity();
                att.setIdAnnonceRetour(getIdAnnonce());
                att.wantCallMethodAfter(false);
                att.wantCallMethodBefore(false);
                att.wantCallValidate(false);
                att.update(transaction);
            }
        } // les autres enregistrements passent comme le nouveau
        HEAnnoncesListViewBean listeAncienne = new HEAnnoncesListViewBean();
        listeAncienne.setSession(getSession());
        listeAncienne.wantCallMethodAfter(false);
        listeAncienne.wantCallMethodBefore(false);
        listeAncienne.setForRefUnique(getOldRefUnique());
        listeAncienne.find(transaction, BManager.SIZE_NOLIMIT);
        for (int i = 0; i < listeAncienne.size(); i++) {
            HEAnnoncesViewBean entity = (HEAnnoncesViewBean) listeAncienne.getEntity(i);
            entity.setRefUnique(getRefUnique());
            entity.setUtilisateur(getUtilisateur());
            entity.setStatut(getStatut());
            entity.wantCallMethodBefore(false);
            entity.wantCallMethodAfter(false);
            entity.wantCallValidate(false);
            entity.update(transaction);
            // si c'est un 38, adapter l'attente retour
            if (entity.getChampEnregistrement().startsWith("38001")) {
                HEAttenteRetourListViewBean attentes38 = new HEAttenteRetourListViewBean();
                attentes38.setSession(transaction.getSession());
                attentes38.setForNumeroCaisse(entity.getNumeroCaisse());
                attentes38.setForNumeroAVS(entity.getNumeroAVS());
                attentes38.setForReferenceUnique(entity.getRefUnique());
                attentes38.setForMotif(entity.getMotif());
                attentes38.setForIdAnnonceRetour("0");
                attentes38.setForIdAnnonceRetourAttendue("8");
                attentes38.wantCallMethodAfter(false);
                attentes38.wantCallMethodBefore(false);
                attentes38.wantCallMethodAfterFind(false);
                attentes38.wantCallMethodBeforeFind(false);
                attentes38.find(transaction, BManager.SIZE_NOLIMIT);
                if (attentes38.size() > 0) {
                    HEAttenteRetourViewBean att = (HEAttenteRetourViewBean) attentes38.getFirstEntity();
                    att.setIdAnnonceRetour(entity.getIdAnnonce());
                    att.wantCallMethodAfter(false);
                    att.wantCallMethodBefore(false);
                    att.wantCallValidate(false);
                    att.update(transaction);
                }
            }
        }
        // voir si il reste des atttentes pour cette référence -> si oui passer
        // en terminé
        HEAttenteRetourListViewBean listeAttentesEnCours = new HEAttenteRetourListViewBean();
        listeAttentesEnCours.setSession(transaction.getSession());
        listeAttentesEnCours.setForReferenceUnique(getRefUnique());
        listeAttentesEnCours.setForMotif(getMotif());
        listeAttentesEnCours.setForIdAnnonceRetour("0");
        listeAttentesEnCours.wantCallMethodAfter(false);
        listeAttentesEnCours.wantCallMethodBefore(false);
        listeAttentesEnCours.wantCallMethodAfterFind(false);
        listeAttentesEnCours.wantCallMethodBeforeFind(false);
        listeAttentesEnCours.find(transaction, BManager.SIZE_NOLIMIT);
        if (listeAttentesEnCours.size() == 0) {
            HEOutputAnnonceListViewBean listeAnnonces = new HEOutputAnnonceListViewBean();
            listeAnnonces.setSession(transaction.getSession());
            listeAnnonces.setForMotif(getMotif());
            listeAnnonces.setForRefUnique(getRefUnique());
            listeAnnonces.wantCallMethodAfter(false);
            listeAnnonces.wantCallMethodBefore(false);
            listeAnnonces.wantCallMethodAfterFind(false);
            listeAnnonces.wantCallMethodBeforeFind(false);
            listeAnnonces.find(transaction, BManager.SIZE_NOLIMIT);
            for (int i = 0; i < listeAnnonces.size(); i++) {
                HEOutputAnnonceViewBean arc = (HEOutputAnnonceViewBean) listeAnnonces.getEntity(i);
                arc.setStatut(IHEAnnoncesViewBean.CS_TERMINE);
                arc.wantCallMethodAfter(false);
                arc.wantCallMethodBefore(false);
                arc.wantCallValidate(false);
                arc.update(transaction);
            }
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeUpdate(BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        // je sauvegarde l'ancienne référence unique
        setOldRefUnique(getRefUnique());
        // pour mettre le même statut et le même utilisateur
        HEAnnoncesOrphelinesManager orphMgr = getArcPossible(0);
        for (int i = 0; i < orphMgr.size(); i++) {
            HEAnnoncesOrphelines arc = (HEAnnoncesOrphelines) orphMgr.getEntity(i);
            if (arc.getRefUnique().equals(getNewRefUnique())) {
                // on a retrouvé l'annonce choisie
                setUtilisateur(arc.getUtilisateur());
                setStatut(arc.getStatut());
            }
        }
        setRefUnique(getNewRefUnique());
    }

    /**
     * Renvoie le nom de la table
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "HEANNOP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la base de données
     * 
     * @exception java.lang.Exception
     *                si la lecture des propriétés a échouée
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idLot = statement.dbReadNumeric("RMILOT");
        champEnregistrement = statement.dbReadString("RNLENR");
        refUnique = statement.dbReadString("RNREFU");
        idAnnonce = statement.dbReadString("RNIANN");
        dateAnnonce = statement.dbReadDateAMJ("RNDDAN");
        utilisateur = statement.dbReadString("RNLUTI");
        statut = statement.dbReadString("RNTSTA");
        idProgramme = statement.dbReadString("RNTPRO");
        idMessage = statement.dbReadString("RNTMES");
        numAvs = statement.dbReadString("RNAVS");
        motif = statement.dbReadString("RNMOT");
        numeroCaisse = statement.dbReadString("RNCAIS");

        // Modification NNSS
        nnss = statement.dbReadBoolean("RNBNNS");
        if (nnss.booleanValue()) {
            numeroAvsNNSS = "true";
        } else {
            numeroAvsNNSS = "false";
        }

    }

    /**
     * @see globaz.globall.db.BEntity#_validate(BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        HEOutputAnnonceListViewBean l = new HEOutputAnnonceListViewBean();
        l.setSession(getSession());
        l.setForRefUnique(getNewRefUnique());
        l.setForNumCaisse(getNumeroCaisse());
        l.setForNumeroAVS(getNumAvs());
        l.setForCodeApplication("39");
        l.find(statement.getTransaction());
        if (l.size() > 0) {

            throw new Exception(FWMessageFormat.format(getSession().getLabel("RCI_DEJA_EXISTANT"), getNumeroCaisse(),
                    NSUtil.formatAVSUnknown(getNumAvs())));
        }
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité composant la clé primaire
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propriétés a échouée
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("RNIANN",
                this._dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "id annonce orpheline"));
    }

    /**
     * Ecrit les valeurs de l'objet dans la table de la bdd
     * 
     * @param statement
     *            statement
     * @exception Exception
     *                si l'écriture échoue
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("RNIANN", this._dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "idAnnonce"));
        statement.writeField("RMILOT", this._dbWriteNumeric(statement.getTransaction(), getIdLot(), "idLot"));
        statement.writeField("RNDDAN",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateAnnonce(), "dateAnnonce"));
        statement.writeField("RNLUTI",
                this._dbWriteString(statement.getTransaction(), getUtilisateur().toUpperCase(), "utilisateur"));
        statement
                .writeField("RNTPRO", this._dbWriteString(statement.getTransaction(), getIdProgramme(), "idProgramme"));
        statement.writeField("RNLENR",
                this._dbWriteString(statement.getTransaction(), getChampEnregistrement(), "enregistrement"));
        statement.writeField("RNREFU", this._dbWriteString(statement.getTransaction(), getRefUnique(), "refUnique"));
        statement.writeField("RNTSTA", this._dbWriteNumeric(statement.getTransaction(), getStatut(), "statut"));
        statement.writeField("RNTMES", this._dbWriteNumeric(statement.getTransaction(), getIdMessage(), "idMessage"));
        statement.writeField("RNAVS",
                this._dbWriteString(statement.getTransaction(), StringUtils.removeDots(getNumAvs()), "numero AVS"));
        statement.writeField("RNMOT", this._dbWriteString(statement.getTransaction(), getMotif(), "motif"));
        statement.writeField("RNCAIS",
                this._dbWriteString(statement.getTransaction(), getNumeroCaisse(), "numero caisse"));
    }

    protected HEAnnoncesOrphelinesManager getArcPossible(int casDeFigure) throws Exception {
        HEAnnoncesOrphelinesManager orphMgr = new HEAnnoncesOrphelinesManager();
        orphMgr.setSession(getSession());
        orphMgr.wantCallMethodBeforeFind(false);
        orphMgr.setLikeNumAVS(getNumAvs());
        // pour l'instant la méthode _afterUpdate n'est pas adapté pour
        // lier autre chose que les 38,39
        if (getChampEnregistrement().startsWith("38") || getChampEnregistrement().startsWith("39")) {
            // on recherche les 2502 ou plus (250n)
            orphMgr.setForCodeAppl("25"); // SI et SSI c'est un 38/39
            // PO 8128: passer un paramètre depuis l'écran afin de minimiser les impacts (uniquement écran)
            if (casDeFigure != 1) {
                orphMgr.setForNotCodeEnr("01");
            }
            orphMgr.setForNotRefUnique(getRefUnique());
            orphMgr.setOrder("RNIANN");
            orphMgr.setForMotif(getMotif());
            orphMgr.find();
        }
        return orphMgr;
    }

    /**
     * Returns the champEnregistrement.
     * 
     * @return String
     */
    public String getChampEnregistrement() {
        return champEnregistrement;
    }

    /**
     * Returns the dateAnnonce.
     * 
     * @return String
     */
    public String getDateAnnonce() {
        return dateAnnonce;
    }

    /**
     * Returns the idAnnonce.
     * 
     * @return String
     */
    public String getIdAnnonce() {
        return idAnnonce;
    }

    /**
     * Returns the idLot.
     * 
     * @return String
     */
    public String getIdLot() {
        return idLot;
    }

    /**
     * Returns the idMessage.
     * 
     * @return String
     */
    public String getIdMessage() {
        return idMessage;
    }

    /**
     * Returns the idProgramme.
     * 
     * @return String
     */
    public String getIdProgramme() {
        return idProgramme;
    }

    /**
     * Returns the motif.
     * 
     * @return String
     */
    public String getMotif() {
        return motif;
    }

    /**
     * Returns the newRefUnique.
     * 
     * @return String
     */
    public String getNewRefUnique() {
        return newRefUnique;
    }

    /**
     * Returns the numAvs.
     * 
     * @return String
     */
    public String getNumAvs() {
        return numAvs;
    }

    public String getNumeroAvsFormatted() {
        try {
            return JAUtil.formatAvs(getNumAvs());
        } catch (Exception e) {
            e.printStackTrace();
            return getNumAvs();
        }
    }

    public String getNumeroAvsNNSS() {
        return numeroAvsNNSS;
    }

    /**
     * Returns the numeroCaisse.
     * 
     * @return String
     */
    public String getNumeroCaisse() {
        return numeroCaisse;
    }

    /**
     * Returns the oldRefUnique.
     * 
     * @return String
     */
    public String getOldRefUnique() {
        return oldRefUnique;
    }

    /**
     * Returns the refUnique.
     * 
     * @return String
     */
    public String getRefUnique() {
        return refUnique;
    }

    public String GetStatusLibelle() {
        try {
            return ((HEApplication) getSession().getApplication()).getCsStatutListe(getSession())
                    .getCodeSysteme(getStatut()).getCurrentCodeUtilisateur().getLibelle();
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    /**
     * Returns the statut.
     * 
     * @return String
     */
    public String getStatut() {
        return statut;
    }

    /**
     * Returns the typeAnnonce.
     * 
     * @return String
     */
    public String getTypeAnnonce() {

        if (getChampEnregistrement().startsWith("3")) {
            return typeAnnonce + " (" + getNumeroCaisse() + ") ";
        } else {
            return typeAnnonce;
        }
    }

    /**
     * Returns the utilisateur.
     * 
     * @return String
     */
    public String getUtilisateur() {
        return utilisateur;
    }

    /**
     * Sets the champEnregistrement.
     * 
     * @param champEnregistrement
     *            The champEnregistrement to set
     */
    public void setChampEnregistrement(String champEnregistrement) {
        this.champEnregistrement = champEnregistrement;
    }

    /**
     * Sets the dateAnnonce.
     * 
     * @param dateAnnonce
     *            The dateAnnonce to set
     */
    public void setDateAnnonce(String dateAnnonce) {
        this.dateAnnonce = dateAnnonce;
    }

    /**
     * Sets the idAnnonce.
     * 
     * @param idAnnonce
     *            The idAnnonce to set
     */
    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
    }

    /**
     * Sets the idLot.
     * 
     * @param idLot
     *            The idLot to set
     */
    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    /**
     * Sets the idMessage.
     * 
     * @param idMessage
     *            The idMessage to set
     */
    public void setIdMessage(String idMessage) {
        this.idMessage = idMessage;
    }

    /**
     * Sets the idProgramme.
     * 
     * @param idProgramme
     *            The idProgramme to set
     */
    public void setIdProgramme(String idProgramme) {
        this.idProgramme = idProgramme;
    }

    /**
     * Sets the motif.
     * 
     * @param motif
     *            The motif to set
     */
    public void setMotif(String motif) {
        this.motif = motif;
    }

    /**
     * Sets the newRefUnique.
     * 
     * @param newRefUnique
     *            The newRefUnique to set
     */
    public void setNewRefUnique(String newRefUnique) {
        this.newRefUnique = newRefUnique;
    }

    /**
     * Sets the numAvs.
     * 
     * @param numAvs
     *            The numAvs to set
     */
    public void setNumAvs(String numAvs) {
        this.numAvs = numAvs;
    }

    /**
     * Sets the numeroCaisse.
     * 
     * @param numeroCaisse
     *            The numeroCaisse to set
     */
    public void setNumeroCaisse(String numeroCaisse) {
        this.numeroCaisse = numeroCaisse;
    }

    /**
     * Sets the oldRefUnique.
     * 
     * @param oldRefUnique
     *            The oldRefUnique to set
     */
    public void setOldRefUnique(String oldRefUnique) {
        this.oldRefUnique = oldRefUnique;
    }

    /**
     * Sets the refUnique.
     * 
     * @param refUnique
     *            The refUnique to set
     */
    public void setRefUnique(String refUnique) {
        this.refUnique = refUnique;
    }

    /**
     * Sets the statut.
     * 
     * @param statut
     *            The statut to set
     */
    public void setStatut(String statut) {
        this.statut = statut;
    }

    /**
     * Sets the typeAnnonce.
     * 
     * @param typeAnnonce
     *            The typeAnnonce to set
     */
    public void setTypeAnnonce(String typeAnnonce) {
        this.typeAnnonce = typeAnnonce;
    }

    /**
     * Sets the utilisateur.
     * 
     * @param utilisateur
     *            The utilisateur to set
     */
    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }
}
