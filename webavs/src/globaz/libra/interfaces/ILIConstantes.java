package globaz.libra.interfaces;

import globaz.journalisation.db.journalisation.access.IJOComplementJournalDefTable;
import globaz.journalisation.db.journalisation.access.IJOGroupeJournalDefTable;
import globaz.journalisation.db.journalisation.access.IJOJournalisationDefTable;
import globaz.libra.db.dossiers.LIDossiers;

/**
 * @author hpe
 */
public interface ILIConstantes {

    // TRI POUR ECRANS ECHEANCES
    public static final String ECHEANCES_TRI_DATE = IJOGroupeJournalDefTable.DATE_RAPPEL;
    public static final String ECHEANCES_TRI_DOMAINE = LIDossiers.FIELDNAME_ID_DOMAINE;
    public static final String ECHEANCES_TRI_GROUPE = LIDossiers.FIELDNAME_ID_GROUPE;
    public static final String ECHEANCES_TRI_TYPE = IJOComplementJournalDefTable.VALEURCODESYSTEME;
    public static final String ECHEANCES_TRI_UTILISATEUR = IJOJournalisationDefTable.IDUTILISATEUR;

}