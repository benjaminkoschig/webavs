class LoadNoga {
    public static String catGroupeName = "VENOGACAT";
    public static String cosBase = "insert into "
            + LoadNoga.cosTable
            + " (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise,pspy) values ( ";
    public static String cosCatMiddle = ", '" + LoadNoga.catGroupeName + "', 1 ,2,0,0, '";
    public static String cosEnd = "', 2,2,2,2,2,2,";
    public static String cosSousCatMiddle = ", '" + LoadNoga.sousCatGroupeName + "', 1 ,2,0,0, '";
    public static String cosTable = LoadNoga.schema + "FWCOSP";
    public static String couBase = "insert into " + LoadNoga.couTable + " (pcosid,plaide,pcouid,pcolut,pspy) values ( ";
    public static String couMiddle = ", 'F', '";
    public static String couTable = LoadNoga.schema + "FWCOUP";
    public static String destInsert = "d:/temp/insertNoga.sql";
    public static String rangeCatCS = "38";
    public static String rangeSousCatCS = "39";
    public static String schema = "WEBAVSI.";
    public static String sepCat = " ";
    public static String source = "d:/temp/noga.txt";
    public static String sousCatGroupeName = "VENOGAVAL";

    public static String cutAndFormatLabel(String label, int size) {
        String result = "";
        if (label != null) {
            result = label;
            if (label.indexOf("'") > 0) {
                StringBuffer buf = new StringBuffer(label);
                // System.out.println("FORMAT ->>>>>"+label);
                int pos = 0;
                while (pos < buf.length()) {
                    if (buf.charAt(pos) == '\'') {
                        pos++;
                        buf.insert(pos, '\'');
                    }
                    pos++;
                }
                result = buf.toString();
            }
            if (result.length() > size) {
                result = result.substring(0, size);
                if (size == 60) {
                    System.out.println("CUT ->>>> " + result.substring(0, size));
                }
                if (result.charAt(result.length() - 1) == '\'') {
                    System.out.println("TO CHANGE ----> " + result);
                    result = result.substring(0, result.length() - 2) + "  ";
                    System.out.println("CHANGED ----> " + result);
                }
            }
        }
        return result;
    }

    /**
     * Lance l'application.
     * 
     * @param args
     *            un tableau d'arguments de ligne de commande
     */
    public static void main(java.lang.String[] args) {
        // Insérez ici le code de démarrage de l'application

        try {
            // inti
            java.io.BufferedReader fileIn = new java.io.BufferedReader(new java.io.FileReader(LoadNoga.source));
            java.io.FileWriter fileOut = new java.io.FileWriter(LoadNoga.destInsert);
            String line = fileIn.readLine();
            int lineCount = 0;
            int catCount = 0;
            int sousCatCount = 0;
            int sousCatId = Integer.valueOf("8" + LoadNoga.rangeSousCatCS + "000").intValue();
            int catId = Integer.valueOf("8" + LoadNoga.rangeCatCS + "000").intValue();
            String catSousCatGroupeId = "108000" + LoadNoga.rangeSousCatCS;
            String catCatGroupeId = "108000" + LoadNoga.rangeCatCS;
            String cos = "";
            String cou = "";
            // création des groupes
            cos = LoadNoga.cosBase + catCatGroupeId + ", '" + LoadNoga.catGroupeName
                    + "',0,1,3,0,'Catégorie NOGA',0,2,2,2,2,0,0,0,'');";
            cou = LoadNoga.couBase + catCatGroupeId + ",'F','','Catégorie NOGA','');";
            // ajout dans le fichier sortie
            fileOut.write(cos + "\n");
            fileOut.write(cou + "\n");
            cos = LoadNoga.cosBase + catSousCatGroupeId + ", '" + LoadNoga.sousCatGroupeName
                    + "',0,1,3,0,'Sous-catégorie NOGA',0,2,2,2,2,0,0,0,'');";
            cou = LoadNoga.couBase + catSousCatGroupeId + ",'F','','Sous-catégorie NOGA','');";
            // ajout dans le fichier sortie
            fileOut.write(cos + "\n");
            fileOut.write(cou + "\n");
            // boucle sur les lignes du fichier source
            boolean ignoreLine = false;
            while (line != null) {
                int sep = line.indexOf(LoadNoga.sepCat);
                if (sep != -1) {
                    ignoreLine = false;
                    String code = line.substring(0, sep);
                    String libelle = line.substring(sep + 1);
                    libelle = libelle.replace('\t', ' ');
                    // System.out.println("code [" + code + "] libelle [" +
                    // libelle + "]");
                    // lecture du premier char pour séparation catégorie et
                    // sous-catégorie
                    if (Character.isDigit(code.charAt(0))) {
                        // sous catégorie
                        if (!Character.isDigit(code.charAt(code.length() - 1))) {
                            sousCatId++;
                            cos = LoadNoga.cosBase + String.valueOf(sousCatId) + LoadNoga.cosSousCatMiddle
                                    + LoadNoga.cutAndFormatLabel(libelle, 40) + LoadNoga.cosEnd + catSousCatGroupeId
                                    + "," + catId + ",'');";
                            cou = LoadNoga.couBase + String.valueOf(sousCatId) + LoadNoga.couMiddle + code + "','"
                                    + LoadNoga.cutAndFormatLabel(libelle, 60) + "' ,'');";
                            sousCatCount++;
                        } else {
                            ignoreLine = true;
                        }
                    } else {
                        // nouvelle catégorie
                        catId++;
                        cos = LoadNoga.cosBase + String.valueOf(catId) + LoadNoga.cosCatMiddle
                                + LoadNoga.cutAndFormatLabel(libelle, 40) + LoadNoga.cosEnd + catCatGroupeId
                                + ",0,'');";
                        cou = LoadNoga.couBase + String.valueOf(catId) + LoadNoga.couMiddle + code + "','"
                                + LoadNoga.cutAndFormatLabel(libelle, 60) + "' ,'');";
                        catCount++;
                    }
                    if (!ignoreLine) {
                        // ajout dans le fichier sortie
                        fileOut.write(cos + "\n");
                        fileOut.write(cou + "\n");
                        // System.out.println(cos);
                        // System.out.println(cou);
                        lineCount++;
                    }
                }
                // lecture prochaine ligne
                line = fileIn.readLine();
            }
            System.out
                    .println(lineCount + " lines converted, [" + catCount + " groups, " + sousCatCount + " elements]");
            fileIn.close();
            fileOut.close();
        } catch (Exception x) {
            x.printStackTrace();
        }
        System.exit(0);
    }

    /**
     * Commentaire relatif au constructeur ConvertReprise.
     */
    public LoadNoga() {
        super();
    }
}
