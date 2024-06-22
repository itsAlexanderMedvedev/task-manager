$(document).ready(function() {
    // Include CSRF token in the headers (for Spring Security)
    // let csrfToken = $("meta[name='_csrf']").attr("content");
    // let csrfHeader = $("meta[name='_csrf_header']").attr("content");
    // const jwtToken = localStorage.getItem('jwtToken');
    const headers = {};
    // headers[csrfHeader] = csrfToken;
    headers["Content-Type"] = "application/json";
    // headers["Authorization"] = "Bearer " + jwtToken;

    const filterSelect = $('#filterSelect');

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
    const taskDeleteBtn = $('#taskDeleteBtn');


    // CATEGORIES
    $.ajax({
        url: '/categories',
        type: 'GET',
        success: function(categories) {
            const categoryData = categories.map(category => {
                taskCategories.append(new Option(category, category, false, false));
                return { id: category, text: category, group: 'Categories'};
            });

            initializeFilter(categoryData, filterSelect);
        },
        error: function(xhr) {
            console.error('Error loading categories:', xhr);
        }
    });

    $('#addCategory').on('click', function() {
        $('#categoryModal').modal('show');
    });

    $('#categoryForm').submit(function(e) {
        e.preventDefault();
        const category = $('#categoryName').val();
        $.ajax({
            url: '/categories',
            type: 'POST',
            data: { name: category },
            success: function() {
                taskCategories.append(new Option(category, category, false, true));
            },
            error: function(xhr) {
                console.error('Error adding category:', xhr);
            }
        });
    });

    taskCategories.select2({
        placeholder: 'Select categories',
        theme: "bootstrap-5",
        allowClear: true
    }).on('select2:select select2:unselect', function(e) {
        updatePlaceholder(taskCategories, "Select categories", "Search");
    });

    taskPriority.select2({
        theme: "bootstrap-5",
        placeholder: 'Select priority',
        dropdownParent: taskPriority.parent()
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
        taskPriority.val(null).trigger('change');
        taskDeleteBtn.css('display', 'none');
        $('.error').text('');

        taskFormModal.modal('show');
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
            taskCategories.val(task.categories).trigger('change');
            taskPriority.val(task.priority).trigger('change');
            taskDeleteBtn.css('display', 'inline-block');
            $('.error').text('');
            taskFormModal.modal('show');
        });
    });

    taskDeleteBtn.click(function() {
        if (confirm('Are you sure you want to delete this task?')) {
            const id = taskId.val();

            $.ajax({
                url: '/tasks/' + id,
                type: 'DELETE',
                headers: headers,
                success: function() {
                    $('.task-row[data-id="' + id + '"]').remove();
                    taskFormModal.modal('hide');
                }
            });
        }
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

        $.ajax({
            url: '/tasks',
            type: 'POST',
            data: JSON.stringify(formDataDict),
            headers: headers,
            success: function(task) { // task is the task we saved returned to us by the server
                formDataDict['id'] = task.id; // ID specified by the server
                updateOrAddTableRow(formDataDict);
                taskFormModal.modal('hide');
            },
            error: function(xhr) {
                const errors = xhr.responseJSON;
                $('.error').text('');
                for (const field in errors) {
                    console.log(field);
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
        const filters = getFilters(filterSelect);
        // const icon = order === 'asc' ? '▲' : '▼';
        table.data('order', order);

        console.log(filters.categories);
        console.log(filters.priorities);

        $.ajax({
            url: '/tasks/sort',
            method: 'GET',
            data: { sort: column, order: order, categories: filters.categories, priorities: filters.priorities},
            traditional: true,
            success: function(data) {
                let rows = '';
                $.each(data, function(index, task) {
                    rows += createNewRow(task).prop('outerHTML');
                });

                $('#taskTable tbody').html(rows);
            }
        });
    });

    // FILTERING
    filterSelect.on('change', function() {
        const filters = getFilters(filterSelect);

        $.ajax({
            url: '/tasks/filter',
            method: 'GET',
            data: { categories: filters.categories, priorities: filters.priorities },
            success: function(data) {
                let rows = '';
                $.each(data, function(index, task) {
                    rows += createNewRow(task).prop('outerHTML');
                });
                $('#taskTable tbody').html(rows);
            }
        });
    });

    // SEARCHING
    $('#searchTerm').keyup(function() {
        const searchTerm = $(this).val();
        // console.log(searchTerm);
        $.ajax({
            url: '/tasks/search',
            type: 'GET',
            data: { term: searchTerm },
            success: function(data) {
                let rows = '';
                $.each(data, function(index, task) {
                    rows += createNewRow(task).prop('outerHTML');
                });
                $('#taskTable tbody').html(rows);
            }
        });
    });
});

// --------- HELPER FUNCTIONS

function clearCategoryModal() {
    $('#categoryName').val('');
    $('.error').text('');
}

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

    const dueDate = new Date(task.dueDate);
    const now = new Date();
    const oneDay = 24 * 60 * 60 * 1000;
    const daysUntilDue = Math.ceil(Math.abs((dueDate - now) / oneDay));

    console.log(dueDate);
    console.log(now);
    console.log(daysUntilDue);
    console.log(dueDate - now);
    console.log((dueDate - now) / oneDay);

    if (daysUntilDue <= 1) {
        newRow.addClass('due-in-one-day');
    } else if (daysUntilDue === 2) {
        newRow.addClass('due-in-two-days');
    }



    return newRow;
}

function updateRow(existingRow, task) {
    existingRow.find('td:eq(0)').text(task.name);
    existingRow.find('td:eq(1)').text(task.dueDate);
    existingRow.find('td:eq(2)').text(task.categories);
    existingRow.find('td:eq(3)').text(task.priority);
}

function initializeFilter(categories, $element) {
    $element.select2({
        placeholder: 'Select filters',
        closeOnSelect: false,
        allowClear: true,
        theme: 'bootstrap-5',
        data: [
            {
                text: 'Categories',
                children: categories
            },
            {
                text: 'Priorities',
                children: [
                    { id: 'High', text: 'High', group: 'Priorities'},
                    { id: 'Medium', text: 'Medium', group: 'Priorities' },
                    { id: 'Low', text: 'Low', group: 'Priorities' }
                ]
            }
        ]
    }).on('select2:select select2:unselect', function(e) {
        updatePlaceholder($element, "Select filters", "Search");
    });
}

function updatePlaceholder($element, msgDefault, msgSelected) {
    const $searchField = $element.data('select2').dropdown.$search || $element.data('select2').selection.$search;
    const selectedItems = $element.select2('data');
    if (selectedItems.length > 0) {
        $searchField.attr('placeholder', msgSelected);
        $searchField.css('width', '100%');
    } else {
        $searchField.attr('placeholder', msgDefault);
    }
}

function getFilters(filterSelect) {
    const selectedCategories = [];
    const selectedPriorities = [];

    filterSelect.data('select2').dataAdapter.current(function(data) {
        data.forEach(group => {
            if (group.group === 'Categories') {
                selectedCategories.push(group.id);
            } else if (group.group === 'Priorities') {
                selectedPriorities.push(group.id);
            }
        });
    });

    return { categories: selectedCategories, priorities: selectedPriorities };
}