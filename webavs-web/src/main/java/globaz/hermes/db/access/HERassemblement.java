package globaz.hermes.db.access;

import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAException;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEAnnoncesListViewBean;
import globaz.hermes.db.gestion.HEAnnoncesViewBean;
import globaz.hermes.db.gestion.HERassemblementListViewBean;
import globaz.hermes.db.gestion.HERassemblementViewBean;
import globaz.hermes.utils.StringUtils;

/**
 * @author ald
 * @version 1.0
 * @since 12.03.04
 */
public class HERassemblement extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateCloture = "";
    private String dateDernierRappel = "";
    private String dateReceptionCI = "";
    private final String HEAREAP_ARCHIVE = "HEAREAR";
    private final String HEAREAP_EN_COURS = "HEAREAP";
    private String idAnnonceDepart = "";
    private String idAnnonceRetour = "";
    private String idAttenteRetour = "";
    private boolean isArchivage = false;
    private String motif = "";
    /** modif NNSS */
    private Boolean nnss = new Boolean(false);
    private String nomAssure = "";
    private String nomAyantDroit = "";
    private String numAVS = "";
    private String numAVSAyantDroit = "";
    private String numCaisse = "";
    private String numeroAvsNNSS = "";

    private String referenceUnique = "";
    private String typeAnnonce = "";

    /**
     * Constructor for HERassemblement.
     */
    public HERassemblement() {
        super();
    }

    /**
     * @see globaz.globall.db.BEntity#_afterDelete(BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        if (getTypeAnnonce().equals("9")) {
            supprimerAttente38(transaction);
            termineAnnonce(getReferenceUnique(), getMotif(), transaction);
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        // if (isLoadedFromManager() && isAnnonceRecue()) {
        // // si du manager (rcListe) et reçue
        // HEOutputAnnonceViewBean annonceDepart = new
        // HEOutputAnnonceViewBean(getSession());
        // annonceDepart.setArchivage(isArchivage());
        // annonceDepart.setIdAnnonce(getIdAnnonceRetour());
        // annonceDepart.wantCallMethodAfter(false);
        // annonceDepart.wantCallMethodBefore(false);
        // annonceDepart.wantCallValidate(false);
        // annonceDepart.retrieve(transaction);
        // setDateReceptionCI(annonceDepart.getDateAnnonceJMA());
        // }
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        if (isArchivage) {
            return HEAREAP_ARCHIVE;
        } else {
            return HEAREAP_EN_COURS;
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdAttenteRetour(statement.dbReadNumeric("ROIARA"));
        setIdAnnonceDepart(statement.dbReadNumeric("RNIANN"));
        setIdAnnonceRetour(statement.dbReadNumeric("HEA_RNIANN"));

        setMotif(statement.dbReadString("ROMOT"));
        setNumCaisse(statement.dbReadString("ROCAIS"));
        setReferenceUnique(statement.dbReadString("ROLRUN"));
        setTypeAnnonce(statement.dbReadNumeric("ROTATT"));
        setDateDernierRappel(statement.dbReadDateAMJ("RODRAP"));
        setDateReceptionCI(statement.dbReadDateAMJ("RNDDAN"));

        // Modification NNSS
        // nnss = statement.dbReadBoolean("RNBNNS");
        nnss = statement.dbReadBoolean("ROBNNS");
        setNumAVS(statement.dbReadString("ROAVS"));

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
        try {
            JAUtil.checkAvs(getNumAVS());
        } catch (JAException ex) {
            _addError(statement.getTransaction(),
                    FWMessageFormat.format(getSession().getLabel("HERMES_00027"), getNumAVS()));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("ROIARA", getIdAttenteRetour());
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("ROIARA", _dbWriteNumeric(statement.getTransaction(), getIdAttenteRetour(), "ROIARA"));
        statement.writeField("RNIANN",
                _dbWriteNumeric(statement.getTransaction(), getIdAnnonceDepart(), "Id Annonce Depart"));
        statement.writeField("HEA_RNIANN",
                _dbWriteNumeric(statement.getTransaction(), getIdAnnonceRetour(), "Id annonce départ"));
        statement.writeField("ROTATT", _dbWriteNumeric(statement.getTransaction(), getTypeAnnonce(), "Type d'annonce"));
        statement.writeField("ROLRUN",
                _dbWriteString(statement.getTransaction(), getReferenceUnique(), "référence unique"));
        statement.writeField("ROAVS", _dbWriteString(statement.getTransaction(), getNumAVS(), "Numéro AVS"));
        statement.writeField("ROMOT", _dbWriteString(statement.getTransaction(), getMotif(), "Motif"));
        statement.writeField("ROCAIS", _dbWriteString(statement.getTransaction(), getNumCaisse(), "Numéro de caisse"));
        statement.writeField("RODRAP",
                _dbWriteDateAMJ(statement.getTransaction(), getDateDernierRappel(), "dateDernierRappel"));
    }

    /**
     * Method attendEncoreRetour.
     * 
     * @param string
     * @return boolean
     */
    private boolean attendEncoreRetour(String referenceUnique, BTransaction transaction) throws Exception {
        // on recherche les enregistrements qui sont de type 9
        // et qui ont le même numéro de l'annonce de départ
        // et qui ont le numéro de l'annonce de retour à 0 (pas de retour)
        HERassemblementListViewBean retourListe = new HERassemblementListViewBean();
        retourListe.setSession(transaction.getSession());
        retourListe.setForTypeAnnonce("9");
        retourListe.setForIdAnnonceRetour("0");
        retourListe.setForReferenceUnique(referenceUnique);
        retourListe.setSearchOnlyRassemblement(true);
        retourListe.find(transaction);
        // si il en existe 0, c'est qu'on en attend plus
        return retourListe.size() == 0;
    }

    public HERassemblementViewBean getAttente38(BTransaction transaction) throws Exception {
        HERassemblementListViewBean attente38list = new HERassemblementListViewBean();
        attente38list.setSession(getSession());
        attente38list.setLikeNumAVS(getNumAVS());
        attente38list.setForMotif(getMotif());
        attente38list.setForTypeAnnonce("8");
        attente38list.setForNumCaisse(getNumCaisse());
        attente38list.setForReferenceUnique(getReferenceUnique());
        attente38list.setSearchOnlyRassemblement(true);
        attente38list.find(transaction);
        if (attente38list.size() > 0) {
            // on a trouvé une attente
            HERassemblementViewBean attente38 = (HERassemblementViewBean) attente38list.getFirstEntity();
            attente38.retrieve(transaction);
            return attente38;
        } else {
            return null;
        }
    }

    /**
     * Returns the dateCloture.
     * 
     * @return String
     */
    public String getDateCloture() {
        return dateCloture;
    }

    /**
     * Returns the dateDernierRappel.
     * 
     * @return String
     */
    public String getDateDernierRappel() {
        return dateDernierRappel;
    }

    /**
     * Returns the dateReceptionCI.
     * 
     * @return String
     */
    public String getDateReceptionCI() {
        return dateReceptionCI;
    }

    /**
     * Returns the idAnnonceDepart.
     * 
     * @return String
     */
    public String getIdAnnonceDepart() {
        return idAnnonceDepart;
    }

    /**
     * Returns the idAnnonceRetour.
     * 
     * @return String
     */
    public String getIdAnnonceRetour() {
        return idAnnonceRetour;
    }

    /**
     * Returns the idAttenteRetour.
     * 
     * @return String
     */
    public String getIdAttenteRetour() {
        return idAttenteRetour;
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
     * Returns the nomAssure.
     * 
     * @return String
     */
    public String getNomAssure() {
        return nomAssure;
    }

    /**
     * Returns the nomAyantDroit.
     * 
     * @return String
     */
    public String getNomAyantDroit() {
        return nomAyantDroit;
    }

    /**
     * Returns the numAVS.
     * 
     * @return String
     */
    public String getNumAVS() {
        return numAVS;
    }

    /**
     * Returns the numAVSAyantDroit.
     * 
     * @return String
     */
    public String getNumAVSAyantDroit() {
        return numAVSAyantDroit;
    }

    /**
     * Returns the numCaisse.
     * 
     * @return String
     */
    public String getNumCaisse() {
        return numCaisse;
    }

    public String getNumeroAvsNNSS() {
        return numeroAvsNNSS;
    }

    /**
     * Returns the reference.
     * 
     * @return String
     */
    public String getReferenceUnique() {
        return referenceUnique;
    }

    /**
     * Returns the typeAnnonce.
     * 
     * @return String
     */
    public String getTypeAnnonce() {
        return typeAnnonce;
    }

    public boolean isAnnonceAttendue() {
        return !StringUtils.isStringEmpty(getIdAnnonceRetour());
    }

    public boolean isAnnonceRecue() {
        return !getIdAnnonceRetour().equals("0");
    }

    /**
     * Returns the isArchivage.
     * 
     * @return boolean
     */
    public boolean isArchivage() {
        return isArchivage;
    }

    public void setArchivage(boolean value) {
        isArchivage = value;
    }

    /**
     * Sets the dateCloture.
     * 
     * @param dateCloture
     *            The dateCloture to set
     */
    public void setDateCloture(String dateCloture) {
        this.dateCloture = dateCloture;
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

    /**
     * Sets the dateReceptionCI.
     * 
     * @param dateReceptionCI
     *            The dateReceptionCI to set
     */
    public void setDateReceptionCI(String dateReceptionCI) {
        this.dateReceptionCI = dateReceptionCI;
    }

    /**
     * Sets the idAnnonceDepart.
     * 
     * @param idAnnonceDepart
     *            The idAnnonceDepart to set
     */
    public void setIdAnnonceDepart(String idAnnonceDepart) {
        this.idAnnonceDepart = idAnnonceDepart;
    }

    /**
     * Sets the idAnnonceRetour.
     * 
     * @param idAnnonceRetour
     *            The idAnnonceRetour to set
     */
    public void setIdAnnonceRetour(String idAnnonceRetour) {
        this.idAnnonceRetour = idAnnonceRetour;
    }

    /**
     * Sets the idAttenteRetour.
     * 
     * @param idAttenteRetour
     *            The idAttenteRetour to set
     */
    public void setIdAttenteRetour(String idAttenteRetour) {
        this.idAttenteRetour = idAttenteRetour;
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
     * Sets the nomAssure.
     * 
     * @param nomAssure
     *            The nomAssure to set
     */
    public void setNomAssure(String nomAssure) {
        this.nomAssure = nomAssure;
    }

    /**
     * Sets the nomAyantDroit.
     * 
     * @param nomAyantDroit
     *            The nomAyantDroit to set
     */
    public void setNomAyantDroit(String nomAyantDroit) {
        this.nomAyantDroit = nomAyantDroit;
    }

    /**
     * Sets the numAVS.
     * 
     * @param numAVS
     *            The numAVS to set
     */
    public void setNumAVS(String numAVS) {
        this.numAVS = numAVS;
    }

    /**
     * Sets the numAVSAyantDroit.
     * 
     * @param numAVSAyantDroit
     *            The numAVSAyantDroit to set
     */
    public void setNumAVSAyantDroit(String numAVSAyantDroit) {
        this.numAVSAyantDroit = numAVSAyantDroit;
    }

    /**
     * Sets the numCaisse.
     * 
     * @param numCaisse
     *            The numCaisse to set
     */
    public void setNumCaisse(String numCaisse) {
        this.numCaisse = numCaisse;
    }

    /**
     * Sets the reference.
     * 
     * @param reference
     *            The reference to set
     */
    public void setReferenceUnique(String referenceUnique) {
        this.referenceUnique = referenceUnique;
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

    public void supprimerAttente38(BTransaction transaction) throws Exception {
        HERassemblementViewBean attente38 = getAttente38(transaction);
        if (attente38 != null) {
            // si le nombre d'écriture = 0, effacer l'attente
            attente38.delete(transaction);
        }
    }

    public void termineAnnonce(String referenceUnique, String motif, BTransaction transaction) throws Exception {
        // effacement de l'attente réponse numéro 8 associée à l'attente numéro
        // 9
        // qu'on vient d'effacer
        // contrôler si le système attend encore des attentes retours
        // pour l'annonce, si non on met à jour le status
        if (attendEncoreRetour(referenceUnique, transaction)) {
            HEAnnoncesListViewBean annonces = new HEAnnoncesListViewBean();
            annonces.setSession(transaction.getSession());
            annonces.setForRefUnique(referenceUnique);
            annonces.setForMotif(motif);
            annonces.wantCallMethodAfter(false);
            annonces.wantCallMethodBefore(false);
            annonces.wantCallMethodAfterFind(false);
            annonces.wantCallMethodBeforeFind(false);
            annonces.find(transaction, BManager.SIZE_NOLIMIT);
            for (int i = 0; i < annonces.size(); i++) {
                HEAnnoncesViewBean annonce = (HEAnnoncesViewBean) annonces.getEntity(i);
                annonce.wantCallMethodAfter(false);
                annonce.wantCallMethodBefore(false);
                annonce.wantCallValidate(false);
                annonce.setStatut(IHEAnnoncesViewBean.CS_TERMINE);
                annonce.update(transaction);
            }
        }
    }

}
