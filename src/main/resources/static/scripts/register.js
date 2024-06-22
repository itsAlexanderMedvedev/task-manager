$(document).ready(function () {
    $('#registerForm').on('submit', function(event) {
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

                    for (const errNum in errors) {
                        $('#' + errors[errNum]['field'] + 'Error').text(errors[errNum]['defaultMessage']);
                    }
                } else if (xhr.responseText === 'Username already exists') {
                    $('#usernameError').text('Username already exists');
                }
            }
        });
    });
});

