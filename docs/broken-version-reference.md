# Broken version reference

File này ghi lại các lỗi ban đầu của đề bài để đối chiếu khi làm báo cáo.

## 1. `.github/workflows/ci.yml` thiếu checkout

Workflow ban đầu không có bước:

```yaml
- name: Checkout source code
  uses: actions/checkout@v4
```

Log thường gặp:

```text
[ERROR] The goal you specified requires a project to execute but there is no POM in this directory
```

## 2. `pom.xml` dùng Logback version không tồn tại

Dependency lỗi:

```xml
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>9.9.9</version>
</dependency>
```

Log thường gặp:

```text
Could not find artifact ch.qos.logback:logback-classic:jar:9.9.9 in central
```

## 3. `pom.xml` dùng Surefire quá cũ

Plugin lỗi:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>2.12.4</version>
</plugin>
```

Với JUnit Jupiter, version này quá cũ và có thể không chạy test đúng.

Log thường gặp:

```text
Tests run: 0, Failures: 0, Errors: 0, Skipped: 0
```
