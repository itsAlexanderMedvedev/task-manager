$(document).ready(function () {
    $('#registerForm').on('submit', function(event) {
        event.preventDefault();

        const csrfToken = $('meta[name="_csrf"]').attr('content');
        const csrfHeader = $('meta[name="_csrf_header"]').attr('content');

        // Clear previous error messages
        $('#usernameError').text('');
        $('#emailError').text('');
        $('#passwordError').text('');

        const user = {
            username: $('#registerUsername').val(),
            email: $('#registerEmail').val(),
            password: $('#registerPassword').val()
        };

        console.log(csrfHeader + ' ' + csrfToken);

        $.ajax({
            url: '/auth/register',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(user),
            headers: {
                // [csrfHeader]: csrfToken
            },
            success: function() {
                alert('Registration successful!');
                window.location.href = '/auth/login';
            },
            error: function(xhr) {
                const errors = xhr.responseJSON;
                if (errors.username) {
                    $('#usernameError').text(errors.username).show();
                }
                if (errors.email) {
                    $('#emailError').text(errors.email).show();
                }
                if (errors.password) {
                    $('#passwordError').text(errors.password).show();
                }
            }
        });
    });
});

