package globaz.lynx.db.facture;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.fournisseur.LXFournisseur;
import globaz.lynx.db.informationcomptable.LXInformationComptable;

public class LXLectureOptiqueManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdSociete;
    private String likeCcpFournisseur;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        // Bug 5442 - Saisie d'une facture pour un fournisseur ayant deux adresses de pmt.
        sqlFrom.append(_getCollection() + LXInformationComptable.TABLE_LXINFCP);
        sqlFrom.append(" INNER JOIN " + _getCollection() + LXFournisseur.TABLE_LXFOURP + " ON " + _getCollection()
                + LXInformationComptable.TABLE_LXINFCP + "." + LXInformationComptable.FIELD_IDFOURNISSEUR + "="
                + _getCollection() + LXFournisseur.TABLE_LXFOURP + "." + LXFournisseur.FIELD_IDFOURNISSEUR);
        sqlFrom.append(" INNER JOIN " + _getCollection() + "TITIERP" + " ON " + _getCollection() + "TITIERP.HTITIE"
                + "=" + _getCollection() + LXFournisseur.TABLE_LXFOURP + "." + LXFournisseur.FIELD_IDTIERS);
        sqlFrom.append(" INNER JOIN (");
        sqlFrom.append("SELECT DISTINCT "
                + _getCollection()
                + "TIAPAIP.HTITIE"
                + ", tib.htlde1 bdes1, tib.htlde2 bdes2, tib.htldec bdec, hincba bcompte, huclea bclearing, HUCSWI bswift, HIIADU");
        sqlFrom.append(" FROM " + _getCollection() + "TIAPAIP");
        sqlFrom.append(" INNER JOIN " + _getCollection() + "TIADRPP ON " + _getCollection() + "TIAPAIP.HIIAPA" + "="
                + _getCollection() + "TIADRPP.HIIAPA");
        sqlFrom.append(" LEFT OUTER JOIN " + _getCollection() + "TIBANQP" + " ON " + _getCollection()
                + "TIADRPP.HTITIE" + "=" + _getCollection() + "TIBANQP.HTITIE");
        sqlFrom.append(" left outer join " + _getCollection() + "TITIERP" + " tib" + " ON " + "tib.htitie="
                + _getCollection() + "TIBANQP.htitie");

        if (!JadeStringUtil.isBlank(getLikeCcpFournisseur())) {
            sqlFrom.append(" WHERE ");
            sqlFrom.append("(" + _getCollection() + "TIADRPP.HICCP" + " like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeCcpFournisseur() + "%"));
            sqlFrom.append(" OR " + _getCollection() + "TIBANQP.HUCCP" + " like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeCcpFournisseur() + "%") + ")");
        }

        sqlFrom.append(") t on t.HTITIE = ").append(_getCollection() + LXFournisseur.TABLE_LXFOURP + ".IDTIERS");

        if (!JadeStringUtil.isIntegerEmpty(getForIdSociete()) && JadeStringUtil.isDigit(getForIdSociete())) {
            sqlFrom.append(" AND ");
            sqlFrom.append(_getCollection() + LXInformationComptable.TABLE_LXINFCP + "."
                    + LXInformationComptable.FIELD_IDSOCIETE + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdSociete()));
        }

        return sqlFrom.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXLectureOptique();
    }

    public String getForIdSociete() {
        return forIdSociete;
    }

    public String getLikeCcpFournisseur() {
        return likeCcpFournisseur;
    }

    public void setForIdSociete(String forIdSociete) {
        this.forIdSociete = forIdSociete;
    }

    public void setLikeCcpFournisseur(String ccpFournisseur) {
        likeCcpFournisseur = ccpFournisseur;
    }

}
