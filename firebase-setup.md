# Firebase Realtime Database 좋아요 카운터 설정

## 🎯 기능 개요

간단한 좋아요 카운터 기능으로 Firebase Realtime Database를 활용합니다:
- ❤️ 하트 버튼: 사랑/축하 표현
- 🙏 축복 버튼: 축복/기도 표현

## 🔒 Firebase 보안 규칙

Firebase Console > Realtime Database > Rules에서 다음 규칙을 설정하세요:

### 기본 보안 규칙 (권장)

```json
{
  "rules": {
    ".read": true,
    ".write": false,
    "likes": {
      ".read": true,
      "heart": {
        ".write": true,
        ".validate": "newData.isNumber() && newData.val() > data.val()"
      },
      "blessing": {
        ".write": true,
        ".validate": "newData.isNumber() && newData.val() > data.val()"
      }
    }
  }
}
```

### 고급 보안 규칙 (스팸 방지)

```json
{
  "rules": {
    ".read": true,
    ".write": false,
    "likes": {
      ".read": true,
      "heart": {
        ".write": "auth != null || root.child('settings/allowAnonymous').val() == true",
        ".validate": "newData.isNumber() && newData.val() > data.val() && newData.val() <= (data.val() + 1)"
      },
      "blessing": {
        ".write": "auth != null || root.child('settings/allowAnonymous').val() == true",
        ".validate": "newData.isNumber() && newData.val() > data.val() && newData.val() <= (data.val() + 1)"
      }
    },
    "settings": {
      ".read": true,
      ".write": "auth != null && auth.uid == 'ADMIN_UID'",
      "allowAnonymous": {
        ".validate": "newData.isBoolean()"
      }
    }
  }
}
```

## 🚀 배포 및 테스트

### 1. GitHub Actions 자동 배포

프로젝트는 main 브랜치에 push할 때 자동으로 GitHub Pages에 배포됩니다.

### 2. 로컬 테스트

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

### 3. Firebase 연결 테스트

브라우저 개발자 도구(F12)에서 다음 명령어로 테스트:

```javascript
// Firebase 함수 확인
console.log('Firebase functions:', {
  incrementLike: typeof window.incrementLike,
  getLikeCount: typeof window.getLikeCount
});

// 좋아요 카운트 테스트
await window.incrementLike('heart');
const heartCount = await window.getLikeCount('heart');
console.log('Heart count:', heartCount);

await window.incrementLike('blessing');
const blessingCount = await window.getLikeCount('blessing');
console.log('Blessing count:', blessingCount);
```

## 📊 데이터 구조

```
wedding-eb136-default-rtdb/
├── likes/
│   ├── heart: 42        // 하트 좋아요 카운트
│   └── blessing: 28     // 축복 좋아요 카운트
└── settings/
    └── allowAnonymous: true
```

## 🎨 UI 사용법

웹사이트 하단에 표시되는 좋아요 섹션:

1. **❤️ 하트 버튼**: 클릭하면 하트 애니메이션과 함께 카운트 증가
2. **🙏 축복 버튼**: 클릭하면 하트 애니메이션과 함께 카운트 증가
3. **실시간 카운트**: 페이지 로드시 현재 카운트를 Firebase에서 가져와 표시

## 🔧 커스터마이징

### 좋아요 타입 추가

`LikeSection.kt`에서 새로운 좋아요 타입을 추가할 수 있습니다:

```kotlin
Column(
    horizontalAlignment = Alignment.CenterHorizontally
) {
    FloatingIconEffect(
        onClick = {
            scope.launch {
                if (likeManager.incrementLike("celebration")) {
                    celebrationCount = likeManager.getLikeCount("celebration")
                }
            }
        }
    ) { }
    Text(
        text = "$celebrationCount 🎉",
        style = fontFamily.bodyLarge,
        fontSize = 14.sp,
        color = Color(0xFF574B40)
    )
}
```

### 애니메이션 커스터마이징

`FloatingIconEffect.kt`에서 애니메이션 속도, 크기, 방향 등을 조정할 수 있습니다:

```kotlin
// 애니메이션 지속 시간 (밀리초)
val animationDuration = 2500

// 떠오르는 높이
val floatHeight = -500f

// 좌우 흔들림 진폭
val waveAmplitude = random.nextInt(5, 20)
```

## 🛡️ 보안 권장사항

1. **도메인 제한**: Firebase Console > 프로젝트 설정에서 허용 도메인 설정
2. **사용량 모니터링**: Firebase Console > 사용량 탭에서 일일 사용량 확인
3. **스팸 방지**: 고급 보안 규칙 사용으로 1회 클릭당 1개씩만 증가 허용
4. **백업**: 정기적인 데이터베이스 백업 설정

## 🎯 운영 팁

- **초기 카운트 설정**: Firebase Console에서 수동으로 초기값 설정 가능
- **통계 확인**: Firebase Console > Realtime Database > 데이터 탭에서 실시간 카운트 확인
- **오류 모니터링**: 브라우저 콘솔에서 Firebase 관련 오류 확인

---

**🎉 이제 웨딩 페이지에서 방문자들이 사랑과 축복을 표현할 수 있습니다!**
