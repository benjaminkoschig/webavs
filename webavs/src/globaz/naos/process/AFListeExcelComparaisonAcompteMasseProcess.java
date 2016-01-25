package globaz.naos.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.naos.listes.excel.AFXmlmlComparaisonAcompteMasse;
import globaz.naos.listes.excel.util.AFExcelmlUtils;
import globaz.naos.listes.excel.util.NaosContainer;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.util.TISQL;
import globaz.pyxis.util.TIToolBox;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class AFListeExcelComparaisonAcompteMasseProcess extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String MODEL_NAME = "listeAcompteMasseFacturee.xml";
    public final static String NUMERO_INFOROM = "0291CAF";

    private java.lang.String anneeAcompte = new String();
    private java.lang.String anneeMasse = new String();
    private java.lang.String differenceTolereeFranc = new String();
    private java.lang.String differenceTolereeTaux = new String();

    // Constructeur
    public AFListeExcelComparaisonAcompteMasseProcess() {
        super();
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception, Exception {

        BigDecimal diffTaux = new BigDecimal(getDifferenceTolereeTaux());
        diffTaux = diffTaux.divide(new BigDecimal(100)).setScale(3, BigDecimal.ROUND_HALF_EVEN);

        String schema = TIToolBox.getCollection(getSession());

        List<Map<String, String>> manager = TISQL
                .query(getSession(),
                        "aff.htitie IDTIERS, malnaf NUMAFFILIE, madesc NOM, ru.idexterne RUBRIQUE,memmap MONTANTACOMPTE, "
                                + "cumulmasse MASSEFACTUREE, memmap-cumulmasse as DIFFERENCE, (memmap-cumulmasse)/memmap as TAUX",
                        " from "
                                + schema
                                + "afaffip aff "
                                + "inner join "
                                + schema
                                + "afplafp pla on (aff.maiaff=pla.maiaff) "
                                + "inner join "
                                + schema
                                + "afcotip cot on (pla.muipla=cot.muipla)	"
                                + "inner join "
                                + schema
                                + "afassup ass on (cot.mbiass=ass.mbiass) "
                                + "inner join "
                                + schema
                                + "carubrp ru on (ass.mbirub=ru.idrubrique) "
                                + "inner join "
                                + schema
                                + "cacptap ca on (aff.htitie=ca.idtiers and aff.malnaf=ca.idexternerole and ca.idrole in (517002, 517039)) "
                                + "left join "
                                + schema
                                + "cacptrp cr on (ca.idcompteannexe=cr.idcompteannexe and ass.mbirub=cr.idrubrique and cr.annee="
                                + getAnneeMasse()
                                + ") "
                                + "where maddeb < "
                                + Integer.parseInt(getAnneeAcompte())
                                + 1
                                + "0000  and (madfin = 0 or madfin > "
                                + getAnneeAcompte()
                                + "0000) "
                                + "and meddeb < "
                                + Integer.parseInt(getAnneeAcompte())
                                + 1
                                + "0000 and (medfin = 0 or medfin > "
                                + getAnneeAcompte()
                                + "0000) and memmap > 0 "
                                + "and mbtgen = 801001 and mbbsda = 1	and mbttyp in (812001, 812002) and abs(memmap-cumulmasse) > "
                                + getDifferenceTolereeFranc() + " and abs(round((memmap-cumulmasse)/memmap,3)) > "
                                + diffTaux.toString() + " and matcfa = 0 and mabrep = 2 order by malnaf");

        if (manager.size() >= 1) {
            setProgressScaleValue(manager.size());
            return createDocument(manager);

        } else {
            setSendCompletionMail(false);
        }

        return false;

    }

    // Méthode
    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);

        // if (JadeStringUtil.isEmpty(this.getFromDateRadiation())
        // || JadeStringUtil.isEmpty(this.getToDateRadiation())
        // || (new JADate(this.getFromDateRadiation()).getYear() != new JADate(this.getToDateRadiation())
        // .getYear())) {
        // this.getSession().addError(this.getSession().getLabel("LISTE_EMPLOYEUR_RADIE_ERREUR_DATE_RADIATION"));
        // }

        /**
         * sécurité supplémentaire ------------------------ mais on ne devrait pas se retrouver avec une adresse email
         * vide en effet, si l'email n'est pas renseigné getEMailAddress() prend l'email du parent ou à défaut, celui du
         * user connecté
         * 
         */
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            getSession().addError(getSession().getLabel("LISTE_EMPLOYEUR_RADIE_ERREUR_EMAIL_OBLIGATOIRE"));
            setSendCompletionMail(false);
            setSendMailOnError(false);
        }
    }

    private boolean createDocument(List<Map<String, String>> manager) throws Exception, Exception {
        NaosContainer container = AFXmlmlComparaisonAcompteMasse.loadResults(manager, this);
        String nomDoc = (getSession().getLabel("LISTE_COMPARAISON_ACOMPTEMASSE"));

        if (isAborted()) {
            return false;
        }

        String docPath = "";
        if (CodeSystem.LANGUE_ALLEMAND.equals(getSession().getIdLangue())) {
            docPath = AFExcelmlUtils.createDocumentExcel("DE" + "/"
                    + AFListeExcelComparaisonAcompteMasseProcess.MODEL_NAME, nomDoc, container);
        } else {
            docPath = AFExcelmlUtils.createDocumentExcel("FR" + "/"
                    + AFListeExcelComparaisonAcompteMasseProcess.MODEL_NAME, nomDoc, container);
        }
        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(AFListeExcelComparaisonAcompteMasseProcess.NUMERO_INFOROM);
        this.registerAttachedDocument(docInfo, docPath);

        return true;
    }

    public java.lang.String getAnneeAcompte() {
        return anneeAcompte;
    }

    public java.lang.String getAnneeMasse() {
        return anneeMasse;
    }

    public java.lang.String getDifferenceTolereeFranc() {
        return differenceTolereeFranc;
    }

    public java.lang.String getDifferenceTolereeTaux() {
        return differenceTolereeTaux;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("NAOS_LISTE_COMPARAISONACOMPTE_ECHEC");
        } else {
            return getSession().getLabel("NAOS_LISTE_COMPARAISONACOMPTE_OK");
        }
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setAnneeAcompte(java.lang.String anneeAcompte) {
        this.anneeAcompte = anneeAcompte;
    }

    public void setAnneeMasse(java.lang.String anneeMasse) {
        this.anneeMasse = anneeMasse;
    }

    public void setDifferenceTolereeFranc(java.lang.String differenceTolereeFranc) {
        this.differenceTolereeFranc = differenceTolereeFranc;
    }

    public void setDifferenceTolereeTaux(java.lang.String differenceTolereeTaux) {
        this.differenceTolereeTaux = differenceTolereeTaux;
    }

}
