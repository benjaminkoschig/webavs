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
    private static String MONTANT_MAX_CVS1 = "100000";
    private static String MONTANT_MIN_CVS1 = "0";
    private String forAnnee;

    @Override
    protected String _getFields(final BStatement statement) {
        return " cont.htitie, cont.maiaff, cont.malnaf, tiers.htlde1,tiers.htlde2, maddeb, madfin,MDTGEN,MDDEFF,MDDPRE,GROUPE.CELGRP,COUP.PCOUID AS CODESUVA,COUP.PCOLUT AS LIBELLESUVA";
    }

    @Override
    protected String _getFrom(final BStatement statement) {
        String idRole = CEAffiliationService.getRoleForAffilieParitaire(getSession());

        StringBuffer sqlFrom = new StringBuffer();

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

        StringBuffer sqlWhere = new StringBuffer();

        sqlWhere.append("mdbfdr = '1' ");
        sqlWhere.append("and SUBSTR(CAST(MDDEFF AS char(8)),1,4) = '" + CEUtils.subAnnee(getForAnnee(), 1) + "' ");
        sqlWhere.append("and MDTGEN <> " + CodeSystem.TYPE_CONTROLE_5_POURCENT + " ");
        sqlWhere.append("and ");
        sqlWhere.append("(" + CEControlesExtraOrdinairesEffectuesManager.MONTANT_MIN_CVS1
                + " < (SELECT SUM(CUMULMASSE) FROM " + _getCollection() + "CACPTRP AS CPTR1 WHERE ANNEE = "
                + CEUtils.subAnnee(getForAnnee(), 1) + " AND CPTR1.IDRUBRIQUE IN( " + idRubriques
                + ")  AND CA.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE))");
        sqlWhere.append("and ");
        sqlWhere.append("((SELECT SUM(CUMULMASSE) FROM " + _getCollection() + "CACPTRP AS CPTR1 WHERE ANNEE = "
                + CEUtils.subAnnee(getForAnnee(), 1) + " AND CPTR1.IDRUBRIQUE IN( " + idRubriques
                + ")  AND CA.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE) < "
                + CEControlesExtraOrdinairesEffectuesManager.MONTANT_MAX_CVS1 + ")");

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
