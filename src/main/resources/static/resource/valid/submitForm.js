let isNoDuplication = false;

// 가입하기 버튼클릭 시 실행
function submitForm() {
    // 중복이 아닌 경우 check
    let usernameField = document.getElementById("usernameMessage");
    let nicknameField = document.getElementById("nicknameMessage");
    let emailField = document.getElementById("emailMessage");

    if (usernameField.classList.contains("text-green-500") &&
        nicknameField.classList.contains("text-green-500") &&
        emailField.classList.contains("text-green-500")) {
        isNoDuplication = true;
    }

    if (!isNoDuplication) {
        toastWarning("중복 확인을 마쳐주세요!");
        event.preventDefault();
    }
}