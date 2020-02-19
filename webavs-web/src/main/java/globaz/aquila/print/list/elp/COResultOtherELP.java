package globaz.aquila.print.list.elp;

public class COResultOtherELP extends COAbstractResultELP {

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
