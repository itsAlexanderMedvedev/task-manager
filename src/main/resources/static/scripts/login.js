$(document).ready(function () {
    $('#loginForm').submit(function (event) {
        event.preventDefault();

        const csrfToken = $('meta[name="_csrf"]').attr('content');
        const csrfHeader = $('meta[name="_csrf_header"]').attr('content');

        const user = {
            username: $('#loginUsername').val(),
            password: $('#loginPassword').val()
        };

        console.log(user);

        $.ajax({
            url: '/auth/login',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(user),
            headers: {
                // [csrfHeader]: csrfToken
            },
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

