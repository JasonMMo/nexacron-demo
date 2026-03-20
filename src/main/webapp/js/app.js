// Nexacro Demo Application - Main JavaScript

class NexacroApp {
    constructor() {
        this.currentPage = 'login';
        this.userId = null;
        this.token = null;
        this.reminders = [];
        this.categories = [];
        this.currentReminderId = null;
        this.currentCategoryId = null;

        this.init();
    }

    init() {
        this.setupEventListeners();
        this.checkAuthentication();
    }

    setupEventListeners() {
        // Navigation
        document.querySelectorAll('.nav-link').forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                const page = e.target.dataset.page;
                if (page) this.navigateToPage(page);
            });
        });

        // Auth forms
        document.getElementById('login-form').addEventListener('submit', (e) => {
            e.preventDefault();
            this.handleLogin();
        });

        document.getElementById('signup-form').addEventListener('submit', (e) => {
            e.preventDefault();
            this.handleSignup();
        });

        // Page switching
        document.querySelectorAll('[data-switch]').forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                const targetPage = e.target.dataset.switch;
                this.showPage(targetPage);
            });
        });

        // Reminder actions
        document.getElementById('add-reminder-btn').addEventListener('click', () => {
            this.openReminderModal();
        });

        document.getElementById('reminder-form').addEventListener('submit', (e) => {
            e.preventDefault();
            this.saveReminder();
        });

        // Category actions
        document.getElementById('add-category-btn').addEventListener('click', () => {
            this.openCategoryModal();
        });

        document.getElementById('category-form').addEventListener('submit', (e) => {
            e.preventDefault();
            this.saveCategory();
        });

        // Filters and search
        document.getElementById('priority-filter').addEventListener('change', () => {
            this.loadReminders();
        });

        document.getElementById('search-input').addEventListener('input', (e) => {
            this.searchReminders(e.target.value);
        });

        // Modal close
        document.querySelectorAll('.close').forEach(closeBtn => {
            closeBtn.addEventListener('click', (e) => {
                e.target.closest('.modal').style.display = 'none';
            });
        });

        // Close modal on outside click
        window.addEventListener('click', (e) => {
            if (e.target.classList.contains('modal')) {
                e.target.style.display = 'none';
            }
        });
    }

    checkAuthentication() {
        const token = localStorage.getItem('token');
        const userId = localStorage.getItem('userId');

        if (token && userId) {
            this.token = token;
            this.userId = parseInt(userId);
            this.navigateToPage('reminders');
            this.loadReminders();
            this.loadCategories();
        } else {
            this.navigateToPage('login');
        }
    }

    navigateToPage(page) {
        // Update active nav link
        document.querySelectorAll('.nav-link').forEach(link => {
            link.classList.remove('active');
            if (link.dataset.page === page) {
                link.classList.add('active');
            }
        });

        this.showPage(page);
    }

    showPage(page) {
        // Hide all pages
        document.querySelectorAll('.page').forEach(p => {
            p.classList.remove('active');
        });

        // Show target page
        const targetPage = document.getElementById(`${page}-page`);
        if (targetPage) {
            targetPage.classList.add('active');
        }

        this.currentPage = page;

        // Load data for specific pages
        if (page === 'reminders' && this.userId) {
            this.loadReminders();
        } else if (page === 'categories' && this.userId) {
            this.loadCategories();
        }
    }

    async handleLogin() {
        const email = document.getElementById('login-email').value;
        const password = document.getElementById('login-password').value;

        try {
            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email, password })
            });

            if (response.ok) {
                const data = await response.json();
                this.token = data.token;
                this.userId = data.user.id;

                // Store in localStorage
                localStorage.setItem('token', this.token);
                localStorage.setItem('userId', this.userId);

                this.showToast('로그인 성공!', 'success');
                this.navigateToPage('reminders');
                this.loadReminders();
            } else {
                const error = await response.json();
                this.showToast(error.message || '로그인 실패', 'error');
            }
        } catch (error) {
            this.showToast('네트워크 오류가 발생했습니다', 'error');
        }
    }

    async handleSignup() {
        const name = document.getElementById('signup-name').value;
        const email = document.getElementById('signup-email').value;
        const password = document.getElementById('signup-password').value;

        try {
            const response = await fetch('/api/auth/signup', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ name, email, password })
            });

            if (response.status === 201) {
                this.showToast('회원가입 성공! 로그인해주세요', 'success');
                this.showPage('login');
            } else {
                const error = await response.json();
                this.showToast(error.message || '회원가입 실패', 'error');
            }
        } catch (error) {
            this.showToast('네트워크 오류가 발생했습니다', 'error');
        }
    }

    async loadReminders() {
        if (!this.userId) return;

        try {
            const priority = document.getElementById('priority-filter').value;
            const url = priority
                ? `/api/reminders/priority/${priority}?userId=${this.userId}`
                : `/api/reminders?userId=${this.userId}`;

            const response = await fetch(url, {
                headers: {
                    'X-User-Id': this.userId
                }
            });

            if (response.ok) {
                this.reminders = await response.json();
                this.renderReminders();
            }
        } catch (error) {
            this.showToast('리마인더를 불러오는 데 실패했습니다', 'error');
        }
    }

    async loadCategories() {
        if (!this.userId) return;

        try {
            const response = await fetch('/api/categories', {
                headers: {
                    'X-User-Id': this.userId
                }
            });

            if (response.ok) {
                this.categories = await response.json();
                this.renderCategories();
                this.updateCategorySelect();
            }
        } catch (error) {
            this.showToast('카테고리를 불러오는 데 실패했습니다', 'error');
        }
    }

    async saveReminder() {
        const title = document.getElementById('reminder-title').value;
        const description = document.getElementById('reminder-description').value;
        const priority = document.getElementById('reminder-priority').value;
        const categoryId = document.getElementById('reminder-category').value;
        const dueDate = document.getElementById('reminder-due-date').value;

        const reminderData = {
            title,
            description,
            priority,
            categoryId: categoryId ? parseInt(categoryId) : null,
            dueDate
        };

        const isEdit = this.currentReminderId !== null;
        const url = isEdit ? `/api/reminders/${this.currentReminderId}` : '/api/reminders';
        const method = isEdit ? 'PUT' : 'POST';

        try {
            const response = await fetch(url, {
                method,
                headers: {
                    'Content-Type': 'application/json',
                    'X-User-Id': this.userId
                },
                body: JSON.stringify(reminderData)
            });

            if (response.ok) {
                this.showToast(isEdit ? '리마인더가 수정되었습니다' : '리마인더가 생성되었습니다', 'success');
                document.getElementById('reminder-modal').style.display = 'none';
                this.loadReminders();
            } else {
                const error = await response.json();
                this.showToast(error.message || '저장에 실패했습니다', 'error');
            }
        } catch (error) {
            this.showToast('네트워크 오류가 발생했습니다', 'error');
        }
    }

    async saveCategory() {
        const name = document.getElementById('category-name').value;
        const description = document.getElementById('category-description').value;

        const categoryData = {
            name,
            description
        };

        const isEdit = this.currentCategoryId !== null;
        const url = isEdit ? `/api/categories/${this.currentCategoryId}` : '/api/categories';
        const method = isEdit ? 'PUT' : 'POST';

        try {
            const response = await fetch(url, {
                method,
                headers: {
                    'Content-Type': 'application/json',
                    'X-User-Id': this.userId
                },
                body: JSON.stringify(categoryData)
            });

            if (response.ok) {
                this.showToast(isEdit ? '카테고리가 수정되었습니다' : '카테고리가 생성되었습니다', 'success');
                document.getElementById('category-modal').style.display = 'none';
                this.loadCategories();
            } else {
                const error = await response.json();
                this.showToast(error.message || '저장에 실패했습니다', 'error');
            }
        } catch (error) {
            this.showToast('네트워크 오류가 발생했습니다', 'error');
        }
    }

    openReminderModal(reminder = null) {
        this.currentReminderId = reminder ? reminder.id : null;

        if (reminder) {
            document.getElementById('reminder-title').value = reminder.title;
            document.getElementById('reminder-description').value = reminder.description || '';
            document.getElementById('reminder-priority').value = reminder.priority || 'MEDIUM';
            document.getElementById('reminder-category').value = reminder.category?.id || '';
            document.getElementById('reminder-due-date').value = reminder.dueDate ?
                new Date(reminder.dueDate).toISOString().slice(0, 16) : '';
        } else {
            document.getElementById('reminder-form').reset();
        }

        document.getElementById('reminder-modal').style.display = 'block';
    }

    openCategoryModal(category = null) {
        this.currentCategoryId = category ? category.id : null;

        if (category) {
            document.getElementById('category-name').value = category.name;
            document.getElementById('category-description').value = category.description || '';
        } else {
            document.getElementById('category-form').reset();
        }

        document.getElementById('category-modal').style.display = 'block';
    }

    async deleteReminder(id) {
        if (!confirm('정�로 삭제하시겠습니까?')) return;

        try {
            const response = await fetch(`/api/reminders/${id}`, {
                method: 'DELETE',
                headers: {
                    'X-User-Id': this.userId
                }
            });

            if (response.ok) {
                this.showToast('리마인더가 삭제되었습니다', 'success');
                this.loadReminders();
            } else {
                this.showToast('삭제에 실패했습니다', 'error');
            }
        } catch (error) {
            this.showToast('네트워크 오류가 발생했습니다', 'error');
        }
    }

    async toggleReminderComplete(id) {
        try {
            const response = await fetch(`/api/reminders/${id}/complete`, {
                method: 'PATCH',
                headers: {
                    'X-User-Id': this.userId
                }
            });

            if (response.ok) {
                this.showToast('완료 상태가 변경되었습니다', 'success');
                this.loadReminders();
            } else {
                this.showToast('변경에 실패했습니다', 'error');
            }
        } catch (error) {
            this.showToast('네트워크 오류가 발생했습니다', 'error');
        }
    }

    async deleteCategory(id) {
        if (!confirm('카테고리를 삭제하면 연결된 모든 리마인더가 삭제됩니다. 정말 삭제하시겠습니까?')) return;

        try {
            const response = await fetch(`/api/categories/${id}`, {
                method: 'DELETE',
                headers: {
                    'X-User-Id': this.userId
                }
            });

            if (response.ok) {
                this.showToast('카테고리가 삭제되었습니다', 'success');
                this.loadCategories();
                this.loadReminders();
            } else {
                this.showToast('삭제에 실패했습니다', 'error');
            }
        } catch (error) {
            this.showToast('네트워크 오류가 발생했습니다', 'error');
        }
    }

    renderReminders() {
        const container = document.getElementById('reminders-list');
        container.innerHTML = '';

        if (this.reminders.length === 0) {
            container.innerHTML = '<p style="text-align: center; color: #999;">리마인더가 없습니다</p>';
            return;
        }

        this.reminders.forEach(reminder => {
            const card = document.createElement('div');
            card.className = 'reminder-card';
            card.innerHTML = `
                <div class="reminder-header">
                    <h3 class="reminder-title">${this.escapeHtml(reminder.title)}</h3>
                    <span class="reminder-priority priority-${reminder.priority.toLowerCase()}">${reminder.priority}</span>
                </div>
                ${reminder.description ? `<p class="reminder-description">${this.escapeHtml(reminder.description)}</p>` : ''}
                ${reminder.category ? `<p class="reminder-meta">카테고리: ${this.escapeHtml(reminder.category.name)}</p>` : ''}
                ${reminder.dueDate ? `<p class="reminder-meta">기한: ${new Date(reminder.dueDate).toLocaleString('ko-KR')}</p>` : ''}
                <div class="reminder-footer">
                    <span>${reminder.completed ? '✅ 완료' : '⏳ 미완료'}</span>
                    <div class="reminder-actions-list">
                        <button onclick="app.toggleReminderComplete(${reminder.id})"
                                class="${reminder.completed ? 'incomplete-btn' : 'complete-btn'}">
                            ${reminder.completed ? '미완료' : '완료'}
                        </button>
                        <button onclick="app.openReminderModal(${JSON.stringify(reminder).replace(/"/g, '&quot;')})"
                                class="edit-btn">수정</button>
                        <button onclick="app.deleteReminder(${reminder.id})"
                                class="delete-btn">삭제</button>
                    </div>
                </div>
            `;
            container.appendChild(card);
        });
    }

    renderCategories() {
        const container = document.getElementById('categories-list');
        container.innerHTML = '';

        if (this.categories.length === 0) {
            container.innerHTML = '<p style="text-align: center; color: #999;">카테고리가 없습니다</p>';
            return;
        }

        this.categories.forEach(category => {
            const card = document.createElement('div');
            card.className = 'category-card';
            card.innerHTML = `
                <div class="category-info">
                    <h4>${this.escapeHtml(category.name)}</h4>
                    ${category.description ? `<p>${this.escapeHtml(category.description)}</p>` : ''}
                </div>
                <div class="category-count">${category.reminderCount || 0}개</div>
                <div class="reminder-actions-list">
                    <button onclick="app.openCategoryModal(${JSON.stringify(category).replace(/"/g, '&quot;')})"
                            class="edit-btn">수정</button>
                    <button onclick="app.deleteCategory(${category.id})"
                            class="delete-btn">삭제</button>
                </div>
            `;
            container.appendChild(card);
        });
    }

    updateCategorySelect() {
        const select = document.getElementById('reminder-category');
        const currentValue = select.value;

        select.innerHTML = '<option value="">없음</option>';

        this.categories.forEach(category => {
            const option = document.createElement('option');
            option.value = category.id;
            option.textContent = category.name;
            select.appendChild(option);
        });

        select.value = currentValue;
    }

    searchReminders(keyword) {
        if (!keyword.trim()) {
            this.loadReminders();
            return;
        }

        fetch(`/api/reminders/search?keyword=${encodeURIComponent(keyword)}&userId=${this.userId}`, {
            headers: {
                'X-User-Id': this.userId
            }
        })
        .then(response => response.json())
        .then(data => {
            this.reminders = data;
            this.renderReminders();
        })
        .catch(error => {
            this.showToast('검색에 실패했습니다', 'error');
        });
    }

    showToast(message, type = 'info') {
        // Remove existing toast
        const existingToast = document.querySelector('.toast');
        if (existingToast) {
            existingToast.remove();
        }

        // Create new toast
        const toast = document.createElement('div');
        toast.className = `toast ${type}`;
        toast.textContent = message;
        document.body.appendChild(toast);

        // Show toast
        setTimeout(() => toast.classList.add('show'), 10);

        // Hide toast after 3 seconds
        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => toast.remove(), 300);
        }, 3000);
    }

    escapeHtml(text) {
        const map = {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#039;'
        };
        return text.replace(/[&<>"']/g, m => map[m]);
    }
}

// Initialize the application
const app = new NexacroApp();