let isNoDuplication = false;

// 가입하기 버튼클릭 시 실행
function submitForm() {
    // 중복이 아닌 경우 check
    const usernameMessage = document.getElementById("usernameMessage");
    const nicknameMessage = document.getElementById("nicknameMessage");
    const emailMessage = document.getElementById("emailMessage");

    if (checkedUsername === usernameMessage.innerText &&
        checkedNickname === nicknameMessage.innerText &&
        checkedEmail === emailMessage.innerText) {
        isNoDuplication = true;
    }

    if (!isNoDuplication) {
        toastWarning("중복 확인을 마쳐주세요!");
        return false;
    }

    return true;
}