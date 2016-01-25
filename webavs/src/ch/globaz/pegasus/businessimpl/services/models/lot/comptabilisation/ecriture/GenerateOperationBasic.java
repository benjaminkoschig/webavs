package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.jade.exception.JadeApplicationException;
import globaz.osiris.api.APIEcriture;
import java.math.BigDecimal;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;

class GenerateOperationBasic {

    protected Ecriture generateEcriture(SectionPegasus section, String codeDebitCredit, BigDecimal montant,
            String idCompteAnnexe, String idRefRubrique, TypeEcriture typeEcriture) throws ComptabiliserLotException {
        return this.generateEcriture(section, codeDebitCredit, idRefRubrique, montant, idCompteAnnexe, typeEcriture,
                null);

    }

    protected Ecriture generateEcriture(SectionPegasus sectionEnum, String codeDebitCredit, String idRefRubrique,
            BigDecimal montant, SectionSimpleModel section, String idCompteAnnexe, TypeEcriture typeEcriture,
            OrdreVersement ov) throws ComptabiliserLotException {

        if (codeDebitCredit == null) {
            throw new IllegalArgumentException("Unable to generateEcriture, the codeDebitCredit is null!");
        }

        if (idRefRubrique == null) {
            throw new IllegalArgumentException("Unable to generateEcriture, the idRefRubrique is null!");
        }

        if (montant == null) {
            throw new IllegalArgumentException("Unable to generateEcriture, the montant is null!");
        }

        if (idCompteAnnexe == null) {
            throw new IllegalArgumentException("Unable to generateEcriture, the  idCompteAnnexe is null!");
        }

        Ecriture ecriture = new Ecriture();
        ecriture.setSection(sectionEnum);
        ecriture.setCodeDebitCredit(codeDebitCredit);
        ecriture.setOrdreVersement(ov);
        ecriture.setIdRefRubrique(idRefRubrique);
        ecriture.setMontant(montant);
        ecriture.setSectionSimple(section);
        if (idCompteAnnexe != null) {
            ecriture.setCompteAnnexe(CompteAnnexeResolver.resolveByIdCompteAnnexe(idCompteAnnexe));
        }
        ecriture.setTypeEcriture(typeEcriture);
        return ecriture;
    }

    protected Ecriture generateEcriture(SectionPegasus sectionEnum, String codeDebitCredit, String idRefRubrique,
            BigDecimal montant, String idCompteAnnexe, TypeEcriture typeEcriture, OrdreVersement ov)
            throws ComptabiliserLotException {
        return this.generateEcriture(sectionEnum, codeDebitCredit, idRefRubrique, montant, null, idCompteAnnexe,
                typeEcriture, ov);
    }

    protected Ecriture generateEcritureCredit(SectionPegasus section, BigDecimal montant, String idCompteAnnexe,
            TypeEcriture typeEcriture, OrdreVersement ov) throws JadeApplicationException {
        return generateEcritureStandard(section, APIEcriture.CREDIT, montant, idCompteAnnexe, typeEcriture, ov);
    }

    /**
     * Est principalement utilisé pour les ordres de versement de type: Restitution, beneficiaire et allocation de noel
     * 
     * @param section
     * @param codeDebitCredit
     * @param montant
     * @param idCompteAnnexe
     * @param typeEcriture
     * @param ov
     * @return
     * @throws JadeApplicationException
     */
    protected Ecriture generateEcritureStandard(SectionPegasus section, String codeDebitCredit, BigDecimal montant,
            String idCompteAnnexe, TypeEcriture typeEcriture, OrdreVersement ov) throws JadeApplicationException {
        if (ov != null) {
            if (ov.isPositif()) {
                String idRubrique = resolveIdRefRubrique(ov);
                return this.generateEcriture(section, codeDebitCredit, idRubrique, montant, idCompteAnnexe,
                        typeEcriture, ov);
            }
        }
        return null;
    }

    //
    // protected Ecriture generateEcritureCredit(SectionPegasus section, String idCompteAnnexe, OrdreVersementForList
    // ov)
    // throws JadeApplicationException {
    // return this.generateEcriture(section, APIEcriture.CREDIT, new BigDecimal(ov.getSimpleOrdreVersement()
    // .getMontant()), idCompteAnnexe, ov);
    // }
    //
    // protected Ecriture generateEcritureDebit(SectionPegasus section, BigDecimal montant, String idCompteAnnexe,
    // OrdreVersementForList ov) throws JadeApplicationException {
    // return this.generateEcriture(section, APIEcriture.DEBIT, montant, idCompteAnnexe, ov);
    // }
    //
    // protected Ecriture generateEcritureDebit(SectionPegasus section, String idCompteAnnexe, OrdreVersementForList ov)
    // throws JadeApplicationException {
    // return this.generateEcriture(section, APIEcriture.DEBIT, new BigDecimal(ov.getSimpleOrdreVersement()
    // .getMontant()), idCompteAnnexe, ov);
    // }

    protected String resolveIdRefRubrique(OrdreVersement ov) throws JadeApplicationException {
        return HandlerRubrique.resolveIdRefRubrique(ov);
    }
}
