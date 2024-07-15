$(document).ready(function () {
    $('#registerForm').submit(function(event) {
        event.preventDefault();

        $('#usernameError').text('');
        $('#emailError').text('');
        $('#passwordError').text('');

        const user = {
            username: $('#registerUsername').val(),
            email: $('#registerEmail').val(),
            password: $('#registerPassword').val()
        };

        $.ajax({
            url: '/auth/register',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(user),
            success: function() {
                alert('Registration successful!');
                window.location.href = '/auth/login';
            },
            error: function(xhr) {
                if (xhr.responseJSON) {
                    const errors = xhr.responseJSON['errors'];

                    for (const field in errors) {
                        $('#' + field + 'Error').text(errors[field]);
                    }
                }
            }
        });
    });
});

