function openIdModal() {
    const modalContainer = document.getElementById("idModalContainer");

    fetch("/member/recover/username", {
        method: 'get'
    })
        .then(response => response.text())
        .then(html => {
            modalContainer.innerHTML = html;
            const modal = modalContainer.querySelector("#idModal");
            modal.style.display = "block";
        });
}

function closeIdModal() {
    const modalContainer = document.getElementById("idModalContainer");
    modalContainer.innerHTML = "";
}

function openPasswordModal() {
    const modalContainer = document.getElementById("passwordModalContainer");

    fetch("/member/recover/password", {
        method: 'get'
    })
        .then(response => response.text())
        .then(html => {
            modalContainer.innerHTML = html;
            const modal = modalContainer.querySelector("#passwordModal");
            modal.style.display = "block";
        });
}

function closePasswordModal() {
    const modalContainer = document.getElementById("passwordModalContainer");
    modalContainer.innerHTML = "";
}

function recoverField(fieldName) {
    const url = '/member/recover/username';
    const fieldValue = document.getElementById(fieldName).value.trim();
    const fieldMessage = document.getElementById(fieldName + 'Message');
    const fieldContainer = document.getElementById(fieldName + 'Container');

    const formData = new FormData();
    formData.append(fieldName, fieldValue);

    fetch(url, {
        method: 'post',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.resultCode !== 'S-1') { // 요청 실패시
                fieldMessage.innerText = data.msg;
                fieldMessage.classList.add("text-red-500");
                fieldMessage.classList.remove("text-green-500");
            } else { // 요청 성공
                fieldMessage.innerText = data.msg;
                fieldMessage.classList.add("text-green-500");
                fieldMessage.classList.remove("text-red-500");
            }

            fieldContainer.style.display = "block";
        });
}

function sendRecoverEmail(fieldName) {
    const url = '/member/recover/password';
    const fieldValue = document.getElementById(fieldName).value.trim();
    const fieldMessage = document.getElementById(fieldName + 'Message');
    const fieldContainer = document.getElementById(fieldName + 'Container');

    const formData = new FormData();
    formData.append(fieldName, fieldValue);

    fieldMessage.innerText = "가입하신 메일주소로 비밀번호가 발송되었습니다.";
    fieldMessage.classList.add("text-green-500");
    fieldMessage.classList.remove("text-red-500");

    fetch(url, {
        method: 'post',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.resultCode !== 'S-1') { // 요청 실패시
                fieldMessage.innerText = data.msg;
                fieldMessage.classList.add("text-red-500");
                fieldMessage.classList.remove("text-green-500");
            }
        });

    fieldContainer.style.display = "block";
}