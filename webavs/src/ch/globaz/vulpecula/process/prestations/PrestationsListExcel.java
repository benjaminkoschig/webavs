package ch.globaz.vulpecula.process.prestations;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;

public abstract class PrestationsListExcel extends AbstractListExcel {

    protected String idPassageFacturation = "";
    protected Employeur employeur = new Employeur();
    protected Travailleur travailleur = new Travailleur();
    protected Convention convention = new Convention();
    protected String periodeDebut;
    protected String periodeFin;

    private static final String EMPTY_CRITERE = "-";

    public PrestationsListExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
    }

    /**
     * Retourne une chaîne de caractère comportant le titre du document Excel.
     * 
     * @return String représentant une liste Excel
     */
    public abstract String getListName();

    protected void createCriteres() {
        createCell(getListName());
        createRow(2);
        createCell(getSession().getLabel("LISTE_LOT"), getStyleCritereTitle());
        createCell(idPassageFacturation, getStyleCritere());
        createRow();
        createCell(getSession().getLabel("LISTE_EMPLOYEUR"), getStyleCritereTitle());
        if (employeur.getId() != null) {
            createCell(employeur.getAffilieNumero() + " " + employeur.getRaisonSociale(), getStyleCritere());
        } else {
            createCell(EMPTY_CRITERE);
        }
        createRow();
        createCell(getSession().getLabel("LISTE_TRAVAILLEUR"), getStyleCritereTitle());
        if (travailleur.getId() != null) {
            createCell(travailleur.getId() + " " + travailleur.getNomPrenomTravailleur(), getStyleCritere());
        } else {
            createCell(EMPTY_CRITERE);
        }
        createRow();
        createCell(getSession().getLabel("LISTE_CONVENTION"), getStyleCritereTitle());
        if (convention.getId() != null) {
            createCell(convention.getCode() + " " + convention.getDesignation(), getStyleCritere());
        } else {
            createCell(EMPTY_CRITERE);
        }
        createRow();
        createCell(getSession().getLabel("LISTE_PERIODE"), getStyleCritereTitle());
        if (!JadeStringUtil.isEmpty(periodeDebut)) {
            if (!JadeStringUtil.isEmpty(periodeFin)) {
                createCell(periodeDebut + " - " + periodeFin);
            } else {
                createCell(periodeDebut + " - ");
            }
        } else {
            createCell(EMPTY_CRITERE);
        }
    }

    public void setIdPassageFacturation(String idPassageFacturation) {
        this.idPassageFacturation = idPassageFacturation;
    }

    public void setTravailleur(Travailleur travailleur) {
        this.travailleur = travailleur;
    }

    public void setEmployeur(Employeur employeur) {
        this.employeur = employeur;
    }

    public void setConvention(Convention convention) {
        this.convention = convention;
    }

    public void setPeriodeDebut(String periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public void setPeriodeFin(String periodeFin) {
        this.periodeFin = periodeFin;
    }
}
