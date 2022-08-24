$(document).ready(function() {

  $.ajax({
    url: '/users',
    type: 'GET',
    success: function(data) {

      $.each(data, function(i, user) {
        $('<tr>').append(
          $('<td>').text(user.idUser),
          $('<td>').text(user.firstName + ' ' + user.lastName),
          $('<td>').text(user.username),
          $('<td>').text(moment(user.birth).format('DD-MM-YYYY')),
          $('<td>').text(user.userRoles.length==0?'':user.userRoles[0].role.nameRole),
          $('<td>').text('Update').wrapInner('<a href="/update/' + user.idUser + '" />'),
          $('<td>').text('Delete').wrapInner('<a href="javascript:deleteUser(' + user.idUser + ')" />')
        ).appendTo('tbody');
      });
    },
    error: function() {
      alert("Error while retrieving Users!");
    }
  });

  deleteUser = function(id) {

    $.ajax({
      url: '/users/' + id,
      type: 'DELETE',
      success: function(data) {
        /* alert("The User was deleted successfully!"); */
        $(location).attr("href","/list/delete");
      },
      error: function() {
        alert("Error while deleting User!");
      }
    });
  };
});
