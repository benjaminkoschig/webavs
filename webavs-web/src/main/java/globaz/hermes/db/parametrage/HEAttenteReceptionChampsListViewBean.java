package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.db.gestion.HELotViewBean;

/**
 * Insérez la description du type ici. Date de création : (25.03.2003 11:18:52)
 * 
 * @author: Administrator
 */
public class HEAttenteReceptionChampsListViewBean extends HEAttenteEnvoiChampsListViewBean implements
        FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur HEAttenteReceptionChampsListViewBean.
     */
    public HEAttenteReceptionChampsListViewBean() {
        super();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        // choisir tout d'abord les bonnes tables en fonction de l'archivage
        String heannop = "";
        String helotsp = "";
        if (getIsArchivage()) {
            heannop = HEANNOP_TBL_ARCHIVE;
            helotsp = HELOTSP_TBL_ARCHIVE;
        } else {
            heannop = HEANNOP_TBL_EN_COURS;
            helotsp = HELOTSP_TBL_EN_COURS;
        }
        return " (select distinct SUBSTR("
                + _getCollection()
                + heannop
                + ".RNLENR, 1, 2) AS CODEAPP, "
                + _getCollection()
                + heannop
                + ".RNLENR,"
                + _getCollection()
                + heannop
                + ".RMILOT,"
                + _getCollection()
                + heannop
                + ".RNREFU,"
                + _getCollection()
                + heannop
                + ".RNIANN,"
                + _getCollection()
                + heannop
                + ".RNDDAN,"
                + _getCollection()
                + heannop
                + ".RNLUTI,"
                + _getCollection()
                + heannop
                + ".RNTPRO,"
                + _getCollection()
                + heannop
                + ".RNTSTA,"
                + _getCollection()
                + heannop
                + ".RNTMES,"
                + _getCollection()
                + helotsp
                + ".RMILOT, "
                + _getCollection()
                + helotsp
                + ".RMTTYP, "
                + _getCollection()
                + "FWCOUP.PCOUID, "
                + _getCollection()
                + "FWCOUP.PCOLUT, "
                + _getCollection()
                + "FWCOUP.PCOSID, "
                + _getCollection()
                + "HEPAREP.REIPAE, SUBSTR("
                + _getCollection()
                + heannop
                + ".RNLENR, CAST("
                + _getCollection()
                + "HECHANP.RDNDEB AS INTEGER), CAST("
                + _getCollection()
                + "HECHANP.RDNLON AS INTEGER)) AS CODENR, SUBSTR("
                + _getCollection()
                + heannop
                + ".RNLENR, CAST(C2.RDNDEB AS INTEGER), CAST(C2.RDNLON AS INTEGER)) AS CHAMP, C2.RDTCHA AS IDCHAMP, C2.RDNDEB, C2.RDNLON, C2LIB.PCOLUT AS CHAMPLIB" /*
                                                                                                                                                                     * +
                                                                                                                                                                     * ",
                                                                                                                                                                     * SUBSTR
                                                                                                                                                                     * (
                                                                                                                                                                     * "
                                                                                                                                                                     * +
                                                                                                                                                                     * _getCollection
                                                                                                                                                                     * (
                                                                                                                                                                     * )
                                                                                                                                                                     * +
                                                                                                                                                                     * "HEANNOP.RNLENR,
                                                                                                                                                                     * CAST
                                                                                                                                                                     * (
                                                                                                                                                                     * C3
                                                                                                                                                                     * .
                                                                                                                                                                     * RDNDEB
                                                                                                                                                                     * AS
                                                                                                                                                                     * INTEGER
                                                                                                                                                                     * )
                                                                                                                                                                     * ,
                                                                                                                                                                     * CAST
                                                                                                                                                                     * (
                                                                                                                                                                     * C3
                                                                                                                                                                     * .
                                                                                                                                                                     * RDNLON
                                                                                                                                                                     * AS
                                                                                                                                                                     * INTEGER
                                                                                                                                                                     * )
                                                                                                                                                                     * )
                                                                                                                                                                     * AS
                                                                                                                                                                     * CODE12
                                                                                                                                                                     * "
                                                                                                                                                                     */
                + " FROM " + _getCollection() + helotsp + ", " + _getCollection() + heannop + ", " + _getCollection()
                + "FWCOUP, " + _getCollection() + "FWCOSP, " + _getCollection() + "HEPAREP, " + _getCollection()
                + "HECHANP, " + _getCollection() + "HECHANP AS C2, " + _getCollection() + "FWCOUP AS C2LIB,"
                + _getCollection() + "HECHANP AS C3 " + " WHERE " + " C2LIB.PLAIDE = '" + getSession().getIdLangue()
                + "' " + "AND C2LIB.PCOSID=C2.RDTCHA" + " AND " + _getCollection() + heannop + ".RMILOT = "
                + _getCollection() + helotsp + ".RMILOT " + getLotReception(helotsp) + " AND " + _getCollection()
                + "FWCOUP.PCOUID = SUBSTR(" + _getCollection() + heannop + ".RNLENR, 1, 2) AND " + _getCollection()
                + "FWCOUP.PLAIDE = '" + getSession().getIdLangue() + "' AND " + _getCollection() + "FWCOUP.PCOSID = "
                + _getCollection() + "FWCOSP.PCOSID AND " + _getCollection() + "FWCOSP.PPTYGR = 'HECODAPP' AND "
                + _getCollection() + "HEPAREP.RETLIB = " + _getCollection() + "FWCOUP.PCOSID AND " + _getCollection()
                + "HECHANP.REIPAE = " + _getCollection() + "HEPAREP.REIPAE AND " + _getCollection()
                + "HECHANP.RDTCHA = " + IHEAnnoncesViewBean.CODE_ENREGISTREMENT + " AND " + _getCollection() + heannop
                + ".RNREFU = '" + getForRefUnique() + "' AND C2.REIPAE = " + _getCollection() + "HEPAREP.REIPAE AND "
                + _getCollection() + "HEPAREP.RENCED <= CAST(SUBSTR(" + _getCollection() + heannop + ".RNLENR,	CAST("
                + _getCollection() + "HECHANP.RDNDEB AS INTEGER),	CAST(" + _getCollection()
                + "HECHANP.RDNLON AS INTEGER)) AS INTEGER) AND " + _getCollection() + "HEPAREP.RENCEF	>= CAST(SUBSTR(	"
                + _getCollection() + heannop + ".RNLENR,	CAST(" + _getCollection()
                + "HECHANP.RDNDEB AS INTEGER), CAST(" + _getCollection()
                + "HECHANP.RDNLON AS INTEGER)) AS INTEGER) AND C3.RDTCHA=" + IHEAnnoncesViewBean.CODE_1_OU_2
                + " and (CONCAT(CAST(" + _getCollection() + "HEPAREP.REIPAE AS CHAR(2)),SUBSTR(" + _getCollection()
                + heannop
                + ".RNLENR, CAST(C3.RDNDEB AS INTEGER), CAST(C3.RDNLON AS INTEGER)) ) <> '82' AND CONCAT(CAST("
                + _getCollection() + "HEPAREP.REIPAE AS CHAR(2)),SUBSTR(" + _getCollection() + heannop
                + ".RNLENR, CAST(C3.RDNDEB AS INTEGER), CAST(C3.RDNLON AS INTEGER)) ) <> '131')) AS RCHAMPQUERY ";
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        // traitement du positionnement
        if (getLikeCodeApplication().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNLENR like '" + _dbWriteNumeric(statement.getTransaction(), getLikeCodeApplication()) + "%' ";
        }
        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité
     * 
     * @return la nouvelle entité
     * @exception java.lang.Exception
     *                si la création a échouée
     */
    @Override
    public BEntity _newEntity() throws Exception {
        return new HEAttenteReceptionChampsViewBean();
    }

    /**
     * @return
     */
    private String getLotReception(String helotsp) {
        String s = "";
        if (HELotViewBean.getLotReception().length > 0) {
            s += " AND (";
        }
        for (int i = 0; i < HELotViewBean.getLotReception().length; i++) {
            if (i > 0) {
                s += " OR ";
            }
            s += _getCollection();
            s += helotsp + ".RMTTYP = ";
            s += HELotViewBean.getLotReception()[i];
        }
        if (HELotViewBean.getLotReception().length > 0) {
            s += ")";
        }
        return s;
    }
}
