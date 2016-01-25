/*
 * Créé le 21 déc. 05
 */
package globaz.naos.util;

import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.pyxis.util.ITIAffiliationMapper;
import globaz.pyxis.util.TIAffiliationValues;
import java.util.ArrayList;
import java.util.List;

/**
 * @author oca
 * 
 */
public class AFNaosAffiliationMapper implements ITIAffiliationMapper {

    private AFAffiliation _getUniqueAffiliation(String idTiers, BISession session) throws Exception {

        if (JadeStringUtil.isEmpty(idTiers)) {
            return null;
        }
        AFAffiliationManager affManager = new AFAffiliationManager();
        affManager.setISession(session);
        affManager.setForIdTiers(idTiers);
        affManager.setOrder("MADDEB DESC");
        affManager.find();
        if (affManager.size() == 0) {
            // aucune affiliation trouvée pour ce tiers
            return null;
        } else if (affManager.size() == 1) {
            // une seule affiliation
            return (AFAffiliation) affManager.getFirstEntity();
        } else {
            // plus d'une affiliation, existe-t-il plus d'une affiliation
            // ouverte et laquelle detient la taxation?
            AFAffiliation affOuverte = null;
            for (int i = 0; i < affManager.size(); i++) {
                AFAffiliation aff = (AFAffiliation) affManager.getEntity(i);
                if (AFAffiliationUtil.isTaxationPrincipale(aff, null)) {
                    // affiliation détenant la taxation principale
                    return aff;
                }
                if ((affOuverte == null) && JadeStringUtil.isIntegerEmpty(aff.getDateFin())) {
                    // affiliation ouverte, conserver la dernière en date
                    affOuverte = aff;
                }
            }
            // si aucune affiliation détient la taxation principale, prendre
            // l'affiliation non radiée
            if (affOuverte != null) {
                return affOuverte;
            } else {
                // aucune affiliation ouverte, prendre la dernière en date
                return (AFAffiliation) affManager.getFirstEntity();
            }
        }

    }

    @Override
    public TIAffiliationValues fillAffiliationValues(String idTiers, BISession session) throws Exception {
        AFAffiliation aff = _getUniqueAffiliation(idTiers, session);
        if (aff != null) {
            TIAffiliationValues values = new TIAffiliationValues();
            values.setNumAffTaxation(aff.getAffilieNumero());
            values.setDateDebutAffTaxation(aff.getDateDebut());
            values.setDateFinAffTaxation(aff.getDateFin());
            values.setTypeAffTaxation(aff.getTypeAffiliation());
            values.setNumeroIDE(AFIDEUtil.formatNumIDE(aff.getNumeroIDE()));
            return values;
        }
        return null;
    }

    @Override
    public String findDateDebutAffTaxation(String idTiers, BISession session) throws Exception {
        AFAffiliation aff = _getUniqueAffiliation(idTiers, session);
        if (aff == null) {
            return "";
        }
        return aff.getDateDebut();
    }

    @Override
    public String findDateFinAffTaxation(String idTiers, BISession session) throws Exception {
        AFAffiliation aff = _getUniqueAffiliation(idTiers, session);
        if (aff == null) {
            return "";
        }
        return aff.getDateFin();
    }

    @Override
    public String findNumAffTaxation(String idTiers, BISession session) throws Exception {
        AFAffiliation aff = _getUniqueAffiliation(idTiers, session);
        if (aff == null) {
            return "";
        }
        return aff.getAffilieNumero();
    }

    @Override
    public List findNumerosAffiliation(String idTiers, BISession session) throws Exception {
        List<String> result = new ArrayList<String>();
        if (JadeStringUtil.isEmpty(idTiers)) {
            return result;
        }
        AFAffiliationManager affManager = new AFAffiliationManager();
        affManager.setISession(session);
        affManager.setForIdTiers(idTiers);
        affManager.find();
        for (int i = 0; i < affManager.size(); i++) {
            result.add(((AFAffiliation) affManager.getEntity(i)).getAffilieNumero());
        }
        return result;
    }

    @Override
    public String findTypeAffTaxation(String idTiers, BISession session) throws Exception {
        AFAffiliation aff = _getUniqueAffiliation(idTiers, session);
        if (aff == null) {
            return "";
        }
        return aff.getTypeAffiliation();
    }

    @Override
    public String getAffiliationProvisoireFiledName() {
        return "AFAFFIP.MABTRA";
    }

    @Override
    public String getDateDebutAffiliationFieldName() {
        return "AFAFFIP.MADDEB";
    }

    @Override
    public String getDateFinAffiliationFieldName() {
        return "AFAFFIP.MADFIN";
    }

    @Override
    public String getIdTiersFieldName() {
        return "AFAFFIP.HTITIE";
    }

    @Override
    public String getLibelleFieldName() {
        return "AFAFFIP.MADESM";
    }

    @Override
    public String getNumAffilieFieldName() {
        return "AFAFFIP.MALNAF";
    }

    @Override
    public String getNumeroIDEFieldName() {
        return "AFAFFIP.MALFED";
    }

    @Override
    public String getTableAffActuelName() {
        return "AFAFFIP";
    }

    @Override
    public String getTableHistAffName() {
        return "AFAFFIP";
    }

    @Override
    public String getTypeAffiliationFiledName() {
        return "AFAFFIP.MATTAF";
    }

    @Override
    public List<TIAffiliationValues> fillListAffiliationValues(String idTiers, BISession session) throws Exception {
        List<TIAffiliationValues> affiliationValues = new ArrayList<TIAffiliationValues>();
        if (JadeStringUtil.isEmpty(idTiers)) {
            return affiliationValues;
        }

        List<AFAffiliation> affiliations = AFUtil.getAffiliationsAAfficherForIdTiers(idTiers, session);
        for (AFAffiliation aff : affiliations) {
            TIAffiliationValues values = new TIAffiliationValues();
            values.setNumAffTaxation(aff.getAffilieNumero());
            values.setDateDebutAffTaxation(aff.getDateDebut());
            values.setDateFinAffTaxation(aff.getDateFin());
            values.setTypeAffTaxation(aff.getTypeAffiliation());
            values.setNumeroIDE(AFIDEUtil.formatNumIDE(aff.getNumeroIDE()));
            affiliationValues.add(values);
        }
        return affiliationValues;
    }
}
