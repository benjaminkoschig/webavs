package ch.globaz.eavs.model.eahvivcommon.v1;

import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.eCH0044.common.AbstractPersonId;
import ch.globaz.eavs.model.eCH0044.common.AbstractPersonIdCategory;
import ch.globaz.eavs.model.eCH0044.v1.PersonId;
import ch.globaz.eavs.model.eCH0044.v1.PersonIdCategory;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractEuPersonId;

public class EuPersonId extends AbstractEuPersonId {
    private PersonId personId = null;
    private PersonIdCategory personIdCategory = null;

    @Override
    public List getChildren() {
        List result = new ArrayList();
        result.add(personIdCategory);
        result.add(personId);
        return result;
    }

    @Override
    protected String getNomClasse() {
        String original = super.getNomClasse();
        return JadeStringUtil.firstLetterToUpperCase(original);
    }

    @Override
    public AbstractPersonId getPersonId() {
        if (personId == null) {
            personId = new PersonId();
        }
        return personId;
    }

    @Override
    public AbstractPersonIdCategory getPersonIdCategory() {
        if (personIdCategory == null) {
            personIdCategory = new PersonIdCategory();
        }
        return personIdCategory;
    }

    public void setPersonId(EAVSAbstractModel _personId) {
        personId = (PersonId) _personId;
    }

    public void setPersonIdCategory(EAVSAbstractModel _personIdCategory) {
        personIdCategory = (PersonIdCategory) _personIdCategory;
    }
}
