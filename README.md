# Bài 10: The broken pipeline

## 1. Mục tiêu

Bài này mô phỏng một dự án Maven có pipeline CI bị lỗi. Nhiệm vụ là đọc log GitHub Actions để xác định nguyên nhân, sửa từng lỗi theo thứ tự và xác nhận pipeline xanh sau mỗi lần sửa.

Dự án sau khi sửa gồm:

- Source chính: `src/main/java/com/lab/ShippingCalculator.java`
- Unit test: `src/test/java/com/lab/ShippingCalculatorTest.java`
- Cấu hình Maven: `pom.xml`
- Workflow CI: `.github/workflows/ci.yml`
- Tài liệu lỗi ban đầu: `docs/broken-version-reference.md`

## 2. Lỗi 1: Workflow thiếu checkout source code

### File lỗi

```text
.github/workflows/ci.yml
```

### Nguyên nhân

GitHub Actions runner là một máy ảo mới, không tự có source code của repository. Nếu thiếu `actions/checkout`, lệnh Maven sẽ chạy trong thư mục không có `pom.xml`.

### Log liên quan

```text
[ERROR] The goal you specified requires a project to execute but there is no POM in this directory
```

### Cách sửa

Thêm bước checkout:

```yaml
- name: Checkout source code
  uses: actions/checkout@v4
```

## 3. Lỗi 2: Version Logback không tồn tại

### File lỗi

```text
pom.xml
```

### Nguyên nhân

Dependency ban đầu dùng:

```xml
<version>9.9.9</version>
```

Phiên bản này không tồn tại trên Maven Central nên Maven không thể tải dependency.

### Log liên quan

```text
Could not find artifact ch.qos.logback:logback-classic:jar:9.9.9 in central
```

### Cách sửa

Đổi sang version hợp lệ:

```xml
<version>1.4.11</version>
```

## 4. Lỗi 3: Maven Surefire Plugin quá cũ

### File lỗi

```text
pom.xml
```

### Nguyên nhân

Project dùng JUnit Jupiter 5.9.2 nhưng `maven-surefire-plugin` ban đầu là `2.12.4`, quá cũ để chạy JUnit 5 ổn định.

### Log liên quan

```text
Tests run: 0, Failures: 0, Errors: 0, Skipped: 0
```

Đây là lỗi nguy hiểm vì pipeline có thể xanh nhưng thực tế không chạy test.

### Cách sửa

Nâng Surefire lên:

```xml
<version>3.1.2</version>
```

## 5. Lỗi thứ 4 tự tạo

Sau khi pipeline xanh, cố tình tạo lỗi thứ 4 không trùng với 3 lỗi ban đầu.

### Lỗi tự tạo

Sửa test express từ:

```java
assertEquals(45000.0, calc.calculate(5, "EXPRESS"));
```

thành:

```java
assertEquals(40000.0, calc.calculate(5, "EXPRESS"));
```

### Log pipeline đỏ

```text
expected: <40000.0> but was: <45000.0>
Tests run: 4, Failures: 1, Errors: 0, Skipped: 0
```

### Nguyên nhân

Công thức tính EXPRESS là:

```text
weight * 5000 + 20000
```

Với `weight = 5`:

```text
5 * 5000 + 20000 = 45000
```

Vì vậy expected đúng phải là `45000.0`.

## 6. Cách chạy local

Chạy test và package:

```bash
mvn clean package
```

Kết quả mong đợi:

```text
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## 7. Cách đẩy lên GitHub

```bash
git init
git add .
git commit -m "Initial fixed shipping pipeline"
git branch -M main
git remote add origin <URL_REPOSITORY_CUA_BAN>
git push -u origin main
```

Sau đó mở tab **Actions** trên GitHub để xem pipeline.

## 8. Kết luận

Các lỗi được sửa theo đúng quy trình đọc log trước, sửa sau:

| Lỗi | File | Nguyên nhân | Cách sửa |
|---|---|---|---|
| Thiếu checkout | `.github/workflows/ci.yml` | Runner không có source code | Thêm `actions/checkout@v4` |
| Version Logback sai | `pom.xml` | Maven không tìm thấy dependency | Đổi sang `1.4.11` |
| Surefire quá cũ | `pom.xml` | Không chạy đúng JUnit Jupiter | Nâng lên `3.1.2` |
| Lỗi test tự tạo | `ShippingCalculatorTest.java` | Expected value sai | Sửa expected về `45000.0` |
