# ---------------------------------------------------------------
# [파이썬 1일차 실습 4 - .env + logging 활용 스크립트 환경 구성]
#
# 작성자(author): 장아현
# 작성일자(date): 2026.01.22
# 변경내역(description): python-dotenv를 이용해 .env 값을 로딩하고,
#                      logging 모듈을 통해 홤경 구성 및 로그 출력하기
# ---------------------------------------------------------------
import os
import logging
from dotenv import load_dotenv
from logging.handlers import TimedRotatingFileHandler

# .env 파일 자동 로드
load_dotenv()

log_level = os.getenv("LOG_LEVEL")
app_name = os.getenv("APP_NAME")

print(f"현재 {app_name}의 로그 레벨은 {log_level} 입니다.\n")

# 로그 디렉토리 생성
log_dir = "Log"
os.makedirs(log_dir, exist_ok=True)

# 로거 생성
logger = logging.getLogger(app_name)
level = getattr(logging, log_level)
logger.setLevel(level)

# 1. 콘솔 핸들러
console_handler = logging.StreamHandler()
console_handler.setLevel(level)
console_format = logging.Formatter("%(asctime)s [%(levelname)s] %(message)s")
console_handler.setFormatter(console_format)

# 2. 파일 핸들러 (DEBUG 이상 모든 로그)
file_handler = TimedRotatingFileHandler(
    filename=f"{log_dir}/app.log",
    when="midnight",
    interval=1,
    backupCount=7,  # 최근 7일 보관
    encoding="utf-8",
)
file_handler.setLevel(level)
file_format = logging.Formatter("%(asctime)s [%(levelname)s] %(message)s")
file_handler.setFormatter(file_format)

# 3. 에러 핸들러 (ERROR 이상만 별도 저장)
error_handler = logging.FileHandler(f"{log_dir}/error.log")
error_handler.setLevel(logging.ERROR)
error_handler.setFormatter(file_format)

# 핸들러 등록
logger.addHandler(console_handler)
logger.addHandler(file_handler)
logger.addHandler(error_handler)

# 4. 로그 출력 테스트
logger.info(f"앱 {app_name} 실행 시작")
logger.debug("환경 변수 로딩 완료")
logger.error("ZeroDivisionError 예외 발생 시 출력")
