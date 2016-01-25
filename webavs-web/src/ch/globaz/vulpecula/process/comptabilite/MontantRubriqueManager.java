package ch.globaz.vulpecula.process.comptabilite;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import ch.globaz.vulpecula.domain.models.common.Date;

public class MontantRubriqueManager extends BManager {
    private static final long serialVersionUID = 8146285796140321197L;

    private static final String AND = " and ";
    private static final String FROM = " from ";
    private static final String INNER_JOIN = " inner join ";

    private Date date;

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new MontantRubriqueEntity();
    }

    @Override
    protected String _getSql(BStatement statement) {
        return "select ru.idexterne, tr.LIBELLE, adm.HBCADM, sum(op.masse) as masse, sum(op.montant) as montant"
                + MontantRubriqueManager.FROM + _getCollection() + "caoperp op" + MontantRubriqueManager.INNER_JOIN
                + _getCollection() + "carubrp ru on (op.idcompte=ru.idrubrique)" + MontantRubriqueManager.INNER_JOIN
                + _getCollection() + "pmtradp tr on (ru.IDTRADUCTION = tr.IDTRADUCTION and tr.CODEISOLANGUE = 'FR')"
                + MontantRubriqueManager.INNER_JOIN + _getCollection() + "tiadmip adm on (op.fancai = adm.htitie)"
                + MontantRubriqueManager.INNER_JOIN + _getCollection() + "cajourp jo on (op.idjournal=jo.idjournal)"
                + " where op.etat=205002" + " and jo.datevaleurcg between " + date.getFirstDayOfMonth().getValue()
                + MontantRubriqueManager.AND + date.getLastDayOfMonth().getValue() + " and ru.USECAISSESPROF = '1'"
                + " group by ru.idexterne, tr.libelle, adm.hbcadm";
    }
}
