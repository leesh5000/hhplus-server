# E-Commerce APP

# 목차

- [설명](#설명)
- [실행 방법](#실행-방법)
- [프로젝트 관련 문서](#프로젝트-관련-문서)
- [공헌하기](#공헌하기)

# 설명

이 프로젝트는 E-Commerce APP을 만들기 위한 프로젝트입니다.

# 프로젝트 요구사항

- Java 17
- Docker & Docker-Compose

# 실행 방법

1. Docker-Compose로 서버 실행을 위한 환경 실행

프로젝트 루트에서 다음 명령어를 실행합니다.
```bash
docker compose -f ./infra/docker-compose-db.yml up -d
```

2. (IntelliJ IDEA) 애플리케이션 실행

IntelliJ IDEA 에서 `ServerApplication` 클래스를 실행합니다.

# 프로젝트 관련 문서

- [요구사항 분석 문서](./docs/requirements_analysis.md)
- [클래스 다이어그램](./docs/class-diagram.md)
- [ERD](./docs/erd.md)````
- [API 문서 (OAS 3)](./docs/e-commerce-api.yml)
  - 해당 문서를 [Swagger Editor](https://editor.swagger.io/)에 붙여넣으면 시각적으로 확인할 수 있습니다.

# 공헌하기

아이디어 혹은 프로젝트 개선사항은 아래 링크를 통해 자유롭게 제출 바랍니다.

- [Discussions](https://github.com/leesh5000/hhplus-server/discussions)
- [Issues](https://github.com/leesh5000/hhplus-server/issues)

# 참고 자료

- [TestContainers](https://testcontainers.com/)

new 없애기
builder 로 테스트 픽스쳐 바꾸기
