periodes = [];
Periode = function (dateDeDebut, dateDeFin, nbJour, tauxImposition, cantonImposition, cantonImpositionLibelle) {
    this.dateDeDebut = dateDeDebut;
    this.dateDeFin = dateDeFin;
    this.nbJour = nbJour;
    this.tauxImposition = tauxImposition;
    this.cantonImposition = cantonImposition;
    this.cantonImpositionLibelle = cantonImpositionLibelle;

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
        return this.getDateDeDebut() + "-" + this.getDateDeFin() + "-" + this.getNbJour()+ "-" + this.getTauxImposition() + "-" + this.getCantonImposition();
    }
    this.toJson = function () {
        return {dateDeDebut: this.dateDeDebut, dateDeFin: this.dateDeFin, nbJour: this.nbJour, tauxImposition: this.tauxImposition, cantonImposition: this.cantonImposition};
    };
}

function addPeriode() {
    var dateDebut = $('#dateDebutPeriode').val();
    var dateFin = $('#dateFinPeriode').val();
    var nbJour = $('#nbJour').val();
    var tauxImposition = "";
    var cantonImposition = "";
    var cantonImpositionLibelle = "";
    if (document.getElementById("isSoumisCotisation").checked) {
        tauxImposition = $('#tauxImpotSource').val();
        cantonImposition = $('#csCantonDomicileAffiche').val();
        cantonImpositionLibelle =$('#csCantonDomicileAffiche').children("option:selected").text();
        document.getElementById("isSoumisCotisation").checked = false;
    }
    if (isAjoutdePeriodeAuthorise(dateDebut, dateFin, nbJour, true)) {
        addPeriodeToTable(dateDebut, dateFin, nbJour, tauxImposition, cantonImposition, cantonImpositionLibelle);
        $('#dateDebutPeriode').val("");
        $('#dateFinPeriode').val("");
        $('#nbJour').val("");
        $('#tauxImpotSource').val(0.00);
        $('#csCantonDomicileAffiche').val("");
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
    if (periode.getCantonImposition() != '' && periode.getCantonImposition() != '0') {
        document.getElementById("isSoumisCotisation").checked = true;
    }
    showCantonImpotSource();
}


function addPeriodeToTable(dateDebut, dateFin, nbJour, tauxImposition, cantonImposition, cantonImpositionLibelle) {
    if (isAjoutdePeriodeAuthorise(dateDebut, dateFin, nbJour, false)) {
        periodes.push(new Periode(dateDebut, dateFin, nbJour, tauxImposition, cantonImposition, cantonImpositionLibelle));
    }
    repaintTablePeriodes();
    showCantonImpotSource();
}

function repaintTablePeriodes() {
    $('#periodes tbody > tr').remove();
    for (var ctr = 0; ctr < periodes.length; ctr++) {
        var periode = periodes[ctr];
        var ddd = '<td width="30%" align="center">' + periode.getDateDeDebut() + '</td>';
        var ddf = '<td width="30%" align="center">' + periode.getDateDeFin() + '</td>';
        var njg = '<td width="10%" align="center">' + periode.getNbJour() + '</td>';
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
        var html = '<tr>' + ddd + ddf + njg + tis + cis + deleteBtn + spacer + editBtn + '</tr>';
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
