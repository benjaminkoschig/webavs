package ch.globaz.eavs.utils;

import ch.globaz.eavs.exception.EAVSNoVersionNumberException;

public class EAVSUtils {
    public static String getNomClasse(Class maClass) {
        String nomClasseAvecPackage = maClass.getName();
        String nomPackage = maClass.getPackage().getName();
        nomClasseAvecPackage = nomClasseAvecPackage.substring(
                nomClasseAvecPackage.lastIndexOf(nomPackage) + nomPackage.length() + 1, nomClasseAvecPackage.length());
        String premierCaractere = nomClasseAvecPackage.substring(0, 1);
        return premierCaractere.toLowerCase() + nomClasseAvecPackage.substring(1, nomClasseAvecPackage.length());
    }

    public static String getNomMethode(Class maClass) {
        String nomClasseAvecPackage = maClass.getName();
        String nomPackage = maClass.getPackage().getName();
        nomClasseAvecPackage = nomClasseAvecPackage.substring(
                nomClasseAvecPackage.lastIndexOf(nomPackage) + nomPackage.length() + 1, nomClasseAvecPackage.length());
        return nomClasseAvecPackage;
    }

    public static String getPackageVersion(Object anObject) {
        Class c = anObject.getClass();
        Package p = c.getPackage();
        String packFullName = p.getName();
        int lastIndex = packFullName.lastIndexOf(".v");
        if (lastIndex < 0) {
            throw new EAVSNoVersionNumberException("no version number for class " + c.getName());
        }
        String lastPart = packFullName.substring(lastIndex + 2);
        try {
            Integer.parseInt(lastPart);
            return lastPart;
        } catch (NumberFormatException nfe) {
            throw new EAVSNoVersionNumberException("no version number for class " + c.getName(), nfe);
        }
    }
}
