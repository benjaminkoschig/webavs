package globaz.helios.process.importecritures;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGJournal;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGPlanComptableManager;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CGImportEcrituresUtils {

    private static final int FIRST_ITEM = 0;
    private static final String TAG_CODEDEBITCREDIT = "codedebitcredit";
    private static final String TAG_COLLECTIVE = "collective";
    private static final String TAG_COURS = "cours";
    public static final String TAG_DATE = "date";
    public static final String TAG_DATE_VALEUR = "dateValeur";
    public static final String TAG_DATEENTETE = "dateentete";
    private static final String TAG_DOUBLE = "double";
    private static final String TAG_ECRITURE = "ecriture";
    private static final String TAG_ID = "id";
    private static final String TAG_IDCENTRECHARGECREDITE = "idcentrechargecredite";
    private static final String TAG_IDCENTRECHARGEDEBITE = "idcentrechargedebite";
    private static final String TAG_IDCOMPTE = "idcompte";
    private static final String TAG_IDCOMPTECREDITE = "idcomptecredite";
    private static final String TAG_IDCOMPTEDEBITE = "idcomptedebite";
    private static final String TAG_IDEXTERNECOMPTE = "idexternecompte";
    private static final String TAG_IDEXTERNECOMPTECREDITE = "idexternecomptecredite";
    private static final String TAG_IDEXTERNECOMPTEDEBITE = "idexternecomptedebite";
    private static final String TAG_LIBELLE = "libelle";
    public static final String TAG_LIBELLE_JOURNAL = "libelleJournal";
    private static final String TAG_LIBELLEENTETE = "libelleentete";

    private static final String TAG_MANDAT = "mandat";
    private static final String TAG_MONTANT = "montant";
    private static final String TAG_MONTANTETRANGER = "montantetranger";

    private static final String TAG_PIECE = "piece";
    private static final String TAG_PIECEENTETE = "pieceentete";
    private static final String TAG_REMARQUE = "remarque";
    private static final String TAG_VALUE = "value";

    private static final String TAG_VALUE_CREDIT = "credit";
    private static final String TAG_VALUE_DEBIT = "debit";

    private static final String TAG_VALUE_EXTOURNECREDIT = "extournecredit";

    private static final String TAG_VALUE_EXTOURNEDEBIT = "extournedebit";

    /**
     * Retourne le premier enfant correspondant au tag souhaité.
     * 
     * @param parent
     * @param tag
     * @return
     * @throws Exception
     */
    private static Element getChildElement(Element parent, String tag) throws Exception {
        return (Element) parent.getElementsByTagName(tag).item(CGImportEcrituresUtils.FIRST_ITEM);
    }

    /**
     * Retourne le code système du code débit/crédit.
     * 
     * @param session
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getCodeDebitCredit(BSession session, Element parent) throws Exception {
        String codeDebitCredit = CGImportEcrituresUtils.getValue(CGImportEcrituresUtils.getChildElement(parent,
                CGImportEcrituresUtils.TAG_CODEDEBITCREDIT));
        if (codeDebitCredit.equals(CGImportEcrituresUtils.TAG_VALUE_DEBIT)) {
            return CodeSystem.CS_DEBIT;
        } else if (codeDebitCredit.equals(CGImportEcrituresUtils.TAG_VALUE_CREDIT)) {
            return CodeSystem.CS_CREDIT;
        } else if (codeDebitCredit.equals(CGImportEcrituresUtils.TAG_VALUE_EXTOURNEDEBIT)) {
            return CodeSystem.CS_EXTOURNE_DEBIT;
        } else if (codeDebitCredit.equals(CGImportEcrituresUtils.TAG_VALUE_EXTOURNECREDIT)) {
            return CodeSystem.CS_EXTOURNE_CREDIT;
        } else {
            throw new Exception("CODE_DEBITCREDIT_INCORRECT");
        }
    }

    /**
     * Retourne le cours.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getCours(Element parent) throws Exception {
        return CGImportEcrituresUtils.getValue(CGImportEcrituresUtils.getChildElement(parent,
                CGImportEcrituresUtils.TAG_COURS));
    }

    /**
     * Retourne la date de valeur.
     * 
     * @param parent
     * @param journal
     * @return
     * @throws Exception
     */
    public static String getDate(Element parent, CGJournal journal) throws Exception {
        return CGImportEcrituresUtils.getDate(parent, journal, CGImportEcrituresUtils.TAG_DATE);
    }

    /**
     * Retourne la date de valeur.
     * 
     * @param parent
     * @param journal
     * @return La date spécifié dans le doc xml. Si vide la date de valeur CG du journal est retournée.
     * @throws Exception
     */
    private static String getDate(Element parent, CGJournal journal, String tag) throws Exception {
        Element date = CGImportEcrituresUtils.getChildElement(parent, tag);
        if (!JadeStringUtil.isBlank(CGImportEcrituresUtils.getValue(date))) {
            new JADate(CGImportEcrituresUtils.getValue(date));

            return CGImportEcrituresUtils.getValue(date);
        } else {
            return journal.getDateValeur();
        }
    }

    /**
     * Retourne la date de valeur de l'entête de lécriture collective.
     * 
     * @param parent
     * @param journal
     * @return
     * @throws Exception
     */
    public static String getDateEntete(Element parent, CGJournal journal) throws Exception {
        return CGImportEcrituresUtils.getDate(parent, journal, CGImportEcrituresUtils.TAG_DATEENTETE);
    }

    /**
     * Retourne la date de valeur.
     * 
     * @param parent
     * @param journal
     * @return La date spécifié dans le doc xml. Si vide la date de valeur CG du journal est retournée.
     * @throws Exception
     */
    public static String getDateValeur(Element parent) {
        try {
            Element date = CGImportEcrituresUtils.getChildElement(parent, CGImportEcrituresUtils.TAG_DATE_VALEUR);
            if (JadeStringUtil.isBlank(CGImportEcrituresUtils.getValue(date)) == true) {
                date = CGImportEcrituresUtils.getChildElement(parent, CGImportEcrituresUtils.TAG_DATE);
            }
            if (JadeStringUtil.isBlank(CGImportEcrituresUtils.getValue(date)) == true) {
                date = CGImportEcrituresUtils.getChildElement(parent, CGImportEcrituresUtils.TAG_DATEENTETE);
            }
            if (JadeStringUtil.isBlank(CGImportEcrituresUtils.getValue(date)) == false) {
                new JADate(CGImportEcrituresUtils.getValue(date));

                return CGImportEcrituresUtils.getValue(date);
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Retourne les écritures spécifiées spécifiées dans l'élément écriture collective.
     * 
     * @param collective
     * @return
     */
    public static NodeList getEcritures(Element collective) {
        return collective.getElementsByTagName(CGImportEcrituresUtils.TAG_ECRITURE);
    }

    /**
     * Retourne les écritures collective spécifiées dans le document xml.
     * 
     * @param doc
     * @return
     */
    public static NodeList getEcrituresCollective(Document doc) {
        return doc.getDocumentElement().getElementsByTagName(CGImportEcrituresUtils.TAG_COLLECTIVE);
    }

    /**
     * Retourne les écritures double spécifiées dans le document xml.
     * 
     * @param doc
     * @return
     */
    public static NodeList getEcrituresDouble(Document doc) {
        return doc.getDocumentElement().getElementsByTagName(CGImportEcrituresUtils.TAG_DOUBLE);
    }

    /**
     * Retourne l'id de l'écriture double/collective.
     * 
     * @param e
     * @return
     */
    public static String getId(Element e) {
        if (e != null) {
            return e.getAttribute(CGImportEcrituresUtils.TAG_ID);
        } else {
            return new String();
        }
    }

    /**
     * Retourne l'id du centre de charge crédité.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getIdCentreChargeCredite(Element parent) throws Exception {
        return CGImportEcrituresUtils.getValue(CGImportEcrituresUtils.getChildElement(parent,
                CGImportEcrituresUtils.TAG_IDCENTRECHARGECREDITE));
    }

    /**
     * Retourne l'id du centre de charge débité.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getIdCentreChargeDebite(Element parent) throws Exception {
        return CGImportEcrituresUtils.getValue(CGImportEcrituresUtils.getChildElement(parent,
                CGImportEcrituresUtils.TAG_IDCENTRECHARGEDEBITE));
    }

    /**
     * Retroune l'idCompte. <br>
     * Si l'idCompte n'est pas renseignée alors le compte sera résolu grâce à son idExterne.
     * 
     * @param session
     * @param transaction
     * @param parent
     * @param idExerciceComptable
     * @param idMandat
     * @param tagIdCompte
     * @param tagIdExterneCompte
     * @return
     * @throws Exception
     */
    private static String getIdCompte(BSession session, BTransaction transaction, Element parent,
            String idExerciceComptable, CGMandat mandat, String tagIdCompte, String tagIdExterneCompte)
            throws Exception {
        Element idCompte = CGImportEcrituresUtils.getChildElement(parent, tagIdCompte);

        if (JadeStringUtil.isIntegerEmpty(CGImportEcrituresUtils.getValue(idCompte))) {
            Element idExterneCompte = CGImportEcrituresUtils.getChildElement(parent, tagIdExterneCompte);

            if (!JadeStringUtil.isBlank(CGImportEcrituresUtils.getValue(idExterneCompte))) {

                CGPlanComptableManager manager = new CGPlanComptableManager();
                manager.setSession(session);

                manager.setForIdExerciceComptable(idExerciceComptable);
                manager.setForIdMandat(mandat.getIdMandat());
                manager.setForIdExterne(CGImportEcrituresUtils.getValue(idExterneCompte));

                manager.find(transaction);

                if (manager.isEmpty()) {
                    throw new Exception(session.getLabel("COMPTE_INTROUVABLE") + " " + manager.getForIdExterne()
                            + " - Exercice " + manager.getForIdExerciceComptable() + " - Mandat " + mandat.getLibelle());
                } else {
                    return ((CGPlanComptableViewBean) manager.getFirstEntity()).getIdCompte();
                }
            } else {
                throw new Exception(session.getLabel("COMPTE_OFAS_COMPTE_INEXISTANT"));
            }
        } else {
            return CGImportEcrituresUtils.getValue(idCompte);
        }
    }

    /**
     * Return l'id du compte crédité. <br/>
     * Cas : Ecriture double.
     * 
     * @param session
     * @param transaction
     * @param parent
     * @param idExerciceComptable
     * @param idMandat
     * @return
     * @throws Exception
     */
    public static String getIdCompteCredite(BSession session, BTransaction transaction, Element parent,
            String idExerciceComptable, CGMandat mandat) throws Exception {
        return CGImportEcrituresUtils.getIdCompte(session, transaction, parent, idExerciceComptable, mandat,
                CGImportEcrituresUtils.TAG_IDCOMPTECREDITE, CGImportEcrituresUtils.TAG_IDEXTERNECOMPTECREDITE);
    }

    /**
     * Return l'id du compte débité. <br/>
     * Cas : Ecriture double.
     * 
     * @param session
     * @param transaction
     * @param parent
     * @param idExerciceComptable
     * @param idMandat
     * @return
     * @throws Exception
     */
    public static String getIdCompteDebite(BSession session, BTransaction transaction, Element parent,
            String idExerciceComptable, CGMandat mandat) throws Exception {
        return CGImportEcrituresUtils.getIdCompte(session, transaction, parent, idExerciceComptable, mandat,
                CGImportEcrituresUtils.TAG_IDCOMPTEDEBITE, CGImportEcrituresUtils.TAG_IDEXTERNECOMPTEDEBITE);
    }

    /**
     * Return l'id du compte. <br/>
     * Cas : Ecriture collective.
     * 
     * @param session
     * @param transaction
     * @param parent
     * @param idExerciceComptable
     * @param idMandat
     * @return
     * @throws Exception
     */
    public static String getIdCompteEcritureCollective(BSession session, BTransaction transaction, Element parent,
            String idExerciceComptable, CGMandat mandat) throws Exception {
        return CGImportEcrituresUtils.getIdCompte(session, transaction, parent, idExerciceComptable, mandat,
                CGImportEcrituresUtils.TAG_IDCOMPTE, CGImportEcrituresUtils.TAG_IDEXTERNECOMPTE);
    }

    /**
     * Retourne le libellé de l'écriture.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getLibelle(Element parent) throws Exception {
        return CGImportEcrituresUtils.getValue(CGImportEcrituresUtils.getChildElement(parent,
                CGImportEcrituresUtils.TAG_LIBELLE));
    }

    /**
     * Retourne le libellé de l'entête de l'écriture collective.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getLibelleEntete(Element parent) throws Exception {
        return CGImportEcrituresUtils.getValue(CGImportEcrituresUtils.getChildElement(parent,
                CGImportEcrituresUtils.TAG_LIBELLEENTETE));
    }

    /**
     * Retourne le mandat.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getMandat(BSession session, Element parent, String idExercice) throws Exception {
        String numMandat = "";
        try {
            numMandat = CGImportEcrituresUtils.getValue(CGImportEcrituresUtils.getChildElement(parent,
                    CGImportEcrituresUtils.TAG_MANDAT));
        } catch (Exception e) {
            numMandat = "";
        }
        // Si balise mandat non présente => recherche selon l'idexercice sélectionnée (chargement manuel)
        if (JadeStringUtil.isEmpty(numMandat) && (JadeStringUtil.isEmpty(idExercice) == false)) {
            CGExerciceComptable exercice = new CGExerciceComptable();
            exercice.setSession(session);
            exercice.setIdExerciceComptable(idExercice);
            exercice.retrieve();
            if (exercice.isNew() == false) {
                numMandat = exercice.getIdMandat();
            }
        }
        return numMandat;
    }

    /**
     * Retourne le montant.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getMontant(Element parent) throws Exception {
        return CGImportEcrituresUtils.getValue(CGImportEcrituresUtils.getChildElement(parent,
                CGImportEcrituresUtils.TAG_MONTANT));
    }

    /**
     * Retourne le montant monnaie étrangère.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getMontantEtranger(Element parent) throws Exception {
        return CGImportEcrituresUtils.getValue(CGImportEcrituresUtils.getChildElement(parent,
                CGImportEcrituresUtils.TAG_MONTANTETRANGER));
    }

    /**
     * Retourne la pièce comptable.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getPiece(Element parent) throws Exception {
        return CGImportEcrituresUtils.getValue(CGImportEcrituresUtils.getChildElement(parent,
                CGImportEcrituresUtils.TAG_PIECE));
    }

    /**
     * Retourne la pièce comptable de l'entête de l'écriture collective.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getPieceEntete(Element parent) throws Exception {
        return CGImportEcrituresUtils.getValue(CGImportEcrituresUtils.getChildElement(parent,
                CGImportEcrituresUtils.TAG_PIECEENTETE));
    }

    /**
     * Retourne la remarque.
     * 
     * @param parent
     * @return
     * @throws Exception
     */
    public static String getRemarque(Element parent) throws Exception {
        return CGImportEcrituresUtils.getValue(CGImportEcrituresUtils.getChildElement(parent,
                CGImportEcrituresUtils.TAG_REMARQUE));
    }

    /**
     * Retourne la valeur de l'attribut.
     * 
     * @param e
     * @return
     * @throws Exception
     */
    private static String getValue(Element e) throws Exception {
        if (e != null) {
            return e.getAttribute(CGImportEcrituresUtils.TAG_VALUE);
        } else {
            return new String();
        }
    }
}
