package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.service.CEAffiliationService;
import globaz.hercule.utils.CEUtils;

/**
 * @author hpe
 * @since Créé le 14 févr. 07
 */

public class CEControlesAEffectuerUnionManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = "";
    private String forAnneeCptr = "";

    /**
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return " couverture.maiaff ";
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        // String idRubriques = CEUtils.getIdRubrique(getSession());
        String roleForAffilie = CEAffiliationService.getRoleForAffilieParitaire(getSession());

        StringBuffer sqlFrom = new StringBuffer();
        sqlFrom.append("  " + _getCollection() + "CECOUVP as couverture ");
        sqlFrom.append(" left outer join " + _getCollection()
                + "afaffip as affiliation on affiliation.maiaff = couverture.maiaff ");
        sqlFrom.append(" left outer join "
                + _getCollection()
                + "CACPTAP AS CA ON (affiliation.MALNAF = CA.IDEXTERNEROLE AND CA.IDTIERS = affiliation.HTITIE  AND CA.IDROLE = "
                + roleForAffilie + ") ");
        sqlFrom.append(" where cenane = " + CEUtils.subAnnee(getForAnnee(), 1) + " and cebcav = '1' ");
        sqlFrom.append(" and (madfin=0 or madfin>=" + CEUtils.subAnnee(getForAnnee(), 1) + "0101" + ") ");
        // sqlFrom.append(" and (SELECT SUM(CUMULMASSE) FROM ccvdqua.CACPTRP AS CPTR1 WHERE ANNEE = "
        // + getForAnneeCptr() + " AND CPTR1.IDRUBRIQUE IN(" + idRubriques +
        // ")");
        // sqlFrom.append(" AND CA.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE ) > " +
        // MONTANT_MIN_POUR_CONTROLE);
        sqlFrom.append(" union ");
        sqlFrom.append(" Select cont.maiaff from " + _getCollection() + "CECONTP cont ");
        sqlFrom.append(" left join " + _getCollection() + "afaffip aff on (aff.maiaff = cont.maiaff) ");
        sqlFrom.append(" where SUBSTR(CAST(MDDPRE AS char(8)),1,4) = '" + CEUtils.subAnnee(getForAnnee(), 0)
                + "' AND MDDEFF = 0 and mdbfdr = '1' ");
        sqlFrom.append(" and (madfin=0 or madfin>=" + CEUtils.subAnnee(getForAnnee(), 1) + "0101" + ") ");
        sqlFrom.append(" union ");
        sqlFrom.append(" Select membre.maiaff from " + _getCollection() + "cememp as membre inner join "
                + _getCollection() + "cegrpp as groupe on (membre.ceidgr = groupe.ceidgr) ");
        sqlFrom.append(" left join " + _getCollection() + "afaffip aff on (aff.maiaff = membre.maiaff) ");
        sqlFrom.append(" where cedcmi = " + CEUtils.subAnnee(getForAnnee(), 1));
        sqlFrom.append(" and (madfin=0 or madfin>=" + CEUtils.subAnnee(getForAnnee(), 1) + "0101" + ") ");
        return sqlFrom.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEControlesAEffectuerUnion();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForAnneeCptr() {
        return forAnneeCptr;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForAnnee(String string) {
        forAnnee = string;
    }

    public void setForAnneeCptr(String forAnneeCptr) {
        this.forAnneeCptr = forAnneeCptr;
    }
}
