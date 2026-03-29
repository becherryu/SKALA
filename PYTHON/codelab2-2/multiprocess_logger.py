# --------------------------------------------------------------------------------
# [파이썬 2일차 Codelab ② - 멀티프로세스 환경의 커스텀 JSON 로거 설계]
#
# 작성자(author): 장아현
# 작성일자(date): 2026.03.29
# 변경내역(description): 멀티프로세스 환경에서 Race Condition 없이 로그를 남기는
#                       커스텀 JSON 로거 설계 및 Race Condition 발생 비교 시연
# --------------------------------------------------------------------------------
# --------------------------------------------------------------------------------
#   실습 가이드
#   - 시나리오
#       멀티프로세스 환경에서 로그가 섞이지 않도록 하고,
#       각 로그에 어떤 프로세스가 작업을 수행했는지(ProcessID),
#       작업의 고유 ID(TaskID)가 포함되도록 커스텀 로거를 설계
#   핵심 점검
#   - Race Condition
#       여러 프로세스가 하나의 파일에 동시에 로그를 남길 때 발생하는 문제 이해
#   - JSON Logging
#       로그 분석 도구(ELK, Splunk 등)에서 파싱하기 쉽도록
#       로그 포맷을 JSON 형태로 표준화
# --------------------------------------------------------------------------------

from __future__ import annotations

import json
import logging
import multiprocessing as mp
import os
import random
import time
from datetime import datetime


# ---------------------------
# JSON 포맷터
# ---------------------------
class JsonFormatter(logging.Formatter):
    """로그를 JSON 형태로 직렬화하는 포맷터"""

    def format(self, record: logging.LogRecord) -> str:
        log_data = {
            "timestamp": datetime.fromtimestamp(record.created).isoformat(),
            "level": record.levelname,
            "process_id": record.process,
            "task_id": getattr(record, "task_id", "-"),
            "message": record.getMessage(),
        }
        return json.dumps(log_data, ensure_ascii=False)


# ---------------------------
# Race Condition 시연 (안전하지 않은 방식)
# ---------------------------
UNSAFE_LOG = "logs/unsafe.log"
SAFE_LOG = "logs/safe.log"


def unsafe_worker(task_id: int) -> None:
    """락 없이 파일에 직접 쓰기 → 시작/완료 사이에 다른 프로세스가 끼어들어 Race Condition 발생"""
    pid = os.getpid()

    # 시작 로그 쓰고 파일 닫기
    with open(UNSAFE_LOG, "a", encoding="utf-8") as f:
        f.write(f"[PID:{pid}] Task-{task_id:03d} 시작\n")

    # 파일이 열려있지 않은 사이에 다른 프로세스가 끼어들 수 있음
    time.sleep(random.uniform(0.001, 0.005))

    # 완료 로그 쓰기
    with open(UNSAFE_LOG, "a", encoding="utf-8") as f:
        f.write(f"[PID:{pid}] Task-{task_id:03d} 완료\n")


def run_unsafe(num_tasks: int = 20) -> None:
    open(UNSAFE_LOG, "w").close()  # 파일 초기화

    with mp.Pool(mp.cpu_count()) as pool:
        pool.map(unsafe_worker, range(num_tasks))

    # 실제 로그가 얼마나 뒤섞였는지 출력
    with open(UNSAFE_LOG, encoding="utf-8") as f:
        lines = f.readlines()

    print("  [unsafe.log 내용 일부]")
    for line in lines[:10]:
        print("  " + line.strip())

    broken = sum(
        1 for i in range(len(lines) - 1)
        if lines[i].split("]")[0] != lines[i + 1].split("]")[0]
        and "시작" in lines[i] and "시작" in lines[i + 1]
    )
    print(f"\n  → 뒤섞인 구간(시작 사이에 다른 PID 끼어든 횟수): {broken}건")


# ---------------------------
# 안전한 로깅 (Lock 방식)
# ---------------------------
formatter = JsonFormatter()


def make_log_line(task_id: int, message: str, timestamp: str) -> str:
    record = logging.LogRecord(
        name="safe_logger",
        level=logging.INFO,
        pathname="",
        lineno=0,
        msg=message,
        args=(),
        exc_info=None,
    )
    record.task_id = f"Task-{task_id:03d}"
    record.created = datetime.fromisoformat(timestamp).timestamp()
    return formatter.format(record)


def safe_worker(task_id: int, lock: mp.Lock) -> None:
    """실제 작업은 락 밖에서 수행, 로그 기록만 락 안에서 한꺼번에 → 시작/완료 쌍이 항상 붙어서 기록됨"""

    # 1. 락 없이 실제 작업 수행 (타임스탬프만 기록)
    start_ts = datetime.now().isoformat()
    time.sleep(random.uniform(0.001, 0.01))
    end_ts = datetime.now().isoformat()

    # 2. 락 안에서 시작/완료를 한꺼번에 기록
    with lock:
        start_line = make_log_line(task_id, "작업 시작", start_ts)
        end_line = make_log_line(task_id, "작업 완료", end_ts)
        with open(SAFE_LOG, "a", encoding="utf-8") as f:
            f.write(start_line + "\n")
            f.write(end_line + "\n")
        print(start_line)
        print(end_line)


def run_safe(num_tasks: int = 20) -> None:
    open(SAFE_LOG, "w").close()  # 파일 초기화

    lock = mp.Manager().Lock()

    with mp.Pool(mp.cpu_count()) as pool:
        pool.starmap(safe_worker, [(i, lock) for i in range(num_tasks)])

    print(f"\n  → safe.log 저장 완료 ({num_tasks}건)")


# ---------------------------
# 메인
# ---------------------------
if __name__ == "__main__":
    os.makedirs("logs", exist_ok=True)

    # --- Race Condition 시연 ---
    print("=" * 50)
    print("[1] Race Condition 시연 (unsafe.log)")
    print("=" * 50)
    run_unsafe(num_tasks=20)

    # --- 안전한 JSON 로깅 ---
    print("\n" + "=" * 50)
    print("[2] 안전한 JSON 로깅 with Lock (safe.log)")
    print("=" * 50)
    run_safe(num_tasks=20)
