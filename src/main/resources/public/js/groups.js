$(document).ready(function() {
    $.ajaxSetup({
      contentType: "application/json; charset=utf-8"
    });

    var loadGroups = function () {
        $.ajax({
            url: "/group/"
        }).then(function(data) {
            var html = '<table class="table table-striped"><tr><th>Name group</th><th width="15%">Actions</th></tr>';

            for(var i in data) {
                var group = data[i];

                html += '<tr><td>' + group.groupName + '</td> <td>'
                + '<button type="button" class="btn btn-default edit-button" data-id="' + group.id + '"><span class="glyphicon glyphicon-pencil"></span></button>&nbsp;'
                + '<button type="button" class="btn btn-default remove-button" data-id="' + group.id + '"><span class="glyphicon glyphicon-trash"></span></button>&nbsp;'
                + '</td></tr>'
            }


            html += '</table>'
            $('#group').html(html);

            $('.remove-button').click(function(){
                var id = $(this).data("id");

                $.ajax({
                    url: '/group/' + id,
                    type: 'DELETE',
                    success: function(result) {
                        loadGroups();
                    },
                    error: function() {
                        var html = '<div class="alert alert-warning alert-dismissible" role="alert">'
                                      + '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>'
                                      + '<strong>Error!</strong> Unable to delete group. Please make sure there are no users in this group.'
                                     + '</div>';
                        $('#alert-area').html(html);
                    }
                });
            });

            $('.edit-button').click(function(){
                var id = $(this).data("id");

                $.ajax({
                    url: "/group/" + id
                }).then(function(data) {
                    $('#edit-group').val(data.groupName);
                    $('#edit-id').val(data.id);

                    $('#editModal').modal('show')
                });
            });
        });
    };

    loadGroups();

    $('#add-group-button').click(function(event) {
        event.preventDefault();

        var groupName = $('#groupname').val();

        $.post("/group/", '{ "groupName": "' + groupName + '" }').done(function( data ) {
            loadGroups()
        });
    });

    $('#edit-group-button').click(function(){
        event.preventDefault();

        var id = $('#edit-id').val();
        console.log("my id is " + id);
        var groupName = $('#edit-group').val();

        $.ajax({
            url: '/group/' + id,
            type: 'PUT',
            data: '{ "groupName": "' + groupName + '" }',
            success: function(result) {
                loadGroups();
                $('#edit-form').hide();
            }
        });
    });
});