package globaz.naos.db.print;

// import inetsoft.report.*;
// import globaz.framework.printing.*;
// import globaz.globall.db.*;
//
// import java.util.*;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (22.01.2002 11:34:36)
 * 
 * @author: Administrator
 */
public class DocAvisMutation extends globaz.framework.printing.FWDocument {

    /**
     * Commentaire relatif au constructeur Doc1_1002.
     */
    public DocAvisMutation() throws Exception {
        super("/naos.properties");
        loadWrapper("globaz.framework.printing.FWXmlReport");
    }

}
