var nbLines = 0;
var line = "line";
$(document).ready(function(){
	$('#add').hide();
	loadMappings();
	$('#filter').val("");
	$('#filter').focus();
	$('input').live("change", function(){
		if ($(this).attr('id')=="filter"){
			loadMappings($(this).val());
		} else if ($(this).attr('id')=="addKey"){
		} else if ($(this).attr('id')=="addValue"){
		} else {
			$.ajax({
				url: './../../mapping-service',
				type: 'POST',
				dataType: 'json',
				contentType: 'application/json',
				data: "{ 'key': '"+$(this).attr('name')+"', 'value': '"+$(this).val()+"' }"
			});
		}
	});
	$('.remove').live("click", function(){
		var confirmed = confirm("Voulez-vous supprimer le mapping '"+$(this).attr('name')+"' ?");
		if (confirmed){
			$.ajax({
				url: './../../mapping-service',
				type: 'DELETE',
				dataType: 'json',
				data: "{ key: '"+ $(this).attr('name')+"' }",
				success: $('tr.'+$(this).attr('id')).remove()
			});
		}
	});
	$('.refresh').live("click", function(){
		loadMappings($('#filter').val());
	});
	$('.add').live("click", function(){
		$('#add').dialog({autoOpen: false, modal: true, width: 600, resizable: false});
		$('#addKey').val("");
		$('#addValue').val("");
		$('#add').dialog('open');
	});
	$('#addOk').live("click", function(){
		var keyVal = jQuery.trim($('#addKey').val()); 
		if (keyVal != '') {
			$.ajax({
				url: './../../mapping-service',
				type: 'PUT',
				dataType: 'json',
				contentType: 'application/json',
				data: "{ 'key': '"+keyVal+"', 'value': '"+$('#addValue').val()+"' }",
				success: function(data) {
					if (data.response=='ok') {
						$('#addKey').val('');
						$('#addValue').val('');
						loadMappings();
						$('#addKey').focus().select();
					} else {
						alert(data.message);
					}
				}
			});
		}
	});
	$('#addCancel').live("click", function(){
		$('#add').dialog('close');
		loadMappings();
	});
});
function loadMappings(theFilter){
		$.getJSON('./../../mapping-service', { filter: theFilter }, function(data) {
		var htmlMappings = "";
		nbLines=0;
 		$.each(data, function(i, item){
 			nbLines = nbLines+1;
 			htmlMappings = htmlMappings+'<tr class="line'+nbLines+'"><td><a class="remove line'+nbLines+'" id="line'+nbLines+'" name="'+item.key+'" style="cursor: pointer;"><img src="images/delete.jpg" width="12px" style="vertical-align: bottom"/></a>'+item.key+'</td><td><input class="input line'+nbLines+'" id="line'+i+'" name="'+item.key+'" value="'+item.value+'" style="width: 98%"/></td><tr>';
 		});
 		$("#mappings").html(htmlMappings);
	});
}
