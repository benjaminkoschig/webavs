package globaz.aquila.db.access.batch;

import globaz.aquila.api.ICOSequenceConstante;
import globaz.aquila.common.COBEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import java.util.HashMap;

/**
 * Représente une entité de type Sequence
 * 
 * @author Arnaud Dostes, 08-oct-2004
 */
public class COSequence extends COBEntity implements ICOSequenceConstante {

    private static final long serialVersionUID = -4518524796553040494L;

    /**
     * charge la bonne séquence pour la section et le contentieux
     * 
     * @param section
     * @param compteAnnexe
     * @return la séquence correspondant à la section
     * @throws Exception
     */
    public static final COSequence loadSequence(BSession session, CASection section, CACompteAnnexe compteAnnexe)
            throws Exception {
        // charger le type de section
        COSequence sequence = new COSequence();

        if (JadeStringUtil.isEmpty(section.getIdSequenceContentieux())) {
            sequence.setIdSequence(section.getTypeSection().getIdSequenceContentieux());
        } else {
            sequence.setIdSequence(section.getIdSequenceContentieux());
        }

        sequence.setSession(session);
        sequence.retrieve();

        return sequence;
    }

    /** (idSequence) */
    private String idSequence = "";

    /** (libSequence) */
    private String libSequence = "";

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdSequence(this._incCounter(transaction, "0"));
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return ICOSequenceConstante.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idSequence = statement.dbReadNumeric(ICOSequenceConstante.FNAME_ID_SEQUENCE);
        libSequence = statement.dbReadNumeric(ICOSequenceConstante.FNAME_LIB_SEQUENCE);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(ICOSequenceConstante.FNAME_ID_SEQUENCE,
                this._dbWriteNumeric(statement.getTransaction(), getIdSequence(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(ICOSequenceConstante.FNAME_ID_SEQUENCE,
                this._dbWriteNumeric(statement.getTransaction(), idSequence, "idSequence"));
        statement.writeField(ICOSequenceConstante.FNAME_LIB_SEQUENCE,
                this._dbWriteNumeric(statement.getTransaction(), libSequence, "libSequence"));
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getIdSequence() {
        return idSequence;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getLibSequence() {
        return libSequence;
    }

    /**
     * @return Le libellé de la séquence
     */
    public String getLibSequenceLibelle() {
        return getSession().getCodeLibelle(libSequence);
    }

    public BManager load(HashMap criteres) throws Exception {
        COSequenceManager sequences = new COSequenceManager();

        // TODO: si d'autres criters que map vide, implementer ici
        sequences.setSession(getSession());
        sequences.find();

        return sequences;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setIdSequence(String string) {
        idSequence = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setLibSequence(String string) {
        libSequence = string;
    }
}
