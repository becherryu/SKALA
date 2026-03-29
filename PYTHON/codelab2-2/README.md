# [Codelab 2] 멀티프로세스 환경의 커스텀 JSON 로거 설계

## 요구사항

- 가이드
  - 멀티프로세스 환경에서 로그가 섞이지 않도록 하고, 각 로그에 어떤 프로세스가 작업을 수행했는지(ProcessID), 작업의 고유 ID(TaskID)가 포함되도록 커스텀 로거를 설계
  - Race Condition이 발생하는 안전하지 않은 방식과 Lock 기반의 안전한 방식을 비교
  - 로그를 JSON 형태로 표준화하여 분석 도구에서 파싱 가능하도록 구성
- 라이브러리
  - `import logging`
  - `import multiprocessing as mp`
  - `import json`

## 핵심 개념

### Race Condition (경쟁 상태)

여러 프로세스가 동시에 하나의 파일에 로그를 쓸 때 발생하는 문제다.

락이 없으면 아래처럼 로그가 뒤섞인다.

```
# 기대하는 순서
[PID:111] Task-000 시작
[PID:111] Task-000 완료   ← 000의 시작/완료가 붙어있어야 함
[PID:222] Task-001 시작
[PID:222] Task-001 완료

# 실제로 발생하는 순서 (Race Condition)
[PID:111] Task-000 시작
[PID:222] Task-001 시작   ← 000이 완료되기 전에 001이 끼어듦
[PID:333] Task-002 시작   ← 심지어 002까지 끼어듦
[PID:111] Task-000 완료
[PID:222] Task-001 완료
[PID:333] Task-002 완료
```

이렇게 되면 어떤 프로세스가 언제 무슨 작업을 했는지 추적이 불가능해진다.

> **참고 - 시연을 위한 의도적 설계**
> macOS는 OS 레벨에서 작은 파일 쓰기를 원자적으로 처리하기 때문에 자연스럽게는 race condition이 잘 보이지 않는다.
> 이를 눈에 보이게 만들기 위해 `unsafe_worker`에서 "시작" 로그를 쓴 뒤 **파일을 닫고 sleep 후 다시 열어서** "완료" 로그를 쓰는 방식으로 구현했다.
> 파일이 닫혀있는 sleep 구간에 다른 프로세스가 끼어드는 상황을 강제로 만든 것이다.
> Linux 서버나 대용량 로그 환경에서는 이런 조작 없이도 자연스럽게 발생한다.

### Lock을 이용한 Race Condition 방지

`multiprocessing.Lock`을 사용하면 한 번에 하나의 프로세스만 파일에 접근할 수 있다.

```
프로세스 A가 락 획득 → 로그 기록 → 락 해제
                                      ↓
                        프로세스 B가 락 획득 → 로그 기록 → 락 해제
```

프로세스 B는 A가 락을 해제할 때까지 기다리기 때문에 로그가 섞이지 않는다.

Lock 설계 시 주의할 점은 **시작/완료 로그를 락 안에서 한꺼번에 써야 한다**는 것이다.
락을 두 번 따로 잡으면 그 사이에 다른 프로세스가 끼어들 수 있다.

### JSON Logging

로그를 구조화된 JSON 형태로 저장하면 ELK, Splunk 같은 로그 분석 도구에서 필드 단위로 파싱·검색이 가능하다.

```json
{"timestamp": "2026-03-29T17:42:58.769058", "level": "INFO", "process_id": 50720, "task_id": "Task-005", "message": "작업 시작"}
```

| 필드 | 설명 |
|------|------|
| `timestamp` | 로그 발생 시각 (ISO 8601) |
| `level` | 로그 레벨 (INFO, ERROR 등) |
| `process_id` | 작업을 수행한 프로세스 ID |
| `task_id` | 작업의 고유 식별자 |
| `message` | 로그 메시지 |

## 실행 결과

### [1] Race Condition 시연 (unsafe.log)

```
[unsafe.log 내용 일부]
[PID:50700] Task-000 시작
[PID:50702] Task-001 시작   ← 000이 완료되기 전에 끼어듦
[PID:50706] Task-002 시작   ← 002까지 연속으로 끼어듦
[PID:50702] Task-001 완료
[PID:50700] Task-000 완료
[PID:50702] Task-003 시작
...

→ 뒤섞인 구간(시작 사이에 다른 PID 끼어든 횟수): 9건
```

### [2] 안전한 JSON 로깅 with Lock (safe.log)

```
{"timestamp": "...", "level": "INFO", "process_id": 50720, "task_id": "Task-005", "message": "작업 시작"}
{"timestamp": "...", "level": "INFO", "process_id": 50720, "task_id": "Task-005", "message": "작업 완료"}
{"timestamp": "...", "level": "INFO", "process_id": 50719, "task_id": "Task-004", "message": "작업 시작"}
{"timestamp": "...", "level": "INFO", "process_id": 50719, "task_id": "Task-004", "message": "작업 완료"}
...

→ safe.log 저장 완료 (20건)
```

- Lock 없이 실행하면 Task의 시작·완료 사이에 다른 프로세스가 끼어들어 로그 뒤섞임 발생
- Lock을 걸면 각 Task의 시작·완료가 항상 붙어서 기록되어 추적 가능

## 파일 구성

```
.
├── multiprocess_logger.py   # 메인: Race Condition 시연 + Lock 기반 JSON 로거
└── logs/
    ├── unsafe.log           # Race Condition으로 뒤섞인 로그
    └── safe.log             # Lock 기반 안전한 JSON 로그
```
