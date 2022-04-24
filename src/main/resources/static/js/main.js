'use strict';

const usernamePage = document.querySelector('#type-page');
const lengthPage = document.querySelector('#length-page');
const chatPage = document.querySelector('#chat-page');
const typeAvtoButton = document.querySelector('#typeAvtoButton');
const typeGenerateButton = document.querySelector('#typeGenerateButton');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const messageArea = document.querySelector('#messageArea');
const arrayArea = document.querySelector('#arrayArea');
const connectingElement = document.querySelector('.connecting');
const setLengthForm = document.querySelector('#setLengthForm');

let stompClient = null;
let username = null;
let length = null;

const colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];
let indexRowTable = 1;

function connect(event) {
    const isNumber = Number.isInteger(Number(length));
    username = event.originalTarget.value;
    console.log(username + " " + length)
    if (username && isNumber) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');
    }
    event.preventDefault();
}


function onConnected() {
    console.log(length)
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);
    // Tell your username to the server
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({
            length: length,
            algorithm: "username",
            type: 'JOIN'
        })
    )
    connectingElement.classList.add('hidden');
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        var chatMessage = {
            content: messageInput.value,
            // algorithm: username,
            type: username
        };

        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function sendTypeAndLength(event) {
    event.preventDefault();
    console.log(" sendTypeAndLength ")
    connect(event)
    if (stompClient) {
        const chatMessage = {
            algorithm: username,
            content: messageInput.value,
            type: username
        };

        stompClient.send("/app/chat.sendTypeAndLength", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    console.log("onMessageReceived")
    console.log(payload)
    var message = JSON.parse(payload.body);
    console.log(message.type)

// какой тип выбрали
    if (message.type === 'JOIN') {
        let messageElementForArray = document.createElement('div');
        messageElementForArray.classList.add('event-message');
        let array = message.generateArray;
        let textElement = document.createElement('p');
        let messageText = document.createTextNode(array);
        textElement.appendChild(messageText);
        messageElementForArray.appendChild(textElement);
        arrayArea.appendChild(messageElementForArray);
    }
    if (message.type === "AUTO") {
        // <tr>
        // <td>данные</td><td>данные</td
        // </tr>
        let messageElement = document.createElement('tr');
        messageElement.classList.add('chat-message');

        console.log(message.sequenceAuto)

        let numberElement = document.createElement('td');
        let numberText = document.createTextNode(indexRowTable);
        indexRowTable++;
        numberElement.appendChild(numberText);
        messageElement.appendChild(numberElement);

        let textElement = document.createElement('td');
        let messageText = document.createTextNode(message.sequenceAuto);
        textElement.appendChild(messageText);
        messageElement.appendChild(textElement);
        messageArea.appendChild(messageElement);
    }
    if (message.type === "GENERATE") {
        console.log(message.sequenceGenerate)
        const prop = Object.entries(message.sequenceGenerate);
        prop.forEach(
            (element) => {
                console.log(element)
                let messageElement = document.createElement('tr');
                messageElement.classList.add('chat-message');
                let numberElement = document.createElement('td');
                let numberText = document.createTextNode(element[0]);
                indexRowTable++;
                numberElement.appendChild(numberText);
                messageElement.appendChild(numberElement);
                let textElement = document.createElement('td');
                let messageText = document.createTextNode(element[1]);
                textElement.appendChild(messageText);
                messageElement.appendChild(textElement);
                messageArea.appendChild(messageElement);
            }
        )
    }
    messageArea.scrollTop = messageArea.scrollHeight;
}

function setLength(evt) {
    evt.preventDefault();
    const lengthFromForm = document.querySelector('#length').value.trim();
    const isNumber = Number.isInteger(Number(length));
    if (isNumber) {
        lengthPage.classList.add('hidden');
        usernamePage.classList.remove('hidden');
        length = lengthFromForm;
        var socket = new SockJS('http://localhost:8080/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
}

typeAvtoButton.onclick = (evt) => {
    sendTypeAndLength(evt)

}
typeGenerateButton.onclick = (evt) => {
    sendTypeAndLength(evt)
}
// usernameForm.addEventListener('submit', connect, true)
setLengthForm.addEventListener('submit', setLength, true)

messageForm.addEventListener('submit', sendMessage, true)
