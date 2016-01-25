package globaz.osiris.print.itext.list;

import globaz.osiris.db.contentieux.CAParametreEtape;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class CAIListSequenceContentieuxBean extends CAParametreEtape {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for CAIListSequenceContentieuxBean.
     */
    public CAIListSequenceContentieuxBean() {
        super();
    }

    public String getCOL_1() {
        return super.getSequence();
    }

    public String getCOL_2() {
        return super.getEtape().getDescription();
    }

    public String getCOL_3() {
        return super.getDelai() + " " + super.getCsUnite().getLibelle();
    }

    public String getCOL_4() {
        if (super.getImputerTaxe().booleanValue()) {
            return "X";
        } else {
            return "";
        }
    }

    public String getCOL_5() {
        return super.getNomClasseImpl();
    }

    public String getCOL_6() {
        return super.getCsDateReference().getLibelle();
    }

    public String getCOL_7() {
        if (super.getSoldelimitedeclenchement().equals("0")) {
            return "";
        } else {
            return super.getSoldelimitedeclenchement();
        }
    }
}
