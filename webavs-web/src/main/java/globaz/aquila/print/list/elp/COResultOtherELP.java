package globaz.aquila.print.list.elp;

import globaz.aquila.process.elp.COAbstractELP;

public class COResultOtherELP extends COAbstractELP {

    private COTypeMessageELP type;

    @Override
    public COTypeMessageELP getTypemessage() {
        return type;
    }

    @Override
    public String getRemarque() {
        return "";
    }


    public void setType(COTypeMessageELP type) {
        this.type = type;
    }
}
