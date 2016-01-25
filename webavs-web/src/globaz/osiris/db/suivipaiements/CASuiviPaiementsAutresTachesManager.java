package globaz.osiris.db.suivipaiements;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CACompteCourant;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;

public class CASuiviPaiementsAutresTachesManager extends CASuiviPaiementsAutresTachesSumMontantManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {
        setModeListSection(true);
    }

    @Override
    protected String _getSql(BStatement statement) {
        setModeListSection(true);

        String select = "select sum(a." + CAOperation.FIELD_MONTANT + ") as " + CAOperation.FIELD_MONTANT + ", b."
                + CARubrique.FIELD_IDRUBRIQUE + ", b." + CARubrique.FIELD_IDEXTERNE + " from " + _getCollection()
                + CAOperation.TABLE_CAOPERP + " a, " + _getCollection() + CARubrique.TABLE_CARUBRP + " b, "
                + getTemporaryTableSecteur() + " e, " + _getCollection() + CAJournal.TABLE_CAJOURP + " f ";
        select += "where ";
        select += "a." + CAOperation.FIELD_IDTYPEOPERATION + " like 'E%' and a." + CAOperation.FIELD_ETAT + " = "
                + APIOperation.ETAT_COMPTABILISE + " and a." + CACompteCourant.FIELD_IDCOMPTECOURANT + " = "
                + getForIdCompteCourant() + " and ";
        select += "a." + CAOperation.FIELD_IDCOMPTE + " = b." + CARubrique.FIELD_IDRUBRIQUE + " and ";
        select += "a." + CAOperation.FIELD_IDJOURNAL + " = f." + CAJournal.FIELD_IDJOURNAL;
        select += getWhereDateUntilBetween();
        select += " and (b." + CARubrique.FIELD_IDSECTEUR + " = e." + CARubrique.FIELD_IDSECTEUR + " and (b."
                + CARubrique.FIELD_NATURERUBRIQUE + " = " + APIRubrique.COTISATION_AVEC_MASSE + " or b."
                + CARubrique.FIELD_NATURERUBRIQUE + " = " + APIRubrique.COTISATION_SANS_MASSE + ")) and ";
        select += "a." + CAOperation.FIELD_IDSECTION + " in (" + super._getSql(statement) + ") ";

        select += "group by b." + CARubrique.FIELD_IDRUBRIQUE + ", b." + CARubrique.FIELD_IDEXTERNE;

        return select;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CASuiviPaiementsAutresTaches();
    }

}
