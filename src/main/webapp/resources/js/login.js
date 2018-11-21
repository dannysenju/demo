/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function showContactInfo() {
    document.getElementById("menu-contact").classList.toggle("show");
}

function hideContactInfo() {
    if (!event.target.matches('.contact')) {

    var dropdowns = document.getElementsByClassName("contact");
    var i;
    for (i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      if (openDropdown.classList.contains('show')) {
        openDropdown.classList.remove('show');
      }
    }
  }
}