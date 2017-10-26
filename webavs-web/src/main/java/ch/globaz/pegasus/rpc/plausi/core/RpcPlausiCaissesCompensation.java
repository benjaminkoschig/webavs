package ch.globaz.pegasus.rpc.plausi.core;

public enum RpcPlausiCaissesCompensation {
    // Caisses cantonales
    CCC_1(1, "Zürich"),
    CCC_2(2, "Berne"),
    CCC_238(2, 38, "Ville de Berne"),
    CCC_249(2, 49, "Ville Biel/Bienne et environnement"),
    CCC_266(2, 66, "Personnel d'état"),
    CCC_3(3, "Lucerne"),
    CCC_4(4, "Uri"),
    CCC_5(5, "Svitto"),
    CCC_6(6, "Obwald"),
    CCC_7(7, "Nidwald"),
    CCC_8(8, "Glaris"),
    CCC_9(9, "Zoug"),
    CCC_10(10, "Fribourg"),
    CCC_11(11, "Soleure"),
    CCC_12(12, "Bâle-Ville"),
    CCC_13(13, "Bâle-Campagne"),
    CCC_14(14, "Schaffhouse"),
    CCC_15(15, "Appenzell Rhodes-Extérieures"),
    CCC_16(16, "Appenzell Rhodes-Intérieures"),
    CCC_17(17, "Saint-Gall"),
    CCC_18(18, "Grisons"),
    CCC_19(19, "Argovie"),
    CCC_20(20, "Thurgovie"),
    CCC_21(21, "Tessin"),
    CCC_22(22, "Vaud"),
    CCC_22132(22, 132, "Vaud, Agence Lausanne"),
    CCC_23(23, "Valais"),
    CCC_24(24, "Neuchâtel"),
    CCC_25(25, "Genève"),
    CCC_150(150, "Jura"),
    CCC_261(26, 1, "Caisse fédérale de compensation CFC"),
    CCC_27(27, "Caisse suisse de compensation CSC"),
    // Caisses professionnelles
    CCP_28(28, "medisuisse"),
    CCP_30(30, "Imorek"),
    CCP_31(31, "Coop"),
    CCP_32(32, "Ostschweizerische Ausgleichskasse für Handel und Industrie"),
    CCP_33(33, "MOBIL"),
    CCP_34(34, "AVS des Bouchers"),
    CCP_35(35, "scienceINDUSTRIES"),
    CCP_37(37, "Centrales Suisse d'Electricité"),
    CCP_38(38, "PANVICA"),
    CCP_40(40, "employeurs Bâle"),
    CCP_43(43, "Verom"),
    CCP_44(44, "Hotela"),
    CCP_46(46, "GastroSocial"),
    CCP_463(46, 3, "GastroSocial;Tessin"),
    CCP_48(48, "Aargauische Industrie- und Handelskammer"),
    CCP_51(51, "Industrie Horlogère;Siège central"),
    CCP_5110(51, 10, "Industrie Horlogère;Agence 51.10 La Chaux-de-Fonds"),
    CCP_513(51, 3, "Industrie Horlogère;Agence 51.3 Genève"),
    CCP_514(51, 4, "Industrie Horlogère;Agence 51.4 Bienne"),
    CCP_515(51, 5, "Industrie Horlogère;Agence 51.5 Granges"),
    CCP_517(51, 7, "Industrie Horlogère;Agence 51.7 Granges"),
    CCP_55(55, "Thurgauer Gewerbeverband"),
    CCP_59(59, "CICICAM CINALFA"),
    CCP_60(60, "Swissmem"),
    CCP_61(61, "NODE AVS"),
    CCP_63(63, "Patrons Bernois"),
    CCP_65(65, "Zürcher Arbeitgeber"),
    CCP_66(66, "Société des Entrepreneurs"),
    CCP_661(66, 1, "Société des Entrepreneurs;Agence Vaud"),
    CCP_662(66, 2, "Société des Entrepreneurs;Agence Genève"),
    CCP_663(66, 3, "Société des Entrepreneurs;Agence Tessin"),
    CCP_69(69, "Transport"),
    CCP_70(70, "Migros"),
    CCP_71(71, "Commerce Suisse"),
    CCP_74(74, "Albicolac"),
    CCP_78(78, "Economie laitière"),
    CCP_79(79, "Spida"),
    CCP_81(81, "Assurance"),
    CCP_87(87, "Bündner / Glarner Gewerbe"),
    CCP_88(88, "SCHULESTA"),
    CCP_89(89, "Banques suisses"),
    CCP_95(95, "Exfour"),
    CCP_98(98, "Horticulteurs et fleuristes"),
    CCP_99(99, "PROMEA"),
    CCP_103(103, "AGRAPI"),
    CCP_104(104, "Menuisier"),
    CCP_1041(104, 1, "Menuisier;Agence Tessin"),
    CCP_105(105, "Arts et métiers Suisses"),
    CCP_106(106, "FER CIAV"),
    CCP_1061(106, 1, "FER CIAM"),
    CCP_1062(106, 2, "FER CIFA"),
    CCP_1063(106, 3, "FER CIGA"),
    CCP_1064(106, 4, "FER CIAN"),
    CCP_1065(106, 5, "FER CIAB"),
    CCP_1067(106, 7, "FER Valais"),
    CCP_107(107, "Commerçants bernois"),
    CCP_109(109, "Chambre vaudoise du commerce et de l'industrie (CVCI)"),
    CCP_110(110, "Caisse AVS de la Fédération patronale vaudoise"),
    CCP_111(111, "Meroba"),
    CCP_1111(111, 1, "Meroba;Agence de Lausanne"),
    CCP_1112(111, 2, "Meroba;Agence Sion"),
    CCP_112(112, "Gewerbe St. Gallen"),
    CCP_113(113, "Coiffure & Esthétique"),
    CCP_114(114, "Wirtschaftskammer Baselland"),
    CCP_115(115, "Cliniques privées"),
    CCP_116(116, "Agrivit"),
    CCP_117(117, "swisstempcomp"),
    // EL_Offices (Delivery Office)
    EL_401(401, "Zürich"),
    EL_402(402, "Bern"),
    EL_403(403, "Luzern"),
    EL_404(404, "Uri"),
    EL_405(405, "Schwyz"),
    EL_406(406, "Obwalden"),
    EL_407(407, "Nidwalden"),
    EL_408(408, "Glarus"),
    EL_409(409, "Zug"),
    EL_410(410, "Freiburg"),
    EL_411(411, "Solothurn"),
    EL_412(412, "Basel-Stadt"),
    EL_413(413, "Basel-Land"),
    EL_414(414, "Schaffhausen"),
    EL_415(415, "Appenzell A. Rh"),
    EL_416(416, "Appenzell I. Rh"),
    EL_417(417, "St. Gallen"),
    EL_418(418, "Graubünden"),
    EL_419(419, "Aargau"),
    EL_420(420, "Thurgau"),
    EL_421(421, "Tessing"),
    EL_422(422, "Waadt"),
    EL_423(423, "Wallis"),
    EL_424(424, "Neuenburg"),
    EL_425(425, "Genf"),
    EL_450(450, "Jura"),
    // Default
    NONE(999, "Default");

    private Integer officeNumber;
    private Integer agenceNumber;
    private String officeLongName;

    private RpcPlausiCaissesCompensation(Integer officeNumber, String officeLongName) {
        this(officeNumber, 0, officeLongName);
    }

    private RpcPlausiCaissesCompensation(Integer officeNumber, Integer agenceNumber, String officeLongName) {
        this.officeNumber = officeNumber;
        this.agenceNumber = agenceNumber;
        this.officeLongName = officeLongName;
    }

    public static RpcPlausiCaissesCompensation parseCaisse(Integer officeNumber, Integer agenceNumber) {
        for (RpcPlausiCaissesCompensation element : RpcPlausiCaissesCompensation.class.getEnumConstants()) {
            if (element.officeNumber.equals(officeNumber) && element.agenceNumber.equals(agenceNumber)) {
                return element;
            }
        }
        return null;
    }
}