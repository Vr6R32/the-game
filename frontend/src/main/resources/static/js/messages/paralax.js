function paralaxHover() {
    const config = {
        rotation: 0.8,
    };

    document.addEventListener('mousemove', handleMouseMove);
    let paralaxContainer = document.getElementById('paralax-hover');
    paralaxContainer.style.animation = 'none';

    function handleMouseMove(e) {
        const rect = paralaxContainer.getBoundingClientRect();
        const centerX = rect.left + rect.width / 2;
        const centerY = rect.top + rect.height / 2;
        const deltaX = e.clientX - centerX;
        const deltaY = e.clientY - centerY;
        const rotateX = deltaY / (config.rotation * 100);
        const rotateY = -deltaX / (config.rotation * 100);
        paralaxContainer.style.transform = `perspective(1500px) rotateX(${rotateX}deg) rotateY(${rotateY}deg) scale(0.99)`;
    }
}