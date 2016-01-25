var nbLines = 0;
var line = "line";
$(document).ready(function(){

	loadParameters("");
	$('#filter').val("");
	$('#filter').focus();
	$('input').live("change", function(){
		if ($(this).attr('id')=="filter"){
			loadParameters($(this).val());
		} else {
		
			var idParam = '';
			var validityStart = '';
			var valNum = '';
			var valAlpha = '';
			
			//on r�cup�re les valeurs des inputs se situant dans la m�me ligne que celui o� on a cliqu� (<tr>)
			$(this).parents("tr").find("input").each(function(index){
		      	
		        if($(this).attr('id').indexOf('id_param')!=-1){       	
		        	idParam = $(this).val();
		        }
		        
		        if($(this).attr('id').indexOf('param_validity')!=-1){
		        	validityStart = $(this).val();
		        }
		        
		        if($(this).attr('id').indexOf('param_val_num')!=-1){      	
		        	valNum = $(this).val();
		        }
		        
		        if($(this).attr('id').indexOf('param_val_alpha')!=-1){       	
		        	valAlpha = $(this).val();
		        	//HACK car JadeApplicationServiceReflection#getParametersForInvoke inteprete le "true"/"false"
		        	//comme du boolean, et ne r�cup�re pas la valeur si la signature de la m�thode appel�e n'a pas de boolean pour le param�tre
		        	//du coup => valAlpha=null dans la m�thode appel�e
		        	
		        	//on ajoute donc $ => $true et on l'env�le ensuite dans la m�thode appel�e
		        	if(valAlpha=='true' || valAlpha=='false'){
		        		valAlpha = "$".concat(valAlpha);
		        	}
		        }
		    });
			
			//alert('update param will be called with following args:'+idParam+','+validityStart+','+valAlpha+','+valNum);
			
			var parametre = {
					userAction: "widget.action.jade.afficher",
					serviceClassName: 'ch.globaz.param.business.service.ParameterModelService',
					serviceMethodName: 'updateValidityAndValues',
					initThreadContext: 'undefined',
					cursor: 5,
					criterias: '',
					cstCriterias: '',
					modelReturnVariables: '',
					searchText: idParam+','+validityStart+','+valAlpha+','+valNum,
					parametres: idParam+','+validityStart+','+valAlpha+','+valNum,
					forceParametres : 'undefined',
					noCache: (new Date()).getMilliseconds()
			};
			
			$.ajax({
				url: './../../al',
				dataType: "json",
				contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
				data: parametre,
				success: function (data) {
					if(ajaxUtils.hasError(data)){
						alert('mise � jour impossible');
					}else{
						alert('param�tre mis � jour');
					}			
				},
				type: "GET"
			});
	
		}
	});
	
	$('.refresh').live("click", function(){
		loadParameters($('#filter').val());
	});
	
	
});
function loadParameters(theFilter){

	$.ajax({
		 type:'GET',
	     url:'./../../al'+'?userAction=al.ajax.parametresMetier.lister&parameterSearchModel.forIdCleDiffere='+theFilter,
	     async: false,
	     beforeSend: function(x) {
	      if(x && x.overrideMimeType) {
	       x.overrideMimeType("application/j-son;charset=UTF-8");
	      }
	 },
	 dataType: "json",
	 success: function(data){
		 
			var htmlProperties = "";
			nbLines=0;
	
			for(var i=0;i<data.search.results.length;i++){
				nbLines = nbLines+1;
			
				htmlProperties += '<tr id="toto'+nbLines+'" class="line'+nbLines+'">';
				htmlProperties += '<td>'+data.search.results[i].param_nom+'</td>';
				htmlProperties += '<td><input type="hidden" id="id_param_'+i+'" value="'+data.search.results[i].id_param+'"/><input id="param_validity_'+i+'" class="input line'+nbLines+'"';
				htmlProperties += 'value="'+data.search.results[i].param_debut_validite+'" style="width: 90%"/></td>';
				htmlProperties += '<td><input id="param_val_num_'+i+'" class="input line'+nbLines+'" value="'+data.search.results[i].param_val_num+'"';
				htmlProperties += 'style="width: 90%"/></td>';
				htmlProperties += '<td><input id="param_val_alpha_'+i+'"  class="input line'+nbLines+'" value="'+data.search.results[i].param_val_alpha+'"';
				htmlProperties += 'style="width: 90%"/></td>';
				htmlProperties += '<tr>';
				
			}

			$("#parameters").html(htmlProperties);
	 }
	});
	
}

function traitementErreur(data){
	ajaxUtils.displayError(data);
}

