var asnn2 = asnn2 || {};


asnn2.setupStdntListTableParsers = function (){   
	    //apply orderers to table
    
	    $("table").tablesorter({ 
	    	//This is the default text extractor for each cell.  It looks for a <span> tag
	    	//and grabs the text inside that tag and uses it to sort the data
	    	//if span tag doesn't exist, it just replaces it with an empty string
	    	textExtraction: function(node) { 
	            // extract data from markup and return it  
	            var spanNode = $("span", node);
	            if(spanNode){
	            	if($(spanNode).html()){
	            		return $(spanNode).html();
	            	}else{
	            		return "";
	            	}
	            }else{
	            	return "";
	            } 
        	},
        	
        	//This is used to disable columns for sorting.  The number represents the column
        	//you are setting	    	
	    	headers: {
        	0: { 
                // disable it by setting the property sorter to false 
                sorter: false 
            },
        	3: { 
                // disable it by setting the property sorter to false 
                sorter: false 
            },
	    	4: { 
                // disable it by setting the property sorter to false 
                sorter: false 
            }
	        }
	    });
	    
	    //this will adjust the sort images so they are right after the text
		//8px added for padding
		jQuery("th", jQuery("tr", $("table"))).each(function(){
			var spanNode = $("span", this);
			if(spanNode){
				$(this).css("background-position", ($(spanNode).width() + 8) + "px");
			}
		});	
   
	};