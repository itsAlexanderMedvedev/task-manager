$(document).ready(function() {
    const taskFormDiv = $('#taskFormDivBox');
    const descCharCount = $('#characterCount');
    const table = $('#taskTable');
    const addTaskBtn = $('#addTask');
    let isEditing = false;

    // Include CSRF token in the headers (for Spring Security)
    const csrfToken = $("meta[name='_csrf']").attr("content");
    const csrfHeader = $("meta[name='_csrf_header']").attr("content");
    const headers = {};
    headers[csrfHeader] = csrfToken;
    headers["Content-Type"] = "application/json";

    $(addTaskBtn).click(function() {
        // Clear the form fields if not in edit mode
        $('#taskId').val('');
        $('#taskName').val('');
        $('#taskDescription').val('');
        descCharCount.text('0 / 500');
        $('#taskDateCreated').val(new Date().toISOString().substring(0, 10)); // Date in YYYY-MM-DD format
        $('#taskDueDate').val('');
        taskFormDiv.css('display', 'flex'); // Show the form
    });

    // done this way so that new dynamically created elements also have listeners
    table.on('click', '.editBtn', function() {
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

    table.on('click', '.deleteBtn', function() {
        const id = $(this).data('id');
        $.ajax({
            url: '/tasks/deleteTask/' + id,
            type: 'DELETE',
            headers: headers,
            success: function() {
                $('.task-row[data-id="' + id + '"]').remove();
            }
        });
    });

    $('#taskForm').submit(function(e) {
        e.preventDefault();

        const formDataArray = $('#taskForm').serializeArray();
        const formDataDict = {};
        $.each(formDataArray, function(i, field){
            formDataDict[field.name] = field.value;
        });

        $.ajax({
            url: '/tasks/saveTask',
            type: 'POST',
            data: JSON.stringify(formDataDict),
            headers: headers,
            success: function(task) { // task is the task we saved returned to us by the server
                formDataDict['id'] = task.id; // ID specified by the server
                updateOrAddTableRow(formDataDict);
                taskFormDiv.hide();
                isEditing = false;
            },
            error: function(jqXHR) {
                const errors = jqXHR.responseJSON;
                $('.error').text('');
                for (const field in errors) {
                    $('#' + field + 'Error').text(errors[field]);
                }
            }
        });
    });


    function updateOrAddTableRow(formDataDict) {
        const existingRow = $('.task-row[data-id="' + formDataDict.id + '"]');

        if (existingRow.length > 0 && formDataDict.id !== '') {
            existingRow.find('td:eq(1)').text(formDataDict.name);
            existingRow.find('.description-column').text(formDataDict.description);
            existingRow.find('td:eq(3)').text(formDataDict.dateCreated);
            existingRow.find('td:eq(4)').text(formDataDict.dueDate);
        } else {
            const newRow = $('<tr class="task-row" data-id="' + formDataDict.id + '"></tr>');
            newRow.append('' +
                '<td>' +
                '   <div>' +
                '       <label>' +
                '           <input type="checkbox" class="task-completed-checkbox">' +
                '       </label>' +
                '   </div>' +
                '</td>');
            newRow.append('<td>' + formDataDict.name + '</td>');
            newRow.append('<td><div class="description-column">' + formDataDict.description + '</div></td>');
            newRow.append('<td>' + formDataDict.dateCreated + '</td>');
            newRow.append('<td>' + formDataDict.dueDate + '</td>');
            newRow.append('' +
                '<td>' +
                    '<div class="controls-container">' +
                        '<button class="control-btn editBtn" data-id="' + formDataDict.id + '">' +
                            '<img src="/images/edit.svg" alt="edit">' +
                        '</button>' +
                        '<button class="control-btn deleteBtn" data-id="' + formDataDict.id + '">' +
                            '<img src="/images/cross.svg" alt="delete">' +
                        '</button>' +
                    '</div>' +
                '</td>');

            $('#taskTable tbody').append(newRow);
        }
    }

    $('#taskFormDivCloseBtn').click(function() {
        taskFormDiv.hide();
        isEditing = false;
    });

    $(document).click(function(event) {
        if (!$(event.target).closest('#taskFormDiv').length && !$(event.target).is(addTaskBtn)) {
            taskFormDiv.hide();
        }
    });
});
