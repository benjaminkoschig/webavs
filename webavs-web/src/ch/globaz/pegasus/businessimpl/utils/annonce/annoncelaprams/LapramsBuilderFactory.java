package ch.globaz.pegasus.businessimpl.utils.annonce.annoncelaprams;

import ch.globaz.pegasus.businessimpl.utils.annonce.annoncelaprams.builder.ILapramsBuilder;
import ch.globaz.pegasus.businessimpl.utils.annonce.annoncelaprams.builder.LapramsTextBuilder;

public final class LapramsBuilderFactory {

    public static ILapramsBuilder getTextBuilder() {
        return new LapramsTextBuilder();
    }

}
