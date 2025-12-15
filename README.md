# Spring WebFlux AWS S3 v2

A reactive file storage service built with Spring WebFlux and AWS SDK v2 for Java. This application provides REST APIs
for uploading, downloading, listing, and deleting files from Amazon S3 using reactive streams.

## Features

- **Reactive file upload** - Multipart upload support with streaming for large files
- **Reactive file download** - Stream files as byte arrays
- **List objects** - Paginated listing of S3 bucket contents
- **Delete objects** - Remove files from S3
- **Swagger UI** - Interactive API documentation
- **Custom endpoint support** - Works with AWS S3, LocalStack, MinIO, or other S3-compatible services

## Tech Stack

- **Java 25**
- **Spring Boot 4.0.0**
- **Spring WebFlux** - Reactive web framework
- **AWS SDK v2 (2.40.x)** - S3 async client with Netty NIO
- **SpringDoc OpenAPI 3.0** - Swagger UI for API documentation
- **Lombok** - Reduce boilerplate code
- **Apache Commons Lang 3** - Utility functions

## Prerequisites

- Java 25 or higher
- Maven 3.9+
- AWS Account with S3 access (or LocalStack/MinIO for local development)

## Configuration

Set the following environment variables before running the application:

```bash
export AWS_ACCESS_KEY=your-access-key
export AWS_SECRET_KEY=your-secret-key
export AWS_REGION=ap-southeast-1                    # Optional, defaults to ap-southeast-1
export AWS_S3_BUCKET_NAME=your-bucket-name          # Optional, defaults to 'your-bucket-name'
export AWS_S3_ENDPOINT=http://localhost:4566        # Optional, for LocalStack/MinIO
```

Or configure in `src/main/resources/application.yml`:

```yaml
aws:
  access-key: ${AWS_ACCESS_KEY}
  secret-key: ${AWS_SECRET_KEY}
  region: ${AWS_REGION:ap-southeast-1}
  s3-bucket-name: ${AWS_S3_BUCKET_NAME:your-bucket-name}
  multipart-min-part-size: 5242880  # 5MB
  endpoint: ${AWS_S3_ENDPOINT:}     # Leave empty for AWS S3, set for LocalStack/MinIO
```

## Building and Running

### Build the project

```bash
./mvnw clean package
```

### Run the application

```bash
./mvnw spring-boot:run
```

Or run the JAR directly:

```bash
java -jar target/webflux-aws-s3-v2-0.0.1-SNAPSHOT.jar
```

### Run tests

```bash
./mvnw test
```

## API Endpoints

The application exposes the following REST endpoints:

| Method   | Endpoint              | Description                    |
|----------|-----------------------|--------------------------------|
| `POST`   | `/object/upload`      | Upload a file to S3            |
| `GET`    | `/object/{fileKey}`   | Download a file by key         |
| `GET`    | `/object`             | List all objects in the bucket |
| `DELETE` | `/object/{objectKey}` | Delete an object by key        |

### Swagger UI

Access the interactive API documentation at:

- Swagger UI: http://localhost:8080/swagger-ui
- OpenAPI JSON: http://localhost:8080/v3/api-doc

## Usage Examples

### Upload a file

```bash
curl -X POST http://localhost:8080/object/upload \
  -F "file-data=@/path/to/your/file.pdf"
```

### Download a file

```bash
curl -X GET http://localhost:8080/object/your-file.pdf -o downloaded-file.pdf
```

### List all objects

```bash
curl -X GET http://localhost:8080/object
```

### Delete a file

```bash
curl -X DELETE http://localhost:8080/object/your-file.pdf
```

## Local Development with LocalStack

You can use LocalStack for local S3 development:

1. Start LocalStack:

```bash
docker run -d -p 4566:4566 localstack/localstack
```

2. Create a bucket:

```bash
aws --endpoint-url=http://localhost:4566 s3 mb s3://test-bucket
```

3. Set environment variables:

```bash
export AWS_ACCESS_KEY=test
export AWS_SECRET_KEY=test
export AWS_S3_BUCKET_NAME=test-bucket
export AWS_S3_ENDPOINT=http://localhost:4566
```

4. Run the application:

```bash
./mvnw spring-boot:run
```

## Project Structure

```
src/main/java/id/my/hendisantika/webfluxawss3v2/
├── SpringWebfluxAwsS3V2Application.java  # Main application class
├── common/
│   ├── AwsSdkUtil.java                   # AWS SDK utilities
│   └── FileUtils.java                    # File processing utilities
├── config/
│   ├── AwsProperties.java                # AWS configuration properties
│   ├── AwsS3Config.java                  # S3 client configuration
│   ├── OpenAPIConfig.java                # Swagger/OpenAPI configuration
│   └── WebConfiguration.java             # Web configuration
├── controller/
│   └── AWSS3Controller.java              # REST API controller
├── domain/
│   ├── AWSS3Object.java                  # S3 object representation
│   ├── FileResponse.java                 # File upload response
│   ├── SuccessResponse.java              # API success response
│   └── UploadStatus.java                 # Multipart upload status
├── exception/
│   ├── FileValidatorException.java       # File validation exception
│   ├── GlobalExceptionHandler.java       # Global exception handler
│   └── UploadException.java              # Upload exception
└── service/
    └── AWSS3FileStorageService.java      # S3 file storage service
```

## License

This project is open source and available under the [MIT License](LICENSE).

## Author

**Hendi Santika**

- Email: hendisantika@gmail.com
- Telegram: @hendisantika34
- Website: https://s.id/hendisantika
