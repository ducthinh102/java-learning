
//Make sure jQuery has been loaded before app.js
if (typeof jQuery === "undefined") {
  throw new Error("jQuery is required");
}

// sidebar-toggle.
$('.sidebar-toggle').click(function(e) {
	e.preventDefault();
	$('body').toggleClass('sidebar-collapse');
	$('body').toggleClass('sidebar-open');
});
$('.content-wrapper').click(function() {
	if($(window).width() <= 767 && $('body').hasClass('sidebar-open')) {
		$('body').removeClass('sidebar-open');
	}
});
