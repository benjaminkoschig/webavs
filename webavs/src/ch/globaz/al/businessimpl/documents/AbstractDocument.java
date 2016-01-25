package ch.globaz.al.businessimpl.documents;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import java.util.Date;
import java.util.HashMap;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.exceptions.document.ALDocumentAddressException;
import ch.globaz.al.business.exceptions.document.ALDocumentException;
import ch.globaz.al.business.exceptions.enteteDocument.ALEnteteDocumentException;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.param.business.service.ParamServiceLocator;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Classe abstraite commune � tous les documents g�n�r�s par l'application
 * 
 * @author jts
 * 
 */
public abstract class AbstractDocument extends ALAbstractBusinessServiceImpl {

    /**
     * D�finit l'adresse dans le document.
     * 
     * La m�thode r�cup�re l'adresse correspondant au tiers <code>idTiersAdresse</code> valable � la date
     * <code>dateValiditeAdresse</code> et la place dans le tag "destinataireAdresse".
     * 
     * @param document
     *            Document dans lequel placer l'adresse
     * @param idTiersAdresse
     *            identifiant du tiers pour l'adresse
     * @param dateValiditeAdresse
     *            date de validit� de l'adresse
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected void addAdresse(DocumentData document, String idTiersAdresse, String dateValiditeAdresse)
            throws JadePersistenceException, JadeApplicationException {

        if (document == null) {
            throw new ALDocumentException("AbstractDocument#addAdresse : document is null");
        }

        if (!JadeNumericUtil.isIntegerPositif(idTiersAdresse)) {
            throw new ALDocumentException("AbstractDocument#addAdresse : idTiersAdresse '" + idTiersAdresse
                    + "' is not a valid idTiers");
        }

        if (!JadeDateUtil.isGlobazDate(dateValiditeAdresse)) {
            throw new ALDocumentException("AbstractDocument#addAdresse : " + dateValiditeAdresse
                    + " is not a valid date");
        }

        AdresseTiersDetail address = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(idTiersAdresse,
                new Boolean(true), dateValiditeAdresse, ALCSTiers.DOMAINE_AF, AdresseService.CS_TYPE_COURRIER, "");

        if (JadeStringUtil.isEmpty(address.getAdresseFormate())) {
            throw new ALDocumentAddressException("AbstractDocument#addDateAdresseLibre : idTiersAdresse '"
                    + idTiersAdresse + "' has no address");
        }

        document.addData("destinataireAdresse", address.getAdresseFormate());
    }

    /**
     * M�thode permettant d'ajouter une date, un lieu selon idTiers
     * 
     * @param document
     *            Document dans lequel placer la date et l'adresse
     * @param dateLettre
     *            Date � afficher sur la lettre
     * @param adresse
     *            Adresse � afficher sur le document
     * @param langueDocument
     *            langue du document
     * @param idTiersLieu
     *            identifiant du tiers pour trouver le lieu
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected void addAdresseLibre(DocumentData document, String dateLettre, String adresse, String idTiersLieu,
            String langueDocument) throws JadePersistenceException, JadeApplicationException {

        if (document == null) {
            throw new ALDocumentException("AbstractDocument#addDateAdresseLibre: document is null");
        }
        if (!JadeDateUtil.isGlobazDate(dateLettre)) {
            throw new ALDocumentException("AbstractDocument#addDateAdresseLibre : " + dateLettre
                    + " is not a globaz'date valide");
        }
        if (adresse == null) {
            throw new ALDocumentException("AbstractDocument#addDateAdresseLibre: adresse is null");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDocumentException("AbstractDocument#addDateAdresseLibre: language  " + langueDocument
                    + " is not  valid ");
        }

        AdresseTiersDetail address = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(idTiersLieu,
                new Boolean(true), dateLettre, ALCSTiers.DOMAINE_AF,
                ch.globaz.pyxis.business.service.AdresseService.CS_TYPE_COURRIER, "");

        if (JadeStringUtil.isEmpty(address.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE))) {
            throw new ALDocumentAddressException("AbstractDocument#addAdresseLibre : idTiersLieu '" + idTiersLieu
                    + "' has not address");
        }

        StringBuffer sb = new StringBuffer(address.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE));

        sb.append(", ").append(this.getText("al.documentCommun.le", langueDocument)).append(" ").append(dateLettre);
        document.addData("lieuDate", sb.toString());
        document.addData("destinataire", adresse);
    }

    /**
     * D�finit l'adresse et la date du document.
     * 
     * La m�thode r�cup�re l'adresse correspondant au tiers <code>idTiersAdresse</code> valable � la date
     * <code>date</code> et la place dans les tags "lieuDate" et "destinataire".
     * 
     * <code>date</code> est �galement plac� dans le tag "lieuDate"
     * 
     * @param document
     *            Document dans lequel placer la date et l'adresse
     * @param date
     *            date de la lettre et de validit� de l'adresse
     * @param idTiersAdresse
     *            id tiers destinataire
     * @param langueDocument
     *            langue du document
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    protected void addDateAdresse(DocumentData document, String date, String idTiersAdresse, String langueDocument,
            String numAffilie) throws JadePersistenceException, JadeApplicationException {
        // contr�le des param�tre
        if (document == null) {
            throw new ALDocumentException("AbstractDocument#addDateAdresse: document is null");
        }

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALDocumentException("AbstractDocument#addDateAdresse: " + date + " is not a valid globaz date");
        }

        if (!JadeNumericUtil.isIntegerPositif(idTiersAdresse)) {
            throw new ALDocumentException("AbstractDocument#addDateAdresse: " + idTiersAdresse
                    + " is not a integer positiv");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDocumentException("AbstractDocument#addDateAdresse: language  " + langueDocument
                    + " is not  valid ");
        }

        this.addDateAdresse(document, date, idTiersAdresse, date, langueDocument, numAffilie);
    }

    /**
     * D�finit l'adresse et la date du document(� utiliser lorsque
     * 
     * La m�thode r�cup�re l'adresse correspondant au tiers <code>idTiersAdresse</code> valable � la date
     * <code>dateValiditeAdress</code>et la place dans les tags "lieuDate" et "destinataire".
     * 
     * <code>dateLettre</code> est plac� dans le tag "lieuDate"
     * 
     * @param document
     *            Document dans lequel placer la date et l'adresse
     * @param dateLettre
     *            Date � afficher sur la lettre
     * @param idTiersAdresse
     *            id du tiers destinataire
     * @param dateValiditeAdresse
     *            date de la validit� de l'adresse
     * @param langueDocument
     *            lanuge du document
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    protected void addDateAdresse(DocumentData document, String dateLettre, String idTiersAdresse,
            String dateValiditeAdresse, String langueDocument, String numAffilie) throws JadePersistenceException,
            JadeApplicationException {

        if (document == null) {
            throw new ALDocumentException("AbstractDocument#addDateAdresse: document is null");
        }
        if (!JadeDateUtil.isGlobazDate(dateLettre)) {
            throw new ALDocumentException("AbstractDocument#addDateAdresse: " + dateLettre
                    + " is not a globaz'date valid");
        }
        if (!JadeNumericUtil.isIntegerPositif(idTiersAdresse)) {
            throw new ALDocumentException("AbstractDocument#addDateAdresse: " + idTiersAdresse
                    + " is not a integer positiv");
        }
        if (!JadeDateUtil.isGlobazDate(dateValiditeAdresse)) {
            throw new ALDocumentException("AbstractDocument#addDateAdresse: " + dateValiditeAdresse
                    + " is not a globaz'date valid");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDocumentException("AbstractDocument#addDateAdresse: language  " + langueDocument
                    + " is not  valid ");
        }

        String today = JadeDateUtil.getGlobazFormattedDate(new Date());
        String noSuccursaleAF = ParamServiceLocator.getParameterModelService()
                .getParameterByName(ALConstParametres.APPNAME, ALConstParametres.NUMERO_SUCCURSALE_CAISSE, today)
                .getValeurAlphaParametre();
        AdministrationSearchComplexModel search = new AdministrationSearchComplexModel();
        search.setForCodeAdministrationLike(noSuccursaleAF);

        AdministrationSearchComplexModel admins = TIBusinessServiceLocator.getAdministrationService().find(search);

        if (admins.getSize() != 1) {
            throw new ALDocumentAddressException(
                    "AbstractDocument#addDateAdresse : impossible de d�terminer l'administration avec le code :"
                            + noSuccursaleAF + ", veuillez contr�ler vos administrations");
        }
        // adresse de la caisse, voir si correspond � la cascade
        AdresseTiersDetail adresse = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(
                ((AdministrationComplexModel) admins.getSearchResults()[0]).getTiers().getIdTiers(), true, today,
                JadeThread.currentContext().getApplicationId(), AdresseService.CS_TYPE_COURRIER, null);

        this.addDateAdresse(document, adresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE), dateLettre,
                idTiersAdresse, dateValiditeAdresse, langueDocument, numAffilie);
    }

    /**
     * D�finit l'adresse et la date du document (� utiliser, par exemple, lorsque le lieu correspond � celui de
     * l'affili�)
     * 
     * @param document
     *            document dans lequel placer l'information
     * @param lieuLettre
     *            lieu de l'exp�diteur
     * @param dateLettre
     *            date dl lettre
     * @param idTiersAdresse
     *            id du tiers destinataire
     * @param dateValiditeAdresse
     *            date de validit� de l'adresse
     * @param langueDocument
     *            langue du document
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    protected void addDateAdresse(DocumentData document, String lieuLettre, String dateLettre, String idTiersAdresse,
            String dateValiditeAdresse, String langueDocument, String numAffilie) throws JadePersistenceException,
            JadeApplicationException {

        // v�rifier les param�tres
        if (document == null) {
            throw new ALDocumentException("AbstractDocument#addDateAdresse: document is null");
        }
        if (lieuLettre == null) {
            throw new ALDocumentException("AbstractDocument#addDateAdresse : lieuLettre is null");
        }
        if (!JadeDateUtil.isGlobazDate(dateLettre)) {
            throw new ALDocumentException("AbstractDocument#addDateAdresse : " + dateLettre + " is not a valid date");
        }
        if (!JadeNumericUtil.isIntegerPositif(idTiersAdresse)) {
            throw new ALDocumentException("AbstractDocument#addDateAdresse : " + idTiersAdresse
                    + " is not a valid idTiers");
        }
        if (!JadeDateUtil.isGlobazDate(dateValiditeAdresse)) {
            throw new ALDocumentException("AbstractDocument# setDateAdresse : " + dateValiditeAdresse
                    + " is not a valid date");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDocumentException("AbstractDocument#addDateAdresse: language  " + langueDocument
                    + " is not  valid ");
        }

        StringBuffer sb = new StringBuffer(lieuLettre);
        sb.append(", ").append(this.getText("al.documentCommun.le", langueDocument)).append(" ").append(dateLettre);

        AdresseTiersDetail address = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(idTiersAdresse,
                new Boolean(true), dateValiditeAdresse, ALCSTiers.DOMAINE_AF,
                ch.globaz.pyxis.business.service.AdresseService.CS_TYPE_COURRIER, numAffilie);

        if (JadeStringUtil.isEmpty(address.getAdresseFormate())) {
            throw new ALDocumentAddressException("AbstractDocument#addDateAdresse : idTiersAdresse '" + idTiersAdresse
                    + "' has no address");
        }

        document.addData("lieuDate", sb.toString());
        document.addData("destinataire", address.getAdresseFormate());
    }

    /**
     * R�cup�re le texte correspondant � <code>textId</code> et dans la langue de l'utilisateur
     * 
     * @param textId
     *            identifiant du texte
     * @return le texte dans la langue de l'utilisateur
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected String getText(String textId) throws JadeApplicationException {

        if (textId == null) {
            throw new ALDocumentException("AbstractDocument#getText: texteId is null");
        }

        return this.getText(textId, JadeThread.currentLanguage(), null);
    }

    /**
     * R�cup�re le texte correspondant � <code>textId</code> et dans la <code>langue</code> indiqu�e
     * 
     * @param textId
     *            identifiant du texte
     * @param langue
     *            Langue dans laquelle r�cup�rer le texte
     * @return le texte dans la langue indiqu�e
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected String getText(String textId, String langue) throws JadeApplicationException {

        if (textId == null) {
            throw new ALDocumentException("AbstractDocument# getText: texteId is null");
        }
        if (langue == null) {
            throw new ALDocumentException("AbstractDocument# getText: langue is null");
        }
        return JadeI18n.getInstance().getMessage(langue, textId, null);

    }

    /**
     * R�cup�re le texte correspondant � <code>textId</code> et dans la <code>langue</code> indiqu�e
     * 
     * @param textId
     *            identifiant du texte
     * @param langue
     *            Langue dans laquelle r�cup�rer le texte
     * @param params
     *            tableau contenant les param�tres
     * @return le texte dans la langue indiqu�e
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected String getText(String textId, String langue, String[] params) throws JadeApplicationException {

        if (textId == null) {
            throw new ALDocumentException("AbstractDocument# getText: texteId is null");
        }
        if (langue == null) {
            throw new ALDocumentException("AbstractDocument# getText: langue is null");
        }

        return JadeI18n.getInstance().getMessage(langue, textId, params);
    }

    /**
     * R�cup�re le texte correspondant � <code>textId</code> et dans la <code>langue</code> indiqu�e
     * 
     * @param textId
     *            identifiant du texte
     * @param params
     *            tableau contenant les param�tres
     * @return le texte dans la langue indiqu�e
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected String getText(String textId, String[] params) throws JadeApplicationException {

        if (textId == null) {
            throw new ALDocumentException("AbstractDocument# getText: texteId is null");
        }
        if (params == null) {
            throw new ALDocumentException("AbstractDocument# getText: params is null");
        }
        return this.getText(textId, JadeThread.currentLanguage(), params);
    }

    /**
     * Permet de d�finir la template du document � utiliser
     * 
     * Exemple : <code>documentData.addData("AL_idDocument", "AL_protocoleErreursCompensation");</code> 'idProcess'
     * correspond � un attribut d'une condition du tag <doc-selection> du fichier topaz-config.xml (projet Properties).
     * 'AL_protocoleErreursCompensation' correspond � sa valeur
     * 
     * @param document
     *            Document auquel assigner la template
     * @throws JadeApplicationException
     *             Exception lev�e si <code>document</code> est <code>null</code>
     */
    protected abstract void setIdDocument(DocumentData document) throws JadeApplicationException;

    /**
     * D�finit l'id de l'en-t�te � utiliser en fonction des param�tres de la caisse
     * 
     * @param document
     *            document � g�n�rer
     * @throws JadePersistenceException
     *             Exception lev�e si le nom de la caisse n'a pas pu �tre r�cup�r�
     * @throws JadeApplicationException
     *             Exception lev�e si le nom de la caisse n'a pas pu �tre r�cup�r�
     */
    protected void setIdEntete(DocumentData document) throws JadePersistenceException, JadeApplicationException {
        // contr�le du param�tre
        if (document == null) {
            throw new ALDocumentException("AbstractDocument# setIdEntete : document is null");
        }

        // TODO (lot 2) am�liorer la gestion des en-t�te (service affiliations)
        document.addData("idEntete", ALServiceLocator.getParametersServices().getNomCaisse() + "_Caisse");
    }

    protected void setIdEntete(DocumentData document, String activiteAllocataire, String typeDocument,
            String langueAffilie) throws JadePersistenceException, JadeApplicationException {
        // contr�le des param�tres
        if (document == null) {
            throw new ALDocumentException("AbstractDocument#setIdEntete : document is null");
        }
        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSDossier.GROUP_ACTIVITE_ALLOC, activiteAllocataire)) {
                throw new ALEnteteDocumentException("");
            }
        } catch (Exception e) {
            throw new ALEnteteDocumentException("AbstractDocument#setIdEntete: activiteAllocataire is not valid "
                    + e.getMessage(), e);
        }
        // typeDocument
        if (!JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_DECISION, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_DECLARATION_VERSEMENT, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_ECHEANCE_AFFILIE, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_ECHEANCE_ALLOC_DIR, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_ECHEANCE_ALLOC_IND, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.LETTRE_ACCOMPAGNEMENT, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_PROTOCOLE, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.RECAPITULATIF_DOCUMENT, false)) {
            throw new ALEnteteDocumentException("AbstractDocument#setIdEntete typeDocument is not valid");
        }

        document.addData(
                "idEntete",
                ALImplServiceLocator.getEnteteService().getEnteteDocument(activiteAllocataire, typeDocument,
                        langueAffilie));
    }

    /**
     * 
     * @param document
     * @param activiteAllocataire
     * @param typeDocument
     * 
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */

    protected void setIdEntete(DocumentData document, String activiteAllocataire, String typeDocument,
            String langueAffilie, HashMap<String, String> userInfos) throws JadePersistenceException,
            JadeApplicationException {
        // contr�le des param�tres
        if (document == null) {
            throw new ALDocumentException("AbstractDocument#setIdEntete : document is null");
        }
        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSDossier.GROUP_ACTIVITE_ALLOC, activiteAllocataire)) {
                throw new ALEnteteDocumentException("");
            }
        } catch (Exception e) {
            throw new ALEnteteDocumentException("AbstractDocument#setIdEntete: activiteAllocataire is not valid "
                    + e.getMessage(), e);
        }
        // typeDocument
        if (!JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_DECISION, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_DECLARATION_VERSEMENT, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_ECHEANCE_AFFILIE, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_ECHEANCE_ALLOC_DIR, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_ECHEANCE_ALLOC_IND, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.LETTRE_ACCOMPAGNEMENT, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_PROTOCOLE, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.RECAPITULATIF_DOCUMENT, false)) {
            throw new ALEnteteDocumentException("AbstractDocument#setIdEntete typeDocument is not valid");
        }

        document.addData(
                "idEntete",
                ALImplServiceLocator.getEnteteService().getEnteteDocument(activiteAllocataire, typeDocument,
                        langueAffilie));
        // ajoute le libell� pour le gestionnaire
        document.addData("gestionnaire_libelle", this.getText("al.documentCommun.gestionnaire", langueAffilie));
        // ajoute les donn�es de l'utilisteur
        ALImplServiceLocator.getEnteteDocumentUserInfoService().addUserInfoToDocument(document, userInfos);

    }

    /**
     * D�finit la signature pour le document
     * 
     * @param document
     *            document � g�n�rer
     * @throws JadePersistenceException
     *             Exception lev�e si le nom de la caisse n'a pas pu �tre r�cup�r�
     * @throws JadeApplicationException
     *             Exception lev�e si le nom de la caisse n'a pas pu �tre r�cup�r�
     */
    protected void setIdSignature(DocumentData document) throws JadeApplicationException, JadePersistenceException {

        if (document == null) {
            throw new ALDocumentException("AbstractDocument#setIdSignature : document is null");
        }

        document.addData("idSignature", ALServiceLocator.getParametersServices().getNomCaisse() + "_Caisse");
    }

    protected void setIdSignature(DocumentData document, String activiteAllocataire) throws JadeApplicationException,
            JadePersistenceException, JadeApplicationException {

        if (document == null) {
            throw new ALDocumentException("AbstractDocument#setIdSignature : document is null");
        }

        document.addData("idSignature",

        ALServiceLocator.getParametersServices().getNomCaisse() + "_Caisse");
    }

    protected void setIdSignature(DocumentData document, String activiteAllocataire, String typeDocument,
            String langueDocument) throws JadeApplicationException, JadePersistenceException {

        if (document == null) {
            throw new ALDocumentException("AbstractDocument#setIdSignature : document is null");
        }
        // activit� de l'allocataire
        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSDossier.GROUP_ACTIVITE_ALLOC, activiteAllocataire)) {
                throw new ALDocumentException("");
            }
        } catch (Exception e) {
            throw new ALDocumentException("AbstractDocument#setIdSignature: activiteAllocataire is not valid "
                    + e.getMessage(), e);

        }
        // typeDocument
        if (!JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_DECISION, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_DECLARATION_VERSEMENT, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_ECHEANCE_AFFILIE, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_ECHEANCE_ALLOC_DIR, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_ECHEANCE_ALLOC_IND, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.LETTRE_ACCOMPAGNEMENT, false)) {
            throw new ALDocumentException("AbstractDocument#setIdSignature typeDocument is not valid");
        }

        // langueDocument
        if (!JadeStringUtil.equals(ALConstLangue.LANGUE_ISO_ALLEMAND, langueDocument, false)
                && !JadeStringUtil.equals(ALConstLangue.LANGUE_ISO_FRANCAIS, langueDocument, false)
                && !JadeStringUtil.equals(ALConstLangue.LANGUE_ISO_ITALIEN, langueDocument, false)) {
            throw new ALDocumentException("AbstractDocument#setIdSignature langueDocument is not valid");
        }

        document.addData(
                "signature_af",
                this.getText(
                        ALImplServiceLocator.getSignatureService().getLibelleSignature(activiteAllocataire,
                                typeDocument), langueDocument));

        // Possibilit� d'avoir un deuxi�me texte s�par� de la signature (exemple : (document valable sans signature))
        document.addData("signature_af_texte", this.getText("al.decision.signature.texte", langueDocument));
    }
}
