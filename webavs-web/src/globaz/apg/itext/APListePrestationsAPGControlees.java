/*
 * Créé le 3 juin 05
 */
package globaz.apg.itext;

import globaz.apg.db.prestation.APPrestationsControlees;
import globaz.apg.db.prestation.APPrestationsControleesManager;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRStringUtils;
import java.math.BigDecimal;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APListePrestationsAPGControlees extends FWIAbstractManagerDocumentList {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String derniereIdPrestation = null;
    private String forEtat = "";

    private String orderBy = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APListePrestationsAPGControlees.
     */
    public APListePrestationsAPGControlees() {
        // session, prefix, Compagnie, Titre, manager, application
        super(null, "PRESTATIONS", "GLOBAZ", "Liste des prestations APG contrôlées",
                new APPrestationsControleesManager(), "APG");
    }

    /**
     * Crée une nouvelle instance de la classe APListePrestationsAPGControlees.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public APListePrestationsAPGControlees(BSession session) {
        // session, prefix, Compagnie, Titre, manager, application
        super(session, "PRESTATIONS", "GLOBAZ", session.getLabel("LISTE_PRESTATIONS_CONTROLEES"),
                new APPrestationsControleesManager(), "APG");
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * transfère des paramètres au manager;
     */

    /**
     */
    @Override
    public void _beforeExecuteReport() {

        // on ajoute au doc info le numéro de référence inforom
        getDocumentInfo().setDocumentTypeNumber(IPRConstantesExternes.LISTE_PRESTATIONS_CONTROLEES_APG);

        APPrestationsControleesManager manager = (APPrestationsControleesManager) _getManager();
        manager.setSession(getSession());
        manager.setForEtat(getForEtat());
        manager.setOrderBy(getOrderBy());

        try {
            if (manager.getCount(getTransaction()) == 0) {
                addRow(new APPrestationsControlees());
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), "", "");
        }

        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
    }

    /*
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getSession().getLabel("EMAIL_VIDE"));
        } else {
            if (getEMailAddress().indexOf('@') == -1) {
                this._addError(getSession().getLabel("EMAIL_INVALIDE"));
            }
        }

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    /*
     * Contenu des cellules
     */
    /**
     * @param entity
     *            DOCUMENT ME!
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    @Override
    protected void addRow(BEntity entity) throws FWIException {
        // valeurs
        APPrestationsControlees prestationsControlees = (APPrestationsControlees) entity;

        if (!prestationsControlees.getIdPrestation().equals(derniereIdPrestation)) {

            PRTiersWrapper tier = null;

            try {
                tier = PRTiersHelper.getTiers(getSession(), prestationsControlees.getNoAVS());
            } catch (Exception e) {
                getSession().addError(getSession().getLabel("ERROR_TIERS_INTROUVABLE_PAR_NO_AVS"));
            }
            if (tier != null) {

                _addCell(prestationsControlees.getNoAVS() + " / "
                        + formatNom(prestationsControlees.getNom() + " " + prestationsControlees.getPrenom()) + " / "
                        + tier.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE) + " / "
                        + getLibelleCourtSexe(tier.getProperty(PRTiersWrapper.PROPERTY_SEXE)) + " / "
                        + getLibellePays(tier.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            } else {
                _addCell("");
            }

            _addCell(prestationsControlees.getDateDebut() + " - " + prestationsControlees.getDateFin());
            _addCell(prestationsControlees.getNbrJoursSoldes());
            derniereIdPrestation = prestationsControlees.getIdPrestation();
        } else {
            _addCell(" ");
            _addCell(" ");
            _addCell(" ");
        }

        // _addCell(JAUtil.isStringEmpty(prestationsControlees.getNoAff()) ?
        // prestationsControlees.getNoAVS()
        // : prestationsControlees.getNoAff());
        _addCell(" ");
        _addCell(prestationsControlees.getNoAff());
        _addCell(formatNom(prestationsControlees.getNomBeneficiaire()));

        if (JadeStringUtil.isIntegerEmpty(prestationsControlees.getIdRepartitionPaiementsParent())) {
            _addCell(prestationsControlees.getMontantBrut());
            _addCell(new BigDecimal(prestationsControlees.getMontant()).negate().toString());
            _addCell(prestationsControlees.getMontantNet());
        } else {
            // ventilation
            _addCell(" ");
            _addCell(" ");
            _addCell(prestationsControlees.getMontantVentile());
        }
    }

    // Formate le nom pour qu'il ne déborde pas de sa cellule
    private String formatNom(String nom) {
        // TODO c pas bien fait du tout, mais au moins ça marche
        if (nom.length() > 20) {
            // on coupe suivant les espaces
            String[] nomCoupe = PRStringUtils.split(nom, ' ');
            char modeCoupage = ' ';

            // si ça n'a pas suffit, on le coupe avec le -
            if (nomCoupe.length == 1) {
                nomCoupe = PRStringUtils.split(nom, '-');
                modeCoupage = '-';
            }

            int size = 0;
            nom = "";

            StringBuffer buffer = new StringBuffer();

            for (int i = 0; i < nomCoupe.length; i++) {
                size += nomCoupe[i].length();

                if (size > 20) {
                    if (i == 0) {
                        nom = nomCoupe[i].substring(0, 20) + "...";
                        buffer.delete(0, 60);

                        break;
                    } else {
                        buffer.append(modeCoupage);
                        buffer.append("\n");
                        size = nomCoupe[i].length();

                        if (size > 20) {
                            buffer.append(nomCoupe[i].substring(0, 20) + "...");
                            nom += buffer.toString();
                            buffer.delete(0, 60);

                            break;
                        }

                        nom += buffer.toString();
                        buffer.delete(0, 30);
                        buffer.append(nomCoupe[i]);
                    }
                } else {
                    if (i != 0) {
                        buffer.append(modeCoupage);
                    }

                    buffer.append(nomCoupe[i]);
                    size += 1;
                }
            }

            if (buffer.length() != 0) {
                nom += buffer.toString();
            }
        }

        return nom;
    }

    /*
     * Titre de l'email
     */
    /**
     * getter pour l'attribut EMail object
     * 
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("LISTE_PRESTATIONS_CONTROLEES");
    }

    /**
     * getter pour l'attribut for etat
     * 
     * @return la valeur courante de l'attribut for etat
     */
    public String getForEtat() {
        return forEtat;
    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est passé en paramètre
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public String getLibelleCourtSexe(String csSexe) {

        if (PRACORConst.CS_HOMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }

    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est passé en paramètre
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public String getLibellePays(String csNationalite) {

        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", csNationalite)))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", csNationalite));
        }

    }

    /**
     * getter pour l'attribut order by
     * 
     * @return la valeur courante de l'attribut order by
     */
    public String getOrderBy() {
        return orderBy;
    }

    /*
     * Initialisation des colonnes et des groupes
     */
    /**
     */
    @Override
    protected void initializeTable() {
        // colonnes
        this._addColumnLeft(getSession().getLabel("LISTE_PRESTATIONS_CONTROLEES_DETAIL_ASSURE"), 36);
        this._addColumnLeft(getSession().getLabel("LISTE_PRESTATIONS_CONTROLEES_PERIODE"), 12);
        this._addColumnRight(getSession().getLabel("LISTE_PRESTATIONS_CONTROLEES_JOURS_SOLDES"), 8);
        this._addColumnRight(" ", 1);
        this._addColumnLeft(getSession().getLabel("LISTE_PRESTATIONS_CONTROLEES_N_AFFILIE"), 8);
        this._addColumnLeft(getSession().getLabel("LISTE_PRESTATIONS_CONTROLEES_BENEFICIAIRE"), 16);
        this._addColumnRight(getSession().getLabel("LISTE_PRESTATIONS_CONTROLEES_MONTANT_BRUT"), 10);
        this._addColumnRight(getSession().getLabel("LISTE_PRESTATIONS_CONTROLEES_COTISATIONS"), 8);
        this._addColumnRight(getSession().getLabel("LISTE_PRESTATIONS_CONTROLEES_MONTANT_NET"), 10);
    }

    /**
     * Set la jobQueue
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * setter pour l'attribut for etat
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForEtat(String string) {
        forEtat = string;
    }

    /**
     * setter pour l'attribut order by
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setOrderBy(String string) {
        orderBy = string;
    }
}
