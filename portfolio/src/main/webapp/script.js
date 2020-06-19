// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Generates a URL for a random image in the images directory and adds an img
 * element with that URL to the page.
 */
function randomizeImage() {
  // The images directory contains 13 images, so generate a random index between
  // 1 and 13.
  const imageIndex = Math.floor(Math.random() * 10) + 1;
  const imgUrl = 'images/cedric-' + imageIndex + '.jpg';

  const imgElement = document.createElement('img');
  imgElement.src = imgUrl;

  const imageContainer = document.getElementById('random-image-container');
  // Remove the previous image.
  imageContainer.innerHTML = '';
  imageContainer.appendChild(imgElement);
}

async function getRandomQuoteUsingAsyncAwait() {
  const response = await fetch('/random-quote');
  const quote = await response.text();
  document.getElementById('quote-container').innerText = quote;
}


/**
 * Fetches stats from the servers and adds them to the DOM.
 */
function getMessages() {
  fetch('/data').then(response => response.json()).then((messages) => {

    const statsListElement = document.getElementById('messages-container');
    statsListElement.innerHTML = '';
    
    statsListElement.appendChild(
        createListElement('Message 1: ' + messages.message_1));
    statsListElement.appendChild(
        createListElement('Message 2: ' + messages.message_2));
    statsListElement.appendChild(
        createListElement('Message 3: ' + messages.message_3));
  });
}

/**
 * Fetches the current comments and show it on the page.
 */
function getComments() {
  fetch('/data').then(response => response.json()).then((allComments) => {
    // Build the list of history entries.
    const historyEl = document.getElementById('history');
    allComments.forEach((line) => {
      historyEl.appendChild(createListElement(line));
    });
  });
}

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}

/** Retrieves the message and language to be translated into and sends
    a POST request to the translation servlet at /translate */
function requestTranslation() {
const text = document.getElementById('text').value;
const languageCode = document.getElementById('language').value;

const resultContainer = document.getElementById('result');
resultContainer.innerText = 'Loading...';

const params = new URLSearchParams();
params.append('text', text);
params.append('languageCode', languageCode);

fetch('/translate', {
    method: 'POST',
    body: params
}).then(response => response.text())
.then((translatedMessage) => {
    resultContainer.innerText = translatedMessage;
});
}