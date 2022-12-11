# TCP 신뢰 전송과 흐름 제어

## TCP 세그먼트 전송 규칙



### 누적 수신확인(Cumulative Acknowledgement)
* 누적적으로 완전하게 수신된 바이트 스트림 번호 확인
* 중복 ACK 세그먼트 수신 가능

### 중복 ACK(Duplicate ACK)
* 이미 수신한 확인번호(Acknowledgement Number)를 가진 ACK


### 중복 ACK 수신

* 순서가 바뀐 세그먼트 도착
* 중간 세그먼트 손실

### 단일 타이머(Single Timer)
* 누적 수신 확인 되지 않은 가장 오래된 세그먼트에 대한 재전송 타이머 유지
