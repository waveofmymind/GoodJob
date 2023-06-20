// 중복확인 버튼 클릭 시 실행되는 함수
function checkDuplication(event) {
    const fieldName = event.target.previousElementSibling.name;

    checkDuplicateField(fieldName);

    event.preventDefault(); // 폼 제출 동작 막기
}

function checkDuplicateField(fieldName) {
    const url = '/member/join/valid/' + fieldName;
    const fieldInput = document.getElementById(fieldName);
    const fieldValue = fieldInput.value.trim();
    const fieldMessage = document.getElementById(fieldName + "Message");

    const formData = new FormData();
    formData.append(fieldName, fieldValue);

    fetch(url, {
        method: 'post',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.resultCode !== 'S-1') { // 중복인 경우
                fieldMessage.innerText = data.msg;
                fieldMessage.classList.add("text-red-500");
                fieldMessage.classList.remove("text-green-500");
            } else { // 중복 아닌 경우
                fieldMessage.innerText = data.msg;
                fieldMessage.classList.add("text-green-500");
                fieldMessage.classList.remove("text-red-500");
            }

            fieldMessage.style.display = "block";
        });
}

