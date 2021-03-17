$(function() {
   $('#div-loginform').dialog({
      autoOpen: false,
      width: 650,
      modal: true,
      closeOnEscape: true
   });

   $('#div-addtocartform').dialog({
      autoOpen: false,
      width: 650,
      modal: true,
      closeOnEscape: true
   });

   $('#div-commentform').dialog({
      autoOpen: false,
      width: 650,
      modal: true,
      closeOnEscape: true
   });

   $( "#button" ).button();

   $('.dropdown').hover(
      function(){
         $(this).children('.sub-menu').slideDown(200);
      },
      function(){
         $(this).children('.sub-menu').slideUp(200);
      }
   );

   $.ajax({
      type: "GET",
      url: '/menu',
      async: true,
      dataType: 'json',
      success: function(data) {
         $.each (data, function(index, obj) {
            $('#productsmenu').append('<li><a href="products?cat=' + obj.id + '">' + obj.label + '</a></li>');
         });
      }
   });
});

function showForm(div, title)
{
   $(div).dialog({
       title: title,
       buttons: [
         {
            text: "Fermer",
            click: function() { $( this ).dialog( "close" ); }
         }
       ]
   });
   $(div).dialog("open");
}

function lostPassword()
{
   username = $('#username').val();
   if (!username)
   {
      alert("Il faut remplir le login");
      return;
   }

   $.ajax({
      type: "GET",
      url: '/lostpassword',
      data: "username=" + username,
      async: true,
      dataType: 'json',
      success: function(data) {
         alert(data.msg);
      }
   });
}
