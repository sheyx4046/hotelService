document.addEventListener('DOMContentLoaded', function() {
    const addInputsButton = document.getElementById('addInputs');
    const removeLastButton = document.getElementById('removeLastInput');
    const inputContainer = document.getElementById('inputContainer');

    addInputsButton.addEventListener('click', function() {
        // Create the new roomtype input
        const newRoomTypeInput = document.createElement('input');
        newRoomTypeInput.type = 'text';
        newRoomTypeInput.name = 'roomtype';
        newRoomTypeInput.className = 'form-control mt-2'; // Bootstrap class for margin top
        newRoomTypeInput.placeholder = 'Room Type';

        // Create the new descriptions input
        const newDescriptionsInput = document.createElement('input');
        newDescriptionsInput.type = 'text';
        newDescriptionsInput.name = 'descriptions';
        newDescriptionsInput.className = 'form-control mt-2'; // Bootstrap class for margin top
        newDescriptionsInput.placeholder = 'Description';

        // Create a line break element
        const lineBreak = document.createElement('br');

        // Append the new inputs and line break to the inputContainer
        inputContainer.appendChild(newRoomTypeInput);
        inputContainer.appendChild(lineBreak);
        inputContainer.appendChild(newDescriptionsInput);
        inputContainer.appendChild(lineBreak);
    });

    removeLastButton.addEventListener('click', function() {
        const inputs = inputContainer.querySelectorAll('input[name="roomtype"], input[name="descriptions"]');
        const lineBreaks = inputContainer.getElementsByTagName('br');

        // Remove the last two input elements and line breaks
        if (inputs.length >= 2 && lineBreaks.length >= 1) {
            inputContainer.removeChild(inputs[inputs.length - 1]);
            inputContainer.removeChild(lineBreaks[lineBreaks.length - 1]);
            inputContainer.removeChild(inputs[inputs.length - 2]);
        }
    });

    const addRoomAmenitiesInputButton = document.getElementById('addRoomAmenitiesInput');
    const removeLastRoomAmenitiesInputButton = document.getElementById('removeLastRoomAmenitiesInput');
    const roomAmenitiesContainer = document.getElementById('roomAmenitiesContainer');

    addRoomAmenitiesInputButton.addEventListener('click', function() {
        // Create the new room amenities input
        const newRoomAmenitiesInput = document.createElement('input');
        newRoomAmenitiesInput.type = 'text';
        newRoomAmenitiesInput.name = 'roomAmenities';
        newRoomAmenitiesInput.className = 'form-control mt-2';
        newRoomAmenitiesInput.placeholder = 'Room Amenities';
        roomAmenitiesContainer.appendChild(newRoomAmenitiesInput);
    });

    removeLastRoomAmenitiesInputButton.addEventListener('click', function() {
        const roomAmenitiesInputs = roomAmenitiesContainer.querySelectorAll('input[name="roomAmenities"]');
        if (roomAmenitiesInputs.length > 1) {
            roomAmenitiesContainer.removeChild(roomAmenitiesInputs[roomAmenitiesInputs.length - 1]);
        }
    });

            const addOfferButton = document.getElementById("add-offer");
            const offerContainer = document.getElementById("offer-container");

            addOfferButton.addEventListener("click", function () {
                const newOffer = offerContainer.firstElementChild.cloneNode(true);
                newOffer.querySelector(".offer-input").value = "";
                const removeOfferButton = newOffer.querySelector(".remove-offer");

                removeOfferButton.addEventListener("click", function () {
                    if (offerContainer.childElementCount > 1) {
                        newOffer.remove();
                    }
                });

                offerContainer.appendChild(newOffer);
            });

            const addLangButton = document.getElementById("add-lang");
            const langContainer = document.getElementById("lang-container");

            addLangButton.addEventListener("click", function () {
                const newLang = langContainer.firstElementChild.cloneNode(true);
                newLang.querySelector(".language-input").value = "";
                const removeOfferButton = newLang.querySelector(".remove-lang");

                removeOfferButton.addEventListener("click", function () {
                    if (langContainer.childElementCount > 1) {
                        newLang.remove();
                    }
                });

                langContainer.appendChild(newLang);
            });
            
            const addEACButton = document.getElementById("add-eac");
            const eacContainer = document.getElementById("eac-container");

            addEACButton.addEventListener("click", function () {
                const newEAC = eacContainer.firstElementChild.cloneNode(true);
                newEAC.querySelector(".eac-input").value = "";
                const removeOfferButton = newEAC.querySelector(".remove-eac");

                removeOfferButton.addEventListener("click", function () {
                    if (eacContainer.childElementCount > 1) {
                        newEAC.remove();
                    }
                });

                eacContainer.appendChild(newEAC);
            });

});

document.getElementById("addInput").addEventListener("click", function () {
    const photoInputs = document.getElementById("photoInputs");
    const newInput = document.createElement("div");
    newInput.classList.add("input-group", "mb-2");
    newInput.innerHTML = `
        <input type="file" class="form-control" name="photos[]" accept="image/*">
        <button type="button" class="btn btn-danger" onclick="removeInput(this)">Remove</button>
    `;
    photoInputs.appendChild(newInput);
});

function removeInput(button) {
    const photoInputs = document.getElementById("photoInputs");
    if (photoInputs.children.length > 1) {
        button.parentElement.remove();
    } else {
    }
}