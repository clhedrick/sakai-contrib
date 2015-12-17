function showEmail(beforeAt, afterAt)
{
	return beforeAt + "@" + afterAt;
}

var starOn = new Image();
starOn.src = "${contextPath}/templates/${templateName}/images/star_on.gif";

var starOff = new Image();
starOff.src = "${contextPath}/templates/${templateName}/images/star_off.gif";

function writeStars(q, postId)
{
	for (var i = 0; i < 5; i++) {
		var name = "star" + postId + "_" + i;
		document.write("<img name='" + name + "'>");
		document.images[name].src = q > i ? starOn.src : starOff.src;
	}
}

function addBookmark(relationType, relationId)
{
	var w = window.open('${JForumContext.encodeURL("/bookmarks/insert/' + relationType + '/' + relationId + '")}', 'bookmark_add', 'width=700, height=180, scrollbars=no');
	w.focus();
}

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