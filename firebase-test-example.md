# Firebase 테스트 예시

## 브라우저 개발자 도구에서 Firebase 테스트

웹사이트가 배포된 후 브라우저 개발자 도구(F12)를 열고 Console 탭에서 다음 명령어들을 실행해보세요:

### 1. Firebase 연결 확인

```javascript
// Firebase 함수들이 정상적으로 로드되었는지 확인
console.log('Firebase functions:', {
  writeToFirebase: typeof window.writeToFirebase,
  readFromFirebase: typeof window.readFromFirebase,
  pushToFirebase: typeof window.pushToFirebase,
  listenToFirebase: typeof window.listenToFirebase
});
```

### 2. 간단한 데이터 쓰기/읽기 테스트

```javascript
// 테스트 데이터 쓰기
await window.writeToFirebase('test', {
  message: 'Hello Firebase!',
  timestamp: Date.now()
});

// 데이터 읽기
const data = await window.readFromFirebase('test');
console.log('읽은 데이터:', data);
```

### 3. 방명록 메시지 추가 테스트

```javascript
// 방명록에 메시지 추가
const messageKey = await window.pushToFirebase('guestbook', {
  name: '테스트 사용자',
  message: '결혼을 축하드립니다! 🎉',
  timestamp: Date.now()
});
console.log('생성된 메시지 키:', messageKey);
```

### 4. 방명록 데이터 읽기

```javascript
// 모든 방명록 메시지 읽기
const guestbook = await window.readFromFirebase('guestbook');
console.log('방명록 데이터:', guestbook);

// 데이터를 배열로 변환
if (guestbook) {
  const messages = Object.keys(guestbook).map(key => ({
    id: key,
    ...guestbook[key]
  })).sort((a, b) => b.timestamp - a.timestamp);
  console.log('정렬된 메시지:', messages);
}
```

### 5. 실시간 데이터 수신 테스트

```javascript
// 방명록 실시간 업데이트 수신
window.listenToFirebase('guestbook', (data) => {
  console.log('실시간 업데이트:', data);
  if (data) {
    const messages = Object.keys(data).map(key => ({
      id: key,
      ...data[key]
    })).sort((a, b) => b.timestamp - a.timestamp);
    console.log('업데이트된 메시지 목록:', messages);
  }
});
```

## Kotlin 코드에서 Firebase 사용 예시

### 방명록 메시지 추가

```kotlin
// 사용자 입력을 받는 함수에서
suspend fun addGuestbookMessage(name: String, message: String) {
    val success = guestbookManager.addMessage(name, message)
    if (success) {
        println("메시지가 성공적으로 추가되었습니다!")
    } else {
        println("메시지 추가에 실패했습니다.")
    }
}
```

### 방명록 메시지 읽기

```kotlin
// 컴포저블에서 메시지 로드
LaunchedEffect(Unit) {
    val messages = guestbookManager.getMessages()
    // UI 업데이트
}
```

### 실시간 데이터 수신

```kotlin
// 실시간 메시지 수신 설정
LaunchedEffect(Unit) {
    guestbookManager.listenToMessages { messages ->
        // 새로운 메시지가 도착했을 때 UI 업데이트
        guestbookEntries = messages
    }
}
```

## 오류 해결

### Firebase 함수가 undefined인 경우

```javascript
// Firebase SDK가 로드되지 않았을 때
if (typeof window.writeToFirebase === 'undefined') {
  console.error('Firebase SDK가 로드되지 않았습니다. index.html을 확인하세요.');
}
```

### CORS 오류가 발생하는 경우

Firebase에서는 일반적으로 CORS 문제가 없지만, 만약 발생한다면:

1. Firebase Console > 프로젝트 설정 > 일반
2. 웹 앱 섹션에서 도메인 확인
3. Authorized domains에 배포 도메인 추가

### 보안 규칙 오류

```javascript
// 권한 오류가 발생할 때
// Firebase Console > Realtime Database > Rules에서 보안 규칙 확인
{
  "rules": {
    ".read": true,
    ".write": true  // 테스트용으로만 사용, 실제 운영에서는 보안 규칙 적용 필요
  }
}
```

## 성능 최적화 팁

1. **데이터 구조 최적화**: 깊은 중첩을 피하고 플랫한 구조 사용
2. **인덱싱**: 자주 쿼리하는 필드에 인덱스 설정
3. **데이터 제한**: 한 번에 가져오는 데이터 양 제한
4. **오프라인 지원**: Firebase의 오프라인 기능 활용

```javascript
// 제한된 수의 메시지만 가져오기 (최근 20개)
// 실제 구현시 Firebase의 limitToLast() 기능 사용 고려
```
