$(document).ready(function() {

  $.ajax({
    url: '/roles',
    type: 'GET',
    success: function(data) {

      $.each(data, function(i, role) {
        $('select').append($('<option>', {
          value: role.idRole,
          text : role.nameRole
        }));
      });
    },
    error: function() {
      alert("Error while retrieving Roles!");
    }
  });

  $('#save').click(function(event){

    if ( !validateForm() ){
      return;
    }

    var first = $("#first").val();
    var last = $("#last").val();
    var username = $("#username").val();
    var password = $("#password").val();
    var birth = $("#birth").val();
    var role = $("#role").val();

    $.ajax({
      url: '/users',
      /*
       * Create a JSON Object with the Form information.
       */
      data: JSON.stringify({
             'firstName': first,
             'lastName': last,
             'username': username,
             'password': password,
             'birth': birth,
             'idRole': role
      }),
      type: 'POST',
      contentType: 'application/json',
      success: function(data) {
        /* alert("The User was created successfully!"); */
        $(location).attr("href","/list/create");
      },
      error: function() {
        alert("Error while creating User!");
      }
    });
  });

  validateForm = function() {

    var first = $("#first").val();
    var last = $("#last").val();
    var username = $("#username").val();
    var password = $("#password").val();
    var birth = $("#birth").val();
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

    if (password.trim()=== "") {

      alert("Please, type the Password!");

      $( "#password" ).focus();
      return false;
    }

    if (birth.trim()=== "") {

        alert("Please, type the Date of Birth!");

        $( "#birth" ).focus();
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
