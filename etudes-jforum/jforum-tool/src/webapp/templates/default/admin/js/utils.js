function toggleElement(id) 
{
	var ele = document.getElementById(id)
	if (ele == null) return;
	
	if (ele.style.display == 'block')
		ele.style.display = 'none';
	else if (ele.style.display == 'none')
		ele.style.display = 'block';
}

function showElement(id) 
{
	var ele = document.getElementById(id)
	if (ele == null) return;

	if (ele.style.display == 'none')
	{
		ele.style.display = 'block';
	}
}

function hideElement(id) 
{
	var ele = document.getElementById(id)
	if (ele == null) return;
	
	if (ele.style.display == 'block')
		ele.style.display = 'none';
}