$(document).ready(function() {
    let isEditing = false;

    // Include CSRF token in the headers (for Spring Security)
    // let csrfToken = $("meta[name='_csrf']").attr("content");
    // let csrfHeader = $("meta[name='_csrf_header']").attr("content");
    // const jwtToken = localStorage.getItem('jwtToken');
    const headers = {};
    // headers[csrfHeader] = csrfToken;
    headers["Content-Type"] = "application/json";
    // headers["Authorization"] = "Bearer " + jwtToken;

    const taskFormDiv = $('#taskFormDivBox');
    const descCharCount = $('#characterCount');
    const table = $('#taskTable');
    const addTaskBtn = $('#addTask');

    const taskId = $('#taskId');
    const taskDescription = $('#taskDescription');
    const taskName = $('#taskName');
    const taskDateCreated = $('#taskDateCreated');
    const taskDueDate = $('#taskDueDate');
    const taskSubmitBtn = $('#taskSubmitBtn');


    // ADDING
    $(addTaskBtn).click(function() {
        // Clear the form fields
        $('#taskId').val('');
        $('#taskName').val('');
        $('#taskDescription').val('');
        descCharCount.text('0 / ' + taskDescription.attr('maxlength'));
        $('#taskDateCreated').val(new Date().toISOString().substring(0, 10)); // Date in YYYY-MM-DD format
        $('#taskDueDate').val('');
        $('.error').text('');
        taskFormDiv.css('display', 'flex'); // Show the form
    });

    // EDITING
    // done this way so that new dynamically created elements also have listeners
    table.on('click', '.editBtn', function() {
        const id = $(this).data('id');
        $.get('/tasks/' + id, function(task) {
            // Populate the form with the task data
            taskId.val(task.id);
            taskName.val(task.name);
            taskDescription.val(task.description);
            descCharCount.text(task.description.length + ' / ' + taskDescription.attr('maxlength'));
            taskDateCreated.val(task.dateCreated);
            taskDueDate.val(task.dueDate);
            taskFormDiv.css('display', 'flex'); // Show the form
            $('.error').text('');
            isEditing = true;
        });
    });

    // DELETING
    table.on('click', '.deleteBtn', function() {
        const id = $(this).data('id');
        $.ajax({
            url: '/tasks/' + id,
            type: 'DELETE',
            headers: headers,
            success: function() {
                $('.task-row[data-id="' + id + '"]').remove();
            }
        });
    });

    // SAVING
    $('#taskForm').submit(function(e) {
        e.preventDefault();

        taskSubmitBtn.attr('disabled', 'disabled');

        const formDataArray = $('#taskForm').serializeArray();
        const formDataDict = {};
        $.each(formDataArray, function(i, field){
            formDataDict[field.name] = field.value;
        });

        $.ajax({
            url: '/tasks',
            type: 'POST',
            data: JSON.stringify(formDataDict),
            headers: headers,
            success: function(task) { // task is the task we saved returned to us by the server
                formDataDict['id'] = task.id; // ID specified by the server
                updateOrAddTableRow(formDataDict);
                taskFormDiv.hide();
                isEditing = false;
            },
            error: function(xhr) {
                const errors = xhr.responseJSON;
                $('.error').text('');
                for (const field in errors) {
                    $('#' + field + 'Error').text(errors[field]);
                }
            },
            complete: function() {
                taskSubmitBtn.removeAttr('disabled');
            }
        });
    });

    function updateOrAddTableRow(formDataDict) {
        const existingRow = $('.task-row[data-id="' + formDataDict.id + '"]');

        if (existingRow.length > 0 && formDataDict.id !== '') {
            updateRow(existingRow, formDataDict);
        } else {
            const row = createNewRow(formDataDict);
            $('#taskTable tbody').append(row);
        }
    }

    // SORTING
    $('th[data-sort]').on('click', function() {
        const column = $(this).data('sort');
        const order = table.data('order') === 'asc' ? 'desc' : 'asc';
        table.data('order', order);

        $.ajax({
            url: '/tasks/sort',
            method: 'GET',
            data: { sort: column, order: order },
            success: function(data) {
                let rows = '';
                // console.log(data);
                $.each(data, function(index, task) {
                    rows += createNewRow(task).prop('outerHTML');
                });
                $('#taskTable tbody').html(rows);
            }
        });
    });

    // CLOSING THE FORM ON CLICKING CLOSE BUTTON OR OUTSIDE THE FORM
    $('#taskFormDivCloseBtn').click(function() {
        taskFormDiv.hide();
        isEditing = false;
    });

    $(document).click(function(event) {
        if (!$(event.target).closest('#taskFormDiv').length && !$(event.target).is(addTaskBtn)) {
            taskFormDiv.hide();
        }
    });
    // ----------------------------
});


function updateCharacterCount() {
    let textarea = $("#taskDescription");
    $("#characterCount").text(textarea.val().length + " / " + textarea.attr("maxlength"));
}

function createNewRow(task) {
    const newRow = $('<tr class="task-row" data-id="' + task.id + '"></tr>');
    newRow.append('' +
        '<td>' +
        '   <div>' +
        '       <label>' +
        '           <input type="checkbox" class="task-completed-checkbox">' +
        '       </label>' +
        '   </div>' +
        '</td>');
    newRow.append('<td>' + task.name + '</td>');
    newRow.append('<td><div class="description-column">' + task.description + '</div></td>');
    newRow.append('<td>' + task.dateCreated + '</td>');
    newRow.append('<td>' + task.dueDate + '</td>');
    newRow.append('' +
        '<td>' +
        '<div class="controls-container">' +
        '<button class="control-btn editBtn" data-id="' + task.id + '">' +
        '<img src="/images/edit.svg" alt="edit">' +
        '</button>' +
        '<button class="control-btn deleteBtn" data-id="' + task.id + '">' +
        '<img src="/images/cross.svg" alt="delete">' +
        '</button>' +
        '</div>' +
        '</td>');

    return newRow;
}

function updateRow(existingRow, task) {
    existingRow.find('td:eq(1)').text(task.name);
    existingRow.find('.description-column').text(task.description);
    existingRow.find('td:eq(3)').text(task.dateCreated);
    existingRow.find('td:eq(4)').text(task.dueDate);
}
