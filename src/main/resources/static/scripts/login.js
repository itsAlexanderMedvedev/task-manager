$(document).ready(function () {
    $('#loginForm').submit(function (event) {
        event.preventDefault();

        $('#credentialError').text('');

        const user = {
            username: $('#loginUsername').val(),
            password: $('#loginPassword').val()
        };

        $.ajax({
            url: '/auth/login',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(user),
            success: function() {
                alert('Login successful!');
                window.location.href = '/';
            },
            error: function(xhr) {
                const errors = xhr.responseJSON['errors'];

                for (const field in errors) {
                    $('#' + field + 'Error').text(errors[field]);
                }
            }
        });
    });
});

