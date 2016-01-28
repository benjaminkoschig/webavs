package globaz.aquila.db.paiement;

import globaz.aquila.api.ICOSequence;
import globaz.aquila.db.access.paiement.COPaiement;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class COPaiementViewBean extends COPaiement {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FNAME_ID_CONTENTIEUX = COContentieux.FNAME_ID_CONTENTIEUX;
    public static final String FNAME_ID_SEQUENCE = COContentieux.FNAME_ID_SEQUENCE;

    private String idContentieux;
    private String idSequence;

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    protected BEntity _newEntity() throws Exception {
        return new COPaiementViewBean();
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdSection(statement.dbReadNumeric(FIELD_IDSECTION));
        setIdCompteAnnexe(statement.dbReadNumeric(FIELD_IDCOMPTEANNEXE));
        setDateOperation(statement.dbReadDateAMJ(FIELD_DATE));
        setMontant(statement.dbReadNumeric(FIELD_MONTANT, 2));
        setIdCompte(statement.dbReadNumeric(FIELD_IDCOMPTE));
        setProvenancePmt(statement.dbReadNumeric(FIELD_PROVENANCEPMT));
        setIdContentieux(statement.dbReadNumeric(FNAME_ID_CONTENTIEUX));
        setIdSequence(statement.dbReadNumeric(FNAME_ID_SEQUENCE));
    }

    public String getIdContentieux() {
        return idContentieux;
    }

    public String getIdSequence() {
        return idSequence;
    }

    public void setIdContentieux(String idContentieux) {
        this.idContentieux = idContentieux;
    }

    public void setIdSequence(String idSequence) {
        if (JadeStringUtil.equals("1", idSequence, false)) {
            idSequence = ICOSequence.CS_SEQUENCE_AVS;
        } else {
            idSequence = ICOSequence.CS_SEQUENCE_ARD;
        }
        this.idSequence = idSequence;
    }

}
