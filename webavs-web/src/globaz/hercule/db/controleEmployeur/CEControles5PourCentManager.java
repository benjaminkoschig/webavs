package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.utils.CodeSystem;

public class CEControles5PourCentManager extends BManager {

    private static final long serialVersionUID = 281598758138513627L;
    private String forAnnee = "";

    @Override
    protected String _getFields(final BStatement statement) {
        return " cont.htitie, cont.maiaff,cont.malnaf, tiers.htlde1,tiers.htlde2, maddeb, madfin,MDTGEN,MDDEFF,MDDPRE,GROUPE.CELGRP,COUP.PCOUID AS CODESUVA,COUP.PCOLUT AS LIBELLESUVA";
    }

    @Override
    protected String _getFrom(final BStatement statement) {

        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + "cecontp cont ");
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
        StringBuffer sqlWhere = new StringBuffer();

        sqlWhere.append("mdbfdr = '1' ");
        sqlWhere.append("and SUBSTR(CAST(MDDPRE AS char(8)),1,4) = '" + getAnneePourAnneeSuivante(1) + "' ");
        sqlWhere.append("and MDTGEN = " + CodeSystem.TYPE_CONTROLE_5_POURCENT + " ");

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEControles5PourCent();
    }

    public String getAnneePourAnneeSuivante(final int nombreAnneeEnAvant) {
        return String.valueOf((Integer.valueOf(getForAnnee())).intValue() + nombreAnneeEnAvant);
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
