package ch.globaz.vulpecula.documents.listesinternes;

import static ch.globaz.vulpecula.documents.DocumentConstants.*;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.util.List;
import ch.globaz.queryexec.bridge.jade.SCM;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.AnneeComptable;
import ch.globaz.vulpecula.external.BProcessWithContext;

public class RecapitulatifParGenreCaisseProcess extends BProcessWithContext {
    private static final long serialVersionUID = 4153561801931950346L;

    private String annee;
    private boolean omitTO = false;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        Annee annee = new Annee(this.annee);
        AnneeComptable anneeComptable = new AnneeComptable(annee);
        TaxationOfficeContainerService toContainerService = new TaxationOfficeContainerService();
        TaxationOfficeContainer toContainer = toContainerService.getTOContainer(annee, omitTO);
        List<RecapitulatifParGenreCaisseDTO> recaps = retrieve(anneeComptable);
        List<RecapitulatifParGenreCaisseDTO> recapsAVS = retrieveAVSFromCG(anneeComptable);
        RecapitulatifParGenreCaisse recapitulatifParGenreCaisse = new RecapitulatifParGenreCaisse(annee, recaps,
                recapsAVS, toContainer);
        String docName = LISTES_INTERNES_RECAP_GENRE_CAISSE_DOC_NAME + "_" + this.annee + "_"
                + (omitTO ? ListesInternesProcess.SANS_TO : ListesInternesProcess.AVEC_TO);
        RecapitulatifParGenreCaisseExcel excelByGenreCaisse = new RecapitulatifParGenreCaisseExcel(getSession(),
                docName, DocumentConstants.LISTES_INTERNES_RECAP_GENRE_CAISSE + " " + this.annee + " ("
                        + (omitTO ? ListesInternesProcess.SANS_TO_TXT : ListesInternesProcess.AVEC_TO_TXT) + ")",
                annee, recapitulatifParGenreCaisse);
        excelByGenreCaisse.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), excelByGenreCaisse.getOutputFile());
        return true;
    }

    @Override
    protected String getEMailObject() {
        return "Listes internes";
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    private List<RecapitulatifParGenreCaisseDTO> retrieve(AnneeComptable annee) {
        String query = "select typ.PCOLUT AS TYPE_LIBELLE, trad.LIBELLE AS COTISATION, f.MBTTYP AS TYPE, f.MBTGEN AS GENRE, c.IDEXTERNE AS ID_EXTERNE, c.IDSECTEUR AS ID_SECTEUR, sum(b.Masse) AS MASSE, sum(b.MONTANT) AS MONTANT from schema.casectp a "
                + "inner join schema.caoperp b on a.IDSECTION=b.IDSECTION "
                + "inner join schema.CARUBRP c on b.IDCOMPTE=c.IDRUBRIQUE "
                + "inner join schema.TIADMIP d on a.IDCAISSEPRO=d.HTITIE "
                + "inner join schema.CACPTAP e on a.IDCOMPTEANNEXE=e.IDCOMPTEANNEXE "
                + "left join schema.afassup f on f.MBIRUB=c.IDRUBRIQUE "
                + "inner join schema.CAJOURP g on g.IDJOURNAL=b.IDJOURNAL "
                + "left join schema.fwcoup gen on f.mbtgen=gen.pcosid and gen.plaide ='F' "
                + "left join schema.fwcoup typ on f.MBTTYP=typ.pcosid and typ.plaide ='F' "
                + "left join schema.PMTRADP trad on c.IDTRADUCTION=trad.IDTRADUCTION and trad.CODEISOLANGUE ='FR' "
                + "where g.DATEVALEURCG between "
                + annee.getDateDebut().getValue()
                + " and "
                + annee.getDateFin().getValue()
                + " and g.ETAT = 202002 "
                + " and (mbttyp is not null or c.IDEXTERNE in ('2110.4050.0000','2110.4070.0000','2110.4300.0000','2160.4050.0000','2160.4090.0000','2170.3055.0000','2170.4670.0000')) and ((f.mbttyp =68904009 and c.IDEXTERNE in ('7710.4030.0100','7720.4030.0100')) or (f.mbttyp not in (812019,68904009) or f.mbttyp is null)) and c.IDSECTEUR not in (7900,9000) "
                + "group by typ.PCOLUT, trad.LIBELLE, f.MBTTYP,f.mbtgen, gen.PCOLUT, c.IDEXTERNE, c.IDSECTEUR "
                + "order by f.MBTTYP, f.MBTGEN";
        return SCM.newInstance(RecapitulatifParGenreCaisseDTO.class).query(query).execute();
    }

    private List<RecapitulatifParGenreCaisseDTO> retrieveAVSFromCG(AnneeComptable anneeComptable) {
        String query = "select idexterne as ID_EXTERNE, sum(montant) AS MONTANT "
                + "from schema.cgplanp pl "
                + "inner join schema.cgsoldp so on (pl.idcompte=so.idcompte and pl.idexercomptable=so.idexercomptable) "
                + "inner join schema.cgjourp jo on (so.idexercomptable=jo.idexercomptable and so.idperiodecomptable=jo.idperiodecomptable) "
                + "inner join schema.cgecrip ec on (pl.idcompte=ec.idcompte and pl.idexercomptable=so.idexercomptable and ec.idjournal=jo.idjournal) "
                + "where jo.DATEVALEUR between " + anneeComptable.getDateDebut().getValue() + " and "
                + anneeComptable.getDateFin().getValue() + " and pl.idexterne like '%.4050.0000%' "
                + "and ec.estactive = '1' " + "group by idexterne ";
        return SCM.newInstance(RecapitulatifParGenreCaisseDTO.class).query(query).execute();

    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public boolean getOmitTO() {
        return omitTO;
    }

    public boolean isOmitTO() {
        return omitTO;
    }

    public void setOmitTO(boolean omitTO) {
        this.omitTO = omitTO;
    }
}
