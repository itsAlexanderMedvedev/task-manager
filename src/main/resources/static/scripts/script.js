function updateCharacterCount() {
    let textarea = document.getElementById("taskDescription");
    let characterCountSpan = document.getElementById("characterCount");
    let currentLength = textarea.value.length;
    let maxLength = textarea.maxLength;
    characterCountSpan.textContent = currentLength + " / " + maxLength;
}
