document.addEventListener('DOMContentLoaded', () => {

    // --- Notification Popover Toggle ---
    const notifBtn = document.getElementById('notif-btn');
    const notifDropdown = document.getElementById('notif-dropdown');
    const closeNotifBtn = document.getElementById('close-notif');

    if (notifBtn && notifDropdown) {
        notifBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            notifDropdown.classList.toggle('show');
        });
        
        // Hide if clicked out
        document.addEventListener('click', (e) => {
            if (!notifDropdown.contains(e.target) && !notifBtn.contains(e.target)) {
                notifDropdown.classList.remove('show');
            }
        });
        
        if (closeNotifBtn) {
            closeNotifBtn.addEventListener('click', () => {
                notifDropdown.classList.remove('show');
            });
        }
    }

    // --- Logout Modal Toggle ---
    const logoutBtn = document.getElementById('logout-btn');
    const logoutModal = document.getElementById('logout-modal');
    const cancelLogoutBtn = document.getElementById('cancel-logout');

    if (logoutBtn && logoutModal) {
        logoutBtn.addEventListener('click', (e) => {
            e.preventDefault();
            logoutModal.classList.add('show');
        });

        if (cancelLogoutBtn) {
            cancelLogoutBtn.addEventListener('click', () => {
                logoutModal.classList.remove('show');
            });
        }
    }

    // --- Toast System ---
    window.showToast = function(type, message) {
        const toastContainer = document.getElementById('toast-container');
        if (!toastContainer) return;

        const toast = document.createElement('div');
        toast.className = `toast-item ${type}`;
        
        const icon = type === 'success' ? 'ph-check-circle' : 'ph-warning-circle';
        
        toast.innerHTML = `
            <i class="${icon}"></i>
            <span>${message}</span>
        `;
        
        toastContainer.appendChild(toast);
        
        setTimeout(() => toast.classList.add('show'), 100);
        
        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => toast.remove(), 400); // Wait for transition
        }, 3000);
    };

    // Thử gọi toast nếu có tham số (phát triển)
    // showToast('success', 'Thành công: Cập nhật giá điện nước');
    
    // Khởi tạo đồ thị
    initCharts();
});

function initCharts() {
    // Check if Chart object exists
    if (typeof Chart === 'undefined') return;

    // Gradient styling overrides for ChartJS globally
    Chart.defaults.font.family = "'Inter', sans-serif";
    Chart.defaults.color = '#8E8E93';

    // 1. Doanh thu Chart
    const ctx1 = document.getElementById('revenueChart');
    if (ctx1) {
        const gradient = ctx1.getContext('2d').createLinearGradient(0, 0, 0, 400);
        gradient.addColorStop(0, 'rgba(10, 132, 255, 0.4)');
        gradient.addColorStop(1, 'rgba(10, 132, 255, 0.0)');

        new Chart(ctx1, {
            type: 'line',
            data: {
                labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10'],
                datasets: [{
                    label: 'Doanh thu',
                    data: [10000000, 28000000, 20000000, 40000000, 30000000, 60000000, 42000000, 75000000, 52000000, 72000000],
                    borderColor: '#0A84FF',
                    backgroundColor: gradient,
                    borderWidth: 3,
                    fill: true,
                    tension: 0.4, // Smooth curve
                    pointRadius: 0, // No points unless hovered
                    pointHoverRadius: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false },
                    tooltip: {
                        backgroundColor: '#1C1C1E',
                        padding: 12,
                        cornerRadius: 8,
                        titleFont: { size: 14, family: 'Inter' },
                        bodyFont: { size: 14, family: 'Inter' },
                        callbacks: {
                            label: function(context) {
                                let value = context.parsed.y;
                                return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
                            }
                        }
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        grid: { color: '#E5E5EA', drawBorder: false },
                        ticks: {
                            callback: function(value) {
                                return (value / 1000000) + '.000.000 đ';
                            }
                        }
                    },
                    x: {
                        grid: { display: false, drawBorder: false }
                    }
                }
            }
        });
    }

    // 2. Utility Usage Chart
    const ctx2 = document.getElementById('utilityChart');
    if (ctx2) {
        const gradient2 = ctx2.getContext('2d').createLinearGradient(0, 0, 0, 400);
        gradient2.addColorStop(0, 'rgba(10, 132, 255, 0.4)');
        gradient2.addColorStop(1, 'rgba(10, 132, 255, 0.0)');

        new Chart(ctx2, {
            type: 'line',
            data: {
                labels: ['10', '13', '14', '15', '16', '18'],
                datasets: [{
                    label: 'Sử dụng (kWh/m3)',
                    data: [75, 45, 125, 78, 65, 135],
                    borderColor: '#0A84FF',
                    backgroundColor: gradient2,
                    borderWidth: 3,
                    fill: true,
                    tension: 0.4,
                    pointRadius: 0,
                    pointHoverRadius: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        grid: { color: '#E5E5EA', drawBorder: false },
                        max: 160,
                        ticks: { stepSize: 40 }
                    },
                    x: {
                        grid: { display: false, drawBorder: false }
                    }
                }
            }
        });
    }
});
