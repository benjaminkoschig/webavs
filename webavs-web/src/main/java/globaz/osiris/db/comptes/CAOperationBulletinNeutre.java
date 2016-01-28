package globaz.osiris.db.comptes;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIOperationBulletinNeutre;
import globaz.osiris.api.APISection;

public class CAOperationBulletinNeutre extends CAOperation implements APIOperationBulletinNeutre {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAOperationBulletinNeutre() {
        super();
        setIdTypeOperation(APIOperation.CAOPERATIONBULLETINNEUTRE);
    }

    public CAOperationBulletinNeutre(CAOperation parent) {
        super(parent);

        setIdTypeOperation(APIOperation.CAOPERATIONBULLETINNEUTRE);
    }

    /**
     * @see globaz.osiris.db.comptes.CAOperation#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);

        if (JadeStringUtil.isBlank(getIdTypeOperation())) {
            setIdTypeOperation(APIOperation.CAOPERATIONBULLETINNEUTRE);
        }
    }

    /**
     * @see globaz.osiris.db.comptes.CAOperation#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) {
        super._validate(statement);

        if (!isInstanceOrSubClassOf(APIOperation.CAOPERATIONBULLETINNEUTRE)) {
            _addError(statement.getTransaction(), getSession().getLabel("OPERATION_NON_BULLETIN_NEUTRE"));
        }

        if (!APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(getSection().getIdTypeSection())) {
            _addError(statement.getTransaction(), getSession().getLabel("SECTION_NON_BULLETIN_NEUTRE"));
        }
    }

    /**
     * @see globaz.osiris.db.comptes.CAOperation#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        super._writeProperties(statement);

        statement.writeField(CAOperation.FIELD_ANNEECOTISATION,
                this._dbWriteNumeric(statement.getTransaction(), getAnneeCotisation(), "anneeCotisation"));
        statement.writeField(CAOperation.FIELD_IDCOMPTE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCompte(), "idCompte"));
        statement.writeField(CAOperation.FIELD_TAUX,
                this._dbWriteNumeric(statement.getTransaction(), getTaux(), "taux"));
        statement.writeField(CAOperation.FIELD_IDCAISSEPROFESSIONNELLE, this._dbWriteNumeric(
                statement.getTransaction(), getIdCaisseProfessionnelle(), "idCaisseProfessionnelle"));
        statement.writeField(CAOperation.FIELD_LIBELLE,
                this._dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
    }
}
