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

    const taskFormModal = $('#taskFormModal');
    const taskModalLabel = $('#taskModalLabel');
    const descCharCount = $('#characterCount');
    const table = $('#taskTable');
    const addTaskBtn = $('#addTask');

    const taskId = $('#taskId');
    const taskDescription = $('#taskDescription');
    const taskName = $('#taskName');
    const taskDateCreated = $('#taskDateCreated');
    const taskDueDate = $('#taskDueDate');
    const taskCategories = $('#taskCategories');
    const taskPriority = $('#taskPriority');
    const taskSubmitBtn = $('#taskSubmitBtn');

    $.ajax({
        url: '/categories',
        type: 'GET',
        success: function(categories) {
            categories.forEach(category => {
                taskCategories.append(new Option(category, category, false, false));
            });
        },
        error: function(xhr) {
            console.error('Error loading categories:', xhr);
        }
    });


    // ADDING
    $(addTaskBtn).click(function() {
        taskModalLabel.text('Create Task');
        // Clear the form fields
        taskId.val('');
        taskName.val('');
        taskDescription.val('');
        descCharCount.text('0 / ' + taskDescription.attr('maxlength'));
        taskDateCreated.val(new Date().toISOString().substring(0, 10)); // Date in YYYY-MM-DD format
        taskDueDate.val('');
        taskCategories.val(null).trigger('change');
        taskPriority.val('');
        $('.error').text('');

        taskFormModal.modal('show');
    });

    $('#addCategory').on('click', function() {
        $('#categoryModal').modal('show');
    });

    taskCategories.select2({
        theme: "bootstrap-5",
        closeOnSelect: false,
        allowClear: true
    });


    // EDITING
    // done this way so that new dynamically created elements also have listeners
    table.on('click', '.task-row', function() {
        const id = $(this).data('id');
        $.get('/tasks/' + id, function(task) {
            // Populate the form with the task data
            taskModalLabel.text('Edit Task');
            taskId.val(task.id);
            taskName.val(task.name);
            taskDescription.val(task.description);
            descCharCount.text(task.description.length + ' / ' + taskDescription.attr('maxlength'));
            taskDateCreated.val(task.dateCreated);
            taskDueDate.val(task.dueDate);

            // task.categories.forEach(category => {
            //     taskCategories.append(new Option(category, category, false, true));
            // });
            taskCategories.val(task.categories).trigger('change');


            taskPriority.val(task.priority);
            taskFormModal.modal('show');
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
        formDataDict['categories'] = taskCategories.val();

        console.log(formDataDict);

        $.ajax({
            url: '/tasks',
            type: 'POST',
            data: JSON.stringify(formDataDict),
            headers: headers,
            success: function(task) { // task is the task we saved returned to us by the server
                formDataDict['id'] = task.id; // ID specified by the server
                updateOrAddTableRow(formDataDict);
                taskFormModal.modal('hide');
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
                $.each(data, function(index, task) {
                    rows += createNewRow(task).prop('outerHTML');
                });
                $('#taskTable tbody').html(rows);
            }
        });
    });
    //
    // $(document).on('mousedown', function(event) {
    //     console.log(taskCategories.data('select2').isOpen());
    //
    //     const target = $(event.target);
    //     // If the target is outside the modal and the dropdown is open, close the dropdown
    //     if (!target.closest('#taskFormModalInner').length && !target.closest('.select2-container').length) {
    //         console.log("EXECUTED");
    //         event.stopPropagation();
    //         event.stopImmediatePropagation();
    //         if (taskCategories.data('select2').isOpen()) {
    //             console.log("CLOSING");
    //             taskCategories.select2('close');
    //         }
    //     }
    // });
});


function updateCharacterCount() {
    let textarea = $("#taskDescription");
    $("#characterCount").text(textarea.val().length + " / " + textarea.attr("maxlength"));
}

function createNewRow(task) {
    const newRow = $('<tr class="task-row" data-id="' + task.id + '">');
    newRow.append('<td>' + task.name +'</td>');
    newRow.append('<td>' + task.dueDate +'</td>');
    newRow.append('<td>' + task.categories +'</td>');
    newRow.append('<td>' + task.priority +'</td>');
    newRow.append('</tr>');

    return newRow;
}

function updateRow(existingRow, task) {
    existingRow.find('td:eq(0)').text(task.name);
    existingRow.find('td:eq(1)').text(task.dueDate);
    existingRow.find('td:eq(2)').text(task.categories);
    existingRow.find('td:eq(3)').text(task.priority);
}
