package globaz.phenix.db.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.divers.CPPeriodeFiscale;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionViewBean;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author jpa
 */
public class CPTraitementAnomaliesViewBean extends globaz.globall.db.BEntity implements java.io.Serializable,
        FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csCanton = "";
    // ajouter pour refactoring Phenix
    private String eMailAdress = "";
    private String receptionFileName = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */

    @Override
    protected String _getTableName() {
        // TODO Auto-generated method stub
        return null;
    }

    public void _logMessage(BTransaction transaction, String idLog, String message, String type) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    public void _suppressionDecision(BTransaction transaction) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    // fin ajout refactoring

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    public void deleteCas(BTransaction transaction) throws Exception {
        // TODO Auto-generated method stub

    }

    public AFAffiliation getAffiliation() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getAnnee1() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getAnnee2() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getAutreRevenu() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getAutreRevenuConjoint() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getCapital() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getCapitalEntreprise() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getCapitalEntrepriseConjoint() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getChangementGenre() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getChangementGenreConjoint() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getCodeCanton() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getCodeSexe() {
        // TODO Auto-generated method stub
        return null;
    }

    public CPCommunicationFiscale getCommunicationFiscale(int modeRecherche) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getCotisation1() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getCotisation2() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getCsCanton() {
        return csCanton;
    }

    public String getDateFortune() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getDateRetour() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getDebutExercice1() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getDebutExercice2() {
        // TODO Auto-generated method stub
        return null;
    }

    public CPDecision getDecisionDeBase() {
        // TODO Auto-generated method stub
        return null;
    }

    public CPDecisionViewBean getDecisionGeneree() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getEMailAdress() {
        return eMailAdress;
    }

    public String getEtatCivil() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getFinExercice1() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getFinExercice2() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getFortune() {
        // TODO Auto-generated method stub
        return null;
    }

    public Boolean getGeneration() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getGenreAffilie() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getGenreConjoint() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getGenreTaxation() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdAffiliation() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdAffiliationConjoint() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdCommunication() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdConjoint() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdIfd() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdJournalRetour() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdLog() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdParametrePlausi() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdRetour() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdTiers() {
        // TODO Auto-generated method stub
        return null;
    }

    public CPJournalRetour getJournalRetour() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getMajNumContribuable() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getNumAffilieRecu() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getNumAvs() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getNumContribuableRecu() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getNumIfd() {
        // TODO Auto-generated method stub
        return null;
    }

    public CPPeriodeFiscale getPeriodeFiscale() {
        // TODO Auto-generated method stub
        return null;
    }

    public LinkedList<String> getReaders() {
        CPReceptionReaderManager cantonList = new CPReceptionReaderManager();
        cantonList.setSession(getSession());
        LinkedList<String> resultList = new LinkedList<String>();
        try {
            cantonList.find();

            // Canton dans lequel se trouve la caisse
            String isoCanton = ((CPApplication) getSession().getApplication()).getCantonCaisse();

            Iterator<?> it = cantonList.iterator();
            while (it.hasNext()) {
                CPReceptionReader reader = (CPReceptionReader) it.next();
                // Si le reader est celui du canton dans lequel se trouve la
                // caisse,
                // on le met à la tête de la liste.
                if (reader.getIsoCanton().equals(isoCanton)) {
                    resultList.addFirst(reader.getNomCanton());
                    // resultList.addFirst((reader.isFormatXml()?"xml:":"txt:")
                    // + reader.getNomClass());
                    resultList.addFirst(reader.getIdCanton());
                } else {
                    // resultList.addLast((reader.isFormatXml()?"xml:":"txt:")
                    // + reader.getNomClass());
                    resultList.addLast(reader.getIdCanton());
                    resultList.addLast(reader.getNomCanton());
                }
            }
            if (resultList.isEmpty()) {
                setMessage(getSession().getLabel("CP_MSG_0000"));
            }
            return resultList;
        } catch (Exception e) {
            setMessage(getSession().getLabel("CP_MSG_0000"));
            return resultList;
        }
    }

    public String getReceptionFileName() {
        return receptionFileName;
    }

    public String getRevenu1() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getRevenu2() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getRevenuA() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getRevenuAConjoint() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getRevenuNA() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getRevenuNAConjoint() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getRevenuR() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getSalaire() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getSalaireConjoint() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getStatus() {
        // TODO Auto-generated method stub
        return null;
    }

    public TITiersViewBean getTiers() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getValeurChampRecherche() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getValeurRechercheBD(String zoneRecherche) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getVisibleStatus() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isNonActif() {
        // TODO Auto-generated method stub
        return false;
    }

    public void removeLogs(String logSource, BTransaction transaction) throws Exception {
        // TODO Auto-generated method stub

    }

    public void setAnnee1(String annee) {
        // TODO Auto-generated method stub

    }

    public void setAnnee2(String string) {
        // TODO Auto-generated method stub

    }

    public void setCapital(String newCapital) {
        // TODO Auto-generated method stub

    }

    public void setChangementGenre(String changementGenre) {
        // TODO Auto-generated method stub

    }

    public void setChangementGenreConjoint(String newChangementGenreConjoint) {
        // TODO Auto-generated method stub

    }

    public void setCommunicationFiscale(CPCommunicationFiscale fiscale) {
        // TODO Auto-generated method stub

    }

    public void setCotisation1(String newCotisation1) {
        // TODO Auto-generated method stub

    }

    public void setCsCanton(String csCanton) {
        this.csCanton = csCanton;
    }

    public void setDateFortune(String newDateFortune) {
        // TODO Auto-generated method stub

    }

    public void setDateRetour(String dateRetour) {
        // TODO Auto-generated method stub

    }

    public void setDebutExercice1(String newDebutExercice1) {
        // TODO Auto-generated method stub

    }

    public void setDebutExercice2(String newDebutExercice2) {
        // TODO Auto-generated method stub

    }

    public void setDecisionDeBase(CPDecision decision) {
        // TODO Auto-generated method stub

    }

    public void setDecisionGeneree(CPDecisionViewBean decision) {
        // TODO Auto-generated method stub

    }

    public void setEMailAdress(String mailAdress) {
        eMailAdress = mailAdress;
    }

    public void setFinExercice1(String newFinExercice1) {
        // TODO Auto-generated method stub

    }

    public void setFinExercice2(String newFinExercice2) {
        // TODO Auto-generated method stub

    }

    public void setFortune(String newFortuneTotale) {
        // TODO Auto-generated method stub

    }

    public void setGeneration(Boolean generation) {
        // TODO Auto-generated method stub

    }

    public void setGenreAffilie(String newGenreAffilie) {
        // TODO Auto-generated method stub

    }

    public void setGenreConjoint(String newGenreconjoint) {
        // TODO Auto-generated method stub

    }

    public void setGenreTaxation(String newGenreTaxation) {
        // TODO Auto-generated method stub

    }

    public void setIdAffiliation(String idAffiliation) {
        // TODO Auto-generated method stub

    }

    public void setIdAffiliationConjoint(String idAffiliationConjoint) {
        // TODO Auto-generated method stub

    }

    public void setIdCommunication(String idCommunication) {
        // TODO Auto-generated method stub

    }

    public void setIdConjoint(String idConjoint) {
        // TODO Auto-generated method stub

    }

    public void setIdIfd(String idIfd) {
        // TODO Auto-generated method stub

    }

    public void setIdJournalRetour(String string) {
        // TODO Auto-generated method stub

    }

    public void setIdLog(String idLog) {
        // TODO Auto-generated method stub

    }

    public void setIdParametrePlausi(String idParametrePlausi) {
        // TODO Auto-generated method stub

    }

    public void setIdRetour(String idRetour) {
        // TODO Auto-generated method stub

    }

    public void setIdTiers(String idTiers) {
        // TODO Auto-generated method stub

    }

    public void setJournalRetour(CPJournalRetour journal) {
        // TODO Auto-generated method stub

    }

    public void setMajNumContribuable(String string) {
        // TODO Auto-generated method stub

    }

    public void setNumIfd(String string) {
        // TODO Auto-generated method stub

    }

    public void setPeriodeFiscale(CPPeriodeFiscale fiscale) {
        // TODO Auto-generated method stub

    }

    public void setReceptionFileName(String receptionFileName) {
        this.receptionFileName = receptionFileName;
    }

    public void setRevenu1(String newRevenu1) {
        // TODO Auto-generated method stub

    }

    public void setRevenu2(String newRevenu2) {
        // TODO Auto-generated method stub

    }

    public void setRevenuAnnee1(String string) {
        // TODO Auto-generated method stub

    }

    public void setRevenuAnnee2(String string) {
        // TODO Auto-generated method stub

    }

    public void setStatus(String codeErreur) {
        // TODO Auto-generated method stub

    }

    public void setTiers(TITiersViewBean tiers) {
        // TODO Auto-generated method stub

    }

    public void setValeurChampRecherche(String string) {
        // TODO Auto-generated method stub

    }

    public void setWantAfterRetrieve(boolean newBoolean) {
        // TODO Auto-generated method stub

    }

    public void updateCas(BTransaction transaction) throws Exception {
        // TODO Auto-generated method stub

    }

}
