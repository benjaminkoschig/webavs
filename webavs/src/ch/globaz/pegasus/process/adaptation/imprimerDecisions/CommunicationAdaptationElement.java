package ch.globaz.pegasus.process.adaptation.imprimerDecisions;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeAdaptationImpression;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeAdaptationImpressionAncienne;
import ch.globaz.pegasus.business.models.renteijapi.RenteMembreFamilleCalculeField;
import ch.globaz.pegasus.business.vo.pcaccordee.Regimes02RFMVo;
import ch.globaz.pegasus.businessimpl.utils.PegasusUtil;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

/**
 * Conteneur de données pour l'impression d'une communication d'adaptation.<br/>
 * En cas de couple séparé par la maladie, le conjoint a sa propre instance du conteneur avec ses montants propres.
 * 
 * @author eco
 * 
 */
public class CommunicationAdaptationElement implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public class ResumePrestations implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        public String designation, nomBeneficiaire, nssBeneficiaire, montantMensuelPrecendent, montantMensuel;
    }

    private final static String PC_LIBELLE = "PC";
    private final static String PC_MOITIE = " (1/2)";
    private final static String REGIME_RFM_LIBELLE = "Régime";

    private String dateSurDocument = null;

    private String dateValidite = null;

    private String idGestionnaire = null;

    private String idTiersAyantDroit = null;

    private boolean isHome = false;

    private boolean isInTypeChambreNonMedicalise = false;

    private final List<ResumePrestations> listePrestations = new ArrayList<CommunicationAdaptationElement.ResumePrestations>();
    private String nomAyantDroit = null;
    private String nssAyantDroit = null;

    private final Map<String, String> resumePlanCalcul = new HashMap<String, String>();
    private String totalMensuelPrecedent, totalMensuel = null;

    public void addClePlanCalcul(String cle, TupleDonneeRapport tupleroot) {
        resumePlanCalcul.put(cle, String.valueOf(tupleroot.getValeurEnfant(cle)));
    }

    public void addPCAccordee(PCAccordeeAdaptationImpression pca, PCAccordeeAdaptationImpressionAncienne pcaPrecedente)
            throws PCAccordeeException {

        if (!pca.isPrecedent()) {

            // s'il y a une prestation du conjoint, c'est c'est un pc a domicile avec 2 rentes principales.
            String designation = CommunicationAdaptationElement.PC_LIBELLE;

            isHome = IPCPCAccordee.CS_GENRE_PC_HOME.equals(pca.getCsGenrePC());

            if (!JadeStringUtil.isEmpty(pca.getNomConjoint())) {
                designation += CommunicationAdaptationElement.PC_MOITIE;
            }

            ResumePrestations entite = new ResumePrestations();

            entite.nomBeneficiaire = PegasusUtil.formatNomPrenom(pca.getNomAyantDroit(), pca.getPrenomAyantDroit());
            entite.nssBeneficiaire = pca.getNssAyantDroit();
            entite.designation = designation;

            entite.montantMensuel = pca.getMontantPrestationAyantDroit();
            if (pcaPrecedente != null) {
                entite.montantMensuelPrecendent = pcaPrecedente.getMontantPrestationAyantDroit();
            }

            listePrestations.add(entite);

            if (!JadeStringUtil.isEmpty(pca.getNomConjoint())) {
                ResumePrestations entiteConjoint = new ResumePrestations();

                entiteConjoint.designation = designation;
                entiteConjoint.nomBeneficiaire = PegasusUtil.formatNomPrenom(pca.getNomConjoint(),
                        pca.getPrenomConjoint());
                entiteConjoint.nssBeneficiaire = pca.getNssConjoint();
                entiteConjoint.montantMensuel = pca.getMontantPrestationConjoint();
                if (pcaPrecedente != null) {
                    entiteConjoint.montantMensuelPrecendent = pcaPrecedente.getMontantPrestationConjoint();
                    if (JadeStringUtil.isBlankOrZero(entiteConjoint.montantMensuelPrecendent)) {
                        entiteConjoint.montantMensuelPrecendent = "0";
                    }
                }

                listePrestations.add(entiteConjoint);
            }
        }

    }

    public void addPrestationDonneeFinanciere(RenteMembreFamilleCalculeField rente,
            RenteMembreFamilleCalculeField precedent) {
        ResumePrestations resume = new ResumePrestations();

        BSession session = BSessionUtil.getSessionFromThreadContext();

        String designation = null;
        String montant = null;
        String montantPrecedent = null;
        if (!JadeStringUtil.isIntegerEmpty(rente.getMontantApi())) {
            designation = "API (" + session.getCode(rente.getCsTypeRenteAPI()) + ")";
            montant = rente.getMontantApi();
            if (precedent != null) {
                montantPrecedent = precedent.getMontantApi();
            } else {
                montantPrecedent = "0";
            }
        } else {
            designation = "Rente (" + session.getCode(rente.getCsTypeRenteAVS()) + ")";

            montant = rente.getMontantAvsAi();
            if (precedent != null) {
                montantPrecedent = precedent.getMontantAvsAi();
            } else {
                montantPrecedent = "0";
            }
        }

        resume.designation = designation;
        resume.nomBeneficiaire = PegasusUtil.formatNomPrenom(rente.getNom(), rente.getPrenom());
        resume.nssBeneficiaire = rente.getNss();
        resume.montantMensuel = montant;
        resume.montantMensuelPrecendent = montantPrecedent;

        listePrestations.add(resume);
    }

    public void addRegimeRFM(Regimes02RFMVo regimeRFM, Regimes02RFMVo regimeRFMAncien) {

        // Ligne de prestaion RFM regime
        ResumePrestations entite = new ResumePrestations();
        entite.nomBeneficiaire = getNomBeneficiaireFromPCForRFMLigne();
        entite.nssBeneficiaire = getNSSBeneficiaireFromPCForRFMLigne();
        entite.designation = CommunicationAdaptationElement.REGIME_RFM_LIBELLE;

        // gestion du montant
        entite.montantMensuel = regimeRFM.getMontantPrestation();
        if (regimeRFMAncien != null) {
            entite.montantMensuelPrecendent = regimeRFMAncien.getMontantPrestation();
        } else {
            entite.montantMensuelPrecendent = regimeRFM.getMontantPrestation();
        }

        listePrestations.add(entite);

    }

    public String getDateSurDocument() {
        return dateSurDocument;
    }

    public String getDateValidite() {
        return dateValidite;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdTiersAyantDroit() {
        return idTiersAyantDroit;
    }

    public Boolean getIsHome() {
        return isHome;
    }

    public List<ResumePrestations> getListePrestations() {
        return listePrestations;
    }

    public String getNomAyantDroit() {
        return nomAyantDroit;
    }

    /**
     * Retourne la chaine nom, prenom
     * 
     * @return
     */
    private String getNomBeneficiaireFromPCForRFMLigne() {
        // Récupération de la première entité de ligne de prestation
        ResumePrestations resume = listePrestations.get(0);
        return resume.nomBeneficiaire;
    }

    public String getNssAyantDroit() {
        return nssAyantDroit;
    }

    private String getNSSBeneficiaireFromPCForRFMLigne() {
        // Récupération de la première entité de ligne de prestation
        ResumePrestations resume = listePrestations.get(0);
        return resume.nssBeneficiaire;
    }

    public Map<String, String> getResumePlanCalcul() {
        return resumePlanCalcul;
    }

    public String getTotalMensuel() {
        return totalMensuel;
    }

    public String getTotalMensuelPrecedent() {
        return totalMensuelPrecedent;
    }

    public boolean isInTypeChambreNonMedicalise() {
        return isInTypeChambreNonMedicalise;
    }

    public void setDateSurDocument(String dateSurDocument) {
        this.dateSurDocument = dateSurDocument;
    }

    public void setDateValidite(String dateValidite) {
        this.dateValidite = dateValidite;
    }

    public void setHome(boolean isHome) {
        this.isHome = isHome;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdTiersAyantDroit(String idTiersAyantDroit) {
        this.idTiersAyantDroit = idTiersAyantDroit;
    }

    public void setInTypeChambreNonMedicalise(boolean isInTypeChambreNonMedicalise) {
        this.isInTypeChambreNonMedicalise = isInTypeChambreNonMedicalise;
    }

    public void setIsHome(Boolean isHome) {
        this.isHome = isHome;
    }

    public void setNomAyantDroit(String nomAyantDroit) {
        this.nomAyantDroit = nomAyantDroit;
    }

    public void setNssAyantDroit(String nssAyantDroit) {
        this.nssAyantDroit = nssAyantDroit;
    }

    public void setTotalMensuel(String totalMensuel) {
        this.totalMensuel = totalMensuel;
    }

    public void setTotalMensuelPrecedent(String totalMensuelPrecedent) {
        this.totalMensuelPrecedent = totalMensuelPrecedent;
    }
}
