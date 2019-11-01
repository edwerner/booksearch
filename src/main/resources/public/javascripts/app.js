(function(){
    document.addEventListener('DOMContentLoaded', function () {
		var book = document.getElementsByClassName("book");
		book.onclick = function() {
			alert("hello world");
		}
		$("#results-term").text(getUrlParameter("term"));
	});
	
	$(window).on('load', function() {
	    $( "#exampleModalCenter" ).animate({
		  opacity: 1,
		  top: "34%"
		}, 2000).removeClass("fade");
	});
	
	function getUrlParameter(name) {
    	name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    	var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
	    var results = regex.exec(location.search);
	    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
	};
	
})();