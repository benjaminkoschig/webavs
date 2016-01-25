package ch.globaz.eavs.model.eahviv2011000101.v0;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.eahviv2011000101.common.AbstractContent;
import ch.globaz.eavs.model.eahviv2011000101.common.AbstractMutation;
import ch.globaz.eavs.model.eahviv2011000101.common.AbstractOrderScope;
import ch.globaz.eavs.model.eahviv2011000101.common.AbstractSpouse;
import ch.globaz.eavs.model.eahviv2011000101.common.AbstractTaxpayer;

public class Content extends AbstractContent {
    private Mutation mutation = null;
    private OrderScope orderScope = null;
    private Spouse spouse = null;
    private Taxpayer taxPayer = null;

    @Override
    public List getChildren() {
        List result = new ArrayList();
        result.add(orderScope);
        result.add(taxPayer);
        result.add(spouse);
        result.add(mutation);
        return result;
    }

    @Override
    public AbstractMutation getMutation() {
        if (mutation == null) {
            mutation = new Mutation();
        }
        return mutation;
    }

    @Override
    public AbstractOrderScope getOrderScope() {
        if (orderScope == null) {
            orderScope = new OrderScope();
        }
        return orderScope;
    }

    @Override
    public AbstractSpouse getSpouse() {
        if (spouse == null) {
            spouse = new Spouse();
        }
        return spouse;
    }

    @Override
    public AbstractTaxpayer getTaxpayer() {
        if (taxPayer == null) {
            taxPayer = new Taxpayer();
        }
        return taxPayer;
    }

}
