$(function () {
// ---------------------------------------------------------------------
// Jquery bind Popup Dialog
// ---------------------------------------------------------------------
    $('.editParticularite').click(function (e) {
        valueButton = $(this).val();
        $('#dialogParticularite').dialog('open');
    });

// ---------------------------------------------------------------------
// Jquery bind Popup Dialog
// ---------------------------------------------------------------------
    $('#dialogParticularite').dialog({
        autoOpen: false,
        modal: true,
        closeOnEscape: true,
        resizable: false,
        width: 640,
        minHeight: 300,
        buttons: [{
            text: SUPPRIMER,
            click: function () {
                showConfirmSuppression(function(){
                    supprimeParticularite();
                });
            }
        }, {
            text: VALIDER,
            click: function () {
                updateParticularite();
            }
        }],
        open: function () {
            findParticularite(valueButton);
            $("#iconOk").hide();
            $("#iconWait").hide();
        }
    });
});

// ---------------------------------------------------------------------
// fonction ajax du chargement de la particularité
// ---------------------------------------------------------------------
function findParticularite (id) {
    $.ajax({
        type:'GET',
        url: CONTEXT+"/naos?userAction=naos.particulariteAffiliation.particulariteAffiliationAjax.afficherAJAX&idEntity="+id,
        async: false,
        success: function(data){
            loadData(data);
        }
    });
}

// ---------------------------------------------------------------------
// Mise à jour des champs de la popup avec les donnés récupérées
// ---------------------------------------------------------------------
this.loadData = function (data) {
    $('#idParticularite')[0].value = data.viewBean.particulariteId;
    $('#particulariteDateDebut')[0].value = data.viewBean.dateDebut;
    $('#particulariteDateFin')[0].value = data.viewBean.dateFin;
    $('#particulariteValeur')[0].value = data.viewBean.champAlphanumerique;
    this.currentViewBean = data.viewBean;
    $("#particulariteCode").val(data.viewBean.particularite);
    $("#particulariteCode").attr("disabled", true );
}

// ---------------------------------------------------------------------
// fonction ajax de supression de la particularite
// ---------------------------------------------------------------------
function supprimeParticularite() {
    var parametres = this.getParametres();
    parametres.viewBean = this.currentViewBean;
    parametres.userAction = "naos.particulariteAffiliation.particulariteAffiliationAjax.supprimerAJAX";
    ajaxUtils.url = CONTEXT+"/naos";
    ajaxUtils.ajaxCustom( "naos.particulariteAffiliation.particulariteAffiliationAjax.supprimerAJAX", parametres, function (data) {
            $('#dialogParticularite').dialog("close");
            parent.document.getElementById('btnFind').click();
        }, function () {});
}

// ---------------------------------------------------------------------
// fonction ajax de mise à jour de la particularite
// ---------------------------------------------------------------------
function updateParticularite() {
    var parametres = this.getParametres();
    parametres.viewBean = this.currentViewBean;
    $("#iconOk").hide();
    $("#iconWait").show();
    ajaxUtils.url = CONTEXT+"/naos";
    ajaxUtils.ajaxCustom( "naos.particulariteAffiliation.particulariteAffiliationAjax.modifierAJAX", parametres, function (data){
            $("#iconOk").show();
            $("#iconWait").hide();
            $('#dialogParticularite').dialog("close");
            parent.document.getElementById('btnFind').click();
        }
        , function () {
        });
}

this.getParametres=function(){
    var o_map = {
        'particulariteId':$('#idParticularite')[0].value,
        'id':$('#idParticularite')[0].value,
        'dateDebut':$('#particulariteDateDebut')[0].value,
        'dateFin':$('#particulariteDateFin')[0].value,
        'champAlphanumerique':$('#particulariteValeur')[0].value,
        'affiliationId':this.currentViewBean.affiliationId,
        'particularite':this.currentViewBean.particularite
    };
    return o_map;
};

// ---------------------------------------------------------------------
// affiche la popup de confirmation de suppression
// ---------------------------------------------------------------------
showConfirmSuppression = function (callback) {
    $( "#dialog-confirm-suppression" ).dialog({
        resizable: false,
        height:200,
        width:300,
        modal: true,
        buttons: [{
            text: SUP_OUI,
            click: function() {
                callback();
               $(this).dialog("close");
            }
        },{
            text: SUP_NON,
            click: function() {
                $(this).dialog("close");
            }
        }]
    });
    $( "#dialog-confirm-suppression" ).dialog(open);
};