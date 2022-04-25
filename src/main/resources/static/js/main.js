'use strict';

const usernamePage = document.querySelector('#type-page');
const lengthPage = document.querySelector('#length-page');
const chatPage = document.querySelector('#chat-page');
const typeAvtoButton = document.querySelector('#typeAvtoButton');
const typeGenerateButton = document.querySelector('#typeGenerateButton');
const messageArea = document.querySelector('#messageArea');
const arrayArea = document.querySelector('#arrayArea');
const connectingElement = document.querySelector('.connecting');
const setLengthForm = document.querySelector('#setLengthForm');

let stompClient = null;
let username = null;
let length = null;

let indexRowTable = 1;

function connectView(event) {
    event.preventDefault();
    const isNumber = Number.isInteger(Number(length));
    username = event.originalTarget.value;
    if (username && isNumber) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');
    }
}


function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);
    // Tell your username to the server
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({
            length: length,
            type: 'JOIN'
        })
    )
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function sendTypeAndLength(event) {
    event.preventDefault();
    connectView(event)
    if (stompClient) {
        const chatMessage = {
            algorithm: username,
            type: username
        };
        stompClient.send("/app/chat.sendTypeAndLength", {}, JSON.stringify(chatMessage));
    }
}

function onMessageReceived(payload) {
    connectingElement.classList.add('hidden');
    let message = JSON.parse(payload.body);
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
    // какой тип выбрали
    if (message.type === "AUTO") {
        let messageElement = document.createElement('tr');
        messageElement.classList.add('chat-message');
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
        const prop = Object.entries(message.sequenceGenerate);
        prop.forEach(
            (element) => {
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
        let socket = new SockJS('http://localhost:8080/ws');
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
setLengthForm.addEventListener('submit', setLength, true)

