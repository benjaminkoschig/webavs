package ch.globaz.vulpecula.process.listedivers;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.listedivers.ListeDivers;
import ch.globaz.vulpecula.external.BProcessWithContext;

public class ListeDiversProcess extends BProcessWithContext implements Observer {
    private static final long serialVersionUID = -5915275758727848732L;

    private Annee annee;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        print();
        return true;
    }

    private void print() throws IOException {
        ListeDiversExcel listeDiversExcel = new ListeDiversExcel(getSession(),
                DocumentConstants.LISTES_DIVERSES_FILE_NAME, DocumentConstants.LISTES_DIVERSES_DOC_NAME);

        List<ListeDivers> affiliationsRadiees = getAffiliationsRadies();
        List<ListeDivers> affiliationsSansPers = getAffiliationsSansPers();
        List<ListeDivers> affiliationsAvecPersSansCP = getAffiliationsAvecPersSansCP();
        List<ListeDivers> affiliationsPlusieursCP = getAffiliationsPlusieursCP();
        List<ListeDivers> affiliationsPerAnn = getAffiliationsPerAnnu();
        List<ListeDivers> affiliationsParConvParLan = getAffiliationsParConvParLan();

        listeDiversExcel.setListeAffiliesRadies(affiliationsRadiees);
        listeDiversExcel.setListeAffiliesSansPerso(affiliationsSansPers);
        listeDiversExcel.setListeAffiliesAvecPersoSansCP(affiliationsAvecPersSansCP);
        listeDiversExcel.setListePlusieursCP(affiliationsPlusieursCP);
        listeDiversExcel.setListeAffiliesAnnuels(affiliationsPerAnn);
        listeDiversExcel.setListeNbreActifParConventionEtLangue(affiliationsParConvParLan);
        listeDiversExcel.setAnnee(annee);

        listeDiversExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), listeDiversExcel.getOutputFile());
    }

    private List<ListeDivers> getAffiliationsRadies() {
        return QueryExecutor.execute(sqlFindAffiliesRadies(annee.toString()), ListeDivers.class);
    }

    private List<ListeDivers> getAffiliationsSansPers() {
        return QueryExecutor.execute(sqlFindEntrepriseMentionneesSansPerso(annee.toString()), ListeDivers.class);
    }

    private List<ListeDivers> getAffiliationsAvecPersSansCP() {
        return QueryExecutor.execute(sqlFindEntreprisesAvecPersoSansCP(annee.toString()), ListeDivers.class);
    }

    private List<ListeDivers> getAffiliationsPlusieursCP() {
        return QueryExecutor.execute(sqlFindEntreprisesAvecPlusieursCP(annee.toString()), ListeDivers.class);
    }

    private List<ListeDivers> getAffiliationsPerAnnu() {
        return QueryExecutor.execute(sqlFindAffiliesAnnuels(annee.toString()), ListeDivers.class);
    }

    private List<ListeDivers> getAffiliationsParConvParLan() {
        return QueryExecutor.execute(sqlFindNbreEmployeursParConvEtLangue(annee.toString()), ListeDivers.class);
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub

    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_DIVERSES_NAME;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public Annee getAnnee() {
        return annee;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    private String sqlFindAffiliesAnnuels(String annee) {
        return "SELECT MALNAF AS no_affilie, MADESC AS raison_social, MADDEB AS date_debut_affiliation, "
                + "MADFIN AS date_fin_affiliation, motif.PCOLUT AS motif_radiation FROM SCHEMA.AFAFFIP aff "
                + "inner join schema.PT_EMPLOYEURS emp on emp.ID_AFAFFIP = aff.MAIAFF "
                + "left join schema.fwcoup motif on motif.pcosid = aff.MATMOT and plaide='F' "
                + "where (aff.madfin=0 or aff.madfin>=" + annee + "0101) AND aff.maddeb <= " + annee
                + "1231 and aff.matper=802004";
    }

    private String sqlFindEntreprisesAvecPlusieursCP(String annee) {
        return "select MALNAF AS no_affilie, MADESC AS raison_social, MADDEB AS date_debut_affiliation, "
                + "MADFIN AS date_fin_affiliation, motif.PCOLUT AS motif_radiation FROM SCHEMA.AFAFFIP aff "
                + "left join schema.fwcoup motif on motif.pcosid = aff.MATMOT and plaide='F' " + "where maiaff in("
                + "select dec.ID_PT_EMPLOYEURS from schema.PT_DECOMPTES dec "
                + "left join schema.PT_TAXATIONS_OFFICE off on off.ID_PT_DECOMPTES=dec.ID "
                + "inner join schema.afaffip aff on aff.MAIAFF=dec.ID_PT_EMPLOYEURS "
                + "where dec.CS_TYPE=68014002 and dec.PERIODE_DEBUT > " + annee + "00  and dec.PERIODE_FIN <" + annee
                + "13 and dec.CS_ETAT<>68012008 and (off.CS_ETAT <> 68019005 or off.CS_ETAT is null) "
                + "group by dec.ID_PT_EMPLOYEURS having count(*)>1) order by aff.malnaf";
    }

    private String sqlFindEntreprisesAvecPersoSansCP(String annee) {
        Integer iAnnee = Integer.valueOf(annee);
        return "SELECT MALNAF AS no_affilie, MADESC AS raison_social, MADDEB AS date_debut_affiliation, MADFIN AS date_fin_affiliation, motif.PCOLUT AS motif_radiation "
                + "FROM SCHEMA.afaffip aff "
                + "LEFT JOIN SCHEMA.AFPARTP part on aff.maiaff=part.maiaff AND part.MFTPAR=818011 "
                + "LEFT JOIN SCHEMA.fwcoup motif on motif.pcosid = aff.MATMOT AND plaide='F' " + "WHERE aff.maddeb<="
                + iAnnee.toString()
                + "1231 AND (aff.madfin=0 OR aff.madfin>"
                + iAnnee.toString()
                + "0101) AND ((part.mfdfin<"
                + iAnnee.toString()
                + "0101 AND part.mfdfin<>0) OR (part.mfddeb>"
                + iAnnee.toString()
                + "1231)) "
                + "AND aff.maiaff not in( "
                + "SELECT dec.ID_PT_EMPLOYEURS "
                + "FROM SCHEMA.PT_DECOMPTES dec "
                + "LEFT JOIN SCHEMA.PT_TAXATIONS_OFFICE off on off.ID_PT_DECOMPTES=dec.ID "
                + "INNER JOIN SCHEMA.afaffip aff on aff.MAIAFF=dec.ID_PT_EMPLOYEURS "
                + "WHERE dec.CS_TYPE=68014002 AND dec.PERIODE_DEBUT > "
                + iAnnee.toString()
                + "00  AND dec.PERIODE_FIN <"
                + iAnnee.toString()
                + "13 AND (off.CS_ETAT <> 68019005 OR off.CS_ETAT IS NULL)"
                + "GROUP BY dec.ID_PT_EMPLOYEURS) "
                + "AND aff.MALNAF not in( "
                + "SELECT malnaf FROM SCHEMA.afaffip aff "
                + "INNER JOIN SCHEMA.AFPARTP part on aff.maiaff=part.maiaff AND part.MFTPAR=818011 "
                + "LEFT JOIN SCHEMA.fwcoup motif on motif.pcosid = aff.MATMOT AND plaide='F' WHERE aff.MALNAF in("
                + "SELECT aff.malnaf FROM SCHEMA.afaffip aff "
                + "LEFT JOIN SCHEMA.AFPARTP part on aff.maiaff=part.maiaff AND part.MFTPAR=818011 "
                + "WHERE aff.maddeb<"
                + iAnnee.toString()
                + "1231 AND (aff.madfin=0 OR aff.madfin>"
                + iAnnee.toString()
                + "0101) AND aff.maiaff not in("
                + "SELECT dec.ID_PT_EMPLOYEURS "
                + "FROM SCHEMA.PT_DECOMPTES dec "
                + "LEFT JOIN SCHEMA.PT_TAXATIONS_OFFICE off on off.ID_PT_DECOMPTES=dec.ID "
                + "INNER JOIN SCHEMA.afaffip aff on aff.MAIAFF=dec.ID_PT_EMPLOYEURS "
                + "WHERE dec.CS_TYPE=68014002 AND dec.PERIODE_DEBUT > "
                + iAnnee.toString()
                + "00  AND dec.PERIODE_FIN < "
                + iAnnee.toString()
                + "13 AND (off.CS_ETAT <> 68019005 OR off.CS_ETAT IS NULL) "
                + "GROUP BY dec.ID_PT_EMPLOYEURS) "
                + "GROUP BY aff.malnaf having count(*)>1 " + "ORDER BY aff.malnaf) " + "GROUP BY aff.malnaf)";
    }

    private String sqlFindEntrepriseMentionneesSansPerso(String annee) {
        Integer iAnnee = Integer.valueOf(annee);
        return "SELECT MALNAF AS no_affilie, MADESC AS raison_social, MADDEB AS date_debut_affiliation, "
                + "MADFIN AS date_fin_affiliation, motif.PCOLUT AS motif_radiation, mfddeb AS date_debut_particularite, mfdfin AS date_fin_particularite "
                + "from schema.afaffip aff "
                + "inner join schema.AFPARTP part on aff.maiaff=part.maiaff and part.MFTPAR=818011 "
                + "left join schema.fwcoup motif on motif.pcosid = aff.MATMOT and plaide='F' where aff.maddeb <= "
                + iAnnee.toString() + "1231 and (aff.madfin=0 or aff.madfin>" + iAnnee.toString()
                + "0101) and ((part.mfdfin>" + iAnnee.toString() + "0101 or part.mfdfin=0) and part.mfddeb <= "
                + iAnnee.toString() + "1231)" + "order by malnaf";
    }

    private String sqlFindNbreEmployeursParConvEtLangue(String annee) {
        return "select TITIERP3.HTLDE1 as convention, count(tradF.PCOSLI) as nb_francais, count(tradD.PCOSLI) as nb_allemand from SCHEMA.afaffip aff "
                + "inner join SCHEMA.titierp tiers on aff.htitie=tiers.htitie "
                + "LEFT OUTER JOIN SCHEMA.TIADMIP TIADMIP1 ON aff.MACONV = TIADMIP1.HTITIE "
                + "LEFT OUTER JOIN SCHEMA.TITIERP TITIERP3 ON TIADMIP1.HTITIE = TITIERP3.HTITIE "
                + "LEFT OUTER JOIN SCHEMA.FWCOSP tradD on tiers.HTTLAN = tradD.PCOSID and tradD.PCOSID = 503002 "
                + "LEFT OUTER JOIN SCHEMA.FWCOSP tradF on tiers.HTTLAN = tradF.PCOSID and tradF.PCOSID = 503001 "
                + "where "
                + "aff.maddeb<"
                + annee
                + "0000 and (aff.madfin=0 or aff.madfin>"
                + annee
                + "0101) and aff.MATTAF in (804002, 804005) GROUP BY TITIERP3.HTLDE1";
    }

    private String sqlFindAffiliesRadies(String annee) {
        return "SELECT MALNAF AS no_affilie, MADESC AS raison_social, MADDEB AS date_debut_affiliation, "
                + "MADFIN AS date_fin_affiliation, motif.PCOLUT AS motif_radiation FROM SCHEMA.AFAFFIP aff "
                + "inner join schema.PT_EMPLOYEURS emp on emp.ID_AFAFFIP = aff.MAIAFF "
                + "left join schema.fwcoup motif on motif.pcosid = aff.MATMOT and plaide='F' "
                + "where aff.madfin BETWEEN " + annee + "0101 AND " + annee + "1231";
    }
}
