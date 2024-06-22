$(document).ready(function () {
    $('#loginForm').submit(function (event) {
        event.preventDefault();

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
                const errors = xhr.responseJSON;
                if (errors) {
                    if (errors.username) {
                        $('#loginUsername').addClass('is-invalid');
                        $('#usernameError').text(errors.username).show();
                    }
                    if (errors.password) {
                        $('#loginPassword').addClass('is-invalid');
                        $('#passwordError').text(errors.password).show();
                    }
                } else {
                    alert('Login failed. Please try again.');
                }
            }
        });
    });
});

