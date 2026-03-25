# Kubernetes Deployment 실습

## 실습 목표

Harbor Registry에 등록한 이미지를 `skala3-ai1` namespace에 Pod 1개 배포하기

---

## 전체 흐름

```
Harbor Registry (이미지 저장소)
        ↓ 이미지 pull
   Deployment
        ↓
      Pod (Running)
        ↑
    Service (ClusterIP)
```

---

## 핵심 개념

| 개념                | 설명                                                                    |
| ------------------- | ----------------------------------------------------------------------- |
| **Harbor Registry** | 컨테이너 이미지를 저장하는 창고. DevOps 과정에서 빌드한 이미지가 저장됨 |
| **Deployment**      | "이 이미지로 Pod를 N개 실행해줘"라는 명세서                             |
| **Pod**             | 컨테이너가 실제로 실행되는 최소 단위                                    |
| **Namespace**       | Kubernetes 내 리소스를 격리하는 공간                                    |
| **imagePullSecret** | Private Registry 접근을 위한 인증 정보                                  |
| **Service**         | Pod에 접근할 수 있는 네트워크 입구                                      |

---

## 파일 구성

- deployment.yaml
- service.yaml

---

## 배포 명령어

```bash
# 배포
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml

# 상태 확인
kubectl get pods -n skala3-ai1
kubectl get service -n skala3-ai1
kubectl get pod -n skala3-ai1 -o wide

# 에러 확인
kubectl describe pod <pod-name> -n skala3-ai1
kubectl logs <pod-name> -n skala3-ai1
```

---

## 실행 결과

```
NAME                                READY   STATUS    NODE
sk3024-maintq-fe-6cfd68df4d-jjmnc   1/1   Running   ip-172-31-12-175.ap-northeast-2.compute.internal
```

- **Pod IP**: `172.31.14.72` (클러스터 내부 접근만 가능)
- **실행 포트**: `8001`
- **실행 노드**: AWS EC2 워커 노드 (`ip-172-31-12-175`)

---

## 트러블슈팅

| 에러                   | 원인                                     | 해결                                  |
| ---------------------- | ---------------------------------------- | ------------------------------------- |
| `namespaces not found` | namespace 오타 (`skala3a-ai1`)           | `skala3-ai1`로 수정                   |
| `ErrImagePull`         | imagePullSecret 미설정                   | `harbor-pull-secret` 추가             |
| `ErrImagePull` 지속    | Registry URL 오류 (`harbor.skala25.com`) | `amdp-registry.skala-ai.com`으로 수정 |

---

## Service 타입 비교

| 타입           | 접근 범위                            |
| -------------- | ------------------------------------ |
| `ClusterIP`    | 클러스터 내부에서만 접근 (현재 설정) |
| `NodePort`     | 노드 IP + 포트로 외부 접근 가능      |
| `LoadBalancer` | AWS ELB 생성하여 외부 접근 가능      |
