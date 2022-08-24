$(document).ready(function() {

  var id = $("#id").val();

  $.ajax({
    url: '/users/' + id,
    type: 'GET',
    success: function(data) {

      var idRole = data.userRoles.length==0?0:data.userRoles[0].role.idRole;

      $('#first').val(data.firstName);
      $('#last').val(data.lastName);
      $('#username').val(data.username);
      $('#birth').val(data.birth);

      $.ajax({
        url: '/roles',
        type: 'GET',
        success: function(data) {
          /*
           * If the User has no Role assigned.
           */
          if ( idRole==0 ){
            $('select').append($('<option>', {
              text : 'Select a Role',
              selected: true
            }));
          }

          $.each(data, function(i, role) {

            $('select').append($('<option>', {
              value: role.idRole,
              text : role.nameRole,
              selected: role.idRole==idRole
            }));
          });
        },
        error: function() {
          alert("Error while retrieving Roles!");
        }
      });
    },
    error: function() {
      alert("Error while retrieving User!");
    }
  });


  $('#save').click(function(event){

    if ( !validateForm() ){
       return;
    }

    var id = $("#id").val();
    var first = $("#first").val();
    var last = $("#last").val();
    var username = $("#username").val();
    var birth = $("#birth").val();
    var role = $("#role").val();

    $.ajax({
      url: '/users/' + id,
      /*
       * Create a JSON Object with the Form information.
       */
      data: JSON.stringify({
             'idUser': id,
             'firstName': first,
             'lastName': last,
             'username': username,
             'birth': birth,
             'idRole': role
      }),
      type: 'PUT',
      contentType: 'application/json',
      success: function(data) {
        /* alert("The User was updated successfully!"); */
        $(location).attr("href","/list/update");
      },
      error: function() {
        alert("Error while updating User!");
      }
    });
  });

  validateForm = function() {

    var first = $("#first").val();
    var last = $("#last").val();
    var username = $("#username").val();
    var role = $("#role").val();

    if (first.trim()=== "") {

      alert("Please, type the First Name!");

      $( "#first" ).focus();
      return false;
    }

    if (last.trim()=== "") {

      alert("Please, type the Last Name!");

      $( "#last" ).focus();
      return false;
    }

    if (username.trim()=== "") {

      alert("Please, type the Username!");

      $( "#username" ).focus();
      return false;
    }

    if ( !isNumeric(role.trim()) ) {

      alert("Please, select a Role!");

      $( "#role" ).focus();
      return false;
    }

    return true;
   };

   isNumeric = function(value) {
     return /^-?\d+$/.test(value);
   };
 });
