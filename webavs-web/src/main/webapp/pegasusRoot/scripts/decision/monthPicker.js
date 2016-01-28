	
function initMonthPickerCalendar(){
	
	//****************** HTML **************************
	var divMonthCalendarStr = "<div id='mainDiv'><div id='ui-datepicker-div' class=''>";
	divMonthCalendarStr += "<div><table id='monthCalTable' class='ui-datepicker-calendar'>";
	divMonthCalendarStr += "<tr><td month='01' class='ui-state-default'>Janvier</td><td month='02' class='ui-state-default'>Février</td></tr>";
	divMonthCalendarStr += "<tr><td month='03' class='ui-state-default'>Mars</td><td month='04' class='ui-state-default'>Avril</td></tr>";
	divMonthCalendarStr += "<tr><td month='05' class='ui-state-default'>Mai</td><td month='06' class='ui-state-default'>Juin</td></tr>";
	divMonthCalendarStr += "<tr><td month='07' class='ui-state-default'>Jullet</td><td month='08' class='ui-state-default'>Aout</td></tr>";
	divMonthCalendarStr += "<tr><td month='09' class='ui-state-default'>Septembre</td><td month='10' class='ui-state-default'>Octobre</td></tr>";
	divMonthCalendarStr += "<tr><td month='11' class='ui-state-default'>Novembre</td><td month='12' class='ui-state-default'>Décembre</td></tr>";
	divMonthCalendarStr += "</table></div></div></div>";
	
	//Ajout de l''image
	$('<img id="btnOpenCal" src="/webavs/images/calendar.gif" />').insertAfter('#monthpicker');
	
	$(divMonthCalendarStr).insertAfter('#btnOpenCal').dialog({
			autoOpen: false,
			resizable:false,
			draggable:false,
			width:150,
			title: '<div class="ui-datepicker-header_mc"><a class="ui-datepicker-prev ui-corner-all"><span id="btnDecYear" class="ui-icon ui-icon-circle-triangle-w"></span></a><span id="lblYear" class="ui-datepicker-year">2010</span><a><span id="btnIncYear" class="ui-icon ui-icon-circle-triangle-e"></span></a><div class="ui-datepicker-title"></div></div>'
	});
	//****************** Fin HTML **************************
	
	//************* Styles ***************************
	$('.ui-datepicker-header_mc').css({
		 'width': '150px', 
		 'height': '15px',
		 'font-family' : 'Verdana,Arial,sans-serif',
		 'font-size' : '15px',
		 'font-weight' : 'normal',
		 'cursor' : 'default'
	});
	$('.ui-datepicker td').css({
		'font-size' :' 10px'
	});
	$('.ui-dialog-titlebar-close').css({
		'display' : 'none'
	});
	$('#btnDecYear').css({
		'position' : 'absolute',
		'top' : '8px',
		'left' : '20px'
	});
	$('#btnIncYear').css({
		'position' : 'absolute',
		'top' : '8px',
		'left' : '135px'
	});
	$('#lblYear').css({
		'position': 'absolute',
		'top' :'8px',
		'left':'60px'
	});
	//************* Fin Styles ***************************
	
	//****************** Events **************************
	//images ouverture calendrier
	$('#btnOpenCal').click(function() {
			//position du text input
			var position = $('#monthpicker').position();
			//alert(position.left+" "+position.top);
			$('#mainDiv').dialog({ position: [position.left,position.top+22]}),
			$('#mainDiv').dialog('open')
			$('#monthpicker').focus();
			return false;
	});
	
	//events bouton avant apres
	$('#btnDecYear').click(function(){
		var year = parseInt($('#lblYear').text());
		$('#lblYear').text(year-1);
	});
	
	$('#btnIncYear').click(function(){
		var year = parseInt($('#lblYear').text());
		$('#lblYear').text(year+1);
	});
	
	//events td
	$('#monthCalTable tr td').each(function(){
		var that = this;
		$(this).click(function(){
			//alert('ok');
			//recup annee
			var year = $('#lblYear').text();
			var month = $(this).attr('month');
			var strDate = month + "." + year;
			$('#monthpicker').val(strDate);
			//Close dialog
			$('#mainDiv').dialog('close');
		});
		
		$(this).hover(function(){
			$(that).addClass('ui-state-hover');
		});
		$(this).mouseout(function(){
			
			$(that).removeClass('ui-state-hover');
		});
		
		
		
	});
	
	
	//Gestion evenement KeyDown avec Tab, affichage date today
	$("#monthpicker").keydown(function(event){
		var touche = window.event ? event.keyCode : event.which;
	    if(touche=="9"){
			if($("#monthpicker").val()=="."){
				
				date = new Date();
				var mois = date.getMonth() +1;
				var an = date.getFullYear();
				
				if(mois<10)
					mois = "0" + mois;
					
				var formatedDate = mois+"."+an;
				$("#monthpicker").val(formatedDate);
				$('#mainDiv').dialog('close');
			}else
			{
				//4 ou 6 caratere
				if($("#monthpicker").val().length==4||$("#monthpicker").val().length==6){
					
					var dateStr = $("#monthpicker").val();
					//on split en groupe de 2
					var mois = dateStr.substr(0,2);
					var an = dateStr.substr(2);
					if(an.length==2){
						if(an > 47)
							an = "19"+an;
						else
							an = "20"+an;
					}
					var formatedDate = mois+"."+an;
					$("#monthpicker").val(formatedDate);
					$('#mainDiv').dialog('close');
				}
				else{
					$("#monthpicker").val('');
					$('#mainDiv').dialog('close');
				}
			}
			
		}
	});
}