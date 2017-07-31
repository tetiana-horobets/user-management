$(document).ready(function() {
    $.ajaxSetup({
      contentType: "application/json; charset=utf-8"
    });

    var loadUsers = function () {
        var url = '/user';

        if($('#group-filter').is(':checked')) {
            url += '?groupId=' + $('#group-filter-select').val()
        }

        $.ajax({
            url: url
        }).then(function(data) {
            var html = '<table class="table table-striped"><tr><th>Username</th><th>First Name</th><th>Last Name</th><th>Birthday</th><th width="15%">Actions</th></tr>';

            for(var i in data) {
                var user = data[i];

                html += '<tr><td>' + user.name + '</td><td>' + user.firstName + '</td><td>' + user.lastName + '</td><td>' + user.birthday + '</td><td>'
                + '<button type="button" class="btn btn-default edit-button" data-id="' + user.id + '"><span class="glyphicon glyphicon-pencil"></span></button>&nbsp;'
                + '<button type="button" class="btn btn-default group-button" data-id="' + user.id + '"><span class="glyphicon glyphicon-list-alt"></span></button>&nbsp;'
                + '<button type="button" class="btn btn-default remove-button" data-id="' + user.id + '"><span class="glyphicon glyphicon-trash"></span></button>&nbsp;'
                + '</td></tr>'
            }

            html += '</table>'

            $('#users').html(html);
            $('.remove-button').click(function(){
                var id = $(this).data("id");

                $.ajax({
                    url: '/user/' + id,
                    type: 'DELETE',
                    success: function(result) {
                        loadUsers();
                    },
                    error: function() {
                        var html = '<div class="alert alert-warning alert-dismissible" role="alert">'
                                      + '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>'
                                      + '<strong>Error!</strong> Unable to delete user. Please make sure user is not part of any group.'
                                     + '</div>';
                        $('#alert-area').html(html);
                    }
                });
            });

            $('.edit-button').click(function(){
                var id = $(this).data("id");

                $.ajax({
                    url: "/user/" + id
                }).then(function(data) {
                    $('#edit-username').val(data.name);
                    $('#edit-password').val(data.password);
                    $('#edit-firstName').val(data.firstName);
                    $('#edit-lastName').val(data.lastName);
                    $('#edit-userbirth').val(data.birthday);
                    $('#edit-id').val(data.id);

                    $('#editModal').modal('show')
                });
            });

            $('.group-button').click(function(){
                var id = $(this).data("id");

                $.ajax({
                    url: "/group"
                }).then(function(allGroups) {
                    $.ajax({
                        url: "/group?userId=" + id
                    }).then(function(userGroups) {
                        var html = '<table class="table table-striped"><tr><th>Is in group</th><th>Group name</th></tr>';

                        var existingIds = [];

                        for(var i in userGroups) {
                            existingIds.push(userGroups[i].id);
                        }

                        for(var i in allGroups) {
                            var group = allGroups[i];

                            if(existingIds.indexOf(group.id) >= 0) {
                                html += '<tr><td><input type="checkbox" data-id="' + group.id + '" class="group-checkbox" checked></td><td>' + group.groupName + '</td></tr>'
                            } else {
                                html += '<tr><td><input type="checkbox" data-id="' + group.id + '" class="group-checkbox"></td><td>' + group.groupName + '</td></tr>'
                            }
                        }

                        html += '</table>'

                        $('#groups-user-id').val(id);

                        $('#groups').html(html);
                        $('#groupModal').modal('show')
                    });
                });
            });
        });
    };

    var buildGroupFilter = function() {
        $.ajax({
            url: "/group/"
        }).then(function(json) {
            $.each(json, function(i, group) {
                $('#group-filter-select').append($('<option>').text(group.groupName).attr('value', group.id));
            });
        });
    };

    loadUsers();
    buildGroupFilter();

    $('#add-user-button').click(function(event) {
        event.preventDefault();

        var username = $('#username').val();
        var userPassword = $('#password').val();
        var userFirsName = $('#firstName').val();
        var userLastName = $('#lastName').val();
        var userbirth = $('#userbirth').val();

        $.post("/user/", '{ "name": "' + username + '","password": "' + userPassword + '", "firstName": "' + userFirsName + '", "lastName": "' + userLastName + '", "birthday": "' + userbirth + '" }').done(function( data ) {
            loadUsers()
        });
    });

    $('#edit-user-button').click(function(){
        event.preventDefault();

        var id = $('#edit-id').val();

        var username = $('#edit-username').val();
        var userpassword = $('#edit-password').val();
        var userfirsName = $('#edit-firstName').val();
        var userlastName = $('#edit-lastName').val();
        var userbirth = $('#edit-userbirth').val();

        $.ajax({
            url: '/user/' + id,
            type: 'PUT',
            data: '{ "name": "' + username + '", "password": "' + userpassword + '", "firstName": "' + userfirsName + '",  "lastName": "' + userlastName + '", "birthday": "' + userbirth + '" }',
            success: function(result) {
                loadUsers();
                $('#edit-form').hide();
            }
        });
    });

    $('#save-groups-button').click(function(){
        event.preventDefault();

        var selectedGroups = $('.group-checkbox:checked').map(function () {
            return { id: $(this).data('id')};
        }).get();

        var json = JSON.stringify(selectedGroups);
        var userId = $('#groups-user-id').val();

        $.ajax({
            url: '/user/' + userId + '/group',
            type: 'PUT',
            data: json,
            success: loadUsers
        });
    });

    $('#group-filter-select').change(loadUsers);
    $('#group-filter').change(function() {

        if($('#group-filter').is(':checked')) {
            $('#group-filter-select').show();
        } else {
            $('#group-filter-select').hide();
        }

        loadUsers();
    });
});