periodes = [];

try {
    JOUR_SUPPLEMENTAIRE;
} catch (e) {
    JOUR_SUPPLEMENTAIRE = false;
}

Periode = function (dateDeDebut, dateDeFin, nbJour, tauxImposition, cantonImposition, cantonImpositionLibelle) {
    this.dateDeDebut = dateDeDebut;
    this.dateDeFin = dateDeFin;
    this.nbJour = nbJour;
    this.tauxImposition = tauxImposition;
    this.cantonImposition = cantonImposition;
    this.cantonImpositionLibelle = cantonImpositionLibelle;
    this.jourSupplementaire = '';

    this.getDateDeDebut = function () {
        return this.dateDeDebut;
    };
    this.getDateDeFin = function () {
        return this.dateDeFin;
    };
    this.getNbJour = function () {
        return this.nbJour;
    };
    this.getTauxImposition = function () {
        return this.tauxImposition;
    };
    this.getCantonImposition = function () {
        return this.cantonImposition;
    };
    this.getCantonImpositionLibelle = function () {
        return this.cantonImpositionLibelle;
    };
    this.toString = function () {
        if (this.jourSupplementaire) {
            return this.dateDeDebut + "-" + this.dateDeFin + "-" + this.nbJour + "-" + this.tauxImposition + "-" + this.cantonImposition + "-" + this.jourSupplementaire;
        }
        return this.getDateDeDebut() + "-" + this.getDateDeFin() + "-" + this.getNbJour() + "-" + this.getTauxImposition() + "-" + this.getCantonImposition();
    }
    this.toJson = function () {
        if (this.jourSupplementaire) {
            return {
                jourSupplementaire: this.jourSupplementaire,
                dateDeDebut: this.dateDeDebut,
                dateDeFin: this.dateDeFin,
                nbJour: this.nbJour,
                tauxImposition: this.tauxImposition,
                cantonImposition: this.cantonImposition
            };
        }
        return {
            dateDeDebut: this.dateDeDebut,
            dateDeFin: this.dateDeFin,
            nbJour: this.nbJour,
            tauxImposition: this.tauxImposition,
            cantonImposition: this.cantonImposition
        };
    };
}

function addPeriode() {
    var dateDebut = $('#dateDebutPeriode').val();
    var dateFin = $('#dateFinPeriode').val();
    var nbJour = $('#nbJour').val();
    var tauxImposition = "";
    var cantonImposition = "";
    var cantonImpositionLibelle = "";
    var isError = checkDateDebutAPG(dateDebut);
    var $jourSupplementaire = $('#jourSupplementaire');

    if (document.getElementById("isSoumisCotisation").checked) {
        tauxImposition = $('#tauxImpotSource').val();
        cantonImposition = $('#csCantonDomicileAffiche').val();
        cantonImpositionLibelle = $('#csCantonDomicileAffiche').children("option:selected").text();
        document.getElementById("isSoumisCotisation").checked = false;
    }
    var dateBegin = new Date(dateDebut.split('.')[2], dateDebut.split('.')[1] - 1, dateDebut.split('.')[0]);
    var dateEnd = new Date(dateFin.split('.')[2], dateFin.split('.')[1] - 1, dateFin.split('.')[0]);

    nbJour = Math.round(Math.abs((dateBegin - dateEnd) / (24 * 60 * 60 * 1000))) + 1;

    if (JOUR_SUPPLEMENTAIRE && $jourSupplementaire.val()) {
        nbJour = nbJour + ($jourSupplementaire.val() * 1)
    }


    if (isAjoutdePeriodeAuthorise(dateDebut, dateFin, nbJour, true) && !isError) {
        addPeriodeToTable(dateDebut, dateFin, nbJour, tauxImposition, cantonImposition, cantonImpositionLibelle, $jourSupplementaire.val());
        $('#dateDebutPeriode').val("");
        $('#dateFinPeriode').val("");
        $('#nbJour').val("");
        $jourSupplementaire.val("");
        $('#tauxImpotSource').val(0.00);
        $('#csCantonDomicileAffiche').val("");
        $('#isSoumisCotisation').prop("disabled", true);
        $('#tauxImpotSource').prop("disabled", true);
    }
}

function deletePeriode(index) {
    periodes.splice(index, 1);
    repaintTablePeriodes();
}

function editPeriode(index) {
    var periode = periodes[index];
    periodes.splice(index, 1);
    repaintTablePeriodes();
    $('#dateDebutPeriode').val(periode.getDateDeDebut());
    $('#dateFinPeriode').val(periode.getDateDeFin());
    $('#nbJour').val(periode.getNbJour());
    $('#tauxImpotSource').val(periode.getTauxImposition());
    $('#csCantonDomicileAffiche').val(periode.getCantonImposition());
    if (periode.jourSupplementaire) {
        $('#jourSupplementaire').val(periode.jourSupplementaire);
    }
    if (periode.getCantonImposition() != '' && periode.getCantonImposition() != '0') {
        document.getElementById("isSoumisCotisation").checked = true;
    }
    $('#isSoumisCotisation').prop("disabled", false);
    $('#tauxImpotSource').prop("disabled", false);
    showCantonImpotSource();
}


function addPeriodeToTable(dateDebut, dateFin, nbJour, tauxImposition, cantonImposition, cantonImpositionLibelle, jourSupplementaire) {
    if (isAjoutdePeriodeAuthorise(dateDebut, dateFin, nbJour, false)) {
        var periode = new Periode(dateDebut, dateFin, nbJour, tauxImposition, cantonImposition, cantonImpositionLibelle);
        if (jourSupplementaire) {
            periode.jourSupplementaire = jourSupplementaire;
        }
        periodes.push(periode);
    }
    repaintTablePeriodes();
    showCantonImpotSource();
}

function repaintTablePeriodes() {
    $('#periodes tbody > tr').remove();
    for (var ctr = 0; ctr < periodes.length; ctr++) {
        var periode = periodes[ctr];
        var width = "30%";
        if (JOUR_SUPPLEMENTAIRE) {
            width = "25%";
        }
        var ddd = '<td width="' + width + '" align="center">' + periode.getDateDeDebut() + '</td>';
        var ddf = '<td width="' + width + '" align="center">' + periode.getDateDeFin() + '</td>';
        var jsp = '';
        if (JOUR_SUPPLEMENTAIRE) {
            jsp = '<td width="10%" align="center">' + periode.jourSupplementaire + '</td>';
        }
        var njg = '<td width="10%" align="center" class="nbJourPourUnePeriode">' + periode.getNbJour() + '</td>';
        if (periode.getCantonImposition() == 0) {
            var tis = '<td width="10%" align="center"></td>';
            var cis = '<td width="10%" align="center"></td>';
        } else {
            var tis = '<td width="10%" align="center">' + periode.getTauxImposition() + '</td>';
            var cis = '<td width="10%" align="center">' + periode.getCantonImpositionLibelle() + '</td>';
        }
        var height = '12px';
        var spacer = '<span style="width:6px; heigth:10px"></span>'
        if (EDITION_MODE) {
            var deleteBtn = '<td width="10%" align="center"><a onclick="deletePeriode(' + ctr + ')"><img src="images/small_error.png" height="' + height + '" width="12px" alt="delete" /></a>';
            var editBtn = '<a onclick="editPeriode(' + ctr + ')"><img src="images/edit_pen.png" height="' + height + '" width="12px" alt="edition" /></a></td>';
        } else {
            var deleteBtn = '<td width="10%" align="center">';
            var editBtn = '</td>';
        }
        var html = '<tr>' + ddd + ddf + jsp + njg + tis + cis + deleteBtn + spacer + editBtn + '</tr>';
        $('#periodes tbody').append(html);
    }
}

function isAjoutdePeriodeAuthorise(dateDebut, dateFin, nbJour, displayMessage) {
    var result = true;
    if (!dateDebut) {
        if (displayMessage) {
            showErrorMessage("La date de début doit être renseignée");
        }
        result = false;
    }
    if (dateFin && !isSameYearPeriode(dateDebut, dateFin)) {
        if (displayMessage) {
            showErrorMessage("Les périodes doivent être sur la même année");
        }
        result = false;
    }
    if (!dateFin && nbJour) {
        if (displayMessage) {
            showErrorMessage("La date de fin doit être renseignée pour saisir le nombre de jours");
        }
        result = false;
    }
    return result;
}

function showErrorMessage(message) {
    var html = '<div>';
    html += message;
    html += '</div>';

    $html = $(html);
    $html.dialog({
        position: 'center',
        title: "Erreur",
        width: 400,
        height: 50,
        show: "blind",
        hide: "blind",
        closeOnEscape: true,
        buttons: {'Close': popupClose}
    });
}

function popupClose() {
    $html.dialog("close");
}

function isSameYearPeriode(dateDebut, dateFin) {
    return dateDebut.substr(6) == dateFin.substr(6);
}
