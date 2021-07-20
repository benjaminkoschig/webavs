package globaz.hercule.db.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.hercule.db.reviseur.CEReviseur;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.service.CEAffiliationService;
import globaz.hercule.service.CEComptabiliteService;
import globaz.hercule.service.CEComptesIndividuelsService;
import globaz.hercule.service.CEControleEmployeurService;
import globaz.hercule.service.CEDeclarationSalaireService;
import globaz.hercule.service.CEFacturationService;
import globaz.hercule.service.CETiersService;
import globaz.hercule.service.dto.CEMassesReprisesByAnneeDTO;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.services.controleemployeur.CACompteAnnexeService;
import globaz.pyxis.adresse.formater.TIAdresseFormater;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import globaz.webavs.common.ICommonConstantes;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * View Bean de l'écran d'attribution de point d'un controle employeur
 * 
 * @author SCO
 * @since 7 sept. 2010
 */
public class CEGestionAttributionPtsViewBean extends CEGestionAttributionPts implements FWViewBeanInterface {

    private static final long serialVersionUID = 2980132241632437081L;
    public static final BigDecimal MIN_MASSE_AVS_POUR_AFFICHAGE = BigDecimal.valueOf(150000);

    private String idAffilie = "";
    private double masseSalariale;
    private double masseSalarialeFinControle;
    private String oldIdAttributionPts;
    private String periodePrevueDebut = "";
    private String periodePrevueFin = "";
    private Map<Integer, BigDecimal> mapMasseSalariale = null;
    private Map<Integer, BigDecimal> mapMasseSalarialeDepuisAnneeDateEffective = null;
    private Map<Integer, BigDecimal> mapNombreCI = null;
    private CEMassesReprisesByAnneeDTO infosReprise = null;
    private Map<String, String> mapFacturation = null;
    private Integer anneeCourante = 0;

    private void retrieveInfosFacturation() {

        if (JadeStringUtil.isEmpty(getIdControle())) {
            mapFacturation = new HashMap<String, String>();
        }

        if (mapFacturation == null) {
            try {
                mapFacturation = CEFacturationService.getInfosFactureForControle(getSession(), null, getIdControle());
            } catch (HerculeException e) {
                JadeLogger.error("Unabled to find anne de fin de controle : date fin = " + getDateFinControle(), e);
            }
        }
    }

    private void findAnneeCourante() {
        if (anneeCourante == 0) {
            try {
                anneeCourante = (JADate.getYear("" + getDateFinControle())).intValue();
            } catch (JAException e) {
                JadeLogger.error("Unabled to find anne de fin de controle : date fin = " + getDateFinControle(), e);
            }
        }
    }

    private void retrieveInfosReprises() {
        findAnneeCourante();

        if (JadeStringUtil.isEmpty(getIdControle())) {
            infosReprise = new CEMassesReprisesByAnneeDTO();
        }

        if (infosReprise == null) {
            try {
                infosReprise = CEDeclarationSalaireService.getInfosReprises5DernieresAnnees(getSession(), null,
                        getIdControle(), anneeCourante);
            } catch (HerculeException e) {
                JadeLogger.error("Unabled to retrieve masse salariale for affilie " + getNumAffilie(), e);
            }
        }
    }

    private void retrieveMasseSalarialeDepuisAnneeCourante() {
        findAnneeCourante();

        if (mapMasseSalariale == null) {
            try {
                mapMasseSalariale = CEComptabiliteService.getMasseSalariale5DernieresAnnees(getSession(), null,
                        getNumAffilie(), anneeCourante);
            } catch (HerculeException e) {
                JadeLogger.error("Unabled to retrieve masse salariale for affilie " + getNumAffilie(), e);
            }
        }
    }

    private void retrieveMasseSalarialeDepuisAnneeDateEffective(Integer anneeDateEffective) {
        findAnneeCourante();

        if (mapMasseSalarialeDepuisAnneeDateEffective == null) {
            try {
                mapMasseSalarialeDepuisAnneeDateEffective = CEComptabiliteService.getMasseSalariale5DernieresAnnees(getSession(), null,
                        getNumAffilie(), anneeDateEffective);
            } catch (HerculeException e) {
                JadeLogger.error("Unabled to retrieve masse salariale for affilie " + getNumAffilie(), e);
            }
        }
    }

    private void retrieveNombreCI() {
        findAnneeCourante();

        if (mapNombreCI == null) {
            try {
                mapNombreCI = CEComptesIndividuelsService.getNombreCI5DernieresAnnees(getSession(), null,
                        getNumAffilie(), anneeCourante);
            } catch (HerculeException e) {
                JadeLogger.error("Unabled to retrieve nombre CI for affilie " + getNumAffilie(), e);
            }
        }
    }

    public String _getDateFacturation() {
        retrieveInfosFacturation();

        if (mapFacturation.containsKey(CEFacturationService.DATE_FACTURE)) {
            return mapFacturation.get(CEFacturationService.DATE_FACTURE);
        }

        return "";
    }

    public String _getTotaFacture() {
        retrieveInfosFacturation();

        if (mapFacturation.containsKey(CEFacturationService.TOTAL_FACTURE)) {
            return JANumberFormatter.format(mapFacturation.get(CEFacturationService.TOTAL_FACTURE));
        }

        return "";
    }

    public String _getMasseAvsRepriseN0() {
        retrieveInfosReprises();

        if (infosReprise.getDonneesAVS() != null && infosReprise.getDonneesAVS().containsKey((anneeCourante))) {
            return JANumberFormatter.format(infosReprise.getDonneesAVS().get(anneeCourante));
        }

        return "";
    }

    public String _getMasseAvsRepriseN1() {
        retrieveInfosReprises();

        if (infosReprise.getDonneesAVS() != null && infosReprise.getDonneesAVS().containsKey((anneeCourante - 1))) {
            return JANumberFormatter.format(infosReprise.getDonneesAVS().get((anneeCourante - 1)));
        }

        return "";
    }

    public String _getMasseAvsRepriseN2() {
        retrieveInfosReprises();

        if (infosReprise.getDonneesAVS() != null && infosReprise.getDonneesAVS().containsKey((anneeCourante - 2))) {
            return JANumberFormatter.format(infosReprise.getDonneesAVS().get((anneeCourante - 2)));
        }
        return "";
    }

    public String _getMasseAvsRepriseN3() {
        retrieveInfosReprises();

        if (infosReprise.getDonneesAVS() != null && infosReprise.getDonneesAVS().containsKey((anneeCourante - 3))) {
            return JANumberFormatter.format(infosReprise.getDonneesAVS().get((anneeCourante - 3)));
        }
        return "";
    }

    public String _getMasseAvsRepriseN4() {
        retrieveInfosReprises();

        if (infosReprise.getDonneesAVS() != null && infosReprise.getDonneesAVS().containsKey((anneeCourante - 4))) {
            return JANumberFormatter.format(infosReprise.getDonneesAVS().get((anneeCourante - 4)));
        }
        return "";
    }

    public String _getNombreCIReprisN0() {
        retrieveInfosReprises();

        if (infosReprise.getDonneesCI() != null && infosReprise.getDonneesCI().containsKey(anneeCourante)) {
            return "" + infosReprise.getDonneesCI().get(anneeCourante);
        }
        return "";
    }

    public String _getNombreCIReprisN1() {
        retrieveInfosReprises();

        if (infosReprise.getDonneesCI() != null && infosReprise.getDonneesCI().containsKey((anneeCourante - 1))) {
            return "" + infosReprise.getDonneesCI().get((anneeCourante - 1));
        }
        return "";
    }

    public String _getNombreCIReprisN2() {
        retrieveInfosReprises();

        if (infosReprise.getDonneesCI() != null && infosReprise.getDonneesCI().containsKey((anneeCourante - 2))) {
            return "" + infosReprise.getDonneesCI().get((anneeCourante - 2));
        }
        return "";
    }

    public String _getNombreCIReprisN3() {
        retrieveInfosReprises();

        if (infosReprise.getDonneesCI() != null && infosReprise.getDonneesCI().containsKey((anneeCourante - 3))) {
            return "" + infosReprise.getDonneesCI().get((anneeCourante - 3));
        }
        return "";
    }

    public String _getNombreCIReprisN4() {
        retrieveInfosReprises();

        if (infosReprise.getDonneesCI() != null && infosReprise.getDonneesCI().containsKey((anneeCourante - 4))) {
            return "" + infosReprise.getDonneesCI().get((anneeCourante - 4));
        }

        return "";
    }

    public String _getNombreCIN0() {
        retrieveNombreCI();

        if (mapNombreCI.containsKey(anneeCourante)) {
            return "" + mapNombreCI.get(anneeCourante);
        }
        return "";
    }

    public String _getNombreCIN1() {
        retrieveNombreCI();

        if (mapNombreCI.containsKey((anneeCourante - 1))) {
            return "" + mapNombreCI.get((anneeCourante - 1));
        }
        return "";
    }

    public String _getNombreCIN2() {
        retrieveNombreCI();

        if (mapNombreCI.containsKey((anneeCourante - 2))) {
            return "" + mapNombreCI.get((anneeCourante - 2));
        }
        return "";
    }

    public String _getNombreCIN3() {
        retrieveNombreCI();

        if (mapNombreCI.containsKey((anneeCourante - 3))) {
            return "" + mapNombreCI.get((anneeCourante - 3));
        }
        return "";
    }

    public String _getNombreCIN4() {
        retrieveNombreCI();

        if (mapNombreCI.containsKey((anneeCourante - 4))) {
            return "" + mapNombreCI.get((anneeCourante - 4));
        }
        return "";
    }

    public String _getMasseSalarialeN0() {
        retrieveMasseSalarialeDepuisAnneeCourante();

        if (mapMasseSalariale.containsKey(anneeCourante)) {
            return JANumberFormatter.format(mapMasseSalariale.get(anneeCourante));
        }
        return "";
    }

    public String _getAnneeN0() {
        findAnneeCourante();
        if (anneeCourante <= 0) {
            return "";
        }
        return "" + anneeCourante;
    }

    public String _getMasseSalarialeN1() {
        retrieveMasseSalarialeDepuisAnneeCourante();

        if (mapMasseSalariale.containsKey((anneeCourante - 1))) {
            return JANumberFormatter.format(mapMasseSalariale.get((anneeCourante - 1)));
        }
        return "";
    }

    public String _getAnneeN1() {
        if (anneeCourante <= 0) {
            return "";
        }
        return "" + (anneeCourante - 1);
    }

    public String _getMasseSalarialeN2() {
        retrieveMasseSalarialeDepuisAnneeCourante();

        if (mapMasseSalariale.containsKey((anneeCourante - 2))) {
            return JANumberFormatter.format(mapMasseSalariale.get((anneeCourante - 2)));
        }
        return "";
    }

    public String _getAnneeN2() {
        if (anneeCourante <= 0) {
            return "";
        }
        return "" + (anneeCourante - 2);
    }

    public String _getMasseSalarialeN3() {
        retrieveMasseSalarialeDepuisAnneeCourante();

        if (mapMasseSalariale.containsKey((anneeCourante - 3))) {
            return JANumberFormatter.format(mapMasseSalariale.get((anneeCourante - 3)));
        }
        return "";
    }

    public String _getAnneeN3() {
        if (anneeCourante <= 0) {
            return "";
        }
        return "" + (anneeCourante - 3);
    }

    public String _getMasseSalarialeN4() {
        retrieveMasseSalarialeDepuisAnneeCourante();

        if (mapMasseSalariale.containsKey((anneeCourante - 4))) {
            return JANumberFormatter.format(mapMasseSalariale.get((anneeCourante - 4)));
        }
        return "";
    }

    public String _getAnneeN4() {
        if (anneeCourante <= 0) {
            return "";
        }
        return "" + (anneeCourante - 4);
    }

    public void _getAffilieForAttribution() {
        if (JadeStringUtil.isEmpty(getIdAffilie())) {
            return;
        }

        CEAffilieForAttrPtsManager mg = new CEAffilieForAttrPtsManager();
        mg.setSession(getSession());
        mg.setForIdAffiliation(getIdAffilie());
        mg.setForIdControle(getIdControle());

        try {
            mg.find();
        } catch (Exception e) {
            JadeLogger.error("Unabled to retrieve controle for affilie " + getNumAffilie(), e);
        }

        if (mg.size() > 0) {
            CEAffilieForAttrPts aff = (CEAffilieForAttrPts) mg.get(0);
            setNom(aff.getNom());
            setDateDebutAffiliation(aff.getDateDebutAffiliation());
            setDateFinAffiliation(aff.getDateFinAffiliation());
            setIdControle(aff.getIdControle());
            setIdTiers(aff.getIdTiers());
            setDateEffective(aff.getDateEffective());
            setDateFinControle(aff.getDateFinPeriodeControle());
            setDateDebutControle(aff.getDateDebutPeriodeControle());
            setIdAffilie(aff.getIdAffiliation());
            setBrancheEconomique(aff.getBrancheEconomique());
            setCodeNOGA(aff.getCodeNOGA());
            setNumAffilieExterne(aff.getNumAffilieExterne());
            setCodeSuva(aff.getCodeSuva());
            setLibelleSuva(aff.getLibelleSuva());
        }
    }

    public String _getAnneeDeControle() {

        int _annee = 0;

        // si la masse est supérieur au minimum on affiche la date
        if (isMasseSalarialeSuperieurMin()) {
            _annee = CEUtils.transformeStringToInt(getAnneeCouverture());
        }

        if (_annee == 0) {
            return "";
        }

        return "" + (_annee + 1);

    }

    public String _getCategorieMasseSalariale() {

        if (masseSalariale == 0) {
            _getMasseSalariale();
        }

        return CEControleEmployeurService.findCategorie(masseSalariale);
    }

    public String _getCategorieMasseSalarialeFinControle() {

        if (masseSalarialeFinControle == 0) {
            _getMasseSalarialeFinControle();
        }

        return CEControleEmployeurService.findCategorie(masseSalarialeFinControle);
    }

    /**
     * Cherche l'id du compte annexe de l'affilie pour un lien direct depuis l'ecran du controle.
     * 
     * @return
     */
    public String _getIdCompteAnnexe() {
        String idCompteAnnexe = null;

        try {
            idCompteAnnexe = CACompteAnnexeService.getIdCompteAnnexeByRole(getSession(),
                    CEAffiliationService.getRoleForAffilieParitaire(getSession()), getNumAffilie());
        } catch (Exception e) {
            idCompteAnnexe = null;
        }

        return idCompteAnnexe;
    }

    public String _getMasseSalariale() {

        if (!JadeStringUtil.isBlank(getNumAffilie())) {
            masseSalariale = CEControleEmployeurService.retrieveMasse(getSession(),
                    CEUtils.getAnneePrecedente(CEUtils.giveAnneeCourante()), getNumAffilie());
        }

        return JANumberFormatter.format(masseSalariale);
    }

    public String _getMasseSalarialeFinControle() {

        try {
            String anneeFinControle = "" + CEUtils.stringDateToAnnee(getDateFinControle());

            if (!JadeStringUtil.isBlank(getNumAffilie())) {
                masseSalarialeFinControle = CEControleEmployeurService.retrieveMasse(getSession(), anneeFinControle,
                        getNumAffilie());
            }
        } catch (JAException e) {
            JadeLogger.error(this, e);
        }

        return JANumberFormatter.format(masseSalarialeFinControle);
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public String getInfoTiers() {

        String _infoTiers = "";

        if (JadeStringUtil.isBlank(getNom())) {
            return _infoTiers;
        }

        String adresse = "";

        try {
            TITiers tiers = CETiersService.retrieveTiers(getSession(), getIdTiers());

            adresse = tiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    ICommonConstantes.CS_APPLICATION_COTISATION, JACalendar.todayJJsMMsAAAA(), new TIAdresseFormater(),
                    true);

            if (JadeStringUtil.isEmpty(adresse)) {
                adresse = getNom();
            }

        } catch (Exception e) {
            JadeLogger.error(this, "Technical Exception, Unabled to retrieve the adresse ( idTiers = " + getIdTiers());
        }

        if (JadeStringUtil.isBlankOrZero(getDateFinAffiliation())) {
            _infoTiers = adresse + getDateDebutAffiliation() + " - *";
        } else {
            _infoTiers = adresse + getDateDebutAffiliation() + " - " + getDateFinAffiliation();
        }

        return _infoTiers;
    }

    public String getLibelleGenreControle() {

        if (!JadeStringUtil.isBlank(getGenreControle())) {
            return getSession().getCodeLibelle(getGenreControle());
        } else {
            return "";
        }
    }

    public String getOldIdAttributionPts() {
        return oldIdAttributionPts;
    }


    /**
     * Définit si au moins une des masses salariale est supérieur au minimum requis pour l'affichage.
     *
     * @return boolean true si au moins une des masses salariale est supérieur au minimum requis pour l'affichage.
     */
    public boolean isMasseSalarialeSuperieurMin() {
        boolean isMasseSalarialeSuperieurMin = false;
        try {
            BigDecimal masse0;
            BigDecimal masse1;

            Integer anneeDateEffective = JACalendar.getYear(getDateEffective());

            retrieveMasseSalarialeDepuisAnneeDateEffective(anneeDateEffective);

            if (mapMasseSalarialeDepuisAnneeDateEffective.containsKey(anneeDateEffective)) {
                masse0 = mapMasseSalarialeDepuisAnneeDateEffective.get(anneeDateEffective);
            } else {
                masse0 = new BigDecimal("0.00");
            }
            if (mapMasseSalarialeDepuisAnneeDateEffective.containsKey((anneeDateEffective - 1))) {
                masse1 = mapMasseSalarialeDepuisAnneeDateEffective.get((anneeDateEffective - 1));
            } else {
                masse1 = new BigDecimal("0.00");
            }

            if (masse0.compareTo(MIN_MASSE_AVS_POUR_AFFICHAGE) >= 0 || masse1.compareTo(MIN_MASSE_AVS_POUR_AFFICHAGE) >= 0) {
                isMasseSalarialeSuperieurMin = true;
            }

        } catch (JAException e) {
            JadeLogger.error("Impossible de parser la date effective", e);
        }

        return isMasseSalarialeSuperieurMin;
    }

    public String getPeriodePrevueDebut() {

        periodePrevueDebut = "";

        // si la masse est supérieur au minimum on affiche la date
        if (isMasseSalarialeSuperieurMin()) {
            try {
                periodePrevueDebut = "01.01." + (CEUtils.stringDateToAnnee(getDateFinControle()) + 1);
            } catch (JAException e) {
                JadeLogger.error("Unabled to calcul the periode prevue debut", e);
            }
        }

        return periodePrevueDebut;
    }

    public String getPeriodePrevueFin() {

        periodePrevueFin = "";

        if (!JadeStringUtil.isBlankOrZero(getAnneeCouverture())) {
            // si la masse est supérieur au minimum on affiche la date
            if (isMasseSalarialeSuperieurMin()) {
                periodePrevueFin = "31.12." + getAnneeCouverture();
            }
        }

        return periodePrevueFin;
    }

    public void setIdAffilie(final String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setOldIdAttributionPts(final String oldIdAttributionPts) {
        this.oldIdAttributionPts = oldIdAttributionPts;
    }

    /**
     * Recherche du libelle de la branche économique suivant le code system de celui ci
     * 
     * @return
     */
    public String _getBrancheEcoLibelle() {
        String brancheEcoLibelle = "";
        if (!JadeStringUtil.isEmpty(getBrancheEconomique())) {

            FWParametersUserCode userCode = new FWParametersUserCode();
            userCode.setSession(getSession());
            userCode.setIdCodeSysteme(getBrancheEconomique());
            userCode.setIdLangue(getSession().getIdLangue());
            try {
                userCode.retrieve();
                brancheEcoLibelle = userCode.getCodeUtilisateur() + " - " + userCode.getLibelle();
            } catch (Exception e) {
                JadeLogger.error("Unabled to retrieve branche economique", e);
            }
        }

        return brancheEcoLibelle;
    }

    /**
     * Recherche du libelle du code noga suivant le code system de celui ci
     * 
     * @return
     */
    public String _getCodeNogaLibelle() {
        String codeNogaLibelle = "";
        if (!JadeStringUtil.isIntegerEmpty(getCodeNOGA())) {
            try {
                FWParametersSystemCodeManager systemCodeManager = new FWParametersSystemCodeManager();
                systemCodeManager.setForGroupLike("VENOGAVAL");
                systemCodeManager.setSession(getSession());
                systemCodeManager.find(BManager.SIZE_NOLIMIT);
                for (int i = 0; i < systemCodeManager.size(); i++) {
                    FWParametersSystemCode code = (FWParametersSystemCode) systemCodeManager.get(i);

                    if (getCodeNOGA().equals(code.getIdCode())) {

                        FWParametersUserCode userCode = new FWParametersUserCode();
                        userCode.setSession(getSession());
                        userCode.setIdCodeSysteme(code.getIdSelection());
                        userCode.setIdLangue(getSession().getIdLangue());
                        userCode.retrieve();

                        codeNogaLibelle = userCode.getCodeUtilisateur() + " - "
                                + code.getCurrentCodeUtilisateur().getCodeUtilisateur() + " - "
                                + getSession().getCodeLibelle(getCodeNOGA());
                    }
                }
            } catch (Exception e) {
                JadeLogger.error("Unabled to retrieve code noga", e);
            }
        }
        return codeNogaLibelle;
    }

    /**
     * Permet l'affichage des informations sur le code suva
     * 
     * @return
     */
    public String _getCodeLibelleSuva() {
        if (JadeStringUtil.isEmpty(getCodeSuva())) {
            return "";
        }

        return getCodeSuva() + " - " + getLibelleSuva();
    }

    public boolean isReviseurSuva() {
        if (!JadeStringUtil.isBlank(getTypeReviseur()) && CEReviseur.TYPE_REV_EXT_SUVA.equals(getTypeReviseur())) {
            return true;
        }

        return false;
    }
}
