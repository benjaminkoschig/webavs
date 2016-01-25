package globaz.pavo.db.compte;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import java.math.BigDecimal;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CITestSommeParAnnee {
    public static void main(String[] args) {
        CICompteIndividuelUtil ut = new CICompteIndividuelUtil();
        try {
            ut.setSession((BSession) GlobazSystem.getApplication("PAVO").newSession("globazf", "ssiiadm"));
            BigDecimal result = new BigDecimal("0");
            // result =
            // ut.getSommeParAnnee("26779303151",CIEcriture.CS_CIGENRE_1,"1999");
            System.out.println(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor for CITestSommeParAnnee.
     */
    public CITestSommeParAnnee() {
        super();
    }
}
