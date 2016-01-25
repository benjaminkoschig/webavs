(function($) {
		
		
		
		
		// définition du plugin jQuery
        $.fn.getObjectByValue = function( method ) {
        	
        	//propriété css prise en compte
    		var cssAllowed = {
    			height:'height',
    			width:'width'
    		};
    		
    		//methodes
    		var methods = {
    				//Fonction recherche max
    				max : function (cssToCompare) {
    					var $_return;
    					
    					if(cssAllowed[cssToCompare]){
    						var $_return;
    						var cssComparator = cssAllowed[cssToCompare];
    						$_return = returnCompareObject(this,true,cssComparator);
    					}else{
    						$_return = this;
    					}
    		        	 //chaînage jQuery
    					return $_return;
    				},
    				min : function (cssToCompare) {
    					var $_return;
    					
    					if(cssAllowed[cssToCompare]){
    						var $_return;
    						var cssComparator = cssAllowed[cssToCompare];
    						$_return = returnCompareObject(this,false,cssComparator);
    					}else{
    						$_return = this;
    					}
    		        	 //chaînage jQuery
    					return $_return;
    				}
    		};
        	
    		//retourne l'objet sortant de la comparaison en fonction des critères définis
    		var returnCompareObject = function ($_objects,isMax,comparator) {
    			
    			var $_return;
    			n_toCompare = 0;
    			if(!isMax){
    				n_toCompare = Number.MAX_VALUE;
    			}
    			
    			$_objects.each(function () {
    				var value = parseInt($(this).css(comparator).split("px")[0]);
    				if(isMax){
    					if(value > n_toCompare){
    						n_toCompare = value;
    	        			$_return = $(this);
    	        		}
    				}else{
    					if(value < n_toCompare){
    						n_toCompare = value;
    	        			$_return = $(this);
    	        		}
    				}
            	});
    			return $_return;
    		};
        	
        	if( methods[method] ){
        		return methods[ method ].apply(this,Array.prototype.slice.call( arguments, 1 ));
        	}else{
        		 $.error( 'Method ' +  method + ' does not exist on  this tooltip' );
        	}
        };
})(jQuery);

