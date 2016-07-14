package globaz.phenix.interfaces;

import globaz.globall.api.BIEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.phenix.db.communications.CPCommunicationFiscale;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.db.divers.CPPeriodeFiscale;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionViewBean;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public interface ICommunicationRetour extends BIEntity {
    public void _logMessage(BTransaction transaction, String idLog, String message, String type) throws Exception;

    public void _suppressionDecision(BTransaction transaction, String idTiers) throws Exception;

    public abstract void deleteCas(BTransaction transaction) throws Exception;

    public abstract AFAffiliation getAffiliation();

    public abstract AFAffiliation getAffiliationConjoint();

    public String getAnnee1();

    public java.lang.String getAnnee2();

    public abstract String getAutreRevenu();

    public abstract String getAutreRevenuConjoint();

    public java.lang.String getCapital();

    public abstract String getCapitalEntreprise();

    public abstract String getCapitalEntrepriseConjoint();

    public abstract String getChangementGenre();

    public abstract String getChangementGenreConjoint();

    public String getCodeCanton();

    public abstract String getCodeSexe();

    public CPCommunicationFiscale getCommunicationFiscale(int modeRecherche);

    public abstract TITiersViewBean getConjoint();

    public java.lang.String getCotisation1();

    public java.lang.String getCotisation2();

    public java.lang.String getDateFortune();

    public java.lang.String getDateRetour();

    public String getDebutExercice1();

    public String getDebutExercice2();

    public CPDecision getDecisionDeBase();

    public CPDecisionViewBean getDecisionGeneree();

    public abstract String getDescription(int cas);

    public abstract String getEtatCivil();

    public String getFinExercice1();

    public String getFinExercice2();

    public java.lang.String getFortune();

    public Boolean getGeneration();

    public abstract String getGenreAffilie();

    public abstract String getGenreConjoint();

    public String getGenreTaxation();

    public String getIdAffiliation();

    public abstract String getIdAffiliationConjoint();

    public java.lang.String getIdCommunication();

    public String getIdConjoint();

    public String getIdIfd();

    public String getIdJournalRetour();

    public String getIdLog();

    public abstract String getIdParametrePlausi();

    public String getIdRetour();

    public String getIdTiers();

    public abstract CPJournalRetour getJournalRetour();

    public abstract String getMajNumContribuable();

    public String getMsgType();

    public abstract String getNumAffilieRecu();

    // public abstract String getNumeroAVS();
    public abstract String getNumAvs(int codeFormat);

    public abstract String getNumAvsFisc(int codeFormat);

    public abstract String getNumContribuableRecu();

    public java.lang.String getNumIfd();

    public abstract CPPeriodeFiscale getPeriodeFiscale();

    public abstract String getRemarque();

    public abstract String getRachatLpp();

    public abstract String getRachatLppCjt();

    public String getReportType();

    public java.lang.String getRevenu1();

    public java.lang.String getRevenu2();

    public abstract String getRevenuA();

    public abstract String getRevenuAConjoint();

    public abstract String getRevenuNA();

    public abstract String getRevenuNAConjoint();

    public abstract String getRevenuR();

    public abstract String getSalaire();

    public abstract String getSalaireConjoint();

    public String getStatus();

    public abstract TITiersViewBean getTiers();

    public abstract String getTri();

    public abstract String getValeurChampRecherche();

    public abstract String getValeurRechercheBD(String zoneRecherche);

    public String getVisibleStatus();

    public abstract boolean isNonActif();

    public abstract boolean isNonActif(boolean traitementConjoint);

    public void removeLogs(String logSource, BTransaction transaction) throws Exception;

    public abstract void retrieve() throws java.lang.Exception;

    public void setAnnee1(java.lang.String annee);

    public void setAnnee2(java.lang.String string);

    public void setCapital(java.lang.String newCapital);

    public void setChangementGenre(String changementGenre);

    public void setChangementGenreConjoint(java.lang.String newChangementGenreConjoint);

    public void setCommunicationFiscale(CPCommunicationFiscale fiscale);

    public void setCotisation1(java.lang.String newCotisation1);

    public void setDateFortune(java.lang.String newDateFortune);

    public void setDateRetour(java.lang.String dateRetour);

    public void setDebutExercice1(java.lang.String newDebutExercice1);

    public void setDebutExercice2(java.lang.String newDebutExercice2);

    public void setDecisionDeBase(CPDecision decision);

    public void setDecisionGeneree(CPDecisionViewBean decision);

    public void setFinExercice1(java.lang.String newFinExercice1);

    public void setFinExercice2(java.lang.String newFinExercice2);

    public void setForBackup(boolean isForBackup);

    public void setFortune(java.lang.String newFortuneTotale);

    public void setGeneration(Boolean generation);

    public void setGenreAffilie(java.lang.String newGenreAffilie);

    public void setGenreConjoint(java.lang.String newGenreconjoint);

    public void setGenreTaxation(java.lang.String newGenreTaxation);

    public void setIdAffiliation(String idAffiliation);

    public void setIdAffiliationConjoint(String idAffiliationConjoint);

    public void setIdCommunication(String idCommunication);

    public void setIdConjoint(String idConjoint);

    public void setIdIfd(String idIfd);

    public void setIdJournalRetour(java.lang.String string);

    public void setIdLog(String idLog);

    public void setIdParametrePlausi(String idParametrePlausi);

    public void setIdRetour(java.lang.String idRetour);

    public void setIdTiers(String idTiers);

    public void setJournalRetour(CPJournalRetour journal);

    public void setMajNumContribuable(java.lang.String string);

    public void setNumAffilie(java.lang.String string);

    public void setNumIfd(java.lang.String string);

    public void setPeriodeFiscale(CPPeriodeFiscale fiscale);

    public void setRevenu1(java.lang.String newRevenu1);

    public void setRevenu2(java.lang.String newRevenu2);

    public void setSession(BSession session);

    public void setStatus(java.lang.String codeErreur);

    public void setTiers(TITiersViewBean tiers);

    public void setTri(String tri);

    public void setValeurChampRecherche(java.lang.String string);

    public void setWantAfterRetrieve(boolean newBoolean);

    public void setWantDonneeBase(boolean b);

    public void setWantDonneeContribuable(boolean b);

    public abstract void updateCas(BTransaction transaction) throws Exception;

    public void wantCallValidate(boolean newBoolean);

    public String getMontantTotalRenteAVS();

    public String getMessageRenteAVS();

    public void setMontantTotalRenteAVS(String montantTotalRenteAVS);

    public void setMessageRenteAVS(String messageRenteAVS);
}
