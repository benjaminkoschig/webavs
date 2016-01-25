$(document).ready(function() {		

	//
    // Select all the a tag with name equal to modal
    //
    // Action pour afficher la sélection des templates
    // -----------------------------------------------
	
	
	$('#deblocageDialog').dialog( {autoOpen: false,width: 430, height: 450, title: 'Déblocage' , modal: true,
									resizable: false,  
									beforeClose: function(event, ui) {$('.dialCalendar').dialog( "close" );
																		$("#error").html('<label style="text-align: center;color: red;">&nbsp;</label>');} } );

	$('#buttonModal').click(function(e) {
		 $("#deblocageDialog").dialog('open');	
		 
		 $('.ui-datepicker-div').height(150);
		 //alert($('.ui-datepicker-div').html());
				 //('.monthPickerClass').find('.dialCalendar').height(200);
	 });
	


	$('.close').click(function() {
		$("#deblocageDialog").dialog('close');
		$("#error").html('<label style="text-align: center;color: red;">&nbsp;</label>');
	});
	
	
	$('.btnDebloquer').click(function() {
		if($("#date").val() == ""){
			alert($("#error").html());
			$("#error").html('<label style="text-align: center;color: red;">Veuillez rentrer une date de libération</label>');
			$("#error").val('veuillez entrer une date de libération');
		} else{
			//$("#dateLiberation").val("07.1455");
			var date = $("#date").val();
			alert(date)
			actionDeblocagePC(date);
		}
	});

})



