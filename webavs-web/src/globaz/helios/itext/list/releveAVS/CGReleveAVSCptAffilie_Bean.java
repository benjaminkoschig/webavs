package globaz.helios.itext.list.releveAVS;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.helios.db.avs.CGExtendedContrePartieCpteAff;
import java.math.BigDecimal;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CGReleveAVSCptAffilie_Bean extends CGReleveAVS_Bean {
    /**
     * Constructor for CGReleveAVSCptAffilie_Bean.
     */
    public CGReleveAVSCptAffilie_Bean() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.helios.itext.list.CGIReleveAVS_Bean#prepareValue(CGExtendedCompteOfas , BTransaction)
     */
    @Override
    public boolean prepareValue(BEntity bEntity, BTransaction transaction, BSession session) {
        try {
            CGExtendedContrePartieCpteAff entity = (CGExtendedContrePartieCpteAff) bEntity;
            String langue = session.getIdLangueISO();
            col0 = entity.getNumeroCompteOfas().substring(0, 3) + "  ";
            if (langue != null) {
                if ("IT".equalsIgnoreCase(langue)) {
                    col0 += entity.getLibelleSectIt();
                } else if ("DE".equalsIgnoreCase(langue)) {
                    col0 += entity.getLibelleSectDe();
                } else {
                    col0 += entity.getLibelleSectFr();
                }
            }
            col1 = entity.getNumeroCompteOfas() + "  ";
            if (langue != null) {
                if ("IT".equalsIgnoreCase(langue)) {
                    col1 += getCol1Libelle(entity.getSession(), entity.getNumeroCompteOfas(), entity.getLibelleIt());
                } else if ("DE".equalsIgnoreCase(langue)) {
                    col1 += getCol1Libelle(entity.getSession(), entity.getNumeroCompteOfas(), entity.getLibelleDe());
                } else {
                    col1 += getCol1Libelle(entity.getSession(), entity.getNumeroCompteOfas(), entity.getLibelleFr());
                }
            }
            BigDecimal result = null;
            // Doit
            result = new BigDecimal(entity.getMontant());
            if (result.signum() == 1) {
                col2 = new Double(result.doubleValue());
            } else {
                col2 = null;
            }
            // Avoir
            result = new BigDecimal(entity.getMontant());
            if (result.signum() == -1) {
                col3 = new Double(result.negate().doubleValue());
            } else {
                col3 = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (col2 != null || col3 != null);
    }
}
