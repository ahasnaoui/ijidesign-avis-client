//'use strict';
//afterLoad();

function initCompareCarousel() {
  $('.carousel-showmanymoveone').each(function(){
    $(this).find('.coffer').removeClass('active');
    $(this).find('.coffer .cloneditem').remove();
    var maxItems = 1;
    if (window.matchMedia("(min-width: 769px)").matches) { maxItems = 3; }
    if (window.matchMedia("(min-width: 1025px)").matches) { maxItems = 4; }
    $(this).find('.item').each(function(index, value){
      if (index == 0) {
        $(this).addClass('active');
      }
      var itemToClone = $(this);

      for (var i=1;i<maxItems;i++) {
        itemToClone = itemToClone.nextAll(".item:first");
        // wrap around if at end of item collection
        if (itemToClone.length) {
          // grab item, clone, add marker class, add to collection
          itemToClone.children(':first-child').clone()
            .addClass("cloneditem")
            .addClass("cloneditem-"+(i))
            .appendTo($(this));

          $(".cloneditem-"+(i)).find('.garantie').each(function(){
            var currenttt = $(this).find('.tt');
            var currentttname = currenttt.attr('data-name');
            var newOpentipClass = currentttname + '-cl' + i;
            currenttt.removeClass('tt-' + currentttname);
            currenttt.addClass('tt-' + newOpentipClass);
            currenttt.attr('data-name', newOpentipClass);
          });
        }
      }
    });
    if ( $(this).find('.item').length > maxItems ) {
      $(this).find('.carousel-control').show();
    }
    else {
      $(this).find('.carousel-control').hide();
      if ($(this).find('.item').length == 0) {
        $(this).parent().hide();
        $('#graphic-mode .formula-add').parent().removeClass('hidden-xs');
      }
      else {
        $(this).parent().show();
        $('#graphic-mode .formula-add').parent().addClass('hidden-xs');
      }
    }

  });

}

$(function() {

  //initTooltipForm();

  $('.check-lmde').each(function() {
    $(this).prop('checked', false);
  });

  $('.carousel-showmanymoveone').each(function(){
    if ($(this).find('.item').length == 0) {
      $(this).parent().hide();
      $('#graphic-mode .formula-add').parent().removeClass('hidden-xs');
    }
    else {
      $(this).parent().show();
      $('#graphic-mode .formula-add').parent().addClass('hidden-xs');
    }
  });

  $('#button-compare').click(function(e){
    e.preventDefault();
    if ( $('.check-lmde:checked').length == 0 ) {
      $.notify({
        title: 'Aucune offre sélectionnée.',
        message: 'Veuillez sélectionner des offres, puis comparer de nouveau.'
      },{ type: 'warning' });
    }
    else {
      var top = $('#comparer-offres').offset().top - 75;
      if (window.matchMedia("(min-width: 769px)").matches) { top = $('#comparer-offres').offset().top - 120; }
      $('body,html').animate({ scrollTop: top }, 800);
    }
    return false;
  });

  $('.formula-add').click(function(e){
    e.preventDefault();
    var top = $('#selectionner-offres').offset().top - 75;
    if (window.matchMedia("(min-width: 769px)").matches) { top = $('#selectionner-offres').offset().top - 120; }
    $('body,html').animate({ scrollTop: top }, 800);
    return false;
  });

  $('.check-lmde').change(function() {
    var offer = $(this).data('offer');
    $('#'+offer).toggleClass('item');
    $('#'+offer).toggleClass('item-hidden');
    $('#detail-'+offer).toggleClass('item');
    $('#detail-'+offer).toggleClass('item-hidden');
    initCompareCarousel();
    initTooltipText($('#detail-mode .garantie'));
  });

  $('input[name="mode"]').change(function() {
    if ( $(this).val() == "simplifie" ) {
      $('#detail-mode').fadeOut(500, function(){
        $('#graphic-mode').fadeIn(500);
      });
    }
    else {
      $('#graphic-mode').fadeOut(500, function(){
        $('#detail-mode').fadeIn(500);
      });
    }
  });

  $('.carousel-showmanymoveone').on('click', '.formula-remove a', function(e) {
    e.preventDefault();
    var offer = $(this).closest('.formula').attr('id');
    var arr = offer.split('-');
    $('.check-lmde[data-offer="offer-' + arr[1] + '"]').trigger("click");
    return false;
  });

  $('.carousel-showmanymoveone .carousel-control.right').on('click', function(e) {
    var count = $(this).closest('.carousel').find(".carousel-inner > .item.active > div").length;
    var next = $(this).closest('.carousel').find(".carousel-inner > .item.active").next(".item").find('> div');
    var countNext = next.length;
    if ( countNext < count ) {
      e.preventDefault();
      return false;
    }
  });

  $('.carousel-showmanymoveone .carousel-control.left').on('click', function(e) {
    var count = $(this).closest('.carousel').find(".carousel-inner > .item.active > div").length;
    var prev = $(this).closest('.carousel').find(".carousel-inner > .item.active").prev(".item").find('> div');
    var countPrev = prev.length;
    if ( countPrev < count ) {
      e.preventDefault();
      return false;
    }
  });
  
  
  $("input.check-lmde[type=checkbox]").click(
		function() { 
			var currentclass=$(this).attr('class');
			var noCheck = true;
			$("input.check-lmde[type=checkbox]").each(
				function() {
					if ($(this).attr('class') != currentclass) {
						$(this).closest('label').closest('div').css('opacity',0.5);
						$(this).prop('disabled', true);
						$(this).prop('checked', false);
					} else {
						$(this).closest('label').closest('div').css('opacity',1);
						$(this).prop('disabled', false);
					}
					if ($(this).prop('checked')) {
						noCheck = false;
					}
				}
			);
			if (noCheck) {
				$("input.check-lmde[type=checkbox]").each(
				function() {
					$(this).closest('label').closest('div').css('opacity',1);
					$(this).prop('disabled', false);
				});
			}
		}
	);

});

$( window ).resize(function() {
  initCompareCarousel();
});
