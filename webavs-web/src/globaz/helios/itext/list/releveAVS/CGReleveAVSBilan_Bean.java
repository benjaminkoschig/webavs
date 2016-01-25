package globaz.helios.itext.list.releveAVS;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.helios.db.avs.CGExtendedCompteOfas;
import globaz.helios.process.helper.CGMontantHelper;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CGReleveAVSBilan_Bean extends CGReleveAVS_Bean {
    /**
     * Constructor for CGReleveAVSBilan_Bean.
     */
    public CGReleveAVSBilan_Bean() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.helios.itext.list.CGIReleveAVS_Bean#prepareValue(CGExtendedCompteOfas , BTransaction)
     */
    @Override
    public boolean prepareValue(BEntity bEntity, BTransaction transaction, BSession session) {
        CGExtendedCompteOfas entity = (CGExtendedCompteOfas) bEntity;
        String langueISO = session.getIdLangueISO();
        // Column 0 - Info Secteur
        col0 = entity.getIdExterne().substring(0, 3) + "  ";
        if (langueISO != null) {
            if ("IT".equalsIgnoreCase(langueISO)) {
                col0 += entity.getLibelleSecteurIt();
            } else if ("DE".equalsIgnoreCase(langueISO)) {
                col0 += entity.getLibelleSecteurDe();
            } else {
                col0 += entity.getLibelleSecteurFr();
            }
        }

        // Column 1 - info compte OFAS
        col1 = entity.getIdExterne() + "  ";
        col1 += getCol1Libelle(entity.getSession(), entity.getIdExterne(), entity.getLibelle());

        try {

            CGMontantHelper result = traitementReleveAvsHelper.getMontantComptesBilan(transaction, session, entity,
                    getIdExerciceComptable(), getIdPeriodeComptable(), isProvisoire());

            // Column 2 - Infos solde actif
            if (result.actif != null) {
                col2 = new Double(result.actif.doubleValue());
            } else {
                col2 = null;
            }

            // Column 3 - Infos solde passif
            if (result.passif != null) {
                result.passif.negate();
                col3 = new Double(result.passif.doubleValue());
            } else {
                col3 = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (col2 != null || col3 != null);
    }
}
