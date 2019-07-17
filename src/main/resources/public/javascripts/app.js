(function(){
    document.addEventListener('DOMContentLoaded', function () {
		var book = document.getElementsByClassName("book");
		book.onclick = function() {
			alert("hello world");
		}
    });
})();