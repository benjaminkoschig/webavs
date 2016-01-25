var nbLines = 0;
var line = "line";
$(document).ready(function(){
	$('#add').hide();
	loadProperties();
	$('#filter').val("");
	$('#filter').focus();
	$('input').live("change", function(){
		if ($(this).attr('id')=="filter"){
			loadProperties($(this).val());
		} else if ($(this).attr('id')=="addName"){
		} else if ($(this).attr('id')=="addValue"){
		} else {
			$.ajax({
				url: './../../properties-service',
				type: 'POST',
				dataType: 'json',
				contentType: 'application/json',
				data: "{ 'name': '"+$(this).attr('name')+"', 'value': '"+$(this).val()+"' }"
			});
		}
	});
	$('.remove').live("click", function(){
		var confirmed = confirm("Voulez-vous supprimer la propriété '"+$(this).attr('name')+"' ?");
		if (confirmed){
			$.ajax({
				url: './../../properties-service',
				type: 'DELETE',
				dataType: 'json',
				data: "{ name: '"+ $(this).attr('name')+"' }",
				success: function(data) {
					$('tr.'+$(this).attr('id')).remove();
				}
			});
		}
	});
	$('.refresh').live("click", function(){
		loadProperties($('#filter').val());
	});
	$('.add').live("click", function(){
		$('#add').dialog({autoOpen: false, modal: true, width: 600, resizable: false});
		$('#addName').val("");
		$('#addValue').val("");
		$('#add').dialog('open');
	});
	$('#addOk').live("click", function(){
		var nameVal = jQuery.trim($('#addName').val()); 
		if (nameVal != '') {
			$.ajax({
				url: './../../properties-service',
				type: 'PUT',
				dataType: 'json',
				contentType: 'application/json',
				data: "{ 'name': '"+nameVal+"', 'value': '"+$('#addValue').val()+"' }",
				success: function(data) {
					if (data.response=='ok') {
						$('#addName').val('');
						$('#addValue').val('');
						loadProperties();
						$('#addName').focus().select();
					} else {
						alert(data.message);
					}
				}
			});
		}
	});
	$('#addCancel').live("click", function(){
		$('#add').dialog('close');
		loadProperties();
	});
});
function loadProperties(theFilter){
		$.getJSON('./../../properties-service', { filter: theFilter }, function(data) {
		var htmlProperties = "";
		nbLines=0;
 		$.each(data, function(i, item){
 			nbLines = nbLines+1;
 			htmlProperties = htmlProperties+'<tr class="line'+nbLines+'"><td><a class="remove line'+nbLines+'" id="line'+nbLines+'" name="'+item.name+'" style="cursor: pointer;"><img src="images/delete.jpg" width="12px" style="vertical-align: bottom"/></a>'+item.name+'</td><td><input class="input line'+nbLines+'" id="line'+i+'" name="'+item.name+'" value="'+item.value+'" style="width: 98%"/></td><tr>';
 		});
 		$("#properties").html(htmlProperties);
	});
}
