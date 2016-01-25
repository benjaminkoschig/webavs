package globaz.ij.vb.acor;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.ij.api.prononces.IIJMesure;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJIJCalculeeManager;
import globaz.ij.db.prestations.IJIndemniteJournaliere;
import globaz.ij.db.prestations.IJIndemniteJournaliereManager;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.helpers.acor.IJRevision;
import globaz.ij.helpers.acor.IJRevisions;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.math.BigDecimal;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJCalculACORDecompteViewBean extends IJAbstractCalculACORViewBean {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private transient IJBaseIndemnisation baseIndemnisation;
    private String dateDebutBaseIndemnisation = "";
    private String dateFinBaseIndemnisation = "";

    // infos sur le prononce
    private String datePrononce = "";

    // infos sur les bases de calcul
    private String idBaseIndemnisation = "";
    private int idIJCalculeeCourante;
    // infos relatives a la navigation entre plusieurs IJ calculees, s'il y en a
    // plusieurs
    private List idsIJCalculees;

    private transient IJIJCalculee ijCalculee;
    private boolean isFileContent = false;
    // private boolean garantieR3;
    private String noRevisionGaranti = null;

    // divers
    private transient IJPrononce prononce;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut date debut base indemnisation
     * 
     * @return la valeur courante de l'attribut date debut base indemnisation
     */
    public String getDateDebutBaseIndemnisation() {
        return dateDebutBaseIndemnisation;
    }

    /**
     * getter pour l'attribut date fin base indemnisation
     * 
     * @return la valeur courante de l'attribut date fin base indemnisation
     */
    public String getDateFinBaseIndemnisation() {
        return dateFinBaseIndemnisation;
    }

    /**
     * getter pour l'attribut date prononce
     * 
     * @return la valeur courante de l'attribut date prononce
     */
    public String getDatePrononce() {
        return datePrononce;
    }

    /**
     * 
     * 
     * @return le No de révision à garantir. retourne une chaine vide, si rien à garantir.
     */
    public String getDefaultNoRevision() {

        try {
            String idPrononce = loadPrononce().getIdPrononce();
            IJIJCalculeeManager mgr = new IJIJCalculeeManager();
            mgr.setSession(getSession());
            mgr.setForIdPrononce(idPrononce);
            mgr.find();
            JACalendar cal = new JACalendarGregorian();

            JADate ddBI = new JADate(getDateDebutBaseIndemnisation());

            String noRevisionAGarantir = "";
            BigDecimal montantMax = new BigDecimal("0");
            for (int i = 0; i < mgr.size(); i++) {
                IJIJCalculee ijc = (IJIJCalculee) mgr.getEntity(i);

                JADate ddIJC = new JADate(ijc.getDateDebutDroit());
                JADate dfIJC = null;
                if (!JadeStringUtil.isBlankOrZero(ijc.getDateFinDroit())) {
                    dfIJC = new JADate(ijc.getDateFinDroit());
                }

                if ((dfIJC == null || cal.compare(ddBI, dfIJC) == JACalendar.COMPARE_FIRSTLOWER || cal.compare(ddBI,
                        dfIJC) == JACalendar.COMPARE_EQUALS)

                        &&

                        (cal.compare(ddBI, ddIJC) == JACalendar.COMPARE_FIRSTUPPER || cal.compare(ddBI, ddIJC) == JACalendar.COMPARE_EQUALS)) {

                    // On récupère les montants int + ext.
                    IJIndemniteJournaliereManager ijMgr = new IJIndemniteJournaliereManager();
                    ijMgr.setSession(getSession());
                    ijMgr.setForIdIJCalculee(ijc.getIdIJCalculee());
                    ijMgr.find();

                    String csTypeHebergement = loadPrononce().getCsTypeHebergement();
                    BigDecimal montantCourant = new BigDecimal("0");
                    for (int j = 0; j < ijMgr.size(); j++) {
                        IJIndemniteJournaliere ij = (IJIndemniteJournaliere) ijMgr.getEntity(j);

                        if (IIJPrononce.CS_INTERNE.equals(csTypeHebergement)
                                && IIJMesure.CS_INTERNE.equals(ij.getCsTypeIndemnisation())) {
                            montantCourant = montantCourant.add(new BigDecimal(ij.getMontantJournalierIndemnite()));
                        } else if (IIJPrononce.CS_EXTERNE.equals(csTypeHebergement)
                                && IIJMesure.CS_EXTERNE.equals(ij.getCsTypeIndemnisation())) {
                            montantCourant = montantCourant.add(new BigDecimal(ij.getMontantJournalierIndemnite()));
                        } else {
                            montantCourant = montantCourant.add(new BigDecimal(ij.getMontantJournalierIndemnite()));
                        }
                    }

                    if (montantMax.compareTo(montantCourant) == -1) {

                        montantMax = new BigDecimal(montantCourant.toString());
                        noRevisionAGarantir = ijc.getNoRevision();
                    }
                }
            }

            return noRevisionAGarantir;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les détails
     * 
     * @return le détail du requérant formaté
     * @throws Exception
     */
    public String getDetailRequerantDetail() throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), getNoAVSAssure());

        if (tiers != null) {

            String nationalite = "";

            if (!"999".equals(getSession()
                    .getCode(
                            getSession().getSystemCode("CIPAYORI",
                                    tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))))) {
                nationalite = getSession().getCodeLibelle(
                        getSession().getSystemCode("CIPAYORI",
                                tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            }

            return PRNSSUtil.formatDetailRequerantDetail(
                    getNoAVSAssure(),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }
    }

    /**
     * getter pour l'attribut id base indemnisation
     * 
     * @return la valeur courante de l'attribut id base indemnisation
     */
    public String getIdBaseIndemnisation() {
        return idBaseIndemnisation;
    }

    /**
     * getter pour l'attribut id IJCalculee courante
     * 
     * @return la valeur courante de l'attribut id IJCalculee courante
     */
    public int getIdIJCalculeeCourante() {
        return idIJCalculeeCourante;
    }

    /**
     * getter pour l'attribut id IJCalculees
     * 
     * @return la valeur courante de l'attribut id IJCalculees
     */
    public List getIdsIJCalculees() {
        return idsIJCalculees;
    }

    public String getNoRevisionGaranti() {
        return noRevisionGaranti;
    }

    /**
     * retourne vrai si, pour la base d'indemnisation dont on veut calculer le decompte, il y a plusieurs ij calculees
     * et que l'ij calculee courante est après la premiere de la liste.
     * 
     * @return DOCUMENT ME!
     */
    public boolean hasIJCalculeePrecedante() {
        return (idsIJCalculees != null) && (idIJCalculeeCourante > 0);
    }

    // /**
    // * getter pour l'attribut mutation r3
    // *
    // * @return la valeur courante de l'attribut mutation r3
    // */
    // public boolean isMutationR3() {
    // return
    // String[].class.isInstance(idsIJCalculees.get(idIJCalculeeCourante));
    // }

    /**
     * retourne vrai si, pour la base d'indemnisation dont on veut calculer le decompte, il y a plusieurs ij calculees
     * et que l'ij calculee courante est avant la derniere de la liste.
     * 
     * @return DOCUMENT ME!
     */
    public boolean hasIJCalculeeSuivante() {
        return (idsIJCalculees != null) && (idIJCalculeeCourante < (idsIJCalculees.size() - 1));
    }

    /** passe a l'ij calculee precedante */
    public void ijCalculeePrecedante() {
        if (hasIJCalculeePrecedante()) {
            idIJCalculeeCourante--;
        }
    }

    /** passe a l'ij calculee precedante */
    public void ijCalculeeSuivante() {
        if (hasIJCalculeeSuivante()) {
            idIJCalculeeCourante++;
        }
    }

    public boolean isFileContent() {
        return isFileContent;
    }

    /**
     * charge la base d'indemnisation pour l'identifiant choisi dans cette instance.
     * 
     * @return une base d'indemnisation ou null si idBaseIndemnisation est null.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJBaseIndemnisation loadBaseIndemnisation() throws Exception {
        if ((baseIndemnisation == null) && !JadeStringUtil.isIntegerEmpty(idBaseIndemnisation)) {
            baseIndemnisation = new IJBaseIndemnisation();
            baseIndemnisation.setISession(getISession());
            baseIndemnisation.setIdBaseIndemisation(idBaseIndemnisation);
            baseIndemnisation.retrieve();
        }

        return baseIndemnisation;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */

    public IJIJCalculee loadIJCalculee() throws Exception {
        String idIJ = null;

        // Au maximum 2 IJCalcule pour une même date donnée, sinon le traitement
        // ne fonctionne pas.
        // Ex. [ 4ème rév. ][ 5ème rév. ]
        // [ 4ème rév. ]

        IJRevisions revisions = (IJRevisions) idsIJCalculees.get(idIJCalculeeCourante);

        // Changement de révision. Laquelle garantir ???
        if (revisions.getRevisions().length > 1) {
            for (int i = 0; i < revisions.getRevisions().length; i++) {
                IJRevision rev = revisions.getRevisions()[i];

                if (getNoRevisionGaranti() != null && getNoRevisionGaranti().equals(rev.getNoRevision())) {
                    idIJ = rev.getIdIJCalculee();
                    break;
                }
            }
        } else {
            IJRevision rev = revisions.getRevisions()[0];
            idIJ = rev.getIdIJCalculee();
        }

        // Si aucune ijcalculee trouvée,
        // On prend l'ij calculee avec le plus grand no de révision.
        if (JadeStringUtil.isBlankOrZero(idIJ)) {
            int maxRev = -1;

            int currentRevId = 0;
            for (int i = 0; i < revisions.getRevisions().length; i++) {
                IJRevision rev = revisions.getRevisions()[i];

                if (JadeStringUtil.isBlankOrZero(rev.getNoRevision()) && maxRev <= 0) {
                    maxRev = 0;
                    currentRevId = i;
                } else {
                    int currentRev = Integer.parseInt(rev.getNoRevision());
                    if (maxRev < currentRev) {
                        maxRev = currentRev;
                        currentRevId = i;
                    }
                }
            }
            idIJ = revisions.getRevisions()[currentRevId].getIdIJCalculee();
        }

        if ((ijCalculee == null) || !ijCalculee.getIdIJCalculee().equals(idIJ)) {
            ijCalculee = IJIJCalculee.loadIJCalculee(getSession(), idIJ, loadBaseIndemnisation().getCsTypeIJ());
        }
        return ijCalculee;
    }

    /**
     * charge une instance correcte d'une sous-classe de IJPrononce pour la base d'indemnisation courante.
     * 
     * @return une instance de grande ou petite ij ou null si idBaseIndemnisation est null.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJPrononce loadPrononce() throws Exception {
        if (prononce == null) {
            prononce = IJPrononce.loadPrononce(getSession(), null, loadBaseIndemnisation().getIdPrononce(),
                    loadBaseIndemnisation().getCsTypeIJ());
        }

        return prononce;
    }

    public IJRevisions loadRevisions() {
        IJRevisions revisions = (IJRevisions) idsIJCalculees.get(idIJCalculeeCourante);
        return revisions;
    }

    /**
     * setter pour l'attribut date debut base indemnisation
     * 
     * @param dateDebutBaseIndemnisation
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutBaseIndemnisation(String dateDebutBaseIndemnisation) {
        this.dateDebutBaseIndemnisation = dateDebutBaseIndemnisation;
    }

    /**
     * setter pour l'attribut date fin base indemnisation
     * 
     * @param dateFinBaseIndemnisation
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFinBaseIndemnisation(String dateFinBaseIndemnisation) {
        this.dateFinBaseIndemnisation = dateFinBaseIndemnisation;
    }

    /**
     * setter pour l'attribut date prononce
     * 
     * @param datePrononce
     *            une nouvelle valeur pour cet attribut
     */
    public void setDatePrononce(String datePrononce) {
        this.datePrononce = datePrononce;
    }

    /**
     * setter pour l'attribut id base indemnisation
     * 
     * @param idBaseIndemnisation
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdBaseIndemnisation(String idBaseIndemnisation) {
        this.idBaseIndemnisation = idBaseIndemnisation;
    }

    /**
     * setter pour l'attribut id IJCalculee courante
     * 
     * @param idIJCalculeeCourante
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdIJCalculeeCourante(int idIJCalculeeCourante) {
        this.idIJCalculeeCourante = idIJCalculeeCourante;
    }

    /**
     * setter pour l'attribut id IJCalculees
     * 
     * @param idIJCalculees
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdsIJCalculees(List idIJCalculees) {
        idsIJCalculees = idIJCalculees;
    }

    public void setIsFileContent(boolean elm) {
        isFileContent = elm;
    }

    public void setNoRevisionGaranti(String noRevisionGaranti) {
        this.noRevisionGaranti = noRevisionGaranti;
    }
}
