(function(){
    document.addEventListener('DOMContentLoaded', function () {
		var book = document.getElementsByClassName("book");
		book.onclick = function() {
			alert("hello world");
		}
    });
    
	$(window).on('load', function() {
	    $( "#exampleModalCenter" ).animate({
		  opacity: 1,
		  top: "34%"
		}, 2000).removeClass("fade");
	});
})();