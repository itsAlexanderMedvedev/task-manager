const addTaskBtn = document.getElementById("addTask"),
      addTaskForm = document.getElementById("task-input-field")

addTaskBtn.addEventListener('click', function () {
    addTaskForm.classList.toggle("active")
});

