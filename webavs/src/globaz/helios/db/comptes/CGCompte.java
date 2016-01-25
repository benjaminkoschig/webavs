package globaz.helios.db.comptes;

import globaz.globall.db.BConstants;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.helios.db.avs.CGCompteCompteOfas;
import globaz.helios.db.avs.CGCompteCompteOfasManager;
import globaz.helios.db.bouclement.CGParametreBouclementManager;
import globaz.helios.db.classifications.CGLiaisonCompteClasse;
import globaz.helios.db.classifications.CGLiaisonCompteClasseManager;
import globaz.helios.db.interfaces.CGLibelle;
import globaz.helios.db.interfaces.CGNeedExerciceComptable;
import globaz.helios.db.interfaces.CGRemarqueInterface;
import globaz.helios.db.modeles.CGEnteteModeleEcriture;
import globaz.helios.db.modeles.CGLigneModeleEcriture;
import globaz.helios.db.modeles.CGLigneModeleEcritureManager;
import globaz.helios.db.utils.CGCompteOfasFictifHelper;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import java.util.StringTokenizer;

/**
 * CGCompte
 * 
 * @Revision SCO 15.01.2010
 */
public class CGCompte extends CGNeedExerciceComptable implements java.io.Serializable, CGRemarqueInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String AUCUN_CENTRE_CHARGE = "0";
    public final static String CODE_ISO_CHF = "CHF"; // Monnaie du compte
    // compte
    public final static String CS_CENTRE_CHARGE = "700004"; // Nature d'un
    public final static String CS_COMPTE_ADMINISTRATION = "702002";
    // Domaine d'un compte
    public final static String CS_COMPTE_BILAN = "702001";
    public final static String CS_COMPTE_COLLECTIF = "700002"; // Nature d'un
    public final static String CS_COMPTE_EXPLOITATION = "702003";
    public final static String CS_COMPTE_INVESTISSEMENT = "702004";
    public final static String CS_COMPTE_TOUS = "702005";
    // Genre d'un compte
    public final static String CS_GENRE_ACTIF = "701001";
    public final static String CS_GENRE_CHARGE = "701005";
    public final static String CS_GENRE_CLOTURE = "701004";
    public final static String CS_GENRE_OUVERTURE = "701003";

    public final static String CS_GENRE_PASSIF = "701002";

    public final static String CS_GENRE_PRODUIT = "701006";

    public final static String CS_GENRE_RESULTAT = "701007";

    public final static String CS_GENRE_TOUS = "701008";
    // compte
    public final static String CS_MONNAIE_ETRANGERE = "700003"; // Nature d'un
    public final static String CS_STANDARD = "700001"; // Nature d'un compte
    public final static String CS_TVA_CREANCIER = "700006"; // Nature d'un
    // compte
    // compte
    public final static String CS_TVA_DEBITEUR = "700005"; // Nature d'un compte
    private final static String LABEL_PREFIXE = "COMPTE_";

    public static boolean hasValidFormat(String compte, int length) {

        String lCompte = "";
        StringTokenizer token = new StringTokenizer(compte, ".");
        while (token.hasMoreTokens()) {
            lCompte += token.nextElement();
        }

        if (JadeStringUtil.isBlank(lCompte)) {
            lCompte = compte;
        }
        if (lCompte == null) {
            return false;
        }
        if (lCompte.length() != length) {
            return false;
        }
        if (lCompte.charAt(0) == '0') {
            return false;
        }

        try {
            Integer.parseInt(lCompte);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private java.lang.String codeISOMonnaie = "";
    private String defaultIdCentreCharge = "";
    private java.lang.Boolean ecritureManuelleEstVerrouille = new Boolean(false);
    private java.lang.Boolean estVerrouille = new Boolean(false);
    protected java.lang.String idCompte = "";
    private java.lang.String idCompteTVA = "";
    private java.lang.String idDomaine = "";

    private java.lang.String idGenre = "";
    private java.lang.String idMandat = "";
    private java.lang.String idNature = CS_STANDARD;
    private java.lang.String idParametreBouclement = "";
    private java.lang.String idRemarque = "0";

    private java.lang.String idSecteurAVS = "";

    private java.lang.String numeroCompteAVS = "";

    private java.lang.String remarque = "";

    // code systeme

    /**
     * Commentaire relatif au constructeur CGCompte
     */
    public CGCompte() {
        super();
    }

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        super._afterRetrieve(transaction);
        if (hasCGRemarque()) {
            CGRemarque rem = retrieveCGRemarque();
            if (rem.isNew()) {
                setIdRemarque("0");
                // Sinon ca boucle en récursivité, 'impossible' à éviter
                // update(transaction);
            } else {
                setRemarque(rem.getRemarque());
            }
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdCompte(_incCounter(transaction, getIdCompte(), "CGPLANP"));
        setIdMandat(getExerciceComptable().getIdMandat());
        updateRemarque(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws java.lang.Exception {
        // Suppression

        CGEcritureListViewBean managerE = new CGEcritureListViewBean();
        managerE.setSession(getSession());
        managerE.setForIdMandat(getIdMandat());
        managerE.setForIdCompte(getIdCompte());
        managerE.find(transaction);
        if (managerE.size() > 0) {
            _addError(transaction, label("SUPPR_ERROR_1"));
            throw (new Exception(label("SUPPR_ERROR_1")));
        }

        CGParametreBouclementManager managerB = new CGParametreBouclementManager();
        managerB.setSession(getSession());
        managerB.setForIdMandat(getIdMandat());
        managerB.setForIdCompte(getIdCompte());
        managerB.find(transaction);
        if (managerB.size() > 0) {
            throw (new Exception(label("SUPPR_ERROR_2")));
        }

        CGLigneModeleEcritureManager managerM = new CGLigneModeleEcritureManager();
        managerM.setSession(getSession());
        managerM.setForIdCompte(getIdCompte());
        managerM.find(transaction, BManager.SIZE_NOLIMIT);
        for (int i = 0; i < managerM.size(); i++) {
            CGLigneModeleEcriture line = (CGLigneModeleEcriture) managerM.getEntity(i);
            CGEnteteModeleEcriture entete = new CGEnteteModeleEcriture();
            entete.setSession(getSession());
            entete.setIdEnteteModeleEcriture(line.getIdEnteteModeleEcriture());
            entete.retrieve(transaction);
            if (entete.getIdMandat().equals(getIdMandat())) {
                throw (new Exception(label("SUPPR_ERROR_3")));
            }
        }

        if (hasCGRemarque()) {
            CGRemarque rem = new CGRemarque();
            rem.setIdRemarque(getIdRemarque());
            rem.retrieve(transaction);
            if (!rem.isNew()) {
                rem.delete(transaction);
            }
        }

        CGLiaisonCompteClasseManager managerL = new CGLiaisonCompteClasseManager();
        managerL.setSession(getSession());
        managerL.setForIdCompte(getIdCompte());
        managerL.find(transaction);
        for (int i = 0; i < managerL.size(); i++) {
            CGLiaisonCompteClasse entity = (CGLiaisonCompteClasse) managerL.getEntity(i);
            entity.delete(transaction);
        }

        CGSoldeManager managerS = new CGSoldeManager();
        managerS.setSession(getSession());
        managerS.setForIdCompte(getIdCompte());
        managerS.setForIdMandat(getIdMandat());
        managerS.find(transaction);
        for (int i = 0; i < managerS.size(); i++) {
            CGSolde entity = (CGSolde) managerS.getEntity(i);
            entity.delete(transaction);
        }

        CGCompteCompteOfasManager managerOfas = new CGCompteCompteOfasManager();
        managerOfas.setSession(getSession());
        managerOfas.setForIdCompte(getIdCompte());
        managerOfas.find(transaction);
        for (int i = 0; i < managerOfas.size(); i++) {
            CGCompteCompteOfas entity = (CGCompteCompteOfas) managerOfas.getEntity(i);
            entity.delete(transaction);
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        updateRemarque(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "CGCOMTP";
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idCompte = statement.dbReadNumeric("IDCOMPTE");
        idRemarque = statement.dbReadNumeric("IDREMARQUE");
        idCompteTVA = statement.dbReadNumeric("IDCOMPTETVA");
        idSecteurAVS = statement.dbReadNumeric("IDSECTEURAVS");
        idMandat = statement.dbReadNumeric("IDMANDAT");
        numeroCompteAVS = statement.dbReadNumeric("NUMEROCOMPTEAVS");
        idParametreBouclement = statement.dbReadNumeric("IDPARAMETREBOUCL");
        idNature = statement.dbReadNumeric("IDNATURE");
        idDomaine = statement.dbReadNumeric("IDDOMAINE");
        idGenre = statement.dbReadNumeric("IDGENRE");
        codeISOMonnaie = statement.dbReadString("CODEISOMONNAIE");
        ecritureManuelleEstVerrouille = statement.dbReadBoolean("ESTCONFIDENTIEL");
        estVerrouille = statement.dbReadBoolean("ESTVERROUILLE");
        idExerciceComptable = statement.dbReadNumeric("IDEXERCOMPTABLE");
        defaultIdCentreCharge = statement.dbReadNumeric("DEFAUTIDCC");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {

        /* idNature - obligatoire */
        if (JadeStringUtil.isBlank(getIdNature())) {
            _addError(statement.getTransaction(), label("CHAMP_NATURE_OBLIGATOIRE"));
        }

        /* idDomaine - obligatoire */
        if (JadeStringUtil.isBlank(getIdDomaine())) {
            _addError(statement.getTransaction(), label("CHAMP_DOMAINE_COMPTE_OBLIGATOIRE"));
        }

        /* idGenre -obligatoire */
        if (JadeStringUtil.isBlank(getIdGenre())) {
            _addError(statement.getTransaction(), label("CHAMP_GENRE_COMPTE_OBLIGATOIRE"));
        }

        /*
         * idCompteTVA - si renseigné le compte doit etre de nature TVA_DEBITEUR ou TVA_CREANCIER
         */
        if (!JadeStringUtil.isIntegerEmpty(idCompteTVA)) {
            if ((!CGCompte.CS_TVA_CREANCIER.equals(idCompteTVA)) || (!CGCompte.CS_TVA_DEBITEUR.equals(idCompteTVA))) {
                _addError(statement.getTransaction(), label("CHAMP_ASS_CPTE_TVA_OBLIGATOIRE"));
            }
        }

        /* codeISOMonnaie -obligatoire */
        // A Faire
        if (JadeStringUtil.isBlank(getCodeISOMonnaie())) {
            _addError(statement.getTransaction(), label("CHAMP_MONNAIE_OBLIGATOIRE"));
        }

        // Seule les comptes de bilan peuvent être en monnaie étrangère
        if (!CGCompte.CS_COMPTE_BILAN.equals(getIdDomaine())) {
            if (!CODE_ISO_CHF.equals(getCodeISOMonnaie()) || CS_MONNAIE_ETRANGERE.equals(getIdNature())) {
                _addError(statement.getTransaction(), label("NATURE_MONNAIE_ETR_CPT_BILAN_ERROR"));
            }
        }

        if (CGCompte.CS_CENTRE_CHARGE.equals(getIdNature()) && CGCompte.CS_COMPTE_BILAN.equals(getIdDomaine())) {
            _addError(statement.getTransaction(), label("BILAN_CENTRE_CH_ERROR"));
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDCOMPTE", _dbWriteNumeric(statement.getTransaction(), getIdCompte(), ""));
    }

    /*
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {

        // statement.writeField("IDPARAMETREBOUCL",_dbWriteNumeric(statement.getTransaction(),
        // getIdParametreBouclement(),"idParametreBouclement"));

        statement.writeField("IDCOMPTE", _dbWriteNumeric(statement.getTransaction(), getIdCompte(), "idCompte"));
        statement.writeField("IDREMARQUE", _dbWriteNumeric(statement.getTransaction(), getIdRemarque(), "idRemarque"));
        statement.writeField("IDCOMPTETVA",
                _dbWriteNumeric(statement.getTransaction(), getIdCompteTVA(), "idCompteTVA"));
        statement.writeField("IDSECTEURAVS",
                _dbWriteNumeric(statement.getTransaction(), getIdSecteurAVS(), "idSecteurAVS"));
        statement.writeField("IDMANDAT", _dbWriteNumeric(statement.getTransaction(), getIdMandat(), "idMandat"));
        statement.writeField("NUMEROCOMPTEAVS",
                _dbWriteNumeric(statement.getTransaction(), getNumeroCompteAVS(), "numeroCompteAVS"));
        statement.writeField("IDNATURE", _dbWriteNumeric(statement.getTransaction(), getIdNature(), "idNature"));
        statement.writeField("IDDOMAINE", _dbWriteNumeric(statement.getTransaction(), getIdDomaine(), "idDomaine"));
        statement.writeField("IDGENRE", _dbWriteNumeric(statement.getTransaction(), getIdGenre(), "idGenre"));
        statement.writeField("CODEISOMONNAIE",
                _dbWriteString(statement.getTransaction(), getCodeISOMonnaie(), "codeISOMonnaie"));
        statement.writeField(
                "ESTCONFIDENTIEL",
                _dbWriteBoolean(statement.getTransaction(), isEcritureManuelleEstVerrouille(),
                        BConstants.DB_TYPE_BOOLEAN_CHAR, "estConfidentiel"));
        statement.writeField(
                "ESTVERROUILLE",
                _dbWriteBoolean(statement.getTransaction(), isEstVerrouille(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "estVerrouille"));
        statement.writeField("DEFAUTIDCC",
                _dbWriteNumeric(statement.getTransaction(), getDefaultIdCentreCharge(), "defaultIdCentreCharge"));

    }

    public boolean belongsToCompteFictif() throws Exception {
        // CGCompteCompteOfasManager managerOfas = new
        // CGCompteCompteOfasManager();
        // managerOfas.setSession(getSession());
        // managerOfas.setForIdCompte(getIdCompte());
        // managerOfas.find();
        // for (int i = 0; i < managerOfas.size(); i++) {
        // CGCompteCompteOfas entity = (CGCompteCompteOfas)
        // managerOfas.getEntity(i);
        // System.out.println("IdCompteOfas : " + entity.getIdCompteOfas());
        // return
        // CGCompteOfasFictifHelper.getInstance().belongsToCompteFictif(entity.getIdCompteOfas());
        // }
        return CGCompteOfasFictifHelper.getInstance().belongsToCompteFictif(getIdSecteurAVS(), getNumeroCompteAVS());
    }

    // *******************************************************
    // Getter
    // *******************************************************
    public java.lang.String getCodeISOMonnaie() {
        return codeISOMonnaie;
    }

    public String getDefaultIdCentreCharge() {
        return defaultIdCentreCharge;
    }

    /**
     * Retourne le libelle du domaine
     * 
     * @return
     */
    public java.lang.String getDomaineLibelle() {
        try {
            return CodeSystem.getLibelle(getSession(), idDomaine);
        } catch (Exception e) {
            e.printStackTrace();
            return CGLibelle.LIBELLE_ERROR;
        }
    }

    /**
     * Retourne le libelle du genre du compte
     * 
     * @return
     */
    public java.lang.String getGenreLibelle() {
        try {
            return CodeSystem.getLibelle(getSession(), idGenre);
        } catch (Exception e) {
            e.printStackTrace();
            return CGLibelle.LIBELLE_ERROR;
        }
    }

    public java.lang.String getIdCompte() {
        return idCompte;
    }

    public java.lang.String getIdCompteTVA() {
        return idCompteTVA;
    }

    public java.lang.String getIdDomaine() {
        return idDomaine;
    }

    public java.lang.String getIdGenre() {
        return idGenre;
    }

    public java.lang.String getIdMandat() {
        return idMandat;
    }

    public java.lang.String getIdNature() {
        return idNature;
    }

    public java.lang.String getIdParametreBouclement() {
        return idParametreBouclement;
    }

    public java.lang.String getIdRemarque() {
        return idRemarque;
    }

    public java.lang.String getIdSecteurAVS() {
        return idSecteurAVS;
    }

    public java.lang.String getNumeroCompteAVS() {
        return numeroCompteAVS;
    }

    @Override
    public java.lang.String getRemarque() {
        return remarque;
    }

    @Override
    public boolean hasCGRemarque() {
        return (getIdRemarque() != null && !getIdRemarque().equals("0"));
    }

    @Override
    public boolean hasRemarque() {
        return (getRemarque() != null && !getRemarque().equals(""));
    }

    /**
     * Permet de savoir si le compte est de type actif ou non
     * 
     * @return
     */
    public boolean isCompteActif() {
        if (CGCompte.CS_GENRE_ACTIF.equals(getIdGenre())) {
            return true;
        }

        return false;
    }

    /**
     * Permet de savoir si le compte est de type passif ou non
     * 
     * @return
     */
    public boolean isComptePassif() {
        if (CGCompte.CS_GENRE_PASSIF.equals(getIdGenre())) {
            return true;
        }

        return false;
    }

    public java.lang.Boolean isEcritureManuelleEstVerrouille() {
        return ecritureManuelleEstVerrouille;
    }

    public java.lang.Boolean isEstVerrouille() {
        return estVerrouille;
    }

    private String label(String code) {
        return getSession().getLabel(LABEL_PREFIXE + code);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 09:35:31)
     * 
     * @return globaz.helios.db.comptes.CGRemarque
     */
    @Override
    public CGRemarque retrieveCGRemarque() {
        if (hasCGRemarque()) {
            CGRemarque rem = new CGRemarque();
            try {
                rem.setIdRemarque(getIdRemarque());
                rem.setSession(getSession());
                rem.retrieve();
                return rem;
            } catch (Exception e) {
                e.printStackTrace();
                return rem;
            }
        } else {
            return null;
        }
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setCodeISOMonnaie(java.lang.String newCodeISOMonnaie) {
        codeISOMonnaie = newCodeISOMonnaie;
    }

    public void setDefaultIdCentreCharge(String defaultIdCentreCharge) {
        this.defaultIdCentreCharge = defaultIdCentreCharge;
    }

    public void setEcritureManuelleEstVerrouille(java.lang.Boolean newEstConfidentiel) {
        ecritureManuelleEstVerrouille = newEstConfidentiel;
    }

    public void setEstVerrouille(java.lang.Boolean newEstVerrouille) {
        estVerrouille = newEstVerrouille;
    }

    public void setExerciceComptable(CGExerciceComptable exerice) {
        exerciceComptable = exerice;
    }

    public void setIdCompte(java.lang.String newIdCompte) {
        idCompte = newIdCompte;
    }

    public void setIdCompteTVA(java.lang.String newIdCompteTVA) {
        idCompteTVA = newIdCompteTVA;
    }

    public void setIdDomaine(java.lang.String newIdDomaine) {
        idDomaine = newIdDomaine;
    }

    public void setIdGenre(java.lang.String newIdGenre) {
        idGenre = newIdGenre;
    }

    public void setIdMandat(java.lang.String newIdMandat) {
        idMandat = newIdMandat;
    }

    public void setIdNature(java.lang.String newIdNature) {
        idNature = newIdNature;
    }

    public void setIdParametreBouclement(java.lang.String newIdParametreBouclement) {
        idParametreBouclement = newIdParametreBouclement;
    }

    public void setIdRemarque(java.lang.String newIdRemarque) {
        idRemarque = newIdRemarque;
    }

    public void setIdSecteurAVS(java.lang.String newIdSecteurAVS) {
        idSecteurAVS = newIdSecteurAVS;
    }

    public void setNumeroCompteAVS(java.lang.String newNumeroCompteAVS) {
        numeroCompteAVS = newNumeroCompteAVS;
    }

    @Override
    public void setRemarque(java.lang.String rem) {
        remarque = rem;
    }

    @Override
    public void updateRemarque(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // L'utilisateur rentre une remarque
        if (hasRemarque()) {

            // La remarque n'existe pas en DB
            if (!hasCGRemarque()) {
                CGRemarque remTemp = new CGRemarque();
                remTemp.setSession(getSession());
                remTemp.setRemarque(getRemarque());
                remTemp.add(transaction);
                setIdRemarque(remTemp.getIdRemarque());
            }
            // La remarque existe et on vérifie que c'est bien la même, sinon on
            // la met à jour
            else {
                CGRemarque remTemp = retrieveCGRemarque();
                // La remarque n'existe plus en BD, donc on la créée
                if (remTemp.isNew()) {
                    remTemp.setRemarque(getRemarque());
                    remTemp.add(transaction);
                    setIdRemarque(remTemp.getIdRemarque());
                }
                // Si c'est pas la même, on update !
                else if (!remTemp.getRemarque().equals(getRemarque())) {
                    remTemp.setRemarque(getRemarque());
                    remTemp.update(transaction);
                }
            }
        }

    }

}
