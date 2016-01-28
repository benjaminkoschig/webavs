package ch.globaz.vulpecula.business.services.decompte;

import globaz.jade.service.provider.application.JadeCrudService;
import ch.globaz.vulpecula.business.models.decomptes.DecompteComplexModelAJAX;
import ch.globaz.vulpecula.business.models.decomptes.DecompteSearchComplexModelAJAX;

public interface DecompteServiceCRUD extends JadeCrudService<DecompteComplexModelAJAX, DecompteSearchComplexModelAJAX> {
}
