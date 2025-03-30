document.addEventListener("DOMContentLoaded", function() {
  const toggleButtons = document.querySelectorAll('.toggle-button');

  toggleButtons.forEach(button => {
    button.addEventListener('click', function() {
      const targetId = this.getAttribute('data-target');
      const targetContent = document.getElementById(targetId);

      // Bascule les classes
      this.classList.toggle('collapsed');
      targetContent.classList.toggle('hidden');
    });
  });
});
