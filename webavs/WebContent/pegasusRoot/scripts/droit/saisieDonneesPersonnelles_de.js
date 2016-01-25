/**************************************************************
 * Script des comportements de l'écran pret envers les tiers
 * 
 * ECO
 */

function add() {}
function upd() {}	
function validate() {}    
function del() {}
function cancel() {}

function init(){}
function postInit(){
	// $('.btnAjax input').attr('disabled',false);	
}


var keyborad = {
		$areaTitre: {},
		n_indexSelected: null,
		$elementSelected:null,
		b_ctrl: false, 
		
		init: function ($html){
			this.$areaTitre = $(".areaTitre");
			this.addEvent($html);
		},
		
		findNextIndex: function (event,n_currentIndex,$container,b_loop){
			return this.findNextIndex0(event, n_currentIndex, $container,b_loop,[38,40]);
		},

		findNextIndex0: function (event,n_currentIndex,$container,b_loop,t_touche){
			var n_index = 0, 
			    n_maxIndex = $container.length - 1;
			b_loop = (typeof b_loop === 'undefined')?true:b_loop; 
			if(event.which === t_touche[0] ){ 
				if((n_currentIndex=== 0 ||n_currentIndex === null) && b_loop){
					n_index = n_maxIndex;
				}else{
					n_index = n_currentIndex - 1;
				}
			}else if(event.which === t_touche[1]){
				if((n_currentIndex === n_maxIndex || n_currentIndex === null ) && b_loop){
					n_index = 0;
				}else{
					n_index = n_currentIndex + 1;
				}
			}
			return n_index;
		},
		
		setMasterKey: function (b_actived,event){
			if(17 === event.which){
				this.b_ctrl = b_actived;
			}
		},
		
		bindZone: function (event){
			var n_indexTable = 0; 
			if(/^(38|40)$/.test(event.which)){
				if(	this.$elementSelected !== null /*&& this.b_autreDetail*/){
					this.$elementSelected.removeClass('ui-state-hover');
				}
				this.n_indexSelected = this.findNextIndex(event,this.n_indexSelected,this.$areaTitre);		
				this.$elementSelected = $(this.$areaTitre.get(this.n_indexSelected));
				this.$elementSelected.addClass('ui-state-hover');
				this.$elementSelected.find('.forFocus').focus();				
			}
		},
		
		addEvent: function ($html){
			var that = this, 
			 	b_addFocus = false;
			//Bind la touche ENTER
			this.$areaTitre.keydown(function (e) {
				if(13 === e.which && that.$elementSelected !== null){
					that.$elementSelected.click();
				}
			});
			$html.keyup(function(e){	
				that.setMasterKey(false,e);
			});
			$html.keydown(function(e){
				if(!b_addFocus){
					ajaxUtils.addElementForFocus(that.$areaTitre);
					b_addFocus = true;
				}
				that.setMasterKey(true,e);
				if (that.b_ctrl){
					that.bindZone(e);
				}
			});
		}
	}

	$(function (){
		var $html = $('html');
		//$html.bind(eventConstant.AJAX_INIT_DONE,function(){
			keyborad.init($html);
		//});
	});