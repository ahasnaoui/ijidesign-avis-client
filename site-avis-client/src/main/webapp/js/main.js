function initTooltipForm() {

  if( $(window).width() >= widthTablet ) {
    Opentip.styles.formStyle = {
      "extends": 'glass',
      target: true,
      stem: true,
      fixed: true,
      tipJoint: 'bottom left',
      targetJoint: 'top right',
      showOn: 'mouseover',
      shadow: false,
      borderRadius: 10,
      background: brandColor,
      borderWidth: 0,
      stemLength: 10,
      offset: [-10,10],
      containInViewport: true
    };
  }
  else {
    Opentip.styles.formStyle = {
      "extends": 'glass',
      target: true,
      stem: true,
      fixed: true,
      tipJoint: 'bottom right',
      targetJoint: 'top left',
      showOn: 'mouseover',
      shadow: false,
      borderRadius: 10,
      background: brandColor,
      borderWidth: 0,
      stemLength: 10,
      offset: [10,2],
      containInViewport: true
    };
  }

  Opentip.defaultStyle = 'formStyle';

  // LMDE-146 :  reprise de la v142
  if ( $(window).width() >= widthTablet ) {
      $('.form-group').hover(
        function() {
          $(this).closest('.form-group').find('.tt').show();
        }, function() {
          if (!($(this).closest('.form-group').find('.tt').hasClass('tt-error')) && !($(this).find(':input').is(":focus"))) {
            $(this).closest('.form-group').find('.tt').hide();
          }
        }
      );
    }
  // LMDE-146 :  reprise de la v142
  
  $('.form-group').find(':input').focus(function() {
    $(this).closest('.form-group').find('.tt').show();
  });

  $('.form-group').find(':input').blur(function() {
    if (!($(this).find('.tt').hasClass('tt-error'))) {
      $(this).closest('.form-group').find('.tt').hide();
    }
  });

  $('input:radio').change(
    function(){
      $(this).closest('.form-group').removeClass('has-error').addClass('has-success');
      $(this).closest('.form-group').find('.glyphicon').removeClass('glyphicon-remove').addClass('glyphicon-ok');
      $(this).closest('.form-group').find('.tt').show().removeClass('tt-error');
    }
  );

};

function initTooltipText() {
  // On récupère la liste des blocs de champs
  var blocs = $('.form-group');

  blocs.each(function() {
    // On récupère le span tt
    var el_tt = $(this).find('.tt');

    // Si pas trouvé, on passe à la suite
    if(el_tt == undefined) {
      return;
    }

    // Si le span tt contient les data "type", "text" et "name" alors on créé le tooltip
    var control_type = el_tt.data('type');
    var control_name = el_tt.data('name');
    var control_text = el_tt.data('text');

    // Si une des données manquent on passe à l'élément suivant
    if(control_type == undefined ||
        control_name == undefined ||
        control_text == undefined) {
      return;
    }

    var control_el = undefined;
    switch(control_type) {
      // MERGE DU 07/06/2016
      case 'select':
        control_el = $(this).find('select');
        break;
      default:
        control_el = $(this).find(':input');
        break;
    }

    if(control_el != undefined ) {
      var control_tooltip = new Opentip($('.tt-' + control_name), control_text);
      control_el.focus(function() {
        control_tooltip.show();
      });
      control_el.blur(function() {
        control_tooltip.hide();
      });
    }
  });
}