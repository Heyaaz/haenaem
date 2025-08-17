// 글로벌 변수
let currentUserId = localStorage.getItem('currentUserId') || null;
let currentUserData = JSON.parse(localStorage.getItem('currentUserData')) || null;
let currentTodoId = null;
let currentItem = null;
let currentRoom = null;

// 글로벌 로딩 오버레이 생성
function createLoadingOverlay() {
  if (!document.getElementById('loading-overlay')) {
    const overlay = document.createElement('div');
    overlay.id = 'loading-overlay';
    overlay.className = 'loading-overlay';
    overlay.innerHTML = '<div class="loading-spinner"></div>';
    document.body.appendChild(overlay);
  }
}

function showLoading(show = true) {
  createLoadingOverlay();
  const overlay = document.getElementById('loading-overlay');
  if (show) {
    overlay.classList.add('active');
  } else {
    overlay.classList.remove('active');
  }
}

// 알림 시스템
function showNotification(message, type = 'success') {
  const notification = document.createElement('div');
  notification.className = `notification ${type}`;
  notification.textContent = message;
  document.body.appendChild(notification);
  
  setTimeout(() => notification.classList.add('show'), 100);
  
  setTimeout(() => {
    notification.classList.remove('show');
    setTimeout(() => notification.remove(), 300);
  }, 3000);
}

// 폼 검증 함수들
function validateEmail(email) {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

function validatePassword(password) {
  return {
    isValid: password.length >= 6,
    messages: [
      password.length >= 6 ? '' : '비밀번호는 최소 6자 이상이어야 합니다.'
    ].filter(Boolean)
  };
}

function validateForm(formElement) {
  const errors = [];
  const inputs = formElement.querySelectorAll('input[required], textarea[required]');
  
  inputs.forEach(input => {
    const value = input.value.trim();
    const fieldName = input.name || input.type;
    
    if (!value) {
      errors.push(`${getFieldDisplayName(fieldName)}은(는) 필수 항목입니다.`);
      input.classList.add('error');
    } else {
      input.classList.remove('error');
      
      if (input.type === 'email' && !validateEmail(value)) {
        errors.push('올바른 이메일 형식을 입력해 주세요.');
        input.classList.add('error');
      }
      
      if (input.type === 'password') {
        const validation = validatePassword(value);
        if (!validation.isValid) {
          errors.push(...validation.messages);
          input.classList.add('error');
        }
      }
    }
  });
  
  return { isValid: errors.length === 0, errors };
}

function getFieldDisplayName(fieldName) {
  const fieldNames = {
    'email': '이메일',
    'password': '비밀번호',
    'nickname': '닉네임',
    'title': '제목',
    'description': '설명'
  };
  return fieldNames[fieldName] || fieldName;
}

function displayFormErrors(errors, container) {
  const errorContainer = container || document.createElement('div');
  errorContainer.className = 'error-message';
  errorContainer.innerHTML = errors.map(error => `<div>${error}</div>`).join('');
  
  if (!container) {
    const form = document.querySelector('form:last-of-type');
    if (form) {
      form.insertBefore(errorContainer, form.firstChild);
      setTimeout(() => errorContainer.remove(), 5000);
    }
  }
}

// API 호출 헬퍼 함수 (개선된 버전)
async function apiFetch(url, options = {}) {
  const startTime = Date.now();
  
  try {
    console.log(`API 호출: ${url}`, options);
    
    showLoading(true);
    const response = await fetch(url, options);
    
    console.log(`응답 상태: ${response.status}`, response);
    
    if (!response.ok) {
      const contentType = response.headers.get('content-type');
      let errorMessage = `HTTP ${response.status}: ${response.statusText}`;
      
      if (contentType && contentType.includes('application/json')) {
        try {
          const errorData = await response.json();
          errorMessage = errorData.message || errorMessage;
        } catch (jsonError) {
          console.error('JSON 파싱 실패:', jsonError);
        }
      } else {
        const textResponse = await response.text();
        console.error('Non-JSON 응답:', textResponse);
        
        if (textResponse.includes('Forbidden') || response.status === 403) {
          errorMessage = '접근 권한이 없습니다. 로그인이 필요하거나 권한이 부족합니다.';
        } else if (textResponse.includes('Unauthorized') || response.status === 401) {
          errorMessage = '인증이 필요합니다. 로그인해 주세요.';
        }
      }
      
      throw new Error(errorMessage);
    }
    
    if (response.status === 204) return null;
    
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
      return await response.json();
    } else {
      const textResponse = await response.text();
      console.warn('Non-JSON 응답 받음:', textResponse);
      return { message: 'Success', data: textResponse };
    }
  } catch (error) {
    console.error('API 에러:', error);
    
    // 알림 시스템 사용으로 변경
    showNotification(error.message, 'error');
    throw error;
  } finally {
    // 최소 로딩 시간 보장 (UX 개선)
    const elapsed = Date.now() - startTime;
    const minLoadingTime = 500;
    
    if (elapsed < minLoadingTime) {
      setTimeout(() => showLoading(false), minLoadingTime - elapsed);
    } else {
      showLoading(false);
    }
  }
}

// 헬퍼 함수들
function displayJson(element, data) {
  element.textContent = JSON.stringify(data, null, 2);
}

function checkLogin() {
  if (!currentUserId) {
    alert('먼저 로그인해주세요.');
    return false;
  }
  return true;
}

function saveUserData(userData) {
  currentUserData = userData;
  currentUserId = userData.id;
  localStorage.setItem('currentUserData', JSON.stringify(userData));
  localStorage.setItem('currentUserId', userData.id);
  updateUserStatus();
  updateNavigation();
}

function logout() {
  currentUserData = null;
  currentUserId = null;
  localStorage.removeItem('currentUserData');
  localStorage.removeItem('currentUserId');
  alert('로그아웃되었습니다.');
  updateUserStatus();
  updateNavigation();
  showPage('home');
}

// 프로필 표시 업데이트 함수
function updateProfileDisplay() {
  const authForms = document.getElementById('auth-forms');
  const profileInfo = document.getElementById('profile-info');
  
  if (currentUserData) {
    // 로그인 상태: 프로필 표시
    authForms.style.display = 'none';
    profileInfo.style.display = 'block';
    
    // 프로필 정보 업데이트
    document.getElementById('profile-nickname').textContent = currentUserData.nickname;
    document.getElementById('profile-email').textContent = currentUserData.email;
    document.getElementById('profile-points').textContent = currentUserData.currentPoint || 0;
  } else {
    // 로그아웃 상태: 로그인/회원가입 폼 표시
    authForms.style.display = 'block';
    profileInfo.style.display = 'none';
  }
}

// 페이지 전환 함수
function showPage(pageName) {
  // 모든 페이지 숨기기
  const pages = document.querySelectorAll('.page');
  pages.forEach(page => page.classList.remove('active'));
  
  // 네비게이션 활성화 상태 업데이트
  const navLinks = document.querySelectorAll('.nav-links a');
  navLinks.forEach(link => link.classList.remove('active'));
  
  // 선택된 페이지 표시
  const targetPage = document.getElementById(`${pageName}-page`);
  if (targetPage) {
    targetPage.classList.add('active');
  }
  
  // 선택된 네비게이션 활성화
  const targetNav = document.getElementById(`nav-${pageName}`);
  if (targetNav) {
    targetNav.classList.add('active');
  }
  
  // 페이지별 초기화 로직
  if (pageName === 'users') {
    updateProfileDisplay();
    if (currentUserId) {
      document.getElementById('todo-userId').value = currentUserId;
    }
  }
  if (pageName === 'inventory' && currentUserId) {
    setTimeout(loadInventory, 100);
  }
  if (pageName === 'room' && currentUserId) {
    setTimeout(loadUserRoom, 100);
  }
}

// 사용자 상태 업데이트
function updateUserStatus() {
  const userStatusDiv = document.getElementById('user-status');
  if (currentUserData) {
    userStatusDiv.innerHTML = `
      <div class="user-info">
        <p>로그인된 사용자: <strong>${currentUserData.nickname}</strong> (${currentUserData.email})</p>
        <p>포인트: <strong>${currentUserData.currentPoint}</strong>점</p>
      </div>
    `;
  } else {
    userStatusDiv.innerHTML = `
      <div class="login-prompt">
        <p>더 많은 기능을 이용하려면 <a href="#" onclick="showPage('users')">로그인</a>해주세요.</p>
      </div>
    `;
  }
}

// 네비게이션 업데이트
function updateNavigation() {
  const logoutBtn = document.getElementById('logout-btn');
  if (currentUserId) {
    logoutBtn.style.display = 'inline-block';
  } else {
    logoutBtn.style.display = 'none';
  }
}

// === 사용자 관리 기능 ===
function initUserManagement() {
  // 회원가입
  document.getElementById('register-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    // 폼 검증
    const validation = validateForm(e.target);
    if (!validation.isValid) {
      displayFormErrors(validation.errors);
      return;
    }
    
    const formData = new FormData(e.target);
    
    try {
      const data = await apiFetch('/api/users', {
        method: 'POST',
        body: formData
      });
      showNotification('회원가입 성공!', 'success');
      e.target.reset();
    } catch (error) {
      console.error('회원가입 실패:', error);
    }
  });

  // 로그인
  document.getElementById('login-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const validation = validateForm(e.target);
    if (!validation.isValid) {
      displayFormErrors(validation.errors);
      return;
    }
    
    const formData = new FormData(e.target);
    const body = Object.fromEntries(formData.entries());
    
    try {
      const data = await apiFetch('/api/users/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
      });
      showNotification('로그인 성공!', 'success');
      saveUserData(data);
      document.getElementById('todo-userId').value = currentUserId;
      e.target.reset();
      updateProfileDisplay();
    } catch (error) {
      console.error('로그인 실패:', error);
    }
  });

  // 프로필 새로고침
  document.getElementById('refresh-profile-btn').addEventListener('click', async () => {
    if (!checkLogin()) return;
    
    try {
      const data = await apiFetch(`/api/users/me/${currentUserId}`);
      saveUserData(data);
      updateProfileDisplay();
      showNotification('프로필 새로고침 완료!', 'success');
    } catch (error) {
      console.error('프로필 새로고침 실패:', error);
    }
  });

  // 프로필 수정 폼 토글
  document.getElementById('edit-profile-btn').addEventListener('click', () => {
    if (!currentUserData) return;
    
    // 현재 값으로 폼 채우기
    document.querySelector('#update-user-form input[name="nickname"]').value = currentUserData.nickname;
    document.getElementById('edit-profile-form').style.display = 'block';
  });

  // 프로필 수정 취소
  document.getElementById('cancel-edit-profile-btn').addEventListener('click', () => {
    document.getElementById('edit-profile-form').style.display = 'none';
    document.getElementById('update-user-form').reset();
  });

  // 프로필 수정
  document.getElementById('update-user-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!checkLogin()) return;

    const validation = validateForm(e.target);
    if (!validation.isValid) {
      displayFormErrors(validation.errors);
      return;
    }

    const formData = new FormData(e.target);
    const body = Object.fromEntries(formData.entries());
    
    try {
      const data = await apiFetch(`/api/users/update/${currentUserId}`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
      });
      showNotification('프로필 수정 완료!', 'success');
      saveUserData(data);
      updateProfileDisplay();
      document.getElementById('edit-profile-form').style.display = 'none';
      document.getElementById('update-user-form').reset();
    } catch (error) {
      console.error('프로필 수정 실패:', error);
    }
  });
}

// === 투두 관리 기능 ===
function initTodoManagement() {
  // 투두 생성
  document.getElementById('create-todo-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!checkLogin()) return;

    const formData = new FormData(e.target);
    const body = Object.fromEntries(formData.entries());
    body.userId = currentUserId;

    try {
      const data = await apiFetch('/api/todos/create', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
      });
      alert('투두 생성 성공!');
      e.target.reset();
      document.getElementById('todo-userId').value = currentUserId;
      
      const createdTodo = await apiFetch(`/api/todos/${data.id}`);
      displayTodoDetail(createdTodo);
    } catch (error) {
      console.error('투두 생성 실패:', error);
    }
  });

  // 투두 수정
  document.getElementById('update-todo-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!currentTodoId) return;

    const formData = new FormData(e.target);
    const body = Object.fromEntries(formData.entries());

    try {
      const data = await apiFetch(`/api/todos/update/${currentTodoId}`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
      });
      alert('투두 수정 성공!');
      displayTodoDetail(data);
      document.getElementById('edit-todo-form').style.display = 'none';
    } catch (error) {
      console.error('투두 수정 실패:', error);
    }
  });

  // 투두 삭제
  document.getElementById('delete-todo-btn').addEventListener('click', async () => {
    if (!currentTodoId) return;
    if (!confirm('정말로 이 투두를 삭제하시겠습니까?')) return;

    try {
      await apiFetch(`/api/todos/delete/${currentTodoId}`, {
        method: 'DELETE'
      });
      alert('투두 삭제 성공!');
      document.getElementById('todo-detail').style.display = 'none';
      currentTodoId = null;
    } catch (error) {
      console.error('투두 삭제 실패:', error);
    }
  });

  // 수정 버튼
  document.getElementById('edit-todo-btn').addEventListener('click', () => {
    if (currentTodoId && currentUserData) {
      const currentData = JSON.parse(document.getElementById('todo-display').textContent);
      document.querySelector('#update-todo-form input[name="title"]').value = currentData.title;
      document.querySelector('#update-todo-form textarea[name="description"]').value = currentData.description || '';
      document.getElementById('edit-todo-form').style.display = 'block';
    }
  });

  // 수정 취소 버튼
  document.getElementById('cancel-edit-btn').addEventListener('click', () => {
    document.getElementById('edit-todo-form').style.display = 'none';
    document.getElementById('update-todo-form').reset();
  });
}

function displayTodoDetail(todo) {
  displayJson(document.getElementById('todo-display'), todo);
  document.getElementById('todo-detail').style.display = 'block';
  currentTodoId = todo.id;
}

// === 상점 기능 ===
function initShop() {
  // 모든 상품 조회
  document.getElementById('get-shop-items-btn').addEventListener('click', async () => {
    try {
      const items = await apiFetch('/api/shop/items');
      displayShopItems(items, '모든 아이템');
    } catch (error) {
      console.error('상품 조회 실패:', error);
    }
  });

  // 활성화된 상품만 조회
  document.getElementById('get-active-items-btn').addEventListener('click', async () => {
    try {
      const items = await apiFetch('/api/shop/items/active');
      displayShopItems(items, '활성화된 아이템');
    } catch (error) {
      console.error('활성화된 상품 조회 실패:', error);
    }
  });

  // 아이템 구매
  document.getElementById('purchase-item-btn').addEventListener('click', async () => {
    if (!checkLogin() || !currentItem) return;
    
    const currentData = JSON.parse(document.getElementById('item-display').textContent);
    if (!confirm(`"${currentData.name}"을(를) ${currentData.price}포인트로 구매하시겠습니까?`)) return;

    try {
      const result = await apiFetch(`/api/shop/users/${currentUserId}/items/${currentItem.id}/purchase`, {
        method: 'POST'
      });
      alert('아이템 구매 성공!');
      displayJson(document.getElementById('purchase-display'), result);
      document.getElementById('purchase-result').style.display = 'block';
      
      if (result.user) {
        saveUserData(result.user);
      }
    } catch (error) {
      console.error('아이템 구매 실패:', error);
    }
  });
}

function displayShopItems(items, title) {
  const shopItemsGrid = document.getElementById('shop-items-grid');
  shopItemsGrid.innerHTML = `<h4>${title} (${items.length}개)</h4>`;
  
  if (items.length === 0) {
    shopItemsGrid.innerHTML += '<p>표시할 아이템이 없습니다.</p>';
    return;
  }

  const grid = document.createElement('div');
  grid.className = 'items-grid';
  
  items.forEach(item => {
    const itemCard = document.createElement('div');
    itemCard.className = 'item-card';
    itemCard.innerHTML = `
      <div class="item-info">
        <h4>${item.name}</h4>
        <p class="item-price">${item.price} 포인트</p>
        <p class="item-description">${item.description || '설명 없음'}</p>
        ${item.active ? '<span class="item-status active">판매중</span>' : '<span class="item-status inactive">판매중지</span>'}
      </div>
    `;
    
    itemCard.addEventListener('click', () => {
      currentItem = item;
      displayJson(document.getElementById('item-display'), item);
      document.getElementById('item-detail').style.display = 'block';
      document.getElementById('purchase-result').style.display = 'none';
    });
    
    grid.appendChild(itemCard);
  });
  
  shopItemsGrid.appendChild(grid);
}

// === 인벤토리 기능 ===
function initInventory() {
  // 인벤토리 새로고침
  document.getElementById('get-inventory-btn').addEventListener('click', () => {
    if (!checkLogin()) return;
    loadInventory();
  });

  // 아이템 장착
  document.getElementById('equip-item-btn').addEventListener('click', async () => {
    if (!currentItem || !checkLogin()) return;

    try {
      await apiFetch(`/api/inventory/users/${currentUserId}/items/${currentItem.id}/equip`, { 
        method: 'POST' 
      });
      alert('장착 성공!');
      loadInventory();
      document.getElementById('inventory-item-detail').style.display = 'none';
    } catch(error) {
      console.error('장착 실패:', error);
    }
  });

  // 아이템 장착해제
  document.getElementById('unequip-item-btn').addEventListener('click', async () => {
    if (!currentItem || !checkLogin()) return;

    try {
      await apiFetch(`/api/inventory/users/${currentUserId}/items/${currentItem.id}/unequip`, { 
        method: 'POST' 
      });
      alert('장착 해제 성공!');
      loadInventory();
      document.getElementById('inventory-item-detail').style.display = 'none';
    } catch(error) {
      console.error('장착 해제 실패:', error);
    }
  });

  // 방에 배치
  document.getElementById('place-room-btn').addEventListener('click', async () => {
    if (!currentItem || !checkLogin()) return;

    const x = Math.floor(Math.random() * 400);
    const y = Math.floor(Math.random() * 200);
    
    try {
      await apiFetch(`/api/room/users/${currentUserId}/items/place`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          inventoryItemId: currentItem.id,
          positionX: x,
          positionY: y
        })
      });
      alert('아이템을 방에 배치했습니다!');
      document.getElementById('inventory-item-detail').style.display = 'none';
    } catch (error) {
      console.error('아이템 배치 실패:', error);
    }
  });
}

async function loadInventory() {
  try {
    const [equippedItems, unequippedItems] = await Promise.all([
      apiFetch(`/api/inventory/users/${currentUserId}/equipped`),
      apiFetch(`/api/inventory/users/${currentUserId}/unequipped`)
    ]);
    
    displayInventoryItems(equippedItems, document.getElementById('equipped-items-grid'), '장착한 아이템', true);
    displayInventoryItems(unequippedItems, document.getElementById('unequipped-items-grid'), '장착하지 않은 아이템', false);
  } catch (error) {
    console.error('인벤토리 로드 실패:', error);
  }
}

function displayInventoryItems(items, container, title, isEquipped) {
  container.innerHTML = `<h4>${title} (${items.length}개)</h4>`;
  
  if (items.length === 0) {
    container.innerHTML += '<p>아이템이 없습니다.</p>';
    return;
  }

  const grid = document.createElement('div');
  grid.className = 'items-grid';
  
  items.forEach(item => {
    const itemCard = document.createElement('div');
    itemCard.className = `item-card ${isEquipped ? 'equipped' : 'unequipped'}`;
    itemCard.innerHTML = `
      <div class="item-info">
        <h4>${item.shopItem.name}</h4>
        <p class="item-description">${item.shopItem.description || '설명 없음'}</p>
        <span class="item-status">${isEquipped ? '장착중' : '미장착'}</span>
      </div>
    `;
    
    itemCard.addEventListener('click', () => {
      currentItem = item;
      displayJson(document.getElementById('inventory-item-display'), item);
      
      document.getElementById('equip-item-btn').style.display = isEquipped ? 'none' : 'inline-block';
      document.getElementById('unequip-item-btn').style.display = isEquipped ? 'inline-block' : 'none';
      document.getElementById('place-room-btn').style.display = isEquipped ? 'none' : 'inline-block';
      
      document.getElementById('inventory-item-detail').style.display = 'block';
    });
    
    grid.appendChild(itemCard);
  });
  
  container.appendChild(grid);
}

// === 방 꾸미기 기능 ===
function initRoom() {
  // 방 불러오기
  document.getElementById('load-room-btn').addEventListener('click', () => {
    if (!checkLogin()) return;
    loadUserRoom();
  });

  // 배치 가능한 아이템 불러오기
  document.getElementById('load-available-items-btn').addEventListener('click', () => {
    if (!checkLogin()) return;
    loadAvailableItems();
  });

  // 아이템 배치
  document.getElementById('place-item-btn').addEventListener('click', async () => {
    if (!currentItem || !checkLogin()) return;

    const x = Math.floor(Math.random() * 400);
    const y = Math.floor(Math.random() * 200);
    
    try {
      await apiFetch(`/api/room/users/${currentUserId}/items/place`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          inventoryItemId: currentItem.id,
          positionX: x,
          positionY: y
        })
      });
      alert('아이템을 방에 배치했습니다!');
      loadUserRoom();
      loadAvailableItems();
      document.getElementById('room-item-detail').style.display = 'none';
    } catch (error) {
      console.error('아이템 배치 실패:', error);
    }
  });

  // 아이템 제거
  document.getElementById('remove-item-btn').addEventListener('click', async () => {
    if (!currentItem || !checkLogin()) return;
    if (!confirm('이 아이템을 방에서 제거하시겠습니까?')) return;
    
    try {
      await apiFetch(`/api/room/users/${currentUserId}/items/${currentItem.inventoryItem.id}`, {
        method: 'DELETE'
      });
      alert('아이템을 방에서 제거했습니다!');
      loadUserRoom();
      loadAvailableItems();
      document.getElementById('room-item-detail').style.display = 'none';
    } catch (error) {
      console.error('아이템 제거 실패:', error);
    }
  });
}

async function loadUserRoom() {
  try {
    const room = await apiFetch(`/api/room/users/${currentUserId}`);
    currentRoom = room;
    displayRoom(room);
  } catch (error) {
    console.error('방 정보 로드 실패:', error);
    document.getElementById('room-display-area').innerHTML = '<div class="error-message">방 정보를 불러올 수 없습니다.</div>';
  }
}

async function loadAvailableItems() {
  try {
    const unequippedItems = await apiFetch(`/api/inventory/users/${currentUserId}/unequipped`);
    displayAvailableItems(unequippedItems);
  } catch (error) {
    console.error('배치 가능한 아이템 로드 실패:', error);
  }
}

function displayRoom(room) {
  const roomDisplayArea = document.getElementById('room-display-area');
  roomDisplayArea.innerHTML = '';
  roomDisplayArea.className = 'room-area';
  
  if (room && room.placedItems && room.placedItems.length > 0) {
    room.placedItems.forEach(placedItem => {
      const itemElement = document.createElement('div');
      itemElement.className = 'placed-item';
      itemElement.style.left = placedItem.positionX + 'px';
      itemElement.style.top = placedItem.positionY + 'px';
      itemElement.textContent = placedItem.inventoryItem.shopItem.name;
      itemElement.title = `${placedItem.inventoryItem.shopItem.name} (클릭하면 제거)`;
      
      itemElement.addEventListener('click', () => {
        currentItem = placedItem;
        displayJson(document.getElementById('room-item-display'), placedItem);
        document.getElementById('place-item-btn').style.display = 'none';
        document.getElementById('remove-item-btn').style.display = 'inline-block';
        document.getElementById('room-item-detail').style.display = 'block';
      });
      
      roomDisplayArea.appendChild(itemElement);
    });
  } else {
    const emptyMessage = document.createElement('div');
    emptyMessage.className = 'empty-room-message';
    emptyMessage.textContent = '배치된 아이템이 없습니다. 아래에서 아이템을 선택하여 방에 배치해보세요!';
    roomDisplayArea.appendChild(emptyMessage);
  }
}

function displayAvailableItems(items) {
  const availableItemsGrid = document.getElementById('available-items-grid');
  availableItemsGrid.innerHTML = `<h4>배치 가능한 아이템 (${items.length}개)</h4>`;
  
  if (items.length === 0) {
    availableItemsGrid.innerHTML += '<p>배치 가능한 아이템이 없습니다. <a href="#" onclick="showPage(\'shop\')">상점</a>에서 아이템을 구매해보세요!</p>';
    return;
  }

  const grid = document.createElement('div');
  grid.className = 'items-grid';
  
  items.forEach(item => {
    const itemCard = document.createElement('div');
    itemCard.className = 'item-card available';
    itemCard.innerHTML = `
      <div class="item-info">
        <h4>${item.shopItem.name}</h4>
        <p class="item-description">${item.shopItem.description || '설명 없음'}</p>
        <span class="item-status">배치 가능</span>
      </div>
    `;
    
    itemCard.addEventListener('click', () => {
      currentItem = item;
      displayJson(document.getElementById('room-item-display'), item);
      document.getElementById('place-item-btn').style.display = 'inline-block';
      document.getElementById('remove-item-btn').style.display = 'none';
      document.getElementById('room-item-detail').style.display = 'block';
    });
    
    grid.appendChild(itemCard);
  });
  
  availableItemsGrid.appendChild(grid);
}

// 초기화
document.addEventListener('DOMContentLoaded', () => {
  // 초기 상태 설정
  updateUserStatus();
  updateNavigation();
  
  if (currentUserId) {
    document.getElementById('todo-userId').value = currentUserId;
  }

  // 각 기능 초기화
  initUserManagement();
  initTodoManagement();
  initShop();
  initInventory();
  initRoom();
  
  // 기본 페이지 표시
  showPage('home');
});