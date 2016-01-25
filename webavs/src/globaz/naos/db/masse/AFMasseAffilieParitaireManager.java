package globaz.naos.db.masse;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.naos.api.IAFAssurance;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.cotisation.AFCotisation;
import java.io.Serializable;

/**
 * Manager permettant de retrouver les affiliés actifs, trimestrielles et paritaires <br>
 * dont la masse salariale annuel est suppérieur à 200'000.-
 * 
 * @author SEL <br>
 *         Date : 15 févr. 08 <br>
 * @requete <pre>
 * select * from webavs.afaffip inner join (
 * 		select maiaff, memmap from webavs.AFPLAFP inner join (
 * 			select muipla, memmap from webavs.afcotip inner join (
 * 				select mbiass from webavs.afassup where webavs.afassup.mbtgen = 801001 and webavs.afassup.mbttyp = 812001
 * 			) ass on ass.mbiass = webavs.afcotip.mbiass where webavs.afcotip.medfin = 0
 * 		) coti on coti.muipla = webavs.AFPLAFP.muipla
 * 	) plan on plan.maiaff = webavs.afaffip.maiaff
 * 	where webavs.afaffip.MADFIN = 0
 * 	and webavs.afaffip.MATPER = 802002
 * 	and memmap > 200000
 * </pre>
 */
public class AFMasseAffilieParitaireManager extends BManager implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String AND = " AND ";
    private static final String COTISATION_AVS = "812001";
    private static final String FROM = " FROM ";
    private static final String INNER_JOIN = " INNER JOIN ";

    private static final String MONTANT_LIMITE = "200000";
    private static final String ON = " ON ";
    private static final String SELECT = "SELECT ";
    private static final String TRIMESTRIELLE = "802002";
    private static final String WHERE = " WHERE ";
    private static final String ZERO = "0";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "aff.MAIAFF, aff.MALNAF, aff.MADDEB, MEMMAP, aff.HTITIE";
    }

    /**
     * Renvoie la clause FROM.
     * 
     * <pre>
     * from webavs.afaffip inner join (
     * 			getSqlIdAffiliation()
     * 		) plan on plan.maiaff = webavs.afaffip.maiaff
     * </pre>
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer fromClause = new StringBuffer(_getCollection() + AFAffiliation.TABLE_NAME + " aff");
        fromClause.append(AFMasseAffilieParitaireManager.INNER_JOIN + "(");
        fromClause.append(getSqlIdAffiliation());
        fromClause.append(") plan" + AFMasseAffilieParitaireManager.ON + "plan.MAIAFF = aff."
                + AFAffiliation.FIELDNAME_AFFILIATION_ID);

        return fromClause.toString();
    }

    /**
     * Renvoie la composante de tri de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "aff.MALNAF";
    }

    /**
     * Renvoie la composante de sélection de la requête SQL.
     * 
     * <pre>
     * where webavs.afaffip.MADFIN = 0
     * 		and webavs.afaffip.MATPER = 802002
     * 		and memmap > 100000
     * </pre>
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer("");
        sqlWhere.append("aff.MADFIN = " + AFMasseAffilieParitaireManager.ZERO);
        sqlWhere.append(AFMasseAffilieParitaireManager.AND);
        sqlWhere.append("aff.MATPER = " + AFMasseAffilieParitaireManager.TRIMESTRIELLE);
        sqlWhere.append(AFMasseAffilieParitaireManager.AND);
        sqlWhere.append("MEMMAP > " + AFMasseAffilieParitaireManager.MONTANT_LIMITE);
        return sqlWhere.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFMasseAffilieParitaire();
    }

    /**
     * <pre>
     * select maiaff, memmap from webavs.AFPLAFP inner join (
     * 			getSqlIdPlanAffiliation()
     * ) coti on coti.muipla = webavs.AFPLAFP.muipla
     * 
     * <pre>
     * @return
     */
    private String getSqlIdAffiliation() {
        StringBuffer sql = new StringBuffer("");
        sql.append(AFMasseAffilieParitaireManager.SELECT);
        sql.append("pla.MAIAFF" + ", MEMMAP");
        sql.append(AFMasseAffilieParitaireManager.FROM);
        sql.append(_getCollection() + "AFPLAFP" + " pla");
        sql.append(AFMasseAffilieParitaireManager.INNER_JOIN);
        sql.append("(");
        sql.append(getSqlIdPlanAffiliation());
        sql.append(") bcot" + AFMasseAffilieParitaireManager.ON);
        sql.append("bcot." + AFCotisation.FIELDNAME_PLANAFFILIATION_ID);
        sql.append("=");
        sql.append("pla.MUIPLA");

        return sql.toString();
    }

    /**
     * select mbiass from webavs.afassup where webavs.afassup.mbtgen = 801001 and webavs.afassup.mbttyp = 812001
     * 
     * @param transaction
     * @return
     */
    private String getSqlIdAssurance() {
        StringBuffer sql = new StringBuffer("");
        sql.append(AFMasseAffilieParitaireManager.SELECT);
        sql.append("ass." + AFAssurance.FIELD_ID_ASSURANCE);
        sql.append(AFMasseAffilieParitaireManager.FROM);
        sql.append(_getCollection() + AFAssurance.TABLE_NAME + " ass");
        sql.append(AFMasseAffilieParitaireManager.WHERE + "ass." + AFAssurance.FIELD_GENRE_ASSURANCE + "="
                + IAFAssurance.PARITAIRE);
        sql.append(AFMasseAffilieParitaireManager.AND + "ass." + AFAssurance.FIELD_TYPE_ASSURANCE + "="
                + AFMasseAffilieParitaireManager.COTISATION_AVS);

        return sql.toString();
    }

    /**
     * <pre>
     * select muipla, memmap from webavs.afcotip inner join (
     * 		getSQLIdAssurance()
     * 	 ) ass on ass.mbiass = webavs.afcotip.mbiass where webavs.afcotip.medfin = 0
     * </pre>
     * 
     * @return
     */
    private String getSqlIdPlanAffiliation() {
        StringBuffer sql = new StringBuffer("");
        sql.append(AFMasseAffilieParitaireManager.SELECT);
        sql.append(AFCotisation.FIELDNAME_PLANAFFILIATION_ID + ", MEMMAP");
        sql.append(AFMasseAffilieParitaireManager.FROM);
        sql.append(_getCollection() + AFCotisation.TABLE_NAME + " cot");
        sql.append(AFMasseAffilieParitaireManager.INNER_JOIN);
        sql.append("(");
        sql.append(getSqlIdAssurance());
        sql.append(") bass" + AFMasseAffilieParitaireManager.ON);
        sql.append("bass." + AFAssurance.FIELD_ID_ASSURANCE);
        sql.append("=");
        sql.append("cot." + AFCotisation.FIELDNAME_ASSURANCE_ID);
        sql.append(AFMasseAffilieParitaireManager.WHERE + "cot." + AFCotisation.FIELDNAME_DATE_FIN + "="
                + AFMasseAffilieParitaireManager.ZERO);

        return sql.toString();
    }
}
