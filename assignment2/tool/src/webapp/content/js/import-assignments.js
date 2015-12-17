var asnn2 = asnn2 || {};
asnn2.importAssignments = asnn2.importAssignments || {};


asnn2.importAssignments.toggleImportButton = function(){
	var anyChecked = false;
	$(".importToggle").each(function(){
			if(this.checked){
				anyChecked = true;				
			}
		});
	
	if(anyChecked){
		$(".importButton").removeAttr('disabled');
	}else{
		$(".importButton").attr('disabled', 'disabled');
	}
};