package ch.globaz.vulpecula.process.comptabilite;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.util.List;
import ch.globaz.queryexec.bridge.jade.SCM;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.external.BProcessWithContext;

public class ListeSoldesCPPAssociationProcess extends BProcessWithContext {
    private String orderBy;
    private String dateUntil;

    @Override
    protected String getEMailObject() {
        return "Listes soldes CPP et associations";
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();

        Date dateRef;
        if (dateUntil == null || !Date.isValid(dateUntil)) {
            dateRef = new Date();
        } else {
            dateRef = new Date(dateUntil);
        }

        ListeSoldesCPPAssociationExcel doc = new ListeSoldesCPPAssociationExcel(dateRef, retrieveDatas(dateRef));

        doc.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), doc.getOutputFile());

        return true;
    }

    private List<SoldesCPPAssociationDTO> retrieveDatas(Date dateRef) {

        String query = "SELECT concat(trim(ap.htlde1), concat(' ', concat(trim(ap.htlde2), concat(' ',trim(ap.htlde3))))) AS NOM_CPP_ASSOCIATION, "
                + "SCHEMA.CACPTAP.idexternerole AS ID_EXTERNE_ROLE, "
                + "SCHEMA.CACPTAP.description AS DESCRIPTION, "
                + "SCHEMA.CACPTAP.IDTIERS AS ID_TIERS, "
                + "s.idexterne AS NUM_SECTION, "
                + "sum (SCHEMA.CAOPERP.montant) as SOLDE "
                + "FROM SCHEMA.CACPTAP "
                + "INNER JOIN SCHEMA.CAOPERP ON SCHEMA.CACPTAP.IDCOMPTEANNEXE = SCHEMA.CAOPERP.IDCOMPTEANNEXE "
                + "inner join SCHEMA.casectp s on s.idsection = SCHEMA.caoperp.idsection "
                + "INNER JOIN SCHEMA.CAJOURP ON SCHEMA.CAJOURP.IDJOURNAL = SCHEMA.CAOPERP.IDJOURNAL "
                + "join SCHEMA.AFAFFIP empl on empl.malnaf = SCHEMA.cacptap.idexternerole "
                + "join SCHEMA.PT_ENTETE_FACTURE_AP fa on fa.id_pt_employeurs=empl.maiaff and fa.NUMERO_SECTION=s.idexterne "
                + "join SCHEMA.TITIERP ap on ap.HTITIE=fa.ID_ASSOCIATION "
                + "WHERE SCHEMA.CAOPERP.ETAT = 205002 AND (SCHEMA.CAOPERP.idtypeoperation like 'E%' OR SCHEMA.CAOPERP.idtypeoperation like 'A%') "
                + "AND SCHEMA.CAJOURP.DATEVALEURCG <= "
                + dateRef.getValue()
                + " AND SCHEMA.CACPTAP.IDROLE=68902003 "
                + "AND SCHEMA.CACPTAP.IDGENRECOMPTE = 0 "
                + "GROUP BY ap.htlde1, ap.htlde2, ap.htlde3,SCHEMA.CACPTAP.IDEXTERNEROLE, SCHEMA.CACPTAP.DESCRIPTION, SCHEMA.CACPTAP.DESCUPCASE, SCHEMA.CACPTAP.idrole, SCHEMA.CACPTAP.IDTIERS, s.idtypesection, s.idexterne "
                + "HAVING SUM (SCHEMA.CAOPERP.montant)<>0  ";

        if (orderBy == null || orderBy.isEmpty()) {
            orderBy = "IDEXTERNEROLE";
        }
        query += "ORDER BY " + orderBy;

        return SCM.newInstance(SoldesCPPAssociationDTO.class).query(query).execute();
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getDateUntil() {
        return dateUntil;
    }

    public void setDateUntil(String dateUntil) {
        this.dateUntil = dateUntil;
    }

}
