package globaz.naos.db.cotisation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Manager destiner au recherches de cotisations
 * 
 * 
 * si path = PLAN_AFFILIATION ( A UTILISER de préférence, sauf cas très particulier): Permet de trouver toutes les
 * cotisations, mais pas forcement toutes les adhésions...)
 * 
 * 
 * TIERS -> AFFILIATION -> PLAN AFFILIATION -> COTISATION -> ASSURANCE \-> ADHESION -> PLAN CAISSE -> CAISSE(TIERS) ->
 * ADMINSTRATION
 * 
 * si path = ADHESION (Attention...): Permet de trouver toutes les adhésions, mais pas forcement toutes les
 * cotisations...)
 * 
 * TIERS -> AFFILIATION -> ADHESION -> COTISATION -> ASSURANCE | \ -> PLAN AFFILIATION \ -> PLAN CAISSE -> CAISSE(TIERS)
 * -> ADMINSTRATION
 * 
 * 
 * @author oca
 * 
 */
public class AFCotiSearchManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static int PATH_ADHESION = 2;
    public final static int PATH_PLAN_AFFILIATION = 1;

    public final static String TRI_PAR_AFFIE_ADHESION = "MADFIN ,afaffipMAIAFF,MRDFIN,afadhepMRIADH,MEDFIN"; // affilié,
    public final static String TRI_PAR_AFFILIE_GENRE_ASSURANCE = "MADFIN ,afaffipMAIAFF,MBTGEN,afplafpMUIPLA,MEDFIN"; // affilié,
    // genre
    // assurance,
    // plan
    // affiliation,
    // cotisation
    // adhésion,
    // cotisation
    public final static String TRI_PAR_GENRE_ASSURANCE = "MBTGEN,afplafpMUIPLA,afaffipMAIAFF,MADFIN desc,MEDFIN"; // affilié,

    // genre
    // assurance,
    // plan
    // affiliation,
    // cotisation
    public final static String TRI_PAR_PLAN_AFFILIATION = "afplafpMUIPLA,afplafpMULLIB,afaffipMAIAFF,MADFIN desc,MEDFIN"; // affilié,
    // genre
    // assurance,
    // plan
    // affiliation,
    // cotisation
    /*
     * expressions complexes
     */
    private String forAnneeActive = "";

    private boolean forCotiActiveTodayJoin = false;

    private Boolean forCotisationSansAdhesion = new Boolean(false);
    /*
     * Expressions simples
     */
    private String forIdTiers = "";
    private String forNumeroAffilie = "";

    /*
     * Tri
     */
    private String order = "";

    public int path = AFCotiSearchManager.PATH_PLAN_AFFILIATION;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "aff.HTLDE1  affHTLDE1," + // nom de l'affilié
                "aff.HTLDE2  affHTLDE2," + // nom de l'affilié
                "aff.HTLDE3  affHTLDE3," + // nom de l'affilié
                "aff.HTLDE4  affHTLDE4," + // nom de l'affilié

                _getCollection() + "AFAFFIP.HTITIE  afaffipHTITIE," + // id du
                // tiers
                _getCollection() + "AFAFFIP.MAIAFF  afaffipMAIAFF," + // id
                // affiliation
                _getCollection() + "AFAFFIP.MALNAF  afaffipMALNAF," + // numero
                // affilié
                _getCollection() + "AFAFFIP.MADDEB  afaffipMADDEB," + // date
                // debut
                // affiliation
                _getCollection() + "AFAFFIP.MADFIN  afaffipMADFIN," + // date
                // fin
                // affiliation
                _getCollection() + "AFAFFIP.MATTAF  afaffipMATTAF," + // type
                // affiliation
                _getCollection() + "AFAFFIP.MATMOT  afaffipMATMOT," + // motif
                // de
                // fin
                // d'affiliation
                _getCollection() + "AFAFFIP.MABREP  afaffipMABREP," + // au
                // relevé
                // (=1
                // seul.
                // paritaire)
                // aux
                // acomptes
                // (=2)
                _getCollection() + "AFAFFIP.MABEAA  afaffipMABEAA," + // impression
                // automatique
                // des
                // relevés
                // à
                // blanc
                _getCollection() + "AFAFFIP.MATPER  afaffipMATPER," + // periodicite
                // de
                // l'affiliation
                _getCollection() + "AFAFFIP.MADESL  afaffipMADESL," + // raison
                // sociale
                // de
                // l'affiliation
                _getCollection() + "AFAFFIP.MATBRA  afaffipMATBRA," + // branche
                // économique

                _getCollection() + "AFPLAFP.MUIPLA  afplafpMUIPLA," + // id du
                // plan
                // d'affiliation
                _getCollection() + "AFPLAFP.MULLIB  afplafpMULLIB," + // libelle
                // du
                // plan
                // d'affiliation
                _getCollection() + "AFPLAFP.MULFAC  afplafpMULFAC," + // libelle
                // facture
                // du
                // plan
                // d'affiliation
                _getCollection() + "AFPLAFP.MUBBLO  afplafpMUBBLO," + // blocage
                // envoi
                // du
                // plan
                // d'affiliation
                _getCollection() + "AFPLAFP.MUBINA  afplafpMUBINA," + // plan
                // d'affiliation
                // actif
                // /
                // inactif

                _getCollection() + "AFPLAFP.HETTAD  afplafpHETTAD," + // type
                // d'adresse
                // du
                // plan
                // d'affiliation
                _getCollection() + "AFPLAFP.HFIAPP  afplafpHFIAPP," + // domaine
                // de
                // courrier
                // du
                // plan
                // d'affiliation
                _getCollection() + "AFPLAFP.HFIAPL  afplafpHFIAPL," + // domaine
                // de
                // recouvrement
                // du
                // plan
                // d'affiliation
                _getCollection() + "AFPLAFP.HFIAPR  afplafpHFIAPR," + // domaine
                // de
                // remboursement
                // du
                // plan
                // d'affiliation

                _getCollection() + "AFASSUP.MBLLII  afassupMBLLII," + // libelle
                // assurance
                // (italien)
                _getCollection() + "AFASSUP.MBLLIF  afassupMBLLIF," + // libelle
                // assurance
                // (français)
                _getCollection() + "AFASSUP.MBLLID  afassupMBLLID," + // libelle
                // assurance
                // (allemand)
                _getCollection() + "AFASSUP.MBTTYP  afassupMBTTYP," + // type de
                // l'assurance
                _getCollection() + "AFASSUP.MBTGEN  afassupMBTGEN," + // genre
                // de
                // l'assurance

                _getCollection() + "AFCOTIP.MEICOT  afcotipMEICOT," + // id de
                // la
                // cotisation
                _getCollection() + "AFCOTIP.MEDDEB  afcotipMEDDEB," + // date de
                // début
                // de la
                // cotisation
                _getCollection() + "AFCOTIP.MEDFIN  afcotipMEDFIN," + // date de
                // fin
                // de la
                // cotisation
                _getCollection() + "AFCOTIP.METMOT  afcotipMETMOT," + // motif
                // de
                // fin
                // de la
                // cotisation
                _getCollection() + "AFCOTIP.METPER  afcotipMETPER," + // periodicite
                // de la
                // cotisation
                _getCollection() + "AFCOTIP.MEMMAP  afcotipMEMMAP," + // masse
                // annuelle
                // de la
                // cotisation
                _getCollection() + "AFCOTIP.MEMTRI  afcotipMEMTRI," + // montant
                // trimestriel
                // de la
                // cotisation
                _getCollection() + "AFCOTIP.MEMSEM  afcotipMEMSEM," + // montant
                // semestriel
                // de la
                // cotisation
                _getCollection() + "AFCOTIP.MEMANN  afcotipMEMANN," + // montant
                // annuel
                // de la
                // cotisation
                _getCollection() + "AFCOTIP.MEMMEN  afcotipMEMMEN," + // montant
                // mensuel
                // de la
                // cotisation
                _getCollection() + "AFCOTIP.MCITAU  afcotipMCITAU," + // taux
                // spécifique
                // pour
                // la
                // cotisation

                _getCollection() + "AFADHEP.MRIADH  afadhepMRIADH," + // type
                // d'adhésion
                _getCollection() + "AFADHEP.MRTADH  afadhepMRTADH," + // type
                // d'adhésion
                _getCollection() + "AFADHEP.MRDDEB  afadhepMRDDEB," + // date de
                // début
                // de
                // l'adhésion
                _getCollection() + "AFADHEP.MRDFIN  afadhepMRDFIN," + // date de
                // fin
                // de
                // l'adhésion

                "caisse.HTLDE1  caisseHTLDE1," + // nom de la caisse
                "caisse.HTLDE2  caisseHTLDE2," + // nom de la caisse
                "caisse.HTLDE3  caisseHTLDE3," + // nom de la caisse
                "caisse.HTLDE4  caisseHTLDE4," + // nom de la caisse

                _getCollection() + "TIADMIP.HBCADM  tiadmipHBCADM" + // code de
                // la
                // caisse

                "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String fromClause = _getCollection() + "TITIERP aff";

        // affiliation - tiers
        fromClause += " INNER JOIN " + _getCollection() + "AFAFFIP ON (" + _getCollection() + "AFAFFIP.HTITIE="
                + "aff.HTITIE)";

        switch (path) {
            case PATH_PLAN_AFFILIATION:
                // affiliation - plan affiliation
                fromClause += " LEFT OUTER JOIN " + _getCollection() + "AFPLAFP ON (" + _getCollection()
                        + "AFAFFIP.MAIAFF=" + _getCollection() + "AFPLAFP.MAIAFF)";
                // plan affiliation - cotisations
                if (!isForCotiActiveTodayJoin()) {
                    fromClause += " LEFT OUTER JOIN " + _getCollection() + "AFCOTIP ON (" + _getCollection()
                            + "AFPLAFP.MUIPLA=" + _getCollection() + "AFCOTIP.MUIPLA)";
                } else {
                    String date = JACalendar.today().toStrAMJ();
                    String cotiActiveFilter = " AND ((" + date
                            + " between MEDDEB and MEDFIN) or (MEDFIN=0 and MEDDEB<= " + date + " ))";

                    fromClause += " LEFT OUTER JOIN " + _getCollection() + "AFCOTIP ON (" + _getCollection()
                            + "AFPLAFP.MUIPLA=" + _getCollection() + "AFCOTIP.MUIPLA " + cotiActiveFilter + ")";
                }
                // cotisation - assurance
                fromClause += " LEFT OUTER JOIN " + _getCollection() + "AFASSUP ON (" + _getCollection()
                        + "AFCOTIP.MBIASS=" + _getCollection() + "AFASSUP.MBIASS)";
                // cotisation - adhésion
                fromClause += " LEFT OUTER JOIN " + _getCollection() + "AFADHEP ON (" + _getCollection()
                        + "AFCOTIP.MRIADH=" + _getCollection() + "AFADHEP.MRIADH)";
                // adhésion - plan caisse
                fromClause += " LEFT OUTER JOIN " + _getCollection() + "AFPLCAP ON (" + _getCollection()
                        + "AFADHEP.MSIPLC=" + _getCollection() + "AFPLCAP.MSIPLC)";
                // plan caisse - caisse métier (tiers)
                fromClause += " LEFT OUTER JOIN " + _getCollection() + "TITIERP caisse ON (" + _getCollection()
                        + "AFPLCAP.HTITIE=" + "caisse.HTITIE)";
                // caisse métier (tiers ) - administration
                fromClause += " LEFT OUTER JOIN " + _getCollection() + "TIADMIP ON (" + "caisse.HTITIE="
                        + _getCollection() + "TIADMIP.HTITIE)";

                break;

            case PATH_ADHESION:
                // affiliation - adhesion
                fromClause += " LEFT OUTER JOIN " + _getCollection() + "AFADHEP ON (" + _getCollection()
                        + "AFAFFIP.MAIAFF=" + _getCollection() + "AFADHEP.MAIAFF)";
                // adhésion - plan caisse
                fromClause += " LEFT OUTER JOIN " + _getCollection() + "AFPLCAP ON (" + _getCollection()
                        + "AFADHEP.MSIPLC=" + _getCollection() + "AFPLCAP.MSIPLC)";
                // plan caisse - caisse métier (tiers)
                fromClause += " LEFT OUTER JOIN " + _getCollection() + "TITIERP caisse ON (" + _getCollection()
                        + "AFPLCAP.HTITIE=" + "caisse.HTITIE)";
                // caisse métier (tiers ) - administration
                fromClause += " LEFT OUTER JOIN " + _getCollection() + "TIADMIP ON (" + "caisse.HTITIE="
                        + _getCollection() + "TIADMIP.HTITIE)";

                // adhesion - cotisations
                if (!isForCotiActiveTodayJoin()) {
                    fromClause += " LEFT OUTER JOIN " + _getCollection() + "AFCOTIP ON (" + _getCollection()
                            + "AFCOTIP.MRIADH=" + _getCollection() + "AFADHEP.MRIADH)";
                } else {
                    String date = JACalendar.today().toStrAMJ();
                    String cotiActiveFilter = " AND ((" + date
                            + " between MEDDEB and MEDFIN) or (MEDFIN=0 and MEDDEB<= " + date + " ))";
                    fromClause += " LEFT OUTER JOIN " + _getCollection() + "AFCOTIP ON (" + _getCollection()
                            + "AFCOTIP.MRIADH=" + _getCollection() + "AFADHEP.MRIADH " + cotiActiveFilter + ")";

                }
                // cotisation - assurance
                fromClause += " LEFT OUTER JOIN " + _getCollection() + "AFASSUP ON (" + _getCollection()
                        + "AFCOTIP.MBIASS=" + _getCollection() + "AFASSUP.MBIASS)";

                // cotisation - plan affiliation
                fromClause += " LEFT OUTER JOIN " + _getCollection() + "AFPLAFP ON (" + _getCollection()
                        + "AFCOTIP.MUIPLA=" + _getCollection() + "AFPLAFP.MUIPLA)";
                break;
        }

        return fromClause;

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return order;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        /*
         * numéro affilié
         */
        if (!JadeStringUtil.isEmpty((getForNumeroAffilie()))) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MALNAF = " + this._dbWriteString(statement.getTransaction(), getForNumeroAffilie());
        }
        /*
         * Id tiers
         */
        if (!JadeStringUtil.isEmpty((getForIdTiers()))) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFAFFIP.HTITIE = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTiers());
        }

        /*
         * cotisation de l'année active
         */
        if (!JadeStringUtil.isEmpty(getForAnneeActive())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            // String dateMax =
            // _dbWriteDateAMJ(statement.getTransaction(), "31.12." +
            // getForAnneeActive());
            sqlWhere += "MEDDEB<" + getForAnneeActive() + "1231 " + " AND ( MEDFIN=0 OR MEDFIN>" + getForAnneeActive()
                    + "0101" + " )";
        }

        /*
         * cotisations sans adhésion
         */
        if (getForCotisationSansAdhesion().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFCOTIP.MRIADH = 0 ";
        }

        return sqlWhere;

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFCotiSearchEntity();
    }

    public String getForAnneeActive() {
        return forAnneeActive;
    }

    public Boolean getForCotisationSansAdhesion() {
        return forCotisationSansAdhesion;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    public int getPath() {
        return path;
    }

    public boolean isForCotiActiveTodayJoin() {
        return forCotiActiveTodayJoin;
    }

    public void setForAnneeActive(String forAnneeActive) {
        this.forAnneeActive = forAnneeActive;
    }

    public void setForCotiActiveTodayJoin(boolean forCotiActiveTodayJoin) {
        this.forCotiActiveTodayJoin = forCotiActiveTodayJoin;
    }

    public void setForCotisationSansAdhesion(Boolean forCotisationSansAdhesion) {
        this.forCotisationSansAdhesion = forCotisationSansAdhesion;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForNumeroAffilie(String forNumeroAffilie) {
        this.forNumeroAffilie = forNumeroAffilie;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setPath(int path) {
        this.path = path;
    }

}
