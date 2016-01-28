package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import globaz.globall.util.JADate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.pegasus.business.models.lot.OrdreVersementForList;
import ch.globaz.pegasus.business.models.lot.OrdreversementTiers;

public class ComptabilisationData {
    private List<CompteAnnexeSimpleModel> comptesAnnexes;
    private JADate dateDernierPmt;
    private JADate dateEchance;
    private JADate dateValeur;
    private String idOrganeExecution;
    private PegasusJournalConteneur journalConteneur;
    private String libelleJournal;
    private List<OrdreVersementForList> listOV;
    private Map<String, OrdreversementTiers> mapIdTierDescription = new HashMap<String, OrdreversementTiers>();
    private String numeroOG;
    private List<SectionSimpleModel> sections = new ArrayList<SectionSimpleModel>();
    private SimpleLot simpleLot;

    public List<CompteAnnexeSimpleModel> getComptesAnnexes() {
        return comptesAnnexes;
    }

    public JADate getDateDernierPmt() {
        return dateDernierPmt;
    }

    public JADate getDateEchance() {
        return dateEchance;
    }

    public JADate getDateValeur() {
        return dateValeur;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    public PegasusJournalConteneur getJournalConteneur() {
        return journalConteneur;
    }

    public String getLibelleJournal() {
        return libelleJournal;
    }

    public List<OrdreVersementForList> getListOV() {
        return listOV;
    }

    public Map<String, OrdreversementTiers> getMapIdTierDescription() {
        return mapIdTierDescription;
    }

    public String getNumeroOG() {
        return numeroOG;
    }

    public List<SectionSimpleModel> getSections() {
        return sections;
    }

    public SimpleLot getSimpleLot() {
        return simpleLot;
    }

    public void setComptesAnnexes(List<CompteAnnexeSimpleModel> comptesAnnexes) {
        this.comptesAnnexes = comptesAnnexes;
    }

    public void setDateDernierPmt(JADate dateDernierPmt) {
        this.dateDernierPmt = dateDernierPmt;
    }

    public void setDateEchance(JADate dateEchance) {
        this.dateEchance = dateEchance;
    }

    public void setDateValeur(JADate dateValeur) {
        this.dateValeur = dateValeur;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    public void setJournalConteneur(PegasusJournalConteneur journalConteneur) {
        this.journalConteneur = journalConteneur;
    }

    public void setLibelleJournal(String libelleJournal) {
        this.libelleJournal = libelleJournal;
    }

    public void setListOV(List<OrdreVersementForList> listOV) {
        this.listOV = listOV;
    }

    public void setMapIdTierDescription(Map<String, OrdreversementTiers> map) {
        mapIdTierDescription = map;
    }

    public void setNumeroOG(String numeroOG) {
        this.numeroOG = numeroOG;
    }

    public void setSections(List<SectionSimpleModel> sections) {
        this.sections = sections;
    }

    public void setSimpleLot(SimpleLot simpleLot) {
        this.simpleLot = simpleLot;
    }
}
