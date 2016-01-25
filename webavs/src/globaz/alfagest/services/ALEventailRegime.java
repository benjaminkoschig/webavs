package globaz.alfagest.services;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.summary.ITISummarizable;
import globaz.pyxis.summary.TISummaryInfo;

public class ALEventailRegime extends BEntity implements ITISummarizable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee;
    private String dateDebut = "";
    private String dateFin = "";
    private String eetat;
    private String element = "";
    private String etat;
    private String icon = "";
    private PY_VG_MODULE_SIZE moduleSize = PY_VG_MODULE_SIZE.SMALL;
    private String mois;
    private String montant;
    private String numero;
    private String style = "";
    private String title = "";
    private String type;

    private String urlTitle = "";

    @Override
    protected String _getTableName() {
        return "";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        etat = statement.dbReadString("ETAT");
        type = statement.dbReadString("TYPE");
        numero = statement.dbReadString("NO");
        mois = statement.dbReadString("MOIS");
        annee = statement.dbReadString("ANNEE");
        montant = statement.dbReadString("MONTANT");
        dateDebut = statement.dbReadDateAMJ("edval");
        dateFin = statement.dbReadDateAMJ("efval");
        eetat = statement.dbReadString("eetat");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

    }

    private String formatSummary(ALEventailRegime crt, BSession userSession) {
        StringBuffer content = new StringBuffer();

        content.append("<table style='border: solid 0px silver;width:100%;'>");
        content.append("<tr><td><i>Etat : </i>");
        content.append(crt.getEtat());
        if ("F".equals(crt.getEetat()) && !JadeStringUtil.isEmpty(crt.getDateFin())) {
            content.append(userSession.getLabel("VG_DATE_LE"));
            content.append(crt.getDateFin());
        } else if ("R".equals(crt.getEetat()) && !JadeStringUtil.isEmpty(crt.getDateFin())) {
            content.append(userSession.getLabel("VG_DATE_AU"));
            content.append(crt.getDateFin());
        } else if (!JadeStringUtil.isEmpty(crt.getDateDebut())) {
            content.append(userSession.getLabel("VG_DATE_DES_LE"));
            content.append(crt.getDateDebut());
        }
        content.append("</td></tr><tr><td><i>" + userSession.getLabel("VG_ALFAGEST_DOSSIER_NO") + " : </i>");
        content.append(crt.getNumero());
        content.append("</td></tr><tr><td colspan=\"2\" align=\"left\"><i>"
                + userSession.getLabel("VG_ALFAGEST_TYPE_ALLOCATAIRE") + " : </i>");
        content.append(crt.getType());
        if (!JadeStringUtil.isEmpty(crt.getMontant())) {
            content.append("</td></tr><tr><td colspan=\"2\" align=\"center\"><b><i>"
                    + userSession.getLabel("VG_DERNIER_MONTANT_MENSUEL_PERIODE")
                    + "</i></b></td></tr><tr><td><i>CHF </i>");
            content.append(crt.getMontant());
            content.append("</td><td><i>" + userSession.getLabel("VG_ALFAGEST_PERIODE") + " : </i>");
            content.append(crt.getMois());
            content.append(".");
            content.append(crt.getAnnee());
        }
        content.append("</td></tr>");
        content.append("</table>");
        return content.toString();
    }

    public String getAnnee() {
        return annee;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getEetat() {
        return eetat;
    }

    @Override
    public String getElement() {
        return element;
    }

    public String getEtat() {
        return etat;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public TISummaryInfo[] getInfoForTiers(String idTiers, BSession userSession) throws Exception {
        setTitle(userSession.getLabel("VG_ALFAGEST_TITLE"));
        setUrlTitle("");

        TISummaryInfo sum = new TISummaryInfo();

        ALEventailRegimeManager search = new ALEventailRegimeManager();
        search.setSession(userSession);
        search.setForIdTiers(idTiers);
        search.find();
        if (search.size() > 0) {
            ALEventailRegime crt = (ALEventailRegime) search.getFirstEntity();
            // traitement cas radié et un autre dossier actif (bz 2609
            if ("R".equals(crt.getEetat()) && (search.size() > 1)) {
                ALEventailRegime prestJusteAvant = (ALEventailRegime) search.getEntity(1);
                // si ce n'est pas le même dossier uniquement
                if (!prestJusteAvant.getNumero().equals(crt.getNumero())) {
                    JACalendar cal = new JACalendarGregorian();
                    StringBuffer datePrestation = new StringBuffer(prestJusteAvant.getAnnee());
                    datePrestation.append(JadeStringUtil.fillWithZeroes(prestJusteAvant.getMois(), 2));
                    datePrestation.append("01");
                    JADate dateDocument = new JADate(datePrestation.toString());
                    JADate dateFinCrt = new JADate(JACalendar.format(crt.getDateFin(), JACalendar.FORMAT_YYYYMMDD));
                    if (cal.compare(dateDocument, dateFinCrt) == JACalendar.COMPARE_FIRSTUPPER) {
                        // la date de fin est plus petite que la dernière prestation d'un autre dossier
                        // ainsi c'est la prestation précédente qui est affichée
                        crt = prestJusteAvant;
                    }
                }
            }
            sum.setText(formatSummary(crt, userSession));
        } else {
            sum.setText("Aucun dossier trouvé");
        }
        return new TISummaryInfo[] { sum };
    }

    @Override
    public int getMaxHorizontalItems() {
        return 0;
    }

    @Override
    public PY_VG_MODULE_SIZE getModuleSize() {
        return moduleSize;
    }

    public String getMois() {
        return mois;
    }

    public String getMontant() {
        return montant;
    }

    public String getNumero() {
        return numero;
    }

    @Override
    public String getStyle() {
        return style;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    @Override
    public String getUrlTitle() {
        return urlTitle;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setEetat(String eetat) {
        this.eetat = eetat;
    }

    @Override
    public void setElement(String element) {
        this.element = element;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public void setModuleSize(PY_VG_MODULE_SIZE moduleSize) {
        this.moduleSize = moduleSize;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void setUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }

}
