# Assignment 04 - Jenkins Pipeline for Java

Dự án Java 21 + Maven mẫu để thực hành Jenkins **Declarative Pipeline** trên Ubuntu.

## Cấu trúc

```text
Assignment04_Jenkins_Java/
├── Jenkinsfile
├── pom.xml
├── README.md
├── .gitignore
└── src/
    ├── main/java/edu/assignment04/
    │   ├── App.java
    │   └── GreetingService.java
    └── test/java/edu/assignment04/
        └── GreetingServiceTest.java
```

## Yêu cầu

- Ubuntu + OpenJDK 21 (`javac`, `java`)
- Maven, Git
- Jenkins LTS (Java 21) với các plugin: Pipeline, Git, JUnit

## Chạy thử trước khi đưa lên Jenkins

```bash
mvn -B clean verify
java -jar target/assignment04-java-demo-1.0.0.jar
```

Mở terminal khác:

```bash
curl http://localhost:9090/health
curl 'http://localhost:9090/hello?name=Jenkins'
```

Kỳ vọng: `OK` và `Xin chao, Jenkins!`. Dừng server bằng `Ctrl+C`.

## Push GitHub (repository tạo mới, rỗng)

```bash
git init -b main
git add .
git commit -m "Add Java Maven Jenkins pipeline demo"
git remote add origin https://github.com/<USERNAME>/<REPOSITORY>.git
git push -u origin main
```

Nếu Git hỏi danh tính, dùng `git config --global user.name "Ten cua ban"` và
`git config --global user.email "email@example.com"` trước khi commit.
GitHub HTTPS push yêu cầu đăng nhập an toàn qua credential manager hoặc token, không dùng mật khẩu tài khoản.

## Jenkins: Tạo Pipeline from SCM

1. `New Item` > đặt tên `Assignment04-Java-Pipeline` > `Pipeline` > `OK`.
2. Trong `Pipeline`: chọn `Definition: Pipeline script from SCM`.
3. `SCM: Git` > `Repository URL`: URL GitHub của bạn.
4. `Credentials`: `- none -` nếu public; chọn credential nếu private.
5. `Branches to build`: `*/main`; `Script Path`: `Jenkinsfile`.
6. `Save` > `Build Now` > xem `Console Output` và stage view.

Các stage: Checkout -> Check Environment -> Compile -> Unit Test -> Package -> Archive Artifact -> Publish to Local Staging.

Stage `Publish to Local Staging` **chỉ copy JAR tới ổ đĩa Jenkins**, không khởi động dịch vụ (không nên gọi đây là tự động triển khai web hoàn chỉnh).

## Chạy artifact mà Jenkins đã publish

Mở terminal Ubuntu riêng (sau build thành công):

```bash
sudo -u jenkins java -jar /var/lib/jenkins/assignment04-release/app.jar
```

Trong terminal thứ hai:

```bash
curl http://localhost:9090/health
curl 'http://localhost:9090/hello?name=Student'
```

Trên Ubuntu, đường dẫn mặc định JENKINS_HOME thường là `/var/lib/jenkins`.
Server demo lắng nghe `0.0.0.0:9090`; chỉ dùng trong mạng lab tin cậy, không mở public ra internet.

## Minh chứng cần chụp cho báo cáo

1. `java -version`, `mvn -version` và `git --version`.
2. `systemctl status jenkins` hiển thị `active (running)`.
3. Giao diện Jenkins Dashboard.
4. GitHub chứa `Jenkinsfile`, `pom.xml`, thư mục `src`.
5. Cấu hình `Pipeline script from SCM`.
6. Stage View / Pipeline Overview: tất cả stage màu xanh.
7. `Console Output`: BUILD SUCCESS.
8. Kết quả kiểm thử JUnit trên Jenkins (3 test).
9. Build Artifacts có file `.jar`.
10. `curl /health`, `curl /hello` hoạt động.

## Kiểm thử fail có chủ đích (không bắt buộc)

Đổi `assertEquals("Xin chao, Jenkins!", ...)` thành chuỗi sai trong `GreetingServiceTest.java`, commit/push, build lại để quan sát Unit Test FAIL; sau đó khôi phục, push và build xanh trở lại.

## Lưu ý

- Jenkinsfile dùng `sh` vì Jenkins chạy trên Linux. Với Windows agent cần `bat` hoặc `powershell`.
- Cần Internet ở lần build Maven đầu tiên để tải dependencies/plugins từ Maven Central.
- `checkout scm` đòi hỏi Job cấu hình **Pipeline script from SCM**, không đơn thuần copy Jenkinsfile vào `Pipeline script`.
- Tuyệt đối không chèn token/mật khẩu vào Jenkinsfile hay GitHub repo.
