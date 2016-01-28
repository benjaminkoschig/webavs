package ch.globaz.common.util;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pyxis.db.adressecourrier.TIAdresse;
import globaz.pyxis.db.adressepaiement.TIAdressePaiement;
import ch.ech.xmlns.ech_0010._4.MailAddressType;
import ch.globaz.al.business.constantes.ALConstEditingDecision;
import ch.globaz.al.utils.ALEditingUtils;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.inforom.xmlns.editing_common._1.BanqueType;
import ch.inforom.xmlns.editing_common._1.CoordoneesPaiementType;

public final class EditingCommon {

    private EditingCommon() {
    };

    public static CoordoneesPaiementType buildCoordoneesPaiementType(AdresseTiersDetail adressPmt)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        CoordoneesPaiementType pmt = new CoordoneesPaiementType();

        // si l'adresse de paiement est un compte CCP
        if (!JadeStringUtil.isBlankOrZero(adressPmt.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_CCP))) {
            pmt.setCCP(adressPmt.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_CCP));

        }
        // si adresse de paiement n'est pas un compte postal, mais un compte
        // bancaire
        else if (!JadeStringUtil.isBlank(adressPmt.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_D1))) {
            // numéro de compte bancaire
            pmt.setCompte(adressPmt.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_COMPTE));
            // données de la banque
            BanqueType banque = new BanqueType();
            // comof.createBanqueType();
            // désignation
            String banqueDes = adressPmt.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_D1) + " "
                    + adressPmt.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_D2);
            banque.setDesignation(banqueDes.trim());
            // clearing
            if (!JadeStringUtil.isEmpty(adressPmt.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_CLEARING))) {
                banque.setClearing(adressPmt.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_CLEARING));
            }

            pmt.setBanque(banque);
        }
        String idAdressePmt = adressPmt.getFields().get(AdresseTiersDetail.ADRESSEP_ID_ADRESSE);
        TIAdressePaiement adrPmt = new TIAdressePaiement();
        adrPmt.setIdAdressePmtUnique(idAdressePmt);
        try {
            adrPmt.retrieve();
            adressPmt.getFields().put(AdresseTiersDetail.ADRESSE_ID_ADRESSE, adrPmt.getIdAdresse()); // adr du benef
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }

        MailAddressType benef = buildMailAdressType(adrPmt.getIdTiersAdresse(), adressPmt);
        pmt.setBeneficiaire(benef);
        return pmt;

    }

    /**
     * méthode qui remplit une adresse de courrier (MailAdressType)
     * 
     * @param dossier
     * @param mailAdressType
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static MailAddressType buildMailAdressType(String idTiers, AdresseTiersDetail addressCourrier)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {

        MailAddressType mailAdressType = new MailAddressType();

        String idAdresse = addressCourrier.getFields().get(AdresseTiersDetail.ADRESSE_ID_ADRESSE);
        TIAdresse adr = new TIAdresse();
        adr.setIdAdresseUnique(idAdresse);
        try {
            adr.retrieve();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }

        boolean adresseSurchargeTiers = !JadeStringUtil.isBlankOrZero(adr.getLigneAdresse1());

        /*
         * Dans ech10, il y a deux type d'adresses :
         * 1) les adresses des pers. physique
         * 2) les adresses des organizations.
         * 
         * Dans pyxis, il n'est pas toujours possible de savoir si une adresse concerne une personne physique.
         * En effet, lorsqu'une adresse "surcharge" les données du tiers, il faut systématiquement considérer cette
         * adresse comme étant celle d'une organisation, ou le nom et le prénom ne sont pas distingués
         * dans des champs précis.
         */
        PersonneEtendueComplexModel personne = TIBusinessServiceLocator.getPersonneEtendueService().read(idTiers);
        boolean tiersIsPersPhysique = "1".equals(personne.getTiers().get_personnePhysique());

        if (tiersIsPersPhysique && (!adresseSurchargeTiers)) {
            ch.ech.xmlns.ech_0010._4.PersonMailAddressInfoType personMail = new ch.ech.xmlns.ech_0010._4.PersonMailAddressInfoType();

            /*
             * Personne physique (et l'adresse ne surcharge pas les donnée du tiers), on pourra identifié le nom et le
             * prénom.
             */
            String csTitre = addressCourrier.getFields().get(AdresseTiersDetail.ADRESSE_VAR_CS_TITRE);

            // le titre du tiers peut contenir soit "genre ech10" soit un title
            if (!JadeStringUtil.isBlankOrZero(csTitre)) {
                String genre = getGenrePersonne(csTitre);
                if (!JadeStringUtil.isBlankOrZero(genre)) {
                    personMail.setMrMrs(genre);
                } else {
                    personMail.setTitle(addressCourrier.getFields().get(AdresseTiersDetail.ADRESSE_VAR_TITRE));
                }
            }

            // firstName
            if (!JadeStringUtil.isEmpty(addressCourrier.getFields().get(AdresseTiersDetail.ADRESSE_VAR_T2))) {
                personMail.setFirstName(addressCourrier.getFields().get(AdresseTiersDetail.ADRESSE_VAR_T2));
            }
            // lastName
            if (!JadeStringUtil.isEmpty(addressCourrier.getFields().get(AdresseTiersDetail.ADRESSE_VAR_T1))) {
                personMail.setLastName(addressCourrier.getFields().get(AdresseTiersDetail.ADRESSE_VAR_T1));
            }
            mailAdressType.setPerson(personMail);
        } else {
            /*
             * Personne morale. (ou adresse surcharge les champs du tiers)
             */
            ch.ech.xmlns.ech_0010._4.OrganisationMailAddressInfoType orgMail = new ch.ech.xmlns.ech_0010._4.OrganisationMailAddressInfoType();
            String d1 = addressCourrier.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D1);
            String d2 = addressCourrier.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D2);
            String d3 = addressCourrier.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D3);
            String d4 = addressCourrier.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D4);
            orgMail.setOrganisationName((d1 + " " + d2).trim());
            orgMail.setOrganisationNameAddOn1(d3);
            orgMail.setOrganisationNameAddOn2(d4);

            mailAdressType.setOrganisation(orgMail);

        }

        ch.ech.xmlns.ech_0010._4.AddressInformationType addressInformationType = new ch.ech.xmlns.ech_0010._4.AddressInformationType();

        // A l'attention de
        if (!JadeStringUtil.isBlankOrZero(addressCourrier.getFields().get(AdresseTiersDetail.ADRESSE_VAR_ATTENTION))) {
            addressInformationType.setAddressLine1(addressCourrier.getFields().get(
                    AdresseTiersDetail.ADRESSE_VAR_ATTENTION));
        }

        // case postale
        if (!JadeStringUtil.isBlankOrZero(addressCourrier.getFields().get(AdresseTiersDetail.ADRESSE_VAR_CASE_POSTALE))) {
            addressInformationType.setPostOfficeBoxNumber(JadeStringUtil.parseLong(
                    addressCourrier.getFields().get(AdresseTiersDetail.ADRESSE_VAR_CASE_POSTALE), 0));
        }

        // rue
        addressInformationType.setStreet(addressCourrier.getFields().get(AdresseTiersDetail.ADRESSE_VAR_RUE));
        // numéro de rue
        if (!JadeStringUtil.isBlankOrZero(addressCourrier.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NUMERO))) {
            addressInformationType.setHouseNumber(addressCourrier.getFields()
                    .get(AdresseTiersDetail.ADRESSE_VAR_NUMERO));
        }
        // localité
        addressInformationType.setTown(addressCourrier.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE));
        // code postale
        addressInformationType.setSwissZipCode(JadeStringUtil.parseLong(
                addressCourrier.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA), 0));
        // pays
        addressInformationType.setCountry(addressCourrier.getFields().get(AdresseTiersDetail.ADRESSE_VAR_PAYS_ISO));

        mailAdressType.setAddressInformation(addressInformationType);

        return mailAdressType;

    }

    /**
     * Méthode qui retourne le genre editing pour une personne titre
     * 
     * @throws JadeApplicationException
     */
    public static String getGenrePersonne(String csTitre) throws JadeApplicationException {
        String titreEditing = "";
        if (JadeStringUtil.equals(ALConstEditingDecision.PERS_CS_GENRE_MADAME, csTitre, false)
                || JadeStringUtil.equals(ALConstEditingDecision.PERS_CS_GENRE_MONSIEUR, csTitre, false)
                || JadeStringUtil.equals(ALConstEditingDecision.PERS_CS_GENRE_MADEMOISELLE, csTitre, false)) {
            titreEditing = ALEditingUtils.getValueEditingGenrePersonne(csTitre);
        }
        return titreEditing;
    }

}
