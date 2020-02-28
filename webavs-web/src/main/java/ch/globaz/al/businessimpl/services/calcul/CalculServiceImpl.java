package ch.globaz.al.businessimpl.services.calcul;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.globaz.al.business.constantes.ALCSCantons;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.models.dossier.DossierComplexModelRoot;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.services.calcul.CalculService;
import ch.globaz.al.businessimpl.calcul.context.ContextCalcul;
import ch.globaz.al.utils.ALDateUtils;
import ch.globaz.naos.business.data.AssuranceInfo;

/**
 * Implémentation du service permettant le calcul des droits AF d'un dossier
 * 
 * @author jts
 * 
 */
public class CalculServiceImpl extends CalculAbstractService implements CalculService {

    @Override
    public int getAgeForCalcul(String dateNaissance, String dateCalcul) throws JadeApplicationException {

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculServiceImpl#getAgeForCalcul : dateNaissance" + dateCalcul
                    + " is not a valid date");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException("CalculServiceImpl#getAgeForCalcul : dateCalcul" + dateCalcul
                    + " is not a valid date");
        }

        dateCalcul = ALDateUtils.getDateDebutMois(dateCalcul);

        int age = JadeDateUtil.getNbYearsBetween(dateNaissance, dateCalcul, JadeDateUtil.FULL_DATE_COMPARISON);
        if (age < 0) {
            age = 0;
        }
        age++;

        if (dateCalcul.substring(0, 5).equals(dateNaissance.substring(0, 5)) && !dateCalcul.equals(dateNaissance)) {
            age--;
        }

        return age;
    }

    @Override
    public List<CalculBusinessModel> getCalcul(DossierComplexModelRoot dossier, String dateCalcul,
                                               AssuranceInfo assuranceInfo) throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculServiceImpl#getCalcul : Unable to compute, dossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCalcul)) {
            throw new ALCalculException(
                    "CalculServiceImpl#getCalcul : Unable to compute, dateCalcul is not a valid date");
        }

        if (assuranceInfo == null) {
            throw new ALCalculException("CalculServiceImpl#getCalcul : Unable to compute, assuranceInfo is null");
        }

        dossier = checkModelComplex(dossier);

        ContextCalcul context = ContextCalcul.getContextCalcul(dossier, dateCalcul, assuranceInfo);

        return execute(context);

    }

    @Override
    public String getCantonForTarif(String csTarif) throws JadeApplicationException {
        HashMap<String, String> tarifs = new HashMap<String, String>();
        tarifs.put(ALCSTarif.CATEGORIE_AG, ALCSCantons.AG);
        tarifs.put(ALCSTarif.CATEGORIE_AI, ALCSCantons.AI);
        tarifs.put(ALCSTarif.CATEGORIE_AR, ALCSCantons.AR);
        tarifs.put(ALCSTarif.CATEGORIE_BE, ALCSCantons.BE);
        tarifs.put(ALCSTarif.CATEGORIE_BL, ALCSCantons.BL);
        tarifs.put(ALCSTarif.CATEGORIE_BS, ALCSCantons.BS);
        tarifs.put(ALCSTarif.CATEGORIE_FR, ALCSCantons.FR);
        tarifs.put(ALCSTarif.CATEGORIE_GE, ALCSCantons.GE);
        tarifs.put(ALCSTarif.CATEGORIE_GL, ALCSCantons.GL);
        tarifs.put(ALCSTarif.CATEGORIE_GR, ALCSCantons.GR);
        tarifs.put(ALCSTarif.CATEGORIE_JU, ALCSCantons.JU);
        tarifs.put(ALCSTarif.CATEGORIE_LU, ALCSCantons.LU);
        tarifs.put(ALCSTarif.CATEGORIE_NE, ALCSCantons.NE);
        tarifs.put(ALCSTarif.CATEGORIE_NW, ALCSCantons.NW);
        tarifs.put(ALCSTarif.CATEGORIE_OW, ALCSCantons.OW);
        tarifs.put(ALCSTarif.CATEGORIE_SG, ALCSCantons.SG);
        tarifs.put(ALCSTarif.CATEGORIE_SH, ALCSCantons.SH);
        tarifs.put(ALCSTarif.CATEGORIE_SO, ALCSCantons.SO);
        tarifs.put(ALCSTarif.CATEGORIE_SZ, ALCSCantons.SZ);
        tarifs.put(ALCSTarif.CATEGORIE_TI, ALCSCantons.TI);
        tarifs.put(ALCSTarif.CATEGORIE_TG, ALCSCantons.TG);
        tarifs.put(ALCSTarif.CATEGORIE_UR, ALCSCantons.UR);
        tarifs.put(ALCSTarif.CATEGORIE_VD, ALCSCantons.VD);
        tarifs.put(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS, ALCSCantons.VD);
        tarifs.put(ALCSTarif.CATEGORIE_VS, ALCSCantons.VS);
        tarifs.put(ALCSTarif.CATEGORIE_ZG, ALCSCantons.ZG);
        tarifs.put(ALCSTarif.CATEGORIE_ZH, ALCSCantons.ZH);

        String canton = tarifs.get(csTarif);

        if (canton == null) {
            throw new ALCalculException("CalculServiceImpl#getCantonForTarif : '" + csTarif + "' is not valid");
        }

        return canton;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.calcul.CalculService#getTarifForCanton (java.lang.String)
     */
    @Override
    public String getTarifForCanton(String canton) throws JadeApplicationException {
        HashMap<String, String> cantons = new HashMap<String, String>();
        cantons.put("61020001", ALCSTarif.CATEGORIE_AG);
        cantons.put("61020002", ALCSTarif.CATEGORIE_AI);
        cantons.put("61020003", ALCSTarif.CATEGORIE_AR);
        cantons.put("61020004", ALCSTarif.CATEGORIE_BE);
        cantons.put("61020005", ALCSTarif.CATEGORIE_BL);
        cantons.put("61020006", ALCSTarif.CATEGORIE_BS);
        cantons.put("61020007", ALCSTarif.CATEGORIE_FR);
        cantons.put("61020008", ALCSTarif.CATEGORIE_GE);
        cantons.put("61020009", ALCSTarif.CATEGORIE_GL);
        cantons.put("61020010", ALCSTarif.CATEGORIE_GR);
        cantons.put("61020011", ALCSTarif.CATEGORIE_JU);
        cantons.put("61020012", ALCSTarif.CATEGORIE_LU);
        cantons.put("61020013", ALCSTarif.CATEGORIE_NE);
        cantons.put("61020014", ALCSTarif.CATEGORIE_NW);
        cantons.put("61020015", ALCSTarif.CATEGORIE_OW);
        cantons.put("61020016", ALCSTarif.CATEGORIE_SG);
        cantons.put("61020017", ALCSTarif.CATEGORIE_SH);
        cantons.put("61020018", ALCSTarif.CATEGORIE_SO);
        cantons.put("61020019", ALCSTarif.CATEGORIE_SZ);
        cantons.put("61020020", ALCSTarif.CATEGORIE_TI);
        cantons.put("61020021", ALCSTarif.CATEGORIE_TG);
        cantons.put("61020022", ALCSTarif.CATEGORIE_UR);
        cantons.put("61020023", ALCSTarif.CATEGORIE_VD);
        cantons.put("61020024", ALCSTarif.CATEGORIE_VS);
        cantons.put("61020025", ALCSTarif.CATEGORIE_ZG);
        cantons.put("61020026", ALCSTarif.CATEGORIE_ZH);

        String tarif = cantons.get(canton);

        if (tarif == null) {
            throw new ALCalculException("CalculServiceImpl#getTarifForCanton : '" + canton + "' is not valid");
        }

        return tarif;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.calcul.CalculService#isDateLAFam(java. lang.String)
     */
    @Override
    public boolean isDateLAFam(String date) throws JadeApplicationException {
        // si la date n'est pas valide
        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALCalculException("CalculServiceImpl#isLAFam : " + date + " is not a valid date");
        }

        // si date < 01.01.2009 ==> non LAFam
        return !JadeDateUtil.isDateBefore(date, "01.01.2009");
    }

}
