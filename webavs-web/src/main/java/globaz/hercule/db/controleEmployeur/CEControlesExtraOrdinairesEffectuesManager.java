/*
 * Globaz SA.
 */
package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.service.CEAffiliationService;
import globaz.hercule.utils.CEUtils;
import globaz.hercule.utils.CodeSystem;

/**
 * @author jpa
 * @since Créé le 3 août 2010
 */
public class CEControlesExtraOrdinairesEffectuesManager extends BManager {

    private static final long serialVersionUID = -7923385583332408124L;
    private String forAnnee;

    @Override
    protected String _getFields(final BStatement statement) {

        StringBuilder sqlFields = new StringBuilder();

        sqlFields.append(" cont.htitie,");
        sqlFields.append(" cont.maiaff,");
        sqlFields.append(" cont.malnaf, ");
        sqlFields.append(" tiers.htlde1,");
        sqlFields.append(" tiers.htlde2,");
        sqlFields.append(" maddeb,");
        sqlFields.append(" madfin,");
        sqlFields.append(" MDTGEN,");
        sqlFields.append(" MDDEFF,");
        sqlFields.append(" MDDPRE,");
        sqlFields.append(" GROUPE.CELGRP,");
        sqlFields.append(" COUP.PCOUID AS CODESUVA,");
        sqlFields.append(" COUP.PCOLUT AS LIBELLESUVA");

        return sqlFields.toString();
    }

    @Override
    protected String _getFrom(final BStatement statement) {
        String idRole = CEAffiliationService.getRoleForAffilieParitaire(getSession());

        StringBuilder sqlFrom = new StringBuilder();

        sqlFrom.append(_getCollection() + "cecontp cont ");
        sqlFrom.append("LEFT OUTER JOIN " + _getCollection()
                + "CACPTAP AS CA ON (cont.MALNAF = CA.IDEXTERNEROLE AND CA.IDTIERS = cont.HTITIE  AND CA.IDROLE = "
                + idRole + ") ");
        sqlFrom.append("INNER JOIN " + _getCollection() + "titierp AS tiers ON (tiers.htitie = cont.htitie) ");
        sqlFrom.append("left outer join " + _getCollection() + "afaffip as aff on (aff.maiaff = cont.maiaff) ");
        sqlFrom.append("LEFT JOIN " + _getCollection() + "cememp as membre on (membre.maiaff = aff.maiaff) ");
        sqlFrom.append("LEFT JOIN " + _getCollection() + "cegrpp as groupe on (groupe.ceidgr = membre.ceidgr) ");
        sqlFrom.append(" LEFT JOIN " + _getCollection()
                + "FWCOUP as coup on (aff.MATSUV = coup.pcosid AND coup.PLAIDE='" + getSession().getIdLangue() + "') ");

        return sqlFrom.toString();
    }

    @Override
    protected String _getOrder(final BStatement statement) {
        return " cont.malnaf ";
    }

    @Override
    protected String _getWhere(final BStatement statement) {
        String idRubriques = CEUtils.getIdRubrique(getSession());

        StringBuilder sqlWhere = new StringBuilder();

        sqlWhere.append("mdbfdr = '1' ");
        sqlWhere.append("and SUBSTR(CAST(MDDEFF AS char(8)),1,4) = '" + CEUtils.subAnnee(getForAnnee(), 1) + "' ");
        sqlWhere.append("and MDTGEN <> " + CodeSystem.TYPE_CONTROLE_5_POURCENT + " ");
        sqlWhere.append("and ");
        sqlWhere.append("((SELECT SUM(CUMULMASSE) FROM " + _getCollection() + "CACPTRP AS CPTR1 WHERE ANNEE = "
                + CEUtils.subAnnee(getForAnnee(), 1) + " AND CPTR1.IDRUBRIQUE IN( " + idRubriques
                + ")  AND CA.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE) >= 0)");
        sqlWhere.append("and ");
        sqlWhere.append("((SELECT SUM(CUMULMASSE) FROM " + _getCollection() + "CACPTRP AS CPTR1 WHERE ANNEE = "
                + CEUtils.subAnnee(getForAnnee(), 1) + " AND CPTR1.IDRUBRIQUE IN( " + idRubriques
                + ")  AND CA.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE) < " + CEUtils.MONTANT_MAX_POUR_CALCUL_5_POUR_CENT
                + ")");

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEControles5PourCent();
    }

    /**
     * L'année sur laquelle on se base, l'année précedente de la liste
     * 
     * @return
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * @param forAnnee
     *            L'année sur laquelle on se base, l'année précedente de la liste
     */
    public void setForAnnee(final String forAnnee) {
        this.forAnnee = forAnnee;
    }
}
