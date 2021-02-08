function onChangeNationalite() {
    if (isSuisse($('#nationaliteAffichee').val())) {
        $('#blocCanton').show();
    } else {
        $('#blocCanton').hide();
    }
}

function isSuisse(nationalite) {
    return nationalite === "100";
}

function createDialog(title, text) {
    return $("<div class='dialog' title='" + title + "'><p>" + text + "</p></div>")
        .dialog({
            resizable: false,
            height:220,
            modal: true,
            buttons: {
                "OK": function() {
                    $( this ).dialog( "close" );
                }
            }
        });
}
