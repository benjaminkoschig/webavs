package globaz.csc.pyxis.api.osiris;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.commons.nss.NSUtil;
import globaz.globall.util.JAException;
import globaz.globall.util.JAStringFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.api.osiris.TIRoleOSI;
import globaz.pyxis.db.tiers.TIRole;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class TIRoleOSICSC extends TIRoleOSI {
    /**
     * Constructor for TIRoleOSICSC.
     */
    public TIRoleOSICSC() {
        super();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (21.05.2002 17:07:31)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    public void checkIdExterneRoleFormat(String idRole, String idExterne) throws Exception {
        if (JadeStringUtil.isBlank(idRole)) {
            throw new Exception("Le r�le doit �tre renseign�");
        }
        if (JadeStringUtil.isBlank(idExterne)) {
            throw new Exception("Le num�ro externe doit �tre renseign�");
            // Contr�le de l'assur�
        }

        if (NSUtil.unFormatAVS(idExterne).length() > 11) {
            String nnss = NSUtil.formatAVSNew(NSUtil.unFormatAVS(idExterne), true);
            if (!nnss.equals(idExterne)) {
                throw new JAException("NNSS mal format�.");
            }
        } else if (idRole.equals(TIRole.CS_ASSURE) || idRole.equals(TIRole.CS_AFFILIE)
                || idRole.equals(CaisseHelperFactory.CS_AFFILIE_PARITAIRE)
                || idRole.equals(CaisseHelperFactory.CS_AFFILIE_PERSONNEL)) {
            JAUtil.checkAvs(idExterne);
        }

    }

    /**
     * Cette m�thode permet de formater l'idExterneRole en fonction des besoins Dans le cas de la CSC le num�ro
     * d'affili� est identique au num�ro AVS
     * 
     * @return java.lang.String
     * @param idRole
     *            java.lang.String
     * @param idExterneRole
     *            java.lang.String
     */
    @Override
    public String formatIdExterneRole(String idRole, String idExterneRole) {
        // Supprimer les caract�res de formattage
        if (NSUtil.unFormatAVS(idExterneRole).length() > 11) {
            return NSUtil.formatAVSNew(NSUtil.unFormatAVS(idExterneRole), true);
        } else if (idRole.equals(TIRole.CS_ASSURE) || idRole.equals(TIRole.CS_AFFILIE)
                || idRole.equals(CaisseHelperFactory.CS_AFFILIE_PARITAIRE)
                || idRole.equals(CaisseHelperFactory.CS_AFFILIE_PERSONNEL)) {
            return JAStringFormatter.formatAVS(NSUtil.unFormatAVS(idExterneRole));
        } else {
            return idExterneRole;
        }
    }
}
