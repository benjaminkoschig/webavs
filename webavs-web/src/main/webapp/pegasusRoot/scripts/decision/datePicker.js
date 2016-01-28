function initDatePickerCalendar(){
	$.datepicker.setDefaults($.datepicker.regional['de']);
	//création du calendrier
	$('#datepicker').datepicker({
		showOn: 'button',
		buttonImage: '/webavs/images/calendar.gif',
		buttonImageOnly: true,
		constrainInput: false,
		dateFormat: 'dd.mm.yy'
	});
	

	//Gestion evenement KeyDown avec Tab, affichage date today
	$("#datepicker").keydown(function(event){
		var touche = window.event ? event.keyCode : event.which;
	    if(touche=="9"){
			if($("#datepicker").val()=="."){
				//alert('point TAB');
				date = new Date();
				var jour = date.getDate();
				var mois = date.getMonth() +1;
				var an = date.getFullYear();
				
				if(jour<10)
					jour = "0" + jour;
				if(mois<10)
					mois = "0" + mois;
					
				var formatedDate = jour+"."+mois+"."+an;
				$("#datepicker").val(formatedDate);
			}else
			{
				//& ou 8 caracterer format 010101 ou 01012001
				if($("#datepicker").val().length==6||$("#datepicker").val().length==8){
					//alert('6');
					var dateStr = $("#datepicker").val();
					//on split en groupe de 2
					var jour = dateStr.substr(0,2);
					var mois = dateStr.substr(2,2);
					var an = dateStr.substr(4);
					if(an.length==2){
						if(an > 47)
							an = "19"+an;
						else
							an = "20"+an;
					}
					var formatedDate = jour+"."+mois+"."+an;
					$("#datepicker").val(formatedDate);
				}
			}
		}
	});
}



