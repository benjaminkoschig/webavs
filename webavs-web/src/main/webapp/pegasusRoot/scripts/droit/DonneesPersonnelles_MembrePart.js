

function DonneesPersonnellesPart(container){
	// variables
	this.ACTION_AJAX=ACTION_AJAX;
	this.mainContainer=container;
	this.modifiedZoneClass="areaDFModified";
	this.currentViewBean=null;
	this.selectedEntityId=container.attr("idDonnePersonnel");
	this.idLocalite=null;

	// functions
	
	this.onRetrieve=function($data){
		this.mainContainer
				.find('.csStatusRefugieApatride').val($data.find('statusRefugie').text()).end()
				.find('.noOCC').val($data.find('noOCC').text()).end()
				.find('.noCaisseAVS').val($data.find('noCaisseAVS').text()).end()
				.find('.csPermis').val($data.find('csPermis').text()).end()
				.find('.noOfficeAI').val($data.find('noOfficeAI').text()).end()
				.find('.communeOrigine').val($data.find('communeOrigine').text()).end()
				.find('.nomDernierDomicile').val($data.find('nomDernierDomicile').text()).end()
				.find('.communeOrigine').val($data.find('communeOrigine').text()).end()
				.find('.communeOrigineCodeOfs').val($data.find('communeOrigineCodeOfs').text()).end();
		this.currentViewBean=$data.find('viewBean').text();
		this.idLocalite=$data.find('idDernierDomicile').text();
		this.addSpy($data);
	};
			
	this.getParametres=function(){
		return {
			"donneesPersonnelles.simpleDonneesPersonnelles.csStatusRefugieApatride":this.mainContainer.find('.csStatusRefugieApatride').val(),
			"donneesPersonnelles.simpleDonneesPersonnelles.noOCC":this.mainContainer.find('.noOCC').val(),
			"donneesPersonnelles.simpleDonneesPersonnelles.noCaisseAvs":this.mainContainer.find('.noCaisseAVS').val(),
			"donneesPersonnelles.simpleDonneesPersonnelles.noOfficeAi":this.mainContainer.find('.noOfficeAI').val(),
			"donneesPersonnelles.simpleDonneesPersonnelles.idDernierDomicileLegale":this.idLocalite,
			"donneesPersonnelles.simpleDonneesPersonnelles.idDonneesPersonnelles":this.selectedEntityId,
			"donneesPersonnelles.simpleDonneesPersonnelles.isRepresentantLegal":this.mainContainer.find('.representant').val(),
			"donneesPersonnelles.simpleDonneesPersonnelles.idTiersRepondant":this.mainContainer.find('.idTiersRepondant').val(),
			"donneesPersonnelles.simpleDonneesPersonnelles.csLienRepondant":this.mainContainer.find('.csLienRepondant').val(),
			"donneesPersonnelles.simpleDonneesPersonnelles.csPermis":this.mainContainer.find('.csPermis').val(),
			"donneesPersonnelles.simpleDonneesPersonnelles.communeOrigine":this.mainContainer.find('.communeOrigine').val(),
			"donneesPersonnelles.simpleDonneesPersonnelles.communeOrigineCodeOfs":this.mainContainer.find('.communeOrigineCodeOfs').val()
		};
	};
	
	this.ajaxLoadVB=function(){
		var that=this;
		$.ajax({
			data: {"userAction": this.ACTION_AJAX+".afficherAJAX",
					"idEntity":this.selectedEntityId},
			success: function(data){
						that.onLoadAjaxVBData(data);
					},
			type: "GET"			
		});		
	};
	
	this.onLoadAjaxVBData=function(data){
		var $tree=$(data);
		if($tree.find(this.XML_DETAIL_TAG).length>0){	
			this.currentViewBean=$tree.find('viewBean').text();
			this.idLocalite=$tree.find('idDernierDomicile').text();
			this.startEdition();
		} else {
			window.location.href="/webavs/pyxis?userAction=quit";
		}	
	}
	
	this.getParentViewBean=function(){
		return droit;
	};
	this.setParentViewBean=function(newViewBean){
		droit=newViewBean;
	};
	
	this.init(function (){
		this.stopEdition();
	})
	// initialization

}

//DonneesPersonnellesPart extends AbstractSimpleAJAXDetailZone
DonneesPersonnellesPart.prototype=AbstractSimpleAJAXDetailZone;


//fonction d'initialisation de la page lorsque JQuery est prêt
$(function(){
	
	$('.areaMembre').each(function(){
		
		var $that=$(this);
		var membrePart=new DonneesPersonnellesPart($that);
		var b_clicked = false;
		this.zone=membrePart;
		$that.find(".areaTitre").click(function () {
			if(!b_clicked){
				membrePart.ajaxLoadVB();
				b_clicked = true;
			}else{
				membrePart.ajaxLoadEntity();
				membrePart.stopEdition();
				b_clicked = false;
			}
		})
		$that.find('.areaDetail .btnAjax')
		.find('.btnAjaxUpdate').click(function(){
			membrePart.ajaxLoadVB();
		}).end()
		.find('.btnAjaxCancel').click(function(){
			membrePart.ajaxLoadEntity();
			membrePart.stopEdition();
		}).end()
		.find('.btnAjaxValidate').click(function(){
			membrePart.ajaxUpdateEntity();
		});	 
		ajaxUtils.afterInit($that);
	});
	
});