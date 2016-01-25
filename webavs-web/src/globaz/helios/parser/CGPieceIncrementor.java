package globaz.helios.parser;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWIncrementation;
import globaz.jade.client.util.JadeStringUtil;

public class CGPieceIncrementor extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String CGNOPIECES = "CGNOPIECES";
    private static final String KEY_NO_PIECE_AUTO = "noPieceAuto";

    /**
     * Créer l'incrément pour le nouvel exercice comptable.
     * 
     * @param session
     * @param transaction
     * @param idExerciceComptable
     * @throws Exception
     */
    public static void createIncrementorForNewExerciceComptable(BSession session, BTransaction transaction,
            String idExerciceComptable) throws Exception {
        if (new Boolean(session.getApplication().getProperty(KEY_NO_PIECE_AUTO)).booleanValue()) {
            FWIncrementation pieceIncrementor = new FWIncrementation();
            pieceIncrementor.setSession(session);

            pieceIncrementor.setIdIncrement(CGNOPIECES);
            pieceIncrementor.setIdCodeSysteme(idExerciceComptable);
            pieceIncrementor.setAnneeIncrement("");
            pieceIncrementor.setValeurIncrement("0");

            pieceIncrementor.add(transaction);
        }
    }

    /**
     * Return le prochain numéro de pièce.
     * 
     * @param session
     * @param idExerciceComptable
     * @return
     */
    public static String getNextNumero(BSession session, String idExerciceComptable) {
        try {
            if (new Boolean(session.getApplication().getProperty(KEY_NO_PIECE_AUTO)).booleanValue()) {
                FWIncrementation incrementation = new FWIncrementation();
                incrementation.setSession(session);
                incrementation.setIdIncrement(CGNOPIECES);
                incrementation.setIdCodeSysteme(idExerciceComptable);
                incrementation.setAnneeIncrement("");

                incrementation.retrieve();

                if (!incrementation.hasErrors() && !incrementation.isNew()) {
                    long oldValue = Long.parseLong(incrementation.getValeurIncrement());
                    oldValue++;
                    return "" + oldValue;
                } else {
                    return "";
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            // If any problems occurs, return empty string.
            return "";
        }
    }

    /**
     * Si le prochain numéro de pièce est la pièce comptable passé en paramètre alors incrémentation du numéro dans
     * FWINCP.
     * 
     * @param session
     * @param transaction
     * @param idExerciceComptable
     * @param noPiece
     * @throws Exception
     */
    public static void setNextNumero(BSession session, BTransaction transaction, String idExerciceComptable,
            String noPiece) throws Exception {
        if (new Boolean(session.getApplication().getProperty(KEY_NO_PIECE_AUTO)).booleanValue()) {
            if (!JadeStringUtil.isIntegerEmpty(noPiece) && noPiece.equals(getNextNumero(session, idExerciceComptable))) {
                CGPieceIncrementor pieceIncrementor = new CGPieceIncrementor();
                pieceIncrementor.setSession(session);
                pieceIncrementor._incCounter(transaction, "0", CGNOPIECES, idExerciceComptable);
            }
        }
    }

    @Override
    protected String _getTableName() {
        // Nothing. Fake Entity. Method _incCounter needed.
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        // Nothing. Fake Entity. Method _incCounter needed.
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Nothing. Fake Entity. Method _incCounter needed.
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // Nothing. Fake Entity. Method _incCounter needed.
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Nothing. Fake Entity. Method _incCounter needed.
    }

}
