package globaz.osiris.eservices.service;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JANumberFormatter;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.contentieux.CALigneExtraitCompte;
import globaz.osiris.eservices.dto.ESInfoFacturationDTO;

public class ESCreationService {

    public ESCreationService() {
    }

    public ESInfoFacturationDTO.ESInfoFacturationSectionDTO createSection(CASection caSection, String langue) {
        ESInfoFacturationDTO.ESInfoFacturationSectionDTO section = new ESInfoFacturationDTO().new ESInfoFacturationSectionDTO();
        section.setSectionNumber(caSection.getIdExterne());
        section.setDescription(caSection.getDescription(langue));
        section.setDate(caSection.getDateSection());
        section.setBaseAmount(caSection.getBase());
        section.setPmtCmpAmount(caSection.getPmtCmp());
        section.setSolde(caSection.getSolde());
        section.setDueDate(caSection.getDateEcheance());

        return section;
    }

    public ESInfoFacturationDTO.ESInfoFacturationLigneExtraitCompteDTO createLigneExtraitCompte(CALigneExtraitCompte caLigneExtraitCompte, FWCurrency soldeCumule) {
        ESInfoFacturationDTO.ESInfoFacturationLigneExtraitCompteDTO ligneExtraitCompte = new ESInfoFacturationDTO().new ESInfoFacturationLigneExtraitCompteDTO();
        ligneExtraitCompte.setDateComptable(caLigneExtraitCompte.getDateJournal());
        ligneExtraitCompte.setDateValeur(caLigneExtraitCompte.getDate());
        ligneExtraitCompte.setDescription(caLigneExtraitCompte.getDescription());
        ligneExtraitCompte.setDoit(calculDoit(caLigneExtraitCompte));
        ligneExtraitCompte.setAvoir(calculAvoir(caLigneExtraitCompte));
        ligneExtraitCompte.setSolde(calculSoldeCumule(caLigneExtraitCompte, soldeCumule));
        ligneExtraitCompte.setDoit(caLigneExtraitCompte.getTotal());

        return ligneExtraitCompte;
    }

    private String calculSoldeCumule(CALigneExtraitCompte caLigneExtraitCompte, FWCurrency soldeCumule) {
        soldeCumule.add(caLigneExtraitCompte.getTotal());
        return JANumberFormatter.deQuote(soldeCumule.toStringFormat());
    }

    private String calculDoit(CALigneExtraitCompte caLigneExtraitCompte) {
        FWCurrency doit = new FWCurrency();
        doit.add(caLigneExtraitCompte.getTotal());
        if (doit.isPositive()) {
             return JANumberFormatter.deQuote(doit.toStringFormat());
        } else {
            return "";
        }
    }

    private String calculAvoir(CALigneExtraitCompte caLigneExtraitCompte) {
        FWCurrency avoir = new FWCurrency();
        avoir.add(caLigneExtraitCompte.getTotal());
        if (avoir.isNegative()) {
            avoir.negate();
            return JANumberFormatter.deQuote(avoir.toStringFormat());
        } else {
            return "";
        }
    }

}
