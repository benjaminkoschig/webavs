package ch.globaz.perseus.business.models.donneesfinancieres;

public enum RevenuType {
    AIDE_FORMATION(426, true),
    AIDES_LOGEMENT(403, false),
    ALLOCATION_CANTONALE_MATERNITE(435, true),
    ALLOCATIONS_AMINH(436, true),
    ALLOCATIONS_FAMILIALES(425, false),
    AUTRES_CREANCES(404, false),
    AUTRES_RENTES(417, false),
    AUTRES_REVENUS_ENFANT(438, false),
    BRAPA(442, false),
    BRAPA_ENFANT(443, false),
    CONTRAT_ENTRETIENS_VIAGER(410, false),
    DROIT_HABITATION(402, false),
    INDEMNITES_JOURNALIERES_ACCIDENTS(419, true),
    INDEMNITES_JOURNALIERES_AI(422, true),
    INDEMNITES_JOURNALIERES_APG(421, true),
    INDEMNITES_JOURNALIERES_CHOMAGE(420, true),
    INDEMNITES_JOURNALIERES_MALADIE(418, true),
    INDEMNITES_JOURNALIERES_MILITAIRE(423, true),
    INTERET_FORTUNE(428, false),
    LOYERS_ET_FERMAGES(430, false),
    PENSION_ALIMENTAIRE(416, false),
    PENSION_ALIMENTAIRE_ENFANT(441, false),
    RENTE_ENFANT(440, false),
    RENTE_VEUF_OU_ORPHELIN(443, false),
    RENTES_3P(409, false),
    RENTES_AI(413, false),
    RENTES_ASSURANCES_PRIVEES(411, false),
    RENTES_AVS(414, false),
    RENTES_ETRANGERES(407, false),
    RENTES_LAA(408, false),
    RENTES_LPP(415, false),
    RENTES_MILITAIRE(412, false),
    REVENU_BRUT_IMPOT_SOURCE(427, false),
    REVENU_HYPOTHETIQUE_CAS_RIGUEUR(424, false),
    REVENU_INDEPENDANT(431, false),
    REVENUS_ACTIVITE_ENFANT(437, false),
    SALAIRE_NATURE(433, false),
    SALAIRE_NET(432, true),
    SOUS_LOCATION(439, false),
    SUCCESSION_NON_PARTAGEE(405, false),
    TAUX_OCCUPATION(434, true),
    TOTAL_RENTES(406, true),
    VALEUR_LOCATIVE_PROPRE_IMMEUBLE(429, false),
    VALEUR_USUFRUIT(401, false);

    /**
     * Permet de retrouver un RevenuType sur la base de son ID stocké en base de données
     * 
     * @param idType
     *            stocké en base de données
     * @return le type correspondant à l'id passé
     */
    public static RevenuType getTypeFromId(String idType) {
        RevenuType[] allValues = RevenuType.values();
        for (int i = 0; i < allValues.length; i++) {
            RevenuType rt = allValues[i];
            if (rt.id.equals(Integer.valueOf(idType))) {
                return rt;
            }
        }
        return null;
    }

    private Integer id;
    private Boolean useSpecialisation;

    private RevenuType(Integer id, boolean useSpecialisation) {
        this.id = id;
        this.useSpecialisation = useSpecialisation;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the useSpecialisation
     */
    public Boolean getUseSpecialisation() {
        return useSpecialisation;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    public Boolean useSpecialisation() {
        return useSpecialisation;
    }

}
