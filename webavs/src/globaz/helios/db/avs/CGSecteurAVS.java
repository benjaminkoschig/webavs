package globaz.helios.db.avs;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.helios.db.comptes.CGCompteManager;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGPlanComptableManager;
import globaz.helios.db.interfaces.CGLibelle;
import globaz.helios.db.interfaces.CGLibelleInterface;
import globaz.helios.db.interfaces.ITreeListable;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CGSecteurAVS extends globaz.globall.db.BEntity implements CGLibelleInterface, ITreeListable,
        java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_AUTRE_TACHE_AGENCE = "708005"; // Secteur AVS
    public final static String CS_AUTRE_TACHE_PROPRE = "708004"; // Secteur AVS
    public final static String CS_CAF_AGENCE = "708003"; // Secteur AVS
    public final static String CS_CAF_PROPRE = "708002"; // Secteur AVS
    public final static String CS_TACHE_FEDERAL = "708001"; // Secteur AVS
    private java.lang.String ccAgenceSiege = new String();
    private java.lang.Boolean clotureManuelle = new Boolean(false);
    private java.lang.Boolean compteAdministration = new Boolean(false);
    private java.lang.Boolean compteBilan = new Boolean(false);
    private java.lang.Boolean compteExploitation = new Boolean(false);
    private java.lang.Boolean compteInvestissement = new Boolean(false);
    private java.lang.Boolean compteResultat = new Boolean(false);
    private java.lang.Boolean exportComptaSiege = new Boolean(false);
    private java.lang.String idMandat = new String();
    private java.lang.String idSecteurAVS = new String();
    private java.lang.String idSecteurBilan = new String();
    private java.lang.String idSecteurResultat = new String();

    private java.lang.String idTypeTache = new String();

    private java.lang.Boolean isCreationAutomatique = new Boolean(false);

    private java.lang.String libelleDe = new String();

    private java.lang.String libelleFr = new String();
    private java.lang.String libelleIt = new String();
    private java.lang.String tauxVentilation = new String();

    // code systeme
    /**
     * Commentaire relatif au constructeur CGSecteurAVS
     */
    public CGSecteurAVS() {
        super();
    }

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // pas d'increment pour cette classe
        CGMandat mandat = new CGMandat();
        mandat.setSession(getSession());
        mandat.setIdMandat(getIdMandat());
        mandat.retrieve(transaction);
        if (mandat.isNew() || !mandat.isEstComptabiliteAVS().booleanValue()) {
            _addError(transaction, getSession().getLabel("SECTEUR_AVS_MANDAT_NON_AVS"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeDelete(BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        super._beforeDelete(transaction);

        CGCompteManager mgr = new CGCompteManager();
        mgr.setSession(getSession());
        mgr.setForIdSecteurAVS(getIdSecteurAVS());
        mgr.setForIdMandat(getIdMandat());
        mgr.find(transaction, 2);
        if (mgr.size() != 0) {
            throw new Exception("Il existe encore des comptes pour ce secteur AVS. Veuillez d'abord les supprimer.");
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CGSECTP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idSecteurAVS = statement.dbReadNumeric("IDSECTEURAVS");
        idMandat = statement.dbReadNumeric("IDMANDAT");
        libelleFr = statement.dbReadString("LIBELLEFR");
        libelleDe = statement.dbReadString("LIBELLEDE");
        libelleIt = statement.dbReadString("LIBELLEIT");
        idTypeTache = statement.dbReadNumeric("IDTYPETACHE");
        clotureManuelle = statement.dbReadBoolean("CLOTUREMANUELLE");
        compteBilan = statement.dbReadBoolean("COMPTEBILAN");
        compteExploitation = statement.dbReadBoolean("COMPTEEXPLOITATION");
        compteAdministration = statement.dbReadBoolean("CPTADMINISTRATION");
        compteInvestissement = statement.dbReadBoolean("CPTINVESTISSEMENT");
        compteResultat = statement.dbReadBoolean("COMPTERESULTAT");
        idSecteurBilan = statement.dbReadNumeric("IDSECTEURBILAN");
        idSecteurResultat = statement.dbReadNumeric("IDSECTEURRESULTAT");
        tauxVentilation = statement.dbReadNumeric("TAUXVENTILATION", 5);
        exportComptaSiege = statement.dbReadBoolean("EXPORTCOMPTASIEGE");
        ccAgenceSiege = statement.dbReadString("CCAGENCESIEGE");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */

    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        if (JadeStringUtil.isBlank(getIdMandat())) {
            _addError(statement.getTransaction(), getSession().getLabel("SECTEUR_AVS_NUMERO_MANDAT_VIDE"));
        }

        CGMandat mandat = new CGMandat();
        mandat.setSession(getSession());
        mandat.setIdMandat(getIdMandat());
        mandat.retrieve(statement.getTransaction());

        if ((_getAction() != BEntity.ACTION_ADD) && ((mandat == null) || mandat.isNew())) {
            _addError(statement.getTransaction(), getSession().getLabel("SECTEUR_AVS_MANDAT_INEXISTANT"));
        }

        BigDecimal secteurAVS = new BigDecimal(getIdSecteurAVS());

        if (CGSecteurAVS.CS_CAF_PROPRE.equals(getIdTypeTache())) {
            BigDecimal minCAFPropre = new BigDecimal(5000);
            BigDecimal maxCAFPropre = new BigDecimal(5490);

            if (!isExportationComptabiliteAuSiege() || !getIdSecteurAVS().startsWith("5")) {
                if ((secteurAVS.compareTo(minCAFPropre) < 0) || (secteurAVS.compareTo(maxCAFPropre) > 0)) {
                    _addError(statement.getTransaction(), getSession().getLabel("SECTEUR_AVS_RANGE_ERROR_CAF_PROPRE"));
                }
            }
        }

        if (CGSecteurAVS.CS_CAF_AGENCE.equals(getIdTypeTache())) {
            BigDecimal minCAFAgence = new BigDecimal(5500);
            BigDecimal maxCAFAgence = new BigDecimal(5999);

            if (!isExportationComptabiliteAuSiege() || !getIdSecteurAVS().startsWith("5")) {
                if ((secteurAVS.compareTo(minCAFAgence) < 0) || (secteurAVS.compareTo(maxCAFAgence) > 0)) {
                    _addError(statement.getTransaction(), getSession().getLabel("SECTEUR_AVS_RANGE_ERROR_CAF_AGENCE"));
                }
            }
        }

        if (CGSecteurAVS.CS_AUTRE_TACHE_AGENCE.equals(getIdTypeTache())
                || CGSecteurAVS.CS_AUTRE_TACHE_PROPRE.equals(getIdTypeTache())) {
            if (!getIdSecteurAVS().startsWith("6") && !getIdSecteurAVS().startsWith("7")
                    && !getIdSecteurAVS().startsWith("8") && !getIdSecteurAVS().startsWith("4")) {
                _addError(statement.getTransaction(), getSession().getLabel("SECTEUR_AVS_RANGE_ERROR_AUTRE_TACHE"));
            }
        }
        if (isExportationComptabiliteAuSiege() && getCcAgenceSiege().isEmpty()) {
            _addError(statement.getTransaction(), getSession()
                    .getLabel("SECTEUR_AVS_CCAGENCESIEGE_DOIT_ETRE_RENSEIGNE"));

        }
        if (!isExportationComptabiliteAuSiege() && !getCcAgenceSiege().isEmpty()) {
            _addError(statement.getTransaction(), getSession().getLabel("SECTEUR_AVS_CCAGENCESIEGE_DOIT_ETRE_VIDE"));
        }
        if (isExportationComptabiliteAuSiege() && !getCcAgenceSiege().isEmpty()) {
            if (!checkNumeroCompte(getCcAgenceSiege())) {
                _addError(statement.getTransaction(),
                        getSession().getLabel("SECTEUR_AVS_CCAGENCESIEGE_FORMAT_NON_CONFORME"));
            }
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDSECTEURAVS", this._dbWriteNumeric(statement.getTransaction(), getIdSecteurAVS(), ""));
        statement.writeKey("IDMANDAT", this._dbWriteNumeric(statement.getTransaction(), getIdMandat(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDSECTEURAVS",
                this._dbWriteNumeric(statement.getTransaction(), getIdSecteurAVS(), "idSecteurAVS"));
        statement.writeField("IDMANDAT", this._dbWriteNumeric(statement.getTransaction(), getIdMandat(), "idMandat"));
        statement.writeField("LIBELLEFR", this._dbWriteString(statement.getTransaction(), getLibelleFr(), "libelleFr"));
        statement.writeField("LIBELLEDE", this._dbWriteString(statement.getTransaction(), getLibelleDe(), "libelleDe"));
        statement.writeField("LIBELLEIT", this._dbWriteString(statement.getTransaction(), getLibelleIt(), "libelleIt"));
        statement.writeField("IDTYPETACHE",
                this._dbWriteNumeric(statement.getTransaction(), getIdTypeTache(), "idTypeTache"));
        statement.writeField("CLOTUREMANUELLE", this._dbWriteBoolean(statement.getTransaction(), isClotureManuelle(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "clotureManuelle"));
        statement.writeField("COMPTEBILAN", this._dbWriteBoolean(statement.getTransaction(), isCompteBilan(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "compteBilan"));
        statement.writeField("COMPTEEXPLOITATION", this._dbWriteBoolean(statement.getTransaction(),
                isCompteExploitation(), BConstants.DB_TYPE_BOOLEAN_CHAR, "compteExploitation"));
        statement.writeField("CPTADMINISTRATION", this._dbWriteBoolean(statement.getTransaction(),
                isCompteAdministration(), BConstants.DB_TYPE_BOOLEAN_CHAR, "compteAdministration"));
        statement.writeField("CPTINVESTISSEMENT", this._dbWriteBoolean(statement.getTransaction(),
                isCompteInvestissement(), BConstants.DB_TYPE_BOOLEAN_CHAR, "compteInvestissement"));
        statement.writeField("COMPTERESULTAT", this._dbWriteBoolean(statement.getTransaction(), isCompteResultat(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "compteResultat"));
        statement.writeField("IDSECTEURBILAN",
                this._dbWriteNumeric(statement.getTransaction(), getIdSecteurBilan(), "idSecteurBilan"));
        statement.writeField("IDSECTEURRESULTAT",
                this._dbWriteNumeric(statement.getTransaction(), getIdSecteurResultat(), "idSecteurResultat"));
        statement.writeField("TAUXVENTILATION",
                this._dbWriteNumeric(statement.getTransaction(), getTauxVentilation(), "tauxVentilation"));
        statement.writeField("EXPORTCOMPTASIEGE", this._dbWriteBoolean(statement.getTransaction(),
                isExportationComptabiliteAuSiege(), BConstants.DB_TYPE_BOOLEAN_CHAR, "exportComptaSiege"));
        statement.writeField("CCAGENCESIEGE",
                this._dbWriteString(statement.getTransaction(), getCcAgenceSiege(), "ccAgenceSiege"));
    }

    public boolean checkNumeroCompte(String numeroCompte) {
        Pattern DIGIT_AND_SIZE_PATTERN = Pattern.compile("([0-9]){4,4}.([0-9]){4,4}.([0-9]){4,4}");
        Matcher mat = DIGIT_AND_SIZE_PATTERN.matcher(numeroCompte);
        return mat.matches();
    }

    public java.lang.String getCcAgenceSiege() {
        return ccAgenceSiege;
    }

    @Override
    public BManager[] getChilds() {
        CGPlanComptableManager planManager = new CGPlanComptableManager();
        planManager.setForIdSecteurAVS(getIdSecteurAVS());
        planManager.setForIdMandat(getIdMandat());
        return new BManager[] { planManager };
    }

    public java.lang.String getIdMandat() {
        return idMandat;
    }

    /**
     * Getter
     */
    public java.lang.String getIdSecteurAVS() {
        return idSecteurAVS;
    }

    public java.lang.String getIdSecteurBilan() {
        if (isCompteBilan().booleanValue() && "0".equals(idSecteurBilan)) {
            return getIdSecteurAVS();
        } else {
            return idSecteurBilan;
        }
    }

    public java.lang.String getIdSecteurResultat() {
        if (isCompteResultat().booleanValue()) {
            return getIdSecteurAVS();
        } else {
            return idSecteurResultat;
        }
    }

    public java.lang.String getIdTypeTache() {
        return idTypeTache;
    }

    /**
     * Returns the isCreationAutomatique.
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getIsCreationAutomatique() {
        return isCreationAutomatique;
    }

    @Override
    public String getLibelle() {
        return CGLibelle.getLibelleApp(this);
    }

    @Override
    public java.lang.String getLibelleDe() {
        return libelleDe;
    }

    @Override
    public java.lang.String getLibelleFr() {
        return libelleFr;
    }

    @Override
    public java.lang.String getLibelleIt() {
        return libelleIt;
    }

    public java.lang.String getTauxVentilation() {
        return tauxVentilation;
    }

    public java.lang.String getTypeTacheLibelle() {
        try {
            return CodeSystem.getLibelle(getSession(), idTypeTache);
        } catch (Exception e) {
            e.printStackTrace();
            return CGLibelle.LIBELLE_ERROR;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 17:31:27)
     * 
     * @return boolean
     */
    public boolean hasAllLibelleEmpty() {

        return JadeStringUtil.isBlank(libelleDe + libelleFr + libelleIt);
    }

    public java.lang.Boolean isClotureManuelle() {
        return clotureManuelle;
    }

    public java.lang.Boolean isCompteAdministration() {
        return compteAdministration;
    }

    public java.lang.Boolean isCompteBilan() {
        return compteBilan;
    }

    public java.lang.Boolean isCompteExploitation() {
        return compteExploitation;
    }

    public java.lang.Boolean isCompteInvestissement() {
        return compteInvestissement;
    }

    public java.lang.Boolean isCompteResultat() {
        return compteResultat;
    }

    public java.lang.Boolean isExportationComptabiliteAuSiege() {
        return exportComptaSiege;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 10:22:20)
     * 
     * @param secteur
     *            java.lang.String
     */
    public void ouvrir(BTransaction transaction, CGExerciceComptable exerciceComptable, String idClassificationCompte,
            String idClassificationSecteur) throws Exception {

        if (!JadeStringUtil.isBlank(idSecteurAVS)) {
            this.ouvrir(transaction, exerciceComptable, idSecteurAVS, idClassificationCompte, idClassificationSecteur);
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 10:22:20)
     * 
     * @param secteur
     *            java.lang.String
     */
    private void ouvrir(BTransaction transaction, CGExerciceComptable exerciceComptable, String num,
            String idClassificationCompte, String idClassificationSecteur) throws Exception {

        try {

            // Mode création automatique, on crée les comptes associées au
            // secteurs
            // à partir du fichier de configuration secteurs.xml
            // On sette les properties du secteurs de la même manière

            CGSecteurAvsCreation secteur = new CGSecteurAvsCreation();
            secteur.setSession(getSession());
            secteur.setSecteur(num);
            secteur.setIdMandat(exerciceComptable.getIdMandat());
            secteur.setIdTypeTache(getIdTypeTache());
            secteur.setIdSecteurAVS(getIdSecteurAVS());

            secteur.setClotureManuelle(isClotureManuelle());
            secteur.setCompteAdministration(isCompteAdministration());
            secteur.setCompteBilan(isCompteBilan());
            secteur.setCompteExploitation(isCompteExploitation());
            secteur.setCompteInvestissement(isCompteInvestissement());
            secteur.setCompteResultat(isCompteResultat());
            secteur.setIdSecteurBilan(getIdSecteurBilan());
            secteur.setIdSecteurResultat(getIdSecteurResultat());
            secteur.setLibelleDe(getLibelleDe());
            secteur.setLibelleFr(getLibelleFr());
            secteur.setLibelleIt(getLibelleIt());
            secteur.setTauxVentilation(getTauxVentilation());
            secteur.setExportationComptabiliteAuSiege(isExportationComptabiliteAuSiege());

            if (isCreationAutomatique.booleanValue()) {

                if (CGSecteurAVS.CS_TACHE_FEDERAL.equals(getIdTypeTache())) {
                    secteur.lireSecteur(transaction);
                } else {
                    if (JadeStringUtil.isBlank(getLibelleFr() + getLibelleDe() + getLibelleIt())) {
                        secteur.setLibelleFr(num);
                        secteur.setLibelleDe(num);
                        secteur.setLibelleIt(num);
                    }
                    secteur.addComptesTypeTache();

                    if (isCompteBilan().booleanValue()) {
                        secteur.addComptesBilan();
                    }
                    if (isCompteAdministration().booleanValue()) {
                        secteur.addComptesAdmin();
                    }
                    if (isCompteExploitation().booleanValue()) {
                        secteur.addComptesExploitation();
                    }
                    if (isCompteInvestissement().booleanValue()) {
                        secteur.addComptesInvestissement();
                    }

                }
                secteur.add(transaction);
                secteur.creeComptes(transaction, exerciceComptable, idClassificationCompte, idClassificationSecteur);
            }
            // Création du secteur en mode manuelle
            else {
                secteur.add(transaction);
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            e.printStackTrace();
            throw e;
        }
    }

    public void setCcAgenceSiege(java.lang.String ccAgenceSiege) {
        this.ccAgenceSiege = ccAgenceSiege;
    }

    public void setClotureManuelle(java.lang.Boolean newClotureManuelle) {
        clotureManuelle = newClotureManuelle;
    }

    public void setCompteAdministration(java.lang.Boolean newCompteAdministration) {
        compteAdministration = newCompteAdministration;
    }

    public void setCompteBilan(java.lang.Boolean newCompteBilan) {
        compteBilan = newCompteBilan;
    }

    public void setCompteExploitation(java.lang.Boolean newCompteExploitation) {
        compteExploitation = newCompteExploitation;
    }

    public void setCompteInvestissement(java.lang.Boolean newCompteInvestissement) {
        compteInvestissement = newCompteInvestissement;
    }

    public void setCompteResultat(java.lang.Boolean newCompteResultat) {
        compteResultat = newCompteResultat;
    }

    public void setExportationComptabiliteAuSiege(java.lang.Boolean newExportComptaSiege) {
        exportComptaSiege = newExportComptaSiege;
    }

    public void setIdMandat(java.lang.String newIdMandat) {
        idMandat = newIdMandat;
    }

    /**
     * Setter
     */
    public void setIdSecteurAVS(java.lang.String newIdSecteurAVS) {
        idSecteurAVS = newIdSecteurAVS;
    }

    public void setIdSecteurBilan(java.lang.String newIdSecteurBilan) {
        idSecteurBilan = newIdSecteurBilan;
    }

    public void setIdSecteurResultat(java.lang.String newIdSecteurResultat) {
        idSecteurResultat = newIdSecteurResultat;
    }

    public void setIdTypeTache(java.lang.String newIdTypeTache) {
        idTypeTache = newIdTypeTache;
    }

    /**
     * Sets the isCreationAutomatique.
     * 
     * @param isCreationAutomatique
     *            The isCreationAutomatique to set
     */
    public void setIsCreationAutomatique(java.lang.Boolean isCreationAutomatique) {
        this.isCreationAutomatique = isCreationAutomatique;
    }

    public void setLibelleDe(java.lang.String newLibelleDe) {
        libelleDe = newLibelleDe;
    }

    public void setLibelleFr(java.lang.String newLibelleFr) {
        libelleFr = newLibelleFr;
    }

    public void setLibelleIt(java.lang.String newLibelleIt) {
        libelleIt = newLibelleIt;
    }

    public void setTauxVentilation(java.lang.String newTauxVentilation) {
        tauxVentilation = newTauxVentilation;
    }

}
