package ch.globaz.al.businessimpl.rafam;

import ch.eahv_iv.xmlns.eahv_iv_fao_empl._0.AllowanceType;
import ch.eahv_iv.xmlns.eahv_iv_fao_empl._0.BeneficiaryType;
import ch.eahv_iv.xmlns.eahv_iv_fao_empl._0.ChildAllowanceType;
import ch.eahv_iv.xmlns.eahv_iv_fao_empl._0.ChildType;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.constantes.enumerations.RafamEvDeclencheur;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.al.utils.ALRafamUtils;

public class ContextAnnonceRafamDelegue extends ContextAnnonceRafam {
    /**
     * Retourne une instance de <code>ContextAnnonceRafam</code>
     *
     * @param dossier
     *                    Dossier en cours de traitement
     * @param droit
     *                    Droit en cours de traitement. Doit être lié au dossier
     *
     * @return nouvelle instance du contexte
     *
     * @throws ALAnnonceRafamException
     *                                     Exception levée si l'un des paramètres n'est pas valide.
     */
    public static ContextAnnonceRafamDelegue getContext(RafamEvDeclencheur evDecl, RafamEtatAnnonce etat,
            String idEmployeur, ChildAllowanceType header, BeneficiaryType beneficiary, ChildType child,
            AllowanceType allowance) throws ALAnnonceRafamException {

        if ((beneficiary == null)) {
            throw new ALAnnonceRafamException("ContextAnnonceRafamDelegue#getContext : beneficiary is null");
        }

        ContextAnnonceRafamDelegue context = new ContextAnnonceRafamDelegue();

        // TODO: check evDecl,etat et idEmployeur (2positionsmax)
        context.idEmployeur = idEmployeur;

        context.header = new ChildAllowanceType();

        context.header.setBusinessID(header.getBusinessID());
        context.header.setCompanyName(header.getCompanyName());
        context.header.setEventDate(header.getEventDate());
        context.header.setFakID(header.getFakID());
        context.header.setFakName(header.getFakName());
        context.header.setMailResponsiblePerson(header.getMailResponsiblePerson());
        context.header.setMessageDate(header.getMessageDate());
        context.header.setMessageID(header.getMessageID());
        context.header.setNameResponsiblePerson(header.getNameResponsiblePerson());
        context.header.setReferenceMessageID(header.getMessageID());
        context.header.setTelResponsiblePerson(header.getTelResponsiblePerson());
        // seule les données header
        context.header.getBeneficiary().clear();

        context.beneficiary = new BeneficiaryType();
        context.beneficiary.setBeneficiaryAHVN13(beneficiary.getBeneficiaryAHVN13());
        context.beneficiary.setBeneficiaryDateOfBirth(beneficiary.getBeneficiaryDateOfBirth());
        context.beneficiary.setBeneficiaryDateOfDeath(beneficiary.getBeneficiaryDateOfDeath());
        context.beneficiary.setBeneficiaryEndDateEmployment(beneficiary.getBeneficiaryEndDateEmployment());
        context.beneficiary.setBeneficiaryFirstName(beneficiary.getBeneficiaryFirstName());
        context.beneficiary.setBeneficiaryGender(beneficiary.getBeneficiaryGender());
        context.beneficiary.setBeneficiaryStartDateEmployment(beneficiary.getBeneficiaryStartDateEmployment());
        context.beneficiary.setBeneficiaryStatus(beneficiary.getBeneficiaryStatus());
        context.beneficiary.setBeneficiarySurname(beneficiary.getBeneficiarySurname());
        // seule les données bénéficiaires sont utilisés
        context.beneficiary.getChild().clear();

        context.child = new ChildType();
        context.child.setChildAHVN13(child.getChildAHVN13());
        context.child.setChildDateOfBirth(child.getChildDateOfBirth());
        context.child.setChildFamilyRelation(child.getChildFamilyRelation());
        context.child.setChildFirstName(child.getChildFirstName());
        context.child.setChildGender(child.getChildGender());
        context.child.setChildSurname(child.getChildSurname());
        // seule les données childs sont utilisés
        context.child.getAllowance().clear();

        context.allowance = new AllowanceType();
        context.allowance.setAllowanceAmount(allowance.getAllowanceAmount());
        context.allowance.setAllowanceApplicableLegislation(allowance.getAllowanceApplicableLegislation());
        context.allowance.setAllowanceBenefitCanton(allowance.getAllowanceBenefitCanton());
        context.allowance.setAllowanceCompleteStorno(allowance.getAllowanceCompleteStorno());
        context.allowance.setAllowanceDateErrorFrom(allowance.getAllowanceDateErrorFrom());
        context.allowance.setAllowanceDateErrorTo(allowance.getAllowanceDateErrorTo());
        context.allowance.setAllowanceDateFrom(allowance.getAllowanceDateFrom());
        context.allowance.setAllowanceDateTo(allowance.getAllowanceDateTo());
        context.allowance.setAllowanceErrorCode(allowance.getAllowanceErrorCode());
        context.allowance.setAllowanceErrorMessage(allowance.getAllowanceErrorMessage());
        context.allowance.setAllowancePeriodeAmount(allowance.getAllowancePeriodeAmount());
        context.allowance.setAllowancePeriodeFrom(allowance.getAllowancePeriodeFrom());
        context.allowance.setAllowancePeriodeTo(allowance.getAllowancePeriodeTo());
        context.allowance.setAllowanceRefNumber(allowance.getAllowanceRefNumber());
        context.allowance.setAllowanceType(allowance.getAllowanceType());
        context.allowance.setAllowanceChildCountryResidence(
                ALRafamUtils.formatCountryIDToThreePositions(allowance.getAllowanceChildCountryResidence()));

        context.setEtat(etat);
        context.setEvDecl(evDecl);

        return context;
    }

    private AllowanceType allowance = null;
    /**
     * la liste de childType de BeneficiaryType n'est pas utilisé dans cet attribut de ContextAnnonceRafam
     */
    private BeneficiaryType beneficiary = null;
    /**
     * la liste de allowanceType de ChildType n'est pas utilisé dans cet attribut de ContextAnnonceRafam
     */
    private ChildType child = null;
    /**
     * la liste de BeneficiaryType de ChildAllowanceType n'est pas utilisé dans cet attribut de ContextAnnonceRafam
     */
    private ChildAllowanceType header = null;
    private String idEmployeur = null;

    public AllowanceType getAllowance() {
        return allowance;
    }

    public BeneficiaryType getBeneficiary() {
        return beneficiary;
    }

    public ChildType getChild() {
        return child;
    }

    public ChildAllowanceType getHeader() {
        return header;
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public void setAllowance(AllowanceType allowance) {
        this.allowance = allowance;
    }

    public void setBeneficiary(BeneficiaryType beneficiary) {
        this.beneficiary = beneficiary;
    }

    public void setChild(ChildType child) {
        this.child = child;
    }

    public void setHeader(ChildAllowanceType header) {
        this.header = header;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }
}
