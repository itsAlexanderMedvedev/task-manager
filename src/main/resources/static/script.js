const addTaskBtn = document.getElementById("addTask"),
      addTaskForm = document.getElementById("task-input-field")

addTaskBtn.addEventListener('click', function () {
    addTaskForm.classList.toggle("active")
});

function updateCharacterCount() {
    let textarea = document.getElementById("taskDescription");
    let characterCountSpan = document.getElementById("characterCount");
    let currentLength = textarea.value.length;
    console.log(currentLength);
    let maxLength = textarea.maxLength;
    characterCountSpan.textContent = currentLength + " / " + maxLength;
}
