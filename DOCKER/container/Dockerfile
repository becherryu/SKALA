FROM python:3.11-alpine

# 빌드
# docker build -f DOCKER/container/Dockerfile -t container-linux:1.0 DOCKER/container
#
# 실행
# docker run -d --name container-linux -p 8000:8000 -v /Users/ahyeon/Desktop/A3024/SKALA/DOCKER/container/mydata:/mydata container-linux:1.0
#
# 확인하기
# docker exec -it container-linux /bin/sh
# cd /mydata
# python webserver.py

WORKDIR /mycode

RUN apk add --no-cache bash curl gcc musl-dev linux-headers jq

RUN pip install --no-cache-dir fastapi uvicorn psutil python-multipart

COPY mycode.py ./mycode.py

CMD [ "sh", "-c", "echo 'start server' && sleep infinity" ]
