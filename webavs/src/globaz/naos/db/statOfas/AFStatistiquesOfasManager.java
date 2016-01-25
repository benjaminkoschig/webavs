package globaz.naos.db.statOfas;

import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.constantes.ILEConstantes;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APISection;
import java.io.Serializable;

public class AFStatistiquesOfasManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String adresse = "tiadrep";
    public final static String afact = "faafacp";
    public final static String affiliation = "afaffip";
    public final static String assurance = "afassup";
    public final static String avoirAdr = "tiaadrp";
    public final static String complementJournal = "jojpcjo";
    public final static String compteur = "cacptrp";
    public final static String controle = "afcontp";
    public final static String cptAnnexe = "cacptap";
    public final static String declaration = "dsdeclp";
    public final static String decompte = "faentfp";
    public final static String groupeJournal = "jojpgjo";
    public final static String journal = "cajourp";
    public final static String journalisation = "jojpjou";
    public final static String ligneDecl = "dsindp";
    public final static String localite = "tilocap";
    public final static String operation = "caoperp";
    public final static String perAvs = "tipavsp";
    public final static String personne = "tipersp";
    public final static String referenceProvenance = "jojprep";
    public final static String section = "casectp";
    public final static String tiers = "titierp";
    private String annee = "";
    private String anneeMoinUn = "";
    private Boolean forExcluLTN = new Boolean(false);
    private Boolean forFields1 = new Boolean(false);
    private Boolean forFields2 = new Boolean(false);
    private Boolean forFields3 = new Boolean(false);
    private Boolean forFields4 = new Boolean(false);
    private Boolean forFieldsLTN3 = new Boolean(false);
    private Boolean forFieldsLTN4 = new Boolean(false);
    private Boolean forFrom1 = new Boolean(false);

    private Boolean forFrom2 = new Boolean(false);
    private Boolean forFrom3 = new Boolean(false);
    private Boolean forFrom4 = new Boolean(false);
    private Boolean forFrom5 = new Boolean(false);
    private Boolean forFromLPP1 = new Boolean(false);
    private Boolean forFromLPP2 = new Boolean(false);
    private Boolean forFromLPP3 = new Boolean(false);
    private Boolean forFromLTN2 = new Boolean(false);
    private Boolean forFromLTN3 = new Boolean(false);
    private Boolean forFromMontant = new Boolean(false);
    private Boolean forIndEmpl = new Boolean(false);
    private Boolean forMontantCoti = new Boolean(false);
    private Boolean forMontantCotiInd = new Boolean(false);
    private Boolean forNaCotiMiniEtuRI = new Boolean(false);
    private Boolean forNaCotiMiniNonEtuRI = new Boolean(false);
    private Boolean forNaPersMaison = new Boolean(false);
    private Boolean forNbrControleEmpl = new Boolean(false);
    // private Boolean forNbrEmployeurs = new Boolean(false);
    private Boolean forNbrEmployes = new Boolean(false);
    private Boolean forNbrEmployeurDS = new Boolean(false);
    private Boolean forNbrEmployeurLPP = new Boolean(false);
    private String forOrder = "";
    private Boolean forRechercheCanton = new Boolean(false);

    private Boolean forRequetePrinc = new Boolean(false);
    private Boolean forTotalAffilie = new Boolean(false);
    private Boolean forTsePersMaison = new Boolean(false);
    private String montantMinime = "";

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        String fields = "";
        if (getForFields1().booleanValue()) {
            fields = " MATTAF, MBTGEN, COUNT(DISTINCT MAIAFF) AS NOMBRE, SUM(MONTANT) AS MONTANT";
        }
        if (getForFields2().booleanValue()) {
            fields = " COUNT(*) AS NOMBRE";
        }
        if (getForFields3().booleanValue()) {
            fields = AFStatistiquesOfasManager.localite + ".HJICAN, COUNT(" + AFStatistiquesOfasManager.tiers
                    + ".HTITIE) AS NOMBRE";
        }
        if (getForFields4().booleanValue()) {
            fields = " COUNT(DISTINCT MAIAFF) AS NOMBRE, SUM(CUMULCOTISATION) AS MONTANT";
        }
        if (getForFieldsLTN3().booleanValue()) {
            fields = " SUM(MONTANT) AS MONTANT";
        }
        if (getForFieldsLTN4().booleanValue()) {
            fields = " COUNT(DISTINCT MAIAFF) AS NOMBRE";
        }
        return fields;
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String from = "";

        if (getForFrom1().booleanValue()) {
            from = _getCollection() + AFStatistiquesOfasManager.affiliation + " "
                    + AFStatistiquesOfasManager.affiliation + " INNER JOIN " + _getCollection()
                    + AFStatistiquesOfasManager.cptAnnexe + " " + AFStatistiquesOfasManager.cptAnnexe + " ON ("
                    + AFStatistiquesOfasManager.affiliation + ".HTITIE=" + AFStatistiquesOfasManager.cptAnnexe
                    + ".IDTIERS AND " + AFStatistiquesOfasManager.affiliation + ".MALNAF="
                    + AFStatistiquesOfasManager.cptAnnexe + ".IDEXTERNEROLE)" + " INNER JOIN " + _getCollection()
                    + AFStatistiquesOfasManager.operation + " " + AFStatistiquesOfasManager.operation + " ON ("
                    + AFStatistiquesOfasManager.operation + ".IDCOMPTEANNEXE=" + AFStatistiquesOfasManager.cptAnnexe
                    + ".IDCOMPTEANNEXE AND " + AFStatistiquesOfasManager.operation + ".ETAT = "
                    + APIOperation.ETAT_COMPTABILISE + ")" + " INNER JOIN " + _getCollection()
                    + AFStatistiquesOfasManager.journal + " " + AFStatistiquesOfasManager.journal + " ON ("
                    + AFStatistiquesOfasManager.operation + ".IDJOURNAL=" + AFStatistiquesOfasManager.journal
                    + ".IDJOURNAL)" + " INNER JOIN " + _getCollection() + AFStatistiquesOfasManager.assurance + " "
                    + AFStatistiquesOfasManager.assurance + " ON (" + AFStatistiquesOfasManager.operation
                    + ".IDCOMPTE=" + AFStatistiquesOfasManager.assurance + ".MBIRUB AND "
                    + AFStatistiquesOfasManager.assurance + ".MBTTYP = " + CodeSystem.TYPE_ASS_COTISATION_AVS_AI + ")";
        }
        if (getForFrom2().booleanValue()) {
            from = "(SELECT HTITIE FROM " + _getCollection() + AFStatistiquesOfasManager.affiliation + " "
                    + AFStatistiquesOfasManager.affiliation + " INNER JOIN " + _getCollection()
                    + AFStatistiquesOfasManager.cptAnnexe + " " + AFStatistiquesOfasManager.cptAnnexe + " ON ("
                    + AFStatistiquesOfasManager.affiliation + ".HTITIE=" + AFStatistiquesOfasManager.cptAnnexe
                    + ".IDTIERS)" + " INNER JOIN " + _getCollection() + AFStatistiquesOfasManager.operation + " "
                    + AFStatistiquesOfasManager.operation + " ON (" + AFStatistiquesOfasManager.operation
                    + ".IDCOMPTEANNEXE=" + AFStatistiquesOfasManager.cptAnnexe + ".IDCOMPTEANNEXE AND "
                    + AFStatistiquesOfasManager.operation + ".ETAT = " + APIOperation.ETAT_COMPTABILISE + ")"
                    + " INNER JOIN " + _getCollection() + AFStatistiquesOfasManager.journal + " "
                    + AFStatistiquesOfasManager.journal + " ON (" + AFStatistiquesOfasManager.operation + ".IDJOURNAL="
                    + AFStatistiquesOfasManager.journal + ".IDJOURNAL)" + " INNER JOIN " + _getCollection()
                    + AFStatistiquesOfasManager.assurance + " " + AFStatistiquesOfasManager.assurance + " ON ("
                    + AFStatistiquesOfasManager.operation + ".IDCOMPTE=" + AFStatistiquesOfasManager.assurance
                    + ".MBIRUB AND " + AFStatistiquesOfasManager.assurance + ".MBTTYP = "
                    + CodeSystem.TYPE_ASS_COTISATION_AVS_AI + ")";
        }
        if (getForFrom3().booleanValue()) {
            from = _getCollection() + AFStatistiquesOfasManager.affiliation + " "
                    + AFStatistiquesOfasManager.affiliation + " INNER JOIN " + _getCollection()
                    + AFStatistiquesOfasManager.cptAnnexe + " " + AFStatistiquesOfasManager.cptAnnexe + " ON ("
                    + AFStatistiquesOfasManager.affiliation + ".HTITIE=" + AFStatistiquesOfasManager.cptAnnexe
                    + ".IDTIERS AND " + AFStatistiquesOfasManager.affiliation + ".MALNAF="
                    + AFStatistiquesOfasManager.cptAnnexe + ".IDEXTERNEROLE)" + " INNER JOIN " + _getCollection()
                    + AFStatistiquesOfasManager.compteur + " " + AFStatistiquesOfasManager.compteur + " ON ("
                    + AFStatistiquesOfasManager.compteur + ".IDCOMPTEANNEXE=" + AFStatistiquesOfasManager.cptAnnexe
                    + ".IDCOMPTEANNEXE)" + " INNER JOIN " + _getCollection() + AFStatistiquesOfasManager.assurance
                    + " " + AFStatistiquesOfasManager.assurance + " ON (" + AFStatistiquesOfasManager.compteur
                    + ".IDRUBRIQUE=" + AFStatistiquesOfasManager.assurance + ".MBIRUB AND "
                    + AFStatistiquesOfasManager.assurance + ".MBTTYP = " + CodeSystem.TYPE_ASS_COTISATION_AVS_AI
                    + " AND " + AFStatistiquesOfasManager.assurance + ".MBTGEN = " + CodeSystem.GENRE_ASS_PERSONNEL
                    + ")";
        }
        if (getForFrom4().booleanValue()) {
            from = _getCollection() + AFStatistiquesOfasManager.tiers + " " + AFStatistiquesOfasManager.tiers
                    + " INNER JOIN " + _getCollection() + AFStatistiquesOfasManager.personne + " "
                    + AFStatistiquesOfasManager.personne + " ON (" + AFStatistiquesOfasManager.personne + ".HTITIE="
                    + AFStatistiquesOfasManager.tiers + ".HTITIE)" + " INNER JOIN " + _getCollection()
                    + AFStatistiquesOfasManager.perAvs + " " + AFStatistiquesOfasManager.perAvs + " ON ("
                    + AFStatistiquesOfasManager.perAvs + ".HTITIE=" + AFStatistiquesOfasManager.tiers + ".HTITIE)"
                    + " LEFT OUTER JOIN " + _getCollection() + AFStatistiquesOfasManager.avoirAdr + " "
                    + AFStatistiquesOfasManager.avoirAdr + " ON (" + AFStatistiquesOfasManager.avoirAdr + ".HTITIE="
                    + AFStatistiquesOfasManager.tiers + ".HTITIE AND " + AFStatistiquesOfasManager.avoirAdr
                    + ".HEIADR = " + AFStatistiquesOfasManager.avoirAdr + ".HEIAAU)" + " LEFT OUTER JOIN "
                    + _getCollection() + AFStatistiquesOfasManager.adresse + " " + AFStatistiquesOfasManager.adresse
                    + " ON (" + AFStatistiquesOfasManager.avoirAdr + ".HAIADR=" + AFStatistiquesOfasManager.adresse
                    + ".HAIADR)" + " LEFT OUTER JOIN " + _getCollection() + AFStatistiquesOfasManager.localite + " "
                    + AFStatistiquesOfasManager.localite + " ON (" + AFStatistiquesOfasManager.adresse + ".HJILOC="
                    + AFStatistiquesOfasManager.localite + ".HJILOC)" + " INNER JOIN " + _getCollection()
                    + AFStatistiquesOfasManager.affiliation + " " + AFStatistiquesOfasManager.affiliation + " ON ("
                    + AFStatistiquesOfasManager.affiliation + ".HTITIE=" + AFStatistiquesOfasManager.personne
                    + ".HTITIE)";
        }
        if (getForFrom5().booleanValue()) {
            from = _getCollection() + AFStatistiquesOfasManager.affiliation;
        }
        if (getForFromMontant().booleanValue()) {
            from = _getCollection() + AFStatistiquesOfasManager.cptAnnexe + " " + AFStatistiquesOfasManager.cptAnnexe
                    + " INNER JOIN " + _getCollection() + AFStatistiquesOfasManager.operation + " "
                    + AFStatistiquesOfasManager.operation + " ON (" + AFStatistiquesOfasManager.operation
                    + ".IDCOMPTEANNEXE=" + AFStatistiquesOfasManager.cptAnnexe + ".IDCOMPTEANNEXE AND "
                    + AFStatistiquesOfasManager.operation + ".ETAT = " + APIOperation.ETAT_COMPTABILISE + ")"
                    + " INNER JOIN " + _getCollection() + AFStatistiquesOfasManager.journal + " "
                    + AFStatistiquesOfasManager.journal + " ON (" + AFStatistiquesOfasManager.operation + ".IDJOURNAL="
                    + AFStatistiquesOfasManager.journal + ".IDJOURNAL)" + " INNER JOIN " + _getCollection()
                    + AFStatistiquesOfasManager.affiliation + " " + AFStatistiquesOfasManager.affiliation + " ON ("
                    + AFStatistiquesOfasManager.affiliation + ".HTITIE=" + AFStatistiquesOfasManager.cptAnnexe
                    + ".IDTIERS AND " + AFStatistiquesOfasManager.affiliation + ".MALNAF="
                    + AFStatistiquesOfasManager.cptAnnexe + ".IDEXTERNEROLE)" + " INNER JOIN " + _getCollection()
                    + AFStatistiquesOfasManager.assurance + " " + AFStatistiquesOfasManager.assurance + " ON ("
                    + AFStatistiquesOfasManager.operation + ".IDCOMPTE=" + AFStatistiquesOfasManager.assurance
                    + ".MBIRUB AND " + AFStatistiquesOfasManager.assurance + ".MBTTYP = "
                    + CodeSystem.TYPE_ASS_COTISATION_AVS_AI + ")";
        }
        if (getForFromLTN2().booleanValue()) {
            from = _getCollection() + AFStatistiquesOfasManager.ligneDecl + " " + AFStatistiquesOfasManager.ligneDecl
                    + " INNER JOIN " + _getCollection() + AFStatistiquesOfasManager.declaration + " "
                    + AFStatistiquesOfasManager.declaration + " ON (" + AFStatistiquesOfasManager.ligneDecl
                    + ".TAIDDE=" + AFStatistiquesOfasManager.declaration + ".TAIDDE)";
        }
        if (getForFromLTN3().booleanValue()) {
            from = _getCollection() + AFStatistiquesOfasManager.section + " " + AFStatistiquesOfasManager.section
                    + " INNER JOIN " + _getCollection() + AFStatistiquesOfasManager.operation + " "
                    + AFStatistiquesOfasManager.operation + " ON (" + AFStatistiquesOfasManager.section + ".IDSECTION="
                    + AFStatistiquesOfasManager.operation + ".IDSECTION)";
        }
        if (getForFromLPP1().booleanValue()) {
            from = " (SELECT malnaf FROM " + _getCollection() + AFStatistiquesOfasManager.journalisation + " "
                    + AFStatistiquesOfasManager.journalisation + " INNER JOIN " + _getCollection()
                    + AFStatistiquesOfasManager.groupeJournal + " " + AFStatistiquesOfasManager.groupeJournal + " ON ("
                    + AFStatistiquesOfasManager.groupeJournal + ".JGJOID=" + AFStatistiquesOfasManager.journalisation
                    + ".JGJOID)" + " INNER JOIN (SELECT * FROM " + _getCollection()
                    + AFStatistiquesOfasManager.complementJournal + " WHERE JCJOTY="
                    + ILEConstantes.CS_CATEGORIE_GROUPE + " AND JCJOVA = " + ILEConstantes.CS_CATEGORIE_SUIVI_LPP
                    + ") AS CAT ON(CAT.JJOUID=" + AFStatistiquesOfasManager.journalisation
                    + ".JJOUID) INNER JOIN (SELECT * FROM " + _getCollection()
                    + AFStatistiquesOfasManager.complementJournal + " " + AFStatistiquesOfasManager.complementJournal
                    + " WHERE JCJOTY=" + ILEConstantes.CS_DEF_FORMULE_GROUPE + " AND (JCJOVA = "
                    + ILEConstantes.CS_DEF_FORMULE_QUEST_LPP + " OR JCJOVA = " + ILEConstantes.CS_DEF_FORMULE_RECEPTION
                    + ")) AS CPL ON(" + "CPL.JJOUID=" + AFStatistiquesOfasManager.journalisation
                    + ".JJOUID) INNER JOIN " + _getCollection() + AFStatistiquesOfasManager.affiliation + " "
                    + AFStatistiquesOfasManager.affiliation + " ON (" + AFStatistiquesOfasManager.journalisation
                    + ".JJOULI = " + AFStatistiquesOfasManager.affiliation + ".MALNAF)";
        }
        if (getForFromLPP2().booleanValue()) {
            from = _getCollection() + AFStatistiquesOfasManager.journalisation + " "
                    + AFStatistiquesOfasManager.journalisation + " INNER JOIN " + _getCollection()
                    + AFStatistiquesOfasManager.groupeJournal + " " + AFStatistiquesOfasManager.groupeJournal + " ON ("
                    + AFStatistiquesOfasManager.groupeJournal + ".JGJOID=" + AFStatistiquesOfasManager.journalisation
                    + ".JGJOID)" + " INNER JOIN (SELECT * FROM " + _getCollection()
                    + AFStatistiquesOfasManager.complementJournal + " WHERE JCJOTY="
                    + ILEConstantes.CS_DEF_FORMULE_GROUPE + " AND JCJOVA = " + ILEConstantes.CS_DEF_FORMULE_ATT_DS
                    + ") AS CPL ON(CPL.JJOUID=" + AFStatistiquesOfasManager.journalisation
                    + ".JJOUID) INNER JOIN (SELECT * FROM " + _getCollection()
                    + AFStatistiquesOfasManager.referenceProvenance + " WHERE JREPTY ="
                    + this._dbWriteString(statement.getTransaction(), ILEConstantes.CS_PARAM_GEN_PERIODE)
                    + " AND JREPIP = " + this._dbWriteString(statement.getTransaction(), getAnnee())
                    + ") AS ANNEE ON (ANNEE.JJOUID = " + AFStatistiquesOfasManager.journalisation + ".JJOUID)";
        }
        if (getForFromLPP3().booleanValue()) {
            from = _getCollection() + AFStatistiquesOfasManager.controle;
        }

        return from;
    }

    /**
     * Renvoie la composante de tri de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        if (JadeStringUtil.isEmpty(getForOrder())) {
            return null;
        } else {
            return getForOrder();
        }
    }

    /**
     * Renvoie la composante de sélection de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForRequetePrinc().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(MADFIN=0 OR MADFIN >=" + this._dbWriteNumeric(statement.getTransaction(), getAnnee())
                    + "1231 OR (MADFIN=" + this._dbWriteNumeric(statement.getTransaction(), getAnneeMoinUn())
                    + "1231 AND MATTAF="
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_NON_ACTIF)
                    + " AND MATBRA="
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.BRANCHE_ECO_ETUDIANTS)
                    + ")) AND MADDEB<=" + this._dbWriteNumeric(statement.getTransaction(), getAnnee()) + "1231 "
                    + "AND " + AFStatistiquesOfasManager.journal + ".DATEVALEURCG BETWEEN "
                    + this._dbWriteNumeric(statement.getTransaction(), getAnnee()) + "0101 AND "
                    + this._dbWriteNumeric(statement.getTransaction(), getAnnee()) + "1231 AND MATTAF <> "
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_FICHIER_CENT)
                    + " GROUP BY MATTAF, MBTGEN";
        }
        // traitement du positionnement
        if (getForNaCotiMiniEtuRI().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(MADFIN=0 OR MADFIN >=" + this._dbWriteNumeric(statement.getTransaction(), getAnnee())
                    + "1231 OR (MADFIN=" + this._dbWriteNumeric(statement.getTransaction(), getAnneeMoinUn())
                    + "1231 AND MATTAF="
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_NON_ACTIF)
                    + " AND MATBRA="
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.BRANCHE_ECO_ETUDIANTS)
                    + ")) AND MADDEB<=" + this._dbWriteNumeric(statement.getTransaction(), getAnnee()) + "1231 "
                    + "AND " + AFStatistiquesOfasManager.journal + ".DATEVALEURCG BETWEEN "
                    + this._dbWriteNumeric(statement.getTransaction(), getAnnee()) + "0101 AND "
                    + this._dbWriteNumeric(statement.getTransaction(), getAnnee()) + "1231 AND MATTAF = "
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_NON_ACTIF)
                    + " AND (MATBRA = "
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.BRANCHE_ECO_ETUDIANTS)
                    + " OR MATBRA = "
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.BRANCHE_ECO_NON_ACTIFS_INSTITUTION)
                    + ") GROUP BY MATTAF, MBTGEN HAVING SUM(MONTANT)>0";
        }

        // traitement du positionnement
        if (getForNaCotiMiniNonEtuRI().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(MADFIN=0 OR MADFIN >=" + this._dbWriteNumeric(statement.getTransaction(), getAnnee())
                    + "1231) AND MADDEB<=" + this._dbWriteNumeric(statement.getTransaction(), getAnnee()) + "1231 "
                    + " AND ANNEE=" + this._dbWriteNumeric(statement.getTransaction(), getAnnee()) + " AND MATBRA<>"
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.BRANCHE_ECO_ETUDIANTS)
                    + " AND MATBRA<>"
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.BRANCHE_ECO_NON_ACTIFS_INSTITUTION)
                    + " AND MATTAF="
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_NON_ACTIF)
                    + " AND CUMULCOTISATION BETWEEN 1 AND "
                    + this._dbWriteNumeric(statement.getTransaction(), getMontantMinime());
        }

        // traitement du positionnement
        if (getForIndEmpl().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(MADFIN=0 OR MADFIN >=" + this._dbWriteNumeric(statement.getTransaction(), getAnnee())
                    + "1231) AND MADDEB<=" + this._dbWriteNumeric(statement.getTransaction(), getAnnee()) + "1231 "
                    + " AND MATTAF<>"
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_FICHIER_CENT) + " AND "
                    + AFStatistiquesOfasManager.journal + ".DATEVALEURCG BETWEEN "
                    + this._dbWriteNumeric(statement.getTransaction(), getAnnee()) + "0101 AND "
                    + this._dbWriteNumeric(statement.getTransaction(), getAnnee())
                    + "1231 AND HTITIE IN (SELECT HTITIE FROM " + _getCollection()
                    + AFStatistiquesOfasManager.affiliation + " WHERE MATTAF IN ("
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_INDEP) + ","
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_INDEP_EMPLOY)
                    + ") AND (MADFIN = 0 OR MADFIN >=" + this._dbWriteNumeric(statement.getTransaction(), getAnnee())
                    + "1231)) GROUP BY HTITIE HAVING SUM(CASE WHEN MBTGEN="
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.GENRE_ASS_PARITAIRE)
                    + " THEN MONTANT ELSE 0 END)>0 AND SUM (CASE WHEN MBTGEN="
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.GENRE_ASS_PERSONNEL)
                    + " THEN MONTANT ELSE 0 END)>0) AS TEMP";
        }

        // traitement du positionnement
        if (getForNaPersMaison().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(MADFIN=0 OR MADFIN >=" + this._dbWriteNumeric(statement.getTransaction(), getAnnee())
                    + "1231) AND MADDEB<=" + this._dbWriteNumeric(statement.getTransaction(), getAnnee()) + "1231 "
                    + " AND MATTAF<>"
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_FICHIER_CENT) + " AND "
                    + AFStatistiquesOfasManager.journal + ".DATEVALEURCG BETWEEN "
                    + this._dbWriteNumeric(statement.getTransaction(), getAnnee()) + "0101 AND "
                    + this._dbWriteNumeric(statement.getTransaction(), getAnnee())
                    + "1231 AND HTITIE IN (SELECT HTITIE FROM " + _getCollection()
                    + AFStatistiquesOfasManager.affiliation + " WHERE MATTAF IN ("
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_NON_ACTIF)
                    + ") AND (MADFIN = 0 OR MADFIN >=" + this._dbWriteNumeric(statement.getTransaction(), getAnnee())
                    + "1231)) GROUP BY HTITIE HAVING SUM(CASE WHEN MBTGEN="
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.GENRE_ASS_PARITAIRE)
                    + " THEN MONTANT ELSE 0 END)>0 AND SUM (CASE WHEN MBTGEN="
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.GENRE_ASS_PERSONNEL)
                    + " THEN MONTANT ELSE 0 END)>0) AS TEMP";
        }

        // traitement du positionnement
        if (getForTsePersMaison().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(MADFIN=0 OR MADFIN >=" + this._dbWriteNumeric(statement.getTransaction(), getAnnee())
                    + "1231) AND MADDEB<=" + this._dbWriteNumeric(statement.getTransaction(), getAnnee()) + "1231 "
                    + " AND MATTAF<>"
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_FICHIER_CENT) + " AND "
                    + AFStatistiquesOfasManager.journal + ".DATEVALEURCG BETWEEN "
                    + this._dbWriteNumeric(statement.getTransaction(), getAnnee()) + "0101 AND "
                    + this._dbWriteNumeric(statement.getTransaction(), getAnnee())
                    + "1231 AND HTITIE IN (SELECT HTITIE FROM " + _getCollection()
                    + AFStatistiquesOfasManager.affiliation + " WHERE MATTAF IN ("
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_TSE) + ","
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE)
                    + ") AND (MADFIN = 0 OR MADFIN >=" + this._dbWriteNumeric(statement.getTransaction(), getAnnee())
                    + "1231)) GROUP BY HTITIE HAVING SUM(CASE WHEN MBTGEN="
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.GENRE_ASS_PARITAIRE)
                    + " THEN MONTANT ELSE 0 END)>0 AND SUM (CASE WHEN MBTGEN="
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.GENRE_ASS_PERSONNEL)
                    + " THEN MONTANT ELSE 0 END)>0) AS TEMP";
        }

        // traitement du positionnement
        if (getForTotalAffilie().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(MADFIN=0 OR MADFIN >=" + this._dbWriteNumeric(statement.getTransaction(), getAnnee())
                    + "1231 OR (MADFIN=" + this._dbWriteNumeric(statement.getTransaction(), getAnneeMoinUn())
                    + "1231 AND MATTAF="
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_NON_ACTIF)
                    + " AND MATBRA="
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.BRANCHE_ECO_ETUDIANTS)
                    + ")) AND MADDEB<=" + this._dbWriteNumeric(statement.getTransaction(), getAnnee()) + "1231 "
                    + " AND MATTAF <> "
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_FICHIER_CENT);
        }
        // traitement du positionnement
        if (getForNbrEmployes().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += AFStatistiquesOfasManager.declaration + ".TATTYP="
                    + this._dbWriteNumeric(statement.getTransaction(), DSDeclarationViewBean.CS_LTN) + " AND "
                    + AFStatistiquesOfasManager.declaration + ".TAANNE="
                    + this._dbWriteNumeric(statement.getTransaction(), getAnnee()) + " AND "
                    + AFStatistiquesOfasManager.declaration + ".TATETA="
                    + this._dbWriteNumeric(statement.getTransaction(), DSDeclarationViewBean.CS_COMPTABILISE);
        }
        if (getForMontantCoti().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += AFStatistiquesOfasManager.section + ".CATEGORIESECTION="
                    + this._dbWriteNumeric(statement.getTransaction(), APISection.ID_CATEGORIE_SECTION_LTN) + " AND "
                    + AFStatistiquesOfasManager.operation + ".ANNEECOTISATION="
                    + this._dbWriteNumeric(statement.getTransaction(), getAnnee()) + " AND "
                    + AFStatistiquesOfasManager.operation + ".ETAT IN ("
                    + this._dbWriteNumeric(statement.getTransaction(), APIOperation.ETAT_COMPTABILISE) + ","
                    + this._dbWriteNumeric(statement.getTransaction(), APIOperation.ETAT_PROVISOIRE) + ") AND "
                    + AFStatistiquesOfasManager.operation + ".IDTYPEOPERATION="
                    + this._dbWriteString(statement.getTransaction(), APIOperation.CAECRITURE);
        }
        if (getForExcluLTN().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MADFIN >=" + this._dbWriteNumeric(statement.getTransaction(), getAnnee())
                    + "0101 AND MADFIN<=" + this._dbWriteNumeric(statement.getTransaction(), getAnneeMoinUn())
                    + "1231 AND MADDEB<=" + this._dbWriteNumeric(statement.getTransaction(), getAnnee()) + "1231 "
                    + " AND MATMOT="
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.MOTIF_FIN_EXCLUSION_LTN);
        }
        if (getForNbrEmployeurLPP().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += AFStatistiquesOfasManager.affiliation + ".MADCRE <="
                    + this._dbWriteNumeric(statement.getTransaction(), getAnnee()) + "1231 AND "
                    + AFStatistiquesOfasManager.affiliation + ".MADCRE >="
                    + this._dbWriteNumeric(statement.getTransaction(), getAnnee()) + "0101 GROUP BY malnaf) as malnaf";
        }
        if (getForNbrEmployeurDS().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += AFStatistiquesOfasManager.journalisation + ".JJOUID IN (SELECT JJOUID FROM " + _getCollection()
                    + AFStatistiquesOfasManager.complementJournal + " WHERE JCJOTY="
                    + this._dbWriteNumeric(statement.getTransaction(), ILEConstantes.CS_CATEGORIE_GROUPE)
                    + " AND (JCJOVA = "
                    + this._dbWriteNumeric(statement.getTransaction(), ILEConstantes.CS_CATEGORIE_SUIVI_DS) + "))";
        }
        if (getForNbrControleEmpl().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MDDEFF >=" + this._dbWriteNumeric(statement.getTransaction(), getAnnee())
                    + "0101 AND MDDEFF <=" + this._dbWriteNumeric(statement.getTransaction(), getAnnee()) + "1231";
        }
        // traitement du positionnement
        if (getForMontantCotiInd().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += AFStatistiquesOfasManager.journal + ".DATEVALEURCG BETWEEN "
                    + this._dbWriteNumeric(statement.getTransaction(), getAnnee()) + "0101 AND "
                    + this._dbWriteNumeric(statement.getTransaction(), getAnnee()) + "1231 AND MATTAF <> "
                    + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_AFFILI_FICHIER_CENT)
                    + " GROUP BY MATTAF, MBTGEN";
        }

        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité.
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFStatistiquesOfas();
    }

    public String getAnnee() {
        return annee;
    }

    public String getAnneeMoinUn() {
        if (JadeStringUtil.isBlankOrZero(anneeMoinUn) && !JadeStringUtil.isBlank(getAnnee())) {
            setAnneeMoinUn("" + (JadeStringUtil.toInt(annee) - 1));
        }
        return anneeMoinUn;
    }

    public Boolean getForExcluLTN() {
        return forExcluLTN;
    }

    public Boolean getForFields1() {
        return forFields1;
    }

    public Boolean getForFields2() {
        return forFields2;
    }

    public Boolean getForFields3() {
        return forFields3;
    }

    public Boolean getForFields4() {
        return forFields4;
    }

    public Boolean getForFieldsLTN3() {
        return forFieldsLTN3;
    }

    public Boolean getForFieldsLTN4() {
        return forFieldsLTN4;
    }

    public Boolean getForFrom1() {
        return forFrom1;
    }

    public Boolean getForFrom2() {
        return forFrom2;
    }

    public Boolean getForFrom3() {
        return forFrom3;
    }

    public Boolean getForFrom4() {
        return forFrom4;
    }

    public Boolean getForFrom5() {
        return forFrom5;
    }

    public Boolean getForFromLPP1() {
        return forFromLPP1;
    }

    public Boolean getForFromLPP2() {
        return forFromLPP2;
    }

    public Boolean getForFromLPP3() {
        return forFromLPP3;
    }

    public Boolean getForFromLTN2() {
        return forFromLTN2;
    }

    public Boolean getForFromLTN3() {
        return forFromLTN3;
    }

    public Boolean getForFromMontant() {
        return forFromMontant;
    }

    public Boolean getForIndEmpl() {
        return forIndEmpl;
    }

    public Boolean getForMontantCoti() {
        return forMontantCoti;
    }

    public Boolean getForMontantCotiInd() {
        return forMontantCotiInd;
    }

    public Boolean getForNaCotiMiniEtuRI() {
        return forNaCotiMiniEtuRI;
    }

    public Boolean getForNaCotiMiniNonEtuRI() {
        return forNaCotiMiniNonEtuRI;
    }

    public Boolean getForNaPersMaison() {
        return forNaPersMaison;
    }

    public Boolean getForNbrControleEmpl() {
        return forNbrControleEmpl;
    }

    public Boolean getForNbrEmployes() {
        return forNbrEmployes;
    }

    public Boolean getForNbrEmployeurDS() {
        return forNbrEmployeurDS;
    }

    public Boolean getForNbrEmployeurLPP() {
        return forNbrEmployeurLPP;
    }

    public String getForOrder() {
        return forOrder;
    }

    public Boolean getForRechercheCanton() {
        return forRechercheCanton;
    }

    public Boolean getForRequetePrinc() {
        return forRequetePrinc;
    }

    public Boolean getForTotalAffilie() {
        return forTotalAffilie;
    }

    public Boolean getForTsePersMaison() {
        return forTsePersMaison;
    }

    public String getMontantMinime() {
        return montantMinime;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setAnneeMoinUn(String anneeMoinUn) {
        this.anneeMoinUn = anneeMoinUn;
    }

    public void setForExcluLTN(Boolean forExcluLTN) {
        this.forExcluLTN = forExcluLTN;
    }

    public void setForFields1(Boolean forFields1) {
        this.forFields1 = forFields1;
    }

    public void setForFields2(Boolean forFields2) {
        this.forFields2 = forFields2;
    }

    public void setForFields3(Boolean forFields3) {
        this.forFields3 = forFields3;
    }

    public void setForFields4(Boolean forFields4) {
        this.forFields4 = forFields4;
    }

    public void setForFieldsLTN3(Boolean forFieldsLTN3) {
        this.forFieldsLTN3 = forFieldsLTN3;
    }

    public void setForFieldsLTN4(Boolean forFieldsLTN4) {
        this.forFieldsLTN4 = forFieldsLTN4;
    }

    public void setForFrom1(Boolean forFrom1) {
        this.forFrom1 = forFrom1;
    }

    public void setForFrom2(Boolean forFrom2) {
        this.forFrom2 = forFrom2;
    }

    public void setForFrom3(Boolean forFrom3) {
        this.forFrom3 = forFrom3;
    }

    public void setForFrom4(Boolean forFrom4) {
        this.forFrom4 = forFrom4;
    }

    public void setForFrom5(Boolean forFrom5) {
        this.forFrom5 = forFrom5;
    }

    public void setForFromLPP1(Boolean forFromLPP1) {
        this.forFromLPP1 = forFromLPP1;
    }

    public void setForFromLPP2(Boolean forFromLPP2) {
        this.forFromLPP2 = forFromLPP2;
    }

    public void setForFromLPP3(Boolean forFromLPP3) {
        this.forFromLPP3 = forFromLPP3;
    }

    public void setForFromLTN2(Boolean forFromLTN2) {
        this.forFromLTN2 = forFromLTN2;
    }

    public void setForFromLTN3(Boolean forFromLTN3) {
        this.forFromLTN3 = forFromLTN3;
    }

    public void setForFromMontant(Boolean forFromMontant) {
        this.forFromMontant = forFromMontant;
    }

    public void setForIndEmpl(Boolean forIndEmpl) {
        this.forIndEmpl = forIndEmpl;
    }

    public void setForMontantCoti(Boolean forMontantCoti) {
        this.forMontantCoti = forMontantCoti;
    }

    public void setForMontantCotiInd(Boolean forMontantCotiInd) {
        this.forMontantCotiInd = forMontantCotiInd;
    }

    public void setForNaCotiMiniEtuRI(Boolean forNaCotiMiniEtuRI) {
        this.forNaCotiMiniEtuRI = forNaCotiMiniEtuRI;
    }

    public void setForNaCotiMiniNonEtuRI(Boolean forNaCotiMiniNonEtuRI) {
        this.forNaCotiMiniNonEtuRI = forNaCotiMiniNonEtuRI;
    }

    public void setForNaPersMaison(Boolean forNaPersMaison) {
        this.forNaPersMaison = forNaPersMaison;
    }

    public void setForNbrControleEmpl(Boolean forNbrControleEmpl) {
        this.forNbrControleEmpl = forNbrControleEmpl;
    }

    public void setForNbrEmployes(Boolean forNbrEmployes) {
        this.forNbrEmployes = forNbrEmployes;
    }

    public void setForNbrEmployeurDS(Boolean forNbrEmployeurDS) {
        this.forNbrEmployeurDS = forNbrEmployeurDS;
    }

    public void setForNbrEmployeurLPP(Boolean forNbrEmployeurLPP) {
        this.forNbrEmployeurLPP = forNbrEmployeurLPP;
    }

    public void setForOrder(String forOrder) {
        this.forOrder = forOrder;
    }

    public void setForRechercheCanton(Boolean forRechercheCanton) {
        this.forRechercheCanton = forRechercheCanton;
    }

    public void setForRequetePrinc(Boolean forRequetePrinc) {
        this.forRequetePrinc = forRequetePrinc;
    }

    public void setForTotalAffilie(Boolean forTotalAffilie) {
        this.forTotalAffilie = forTotalAffilie;
    }

    public void setForTsePersMaison(Boolean forTsePersMaison) {
        this.forTsePersMaison = forTsePersMaison;
    }

    public void setMontantMinime(String montantMinime) {
        this.montantMinime = montantMinime;
    }

}
