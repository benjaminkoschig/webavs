package ch.globaz.pegasus.process.statistiquesOFAS.step1;

import globaz.pyxis.constantes.IConstantes;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String ofs = TIUtil.getCodeCantonOFS(IConstantes.CS_LOCALITE_CANTON_SOLEURE);
        System.out.print(ofs);
    }
}
