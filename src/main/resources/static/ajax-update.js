$(document).ready(function() {
    const taskFormDiv = $('#taskFormDiv');
    let isEditing = false;
    const descCharCount = $('#characterCount');

    $('#addTask').click(function() {
        // Clear the form fields if not in edit mode
        $('#taskId').val('');
        $('#taskName').val('');
        $('#taskDescription').val('');
        descCharCount.text('0 / 500');
        $('#taskDateCreated').val(new Date().toISOString().substring(0, 10)); // Date in YYYY-MM-DD format
        $('#taskDueDate').val('');
        taskFormDiv.css('display', 'flex'); // Show the form
    });

    $('.editBtn').click(function() {
        const id = $(this).data('id');

        $.get('/tasks/editTask/' + id, function(task) {
            // Populate the form with the task data
            $('#taskId').val(task.id);
            $('#taskName').val(task.name);
            $('#taskDescription').val(task.description);
            descCharCount.text(task.description.length + ' / 500');
            $('#taskDateCreated').val(task.dateCreated);
            $('#taskDueDate').val(task.dueDate);
            taskFormDiv.css('display', 'flex'); // Show the form
            isEditing = true;
        });
    });

    $('#taskForm').submit(function(e) {
        e.preventDefault();

        // Include CSRF token in the headers (for Spring Security)
        const csrfToken = $("meta[name='_csrf']").attr("content");
        const csrfHeader = $("meta[name='_csrf_header']").attr("content");
        const headers = {};
        headers[csrfHeader] = csrfToken;
        headers["Content-Type"] = "application/json";

        const formDataArray = $('#taskForm').serializeArray();
        const formDataObject = {};
        $.each(formDataArray, function(i, field){
            formDataObject[field.name] = field.value;
        });

        $.ajax({
            url: '/tasks/saveTask',
            type: 'POST',
            data: JSON.stringify(formDataObject),
            headers: headers,
            success: function() {
                location.reload();
                taskFormDiv.hide();
                isEditing = false;
            },
            error: function(jqXHR) {
                const errors = jqXHR.responseJSON;
                $('.error').text('');
                for (const field in errors) {
                    $('#' + field + 'Error').text(errors[field]);
                    console.log(errors[field]);
                }
            }
        });

    });
});
