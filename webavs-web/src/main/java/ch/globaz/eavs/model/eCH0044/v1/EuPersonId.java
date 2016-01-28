package ch.globaz.eavs.model.eCH0044.v1;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.eCH0044.common.AbstractEuPersonId;
import ch.globaz.eavs.model.eCH0044.common.AbstractPersonId;
import ch.globaz.eavs.model.eCH0044.common.AbstractPersonIdCategory;

public class EuPersonId extends AbstractEuPersonId {
    private PersonId personId = null;
    private PersonIdCategory personIdCategory = null;

    @Override
    public List getChildren() {
        List result = new ArrayList();
        result.add(personId);
        result.add(personIdCategory);
        return result;
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

    @Override
    public void setPersonId(EAVSAbstractModel personId) {
        this.personId = (PersonId) personId;

    }

    @Override
    public void setPersonIdCategory(EAVSAbstractModel personIdCategory) {
        this.personIdCategory = (PersonIdCategory) personIdCategory;

    }

}
