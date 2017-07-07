//validatePartySize
// - check party size of "people" element (should be text input)
// @param min   min party size
// @param max   max party size
// @return      true if "people" element value > min && < max
function validatePartySize(min, max) {
  inputPartySize = document.getElementById("people").value;
  if (inputPartySize < 1 || inputPartySize > 10) {
    window.alert("Please enter a party size of 1 - 10 humans (you entered " +
    inputPartySize + ")");
    return false;
  } else {
      return true;
  }
}
