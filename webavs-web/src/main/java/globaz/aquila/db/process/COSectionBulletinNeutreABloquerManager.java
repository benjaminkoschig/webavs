/**
 *
 */
package globaz.aquila.db.process;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.common.COBManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CASection;

/**
 * @author SEL
 * 
 *         <pre>
 * 		select ca.idexternerole,se.idexterne,se.base,se.pmtcmp,se.solde,sedf.idexterne
 * 		from webavss.cacptap ca
 * 		inner join webavss.casectp se on (ca.idcompteannexe=se.idcompteannexe)
 * 		inner join webavss.casectp sedf on (ca.idcompteannexe=sedf.idcompteannexe and sedf.categoriesection in (227013, 227014, 227030) and
 * 		se.datedebutperiode between sedf.datedebutperiode and sedf.datefinperiode)
 * 		where se.idtypesection=81
 * 		and se.solde = 0
 * 		and se.pmtcmp = 0
 * 		and se.datefinperiode < 20110101
 * 		and (se.statutbn in (0, 257002) OR se.statutbn is null)
 * 		and se.idlastetataquila in (0, 5200030)
 * </pre>
 */
public class COSectionBulletinNeutreABloquerManager extends COBManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String SECTION = " se";
    private static final String SECTIONDF = " sedf";

    private String dateReference = null;
    private String forIdCompteAnnexe = null;
    private String forIdSection = null;
    private boolean forSommation = false;
    private boolean testerSoldeEtPmtCmpZero = true;

    @Override
    protected String _getFields(BStatement statement) {
        return "DISTINCT " + COSectionBulletinNeutreABloquerManager.SECTION + ".*";
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer("");

        sqlFrom.append(_getCollection());
        sqlFrom.append(CASection.TABLE_CASECTP);
        sqlFrom.append(COSectionBulletinNeutreABloquerManager.SECTION);

        // inner join webavss.casectp sedf on (ca.idcompteannexe=sedf.idcompteannexe
        // and sedf.categoriesection in (227013, 227014, 227030)
        // and se.datedebutperiode between sedf.datedebutperiode and sedf.datefinperiode)

        sqlFrom.append(COBManager.INNER_JOIN);
        sqlFrom.append(_getCollection());
        sqlFrom.append(CASection.TABLE_CASECTP);
        sqlFrom.append(COSectionBulletinNeutreABloquerManager.SECTIONDF);
        sqlFrom.append(COBManager.ON);
        sqlFrom.append("(");
        sqlFrom.append(COSectionBulletinNeutreABloquerManager.SECTION + ".");
        sqlFrom.append(CASection.FIELD_IDCOMPTEANNEXE);
        sqlFrom.append(COBManager.EGAL);
        sqlFrom.append(COSectionBulletinNeutreABloquerManager.SECTIONDF + ".");
        sqlFrom.append(CASection.FIELD_IDCOMPTEANNEXE);
        sqlFrom.append(COBManager.AND);
        sqlFrom.append(COSectionBulletinNeutreABloquerManager.SECTIONDF + ".");
        sqlFrom.append(CASection.FIELD_CATEGORIESECTION);
        sqlFrom.append(COBManager.IN);
        sqlFrom.append("(");
        sqlFrom.append(APISection.ID_CATEGORIE_SECTION_DECOMPTE_FINAL);
        sqlFrom.append(", ");
        sqlFrom.append(APISection.ID_CATEGORIE_SECTION_BOUCLEMENT_ACOMPTE);
        sqlFrom.append(", ");
        sqlFrom.append(APISection.ID_CATEGORIE_SECTION_DECISION_DE_TAXATION_OFFICE);
        sqlFrom.append(")");
        sqlFrom.append(COBManager.AND);
        sqlFrom.append(COSectionBulletinNeutreABloquerManager.SECTION + ".");
        sqlFrom.append(CASection.FIELD_DATEDEBUTPERIODE);
        sqlFrom.append(COBManager.BETWEEN);
        sqlFrom.append(COSectionBulletinNeutreABloquerManager.SECTIONDF + ".");
        sqlFrom.append(CASection.FIELD_DATEDEBUTPERIODE);
        sqlFrom.append(COBManager.AND);
        sqlFrom.append(COSectionBulletinNeutreABloquerManager.SECTIONDF + ".");
        sqlFrom.append(CASection.FIELD_DATEFINPERIODE);
        sqlFrom.append(")");

        return sqlFrom.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer("");

        // * where se.idtypesection=81
        // * and se.solde = 0
        // * and se.pmtcmp = 0
        // * and se.datefinperiode < 20110101
        // * and (se.statutbn in (0, 257002) OR se.statutbn is null)
        // * and se.idlastetataquila in (0, 5200030)

        // Par défaut : idTypeSection = 81
        sqlWhere.append(COSectionBulletinNeutreABloquerManager.SECTION + ".");
        sqlWhere.append(CASection.FIELD_IDTYPESECTION);
        sqlWhere.append(COBManager.EGAL);
        sqlWhere.append(APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE);

        if (isForSommation()) {
            if (isTesterSoldeEtPmtCmpZero()) {
                sqlWhere.append(COBManager.AND);
                sqlWhere.append(COSectionBulletinNeutreABloquerManager.SECTION + ".");
                sqlWhere.append(CASection.FIELD_SOLDE);
                sqlWhere.append(COBManager.EGAL);
                sqlWhere.append(COBManager.ZERO);

                sqlWhere.append(COBManager.AND);
                sqlWhere.append(COSectionBulletinNeutreABloquerManager.SECTION + ".");
                sqlWhere.append(CASection.FIELD_PMTCMP);
                sqlWhere.append(COBManager.EGAL);
                sqlWhere.append(COBManager.ZERO);
            }

            sqlWhere.append(COBManager.AND);
            sqlWhere.append(COSectionBulletinNeutreABloquerManager.SECTION + ".");
            sqlWhere.append(CASection.FIELD_DATEFINPERIODE);
            sqlWhere.append(COBManager.PLUS_PETIT_EGAL);
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), getDateReference()));

            sqlWhere.append(COBManager.AND);
            sqlWhere.append("(");
            sqlWhere.append(COSectionBulletinNeutreABloquerManager.SECTION + ".");
            sqlWhere.append(CASection.FIELD_STATUTBN);
            sqlWhere.append(COBManager.IN);
            sqlWhere.append("(");
            sqlWhere.append(COBManager.ZERO + ",");
            sqlWhere.append(APISection.STATUTBN_REACTIVE);
            sqlWhere.append(")");
            sqlWhere.append(COBManager.OR);
            sqlWhere.append(COSectionBulletinNeutreABloquerManager.SECTION + ".");
            sqlWhere.append(CASection.FIELD_STATUTBN);
            sqlWhere.append(COBManager.IS_NULL);
            sqlWhere.append(")");

            sqlWhere.append(COBManager.AND);
            sqlWhere.append(COSectionBulletinNeutreABloquerManager.SECTION + ".");
            sqlWhere.append(CASection.FIELD_IDLASTETATAQUILA);
            sqlWhere.append(COBManager.IN);
            sqlWhere.append("(");
            sqlWhere.append(COBManager.ZERO + ",");
            sqlWhere.append(ICOEtape.CS_AUCUNE);
            sqlWhere.append(")");
        }

        if (!JadeStringUtil.isBlank(getForIdCompteAnnexe())) {
            sqlWhere.append(COBManager.AND);
            sqlWhere.append(COSectionBulletinNeutreABloquerManager.SECTION + ".");
            sqlWhere.append(CASection.FIELD_IDCOMPTEANNEXE);
            sqlWhere.append(COBManager.EGAL);
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), getForIdCompteAnnexe()));
        }

        if (!JadeStringUtil.isBlank(getForIdSection())) {
            sqlWhere.append(COBManager.AND);
            sqlWhere.append(COSectionBulletinNeutreABloquerManager.SECTION + ".");
            sqlWhere.append(CASection.FIELD_IDSECTION);
            sqlWhere.append(COBManager.EGAL);
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), getForIdSection()));
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CASection();
    }

    /**
     * @return the dateReference
     */
    public String getDateReference() {
        return dateReference;
    }

    /**
     * @return the forIdCompteAnnexe
     */
    public String getForIdCompteAnnexe() {
        return forIdCompteAnnexe;
    }

    /**
     * @return
     */
    public String getForIdSection() {
        return forIdSection;
    }

    /**
     * @return the forSommation
     */
    public boolean isForSommation() {
        return forSommation;
    }

    /**
     * @return the testerSoldeEtPmtCmpZero
     */
    public boolean isTesterSoldeEtPmtCmpZero() {
        return testerSoldeEtPmtCmpZero;
    }

    /**
     * @param dateReference
     *            the dateReference to set
     */
    public void setDateReference(String dateReference) {
        this.dateReference = dateReference;
    }

    /**
     * @param forIdCompteAnnexe
     *            the forIdCompteAnnexe to set
     */
    public void setForIdCompteAnnexe(String forIdCompteAnnexe) {
        this.forIdCompteAnnexe = forIdCompteAnnexe;
    }

    /**
     * @param forIdSection
     */
    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }

    /**
     * @param forSommation
     *            the forSommation to set
     */
    public void setForSommation(boolean forSommation) {
        this.forSommation = forSommation;
    }

    /**
     * @param testerSoldeEtPmtCmpZero
     *            the testerSoldeEtPmtCmpZero to set
     */
    public void setTesterSoldeEtPmtCmpZero(boolean testerSoldeEtPmtCmpZero) {
        this.testerSoldeEtPmtCmpZero = testerSoldeEtPmtCmpZero;
    }

}
