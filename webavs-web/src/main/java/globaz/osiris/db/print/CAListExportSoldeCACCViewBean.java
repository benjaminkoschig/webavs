package globaz.osiris.db.print;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.osiris.db.export.CAExportListSoldeCC;

/**
 * Insérez la description du type ici. Date de création : (17.06.2003 08:29:58)
 * 
 * @author: ado
 */
public class CAListExportSoldeCACCViewBean extends CAExportListSoldeCC implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur TIExport.
     */
    public CAListExportSoldeCACCViewBean() {
        super();
    }

    public String getForDate() {
        return forDate;
    }

    public String getForSelectionCC() {
        return forSelectionCC;
    }

    public String getForSelectionRole() {
        return forSelectionRole;
    }

    public String getForSelectionSigne() {
        return forSelectionSigne;
    }

    public String getForSelectionTri() {
        return forSelectionTri;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public void setForDate(String _forDate) {
        forDate = _forDate;
    }

    public void setForSelectionCC(String _forSelectionCC) {
        forSelectionCC = _forSelectionCC;
    }

    public void setForSelectionRole(String _forSelectionRole) {
        forSelectionRole = _forSelectionRole;
    }

    public void setForSelectionSigne(String _forSelectionSigne) {
        forSelectionSigne = _forSelectionSigne;
    }

    public void setForSelectionTri(String _forSelectionTri) {
        forSelectionTri = _forSelectionTri;
    }

    public void setIdCompteAnnexe(String _idCompteAnnexe) {
        idCompteAnnexe = _idCompteAnnexe;
    }
}
